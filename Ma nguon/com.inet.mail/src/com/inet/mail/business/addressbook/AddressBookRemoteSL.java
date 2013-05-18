/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.mail.business.addressbook;

import java.util.List;

import javax.ejb.Remote;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.persistence.AddressBookInfo;

/**
 * AddressBookRemoteSL.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Remote
public interface AddressBookRemoteSL extends BaseMailBusiness<AddressBookInfo>{
	
	/**
	 * get list contact of user
	 * @param userId
	 * @return
	 * @throws EJBException
	 */
	public List<AddressBookInfo> getAllUserContact(long userId) throws EJBException;
	
	/**
	 * search contact of user
	 * @param userID
	 * @param keyword
	 * @return
	 * @throws EJBException
	 */
	public List<AddressBookInfo> searchContact(long userID, String keyword) throws EJBException;
	
	/**
	 * search contact of user
	 * @param userID
	 * @param keyword
	 * @param groupName
	 * @return
	 * @throws EJBException
	 */
	public List<AddressBookInfo> searchContact(long userID, String keyword, String groupName) throws EJBException;
	
	/**
	 * get group contact of user
	 * @param userId
	 * @return
	 * @throws EJBException
	 */
	public List<String> getAllGroup(long userId) throws EJBException;
	
	/**
	 * Get all AddressBookInfo
	 * @return list AddressBookInfo
	 * @throws EJBException
	 */
	public List<AddressBookInfo> getAll() throws EJBException;
		
}
