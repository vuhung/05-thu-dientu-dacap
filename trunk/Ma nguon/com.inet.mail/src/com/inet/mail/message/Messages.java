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
package com.inet.mail.message;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Messages
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Messages.java Dec 12, 2008 nguyen_dv $
 * 
 * Create date: Dec 12, 2008
 * <pre>
 *  Initialization Messages class.
 * </pre>
 */
public final class Messages {
	//~ Static fields =========================================================
	/* bundle name. */
	private static final String BUNDLE_NAME = Messages.class.getName() ;
	
	/* resource bundle. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	
	/* original message. */
	public static final String mail_message_original_msg = "mail_message_original_msg";
	/* forward message. */
	public static final String mail_message_forward = "mail_message_forward" ;
	/* reply message. */
	public static final String mail_message_msg = "mail_message_msg" ;
	/* from */
	public static final String mail_message_from = "mail_message_from" ;
	/* to */
	public static final String mail_message_to = "mail_message_to" ;
	/* cc */
	public static final String mail_message_cc = "mail_message_cc" ;
	/* subject */
	public static final String mail_message_subject = "mail_message_subject" ;
	//~ Methods ===============================================================
	/**
	 * @return the resource message.
	 */
	public static String getMessage(String key){
		return RESOURCE_BUNDLE.getString(key) ;
	}
}
