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
package com.inet.mail.exception;

/**
 * PoolObjectException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PoolObjectException.java 2009-01-13 17:44:20z nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization PoolObjectException class.
 * </pre>
 */
public class PoolObjectException extends MailException {
	//~ Instance fields =======================================================
	/**
	 * serialVerionUID
	 */
	private static final long serialVersionUID = 6556080142064563932L;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>PoolObjectException</tt> instance from the given
	 * exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public PoolObjectException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <tt>PoolObjectException</tt> instance from the given
	 * exception message and {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance.
	 */
	public PoolObjectException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
