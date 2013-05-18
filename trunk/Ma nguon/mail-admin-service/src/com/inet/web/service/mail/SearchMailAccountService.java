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
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.data.search.ldap.LdapMailSearchBean;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.GroupUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * SearchMailAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchMailAccountService extends AbstractLotusService {
	private static INetLogger logger = INetLogger.getLogger(SearchMailAccountService.class);
	private ILdapMailAccountManager mailAccountManager;
	
	/**
	 * The cache key to cache the cookie of searching
	 */
	private final String SEARCH_CACHE_KEY = "lotus-mail-search";

	/**
	 * The constructor
	 * 
	 * @param mailAccountManager IMailAccountManager - the mail account manager
	 */
	public SearchMailAccountService(ILdapMailAccountManager mailAccountManager) {
		this.mailAccountManager = mailAccountManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(MailAccountUtil.ACTION_SEARCH.equals(action)) {
			// search mail account
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
	 * search mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */
	protected JSON searchAccounts(HttpServletRequest request) {
		try {
			if(logger.isDebugEnabled()) logger.debug("DOMAIN SEARCH:" + getData(request, MailAccountUtil.MAIL_SEARCH_DOMAIN));
			
			// begin search data 
			PageResult<LdapMailAccount> wrapper =  mailAccountManager.pagination(getSearchBean(request));
			
			// check data
			if(wrapper == null) return FAILURE_JSON;
						
			// put token to cache.
			setDataToCache(SEARCH_CACHE_KEY, wrapper.getToken()) ;
			
			// convert data and return to GUI
			return convertWrapperToJSON(wrapper);
		} catch (LotusException ex) {
			logger.warning("ERROR while search mail account, message: [" + ex.getMessage() + "]", ex);
			
			// return failure data.
			return FAILURE_JSON;
		}		
	}
	
	/**
	 * get search information from request
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */	
	private LdapMailSearchBean getSearchBean(HttpServletRequest request) {
		//boolean firstTime = Boolean.valueOf(this.getData(request, MailAccountUtil.MAIL_SEARCH_FIRST));
		int start = WebCommonService.toInt(
				this.getData(request, MailAccountUtil.MAIL_SEARCH_START),0);
		int limit = WebCommonService.toInt(
				this.getData(request, MailAccountUtil.MAIL_SEARCH_LIMIT), this.getDefaultLimit());
		String cookie = null;
		if(start == 0) { 
			// remove the previous token to cache.
			removeDataFromCache(SEARCH_CACHE_KEY) ;
		} else {
			// get token from cache.
			cookie = getDataFromCache(SEARCH_CACHE_KEY, String.class) ;
		}
		// get the key for search
		String key = getData(request, MailAccountUtil.MAIL_SEARCH_KEY);
		String domainName = getData(request, MailAccountUtil.MAIL_SEARCH_DOMAIN);
		String department = getData(request, MailAccountUtil.MAIL_GROUP);
		
		LdapMailSearchBean entryBean = new LdapMailSearchBean(start, limit, cookie);
		
		//put data to search bean
		entryBean.setKey(key);
		entryBean.setDomainName(domainName);
		if(! GroupUtil.NONE_GROUP.equals(department)) {
			entryBean.setDepartment(department);
		}
		// return search bean.
		return entryBean;
	}
	
	/**
	 * convert the search wrapper to JSON
	 * 
	 * @param wrapper SearchWrapper - the search wrapper
	 * @return JSON - the returned JSON
	 */
	protected JSON convertWrapperToJSON(PageResult<LdapMailAccount> wrapper) {
		// get the list of account in search result
		List<LdapMailAccount> accounts = wrapper.getData();
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json = null;
		if(accounts != null) {
			for(MailAccount account : accounts) {
				json = MailAccountUtil.convertToJSON(account);
				json.accumulate(MailAccountUtil.MAIL_CREATE_DATE, account.getCreateDate().getTime())
					.accumulate(MailAccountUtil.MAIL_IS_MAIL_ACCOUNT, true)
					.accumulate(MailAccountUtil.MAIL_DISPLAY_NAME, account.getLastName() + " " + account.getFullName());
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
