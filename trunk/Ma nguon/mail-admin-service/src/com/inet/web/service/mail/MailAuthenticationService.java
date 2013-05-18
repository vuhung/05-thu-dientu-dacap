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
package com.inet.web.service.mail;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.auth.LotusAuthenticateException;
import com.inet.lotus.auth.ldap.LdapAuthenticateManager;
import com.inet.web.security.AuthenticationService;
import com.inet.web.security.Principal;
import com.inet.web.security.WebOSAuthenticationException;
import com.inet.web.service.mail.utils.UserCacheService;

/**
 * MailAuthenticationService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAuthenticationService implements AuthenticationService {
	private static final INetLogger logger = INetLogger.getLogger(MailAuthenticationService.class);
	private LdapAuthenticateManager authenticateManager;
	
	/**
	 * The constructor
	 * 
	 * @param authenticateManager IAuthenticateManager - the authenticate manager
	 */
	public MailAuthenticationService(LdapAuthenticateManager authenticateManager) {
		this.authenticateManager = authenticateManager;
	}


	/**
	 * @see com.inet.web.security.AuthenticationService#authenticate(com.inet.web.security.Principal, java.lang.Object)
	 */
	public boolean authenticate(Principal principal, Object credentials)
			throws WebOSAuthenticationException {
		// get user name and password.
		String userName = (principal == null ? null : principal.getName()) ;
		String password = (credentials == null ? StringService.EMPTY_STRING : (String)credentials) ;
		// check the valid of user name and password 
		if(!StringService.hasLength(userName) || !StringService.hasLength(password)) return false;
		
		logger.debug("[user login]: " + userName);
		// authenticate user
		try {
			Account account = (Account) this.authenticateManager.authenticateUser(userName, password);
			logger.debug("[user]: " + account);
			if(account == null) return false;
			
			// cache the login user
			UserCacheService.put(account);
			return true;
		} catch (LotusAuthenticateException ex) {
			logger.error("ERROR while authenticating user", ex);
			throw new WebOSAuthenticationException("ERROR while authenticating user.");
		}
	}

}
