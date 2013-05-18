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
package com.inet.web.service.lotus.account;

import java.util.ArrayList;
import java.util.Date;
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
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.conf.ClientChagePwdData;
import com.inet.web.service.conf.ClientConfigurationData;
import com.inet.web.service.conf.HandleChangePwdMailService;
import com.inet.web.service.conf.HandleSavingMailService;
import com.inet.web.service.conf.ClientConfigurationData.ACTION;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * AccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AccountService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(AccountService.class);
	private LdapAccountManager accountManager;
	private LdapMailAccountManager mailAccountManager;
	private List<HandleChangePwdMailService> handleChangePwdMailService;
	private List<HandleSavingMailService> handleSavingMailServices;
	/* the password scheme. */
	private String hashMethod ;
	
	
	/**
	 * Create <tt>AccountService</tt> instance
	 * 
	 * @param accountManager IAccountManager - the account manager
	 * @param mailAccountManager LdapMailAccountManager - the mail account manager
	 * @param hashMethod the given password scheme value.  
	 */
	public AccountService(LdapAccountManager accountManager, LdapMailAccountManager mailAccountManager, String hashMethod) {
		this.accountManager = accountManager;
		this.mailAccountManager = mailAccountManager;
		this.hashMethod = hashMethod ;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(AccountUtil.ACTION_SAVE.equals(action)) {
			// add new account
			return this.createAccount(request);
		} else if(AccountUtil.ACTION_UPDATE.equals(action)) {
			// update account
			return this.updateAccount(request);
		} else if(AccountUtil.ACTION_RESET_DELETE.equals(action)) {
			// delete account
			this.deleteAccount(request);
		} else if(AccountUtil.ACTION_RESET_PWD.equals(action)) {
			// reset user's password
			return this.resetPassword(request);
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * create new user account
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 */
	private JSON createAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, AccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// create new user account
		LdapAccount account = new LdapAccount(); 
		// refresh account information
		this.refreshUserAccount(account, object);
		// checking email
		if(!MailUtil.isEmailAddress(account.getEmail())) {
			logger.error("Invalid email address.");
			return FAILURE_JSON;
		}
		String password = WebCommonService.toString(object, AccountUtil.ACCOUNT_PASSWORD); 
		// digest password and put to account
		account.setPassword(MailCommonUtils.getLdapPassword(password, hashMethod));
		
		try {
			// add new account
			account = this.accountManager.addAccount(account);
			// adding account failed
			if(account == null) return FAILURE_JSON;
			// create email account from given information
			if(!this.createMailAccount(
					WebCommonService.toArray(object, AccountUtil.ACCOUNT_EMAIL_ACCOUNTS),
					account.getCode(), account.getPassword(), password)) {
				return FAILURE_JSON;
			}
			
			// convert data for GUI
			return AccountUtil.convertAccountToJSON(account);
		} catch (LotusException ex) {
			logger.error("ERROR while creating user account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * create email accounts
	 * 
	 * @param mailAccounts JSONArray - the array of mail account as JSON object
	 * @param userCode String - the user code
	 * @param pwd String - the digest password
	 * @param textPwd String - the clean text password
	 * @return boolean - the flag to know creating account success or not
	 */
	private boolean createMailAccount(JSONArray mailAccounts, String userCode, String pwd, String textPwd) {
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
				mailAccount.setPassword(pwd);
				
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
	 * update account information
	 * 
	 * @param request HttpServletRequest - the HTTP request
	 * @return JSON - the JSON object
	 */
	private JSON updateAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, AccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user's name
		String userName = WebCommonService.toString(object, AccountUtil.ACCOUNT_USER_NAME);
		if(!StringService.hasLength(userName)) return FAILURE_JSON;
		try {
			// load the account information from database
			LdapAccount account = this.accountManager.findByName(userName);
			if(account == null) return FAILURE_JSON;
			
			// refresh account information	
			this.refreshUserAccount(account, object);
			// checking email
			if(!MailUtil.isEmailAddress(account.getEmail())) {
				logger.error("Invalid email address.");
				return FAILURE_JSON;
			}			
		
			// update account
			this.accountManager.update(account);
			// update email account
			if(!this.updateMailAccount(
					WebCommonService.toArray(object, AccountUtil.ACCOUNT_EMAIL_ACCOUNTS),
					account.getCode(), account.getPassword())) {
				return FAILURE_JSON;
			}
		} catch (LotusException ex) {
			logger.error("ERROR while creating user account", ex);
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;		
	}
	
	/**
	 * update email accounts
	 * 
	 * @param mailAccounts JSONArray - the array of mail account as JSON object
	 * @param userCode String - the user code
	 * @param pwd String - the digest password
	 * @return boolean - the flag to know creating account success or not
	 */
	private boolean updateMailAccount(JSONArray mailAccounts, String userCode, String pwd) {
		// find all email address of this user
		List<LdapMailAccount> ldapMailAccounts = this.mailAccountManager.findByUserCode(userCode);
		
		try {
			if(ldapMailAccounts == null || ldapMailAccounts.size() == 0) {
				if(mailAccounts != null && mailAccounts.size() > 0) {
					// create new email accounts
					return this.createMailAccount(mailAccounts, userCode, pwd, StringService.EMPTY_STRING);
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
					// create the list to contain all new email accounts
					List<JSONObject> newMailAccounts = new ArrayList<JSONObject>();
					for(LdapMailAccount mailAccount : ldapMailAccounts) {
						remove = true;
						for (int index = 0; index < mailAccounts.size(); index++) {
							JSONObject object = mailAccounts.getJSONObject(index);
							// get the email address
							String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
							String code = WebCommonService.toString(object, MailAccountUtil.ACCOUNT_CODE);
							if(!StringService.hasLength(code)) {
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
					/**
					 *  create new mail account
					 */
					if(newMailAccounts.size() > 0) {
						// create mail account object from configuration
						LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
						LdapMailAccount[] cacheAccounts = new LdapMailAccount[newMailAccounts.size()];
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
								mailAccount.setPassword(pwd);
								
								//insert new mail account to database
								this.mailAccountManager.add(mailAccount);
								// launch new object for next using
								temp = IOService.makeNewObject(mailAccount, LdapMailAccount.class);
								temp.setPassword(StringService.EMPTY_STRING);
								cacheAccounts[index] = temp;
							} catch (Exception e) {
								logger.error("ERROR while updating email of account", e);
								return false;
							}
						}
						// saving configuration for mail client
						this.handleAddAccount(new ClientConfigurationData(ACTION.ADD, cacheAccounts));
					}
				}
			}
		} catch (Exception ex) {
			logger.error("ERROR while updating email of account", ex);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Delete the given account
	 * 
	 * @param request HttpServletRequest - the HTTP request
	 * @return JSON - the JSON object
	 */
	private JSON deleteAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, AccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user's code
		String userCode = WebCommonService.toString(object, AccountUtil.ACCOUNT_USER_CODE);
		if(!StringService.hasLength(userCode)) return FAILURE_JSON;
		
		try {
			// delete account from database
			this.accountManager.deleteAccount(userCode);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("ERROR while deleting account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Reset the user's password
	 * 
	 * @param request HttpServletRequest - the HTTP request
	 * @return JSON - the JSON object
	 */
	private JSON resetPassword(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, AccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the user's name
		String userCode = WebCommonService.toString(object, AccountUtil.ACCOUNT_USER_CODE);
		if(!StringService.hasLength(userCode)) return FAILURE_JSON;
		
		try {
			// generate new password
			//String password = MailCommonUtils.generatePassword();
			String password = WebCommonService.toString(object, AccountUtil.ACCOUNT_PASSWORD);
			String digestPwd = MailCommonUtils.getLdapPassword(password, hashMethod);
			// reset password
			this.accountManager.resetPasswordByCode(userCode, digestPwd);
			// reset the password of mail account
			this.mailAccountManager.resetPassword(userCode, digestPwd);
			// TODO send mail this password for user
			// update the mail configuration
			this.updateConfiguration(userCode, password);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("ERROR while reseting password", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Refresh account information
	 * 
	 * @param account Account - the given account need to refresh
	 * @param object JSONObject - the given JSON object 
	 */
	private void refreshUserAccount(LdapAccount account, JSONObject object) {
		// The required information
		account.setName(WebCommonService.toString(object, AccountUtil.ACCOUNT_USER_NAME));		
		account.setEmail(WebCommonService.toString(object, AccountUtil.ACCOUNT_EMAIL));
		account.setTimeZone(WebCommonService.toString(object, AccountUtil.ACCOUNT_TIMEZONE));
		account.setCountry(WebCommonService.toString(object, AccountUtil.ACCOUNT_COUNTRY));
		
		// The option information
		account.setCity(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_CITY)));
		account.setState(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_STATE)));
		account.setFirstName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_FIRST_NAME)));
		account.setMiddleName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_MIDDLE_NAME)));
		account.setLastName(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_LAST_NAME)));
		account.setLanguage(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_LANGUAGE)));
		account.setBirthday(WebCommonService.toDate(object, AccountUtil.ACCOUNT_BIRTHDAY, null));
		account.setGender(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_GENTLE)));
		account.setPostalCode(MailCommonUtils.getLdapValue(WebCommonService.toString(object, AccountUtil.ACCOUNT_POSTCODE)));
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
	 * @return the handleSavingMailServices
	 */
	public List<HandleSavingMailService> getHandleSavingMailServices() {
		return this.handleSavingMailServices;
	}

	/**
	 * @param handleSavingMailServices the handleSavingMailServices to set
	 */
	public void setHandleSavingMailServices(
			List<HandleSavingMailService> handleSavingMailServices) {
		this.handleSavingMailServices = handleSavingMailServices;
	}

}
