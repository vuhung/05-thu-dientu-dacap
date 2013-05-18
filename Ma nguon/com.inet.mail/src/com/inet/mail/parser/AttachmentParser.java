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
package com.inet.mail.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AttachmentParser.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 3.2i
 */
public abstract class AttachmentParser {
	//~ Static fields =========================================================
	/**
	 * attachment number.
	 */
	private  static final int ATTACHMENT_NO 		= 1;
	
	/**
	 * Attachment file name.
	 */
	private static final int ATTACHMENT_FILE_NAME 	= 2;
	
	/**
	 * Attachment pattern.
	 */
	private static final Pattern ATTACHMENT_PATTERN = Pattern.compile("(\\d+):(.*)", Pattern.CASE_INSENSITIVE) ;
	
	//~ Methods ===============================================================
	/**
	 * get file name of the given attachment
	 * 
	 * @param attachment the given attachment key
	 * @return String - file name of attachment
	 */
	public static String getFileName(String attachment){
		return getAttachmentPart(attachment, ATTACHMENT_FILE_NAME);
	}
	
	/**
	 * get file name of the given attachment
	 * 
	 * @param attachment String - the given attachment key
	 * @return String - file name of attachment
	 */
	public static String getNumberOrder(String attachment){
		return getAttachmentPart(attachment, ATTACHMENT_NO);
	}
	
	/**
	 * get part of attachment key
	 * 
	 * @param attachment  String - the given attachment key
	 * @param no Integer - number order
	 * @return String
	 */
	private static String getAttachmentPart(String attachment, int no){
		// matcher.
		Matcher matcher = ATTACHMENT_PATTERN.matcher(attachment) ;
		
		// return the attachment data.
		if(matcher.find()) return matcher.group(no) ;
		
		// does not match.
		return null;
	}
}
