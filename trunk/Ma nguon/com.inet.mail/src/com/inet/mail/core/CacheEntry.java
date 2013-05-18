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

import com.inet.mail.conf.PersonMailConfig;

/**
 * CacheEntry
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CacheEntry.java 2008-12-12 14:20:10z nguyen_dv $
 * 
 * Create date: Dec 12, 2008
 * <pre>
 *  Initialization CacheEntry class.
 * </pre>
 */
public class CacheEntry extends DataEntry<PersonMailConfig> implements Serializable {
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5574031593688785146L;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>CacheEntry</tt> instance from the given
	 * {@link PersonMailConfig} instance.
	 * 
	 * @param content the given {@link PersonMailConfig} instance.
	 */
	public CacheEntry(PersonMailConfig content){
		super(content);
	}
}
