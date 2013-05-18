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
package com.inet.web.service.support.acegi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.adapters.PrincipalAcegiUserToken;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.acegisecurity.userdetails.UserDetails;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.web.service.utils.AccountCacheService;

/**
 * AccountCacheLogoutHandler
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AccountCacheLogoutHandler.java Nov 12, 2008 nguyen_dv $
 * 
 * Create date: Nov 12, 2008
 * <pre>
 *  Initialization AccountCacheLogoutHandler class.
 * </pre>
 */
public class AccountCacheLogoutHandler implements LogoutHandler{
	//~ Static fields =========================================================
	/* class logger. */
	private static final INetLogger logger = INetLogger.getLogger(AccountCacheLogoutHandler.class) ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		if(logger.isDebugEnabled()) logger.debug("BEFORE cleaning the account cache...") ;
		
		if(authentication != null){
			String username = authentication.getName() ;
			if(authentication instanceof PrincipalAcegiUserToken){
				username = ((PrincipalAcegiUserToken)authentication).getName() ;
			}else if(authentication.getPrincipal() instanceof UserDetails){
				username = ((UserDetails)authentication.getPrincipal()).getUsername() ;
			}
			
			// show information.
			if(logger.isDebugEnabled()) logger.debug("Cleaning account of user: [" + username + "]") ;
			
			try{
				if(StringService.hasLength(username)) AccountCacheService.remove(username) ;
			}catch(Exception ex){
				logger.warning("Could not cleaning the cache, message: [" + ex.getMessage() + "].") ;
			}
		}
		
		if(logger.isDebugEnabled()) logger.debug("AFTER cleaning the account cache...") ;
	}
}
