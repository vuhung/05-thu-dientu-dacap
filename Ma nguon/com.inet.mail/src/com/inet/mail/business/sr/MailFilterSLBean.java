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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import org.hibernate.Criteria;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.StringService;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.core.RuleFilterManager;
import com.inet.mail.persistence.MailFilter;

/**
 * MailFilterSLBean
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 25, 2008
 * <pre>
 *  Initialization MailFilterSLBean class.
 * </pre>
 */
@TransactionManagement(value=TransactionManagementType.CONTAINER)
@TransactionAttribute(value=TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class MailFilterSLBean extends BaseMailBusinessBean<MailFilter>
		implements MailFilterSL, MailFilterRemoteSL {
	//~ Methods ===============================================================
	/**
	 * @see com.inet.mail.business.sr.MailFilterBase#findByMailFolder(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findByMailFolder(long folderId) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailFilter.findByMailFolder") ;
		
		// set query parameter.
		query.setParameter("id", folderId) ;
		
		// execute and return result.
		return load(query);
	}
	
	/**
	 * @see com.inet.mail.business.sr.MailFilterBase#findAll()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findAll() throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailFilter.findAll") ;
		
		// execute and return result.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#save(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailFilter save(MailFilter biz) throws EJBException {
		String code = getUserCode() ;
		if(StringService.hasLength(code)){
			RuleFilterManager.remove(code) ;
		}
		
		// save data.
		return super.save(biz);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#update(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailFilter update(MailFilter biz) throws EJBException {
		String code = getUserCode() ;
		if(StringService.hasLength(code)){
			RuleFilterManager.remove(code) ;
		}
		
		// update data.
		return super.update(biz);
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusiness#delete(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		// get user code.
		String code = getUserCode() ;
		if(StringService.hasLength(code)){
			RuleFilterManager.remove(code) ;
		}
		
		remove(bizId, MailFilter.class) ;
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusiness#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFilter load(long bizId) throws EJBException {
		return load(bizId, MailFilter.class);
	}
	
	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected Criteria buildQuery(MailFilter search) throws EJBException {
		throw new EJBException("This function has not been implemented yet.");
	}

	/** 
	 * @see com.inet.mail.business.sr.MailFilterBase#findByUser(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findByUser(String owner) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailFilter.findByUser") ;
		// set parameter
		query.setParameter("owner", owner);
		// execute and return result.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFilterRemoteSL#findByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findByUser() throws EJBException {
		return findByUser(getUserCode());
	}
}
