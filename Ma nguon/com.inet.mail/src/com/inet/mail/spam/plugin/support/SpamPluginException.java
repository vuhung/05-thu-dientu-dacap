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
package com.inet.mail.spam.plugin.support;

import com.inet.mail.spam.SpamException;

/**
 * SpamPluginException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginException.java 2009-01-09 17:52:22z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization SpamPluginException class.
 * </pre>
 */
public class SpamPluginException extends SpamException {
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5249069022765042214L;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SpamPluginException</tt> instance from the given
	 * exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public SpamPluginException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <tt>SpamPluginException</tt> instance from the given
	 * exception message and {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance.
	 */
	public SpamPluginException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
