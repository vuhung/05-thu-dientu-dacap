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
package com.inet.mail.command;

import java.util.Date;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.CommonService;
import com.inet.base.service.IOService;
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.data.Address;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailType;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.parser.support.sun.MessageComposer;
import com.inet.mail.persistence.MailHeader;
import com.inet.mail.util.MailService;

/**
 * AbstractCommand
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 *
 * @date Feb 14, 2008
 * <pre>
 *  Initialization AbstractCommand class.
 * </pre>
 */
public abstract class AbstractCommand {
	// mail header remote state-less.
	protected MailBridgeRemote bridgeSL ;

	/**
	 * Create AbstractCommand instance.
	 *
	 * @param headerSL MailBridgeRemote - the given header remote state-less.
	 * @param configure MailConfigure - the given mail configuration.
	 */
	public AbstractCommand(MailBridgeRemote bridgeSL){
		this.bridgeSL = bridgeSL ;
	}

	/**
	 * @return the mail header remote state-less.
	 */
	public MailBridgeRemote getBusiness(){
		return this.bridgeSL ;
	}

	//----------------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Delete the mail header from the given mail header identifier.
	 *
	 * @param id long - the given mail header identifier.
	 * @throws EJBException if an error occurs during deleting mail header.
	 */
	protected void delete(long id) throws EJBException{
		this.bridgeSL.delete(id) ;
	}

	/**
	 * Move the mail from the current folder to the folder that corresponding with the
	 * given folder identifier.
	 *
	 * @param id long - the given mail header identifier.
	 * @param folderId long - the given folder identifier.
	 * @throws EJBException if an error occurs during moving mail header.
	 */
	protected void move(long id, long folderId) throws EJBException{
		this.bridgeSL.move(id, folderId) ;
	}

	/**
	 * Send the message to user.
	 *
	 * @param messageId
	 * @param host
	 * @param account
	 * @throws EJBException
	 */
	protected void send(Long messageId, String host, String account) throws EJBException{
		this.bridgeSL.sendMessage(messageId, MailService.createTicket(host, account)) ;
	}

	/**
	 * Send the message to user.
	 *
	 * @param message Object - the given message instance.
	 * @throws MailException if an error occurs during sending mail.
	 */
	protected void send(MessageComposer composer, Long messageId, String host, String account) throws EJBException{
		this.bridgeSL.sendMessage(composer,messageId, MailService.createTicket(host, account)) ;
	}

	/**
	 * Update the mail type.
	 *
	 * @param id long - the given mail identifier.
	 * @param type MailType - the given mail type.
	 * @throws EJBException if an error occurs during updating mail type.
	 */
	protected void update(long id, MailType type) throws EJBException{
		this.bridgeSL.update(id, type) ;
	}

	/**
	 * Update the mail flags.
	 *
	 * @param id long - the given mail identifier.
	 * @param type MailType - the given mail type.
	 * @throws EJBException if an error occurs during updating mail type.
	 */
	protected void update(long id, MailFlag flag) throws EJBException{
		this.bridgeSL.update(id, flag) ;
	}

	/**
	 * Saving mail content.
	 *
	 * @param header MailHeader - the mail content.
	 * @param data byte[] - the mail data.
	 * @param body byte[] - the body data.
	 * @param folderId long - the folder identifier.
	 * @return the mail content.
	 * @throws EJBException if an error occurs during saving mail.
	 */
	protected MailHeader save(MailHeader header, byte[] data, long folderId) throws EJBException{
		return this.bridgeSL.save(header, data, folderId) ;
	}

	/**
	 *
	 * @param composer
	 * @return
	 * @throws EJBException
	 */
	protected MailHeader drafts(IMessageComposer composer) throws EJBException{
		MailHeader header = new MailHeader();
		header.setAttached(CommonService.getAnswer(composer.isAttached()));
		header.setCreated(new Date());
		header.setRead(CommonService.YES);
		header.setPriority(composer.getPriority());
		header.setType(MailType.DRAFT);
		header.setRecipients(MailService.buildRecipientsAddress(composer.getTo()));

		Address from = composer.getFrom();
		if (from != null){
			header.setSender(from.toUnicodeString());
		}

		byte[] data = IOService.getStream(composer);
		header.setSubject(composer.getSubject());
		header.setSize(data.length);

		return this.bridgeSL.save(header, data,FolderType.DRAFT) ;
	}

	/**
	 *
	 * @param composer
	 * @return
	 * @throws EJBException
	 */
	protected MailHeader drafts(MailHeader header,IMessageComposer composer) throws EJBException{
		header.setAttached(CommonService.getAnswer(composer.isAttached()));
		header.setCreated(new Date());
		header.setRead(CommonService.YES);
		header.setPriority(composer.getPriority());
		header.setRecipients(MailService.buildRecipientsAddress(composer.getTo()));

		Address from = composer.getFrom();
		if (from != null){
			header.setSender(from.toUnicodeString());
		}

		byte[] data = IOService.getStream(composer);
		header.setSubject(composer.getSubject());
		header.setSize(data.length);
		header.setType(MailType.DRAFT);

		if (header.getId() > 0){
			return getBusiness().update(header, data);
		}else{
			return getBusiness().save(header, data, FolderType.DRAFT);
		}
	}
}
