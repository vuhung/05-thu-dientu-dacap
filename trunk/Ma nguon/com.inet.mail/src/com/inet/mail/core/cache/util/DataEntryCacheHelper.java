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

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.inet.mail.core.CacheManager;
import com.inet.mail.core.DataEntry;
import com.inet.mail.core.UIDStore;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.util.WebOSCacheHelper;

/**
 * DataEntryCacheHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: DataEntryCacheHelper.java Dec 12, 2008 nguyen_dv $
 * 
 * Create date: Dec 12, 2008
 * <pre>
 *  Initialization DataEntryCacheHelper class.
 * </pre>
 */
public abstract class DataEntryCacheHelper {
	//~ Static fields =========================================================
	/* data entry cache key. */
	private static final String CACHE_KEY = DataEntryCacheHelper.class.getName() ;
	
	/* class logger. */
        static final Logger logger = Logger.getLogger(DataEntryCacheHelper.class);	
	//~ Methods ===============================================================
	/**
	 * Returns the {@link UIDStore} associate to the name from the subsystem.
	 * 
	 * @param account the account name used to looking for {@link UIDStore} information.
	 * @return the {@link UIDStore} instance.
	 */
	public static DataEntry<? extends Serializable> get(String account){
		try{
			return CacheManager.getInstance().getItem(createKey(account)) ;
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
	 * @param store the given {@link UIDStore} used to put to cache.
	 */
	public static void put(String account, DataEntry<? extends Serializable> entry){
		try{
			CacheManager.getInstance().setItem(createKey(account), entry) ;
		}catch(CacheSupportException cex){
			logger.warn("could not put data to cache, will be looked data in database, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not put data to cache, will be looked data in database, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Removes the existing {@link UIDStore} that representing with account out
	 * of the cache.
	 * 
	 * @param account the given user to looking for {@link UIDStore}.
	 */
	public static void remove(String account){
		try{
			CacheManager.getInstance().removeItem(createKey(account)) ;
		}catch(CacheSupportException cex){
			logger.warn("could not remove data from cache, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not remove data from cache, message: [" + ex.getMessage() + "]") ;
		}
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
