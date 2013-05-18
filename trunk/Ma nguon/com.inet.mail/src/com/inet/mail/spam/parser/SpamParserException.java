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
package com.inet.mail.spam.parser;

import com.inet.mail.spam.SpamException;

/**
 * SpamParserException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamParserException.java 2009-01-10 21:44:18z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamParserException class.
 * </pre>
 */
public class SpamParserException extends SpamException {
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2015930583610570282L;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SpamParserException</tt> instance from the given
	 * exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public SpamParserException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <tt>SpamParserException</tt> instance from the given
	 * exception message and {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance
	 */
	public SpamParserException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
