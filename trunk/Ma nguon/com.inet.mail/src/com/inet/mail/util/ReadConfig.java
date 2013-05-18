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
package com.inet.mail.util;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;

import com.inet.base.ejb.ServiceLocator;
import com.inet.base.ejb.ServiceLocatorManager;
import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.StringService;
import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.persistence.MailAcctConfigInfo;

/**
 * ReadConfig.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ReadConfig.java Mar 13, 2011 11:39:57 AM Tan Truong $
 *
 * @since 1.0
 */
public class ReadConfig {
  private static final int START = 1;
  private static final int END = 20;
  /**
   * Get the default properties
   * 
   * @return - Properties
   */
  private Properties getProperties() {
    Properties env = new Properties();
    env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    env.setProperty(Context.PROVIDER_URL, "mail:1099");

    return env;
  }

  /**
   * Lookup MailAcctConfigInfoRemote bean
   * 
   * @return
   */
  private MailAcctConfigInfoRemoteSL getBean() {
    ServiceLocator locator = ServiceLocatorManager.getInstance().getServiceLocator(
        this.getProperties());
    MailAcctConfigInfoRemoteSL bean = locator.getRemoteBean("MailAcctConfigInfoSLBean",
        MailAcctConfigInfoRemoteSL.class);
    return bean;
  }

  private void readDB() {
    MailAcctConfigInfoRemoteSL bean = getBean();
    for (int i = START; i <= END; i++) {
      try {
        MailAcctConfigInfo config = bean.load(i);
        showData(config);
      } catch (EJBException e) {
        e.printStackTrace();
      }
    }
  }

  private void showData(MailAcctConfigInfo config) {
    System.out.println(config.getId() + "----------" +config.getFullname() +  "--------------------------------------------------");
    System.out.println("Default SMTP--" + config.getDefaultSMTP());
    MailReceiverDTO oldReceiverDTO = config.getReceiverObject();
    
    for(MailReceiverObject object : oldReceiverDTO.getAccounts()){
      System.out.println("Active:    " + object.getActive());
      System.out.println("Protocol:  " + object.getProtocol());
      System.out.println("Security   " + object.getSecurity());
      System.out.println("ServerName " + object.getServerName());
      System.out.println("Port       " + object.getServerPort());
      System.out.println("Pwd        " + MailService.decrypt(object.getAccountName(), object.getAccountPassword()));
      System.out.println("Acc Name   " + object.getAccountName());
      System.out.println("Email      " + object.getEmailAddress());
      System.out.println("Des        " + object.getDescription());
      
      System.out.println("Smtp Acc   " + object.getSMTPAccountName());
      System.out.println("Smtp Sec   " + object.getSMTPSecurity());
      System.out.println("Smtp Server" + object.getSMTPServerName());
      System.out.println("Smtp port  " + object.getSMTPServerPort());
      System.out.println("Smtp pwd   " + MailService.decrypt(object.getAccountName(), object.getSMTPAccountPassword()));
    }
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    ReadConfig update = new ReadConfig();
    update.readDB();
  }
}
