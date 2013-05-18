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

import java.io.Serializable;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.DigestService;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.Region;
import com.inet.web.cache.exception.CacheException;

/**
 * LongLiveRegionService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 24, 2008
 * <pre>
 *  Initialization LongLiveRegionService class.
 * </pre>
 */
public abstract class LongLiveRegionService {
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(LongLiveRegionService.class) ;
	
	/**
	 * Long live region service.
	 */
	private static final String LONG_LIVE_REGION = "longlive" ;
	
	/**
	 * Put the cache data from the given key and data value.
	 * 
	 * @param key String - the given key data.
	 * @param data T - the given data value.
	 * @throws CacheException when error occurs during putting data to cache.
	 */
	public static <T> void put(String key, T data) throws CacheException{
		if(logger.isDebugEnabled()) logger.debug("put [" + key + "] with value [" + data + "] to long live cache.") ;
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheException("could not put none serialization data to cache.") ;
		
		// serialized object.
		byte[] serialData = IOService.getStream(data) ;
		if(serialData == null) throw new CacheException("could not serialization data") ;
		String cacheData = DigestService.base64Encode(serialData) ;
		
		// store data to cache.
		getCache().add(key, cacheData) ;
	}
	
	/**
	 * Update data in cache from the given data key and data value.
	 * 
	 * @param key String - the given data key.
	 * @param data T - the given cache data value.
	 * @throws CacheException when error occurs during updating data to cache.
	 */
	public static <T> void update(String key, T data) throws CacheException{
		if(logger.isDebugEnabled()) logger.debug("update [" + key + "] with value [" + data + "] to long live cache.") ;
		
		if(data == null) return;
		if(!(data instanceof Serializable)) throw new CacheException("could not put none serialization data to cache.") ;
		
		// serialized object.
		byte[] serialData = IOService.getStream(data) ;
		if(serialData == null) throw new CacheException("could not serialization data") ;
		String cacheData = DigestService.base64Encode(serialData) ;
		
		// update data to cache.
		getCache().add(key, cacheData) ;		
	}
	
	/**
	 * Get the data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @return the cache data.
	 * @throws CacheException when error occurs during getting data from cache.
	 */
	public static <T> T get(String key, Class<T> clazz) throws CacheException{
		if(logger.isDebugEnabled()) logger.debug("get data from cache with key [" + key + "].") ;
		String cacheData = getCache().get(key) ;
		
		if(!StringService.hasLength(cacheData)) return null ;
		
		// convert to data.
		byte[] serialData = DigestService.base64Decode(cacheData) ;
		if(serialData == null || serialData.length == 0) return null ;		
		
		// return object.
		return IOService.getObject(serialData, clazz) ;
	}
	
	/**
	 * Remove data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @throws CacheException when error occurs during getting data from cache.
	 */
	public static void remove(String key) throws CacheException{
		if(logger.isDebugEnabled()) logger.debug("remove data from cache with key [" + key + "].") ;
		getCache().remove(key) ;
	}
	
	/**
	 * @return <tt>Region</tt> instance.
	 * @throws CacheException when error occurs during getting cache.
	 */
	protected static Region<String> getCache() throws CacheException{
		return WebApplicationContext.getInstance().getCache(LONG_LIVE_REGION) ;
	}
}
