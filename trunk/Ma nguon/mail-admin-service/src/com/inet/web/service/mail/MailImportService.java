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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.LdapUserManager;
import com.inet.web.common.json.JSONService;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.conf.ClientConfigurationData;
import com.inet.web.service.conf.HandleSavingMailService;
import com.inet.web.service.data.AccountImport;
import com.inet.web.service.data.AccountImportInfo;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountImportCacheService;
import com.inet.web.service.mail.utils.DocumentHelper;
import com.inet.web.service.mail.utils.ImportUtil;
import com.inet.web.service.mail.utils.MailAccountUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;
import com.inet.web.service.mail.utils.WebCommonService;

/**
 * MailImportService.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailImportService.java Apr 7, 2011 11:00:46 AM Tan Truong $
 * 
 * @since 1.0
 */
public class MailImportService extends AbstractLotusService {
  private static final INetLogger logger = INetLogger.getLogger(MailImportService.class);
  
  private static final String KEY       = "key";
  private static final String ACTION    = "action";
  private static final String ACTION_IMPORT = "import";
  private static final String ACTION_READ_ACCOUNT = "read";
  private static final String DOMAIN     = "org";
  
  private static final String FULLNAME  = "fullname";
  private static final String EMAIL     = "id";
  private static final String STATUS    = "status";
  private static final String NUMBER    = "number";
  private static final String ROWS      = "rows";
  private static final String TOTAL     = "total";
  private static final String START     = "start";
  private static final String LIMIT     = "limit";
  private static final String DUPLICATE = "duplicate";
  
  private LdapAccountManager accountManager;
  private LdapMailAccountManager mailAccountManager;
  private LdapUserManager userManager;
  private String hashMethod;
  private List<HandleSavingMailService> handleSavingMailServices;
  
  /**
   * MailImportService constructor
   */
  public MailImportService(LdapAccountManager accountManager,
      LdapMailAccountManager mailAccountManager,
      LdapUserManager userManager,
      String hashMethod,
      List<HandleSavingMailService> handleSavingMailServices) {
    this.accountManager = accountManager;
    this.mailAccountManager = mailAccountManager;
    this.userManager = userManager;
    this.hashMethod = hashMethod;
    this.handleSavingMailServices = handleSavingMailServices;
  }

  /**
   * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  public JSON execute(HttpServletRequest request, HttpServletResponse response)
      throws MailAdministratorException {
    String action = this.getData(request, ACTION);
    if(ACTION_READ_ACCOUNT.equals(action)){
      return readAccount(request);
    }else if (ACTION_IMPORT.equals(action)) {
      return importAccount(request);
    }
    return FAILURE_JSON;
  }
  
  /**
   * read account from file excel
   * @param request
   * @return
   */
  private JSON readAccount(HttpServletRequest request){
    // get key attach
    String key = this.getData(request, KEY);
    String org = this.getData(request, DOMAIN);
    // check key attach
    if (!StringService.hasLength(key) || !StringService.hasLength(org)) {
      return FAILURE_JSON;
    }
    byte[] data = DocumentHelper.getContent(key);
    InputStream is = new ByteArrayInputStream(data);
    List<AccountImportInfo> infos = null;
    try {
      infos = ImportUtil.readEmail(is);
    }catch (IOException e) {
      logger.error(e);
      return FAILURE_JSON;
    }catch (Exception e) {
      logger.error(e);
      return FAILURE_JSON;
    }
    
    AccountImport accountImport = new AccountImport(org);
    accountImport.setTotal(infos.size());
    accountImport.setList(infos);
    key = AccountImportCacheService.put(accountImport, this.getUserName());
    
    return new JSONObject().accumulate("success", true)
                          .accumulate(TOTAL, infos.size())
                          .accumulate(KEY, key); 
  }
  
  /**
   * Import account email
   * @param request
   * @return
   */
  private JSON importAccount(HttpServletRequest request){
    // get key attach
    String key = this.getData(request, KEY);
    int start = WebCommonService.toInt(this.getData(request, START), 0);
    int limit = WebCommonService.toInt(this.getData(request, LIMIT), 25);
    
    AccountImport accountImport = AccountImportCacheService.get(key);
    
    if(accountImport == null){
      return FAILURE_JSON;
    }
    
    List<AccountImportInfo> errors = new ArrayList<AccountImportInfo>();
    int end = (start + limit)> accountImport.getTotal()?accountImport.getTotal():(start + limit);
    for(int i = start; i < end; i++){
      AccountImportInfo account = accountImport.getList().get(i);
      AccountImportInfo existAccount = accountImport.getAccount((account.getAccount()));
      // check domain
      if(!account.isSameOrg(accountImport.getOrg())){
        account.setStatus(AccountImportInfo.ERROR);
        errors.add(account);
        continue;
      }
      
      if(existAccount != null ){
        account.setStatus(AccountImportInfo.DUPLICATE);
        account.setExistAccount(String.format("%s-%s",existAccount.getNumber(), existAccount.getFullName()));
        errors.add(account);
      }else{
        try{
          addAccount(account, errors, accountImport.getOrg());
          accountImport.addAccount(account.getAccount(), account);
        }catch(Exception ex){
          account.setStatus(AccountImportInfo.ERROR);
          errors.add(account);
        }
      }
    }
    
    accountImport.getError().addAll(errors);
//    if(end >= (accountImport.getList().size() - 1)){
//      AccountImportCacheService.remove(key);
//    }else{
      AccountImportCacheService.update(key, accountImport);
//    }
    
    return convertJson(end - start, start, limit, errors);    
  }
  /**
   * Add account
   * @param info
   * @param errors
   */
  private void addAccount(AccountImportInfo info, List<AccountImportInfo> errors, String org) throws LotusException {
    
    ///////////1.CHECK ACCOUNT//////////////////////////////////////
    //1
    LdapAccount account = this.accountManager.findByName(info.getAccount(), true);
    if (account != null) {//2
      // is deleted
      if(account.isDeleted()){//3
        account.setDeleted(false);
        account.setFirstName(info.getFirstName());
        account.setMiddleName(MailCommonUtils.getLdapValue(info.getMiddleName()));
        account.setLastName(info.getLastName());
        String digestPwd = MailCommonUtils.getLdapPassword(info.getPassword(), this.hashMethod);
        account.setPassword(digestPwd);
        //update LDAP account
        accountManager.update(account);
        info.setCode(account.getCode());
        
        //4 create user
        // create LDAP user
        createLdapUser(info, org);
        //5 create email
        createEmail(info, false);
      }else{//6 LDAP account not deleted
        LdapUser ldapUser = this.userManager.findByName(info.getAccount());
        if(ldapUser != null){// 7 check mail 
          List<LdapMailAccount> mailAccs = this.mailAccountManager.findByUserName(info.getAccount());
          if(mailAccs == null || mailAccs.size() == 0){//8 create email
            // add Mail Account 
            createEmail(account, true, info.getQuota());
            // add error
            info.setStatus(AccountImportInfo.EXIST_USER);
            errors.add(info);
          }else{
            info.setStatus(AccountImportInfo.EXIST);
            errors.add(info);
          }
        }else{// 9 create user
          info.setCode(account.getCode());
          
          createLdapUser(info, org);
          // 10 create email
          createEmail(account, true, info.getQuota());
          info.setStatus(AccountImportInfo.EXIST_USER);
          errors.add(info);
        }
      }
    }else{
      // create LDAP account
      account = getLdapAccount(info);
      // add LDAP account
      String code = this.accountManager.addAccount(account).getCode();
      info.setCode(code);
      // create LDAP user
      createLdapUser(info, org);
      
      // add Mail Account 
      createEmail(info, false);
    }
  }
  
  /**
   * Create Ldap User
   * @param info
   */
  private void createLdapUser(AccountImportInfo info, String org) throws LotusException{
    LdapUser user = getLdapUser(info);
    // add the user information to organization
    this.userManager.addUser(org, user);
  }
  
  /**
   * create email account
   * @param info AccountImportInfo
   */
  private void createEmail(AccountImportInfo info, boolean empty) throws LotusException{
    LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
    getLdapMailAccount(mailAccount, info);
    // insert new mail account to database
    this.mailAccountManager.add(mailAccount);
    
    // launch new object for next using
    LdapMailAccount temp  = IOService.makeNewObject(mailAccount, LdapMailAccount.class);
    temp.setPassword(info.getPassword());
    
    // saving configuration for mail client
    this.handleAddAccount(new ClientConfigurationData(com.inet.web.service.conf.ClientConfigurationData.ACTION.ADD, temp));
  }
  
  /**
   * create email account
   * @param info AccountImportInfo
   */
  private void createEmail(LdapAccount info, boolean empty, String quota) throws LotusException{
    LdapMailAccount mailAccount = MailAccountUtil.initMailAccount(this.getConfiguration());
    getLdapMailAccount(mailAccount, info, quota);
    // insert new mail account to database
    this.mailAccountManager.add(mailAccount);
    
    // launch new object for next using
    LdapMailAccount temp  = IOService.makeNewObject(mailAccount, LdapMailAccount.class);
    temp.setPassword(StringService.EMPTY_STRING);
    
    // saving configuration for mail client
    this.handleAddAccount(new ClientConfigurationData(com.inet.web.service.conf.ClientConfigurationData.ACTION.ADD, temp));
  }
  
  /**
   * Create LDAP account from given AccountImportInfo
   * @param info
   * @return
   */
  private LdapAccount getLdapAccount(AccountImportInfo info){
    // create new LDAP account instance
    LdapAccount account = new LdapAccount();
    account.setName(info.getAccount());             
    account.setEmail(info.getAccount());
    account.setTimeZone("+0700");
    account.setCountry("vn");
    account.setFirstName(info.getFirstName());
    account.setLastName(info.getLastName());
    account.setMiddleName(MailCommonUtils.getLdapValue(info.getMiddleName()));
    
    // get the digest password          
    String digestPwd = MailCommonUtils.getLdapPassword(info.getPassword(), this.hashMethod);
    account.setPassword(digestPwd);
    
    return account;
  }
  
  /**
   * Create LDAP user from given AccountImportInfo
   * @param info
   * @return
   */
  private LdapUser getLdapUser(AccountImportInfo info){
    // create new LDAP account instance
    LdapUser account = new LdapUser();
    account.setName(info.getAccount());             
    account.setEmail(info.getAccount());
    account.setFirstName(MailCommonUtils.getLdapValue(info.getFirstName()));
    account.setLastName(MailCommonUtils.getLdapValue(info.getLastName()));
    account.setMiddleName(MailCommonUtils.getLdapValue(info.getMiddleName()));
    account.setFullName(MailCommonUtils.getLdapValue(info.getFullName()));
    account.setTitle(MailCommonUtils.getLdapValue(info.getPosition()));
    account.setHomePhone(MailCommonUtils.getLdapValue(info.getTelephone()));
    account.setMobile(MailCommonUtils.getLdapValue(info.getMobile()));
    
    // get the digest password          
    String digestPwd = MailCommonUtils.getLdapPassword(info.getPassword(), this.hashMethod);
    account.setPassword(digestPwd);
    
    return account;
  }
  
  /**
   * Set data for LDAP mail account
   * @param account LdapMailAccount
   * @param info AccountImportInfo
   */
  private void getLdapMailAccount(LdapMailAccount account, AccountImportInfo info){
    account.setEmail(info.getAccount());
    account.setAccountActive(true);
    account.setAmavisBypassSpamChecks(true);
    account.setAmavisBypassVirusChecks(true);
    account.setSmtpAuth(true);
    account.setQuota(info.getQuota());
    account.setForwardActive(false);
    account.setCreateDate(new Date());
    account.setFullName(MailCommonUtils.getFullName(info.getFirstName(), info.getMiddleName(), StringService.EMPTY_STRING));
    account.setLastName(info.getLastName());
    account.setEmail(info.getAccount());
    account.setMailBox(MailAccountUtil.getMailBox(info.getAccount()));
    account.setUserName(info.getAccount());
    account.setPassword(MailCommonUtils.getLdapPassword(info.getPassword(), this.hashMethod));
    account.setUserCode(info.getCode());
  }
  
  /**
   * Set data for LDAP mail account
   * @param account LdapMailAccount
   * @param info AccountImportInfo
   */
  private void getLdapMailAccount(LdapMailAccount account, LdapAccount info, String quota){
    account.setEmail(info.getName());
    account.setAccountActive(true);
    account.setAmavisBypassSpamChecks(true);
    account.setAmavisBypassVirusChecks(true);
    account.setSmtpAuth(true);
    account.setQuota(quota);
    account.setForwardActive(false);
    account.setCreateDate(new Date());
    account.setFullName(MailCommonUtils.getFullName(info.getFirstName(), info.getMiddleName(), StringService.EMPTY_STRING));
    account.setLastName(info.getLastName());
    account.setEmail(info.getName());
    account.setMailBox(MailAccountUtil.getMailBox(info.getName()));
    account.setUserName(info.getName());
    account.setPassword(MailCommonUtils.getLdapPassword(info.getPassword(), this.hashMethod));
    account.setUserCode(info.getCode());
    account.setPassword(info.getPassword());
  }
  
  /**
   * handle after saving mail account
   * 
   * @param configuration ClientConfiguration - the client configuration
   */
  private void handleAddAccount(ClientConfigurationData configuration) throws WebOSException {
    if(this.handleSavingMailServices == null) return;
    for(HandleSavingMailService savingMailService : this.handleSavingMailServices) {
            savingMailService.execute(configuration);
    }
  }
  
  /**
   * ConvertJSON
   * @param errors
   * @return
   */
  private JSON convertJson(int total, int start, int limit, List<AccountImportInfo> errors){
    JSONObject object = new JSONObject();
    List<JSONObject> objs = new ArrayList<JSONObject>();
    for(AccountImportInfo item: errors){
      JSONObject obj = new JSONObject();
      obj.accumulate(FULLNAME, item.getFullName())                                     
              .accumulate(EMAIL, item.getAccount())                                       
              .accumulate(STATUS, item.getStatus())
              .accumulate(NUMBER, item.getNumber())
              .accumulate(DUPLICATE, item.getExistAccount());
              
      // put to list 
      objs.add(obj);
    }
    
    return object.accumulate(ROWS, JSONService.toJSONArray(objs))
                .accumulate(TOTAL, total)
                .accumulate(START, start)
                .accumulate(LIMIT, limit);                         
  }
}
