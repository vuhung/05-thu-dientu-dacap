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

import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.lotus.mail.domain.MailDomain;
import com.inet.lotus.mail.report.ReportResult;
import com.inet.web.common.json.JSONService;

/**
 * MailDomainUtil.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailDomainUtil {
	///////////////////////////////////////////////////////////
	///////////// The key for action///////////////////////////
	/**
	 * The key for action mode
	 */
	public static final String ACTION_MODE_PARAMETER = "action";
	
	/**
	 * The key for save action
	 */
	public static final String ACTION_SAVE = "save";
	
	/**
	 * The key for update action
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * The key for delete action
	 */
	public static final String ACTION_DELETE = "delete";
	
	/**
	 * The key for active action
	 */
	public static final String ACTION_ACTIVE = "active";
	
	/**
	 * The key for load all domain  depend on permission action
	 */
	public static final String ACTION_LOAD_ALL_DOMAIN = "loadAll";
	
	/**
	 * The key for load all domain in system action
	 */
	public static final String ACTION_LOAD_ALL_DOMAIN_IN_SYSTEM = "loadallsys";
	
	/**
	 * The key for load all domains which user have domain role action
	 */
	public static final String ACTION_LOAD_DR_DOMAIN = "loaddrs";
	
	/**
	 * The key for load domain action
	 */
	public static final String ACTION_LOAD_DOMAIN = "load";
	
	/**
	 * The key for load domain action
	 */
	public static final String ACTION_REPORT = "report";
	
	///////////////////////////////////////////////////////////	
	public static final String NONE = "---";
	
	///////////////////////////////////////////////////////////
	////////////// The key for domain information//////////////
	/**
	 * The key for mail domain object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/**
	 * The JSON key for domain name
	 */
	public static final String DOMAIN_NAME = "text";
	
	/**
	 * The JSON key for domain description
	 */
	public static final String DOMAIN_DESC = "desc";
	
	/**
	 * The JSON key for display domain name
	 */
	public static final String DOMAIN_DISPLAY_NAME = "disname";
	
	/**
	 * The JSON key for domain ID
	 */
	public static final String DOMAIN_ID = "id";
	
	/**
	 * The JSON key for leaf of tree
	 */
	public static final String TREE_LEAFT = "leaf";
	
	/**
	 * The JSON key for leaf of tree
	 */
	public static final String TREE_ICON = "iconCls";
	
	/**
	 * The JSON key for leaf of tree
	 */
	public static final String TREE_CLS = "cls";
	
	/**
	 * The JSON key for list user owner on domain
	 */
	public static final String DOMAIN_USER_LIST = "list";
	
	/**
	 * The JSON key for edit accounts in domain
	 */
	public static final String DOMAIN_EDIT_ACCOUNT = "edit";
	
	/**
	 * The JSON key for active domain
	 */
	public static final String DOMAIN_ACTIVE = "active";
	
	///////////////////////////////////////////////////////////
	///////////// The key for department of domain/////////////
	/**
	 * The JSON key for the name of department
	 */
	public static final String DEPARTMENT_NAME = "depName";
	
	/**
	 * The JSON key for the number of department
	 */
	public static final String DEPARTMENT_NUMBER = "depNumber";
	
	/**
	 * The JSON key for the list of departments
	 */
	public static final String DEPARTMENT_LIST = "list";
	
	/**
	 * The JSON key for the role on department
	 */
	public static final String DEPARTMENT_USER_ROLE = "role";
	
	////////////////////////////////////////////////////////////
	/////////////////// The key for reporting //////////////////
	/**
	 * The JSON key for total email account
	 */
	public static final String TOTAL_MAIL_ACC = "tmail";
	
	/**
	 * The JSON key for total alias account
	 */
	public static final String TOTAL_ALIAS_ACC = "talias";
	
	/**
	 * The JSON key for domain names
	 */
	public static final String DOMAIN_NAMES = "domains";
	
	/**
	 * convert mail domain to JSON object
	 * 
	 * @param domain MailDomain - the given mail domain
	 * @return JSON - the returned JSON object
	 */
	public static JSONObject convertToJSON(MailDomain domain) {
		JSONObject json = new JSONObject();
		 // put domain information to JSON object		
		 json.accumulate(MailDomainUtil.DOMAIN_ID, domain.getDomainName())
		 	 .accumulate(MailDomainUtil.DOMAIN_NAME, domain.getDomainName())
		 	 .accumulate(MailDomainUtil.DOMAIN_DISPLAY_NAME, getDisplayName(domain))
		 	 .accumulate(MailDomainUtil.DOMAIN_DESC, domain.getDescription())
		 	 .accumulate(MailDomainUtil.DOMAIN_ACTIVE, domain.isAccountActive())
		 	 .accumulate(MailDomainUtil.DOMAIN_EDIT_ACCOUNT, domain.isAdminEdit());
		return json;
	}
	
	/**
	 * Convert list of report result to JSON object
	 * 
	 * @param results List<ReportResult> - the list of report result object
	 * @return JSONArray - the returned JSON array
	 */
	public static final JSON convertToJSON(List<ReportResult> results) {		
		JSONArray jsons = new JSONArray();
		JSONObject json = null;
		for(ReportResult result : results) {
			json = new JSONObject();
			// put data to JSON object
			json.accumulate(MailDomainUtil.DOMAIN_NAME, result.getDomain())
				.accumulate(MailDomainUtil.DOMAIN_DESC, MailCommonUtils.getDisplayValue(result.getDescription()))
				.accumulate(MailDomainUtil.TOTAL_MAIL_ACC, result.getMail())
				.accumulate(MailDomainUtil.TOTAL_ALIAS_ACC, result.getAlias());
			jsons.add(json);
		}
		
		return JSONService.toJSONArray(jsons);
	}
	
	/**
	 * Get the display name
	 * 
	 * @param domain MailDomain - the mail domain
	 * @return String - the display name
	 */
	public static String getDisplayName(MailDomain domain) {
		if(!StringService.hasLength(domain.getDescription()))
			return domain.getDomainName();
		return domain.getDomainName() + " [" + domain.getDescription() + "]"; 
	}
}
