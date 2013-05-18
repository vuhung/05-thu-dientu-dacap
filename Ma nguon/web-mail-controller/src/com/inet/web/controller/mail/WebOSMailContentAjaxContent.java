/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.web.controller.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.web.controller.AbstractWebOSTxAjaxContentController;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.MailContentService;

/**
 * WebOSMailContentAjaxContent.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class WebOSMailContentAjaxContent extends AbstractWebOSTxAjaxContentController<MailContentService> {
	/**
	 * WebOsMailContentAjaxContent constructor
	 * @param service
	 */
	public WebOSMailContentAjaxContent(MailContentService service) {
		super(service);
	}

	/**
	 * @see com.inet.web.core.ajax.AbstractAjaxContent#executeBusiness(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected String executeBusiness(HttpServletRequest request,
			HttpServletResponse response) throws WebOSException {
		return this.getBusinessBean().execute(request, response).toString();
	}
}
