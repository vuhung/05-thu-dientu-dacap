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
package com.inet.web.bf.mail;

import com.inet.mail.business.sr.MailRecipientRemoteSL;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.facade.AbstractEJBFacade;

/**
 * MailRecipientBF.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailRecipientBF.java Jan 11, 2010 11:10:32 AM Tan Truong $
 *
 * @since 1.0
 */
public class MailRecipientBF extends AbstractEJBFacade<MailRecipientRemoteSL>{
  /**
   *  mail recipient bean name
   */
  private static final String MAIL_RECIPIENT_BEAN_NAME = "MailRecipientSLBean";
  
  /**
   * MailRecipientBF constructor
   * @param provider AbstractApplicationServerProvider
   */
  public MailRecipientBF(AbstractApplicationServerProvider provider) {
          super(provider, MAIL_RECIPIENT_BEAN_NAME, BEAN_SCOPE_REMOTE);
  }
}
