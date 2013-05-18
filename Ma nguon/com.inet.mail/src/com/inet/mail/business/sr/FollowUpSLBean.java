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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.persistence.FollowUp;

/**
 * FollowUpSLBean
 *
 * @author <a href="mailto:tntan@truthinet.com">Tan Truong</a>
 * @version 1.0i
 * 
 * <pre>
 *  Initialization FollowUpSLBean class.
 * </pre>
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class FollowUpSLBean extends BaseMailBusinessBean<FollowUp> implements FollowUpSL, FollowUpRemoteSL{

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		this.remove(bizId, FollowUp.class);
	}
	
	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public FollowUp load(long bizId) throws EJBException {
		return this.load(bizId, FollowUp.class);
	}

	/**
	 * @see com.inet.mail.business.sr.FollowUpBase#loadAll()
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<FollowUpDTO> loadAll() throws EJBException {
		String userCode = getUserCode();
		//Create name query
		Query query = this.getEntityManager().createNamedQuery("FollowUp.loadAll");
		
		//Set the condition
		query.setParameter("userCode", userCode);
		
		try{
			// execute the query and return the result.
			return (List<FollowUpDTO>)query.getResultList();
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not load all follow up", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not load all follow up", ex) ;
		}
	}

	/**
	 * @see com.inet.mail.business.sr.FollowUpBase#update(long, java.util.Date)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public FollowUp update(long id, Date date) throws EJBException {
		FollowUp followUp = this.load(id);
		if(followUp != null){
			followUp.setDate(date);
			followUp = super.update(followUp);
		}
		return followUp;
	}

	/**
	 * @see com.inet.mail.business.sr.FollowUpBase#deleteByData(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void deleteByData(long id) throws EJBException {
		try{
			//Create name query
			Query query = this.getEntityManager().createNamedQuery("FollowUp.deleteByData");
			
			//Set the condition
			query.setParameter("id", id);
			
			//delete follow-up items
			query.executeUpdate();
		}catch(Exception ex){
			throw new EJBException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.mail.business.sr.FollowUpBase#delete(java.util.List)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void delete(List<Long> ids) throws EJBException {
		for(Long id : ids ){
			this.delete(id);
		}
	}

	/**
	 * @see com.inet.mail.business.sr.FollowUpBase#deleteByUids(java.util.List)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void deleteByUids(List<String> uids) throws EJBException {
		try{
			//Create name query
			Query query = this.getEntityManager().createNamedQuery("FollowUp.deleteByUID");
			query.setParameter("uids", uids);
			query.executeUpdate();
		}catch(Exception ex){
			throw new EJBException(ex.getMessage(), ex) ;
		}
	}
}
