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
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.exception.SystemBusyException;

/**
 * FetchMailTest.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class FetchMailTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties env = new Properties();
		env.setProperty(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		env.setProperty(Context.PROVIDER_URL, "iwebos-server2.truthinet.com.vn:1099");
		env.setProperty(Context.SECURITY_PRINCIPAL, "nvchuyen");
		env.setProperty(Context.SECURITY_CREDENTIALS, "{SMD5}sd+1iiB5uPMlroaeX1SZLc0MKJw8f/nq");
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.security.jndi.JndiLoginInitialContextFactory");
		
		System.out.println(">>>>>>>>>>>>>>>>>>");
		InitialContext ctx = null;
		try {
			ctx = new InitialContext(env);
		} catch (NamingException e) {
			ctx = null;
		}

		MailBridgeRemote mailMF;
		try {
			mailMF = (MailBridgeRemote) ctx.lookup("MailBridgeSLBean/remote");
			
			// get new message
			int count = mailMF.countNewMessage();
			System.out.println("[New messages]= " + count);
			//count = 1;
			while (count > 0) {
				try {
					// fetch message
					MailHeaderDTO header = mailMF.fetch();
					
					// check to make sure this message is downloaded
					boolean downloading = true;
					while (downloading)
					{
						Long headerID = mailMF.isAvailable(header.getMailhost(), header.getUid());
						if (headerID == 0)
						{
							// sleep to get back next time
							try {
								Thread.sleep(300);
							} catch (InterruptedException e1) {
							}
						} else if (headerID > 0)
						{
							// update to grid
							header.setHeaderId(headerID);
							System.out.println(count + ":" + header.getSubject());
							count--;
							downloading = false;
						} 
						else if (headerID < 0)
						{
							// message fetching error!!!!
							downloading = false;
						}
					}
				} 
				catch (SystemBusyException e) {
					// count++;
					try {
						Thread.sleep(300);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					}
					// e.printStackTrace();
				} catch (EJBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (NamingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SystemBusyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
