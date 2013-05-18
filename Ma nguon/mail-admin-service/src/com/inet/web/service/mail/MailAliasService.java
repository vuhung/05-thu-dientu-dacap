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
package com.inet.web.service.mail;

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
import com.inet.lotus.mail.MailAlias;
import com.inet.lotus.mail.ldap.LdapMailAlias;
import com.inet.lotus.mail.manage.ldap.ILdapMailAliasManager;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.MailAliasUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * MailAliasService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAliasService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(MailAliasService.class);
	private ILdapMailAliasManager mailAliasManager;
	/* hash method. */
	private String hashMethod ;
	
	/**
	 * The constructor
	 * 
	 * @param mailAliasManager IMailAliasManager - the mail alias manager
	 */
	public MailAliasService(ILdapMailAliasManager mailAliasManager, String hashMethod) {
		this.mailAliasManager = mailAliasManager;
		this.hashMethod = hashMethod ;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAliasUtil.ACTION_MODE_PARAMETER);
		if(MailAliasUtil.ACTION_SAVE.equals(action)) {
			// add alias
			return this.addAlias(request);
		} else if(MailAliasUtil.ACTION_UPDATE.equals(action)) {
			// update alias
			return this.updateAlias(request);
		} else if(MailAliasUtil.ACTION_ACTIVE.equals(action)) {
			// active/inactive alias
			return this.activeAlias(request);
		} else if(MailAliasUtil.ACTION_DELETE.equals(action)) {
			// delete alias
			return this.deleteAlias(request);
		} else if(MailAliasUtil.ACTION_SMTP_AUTH.equals(action)) {
			// SMTP authenticate
			return this.smtpAuthentication(request);
		} else if(MailAliasUtil.ACTION_VIRUS_CHECK.equals(action)) {
			// checking virus
			return this.virusCheck(request);
		} else if(MailAliasUtil.ACTION_SPAM_CHECK.equals(action)) {
			// checking SPAM
			return this.spamCheck(request);
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * add new alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON addAlias(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the email address
		String email = WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS);
		if(!MailUtil.isEmailAddress(email)) return FAILURE_JSON;
		
		// create new alias instance
		LdapMailAlias alias = this.createNewAlias();
		// fill information inputed from GUI
		alias.setEmail(email);		
		try {
			// refresh information from GUI
			this.refreshAlias(alias, object);
			// save alias
			this.mailAliasManager.add(alias);
		} catch (MailAdministratorException ex) {
			logger.error("ERROR: mail drop invalid.");
			return FAILURE_JSON;
		} catch (LotusException ex) {
			logger.error("ERROR while adding mail alias.", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * update mail alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON updateAlias(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// load mail alias from server
			LdapMailAlias alias = this.mailAliasManager.findByName(
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS));
			if(alias == null) return FAILURE_JSON;
			
			// refresh alias information
			this.refreshAlias(alias, object);
			alias.setLastChange(new Date());
			
			// update alias
			this.mailAliasManager.update(alias);
		} catch (LotusException ex) {
			logger.error("ERROR while update alias", ex);
			return FAILURE_JSON;
		} catch (MailAdministratorException ex) {
			logger.error("ERROR: mail drop invalid.");
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * delete the given alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON deleteAlias(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// delete given alias
			this.mailAliasManager.deleteAlias(
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS));
		} catch (LotusException ex) {
			logger.error("ERROR while delete alias", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON activeAlias(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given alias
			this.mailAliasManager.activeAccount(
					// alias  address
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAliasUtil.ALIAS_ACCOUNT_ACTIVE));
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive alias", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * SMTP authenticate or not
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON smtpAuthentication(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given alias
			this.mailAliasManager.smtpAuthentication(
					// alias  address
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAliasUtil.ALIAS_SMTP_AUTH));
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive alias", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON spamCheck(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given alias
			this.mailAliasManager.spamCheck(
					// alias  address
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAliasUtil.ALIAS_SPAM_CHECKING));
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive alias", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * active/inactive alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the returned JSON object
	 */
	protected JSON virusCheck(HttpServletRequest request) {
		JSONObject object = WebCommonService.toJSONObject(request, MailAliasUtil.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		try {
			// active the given alias
			this.mailAliasManager.virusCheck(
					// alias  address
					WebCommonService.toString(object, MailAliasUtil.ALIAS_MAIL_ADDRESS),
					// the active value
					WebCommonService.toBool(object, MailAliasUtil.ALIAS_VIRUS_CHECKING));
		} catch (LotusException ex) {
			logger.error("ERROR while active/inactive alias", ex);
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * update information from JSON object to mail alias
	 * 
	 * @param alias MailAlias - the given mail alias
	 * @param object JSONObject - the given JSON object
	 */
	private void refreshAlias(MailAlias alias, JSONObject object) 
			throws MailAdministratorException {
		// fill information on GUI to alias object		
		alias.setPassword(MailCommonUtils.getLdapPassword(
				WebCommonService.toString(object, MailAliasUtil.ALIAS_PASSWORD), 
				hashMethod)
			);
		alias.setFullName(WebCommonService.toString(object, MailAliasUtil.ALIAS_NAME));
		alias.setLastName(WebCommonService.toString(object, MailAliasUtil.ALIAS_LAST_NAME));
		alias.setAccountActive(WebCommonService.toBool(object, MailAliasUtil.ALIAS_ACCOUNT_ACTIVE));
		alias.setSmtpAuth(WebCommonService.toBool(object, MailAliasUtil.ALIAS_SMTP_AUTH));
		alias.setAmavisBypassVirusChecks(WebCommonService.toBool(object, MailAliasUtil.ALIAS_VIRUS_CHECKING));
		alias.setAmavisBypassSpamChecks(WebCommonService.toBool(object, MailAliasUtil.ALIAS_SPAM_CHECKING));
		
		// get the department
		String department = MailCommonUtils.getLdapValue(WebCommonService.toString(object, MailAliasUtil.ALIAS_DEPARTMENT_NUMBER));
		if(StringService.hasLength(department)) {
			List<String> departments = alias.getDepartments();
			if(departments == null) {
				departments = new ArrayList<String>();
				departments.add(department);				
			} else {
				boolean exist = false;
				for(String dep : departments) {
					if(department.equals(dep)) {
						exist = true;
						break;
					}
				}
				if(!exist) departments.add(department);
			}
			alias.setDepartments(departments);
		}
		
		// get list of email depend on alias 
		JSONArray emailJSONs = WebCommonService.toArray(object, MailAliasUtil.ALIAS_ACCOUNTS_LIST);
		if(emailJSONs == null || emailJSONs.size() == 0) 
			throw new MailAdministratorException("Mail drop must be not empty.");
		
		// initialize list to contain all email addressed
		List<String> emails = new ArrayList<String>(emailJSONs.size());		
		for (int index = 0; index < emailJSONs.size(); index++) {
			// get the JSON object
			JSONObject email = emailJSONs.getJSONObject(index);
			emails.add(WebCommonService.toString(email, MailAliasUtil.ALIAS_MAIL_ADDRESS));
		}
		// put list of mail-drops to alias object
		alias.setMailDrop(emails);
	}
	
	/**
	 * create new mail alias object
	 * 
	 * @return MailAlias - the new mail alias
	 */
	private LdapMailAlias createNewAlias() {
		LdapMailAlias alias = new LdapMailAlias();
		alias.setLastChange(new Date());
		
		return alias;
	}

}
