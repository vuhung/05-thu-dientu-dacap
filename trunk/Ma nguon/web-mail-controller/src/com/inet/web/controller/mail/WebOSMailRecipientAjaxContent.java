/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *****************************************************************/
package com.inet.web.controller.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.web.controller.AbstractWebOSAjaxContentController;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.MailRecipientService;

/**
 * WebOSMailRecipientAjaxContent.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: WebOSMailRecipientAjaxContent.java Jan 11, 2010 2:11:45 PM Tan Truong $
 *
 * @since 1.0
 */
public class WebOSMailRecipientAjaxContent extends AbstractWebOSAjaxContentController<MailRecipientService> {
  /**
   * WebOSMailRecipientAjaxContent constructor
   * @param service
   */
  public WebOSMailRecipientAjaxContent(MailRecipientService service) {
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
