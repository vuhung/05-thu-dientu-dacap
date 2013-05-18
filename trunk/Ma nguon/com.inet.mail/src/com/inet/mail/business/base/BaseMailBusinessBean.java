/*****************************************************************
 Copyright 2006 by hvnguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.business.base;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import com.inet.base.ejb.business.BaseSessionBean;
import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.account.manage.ldap.LotusLdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.mail.core.cache.util.AccountCacheHelper;

/**
 * BaseOutlookBusinessBean.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 * @param <T>
 */
@TransactionManagement(value=TransactionManagementType.CONTAINER)
@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
public abstract class BaseMailBusinessBean<T> extends BaseSessionBean<Long,T> {	
	//~ Instance fields =======================================================
	/* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	//~ Methods ===============================================================
	/**
	 * Set EntityManager.
	 * 
	 * @param entityManager
	 */
	@PersistenceContext(name="mail-ejb")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * get user code in the context
	 * @return String
	 * @throws EJBException
	 */
	protected String getUserCode() throws EJBException{
		return this.getUserCode(getUsername());
	}
	
	/**
	 * get user code from given user name
	 * 
	 * @param username the given user name
	 * 
	 * @return the user code; never null.
	 * @throws EJBException when error occurs during getting user code.
	 */
	protected String getUserCode(String username) throws EJBException{
		// hold account information.
		Account account = null ;
		
		// get account from cache.
		try{
			account = AccountCacheHelper.get(username) ;
		}catch(CacheSupportException csex){
			logger.warn("Could not get account information from cache, message: [" + csex.getMessage() + "]") ;
		}

		// lookup account on LDAP and put to cache.
		if(account == null){
			MailApplicationContext applicationContext = MailApplicationContext.getInstance() ;
			if(applicationContext != null){
				try{
					// get account manager.
					AccountManager<LdapAccount> accountManager = 
						applicationContext.getObject("accountManager", LotusLdapAccountManager.class) ;
					
					// get account information.
					account = accountManager.findByName(username) ;
					
					// put account to cache.
					if(account != null){
						AccountCacheHelper.put(account) ;
					}
				}catch(LotusException lex){
					logger.warn("Could not lookup account information from LDAP, message: [" + lex.getMessage() + "]") ;
				}catch(CacheSupportException csex){
					logger.warn("Could not put account information to cache, message: [" + csex.getMessage() + "]") ;
				}catch(Exception ex){
					logger.warn("Error occurs during finding account, message: [" + ex.getMessage() + "]") ;
				}
			}
		}
		
		if(account == null){
			throw new EJBException("the user has not login or not applied security context, please relogin and try again.") ;
		}
		
		// return user code.
		return account.getCode() ;
	}
	
	/**
	 * Save the entity bean to database.
	 * 
	 * @param biz T - the given entity bean.
	 * @throws EJBException
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public T save(T biz) throws EJBException {
		try{
			return super.insert(biz);
		}catch(EJBException ex){
			this.getSessionContext().setRollbackOnly() ;
			
			throw ex ;
		}		
	}

	/**
	 * @see com.inet.outlook.business.base.BaseSessionBean#update(java.lang.Object)
	 */
	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public T update(T biz) throws EJBException {
		try{
			return super.update(biz);
		}catch(EJBException ex){
			this.getSessionContext().setRollbackOnly() ;
			
			throw ex ;
		}	
	}

	/**
	 * Delete the entity bean from the given identifier.
	 * 
	 * @param bizId	long - the given entity identifier.
	 * @throws EjbException
	 */
	public abstract void delete(long bizId) throws EJBException;

	/**
	 * Load entity bean from the given entity identifier.
	 * 
	 * @param bizId	long -the given entity identifier.
	 * @return T - the entity bean.
	 * @throws EjbException 
	 */
	public abstract T load(long bizId) throws EJBException;

	/**
	 * Search and result the list of entity that match the given criteria.
	 * 
	 * @param search	T - the given criteria.
	 * @return List<T> - list of entity.
	 * @throws EjbException
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<T> query(T search) throws EJBException {
		return buildQuery(search).list();
	}
	
	/**
	 * Search and result the list of entity that match the given criteria.
	 * 
	 * @param search	T - the given criteria.
	 * @param startAt 	int - the given start item position.
	 * @param maxItem	int - the given max items.
	 * @return List<T> - list of entity.
	 * @throws EjbException
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public List<T> query(T search, int startAt, int maxItems) throws EJBException {
		// create criteria.
		Criteria criteria = buildQuery(search) ;
		
		// set start item position.
		criteria.setFirstResult(startAt) ;
		
		// set the expected max items.
		criteria.setMaxResults(maxItems) ;
		
		return criteria.list();
	}	
	
	/**
	 * Creates the criteria from the given entity information.
	 * 
	 * @param search T - the given entity information.
	 * @return Criteria.
	 * @throws EjbException
	 */
	protected Criteria buildQuery(T search) throws EJBException{
		throw new EJBException("This function has not been implemented yet.");
	}
	
	/**
	 * @return the current login user.
	 */
	protected String getUsername(){
		String username = super.getUsername() ;
		if(!StringService.hasLength(username)){
			throw new IllegalStateException("The context is not authenticated.") ;
		}
		
		return username ;
	}
}
