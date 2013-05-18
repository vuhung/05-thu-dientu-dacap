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

import com.inet.base.service.Assert;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.RegionManager;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.cache.support.ZipRegionManagerSupport;

/**
 * LongLiveZipRegionHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: LongLiveZipRegionHelper.java 2009-01-05 17:24:07z nguyen_dv $
 * 
 * Create date: Jan 5, 2009
 * <pre>
 *  Initialization LongLiveZipRegionHelper class.
 * </pre>
 */
public class LongLiveZipRegionHelper {
	//~ Static fields =========================================================
	/* long live region name. */
	private static final String LONG_LIVE_REGION = "longlive" ;
	
	//~ Methods ===============================================================
	/**
	 * Put the cache data from the given key and data value.
	 * 
	 * @param key String - the given key data.
	 * @param data T - the given data value.
	 * @throws CacheSupportException when error occurs during putting data to cache.
	 */
	public static <T> void put(String key, T data) throws CacheSupportException{
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheSupportException("could not put none serialization data to cache.") ;
		
		// put data to cache.
		try{
			getCache().put(key, data) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}
	
	/**
	 * Update data in cache from the given data key and data value.
	 * 
	 * @param key String - the given data key.
	 * @param data T - the given cache data value.
	 * @throws CacheSupportException when error occurs during updating data to cache.
	 */
	public static <T> void update(String key, T data) throws CacheSupportException{
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheSupportException("could not put none serialization data to cache.") ;
		
		try{
			getCache().update(key, data) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}
	
	/**
	 * Get the data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @return the cache data.
	 * @throws CacheSupportException when error occurs during getting data from cache.
	 */
	public static <T> T get(String key, Class<T> clazz) throws CacheSupportException{
		try{
			return getCache().get(key, clazz) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}
	
	/**
	 * Remove data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @throws CacheSupportException when error occurs during getting data from cache.
	 */
	public static void remove(String key) throws CacheSupportException{
		try{
			getCache().remove(key) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}
	
	/**
	 * Create and return the cache {@link RegionManager} instance.
	 * 
	 * @return the {@link RegionManager} instance.
	 * @throws CacheSupportException when error occurs during getting cache.
	 */
	protected static RegionManager getCache() throws CacheSupportException{
		// get application context region.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		Assert.isNotNull(context, "MailApplicationContext must be set.") ;
		Assert.isNotNull(context.getContext(), "WebOSApplicationContext must be set.") ;
		
		// return the cache.
		return ZipRegionManagerSupport.getInstance(context.getContext(), LONG_LIVE_REGION) ;
	}	
}
