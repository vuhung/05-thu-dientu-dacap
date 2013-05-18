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

import com.inet.base.service.StringService;
import com.inet.lotus.mail.util.MailUtil;

/**
 * AccountImportInfo.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: AccountImportInfo.java Apr 7, 2011 11:23:12 AM Tan Truong $
 *
 * @since 1.0
 */
public class AccountImportInfo implements Serializable {

  /**
   * Serial version UID
   */
  private static final long serialVersionUID = -6675994992672254144L;
  public static final byte NONE         = 0;
  public static final byte EXIST_USER   = 1;
  public static final byte EXIST        = 2;
  public static final byte ERROR        = 3;
  public static final byte DUPLICATE    = 4;
  
  private long number;
  private String fullName;
  private String account;
  private String lastName;
  private String middleName = StringService.EMPTY_STRING;
  private String firstName;
  private String password = StringService.EMPTY_STRING;
  private String position;
  private String telephone;
  private String mobile;
  private String code;
  private String existAccount = StringService.EMPTY_STRING;
  private byte status;
  private String quota = String.valueOf(50 * 1024 * 1024);
  /**
   * AccountImportInfo constructor
   */
  public AccountImportInfo(){
    
  }

  /**
   * @return the number
   */
  public double getNumber() {
    return number;
  }

  /**
   * @param number the number to set
   */
  public void setNumber(long number) {
    this.number = number;
  }

  /**
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * @return the account
   */
  public String getAccount() {
    return account;
  }

  /**
   * @param account the account to set
   */
  public void setAccount(String account) {
    this.account = account;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the middleName
   */
  public String getMiddleName() {
    return middleName;
  }

  /**
   * @param middleName the middleName to set
   */
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the status
   */
  public byte getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(byte status) {
    this.status = status;
  }

  /**
   * @return the existAccount
   */
  public String getExistAccount() {
    return existAccount;
  }

  /**
   * @param existAccount the existAccount to set
   */
  public void setExistAccount(String existAccount) {
    this.existAccount = existAccount;
  }

  /**
   * @return the position
   */
  public String getPosition() {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(String position) {
    this.position = position;
  }

  /**
   * @return the telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * @param telephone the telephone to set
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  /**
   * @return the mobile
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * @param mobile the mobile to set
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * @return the quota
   */
  public String getQuota() {
    return quota;
  }

  /**
   * @param quota the quota to set
   */
  public void setQuota(long quota) {
    this.quota = String.valueOf(quota * 1024 * 1024);
  }
  
  /**
   * check user is same organization
   * @param org
   * @return
   */
  public boolean isSameOrg(String org){
    if(StringService.hasLength(this.account)){
      String domain = MailUtil.getDomainName(this.account);
      if(StringService.hasLength(domain)){
        return domain.equals(org);
      }
    }
    
    return false;
  }
}