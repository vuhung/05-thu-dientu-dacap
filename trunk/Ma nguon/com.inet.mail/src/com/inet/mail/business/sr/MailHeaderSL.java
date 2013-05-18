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

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FolderCountDTO;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailFolderEntry;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailType;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;

/**
 * MailHeaderSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date: Jan 28, 2008
 * <pre>
 *  Initialization MailHeaderSL class.
 * </pre>
 */
@Local
public interface MailHeaderSL extends MailHeaderBase {
	/**
	 * 
	 * @param expired
	 * @throws EJBException
	 */
	public int cacheExpired(Date expired) throws EJBException;
	
	/**
	 * Find the {@link MailFolder} from the given owner and mail identifier.
	 * 
	 * @param id the given mail header identifier.
	 * @param owner the given owner code.
	 * 
	 * @return the {@link MailFolder} instance.
	 * @throws EJBException when error occur during finding {@link MailFolder}.
	 */
	MailFolder findFolderById(long id, String owner) throws EJBException ;	
	
	/**
	 * Search by folder default of owner.
	 * @param owner  String - the given user code
	 * @param startPos - the given start position
	 * @param maxResult - the given end position
	 * @return the search result DTO.
	 * @throws EJBException  if an error occurs during searching data.
	 */
	SearchResultDTO<MailHeader> searchByFolderDefault(String owner, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all mails in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail headers.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByFolder(long id, String owner, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all read/unread mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param read boolean - If <code>true</code> user want to view read mail, otherwise view unread mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	SearchResultDTO<MailHeader> findByRead(long id, String owner, boolean read, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all attached/unattached mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param attached boolean - If <code>true</code> user want to view attached mail, otherwise 
	 * view unattached mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	SearchResultDTO<MailHeader> findByAttached(long id, String owner, boolean attached, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given priority.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param priority MailPriority - the given mail priority.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.	 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	SearchResultDTO<MailHeader> findByPriority(long id, String owner, MailPriority priority, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given mail flag.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param flag MailFlag - the given mail flag.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header
	 */
	SearchResultDTO<MailHeader> findByFlag(long id, String owner, MailFlag flag, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given mail type.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param type MailType - the given mail type.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByType(long id, String owner, MailType type, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param subject String - the given mail subject.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findBySubject(long id, String owner,String subject, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param sender String - the given mail sender.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findBySender(long id, String owner,String sender, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all mails from the given subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param sender String - the given mail sender.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByDate(long id, String owner,Date date, int startPos, int maxResult) throws EJBException;	
	
	/**
	 * Finds all mail from the given folder and order by subject.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByFolderOrderBySubject(long id, String owner, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all mail from the given folder and order by sent.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByFolderOrderBySent(long id, String owner, int startPos, int maxResult) throws EJBException ;	
	
	/**
	 * Count all unread mail items in the given mail folder.
	 * 
	 * @param id long - the given mail folder identifier.
	 * @param owner String - the given user code.
	 * @return the size of unread mail item.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<FolderCountDTO> countUnread(String owner) throws EJBException ;
	
	/**
	 * Count all mail items in the given mail folder.
	 * 
	 * @param id long - the given mail folder identifier.
	 * @param owner String - the given user code.
	 * @return the size of mail item in current folder.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<FolderCountDTO> countMessage(String owner) throws EJBException ;
	
	/**
	 * Get mail folder.
	 * 
	 * @param owner String - the given mail folder owner.
	 * @return the mail folder entry.
	 * @throws EJBException if an error occurs during getting mail folder.
	 */
	MailFolderEntry getFolders(String owner) throws EJBException ;
	
	/**
	 * Find UID by folder identifier.
	 * 
	 * @param usercode the given login user code.
	 * @param folderId the given folder identifier.
	 * 
	 * @return the list of unique identifiers.
	 * @throws EJBException when error occur during finding the UIDs.
	 */
	List<String> findUidByFolder(String usercode, long folderId) throws EJBException ;	
	
	/**
	 * Find UID by account.
	 * 
	 * @param usercode the given login user code.
	 * @param account the given account information.
	 * 
	 * @return the list of unique identifiers.
	 * @throws EJBException when error occur during finding the UIDs.
	 */
	List<String> findUidByAccount(String usercode, String account) throws EJBException ;

	/**
	 * Delete the mail from the given list of UIDs.
	 * 
	 * @param usercode the given login user code.
	 * @param uids the given list of UIDs.
	 * @throws EJBException when error occurs during deleting UIDs.
	 */
	void emptyFolder(String usercode, List<String> uids) throws EJBException ;	
	
	/**
	 * Delete the mail from the given list of UIDs.
	 * 
	 * @param usercode the given login user code.
	 * @param account the given login user account.
	 * @param uids the given list of UIDs.
	 * @throws EJBException when error occurs during deleting UIDs.
	 */
	void deleteByUid(String usercode, String account, List<String> uids) throws EJBException ;
	
	/**
	 * Delete all none received mail from the given folder identifier.
	 * 
	 * @param usercode the given owner user code.
	 * @param folderId the given folder identifier.
	 * @throws EJBException when error occurs during deleting none received mail.
	 */
	void deleteNoneReceivedMail(String usercode, long folderId) throws EJBException ;
	
	/**
	 * Find all {@link MailFilter} of owner
	 * 
	 * @param owner the given user code. 
	 * 
	 * @return the {@link List} of {@link MailFilter} instance.
	 * @throws EJBException when error occurs during finding {@link MailFilter}.
	 */
	List<MailFilter> findFilterByUser(String owner) throws EJBException ;
}
