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

import java.util.List;

import org.apache.log4j.Logger;

import com.inet.mail.core.cache.CacheSupportException;
import com.inet.mail.core.cache.util.MailRegionHelper;
import com.inet.mail.persistence.MailFilter;
import com.inet.web.util.WebOSCacheHelper;


/**
 * RuleFilterManager.
 * 
 * NOTE: Filter manager will receive only data from EJB bean and put into the
 * container. In this case, WEB or Client-Server model always work well.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class RuleFilterManager {
	//~ Static fields =========================================================
	/* rule cache key prefix. */
	private static final String RULE_CACHE_KEY_PREFIX = RuleFilterManager.class.getName() ;
	
	/* class logger. */
        static final Logger logger = Logger.getLogger(RuleFilterManager.class);
	
	//~ Methods ===============================================================
	/**
	 * Put filter to cache from the given user and user filters.
	 * 
	 * @param user the given user.
	 * @param filters the given user filter.
	 */
	public static void put(String user, List<MailFilter> filters) {
		try{
			// get key.
			String key = getKey(user) ;
			
			// put filter to cache.
			MailRegionHelper.put(key, filters) ;
		}catch(CacheSupportException cex){
			logger.warn("could not put filters to cache, message: [" + cex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not put filters to cache, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Gets filters from cache from the given user.
	 * 
	 * @param user the given user.
	 * @return the filters.
	 */
	@SuppressWarnings({"unchecked"})
	public static List<MailFilter> get(String user){
		try{
			// get the key.
			String key = getKey(user) ;
			
			// get filters from cache.
			return (List<MailFilter>)MailRegionHelper.get(key, List.class) ;
		}catch(CacheSupportException csex){
			logger.warn("could not get filters from cache, message: [" + csex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not get filters from cache, message: [" + ex.getMessage() + "]") ;
		}
		
		// does not retrieved filter from cache.
		return null ;
	}
	
	/**
	 * Remove cache filters from cache from the given user.
	 * 
	 * @param user the given user.
	 */
	public static void remove(String user){
		try{
			// get the filter key.
			String key = getKey(user) ;
			
			// remove the cache filter.
			MailRegionHelper.remove(key) ;
		}catch(CacheSupportException csex){
			logger.warn("could not remove filter out of cache, message: [" + csex.getMessage() + "]") ;
		}catch(Exception ex){
			logger.warn("could not remove filter out of cache, message: [" + ex.getMessage() + "]") ;
		}
	}
	
	/**
	 * Return the cache key from the given user.
	 * 
	 * @param user the given user information to make a key.
	 */
	protected static String getKey(String user){
		return WebOSCacheHelper.combineKey(RULE_CACHE_KEY_PREFIX, user) ;
	}
}
