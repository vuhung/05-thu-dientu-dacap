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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import org.hibernate.Criteria;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.persistence.AddressBookInfo;

/**
 * AddressBookInfoSLBean.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@TransactionManagement(value=TransactionManagementType.CONTAINER)
@TransactionAttribute(value=TransactionAttributeType.NOT_SUPPORTED)
public @Stateless class AddressBookInfoSLBean extends BaseMailBusinessBean<AddressBookInfo> 
									implements AddressBookInfoSL, AddressBookRemoteSL {

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	protected Criteria buildQuery(AddressBookInfo search) throws EJBException {
		throw new EJBException("This method is not supported.");
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(int)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		try{
			super.remove(bizId, AddressBookInfo.class);
		}catch(EJBException ex){
			this.getSessionContext().setRollbackOnly() ;
			
			throw ex ;
		}
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(int)
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public AddressBookInfo load(long bizId) throws EJBException {		
		return super.load(bizId, AddressBookInfo.class);
	}
	
	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#save(java.lang.Object)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public AddressBookInfo save(AddressBookInfo abInfo) throws EJBException {	
		AddressBookInfo oldABInfo = findByEmail(abInfo.getOwner(), abInfo.getEmailAddress());
		if(oldABInfo != null) {
			throw new EJBException("Duplicate data info.");
		}
		try{
			return super.insert(abInfo);
		}catch(EJBException ex){
			this.getSessionContext().setRollbackOnly() ;
			
			throw ex ;
		}
	}
	
	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#update(java.lang.Object)
	 */
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public AddressBookInfo update(AddressBookInfo abInfo) throws EJBException {
		AddressBookInfo oldABInfo = findByEmail(abInfo.getOwner(), abInfo.getEmailAddress());
		if(oldABInfo != null && !abInfo.getId().equals(oldABInfo.getId())) {
			throw new EJBException("Duplicate data info.");
		}
		try{
			return super.update(abInfo);
		}catch(EJBException ex){
			this.getSessionContext().setRollbackOnly() ;
			
			throw ex ;
		}
	}
	
	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#findByEmail(long, java.lang.String)
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public AddressBookInfo findByEmail(long userId, String email)
			throws EJBException {
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.getAddressBookInfo");
		query.setParameter("owner", userId);
		query.setParameter("emailAddress", email);
		return super.loadSingle(query);
	}

	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#getAllUserContact(long)
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<AddressBookInfo> getAllUserContact(long userId) throws EJBException {
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.getAllUserContact");
		query.setParameter("owner", userId);
		return super.load(query);
	}

	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#searchContact(int, java.lang.String)
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<AddressBookInfo> searchContact(long userID, String keyword) throws EJBException {
		String searchKey = "%" + keyword + "%";
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.searchContact");
		query.setParameter("owner", userID);
		query.setParameter("fullName", searchKey);
		query.setParameter("email", searchKey);
		return super.load(query);
	}

	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#getAllGroup(int)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<String> getAllGroup(long userId) throws EJBException {
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.getAllGroup");
		query.setParameter("owner", userId);
		return query.getResultList();
	}

	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#searchContact(int, java.lang.String, java.lang.String)
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<AddressBookInfo> searchContact(long userID, String keyword, String groupName) throws EJBException {
		String searchKey = "%" + keyword + "%";
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.searchContactWithGroup");
		query.setParameter("owner", userID);
		query.setParameter("groupName", groupName);
		query.setParameter("fullName", searchKey);
		query.setParameter("email", searchKey);
		return super.load(query);
	}

	/**
	 * @see com.inet.mail.business.addressbook.AddressBookInfoSL#getAll()
	 */
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<AddressBookInfo> getAll() throws EJBException {
		Query query = getEntityManager().createNamedQuery("AddressBookInfo.getAll");
		return super.load(query);
	}

}
