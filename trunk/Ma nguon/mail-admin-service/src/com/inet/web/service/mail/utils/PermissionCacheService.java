/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
import com.inet.lotus.mail.permission.Permission;
import com.inet.web.cache.exception.CacheException;

/**
 * PermissionCacheService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class PermissionCacheService {
	private static final String PERMISSION_CACHE_KEY = PermissionCacheService.class.getCanonicalName();
	
	/**
	 * Return user permission in cache
	 * 
	 * @param code String - the user code
	 * @return Permission - the user permission
	 * @throws CacheException - when there is any error happens
	 */
	public static Permission get(String code) throws CacheException{
		return LongLiveRegionService.get(applyStrategy(code), Permission.class);
	}
	
	/**
	 * Put the permission to cache
	 * 
	 * @param code String - the user code
	 * @param permission Permission - the user permission
	 * @throws CacheException - when there is any error happens
	 */
	public static void put(String code, Permission permission) throws CacheException {
		LongLiveRegionService.put(applyStrategy(code), permission);
	}
	
	/**
	 * Remove user permission out of cache 
	 * 
	 * @param code String - the user code
	 * @throws CacheException - when there is any error happens
	 */
	public static void remove(String code) throws CacheException {
		
	}

	/**
	 * @return the cache key from the given user code.
	 */
	protected static String applyStrategy(String code) {
		if(!StringService.hasLength(code)) return PERMISSION_CACHE_KEY ;
		return PERMISSION_CACHE_KEY + '#' + code ;
	}
}
