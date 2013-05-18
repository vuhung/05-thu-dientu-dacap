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
package com.inet.mail.parser;

import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.mail.exception.MailException;

/**
 * ParserException
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date: Jan 28, 2008
 * <pre>
 *  Initialization ParserException class.
 * </pre>
 */
public class ParserException extends MailException {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2688425889007478667L;
	
	//---------------------------------------------------------
	// Class element.
	//
	// the exception data.
	private CharSequence data ;

	/**
	 * Create ParserException instance.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Create ParserException instance.
	 * 
	 * @param msg String - the given message.
	 * @param throwable Throwable - the given throwable instance.
	 */
	public ParserException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	/**
	 * Create ParserException instance.
	 * 
	 * @param msg String - the given error message.
	 */
	public ParserException(String msg) {
		super(msg);
	}
	
	/**
	 * Create ParserException instance.
	 * 
	 * @param data CharSequence - the given error data.
	 */
	public ParserException(CharSequence data){
		this.data = data ;
	}
	
	/**
	 * Create ParserException instance.
	 * 
	 * @param msg String - the given error message.
	 * @param data CharSequence - the given error data.
	 */
	public ParserException(String msg, CharSequence data){
		this(msg) ;
		this.data = data ;
	}

	/**
	 * Create ParserException instance.
	 * 
	 * @param throwable Throwable - the given throwable instance.
	 */
	public ParserException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Set the error data.
	 * 
	 * @param data CharSequence - the given error data.
	 */
	public void setData(CharSequence data) {
		this.data = data;
	}
	
	/**
	 * @return the error data.
	 */
	public CharSequence getData() {
		return this.data;
	}
	
	/**
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		// prepare output data.
		StringBuilder builder = new StringBuilder() ;
		
		if(StringService.hasLength(this.getMessage())){
			builder.append("Message: [").append(this.getMessage()).append("]").append(IOService.LINE_SEPARATOR) ;
		}
		
		// append error data.
		builder.append("Source: [").append(this.data).append("]") ;
		
		// return the builder.
		return builder.toString();
	}
}
