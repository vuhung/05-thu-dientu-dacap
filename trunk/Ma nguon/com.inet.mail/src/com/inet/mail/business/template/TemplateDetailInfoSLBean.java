/*****************************************************************
 Copyright 2007 by hiennguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.business.template;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.Criteria;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.core.CacheManager;
import com.inet.mail.core.TemplateEntry;
import com.inet.mail.data.TemplateStatus;
import com.inet.mail.data.TemplateType;
import com.inet.mail.persistence.TemplateDetailInfo;
import com.inet.mail.util.MailService;

/**
 * TemplateDetailInfoSLBean.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public @Stateless
class TemplateDetailInfoSLBean extends BaseMailBusinessBean<TemplateDetailInfo>
		implements TemplateDetailInfoSL, TemplateDetailInfoRemoteSL {

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	protected Criteria buildQuery(TemplateDetailInfo search)
			throws EJBException {
		throw new EJBException("Ths function is not implemented.");
	}

	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	@Override
	public void delete(long bizId) throws EJBException {
		TemplateDetailInfo template = load(bizId);
		if (template != null)
		{
			// remove from cache as well
			if (template.getStatus() == TemplateStatus.APPROVAL){
				String keyCache = MailService.createTicket(template.getId().toString(), template.getName());
				CacheManager.getInstance().removeItem(keyCache);
			}
			
			remove(template);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#save(java.lang.Object)
	 */
	@Override
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public TemplateDetailInfo save(TemplateDetailInfo template) throws EJBException
	{
		// save this template information first
		template = super.save(template);
		
		// save into cache if the status is approval
		if (template.getStatus() == TemplateStatus.APPROVAL)
		{
			String keyCache = MailService.createTicket(template.getId().toString(), template.getName());
			CacheManager.getInstance().setItem(keyCache,new TemplateEntry(template.getTemplate(),template.getFormat()));
		}
		
		return template;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#update(java.lang.Object)
	 */
	@Override
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public TemplateDetailInfo update(TemplateDetailInfo template) throws EJBException
	{
		template = super.update(template);
		// save into cache if the status is approval
		if (template.getStatus() == TemplateStatus.APPROVAL)
		{
			String keyCache = MailService.createTicket(template.getId().toString(), template.getName());
			CacheManager.getInstance().setItem(keyCache,new TemplateEntry(template.getTemplate(),template.getFormat()));
		}
		
		return template;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.template.TemplateDetailInfoBase#update(java.lang.Long, com.inet.mail.data.TemplateStatus)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public TemplateDetailInfo update(Long bizId, TemplateStatus status)
			throws EJBException {
		TemplateDetailInfo template = load(bizId);
		if (template != null)
		{
			template.setStatus(status);
			return update(template);
		}
		
		return template;
	}
	
	/**
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public TemplateDetailInfo load(long bizId) throws EJBException {
		return load(bizId, TemplateDetailInfo.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see
	 * com.inet.mail.business.template.TemplateDetailInfoBase#query(java.lang
	 * .String, com.inet.mail.data.TemplateStatus)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<TemplateDetailInfo> query(String own, TemplateStatus status)
			throws EJBException {
		return getEntityManager().createNamedQuery(
				"TemplateDetailInfo.queryByOwnerStatus").setParameter("owner",
				own).setParameter("status", status).getResultList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see
	 * com.inet.mail.business.template.TemplateDetailInfoBase#query(java.lang
	 * .String)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<TemplateDetailInfo> query(String own) throws EJBException {
		return getEntityManager().createNamedQuery(
				"TemplateDetailInfo.queryByOwner").setParameter("owner", own)
				.getResultList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.template.TemplateDetailInfoBase#query()
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<TemplateDetailInfo> query() throws EJBException {
		return getEntityManager().createNamedQuery(
				"TemplateDetailInfo.queryByType").setParameter("type",
				TemplateType.SYSTEM).getResultList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see
	 * com.inet.mail.business.template.TemplateDetailInfoBase#query(com.inet
	 * .mail.data.TemplateStatus)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<TemplateDetailInfo> query(TemplateStatus status)
			throws EJBException {
		return getEntityManager().createNamedQuery(
				"TemplateDetailInfo.queryByTypeStatus").setParameter("status",
				status).setParameter("type", TemplateType.SYSTEM)
				.getResultList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see
	 * com.inet.mail.business.template.TemplateDetailInfoBase#queryAll(com.inet
	 * .mail.data.TemplateStatus)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<TemplateDetailInfo> queryAll(TemplateStatus status)
			throws EJBException {
		return getEntityManager().createNamedQuery(
				"TemplateDetailInfo.queryAllByStatus").setParameter("status",
				status).getResultList();
	}
}
