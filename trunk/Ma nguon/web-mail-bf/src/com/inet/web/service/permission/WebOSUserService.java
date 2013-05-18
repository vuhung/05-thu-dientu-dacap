/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
package com.inet.web.service.permission;

import java.util.Collections;
import java.util.List;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.IOService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.ldap.LotusLdapAccountManager;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.security.UserService;
import com.inet.web.security.WebOSAuthenticationException;
import com.inet.web.security.acl.Module;
import com.inet.web.security.acl.basic.WebOSRole;
import com.inet.web.security.meta.UserServiceInvocation;
import com.inet.web.security.user.basic.WebOSUser;

/**
 * WebOSUserService
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: WebOSUserService.java 2008-10-31 22:10:22 nguyen_dv $
 * 
 * Create date: Oct 31, 2008
 * <pre>
 *  Initialization WebOSUserService class.
 * </pre>
 */
public class WebOSUserService implements UserService {
	//~ Static fields =========================================================
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(WebOSUserService.class) ;
	
	/**
	 * anonymous role.
	 */
	private static final com.inet.web.security.acl.Role ROLE_ANONYMOUS = new WebOSRole("anonymous") ;
	
	/**
	 * user role.
	 */
	private static final com.inet.web.security.acl.Role ROLE_USER = new WebOSRole("User") ;
	//~ Instance fields =======================================================	
	/**
	 * Find account by name.
	 */
	private LotusLdapAccountManager accountManager ;
	
	//~ Constructors ==========================================================
	/**
	 * Create <tt>WebOSUserService</tt> instance from the given 
	 * {@link LotusLdapAccountManager} instance.
	 * 
	 * @param accountManager the given {@link LotusLdapAccountManager} instance.
	 */
	public WebOSUserService(LotusLdapAccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked"})
	public List<Module> findModulesByName(String username)
			throws WebOSAuthenticationException {
		return Collections.EMPTY_LIST ;
	}

	/**
	 * {@inheritDoc}
	 */
	@UserServiceInvocation
	public com.inet.web.security.user.User findByName(String username)
			throws WebOSAuthenticationException {
		try {
			if(logger.isDebugEnabled()) logger.debug("Create user principal: [" + username + "]") ;
			
			// get user information.
			Account user = accountManager.findByName(username) ;
			
			// find the account.
			if(user == null) throw new WebOSAuthenticationException("user could not found") ;
			
			// create principal.
			com.inet.web.security.user.User principal = new WebOSUser(
											user.getName(), 
											user.getPassword(), 
											user.getCode(),
											user.isDeleted()
										) ;

			if(principal.isLocked()){
				principal.getRoles().add(ROLE_ANONYMOUS) ;
			}else{
				principal.getRoles().add(ROLE_USER) ;
				principal.getRoles().add(ROLE_ANONYMOUS) ;
			}
			
			// show user information.
			if(logger.isDebugEnabled()) logger.debug("User principal: [" + IOService.LINE_SEPARATOR + principal + IOService.LINE_SEPARATOR + "].") ;
			
			return principal;
		}catch(WebOSBOException wbex) {
			// show logger.
			logger.error("Could not build user principal.", wbex) ;
	
			// throw error.
			throw new WebOSAuthenticationException("Could not build user principal.", wbex) ;
		}
	}
}
