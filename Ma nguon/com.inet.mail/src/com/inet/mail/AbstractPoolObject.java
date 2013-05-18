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

import org.apache.log4j.Logger;

import com.inet.base.service.CompareService;

/**
 * AbstractPoolObject
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractPoolObject.java 2009-01-13 16:01:14z nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization AbstractPoolObject class.
 * </pre>
 */
public abstract class AbstractPoolObject<T> implements PoolObject<T> {
	//~ Instance fields =======================================================
	/* used flag */
	protected boolean used ;
	/* object timestamp. */
	private long timestamp ;
	/* object data. */
	protected final T object ;
	/* pool object name. */
	protected final String name ;
	/* class logger. */
	protected final Logger logger = Logger.getLogger(getClass());
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AbstractPoolObject</tt> instance
	 * from the given object name and instance.
	 * 
	 * @param name the given object name.
	 * @param object the given object instance.
	 */
	protected AbstractPoolObject(String name, T object){
		this.used = false ;
		this.timestamp = System.currentTimeMillis() ;
		this.object = object ;
		this.name = name ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#getObject()
	 */
	public T getObject() {
		return object;
	}
		
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#getTimestamp()
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#isUsed()
	 */
	public synchronized boolean isUsed() {
		return used;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#markIsUsed()
	 */
	public synchronized void markIsUsed() {
		this.used = true ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#reset()
	 */
	public synchronized void reset() {
		this.used = false ;
		this.timestamp = System.currentTimeMillis() ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof AbstractPoolObject)) return false ;
		AbstractPoolObject<T> another = (AbstractPoolObject<T>)obj ;
		
		if(!CompareService.equals(name, another.name)) return false ;
		if(used != another.used) return false ;
		return CompareService.equals(object, another.object) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer() ;
		
		buffer.append(getClass().getName()).append("[")
			  .append("Name: ").append(name)
		      .append("; Object: ").append(object)
		      .append("; Used: ").append(isUsed() ? "YES" : "NO")
		      .append("; Timestamp: ").append(timestamp)
		      .append("]") ;
		
		return buffer.toString();
	}
}
