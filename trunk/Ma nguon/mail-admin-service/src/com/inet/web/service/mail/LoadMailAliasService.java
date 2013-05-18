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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

import com.inet.base.logging.INetLogger;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.MailAlias;
import com.inet.lotus.mail.manage.ldap.ILdapMailAliasManager;
import com.inet.lotus.mail.util.MailUtil;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.MailAliasUtil;

/**
 * LoadMailAliasService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class LoadMailAliasService extends AbstractLotusService {
	private static INetLogger logger = INetLogger.getLogger(LoadMailAliasService.class);
	private ILdapMailAliasManager mailAliasManager;
	
	/**
	 * The constructor
	 * 
	 * @param mailAliasManager IMailAliasManager - the mail alias manager
	 */
	public LoadMailAliasService(ILdapMailAliasManager mailAliasManager) {
		this.mailAliasManager = mailAliasManager;
	}

	/**
	 * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws MailAdministratorException {
		String action = this.getData(request, MailAliasUtil.ACTION_MODE_PARAMETER);
		if(MailAliasUtil.ACTION_LOAD.equals(action)) {
			// load alias
			return this.loadAlias(this.getData(request, MailAliasUtil.ALIAS_MAIL_ADDRESS));
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * load mail alias
	 * 
	 * @param email String - the email address
	 * @return JSON - the alias as JSON object
	 */
	private JSON loadAlias(String email) {
		// checking email address
		if(!MailUtil.isEmailAddress(email)) return FAILURE_JSON;
		try {
			// get alias from database
			MailAlias alias = this.mailAliasManager.findByName(email);
			// check alias
			if(alias == null) return FAILURE_JSON;
			
			return MailAliasUtil.convertToJSON(alias);
		} catch (LotusException ex) {
			logger.error("ERROR while load mail alias", ex);
		}
		return FAILURE_JSON;
	}

}
