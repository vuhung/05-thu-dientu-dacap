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
package com.inet.web.service.mail.utils;

import com.inet.base.service.StringService;
import com.inet.web.cache.exception.CacheException;
import com.inet.lotus.account.Account;

/**
 * UserCacheService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 1.0i
 * 
 * @date Jul 24, 2008
 * <pre>
 *  Initialization UserCacheService class.
 * </pre>
 */
public abstract class UserCacheService {
	/**
	 * user cache key.
	 */
	private static final String CACHE_KEY = UserCacheService.class.getCanonicalName() ;
	
	/**
	 * @return the user data in cache.
	 * @throws CacheException when error occurs during getting data from cache.
	 */
	public static Account get(String username) throws CacheException{
		return LongLiveRegionService.get(applyStrategy(username), Account.class) ;
	}
	
	/**
	 * Put the user to cache.
	 * 
	 * @param account Account - the given account data.
	 * @throws CacheException when error occurs during putting data to cache.
	 */
	public static void put(Account account) throws CacheException{
		LongLiveRegionService.put(applyStrategy(account.getName()), account) ;
	}
	
	/**
	 * The user code from the given user name.
	 * 
	 * @param username String - the given user name.
	 * @return the user code.
	 * @throws CacheException when error occurs during getting user code.
	 */
	public static String getUserCode(String username) throws CacheException{
		// get user from cache.
		Account user = get(username) ;
		return (user != null ? user.getCode() : StringService.EMPTY_STRING);
	}
	
	/**
	 * @return the name from the given user name.
	 * @throws CacheException when error occurs during getting name.
	 */
	public static String getName(String username) throws CacheException{
		// get user from cache.
		Account user = get(username) ;		
		return (user != null) ? user.getLastName() : StringService.EMPTY_STRING ;
	}
	
	/**
	 * Remove user from the cache.
	 * 
	 * @throws CacheException when error occurs during removing data from cache.
	 */
	public static void remove(String username) throws CacheException {
		LongLiveRegionService.remove(username) ;
	}
	
	/**
	 * @return the cache key from the given user name.
	 */
	protected static String applyStrategy(String username) {
		if(!StringService.hasLength(username)) return CACHE_KEY ;
		return CACHE_KEY + '#' + username ;
	}
}
