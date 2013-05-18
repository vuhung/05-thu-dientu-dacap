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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.util.MailService;
import com.inet.web.bo.mail.MailConfigureBO;
import com.inet.web.bo.mail.MailHeaderBO;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;

/**
 * QuotaService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: QuotaService.java Jan 7, 2010 10:29:50 AM Tan Truong $
 *
 * @since 1.0
 */
public class QuotaService extends AbstractWebOSService{
  /**
   * logger.
   */
  private static final INetLogger logger = INetLogger.getLogger(QuotaService.class) ;
  
  private static final String ACTION_QUOTA = "loadQuota";
  private static final String FIELD_QUOTA = "quota";
  private static final String FIELD_USED = "used";
  
  /**
   * the {@link MailHeaderBO} instance.
   */
  private MailHeaderBO headerBo;
  
  /**
   * the {@link MailConfigureBO} instance.
   */
  private MailConfigureBO configBo;
  /**
   * the {@link LdapMailAccountManager} instance.
   */
  private final LdapMailAccountManager mailAccountManager;
  /**
   * @param accountManager
   */
  public QuotaService(AccountManager<Account> accountManager, MailHeaderBO headerBo,
      MailConfigureBO configBo, LdapMailAccountManager mailAccountManager) {
    super(accountManager);
    this.headerBo = headerBo;
    this.configBo = configBo;
    this.mailAccountManager = mailAccountManager;
  }

  /** 
   * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public JSON execute(HttpServletRequest request, HttpServletResponse response)
      throws WebOSServiceException {
    String action = getData(request, MailConstants.ACTION);
    
    if(ACTION_QUOTA.equals(action)){
      return loadQuota();
    }
    return FAILURE_JSON;
  }
  
  /**
   * load quota and current using space
   * @return JSON 
   */
  private JSON loadQuota(){
    MailAcctConfigInfo config = this.configBo.findByUser();
    if(config == null)
      return FAILURE_JSON;
    
    String smtpDef = config.getDefaultSMTP();
    if(!StringService.hasLength(smtpDef)){
      return FAILURE_JSON;
    }
    
    //get email account from ticket
    String email = MailService.getUsername(smtpDef);
    
    // find detail LDAP mail account
    LdapMailAccount ldapMailAccount = mailAccountManager.findByName(email);
    
    if(ldapMailAccount == null)
      return FAILURE_JSON;
    
    //get quota
    int quota = 0;
    try{
      quota = Integer.parseInt(ldapMailAccount.getQuota()); 
    }catch (NumberFormatException ex) {
      logger.warning("Error to parse quota to integer",ex);
      return FAILURE_JSON;
    }
    
    // check quota equal zero
    if(quota == 0)
      return FAILURE_JSON;
    
    long spaceUsed = this.headerBo.getCurrentUsing(getCode(), smtpDef);
    
    // create JSON to return result
    JSONObject object = new JSONObject();
    object.accumulate(FIELD_QUOTA, quota)
          .accumulate(FIELD_USED, spaceUsed);
    
    return object;
  }
}
