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
package com.inet.mail.core.cache;


/**
 * Cache
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Cache.java 2008-12-07 12:19:35Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization Cache class.
 * </pre>
 */
public interface Cache {
	/**
	 * Put the value to cache from the given cache key and cache value.
	 * 
	 * @param key the given cache key.
	 * @param value the given cache value.
	 * @throws CacheSupportException when error occurs during putting value 
	 * to cache.
	 */
	<T> void put(String key, T value) throws CacheSupportException ;
	
	/**
	 * Remove the value from the given cache key.
	 * 
	 * @param key the given cache key.
	 * @throws CacheSupportException when error occurs during removing 
	 * value from cache.
	 */
	void remove(String key) throws CacheSupportException ;
	
	/**
	 * Removing the cache value from the cache key.
	 * 
	 * @param key the given from the cache key.
	 * @param clazz the given cache data.
	 * @return the cache value.
	 * @throws CacheSupportException when error occurs during getting 
	 * cache value.
	 */
	<T> T get(String key, Class<T> clazz) throws CacheSupportException ;
}
