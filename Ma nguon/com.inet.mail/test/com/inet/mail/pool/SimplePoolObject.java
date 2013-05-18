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

import com.inet.mail.AbstractPoolObject;

/**
 * SimplePoolObject
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SimplePoolObject.java Jan 13, 2009 nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization SimplePoolObject class.
 * </pre>
 */
public class SimplePoolObject extends AbstractPoolObject<String> {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SimplePoolObject</tt> instance from the given
	 * object name and object value.
	 * 
	 * @param name the given object name.
	 * @param object the given object value.
	 */
	public SimplePoolObject(String name, String object){
		super(name, object) ;
	}	
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	/**
	 * @see com.inet.mail.PoolObject#dispose()
	 */
	public void dispose() {}
}
