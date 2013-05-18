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

import java.util.HashMap;
import java.util.List;

import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.permission.LotusRole;
import com.inet.lotus.mail.permission.Permission;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.LdapGroupManager;

/**
 * LoadRoleService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadRoleService {

	/**
	 * Load the user's role
	 * 
	 * @param groupManager - the group manager
	 * @param organization String - the given organization
	 * @param userName String - the user's name
	 * @throws LotusException - when there is any error happens
	 */
	public static LotusRole loadRole(ILdapGroupManager groupManager, 
			Permission permission, String organization, String userName) throws LotusException {
		List<LdapGroup> groups = groupManager.findAllOwnerGroups(organization, userName);
		// get the roles
		HashMap<String, LotusRole> roles = permission.getRoles();
		if(roles == null) roles = new HashMap<String, LotusRole>();

		// The role on organization
		LotusRole role = LotusRole.ROLE_DEPARTMENT;
		if(groups == null || groups.size() == 0) {
			// get the role
			role = permission.getAccessRole()==LotusRole.ROLE_SUPER?
					LotusRole.ROLE_SUPER:LotusRole.ROLE_NONE;
			roles.put(organization, role);
			permission.setRoles(roles);
			return role;
		} 
		
		for(LdapGroup group : groups) {
			if(LdapGroupManager.DEFAULT_GROUP.equals(group.getName())) {
				// This case: user is a manager of domain
				role = LotusRole.ROLE_DOMAIN;
				break;
			}
		}
		roles.put(organization, role);
		permission.setRoles(roles);
		
		return role;
	}
}
