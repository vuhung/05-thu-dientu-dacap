/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.demo;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.data.MailReceiverProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.persistence.MailConfigure;
import com.inet.mail.util.MailService;

/**
 * MailConfigTest.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class MailConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties env = new Properties();
		//env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		env.setProperty(Context.PROVIDER_URL, "hiennguyenvanlt:1099");
		env.setProperty(Context.SECURITY_PRINCIPAL, "hiennguyen");
		env.setProperty(Context.SECURITY_CREDENTIALS, "hiennguyen");
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");

		InitialContext ctx = null;
		try {
			ctx = new InitialContext(env);
		} catch (NamingException e) {
			ctx = null;
		}
		
		
		try {
			MailAcctConfigInfoRemoteSL mail = (MailAcctConfigInfoRemoteSL) ctx.lookup("MailConfigureSLBean/remote");
			//mail.delete(4);
			MailReceiverObject truthinet = new MailReceiverObject("hiennguyen@truthinet.com.vn") ;
			truthinet.setAccountPassword(MailService.encrypt(truthinet.getAccountName(),"hiennguyen")) ;
			truthinet.setProtocol(MailReceiverProtocol.POP3) ;
			truthinet.setSecurity(MailSecurity.NONE) ;
			truthinet.setServerName("mail.truthinet.com.vn") ;
			truthinet.setServerPort(110) ;
						
			MailConfigure config = new MailConfigure();
			config.setServer("mail.truthinet.com.vn");
			config.setOwner("0000011ac86df0c2-ac100126008d2e37-a10aa8de");
			config.setAccountName("hiennguyen@truthinet.com.vn");
			config.setPassword(MailService.encrypt(config.getAccountName(),"hiennguyen"));
			
			//config.setEmailAddress("hiennguyen@truthinet.com.vn");
			config.setSecurity(MailSecurity.NONE);
			config.setFullname("Nguyen Van Chuyen");
			config.setPort(25);
			config.setLeaveonserver(-1);
			config.setRefresh(15);
			
			MailReceiverDTO receiverDTO = new MailReceiverDTO() ;
			receiverDTO.addAccount(truthinet) ;
			config.setReceiverObject(receiverDTO);
			config.setId(2L);
			mail.update(config);
			
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
