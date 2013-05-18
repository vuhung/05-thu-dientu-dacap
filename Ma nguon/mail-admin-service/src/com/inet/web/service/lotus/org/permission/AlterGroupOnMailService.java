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
import java.util.List;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.web.exception.WebOSException;

/**
 * AlterGroupOnMailService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AlterGroupOnMailService implements HandleAlterGroupService {
	private static final INetLogger logger = INetLogger.getLogger(AlterGroupOnMailService.class);
	private ILdapMailAccountManager mailAccountManager;
	
	/**
	 * The constructor
	 * 
	 * @param mailAccountManager ILdapMailAccountManager - the mail account manager
	 */
	public AlterGroupOnMailService(ILdapMailAccountManager mailAccountManager) {
		this.mailAccountManager = mailAccountManager;
	}

	/**
	 * @see com.inet.web.service.HandleService#execute(java.lang.Object)
	 */
	public void execute(AlterGroupData data) throws WebOSException {
		if(data == null || !StringService.hasLength(data.getGroup()) 
				|| !StringService.hasLength(data.getOrganization())) return;
		
		switch (data.getAction()) {
		case ADD:
			this.addGroup(data);
			break;
		case UPDATE:
			this.updateGroup(data);
		case DELETE: 
			this.deleteGroup(data);
		}
	}
	
	/**
	 * Handle  after adding group
	 * 
	 * @param data AlterGroupData - the altering data information
	 * @throws WebOSException - when there is any error happens
	 */
	private void addGroup(AlterGroupData data) throws WebOSException {
		if(data.getInsertMembers() != null && data.getInsertMembers().size() > 0) {
			try {
				// load all mail account from given user's name				
				List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserNames(
						data.getOrganization(), data.getInsertMembers());
				if(mailAccounts == null || mailAccounts.size() == 0) return;
				// update groups for mail account
				this.updateAdd(mailAccounts, data.getGroup());
				// update mail account information
				this.updateMailAccounts(mailAccounts);
			} catch (LotusException ex) {
				logger.error("ERROR while adding group for user", ex);
				throw new WebOSException(ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Handle after updating group
	 * 
	 * @param data AlterGroupData - the altering data information
	 * @throws WebOSException - when there is any error happens
	 */
	private void updateGroup(AlterGroupData data) throws WebOSException {
		this.addGroup(data);
		if(data.getRemoveMembers() != null && data.getRemoveMembers().size() > 0) {
			try {
				// load all user from given user's name				
				List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserNames(
						data.getOrganization(), data.getRemoveMembers());
				
				if(mailAccounts == null || mailAccounts.size() == 0) return;
				// update groups for users
				this.updateRemove(mailAccounts, data.getGroup());
				// update user information
				this.updateMailAccounts(mailAccounts);
			} catch (LotusException ex) {
				logger.error("ERROR while adding group for user", ex);
				throw new WebOSException(ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Handle user after delete group
	 * 
	 * @param data AlterGroupData - the altering data information
	 * @throws WebOSException - when there is any error happens
	 */
	private void deleteGroup(AlterGroupData data) throws WebOSException {
		if(data.getRemoveMembers() == null || data.getRemoveMembers().size() == 0) return;
		
		// find all user in organization from given list of user name
		List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserNames(
				data.getOrganization(), data.getRemoveMembers());
		if(mailAccounts == null || mailAccounts.size() == 0) return;
		
		// remove group name out of this users
		this.updateRemove(mailAccounts, data.getGroup());
		// update user information
		this.updateMailAccounts(mailAccounts);
	}
	
	/**
	 * Update the group for mail account
	 * 
	 * @param mailAccounts List<LdapMailAccount> - the list of mail account
	 * @param group String - the group name
	 */
	private void updateAdd(List<LdapMailAccount> mailAccounts, String group) {
		if(mailAccounts == null || mailAccounts.size() == 0) return;
		
		for(LdapMailAccount mailAccount : mailAccounts) {
			// get the current user's groups
			List<String> groups = mailAccount.getDepartments();
			if(groups == null) groups = new ArrayList<String>();
			
			// add new group for user
			groups.add(group);
			mailAccount.setDepartments(groups);
		}
	}
	
	/**
	 * Update the group for user
	 * 
	 * @param users List<LdapMailAccount> - the list of mail account information
	 * @param group String - the group name
	 */
	private void updateRemove(List<LdapMailAccount> mailAccounts, String group) {
		if(mailAccounts == null || mailAccounts.size() == 0) return;
		
		for(LdapMailAccount mailAccount : mailAccounts) {
			// get the current group of mail account
			List<String> groups = mailAccount.getDepartments();
			if(groups != null) {
				for(String current : groups) {
					if(group.equals(current)) {
						// remove group name out of list
						groups.remove(group);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Update the list of mail account information
	 * 
	 * @param mailAccounts List<LdapMailAccount> - the list of mail account information
	 * @throws LotusException - when there is any error happens
	 */
	private void updateMailAccounts(List<LdapMailAccount> mailAccounts) throws LotusException {
		for(LdapMailAccount mailAccount : mailAccounts) {
			// update the mail account information
			this.mailAccountManager.update(mailAccount);
		}
	}

}
