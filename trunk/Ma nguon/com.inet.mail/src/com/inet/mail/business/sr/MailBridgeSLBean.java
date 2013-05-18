/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *****************************************************************/
package com.inet.mail.business.sr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.jboss.annotation.JndiInject;
import org.jboss.ejb3.mdb.ProducerManager;
import org.jboss.ejb3.mdb.ProducerObject;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.CommonService;
import com.inet.base.service.FileService;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.mail.IMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.PoolObject;
import com.inet.mail.PoolObjectManager;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.conf.PersonMailConfig.ConfigDTO;
import com.inet.mail.core.CacheEntry;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.UIDStore;
import com.inet.mail.core.cache.util.DataEntryCacheHelper;
import com.inet.mail.core.cache.util.UIDStoreCacheHelper;
import com.inet.mail.data.Address;
import com.inet.mail.data.FolderCountDTO;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.data.MailType;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;
import com.inet.mail.filter.MailFilterEngine;
import com.inet.mail.message.Messages;
import com.inet.mail.parser.AbstractMessageFactory;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.FollowUp;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailError;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;
import com.inet.mail.service.MailSynchronizeSL;
import com.inet.mail.sun.SunMailFactory;
import com.inet.mail.supports.MailFactoryPoolObjectSupport;
import com.inet.mail.supports.PoolObjectManagerSupport;
import com.inet.mail.util.MailService;

/**
 * MailBridgeSLBean.
 *
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class MailBridgeSLBean extends BaseMailBusinessBean<MailHeaderDTO>
		implements MailBridgeRemote {
	//~ Static fields =========================================================
	/* pool manager key. */
	private static final String POOL_MANAGER_KEY = IMailFactory.class.getName() ;
	//~ Instance fields =======================================================
	/**
	 * mail configuration bean.
	 */
	@EJB(name = "MailConfigureSLBean")
	private MailAcctConfigInfoSL mailCfgBean;

	/**
	 * mail header bean.
	 */
	@EJB(name = "MailHeaderSLBean")
	private MailHeaderSL mailBean;

	/**
	 * mail folder bean.
	 */
	@EJB(name = "MailFolderSLBean")
	private MailFolderSL folderBean;

	/**
	 * mail follow up bean.
	 */
	@EJB(name = "FollowUpSLBean")
	private FollowUpSL followUpBean;
	/**
	 * producer manager bean.
	 */
	private ProducerManager localManager;

	/**
	 * Mail synchronized bean.
	 */
	private MailSynchronizeSL synchonizeBean;
	
	/**
	 * Mail Error bean
	 */
	@EJB(name = "MailErrorSLBean")
	private MailErrorSL mailErrorSL;

	//~ Methods ===============================================================
	/**
	 * Initialization the bean.
	 */
	@JndiInject(jndiName = "com.inet.mail.service.MailSynchronizeSL")
	public void setLocal(MailSynchronizeSL synchonizeBean) {
		this.synchonizeBean = synchonizeBean;
		this.localManager = ((ProducerObject) synchonizeBean).getProducerManager();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#fetch(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public MailHeader fetch(String account) throws EJBException {
		try{
			// get user login into the system
			String userCode = getUserCode();

			// 1. get email config
			PersonMailConfig config = getMailConfig(userCode);

			// 2. fetch new email
			if (	config != null &&
					config.getFolders() != null &&
					config.getAccounts() != null &&
					config.getAccounts().containsKey(account)) {
				// and get the account configuration
				ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

				// initialize the factory
				PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account);

				// get factory.
				IMailFactory factory = factoryObject.getObject() ;

				// load the UID store.
				UIDStore store = getUIDStore(userCode, account) ;

				// hold the new message.
				List<IMessageComposer> composers = null ;

				try{
					// make connection
					if(!factory.isConnected()){
						factory.connect(
								MailProtocol.POP3,
								pop3Cfg.getSecurityMode(),
								pop3Cfg.getServerName(),
								pop3Cfg.getServerPort(),
								pop3Cfg.getAccountName(),
								pop3Cfg.getAccountPassword()) ;
					}

					// fetch email header
					 composers = factory.fetch(null, 1, store, false) ;
				} catch(MailParserException mpe){
				  if(StringService.hasLength(mpe.getUuid())){
                                    mailErrorSL.save(new MailError(mpe.getUuid(), userCode, account));
                                    // put to cache.
                                    UIDStoreCacheHelper.put(userCode, account, store) ;
				  }
				}finally{
					try{
						// reset factory object.
						factoryObject.reset() ;
					}catch(Exception ex){}
				}

				// there is no message.
				if(composers == null || composers.size() == 0) return null;

				// create message header DTO.
				MailHeaderDTO headerDTO = new MailHeaderDTO(composers.get(0)) ;

				// get user ID of this email
				headerDTO.setUsername(userCode);

				// save header email into database
				MailHeader header = new MailHeader();

				// set hader data.
				header.setRead(CommonService.NO);
				header.setAttached(CommonService.getAnswer(headerDTO.isAttached()));
				header.setCreated(new Date());
				header.setPriority(headerDTO.getPriority());
				header.setReceived(headerDTO.getReceived());

				// get sender address.
				// FIXED: NullPointerException when checking mail with null from address.
				final Address sender = headerDTO.getSender();
				header.setSender((sender == null ? StringService.EMPTY_STRING : sender.toUnicodeString()));

				header.setSize(headerDTO.getSize());
				header.setSubject(headerDTO.getSubject());
				header.setSent(headerDTO.getSent());
				header.setUid(headerDTO.getUid());
				header.setAccount(account);
				header.setOwner(headerDTO.getUsername());
				header.setSpam(CommonService.getAnswer(headerDTO.isSpam())) ;

				// Default folder is INBOX
				long folderId = config.getFolders().get(FolderType.INBOX);
				if(CommonService.isTrue(header.getSpam())){
					folderId = config.getFolders().get(FolderType.SPAM) ;
				}

				// 3. saving email content
				header = mailBean.save(header, IOService.getStream(composers.get(0)), folderId);

				// do filter.
				header = new MailFilterEngine(mailBean, userCode).execute(header) ;

				// optimize the store.
				List<String> uids = store.optimize() ;

				// remove delete mail.
				if(uids != null && uids.size() > 0){
					followUpBean.deleteByUids(uids);
					mailBean.deleteByUid(userCode, account, uids) ;
				}

				// put to cache.
				UIDStoreCacheHelper.put(userCode, account, store) ;

				// return fetch mail.
				return header;
			}
		}catch(MailException mex){
			logger.error("Could not fetch any message", mex) ;

			// throw exception.
			throw new EJBException(mex.getMessage(), mex) ;
		}catch(EJBException eex){
			logger.error("Could not save data to db, message: [" + eex.getMessage() + "]", eex) ;

			throw eex ;
		}catch(Exception ex){
			logger.error("Could not save data to db, message: [" + ex.getMessage() + "]", ex) ;

			throw new EJBException(ex.getMessage(), ex) ;
		}

		// does not have any message.
		return null ;
	}


	/**
	 * {@inheritDoc}
	 * if this email is not in Delete Items box, we move to this box, else
	 * remove message out of server
	 *
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		// get user login into the system
		String userCode = getUserCode();
		// 1. get email configuration.
		PersonMailConfig config = getMailConfig(userCode);
		if (config != null && config.getFolders() != null){
			// if this email is not in Delete Items box, we move to this
			// box, else remove message out of server
			MailHeader header = mailBean.load(bizId);
			if(!userCode.equals(header.getOwner())){
			  return;
			}
			Long folderId = header.getFolder().getId();

			// If the message is persisted in Delete folder,
			// delete this message
			if (folderId.equals(config.getFolders().get(FolderType.TRASH))){
				// this message is persisted on mail server
				if (header.getUid() != null){
					String account = header.getAccount();
					if (config.getAccounts() != null && config.getAccounts().containsKey(account)){
						// and get the account configuration
						ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

						// get factory object.
						PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account);

						// get factory
						IMailFactory factory = factoryObject.getObject() ;

						try{
							if(!factory.isConnected()){
								factory.connect(
										MailProtocol.POP3, pop3Cfg.getSecurityMode(),
										pop3Cfg.getServerName(),
										pop3Cfg.getServerPort(),
										pop3Cfg.getAccountName(),
										pop3Cfg.getAccountPassword()) ;
							}
							// make connection
							// create the list of delete mail.
							List<String> uids = new ArrayList<String>();
							uids.add(header.getUid());

							// delete the mail.
							factory.delete(null, uids);
						}finally{
							try{
								factoryObject.reset() ;
							}catch(Exception ex){}
						}
					}
				}
				followUpBean.deleteByData(bizId);
				// remove header message as well as content
				mailBean.delete(bizId);
			}else{
				followUpBean.deleteByData(bizId);
				// move to trash.
				mailBean.move(bizId, config.getFolders().get(FolderType.TRASH));
			}

		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#emptyFolder(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void emptyFolder(long folderId) throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// list of real uids to delete.
		List<String> uids = new ArrayList<String>() ;

		try{
			// get all message id.
			List<String> duids = mailBean.findUidByFolder(userCode, folderId) ;

			// delete mail on mail server.
			// 1. get email configuration.
			PersonMailConfig config = getMailConfig(userCode);
			
			if(config == null || config.getFolders() == null){
			  return;
			}
			
			if (config.getFolders().get(FolderType.TRASH).equals(folderId)||
			    config.getFolders().get(FolderType.SPAM).equals(folderId)){
				// get account configuration.
				Set<String> accounts = config.getAccounts().keySet() ;
				if(accounts != null && !accounts.isEmpty()){
					for(String account : accounts){
						// connect to mail factory.
						PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account) ;
						IMailFactory factory = factoryObject.getObject() ;

						// get pop3 configuration.
						ConfigDTO pop3Cfg = config.getAccounts().get(account)[0] ;
						try{
							if(!factory.isConnected()){
								factory.connect(
										MailProtocol.POP3, pop3Cfg.getSecurityMode(),
										pop3Cfg.getServerName(),
										pop3Cfg.getServerPort(),
										pop3Cfg.getAccountName(),
										pop3Cfg.getAccountPassword());
							}

							List<String> ruids = factory.delete(null, duids) ;
							if(ruids != null && ruids.size() > 0){
								uids.addAll(ruids) ;
							}
						}catch(Exception ex){
							logger.warn("could not delete message in server, message: [" + ex.getMessage() + "]") ;
						}finally{
							try{
								factoryObject.reset() ;
							}catch(Exception ex){}
						}
					}
				}
			}else{// move all mail to trash
			  this.mailBean.move2Folder(folderId, config.getFolders().get(FolderType.TRASH));
			  return;
			}

			// remove message header.
			if(uids != null && uids.size() > 0){
				mailBean.emptyFolder(userCode, uids) ;
			}

			// delete none received mail.
			mailBean.deleteNoneReceivedMail(userCode, folderId) ;
		}catch(Exception ex){
			if(ex instanceof EJBException){
				throw (EJBException)ex ;
			}else{
				throw new EJBException("Could not empty folder: [" + folderId + "].", ex) ;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#fetchNewMessage(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  int fetchNewMessage(String account) throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email configuration.
		PersonMailConfig config = getMailConfig(userCode);

		// the number of message.
		int count = 0 ;

		// fetch the new message.
		if (	config != null &&
				config.getFolders() != null &&
				config.getAccounts() != null &&
				config.getAccounts().containsKey(account)){

			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

			// init the factory
			PoolObject<IMailFactory> factoryObject = getMailFactory(config,userCode, account);
			IMailFactory factory = factoryObject.getObject() ;

			// fetch the UID.
			UIDStore store = getUIDStore(userCode, account) ;

			// count the new mails.
			try{
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}
				// count new mails
				count = factory.count(null, store);
			}finally{
				// close the connection.
				try{
					factoryObject.reset() ;
				}catch(Exception ex){}
			}

			// optimize the store.
			List<String> uids = store.optimize() ;

			// remove delete mail.
			if(uids != null && uids.size() > 0){
				followUpBean.deleteByUids(uids);
				mailBean.deleteByUid(userCode, account, uids) ;
			}

			// put to cache.
			UIDStoreCacheHelper.put(userCode, account, store) ;
		}

		// there is no mail.
		return count;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#sendMessage(long, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader sendMessage(long messageId, String account) throws EJBException {
		return sendMessage(null, messageId,account);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#sendMessage(com.inet.mail.parser.IMessageComposer, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader sendMessage(IMessageComposer message, String account)
			throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email config
		PersonMailConfig config = getMailConfig(userCode);
		if (	config == null ||
				config.getFolders() == null ||
				config.getAccounts() == null ||
				!config.getAccounts().containsKey(account)){
			throw new EJBException("Could not find the mail server from " + userCode);
		}

		// base on the SMTP sending option, we have the sender email!
		ConfigDTO smtp = config.getAccounts().get(account)[1];

		// load the message and mark message to send.
		long folderId = config.getFolders().get(FolderType.SENT);

		// create the mail folder.
		MailFolder folder = new MailFolder();
		folder.setId(config.getFolders().get(FolderType.OUTBOX));

		// create new message.
		MailHeader header = new MailHeader();

		if(message != null){
			// update header information.
			if (message.getFrom() == null){
				message.setFrom(new Address(config.getFullname(), smtp.getAccountName()));
			}

			// update header information.
			header.setFolder(folder);
			header.setSent(new Date());
			header.setOwner(userCode) ;

			header.setAttached(CommonService.getAnswer(message.isAttached()));
			header.setCreated(new Date());
			header.setRead(CommonService.YES);
			header.setPriority(message.getPriority());
			header.setType(MailType.DRAFT);
			header.setRecipients(MailService.buildRecipientsAddress(message.getTo()));

			Address from = message.getFrom();
			if (from != null){
				header.setSender(from.toUnicodeString());
			}

			byte[] data = IOService.getStream(message);
			header.setSubject(message.getSubject());
			header.setSize(data.length);

			// save the header.
			header = mailBean.save(header, data, header.getFolder().getId()) ;
		}

		// send message.
		try {
			// connect producer manager.
			localManager.connect();

			// send message.
			synchonizeBean.send(smtp, message, header.getId(), folderId);

			// close producer manager.
			localManager.close();

			// return header.
			return header;
		} catch (JMSException ex) {
			throw new EJBException("Could not send this message", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#sendMessage(com.inet.mail.parser.IMessageComposer, com.inet.mail.persistence.MailHeader, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader sendMessage(IMessageComposer message, MailHeader header,
			String account) throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email config
		PersonMailConfig config = getMailConfig(userCode);
		if (	config == null ||
				config.getFolders() == null ||
				config.getAccounts() == null ||
				!config.getAccounts().containsKey(account)){
			throw new EJBException("Could not find the mail server from " + userCode);
		}

		// base on the SMTP sending option, we have the sender email!
		ConfigDTO smtp = config.getAccounts().get(account)[1];

		// load the message and mark message to send.
		long folderId = config.getFolders().get(FolderType.SENT);

		// create the mail folder.
		MailFolder folder = new MailFolder();
		folder.setId(config.getFolders().get(FolderType.OUTBOX));

		// load the header.
		if(header.getId() != null) header = mailBean.load(header.getId()) ;

		// update header information.
		header.setFolder(folder);
		header.setSent(new Date());
		header.setOwner(userCode) ;

		if (message != null) {
			if (message.getFrom() == null){
				message.setFrom(new Address(config.getFullname(), smtp.getAccountName()));
			}

			byte[] data = IOService.getStream(message);

			// update header of message
			header.setAttached(CommonService.getAnswer(message.isAttached()));
			header.setPriority(message.getPriority());
			header.setSize(data.length);
			header.setSubject(message.getSubject());
			header.setRecipients(MailService.buildRecipientsAddress(message.getTo()));

			if (message.getFrom() != null){
				header.setSender(message.getFrom().toUnicodeString());
			}

			// saving content before sending
			if(header.getId() != null){
				header = mailBean.update(header, data);
			}else{
				header = mailBean.save(header, data, header.getFolder().getId()) ;
			}
		}

		// send message.
		try {
			// connect producer manager.
			localManager.connect();

			// send message.
			synchonizeBean.send(smtp, message, header.getId(), folderId);

			// close producer manager.
			localManager.close();

			// return header.
			return header;
		} catch (JMSException ex) {
			throw new EJBException("Could not send this message", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#sendMessage(IMessageComposer, long, String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader sendMessage(IMessageComposer message, long messageId, String account)
			throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email config
		PersonMailConfig config = getMailConfig(userCode);
		if (	config == null ||
				config.getFolders() == null ||
				config.getAccounts() == null ||
				!config.getAccounts().containsKey(account)){
			throw new EJBException("Could not find the mail server from " + userCode);
		}

		// base on the SMTP sending option, we have the sender email!
		ConfigDTO smtp = config.getAccounts().get(account)[1];

		// load the message and mark message to send.
		long folderId = config.getFolders().get(FolderType.SENT);
		MailHeader header = mailBean.load(messageId);
		header.setType(MailType.SENT);

		// create the mail folder.
		MailFolder folder = new MailFolder();
		folder.setId(config.getFolders().get(FolderType.OUTBOX));
		header.setFolder(folder);

		header.setSent(new Date());

		// create the message.
		if(message == null ){
			message = IOService.getObject(mailBean.getComposer(header.getComposeID()), IMessageComposer.class);
		}

		if (message.getFrom() == null){
			message.setFrom(new Address(config.getFullname(), smtp.getAccountName()));
		}

		// send message.
		if (message != null) {
			byte[] data = IOService.getStream(message);

			// update header of message
			header.setAttached(CommonService.getAnswer(message.isAttached()));
			header.setPriority(message.getPriority());
			header.setSize(data.length);
			header.setSubject(message.getSubject());
			header.setRecipients(MailService.buildRecipientsAddress(message.getTo()));

			if (message.getFrom() != null){
				header.setSender(message.getFrom().toUnicodeString());
			}

			// saving content before sending
			header = mailBean.update(header, data);
		}

		// put into MDB queue for sending
		try {
			// connect producer manager.
			localManager.connect();

			// send message.
			synchonizeBean.send(smtp, message, messageId, folderId);

			// close producer manager.
			localManager.close();

			return header;
		} catch (JMSException ex) {
			throw new EJBException("Could not send this message", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.MailBridgeRemote#fowardMessage(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeaderDTO fowardMessage(long bizID) throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email config
		PersonMailConfig config = getMailConfig(userCode);
		if (config == null || config.getFolders() == null){
			throw new EJBException("The configure of [" + userCode + "] user is not found.");
		}

		// load the message header.
		MailHeader header = mailBean.load(bizID);
		// update status is forward
		if (header.getType() != MailType.FORWARD) {
			header.setType(MailType.FORWARD);
			mailBean.update(header);
		}

		//  message composer.
		IMessageComposer composer = null;
		// this message is not stored from server ?
		if (StringService.hasLength(header.getComposeID()) && header.getUid() == null){
			// get content of the forward message from database
			byte[] msg = mailBean.getComposer(header.getComposeID());
			composer = IOService.getObject(msg, IMessageComposer.class) ;
		} else {
			// get account information.
			String account = header.getAccount() ;

			if (!config.getAccounts().containsKey(account)){
				throw new EJBException("The account [" + account + "] is not found.");
			}

			// get from mail server the factory
			PoolObject<IMailFactory> factoryObject = getMailFactory(config,userCode,account);
			IMailFactory factory = factoryObject.getObject() ;

			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];
			// make connection and download the mail message.

			try{
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}
				// get the mail message.
				byte[] data = factory.download(null, header.getUid());

				// create the message composer.
				composer = factory.createMessage(data, true, true) ;
			}finally{
				try{
					factoryObject.reset() ;
				}catch(Exception ex){}
			}
		}

		// could not download message.
		if(composer == null){
			throw new EJBException("Could not create mail composer: [" + bizID + "] of user: [" + userCode + "].") ;
		}

		// create forward message.
		AbstractMessageFactory messageFactory = MailConfigureFactory.createMessageFactory() ;
		IMessageComposer forward = messageFactory.forward(
					composer,
					Messages.getMessage(Messages.mail_message_forward)
				) ;

		// create the header
		MailHeader forwardHeader = new MailHeader();

		// set forward information.
		forwardHeader.setSubject(forward.getSubject());
		forwardHeader.setOwner(getUserCode());
		forwardHeader.setType(MailType.DRAFT);
		forwardHeader.setCreated(new Date());
		forwardHeader.setAttached(CommonService.getAnswer(composer.isAttached()));
		forwardHeader.setPriority(MailPriority.NORMAL);
		forwardHeader.setRead(CommonService.YES);

		byte[] msg = IOService.getStream(forward);
		forwardHeader.setSize(msg.length);

		// save message
		header = mailBean.save(forwardHeader, msg, config.getFolders().get(FolderType.DRAFT));

		// return the message DTO.
		return new MailHeaderDTO(forward, header) ;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.MailBridgeRemote#replyMessage(long, boolean)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeaderDTO replyMessage(long bizID, boolean replyAll)
			throws EJBException {
		// get user login into the system
		String userCode = getUserCode();

		// get email config
		PersonMailConfig config = getMailConfig(userCode);
		if (config == null || config.getFolders() == null){
			throw new EJBException("The configure of [" + userCode + "] user is not found.");
		}

		MailHeader header = mailBean.load(bizID);

		// update status is forward
		if (header.getType() != MailType.REPLY) {
			header.setType(MailType.REPLY);
			mailBean.update(header);
		}

		// keep sender
		String sender = header.getSender();

		// keep sent date
		Date sent = header.getSent();

		// create message composer.
		IMessageComposer composer = null;
		if (StringService.hasLength(header.getComposeID())){
			// get content of the forward message from database
			byte[] data = mailBean.getComposer(header.getComposeID());

			composer = IOService.getObject(data, IMessageComposer.class) ;
		} else {
			// get account.
			String account = header.getAccount() ;

			if (!config.getAccounts().containsKey(account)){
				throw new EJBException("The account [" + account + "] is not found.");
			}

			// create message factory from the given user configuration.
			PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account);
			IMailFactory factory = factoryObject.getObject() ;

			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

			try{
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}

				// down load the mail.
				byte[] data = factory.download(null, header.getUid());

				// create composer and parse the body message.
				composer = factory.createMessage(data, true, false) ;
			}finally{
				try{
					factoryObject.reset() ;
				}catch(Exception ex){}
			}
		}

		// there is no mail.
		if (composer == null){
			throw new EJBException("The message content of [" + userCode + "] user is empty.");
		}

		// create replay message.
		AbstractMessageFactory messageFactory = MailConfigureFactory.createMessageFactory() ;
		IMessageComposer reply = messageFactory.reply(
					composer,
					Messages.getMessage(Messages.mail_message_original_msg),
					replyAll
				) ;

		// create the header
		MailHeader replyHeader = new MailHeader();

		replyHeader.setSubject(reply.getSubject());
		replyHeader.setOwner(getUserCode());
		replyHeader.setType(MailType.DRAFT);
		replyHeader.setCreated(new Date());
		replyHeader.setAttached(CommonService.NO);
		replyHeader.setPriority(MailPriority.NORMAL);
		replyHeader.setRead(CommonService.YES);

		byte[] data = IOService.getStream(reply) ;
		replyHeader.setSize(data.length);

		// save message
		header = mailBean.save(replyHeader, data, config.getFolders().get(FolderType.DRAFT));

		// set sent date and sender
		header.setSender(sender);
		header.setSent(sent);

		// create reply header.
		return new MailHeaderDTO(reply, header);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#countUnread()
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Map<Long,Long> countUnread() throws EJBException{
		// get user login into the system
		String userCode = getUserCode();

		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.countUnreadMessage") ;

		// set query parameter.
		query.setParameter("owner", userCode) ;
		query.setParameter("read", CommonService.NO) ;

		try{
			// get result.
			List<FolderCountDTO> result = (List<FolderCountDTO>)query.getResultList() ;

			// create unread message store.
			Map<Long,Long> unread = new  HashMap<Long,Long>();

			if(result != null){
				for (FolderCountDTO folder : result){
					unread.put(folder.getFolderID(), folder.getCount());
				}
			}

			// return data.
			return unread;
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not count the number of unread mail message", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not count the number of unread mail message", ex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#countMessage()
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Map<Long,Long> countMessage() throws EJBException{
		// get user login into the system
		String userCode = getUserCode();

		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.countMessage") ;

		// set query parameter.
		query.setParameter("owner", userCode) ;

		try{
			// execute query and get result.
			List<FolderCountDTO> result = (List<FolderCountDTO>)query.getResultList() ;

			// prepare the result.
			Map<Long, Long> messages = new HashMap<Long, Long>() ;
			for(FolderCountDTO folder : result){
				messages.put(folder.getFolderID(), folder.getCount()) ;
			}

			return messages ;
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not count the total of mail messages", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not count the total of mail messages", ex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByFolder(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolder(long id, int startAt, int max)
			throws EJBException {
		return mailBean.findByFolder(id, getUserCode(), startAt, max);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByAttached(long, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByAttached(long id, boolean attached,
			int startPos, int maxResult) throws EJBException {
		return mailBean.findByAttached(id, getUserCode(), attached, startPos,maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByDate(long, java.util.Date, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByDate(long id, Date date, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByDate(id, getUserCode(), date, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByFlag(long, com.inet.mail.data.MailFlag, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByFlag(long id, MailFlag flag, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByFlag(id, getUserCode(), flag, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.MailBusinessRemote#queryOrderSent(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySent(long id, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByFolderOrderBySent(id, getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByFolderOrderBySubject(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySubject(long id, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByFolderOrderBySubject(id, getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByPriority(long, com.inet.mail.data.MailPriority, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByPriority(long id, MailPriority priority,
			int startPos, int maxResult) throws EJBException {
		return mailBean.findByPriority(id, getUserCode(), priority, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findBySender(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySender(long id, String sender, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findBySender(id, getUserCode(), sender, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findBySubject(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySubject(long id, String subject,
			int startPos, int maxResult) throws EJBException {
		return mailBean.findBySubject(id, getUserCode(), subject, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByRead(long, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByRead(long id, boolean read, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByRead(id, getUserCode(), read, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findByType(long, com.inet.mail.data.MailType, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByType(long id, MailType type, int startPos,
			int maxResult) throws EJBException {
		return mailBean.findByType(id, getUserCode(), type, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#findFilterByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findFilterByUser() throws EJBException {
		return mailBean.findFilterByUser(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#move(long, long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader move(long bizId, long id) throws EJBException {
		// load the message header.
		MailHeader header = mailBean.load(bizId);

		// get user code.
		String userCode = getUserCode();
		if (header.getOwner() != null && !header.getOwner().equals(userCode)){
			throw new EJBException("This is not your own message.");
		}

		// move the message.
		return mailBean.move(bizId, id);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#save(com.inet.mail.persistence.MailHeader, byte[], long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader save(MailHeader header, byte[] stream, long id) throws EJBException {
		// get user code.
		String userCode = getUserCode();

		if (header.getOwner() != null && !header.getOwner().equals(userCode)){
			throw new EJBException("This is not your own message.");
		}
		header.setOwner(userCode);

		return mailBean.save(header, stream, id);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#save(com.inet.mail.persistence.MailHeader, byte[], com.inet.mail.data.FolderType)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader save(MailHeader header, byte[] stream, FolderType folderType)
		throws EJBException {
		// get user code.
		String userCode = getUserCode();
		if (header.getOwner() != null && !header.getOwner().equals(userCode)){
			throw new EJBException("This is not your own message.");
		}

		// load personal mail configuration.
		PersonMailConfig config = getMailConfig(userCode);
		if (config == null || config.getFolders() == null) {
			throw new EJBException("The configure of [" + userCode + "] user is not found.");
		}
		header.setOwner(userCode);

		// save the bean.
		return mailBean.save(header, stream, config.getFolders().get(folderType));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#update(com.inet.mail.persistence.MailHeader, byte[])
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(MailHeader header, byte[] stream) throws EJBException {
		// get user code.
		String userCode = getUserCode();
		if (header.getOwner() != null && !header.getOwner().equals(userCode)){
			throw new EJBException("This is not your own message.");
		}

		// set owner.
		header.setOwner(userCode);

		// update the header.
		return mailBean.update(header, stream);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#update(long, com.inet.mail.data.MailFlag)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(long bizId, MailFlag flag) throws EJBException {
		return mailBean.update(bizId, flag);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#update(long, com.inet.mail.data.MailType)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(long bizId, MailType type) throws EJBException {
		return mailBean.update(bizId, type);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#search(com.inet.mail.data.SearchResultDTO)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> search(SearchResultDTO<MailHeader> criteria)
			throws EJBException {
		return mailBean.search(criteria);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#search(java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> search(String criteria, int startAt,
			int maxItem) throws EJBException {
		return mailBean.search(criteria, startAt, maxItem);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	protected Criteria buildQuery(MailHeaderDTO search) throws EJBException {
		throw new EJBException("This function is not supported");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailHeaderDTO load(long bizId) throws EJBException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#viewBody(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeaderDTO viewBody(long id) throws EJBException {
		// load message header.
		MailHeader header = mailBean.load(id);
		if (header == null) throw new EJBException("Invalidate mail content load");
		// get user login into the system
                String userCode = getUserCode();
                if(!userCode.equals(header.getOwner())){
                  throw new EJBException("This email is not belong to " + getUsername());
                }
                
		IMessageComposer composer = null;
		// read content from database (this content is made from client used)
		if (StringService.hasLength(header.getComposeID())){
			// get mail data.
			byte[] data = mailBean.getComposer(header.getComposeID());

			// the data is null ?
			if (data == null || data.length == 0){
				throw new EJBException("Invalidate mail content load");
			}

			// get composer.
			composer = IOService.getObject(data, IMessageComposer.class);
		}else{
			// get email config
			PersonMailConfig config = getMailConfig(userCode);

			// get header account.
			String account = header.getAccount() ;

			// This content is loading from mail server
			if (!config.getAccounts().containsKey(account)){
				throw new EJBException("The account [" + account + "] is not found.");
			}

			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

			// get from mail server init the factory
			PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account);

			// get mail object.
			IMailFactory factory = factoryObject.getObject() ;

			try{
				// make connection
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}
				// down load the message.
				byte[] data = factory.download(
							null,
							header.getUid()
						);

				// create message composer.
				composer = factory.createMessage(data, true, false) ;
			}finally{
				try{
					// return object.
					factoryObject.reset() ;
				}catch(Exception ex){}
			}
			if (composer == null) throw new EJBException("Invalidate mail content load");
		}

		boolean update = !CommonService.isTrue(header.getRead());
		// update the content is read.
		if (update){
			header.setRead(CommonService.YES);
		}

		// cache this data into server!!!
		if (header.getComposeID() == null){
			this.mailBean.update(header,IOService.getStream(composer));
		}else if (update){
			this.mailBean.update(header);
		}

		// return the mail header data.
		return new MailHeaderDTO(composer, header);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#viewAttachment(long, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public PairValueDTO<byte[]> viewAttachment(long id, String fileindex) throws EJBException {
		MailHeader header = this.mailBean.load(id);
		if (header == null) throw new EJBException("Invalidate mail content load");

		String extFile = FileService.getExtension(fileindex);
		String fType[] = MailService.getAttachType(extFile);

		PairValueDTO<byte[]> attach = null;

		// read content from database (this content is made from client used)
		if (StringService.hasLength(header.getComposeID()) &&  header.getUid() == null) {
			byte[] data = mailBean.getComposer(header.getComposeID());
			// the data is null ?
			if (data == null || data.length == 0) throw new EJBException("Invalidate mail content load");
			// get message composer.
			IMessageComposer composer = IOService.getObject(data, IMessageComposer.class);
			// get message attachment.
			Map<String, byte[]> attachments = composer.getAttachments() ;
			if (attachments != null /*&& attachments.containsKey(fileindex)*/){
			  String index = AttachmentParser.getNumberOrder(fileindex);
			  for(String key: attachments.keySet()){
			    String indexKey = AttachmentParser.getNumberOrder(key);
			    if(indexKey != null && index.equals(indexKey)){
			      attach = new PairValueDTO<byte[]>(fType[1], attachments.get(key));
			      break;
			    }
			  }	
			}
		}else{
			// get user login into the system
			String userCode = getUserCode();
			// get email config
			PersonMailConfig config = getMailConfig(userCode);

			// get account.
			String account = header.getAccount() ;

			// This content is loading from mail server
			if (!config.getAccounts().containsKey(account)){
				throw new EJBException("The account [" + account + "] is not found.");
			}
			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

			// get from mail server init the factory
			PoolObject<IMailFactory> factoryObject = getMailFactory(config,userCode, account);

			// get factory.
			IMailFactory factory = factoryObject.getObject() ;
			try{
				// make connection
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}
				// down load mail.
				byte[] data = factory.download(null, header.getUid());
				try {
					attach = new PairValueDTO<byte[]>(fType[1],MailService.dataAttachment(data, fileindex));
				} catch (MessagingException ex) {
					throw new EJBException("Message is invalidate.",ex);
				} catch (IOException ex) {
					throw new EJBException("Attach [" + fileindex + "] is fail.",ex);
				}
			}finally{
				// close the factory.
				try{
					factoryObject.reset() ;
				}catch(Exception ex){}
			}
		}

		// return attach value.
		return attach;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#viewAttachment(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Map<String, byte[]> viewAttachment(long id) throws EJBException {
		MailHeader header = this.mailBean.load(id);
		if (header == null) throw new EJBException("Invalidate mail content load");

		// read content from database (this content is made from client used)
		if (StringService.hasLength(header.getComposeID()) &&  header.getUid() == null) {
			byte[] data = mailBean.getComposer(header.getComposeID());

			// the data is null ?
			if (data == null || data.length == 0) throw new EJBException("Invalidate mail content load");

			IMessageComposer composer = IOService.getObject(data, IMessageComposer.class);
			return composer.getAttachments() ;
		}else{
			// get user login into the system
			String userCode = getUserCode();

			// get person configuration.
			PersonMailConfig config = getMailConfig(userCode);

			// get account.
			String account = header.getAccount() ;

			// This content is loading from mail server
			if (!config.getAccounts().containsKey(account)){
				throw new EJBException("The account [" + account + "] is not found.");
			}

			// and get the account configuration
			ConfigDTO  pop3Cfg = config.getAccounts().get(account)[0];

			// get from mail server init the factory
			PoolObject<IMailFactory> factoryObject = getMailFactory(config, userCode, account);
			IMailFactory factory = factoryObject.getObject() ;

			try{
				// make connection
				if(!factory.isConnected()){
					factory.connect(
							MailProtocol.POP3,
							pop3Cfg.getSecurityMode(),
							pop3Cfg.getServerName(),
							pop3Cfg.getServerPort(),
							pop3Cfg.getAccountName(),
							pop3Cfg.getAccountPassword()) ;
				}

				// download the mail.
				byte[] data = factory.download(SunMailFactory.DEFAULT_FOLDER, header.getUid());

				MimeMessage  message = new MimeMessage(null, new ByteArrayInputStream(data));
				// return the mail attachment.
				return MailService.getAttachments(message, true, null) ;
			} catch (MessagingException ex) {
				throw new EJBException("Error part messages " , ex);
			} catch (IOException ex) {
				throw new EJBException("Error get all attachments",ex);
			}finally{
				try{
					factoryObject.reset() ;
				}catch(Exception ex){}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#getSmtpAccount()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> getSmtpAccount() throws EJBException {
		// get the configuration from the given user code.
		PersonMailConfig config = getMailConfig(getUserCode());
		List<String> smtpAccounts = new ArrayList<String>();

		for (String receiver : config.getAccounts().keySet()) {
			smtpAccounts.add(receiver);
		}

		return smtpAccounts;
	}

	/**
	 * @see com.inet.mail.business.sr.MailBridgeRemote#getMailConfig(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public PersonMailConfig getMailConfig(String userCode) throws EJBException {
		// 1. load the configure from cache
		CacheEntry entry = (CacheEntry) DataEntryCacheHelper.get(userCode);

		// get personal configuration.
		PersonMailConfig config = (entry == null ? null : entry.getContent());
		if (config == null) {
			// retry and add into cache
			MailAcctConfigInfo dataCfg = mailCfgBean.findByUser(userCode);

			// the data does not exist.
			if (dataCfg == null) throw new EJBException("The user does not register the mail before get mail messages.");

			// create personal configuration.
			config = new PersonMailConfig(dataCfg);

			// load Default folders
			List<MailFolder> defFolders = folderBean.queryDefaultFolder(userCode);
			for (MailFolder defFolder : defFolders) {
				int index = (int) (-1 * defFolder.getParentId());
				config.getFolders().put(FolderType.values()[index - 1], defFolder.getId());
			}

			entry = new CacheEntry(config);
			DataEntryCacheHelper.put(userCode, entry);
		}

		// return the configuration.
		return config;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.business.sr.MailBridgeRemote#loadMailConfig()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailAcctConfigInfo loadMailConfig() throws EJBException {
		return mailCfgBean.findByUser(getUserCode());
	}
	//~ Helper Methods ========================================================
	/**
	 * Find the list of UIDStore
	 *
	 * @param usercode the given login user code.
	 * @param account the given user account.
	 * @return the {@link UIDStore} instance.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected UIDStore getUIDStore(String usercode, String account){
		try{
			// get UIDStore from cache.
			UIDStore store = UIDStoreCacheHelper.get(usercode, account) ;

			// does not exist, try to get new and put to cache.
			if(store == null){
				List<String> uids = mailBean.findUidByAccount(usercode, account) ;
				store = new UIDStore(uids) ;

				// put to cache.
				UIDStoreCacheHelper.put(usercode, account, store) ;
			}

			return store ;
		}catch(EJBException ex){
			logger.warn("Could not load the UID store.", ex) ;
		}

		// the UID store.
		return new UIDStore() ;
	}

	/**
	 * Initiate the factory as well as loading mail filter.
	 *
	 * @param config the given {@link PersonMailConfig} instance.
	 * @param usercode the given user code.
	 * @param account the given account information.
	 *
	 * @return the {@link IMailFactory} instance.
	 */
	private PoolObject<IMailFactory> getMailFactory(PersonMailConfig config,
						String usercode,
						String account) throws EJBException {
		// get pool object key.
		String key = getKey(usercode, account) ;

		// get mail application context.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		PoolObjectManager<IMailFactory> factoryManager = context.get(POOL_MANAGER_KEY) ;

		// register factory manager.
		if(factoryManager == null){
			factoryManager = new PoolObjectManagerSupport<IMailFactory>() ;
			factoryManager = context.register(POOL_MANAGER_KEY, factoryManager) ;

			// run factory.
			factoryManager.start() ;
		}

		// get factory object.
		PoolObject<IMailFactory> factoryObject = factoryManager.get(key) ;
		if(factoryObject == null){
			IMailFactory factory = MailConfigureFactory.createFactory(config, account) ;
			factoryObject = new MailFactoryPoolObjectSupport(key, factory) ;
			factoryManager.put(factoryObject) ;
		}

		return factoryManager.get(key) ;
	}

	/**
	 * @return the key from the given usercode and account.
	 */
	private String getKey(String usercode, String account){
		return usercode + '#' + account ;
	}

	/**
	 * @see com.inet.mail.business.sr.MailBridgeRemote#saveFollowUp(java.util.List)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public List<FollowUpDTO> saveFollowUp(List<FollowUp> followUps)
			throws EJBException {
		String userCode = getUserCode();
		List<FollowUpDTO> followUpDTOs = new ArrayList<FollowUpDTO>();
		for(FollowUp followUp: followUps){
			followUp.setUserCode(userCode);
			followUpBean.save(followUp);
			//load mail header
			MailHeader header = mailBean.load(followUp.getMail().getId());

			followUpDTOs.add(new FollowUpDTO(followUp, header));
		}

		return followUpDTOs;
	}

	/**
	 * @see com.inet.mail.business.sr.MailBridgeRemote#updateFollowUp(java.util.List, java.util.Date)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public List<FollowUpDTO> updateFollowUp(List<Long> ids, Date date)
			throws EJBException {
		List<FollowUpDTO> followUpDTOs = new ArrayList<FollowUpDTO>();
		for(Long id : ids){
			FollowUp followUp = followUpBean.load(id);
			followUp.setDate(date);
			followUp = followUpBean.update(followUp);

			MailHeader header = mailBean.load(followUp.getMail().getId());

			followUpDTOs.add(new FollowUpDTO(followUp, header));
		}
		return followUpDTOs;
	}
}
