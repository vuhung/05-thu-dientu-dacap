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


/**
 * MailConstants.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public interface MailConstants {
	
	// COMMON ===================================================================
	
	public static final String OBJECT 		= "object"; 
	
	// mail header================================================================
	
	/**
	 * JSON result key.
	 * value type : INTEGER
	 */
	public static final String RESULT_KEY = "results" ;
	/**
	 * JSON rows key.
	 * value type : List<JSON>
	 */
	public static final String ROWS_KEY = "rows" ;
	
	/**
	 * JSON mail identifier
	 * value type : LONG
	 */
	public static final String HEADER_ID = "hid";
	
	/**
	 *JSON list of mail identifier
	 * value type : List<Long> 
	 */
	public static final String HEADER_LIST_ID = "listid";
	/**
	 * JSON mail sender
	 * value type : String
	 * format : full name<email address>
	 */
	public static final String SENDER = "sender";
	
	/**
	 * JSON mail recipient
	 * value type : unknown
	 * mail recipient. That is MessageDOM instance that stored to,cc,bcc,...
	 */
	public static final String RECIPIENT = "recipient";
	
	/**
	 * JSON mail from address
	 * value type : String 
	 */
	public static final String FROM = "from";
	
	/**
	 * JSON mail TO address
	 * value type : String 
	 */
	public static final String TO = "to";
	
	/**
	 * JSON mail CC address
	 * value type : String 
	 */
	public static final String CC = "cc";
	
	/**
	 * JSON mail BCC address
	 * value type : String 
	 */
	public static final String BCC = "bcc";
	
	/**
	 * JSON mail subject
	 * value type : String
	 */
	public static final String SUBJECT = "subject";
	
	/**
	 * JSON mail sent date
	 * value type : Long
	 */
	public static final String SENT_DATE = "sent";  
	
	/**
	 * JSON mail received date
	 * value type : Long
	 */
	public static final String RECEIVED_DATE = "received";
	
	/**
	 * JSON mail stored date
	 * value type : Long
	 */
	public static final String CREATED_DATE = "created";
	
	/**
	 * JSON mail size
	 * value type : String
	 */
	public static final String SIZE = "size";
	
	/**
	 * JSON mail priority
	 * value type : String
	 */
	public static final String MAIL_PRIORITY = "priority";
	
	/**
	 * JSON mail mark read
	 * value type : STRING(Y/N)
	 */
	public static final String READ = "read";

	/**
	 * JSON mail mark read
	 * value type : STRING(Y)
	 */
	public static final String READ_YES = "Y";
	
	
	/**
	 * JSON mail mark unread
	 * value type : STRING(N)
	 */
	public static final String READ_NO = "N";
	
	/**
	 * JSON mail mark attach
	 * value type : boolean
	 */
	public static final String ATTACHED = "attached";

	/**
	 * JSON mail attachments 
	 * value type : String
	 */
	public static final String ATTACHMENTS = "attachments";
	
	/**
	 * JSON attach file name
	 * value type : String
	 */
	public static final String ATTACHMENT_FILE_NAME = "fname";
	
	/**
	 * JSON attach key
	 * value type : String
	 */
	public static final String ATTACHMENT_KEY = "fkey";
	
	public static final String EML_ATTACHMENT_KEY = "emlkey";
	
	/**
	 * JSON attach file
	 * value type : String
	 */
	public static final String ATTACHMENT_FILE = "file";
	/**
	 * JSON attach icon
	 * value type : String
	 */
	public static final String ATTACHMENT_ICON = "icon";
	
	/**
	 * JSON mail type
	 * value type : String
	 */
	public static final String TYPE = "type" ;
	
	/**
	 * JSON mail flag
	 * value type : String
	 */
	public static final String FLAG = "flag";
	
	/**
	 * JSON mail content text
	 * @value type : String
	 */
	public static final String BODY_TEXT = "body";
	
	public static final String MODE_CHECK_FETCH = "fetch";
	
	public static final String MODE_CHECK_NEW_MESSAGE = "newMessage";
	// FOLDER  ================================================================================================
	/**
	 * JSON mail folder identifier
	 * value type : Long
	 */
	public static final String FOLDER_ID 	= "id" ;
	public static final String FOLDER 		= "folder" ;
	public static final String FOLDER_INBOX	= "inbox";
	public static final String FOLDER_OUTBOX= "outbox";
	public static final String FOLDER_SENT	= "sent";
	public static final String FOLDER_DRAFT	= "draft";
	public static final String FOLDER_SPAM	= "spam";
	public static final String FOLDER_TRASH	= "trash";
	/**
	 * JSON mail folder name
	 * value type : String
	 */
	public static final String FOLDER_NAME = "text" ;
	
	public static final String FOLDER_ORIGINAL = "original";
	/**
	 * FOLDER ICON CSS 
	 * value : iconCls
	 */
	public static final String FOLDER_ICON_CSS = "iconCls" ;
	
	/**
	 * FOLDER CSS 
	 * value : iconCls
	 */
	public static final String FOLDER_CSS = "cls" ;
	
	/**
	 * JSON mail folder total
	 * value type : Long
	 */
	public static final String FOLDER_TOTAL = "total" ;
	
	/**
	 * JSON mail folder parent identifier
	 * value type : Long
	 */
	public static final String FOLDER_PARENT = "parent" ;
	
	/**
	 * JSON leaf folder
	 * value type : Boolean
	 */
	public static final String FOLDER_LEAF = "leaf" ;
	/**
	 * JSON mail folder unread
	 * value type : Long
	 */
	public static final String FOLDER_UNREAD = "unread" ;
	
	/**
	 * JSON mail folder children
	 * value type : JSON
	 */
	public static final String FOLDER_CHILDREN = "children" ;
	
	/**
	 * JSON mail folder type
	 * value type : JSON
	 */
	public static final String FOLDER_TYPE = "type";
	
	
	// CONTENT ================================================================================================
	
	/**
	 * JSON mail content identifier 
	 * value type : String
	 */
	public static final String CONTENT_ID = "contentId" ; 
	
	public static final String CONTENT_NEW_MESSAGE = "newMessages";
	
	// CONFIGURE =============================================================================================
	public static final String CFG_ID				= "id";
	
	public static final String CFG_DESCRIPTION		= "desc";
		
	public static final String CFG_OWNER			= "owner";
	
	public static final String CFG_ACTIVE			= "active";
	
	public static final String CFG_FULL_NAME 		= "name";
	
	public static final String CFG_DEFAULT_SMTP		= "smtp";
	
	public static final String CFG_SMTPS			= "smtps";
	
	public static final String CFG_SMTP_ACCOUNT		= "smtpAccount";
	
	public static final String CFG_ADDRESS 			= "address";
	
	public static final String CFG_ORGANIZATION 	= "org";
	
	public static final String CFG_SIGN_REMOVE		= "signRemove";
	
	public static final String CFG_SIGNATURE_DEFAULT= "sign";
	
	public static final String CFG_SIGNATURES 		= "signs";
	
	public static final String CFG_SERVER 			= "server";
	
	public static final String CFG_PORT				= "port";
	
	public static final String CFG_SECURITY 		= "security";
	
	public static final String CFG_USER				= "user";
	
	public static final String CFG_PASSWORD 		= "pass";
	
	public static final String CFG_AUTO_CHECK 		= "refresh";
	
	public static final String CFG_PROTOCOL 		= "protocol";

	public static final String CFG_LEAVE_MESSAGES	= "leave";
	
	public static final String CFG_COMMON_INFO		= "common";
	
	public static final String CFG_ACCOUNTS			= "accounts";
	
	public static final String CFG_SIGN_ID 			= "id";
	
	public static final String CFG_SIGN_NAME 		= "name";
	
	public static final String CFG_SIGN_CONTENT		= "content";
	
	public static final String CFG_SIGN_USED		= "used";
	
	public static final String CFG_SMTP_SERVER 		= "smtpserver";
	
	public static final String CFG_SMTP_PORT		= "smtpport";
	
	public static final String CFG_SMTP_SECURITY 	= "smtpsecurity";
	
	public static final String CFG_SMTP_USER		= "smtpuser";
	
	public static final String CFG_SMTP_PASSWORD 	= "smtppass";
	
	public static final String CFG_MODE_DETAIL		= "detail";
	
	public static final String CFG_MODE_SMTP		=  "smtp";
	
	public static final String CFG_MODE_SMTP_ACCOUNT=  "smtpAccount";
	
	public static final String CFG_MODE_PERSONAL	=  "personal";
	//
	
	public static final int CFG_PORT_SMTP			= 25;
		
	public static final int CFG_PORT_POP3			= 110;

	// ADDRESS ================================================================================================
	
	public static final String DISPLAY_NAME 		= "name";
	
	public static final String ADDRESS 			= "address";
	
	// SEARCH =================================================================================================
	public static final String START_PARAM 			= "start";
	
	public static final String LIMIT_PARAM 			= "limit";
	
	public static final String MODE 			= "mode";
	
	public static final String MODE_BASIC 			= "basic";
	
	public static final String MODE_ADVANCED 		= "advanced";
	
	public static final String MODE_VIEW			= "view";
	
	public static final String KEYWORD 			= "key";
	
	public static final String FIELD 			= "field";
	
	public static final String FIELD_ALL			= "all";
	
	public static final String FIELD_SUBJECT		= "subject";
	
	public static final String FIELD_DATE			= "date";
	
	public static final String FIELD_SENDER			= "sender";
	
	// ACTION ================================================================================================= 
	public static final String ACTION		 	= "action";
	
	public static final String ACTION_DELETE		= "delete";
	
	public static final String ACTION_CHECK_MAIL	        = "check";
	
	public static final String ACTION_SELECT		= "select";
	
	public static final String ACTION_LOAD			= "load";
	
	public static final String ACTION_SEARCH		= "search";
	
	public static final String ACTION_REPLY 		= "reply";
	
	public static final String ACTION_REPLY_ALL		= "replyA";
	
	public static final String ACTION_FORWARD 		= "forward";
	
	public static final String ACTION_DRAFT 		= "draft";
	
	public static final String ACTION_SAVE 			= "save";
	
	public static final String ACTION_UPDATE		= "update";
	
	public static final String ACTION_SEND 			= "send";
	
	public static final String ACTION_FOLDER_CREATE	        = "create";
	
	public static final String ACTION_FOLDER_DELETE	        = "delete";
	
	public static final String ACTION_FOLDER_RENAME	        = "rename";
	
	public static final String ACTION_FOLDER_UNREAD         = "unread";
	
	public static final String ACTION_FOLDER_DEFAULT        = "default";

	public static final String ACTION_FOLDER_BUILD	        = "build";
	
	public static final String ACTION_FOLDER_MOVE           = "move";
	
	public static final String ACTION_MARK_FLAG   	        = "flag";
	
	public static final String ACTION_MARK_PRIORITY         = "priority";
	
	public static final String ACTION_MARK_READ   	        = "read";
	
	public static final String ACTION_HEADER_DELETE         = "delete";
	
	public static final String ACTION_HEADER_MOVE	        = "move";
	
	public static final String ACTION_CFG_LOAD 		= "load";
	
	public static final String ACTION_CFG_UPDATE_PASSWORD	= "updatePW";
	
	public static final String ACTION_CFG_LOAD_DEFAULT	= "loadDefault";
	
	public static final String ACTION_OPEN_EML		= "openEml";
	
	public static final String ACTION_EMPTY_FOLDER	        = "emptyFolder";
	
	public static final String ACTION_FOLLOWUP_LOAD         = "load";
	
	public static final String ACTION_FOLLOWUP_ADD	        = "save";
	
	public static final String ACTION_FOLLOWUP_UPDATE       = "update";
	
	public static final String ACTION_MARK_AS_READ_FOLDER   = "markAsReadFolder";
	// USER CONTACT===================================================
	
	public static final String MODE_SEARCH_GLOBAL = "global";
	
	public static final String MODE_SEARCH_PERSONAL = "personal";
	
	public static final String CONTACT_ID = "id";
	
	public static final String CONTACT_EMAIL = "email";
	
	public static final String CONTACT_LEAF = "leaf";
	
	public static final String CONTACT_NAME = "text";
	
	public static final String CONTACT_CSS = "iconCls";
	
	public static final String CONTACT_CHILDREN = "children";
	
	// Composer ======================================================
	/**
	 * String
	 * {@value} Vao luc %s, %s da viet: 
	 */
	public static final String MSG_REPLY = "msgReply";
	
	// Mail Filter ===================================================
	
	public static final String FILTER_ID	= "id";
	
	public static final String FILTER_NAME	= "name";
	
	public static final String FILTER_FOLDER = "folder";
	
	public static final String FILTER_FOLDER_ID = "fId";
	
	public static final String FILTER_MAIL_CLAUSE = "clause";
	
	public static final String FILTER_MAIL_OPERATOR = "operator";
	
	public static final String FILTER_MAIL_CLAUSE_UNIT = "units";
	
	public static final String FILTER_OBJECT	= "object";
	
	public static final String FILTER_OPERATOR 	= "operator";
	
	public static final String FILTER_DATA		= "data";
	
	// Follow up 
	public static final String FOLLOWUPS = "followups";
	
	public static final String FOLLOWUP_ID = "id";
	
	public static final String FOLLOWUP_NAME = "name";
	
	public static final String FOLLOWUP_DATE = "date";
	
	public static final String FOLLOWUP_MAIL = "data";
	
	public static final String FOLLOWUP_LIST_MAIL = "mails";
	
	public static final String FOLLOWUP_IDS = "ids";
	
}


