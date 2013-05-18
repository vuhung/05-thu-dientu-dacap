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
package com.inet.web.controller.secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.adapters.PrincipalAcegiUserToken;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.acegisecurity.userdetails.UserDetails;

import com.inet.base.logging.INetLogger;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.service.mail.utils.UserCacheService;

/**
 * WebOSLogoutHandler.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 28, 2008
 * <pre>
 *  Initialization WebOSLogoutHandler class.
 * </pre>
 */
public class WebOSLogoutHandler implements LogoutHandler {
	//~ Static fields =========================================================
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(WebOSLogoutHandler.class) ;
	
	//~ Methods ===============================================================	
	/**
	 * @see org.acegisecurity.ui.logout.LogoutHandler#logout(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		// clear the cache.
		cleanUserCache(authentication) ;
		
	}
	
	//~ Helper Methods ========================================================
	/**
	 * @return the bean instance from the given bean name and bean class.
	 */
	protected <T> T getBean(String name, Class<T> clazz) {
		return WebApplicationContext.getInstance().getObject(name, clazz) ;
	}
	
	/**
	 * Clean the cache from the given authentication.
	 * 
	 * @param authentication Authentication - the given authentication information.
	 */
	protected void cleanUserCache(Authentication authentication) {
		if(logger.isDebugEnabled()) logger.debug("BEFORE: Cleaning the cache ...") ;
		
		//check authentication before logout
		if (authentication == null){
			if(logger.isDebugEnabled()) logger.debug("AFTER: Cleaning the cache is null...") ;
			return;
		}
		
		// logout user.
		String username = authentication.getName() ;
		
		if(authentication instanceof PrincipalAcegiUserToken) {
			PrincipalAcegiUserToken token = (PrincipalAcegiUserToken)authentication ;
			username = token.getName() ;
		}else if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)authentication.getPrincipal() ;
			username = userDetails.getUsername() ;
		}
		
		if(logger.isDebugEnabled()) logger.debug("User logout: [" + username + "]") ;
		
		try {
			// clean service cache.
			if(logger.isDebugEnabled()) logger.debug("Clean the user cache ...") ;
			UserCacheService.remove(username) ;
		}catch(Exception ex) {
			logger.warning("could not clean the service cache.", ex) ;
		}
		
		if(logger.isDebugEnabled()) logger.debug("AFTER: Cleaning the cache ...") ;
	}
}
