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
package com.inet.web.controller.bean.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.logging.INetLogger;
import com.inet.web.core.struts.bean.AbstractBean;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.data.AccountInfo;
import com.inet.web.service.mail.LoadAccountInfoService;

/**
 * WebOSLoginInfoBean.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class WebOSLoginInfoBean extends AbstractBean{
	// the class logger.
	private static final INetLogger logger = INetLogger.getLogger(WebOSLoginInfoBean.class) ;
	
	// the login info bean name.
	private static final String LOGIN_INFO_BEAN_NAME = "account" ;

	// the <tt>LoadAccountInfoService</tt> instance.
	private LoadAccountInfoService service ;
	
	/**
	 * Create <tt>WebOSLoginInfoBean</tt> instance from the given 
	 * the <tt>LoadAccountInfoService</tt> instance.
	 * 
	 * @param service LoadAccountInfoService - the given <tt>LoadAccountInfoService</tt> instance.
	 */
	public WebOSLoginInfoBean(LoadAccountInfoService service) {
		this.service = service;
	}
	
	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Object execute(HttpServletRequest request, HttpServletResponse response) throws WebOSException {
		logger.debug("execute the login info service.") ;
		
		//load the info login 
		AccountInfo info = this.service.execute(request, response);
		
		return info;
	}
	
	/**
	 * @see com.inet.web.core.struts.bean.AbstractBean#getName()
	 */
	public String getName() {
		return LOGIN_INFO_BEAN_NAME;
	}

}
