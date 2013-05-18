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
package com.inet.mail.data;

import com.inet.base.service.StringService;

/**
 * MailFactoryType
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailFactoryType.java 2008-01-31 17:03:36Z nguyen_dv $
 * 
 * Create date: Jan 31, 2008
 * <pre>
 *  Initialization MailFactoryType class.
 * </pre>
 */
public enum MailFactoryType {
	/**
	 * SUN Mail factory.
	 */
	SUN("com.inet.mail.parser.support.sun.SunMessageFactory", "com.inet.mail.sun.SunMailFactory"),
	
	/**
	 * COLUMBAR Mail factory.
	 */
	COLUMBA(StringService.EMPTY_STRING, StringService.EMPTY_STRING),
	
	/**
	 * INET Mail Factory.
	 */
	INET(StringService.EMPTY_STRING, StringService.EMPTY_STRING);
	
	/**
	 * The message factory class.
	 */
	private String clazz ;
	
	/**
	 * Mail factory class.
	 */
	private String mailFactory ;
	
	/**
	 * Create MailFactory instance.
	 * 
	 * @param clazz String - the given message factory class.
	 */
	private MailFactoryType(String clazz, String mailFactory){
		this.clazz = clazz ;
		this.mailFactory = mailFactory ;
	}
	
	/**
	 * @return the message factory class.
	 */
	public String getFactory(){
		return this.clazz ;
	}
	
	/**
	 * @return the mail factory.
	 */
	public String getMailFactory(){
		return this.mailFactory ;
	}
}
