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

import com.inet.lotus.account.Account;
import com.inet.lotus.org.permission.User;
import com.inet.web.common.json.JSONService;

import net.sf.json.JSONObject;

/**
 * UserUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class UserUtil {

	///////////////////////////////////////////////////////////
	///////////// The keys for action///////////////////////////
	/**
	 * The key for action mode
	 */
	public static final String ACTION_MODE_PARAMETER = "action";
	
	/**
	 * The key for add user action
	 */
	public static final String ACTION_ADD = "save";
	
	/**
	 * The key for update user action
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * The key for checking user name action
	 */
	public static final String ACTION_CHECK_USER_NAME = "checkuname";
	
	/**
	 * The key for reset password action
	 */
	public static final String ACTION_RESET_PWD = "resetpwd";
	
	/**
	 * The key for checking permission to delete user action
	 */
	public static final String ACTION_CAN_DELETE_DELETE = "candelete";
	
	/**
	 * The key for update user action
	 */
	public static final String ACTION_DELETE = "delete";
	
	/**
	 * The key for load full user information action
	 */
	public static final String ACTION_LOAD_FULL = "loadfull";
	
	/**
	 * The key for load user information action
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The key for load user information by lotus identify
	 */
	public static final String ACTION_LOAD_BY_LOTUS_ID = "loadlotus";
	
	/**
	 * The key for load user information and email account by lotus identify
	 */
	public static final String ACTION_LOAD_FULL_BY_LOTUS_ID = "loadfulllotus";
	
	/**
	 * The key for search user action
	 */
	public static final String ACTION_SEARCH = "search";
	
	/**
	 * The JSOn key for request object
	 */
	public static final String OBJECT_PARAM = "object";
	
	////////////////////////////////////////////////////////////
	/**
	 * The constant for deleting manager error
	 */
	public static final String ERROR_DELETE_MANAGER = "manager";
	
	/**
	 * The constant for adding existed user
	 */
	public static final String ERROR_ADD_USER_EXIST_IN_ORG = "exist";
	
	/**
	 * The constant for invalid user name
	 */
	public static final String ERROR_INPUT_INVALID_USER_NAME = "invaliduname";
	
	/**
	 * The constant for not finding 
	 */
	public static final String ERROR_NO_EXIST = "noexist";
	
	/**
	 * The constant for not having permission
	 */
	public static final String ERROR_NO_HAVE_PERMISSION = "nopermit";
	
	////////////////////////////////////////////////////////////
	//////////// The JSON keys for user information ////////////
	/**
	 * The JSON key for user name 
	 */
	public static final String USER_NAME = "uname";
	
	/**
	 * The JSON key for display user name
	 */
	public static final String USER_DISPLAY_USER_NAME = "duname";
	
	/**
	 * The JSON key for last name
	 */
	public static final String USER_LAST_NAME = "lname";
	
	/**
	 * The JSON key for middle name
	 */
	public static final String USER_MIDDLE_NAME = "mname";
	
	/**
	 * The JSON key for first name
	 */
	public static final String USER_FIRST_NAME = "fname";
	
	/**
	 * The JSON key for full name
	 */
	public static final String USER_FULL_NAME = "fullname";
	
	/**
	 * The JSON key for password 
	 */
	public static final String USER_PASSWORD = "pwd";
	
	/**
	 * The JSON key for email address 
	 */
	public static final String USER_EMAIL = "email";
	
	/**
	 * The JSON key for mobile
	 */
	public static final String USER_MOBILE = "mobile";
	
	/**
	 * The JSON key for title 
	 */
	public static final String USER_TITLE = "title";
	
	
	/**
	 * The JSON key for employee number 
	 */
	public static final String USER_EMPLOYEE_NUMBER = "employee";
	
	/**
	 * The JSON key for work phone
	 */
	public static final String USER_WORK_PHONE = "wphone";
	
	/**
	 * The JSON key for home phone
	 */
	public static final String USER_HOME_PHONE = "hphone";
	
	/**
	 * The JSON key for organization 
	 */
	public static final String USER_ORGANIZATION = "org";
	
	/**
	 * The JSON key for edit permission 
	 */
	public static final String USER_EDIT = "edit";
	
	/**
	 * The JSON key for group 
	 */
	public static final String USER_GROUP = "group";
	
	/**
	 * The JSON key for list of groups 
	 */
	public static final String USER_GROUPS = "groups";
	
	/**
	 * The JSON key for list of email accounts 
	 */
	public static final String USER_EMAIL_DEL_ALL = "delall";
	
	/**
	 * The JSON key for list of email accounts 
	 */
	public static final String USER_EMAIL_ACCOUNTS = "list";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String USER_SEARCH_RESULT_LIST = "rows";
	
	/**
	 * The JSON key to know the total of result
	 */
	public static final String USER_SEARCH_TOTAL_RESULT = "results";
	
	/**
	 * The JSON key for limit of searching
	 */
	public static final String USER_SEARCH_LIMIT = "limit";
	
	/**
	 * The JSON key for first time searching
	 */
	public static final String USER_SEARCH_START = "start";
	
	/**
	 * The JSON key for the key to search
	 */
	public static final String USER_SEARCH_KEY = "key";
	
	/**
	 * The JSON key for checking exist
	 */
	public static final String USER_EXIST_UNAME = "exist";
	
	/**
	 * The JSON key for error code
	 */
	public static final String USER_ERROR_CODE = "error";
	
	
	/**
	 * convert the user information to JSON
	 * 
	 * @param user User - the user information
	 * @return JSONObject - the return JSON object
	 */
	public static JSONObject convertToJSON(User user) {
		JSONObject object = new JSONObject();
		object.accumulate(USER_NAME, user.getName())
			  .accumulate(USER_EMAIL, user.getEmail())
			  .accumulate(USER_FIRST_NAME, user.getFirstName())
			  .accumulate(USER_LAST_NAME, user.getLastName())
			  .accumulate(USER_MIDDLE_NAME, user.getMiddleName())
			  .accumulate(USER_MOBILE, user.getMobile())
			  .accumulate(USER_TITLE, user.getTitle())
			  .accumulate(USER_EMPLOYEE_NUMBER, user.getEmployeeNumber())
			  .accumulate(USER_WORK_PHONE, user.getWorkPhone())
			  .accumulate(USER_HOME_PHONE, user.getHomePhone())
			  .accumulate(USER_PASSWORD, user.getPassword());
		if(user.getGroups() != null && user.getGroups().size() > 0) {
			List<JSONObject> jsons = new ArrayList<JSONObject>(user.getGroups().size());
			for(String group : user.getGroups()) {
				// put group to JSON array
				jsons.add(new JSONObject().accumulate(USER_GROUP, group));
			}
			object.accumulate(USER_GROUPS, JSONService.toJSONArray(jsons));
		}
		return object;
	}
	
	/**
	 * convert the account information to JSON as user
	 * 
	 * @param account Account - the account information
	 * @return JSONObject - the return JSON object
	 */
	public static JSONObject convertToJSON(Account account) {
		JSONObject object = new JSONObject();
		object.accumulate(USER_NAME, account.getName())
			  .accumulate(USER_EMAIL, account.getEmail())
			  .accumulate(USER_FIRST_NAME, account.getFirstName())
			  .accumulate(USER_LAST_NAME, account.getLastName())
			  .accumulate(USER_MIDDLE_NAME, account.getMiddleName())
			  .accumulate(USER_PASSWORD, account.getPassword());
		return object;
	}
}
