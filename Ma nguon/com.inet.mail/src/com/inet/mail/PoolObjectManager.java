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
package com.inet.mail;

import com.inet.mail.exception.PoolObjectException;

/**
 * PoolObjectManager
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PoolObjectManager.java 2009-01-13 17:31:41z nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization PoolObjectManager class.
 * </pre>
 */
public interface PoolObjectManager<T> {
	/**
	 * Return the object instance from the given object name.
	 * 
	 * @param name the given object name.
	 * @return the object instance or null.
	 */
	PoolObject<T> get(String name) ;
	
	/**
	 * Put the {@link PoolObject} to the {@link PoolObjectManager}
	 * 
	 * @param poolObject the given {@link PoolObject} instance.
	 * @return the {@link PoolObject} instance.
	 */
	void put(PoolObject<T> poolObject) ;
	
	/**
	 * Start the {@link PoolObjectManager}
	 * 
	 * @throws PoolObjectException when error occurs during starting {@link PoolObjectManager}
	 */
	void start() throws PoolObjectException ;
	
	/**
	 * Stop the {@link PoolObjectManager}.
	 * 
	 * @throws PoolObjectException when error occurs during stopping {@link PoolObjectManager}
	 */
	void stop() throws PoolObjectException ;
}
