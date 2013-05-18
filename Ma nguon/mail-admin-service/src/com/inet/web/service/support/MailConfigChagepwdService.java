/*****************************************************************
   Copyright 2008 by Tung Luong (lqtung@truthinet.com.vn)

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

import com.inet.base.service.Assert;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.web.bo.config.MailAcctConfigInfoBO;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * LotusChagepwService.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class MailConfigChagepwdService extends AbstractService implements ChangePasswdService{

	//~ Instance fields =======================================================
	/* account manager. */
	protected MailAcctConfigInfoBO mailCfgBO ;
	private LdapMailAccountManager mailAccountManager;
	
	///~ Constructors ==========================================================
	/**
	 * Create a new MailConfigChagepwdService instance from the given {@link MailAcctConfigInfoBO} instance.
	 * 
	 * @param mailCfgBO the given {@link MailAcctConfigInfoBO} instance.
	 * @param mailAccountManager the given {@link LdapMailAccountManager} instance
	 */
	public MailConfigChagepwdService(MailAcctConfigInfoBO mailCfgBO, 
			LdapMailAccountManager mailAccountManager) {
		Assert.isNotNull(mailCfgBO, "MailAcctConfigInfoBO must be not null.") ;
		Assert.isNotNull(mailAccountManager, "LdapMailAccountManager must be not null.") ;
		this.mailCfgBO 	= mailCfgBO ;
		this.mailAccountManager = mailAccountManager;
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
	 */
	public boolean verifyPassword(String pasword) throws WebOSException {
		return true;
	}
	
	/**
	 * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String)
	 */
	public void changePassword(String oldPassword, String newPassword)
			throws WebOSException {
		try{
			//user code
			String userCode = this.getCode();
			
			// find all mail address of user
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(userCode);
			if(mailAccounts == null || mailAccounts.size() == 0) return;
			
			// create the list to contain email address
			List<String> emails = new ArrayList<String>(mailAccounts.size());
			for(MailAccount mailAccount : mailAccounts) {
				// put the email to list
				emails.add(mailAccount.getEmail());
			}
			mailCfgBO.changePassword(userCode, emails, newPassword);
		} catch(LotusException ex){
			throw new WebOSException("Change configure mail password fail "+ ex.getMessage(), ex);
		}
	}

	/**
	 * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
	 */
	public void resetPassword(String userName, String newPassword) throws WebOSException {
		this.changePassword(null, newPassword);
	}
}
