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
package com.inet.web.service.mail.permission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.permission.Permission;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.PermissionCacheService;

/**
 * PermissionService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadPermissionService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(LoadPermissionService.class);
	
	/**
	 * The key for action mode
	 */
	private static final String ACTION_MODE_PARAMETER = "action";
	
	/**
	 * The key for save action
	 */
	private static final String ACTION_LOAD_PERMISSION = "load";
	
	/**
	 * The key for save action
	 */
	private static final String USER_ROLE = "role";	
	
	/**
	 * The default constructor
	 */
	public LoadPermissionService() {}


	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, ACTION_MODE_PARAMETER);
		if(ACTION_LOAD_PERMISSION.equals(action)) {
			return this.loadRole(request);
		}
		return SUCCESS_JSON;
	}
	
	
	/**
	 * load the role of user
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the returned JSON object
	 */
	private JSON loadRole(HttpServletRequest request) {
		try {
			// get the permission
			Permission permission = PermissionCacheService.get(this.getCode());
			
			/*if(permission == null) {				
				permission = this.domainAdminManager.getPermission(this.getCode());
				PermissionCacheService.put(this.getCode(), permission);
			}*/
			
			// get the role of user
			String role = permission.getAccessRole().toString();
			JSONObject object = new JSONObject();
			object.accumulate(USER_ROLE, role);
			
			return object;
		} catch (LotusException ex) {
			logger.error("ERROR while loading role", ex);
			return FAILURE_JSON;
		}
	}

}
