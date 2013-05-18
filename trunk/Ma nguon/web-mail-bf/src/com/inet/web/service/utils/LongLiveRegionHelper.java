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

import java.io.Serializable;

import com.inet.base.service.Assert;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.RegionManager;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.cache.support.RegionManagerSupport;

/**
 * LongLiveRegionHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: LongLiveRegionHelper.java 2008-12-06 23:19:24Z nguyen_dv $
 * 
 * Create date: Dec 6, 2008
 * <pre>
 *  Initialization LongLiveRegionHelper class.
 * </pre>
 */
public abstract class LongLiveRegionHelper {
	//~ Static fields =========================================================
	/* long live region name. */
	private static final String LONG_LIVE_REGION = "longlive" ;
	
	//~ Methods ===============================================================
	/**
	 * Put the cache data from the given key and data value.
	 * 
	 * @param key String - the given key data.
	 * @param data T - the given data value.
	 * @throws CacheException when error occurs during putting data to cache.
	 */
	public static <T> void put(String key, T data) throws CacheException{
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheException("could not put none serialization data to cache.") ;
		
		// put to cache.
		getCache().put(key, data) ;
	}
	
	/**
	 * Update data in cache from the given data key and data value.
	 * 
	 * @param key String - the given data key.
	 * @param data T - the given cache data value.
	 * @throws CacheException when error occurs during updating data to cache.
	 */
	public static <T> void update(String key, T data) throws CacheException{
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheException("could not put none serialization data to cache.") ;
		
		getCache().update(key, data) ;
	}
	
	/**
	 * Get the data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @return the cache data.
	 * @throws CacheException when error occurs during getting data from cache.
	 */
	public static <T> T get(String key, Class<T> clazz) throws CacheException{
		return getCache().get(key, clazz) ;
	}
	
	/**
	 * Remove data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @throws CacheException when error occurs during getting data from cache.
	 */
	public static void remove(String key) throws CacheException{
		getCache().remove(key) ;
	}
	
	/**
	 * Create and return the cache {@link RegionManager} instance.
	 * 
	 * @return <tt>Region</tt> instance.
	 * @throws CacheException when error occurs during getting cache.
	 */
	protected static RegionManager getCache() throws CacheException{
		// get application context region.
		WebApplicationContext context = WebApplicationContext.getInstance() ;
		Assert.isNotNull(context, "WebApplicationContext must be set.") ;
		Assert.isNotNull(context.getApplicationContext(), "WebOSApplicationContext must be set.") ;
		
		// return the cache.
		return RegionManagerSupport.getInstance(context.getApplicationContext(), LONG_LIVE_REGION) ;
	}	
}
