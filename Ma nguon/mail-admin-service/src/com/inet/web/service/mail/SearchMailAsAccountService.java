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

import com.inet.ldap.PageResult;
import com.inet.lotus.mail.MailAccount;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;

/**
 * SearchMailAsAccountService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchMailAsAccountService extends SearchMailAccountService {
	//private MailConfiguration mailConfiguration;

	/**
	 * Create <tt>SearchMailAsAccountService</tt> instance
	 * 
	 * @param mailAccountManager IMailAccountManager - the mail account manager
	 */
	public SearchMailAsAccountService(ILdapMailAccountManager mailAccountManager) {
		super(mailAccountManager);
	}
	
	/**
	 * @see com.inet.web.service.mail.SearchMailAccountService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAccountUtil.ACTION_MODE_PARAMETER);
		if(AccountUtil.ACTION_SEARCH.equals(action)) {
			// search mail account
			return this.searchAccounts(request);
		}
		return FAILURE_JSON;
	}
	
	/**
	 * @see com.inet.web.service.mail.SearchMailAccountService#convertWrapperToJSON(com.inet.ldap.PageResult)
	 */
	@Override
	protected JSON convertWrapperToJSON(PageResult<LdapMailAccount> wrapper) {
		// get the list of account in search result
		List<LdapMailAccount> accounts = wrapper.getData();
		List<JSON> jsons = new ArrayList<JSON>();
		JSONObject json = null;
		if(accounts != null) {
			for(MailAccount account : accounts) {
				json = new JSONObject();
				json.accumulate(MailAccountUtil.ACCOUNT_CODE, account.getUserCode())	
					.accumulate(MailAccountUtil.MAIL_ADDRESS, account.getEmail())
					.accumulate(MailAccountUtil.MAIL_DISPLAY_NAME, account.getLastName() + " " + account.getFullName());
//					.accumulate(AccountUtil.ACCOUNT_EDITATBLE,this.mailConfiguration!=null?
//						!account.getName().equals(this.mailConfiguration.getAdminID()):true);
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
