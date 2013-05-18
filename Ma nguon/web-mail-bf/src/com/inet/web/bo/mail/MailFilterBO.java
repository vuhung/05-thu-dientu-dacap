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
package com.inet.web.bo.mail;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.persistence.MailFilter;
import com.inet.web.bf.mail.MailFilterBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.exception.WebOSException;

/**
 * MailFilterBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailFilterBO extends AbstractWebOSBO<MailFilterBF>{

	/**
	 * MailFilterBO constructor
	 * @param businessFacade
	 */
	protected MailFilterBO(MailFilterBF businessFacade) {
		super(businessFacade);
	}
	
	/**
	 * find mail filter by user
	 * 
	 * @param userCode the given user code
	 * @return List<MailFilter>
	 * @throws WebOSBOException if an error occurs during find mail filter.
	 */
	public List<MailFilter> findByUser() throws WebOSBOException{
		try {
			return this.getBusinessFacade().getFacade().findByUser();
		}catch (EJBException ejbEX) {
			throw new WebOSBOException("error while search mail filter" , ejbEX );
		}catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * delete mail filter from mail filter identifier
	 * @param id the given mail filter identifier
	 * @throws WebOSException if an error occurs during delete mail filter
	 */
	public void delete(long id) throws WebOSException{
		try {
			this.getBusinessFacade().getFacade().delete(id);
		}catch (EJBException ejbEX) {
			throw new WebOSBOException("error while delete mail filter" , ejbEX );
		}catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * save mail filter from mail filter
	 * @param filter - the given mail filter need to save
	 * @return MailFilter - the mail filter after save
	 * @throws WebOSException if an error occurs during save mail filter
	 */
	public MailFilter save(MailFilter filter) throws WebOSException{
		try {
			return this.getBusinessFacade().getFacade().save(filter);
		}catch (EJBException ejbEX) {
			throw new WebOSBOException("error while save mail filter" , ejbEX );
		}catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * update mail filter from mail filter
	 * @param filter - the given mail filter need to update
	 * @return MailFilter - the mail filter after update
	 * @throws WebOSException if an error occurs during update mail filter
	 */
	public MailFilter update(MailFilter filter) throws WebOSException{
		try {
			return this.getBusinessFacade().getFacade().update(filter);
		}catch (EJBException ejbEX) {
			throw new WebOSBOException("error while update mail filter" , ejbEX );
		}catch (Exception ex) {
			throw translateException(ex);
		}
	}
	
	/**
	 * load mail filter from given identifier mail filter
	 * @param id long -the given identifier mail filter
	 * @return MailFilter
	 * @throws WebOSException if an error occurs during load mai filter.
	 */
	public MailFilter load(long id) throws WebOSException{
		try {
			return this.getBusinessFacade().getFacade().load(id);
		}catch (EJBException ejbEX) {
			throw new WebOSBOException("error while load mail filter" , ejbEX );
		}catch (Exception ex) {
			throw translateException(ex);
		}
	}
}
