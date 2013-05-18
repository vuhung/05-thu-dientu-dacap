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

/**
 * EmailDictionaryKey
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 18, 2008
 * <pre>
 *  Initialization EmailDictionaryKey class.
 * </pre>
 */
public interface EmailDictionaryKey {
	/**
	 * SMTP server name.
	 */
	static final String EMAIL_SMTP_SERVER 				= "SMTP_SERVER";
	/**
	 * SMTP server port.
	 */				
	static final String EMAIL_SMTP_SERVER_PORT			= "SMTP_SERVER_PORT";
	/**
	 * SMTP server security.
	 */
	static final String EMAIL_SMTP_SERVER_SECURITY		= "SMTP_SERVER_SECURITY";
	/**
	 * Receiver mail server name.
	 */				
	static final String EMAIL_SERVER					= "SERVER";
	/**
	 * Receiver mail port name.
	 */					
	static final String EMAIL_SERVER_PORT				= "SERVER_PORT";
	/**
	 * Receiver mail server security.
	 */					
	static final String EMAIL_SERVER_SECURITY			= "SERVER_SECURITY";
	/**
	 * Receiver mail server security default.
	 */
	static final String EMAIL_SERVER_SECURITY_DEFAULT	= "SERVER_SECURITY_DEFAULT";	
	/**
	 * Receiver mail server type.
	 */
	static final String EMAIL_SERVER_TYPE				= "SERVER_TYPE";
	/**
	 * Receiver mail server type default.
	 */
	static final String EMAIL_SERVER_TYPE_DEFAULT		= "SERVER_TYPE_DEFAULT";
	/**
	 * Receiver mail domain.
	 */
	static final String EMAIL_DOMAIN					= "DOMAIN";
}
