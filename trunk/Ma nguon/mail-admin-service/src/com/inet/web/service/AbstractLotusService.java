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
package com.inet.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.web.service.conf.Configuration;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.ConfigurationService;

/**
 * AbstractMailAdministratorService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public abstract class AbstractLotusService extends AbstractService {	
	/**
	 * Success data.
	 */
	protected static final JSON SUCCESS_JSON = new JSONObject().accumulate("success", true) ;
	
	/**
	 * Failure data.
	 */
	protected static final JSON FAILURE_JSON = new JSONObject().accumulate("success", false) ;
	
	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return ConfigurationService.getConfiguration();
	}
	
	/**
	 * @return the default limited data.
	 */
	protected int getDefaultLimit() {
		return ConfigurationService.getDefaultLimit();
	}

	/**
	 * Executes the business logic and return JSON data to user.
	 * 
	 * @param request HttpServletRequest - the given <tt>HttpServletRequest</tt> 
	 * to retrieve the information.
	 * @param response HttpServletResponse - the given <tt>HttpServletResponse</tt>
	 * to export the information.
	 * @return the JSON data.
	 * @throws MailAdministratorException when error occurs during executing business logic.
	 */
	public abstract JSON execute(HttpServletRequest request, 
			HttpServletResponse response) throws MailAdministratorException ;

}
