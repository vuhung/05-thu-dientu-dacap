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
package com.inet.mail.spam.support;

import com.inet.mail.spam.SpamException;

/**
 * InetAddressResolverException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: InetAddressResolverException.java 2009-01-08 14:40:00z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization InetAddressResolverException class.
 * </pre>
 */
public class InetAddressResolverException extends SpamException {
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7909890615838644582L;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>InetAddressResolverException</tt> instance.
	 */
	public InetAddressResolverException() {}

	/**
	 * Create a new <tt>InetAddressResolverException</tt> instance from
	 * the given exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public InetAddressResolverException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <tt>InetAddressResolverException</tt> instance from
	 * the given {@link Throwable} instance.
	 * 
	 * @param throwable the given {@link Throwable} instance.
	 */
	public InetAddressResolverException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Create a new <tt>InetAddressResolverException</tt> instance from
	 * the given exception message and  {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance.
	 */
	public InetAddressResolverException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
