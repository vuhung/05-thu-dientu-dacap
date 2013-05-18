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

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.ldap.PageResult;
import com.inet.lotus.mail.MailAlias;
import com.inet.lotus.mail.ldap.LdapMailAlias;
import com.inet.lotus.mail.manage.ldap.ILdapMailAliasManager;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailAliasUtil;

/**
 * ReportMailAliasService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class ReportMailAliasService extends SearchMailAliasService {

	/**
	 * Create <tt>ReportMailAliasService</tt> instance
	 * 
	 * @param mailAliasManager ILdapMailAliasManager - the mail alias manager
	 */
	public ReportMailAliasService(ILdapMailAliasManager mailAliasManager) {
		super(mailAliasManager);
	}
	
	/**
	 * @see com.inet.web.service.AbstractLotusService#getDefaultLimit()
	 */
	protected int getDefaultLimit() {
		return 200;
	}
	
	/**
	 * @see com.inet.web.service.mail.SearchMailAliasService#convertWrapperToJSON(com.inet.ldap.PageResult)
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
				    .accumulate(MailAccountUtil.MAIL_DISPLAY_NAME, alias.getLastName() + " " + alias.getFullName());
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
