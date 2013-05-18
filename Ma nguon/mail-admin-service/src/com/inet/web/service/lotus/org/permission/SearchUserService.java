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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.ldap.PageResult;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.core.SearchBean;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.lotus.org.permission.manage.ldap.LdapUserSearchBean;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.GroupUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.UserUtil;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * SearchUserService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchUserService extends AbstractLotusService {
	private static final INetLogger logger = INetLogger.getLogger(SearchUserService.class);
	private ILdapUserManager userManager;
	
	/**
	 * The cache key to cache the cookie of searching
	 */
	private final String SEARCH_CACHE_KEY = "user-search";
	
	/**
	 * The constructor
	 * 
	 * @param userManager ILdapUserManager - the user manager
	 */
	public SearchUserService(ILdapUserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, UserUtil.ACTION_MODE_PARAMETER);
		if(UserUtil.ACTION_SEARCH.equals(action)) {
			 //search user
			return this.searchUsers(request);
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
	 * search user information
	 * 
	 * @param request HttpServletRequest - the given request
	 * @return JSON - the search result
	 */
	private JSON searchUsers(HttpServletRequest request) {
		try {
			// begin search data 
			PageResult<LdapUser> wrapper = 
				this.userManager.pagination(this.getSearchBean(request));
			
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
				this.getData(request, UserUtil.USER_SEARCH_LIMIT), this.getDefaultLimit());
		int start = WebCommonService.toInt(
				this.getData(request, UserUtil.USER_SEARCH_START), 0);
		
		String cookie = null;
		if(start == 0) { 
			// remove the old cache key.
			removeDataFromCache(SEARCH_CACHE_KEY);
		} else {
			// get cookie.
			cookie = getDataFromCache(SEARCH_CACHE_KEY, String.class) ;
		}
		
		// get the key for search
		String key = getData(request, UserUtil.USER_SEARCH_KEY);
		LdapUserSearchBean searchBean = new LdapUserSearchBean(start, limit, cookie);
		
		//put data to search bean
		searchBean.setKey(key);
		String group = this.getData(request, UserUtil.USER_GROUP);
		if(!GroupUtil.NONE_GROUP.equals(group)) {
			searchBean.setGroup(group);
		}
		searchBean.setOrganization(this.getData(request, UserUtil.USER_ORGANIZATION));
		
		// return data.
		return searchBean;
	}
	
	/**
	 * convert the search wrapper to JSON
	 * 
	 * @param wrapper SearchWrapper - the search wrapper
	 * @return JSON - the returned JSON
	 */
	private JSON convertWrapperToJSON(PageResult<LdapUser> wrapper) {
		// get the list of account in search result
		List<LdapUser> users = wrapper.getData();
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json = null;
		if(users != null) {			
			for(LdapUser user : users) {
				json = new JSONObject();
				json.accumulate(UserUtil.USER_NAME, user.getName())					
					.accumulate(UserUtil.USER_FULL_NAME, MailCommonUtils.getDisplayValue(user.getFullName()))					
					.accumulate(UserUtil.USER_EMAIL, MailCommonUtils.getDisplayValue(user.getEmail()))
					.accumulate(UserUtil.USER_TITLE,MailCommonUtils.getDisplayValue(user.getTitle()));
					
				// put to list 
				jsons.add(json);
			}
		}
		// create new JSON object
		JSONObject object = new JSONObject();
		object.accumulate(UserUtil.USER_SEARCH_RESULT_LIST, JSONService.toJSONArray(jsons))			  
			  .accumulate(UserUtil.USER_SEARCH_TOTAL_RESULT, wrapper.getSize());
		return object;
	}


}
