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
package com.inet.web.service.utils;

import com.inet.base.logging.INetLogger;
import com.inet.lotus.account.Account;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.util.WebOSCacheHelper;

/**
 * AccountCacheService
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AccountCacheService.java Nov 11, 2008 nguyen_dv $
 * 
 * Create date: Nov 11, 2008
 * <pre>
 *  Initialization AccountCacheService class.
 * </pre>
 */
public abstract class AccountCacheService {
	//~ Static fields =========================================================
	/* account cache key. */
	private static final String CACHE_KEY = Account.class.getName() ;

	/* class logger. */
	private static final INetLogger logger = INetLogger.getLogger(AccountCacheService.class) ;
	//~ Methods ===============================================================
	/**
	 * Returns the account associate to the name from the subsystem.
	 * 
	 * @param username the account name used to looking for {@link Account} information.
	 */
	public static Account get(String username){
		try{
			return LongLiveRegionHelper.get(createKey(username), Account.class) ;
		}catch(CacheException cex){
			logger.warning("Could not get Account from cache, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warning("Could not get Account from cache, message: [" + ex.getMessage() + "]") ;
		}
		
		return null ;
	}
	
	/**
	 * Puts the account to cache.
	 * 
	 * @param account the given {@link Account} used to put to cache.
	 */
	public static void put(Account account){
		try{
			LongLiveRegionHelper.put(createKey(account.getName()), account) ;
		}catch(CacheException cex){
			logger.warning("Could not put Account to cache, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warning("Could not put Account to cache, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Removes the existing account that representing with username out
	 * of the cache.
	 * 
	 * @param username the given user to looking for account.
	 */
	public static void remove(String username){
		try{
			LongLiveRegionHelper.remove(createKey(username)) ;
		}catch(CacheException cex){
			logger.warning("Could not removing Account from cache, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warning("Could not removing Account from cache, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Create the cache key and return to the system from the given
	 * part of key.
	 * 
	 * @param name the given part of key.
	 * @return the key name.
	 * @throws CacheException when error occurs during creating key.
	 */
	protected static String createKey(String name) throws CacheException{
		return WebOSCacheHelper.combineKey(CACHE_KEY, name) ;
	}
}
