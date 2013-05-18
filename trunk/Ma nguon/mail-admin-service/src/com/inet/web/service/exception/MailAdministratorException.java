/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
 * MailAdministratorException.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAdministratorException extends WebOSException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8090132739037559644L;
	
	/**
	 * Create <tt>MailAdministratorException</tt> instance.
	 */
	public MailAdministratorException() {}

	/**
	 * Create <tt>MailAdministratorException</tt> instance from the given
	 * error message.
	 * 
	 * @param msg String - the given error message.
	 */
	public MailAdministratorException(String msg) {
		super(msg);
	}

	/**
	 * Create <tt>MailAdministratorException</tt> instance from the given
	 * <tt>Throwable</tt> instance.
	 * 
	 * @param throwable Throwable - the given <tt>Throwable</tt> instance.
	 */
	public MailAdministratorException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Create <tt>MailAdministratorException</tt> instance from the given
	 * error message and <tt>Throwable</tt> instance.
	 * 
	 * @param msg String - the given error message.
	 * @param throwable Throwable - the given <tt>Throwable</tt> instance.
	 */
	public MailAdministratorException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
