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

import com.inet.base.service.Assert;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.org.permission.manage.UserManager;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * LotusOrgChagepwdService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LotusOrgChagepwdService extends AbstractService implements
		ChangePasswdService {
	/* user manager. */
	protected UserManager<?> userManager ;

	/**
	 * Create a new LotusOrgChagepwdService instance from the given {@link UserManager} instance.
	 * 
	 * @param userManager the given {@link UserManager} instance.
	 */
	public LotusOrgChagepwdService(UserManager<?> userManager) {
		Assert.isNotNull(userManager, "AccountManager must be not null.") ;
		this.userManager = userManager ;
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String)
	 */
	public void changePassword(String oldPassword, String newPassword) throws WebOSException {
		try {
			if(this.userManager.isExist(this.getUserName())) {
				// reset password
				this.userManager.resetPassword(this.getUserName(), newPassword);
			}
		} catch (LotusException ex) {
			throw new WebOSException("Change password in organization failed "+ ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
	 */
	public void resetPassword(String userName, String newPassword) throws WebOSException {
		throw new WebOSException("This function is not support.");
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
	 */
	public boolean verifyPassword(String pwd) throws WebOSException {
		return true;
	}

}
