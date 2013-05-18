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
package com.inet.web.service.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.org.permission.ldap.LdapGroup;

/**
 * AccountExport.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: AccountExport.java Aug 16, 2011 5:53:30 PM Tan Truong $
 *
 * @since 1.0
 */
public class AccountExport implements Serializable {
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -7490608259465465142L;
  
  private Map<LdapGroup, List<AccountExportInfo>> map = new HashMap<LdapGroup, List<AccountExportInfo>>();
  private List<LdapGroup> group = new ArrayList<LdapGroup>();
  private List<LdapMailAccount> total = new ArrayList<LdapMailAccount>();
  private String domain;
  
  /**
   * AccountExport constructor default
   */
  public AccountExport(){}
  
  /**
   * @return the map
   */
  public Map<LdapGroup, List<AccountExportInfo>> getMap() {
    return map;
  }

  /**
   * @param map the map to set
   */
  public void setMap(Map<LdapGroup, List<AccountExportInfo>> map) {
    this.map = map;
  }
  
  /**
   * Add department
   * @param department the given department
   * @param accounts the given account of department
   */
  public void addDepartment(LdapGroup department, List<AccountExportInfo> accounts){
    this.map.put(department, accounts);
  }

  /**
   * @return the group
   */
  public List<LdapGroup> getGroup() {
    return group;
  }

  /**
   * @param group the group to set
   */
  public void setGroup(List<LdapGroup> group) {
    this.group = group;
  }

  /**
   * @return the total
   */
  public List<LdapMailAccount> getTotal() {
    return total;
  }

  /**
   * @param total the total to set
   */
  public void setTotal(List<LdapMailAccount> total) {
    this.total = total;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * @param domain the domain to set
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }  
}
