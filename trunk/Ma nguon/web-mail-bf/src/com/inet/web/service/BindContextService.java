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
package com.inet.web.service;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.WebService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.utils.AccountCacheService;

/**
 * BindContextService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 28, 2008
 * <pre>
 *  Initialization BindContextService class.
 * </pre>
 */
public class BindContextService extends AbstractWebOSBeanService<Subject> {
	//~ Static fields =========================================================
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(BindContextService.class) ;
	
	/**
	 * action parameter.
	 */
	private static final String ATTRIBUTE_PARAM = "iwebos-action" ;
	
	/**
	 * bind context action.
	 */
	private static final String BIND_CONTEXT_ACTION = "bind" ;
	
	/**
	 * the page limit parameter.
	 */
	private static final String PAGE_LIMIT = "pageLimit" ;
	
	/**
	 * logout action.
	 */
	private static final String LOGOUT_ACTION = "logout" ;
	
	/**
	 * user subject.
	 */
	private static final String SUBJECT = "subject" ;
	
	/**
	 * the given user information.
	 */
	private static final String USER = "user" ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>BindContextService</tt> instance from the given
	 * {@link AccountManager} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 */
	public BindContextService(AccountManager<Account> accountManager){
		super(accountManager) ;
	}
			
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Subject execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		// get request.
		String action = (String)request.getAttribute(ATTRIBUTE_PARAM) ;
		if(logger.isDebugEnabled()) logger.debug("execute action = [" + action + "]") ;
		
		// set limit parameter.
		WebService.setSessionAttribute(request, PAGE_LIMIT, 25) ;
		
		if(BIND_CONTEXT_ACTION.equals(action)) {			
			// get subject.
			if(WebApplicationContext.getInstance() == null) return null;
			AbstractApplicationServerProvider serverProvider = WebApplicationContext.getInstance().getApplicationServerProvider();
			
			// bind context.
			if(serverProvider == null) return null ;
			if(serverProvider.getContext() == null || serverProvider.getContext().getContext() == null) return null ;
		
			// get the subject.
			Subject subject = serverProvider.getContext().getContext().getSubject() ;
			
			// find user.
			Account user = null ;
			
			// get user.
			try{
				user = AccountCacheService.get(getUserName()) ;
			}catch(CacheException cex){
				logger.warning("Could not get account from cache, message: [" + cex.getMessage() + "]") ;
			}
			
			// lookup account from account manager.
			if(user == null){
				try{
					user = getAccountManager().findByName(getUserName()) ;
					
					// put to cache.
					if(user != null){
						AccountCacheService.put(user) ;
					}
				}catch(LotusException lex){
					logger.warning("Could not get account information from LDAP, message: [" + lex.getMessage() + "].") ;
				}catch(CacheException cex){
					logger.warning("Could not put account information to cache, message: [" + cex.getMessage() + "].") ;
				}
			}
			
			// get subject.
			if(logger.isDebugEnabled()) logger.debug("subject = [" + subject + "]") ;
			if(subject != null) {
				WebService.setSessionAttribute(request, SUBJECT, subject) ;
			}
			
			// setting up user object.
			if(user != null) {
				if(logger.isDebugEnabled()) logger.debug("current login user = [" + user.getName() + "]") ;
				WebService.setSessionAttribute(request, USER, user) ;
			}
		}else if(LOGOUT_ACTION.equals(action)) {			
			// get subject.
			Subject subject = (Subject)WebService.getSessionAttribute(request, SUBJECT) ;
			if(logger.isDebugEnabled()) logger.debug("subject = [" + subject + "]") ;
			
			// remove subject.
			WebService.removeSessionAttribute(request, SUBJECT) ;
			WebService.removeSessionAttribute(request, USER) ;
			
			// return the subject.
			return subject ;
		}
		
		// default, do nothing.
		return null ;
	}
}
