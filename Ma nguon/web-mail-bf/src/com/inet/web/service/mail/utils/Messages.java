/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.web.service.mail.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import com.inet.web.application.WebApplicationContext;

public class Messages {

	private static String BUNDLE_NAME = "resources.mail.messages.messages";
	private static ResourceBundle resourceBundle;
	
	public static final String const_inbox = "const_inbox";
	public static final String const_outbox = "const_outbox" ;
	public static final String const_sent = "const_sent";
	public static final String const_trash = "const_trash";
	public static final String const_spam = "const_spam";
	public static final String const_draft = "const_draft";
	public static final String const_contact = "const_contact";
	
	
	static{
		resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("vi","VN")/*Locale.getDefault()*/);
	}
	public static String getMessage(String key){
		return resourceBundle.getString(key);
	}
	
	protected String getUserName() {
		if(WebApplicationContext.getInstance() == null) return null ;
		if(WebApplicationContext.getInstance().getApplicationContext() == null) return null ;
		if(WebApplicationContext.getInstance().getApplicationContext().getAuthentication() == null) return null ;
		
		return WebApplicationContext.getInstance().getApplicationContext().getAuthentication().getPrincipal() ;
	}
}

