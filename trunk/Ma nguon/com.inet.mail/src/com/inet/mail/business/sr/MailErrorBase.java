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
package com.inet.mail.business.sr;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.persistence.MailError;

/**
 * MailErrorBase.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailErrorBase.java Feb 19, 2011 10:56:51 AM Tan Truong $
 *
 * @since 1.0
 */
public interface MailErrorBase extends BaseMailBusiness<MailError> {
  
  /**
   * Find UID by account.
   * 
   * @param usercode the given login user code.
   * @param account the given account information.
   * 
   * @return the list of unique identifiers.
   * @throws EJBException when error occur during finding the UIDs.
   */
  public List<String> findUidByAccount(String usercode, String account) throws EJBException;
}
