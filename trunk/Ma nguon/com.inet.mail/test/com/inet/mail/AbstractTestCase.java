/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;
import org.testng.annotations.BeforeTest;

import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.ldap.LotusLdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.cache.util.AccountCacheHelper;

/**
 * AbstractTestCase
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractTestCase.java 2008-12-13 05:56:16z nguyen_dv $
 * 
 * Create date: Dec 13, 2008
 * <pre>
 *  Initialization AbstractTestCase class.
 * </pre>
 */
public abstract class AbstractTestCase {
	//~ Instance fields =======================================================
	/* context. */
	protected InitialContext context ;
	
	/**
	 * Setting up the environment.
	 */
	@BeforeTest
	public void setUp(){
		if(context == null){
			// create properties.
			Properties env = new Properties();
			
			env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			env.setProperty(Context.PROVIDER_URL, "iwebos-server2.truthinet.com.vn:1099");
			env.setProperty(Context.SECURITY_PRINCIPAL, "tttduyen");
			env.setProperty(Context.SECURITY_CREDENTIALS, "{SMD5}8NfsZC7YJDs1F6PWSulFnbMbWH89OXa1");
			
			try{
				context = new InitialContext(env) ;
			}catch(NamingException nex){}
		}		
		
		// login to system.
		login("tttduyen") ;		
		
		SecurityAssociation.setPrincipal(new SimplePrincipal("tttduyen")) ;
		SecurityAssociation.setCredential("{SMD5}8NfsZC7YJDs1F6PWSulFnbMbWH89OXa1".toCharArray()) ;		
	}
	
	/**
	 * Login to LDAP.
	 * 
	 * @param account the given LDAP account.
	 */
	protected void login(String uid){
		if(AccountCacheHelper.get(uid) == null){
			// get the account manager bean.
			LotusLdapAccountManager accountManager = MailApplicationContext.getInstance().getObject(
						"accountManager",
						LotusLdapAccountManager.class
					) ;
			
			try{
				// lookup the account.
				Account account = accountManager.findByName(uid) ;
				
				// put account to cache.
				if(account != null){
					AccountCacheHelper.put(account) ;
				}
			}catch(LotusException lex){}
		}
	}
}
