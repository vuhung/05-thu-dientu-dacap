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

import com.inet.base.exception.BaseRuntimeException;

/**
 * MailException
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailException.java 2008-12-07 13:54:36Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization MailException class.
 * </pre>
 */
public class MailException extends BaseRuntimeException{
	//~ Static fields =========================================================
	/* serialVersionUID */
	private static final long serialVersionUID = -1186555974329110276L;

	//~ Constructors ==========================================================
	/**
	 * Create MailException instance. 
	 */
	public MailException() {}
	
	/**
	 * Create MailException instance.
	 * 
	 * @param msg String - the given error message.
	 */
	public MailException(String msg){
		super(msg) ;
	}
	
	/**
	 * Create MailException instance.
	 * 
	 * @param throwable Throwable - the given throwable instance.
	 */
	public MailException(Throwable throwable){
		super(throwable) ;
	}
	
	/**
	 * Create MailException instance.
	 * 
	 * @param msg String - the given error message.
	 * @param throwable Throwable - the given throwable instance.
	 */
	public MailException(String msg, Throwable throwable){
		super(msg, throwable) ;
	}
}
