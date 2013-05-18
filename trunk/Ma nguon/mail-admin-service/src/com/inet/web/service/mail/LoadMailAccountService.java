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
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.conf.MailConfiguration;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.MailAccountUtil;

/**
 * LoadMailAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadMailAccountService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(LoadMailAccountService.class);
	private ILdapMailAccountManager mailAccountManager;
	private LdapAccountManager accountManager;
	private MailConfiguration mailConfiguration;
	
	/**
	 * Create <tt>LoadMailAccountService</tt> instance
	 * 
	 * @param mailAccountManager IMailAccountManager - the mail account manager
	 * @param accountManager LdapAccountManager - the account manager
	 * @param mailConfiguration MailConfiguration - the mail configuration
	 */
	public LoadMailAccountService(ILdapMailAccountManager mailAccountManager,
			LdapAccountManager accountManager, MailConfiguration mailConfiguration) {
		this.mailAccountManager = mailAccountManager;
		this.accountManager = accountManager;
		this.mailConfiguration = mailConfiguration;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(MailAccountUtil.ACTION_LOAD.equals(action)) {
			// load mail account
			return this.loadAccount(this.getData(request, MailAccountUtil.MAIL_ADDRESS));
		} else if(MailAccountUtil.ACTION_LOAD_ALL.equals(action)) {
			return this.loadAll(request);
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * load mail account
	 * 
	 * @param email String - the given email address
	 * @return JSON - the mail account as JSON object
	 */
	private JSON loadAccount(String email) {
		if(!MailUtil.isEmailAddress(email)) return FAILURE_JSON;
		try {
			// load mail account from database
			MailAccount account = this.mailAccountManager.findByName(email);
			if(account != null) {
				JSONObject object = MailAccountUtil.convertToJSON(account);				
				object.accumulate(MailAccountUtil.MAIL_FORWARD_ADDRESS, account.getForwardAddress())
					  .accumulate(MailAccountUtil.MAIL_CHANGE_OWNER_ACCOUNT, 
							  this.mailConfiguration!=null?this.mailConfiguration.isChangeAccountOwner():false);
				
				// load the user who is the owner of this mail account
				LdapAccount user = this.accountManager.findByCode(account.getUserCode());
				if(user != null) {
					// user exist
					object.accumulate(MailAccountUtil.ACCOUNT_USER_NAME, user.getName());
				}
				return object; 
			}
		} catch (LotusException ex) {
			logger.error("ERROR while getting mail account", ex);						
		}
		return FAILURE_JSON;
	}
	
	/**
	 * Load all email address of user
	 * 
	 * @param email String - the given email address
	 * @return JSON - the mail account as JSON object
	 */
	private JSON loadAll(HttpServletRequest request) {
		// get the user name from given request
		String userName = getData(request, MailAccountUtil.ACCOUNT_USER_NAME);		
		if(!StringService.hasLength(userName)) return FAILURE_JSON;
		try {
			JSONObject object = new JSONObject();
			// get all this user's email addresses
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserName(userName);
			if(mailAccounts != null && mailAccounts.size() > 0) {
				object.accumulate(MailAccountUtil.MAIL_LIST_ADDRESS,
						MailAccountUtil.convertToJSON(mailAccounts));
			}
			return object;
		} catch (LotusException ex) {
			logger.error("ERROR while load emails", ex);
			return FAILURE_JSON;
		}
	}

}
