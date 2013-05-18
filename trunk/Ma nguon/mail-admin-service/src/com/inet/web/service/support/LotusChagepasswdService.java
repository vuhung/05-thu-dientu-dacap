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
package com.inet.web.service.support;

import com.inet.base.service.Assert;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.auth.AuthenticateManager;
import com.inet.lotus.auth.LotusAuthenticateException;
import com.inet.lotus.core.LotusException;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * LotusChagepwService.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class LotusChagepasswdService extends AbstractService implements ChangePasswdService{

	//~ Instance fields =======================================================
	/* authenticate manager. */
	protected AuthenticateManager authenticateManager ;
	/* account manager. */
	protected AccountManager<?> accountManager ;
	
	///~ Constructors ==========================================================
	/**
	 * Create a new LotusChagepwService instance from the given {@link AccountManager} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 */
	public LotusChagepasswdService(AccountManager<?> accountManager, AuthenticateManager authenticateManager) {
		Assert.isNotNull(accountManager, "AccountManager must be not null.") ;
		Assert.isNotNull(authenticateManager, "AuthenticateManager must be not null.") ;
		this.authenticateManager = authenticateManager ;
		this.accountManager = accountManager ;
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
	 */
	public boolean verifyPassword(String password) throws WebOSException {
		try{
			String userName = this.getUserName();
			if (authenticateManager.authenticate(userName, password) == null) {
				return false;
			}
		}catch(LotusAuthenticateException laex){
			return false;
		}
		return true;
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String)
	 */
	public void changePassword(String oldPassword, String newPassword)
			throws WebOSException {
		try{
			String userName = this.getUserName();
			this.accountManager.changePassword(userName, oldPassword, newPassword);
		} catch(LotusException ex){
			throw new WebOSException("Change password fail "+ ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
	 */
	public void resetPassword(String userName, String newPassword) throws WebOSException {
		try{
			this.accountManager.resetPassword(userName, newPassword);
		} catch(LotusException ex){
			throw new WebOSException("Reset password fail "+ ex.getMessage(), ex);
		}
	}
}
