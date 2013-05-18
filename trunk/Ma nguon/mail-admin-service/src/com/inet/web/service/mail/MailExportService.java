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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.ldap.PageResult;
import com.inet.lotus.mail.data.search.ldap.LdapMailSearchBean;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.ILdapMailAccountManager;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.web.service.AbstractLotusService;
import com.inet.web.service.data.AccountExport;
import com.inet.web.service.data.AccountExportInfo;
import com.inet.web.service.exception.MailAdministratorException;
import com.inet.web.service.mail.utils.AccountExportCacheService;
import com.inet.web.service.mail.utils.MailCommonUtils;

/**
 * MailExportService.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailExportService.java Aug 16, 2011 9:15:52 AM Tan Truong $
 * 
 * @since 1.0
 */
public class MailExportService extends AbstractLotusService {
  private static final String KEY = "key";
  private static final String ACTION = "action";
  private static final String ACTION_READ_ACCOUNT = "read";
  private static final String ACTION_EXPORT_ACCOUNT = "export";
  private static final String TOTAL = "total";
  // The domain identify parameter
  private static final String DOMAIN = "id";
  
  private ILdapMailAccountManager mailAccountManager;
  private ILdapUserManager userManager;
  private ILdapGroupManager groupManager;

  /**
   * MailExportService constructor
   * 
   * @param mailAccountManager
   * @param userManager
   * @param groupManager
   */
  public MailExportService(ILdapMailAccountManager mailAccountManager,
      ILdapUserManager userManager, ILdapGroupManager groupManager) {
    this.mailAccountManager = mailAccountManager;
    this.userManager = userManager;
    this.groupManager = groupManager;
  }

  /**
   * @see com.inet.web.service.AbstractLotusService#execute(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  public JSON execute(HttpServletRequest request, HttpServletResponse response)
      throws MailAdministratorException {
    String action = this.getData(request, ACTION);
    if (ACTION_READ_ACCOUNT.equals(action)) {
      return readAccount(request);
    }else if(ACTION_EXPORT_ACCOUNT.equals(action)){
      try {
        return export(request);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    return FAILURE_JSON;
  }
  
  /**
   * Read account
   * @param request
   * @return
   */
  private JSON readAccount(HttpServletRequest request){
    // get key attach
    String domain = request.getParameter(DOMAIN);
    
    LdapMailSearchBean searchBean = new LdapMailSearchBean(0, 10, null);
    searchBean.setDomainName(domain);
    
    PageResult<LdapMailAccount> wrapper = this.mailAccountManager.pagination(searchBean);
    if(wrapper.getSize() > 10){
      searchBean = new LdapMailSearchBean(0, wrapper.getSize(), null);
      searchBean.setDomainName(domain);
      
      // find all contacts of given domain
      wrapper = this.mailAccountManager.pagination(searchBean);
    }
    
    List<LdapGroup> groups = this.groupManager.findAllRealGroups(domain);
    
    AccountExport accountExport = new AccountExport();
    if(groups != null && groups.size() > 0){  
      accountExport.setGroup(groups);
    }
    
    accountExport.setTotal(wrapper.getData());
    accountExport.setDomain(domain);
    
    String key = AccountExportCacheService.put(accountExport, this.getUserName());
    
    return new JSONObject().accumulate("success", true)
                          .accumulate(TOTAL, wrapper.getSize())
                          .accumulate(KEY, key); 
  }
  
  /**
   * Export data
   * @param request
   * @return
   */
  private JSON export(HttpServletRequest request) throws Exception{
    // get key attach
    String key = request.getParameter(KEY);
    
    AccountExport export = AccountExportCacheService.get(key);
    List<LdapMailAccount> total = export.getTotal();
    List<AccountExportInfo> infos = new ArrayList<AccountExportInfo>();
    Map<LdapGroup, List<AccountExportInfo>> map = export.getMap();
    
    if(export.getGroup().size() > 0){
      List<LdapGroup> groups = export.getGroup();
      
      infos = exportGroup(groups.get(0),export.getTotal(), export.getDomain()); 
      map.put(groups.get(0), infos);
      
      // remove data
      groups.remove(0);
      
      // set data
      export.setGroup(groups);
      export.setTotal(total);
      
      AccountExportCacheService.update(key, export);
      
      return new JSONObject().accumulate(TOTAL, infos.size());
      
    }else{
      int index = 1;
      for(LdapMailAccount account: total){
        infos.add(exportAccount(account, index++, export.getDomain()));
      }
      
      LdapGroup commonGroup = new LdapGroup();
      commonGroup.setName("COMMON");
      commonGroup.setDescription("Description");
      
      map.put(commonGroup,infos);
      
      AccountExportCacheService.update(key, export);
      
      return new JSONObject().accumulate(TOTAL, infos.size())
                          .accumulate("COMPLETE", true);
    }
  }
  
  /**
   * Export Group
   * @param group
   * @param accounts
   * @param total
   * @param domain
   * @return
   */
  private List<AccountExportInfo> exportGroup(LdapGroup group, 
      List<LdapMailAccount> total, String domain) throws Exception{
    LdapMailSearchBean searchBean = new LdapMailSearchBean(0, 10, null);
    searchBean.setDomainName(domain);
    
    
    PageResult<LdapMailAccount> wrapper = this.mailAccountManager.pagination(searchBean);
    if(wrapper.getSize() > 10){
      searchBean = new LdapMailSearchBean(0, wrapper.getSize(), null);
      searchBean.setDomainName(domain);
      searchBean.setDepartment(group.getName());
      
      // find all contacts of given domain
      wrapper = this.mailAccountManager.pagination(searchBean);
    }
    
    int index = 1;
    List<AccountExportInfo> infos = new ArrayList<AccountExportInfo>();
    
    for(LdapMailAccount account: wrapper.getData()){
      infos.add(exportAccount(account, index++, domain));
      total.remove(account);
    }
    
    return infos;
  }
  
  /**
   * Export Account 
   * @param account
   * @param index
   * @param domain
   * @return
   */
  private AccountExportInfo exportAccount(LdapMailAccount account, 
      int index, String domain) throws Exception{
    AccountExportInfo info = new AccountExportInfo();
    info.setNumber(index);
    info.setAccount(account.getUserName());
    info.setQuota(String.valueOf(MailCommonUtils.getMByte(Long.valueOf(account.getQuota()))));
    
    LdapUser user = this.userManager.loadUser(domain, account.getUserName());
    
    if(user == null){
      return null;
    }
    
    info.setLastName(user.getLastName());
    info.setFullName(user.getFullName());
    info.setFirstName(user.getFirstName());
    info.setMiddleName(user.getMiddleName());
    info.setPosition(user.getTitle());
    info.setMobile(user.getMobile());
    info.setTelephone(user.getHomePhone());
    
    return info;
  }
}
