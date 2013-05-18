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
package com.inet.web.service.mail.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.lotus.org.permission.Group;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.web.common.json.JSONService;

/**
 * GroupUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class GroupUtil {
	///////////////////////////////////////////////////////////
	///////////// The keys for action///////////////////////////
	/**
	 * The key for action mode
	 */
	public static final String ACTION_MODE_PARAMETER = "action";
	
	/**
	 * The key for add group action
	 */
	public static final String ACTION_ADD = "save";
	
	/**
	 * The key for update group action
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * The key for add member action
	 */
	public static final String ACTION_ADD_MEMBER = "addmember";
	
	/**
	 * The key for remove member out of group action
	 */
	public static final String ACTION_REMOVE_MEMBER = "removemember";
	
	/**
	 * The key for delete group action
	 */
	public static final String ACTION_DELETE = "delete";
	
	/**
	 * The key for load group action
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The key for load all group action
	 */
	public static final String ACTION_LOAD_PERMIT_GROUP = "loadall";
	
	/**
	 * The key for search group action
	 */
	public static final String ACTION_SEARCH_GROUPS = "search";
	
	/**
	 * The JSOn key for request object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/////////////////////////////////////////////////////////
	public static final String NONE_GROUP = "---";
	
	///////////////////////////////////////////////////////////
	//////////// The JSON keys for group information //////////
	/**
	 * The JSON key for group name
	 */
	public static final String GROUP_NAME = "name";
	
	/**
	 * The JSON key for group description
	 */
	public static final String GROUP_DESCRIPTION = "description";
	
	/**
	 * The JSON key for list of managers
	 */
	public static final String GROUP_MANAGERS = "managers";
	
	/**
	 * The JSON key for the name of manager
	 */
	public static final String GROUP_MANAGER_NAME = "uname";
	
	/**
	 * The JSON key for list of members 
	 */
	public static final String GROUP_MEMBERS = "members";
	
	/**
	 * The JSON key for member name
	 */
	public static final String GROUP_MEMBER_NAME = "uname";
	
	/**
	 * The JSON key for organization
	 */
	public static final String GROUP_ORGANIZATION = "org";
	
	/**
	 * The JSON key for list of groups
	 */
	public static final String GROUP_LIST_GROUP = "list";
	
	/**
	 * The JSON key for role on group
	 */
	public static final String GROUP_ROLE = "role";
	
	/**
	 * The JSON key for list of search result
	 */
	public static final String GROUP_SEARCH_RESULT_LIST = "rows";
	
	/**
	 * The JSON key for total search result
	 */
	public static final String GROUP_TOTAL_SEARCH_RESULT = "results";
	
	/**
	 * The JSON key for search key
	 */
	public static final String GROUP_SEARCH_KEY = "key";
	
	/**
	 * Convert group to JSON object
	 * 
	 * @param group Group - the given group
	 * @return JSONObject - the return JSON object
	 */
	public static final JSONObject convertToJSON(Group group) {
		JSONObject object = new JSONObject();
		object.accumulate(GROUP_NAME, group.getName())
			  .accumulate(GROUP_DESCRIPTION, MailCommonUtils.getDisplayValue(group.getDescription()));
		
		return object;
	}
	
	/**
	 * Convert list of group to JSON
	 * 
	 * @param groups List<LdapGroup> - the given groups
	 * @return JSON - the return JSON
	 */
	public static final JSON convertToJSON(List<LdapGroup> groups) {
		List<JSONObject> array = new ArrayList<JSONObject>();
		if(groups != null) {
			for(LdapGroup group : groups) {
				array.add(GroupUtil.convertToJSON(group));			
			}
		}
		
		return JSONService.toJSONArray(array);
	}
}
