/*****************************************************************
   Copyright 2008 by Tung Luong (lqtung@truthinet.com.vn)

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
package com.inet.web.controller.bean.passwd;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.web.core.struts.bean.AbstractBean;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;

/**
 * ChangePasswdBean.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class WebOSChangePasswdBean extends AbstractBean {
	// the class logger.
	private static final INetLogger logger = INetLogger.getLogger(WebOSChangePasswdBean.class) ;
	
	// the password bean name.
	private static final String PASSWD_BEAN_NAME = "passwd" ;
	
	/**
	 * the action parameter.
	 */
	private static final String ACTION_PARAM = "iwebos-action" ;
	
	/**
	 * The update or change the password action
	 */
	private static final String ACTION_UPDATE = "update" ;
	
	/**
	 * The old password parameter
	 */
	private static final String OLD_PASSWORD_PARAM = "old_password" ;
	
	/**
	 * The new password parameter
	 */
	private static final String NEW_PASSWORD_PARAM = "new_password" ;
	
	/**
	 * The confirm new password parameter
	 */
	private static final String CFNEW_PASSWORD_PARAM = "cf_new_password" ;

	/**
	 * Change password service instance.
	 */
	private ChangePasswdService changePasswdService ;
	
	/**
	 * List service need update password
	 */
	private List<ChangePasswdService> updatepwServices;
	
	/**
	 * Create WebOSChangePasswdBean instance from the given <tt>ChangePasswdService</tt> instance.
	 * 
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 */
	public WebOSChangePasswdBean(ChangePasswdService changePasswdService) {
		this.changePasswdService = changePasswdService ;
	}
	
	/**
	 * Create WebOSChangePasswdBean instance from the given 
	 * <tt>List<ChangePasswdService></tt> instance : update the password.	
	 * <tt>ChangePasswdService</tt> instance : change the password.
	 * @param updatepwServices List<ChangePasswdService> - the given <tt>List<ChangePasswdService></tt> instance.
	 * @param changePasswdService ChangePasswdService - the given <tt>ChangePasswdService</tt> instance.
	 */
	public WebOSChangePasswdBean( List<ChangePasswdService> updatepwServices, ChangePasswdService changePasswdService) {
		this.updatepwServices 	 = updatepwServices;
		this.changePasswdService = changePasswdService ;
	}
	
	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Object execute(HttpServletRequest request, HttpServletResponse response) throws WebOSException {
		// get action parameter.
		String action = (String)request.getParameter(ACTION_PARAM);
		
		//Check the action
		if (ACTION_UPDATE.equals(action)){
			String oldPasswd = (String)request.getParameter(OLD_PASSWORD_PARAM);
			String newPasswd = (String)request.getParameter(NEW_PASSWORD_PARAM);
			String cfnewPasswd = (String)request.getParameter(CFNEW_PASSWORD_PARAM);
			logger.debug("The old password is ["+oldPasswd+"] and new password is ["+ newPasswd +"] ["+cfnewPasswd+"]");
			
			//check password before change
			if (StringService.hasLength(oldPasswd) &&
					StringService.hasLength(newPasswd) 
					&& newPasswd.equals(cfnewPasswd)
					&& this.changePasswdService.verifyPassword(oldPasswd)){
				try{
					//update the new password 
					this.updateNewPassword(oldPasswd, newPasswd);
					
					//change password
					this.changePasswdService.changePassword(oldPasswd, newPasswd);
					
					// change password successful
					return true;
				} catch(WebOSException ex){
					logger.error("****ERROR when change the password ", ex);
				}
			}
			
			//fail when change the password
			return false;
		}
		
		//do nothing
		return null;
	}
		
	/**
	 * Update all password of service which used user lotus for login 
	 * 
	 * @param oldPasswd : the given old password of service
	 * @param newPasswd : the given new password of service
	 */
	private void updateNewPassword(String oldPasswd, String newPasswd) throws WebOSException{	
		if (updatepwServices != null){
			for(ChangePasswdService updatePW:  updatepwServices){
				updatePW.changePassword(oldPasswd, newPasswd);
			}
		}
	}

	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#getName()
	 */
	public String getName() {
		return PASSWD_BEAN_NAME;
	}
}
