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
package com.inet.mail.core.cache.support;

import com.inet.mail.core.cache.Cache;
import com.inet.mail.core.cache.CacheSupportException;
import com.inet.web.cache.RegionManager;
import com.inet.web.cache.exception.CacheException;

/**
 * WebOSCacheSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: WebOSCacheSupport.java 2008-12-07 20:30:42Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization WebOSCacheSupport class.
 * </pre>
 */
public class WebOSCacheSupport implements Cache {
	//~ Instance fields =======================================================
	/* region manager instance. */
	private RegionManager manager ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>WebOSCacheSupport</tt> from the given
	 * {@link RegionManager} instance.
	 * 
	 * @param manager the given {@link RegionManager} instance.
	 */
	public WebOSCacheSupport(RegionManager manager){
		this.manager = manager ;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T get(String key, Class<T> clazz) throws CacheSupportException {
		try{
			return manager.get(key, clazz);
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> void put(String key, T value) throws CacheSupportException {
		try{
			manager.put(key, value) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(String key) throws CacheSupportException {
		try{
			manager.remove(key) ;
		}catch(CacheException cex){
			throw new CacheSupportException(cex.getMessage(), cex) ;
		}catch(Exception ex){
			throw new CacheSupportException(ex.getMessage(), ex) ;
		}
	}	
}
