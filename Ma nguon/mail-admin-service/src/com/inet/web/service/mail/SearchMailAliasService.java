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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.ldap.PageResult;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.MailAlias;
import com.inet.lotus.mail.data.search.ldap.LdapAliasSearchBean;
import com.inet.lotus.mail.ldap.LdapMailAlias;
import com.inet.lotus.mail.manage.ldap.ILdapMailAliasManager;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailAliasUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * SearchMailAliasService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchMailAliasService extends AbstractLotusService {
	private static INetLogger logger = INetLogger.getLogger(SearchMailAliasService.class);
	private ILdapMailAliasManager mailAliasManager;	
	
	/**
	 * The cache key to cache the cookie of searching
	 */
	private final String SEARCH_CACHE_KEY = "alias-search";
	
	/**
	 * The constructor
	 * 
	 * @param mailAliasManager IMailAliasManager - the mail alias manager
	 */
	public SearchMailAliasService(ILdapMailAliasManager mailAliasManager) {
		this.mailAliasManager = mailAliasManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAliasUtil.ACTION_MODE_PARAMETER);
		if(MailAliasUtil.ACTION_SEARCH.equals(action)) {
			// search alias
			return this.searchAccounts(request);
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * @see com.inet.web.service.AbstractService#getCacheSuffix()
	 */
	@Override
	protected String getCacheSuffix() {
		return this.getCode();
	}
	
	/**
	 * search mail alias
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */
	private JSON searchAccounts(HttpServletRequest request) {
		try {
			// begin search data 
			PageResult<LdapMailAlias> wrapper = mailAliasManager.pagination(getSearchBean(request));
		
			// invalidate data.
			if(wrapper == null) return null;
			
			// put to cache.
			setDataToCache(SEARCH_CACHE_KEY, wrapper.getToken());
			
			// convert data and return to GUI
			return convertWrapperToJSON(wrapper);
		} catch (LotusException ex) {
			logger.warning("ERROR while search mail alias, message: [" + ex.getMessage() + "]", ex);
			
			return FAILURE_JSON;
		}		
	}
	
	/**
	 * get search information from request
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */	
	private LdapAliasSearchBean getSearchBean(HttpServletRequest request) {
		int start = WebCommonService.toInt(getData(request, MailAliasUtil.MAIL_SEARCH_START),0);
		int limit = WebCommonService.toInt(getData(request, MailAliasUtil.MAIL_SEARCH_LIMIT), getDefaultLimit());
		
		String cookie = null;
		if(start == 0) { 
			// remove data from cache.
			removeDataFromCache(SEARCH_CACHE_KEY);
		} else {
			// get the page to search
			cookie = getDataFromCache(SEARCH_CACHE_KEY, String.class) ;
		}
		
		// get the key for search
		String key = getData(request, MailAliasUtil.MAIL_SEARCH_KEY);
		String domainName = getData(request, MailAliasUtil.MAIL_SEARCH_DOMAIN);
		String department = getData(request, MailAliasUtil.ALIAS_DEPARTMENT_NUMBER);
		
		LdapAliasSearchBean entryBean = new LdapAliasSearchBean(start, limit, cookie);
		
		//put data to search bean
		entryBean.setKey(key);
		entryBean.setDomainName(domainName);		
		entryBean.setDepartment(department);
		
		// return search bean data.
		return entryBean;
	}
	
	/**
	 * convert the search wrapper to JSON
	 * 
	 * @param wrapper SearchWrapper - the search wrapper
	 * @return JSON - the returned JSON
	 */
	protected JSON convertWrapperToJSON(PageResult<LdapMailAlias> wrapper) {
		// get the list of alias in search result
		List<LdapMailAlias> aliases = wrapper.getData();
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json = null;
		if(aliases != null) {
			for(MailAlias alias : aliases) {
				json = new JSONObject();
				// fill information
				json.accumulate(MailAliasUtil.ALIAS_MAIL_ADDRESS, alias.getEmail())
				    .accumulate(MailAliasUtil.ALIAS_NAME, alias.getFullName())
				    .accumulate(MailAliasUtil.ALIAS_ACCOUNT_ACTIVE, alias.isAccountActive())
				    .accumulate(MailAliasUtil.ALIAS_SMTP_AUTH, alias.isSmtpAuth())
				    .accumulate(MailAliasUtil.ALIAS_VIRUS_CHECKING, alias.isAmavisBypassVirusChecks())
				    .accumulate(MailAliasUtil.ALIAS_SPAM_CHECKING, alias.isAmavisBypassSpamChecks())
				    .accumulate(MailAliasUtil.ALIAS_CREATE_DATE, alias.getLastChange().getTime())
				    .accumulate(MailAccountUtil.MAIL_DISPLAY_NAME, alias.getLastName() + " " + alias.getFullName())
				    .accumulate(MailAccountUtil.MAIL_IS_MAIL_ACCOUNT, false);				
				// put to list
				jsons.add(json);
			}
		}
		// create new JSON object
		JSONObject object = new JSONObject();
		object.accumulate(MailAccountUtil.MAIL_SEARCH_RESULT_LIST, JSONService.toJSONArray(jsons))
			  .accumulate(MailAccountUtil.MAIL_SEARCH_TOTAL_RESULT, wrapper.getSize());
		return object;
	}

}
