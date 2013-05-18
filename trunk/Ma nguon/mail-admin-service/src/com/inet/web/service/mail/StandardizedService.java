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
import com.inet.ldap.PageResult;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.data.search.ldap.LdapMailSearchBean;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.LdapUserManager;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.ImportUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * StandardizedService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: StandardizedService.java Aug 10, 2011 6:22:45 PM Tan Truong $
 *
 * @since 1.0
 */
public class StandardizedService extends AbstractLotusService {
  private static final INetLogger logger = INetLogger.getLogger(StandardizedService.class);
  
  private static final String DOMAIN    = "domain";
  private static final String TOTAL     = "total";
  /**
   * The cache key to cache the cookie of searching
   */
  private final String SEARCH_CACHE_KEY = "Standardized-mail-search";
  
  private LdapAccountManager accountManager;
  private LdapMailAccountManager mailAccountManager;
  private LdapUserManager userManager;
  
  /**
   * StandardizedService constructor
   * @param accountManager
   * @param mailAccountManager
   * @param userManager
   */
  public StandardizedService(LdapAccountManager accountManager,
      LdapUserManager userManager,
      LdapMailAccountManager mailAccountManager) {
    this.accountManager = accountManager;
    this.mailAccountManager = mailAccountManager;
    this.userManager = userManager;
  }
  
  /**
   * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  public JSON execute(HttpServletRequest request, HttpServletResponse response) throws MailAdministratorException {
    String domain = this.getData(request, DOMAIN);
    
    if(StringService.hasLength(domain)){
      return standardized(domain, request);
    }
    
    return FAILURE_JSON;
  }
  
  /**
   * standardized
   * @param domain the given domain name
   * @return JSON
   */
  private JSON standardized(String domain, HttpServletRequest request){
    // begin search data 
    PageResult<LdapMailAccount> wrapper =  mailAccountManager.pagination(getSearchBean(request));
    
    // check data
    if(wrapper == null) return FAILURE_JSON;
                            
    for(LdapMailAccount account: wrapper.getData()){
      try{
        
          // standardized LDAP account
          String pwd = standardizedAccount(account.getUserName());
          // standardized LDAP User
          standardizedUser(domain, account.getUserName(), pwd);
          // standardized Mail account
          standardizedMailAccount(account);
       
      }catch (LotusException ex) {
        logger.error("Error while stadarding name", ex);
      }
    }
    return new JSONObject().accumulate("success", true)
                          .accumulate(TOTAL, wrapper.getSize());
  }
  
  /**
   * get search information from request
   * 
   * @param request HttpServletRequest - the given request
   * @return JSON - the search result
   */     
  private LdapMailSearchBean getSearchBean(HttpServletRequest request) {
          //boolean firstTime = Boolean.valueOf(this.getData(request, MailAccountUtil.MAIL_SEARCH_FIRST));
          int start = WebCommonService.toInt(
                          this.getData(request, MailAccountUtil.MAIL_SEARCH_START),0);
          int limit = WebCommonService.toInt(
                          this.getData(request, MailAccountUtil.MAIL_SEARCH_LIMIT), this.getDefaultLimit());
          String cookie = null;
          if(start == 0) { 
                  // remove the previous token to cache.
                  removeDataFromCache(SEARCH_CACHE_KEY) ;
          } else {
                  // get token from cache.
                  cookie = getDataFromCache(SEARCH_CACHE_KEY, String.class) ;
          }
          // get the key for search
          String domainName = getData(request, MailAccountUtil.MAIL_SEARCH_DOMAIN);
          
          LdapMailSearchBean entryBean = new LdapMailSearchBean(start, limit, cookie);
          
          //put data to search bean
          entryBean.setDomainName(domainName);

          // return search bean.
          return entryBean;
  }
  
  /**
   * standardized LDAP Account
   * @param name the given LDAP account name
   * @throws LotusException
   */
  private String standardizedAccount(String name) throws LotusException{
    LdapAccount account = this.accountManager.findByName(name, true);
    
    if(account == null){
      throw new LotusException("Count not find account name:" + name); 
    }
    
    account.setFirstName(MailCommonUtils.getLdapValue(ImportUtil.standardized(account.getFirstName())));
    account.setLastName(MailCommonUtils.getLdapValue(ImportUtil.standardized(account.getLastName())));
    account.setMiddleName(MailCommonUtils.getLdapValue(ImportUtil.standardized(account.getMiddleName())));
    
    this.accountManager.update(account);
    
    return account.getPassword();
  }
  
  /**
   * standardized LDAP User
   * @param name the given LDAP user name
   * @throws LotusException
   */
  private void standardizedUser(String domain, String name, String pwd) throws LotusException{
    LdapUser user = this.userManager.loadUser(domain, name);
    
    if(user == null){
      return;
    }
    
    user.setFullName(MailCommonUtils.getLdapValue(ImportUtil.standardized(user.getFullName())));
    user.setFirstName(MailCommonUtils.getLdapValue(ImportUtil.standardized(user.getFirstName())));
    user.setMiddleName(MailCommonUtils.getLdapValue(ImportUtil.standardized(user.getMiddleName())));
    user.setLastName(MailCommonUtils.getLdapValue(ImportUtil.standardized(user.getLastName())));
    user.setPassword(pwd);
    this.userManager.updateUser(domain, user);
  }
  
  /**
   * standardized Mail account
   * @param account the given LDAP account
   */
  private void standardizedMailAccount(LdapMailAccount account) throws LotusException{
    account.setFullName(MailCommonUtils.getLdapValue(ImportUtil.standardized(account.getFullName())));
    account.setLastName(MailCommonUtils.getLdapValue(ImportUtil.standardized(account.getLastName())));
    this.mailAccountManager.update(account);
  }
}
