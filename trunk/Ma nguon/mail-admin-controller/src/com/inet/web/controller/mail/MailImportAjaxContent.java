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

import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.MailImportService;

/**
 * MailImportAjaxContent.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailImportAjaxContent.java Apr 7, 2011 11:41:34 PM Tan Truong $
 *
 * @since 1.0
 */
public class MailImportAjaxContent extends AbstractWebOSAjaxContentController<MailImportService> {

  /**
   * MailImportAjaxContent constructor
   * @param service MailImportService
   */
  public MailImportAjaxContent(MailImportService service) {
    super(service);
  }

  /**
   * @see com.inet.web.core.ajax.AbstractAjaxContent#executeBusiness(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected String executeBusiness(HttpServletRequest request, HttpServletResponse response)
      throws WebOSException {
    return getBusinessBean().execute(request, response).toString();
  }
}
