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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailType;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.FollowUp;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailHeader;

/**
 * MailConnectionRemote.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Remote
public interface MailBridgeRemote {
	/**
	 * Delete a message 
	 * @param bizId
	 * @throws EJBException
	 */
	public void delete(long bizId) throws EJBException;
	
	/**
	 * Fetch new email from server
	 * 
	 * @param account
	 * @return
	 * @throws EJBException
	 */
	public MailHeader fetch(String account) throws EJBException;
	
	/**
	 * Get new count message
	 */
	public  int fetchNewMessage(String account) throws EJBException;
	
	/**
	 * count unread messages 
	 * 
	 * @return
	 * @throws EJBException
	 */
	public Map<Long,Long> countUnread() throws EJBException;

	/**
	 * count all messages in the folder
	 * 
	 * @return
	 * @throws EJBException
	 */
	public Map<Long,Long> countMessage() throws EJBException;
	
	/**
	 * 
	 * @param messageId
	 * @param smtpAccount
	 * @return
	 * @throws EJBException
	 */
	public MailHeader sendMessage(long messageId, String account) throws EJBException;
	
	/**
	 * Send the message from the given {@link IMessageComposer} instance and 
	 * account information.
	 * 
	 * @param message the given {@link IMessageComposer} instance.
	 * @param account the given account information.
	 * @return the {@link MailHeader} information.
	 * 
	 * @throws EJBException when error occurs during sending mail message.
	 */
	MailHeader sendMessage(IMessageComposer message, String account) throws EJBException;	

	/**
	 * Send the message from the given {@link IMessageComposer} instance, {@link MailHeader} instance
	 * and the given account.
	 * 
	 * @param message the given {@link IMessageComposer} instance.
	 * @param header the given {@link MailHeader} instance.
	 * @param account the given account information.
	 * 
	 * @return the given {@link MailHeader} information.
	 * @throws when error occurs during sending message.
	 */
	MailHeader sendMessage(IMessageComposer message, MailHeader header, String account) throws EJBException;		
	
	/**
	 * Send this message from draft folder
	 * 
	 * @param message
	 * @param messageId
	 * @param account
	 * @return
	 * @throws EJBException
	 */
	public MailHeader sendMessage(IMessageComposer message, long messageId, String account) throws EJBException;
	
	/**
	 * Want to reply this message, after this command is created  successfully, the message will be saved
	 * into drafts folder
	 * @param bizID
	 * @param replyAll
	 * @return
	 * @throws EJBException
	 */
	public MailHeaderDTO replyMessage(long bizID, boolean replyAll) throws EJBException;
	
	/**
	 * Want to forward this message, after this command is created successfully, the message will
	 * be saved into drafts folder
	 * @param bizID
	 * @param replyAll
	 * @return
	 * @throws EJBException
	 */
	public MailHeaderDTO fowardMessage(long bizID) throws EJBException;
	
	/**
	 * Search the given criteria.
	 * 
	 * @param criteria SearchResultDTO<MailHeader> - the given criteria.
	 * @return the search result DTO.
	 * @throws EJBException if an error occurs during searching data.
	 */
	SearchResultDTO<MailHeader> search(SearchResultDTO<MailHeader> criteria) throws EJBException ;
	
	/**
	 * Search the given criteria.
	 * 
	 * @param criteria the given search criteria.
	 * @param startAt the given start position.
	 * @param maxItem the given max fetched item.
	 * 
	 * @return the search result DTO.
	 * @throws EJBException when error occurs during searching data.
	 */
	SearchResultDTO<MailHeader> search(String criteria, int startAt, int maxItem) throws EJBException ;
	
	
	/**
	 * Find all mails in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail headers.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByFolder(long id, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all read/unread mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param read boolean - If <code>true</code> user want to view read mail, otherwise view unread mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByRead(long id, boolean read, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all attached/unattached mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param attached boolean - If <code>true</code> user want to view attached mail, otherwise 
	 * view unattached mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByAttached(long id, boolean attached, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given priority.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.	 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByPriority(long id, MailPriority priority, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given mail flag.
	 * 
	 * @param id long - the given folder identifier.
	 * @param flag MailFlag - the given mail flag.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header
	 */
	public SearchResultDTO<MailHeader> findByFlag(long id, MailFlag flag, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given mail type.
	 * 
	 * @param id long - the given folder identifier.
	 * @param type MailType - the given mail type.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByType(long id, MailType type, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param subject String - the given mail subject.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findBySubject(long id, String subject, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param sender String - the given mail sender.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findBySender(long id, String sender, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param sender String - the given mail sender.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByDate(long id, Date date, int startPos, int maxResult) throws EJBException;	
	
	/**
	 * Finds all mail from the given folder and order by subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByFolderOrderBySubject(long id, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mail from the given folder and order by sent.
	 * 
	 * @param id long - the given folder identifier.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByFolderOrderBySent(long id, int startPos, int maxResult) throws EJBException ;	
	
	/**
	 * Find all mail filters of owner
	 * @return the list of mail filters.
	 * @throws EJBException when error occurs during finding mail filter.
	 */
	public List<MailFilter> findFilterByUser() throws EJBException ;	
	
	/**
	 * Move the mail from current directory to another directory.
	 * 
	 * @param id long - the given mail identifier.
	 * @param folderId long - the given folder identifier.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during moving data.
	 */
	public MailHeader move(long id, long folderId) throws EJBException ;
	
	/**
	 * Delete all the message in folder.
	 * 
	 * @param folderId the given folder identifier.
	 * 
	 * @throws EJBException when error occur deleting messages in folder.
	 */
	void emptyFolder(long folderId) throws EJBException ;		
	
	/**
	 * Save mail header.
	 * 
	 * @param header MailHeader - the given mail header.
	 * @param content byte[] - the given mail content.
	 * @param folderId long - the given folder identifier.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during saving data to database.
	 */
	public MailHeader save(MailHeader header, byte[] content, long folderId) throws EJBException ;
	
	/**
	 * Save mail header from the given {@link MailHeader} instance, mail content and {@link FolderType} instance.
	 * 
	 * @param header the given {@link MailHeader} instance.
	 * @param content the given mail content.
	 * @param folderType the given {@link FolderType} instance.
	 * @return the given {@link MailHeader} instance after saving to database.
	 * 
	 * @throws EJBException when error occurs during saving to database.
	 */
	public MailHeader save(MailHeader header, byte[] content, FolderType folderType) throws EJBException ;
	
	/**
	 * Update mail header
	 * 
	 * @param header MailHeader - the given mail header.
	 * @param content byte[] - the given mail content.
	 * @param body byte[] - the given mail body.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating data to database.
	 */
	public MailHeader update(MailHeader  header, byte[] content) throws EJBException;
	
	/**
	 * Update mail flag.
	 * 
	 * @param id long - the given mail header identifier.
	 * @param flag MailFlag - the given mail flag.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating information.
	 */
	public MailHeader update(long id, MailFlag flag) throws EJBException ;
	
	/**
	 * Update mail type.
	 * 
	 * @param id long - the given mail header identifier.
	 * @param type MailType - the given mail type.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating information.
	 */
	public MailHeader update(long id, MailType type) throws EJBException ;
	
	/**
	 * view body content
	 * @param id Long - - the given mail header identifier.
	 * @return MailHeader the mail header information.
	 * @throws EJBException if an error occurs during load information.
	 */
	public MailHeaderDTO viewBody(long id) throws EJBException;
	
	/**
	 * Load attachment data from mail
	 * @param id
	 * @param fileindex
	 * @return
	 * @throws EJBException
	 */
	public PairValueDTO<byte[]> viewAttachment(long id, String fileindex) throws EJBException;
	
	/**
	 * load all attachments
	 * @param id
	 * @return
	 * @throws EJBException
	 */
	public Map<String, byte[]> viewAttachment(long id) throws EJBException;
	
	/**
	 * get list of String which format SMTPserver:accountSMTP
	 * @return List<String>
	 * @throws EJBException
	 */
	public List<String> getSmtpAccount() throws EJBException;
	
	/**
	 * Get MailConfig of the login user
	 * 
	 * @param userCode the given user code.
	 * @return the {@link PersonMailConfig} instance.
	 * 
	 * @throws EJBException when error occurs during getting the mail configuration.
	 */
	public PersonMailConfig getMailConfig(String userCode) throws EJBException;
	
	/**
	 * Load the user mail configuration.
	 * 
	 * @return the login {@link MailAcctConfigInfo} instance.
	 * @throws EJBException when error occurs during loading {@link MailAcctConfigInfo} information.
	 */
	public MailAcctConfigInfo loadMailConfig() throws EJBException ;
	
	/**
	 * Saves the list follow-up information to database.
	 * 
	 * @param followUps
	 *            List<FollowUpDTO> - the given list follow-up information.
	 * @throws EJBException
	 */
	public List<FollowUpDTO> saveFollowUp(List<FollowUp> followUps) throws EJBException;
	
	/**
	 * update list of follow up information
	 * @param ids  List<FollowUpDTO> - the given identifier follow up
	 * @param date
	 * @return
	 * @throws EJBException
	 */
	public List<FollowUpDTO> updateFollowUp(List<Long> ids, Date date) throws EJBException;
	
}
