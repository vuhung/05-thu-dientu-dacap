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
 * UpdateConfiguration.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: UpdateConfiguration.java Mar 13, 2011 9:51:46 AM Tan Truong $
 * 
 * @since 1.0
 */
public class UpdateConfiguration {

  private static final int TOTAL = 3303;

  /**
   * Get the default properties
   * 
   * @return - Properties
   */
  private Properties getProperties() {
    Properties env = new Properties();
    env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    env.setProperty(Context.PROVIDER_URL, "tntan:1099");

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

  private void updateDB() {
    MailAcctConfigInfoRemoteSL bean = getBean();
    for (int i = 1; i <= TOTAL; i++) {
      try {
        MailAcctConfigInfo config = bean.load(i);
        System.out.println("Id : " + config.getId() + "----" + config.getFullname());
        // update data
        updateConfig(config);
        
        //preProcessing(config, true);
        // upfate to DB
        bean.update(config);
      } catch (EJBException e) {
        e.printStackTrace();
      }
    }
  }

  private void updateConfig(MailAcctConfigInfo config) {
    MailReceiverDTO oldReceiverDTO = config.getReceiverObject();
    MailReceiverDTO newReceiverDTO = new MailReceiverDTO() ;
    List<MailReceiverObject> objects =  oldReceiverDTO.getAccounts();
    
    for(MailReceiverObject object : objects){
      MailReceiverObject newObj = new MailReceiverObject(lowerCase(object.getSMTPAccountName()));
      newObj.setActive(object.getActive());
      newObj.setProtocol(object.getProtocol());
      newObj.setSecurity(object.getSecurity());
      newObj.setServerName(object.getServerName());
      newObj.setServerPort(object.getServerPort());
      
      newObj.setAccountName(lowerCase(object.getAccountName()));
      
      String pwd = MailService.decrypt(object.getAccountName(), object.getAccountPassword());
      pwd = MailService.encrypt( newObj.getAccountName(), pwd);
      newObj.setAccountPassword(pwd);
      /**/
      newObj.setEmailAddress(lowerCase(object.getAccountName()));
      newObj.setDescription(lowerCase(object.getAccountName()));
      
      newObj.setSMTPAccountName(lowerCase(object.getSMTPAccountName()));
      newObj.setSMTPSecurity(object.getSMTPSecurity());
      newObj.setSMTPServerName(object.getSMTPServerName());
      newObj.setSMTPServerPort(object.getSMTPServerPort());
      newObj.setSMTPAccountPassword(pwd);
      
      newReceiverDTO.addAccount(newObj);
    }
    
    config.setReceiverObject(newReceiverDTO);
    
//    MailReceiverObject object = new MailReceiverObject(old.get);
//    object.setActive(true);
//    object.setProtocol(MailReceiverProtocol.POP3);
//    object.setSecurity(MailSecurity.NONE);
//    object.setServerName("mail.tayninh.gov.vn");
//    object.setServerPort(110);
//    object.setAccountPassword(MailService.encrypt(email,password));
//    object.setAccountName(email);
//    object.setEmailAddress(email);
//    object.setDescription(email);
//    
//    object.setSMTPAccountName(email);
//    object.setSMTPSecurity(MailSecurity.TLS);
//    object.setSMTPServerName("mail.tayninh.gov.vn");
//    object.setSMTPServerPort(25);
//    object.setSMTPAccountPassword(MailService.encrypt(email,password));
//    }
  }

  private String lowerCase(String text){
    return StringService.hasLength(text)?text.toLowerCase():text;
  }
  
  private void preProcessing(MailAcctConfigInfo configure, boolean save){
    if(configure == null) return;
            
    // load configuration from database.
    if(!save){
            if(configure.getReceiverServer() != null){
                    try{
                            // get receiver object.
                            configure.setReceiverObject(MailReceiverDTO.convertFrom(configure.getReceiverServer()));
                    }catch(Exception ex){}
            }
    }else{ // save data to database.
            if(configure.getReceiverObject() != null){
                    configure.setReceiverServer(configure.getReceiverObject().getData()) ;
            }else{
                    configure.setReceiverServer(null) ;
            }
    }
}
  /**
   * @param args
   */
  public static void main(String[] args) {
    UpdateConfiguration update = new UpdateConfiguration();
    update.updateDB();
  }
}
