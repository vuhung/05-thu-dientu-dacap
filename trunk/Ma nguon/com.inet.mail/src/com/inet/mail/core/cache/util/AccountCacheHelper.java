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
package com.inet.mail.core.cache.util;

import com.inet.lotus.account.Account;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.util.WebOSCacheHelper;

/**
 * AccountCacheHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AccountCacheHelper.java 2008-12-10 10:25:11Z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization AccountCacheHelper class.
 * </pre>
 */
public class AccountCacheHelper {
	//~ Static fields =========================================================
	/* account cache key. */
	private static final String CACHE_KEY = Account.class.getName() ;
	
	//~ Methods ===============================================================
	/**
	 * Returns the account associate to the name from the subsystem.
	 * 
	 * @param username the account name used to looking for {@link Account} information.
	 * @throws CacheSupportException when error occur during getting account from cache.
	 */
	public static Account get(String username) throws CacheSupportException{
		return LongLiveRegionHelper.get(createKey(username), Account.class) ;
	}
	
	/**
	 * Puts the account to cache.
	 * 
	 * @param account the given {@link Account} used to put to cache.
	 * @throws CacheSupportException when error occurs during putting {@link Account} to cache.
	 */
	public static void put(Account account) throws CacheSupportException{
		LongLiveRegionHelper.put(createKey(account.getName()), account) ;
	}
	
	/**
	 * Removes the existing account that representing with username out
	 * of the cache.
	 * 
	 * @param username the given user to looking for account.
	 * @throws CacheSupportException when error occurs during removing account.
	 */
	public static void remove(String username) throws CacheSupportException{
		LongLiveRegionHelper.remove(createKey(username)) ;
	}
	
	/**
	 * Create the cache key and return to the system from the given
	 * part of key.
	 * 
	 * @param name the given part of key.
	 * @return the key name.
	 * @throws CacheSupportException when error occurs during creating key.
	 */
	protected static String createKey(String name) throws CacheSupportException{
		try{
			return WebOSCacheHelper.combineKey(CACHE_KEY, name) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}
	}
	
}
