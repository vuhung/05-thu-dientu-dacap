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

import java.util.List;
import java.util.Map;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailType;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailHeader;

/**
 * MailHeaderBase
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 27, 2008
 * <pre>
 *  Initialization MailHeaderBase class.
 * </pre>
 */
public interface MailHeaderBase extends BaseMailBusiness<MailHeader> {
	/**
	 * Update mail flag.
	 * 
	 * @param id long - the given mail header identifier.
	 * @param flag MailFlag - the given mail flag.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating information.
	 */
	MailHeader update(long id, MailFlag flag) throws EJBException ;
	
	/**
	 * Update mail type.
	 * 
	 * @param id long - the given mail header identifier.
	 * @param type MailType - the given mail type.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating information.
	 */
	MailHeader update(long id, MailType type) throws EJBException ;
	
	/**
	 * Save mail header.
	 * 
	 * @param header MailHeader - the given mail header.
	 * @param content byte[] - the given mail content.
	 * @param folderId long - the given folder identifier.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during saving data to database.
	 */
	MailHeader save(MailHeader header, byte[] content, long folderId) throws EJBException ;
	
	/**
	 * Update mail header
	 * 
	 * @param header MailHeader - the given mail header.
	 * @param content byte[] - the given mail content.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during updating data to database.
	 */
	MailHeader update(MailHeader  header, byte[] content) throws EJBException;
	
	/**
	 * Move the mail from current directory to another directory.
	 * 
	 * @param id long - the given mail identifier.
	 * @param folderId long - the given folder identifier.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during moving data.
	 */
	MailHeader move(long id, long folderId) throws EJBException ;
	
	/**
	 * Move all email from currentId to another folder
	 * @param currentId - the give current folder identifier.
	 * @param folderId - the give destination folder identifier.
	 * @throws EJBException 
	 */
	void move2Folder(long currentId, long folderId) throws EJBException;
	
	/**
	 * Move the mail from INBOX folder to another directory.
	 * 
	 * @param id long - the given mail identifier.
	 * @param folderId long - the given folder identifier.
	 * @return the mail header information.
	 * @throws EJBException if an error occurs during moving data.
	 */
	MailHeader moveInbox2Folder(long id, long folderId) throws EJBException ;	
	
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
	 * @param maxItem the given max items to get.
	 * 
	 * @return the search result.
	 * @throws EJBException when error occurs during searching data.
	 */
	SearchResultDTO<MailHeader> search(String criteria, int startAt, int maxItem) throws EJBException ;	
	
	/**
	 * Get content from the given content identifier.
	 * 
	 * @param contentId String - the given content identifier.
	 * @return the mail content data.
	 * @throws EJBException if an error occurs during getting content data.
	 */
	byte[] getComposer(String composeId) throws EJBException ;
	
	/**
	 * Get default configuration.
	 * 
	 * @return the list of mail configuration.
	 * @throws EJBException if an error occurs during getting default configuration.
	 */
	Map<String, String> getDefaultConfigure() throws EJBException ;
	
	/**
	 * Find all {@link MailFilter} of owner
	 * 
	 * @return the {@link List} of {@link MailFilter} instance.
	 * @throws EJBException when error occurs during finding {@link MailFilter}.
	 */
	List<MailFilter> findFilterByUser() throws EJBException ;
	
	/**
	 * mark as read from the given folder identifier.
	 *  
	 * @param folderId the given folder identifier.
	 * @throws EJBException when error occurs during mark as read folder.
	 */
	void markAsReadFolder(long folderId) throws  EJBException;
	
	/**
	 * Get total current using from account
	 * @param owner the given user code  
	 * @param account the given account SMTP
	 * @return 
	 * @throws EJBException
	 */
	long getCurrentUsing(String owner, String account) throws EJBException;
}
