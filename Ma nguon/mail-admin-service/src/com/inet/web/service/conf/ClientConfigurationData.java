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
package com.inet.web.service.conf;

import com.inet.lotus.mail.ldap.LdapMailAccount;

/**
 * ClientConfiguration.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class ClientConfigurationData {
	// The ENUM contain action
	public enum ACTION {ADD, ACTIVE, DELETE};
	// The action
	private ClientConfigurationData.ACTION action;	
	// The mail account information
	private LdapMailAccount[] mailAccounts;
	
	/**
	 * Create <tt>ClientConfiguration</tt> instance
	 * 
	 * @param mailAccounts LdapMailAccount[] - the array of mail accounts
	 * @param action String - the action
	 */
	public ClientConfigurationData(ClientConfigurationData.ACTION action, LdapMailAccount... mailAccounts) {
		this.mailAccounts = mailAccounts;
		this.action = action;
	}

	/**
	 * Return the action
	 * 
	 * @return ACTION - the action
	 */
	public ClientConfigurationData.ACTION getAction() {
		return this.action;
	}

	/**
	 * Set the action
	 * 
	 * @param action ACTION - the action to set
	 */
	public void setAction(ClientConfigurationData.ACTION action) {
		this.action = action;
	}

	/**
	 * Return the array of account information
	 * 
	 * @return LdapMailAccount[] - the array of mailAccount
	 */
	public LdapMailAccount[] getMailAccounts() {
		return this.mailAccounts;
	}

	/**
	 * set the array of account information
	 * 
	 * @param mailAccounts LdapMailAccount... - the mailAccount to set
	 */
	public void setMailAccounts(LdapMailAccount... mailAccounts) {
		this.mailAccounts = mailAccounts;
	}
	
}
