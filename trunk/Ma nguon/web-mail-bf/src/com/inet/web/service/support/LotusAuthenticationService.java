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
package com.inet.web.service.support;

import com.inet.base.service.Assert;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.auth.AuthenticateManager;
import com.inet.lotus.auth.LotusAuthenticateException;
import com.inet.web.security.Principal;
import com.inet.web.security.WebOSAuthenticationException;
import com.inet.web.service.AbstractAuthenticationService;

/**
 * LotusAuthenticationService
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: LotusAuthenticationService.java Nov 11, 2008 nguyen_dv $
 * 
 * Create date: Nov 11, 2008
 * <pre>
 *  Initialization LotusAuthenticationService class.
 * </pre>
 */
public class LotusAuthenticationService extends AbstractAuthenticationService {
	//~ Instance fields =======================================================
	/* authenticate manager. */
	private AuthenticateManager authenticateManager ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new LotusAuthenticationService instance from the given {@link AuthenticateManager}
	 * instance.
	 * 
	 * @param authenticateManager the given {@link AuthenticateManager} instance.
	 */
	public LotusAuthenticationService(AuthenticateManager authenticateManager){
		Assert.isNotNull(authenticateManager, "AuthenticateManager must be not null.") ;
		this.authenticateManager = authenticateManager ;
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.security.AuthenticationService#authenticate(com.inet.web.security.Principal, java.lang.Object)
	 */
	public boolean authenticate(Principal principal, Object credentials) 
			throws WebOSAuthenticationException {
		// get user name and password.
		String username = (principal == null ? null : principal.getName()) ;
		String password = (credentials == null ? StringService.EMPTY_STRING : (String)credentials) ;
		
		// checking data.
		if(!StringService.hasLength(username)) throw new WebOSAuthenticationException("the user name must be not null.") ;
		
		// get login account.
		try{
			Account account = authenticateManager.authenticateUser(username, password) ;
			if(account == null) return false ;
			
			// put to cache.
			putLoginAccountToCache(account) ;
			
			// login successful.
			return true ;
		}catch(LotusAuthenticateException laex){
			logger.warning("could not log in to system, message: [" + laex.getMessage() + "]", laex) ;
		}
		
		return false;
	}
}
