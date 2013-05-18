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
package com.inet.web.service.lotus.account;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.ldap.PageResult;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.BasicLdapAccountSearchBean;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.core.SearchBean;
import com.inet.lotus.mail.conf.MailConfiguration;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * SearchAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchAccountService extends AbstractLotusService {
	private static INetLogger logger = INetLogger.getLogger(SearchAccountService.class);
	private LdapAccountManager accountManager;
	private MailConfiguration mailConfiguration;

	/**
	 * The cache key to cache the cookie of searching
	 */
	private final String SEARCH_CACHE_KEY = "account-search";
	
	/**
	 * @param accountManager
	 */
	public SearchAccountService(LdapAccountManager accountManager, MailConfiguration mailConfiguration) {
		this.accountManager = accountManager;
		this.mailConfiguration = mailConfiguration;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, AccountUtil.ACTION_MODE_PARAMETER);
		if(AccountUtil.ACTION_SEARCH.equals(action)) {
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
		return getCode();
	}

	//~ Helper functions ======================================================
	/**
	 * search mail account
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */
	private JSON searchAccounts(HttpServletRequest request) {
		try {
			// begin search data 
			PageResult<LdapAccount> wrapper = 
				this.accountManager.pagination(this.getSearchBean(request));
			
			//check data
			if(wrapper == null) return FAILURE_JSON;
					
			// go to next page.
			setDataToCache(SEARCH_CACHE_KEY, wrapper.getToken());
				
			// convert data and return to GUI
			return convertWrapperToJSON(wrapper);
		} catch (LotusException ex) {
			logger.warning("ERROR while search mail account, message: [" + ex.getMessage() + "]", ex);
			
			// return failure.
			return FAILURE_JSON;
		}		
	}
	
	/**
	 * get search information from request
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */	
	private SearchBean getSearchBean(HttpServletRequest request) {
		int limit = WebCommonService.toInt(
				this.getData(request, AccountUtil.ACCOUNT_SEARCH_LIMIT), this.getDefaultLimit());
		int start = WebCommonService.toInt(
				this.getData(request, AccountUtil.ACCOUNT_SEARCH_START), 0);
		
		String cookie = null;
		if(start == 0) { 
			// remove the old cache key.
			removeDataFromCache(SEARCH_CACHE_KEY);
		} else {
			// get cookie.
			cookie = getDataFromCache(SEARCH_CACHE_KEY, String.class) ;
		}
		
		// get the key for search
		String key = getData(request, AccountUtil.ACCOUNT_SEARCH_KEY);
		Boolean deleted = Boolean.valueOf(getData(request, AccountUtil.ACCOUNT_DELETED_KEY));
		BasicLdapAccountSearchBean searchBean = new BasicLdapAccountSearchBean(start, limit, cookie);
		
		//put data to search bean
		searchBean.setValue(key);
		searchBean.setDeleted(deleted);
		
		// return data.
		return searchBean;
	}
	
	/**
	 * convert the search wrapper to JSON
	 * 
	 * @param wrapper SearchWrapper - the search wrapper
	 * @return JSON - the returned JSON
	 */
	private JSON convertWrapperToJSON(PageResult<LdapAccount> wrapper) {
		// get the list of account in search result
		List<LdapAccount> accounts = wrapper.getData();
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json = null;
		if(accounts != null) {			
			for(Account account : accounts) {
				json = new JSONObject();
				json.accumulate(AccountUtil.ACCOUNT_USER_NAME, account.getName())
					.accumulate(AccountUtil.ACCOUNT_USER_CODE, account.getCode())
					.accumulate(AccountUtil.ACCOUNT_PASSWORD, account.getPassword())
					.accumulate(AccountUtil.ACCOUNT_FIRST_NAME,MailCommonUtils.getDisplayValue(account.getFirstName()))
					.accumulate(AccountUtil.ACCOUNT_MIDDLE_NAME, MailCommonUtils.getDisplayValue(account.getMiddleName()))
					.accumulate(AccountUtil.ACCOUNT_LAST_NAME, MailCommonUtils.getDisplayValue(account.getLastName()))
					.accumulate(AccountUtil.ACCOUNT_EMAIL, MailCommonUtils.getDisplayValue(account.getEmail()))
					.accumulate(AccountUtil.ACCOUNT_EDITATBLE,this.mailConfiguration!=null?
							!account.getName().equals(this.mailConfiguration.getAdminID()):true);
				
				// put to list 
				jsons.add(json);
			}
		}
		// create new JSON object
		JSONObject object = new JSONObject();
		object.accumulate(AccountUtil.ACCOUNT_SEARCH_RESULT_LIST, JSONService.toJSONArray(jsons))			  
			  .accumulate(AccountUtil.ACCOUNT_SEARCH_TOTAL_RESULT, wrapper.getSize());
		return object;
	}

}
