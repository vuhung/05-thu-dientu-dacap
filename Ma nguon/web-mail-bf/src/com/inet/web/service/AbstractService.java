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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.Region;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.service.utils.AccountCacheService;
import com.inet.web.service.utils.Helpers;
import com.inet.web.util.WebOSCacheHelper;

/**
 * AbstractService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 18, 2008
 * <pre>
 *  Initialization AbstractService class.
 * </pre>
 */
public abstract class AbstractService {
	//~ Static fields =========================================================
	/**
	 * class logger.
	 */
	protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * the service cache region.
	 */
	public static final String SERVICE_REGION = "service" ;
	
	/**
	 * {@link AccountManager} instance.
	 */
	private final AccountManager<Account> accountManager ;	
	//~ Constructors ==========================================================
	/**
         * Create a new <tt>AbstractService</tt> instance from the given
         * {@link AccountManager} instance.
         */
        protected AbstractService(){
                this.accountManager = null ;
        }
	
	/**
	 * Create a new <tt>AbstractService</tt> instance from the given
	 * {@link AccountManager} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 */
	protected AbstractService(AccountManager<Account> accountManager){
		this.accountManager = accountManager ;
	}
	

	//~ Methods ===============================================================
	/**
	 * @return the service cache instance.
	 * 
	 * @throws CacheException when error occurs during caching data.
	 */
	protected Region<String> getCache() throws CacheException{
		return Helpers.getCache(SERVICE_REGION) ;
	}

	/**
	 * Gets data from the given data name.
	 * 
	 * @param request HttpServletRequest - the given <tt>HttpServletRequest</tt>
	 * to search information through.
	 * @param name String - the given data name.
	 * @return the data value, empty if not found/matched.
	 */
	protected String getData(HttpServletRequest request, String name) {
		String value =  request.getParameter(name) ;
		return StringService.hasLength(value) ? value : StringService.EMPTY_STRING ;
	}

	/**
	 * @return the current login user name.
	 */
	protected String getUserName() {
		if(WebApplicationContext.getInstance() == null) return null ;
		if(WebApplicationContext.getInstance().getApplicationContext() == null) return null ;
		if(WebApplicationContext.getInstance().getApplicationContext().getAuthentication() == null) return null ;
		
		return WebApplicationContext.getInstance().getApplicationContext().getAuthentication().getPrincipal() ;
	}
	
	/**
	 * @return the current user code.
	 */
	protected String getCode() {
		String username = getUserName() ;
		
		// debugger.
		if(logger.isDebugEnabled()) logger.debug("user name = [" + username + "]") ;
		
		// has data.
		if(StringService.hasLength(username)) {
			// get user code.
			Account account = AccountCacheService.get(username) ;
			
			// get account from account manager.
			if(account == null){
				try{
					account = accountManager.findByName(username) ;
				}catch(LotusException lex){
					logger.warn("Does not get account from AccountManager, message: [" + lex.getMessage() + "]") ;
				}catch(Exception ex){
					logger.warn("Does not get account from AccountManager, message: [" + ex.getMessage() + "]") ;
				}
				
				// put account to cache.
				if(account != null) AccountCacheService.put(account) ;
			}
					
			// return user code.
			return (account != null ? account.getCode() : StringService.EMPTY_STRING);
		}
		
		// return empty.
		return StringService.EMPTY_STRING ;
	}	

	/**
	 * @return the default limited data.
	 */
	protected int getDefaultLimit() {
		return 25 ;
	}
	
	/**
	 * @return the current user full name.
	 */
	protected String getFullName() {		
		String username = getUserName() ;

		// debugger.
		if(logger.isDebugEnabled()) logger.debug("user name = [" + username + "]" ) ;
		
		if(StringService.hasLength(username)) {
			// get user full name.
			Account account = AccountCacheService.get(username) ;
			
			// the user fullname.
			StringBuilder name = new StringBuilder() ;
			name.append(account.getFirstName())
				.append(" ")
				.append(account.getLastName()) ;
			
			// show user full name.
			if(logger.isDebugEnabled()) logger.debug("user full name = [" + name + "]") ;
			
			// return user full name.
			return name.toString() ;
		}
		
		// return empty.
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * @return current domain that user belong
	 */
	protected String getDomain() {
	  String username = getUserName();
	  // debugger.
          if(logger.isDebugEnabled()) logger.debug("user name = [" + username + "]" ) ;
          
          if(!StringService.hasLength(username)) 
            return StringService.EMPTY_STRING;
          
          try {
            int index = username.indexOf("@");
            if(index < 0 )
              return StringService.EMPTY_STRING;
            
            return username.substring(index + 1);
          }catch (Exception ex) {
            return StringService.EMPTY_STRING;
          }
	}
	
	/**
	 * @return the data cache from the given cache name; may be null.
	 */
	protected <T> T getDataFromCache(String name, Class<T> clazz) {
		try {
			String key = getCacheKey(name) ;
			if(logger.isDebugEnabled()) logger.debug("get object from cache associate to key: [" + key + "]") ;
			
			return WebOSCacheHelper.getValue(getCache(), key, clazz) ;
		}catch(CacheException cex) {
			logger.warn("could not get data from cache: [" + name + "]", cex) ;
		}catch(Exception ex) {
			logger.warn("could not get data from cache: [" + name + "]", ex) ;
		}
		
		return null ;
	}
	
	/**
	 * Set data to cache.
	 * 
	 * @param name String - the given cache name.
	 * @param data Object - the given cache data.
	 */
	protected <T> void setDataToCache(String name, T data) {
		try {
			// the data is null.
			if(data == null) return;
			
			// get cache key.
			String key = getCacheKey(name) ;
			
			// put value to cache.
			WebOSCacheHelper.putValue(getCache(), key, data) ;
		}catch(CacheException cex) {
			logger.warn("could not put data to cache: " + name, cex) ;
		}catch(Exception ex) {
			logger.warn("could not put data to cache: " + name, ex) ;
		}
	}
	
	/**
	 * Remove data from cache from the given cache key name.
	 * 
	 * @param name the given key name.
	 */
	protected void removeDataFromCache(String name){
		try {
			// get cache key.
			String key = getCacheKey(name) ;
			
			// put value to cache.
			WebOSCacheHelper.remove(getCache(), key) ;
		}catch(CacheException cex) {
			logger.warn("could not put data to cache: " + name, cex) ;
		}catch(Exception ex) {
			logger.warn("could not put data to cache: " + name, ex) ;
		}		
	}

	/**
	 * @return the cache key from the given key name.
	 */
	protected String getCacheKey(String name) {
		return WebOSCacheHelper.combineKey(name, getCacheSuffix()) ;
	}
	
	/**
	 * @return the cache key suffix.
	 */
	protected String getCacheSuffix() { return null; }
	
	/**
	 * @return the {@link AccountManager} instance.
	 */
	protected AccountManager<Account> getAccountManager(){
		return accountManager ;
	}
}
