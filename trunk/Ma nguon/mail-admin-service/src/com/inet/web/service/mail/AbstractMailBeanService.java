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
package com.inet.web.service.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.web.service.AbstractService;
import com.inet.web.service.exception.MailAdministratorException;

/**
 * AbstractWebOSBeanService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 18, 2008
 * <pre>
 *  Initialization AbstractWebOSBeanService class.
 * </pre>
 */
public abstract class AbstractMailBeanService<T> extends AbstractService{
	/**
	 * Execute the business logic and return the data to sub system.
	 * 
	 * @param request HttpServletRequest - the given <tt>HttpServletRequest</tt> instance.
	 * @param response HttpServletResponse - the given <tt>HttpServletResponse</tt> instance.
	 * @return the business data.
	 * @throws WebOSServiceException when error occurs during executing the business logic.
	 */
	public abstract T execute(HttpServletRequest request, HttpServletResponse response) 
		throws MailAdministratorException ;
}
