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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.persistence.MailFilter;

/**
 * MailFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 25, 2008
 * <pre>
 *  Initialization MailFilter class.
 * </pre>
 */
public interface MailFilterBase extends BaseMailBusiness<MailFilter> {
	/**
	 * Find all mail filters from the given folder identifier.
	 * 
	 * @param folderId long - the given folder identifier.
	 * @return the list of mail filters.
	 * @throws EJBException if an error occurs during finding mail filter.
	 */
	List<MailFilter> findByMailFolder(long folderId) throws EJBException ;
	
	/**
	 * Find all mail filters in current system.
	 * 
	 * @return the list of mail filters.
	 * @throws EJBException when error occurs during finding mail filter.
	 */
	List<MailFilter> findAll() throws EJBException ;	
}
