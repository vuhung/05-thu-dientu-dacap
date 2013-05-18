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

import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.DigestService;
import com.inet.base.ssl.SecureService;
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.exception.SystemBusyException;

/**
 * CheckMailTest.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class CheckMailTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(DigestService.getPassword(DigestService.HASH_METHOD_SMD5,"tntan"));
		
		Properties env = new Properties();
		//env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		env.setProperty(Context.PROVIDER_URL, "pc09:1099");
		env.setProperty(Context.SECURITY_PRINCIPAL, "tntan");
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.setProperty(Context.SECURITY_CREDENTIALS, "tntan");
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");

		InitialContext ctx = null;
		try {
			ctx = new InitialContext(env);
			MailBridgeRemote mailMF = (MailBridgeRemote) ctx.lookup("MailBridgeSLBean/remote");
			mailMF.delete(1229);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		//System.out.println(DigestService.base64Encode(DigestService.utf8encode("tntan")));
		catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try {
			MailBridgeRemote mailMF = (MailBridgeRemote) ctx.lookup("MailBridgeSLBean/remote");
			int count = mailMF.countNewMessage();
			System.out.println(count);
			HashMap<Long,Long> folders = mailMF.countMessage();
			for (Long folderID : folders.keySet())
				System.out.println("Folder ID : " + folderID + " [" + folders.get(folderID) + "]");
	} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemBusyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
	}
	

}
