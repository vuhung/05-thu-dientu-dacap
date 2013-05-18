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
package com.inet.web.service.mail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.persistence.RecipientSender;
import com.inet.web.bo.mail.MailRecipientBO;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;

/**
 * MailRecipientService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailRecipientService.java Jan 11, 2010 12:04:23 PM Tan Truong $
 *
 * @since 1.0
 */
public class MailRecipientService extends AbstractWebOSService {
  private static final String ACTION_RECIPIENT = "loadRecipient";
  private MailRecipientBO recipientBo; 
  
  /**
   * @param accountManager
   */
  protected MailRecipientService(AccountManager<Account> accountManager, MailRecipientBO recipientBo) {
    super(accountManager);
    this.recipientBo = recipientBo;
  }

  /**
   * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public JSON execute(HttpServletRequest request, HttpServletResponse response)
      throws WebOSServiceException {
    String action = getData(request, MailConstants.ACTION);
    
    if(ACTION_RECIPIENT.equals(action)){
      return loadRecipient();
    }
    
    return FAILURE_JSON;
  }

  /**
   * @return the JSON contains mail recipient
   */
  private JSON loadRecipient(){
    List<RecipientSender> senders = this.recipientBo.findByOwner(getCode());
    List<String> jsender = new ArrayList<String>();
    
    for(RecipientSender sender : senders){
      jsender.add(StringService.hasLength(sender.getRecipient())?
          sender.getRecipient() + " &lt;" + sender.getEmail() + "&gt;" : sender.getEmail());
    }
    
    JSONObject result = new JSONObject();
    result.accumulate(MailConstants.RESULT_KEY, JSONService.toJSONArray(jsender));
    
    return result;
  }
}
