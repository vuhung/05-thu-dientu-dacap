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
package com.inet.web.service.mail;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.conf.ClientConfigurationData;
import com.inet.web.service.conf.HandleSavingMailService;
import com.inet.web.service.conf.ClientConfigurationData.ACTION;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * MailAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAccountService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(MailAccount.class);
	private ILdapMailAccountManager mailAccountManager;
	private List<HandleSavingMailService> handleSavingMailServices;
	
	/**
	 * The constructor
	 * 
	 * @param mailAccountManager IMailAccountManager - the mail manager
	 */
	public MailAccountService(ILdapMailAccountManager mailAccountManager) {
		this.mailAccountManager = mailAccountManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(MailAccountUtil.ACTION_SAVE.equals(action)) {
			// add new account
			return this.addMailAccount(request);
		} else if(MailAccountUtil.ACTION_UPDATE.equals(action)) {
			// update account
			return this.updateMailAccount(request);
		} else if(MailAccountUtil.ACTION_ACTIVE.equals(action)) {
			// active/inactive account
			return this.activeAccount(request);
		} else if(MailAccountUtil.ACTION_ACTIVE_ALL.equals(action)){
			// active/inactive all accounts
			return this.activeAccounts(request);
		} else if(MailAccountUtil.ACTION_DELETE.equals(action)) {
			// delete account
			return this.deleteMailAccount(request);
		} else if(MailAccountUtil.ACTION_DELETE_ALL.equals(action)) {
			// delete all account
			return this.deleteMailAccounts(request);
		}else if(MailAccountUtil.ACTION_SMTP_AUTH.equals(action)) {
			// SMTP authenticate
			return this.smtpAuthentication(request);
		} else if(MailAccountUtil.ACTION_FORWARD.equals(action)) {
			// forward mail
			return this.forward(request);
		} else if(MailAccountUtil.ACTION_VIRUS_CHECK.equals(action)) {
			// checking virus
			return this.virusCheck(request);
		} else if(MailAccountUtil.ACTION_SPAM_CHECK.equals(action)) {
			// checking SPAM
			return this.spamCheck(request);
		}
		
		return null;
	}
	
	/**
	 * add new mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON
	 */
	protected JSON addMailAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the email address
		String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
		// not check, this has been checked on GUI
		//if(!MailUtil.isEmailAddress(email)) return FAILURE_JSON;
		
		// create mail account
		LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
		// fill information from GUI
		mailAccount.setEmail(email);		
		mailAccount.setMailBox(MailAccountUtil.getMailBox(email));
		
		// get digested password and put to mail account
		mailAccount.setPassword(WebCommonService.toString(object, MailAccountUtil.MAIL_PASSWORD));
		MailAccountUtil.refreshMailAccount(mailAccount, object);
		
		try {			
			// save mail account
			this.mailAccountManager.add(mailAccount);
			// clear password
			mailAccount.setPassword(StringService.EMPTY_STRING);
			this.handle(new ClientConfigurationData(ACTION.ADD, mailAccount));
		} catch (LotusException ex) {
			logger.error("ERROR while adding mail account.", ex);
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * update mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON updateMailAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// load mail account from server
			LdapMailAccount account = this.mailAccountManager.findByName(
					WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS));
			if(account == null) return FAILURE_JSON;
			boolean changeOwner = false;
			if(!account.getUserCode().equals(WebCommonService.toString(
					object, MailAccountUtil.ACCOUNT_CODE))) {
				// This case: change owner of account
				changeOwner = true;
			}
			
			// refresh mail account information
			MailAccountUtil.refreshMailAccount(account, object);
			
			/**
			 *  not update these field
			 */
			if(!changeOwner) {
				account.setPassword(StringService.EMPTY_STRING);
			} else {
				account.setPassword(WebCommonService.toString(object, MailAccountUtil.MAIL_PASSWORD));
			}
			
			// update the last change date
			account.setLastChange(new Date());
			
			// update mail account
			this.mailAccountManager.update(account);
			if(changeOwner) {
			        // clear password
			        account.setPassword(StringService.EMPTY_STRING);
				this.handle(new ClientConfigurationData(ACTION.ADD, account));
			}
		} catch (LotusException ex) {
			logger.error("ERROR while update mail account", ex);
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * delete the mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON deleteMailAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
			
			// Load mail account
			LdapMailAccount mailAcc = this.mailAccountManager.findByName(email);
			if(mailAcc == null) return FAILURE_JSON;
			
			// delete given account
			this.mailAccountManager.deleteMailAccount(email);
			// delete configuration
			this.handle(new ClientConfigurationData(ACTION.DELETE, mailAcc));			
		} catch (LotusException ex) {
			logger.error("ERROR while deleting mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * delete all mail accounts of given user
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON deleteMailAccounts(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			String code = WebCommonService.toString(object, MailAccountUtil.ACCOUNT_CODE);
			// find all user's email account
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(code);
			
			// delete all user's mail account with given user code
			this.mailAccountManager.deleteMailAccounts(code);
			
			if(mailAccounts != null && mailAccounts.size() > 0) {
				LdapMailAccount[] mailAccountArray = new LdapMailAccount[mailAccounts.size()];
				// handle after delete account
				this.handle(new ClientConfigurationData(ACTION.DELETE,
						mailAccounts.toArray(mailAccountArray)));
			}
		} catch (LotusException ex) {
			logger.error("ERROR while deleting mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON activeAccount(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		String email = WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS);
		try {
			// active the given mail account
			this.mailAccountManager.activeAccount(
					// mail address
					email,
					// the active value
					WebCommonService.toBool(object, MailAccountUtil.MAIL_ACCOUNT_ACTIVE));
			this.handle(new ClientConfigurationData(ACTION.ACTIVE, this.mailAccountManager.findByName(email)));
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive all mail accounts of given user
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON activeAccounts(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		String code = WebCommonService.toString(object, MailAccountUtil.ACCOUNT_CODE);
		try {
			// active the given mail account
			this.mailAccountManager.activeAccounts(
					// the user code
					code,
					// the active value
					WebCommonService.toBool(object, MailAccountUtil.MAIL_ACCOUNT_ACTIVE));
			
			// find all mail account to update the client configuration
			List<LdapMailAccount> accountList = this.mailAccountManager.findByUserCode(code);
			if(accountList != null && accountList.size() > 0) {
				this.handle(new ClientConfigurationData(ACTION.ACTIVE,
						accountList.toArray(new LdapMailAccount[accountList.size()])));
			}
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * SMTP authenticate or not
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON smtpAuthentication(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given mail account
			this.mailAccountManager.smtpAuthentication(
					// mail address
					WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAccountUtil.MAIL_SMTP_AUTH));
		} catch (LotusException ex) {
			logger.error("ERROR while check SMTP authenticate mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * SMTP authenticate or not
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON virusCheck(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given mail account
			this.mailAccountManager.virusCheck(
					// mail address
					WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAccountUtil.MAIL_VIRUS_CHECKING));
		} catch (LotusException ex) {
			logger.error("ERROR while check SMTP authenticate mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * forward mail or not
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON forward(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		String email = WebCommonService.toString(object, MailAccountUtil.MAIL_FORWARD_ADDRESS);
		boolean forward = WebCommonService.toBool(object, MailAccountUtil.MAIL_FORWARD);
		if(forward && !MailUtil.isEmailAddress(email)) return FAILURE_JSON;
		
		try {
			// active the given mail account
			this.mailAccountManager.forward(
					// mail address
					WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS),
					// the active value
					forward,
					email);
		} catch (LotusException ex) {
			logger.error("ERROR while check SMTP authenticate mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * SMTP authenticate or not
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON spamCheck(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAccountUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given mail account
			this.mailAccountManager.spamCheck(
					// mail address
					WebCommonService.toString(object, MailAccountUtil.MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAccountUtil.MAIL_SPAM_CHECKING));
		} catch (LotusException ex) {
			logger.error("ERROR while check SMTP authenticate mail account", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * handle after saving mail account
	 * 
	 * @param configuration ClientConfiguration - the client configuration
	 */
	private void handle(ClientConfigurationData configuration) throws WebOSException {
		if(this.handleSavingMailServices == null) return;
		for(HandleSavingMailService savingMailService : this.handleSavingMailServices) {
			savingMailService.execute(configuration);
		}
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
