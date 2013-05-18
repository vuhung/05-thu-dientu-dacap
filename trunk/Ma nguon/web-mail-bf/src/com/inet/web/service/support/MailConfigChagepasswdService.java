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
package com.inet.web.service.support;

import java.util.ArrayList;
import java.util.List;

import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.web.bo.mail.MailConfigureBO;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * MailConfigChagepasswdService.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailConfigChagepasswdService extends AbstractService implements ChangePasswdService {
	//~ Instance fields =======================================================
	/**
	 * the {@link MailConfigureBO} instance.
	 */
	private final MailConfigureBO configureBO;
	/**
	 * the {@link LdapMailAccountManager} instance.
	 */
	private final LdapMailAccountManager mailAccountManager;
	
	//~ Constructors ==========================================================
	/**
	 * MailConfigChagepasswdService constructor
	 * @param configureBO MailConfigureBO
	 * @param LdapMailAccountManager mailAccountManager
	 */
	public MailConfigChagepasswdService(
			AccountManager<Account> accountManager,
			MailConfigureBO configureBO, 
			LdapMailAccountManager mailAccountManager){
		super(accountManager) ;
		this.configureBO = configureBO;
		this.mailAccountManager = mailAccountManager;		
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
	 */
	public boolean verifyPassword(String password) throws WebOSException {
		return true;
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String)
	 */
	public void changePassword(String oldPassword, String password) throws WebOSException {
		//user code
		String userCode = getCode();
		
		// find all mail address of user
		List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(userCode);
		if(mailAccounts == null || mailAccounts.size() == 0) return;
		
		// create the list to contain email address
		List<String> emails = new ArrayList<String>(mailAccounts.size());
		for(MailAccount mailAccount : mailAccounts) {
			// put the email to list
			emails.add(mailAccount.getEmail());
		}
		
		configureBO.changePassword(emails, password);
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
	 */
	public void resetPassword(String userName, String password) throws WebOSException {
		changePassword(null,password);
	}
}
