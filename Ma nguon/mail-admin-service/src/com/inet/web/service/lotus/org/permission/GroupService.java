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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.org.permission.Group;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.manage.ldap.LdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.LdapUserManager;
import com.inet.lotus.org.util.ldap.OrganizationUtil;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.lotus.org.permission.AlterGroupData.ACTION;
import com.inet.web.service.mail.utils.GroupUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.UserUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * GroupService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class GroupService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(GroupService.class);
	private LdapGroupManager groupManager;
	private LdapUserManager userManager;
	private List<HandleAlterGroupService> handleAlterGroupService;
	
	/**
	 * Create <tt>GroupService</tt> instance
	 * 
	 * @param groupManager LdapGroupManager - the group manager
	 * @param userManager LdapUserManager - the user manager
	 */
	public GroupService(LdapGroupManager groupManager, LdapUserManager userManager) {
		this.groupManager = groupManager;
		this.userManager = userManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, GroupUtil.ACTION_MODE_PARAMETER);
		if(GroupUtil.ACTION_ADD.equals(action)) {
			// add new group
			return this.addGroup(request);
		} else if(GroupUtil.ACTION_UPDATE.equals(action)) {
			// update group
			return this.update(request);
		} else if(GroupUtil.ACTION_DELETE.equals(action)) {
			// delete group
			return this.delete(request);
		} else if(GroupUtil.ACTION_ADD_MEMBER.equals(action)) {
			// add member to group
			return this.addMembers(request);
		} else if(GroupUtil.ACTION_REMOVE_MEMBER.equals(action)) {
			// remove member out of group
			return this.removeMembers(request);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * Add new group
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the given JSON object
	 */
	protected JSON addGroup(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, GroupUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the name of group
		String name = WebCommonService.toString(object, GroupUtil.GROUP_NAME);		
		String org = WebCommonService.toString(object, GroupUtil.GROUP_ORGANIZATION);
		if(!StringService.hasLength(name) || !StringService.hasLength(org)) {
			logger.debug("The group name and organization name must not empty");
			return FAILURE_JSON;
		}
		
		LdapGroup group = new LdapGroup();
		
		try {
			// refresh the group information
			List<String> memeberNames = this.refreshGroup(group, object);
			
			// add new group
			this.groupManager.addGroup(org, group);
			AlterGroupData data = new AlterGroupData(org, name, ACTION.ADD);
			data.setInsertMembers(memeberNames);
			List<String> newManagers = new ArrayList<String>(group.getManagers().size());
			for(String managerDN : group.getManagers()) {
				newManagers.add(OrganizationUtil.getRDNValueFromDN(managerDN));
			}
			data.setNewManagers(newManagers);
			this.handle(data);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error during adding group", ex);
			return FAILURE_JSON;
		} catch (WebOSException ex) {
			logger.error("Error during adding group", ex);
			return FAILURE_JSON;
		} catch (Exception ex) {
			logger.error("Invalid data input", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Update the group information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON object
	 */
	protected JSON update(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, GroupUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the name of group
		String name = WebCommonService.toString(object, GroupUtil.GROUP_NAME);		
		String org = WebCommonService.toString(object, GroupUtil.GROUP_ORGANIZATION);
		if(!StringService.hasLength(name) || !StringService.hasLength(org)) {
			logger.debug("The group name and organization name must not empty");
			return FAILURE_JSON;
		}
		try {
			// load the group information from given name
			LdapGroup group = this.groupManager.findByName(name);
			if(group == null) return FAILURE_JSON;
			List<String> existMemberDN = group.getMembers();
			
			// refresh the group information
			List<String> memberNames = this.refreshGroup(group, object);
			// add new group
			this.groupManager.updateGroup(org, group);
			
			List<String> insertMembers = new ArrayList<String>();
			List<String> removeMembers = new ArrayList<String>();
			if(existMemberDN == null || existMemberDN.size() == 0) {
				insertMembers = memberNames;
			} else {
				List<String> existMemberNames = new ArrayList<String>(); 
				for(String memberDN : existMemberDN) {
					// put the member name to list
					existMemberNames.add(OrganizationUtil.getRDNValueFromDN(memberDN));
				}
				
				// get the new members
				for(String memberName : memberNames) {
					if(!existMemberNames.contains(memberName))
						insertMembers.add(memberName);
				}
				
				// get the remove members
				for(String current : existMemberNames) {
					if(!memberNames.contains(current))
						removeMembers.add(current);
				}
			}
			// create AlterGroupData instance
			AlterGroupData data = new AlterGroupData(org, name, ACTION.UPDATE);
			data.setInsertMembers(insertMembers);
			data.setRemoveMembers(removeMembers);
			this.handle(data);
			
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error during updating group", ex);
			return FAILURE_JSON;
		} catch (WebOSException ex) {
			logger.error("Error during updating group", ex);
			return FAILURE_JSON;
		} catch (Exception ex) {
			logger.error("Invalid data input", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Deleting the given group
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON delete(HttpServletRequest request) {
		// get the JSON object from given request
		JSONObject object = WebCommonService.toJSONObject(request, GroupUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the name of group
		String name = WebCommonService.toString(object, GroupUtil.GROUP_NAME);		
		String org = WebCommonService.toString(object, GroupUtil.GROUP_ORGANIZATION);
		if(!StringService.hasLength(name) || !StringService.hasLength(org)) {
			logger.debug("The group name and organization name must not empty");
			return FAILURE_JSON;
		}
		
		try {
			// find the given group
			LdapGroup group = this.groupManager.loadGroup(org, name);
			if(group == null) return FAILURE_JSON;
			
			// delete the given group
			this.groupManager.deleteGroup(org, name);
			
			// create list to contain all user name
			List<String> members = new ArrayList<String>();
			for(String memberDN : group.getMembers()) {
				// add user name to list
				members.add(OrganizationUtil.getRDNValueFromDN(memberDN));
			}
			// create AlterGroupData instance
			AlterGroupData data = new AlterGroupData(org, name, ACTION.DELETE);
			data.setRemoveMembers(members);
			this.handle(data);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error during deleting group");
			return FAILURE_JSON;
		} catch (WebOSException ex) {
			logger.error("Error during deleting group", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Adding the given members to group
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON addMembers(HttpServletRequest request) {
		// get the group and organization
		String groupName = this.getData(request, GroupUtil.GROUP_NAME);
		String organization = this.getData(request, GroupUtil.GROUP_ORGANIZATION);
		// get the list of member
		JSONArray members = WebCommonService.toJSONArray(request, GroupUtil.GROUP_MEMBERS);
		if(!StringService.hasLength(groupName) || !StringService.hasLength(organization)
				|| members == null || members.size() == 0) return FAILURE_JSON;
		try {
			// load given group
			LdapGroup group = this.groupManager.loadGroup(organization, groupName);
			if(group == null) return FAILURE_JSON;			
			
			// get the current member in group
			List<String> currentMembers = group.getMembers();
			if(currentMembers == null) currentMembers = new ArrayList<String>();
			
			// create list to contain user who is added to group
			List<String> memberNames = new ArrayList<String>();
			String userDN;
			for (int index = 0; index < members.size(); index++) {
				String memberName = WebCommonService.toString(members.getJSONObject(index), GroupUtil.GROUP_MEMBER_NAME);				
				// get the user DN from given user name
				userDN = this.userManager.getUserDN(organization, memberName);
				if(!currentMembers.contains(userDN)) {
					currentMembers.add(userDN);
					// add user name to list
					memberNames.add(memberName);
				}
			}
			
			if(memberNames.size() > 0) {
				// put member to group
				group.setMembers(currentMembers);
				// update group
				this.groupManager.updateGroup(organization, group);
				
				// create AlterGroupData instance
				AlterGroupData data = new AlterGroupData(organization, groupName, ACTION.UPDATE);
				data.setInsertMembers(memberNames);
				this.handle(data);
			}
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Remove the members out of group
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON
	 */
	protected JSON removeMembers(HttpServletRequest request) {
		// get the group and organization
		String groupName = this.getData(request, GroupUtil.GROUP_NAME);
		String organization = this.getData(request, GroupUtil.GROUP_ORGANIZATION);
		String uname = this.getData(request, GroupUtil.GROUP_MEMBER_NAME);
		
		// check request
		if(!StringService.hasLength(uname) || !StringService.hasLength(organization) 
				|| !StringService.hasLength(groupName)) return FAILURE_JSON;
		try {
			// load given group
			LdapGroup group = this.groupManager.loadGroup(organization, groupName);
			if(group == null) return FAILURE_JSON;
			
			// get the user DN
			String userDN = this.userManager.getUserDN(organization, uname);
			
			if(group.getManagers().contains(userDN)) {
				// the member is a manager
				JSONObject failure = new JSONObject().accumulate("success", true);						
				// add the type of error
				failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_DELETE_MANAGER);
				return failure;
			}
			
			if(group.getMembers().contains(userDN)) {
				group.getMembers().remove(userDN);
				// update group
				this.groupManager.updateGroup(organization, group);
				
				// create AlterGroupData instance
				AlterGroupData data = new AlterGroupData(organization, groupName, ACTION.UPDATE);
				List<String> removeMembers = new ArrayList<String>();
				removeMembers.add(uname);
				data.setRemoveMembers(removeMembers);
				this.handle(data);	
				return SUCCESS_JSON;
			} else {
				JSONObject failure = new JSONObject().accumulate("success", true);						
				// add the type of error
				failure.accumulate(UserUtil.USER_ERROR_CODE, UserUtil.ERROR_NO_EXIST);
				return failure;
			}			
		} catch (LotusException ex) {
			logger.error("ERROR while remove members out of group", ex);
			return FAILURE_JSON;
		}	
	}
	
	/**
	 * Refresh the group information
	 * 
	 * @param group Group - the given group need to refresh
	 * @param object JSONObject - the given JSON object
	 * @return List<String> - the list of member names
	 */
	private List<String> refreshGroup(Group group, JSONObject object) throws Exception {
		group.setName(WebCommonService.toString(object, GroupUtil.GROUP_NAME));
		group.setDescription(MailCommonUtils.getLdapValue(WebCommonService.toString(object, GroupUtil.GROUP_DESCRIPTION)));
		String org = WebCommonService.toString(object, GroupUtil.GROUP_ORGANIZATION);
		
		// get array contain managers of group
		JSONArray managerArray = WebCommonService.toArray(object, GroupUtil.GROUP_MANAGERS);
		if(managerArray == null || managerArray.size() == 0) 
			throw new Exception("The managers must be not null");
		// create list to contain the manager
		List<String> managers = new ArrayList<String>(managerArray.size());
		List<String> memberNames = new ArrayList<String>();
		String userDN;
		for (int index = 0; index < managerArray.size(); index++) {
			String managerName = WebCommonService.toString(managerArray.getJSONObject(index), GroupUtil.GROUP_MANAGER_NAME);
			memberNames.add(managerName);
			// get the user DN from given user name
			userDN = this.userManager.getUserDN(org, managerName);
			// add user DN to list
			managers.add(userDN);
		}
		// put managers to group
		group.setManagers(managers);
		
		// get array contain members of group
		JSONArray memberArray = WebCommonService.toArray(object, GroupUtil.GROUP_MEMBERS);
		
		// create list to contain the members
		List<String> menbers = new ArrayList<String>();
		if(memberArray != null && memberArray.size() != 0) {
			for (int index = 0; index < memberArray.size(); index++) {
				String memberName = WebCommonService.toString(memberArray.getJSONObject(index), GroupUtil.GROUP_MEMBER_NAME);
				memberNames.add(memberName);
				// get the user DN from given user name
				userDN = this.userManager.getUserDN(org, memberName);
				// add user DN to list
				menbers.add(userDN);
			}
		}		
		menbers.addAll(managers);
		// put members to group
		group.setMembers(menbers);
		return memberNames;
	}
	
	/**
	 * Handle after altering group
	 * 
	 * @param data AlterGroupData - the alter data
	 */
	private void handle(AlterGroupData data) throws WebOSException {
		if(this.handleAlterGroupService == null || data == null) return;
		
		for(HandleAlterGroupService alterGroupService : this.handleAlterGroupService) {
			alterGroupService.execute(data);
		}
	}

	/**
	 * @return the handleAlterGroupService
	 */
	public List<HandleAlterGroupService> getHandleAlterGroupService() {
		return this.handleAlterGroupService;
	}

	/**
	 * @param handleAlterGroupService the handleAlterGroupService to set
	 */
	public void setHandleAlterGroupService(
			List<HandleAlterGroupService> handleAlterGroupService) {
		this.handleAlterGroupService = handleAlterGroupService;
	}
	
}
