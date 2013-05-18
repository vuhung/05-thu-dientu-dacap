/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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

import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.jsr305.Nonnull;
import com.inet.base.service.CommonService;
import com.inet.base.service.StringService;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.core.CacheEntry;
import com.inet.mail.core.cache.util.DataEntryCacheHelper;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailConstant;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.util.MailService;

/**
 * MailAcctConfigInfoSLBean
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date Jan 26, 2008
 * <pre>
 *  Initialization MailConfigureSLBean class.
 * </pre>
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class MailAcctConfigInfoSLBean extends BaseMailBusinessBean<MailAcctConfigInfo>
		implements MailAcctConfigInfoSL, MailAcctConfigInfoRemoteSL {
	//~ Instance fields =======================================================
	// define dependency bean.
	@EJB(name = "MailFolderSLBean")
	private MailFolderSL folderSL; // mail folder state-less bean.
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#findByUser(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailAcctConfigInfo findByUser(String code) throws EJBException {		
		// create query.
		Query query = getEntityManager().createNamedQuery("MailAcctConfigInfo.findByUser") ;
		
		// set query parameter.
		query.setParameter("owner", code) ;
		
		// execute query and return result.
		List<MailAcctConfigInfo> configures = load(query) ;
		
		// there is not configurations.
		if(configures == null || configures.size() == 0) return null ;
		
		// get the first mail configuration.
		MailAcctConfigInfo configure = configures.get(0);
		
		// pre-processing data.
		preProcessing(configure, false) ;
		
		// return the mail configuration.
		return configure ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#findByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailAcctConfigInfo findByUser() throws EJBException {
		return findByUser(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#update(long, com.inet.mail.data.MailReceiverDTO)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo update(long bizId, MailReceiverDTO receiverDTO)
			throws EJBException {
		// load configuration.
		MailAcctConfigInfo configure = load(bizId, MailAcctConfigInfo.class) ;
		
		// set mail receiver DTO.
		configure.setReceiverObject(receiverDTO) ;
				
		// save data.
		return update(configure) ;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusiness#delete(long)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		MailAcctConfigInfo item = load(bizId);
		remove(item) ;
		
		// cache remove if it is existing from database
		DataEntryCacheHelper.remove(item.getOwner()) ;
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusiness#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailAcctConfigInfo load(long bizId) throws EJBException {
		// load the object.
		MailAcctConfigInfo configure = load(bizId, MailAcctConfigInfo.class) ;
		
		// pre-processing data after loading.
		preProcessing(configure, false) ;
		
		// return result.
		return configure;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusiness#save(java.lang.Object)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo save(MailAcctConfigInfo configure) throws EJBException {
		if(configure == null) return null;
		
		// create folders.
		Hashtable<FolderType, Long> folders = new Hashtable<FolderType, Long>();		
		// add default folders if any
		if (!folderSL.countDefaultFolder(configure.getOwner())){
			// automatically create all default folder
			// except CUSTOM and UNKNOWN ones
			for (int index = 0; index < FolderType.values().length - 2; index++){
				MailFolder folder = new MailFolder();
				folder.setOwner(configure.getOwner());
				folder.setType(FolderType.values()[index]);
				folder.setParentId(-(index+1));
				
				if (FolderType.values()[index] == FolderType.INBOX){
					folder.setName(MailConstant.MAIL_INBOX);
				}else if (FolderType.values()[index] == FolderType.DRAFT){
					folder.setName(MailConstant.MAIL_DRAFT);
				}else if (FolderType.values()[index] == FolderType.TRASH){
					folder.setName(MailConstant.MAIL_TRASH);
				}else if (FolderType.values()[index] == FolderType.OUTBOX){
					folder.setName(MailConstant.MAIL_OUTBOX);
				}else if (FolderType.values()[index] == FolderType.SENT){
					folder.setName(MailConstant.MAIL_SENT);
				}else if (FolderType.values()[index] == FolderType.SPAM){
					folder.setName(MailConstant.MAIL_SPAM);				
				}
				
				// save folder.
				folder = folderSL.save(folder);
				
				// update folder
				folders.put(folder.getType(), folder.getId());				
			}
		}
		
		// pre-processing data before saving.
		preProcessing(configure, true) ;		
		
		// save configuration.
		MailAcctConfigInfo configInfo = insert(configure) ;
		// pre-processing data.
		byte[] data = configure.getReceiverServer();
		if (data != null){
			configure.setReceiverObject(MailReceiverDTO.convertFrom(data));
		}
		// create mail configuration.
		PersonMailConfig mailCfg = new PersonMailConfig(configure);
		// set mail folders.
		mailCfg.setFolders(folders);
		// create cache entry.
		CacheEntry entry = new CacheEntry(mailCfg) ;		
		// update cache
		DataEntryCacheHelper.put(configure.getOwner(), entry);
		// return the configuration.
		return configInfo ;
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusiness#update(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo update(MailAcctConfigInfo configure) throws EJBException {
		if(configure == null) return null;
		
		Hashtable<FolderType, Long> folders = new Hashtable<FolderType, Long>();
		
		// add default folders if any
		if (!folderSL.countDefaultFolder(configure.getOwner())){
			// automatically create all default folder
			// except CUSTOM and UNKNOWN ones
			for (int index = 0; index < FolderType.values().length - 2; index++){
				MailFolder folder = new MailFolder();
				folder.setOwner(configure.getOwner());
				folder.setType(FolderType.values()[index]);
				folder.setParentId(-(index+1));
				
				if (FolderType.values()[index] == FolderType.INBOX){
					folder.setName(MailConstant.MAIL_INBOX);
				}else if (FolderType.values()[index] == FolderType.DRAFT){
					folder.setName(MailConstant.MAIL_DRAFT);
				}else if (FolderType.values()[index] == FolderType.TRASH){
					folder.setName(MailConstant.MAIL_TRASH);
				}else if (FolderType.values()[index] == FolderType.OUTBOX){
					folder.setName(MailConstant.MAIL_OUTBOX);
				}else if (FolderType.values()[index] == FolderType.SENT){
					folder.setName(MailConstant.MAIL_SENT);
				}else if (FolderType.values()[index] == FolderType.SPAM){
					folder.setName(MailConstant.MAIL_SPAM);
				}
				
				// save folder.
				folder = folderSL.save(folder);
				
				// update folder
				folders.put(folder.getType(), folder.getId());
			}
		}
		
		// pre-processing data before saving.
		preProcessing(configure, true) ;
		
		// merge configuration.
		configure = merge(configure);
		
		// get cache from distributed system
		CacheEntry entry = (CacheEntry)DataEntryCacheHelper.get(configure.getOwner());
		
		// cache update if it is existing from database
		if (entry != null) {
			// pre-processing data.
			byte[] data = configure.getReceiverServer();
			if (data != null){
				configure.setReceiverObject(MailReceiverDTO.convertFrom(data));
			}
			
			PersonMailConfig mailCfg = new PersonMailConfig(configure);
			if (folders.size() == 0 && entry.getContent() != null){
				folders.putAll(entry.getContent().getFolders());
			}
			
			mailCfg.setFolders(folders);
			
			entry.setContent(mailCfg);
			
			// update cache
			DataEntryCacheHelper.put(configure.getOwner(), entry);
		}
		
		// save data.
		return configure;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#update(long, java.lang.String)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo update(long bizId, String defaultSMTP)
			throws EJBException {
		MailAcctConfigInfo item = load(bizId);
		if (item != null){
			item.setDefaultSMTP(defaultSMTP);
			item = update(item);
		}
		
		return item;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#createConfig(java.lang.String, java.lang.String, com.inet.mail.data.MailReceiverObject)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo createConfig(String userCode,	String fullName, MailReceiverObject receiver) throws EJBException {
		MailAcctConfigInfo config = this.findByUser(userCode);
		try{
			// configure is null ?
			if(config == null){
				config = new MailAcctConfigInfo();
				config.setFullname(fullName);
				config.setMultipart(CommonService.YES);
				config.setDigitalAlgorithm(StringService.EMPTY_STRING);
				config.setOwner(userCode);
				config.setRefresh(0);			
			}
			
			MailReceiverDTO receiverDTO = config.getReceiverObject();
			
			if(receiverDTO == null){
				receiverDTO = new MailReceiverDTO();
				receiverDTO.addAccount(receiver);
				// add default smtp
				config.setDefaultSMTP(MailService.createTicket(receiver.getSMTPServerName(), receiver.getSMTPAccountName()));
			}else{
				MailReceiverObject newReceiverObject = new MailReceiverObject(receiver.getAccountName());
				
				newReceiverObject.setDescription(receiver.getDescription());
				newReceiverObject.setEmailAddress(receiver.getEmailAddress());
				newReceiverObject.setActive(receiver.getActive());
				
				newReceiverObject.setAccountName(receiver.getAccountName());
				newReceiverObject.setAccountPassword(receiver.getAccountPassword());
				newReceiverObject.setProtocol(receiver.getProtocol() );
				newReceiverObject.setSecurity(receiver.getSecurity());
				newReceiverObject.setServerName(receiver.getServerName());
				newReceiverObject.setServerPort(receiver.getServerPort());
				
				newReceiverObject.setSMTPAccountName(receiver.getSMTPAccountName());
				newReceiverObject.setSMTPAccountPassword(receiver.getSMTPAccountPassword());
				newReceiverObject.setSMTPSecurity(receiver.getSMTPSecurity());
				newReceiverObject.setSMTPServerName(receiver.getSMTPServerName());
				newReceiverObject.setSMTPServerPort(receiver.getSMTPServerPort());
				
				receiverDTO.addAccount(newReceiverObject);
				if(!StringService.hasLength(config.getDefaultSMTP())){
					// add default smtp
					config.setDefaultSMTP(MailService.createTicket(newReceiverObject.getSMTPServerName(), newReceiverObject.getSMTPAccountName()));
				}
			}
			
			// set accounts
			config.setReceiverObject(receiverDTO);
			
			// save/update configure
			if(config.getId() > 0L){
				config = update(config);
			}else{
				config = save(config);
			}
		}catch(Exception ex){			
			throw new EJBException(ex);
		}
		
		
		return config;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#createConfig(java.lang.String, com.inet.mail.data.MailReceiverObject)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo createConfig(String fullName,
			MailReceiverObject object) throws EJBException {
		return createConfig(getUserCode(), fullName, object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#activeAccount(java.lang.String, java.lang.String, boolean)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo activeAccount(String userCode,String emailAddress, boolean isActive) throws EJBException {
		// find mail configuration.
		MailAcctConfigInfo config = findByUser(userCode);		
		if(config == null) return null;
		
		// get mail receiver DTO and active account.
		MailReceiverDTO receiverDTO = config.getReceiverObject();
		
		// configure is empty
		if(receiverDTO == null || receiverDTO.getAccounts()== null ) return config;
		
		MailReceiverDTO newReceiverDTO = new MailReceiverDTO();
		
		for(MailReceiverObject item : receiverDTO.getAccounts()){
			// is email which need active
			if(item.getEmailAddress().equals(emailAddress)){
				item.setActive(isActive);
			}
			newReceiverDTO.addAccount(item);
		}
		
		config.setReceiverObject(newReceiverDTO);
		
		// update configure
		return update(config);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#activeAccount(java.lang.String, boolean)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo activeAccount(String emailAddress,
			boolean isActive) throws EJBException {
		return activeAccount(getUserCode(), emailAddress, isActive);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#changePassword(java.lang.String, java.util.List, java.lang.String)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo changePassword(String userCode,List<String> emailAddress, String password) throws EJBException {
		// load mail account configuration.
		MailAcctConfigInfo config = findByUser(userCode);		
		if(config == null) return null;
		
		// get mail receiver DTO and change password.
		MailReceiverDTO receiverDTO = config.getReceiverObject();
		
		// configure is empty
		if(receiverDTO == null || receiverDTO.getAccounts()== null ) return config;
		
		MailReceiverDTO newReceiverDTO = new MailReceiverDTO();
		
		for(MailReceiverObject item : receiverDTO.getAccounts()){
			// is email which need active
			if(emailAddress.contains(item.getEmailAddress())){
				item.setAccountPassword(MailService.encrypt(item.getAccountName(), password));
				item.setSMTPAccountPassword(MailService.encrypt(item.getSMTPAccountName(), password));
			}
			newReceiverDTO.addAccount(item);
		}
		
		config.setReceiverObject(newReceiverDTO);
		
		// update configure
		return update(config);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#changePassword(java.util.List, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailAcctConfigInfo changePassword(List<String> emails,
			String password) throws EJBException {
	  try {
	    return changePassword(getUserCode(), emails, password);
	  } catch (final Exception ex) {
	    ex.printStackTrace();
	    throw new EJBException("***BUGS 123 Cannot change password.", ex);
	  }
	}
	
	//~ Helper methods ========================================================
	/**
	 * pre-processing mail configuration data.
	 * 
	 * @param configure MailAcctConfigInfo - the given mail configuration.
	 * @param save boolean - if <code>true</code>, pre-processing data used to save to database
	 * otherwise, load from database.
	 */
	private void preProcessing(MailAcctConfigInfo configure, boolean save){
		if(configure == null) return;
			
		// load configuration from database.
		if(!save){
			if(configure.getReceiverServer() != null){
				try{
					// get receiver object.
					configure.setReceiverObject(MailReceiverDTO.convertFrom(configure.getReceiverServer()));
				}catch(Exception ex){}
			}
		}else{ // save data to database.
			if(configure.getReceiverObject() != null){
				configure.setReceiverServer(configure.getReceiverObject().getData()) ;
			}else{
				configure.setReceiverServer(null) ;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#delete(java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(List<String> accounts) throws EJBException {
		delete(getUserCode(), accounts) ;
	}

	/**
	 * @see com.inet.mail.business.sr.MailAcctConfigInfoBase#delete(java.lang.String, java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED) 
	public void delete(@Nonnull String userCode,@Nonnull List<String> accounts) throws EJBException {
		// load mail account configuration.
		MailAcctConfigInfo config = findByUser(userCode);		
		if(config == null) return ;
		
		// get mail receiver DTO .
		MailReceiverDTO receiverDTO = config.getReceiverObject();
		
		// configure is empty
		if(receiverDTO == null || receiverDTO.getAccounts()== null ||receiverDTO.getAccounts().size()== 0) return ;
		
		MailReceiverDTO newReceiverDTO = new MailReceiverDTO();
		
		for(MailReceiverObject item : receiverDTO.getAccounts()){
			if(!accounts.contains(item.getEmailAddress())){
				newReceiverDTO.addAccount(item);
			}
		}
		
		config.setReceiverObject(newReceiverDTO);
		
		// update configure
		update(config);
	}

  /* (non-Javadoc)
   * @see com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL#addAccount(java.lang.String, java.lang.String, com.inet.mail.data.MailReceiverObject)
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public MailAcctConfigInfo addAccount(String userCode, String fullName, MailReceiverObject receiver) throws EJBException {
    try{
      MailAcctConfigInfo config = new MailAcctConfigInfo();
      // configure is null ?
      config = new MailAcctConfigInfo();
      config.setFullname(fullName);
      config.setMultipart(CommonService.YES);
      config.setDigitalAlgorithm(StringService.EMPTY_STRING);
      config.setOwner(userCode);
      config.setRefresh(0);                   
      
      MailReceiverDTO receiverDTO = new MailReceiverDTO();
      receiverDTO.addAccount(receiver);
      config.setDefaultSMTP(MailService.createTicket(receiver.getSMTPServerName(), receiver.getSMTPAccountName()));
      
      // set accounts
      config.setReceiverObject(receiverDTO);
      return save(config);
    }catch(Exception ex){
      ex.printStackTrace();
      throw new EJBException(ex);
    }
  }
}