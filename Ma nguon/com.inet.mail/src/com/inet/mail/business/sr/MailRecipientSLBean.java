/*****************************************************************
   Copyright 2009 by Truong Ngoc Tan (tntan@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

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

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.StringService;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.persistence.RecipientSender;

/**
 * MailRecipientSLBean.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 0.2i
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public class MailRecipientSLBean extends BaseMailBusinessBean<RecipientSender> 
			implements MailRecipientBase, MailRecipientRemoteSL{

	/**
	 * {@inheritDoc} 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		remove(bizId, RecipientSender.class);
	}

	/**
	 * {@inheritDoc} 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public RecipientSender load(long bizId) throws EJBException {
		return load(bizId, RecipientSender.class);
	}

	/**
	 * {@inheritDoc} 
	 * @see com.inet.mail.business.sr.MailRecipientBase#add(com.inet.mail.persistence.RecipientSender)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long add(RecipientSender recipient) throws EJBException {
		// 1. check email is exist
		if(recipient.getOwner() == null || recipient.getEmail() == null){
			throw new EJBException("The owner or email should not null");
		}
		RecipientSender sender = this.findByEmail(recipient.getOwner(), recipient.getEmail());
		
		// 2. add or update
		if(sender != null){
			if(!recipient.getRecipient().equals(sender.getRecipient())){
				if(StringService.hasLength(recipient.getRecipient())){
					// update sender
					sender.setRecipient(recipient.getRecipient());
					return this.update(sender).getId();					
				}
			}
			
		}else{
			// add new
			return this.save(recipient).getId();
		}
		
		return 0;
	}

	/**
	 * {@inheritDoc} 
	 * @see com.inet.mail.business.sr.MailRecipientBase#findByOwner(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<RecipientSender> findByOwner(String owner) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("RecipientSender.findByOwner") ;
		
		// set query parameter.
		query.setParameter("owner", owner) ;
		
		// execute query and return data.
		return load(query) ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.inet.mail.business.sr.MailRecipientBase#add(java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(List<RecipientSender> recipients) throws EJBException {
		for(RecipientSender recipient: recipients){
			this.add(recipient);
		}
	}
	/**
	 * find by email
	 * @param owner - the given user code 
	 * @param email - the given email address
	 * @return RecipientSender
	 * @throws EJBException 
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private RecipientSender findByEmail(String owner, String email) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("RecipientSender.findByEmail") ;
		
		// set query parameter.
		query.setParameter("owner", owner) ;
		query.setParameter("email", email);
		
		// execute query and return data.
		return loadSingle(query) ;
	}
}
