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

import org.acegisecurity.AccountExpiredException;
import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.CredentialsExpiredException;
import org.acegisecurity.DisabledException;
import org.acegisecurity.LockedException;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.providers.dao.cache.NullUserCache;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsChecker;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.inet.base.service.DigestService;
import com.inet.web.security.AuthenticationService;
import com.inet.web.security.SimplePrincipal;
import com.inet.web.security.WebOSAuthenticationException;

/**
 * WebOSAuthenticationProvider
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: WebOSAuthenticationProvider.java 02-12-2008 14:20:35Z nguyen_dv $
 * 
 * Create date: Dec 2, 2008
 * <pre>
 *  Initialization WebOSAuthenticationProvider class.
 * </pre>
 */
public class WebOSAuthenticationProvider implements AuthenticationProvider,
		MessageSourceAware, InitializingBean {
	//~ Instance fields =======================================================
	/* Message source accessor. */
	protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor() ;
	/* pre-authentication checks */
	private UserDetailsChecker preAuthenticationChecks = new PreAuthenticationChecks() ;
	/* post-authentication posts */
	private UserDetailsChecker postAuthenticationChecks = new PostAuthenticationChecks() ;
	/* authentication service. */
	private AuthenticationService authenticationService = null ;
	/* user details service. */
	private UserDetailsService userDetailsService = null ;
	/* user cache. */
	private UserCache userCache = new NullUserCache() ;
	
	//~ Methods ===============================================================
	/**
	 * @see org.acegisecurity.providers.AuthenticationProvider#authenticate(org.acegisecurity.Authentication)
	 */
	public Authentication authenticate(Authentication authentication) 
		throws AuthenticationException {
		if(!supports(authentication.getClass())){
			throw new IllegalStateException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", 
					"Only UsernamePasswordAuthenticationToken is supported")) ;
		}
		
		// determine username.
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDER" : authentication.getName() ;
		boolean cacheWasUsed = true ;
		boolean authenticated = false ;
		UserDetails user = userCache.getUserFromCache(username) ;
		
		// check cache user.
		if(user != null){
			// user existing in user cached, does not need to put again.
			cacheWasUsed = false ;
			
			preAuthenticationChecks.check(user) ;
			
			// check credentials.
			authenticated = checkCredentials(user, (UsernamePasswordAuthenticationToken)authentication) ;
		}
		
		// login to sub system.
		if(!authenticated){
			try{
				// get authentication token.
				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)authentication ;
				// get credentials.
				String credentials = (authenticationToken.getCredentials() == null ? "" : authenticationToken.getCredentials().toString()) ;
				
				// authenticated data.
				authenticated = getAuthenticationService().authenticate(
						new SimplePrincipal(username), 
						credentials
					) ;
			}catch(WebOSAuthenticationException waex){
				throw new BadCredentialsException(
						messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
											"Bad credentials")) ;
			}
			
			// login success ful, get user detail information.
			if(authenticated){
				try{
					user = getUserDetailsService().loadUserByUsername(username) ;
				}catch(UsernameNotFoundException unex){
					throw new BadCredentialsException(
							messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
												"Bad credentials")) ;
					
				}catch(DataAccessException dex){
					throw new BadCredentialsException(
							messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
												"Bad credentials")) ;					
				}
				
				// check user.
				preAuthenticationChecks.check(user) ;
 			}else{
				throw new BadCredentialsException(
						messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
											"Bad credentials")) ;					 				
 			}
		}
		
		// post authentication check.
		postAuthenticationChecks.check(user) ;
		
		// put user to cache.
		if(!cacheWasUsed) userCache.putUserInCache(user) ;
		
		// create authentication.
		return createSuccessAuthentication(user, authentication, user);
	}

	/**
	 * @see org.acegisecurity.providers.AuthenticationProvider#supports(java.lang.Class)
	 */
	@SuppressWarnings({"unchecked"})
	public boolean supports(Class clazz) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(clazz);
	}

	/**
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
	 */
	public void setMessageSource(MessageSource source) {
		messages = new MessageSourceAccessor(source) ;
	}
	
	/**
	 * Sets the policy will be used to verify the status of the loaded <tt>UserDetails</tt> <em>before</em>
	 * validation of the credentials takes places.
	 * 
	 * @param preAuthenticationChecks strategy to be invoked prior to authentication.
	 */
	public void setPreAuthenticationChecks(
			UserDetailsChecker preAuthenticationChecks) {
		this.preAuthenticationChecks = preAuthenticationChecks;
	}
	
	/**
	 * @return strategy to be invoked prior to authentication.
	 */
	public UserDetailsChecker getPreAuthenticationChecks() {
		return preAuthenticationChecks;
	}
	
	/**
	 * Sets the policy will be used to verify the status of the loaded <tt>UserDetails</tt> <em>after</em>
	 * validation of the credentials takes places.
	 * 
	 * @param postAuthenticationChecks strategy to be invoked poster to authentication.
	 */
	public void setPostAuthenticationChecks(
			UserDetailsChecker postAuthenticationChecks) {
		this.postAuthenticationChecks = postAuthenticationChecks;
	}
	
	/**
	 * @return strategy to be invoked poster to authentication.
	 */
	public UserDetailsChecker getPostAuthenticationChecks() {
		return postAuthenticationChecks;
	}
	
	/**
	 * @return authentication server.
	 */
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}
	
	/**
	 * Sets the system {@link AuthenticationService} used to validate principal and credentials.
	 * 
	 * @param authenticationService the authentication service.
	 */
	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	/**
	 * @return the user details service used to retrieved details user information.
	 */
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
	/**
	 * Sets the {@link UserDetailsService} used to retrieved details user information.
	 * 
	 * @param userDetailsService the user details service.
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	/**
	 * Sets the {@link UserCache} used to cached user.
	 * 
	 * @param userCache the {@link UserCache} instance.
	 */
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
	
	/**
	 * @return the {@link UserCache} instance.
	 */
	public UserCache getUserCache() {
		return userCache;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(authenticationService, "An authentication service must be set.") ;
		Assert.notNull(messages, "A message source must be set") ;
		Assert.notNull(userDetailsService, "An user details service must be set.") ;
		Assert.notNull(userCache, "A user cache must be set") ;
	}	
	
	/**
	 * Create a successful {@link Authentication} object.
	 * <p>
	 * Subclasses will usually store the original credentials the user supplied
	 * in the returned {@link Authentication} object.
	 * </p>
	 * 
	 * @param principal that sould be the principal in the returned object.
	 * @param authentication that was presented to the provider for validation.
	 * @param user that was loaded by the implementation.
	 * 
	 * @return the successful authentication token.
	 */
	protected Authentication createSuccessAuthentication(Object principal, 
			Authentication authentication, UserDetails user){
		// Ensure we return the original credentials the user supplied,
		// so subsequent attempts are successful even with encoded password.
		// Also ensure we return the original getDetails(), so that future
		// authentication events after cache expiry contain the details
		UsernamePasswordAuthenticationToken authenticationToken = 
			new UsernamePasswordAuthenticationToken(
					principal,
					authentication.getCredentials(), 
					user.getAuthorities()
				) ;
		authenticationToken.setDetails(authentication.getDetails()) ;
		
		return authenticationToken ;
	}
	
	/**
	 * Return if the {@link UsernamePasswordAuthenticationToken#getCredentials()} validated with
	 * {@link UserDetails#getPassword()}
	 * 
	 * @param user the {@link UserDetails} instance was retrieved from sub system or {@link UserCache}
	 * @param token the current request that needs to be authenticated.
	 * 
	 * @return if the {@link UsernamePasswordAuthenticationToken#getCredentials()} validated with
	 * {@link UserDetails#getPassword()}
	 */
	protected boolean checkCredentials(UserDetails user, UsernamePasswordAuthenticationToken token){
		try{
			// get the credentials.
			String credentials = (token.getCredentials() == null ? "" : token.getCredentials().toString()) ;
			return DigestService.verify(credentials, user.getPassword()) ;
		}catch(Exception ex){}
		return false ;
	}
	
	//~ Internal classes ======================================================
	/**
	 * PreAuthenticationChecks
	 * 
	 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
	 * @version $Id: WebOSAuthenticationProvider.java 02-12-2008 14:20:55Z nguyen_dv $
	 * 
	 * Create date: Dec 2, 2008
	 * <pre>
	 *  Initialization PreAuthenticationChecks class.
	 * </pre>
	 */
	private class PreAuthenticationChecks implements UserDetailsChecker{
		/**
		 * @see org.acegisecurity.userdetails.UserDetailsChecker#check(org.acegisecurity.userdetails.UserDetails)
		 */
		public void check(UserDetails user) {
			if(!user.isAccountNonLocked()){
				throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked","User account is locked"), user) ;
			}
			
			if(!user.isEnabled()){
				throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"), user) ;
			}
			
			if(!user.isAccountNonExpired()){
				throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"), user) ;
			}
		}
	}
	
	/**
	 * PostAuthenticationChecks
	 * 
	 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
	 * @version $Id: WebOSAuthenticationProvider.java 02-12-2008 14:30:01Z nguyen_dv $
	 * 
	 * Create date: Dec 2, 2008
	 * <pre>
	 *  Initialization PostAuthenticationChecks class.
	 * </pre>
	 */
	private class PostAuthenticationChecks implements UserDetailsChecker{
		/**
		 * @see org.acegisecurity.userdetails.UserDetailsChecker#check(org.acegisecurity.userdetails.UserDetails)
		 */
		public void check(UserDetails user) {
			if(!user.isCredentialsNonExpired()){
				throw new CredentialsExpiredException(
						messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"), 
						user
					) ;
			}
		}
	}
}
