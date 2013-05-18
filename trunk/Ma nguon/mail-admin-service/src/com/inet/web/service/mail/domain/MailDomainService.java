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
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.domain.ldap.LdapDomainAdministrator;
import com.inet.lotus.mail.domain.ldap.LdapMailDomain;
import com.inet.lotus.mail.manage.domain.ldap.LdapMailDomainManager;
import com.inet.lotus.mail.report.ReportResult;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.conf.Configuration;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.MailDomainUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * MailDomainService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailDomainService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(MailDomainService.class);
	private LdapMailDomainManager mailDomainManager;
	
	/**
	 * The constructor
	 * 
	 * @param mailDomainManager IMailDomainManager - the domain manager
	 */
	public MailDomainService(LdapMailDomainManager mailDomainManager) {
		this.mailDomainManager = mailDomainManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailDomainUtil.ACTION_MODE_PARAMETER);
		if(MailDomainUtil.ACTION_SAVE.equals(action)) {
			// add new domain
			return this.addDomain(request);
		} else if(MailDomainUtil.ACTION_UPDATE.equals(action)) {
			// update domain
			return this.updateDomain(request);
		} else if(MailDomainUtil.ACTION_DELETE.equals(action)) {
			// delete domain
			return this.deleteDomain(request);
		} else if(MailDomainUtil.ACTION_ACTIVE.equals(action)) {
			// active/inactive domain
			return this.activeDomain(request);
		} else if(MailDomainUtil.ACTION_REPORT.equals(action)) {
			return this.report(request);
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * add new mail domain
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON addDomain(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailDomainUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get domain name and description
		String domainName = WebCommonService.toString(object, MailDomainUtil.DOMAIN_NAME);
		String domainDesc = MailCommonUtils.getLdapValue(WebCommonService.toString(object, MailDomainUtil.DOMAIN_DESC));
		
		//All account owner this domain
		JSONArray accounts = WebCommonService.toArray(object, MailDomainUtil.DOMAIN_USER_LIST);
		List<String> managers = new ArrayList<String>();
		for (int index = 0; index < accounts.size(); index++) {
			JSONObject account = accounts.getJSONObject(index);
			managers.add(WebCommonService.toString(account, AccountUtil.ACCOUNT_USER_NAME));
		}
		
		// check the validation of domain name
		if(managers.size() == 0 || !MailUtil.isDomainName(domainName)) return FAILURE_JSON;
		
		// create new instance of domain
		LdapMailDomain domain = this.createNewDomain();
		domain.setDomainName(domainName);
		domain.setDescription(domainDesc);
		
		boolean editAccount = WebCommonService.toBool(object, MailDomainUtil.DOMAIN_EDIT_ACCOUNT);
		
		// initialize DomainAdministrator
		LdapDomainAdministrator admin = new LdapDomainAdministrator();
		admin.setEditAccount(editAccount);
		admin.setManagers(managers);
		
		try {
			// add new domain
			this.mailDomainManager.addDomain(domain, admin);
		} catch (LotusException ex) {
			logger.error("ERROR while adding domain", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * update mail domain
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON updateDomain(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailDomainUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		//domain name
		String domainName = WebCommonService.toString(object, MailDomainUtil.DOMAIN_NAME); 
		String domainDesc = WebCommonService.toString(object, MailDomainUtil.DOMAIN_DESC);
		
		//All account owner this domain
		JSONArray accounts = WebCommonService.toArray(object, MailDomainUtil.DOMAIN_USER_LIST);
		List<String> managers = new ArrayList<String>();
		for (int index = 0; index < accounts.size(); index++) {
			JSONObject account = accounts.getJSONObject(index);
			managers.add(WebCommonService.toString(account, AccountUtil.ACCOUNT_USER_NAME));
		}
		
		//check data
		if(managers.size() == 0 || !StringService.hasLength(domainName)) return FAILURE_JSON;
		
		try {
			// find the domain
			LdapMailDomain domain = this.mailDomainManager.findByName(domainName);
			domain.setDescription(domainDesc);
			
			// find the administrator of domain
			LdapDomainAdministrator admin = 
				this.mailDomainManager.getDomainAdminManager().getAdminOfDomain(domainName);
			if(admin == null) return FAILURE_JSON;
			admin.setManagers(managers);
			admin.setEditAccount(WebCommonService.toBool(object, MailDomainUtil.DOMAIN_EDIT_ACCOUNT));
			
			// update mail domain
			this.mailDomainManager.updateDomain(domain,  admin);
		} catch (LotusException ex) {
			logger.error("ERROR while updating domain", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * delete an mail domain
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON deleteDomain(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailDomainUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get domain name
		String domainName = WebCommonService.toString(object, MailDomainUtil.DOMAIN_NAME);
		if(!StringService.hasLength(domainName)) return FAILURE_JSON;
		try {
			// delete the given domain
			this.mailDomainManager.deleteDomain(domainName);
		} catch (LotusException ex) {
			logger.error("ERROR while updating domain", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive domain
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	public JSON activeDomain(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailDomainUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given domain
			this.mailDomainManager.activeAccount(
					// domain name
					WebCommonService.toString(object, MailDomainUtil.DOMAIN_NAME),
					// active value
					WebCommonService.toBool(object, MailDomainUtil.DOMAIN_ACTIVE));
		} catch (LotusException ex) {
			logger.error("ERROR while active domain", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * create new domain object
	 * 
	 * @return MailDomain - the mail domain object
	 */
	private LdapMailDomain createNewDomain() {
		// initialize mail domain
		LdapMailDomain domain = new LdapMailDomain();		
		// get the configuration
		Configuration config = this.getConfiguration();
		
		// fill configured information for domain
		domain.setPostfixTransport(config.getPostfixTransport());
		domain.setMaxAlias(config.getMaxAlias());
		domain.setMaxMail(config.getMaxMail());
		domain.setMaxQuota(config.getMaxQuota());
		// fill other info
		domain.setLastChange(new Date());
		domain.setAccountActive(true);
		
		return domain;
	}
	
	/**
	 * Generate report
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	private JSON report(HttpServletRequest request) {	
		try {
			// get data from request
			String domain = getData(request, MailDomainUtil.DOMAIN_NAMES);
			if(!StringService.hasLength(domain)) return FAILURE_JSON;
			
			// get list of domains
			String[] domainArray = domain.split(WebCommonService.DELIMIT);			
			List<String> domainNames = new ArrayList<String>();
			for(String name : domainArray) {
				// put domain to list
				domainNames.add(name);
			}
		
			// call business
			List<ReportResult> results = this.mailDomainManager.report(domainNames);
			if(results == null || results.isEmpty()) return FAILURE_JSON;
			
			// convert to JSON and response the request
			return MailDomainUtil.convertToJSON(results);
			
		} catch (LotusException ex) {
			logger.error("ERROR whle loading data", ex);
			return FAILURE_JSON;
		}
	}
	
}
