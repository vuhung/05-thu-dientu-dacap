/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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

import java.util.ArrayList;
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
import com.inet.mail.persistence.MailSignatureInfo;

/**
 * MailSignatureInfoSLBean.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class MailSignatureInfoSLBean extends
		BaseMailBusinessBean<MailSignatureInfo> implements
		MailSignatureInfoRemoteSL, MailSignatureInfoSL {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	@Override
	protected Criteria buildQuery(MailSignatureInfo search) throws EJBException {
		throw new EJBException("This function is not implemented.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		remove(bizId, MailSignatureInfo.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailSignatureInfo load(long bizId) throws EJBException {
		return load(bizId, MailSignatureInfo.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailSignatureInfoBase#query(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailSignatureInfo> query(String owner) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailSignatureInfo.findByUser") ;
		
		// set query parameter.
		query.setParameter("owner", owner) ;
		
		// execute query and return data.
		return load(query) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailSignatureInfoRemoteSL#query()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailSignatureInfo> query() throws EJBException {
		return query(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailSignatureInfoBase#update(long, char)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailSignatureInfo update(long bizId, char currentUsed)
			throws EJBException {
		// get signature information.
		MailSignatureInfo item = load(bizId);
		
		// update data.
		if (item != null){
			item.setCurrentUsed(currentUsed);
			item = update(item);
		}
		
		return item;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailSignatureInfoBase#update(java.util.List, java.util.List, java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<MailSignatureInfo> update(List<MailSignatureInfo> adds, List<MailSignatureInfo> updates, List<Long> removes) 
		throws EJBException {
		// create result list.
		List<MailSignatureInfo> result = new ArrayList<MailSignatureInfo>();

		// add new 
		if(adds != null){
			for(MailSignatureInfo item : adds){
				result.add(save(item));
			}
		}
		
		// update 
		if(updates != null){
			for(MailSignatureInfo item : updates){
				result.add(update(item));
			}
		}
		
		// delete
		if(removes != null){
			for(long id : removes){
				delete(id);
			}
		}
		
		// return result.
		return result;
	}
}
