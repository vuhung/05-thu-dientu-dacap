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

import org.apache.log4j.Logger;

import com.inet.mail.core.UIDStore;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.util.WebOSCacheHelper;

/**
 * UIDStoreCacheHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: UIDStoreCacheHelper.java Dec 11, 2008 nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization UIDStoreCacheHelper class.
 * </pre>
 */
public abstract class UIDStoreCacheHelper {
	//~ Static fields =========================================================
	/* UID store cache key. */
	private static final String CACHE_KEY = UIDStoreCacheHelper.class.getName() ;
	
	/* class logger. */
        static final Logger logger = Logger.getLogger(UIDStoreCacheHelper.class);	
	//~ Methods ===============================================================
	/**
	 * Returns the {@link UIDStore} associate to the name from the subsystem.
	 * 
	 * @param usercode the given login user code.
	 * @param account the account name used to looking for {@link UIDStore} information.
	 * @return the {@link UIDStore} instance or null.
	 */
	public static UIDStore get(String usercode, String account){
		try{
			return LongLiveZipRegionHelper.get(createKey(usercode, account), UIDStore.class) ;
		}catch(CacheSupportException cex){
			logger.warn("could not looking data into cache, looking to database, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not looking data into cache, looking to database, message: [" + ex.getMessage() + "]") ;
		}
		
		return null ;
	}
	
	/**
	 * Puts the {@link UIDStore} to cache.
	 * 
	 * @param usercode the given login user code.
	 * @param store the given {@link UIDStore} used to put to cache.
	 */
	public static void put(String usercode, String account, UIDStore store){
		try{
			LongLiveZipRegionHelper.put(createKey(usercode, account), store) ;
		}catch(CacheSupportException cex){
			logger.warn("could not looking data into cache, looking to database, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not looking data into cache, looking to database, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Removes the existing {@link UIDStore} that representing with account out
	 * of the cache.
	 * 
	 * @param usercode the given login user code.
	 * @param account the given user to looking for {@link UIDStore}.
	 */
	public static void remove(String usercode, String account){
		try{
			LongLiveZipRegionHelper.remove(createKey(usercode, account)) ;
		}catch(CacheSupportException cex){
			logger.warn("could not looking data into cache, looking to database, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not looking data into cache, looking to database, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Create the cache key and return to the system from the given
	 * part of key.
	 * 
	 * @param usercode the given login user code.
	 * @param name the given part of key.
	 * @return the key name.
	 * @throws CacheSupportException when error occurs during creating key.
	 */
	protected static String createKey(String usercode, String name) throws CacheSupportException{
		try{
			return WebOSCacheHelper.combineKey(CACHE_KEY, usercode, name) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}
	}
}
