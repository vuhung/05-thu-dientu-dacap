/*****************************************************************
   Copyright 2008 by Tung Luong (lqtung@truthinet.com.vn)

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

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.base.service.WebService;
import com.inet.lotus.account.Account;
import com.inet.web.service.data.AccountInfo;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.ConfigurationService;
import com.inet.web.service.mail.utils.UserCacheService;

/**
 * LoadAccountInfoService.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class LoadAccountInfoService extends AbstractMailBeanService<AccountInfo>{
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(LoadAccountInfoService.class) ;

	/**
	 * The full name format : Last name + middle name + First name
	 */
	private String FULL_NAME_FORMAT = "%s %s %s";
	
	/**
	 * The page limit parameter.
	 */
	private static final String PAGE_LIMIT = "pageLimit" ;
	
	
	/**
	 * @see com.inet.web.service.mail.AbstractMailBeanService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public AccountInfo execute(HttpServletRequest request,
			HttpServletResponse response) throws MailAdministratorException {
		//get the user name 
		String userName = getUserName() ;
		
		// debugger.
		if(logger.isDebugEnabled()) logger.debug("user name = [" + userName + "]") ;
		WebService.setSessionAttribute(request, PAGE_LIMIT, ConfigurationService.getDefaultLimit()) ;
		
		//create account info
		AccountInfo info = new AccountInfo();
		
		//user is not login?
		if (userName != null){
			//get Account info from cache service
			Account account = UserCacheService.get(userName);
			
			//account is not null
			if (account != null ){
				info.setUserName(userName);
				info.setCode(account.getCode());
				info.setEmail(account.getEmail());
				
				//append full name
				String fullName = String.format(FULL_NAME_FORMAT,
						StringService.getValue(account.getLastName()),
						StringService.getValue(account.getMiddleName()),
						StringService.getValue(account.getFirstName()));
				
				//trim all separator space
				fullName = fullName.trim();
				
				//empty full name so that return the user name
				if (! StringService.hasLength(fullName)){
					fullName = userName;
				} 
				
				//set the display full name
				info.setFullName(fullName);
			}
		}
		
		//account info
		return info;
	}
}
