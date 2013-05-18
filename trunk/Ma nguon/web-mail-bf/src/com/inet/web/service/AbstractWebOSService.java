/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
package com.inet.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.web.service.exception.WebOSServiceException;

/**
 * AbstractWebOSService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 14, 2008
 * <pre>
 *  Initialization AbstractWebOSService class.
 * </pre>
 */
public abstract class AbstractWebOSService extends AbstractService{	
	//~ Static fields =========================================================
	/**
	 * Success data.
	 */
	protected static final JSON SUCCESS_JSON = new JSONObject().accumulate("success", true) ;
	
	/**
	 * Failure data.
	 */
	protected static final JSON FAILURE_JSON = new JSONObject().accumulate("success", false) ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AbstractWebOSService</tt> instance from the given
	 * {@link AccountManager} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 */
	protected AbstractWebOSService(AccountManager<Account> accountManager){
		super(accountManager) ;
	}	
	
	//~ Methods ===============================================================	
	/**
	 * Executes the business logic and return JSON data to user.
	 * 
	 * @param request the given {@link HttpServletRequest}  to retrieve the information.
	 * @param response the given {@link HttpServletResponse} to export the information.
	 * @return the JSON data.
	 * @throws WebOSServiceException when error occurs during executing business logic.
	 */
	public abstract JSON execute(HttpServletRequest request, 
			HttpServletResponse response) throws WebOSServiceException ;	
}
