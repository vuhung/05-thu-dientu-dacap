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

import com.inet.web.application.WebApplicationContext;
import com.inet.web.cache.Region;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.util.WebOSCacheHelper;

/**
 * Helpers
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Helpers.java 2008-12-06 nguyen_dv $
 * 
 * Create date: Dec 6, 2008
 * <pre>
 *  Initialization Helpers class.
 * </pre>
 */
public abstract class Helpers {
	/**
	 * Create the cache region and return to subsystem.
	 * 
	 * 
	 * @param name String - the given region name.
	 * @return the {@link Region} instance.
	 * @throws CacheException when error occurs during creating the cache.
	 */
	public static Region<String> getCache(String name) throws CacheException{
		WebApplicationContext context = WebApplicationContext.getInstance() ;
		if(context == null || context.getApplicationContext() == null){
			throw new CacheException("The WebOSApplicationContext instance is null.") ;
		}
		
		// return the cache.
		return WebOSCacheHelper.getCache(context.getApplicationContext(), name) ;
	}
}
