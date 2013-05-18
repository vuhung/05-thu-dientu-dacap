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
 * ReceivedHeaderParserException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ReceivedHeaderParserException.java Jan 8, 2009 nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization ReceivedHeaderParserException class.
 * </pre>
 */
public class ReceivedHeaderParserException extends SpamException {
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2841405626206861348L;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>ReceivedHeaderParserException</tt> instance from the given
	 * exception message.
	 * 
	 * @param msg the given exception message.
	 */
	public ReceivedHeaderParserException(String msg){
		super(msg) ;
	}
	
	/**
	 * Create a new <tt>ReceivedHeaderParserException</tt> instance from the given
	 * exception message and {@link Throwable} instance.
	 * 
	 * @param msg the given exception message.
	 * @param throwable the given {@link Throwable} instance.
	 */
	public ReceivedHeaderParserException(String msg, Throwable throwable) {
		super(msg, throwable) ;
	}
}
