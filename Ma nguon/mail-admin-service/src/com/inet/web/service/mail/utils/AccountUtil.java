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

import net.sf.json.JSONObject;

import com.inet.lotus.account.Account;

/**
 * AccountUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AccountUtil {
///////////////////////////////////////////////////////////
	///////////// The key for action///////////////////////////
	/**
	 * The key for action mode
	 */
	public static final String ACTION_MODE_PARAMETER = "action";
		
	/**
	 * The key for search mail account action action
	 */
	public static final String ACTION_SEARCH = "search";
	
	/**
	 * The key for saving action
	 */
	public static final String ACTION_SAVE = "save";
	
	/**
	 * The key for updating action
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * The key for deleting action
	 */
	public static final String ACTION_RESET_DELETE = "delete";
	
	/**
	 * The key for reseting password action
	 */
	public static final String ACTION_RESET_PWD = "resetPwd";
	
	/**
	 * The key for loading action
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The key for loading action
	 */
	public static final String ACTION_LOAD_FULL = "loadFull";
	
	///////////////////////////////////////////////////////////
	//////////The JSON key for search mail account/////////////
	/**
	 * The JSON key for first time searching
	 */
	public static final String ACCOUNT_SEARCH_FIRST = "first";
	
	/**
	 * The JSON key for first time searching
	 */
	public static final String ACCOUNT_SEARCH_START = "start";
	
	/**
	 * The JSON key for limit of searching
	 */
	public static final String ACCOUNT_SEARCH_LIMIT = "limit";
	
	/**
	 * The JSON key for page number of searching
	 */
	public static final String ACCOUNT_SEARCH_PAGE = "page";
	
	/**
	 * The JSON key for the key to search
	 */
	public static final String ACCOUNT_SEARCH_KEY = "key";
	
	/**
	 * The JSON key for the mark as deleted account
	 */
	public static final String ACCOUNT_DELETED_KEY = "deleted";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String ACCOUNT_SEARCH_RESULT_LIST = "rows";
	
	/**
	 * The JSON key to know the total of result
	 */
	public static final String ACCOUNT_SEARCH_TOTAL_RESULT = "results";
	
	/**
	 * The JSON key to know the end of result
	 */
	public static final String ACCOUNT_SEARCH_END_RESULT = "end";
	
	
	//////////////////////////////////////////////////////////////
	/////////////////////Account properties///////////////////////
	/**
	 * The key for mail domain object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/**
	 * The JSON key for user's name
	 */
	public static final String ACCOUNT_USER_NAME = "uname";
	
	/**
	 * The JSON key for user's password 
	 */
	public static final String ACCOUNT_PASSWORD = "pwd";
	
	/**
	 * The JSON key for user's code 
	 */
	public static final String ACCOUNT_USER_CODE = "userCode";
	
	/**
	 * The JSON key for user's email 
	 */	
	public static final String ACCOUNT_EMAIL = "email";
	
	/**
	 * The JSON key for time zone 
	 */
	public static final String ACCOUNT_TIMEZONE = "tzone";
	
	/**
	 * The JSON key for country
	 */
	public static final String ACCOUNT_COUNTRY = "country";
	
	/**
	 * The JSON key for city
	 */
	public static final String ACCOUNT_CITY = "city";
	
	/**
	 * The JSON key for province/state
	 */
	public static final String ACCOUNT_STATE = "state";
	
	/**
	 * The JSON key for first name 
	 */
	public static final String ACCOUNT_FIRST_NAME = "fname";
	
	/**
	 * The JSON key for middle name
	 */
	public static final String ACCOUNT_MIDDLE_NAME = "mname";
	
	/**
	 * The JSON key for last name
	 */
	public static final String ACCOUNT_LAST_NAME = "lname";
	
	/**
	 * The JSON key for language 
	 */
	public static final String ACCOUNT_LANGUAGE = "language";
	
	/**
	 * The JSON key for birthday 
	 */
	public static final String ACCOUNT_BIRTHDAY = "birthday";
	
	/**
	 * The JSON key for gentle 
	 */
	public static final String ACCOUNT_GENTLE = "gentle";
	
	/**
	 * The JSON key for post code 
	 */
	public static final String ACCOUNT_POSTCODE = "pcode";
	
	/**
	 * The JSON key for editing on account
	 */
	public static final String ACCOUNT_EDITATBLE = "edit";
	
	/**
	 * The JSON key for list of email accounts
	 */
	public static final String ACCOUNT_EMAIL_ACCOUNTS = "list";	
	
	/**
	 * convert account to JSON object
	 * 
	 * @param account Account - the account
	 * @return JSONObject - the JSON object
	 */
	public static JSONObject convertAccountToJSON(Account account) {
		JSONObject object = new JSONObject();
		object.accumulate(AccountUtil.ACCOUNT_USER_NAME, account.getName())
			  .accumulate(AccountUtil.ACCOUNT_USER_CODE, account.getCode())
			  .accumulate(AccountUtil.ACCOUNT_EMAIL, account.getEmail())
			  .accumulate(AccountUtil.ACCOUNT_LAST_NAME, account.getLastName())
			  .accumulate(AccountUtil.ACCOUNT_MIDDLE_NAME, account.getMiddleName())
			  .accumulate(AccountUtil.ACCOUNT_FIRST_NAME, account.getFirstName())
			  .accumulate(AccountUtil.ACCOUNT_BIRTHDAY, account.getBirthday()==null?0:account.getBirthday().getTime())
			  .accumulate(AccountUtil.ACCOUNT_GENTLE, account.getGender())
			  .accumulate(AccountUtil.ACCOUNT_CITY, account.getCity())
			  .accumulate(AccountUtil.ACCOUNT_COUNTRY, account.getCountry())
			  .accumulate(AccountUtil.ACCOUNT_LANGUAGE, account.getLanguage())
			  .accumulate(AccountUtil.ACCOUNT_STATE, account.getState())
			  .accumulate(AccountUtil.ACCOUNT_POSTCODE, account.getPostalCode())
			  .accumulate(AccountUtil.ACCOUNT_TIMEZONE, account.getTimeZone());
			  
		return object;
	}

}
