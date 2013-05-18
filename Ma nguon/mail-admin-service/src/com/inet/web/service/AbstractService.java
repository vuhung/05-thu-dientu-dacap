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

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.Region;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.service.mail.utils.UserCacheService;
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
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(AbstractService.class) ;
	
	/**
	 * the service cache region.
	 */
	private static final String SERVICE_REGION = "lotus-service" ;
	
	/**
	 * @return the service cache instance.
	 * 
	 * @throws CacheException when error occurs during caching data.
	 */
	protected Region<String> getCache() throws CacheException{
		return WebOSCacheHelper.getCache(WebApplicationContext.getInstance().getApplicationContext(), SERVICE_REGION) ;
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
			String code = UserCacheService.getUserCode(username) ;
			
			// show user code.
			if(logger.isDebugEnabled()) logger.debug("user code = [" + code + "]") ;
		
			// return user code.
			return code;
		}
		
		// return empty.
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * @return the user identifier.
	 */
	/*protected long getId() {
		String username = getUserName() ;
		
		// debugger.
		if(logger.isDebugEnabled()) logger.debug("user name = [" + username + "]" ) ;
		
		// has data.
		if(StringService.hasLength(username)) {
			// get user identifier.
			long id = UserCacheService.getId(username) ;
			
			// show user identifier.
			if(logger.isDebugEnabled()) logger.debug("user identifier = [" + id + "]") ;
			
			// return user identifier.
			return id ;
		}
		
		// return empty.
		return 0L ;		
	}*/

	/**
	 * @return the current user full name.
	 */
	protected String getLastName() {		
		String username = getUserName() ;

		// debugger.
		if(logger.isDebugEnabled()) logger.debug("user name = [" + username + "]" ) ;
		
		if(StringService.hasLength(username)) {
			// get user full name.
			String name = UserCacheService.getName(username) ;
			
			// show user full name.
			if(logger.isDebugEnabled()) logger.debug("user full name = [" + name + "]") ;
			
			// return user full name.
			return name ;
		}
		
		// return empty.
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * @return the data cache from the given cache name; may be null.
	 */
	protected <T> T getDataFromCache(String name, Class<T> clazz) {
		try {
			String key = getCacheKey(name);
			if(logger.isDebugEnabled()) logger.debug("get object from cache associate to key: [" + key + "]") ;
			
			return WebOSCacheHelper.getValue(getCache(), key, clazz) ;
		}catch(CacheException cex) {
			logger.warning("could not get data from cache: " + name, cex) ;
		}catch(Exception ex) {
			logger.warning("could not get data from cache: " + name, ex) ;
		}
		
		return null ;
	}
	
	/**
	 * remove data from cache
	 * 
	 * @param name String - the cache key 
	 */
	protected void removeDataFromCache(String name) {
		String key = getCacheKey(name);
		
		WebOSCacheHelper.remove(getCache(), key);
	}
	
	/**
	 * Set data to cache.
	 * 
	 * @param name String - the given cache name.
	 * @param data Object - the given cache data.
	 */
	protected <T> void setDataToCache(String name, T data) {
		logger.debug("get data from cache with key: " + getCacheKey(name));
		try {
			// the data is null.
			if(data == null) return;
			
			// get cache key.
			String key = getCacheKey(name) ;
			
			// put value to cache.
			WebOSCacheHelper.putValue(getCache(), key, data) ;
		}catch(CacheException cex) {
			logger.warning("could not put data to cache: " + name, cex) ;
		}catch(Exception ex) {
			logger.warning("could not put data to cache: " + name, ex) ;
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
}
