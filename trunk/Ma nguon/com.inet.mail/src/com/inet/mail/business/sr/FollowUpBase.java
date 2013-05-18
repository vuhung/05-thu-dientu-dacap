/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.persistence.FollowUp;

/**
 * FollowUpBase.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public interface FollowUpBase extends BaseMailBusiness<FollowUp>{
	/**
	 * Load all follow up from given user code 
	 * @return List<FollowUpDTO> List of follow up
	 * @throws EJBException if an error occurs during load object.
	 */
	public List<FollowUpDTO> loadAll() throws EJBException;
	
	/**
	 * update follow up information
	 * @param id  Long - the given identifier follow up
	 * @param date
	 * @return
	 * @throws EJBException
	 */
	public FollowUp update(long id, Date date) throws EJBException;
	
	/**
	 * delete follow up from given identifier header
	 * @param id Long - the given identifier mail header
	 * @throws EJBException if an error occurs during delete follow up
	 */
	public void deleteByData(long id) throws EJBException;
	
	/**
	 * delete follow up from given UID mail header 
	 * @param uids List<String> - the given list of UID
	 * @throws EJBException if an error occurs during delete follow up
	 */
	public void deleteByUids(List<String> uids) throws EJBException;
	
	/**
	 * delete list of follow ups
	 * @param ids List<Long> - the given list of identifier follow up
	 * @throws EJBException if an error occurs during delete list of follow up
	 */
	public void delete(List<Long> ids) throws EJBException;
}
