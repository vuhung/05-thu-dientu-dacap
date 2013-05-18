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
package com.inet.web.service.mail.permission;

import java.util.ArrayList;
import java.util.List;

import com.inet.base.logging.INetLogger;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.manage.domain.ldap.ILdapDomainAdminManager;
import com.inet.lotus.mail.permission.Permission;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.UserService;
import com.inet.web.security.WebOSAuthenticationException;
import com.inet.web.security.WebOSUsernameNotFoundException;
import com.inet.web.security.acl.Module;
import com.inet.web.security.acl.basic.WebOSRole;
import com.inet.web.security.meta.UserServiceInvocation;
import com.inet.web.security.user.User;
import com.inet.web.security.user.basic.WebOSUser;
import com.inet.web.service.mail.utils.PermissionCacheService;

/**
 * MailUserService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailUserService implements UserService {
	private static INetLogger logger = INetLogger.getLogger(MailUserService.class);
	private ILdapDomainAdminManager domainAdminManager;
	private LdapAccountManager accountManager;
	
	/**
	 * @param domainAdministratorManager DomainAdministratorManager - the domain admin manager
	 */
	public MailUserService(ILdapDomainAdminManager domainAdministratorManager,
			LdapAccountManager accountManager) {
		this.domainAdminManager = domainAdministratorManager;
		this.accountManager = accountManager;
	}

	/**
	 * @see com.inet.web.security.UserService#findModulesByName(java.lang.String)
	 */
	public List<Module> findModulesByName(String arg0)
			throws WebOSUsernameNotFoundException, WebOSException {
		return new ArrayList<Module>();
	}
	

	/**
	 * @see com.inet.web.security.UserService#findByName(java.lang.String)
	 */
	@UserServiceInvocation
	public User findByName(String userName) throws WebOSUsernameNotFoundException, WebOSException {
		try {
			// get the user account
			Account account = this.accountManager.findByName(userName);
			if(account == null) throw new WebOSAuthenticationException("user could not found") ;
			
			// create principal.
			com.inet.web.security.user.User principal = new WebOSUser(
											userName, //account.getLastName(), user.getUserName(), 
											account.getPassword(), 
											account.getCode()
										) ;
			
			// load the user's permission
			Permission permission = this.domainAdminManager.getPermission(account.getName());
			// put the permission to cache
			PermissionCacheService.put(account.getCode(), permission);
			
			// get the user's role
			String role = permission.getAccessRole().toString();

			// get user role.
			if(logger.isDebugEnabled()) logger.debug("user roles: [" + role + "]") ;
			
			// get the role of user
			principal.getRoles().add(new WebOSRole(role));
			
			if(logger.isDebugEnabled()) logger.debug("principal : [" + principal + "]") ;
			
			return principal;
		} catch (LotusException ex) {
			logger.error("Could not build user principal", ex);
			throw new WebOSAuthenticationException("Could not build user principal", ex);
		}
	}
}
