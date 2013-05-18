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
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.web.application.exception.ApplicationServerException;
import com.inet.web.bf.mail.MailConfigureBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;

/**
 * MailConfigureBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailConfigureBO extends AbstractWebOSBO<MailConfigureBF>{

	/**
	 * MailConfigureBO constructor
	 * @param businessFacade MailConfigureBF
	 */
	protected MailConfigureBO(MailConfigureBF businessFacade) {
		super(businessFacade);
	}
	
	/**
	 * find mail configure from given user code
	 * @param owner String user code
	 * @return MailConfigure
	 * @throws WebOSBOException if an error occurs during find mail configure.
	 */
	public MailAcctConfigInfo findByUser() throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().findByUser();
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while find mail configure",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * save mail configure
	 * @param configure
	 * @return
	 * @throws WebOSBOException
	 */
	public MailAcctConfigInfo save(MailAcctConfigInfo configure) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().save(configure);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while save mail configure",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * update mail configure
	 * @param configure
	 * @return
	 * @throws WebOSBOException
	 */
	public MailAcctConfigInfo update(MailAcctConfigInfo configure) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().update(configure);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while save mail configure",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * change password for the given list of e-mails 
	 * @param userCode String - the given user code
	 * @param emails - List of email which need change password
	 * @param password - new password
	 * @return Mail
	 * @throws WebOSBOException if errors occurs during change password
	 */
	public MailAcctConfigInfo changePassword(List<String> emails, String password) throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().changePassword(emails, password);
		}  catch (ApplicationServerException asEx) {
			throw new WebOSBOException("error while change password for list email",asEx);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("error while change password for list email",ejbEx);
		}catch(Exception ex){
			throw translateException(ex) ;
		}
	}
}
