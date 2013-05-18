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
package com.inet.web.controller.bean;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.service.StringService;
import com.inet.base.service.WebService;
import com.inet.web.core.struts.bean.AbstractBean;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.BindContextService;

/**
 * WebOSWelcomeBean.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date Jul 28, 2008
 * <pre>
 *  Initialization WebOSWelcomeBean class.
 * </pre>
 */
public class WebOSBindContextBean extends AbstractBean {
	/**
	 * the action param.
	 */
	private static final String ACTION_PARAM = "iwebos-action" ;
	
	/**
	 * Bind context service instance.
	 */
	private BindContextService contextService ;
	
	/**
	 * Create WebOSWelcomeBean instance from the given <tt>BindContextService</tt> instance.
	 * 
	 * @param contextService BindContextService - the given <tt>BindContextService</tt> instance.
	 */
	public WebOSBindContextBean(BindContextService contextService) {
		this.contextService = contextService ;
	}
	
	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Object execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSException {
		// do the first time, when user login successful.
		Object subject = (Subject)WebService.getSessionAttribute(request, "subject") ;

		// get action parameter.
		String action = (String)request.getAttribute(ACTION_PARAM) ;
		
		if((subject == null) || !(subject instanceof Subject) || "logout".equals(action)) {
			if(!StringService.hasLength(action)) request.setAttribute(ACTION_PARAM, "bind") ;
			
			// execute business logic.
			return contextService.execute(request, response) ;
		}
		
		// does not execute business logic.
		return null ;
	}

	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#getName()
	 */
	public String getName() {
		return null;
	}
}
