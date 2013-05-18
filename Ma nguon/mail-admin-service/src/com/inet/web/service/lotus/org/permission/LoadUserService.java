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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.conf.MailConfiguration;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.lotus.mail.permission.LotusRole;
import com.inet.lotus.mail.permission.Permission;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.PermissionCacheService;
import com.inet.web.service.mail.utils.UserUtil;

/**
 * LoadUserService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadUserService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(LoadUserService.class);	
	private ILdapUserManager userManager;
	private LdapMailAccountManager mailAccountManager;
	private LdapAccountManager accountManager;
	private ILdapGroupManager groupManager;
	private MailConfiguration mailConfiguration;

	/**
	 * The constructor
	 * 
	 * @param userManager ILdapUserManager - the user manager
	 * @param mailAccountManager LdapMailAccountManager - the mail account manager
	 */
	public LoadUserService(ILdapUserManager userManager,
			LdapMailAccountManager mailAccountManager, LdapAccountManager accountManager,
			ILdapGroupManager groupManager, MailConfiguration mailConfiguration) {
		this.mailAccountManager = mailAccountManager;
		this.userManager = userManager;
		this.accountManager = accountManager;
		this.groupManager = groupManager;
		this.mailConfiguration = mailConfiguration;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(UserUtil.ACTION_LOAD_FULL.equals(action)) {
			return this.loadUserAndEmails(request);
		} else if(UserUtil.ACTION_LOAD_FULL_BY_LOTUS_ID.equals(action)) {
			return this.loadFullByLotusId(request);
		} else if(UserUtil.ACTION_LOAD_BY_LOTUS_ID.equals(action)) {
			return this.loadByLotusId(request);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * Load user informations and all its email addresses
	 * [SU DUNG KHI DOUBLE CLICK LEN 1 USER]
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON loadUserAndEmails(HttpServletRequest request) {
		// get the user name from given request
		String userName = getData(request, UserUtil.USER_NAME);
		String organization = getData(request, UserUtil.USER_ORGANIZATION);
		if(!StringService.hasLength(userName) || !StringService.hasLength(organization)) return FAILURE_JSON;
		
		try {
			// load the user information
			LdapUser user = this.userManager.loadUser(organization, userName);
			if(user == null) return FAILURE_JSON;
			
			// convert user object to JSON
			JSONObject userObject = UserUtil.convertToJSON(user);
			userObject.accumulate(UserUtil.USER_DISPLAY_USER_NAME, 
					MailCommonUtils.getDUserName(userName, this.mailConfiguration));
			
			// get all this user's email addresses
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserName(userName);
			if(mailAccounts != null && mailAccounts.size() > 0) {
				userObject.accumulate(AccountUtil.ACCOUNT_EMAIL_ACCOUNTS,
						MailAccountUtil.convertToJSONFull(mailAccounts));
			}
			return userObject;
		} catch (LotusException ex) {
			logger.error("ERROR while load account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Load user informations and all its email addresses
	 * [SU DUNG KHI CHON 1 USER CO SAN TREN LOTUS]
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON loadFullByLotusId(HttpServletRequest request) {
		// get the user name from given request
		String userName = getData(request, UserUtil.USER_NAME);		
		if(!StringService.hasLength(userName)) return FAILURE_JSON;
		
		try {
			// load the user information
			LdapUser user = this.userManager.findByName(userName);
			// the JSON object to return
			JSONObject userObject;
			
			boolean edit = (this.mailConfiguration != null && !this.mailConfiguration.getAdminID().equals(userName));
			if(user == null) { // the given user doesn't exist in any organization
				// load the account information
				LdapAccount account = this.accountManager.findByName(userName, true);
				if(account == null) return FAILURE_JSON;
				
				// convert account to JSON object as user
				userObject = UserUtil.convertToJSON(account);
			} else {
				// get the organization which this user depend on
				String org = MailCommonUtils.getOrganizationFromUserDN(user.getDistinguishedName());
				if(!StringService.hasLength(org)) return FAILURE_JSON;
				
				// convert the user information to JSON
				userObject = UserUtil.convertToJSON(user);				
				userObject.accumulate(UserUtil.USER_ORGANIZATION, org);
				// check the user permission
				edit = this.checkPermission(org, user)!=LotusRole.ROLE_NONE?true:false;
			}
			userObject.accumulate(UserUtil.USER_EDIT, edit);
			userObject.accumulate(UserUtil.USER_DISPLAY_USER_NAME, 
					MailCommonUtils.getDUserName(userName, this.mailConfiguration));
			// get all this user's email addresses
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserName(userName);
			if(mailAccounts != null && mailAccounts.size() > 0) {
				userObject.accumulate(UserUtil.USER_EMAIL_ACCOUNTS,
						MailAccountUtil.convertToJSONFull(mailAccounts));
			}
			return userObject;
		} catch (LotusException ex) {
			logger.error("ERROR while load account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Check the permission on user in organization
	 * 
	 * @param organization String - the organization
	 * @param user LdapUser - the given user information
	 * @return LotusRole - the role on or
	 */
	private LotusRole checkPermission(String organization, LdapUser user) {
		// get permission from cache
		Permission permission = PermissionCacheService.get(getCode());
		// check the permission
		if(permission == null || permission.getAccessRole() == LotusRole.ROLE_NONE){
			logger.debug("There is no permission to load group");
			//return false;
			return LotusRole.ROLE_NONE;
		}
		
		// The role on organization
		LotusRole role = LotusRole.ROLE_NONE;
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
		if(role != LotusRole.ROLE_DEPARTMENT) return role;
		
		if(user != null && user.getGroups() != null && user.getGroups().size() > 0) {
			// find groups of which user is a manager
			List<LdapGroup> groups = this.groupManager.findOwnerGroups(organization, getUserName());
			if(groups != null && groups.size() > 0) {
				for(LdapGroup group : groups) {
					// user can manage given user
					if(user.getGroups().contains(group.getName())) {
						return LotusRole.ROLE_DEPARTMENT;
					}
				}
			}
		}
		
		return LotusRole.ROLE_NONE;
	}
	
	/**
	 * Load user informations and all its email addresses
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON loadByLotusId(HttpServletRequest request) {
		// get the user name from given request
		String userName = getData(request, UserUtil.USER_NAME);
		String mailOrg = getData(request, UserUtil.USER_ORGANIZATION);
		if(!StringService.hasLength(userName) || !StringService.hasLength(mailOrg)) return FAILURE_JSON;		
		
		try {
			// load the user information
			LdapUser user = this.userManager.findByName(userName);
			// the JSON object to return
			JSONObject userObject;
			
			// check the role on mail domain
			LotusRole role = this.checkPermission(mailOrg, null); 
			boolean edit = (role==LotusRole.ROLE_DOMAIN	|| role==LotusRole.ROLE_SUPER);
			
			if(user == null) { // the given user doesn't exist in any organization
				// load the account information
				LdapAccount account = this.accountManager.findByName(userName);
				if(account == null) return FAILURE_JSON;
				
				// convert account to JSON object as user
				userObject = UserUtil.convertToJSON(account);
				
			} else {
				// get the organization which this user depend on
				String org = MailCommonUtils.getOrganizationFromUserDN(user.getDistinguishedName());
				if(!StringService.hasLength(org)) return FAILURE_JSON;
				
				// convert the user information to JSON
				userObject = UserUtil.convertToJSON(user);
				userObject.accumulate(UserUtil.USER_ORGANIZATION, org);
				if(!edit && mailOrg.equals(org)) {
					// check the user permission
					edit = this.checkPermission(org, user)!=LotusRole.ROLE_NONE?true:false;
				}				
			}			
			userObject.accumulate(UserUtil.USER_EDIT, edit);
			return userObject;
		} catch (LotusException ex) {
			logger.error("ERROR while load account", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Load the user information
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the return JSON object
	 */
	protected JSON load(HttpServletRequest request) {
		// get the user name from given request
		String userName = getData(request, UserUtil.USER_NAME);
		String organization = getData(request, UserUtil.USER_ORGANIZATION);
		if(!StringService.hasLength(userName) || !StringService.hasLength(organization)) return FAILURE_JSON;
		
		try {
			// load the user information
			LdapUser user = this.userManager.loadUser(organization, userName);
			if(user == null) return FAILURE_JSON;
			
			return UserUtil.convertToJSON(user);			
		} catch (LotusException ex) {
			logger.error("ERROR while load account", ex);
			return FAILURE_JSON;
		}
	}

}
