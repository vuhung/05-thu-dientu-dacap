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
package com.inet.web.bo.exception;

import com.inet.web.exception.WebOSException;

/**
 * WebOSBOException.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 8, 2008
 * <pre>
 *  Initialization WebOSBOException class.
 * </pre>
 */
public class WebOSBOException extends WebOSException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8025052048734072907L;

	/**
	 * Create <tt>WebOSBOException</tt> instance.
	 */
	public WebOSBOException() {}

	/**
	 * Create <tt>WebOSBOException<tt> instance from the 
	 * given error message.
	 * 
	 * @param msg String - the given error message.
	 */
	public WebOSBOException(String msg) {
		super(msg);
	}

	/**
	 * Create <tt>WebOSBOException</tt> instance from the 
	 * given <tt>Throwable</tt> instance.
	 * 
	 * @param throwable Throwable - the given <tt>Throwable</tt>
	 * instance.
	 */
	public WebOSBOException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Create <tt>WebOSBOException</tt> instance from the given
	 * error message and <tt>Throwable</tt> instance.
	 * 
	 * @param msg String - the given error message.
	 * @param throwable Throwable - the given <tt>Throwable</tt> instance.
	 */
	public WebOSBOException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
