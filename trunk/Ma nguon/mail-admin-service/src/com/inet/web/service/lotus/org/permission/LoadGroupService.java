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
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.permission.LotusRole;
import com.inet.lotus.mail.permission.Permission;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.lotus.org.util.ldap.OrganizationUtil;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.GroupUtil;
import com.inet.web.service.mail.utils.PermissionCacheService;
import com.inet.web.service.mail.utils.UserUtil;

/**
 * LoadGroupService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadGroupService extends AbstractLotusService {
	private INetLogger logger = INetLogger.getLogger(LoadGroupService.class);
	private ILdapGroupManager groupManager;
	private ILdapUserManager userManager;

	/**
	 * The constructor
	 * 
	 * @param groupManager ILdapGroupManager - the group manager
	 * @param userManager ILdapUserManager - the user manager
	 */
	public LoadGroupService(ILdapGroupManager groupManager,
			ILdapUserManager userManager) {
		this.groupManager = groupManager;
		this.userManager = userManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = getData(request, GroupUtil.ACTION_MODE_PARAMETER);
		if(GroupUtil.ACTION_LOAD_PERMIT_GROUP.equals(action)) {
			return this.loadPermitGroup(request);
		} if(GroupUtil.ACTION_LOAD.equals(action)) {
			// load group information
			return this.load(request);
		} else if(GroupUtil.ACTION_SEARCH_GROUPS.equals(action)) {
			// load all group
			return this.search(request);
		}
		
		return FAILURE_JSON;
	}
	
	/**
	 * Load group information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON load(HttpServletRequest request) {
		// get the parameter from request
		String name = this.getData(request, GroupUtil.GROUP_NAME);
		String organization = this.getData(request, GroupUtil.GROUP_ORGANIZATION);		
		if(!StringService.hasLength(name) || !StringService.hasLength(organization)) return FAILURE_JSON;
		
		try {
			// load the group information
			LdapGroup group = this.groupManager.loadGroup(organization, name);
			if(group == null) return FAILURE_JSON;
			
			// convert group to JSON
			JSONObject groupObject = GroupUtil.convertToJSON(group);
			
			if(group.getMembers() != null && group.getMembers().size() > 0) {
				// create list to contain member names
				List<String> memberNames = new ArrayList<String>();
				for(String memberDN : group.getMembers()) {
					memberNames.add(OrganizationUtil.getRDNValueFromDN(memberDN));
				}
				
				// find all user from given user's names
				List<LdapUser> members = this.userManager.findUsers(organization, memberNames);
				
				/**
				 * convert the member to JSON
				 */
				if(members != null && members.size() > 0) {
					List<JSON> jsons = new ArrayList<JSON>();
					List<JSON> managerJsons = new ArrayList<JSON>();
					JSONObject temp;
					for(LdapUser user : members) {
						temp = new JSONObject();
						temp.accumulate(UserUtil.USER_NAME, user.getName())
							.accumulate(UserUtil.USER_FULL_NAME, user.getFullName());
						if(group.getManagers().contains(user.getDistinguishedName())) {
							managerJsons.add(temp);
						} else {
							jsons.add(temp);
						}						
					}
					groupObject.accumulate(GroupUtil.GROUP_MEMBERS, JSONService.toJSONArray(jsons))
							   .accumulate(GroupUtil.GROUP_MANAGERS, JSONService.toJSONArray(managerJsons));
				}
			}
			return groupObject;
		} catch (LotusException ex) {
			logger.error("ERROR while loading group", ex);
			return FAILURE_JSON;
		}
	}
	

	/**
	 * Load all group of the organization
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	private JSON search(HttpServletRequest request) {
		// get the parameter from given request
		String organization = getData(request, GroupUtil.GROUP_ORGANIZATION);
		if(!StringService.hasLength(organization)) return FAILURE_JSON;
		String key = getData(request, GroupUtil.GROUP_SEARCH_KEY);
		
		try {
			// find all groups of organization
			List<LdapGroup> groups = this.groupManager.findRealGroups(key, organization);
			
			JSONObject object = new JSONObject();
			object.accumulate(GroupUtil.GROUP_SEARCH_RESULT_LIST, GroupUtil.convertToJSON(groups));
			object.accumulate(GroupUtil.GROUP_TOTAL_SEARCH_RESULT, groups!=null?groups.size():0);
			return object;			
		} catch (LotusException ex) {
			logger.error("ERROR while load all group of organization",ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Load all group of the organization
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	private JSON loadPermitGroup(HttpServletRequest request) {
		// get the parameter from given request
		String organization = getData(request, GroupUtil.GROUP_ORGANIZATION);
		if(!StringService.hasLength(organization)) return FAILURE_JSON;
		
		// get permission from cache
		Permission permission = PermissionCacheService.get(getCode());
		// check the permission
		if(permission == null || permission.getAccessRole() == LotusRole.ROLE_NONE){
			logger.debug("There is no permission to load group");
			return FAILURE_JSON;
		}
		
		// The role on organization
		LotusRole role = LotusRole.ROLE_NONE;
		try {
			// get the roles on domain
			HashMap<String, LotusRole> roles = permission.getRoles();
			if(roles == null || !roles.containsKey(organization)) {
				// load role
				role = LoadRoleService.loadRole(this.groupManager, permission, organization, this.getUserName());
				// put data to cache
				PermissionCacheService.put(getCode(), permission);
			} else {
				// get the role from cache
				role = roles.get(organization);
			}
			
			// check role
			if(role == LotusRole.ROLE_NONE) return FAILURE_JSON;
			
			List<LdapGroup> groups = null;
			if(role == LotusRole.ROLE_DOMAIN) {
				// find all group in organization
				groups = this.groupManager.findAllRealGroups(organization);
			} else if(role == LotusRole.ROLE_DEPARTMENT) {
				// find groups of which user is a manager
				groups = this.groupManager.findOwnerGroups(organization, getUserName());
			}
			if(role == LotusRole.ROLE_DOMAIN || role == LotusRole.ROLE_SUPER) {
				if(groups == null) {
					groups = new ArrayList<LdapGroup>();
				}
				// create none group
				LdapGroup group = new LdapGroup();
				group.setName(GroupUtil.NONE_GROUP);
				group.setDescription(GroupUtil.NONE_GROUP);
				groups.add(0, group);
			}
			if(groups == null || groups.size() == 0) return FAILURE_JSON;
			
			JSONObject object = new JSONObject();
			object.accumulate(GroupUtil.GROUP_LIST_GROUP, GroupUtil.convertToJSON(groups))
				  .accumulate(GroupUtil.GROUP_ROLE, role);
			
			return object;
		} catch (LotusException ex) {
			logger.error("ERROR while load all group of organization",ex);
			return FAILURE_JSON;
		}
	}
	
}
