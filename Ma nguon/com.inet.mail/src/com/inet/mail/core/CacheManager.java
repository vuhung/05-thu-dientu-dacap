/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.core;

import com.inet.mail.core.cache.Cache;

/**
 * CacheManager
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CacheManager.java 2008-12-06 15:18:14Z nguyen_dv $
 * 
 * Create date: Dec 6, 2008
 * <pre>
 *  Initialization CacheManager class.
 * </pre>
 */
public class CacheManager {
	//~ Static fields =========================================================
	/* cache manager name. */
	private static final String MAIL_CACHE_MANAGER = "wmail";
	
	/* sole instance. */
	private static final CacheManager SOLE_INSTANCE = new CacheManager();
	
	//~ Instance fields =======================================================	
	/* cache manager. */
	private final Cache CACHE_MANAGER;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>CacheManager</tt> instance.
	 */
	private CacheManager(){
		// get cache manager.
		CACHE_MANAGER = MailApplicationContext.getInstance().getCache(MAIL_CACHE_MANAGER) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * @return the {@link CacheManager} instance.
	 */
	public static CacheManager getInstance() {
		return SOLE_INSTANCE;
	}
	
	/**
	 * Put the value to cache.
	 * 
	 * @param key the given key name.
	 * @param item the given value data.
	 */
	public void setItem(String key,DataEntry<?> item){
		CACHE_MANAGER.put(key, item);
	}
	
	/**
	 * Retrieves the cache value from the given key name.
	 * 
	 * @param key the given key name.
	 * @return the cache value.
	 */
	public DataEntry<?> getItem(String key){
		return CACHE_MANAGER.get(key, DataEntry.class);
	}
	
	/**
	 * Remove the cache value from the given key name.
	 * 
	 * @param key the given key name.
	 */
	public void removeItem(String key){
		CACHE_MANAGER.remove(key);
	}
}
