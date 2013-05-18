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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;

/**
 * LoadAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadAccountService extends AbstractLotusService {
	private static INetLogger logger = INetLogger.getLogger(LoadAccountService.class);
	private LdapAccountManager accountManager;
	private LdapMailAccountManager mailAccountManager;
	
	/**
	 * Create <tt>LoadAccountService</tt> instance
	 * 
	 * @param accountManager LdapAccountManager - the account manager
	 */
	public LoadAccountService(LdapAccountManager accountManager,
			LdapMailAccountManager mailAccountManager) {
		this.accountManager = accountManager;
		this.mailAccountManager = mailAccountManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(AccountUtil.ACTION_LOAD.equals(action)) {
			// load data
			return this.loadAccount(request);
		} else if(AccountUtil.ACTION_LOAD_FULL.equals(action)) {
			return this.loadAccountAndMail(request);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * Load account service
	 * 
	 * @param request HttpServletRequest - the HTTP request
	 * @return JSON - the JSON object
	 */
	private JSON loadAccount(HttpServletRequest request) {
		// get the user's name
		String userCode = getData(request, AccountUtil.ACCOUNT_USER_CODE);
		if(!StringService.hasLength(userCode)) return FAILURE_JSON;
		
		try {
			// load the account information from database
			LdapAccount account = this.accountManager.findByCode(userCode);
			if(account == null) return FAILURE_JSON;
			
			// convert data and return to GUI
			return AccountUtil.convertAccountToJSON(account);
		} catch (LotusException ex) {
			logger.error("ERROR while loading account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Load account information and all its email
	 * 
	 * @param request HttpServletRequest - the HTTP request
	 * @return JSON - the JSON object
	 */
	private JSON loadAccountAndMail(HttpServletRequest request) {
		// get the user's name
		String userCode = getData(request, AccountUtil.ACCOUNT_USER_CODE);
		if(!StringService.hasLength(userCode)) return FAILURE_JSON;
		
		try {
			// load the account information from database
			LdapAccount account = this.accountManager.findByCode(userCode);
			if(account == null) return FAILURE_JSON;
			
			// convert data and return to GUI
			JSONObject accountJSON = AccountUtil.convertAccountToJSON(account);
			
			// get all email address of this account
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(account.getCode());
			if(mailAccounts != null && mailAccounts.size() > 0) {
				accountJSON.accumulate(AccountUtil.ACCOUNT_EMAIL_ACCOUNTS,
						MailAccountUtil.convertToJSONFull(mailAccounts));
			}
			return accountJSON;
		} catch (LotusException ex) {
			logger.error("ERROR while loading account", ex);
			return FAILURE_JSON;
		}
	}
	
}
