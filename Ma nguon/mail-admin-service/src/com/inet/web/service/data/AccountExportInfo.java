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

/**
 * AccountExportInfo.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: AccountExportInfo.java Aug 16, 2011 5:40:16 PM Tan Truong $
 *
 * @since 1.0
 */
public class AccountExportInfo implements Serializable {
  
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 9212620839865979320L;
  
  private int number;
  private String fullName;
  private String account;
  private String lastName;
  private String middleName;
  private String firstName;
  private String position;
  private String telephone;
  private String mobile;
  private String quota;
  private String department;

  /**
   * @return the number
   */
  public int getNumber() {
    return number;
  }
  /**
   * @param number the number to set
   */
  public void setNumber(int number) {
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
  public void setQuota(String quota) {
    this.quota = quota;
  }
  /**
   * @return the department
   */
  public String getDepartment() {
    return department;
  }
  /**
   * @param department the department to set
   */
  public void setDepartment(String department) {
    this.department = department;
  }
}
