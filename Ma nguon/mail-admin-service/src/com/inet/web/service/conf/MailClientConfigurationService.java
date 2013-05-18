/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *****************************************************************/
package com.inet.web.service.conf;

import java.util.ArrayList;
import java.util.List;

import com.inet.base.service.StringService;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.util.MailService;
import com.inet.web.bo.config.MailAcctConfigInfoBO;
import com.inet.web.exception.WebOSException;

/**
 * MailClientConfigurationService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailClientConfigurationService implements HandleSavingMailService {
  private MailAcctConfigInfoBO mailAcctConfigInfoBO;
  private MailReceiverObject receiverObject;

  /**
   * Create <tt>MailClientConfigurationService</tt> instance
   * 
   * @param mailAcctConfigInfoBO
   *          MailAcctConfigInfoBO - the mail account configure BO
   * @param receiverObject
   *          MailReceiverObject - the mail receiver object
   */
  public MailClientConfigurationService(MailAcctConfigInfoBO mailAcctConfigInfoBO,
      MailReceiverObject receiverObject) {
    this.mailAcctConfigInfoBO = mailAcctConfigInfoBO;
    this.receiverObject = receiverObject;
  }

  /**
   * @see com.inet.web.service.HandleService#execute(java.lang.Object)
   */
  public void execute(ClientConfigurationData configuration) throws WebOSException {
    if (configuration == null || configuration.getMailAccounts() == null)
      return;
    switch (configuration.getAction()) {
    case ADD: // handle after adding mail account
      this.addMailConfiguration(configuration.getMailAccounts());
      break;

    case ACTIVE: // handle after active/inactive mail account
      this.active(configuration.getMailAccounts());
      break;

    case DELETE: // handle after deleting account
      this.delete(configuration.getMailAccounts());
      break;
    }
  }

  /**
   * saving mail configuration for mail client
   * 
   * @param mailAccount
   *          LdapMailAccount... - the mail accounts
   * @throws WebOSException
   *           - when there is error happens
   */
  private void addMailConfiguration(LdapMailAccount... mailAccounts) throws WebOSException {
    if (this.receiverObject == null)
      return;

    if (mailAccounts != null && mailAccounts.length != 0) {
      for (LdapMailAccount mailAccount : mailAccounts) {
        // fill the information
        this.receiverObject.setEmailAddress(mailAccount.getEmail());
        this.receiverObject.setDescription(mailAccount.getEmail());
        this.receiverObject.setAccountName(mailAccount.getEmail());
        this.receiverObject.setSMTPAccountName(mailAccount.getEmail());
        this.receiverObject.setActive(mailAccount.isAccountActive());        
        if (StringService.hasLength(mailAccount.getPassword())) {
          // encrypt password
          String password = MailService.encrypt(
              this.receiverObject.getAccountName(), mailAccount.getPassword());
          
          // put password to receiver object
          this.receiverObject.setAccountPassword(password);
          this.receiverObject.setSMTPAccountPassword(password);
        } else {
          // clear password in receiver object
          this.receiverObject.setAccountPassword(StringService.EMPTY_STRING);
          this.receiverObject.setSMTPAccountPassword(StringService.EMPTY_STRING);
        }

        // saving data
        this.mailAcctConfigInfoBO.addConfig(mailAccount.getUserCode(), mailAccount.getLastName()
            + " " + mailAccount.getFullName(), this.receiverObject);
      }
    }
  }

  /**
   * delete client configuration
   * 
   * @param accounts
   *          LdapMailAccount... - the list of email account
   * @throws WebOSException
   *           - when there is error happens
   */
  private void delete(LdapMailAccount... accounts) throws WebOSException {
    if (accounts != null && accounts.length > 0) {
      // create list to contain email address
      List<String> emails = new ArrayList<String>(accounts.length);
      for (LdapMailAccount account : accounts) {
        // add email to list
        emails.add(account.getEmail());
      }
      // delete configuration
      this.mailAcctConfigInfoBO.deleteConfig(accounts[0].getUserCode(), emails);
    }
  }

  /**
   * active/inactive account in mail configuration
   * 
   * @param mailAccount
   *          LdapMailAccount... - the mail account informations
   * @throws WebOSException
   *           - when there is error happens
   */
  private void active(LdapMailAccount... activeAccounts) throws WebOSException {
    if (activeAccounts != null && activeAccounts.length != 0) {
      for (LdapMailAccount mailAccount : activeAccounts) {
        // update configuration
        this.mailAcctConfigInfoBO.active(mailAccount.getUserCode(), mailAccount.getEmail(),
            mailAccount.isAccountActive());
      }
    }

  }
}
