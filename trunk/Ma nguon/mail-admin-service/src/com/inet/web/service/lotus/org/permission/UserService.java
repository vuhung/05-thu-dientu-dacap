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
package com.inet.web.service.lotus.org.permission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.conf.MailConfiguration;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.lotus.mail.permission.LotusRole;
import com.inet.lotus.mail.permission.Permission;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.LdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.LdapUserManager;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.conf.ClientChagePwdData;
import com.inet.web.service.conf.ClientConfigurationData;
import com.inet.web.service.conf.HandleChangePwdMailService;
import com.inet.web.service.conf.HandleSavingMailService;
import com.inet.web.service.conf.ClientConfigurationData.ACTION;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.GroupUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.PermissionCacheService;
import com.inet.web.service.mail.utils.UserUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * UserService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class UserService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(UserService.class);
	// The business object
	private LdapAccountManager accountManager;
	private LdapUserManager userManager;
	private LdapMailAccountManager mailAccountManager;
	private LdapGroupManager groupManager;
	private MailConfiguration mailConfiguration;
	private List<HandleSavingMailService> handleSavingMailServices;
	private List<HandleChangePwdMailService> handleChangePwdMailService;
	// the hash method
	private String hashMethod;
	private String country = "vn";
	private String timezone = "+0700";

	/**
	 * Create <tt>UserService</tt> instance
	 * 
	 * @param accountManager LdapAccountManager - the account manager
	 * @param userManager LdapUserManager - the user manager
	 * @param mailAccountManager LdapMailAccountManager - the mail account manager
	 * @param groupManager LdapGroupManager - the group manager
	 * @param mailConfiguration MailConfiguration - the mail configuration
	 * @param hashMethod String - the password schema
	 */
	public UserService(LdapAccountManager accountManager, LdapUserManager userManager,
			LdapMailAccountManager mailAccountManager, LdapGroupManager groupManager,
			MailConfiguration mailConfiguration, String hashMethod) {
		this.accountManager = accountManager;
		this.hashMethod = hashMethod;
		this.mailAccountManager = mailAccountManager;
		this.userManager = userManager;
		this.groupManager = groupManager;
		this.mailConfiguration = mailConfiguration;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, UserUtil.ACTION_MODE_PARAMETER);
		if(UserUtil.ACTION_ADD.equals(action)) {
			// add new user information
			return this.add(request);
		} else if(UserUtil.ACTION_CHECK_USER_NAME.equals(action)) {
			// checking the user name
			return this.isExist(request);
		} else if(UserUtil.ACTION_UPDATE.equals(action)) {
			// update user information
			return this.update(request);
		} else if(UserUtil.ACTION_CAN_DELETE_DELETE.equals(action)) {
			// checking before delete user
			return this.checkDelete(request);
		} else if(UserUtil.ACTION_DELETE.equals(action)) {
			// delete user
			return this.delete(request);
		} else if(UserUtil.ACTION_RESET_PWD.equals(action)) {
			return this.resetPwd(request);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * Add new user information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON object
	 */
	protected JSON add(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, UserUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user name
		String name = WebCommonService.toString(object, UserUtil.USER_DISPLAY_USER_NAME);
		String organization = WebCommonService.toString(object, UserUtil.USER_ORGANIZATION);
		// generate user name depend on configuration
		name = MailCommonUtils.getUserName(name, organization, this.mailConfiguration);
		
		// checking user name 
		if(!StringService.hasLength(name)) {
			logger.debug("The invalid input data");
			JSONObject failure = new JSONObject().accumulate("success", false);						
			// add the type of error
			failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_INPUT_INVALID_USER_NAME);
			return failure;
		}
		// replace user name field
		object.accumulate(UserUtil.USER_NAME, name);
		
		String password = WebCommonService.toString(object, UserUtil.USER_PASSWORD);
		// get the digest password		
		String digestPwd = MailCommonUtils.getLdapPassword(password, this.hashMethod);
		String code = null;
		
		try {
			if(this.accountManager != null) {
				// find user information on lotus account
				LdapAccount account = this.accountManager.findByName(name, true);
				if(account != null) {
					JSONObject failure = new JSONObject().accumulate("success", false);						
					// add the type of error
					failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_ADD_USER_EXIST_IN_ORG);
					return failure;
				}
				
				// add new lotus account
				code = this.addLotusAccount(object, digestPwd);
			}
			
			// create an user instance
			LdapUser user = new LdapUser();
			// refresh the user information
			this.refreshUserInfo(user, object);
			user.setPassword(digestPwd);
			
			// add the user information to organization
			this.userManager.addUser(organization, user);
			
			//update group
			if(user.getGroups() != null && user.getGroups().size() > 0) {
				// there is one group added when create user
				this.groupManager.addMember(organization, user.getGroups().get(0), user.getName());
			}
				
			// add mail accounts
			if(!this.addMailAccounts(
					WebCommonService.toArray(object, UserUtil.USER_EMAIL_ACCOUNTS),
					code, name, digestPwd, password)) return FAILURE_JSON;
			
			// prepare data to return
			JSONObject returnObject = new JSONObject().accumulate(UserUtil.USER_NAME, name);			
			List<LdapMailAccount> mailAccs = this.mailAccountManager.findByUserName(name);
			if(mailAccs != null && mailAccs.size() != 0) {
				returnObject.accumulate(AccountUtil.ACCOUNT_EMAIL_ACCOUNTS,
						MailAccountUtil.convertToJSONFull(mailAccs));
			}
			return returnObject;
		} catch (LotusException ex) {
			logger.error("Error while add user", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Add new lotus account
	 * 
	 * @param object JSONObject - the given JSON object
	 * @param pwd String - the given digest password
	 * @return String - the user code 
	 * @throws LotusException - when there is any error happens
	 */
	private String addLotusAccount(JSONObject object, String pwd) throws LotusException {
		// create new LDAP account instance
		LdapAccount account = new LdapAccount();
		// refresh account information
		this.refreshAccount(account, object);
		account.setPassword(pwd);
		// add lotus account
		return this.accountManager.addAccount(account).getCode();
	}
	
	/**
	 * create email accounts
	 * 
	 * @param mailAccounts JSONArray - the array of mail account as JSON object
	 * @param userCode String - the user code
	 * @param uName String - the given user name
	 * @param digestPwd String - the digest password
	 * @param textPwd String the clean text password
	 * @return boolean - the flag to know creating account success or not
	 */
	private boolean addMailAccounts(JSONArray mailAccounts, String userCode, String uName
	    ,String digestPwd, String textPwd) {
		if(mailAccounts == null || mailAccounts.size() == 0) return true;
		
		// create mail account object from configuration
		LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
		LdapMailAccount[] cacheAccounts = new LdapMailAccount[mailAccounts.size()];
		LdapMailAccount temp = null;
		for (int index = 0; index < mailAccounts.size(); index++) {
			try {
				// get the JSON object contain all information
				JSONObject object = mailAccounts.getJSONObject(index);
				
				// refresh the information for mail account
				MailAccountUtil.refreshMailAccount(mailAccount, object);
				
				// get the email address
				String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
				mailAccount.setEmail(email);
				mailAccount.setMailBox(MailAccountUtil.getMailBox(email));			
				
				// fill the user code
				mailAccount.setUserCode(userCode);
				mailAccount.setUserName(uName);
				mailAccount.setPassword(digestPwd);
				
				//insert new mail account to database
				this.mailAccountManager.add(mailAccount);
				// launch new object for next using
				temp = IOService.makeNewObject(mailAccount, LdapMailAccount.class);
				temp.setPassword(textPwd);
				cacheAccounts[index] = temp;				
			} catch (Exception ex) {
				logger.error("ERROR during adding mail account", ex);
				return false;
			}
		}
		// saving configuration for mail client
		this.handleAddAccount(new ClientConfigurationData(ACTION.ADD, cacheAccounts));
		return true;
	}
	
	/**
	 * handle after saving mail account
	 * 
	 * @param configuration ClientConfiguration - the client configuration
	 */
	private void handleAddAccount(ClientConfigurationData configuration) throws WebOSException {
		if(this.handleSavingMailServices == null) return;
		for(HandleSavingMailService savingMailService : this.handleSavingMailServices) {
			savingMailService.execute(configuration);
		}
	}
	
	/**
	 * Update user information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON update(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, UserUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user name
		String name = WebCommonService.toString(object, UserUtil.USER_NAME);
		String organization = WebCommonService.toString(object, UserUtil.USER_ORGANIZATION);
		if(!StringService.hasLength(name) || !StringService.hasLength(organization)) {
			logger.debug("The user name and organization name are null");
			return FAILURE_JSON;
		}	
		
		try {
			String code = null;
			String pwd = null;
			boolean addNew = false;
			if(this.accountManager != null) {
				// find user information on lotus account
				LdapAccount account = this.accountManager.findByName(name, true);
				if(account == null) return FAILURE_JSON;
				
				if(!account.isDeleted() || this.mailConfiguration == null 
						|| !this.mailConfiguration.isJoinDomain() 
						|| name.endsWith("@" + organization)) {
					// update lotus account
					account = this.updateLotusAccount(account, object); 
					
					code = account.getCode();
					pwd = account.getPassword();
				} else {
					// get the new user name
					name = MailCommonUtils.getUserName(
							MailCommonUtils.getDUserName(name, mailConfiguration),
							organization,
							this.mailConfiguration);
					// find user information on lotus account
					LdapAccount newaccount = this.accountManager.findByName(name, true);
					if(newaccount != null) {
						JSONObject failure = new JSONObject().accumulate("success", false);						
						// add the type of error
						failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_ADD_USER_EXIST_IN_ORG);
						return failure;
					}
					
					object.remove(UserUtil.USER_NAME);
					object.accumulate(UserUtil.USER_NAME, name);
					
					pwd = account.getPassword();
					// add new account
					code = this.addLotusAccount(object, pwd);
					addNew = true;
				}
				
				if(!StringService.hasLength(code)) return FAILURE_JSON;
			}
			
			// load the user information
			LdapUser user = this.userManager.loadUser(organization, name);
			if(user == null) {
				/**
				 * IN THIS CASE: Add user to organization 
				 */
				// create an user instance
				user = new LdapUser();
				// refresh the user information
				this.refreshUserInfo(user, object);
				user.setPassword(pwd);
				
				// add the user information to organization
				this.userManager.addUser(organization, user);				
				//update group
				if(user.getGroups() != null && user.getGroups().size() > 0) {
					// there is one group added when create user
					this.groupManager.addMember(organization, user.getGroups().get(0), user.getName());
				}
			} else {
				/**
				 * User had already exist in organization
				 */
				// refresh the user information
				this.refreshUserInfo(user, object);
				// update user information
				this.userManager.updateUser(organization, user);
			}
			
			// update the user's email account
			if(!this.updateMailAccounts(WebCommonService.toArray(object, UserUtil.USER_EMAIL_ACCOUNTS),
					code, name, user.getPassword(), addNew)) {
				return FAILURE_JSON;			
			}
		} catch (LotusException ex) {
			logger.error("Error while updating user information", ex);
			return FAILURE_JSON;
		}
		// prepare data to return
		JSONObject returnObject = new JSONObject().accumulate(UserUtil.USER_NAME, name);			
		List<LdapMailAccount> mailAccs = this.mailAccountManager.findByUserName(name);
		if(mailAccs != null && mailAccs.size() != 0) {
			returnObject.accumulate(AccountUtil.ACCOUNT_EMAIL_ACCOUNTS,
					MailAccountUtil.convertToJSONFull(mailAccs));
		}
		return returnObject;
	}
	
	/**
	 * Update the lotus account information
	 * 
	 * @param object JSONObject - the given JSON object information
	 * @return LdapAccount - the updated account information
	 * @throws LotusException - when error happens 
	 */
	private LdapAccount updateLotusAccount(LdapAccount account,JSONObject object) throws LotusException {
		// refresh the account information
		this.refreshAccount(account, object);
		// un-mark deleted
		account.setDeleted(false);
		
		// checking email
		if(StringService.hasLength(account.getEmail()) && 
		    !MailUtil.isEmailAddress(account.getEmail())) {
			logger.error("Invalid email address.");
			return null;
		}	
		
		// update lotus account
		this.accountManager.update(account);
		
		return account;
	}
	
	/**
	 * update email accounts
	 * 
	 * @param mailAccounts JSONArray - the array of mail account as JSON object
	 * @param userCode String - the user code
	 * @param userName String - the given user name
	 * @param pwd String - the digest password
	 * @param addNew boolean - add new lotus or not
	 * @return boolean - the flag to know creating account success or not
	 */
	private boolean updateMailAccounts(JSONArray mailAccounts,
			String userCode, String userName, String pwd, boolean addNew) {		
		try {
			// create the list to contain all new email accounts
			List<JSONObject> newMailAccounts = null;
			if(!addNew) {
				// find all email address of this user
				List<LdapMailAccount> ldapMailAccounts = this.mailAccountManager.findByUserCode(userCode);
				if(ldapMailAccounts == null || ldapMailAccounts.size() == 0) {
					if(mailAccounts != null && mailAccounts.size() > 0) {
						// create new email accounts
						return this.addMailAccounts(mailAccounts, userCode, userName, pwd, StringService.EMPTY_STRING);
					}
				} else {
					if(mailAccounts == null || mailAccounts.size() == 0) {
						// This case: delete all mail account of this user
						for(LdapMailAccount mailAccount : ldapMailAccounts) {
							// delete mail account from given email address
							this.mailAccountManager.deleteMailAccount(mailAccount.getEmail());
						}
					} else {
						boolean remove;
						newMailAccounts = new ArrayList<JSONObject>();
						for(LdapMailAccount mailAccount : ldapMailAccounts) {
							remove = true;
							for (int index = 0; index < mailAccounts.size(); index++) {
								JSONObject object = mailAccounts.getJSONObject(index);
								// get the email address
								String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
								String name = WebCommonService.toString(object, MailAccountUtil.ACCOUNT_USER_NAME);
								if(!StringService.hasLength(name)) {
									newMailAccounts.add(object);
									// remove the JSON object out array
									mailAccounts.remove(index);
									index--;
								} else if(mailAccount.getEmail().equals(email)) {
									remove = false;
									//This case: the email address already exist
									MailAccountUtil.refreshMailAccount(mailAccount, object);
									mailAccount.setPassword(pwd);
									// update the last change date
									mailAccount.setLastChange(new Date());
									
									// updating this email account
									this.mailAccountManager.update(mailAccount);
								}
							}
							if(remove) {
								// remove the mail account
								this.mailAccountManager.deleteMailAccount(mailAccount.getEmail());
							}
						}
					}
				}
			} else {
				newMailAccounts = this.updateChangeOwnerMail(mailAccounts, userCode, userName, pwd);
			}
			/**
			 *  create new mail account
			 */
			if(newMailAccounts != null && newMailAccounts.size() > 0) {
				// create mail account object from configuration
				LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
				LdapMailAccount[] cacheAccounts = new LdapMailAccount[newMailAccounts.size()];
				LdapMailAccount temp = null;
				for (int index = 0; index < newMailAccounts.size(); index++) {
					try {
						// get the JSON object contain all information
						JSONObject object = newMailAccounts.get(index);
				
						// refresh the information for mail account
						MailAccountUtil.refreshMailAccount(mailAccount, object);
						
						// get the email address
						String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
						mailAccount.setEmail(email);
						mailAccount.setMailBox(MailAccountUtil.getMailBox(email));			
						
						// fill the user code
						mailAccount.setUserName(userName);
						mailAccount.setUserCode(userCode);
						mailAccount.setPassword(pwd);
						
						//insert new mail account to database
						this.mailAccountManager.add(mailAccount);
						// launch new object for next using
						temp = IOService.makeNewObject(mailAccount, LdapMailAccount.class);
						temp.setPassword(StringService.EMPTY_STRING);
						cacheAccounts[index] = temp;
						mailAccount.setDepartments(null);
					} catch (Exception e) {
						logger.error("ERROR while updating email of account", e);
						return false;
					}
				}
				// saving configuration for mail client
				this.handleAddAccount(new ClientConfigurationData(ACTION.ADD, cacheAccounts));
			}
			
		} catch (Exception ex) {
			logger.error("ERROR while updating email of account", ex);
			return false;
		}
		
		return true;
	}
	
	/**
	 * change owner for email accounts
	 * 
	 * @param mailAccounts JSONArray - the array of mail account as JSON object
	 * @param userCode String - the user code
	 * @param userName String - the given user name
	 * @param pwd String - the digest password
	 * @return List<JSONObject> - the list of new account
	 */
	private List<JSONObject> updateChangeOwnerMail(JSONArray mailAccounts,
			String userCode, String userName, String pwd) throws LotusException {
		if(mailAccounts == null) return null;
		// create the list to contain all new email accounts
		List<JSONObject> newMailAccounts = new ArrayList<JSONObject>();
		for (int index = 0; index < mailAccounts.size(); index++) {
			JSONObject object = mailAccounts.getJSONObject(index);
			// get the email address
			String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
			String name = WebCommonService.toString(object, MailAccountUtil.ACCOUNT_USER_NAME);
			if(!StringService.hasLength(name)) {
				newMailAccounts.add(object);
			} else { //This case: the email address already exist
				// find email				
				LdapMailAccount mailAccount = this.mailAccountManager.findByName(email);
				if(mailAccount == null) continue;
				
				MailAccountUtil.refreshMailAccount(mailAccount, object);
				mailAccount.setPassword(pwd);
				// update the last change date
				mailAccount.setLastChange(new Date());
				// change owner
				mailAccount.setUserCode(userCode);
				mailAccount.setUserName(userName);
				
				// updating this email account
				this.mailAccountManager.update(mailAccount);
			}
		}
		return newMailAccounts;
	}
	
	/**
	 * Check the current user name whether is had already exist or not
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON object
	 */
	protected JSON isExist(HttpServletRequest request) {
		// get the user name
		String userName = this.getData(request, UserUtil.USER_NAME);
		String org = this.getData(request, UserUtil.USER_ORGANIZATION);
		userName = MailCommonUtils.getUserName(userName, org, this.mailConfiguration);
		if(!StringService.hasLength(userName)) {
			JSONObject failure = new JSONObject().accumulate("success", false);						
			// add the type of error
			failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_INPUT_INVALID_USER_NAME);
			return failure;
		}
		
		try {
			// find user information on lotus account
			LdapAccount account = this.accountManager.findByName(userName, true);
			
			JSONObject object = new JSONObject();
			// add result to JSON object
			object.accumulate(UserUtil.USER_EXIST_UNAME, account != null);
			return object;
		} catch (LotusException ex) {
			logger.error("Error while checking user name", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Reset user's password
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON resetPwd(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, UserUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		// get the user name
		String name = WebCommonService.toString(object, UserUtil.USER_NAME);
		if(!StringService.hasLength(name)) return FAILURE_JSON;
		String organization = WebCommonService.toString(object, UserUtil.USER_ORGANIZATION);
		// generate new password
		//String password = MailCommonUtils.generatePassword();
		String password = WebCommonService.toString(object, AccountUtil.ACCOUNT_PASSWORD);
		String digestPwd = MailCommonUtils.getLdapPassword(password, hashMethod);
		
		try {			
			// load the account information
			LdapAccount account = this.accountManager.findByName(name);
			if(account == null) return FAILURE_JSON;
			String userCode = account.getCode();
			
			// update the mail configuration
			this.updateConfiguration(userCode, password);			
			// reset the password on organization
			this.userManager.resetPassword(organization, name, digestPwd);
			// reset the password of mail account
			this.mailAccountManager.resetPassword(userCode, digestPwd);
			// reset password on lotus account
			this.accountManager.resetPasswordByCode(userCode, digestPwd);
			// TODO send mail this password for user
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("ERROR while reset user password");
			return FAILURE_JSON;
		}
		
	}

	/**
	 * update the password for mail client configuration
	 * 
	 * @param code String - the user code
	 * @param password String - the new password
	 * @throws WebOSException - when there is any error happens
	 */
	private void updateConfiguration(String code, String password) throws WebOSException{
		// find all mail address of user
		List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(code);
		if(mailAccounts == null || mailAccounts.size() == 0) return;
		
		// create the list to contain email address
		List<String> emails = new ArrayList<String>(mailAccounts.size());
		for(MailAccount mailAccount : mailAccounts) {
			// put the email to list
			emails.add(mailAccount.getEmail());
		}
		
		this.handle(new ClientChagePwdData(code, emails, password));
	}
	
	/**
	 * handle after changing password of mail account
	 * 
	 * @param data ClientChagePwdData - the given data
	 */
	private void handle(ClientChagePwdData data) throws WebOSException {
		if(this.handleChangePwdMailService == null) return;
		for(HandleChangePwdMailService changePwdMailService : this.handleChangePwdMailService) {
			changePwdMailService.execute(data);
		}
	}
	
	/**
	 * Delete the user information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the given JSON object
	 */
	protected JSON delete(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, UserUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user name
		String name = WebCommonService.toString(object, UserUtil.USER_NAME);
		if(!StringService.hasLength(name)) return FAILURE_JSON;
		String organization = WebCommonService.toString(object, UserUtil.USER_ORGANIZATION);
		
		try {			
			// find all groups of which user is a member
			List<LdapGroup> groups = this.groupManager.findMemberGroups(organization, name);
			List<String> groupNames = new ArrayList<String>(); 
			if(groups != null && groups.size() > 0) {
				// get the user DN
				String userDN = this.userManager.getUserDN(organization, name);
				for(LdapGroup group : groups) {
					if(group.getManagers().contains(userDN)) {
						JSONObject failure = new JSONObject().accumulate("success", false);						
						// add the type of error
						failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_DELETE_MANAGER);
						failure.accumulate(UserUtil.USER_GROUP, group.getName());
						return failure;
					}
					// remove user out of group
					group.getMembers().remove(userDN);
					groupNames.add(group.getName());
				}
				// update groups
				this.groupManager.updateGroups(organization, groups);
			}
			
			// delete the user information
			this.userManager.deleteUser(organization, name);
			
			// delete lotus account
			if(this.mailConfiguration != null
					&& !name.equals(this.mailConfiguration.getAdminID())
					&& (MailConfiguration.MARK_DELETE == this.mailConfiguration.getAccDeleteType()
							|| MailConfiguration.PERTINENT_DELETE == this.mailConfiguration.getAccDeleteType())) {
				// delete lotus account depend on configuration
				this.accountManager.deleteAccount(name, 
						MailConfiguration.MARK_DELETE == this.mailConfiguration.getAccDeleteType());
			}
			
			boolean all = WebCommonService.toBool(object, UserUtil.USER_EMAIL_DEL_ALL, false);
			if(all) {
				// delete all email accounts
				this.mailAccountManager.deleteByUserName(name);
			} else {
				// delete all selected mail accounts
				JSONArray array = WebCommonService.toArray(object, UserUtil.USER_EMAIL_ACCOUNTS);
				if(array != null && array.size() > 0) {
					// delete mail accounts
					this.deleteMailAccounts(array);
				}
			}
			
			// user doesn't depend on any group
			if(groupNames.size() == 0) return SUCCESS_JSON;
			
			// remove group name in mail accounts
			//TODO change function (only find in current organization)
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserName(name);
			if(mailAccounts != null && mailAccounts.size() > 0) {
				for(LdapMailAccount mailAccount : mailAccounts) {
					if(mailAccount.getDepartments() != null 
							&& organization.equals(MailUtil.getDomainName(mailAccount.getEmail()))
							&& mailAccount.getDepartments().removeAll(groupNames)) {
						this.mailAccountManager.update(mailAccount);						
					}
				}
			}
			
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error during deleting user information", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Check the permission before deleting
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON object
	 */
	protected JSON checkDelete(HttpServletRequest request) {
		// get the user name
		String name = this.getData(request, UserUtil.USER_NAME);
		String organization = this.getData(request, UserUtil.USER_ORGANIZATION);
		if(!StringService.hasLength(name) || !StringService.hasLength(organization)) return FAILURE_JSON;
		
		try {
			// find the user in organization with given user name
			LdapUser user = this.userManager.loadUser(organization, name);
			
			if(user == null) { // the user doesn't exist in organization
				JSONObject failure = new JSONObject().accumulate("success", true);						
				// add the type of error
				failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_NO_EXIST);
				return failure;
			}
			
			if(user.getGroups() == null || user.getGroups().size() == 1) {
				/**
				 * The user don't depend on any group or in one group
				 */
				return SUCCESS_JSON;
			}
			
			/**
			 * Check the permission on organization 
			 */
			// get permission from cache
			Permission permission = PermissionCacheService.get(getCode());
			// check the permission
			if(permission == null || permission.getAccessRole() == LotusRole.ROLE_NONE){
				logger.debug("There is no permission to load group");
				return FAILURE_JSON;
			}
			// The role on organization
			LotusRole role = LotusRole.ROLE_NONE;
			// get the roles on domain
			HashMap<String, LotusRole> roles = permission.getRoles();
			if(roles == null || !roles.containsKey(organization)) {
				// load role
				role = LoadRoleService.loadRole(this.groupManager, permission, organization, this.getUserName());
				// put data to cache
				PermissionCacheService.put(getCode(), permission);
			} else {
				// get the role from cache
				role = roles.get(organization);
			}
			
			// check role
			if(role != LotusRole.ROLE_DOMAIN && role != LotusRole.ROLE_SUPER) {
				JSONObject failure = new JSONObject().accumulate("success", true);						
				// add the type of error
				failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_NO_HAVE_PERMISSION);
				return failure;
			}
			
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("There is error happens during checking permission for deleting user", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Delete the given email addresses 
	 * 
	 * @param array JSONArray - the JSON array contain email addresses need to delete
	 * @throws LotusException - when error happens
	 */
	private void deleteMailAccounts(JSONArray array) throws LotusException{
		for (int index = 0; index < array.size(); index++) {
			JSONObject object = array.getJSONObject(index);
			this.mailAccountManager.deleteMailAccount(WebCommonService.toString(object, UserUtil.USER_EMAIL));
		}
	}
	
	/**
	 * Refresh account information
	 * 
	 * @param account Account - the given account need to refresh
	 * @param object JSONObject - the given JSON object 
	 */
	private void refreshAccount(LdapAccount account, JSONObject object) {
		// The required information
		account.setName(WebCommonService.toString(object, UserUtil.USER_NAME));		
		account.setEmail(WebCommonService.toString(object, UserUtil.USER_EMAIL));
		account.setTimeZone(this.timezone);
		account.setCountry(this.country);
		
		// The option information
		account.setFirstName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_FIRST_NAME)));
		account.setMiddleName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_MIDDLE_NAME)));
		account.setLastName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_LAST_NAME)));
	}
	
	/**
	 * Refresh the user information from given JSON object
	 * 
	 * @param user LdapUser - the user need to refresh
	 * @param object JSONObject - the given JSON object
	 */
	private void refreshUserInfo(LdapUser user, JSONObject object) {
		// The required information
		user.setName(WebCommonService.toString(object, UserUtil.USER_NAME));
		user.setLastName(WebCommonService.toString(object, UserUtil.USER_LAST_NAME));
		user.setEmail(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_EMAIL)));
		user.setFirstName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_FIRST_NAME)));
		user.setMiddleName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_MIDDLE_NAME)));
		user.setFullName(MailCommonUtils.getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName()));
		user.setMobile(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_MOBILE)));
		user.setTitle(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_TITLE)));
		user.setEmployeeNumber(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_EMPLOYEE_NUMBER)));
		user.setWorkPhone(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_WORK_PHONE)));
		user.setHomePhone(MailCommonUtils.getLdapValue(WebCommonService.toString(object, UserUtil.USER_HOME_PHONE)));
		JSONArray groups = WebCommonService.toArray(object, MailAccountUtil.MAIL_GROUPS);
		if(groups != null && groups.size() > 0) {
			List<String> groupNames = new ArrayList<String>(groups.size());
			for (int index = 0; index < groups.size(); index++) {
				String group = WebCommonService.toString(groups.getJSONObject(index), MailAccountUtil.MAIL_GROUP);
				if(StringService.hasLength(group) && !group.equals(GroupUtil.NONE_GROUP)) {
					groupNames.add(group);
				}				
			}
			user.setGroups(groupNames);
		}
	}

	/**
	 * Return the list of saving mail services
	 * 
	 * @return List<HandleSavingMailService> - the handleSavingMailServices
	 */
	public List<HandleSavingMailService> getHandleSavingMailServices() {
		return this.handleSavingMailServices;
	}

	/**
	 * Set the list of saving mail services
	 * 
	 * @param handleSavingMailServices List<HandleSavingMailService> - the handleSavingMailServices to set
	 */
	public void setHandleSavingMailServices(
			List<HandleSavingMailService> handleSavingMailServices) {
		this.handleSavingMailServices = handleSavingMailServices;
	}

	/**
	 * @return the handleChangePwdMailService
	 */
	public List<HandleChangePwdMailService> getHandleChangePwdMailService() {
		return this.handleChangePwdMailService;
	}

	/**
	 * @param handleChangePwdMailService the handleChangePwdMailService to set
	 */
	public void setHandleChangePwdMailService(
			List<HandleChangePwdMailService> handleChangePwdMailService) {
		this.handleChangePwdMailService = handleChangePwdMailService;
	}

	/**
	 * Return the time zone
	 * 
	 * @return String - the timezone
	 */
	public String getTimezone() {
		return this.timezone;
	}

	/**
	 * Set the time zone
	 * 
	 * @param timezone String - the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * Return the country
	 * 
	 * @return String - the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Set the country
	 * 
	 * @param country String - the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
}
