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

import java.io.Serializable;

/**
 * DataEntry
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: DataEntry.java 2008-12-12 14:22:35z nguyen_dv $
 * 
 * Create date: Dec 12, 2008
 * <pre>
 *  Initialization DataEntry class.
 * </pre>
 */
public abstract class DataEntry<T extends Serializable> implements Serializable {
	//~ Static fields =======================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 915565516124401586L;
	
	//~ Instance fields =======================================================
	/* the cache content. */
	protected T content;
	
	/* the cache version. */
	protected Long timestamp;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>DataEntry</tt> from the given cache content.
	 * 
	 * @param content the given cache content.
	 */
	public DataEntry(T content){
		// set the cache content.
		setContent(content) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * @return the cache content.
	 */
	public T getContent() {
		return this.content;
	}

	/**
	 * Set the cache content.
	 * 
	 * @param content the given cache content.
	 */
	public void setContent(T content) {
		// set the new time stamp.
		timestamp = System.currentTimeMillis();
		
		// set the cache content.
		this.content = content;
	}	
	
	/**
	 * @return the cache time stamp.
	 */
	public Long getTimestamp() {
		return timestamp;
	}
}
