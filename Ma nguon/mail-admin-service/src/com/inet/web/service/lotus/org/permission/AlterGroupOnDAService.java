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
import com.inet.lotus.mail.domain.ldap.LdapDomainAdministrator;
import com.inet.lotus.mail.manage.domain.ldap.ILdapDomainAdminManager;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.util.ldap.OrganizationUtil;
import com.inet.web.exception.WebOSException;

/**
 * AlterGroupOnDAService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AlterGroupOnDAService implements HandleAlterGroupService {
	private INetLogger logger = INetLogger.getLogger(AlterGroupOnDAService.class);
	private ILdapDomainAdminManager adminManager;
	private ILdapGroupManager groupManager;
	
	/**
	 * The constructor
	 * 
	 * @param adminManager ILdapDomainAdminManager - the domain administrator manager
	 * @param groupManager ILdapDomainAdminManager - the group manager
	 */
	public AlterGroupOnDAService(ILdapDomainAdminManager adminManager,
			ILdapGroupManager groupManager) {
		this.adminManager = adminManager;
		this.groupManager = groupManager;
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
		case DELETE:
			this.updateManager(data);
			break;
		case UPDATE:
			this.updateManager(data);
		}

	}
	
	/**
	 * Handle  after adding group
	 * 
	 * @param data AlterGroupData - the altering data information
	 * @throws WebOSException - when there is any error happens
	 */
	private void addGroup(AlterGroupData data) throws WebOSException {
		if(data.getNewManagers() != null && data.getNewManagers().size() > 0) {
			try {
				// get the postmaster of domain				
				LdapDomainAdministrator administrator = this.adminManager.getAdminOfDomain(data.getOrganization());
				if(administrator == null) throw new WebOSException("Error while update administrator of domain");
				
				// update the manager for domain
				this.updateAdd(administrator, data.getNewManagers());
				// update the domain administrator information
				this.adminManager.updateDA(data.getOrganization(), administrator);
			} catch (LotusException ex) {
				logger.error("ERROR while adding group for user", ex);
				throw new WebOSException(ex.getMessage(), ex);
			}
		}		
	}
	
	/**
	 * update the manager of domain
	 * 
	 * @param data AlterGroupData - the altering data information
	 * @throws WebOSException - when there is any error happens
	 */
	public void updateManager(AlterGroupData data) throws WebOSException {
		// get the postmaster of domain				
		LdapDomainAdministrator administrator = this.adminManager.getAdminOfDomain(data.getOrganization());
		if(administrator == null) throw new WebOSException("Error while update administrator of domain");
		
		// find all group in domain
		List<LdapGroup> groups = this.groupManager.findAllGroups(data.getOrganization());
		if(groups == null || groups.size() == 0) 
			throw new WebOSException("Error while load all of organization");
		
		List<String> unames = new ArrayList<String>();
		for(LdapGroup group : groups) {
			for(String managerDN : group.getManagers()) {
				// get the user name from given DN
				String uname = OrganizationUtil.getRDNValueFromDN(managerDN);
				if(!unames.contains(uname)) {
					// put the given user name to list
					unames.add(uname);
				}
			}
		}
		
		administrator.setManagers(unames);
		// update the domain administrator
		this.adminManager.updateDA(data.getOrganization(), administrator);
		
	}
	
	/**
	 * Update the manager for mail domain
	 * 
	 * @param administrator LdapDomainAdministrator - the manager of domain
	 * @param managers List<String> - the list of user name
	 */
	private void updateAdd(LdapDomainAdministrator administrator, List<String> managers) {
		if(managers == null || managers.size() == 0) return;
		
		// get the manager of domain
		List<String> currentManagers = administrator.getManagers();
		if(currentManagers == null) {
			currentManagers = managers;
		} else {
			// add all new manager to current manager
			currentManagers.addAll(managers);
		}
		
		// put the manager for domain
		administrator.setManagers(currentManagers);
	}
	
}
