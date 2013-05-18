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

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FolderType;
import com.inet.mail.persistence.MailFolder;

/**
 * MailFolderSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date: Jan 25, 2008
 * <pre>
 *  Initialization MailFolderSL class.
 * </pre>
 */
@Local
public interface MailFolderSL extends MailFolderBase {
	/**
	 * Find {@link List} of {@link MailFolder} from the given user code.
	 * 
	 * @param code the given user code.
	 * 
	 * @return the {@link List} of {@link MailFolder}.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	List<MailFolder> findByUser(String code) throws EJBException ;
	
	/**
	 * Find {@link MailFolder} by {@link FolderType}.
	 * 
	 * @param code the given user code.
	 * @param type the given {@link FolderType}.
	 * 
	 * @return the {@link List} of {@link MailFolder}.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	List<MailFolder> findByType(String code, FolderType type) throws EJBException ;
	
	/**
	 * Find {@link List} of {@link MailFolder} by parent identifier.
	 * 
	 * @param code the given user code.
	 * @param parentId the given parent identifier.
	 * 
	 * @return the {@link List} of {@link MailFolder} instance.
	 * @throws EJBException if an error occurs during finding mail folder.
	 */
	List<MailFolder> findByParentId(String code, long parentId) throws EJBException ;
	
	/**
	 * Find root {@link MailFolder} from the given user code.
	 * 
	 * @param code the given user code.
	 * @return the root {@link MailFolder}.
	 * @throws EJBException if an error occurs during finding root {@link MailFolder}.
	 */
	MailFolder findRootByUser(String code) throws EJBException ;
	
	/**
	 * Find {@link MailFolder} from the given folder identifier.
	 * 
	 * @param code the user code.
	 * @param id the given folder identifier.
	 * @return the {@link MailFolder} information.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	MailFolder findById(String code, long id) throws EJBException ;	
	
	/**
	 * Get default folder is existing or not....
	 * 
	 * @param author the given folder.
	 * @return if creating folder successfully.
	 */
	public boolean countDefaultFolder(String author);
	
	/**
	 * 
	 * @param author
	 * @param index
	 * @return
	 * @throws EJBException
	 */
	public MailFolder getDefaultFolder(String author, long index) throws EJBException;
	
	/**
	 * 
	 * @param author
	 * @param index
	 * @return
	 * @throws EJBException
	 */
	public List<MailFolder> queryDefaultFolder(String author) throws EJBException;
}
