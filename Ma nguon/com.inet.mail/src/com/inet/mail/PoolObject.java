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

/**
 * PoolObject
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PoolObject.java 2009-01-13 15:31:10z nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization PoolObject class.
 * </pre>
 */
public interface PoolObject<T> {
	/**
	 * @return the pooled object.
	 */
	T getObject() ;
	
	/**
	 * @return if the object is used.
	 */
	boolean isUsed() ;
	
	/**
	 * Marks the object is used.
	 */
	void markIsUsed() ;
	
	/**
	 * @return the object time stamp.
	 */
	long getTimestamp() ;
	
	/**
	 * @return the {@link PoolObject} name.
	 */
	String getName() ;
	
	/**
	 * reset the object timestamp and used flag.
	 */
	void reset() ;
	
	/**
	 * dispose object.
	 */
	void dispose() ;
	
	/**
	 * Check the given object is matched with current object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj);
}
