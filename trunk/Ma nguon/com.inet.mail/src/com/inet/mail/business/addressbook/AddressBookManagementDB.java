/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

   Licensed under the iNet SolutioAddressBookRemoteSLns Corp.,;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.inet.addressbook.PersonalAddressBookManagement;
import com.inet.addressbook.data.ContactInfo;
import com.inet.addressbook.data.IContactInfo;
import com.inet.authenticate.exception.AddressBookException;
import com.inet.base.ejb.exception.EJBException;
import com.inet.base.exception.BaseException;
import com.inet.mail.persistence.AddressBookInfo;

/**
 * AddressBookManagementDB.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
public class AddressBookManagementDB implements PersonalAddressBookManagement, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 620265134048993933L;

	//The session context
	private SessionContext ctx; 
	
	// the user transaction.
	private UserTransaction userTransaction ;
	
	//local bean addressbook
	private AddressBookInfoSL addressBookInfoSL = null;
		
	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#delete(com.inet.addressbook.data.IContactInfo)
	 */
	public void delete(IContactInfo contactInfo) throws AddressBookException {
		try {
			// begin transaction.
			this.beginTransaction() ;
			
			this.initBean();
			
			this.addressBookInfoSL.delete(contactInfo.getId());
			
			// commit database.
			this.commit() ;
		} catch (NamingException ex) {			
			// rollback data.
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		}		
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#getAllGroup(long)
	 */
	public List<String> getAllGroup(long userId) throws AddressBookException {
		try {
			// initialization the state-less bean.
			this.initBean();
			
			// get all group.
			return this.addressBookInfoSL.getAllGroup(userId);
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#getAllUserContact(int)
	 */
	public List<? extends IContactInfo> getAllUserContact(long userId)
			throws AddressBookException {
		try {
			// initialization the state-less bean.
			this.initBean();
			// retrieved data from server.
			List<AddressBookInfo> resultList = this.addressBookInfoSL.getAllUserContact(userId);			
			// return the contact information.
			return this.convertListToContact(resultList);
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#insert(com.inet.addressbook.data.IContactInfo)
	 */
	public IContactInfo insert(IContactInfo contactInfo) throws AddressBookException {
		try {
			// begin transaction.
			this.beginTransaction() ;
			
			// initialization state-less bean.
			this.initBean();			
			
			// convert to address book information.
			AddressBookInfo abInfo = this.converToBean(contactInfo);
			
			// save to database.
			AddressBookInfo newabInfo = this.addressBookInfoSL.save(abInfo);
			
			// commit to database.
			this.commit() ;
			
			// return the contact.
			return this.convertToContact(newabInfo);
		} catch (NamingException ex) {
			// rollback data.
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			// rollback data.
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#insert(java.util.List)
	 */
	public String insert(List<IContactInfo> contacts) throws AddressBookException {
		//Error message string
		StringBuffer ex = new StringBuffer("");
		
		try {
			// begin the transaction.
			this.beginTransaction() ;
			
			// initialization bean.
			this.initBean();
			
			for(IContactInfo contact : contacts) {
				try{
					AddressBookInfo abInfo = converToBean(contact);
					this.addressBookInfoSL.save(abInfo);
				}catch (EJBException ejbex) {
					//Add error to buffer
					ex.append(ejbex.getMessage());
				}	
			}
			
			// commit data transaction.
			this.commit() ;
		} catch (NamingException nex) {
			// rollback the transaction.
			this.rollback() ;
			
			throw new AddressBookException(nex.getMessage(), nex);
		}
		
		//if error exists, return string exception, otherwise return emty string
		return ex.toString();
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#searchUserContact(long, java.lang.String)
	 */
	public List<? extends IContactInfo> searchUserContact(long userId, String keyword)
			throws AddressBookException {
		try {
			// initialization bean.
			this.initBean();
			
			// get and convert data.
			return this.convertListToContact(this.addressBookInfoSL.searchContact(userId, keyword));
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		} 
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#findByEmail(long, java.lang.String)
	 */
	public IContactInfo findByEmail(long userId, String email)
			throws AddressBookException {
		try {
			// initialization bean.
			this.initBean();
			
			// get and convert data.
			return this.convertToContact(this.addressBookInfoSL.findByEmail(userId, email));
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#load(long)
	 */
	public IContactInfo load(long adId) throws AddressBookException {
		try {
			// initialization bean.
			this.initBean();
			
			// get and convert data.
			return this.convertToContact(this.addressBookInfoSL.load(adId));
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#searchUserContact(long, java.lang.String, java.lang.String)
	 */
	public List<? extends IContactInfo> searchUserContact(long userId, String searchKey,
			String group) throws AddressBookException {
		try {
			// initialization bean.
			this.initBean();
			
			// get and convert data.
			return this.convertListToContact(
					this.addressBookInfoSL.searchContact(
							userId, 
							searchKey, 
							group
						)
					);
		} catch (NamingException ex) {			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * @see com.inet.addressbook.PersonalAddressBookManagement#update(com.inet.addressbook.data.IContactInfo)
	 */
	public IContactInfo update(IContactInfo contact) throws AddressBookException {
		try {
			// initialization bean.
			this.initBean();
			
			// covert to address book information.
			AddressBookInfo abInfo = this.converToBean(contact);
			
			// update the contact information.
			AddressBookInfo addressBookInfo = this.addressBookInfoSL.update(abInfo) ;
			
			// commit to database.
			this.commit() ;
			
			// convert to contact information.
			return this.convertToContact(addressBookInfo);
		} catch (NamingException ex) {	
			// rollback data.
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		} catch (EJBException ex) {
			// roolback data.
			this.rollback() ;
			
			throw new AddressBookException(ex.getMessage(), ex);
		}
	}

	/**
	 * Transfer the context of ejb
	 * @see com.inet.authenticate.manager.AuthenticateManager#setContext(java.lang.Object)
	 */
	public void setContext(Object ctx) throws BaseException {
		this.ctx = (SessionContext)ctx; 
		
		// get user transaction.
		this.userTransaction = this.ctx.getUserTransaction() ;	
	}
	
	/**
	 * @see com.inet.base.IDisposable#dispose()
	 */
	public void dispose() {
	}
	
	/**
	 * Roll-back transaction.
	 */
	public void rollback(){
		try {
			if(this.userTransaction != null) this.userTransaction.rollback() ;
		} catch (IllegalStateException ex) {
		} catch (SecurityException e) {
		} catch (SystemException e) {}
	}
	
	/**
	 * Begin transaction.
	 */
	public void beginTransaction(){
		try {
			if(this.userTransaction != null) this.userTransaction.begin() ;
		} catch (NotSupportedException e) {
		} catch (SystemException e) {}
	}
	
	/**
	 * Commit transaction.
	 */
	public void commit(){
		try {
			if(this.userTransaction != null) this.userTransaction.commit() ;
		} catch (SecurityException e) {
		} catch (IllegalStateException e) {
		} catch (RollbackException e) {
		} catch (HeuristicMixedException e) {
		} catch (HeuristicRollbackException e) {
		} catch (SystemException e) {}
	}	
	
	////////////////////////////////////////////////////////////////////////////////////
	// Helper functions.
	//
	/**
	 * Initialization bean.
	 * 
	 * @throws NamingException
	 */
	private void initBean() throws NamingException {
		if(this.addressBookInfoSL == null) {
			this.addressBookInfoSL = (AddressBookInfoSL) this.ctx.lookup("AddressBookInfoSLBean/local");
		}
	}
	
	/**
	 * Convert from AddressBookInfo to IContactInfo
	 * 
	 * @param addressBookInfo : the given address info will be convert
	 * @return
	 */
	private IContactInfo convertToContact(AddressBookInfo addressBookInfo) {
		if(addressBookInfo == null) {
			return null;
		}
		IContactInfo contact = new ContactInfo();
		contact.setCity(addressBookInfo.getCity());
		contact.setDepartment(addressBookInfo.getDepartment());
		contact.setDisplayName(addressBookInfo.getDisplayName());
		contact.setEmailAddress(addressBookInfo.getEmailAddress());
		contact.setFaxNumber(addressBookInfo.getFaxNumber());
		contact.setFirstName(addressBookInfo.getFirstName());
		contact.setFullName(addressBookInfo.getFullName());
		contact.setGroupName(addressBookInfo.getGroupName());
		contact.setHomeNumber(addressBookInfo.getHomeNumber());
		contact.setId(addressBookInfo.getId());
		contact.setLastName(addressBookInfo.getLastName());
		contact.setMobileNumber(addressBookInfo.getMobileNumber());
		contact.setOwner(addressBookInfo.getOwner());
		contact.setPagerNumber(addressBookInfo.getPagerNumber());
		contact.setPostalAddress(addressBookInfo.getPostalAddress());
		contact.setWorkNumber(addressBookInfo.getWorkNumber());
		return contact;
	}
	
	/**
	 * Convert a list AdderssBookInfo to a list IContactInfo
	 * 
	 * @param addressBookList : the given list address book info
	 * @return
	 */
	private List<IContactInfo> convertListToContact(List<AddressBookInfo> addressBookList) {
		if(addressBookList == null) {
			return null;
		}
		
		List<IContactInfo> list = new ArrayList<IContactInfo>();
		for(AddressBookInfo abInfo : addressBookList) {
			list.add(convertToContact(abInfo));
		}
		return list;
	}
	
	/**
	 * Convert from IContactInfo to AddressBookInfo
	 * 
	 * @param contact : the given IContactInfos
	 * @return
	 */
	private AddressBookInfo converToBean(IContactInfo contact) {
		AddressBookInfo abInfo = new AddressBookInfo();
		abInfo.setCity(contact.getCity());
		abInfo.setDepartment(contact.getDepartment());
		abInfo.setDisplayName(contact.getDisplayName());
		abInfo.setEmailAddress(contact.getEmailAddress());
		abInfo.setFaxNumber(contact.getFaxNumber());
		abInfo.setFirstName(contact.getFirstName());
		abInfo.setFullName(contact.getFullName());
		abInfo.setGroupName(contact.getGroupName());
		abInfo.setHomeNumber(contact.getHomeNumber());
		if(contact.getId() != 0) {
			abInfo.setId(contact.getId());
		}		
		abInfo.setLastName(contact.getLastName());
		abInfo.setMobileNumber(contact.getMobileNumber());
		abInfo.setOwner(contact.getOwner());
		abInfo.setPagerNumber(contact.getPagerNumber());
		abInfo.setPostalAddress(contact.getPostalAddress());
		abInfo.setWorkNumber(contact.getWorkNumber());
		return abInfo;
	}

}
