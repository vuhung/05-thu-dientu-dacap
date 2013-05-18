/*****************************************************************
   Copyright 2006 by Tung Luong (lqtung@truthinet.com.vn)

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
package com.inet.mail;

import java.util.Properties;

import javax.naming.Context;

import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import junit.framework.TestCase;

/**
 * TestSendMail.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 0.2i
 */
public abstract class AbstractTestMail<T> extends TestCase{
	protected T bean;
	protected final String userCode = "0000011ac86df0c1-ac100126008d2e37-a10aa8de";
	
	/**
	 * Get the default properties
	 * @return - Properties
	 */
	protected Properties getProperties(){
		Properties env = new Properties();
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		env.setProperty(Context.PROVIDER_URL, "iwebos-server2:1099");
		env.setProperty(Context.SECURITY_PRINCIPAL, "nvchuyen");
		env.setProperty(Context.SECURITY_CREDENTIALS, "{SMD5}sd+1iiB5uPMlroaeX1SZLc0MKJw8f/nq");
	
		//env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");
		SecurityAssociation.setPrincipal(new SimplePrincipal("nvchuyen")) ;
		SecurityAssociation.setCredential("{SMD5}sd+1iiB5uPMlroaeX1SZLc0MKJw8f/nq".toCharArray()) ;
		//return the environment
		return env;
	}
}
