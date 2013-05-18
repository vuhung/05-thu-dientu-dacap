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
import java.util.Date;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.conf.Configuration;

/**
 * MailAccountUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAccountUtil {	
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
	 * The key for delete all action
	 */
	public static final String ACTION_DELETE_ALL = "deleteAll";
	
	/**
	 * The key for active action
	 */
	public static final String ACTION_ACTIVE = "active";
	
	/**
	 * The key for active all mail accounts action
	 */
	public static final String ACTION_ACTIVE_ALL = "activeAll";
	
	/**
	 * The key for SMTP authentication action
	 */
	public static final String ACTION_SMTP_AUTH = "smtpAuth";
	
	/**
	 * The key for forward action
	 */
	public static final String ACTION_FORWARD = "forward";
	
	/**
	 * The key for virus check action
	 */
	public static final String ACTION_VIRUS_CHECK = "checkVirus";
	
	/**
	 * The key for SPAM check action
	 */
	public static final String ACTION_SPAM_CHECK = "checkSpam";
	
	/**
	 * The key for load mail account action action
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The key for load mail account action action
	 */
	public static final String ACTION_LOAD_ALL = "loadall";
	
	/**
	 * The key for search mail account action action
	 */
	public static final String ACTION_SEARCH = "search";
	
	///////////////////////////////////////////////////////
	///////The JSON key for alias information//////////////
	/**
	 * The key for mail domain object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/**
	 * The key for changing owner of mail account
	 */
	public static final String MAIL_CHANGE_OWNER_ACCOUNT = "chgOwner";
	
	/**
	 * The JSON key for email address
	 */
	public static final String MAIL_ADDRESS = "email";
	
	/**
	 * The JSON key for password of email
	 */
	public static final String MAIL_PASSWORD = "pwd";
	
	/**
	 * The JSON key for full name of user
	 */
	public static final String MAIL_FULL_NAME = "fname";
	
	/**
	 * The JSON key for display name of user
	 */
	public static final String MAIL_DISPLAY_NAME = "displayname";
	
	/**
	 * The JSON key for last name of user
	 */
	public static final String MAIL_LAST_NAME = "lname";
	
	/**
	 * The JSON key for group of email
	 */
	public static final String MAIL_GROUP = "group";
	
	/**
	 * The JSON key for list of groups
	 */
	public static final String MAIL_GROUPS = "groups";
	
	/**
	 * The JSON key for SMTP authenticate
	 */
	public static final String MAIL_SMTP_AUTH = "smtp";
	
	/**
	 * The JSON key for capacity of mail
	 */
	public static final String MAIL_CAPACITY = "capacity";
	
	/**
	 * The JSON key for account active
	 */
	public static final String MAIL_ACCOUNT_ACTIVE = "active";
	
	/**
	 * The JSON key for mail forward
	 */
	public static final String MAIL_FORWARD = "forward";
	
	/**
	 * The JSON key for forward address
	 */
	public static final String MAIL_FORWARD_ADDRESS = "fAddress";
	
	/**
	 * The JSON key for virus checking
	 */
	public static final String MAIL_VIRUS_CHECKING = "virus";
	
	/**
	 * The JSON key for SPAM checking
	 */
	public static final String MAIL_SPAM_CHECKING = "spam";
	
	/**
	 * The JSON key for create date
	 */
	public static final String MAIL_CREATE_DATE = "date";
	
	/**
	 * The JSON key for create date
	 */
	public static final String MAIL_UID = "uid";
	
	/**
	 * The JSON key for create date
	 */
	public static final String MAIL_SYMBOL = "symbol";
	
	/**
	 * The JSON key for create date
	 */
	public static final String MAIL_DOMAIN = "domain";
	
	/**
	 * The JSON key for user's code 
	 */
	public static final String ACCOUNT_CODE = "code";
	
	/**
	 * The JSON key for user's name 
	 */
	public static final String ACCOUNT_USER_NAME = "uname";
	
	/**
	 * The JSON key for type of data
	 */
	public static final String MAIL_IS_MAIL_ACCOUNT = "isMail";
	
	/**
	 * The JSON key for type of data
	 */
	public static final String MAIL_EDIT_ACCOUNT = "edit";
	
	///////////////////////////////////////////////////////////
	//////////The JSON key for search mail account/////////////
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
	 * The JSON key for the domain to search
	 */
	public static final String MAIL_SEARCH_DOMAIN = "domain";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String MAIL_LIST_ADDRESS = "list";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String MAIL_SEARCH_RESULT_LIST = "rows";	
	
	/**
	 * The JSON key to know the total of result
	 */
	public static final String MAIL_SEARCH_TOTAL_RESULT = "results";
	
	/**
	 * The JSON key to know the end of result
	 */
	public static final String MAIL_SEARCH_END_RESULT = "end";
	
	/**
	 * convert mail account to JSON
	 * 
	 * @param account MailAccount - the mail account
	 * @return JSONObject - the mail account as JSON
	 */
	public static JSONObject convertToJSON(MailAccount account) {
		JSONObject object = new JSONObject();
		object.accumulate(MailAccountUtil.MAIL_ADDRESS, account.getEmail())
			  .accumulate(MailAccountUtil.ACCOUNT_CODE, account.getUserCode())
			  .accumulate(MailAccountUtil.MAIL_FULL_NAME, account.getFullName())
			  .accumulate(MailAccountUtil.MAIL_LAST_NAME, account.getLastName())
			  .accumulate(MailAccountUtil.MAIL_SMTP_AUTH, account.isSmtpAuth())
			  .accumulate(MailAccountUtil.MAIL_CAPACITY,
					  MailCommonUtils.getMByte(Long.valueOf(account.getQuota())) + "")
			  .accumulate(MailAccountUtil.MAIL_ACCOUNT_ACTIVE, account.isAccountActive())
			  .accumulate(MailAccountUtil.MAIL_FORWARD, account.isForwardActive())
			  .accumulate(MailAccountUtil.MAIL_VIRUS_CHECKING, account.isAmavisBypassVirusChecks())
			  .accumulate(MailAccountUtil.MAIL_SPAM_CHECKING, account.isAmavisBypassSpamChecks());
		if(account.getDepartments() != null && account.getDepartments().size() > 0) {
			List<JSONObject> jsons = new ArrayList<JSONObject>(account.getDepartments().size());
			for(String group : account.getDepartments()) {
				// put group to JSON array
				jsons.add(new JSONObject().accumulate(MailAccountUtil.MAIL_GROUP, group));
			}
			object.accumulate(MailAccountUtil.MAIL_GROUPS, JSONService.toJSONArray(jsons));
		}
		return object;
	}
	
	/**
	 * convert list of mail account to JSON
	 * 
	 * @param mailAccounts List<LdapMailAccount> - the list of email account
	 * @return JSON - The JSON array
	 */
	public static JSON convertToJSONFull(List<LdapMailAccount> mailAccounts) {
		// create list to contain JSON object
		List<JSONObject> objects = new ArrayList<JSONObject>();
		String mailSymbol = "@";
		JSONObject object;		
		for(LdapMailAccount account : mailAccounts) {
			object = new JSONObject();
			String[] strs = account.getEmail().split(mailSymbol);
			object.accumulate(MailAccountUtil.MAIL_ADDRESS, account.getEmail())
				  .accumulate(MailAccountUtil.ACCOUNT_CODE, account.getUserCode())
				  .accumulate(MailAccountUtil.ACCOUNT_USER_NAME, account.getUserName())
				  .accumulate(MailAccountUtil.MAIL_UID, strs[0])
				  .accumulate(MailAccountUtil.MAIL_SYMBOL, mailSymbol)
				  .accumulate(MailAccountUtil.MAIL_DOMAIN, strs[1])
				  .accumulate(MailAccountUtil.MAIL_FULL_NAME, account.getFullName())
				  .accumulate(MailAccountUtil.MAIL_LAST_NAME, account.getLastName())
				  .accumulate(MailAccountUtil.MAIL_SMTP_AUTH, account.isSmtpAuth())
				  .accumulate(MailAccountUtil.MAIL_CAPACITY,
						  MailCommonUtils.getMByte(Long.valueOf(account.getQuota())) + "")
				  .accumulate(MailAccountUtil.MAIL_ACCOUNT_ACTIVE, account.isAccountActive())
				  .accumulate(MailAccountUtil.MAIL_FORWARD, account.isForwardActive())
				  .accumulate(MailAccountUtil.MAIL_FORWARD_ADDRESS, account.getForwardAddress())
				  .accumulate(MailAccountUtil.MAIL_VIRUS_CHECKING, account.isAmavisBypassVirusChecks())
				  .accumulate(MailAccountUtil.MAIL_SPAM_CHECKING, account.isAmavisBypassSpamChecks());
			if(account.getDepartments() != null && account.getDepartments().size() > 0) {
				List<JSONObject> jsons = new ArrayList<JSONObject>(account.getDepartments().size());
				for(String group : account.getDepartments()) {
					// put group to JSON array
					jsons.add(new JSONObject().accumulate(MailAccountUtil.MAIL_GROUP, group));
				}
				object.accumulate(MailAccountUtil.MAIL_GROUPS, JSONService.toJSONArray(jsons));
			}
			objects.add(object);
		}
		return JSONService.toJSONArray(objects);
	}
	
	/**
	 * convert list of mail account to JSON
	 * 
	 * @param mailAccounts List<LdapMailAccount> - the list of email account
	 * @return JSON - The JSON array
	 */
	public static JSON convertToJSON(List<LdapMailAccount> mailAccounts) {
		// create list to contain JSON object
		List<JSONObject> objects = new ArrayList<JSONObject>();
		String mailSymbol = "@";
		JSONObject object;		
		for(LdapMailAccount account : mailAccounts) {
			object = new JSONObject();
			String[] strs = account.getEmail().split(mailSymbol);
			object.accumulate(MailAccountUtil.MAIL_ADDRESS, account.getEmail())
				  .accumulate(MailAccountUtil.MAIL_UID, strs[0])
				  .accumulate(MailAccountUtil.MAIL_SYMBOL, mailSymbol)
				  .accumulate(MailAccountUtil.MAIL_DOMAIN, strs[1]);
			objects.add(object);
		}
		return JSONService.toJSONArray(objects);
	}
	
	/**
	 * initialize mail account from configuration 
	 * 
	 * @param config Configuration - the mail configuration
	 * @return MailAccount - the new mail account instance
	 */
	public static LdapMailAccount initMailAccount(Configuration config) {
		// create new mail account
		LdapMailAccount mailAccount = new LdapMailAccount();
		
		// fill configured information to mail account
		mailAccount.setVdHome(config.getDomainHome());
		mailAccount.setOtherTransport(config.getOtherTransport());
		mailAccount.setAmavisSpamKillLevel(config.getSpamKillLevel());
		mailAccount.setAmavisSpamTag2Level(config.getSpamTag2Level());
		mailAccount.setAmavisSpamTagLevel(config.getSpamTagLevel());
		// other information
		mailAccount.setCreateDate(new Date());
		mailAccount.setLastChange(new Date());
		
		return mailAccount;
	}
	
	/**
	 * refresh mail account from given JSON object
	 * 
	 * @param mailAccount MailAccount - the mail account
	 * @param object JSONObject - the given JSON object
	 */
	public static void refreshMailAccount(MailAccount mailAccount, JSONObject object) {
		mailAccount.setUserCode(WebCommonService.toString(object, MailAccountUtil.ACCOUNT_CODE));
		mailAccount.setUserName(WebCommonService.toString(object, MailAccountUtil.ACCOUNT_USER_NAME));
		mailAccount.setFullName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, MailAccountUtil.MAIL_FULL_NAME)));		
		mailAccount.setLastName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, MailAccountUtil.MAIL_LAST_NAME)));		
		mailAccount.setSmtpAuth(WebCommonService.toBool(object, MailAccountUtil.MAIL_SMTP_AUTH));
		mailAccount.setQuota(MailCommonUtils.getByte(
				Long.valueOf(WebCommonService.toString(object, MailAccountUtil.MAIL_CAPACITY))) + "");
		mailAccount.setAccountActive(WebCommonService.toBool(object, MailAccountUtil.MAIL_ACCOUNT_ACTIVE));
		mailAccount.setForwardActive(WebCommonService.toBool(object, MailAccountUtil.MAIL_FORWARD));
		mailAccount.setForwardAddress(MailCommonUtils.getLdapValue(WebCommonService.toString(object, MailAccountUtil.MAIL_FORWARD_ADDRESS)));
		mailAccount.setAmavisBypassVirusChecks(
				WebCommonService.toBool(object, MailAccountUtil.MAIL_VIRUS_CHECKING));
		mailAccount.setAmavisBypassSpamChecks(
				WebCommonService.toBool(object, MailAccountUtil.MAIL_SPAM_CHECKING));
		// get the list of group
		JSONArray groups = WebCommonService.toArray(object, MailAccountUtil.MAIL_GROUPS);
		if(groups != null && groups.size() > 0) {
			List<String> groupNames = new ArrayList<String>(groups.size());
			for (int index = 0; index < groups.size(); index++) {
				String group = WebCommonService.toString(groups.getJSONObject(index), MailAccountUtil.MAIL_GROUP);
				if(StringService.hasLength(group) && !group.equals(GroupUtil.NONE_GROUP)) {
					groupNames.add(group);
				}				
			}
			mailAccount.setDepartments(groupNames);
		}
	}
	
	/**
	 * get mail box from given email address
	 * 
	 * @param email String - the email address
	 * @return String - the mail box
	 */
	public static String getMailBox(String email) {
		if(!MailUtil.isEmailAddress(email)) return null;
		
		String[] strs = email.split("@");
		
		// return the mail box
		return strs[1] + IOService.FILE_SEPARATOR + strs[0] + IOService.FILE_SEPARATOR;
	}
	
}
