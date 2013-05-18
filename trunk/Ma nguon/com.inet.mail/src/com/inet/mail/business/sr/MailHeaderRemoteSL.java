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

import javax.ejb.Remote;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FolderCountDTO;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailFolderEntry;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailType;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;

/**
 * MailHeaderRemoteSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date: Jan 28, 2008
 * <pre>
 *  Initialization MailHeaderRemoteSL class.
 * </pre>
 */
@Remote
public interface MailHeaderRemoteSL extends MailHeaderBase {
	/**
	 * Find the {@link MailFolder} from the given mail identifier.
	 * 
	 * @param id the given mail header identifier.
	 * @param owner the given owner code.
	 * 
	 * @return the {@link MailFolder} instance.
	 * @throws EJBException when error occur during finding {@link MailFolder}.
	 */
	MailFolder findFolderById(long id) throws EJBException ;	
	
	/**
	 * Search by {@link MailHeader} default of owner.
	 * 
	 * @param owner the given user code
	 * @param startPos the given start position
	 * @param maxResult the given end position
	 * 
	 * @return the {@link SearchResultDTO} instance.
	 * @throws EJBException  if an error occurs during searching data.
	 */
	SearchResultDTO<MailHeader> searchByFolderDefault(int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all {@link MailHeader} in the current {@link MailFolder}.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the list of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByFolder(long id, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all read/unread mails header in the current {@link MailFolder}.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param read If <code>true</code> user want to view read mail, otherwise view unread mail.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the list of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding {@link MailHeader}.
	 */
	SearchResultDTO<MailHeader> findByRead(long id, boolean read, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Find all attached/unattached {@link MailFolder} in the current folder.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param attached If <code>true</code> user want to view attached mail, otherwise 
	 * view unattached mail.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding {@link MailHeader}.
	 */
	SearchResultDTO<MailHeader> findByAttached(long id, boolean attached, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all {@link MailHeader} from the given {@link MailPriority}.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param priority the given {@link MailPriority}.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.	 
	 * @return the {@link List} of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding {@link MailHeader}.
	 */
	SearchResultDTO<MailHeader> findByPriority(long id, MailPriority priority, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all {@link MailHeader} from the given {@link MailFlag}.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param flag the given {@link MailFlag}.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader} instance.
	 * @throws EJBException if an error occurs during finding mail header
	 */
	SearchResultDTO<MailHeader> findByFlag(long id, MailFlag flag, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all {@link MailHeader} from the given {@link MailType}.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param type the given {@link MailType}.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the list of {@link MailHeader} instance.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findByType(long id, MailType type, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all {@link MailHeader} from the given subject.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param subject the given mail subject.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<MailHeader> findBySubject(long id, String subject, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all {@link MailHeader} from the given sender.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param sender the given mail sender.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader} instance.
	 * @throws EJBException if an error occurs during finding {@link MailHeader} instance.
	 */
	List<MailHeader> findBySender(long id, String sender, int startPos, int maxResult) throws EJBException ;

	/**
	 * Finds all {@link MailHeader} from the given {@link Date} instance.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param date the given {@link Date} instance.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader} instance.
	 * @throws EJBException if an error occurs during finding {@link MailHeader} instance.
	 */
	List<MailHeader> findByDate(long id, Date date, int startPos, int maxResult) throws EJBException;	
	
	/**
	 * Finds all {@link MailHeader} from the given folder and order by subject.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader} instance.
	 * @throws EJBException if an error occurs during finding {@link MailHeader}.
	 */
	List<MailHeader> findByFolderOrderBySubject(long id, int startPos, int maxResult) throws EJBException ;
	
	/**
	 * Finds all {@link MailHeader} from the given folder and order by sent.
	 * 
	 * @param id the given {@link MailFolder} identifier.
	 * @param startPos the given start position.
	 * @param maxResult the given end position.
	 * 
	 * @return the {@link List} of {@link MailHeader}.
	 * @throws EJBException if an error occurs during finding {@link MailHeader}.
	 */
	List<MailHeader> findByFolderOrderBySent(long id, int startPos, int maxResult) throws EJBException ;	
	
	/**
	 * Count all unread mail items in the given mail folder.
	 * 
	 * @return the {@link List} of {@link FolderCountDTO} instance.
	 * 
	 * @throws EJBException if an error occurs during finding mail header.
	 */
	List<FolderCountDTO> countUnread() throws EJBException ;
	
	/**
	 * Count all mail items in the given mail folder.
	 * 
	 * @return the {@link List} of {@link FolderCountDTO} instance.
	 * @throws EJBException if an error occurs during finding {@link FolderCountDTO} instance.
	 */
	List<FolderCountDTO> countMessage() throws EJBException ;
	
	/**
	 * Get {@link MailFolderEntry} instance.
	 * 
	 * @return the {@link MailFolderEntry} instance.
	 * @throws EJBException if an error occurs during getting {@link MailFolderEntry} instance.
	 */
	MailFolderEntry getFolders() throws EJBException ;
	
	/**
	 * Find UID by folder identifier.
	 * 
	 * @param folderId the given folder identifier.
	 * 
	 * @return the {@link List} of unique identifiers.
	 * @throws EJBException when error occur during finding the UIDs.
	 */
	List<String> findUidByFolder(long folderId) throws EJBException ;	
	
	/**
	 * Find UID by account.
	 * 
	 * @param account the given account information.
	 * 
	 * @return the {@link List} of unique identifiers.
	 * @throws EJBException when error occur during finding the UIDs.
	 */
	List<String> findUidByAccount(String account) throws EJBException ;

	/**
	 * Delete the mail from the given list of UIDs.
	 * 
	 * @param uids the given list of UIDs.
	 * 
	 * @throws EJBException when error occurs during deleting UIDs.
	 */
	void emptyFolder(List<String> uids) throws EJBException ;	
	
	/**
	 * Delete the mail from the given list of UIDs.
	 * 
	 * @param account the given login user account.
	 * @param uids the given list of UIDs.
	 * 
	 * @throws EJBException when error occurs during deleting UIDs.
	 */
	void deleteByUid(String account, List<String> uids) throws EJBException ;
	
	/**
	 * Delete all none received mail from the given folder identifier.
	 * 
	 * @param folderId the given folder identifier.
	 * 
	 * @throws EJBException when error occurs during deleting none received mail.
	 */
	void deleteNoneReceivedMail(long folderId) throws EJBException ;	
}
