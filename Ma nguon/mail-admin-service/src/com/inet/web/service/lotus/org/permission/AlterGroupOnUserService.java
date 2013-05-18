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
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.web.exception.WebOSException;

/**
 * AlterGroupOnUserService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AlterGroupOnUserService implements HandleAlterGroupService {
	private static final INetLogger logger = INetLogger.getLogger(AlterGroupOnUserService.class);
	private ILdapUserManager userManager;

	/**
	 * The constructor
	 * 
	 * @param userManager ILdapUserManager - the user manager
	 */
	public AlterGroupOnUserService(ILdapUserManager userManager) {
		this.userManager = userManager;
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
				// load all user from given user's name				
				List<LdapUser> users = this.userManager.findUsers(
						data.getOrganization(), data.getInsertMembers());
				if(users == null || users.size() == 0) return;
				// update groups for users
				this.updateAdd(users, data.getGroup());
				// update user information
				this.updateUser(data.getOrganization(), users);
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
				List<LdapUser> users = this.userManager.findUsers(
						data.getOrganization(), data.getRemoveMembers());
				
				if(users == null || users.size() == 0) return;
				// update groups for users
				this.updateRemove(users, data.getGroup());
				// update user information
				this.updateUser(data.getOrganization(), users);
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
		List<LdapUser> users = this.userManager.findUsers(data.getOrganization(), data.getRemoveMembers());
		if(users == null || users.size() == 0) return;
		
		// remove group name out of this users
		this.updateRemove(users, data.getGroup());
		// update user information
		this.updateUser(data.getOrganization(), users);
	}
	
	/**
	 * Update the group for user
	 * 
	 * @param users List<LdapUser> - the list of user information
	 * @param group String - the group name
	 */
	private void updateAdd(List<LdapUser> users, String group) {
		if(users == null || users.size() == 0) return;
		
		for(LdapUser user : users) {
			// get the current user's groups
			List<String> groups = user.getGroups();
			if(groups == null) groups = new ArrayList<String>();
			
			// add new group for user
			groups.add(group);
			user.setGroups(groups);
		}
	}
	
	/**
	 * Update the group for user
	 * 
	 * @param users List<LdapUser> - the list of user information
	 * @param group String - the group name
	 */
	private void updateRemove(List<LdapUser> users, String group) {
		if(users == null || users.size() == 0) return;
		
		for(LdapUser user : users) {
			// get the current user's groups
			List<String> groups = user.getGroups();
			if(groups != null) {
				groups.remove(group);
			}
		}
	}
	
	/**
	 * Update the list of user information
	 * 
	 * @param organization String - the organization
	 * @param users List<LdapUser> - the list of user information
	 * @throws LotusException - when there is any error happens
	 */
	private void updateUser(String organization, List<LdapUser> users) throws LotusException {
		for(LdapUser user : users) {
			// update the user information
			this.userManager.updateUser(organization, user);
		}
	}

}
