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

import javax.ejb.Remote;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FolderType;
import com.inet.mail.persistence.MailFolder;

/**
 * MailFolderRemoteSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 25, 2008
 * <pre>
 *  Initialization MailFolderRemoteSL class.
 * </pre>
 */
@Remote
public interface MailFolderRemoteSL extends MailFolderBase {
	/**
	 * Find {@link List} of {@link MailFolder}.
	 * 
	 * @return the {@link List} of {@link MailFolder}.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	List<MailFolder> findByUser() throws EJBException ;
	
	/**
	 * Find {@link MailFolder} by {@link FolderType} instance.
	 * 
	 * @param type the given {@link FolderType}.
	 * 
	 * @return the {@link List} of {@link MailFolder}.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	List<MailFolder> findByType(FolderType type) throws EJBException ;
	
	/**
	 * Find {@link MailFolder} by parent identifier.
	 * 
	 * @param parentId the given parent identifier
	 * @return the {@link List} of {@link MailFolder} instances.
	 * 
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	List<MailFolder> findByParentId(long parentId) throws EJBException ;
	
	/**
	 * Find root mail folder from the given user code.
	 * 
	 * @return the root {@link MailFolder} instance.
	 * @throws EJBException if an error occurs during finding root {@link MailFolder}.
	 */
	MailFolder findRootByUser() throws EJBException ;
	
	/**
	 * Find mail folder from the given folder identifier.
	 * 
	 * @param id the given folder identifier.
	 * 
	 * @return the {@link MailFolder} information.
	 * @throws EJBException if an error occurs during finding {@link MailFolder}.
	 */
	MailFolder findById(long id) throws EJBException ;	
}
