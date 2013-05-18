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
package com.inet.web.bo.config;

import java.util.List;

import com.inet.mail.data.MailReceiverObject;
import com.inet.web.bf.config.MailAcctConfigInfoBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.exception.WebOSException;

/**
 * MailAcctConfigInfoBO.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAcctConfigInfoBO extends AbstractWebOSBO<MailAcctConfigInfoBF> {

	/**
	 * Create <tt>MailAcctConfigInfoBO</tt> instance
	 * 
	 * @param businessFacade MailAcctConfigInfoBF - the mail account configuration BF
	 */
	public MailAcctConfigInfoBO(MailAcctConfigInfoBF mailAcctConfigInfoBF) {
		super(mailAcctConfigInfoBF);		
	}

	/**
	 * add new mail configuration
	 * 
	 * @param userCode String - the user code
	 * @param name String - the display name
	 * @param receiverObject MailReceiverObject - the receiver object
	 * @throws WebOSException - when there is an error happens
	 */
	public void addConfig(String userCode, String name, MailReceiverObject receiverObject) throws WebOSException{
		try {
			this.getBusinessFacade().getFacade().createConfig(userCode, name, receiverObject);
		} catch (Exception ex) {
			throw new WebOSException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * delete mail configuration
	 * 
	 * @param userCode String - the user code
	 * @param emails List<String> - the list of email address
	 * @throws WebOSException - when there is an error happens
	 */
	public void deleteConfig(String userCode, List<String> emails) throws WebOSException {
		try {
			this.getBusinessFacade().getFacade().delete(userCode, emails);
		} catch (Exception ex) {
			throw new WebOSException(ex.getMessage(), ex);
		}
		
	}
	
	/**
	 * active/inactive account
	 * 
	 * @param userCode String - the user code
	 * @param email String - the email address
	 * @param active boolean - active or inactive account
	 * @throws WebOSException - when there is an error happens
	 */
	public void active(String userCode, String email, boolean active) throws WebOSException {
		try {
			this.getBusinessFacade().getFacade().activeAccount(userCode, email, active);
		} catch (Exception ex) {
			throw new WebOSException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * change password form email
	 * 
	 * @param userCode String - the user code
	 * @param emails List<String> - the list of email addresses
	 * @param password String - the new password
	 * @throws WebOSException - when there is an error happens
	 */
	public void changePassword(String userCode, List<String> emails, String password) throws WebOSException {
		try {
			this.getBusinessFacade().getFacade().changePassword(userCode, emails, password);
		} catch (Exception ex) {
			throw new WebOSException(ex.getMessage(), ex);
		}
	}
	
	
}
