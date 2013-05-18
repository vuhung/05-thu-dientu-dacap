/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.lotus.mail.MailAlias;
import com.inet.web.common.json.JSONService;

/**
 * MailAliasUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAliasUtil {
	///////////////////////////////////////////////////////////
	///////////// The key for action///////////////////////////
	/**
	 * The key for action mode
	 */
	public static final String ACTION_MODE_PARAMETER = "action";
	
	/**
	 * The key for save action
	 */
	public static final String ACTION_SAVE = "save";
	
	/**
	 * The key for update action
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_DELETE = "delete";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_ACTIVE = "active";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_SMTP_AUTH = "smtpAuth";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_VIRUS_CHECK = "checkVirus";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_SPAM_CHECK = "checkSpam";
	
	/**
	 * The key for load alias action
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The key for search alias action
	 */
	public static final String ACTION_SEARCH = "search";
	
	
	///////////////////////////////////////////////////////
	///////The JSON key for alias information//////////////
	/**
	 * The key for mail domain object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/**
	 * The JSON key for email address
	 */
	public static final String ALIAS_MAIL_ADDRESS = "email";
	
	/**
	 * The JSON key for mail forward
	 */
	public static final String ALIAS_DEPARTMENT_NUMBER = "depNumber";
	
	/**
	 * The JSON key for password of alias
	 */
	public static final String ALIAS_PASSWORD = "pwd";
	
	/**
	 * The JSON key for name of alias
	 */
	public static final String ALIAS_NAME = "fname";
	
	/**
	 * The JSON key for display name of user
	 */
	public static final String MAIL_DISPLAY_NAME = "displayname";
	
	/**
	 * The JSON key for last name of alias
	 */
	public static final String ALIAS_LAST_NAME = "lastName";
	
	/**
	 * The JSON key for active account of alias
	 */
	public static final String ALIAS_ACCOUNT_ACTIVE = "active";
	
	/**
	 * The JSON key for SMTP authenticate
	 */
	public static final String ALIAS_SMTP_AUTH = "smtp";
	
	/**
	 * The JSON key for virus checking
	 */
	public static final String ALIAS_VIRUS_CHECKING = "virus";
	
	/**
	 * The JSON key for SPAM checking
	 */
	public static final String ALIAS_SPAM_CHECKING = "spam";
	
	/**
	 * The JSON key for list of mail accounts
	 */
	public static final String ALIAS_ACCOUNTS_LIST = "list";
	
	/**
	 * The modify date
	 */
	public static final String ALIAS_CREATE_DATE = "date";
	
	///////////////////////////////////////////////////////////
	//////////The JSON key for search mail alias///////////////
	/**
	 * The JSON key for first time searching
	 */
	public static final String MAIL_SEARCH_FIRST = "first";
	
	/**
	 * The JSON key for first time searching
	 */
	public static final String MAIL_SEARCH_START = "start";
	
	/**
	 * The JSON key for limit of searching
	 */
	public static final String MAIL_SEARCH_LIMIT = "limit";
	
	/**
	 * The JSON key for page number of searching
	 */
	public static final String MAIL_SEARCH_PAGE = "page";
	
	/**
	 * The JSON key for the key to search
	 */
	public static final String MAIL_SEARCH_KEY = "key";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String MAIL_SEARCH_RESULT_LIST = "list";
	
	/**
	 * The JSON key to know the end of result
	 */
	public static final String MAIL_SEARCH_END_RESULT = "end";
	
	/**
	 * The JSON key for the domain to search
	 */
	public static final String MAIL_SEARCH_DOMAIN = "domain";
	
	
	/**
	 * convert mail alias to JSON object
	 * 
	 * @param alias MailAlias - the given mail alias
	 * @return JSON - the returned JSON object
	 */
	public static JSONObject convertToJSON(MailAlias alias) {
		JSONObject object = new JSONObject();
		object.accumulate(MailAliasUtil.ALIAS_MAIL_ADDRESS, alias.getEmail())
			  .accumulate(MailAliasUtil.ALIAS_NAME, alias.getFullName())
			  .accumulate(MailAliasUtil.ALIAS_LAST_NAME, alias.getLastName())
			  .accumulate(MailAliasUtil.ALIAS_ACCOUNT_ACTIVE, alias.isAccountActive())
			  .accumulate(MailAliasUtil.ALIAS_SMTP_AUTH, alias.isSmtpAuth())
			  .accumulate(MailAliasUtil.ALIAS_VIRUS_CHECKING, alias.isAmavisBypassVirusChecks())
			  .accumulate(MailAliasUtil.ALIAS_SPAM_CHECKING, alias.isAmavisBypassSpamChecks());
		
		// get list of mail drop
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json;
		for(String email : alias.getMailDrop()) {
			json = new JSONObject();
			json.accumulate(MailAliasUtil.ALIAS_MAIL_ADDRESS, email);
			// put JSON object to list
			jsons.add(json);
		}
		// put mail drop to JSON object
		object.accumulate(MailAliasUtil.ALIAS_ACCOUNTS_LIST, JSONService.toJSONArray(jsons));
		
		return object;
	}

}
