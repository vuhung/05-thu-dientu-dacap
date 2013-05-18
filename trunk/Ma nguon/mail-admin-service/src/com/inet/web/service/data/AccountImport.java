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

/**
 * AccountImport.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: AccountImport.java Apr 8, 2011 11:23:36 AM Tan Truong $
 *
 * @since 1.0
 */
public class AccountImport implements Serializable {

  /**
   * Serial version UID
   */
  private static final long serialVersionUID = 3025891247596897985L;
  private final String org;
  private int total;
  private List<AccountImportInfo> list;
  private List<AccountImportInfo> error = new ArrayList<AccountImportInfo>();
  private HashMap<String,AccountImportInfo> map = new HashMap<String, AccountImportInfo>();
  
  /**
   * AccountImport constructor default
   */
  public AccountImport(String org){
    this.org = org;
  }

  
  /**
   * @return the org
   */
  public String getOrg() {
    return org;
  }



  /**
   * @return the total
   */
  public int getTotal() {
    return total;
  }

  /**
   * @param total the total to set
   */
  public void setTotal(int total) {
    this.total = total;
  }

  /**
   * @return the list
   */
  public List<AccountImportInfo> getList() {
    return list;
  }

  /**
   * @param list the list to set
   */
  public void setList(List<AccountImportInfo> list) {
    this.list = list;
  }

  /**
   * @return the error
   */
  public List<AccountImportInfo> getError() {
    return error;
  }

  /**
   * @param error the error to set
   */
  public void setError(List<AccountImportInfo> error) {
    this.error = error;
  }

  /**
   * @return the map
   */
  public HashMap<String, AccountImportInfo> getMap() {
    return map;
  }

  /**
   * @param map the map to set
   */
  public void setMap(HashMap<String, AccountImportInfo> map) {
    this.map = map;
  }
  
  /**
   * get account 
   * @param email
   * @return
   */
  public AccountImportInfo getAccount(String email){
    return this.map.get(email);
  }
  
  /**
   * add account
   * @param email
   * @param info
   */
  public void addAccount(String email, AccountImportInfo info){
    this.map.put(email, info);
  }
}
