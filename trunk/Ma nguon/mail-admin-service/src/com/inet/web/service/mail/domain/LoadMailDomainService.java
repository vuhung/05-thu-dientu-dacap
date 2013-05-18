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
package com.inet.web.service.mail.domain;

import java.util.ArrayList;
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
import com.inet.lotus.mail.domain.DomainAdministrator;
import com.inet.lotus.mail.domain.MailDomain;
import com.inet.lotus.mail.domain.ldap.LdapMailDomain;
import com.inet.lotus.mail.manage.domain.ldap.ILdapDomainAdminManager;
import com.inet.lotus.mail.manage.domain.ldap.ILdapMailDomainManager;
import com.inet.lotus.mail.permission.LotusRole;
import com.inet.lotus.mail.permission.Permission;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.util.ldap.OrganizationUtil;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.MailDomainUtil;
import com.inet.web.service.mail.utils.PermissionCacheService;

/**
 * SearchDomainService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadMailDomainService extends AbstractLotusService {
	private INetLogger logger = INetLogger.getLogger(LoadMailDomainService.class);
	private ILdapMailDomainManager mailDomainManager;
	private ILdapDomainAdminManager domainAdministratorManager;
	private  LdapAccountManager accountManage;
	private ILdapGroupManager groupManager;
	
	/**
	 * The constructor
	 * 
	 * @param mailDomainManager IMailDomainManager - the domain manager
	 */
	public LoadMailDomainService(ILdapMailDomainManager mailDomainManager
			, ILdapDomainAdminManager domainAdministratorManager
			, LdapAccountManager accountManager
			, ILdapGroupManager groupManager) {
		this.mailDomainManager = mailDomainManager;
		this.domainAdministratorManager = domainAdministratorManager;
		this.accountManage = accountManager;
		this.groupManager = groupManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailDomainUtil.ACTION_MODE_PARAMETER);
		if(MailDomainUtil.ACTION_LOAD_ALL_DOMAIN.equals(action)) {
			// load all domains depend on permission
			return this.loadPermitDomains();
		} else if(MailDomainUtil.ACTION_LOAD_DOMAIN.equals(action)) {			
			String domainName = this.getData(request, MailDomainUtil.DOMAIN_NAME);
			// load given domain
			return this.loadDomain(domainName);
		} else if(MailDomainUtil.ACTION_LOAD_DR_DOMAIN.equals(action)) {
			return this.loadDRDomains();
		} else if(MailDomainUtil.ACTION_LOAD_ALL_DOMAIN_IN_SYSTEM.equals(action)) {
			// load all domains in system
			return this.loadAllDomains();
		}
		return FAILURE_JSON;
	}
	
	/**
	 * load all domain
	 * 
	 * @return JSON - the returned JSON object
	 */
	private JSON loadAllDomains() {
		try {
			logger.debug("The code of user: " + this.getCode());
			Permission permission = PermissionCacheService.get(getCode());
			// check the permission
			if(permission == null || permission.getAccessRole() == LotusRole.ROLE_NONE){
				logger.debug("There is no permission to load domain");
				return FAILURE_JSON;
			}
			// load all domain in database
			List<LdapMailDomain> domains = this.mailDomainManager.findAll();
			logger.debug("DOMAIN: " + domains);
			
			// create list to contain all JSON object
			List<JSON> jsons = new ArrayList<JSON>();
			
			// create empty JSON object for all domain
			JSONObject emptyObject = new JSONObject();
			emptyObject.accumulate(MailDomainUtil.DOMAIN_ID, "")
		 	 		   .accumulate(MailDomainUtil.DOMAIN_NAME, "")
		 	 		   .accumulate(MailDomainUtil.DOMAIN_DISPLAY_NAME, MailDomainUtil.NONE);
			jsons.add(emptyObject);
			
			if(domains != null) {
				for(MailDomain domain : domains) {
					 // put JSON object to list
					 JSONObject object = MailDomainUtil.convertToJSON(domain);
					 jsons.add(object);
				}
				return JSONService.toJSONArray(jsons);
			}
		} catch (LotusException ex) {
			logger.error("ERROR while getting all domains", ex);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * load all domain
	 * 
	 * @return JSON - the returned JSON object
	 */
	private JSON loadPermitDomains() {
		try {
			logger.debug("The code of user: " + this.getCode());
			// load all domain in database
			List<LdapMailDomain> domains = null;		
			Permission permission = PermissionCacheService.get(getCode());
			// check the permission
			if(permission == null || permission.getAccessRole() == LotusRole.ROLE_NONE){
				logger.debug("There is no permission to load domain");
				return FAILURE_JSON;
			}
			
			if(permission.getAccessRole() == LotusRole.ROLE_SUPER) {
				domains = this.mailDomainManager.findAll();		
				if(domains != null) {
					for(MailDomain domain : domains) {					
						domain.setAdminEdit(true);
					}
				}
			} else {
				domains = this.mailDomainManager.findByUserName(this.getUserName());
			}
			
			logger.debug("DOMAIN: " + domains);
			if(domains != null) {
				// create list to contain all JSON object
				List<JSON> jsons = new ArrayList<JSON>();
				for(MailDomain domain : domains) {
					 // put JSON object to list
					 JSONObject object = MailDomainUtil.convertToJSON(domain);
					 object.accumulate(MailDomainUtil.TREE_LEAFT, true)
					 	   .accumulate(MailDomainUtil.TREE_ICON, "icon-email-treeview-br")
					 	   .accumulate(MailDomainUtil.TREE_CLS, "email-treeview-text");
					 		
					 jsons.add(object);
				}
				return JSONService.toJSONArray(jsons);
			}
		} catch (LotusException ex) {
			logger.error("ERROR while getting all domains", ex);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * load all domains on which user have domain role
	 * 
	 * @return JSON - the return JSON data
	 */
	protected JSON loadDRDomains() {
		logger.debug("load all domains on which user have domain role");
		try {
			// load all default group
			List<LdapGroup> groups = this.groupManager.findDefaults(this.getUserName());
			if(groups == null || groups.size() == 0) return FAILURE_JSON;
			
			List<String> domainNames = new ArrayList<String>();
			for(LdapGroup group : groups) {
				domainNames.add(MailCommonUtils.getOrganizationFromGroupDN(group.getDistinguishedName()));
			}
			
			// load domains from given domain names
			List<LdapMailDomain> domains = this.mailDomainManager.findByRDN(domainNames);
			if(domains == null || domains.size() == 0) return FAILURE_JSON;
			
			List<JSONObject> jsons = new ArrayList<JSONObject>(groups.size());
			JSONObject object = null;
			for(LdapMailDomain domain : domains) {
				object = MailDomainUtil.convertToJSON(domain);
				jsons.add(object);
			}
			
			// return data
			return JSONService.toJSONArray(jsons);
		} catch (LotusException ex) {
			logger.error("Error while load default groups", ex);
			return FAILURE_JSON;			
		}
	}
	
	/**
	 * load domain with given domain name
	 * 
	 * @param domainName String - domain name
	 * @return JSON - the domain as JSON
	 */
	private JSON loadDomain(String domainName) {
		// check domain name
		if(!MailUtil.isDomainName(domainName)) return FAILURE_JSON;
		try {
			// load domain from database
			MailDomain domain = this.mailDomainManager.findByName(domainName);
			if(domain != null) {
				// convert mail domain to JSON
				JSONObject object = MailDomainUtil.convertToJSON(domain);
				
				// find the domain administrator of domain
				DomainAdministrator admin = this.domainAdministratorManager.getAdminOfDomain(domainName);
				object.remove(MailDomainUtil.DOMAIN_EDIT_ACCOUNT);
				if(admin == null) return FAILURE_JSON;
				object.accumulate(MailDomainUtil.DOMAIN_EDIT_ACCOUNT, admin.isEditAccount());
				
				// load the default group which contain the manager of domain
				LdapGroup defaultGroup = this.groupManager.findDefault(domainName);
				if(defaultGroup == null) return FAILURE_JSON;
				
				//Create list JSONObject
				List<JSONObject> jsons = new ArrayList<JSONObject>();
				
				//convert form DN to RDN
				List<String> rdns = new ArrayList<String>();
				for(String dn: defaultGroup.getManagers()){
					rdns.add(OrganizationUtil.getRDNValueFromDN(dn));
				}
				
				//load all LDAP common account with RDN
				List<LdapAccount> ldapAccounts =  accountManage.findByRDN(rdns);
				for(LdapAccount account : ldapAccounts){
					JSONObject ownerInfo = new JSONObject();
					ownerInfo.accumulate(AccountUtil.ACCOUNT_USER_NAME, StringService.getValue(account.getName()));
					ownerInfo.accumulate(AccountUtil.ACCOUNT_FIRST_NAME, StringService.getValue(account.getFirstName()));
					ownerInfo.accumulate(AccountUtil.ACCOUNT_MIDDLE_NAME, StringService.getValue(account.getMiddleName()));
					ownerInfo.accumulate(AccountUtil.ACCOUNT_LAST_NAME, StringService.getValue(account.getLastName()));
					jsons.add(ownerInfo);
				}
				
				object.accumulate(MailDomainUtil.DOMAIN_USER_LIST, JSONService.toJSONArray(jsons));
				return object;
			}
		} catch (LotusException ex) {
			logger.error("ERROR while getting domain", ex);
		}
		return FAILURE_JSON;
	}
}
