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
package com.inet.mail.pool;

import com.inet.base.concurrent.CollectionFactory;
import com.inet.base.concurrent.ConcurrentMap;
import com.inet.mail.PoolObjectManager;

/**
 * SimpleObjectManager
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SimpleObjectManager.java Jan 13, 2009 nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization SimpleObjectManager class.
 * </pre>
 */
public class SimpleObjectManager {
	//~ Static fields =========================================================
	/**
	 * concurrent hash map.
	 */
	private static final ConcurrentMap cache = CollectionFactory.createConcurrentMap(16) ;
	
	/* the SimpleObjectManager instance. */
	private static final SimpleObjectManager SOLE_INSTANCE = new SimpleObjectManager() ;
	//~ Methods ===============================================================
	/**
	 * Register pool object manager.
	 * 
	 * @param name the given pool object manager.
	 * @param poolManager the given {@link PoolObjectManager} manager.
	 */
	@SuppressWarnings({"unchecked"})
	public <T> PoolObjectManager<T> registerPoolManager(String name, PoolObjectManager<T> poolManager){
		return (PoolObjectManager<T>)cache.putIfAbsent(name, poolManager) ;
	}
	
	/**
	 * Remove the pool manager.
	 * 
	 * @param name the given pool manager name.
	 */
	public void removePoolManager(String name){
		cache.remove(name) ;
	}
	
	/**
	 * @return {@link PoolObjectManager} from the given name.
	 */
	@SuppressWarnings({"unchecked"})
	public <T> PoolObjectManager<T> getPoolManager(String name){
		return (PoolObjectManager<T>)cache.get(name) ;
	}
	
	/**
	 * @return the SimpleObjectManager instance.
	 */
	public static SimpleObjectManager getInstance(){
		return SOLE_INSTANCE ;
	}
}
