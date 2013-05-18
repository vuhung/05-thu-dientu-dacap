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
package com.inet.web.service.exception;

import com.inet.web.exception.WebOSException;

/**
 * WebOSServiceException.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 14, 2008
 * <pre>
 *  Initialization WebOSServiceException class.
 * </pre>
 */
public class WebOSServiceException extends WebOSException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5291819834398596798L;

	/**
	 * Create <tt>WebOSServiceException</tt> instance.
	 */
	public WebOSServiceException() {}

	/**
	 * Create <tt>WebOSServiceException</tt> instance from the given
	 * error message.
	 * 
	 * @param msg String - the given error message.
	 */
	public WebOSServiceException(String msg) {
		super(msg);
	}

	/**
	 * Create <tt>WebOSServiceException</tt> instance from the given
	 * <tt>Throwable</tt> instance.
	 * 
	 * @param throwable Throwable - the given <tt>Throwable</tt> instance.
	 */
	public WebOSServiceException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Create <tt>WebOSServiceException</tt> instance from the given
	 * error message and <tt>Throwable</tt> instance.
	 * 
	 * @param msg String - the given error message.
	 * @param throwable Throwable - the given <tt>Throwable</tt> instance.
	 */
	public WebOSServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
