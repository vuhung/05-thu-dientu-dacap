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
import com.inet.mail.core.cache.Cache;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.RegionManager;

/**
 * MailRegionHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailRegionHelper.java 2008-12-10 19:48:44z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization MailRegionHelper class.
 * </pre>
 */
public abstract class MailRegionHelper {
	//~ Static fields =========================================================
	/* long live region name. */
	private static final String MAIL_REGION = "wmail" ;
	
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
		if(!(data instanceof Serializable)){
			throw new CacheSupportException("could not put none serialization data to cache.") ;
		}
		
		// put to cache.
		getCache().put(key, data) ;
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
		if(!(data instanceof Serializable)){
			throw new CacheSupportException("could not put none serialization data to cache.") ;
		}
		
		getCache().put(key, data) ;
	}
	
	/**
	 * Get the data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @return the cache data.
	 * @throws CacheSupportException when error occurs during getting data from cache.
	 */
	public static <T> T get(String key, Class<T> clazz) throws CacheSupportException{
		return getCache().get(key, clazz) ;
	}
	
	/**
	 * Remove data in cache from the given cache key.
	 * 
	 * @param key String - the given cache key.
	 * @throws CacheSupportException when error occurs during getting data from cache.
	 */
	public static void remove(String key) throws CacheSupportException{
		getCache().remove(key) ;
	}
	
	/**
	 * Create and return the cache {@link RegionManager} instance.
	 * 
	 * @return <tt>Region</tt> instance.
	 * @throws CacheSupportException when error occurs during getting cache.
	 */
	protected static Cache getCache() throws CacheSupportException{
		// get application context region.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		Assert.isNotNull(context, "MailApplicationContext must be set.") ;
		Assert.isNotNull(context.getContext(), "WebOSApplicationContext must be set.") ;
		
		// return the cache.
		return context.getCache(MAIL_REGION) ;
	}		
}
