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
package com.inet.mail.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.inet.base.ejb.persistence.BasePersistence;

/**
 * MailError.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailError.java Feb 19, 2011 10:03:05 AM Tan Truong $
 *
 * @since 1.0
 */
@Entity
@Table(name="MAIL_ERROR")
@NamedQueries(value={
    @NamedQuery(name="MailError.findUidByAccount", query="SELECT NEW java.lang.String(m.uid) FROM MailError m WHERE m.account=:account AND m.owner=:owner")
})
public class MailError extends BasePersistence<Long> {

  /**
   * Serial version UID
   */
  private static final long serialVersionUID = -3854624184325153634L;
  private String owner ;
  private String uid;
  private String account;
  
  /**
   * MailError constructor
   * @param uid String - the given uuid message
   * @param owner - the given user code
   * @param account - the given account information
   */
  public MailError(String uid, String owner, String account){
    this.uid = uid;
    this.owner = owner;
    this.account = account;
  }
  
  /**
   * @return the mail filter identifier.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  public Long getId(){
    return this.id ;
  }
  
  /**
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }
  /**
   * @param owner the owner to set
   */
  public void setOwner(String owner) {
    this.owner = owner;
  }
  /**
   * @return the uid
   */
  public String getUid() {
    return uid;
  }
  /**
   * @param uid the uid to set
   */
  public void setUid(String uid) {
    this.uid = uid;
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
}
