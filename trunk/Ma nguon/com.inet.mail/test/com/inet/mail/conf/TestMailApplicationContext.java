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
package com.inet.mail.conf;

import java.util.Map;

import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.inet.mail.conf.beans.ConfigurationBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.EmailDictionaryKey;


/**
 * TestMailApplicationContext
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestMailApplicationContext.java 2008-12-10 14:09:31Z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization TestMailApplicationContext class.
 * </pre>
 */
public class TestMailApplicationContext {
	//~ Instance fields =======================================================
	/* mail application context. */
	private MailApplicationContext context = null ;
	
	//~ Methods ===============================================================
	/**
	 * Initialization methods. 
	 */
	@BeforeTest
	public void setUp(){
		context = MailApplicationContext.getInstance() ;
	}
	
	/**
	 * Test spring context.
	 */
	@Test(threadPoolSize=5, invocationCount=10)
	public void testSpringContext(){
		Assert.assertNotNull("WebOSApplicationContext has set.", context.getContext()) ;
		Assert.assertNotNull("Spring context has set.", context.getContext().getSpringContext()) ;
		
		Object bean = context.getContext().getSpringContext().getBean("applicationServer") ;
		Assert.assertEquals(bean.getClass().getName(), "com.inet.web.application.spring.ApplicationServerProvider") ;
	}
	
	/**
	 * Test configuration.
	 */
	@Test(threadPoolSize=5, invocationCount=10)
	public void testConfigure(){
		// get configuration bean.
		ConfigurationBeanDefinition definition = context.getContext()
								.getBean(ConfigurationBeanDefinition.CONFIGURATION_BEAN_DEFINITION_NAME, ConfigurationBeanDefinition.class) ;
		
		Assert.assertNotNull("ConfigurationBeanDefinition has set.", definition) ;
		
		Map<String, String> sysconfig = definition.getSysConfigure() ;
		Assert.assertEquals("truthinet.com.vn", sysconfig.get(EmailDictionaryKey.EMAIL_DOMAIN)) ;
		Assert.assertEquals("25", sysconfig.get(EmailDictionaryKey.EMAIL_SMTP_SERVER_PORT)) ;
		Assert.assertEquals("mail.truthinet.com.vn", sysconfig.get(EmailDictionaryKey.EMAIL_SMTP_SERVER)) ;
	}
}
