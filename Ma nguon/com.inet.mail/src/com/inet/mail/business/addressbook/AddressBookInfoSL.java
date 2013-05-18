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

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.persistence.AddressBookInfo;

/**
 * AddressBookInfoSL.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Local
public interface AddressBookInfoSL extends BaseMailBusiness<AddressBookInfo>{

	/**
	 * Get all contact information in current system.
	 * 
	 * @param userId long - the given user identifier.
	 * @return List<AddressBookInfo> - the list of contact information.
	 * 
	 * @throws EJBException when errors occur during getting contact data.
	 */
	public List<AddressBookInfo> getAllUserContact(long userId) throws EJBException;
	
	/**
	 * Find contact from given email address
	 * 
	 * @param userId long - the identify of user who retrieve data
	 * @param email String - the email address of contact
	 * @return AddressBookInfo - the contact information
	 * @throws EJBException when error occur during finding data
	 */
	AddressBookInfo findByEmail(long userId, String email) throws EJBException;
	
	/**
	 * search contact of user
	 * 
	 * @param userID long - the given user identify
	 * @param keyword String - the key for search
	 * @return List<AddressBookInfo> - the list of contact information
	 * @throws EJBException when error occur during searching contact
	 */
	public List<AddressBookInfo> searchContact(long userID, String keyword) throws EJBException;
	
	/**
	 * search contact of user
	 * 
	 * @param userID long - the given user identify
	 * @param keyword String - the key for search
	 * @param groupName String - the name of group
	 * @return List<AddressBookInfo> - the list of contact information
	 * @throws EJBException when error occur during searching contact
	 */
	public List<AddressBookInfo> searchContact(long userID, String keyword, String groupName) throws EJBException;
	
	/**
	 * get group contact of user
	 * 
	 * @param userID long - the given user identify
	 * @return List<String> - the list of group name
	 * @throws EJBException when error occur during searching contact
	 */
	public List<String> getAllGroup(long userId) throws EJBException;
	
	/**
	 * Get all address book information
	 * 
	 * @returnList<AddressBookInfo> - the list of address book information
	 * @throws EJBException when error occur during searching contact
	 */
	public List<AddressBookInfo> getAll() throws EJBException;
		
}
