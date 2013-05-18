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
package com.inet.mail.core.cache;

import com.inet.mail.exception.MailException;

/**
 * CacheSupportException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CacheSupportException.java 2008-12-07 13:54:59Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization CacheSupportException class.
 * </pre>
 */
public class CacheSupportException extends MailException {
	//~ Static fields =========================================================
	/* serialVersionUID */
	private static final long serialVersionUID = -5683778240366545387L;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>CacheSupportException</tt> instance.
	 */
	public CacheSupportException(){}
	
	/**
	 * Create a new <tt>CacheSupportException</tt> instance from the
	 * given exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public CacheSupportException(String msg){
		super(msg) ;
	}
	
	/**
	 * Create a new <tt>CacheSupportException</tt> instance from the 
	 * given {@link Throwable} instance.
	 * 
	 * @param throwable the given {@link Throwable} instance.
	 */
	public CacheSupportException(Throwable throwable){
		super(throwable) ;
	}
	
	/**
	 * Create a new <tt>CacheSupportException</tt> instance from the
	 * given exception message and {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance.
	 */
	public CacheSupportException(String msg, Throwable throwable){
		super(msg, throwable) ;
	}
}
