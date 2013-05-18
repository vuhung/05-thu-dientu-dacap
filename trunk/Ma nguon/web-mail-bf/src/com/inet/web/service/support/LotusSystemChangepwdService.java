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
package com.inet.web.service.support;

import java.util.List;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.DigestService;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * LotusSystemChangepwdService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LotusSystemChangepwdService extends AbstractService implements ChangePasswdService {
	protected INetLogger logger = INetLogger.getLogger(LotusSystemChangepwdService.class);
	/**
	 * The hash method when user change password
	 */
	protected String hashMethod;
	/**
	 * Change password service instance.
	 */
	private ChangePasswdService changePasswdService ;
	
	/**
	 * List service need update password
	 */
	private List<ChangePasswdService> updatepwServices;
	
	/**
	 * Create LotusSystemChangepwdService instance from the given <tt>ChangePasswdService</tt> instance.
	 * 
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 */
	public LotusSystemChangepwdService(ChangePasswdService changePasswdService) {
		this.changePasswdService = changePasswdService;
		this.hashMethod = DigestService.HASH_METHOD_MD5;
	}
	
	/**
	 * Create LotusSystemChangepwdService instance from the given <tt>ChangePasswdService</tt> instance.
	 * 
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 * @param hashMethod the given hash method {SHA, SSHA, MD5, SMD5,CRYPT}.
	 */
	public LotusSystemChangepwdService(ChangePasswdService changePasswdService, String hashMethod) {
		this.changePasswdService = changePasswdService;
		this.hashMethod = hashMethod;
	}
	
	/**
	 * Create LotusSystemChangepwdService instance from the given 
	 * <tt>List<ChangePasswdService></tt> instance : update the password.	
	 * <tt>ChangePasswdService</tt> instance : change the password.
	 * 
	 * @param updatepwServices List<ChangePasswdService> - the given <tt>List<ChangePasswdService></tt> instance.
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 */
	public LotusSystemChangepwdService(List<ChangePasswdService> updatepwServices,
			ChangePasswdService changePasswdService) {
		this.updatepwServices 	 = updatepwServices;
		this.changePasswdService = changePasswdService;
		this.hashMethod = DigestService.HASH_METHOD_MD5;
	}
	
	/**
	 * Create LotusSystemChangepwdService instance from the given 
	 * <tt>List<ChangePasswdService></tt> instance : update the password.	
	 * <tt>ChangePasswdService</tt> instance : change the password.
	 * 
	 * @param updatepwServices List<ChangePasswdService> - the given <tt>List<ChangePasswdService></tt> instance.
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 * @param hashMethod the given hash method {SHA, SSHA, MD5, SMD5,CRYPT}.
	 */
	public LotusSystemChangepwdService(List<ChangePasswdService> updatepwServices,
			ChangePasswdService changePasswdService, String hashMethod) {
		this.updatepwServices 	 = updatepwServices;
		this.changePasswdService = changePasswdService;
		this.hashMethod = hashMethod;
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String)
	 */
	public void changePassword(String oldPwd, String newPwd) throws WebOSException {
		// encrypt password
		newPwd = DigestService.getPassword(hashMethod, newPwd);
		
		try {
			// update new password
			this.updateNewPassword(oldPwd, newPwd);
			
			// change password
			this.changePasswdService.changePassword(oldPwd, newPwd);
		} catch (WebOSException ex) {
			logger.error("Error while changing password", ex);
		}
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
	 */
	public void resetPassword(String userName, String newPwd) throws WebOSException {
		// encrypt password
		newPwd = DigestService.getPassword(hashMethod, newPwd);	
		
		try {
			this.updateResetPassword(userName, newPwd);
			
			// reset password
			this.changePasswdService.resetPassword(userName, newPwd);
		} catch (WebOSException ex) {
			logger.error("Error while changing password", ex);
		}
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
	 */
	public boolean verifyPassword(String oldPwd) throws WebOSException {
		return this.changePasswdService.verifyPassword(oldPwd);
	}
	
	/**
	 * Update all password of service which used user lotus for login 
	 * 
	 * @param oldPasswd : the given old password of service
	 * @param newPasswd : the given new password of service
	 */
	private void updateNewPassword(String oldPasswd, String newPasswd) throws WebOSException {	
		if (updatepwServices != null){
			for(ChangePasswdService updatePW:  updatepwServices){
				updatePW.changePassword(oldPasswd, newPasswd);
			}
		}
	}
	
	/**
	 * Update all password of service which used user lotus for login 
	 * 
	 * @param userName {@link String} : the given user name
	 * @param newPasswd {@link String} : the given new password of service
	 */
	private void updateResetPassword(String userName, String newPasswd) throws WebOSException {	
		if (updatepwServices != null){
			for(ChangePasswdService updatePW:  updatepwServices){
				updatePW.resetPassword(userName, newPasswd);
			}
		}
	}

}
