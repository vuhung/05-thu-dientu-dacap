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
package com.inet.web.bo.addressbook;

import java.util.List;

import com.inet.addressbook.data.IContactInfo;
import com.inet.authenticate.exception.AddressBookException;
import com.inet.web.bf.addressbook.AddressBookManagementBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;

public class AddressBookManagementBO  extends AbstractWebOSBO<AddressBookManagementBF>{
	/**
	 * Address book management BO constructor
	 * @param businessFacade
	 */
	protected AddressBookManagementBO(AddressBookManagementBF businessFacade) {
		super(businessFacade);
	}

	/**
	 * search contact user ins address book
	 * @param userId - long the given user login identifier
	 * @param keyword - String the given keyword 
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException if an error occurs during loading contact user
	 */
	public List<? extends IContactInfo> searchPersonalContact(long userId, String keyword)  throws WebOSBOException {
		try {
			return this.getBusinessFacade().getFacade().searchUserContact(userId, keyword);
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load user contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * search contact user in LDAP
	 * @param keyword - String the given keyword 
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException if an error occurs during loading contact user
	 */
	public List<? extends IContactInfo> searchGlobalContact(String keyword)  throws WebOSBOException {
		try {
			return this.getBusinessFacade().getFacade().searchContact(keyword) ;
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load user contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Get global address
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException if an error occurs during loading contact user
	 */
	public List<? extends IContactInfo> getAllContact() throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().getAllContact();
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load user contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Get all group
	 * @return List<String>
	 * @throws WebOSBOException when error occurs during load group contact
	 */
	public List<String> getAllGroup() throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().getAllGroup();
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load group contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 *  Insert new contact
	 * @param contact IContactInfo
	 * @return IContactInfo
	 * @throws WebOSBOException
	 */
	public IContactInfo insert(IContactInfo contact) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().insert(contact);
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while add new contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Insert new list of contact
	 * @param contacts List<IContactInfo>
	 * @return String
	 * @throws WebOSBOException
	 */
	public String insert(List<IContactInfo> contacts) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().insert(contacts);
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while add new list of contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Delete given contact
	 * @param contact IContactInfo
	 * @throws WebOSBOException
	 */
	public void delete(IContactInfo contact) throws WebOSBOException{
		try{
			this.getBusinessFacade().getFacade().delete(contact);
		} catch (AddressBookException abEx) {
			throw new WebOSBOException("error while delete contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * update given contact
	 * @param contact IContactInfo
	 * @return IContactInfo
	 * @throws WebOSBOException
	 */
	public IContactInfo update(IContactInfo contact) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().update(contact);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while update contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Get all user contact
	 * @param userId long
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException
	 */
	public List<? extends IContactInfo> getAllUserContact(long userId) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().getAllUserContact(userId);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load all user contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Search user contact
	 * @param userId long
	 * @param key String
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException
	 */
	public List<? extends IContactInfo> searchUserContact(long userId, String key) throws WebOSBOException{
		try{
			return this.getBusinessFacade().getFacade().searchUserContact(userId, key);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load given user contact",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	/**
	 * search user contact
	 * @param key String 
	 * @param groupName
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException
	 */
	public List<? extends IContactInfo> searchContact( String key, String groupName) throws WebOSBOException{
		try{
			return this.getBusinessFacade().getFacade().searchContact(key, groupName);
		}catch(AddressBookException abEx){
			throw new WebOSBOException ("error while load given user contact", abEx);
		}catch (Exception ex){
			throw translateException(ex);
		}
	}
	/**
	 * search user contact in given group
	 * @param userId long
	 * @param searchKey String
	 * @param groupName String
	 * @return List<? extends IContactInfo>
	 * @throws WebOSBOException
	 */
	public List<? extends IContactInfo> searchUserContact(long userId, String searchKey, String groupName)
		throws WebOSBOException{
		try{
			return this.getBusinessFacade().getFacade().searchUserContact(userId, searchKey, groupName);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load given user contact in group",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * get all group of contact
	 * @param userId long
	 * @return List<String>
	 * @throws WebOSBOException
	 */
	public List<String> getAllGroup(long userId) throws WebOSBOException{
		try{
			return this.getBusinessFacade().getFacade().getAllGroup();
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load all group",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Load contact info with email
	 * @param email String
	 * @return IContactInfo
	 * @throws WebOSBOException
	 */
	public IContactInfo findByEmail(String email) throws WebOSBOException{
		try{
			return this.getBusinessFacade().getFacade().findByEmail(email);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load contact info by email",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Load contact info with email
	 * @param id
	 * @return
	 * @throws WebOSBOException
	 */
	public IContactInfo load(long id) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().load(id);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load contact info by id",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * Load contact info by email and id
	 * @param id
	 * @param email
	 * @return
	 * @throws WebOSBOException
	 */
	public IContactInfo findByEmail(long id, String email) throws WebOSBOException {
		try{
			return this.getBusinessFacade().getFacade().findByEmail(id, email);
		}catch (AddressBookException abEx) {
			throw new WebOSBOException("error while load contact info by id and email",abEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}
}
