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
package com.inet.mail.conf.beans;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.inet.mail.data.EmailDictionaryKey;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * ConfigurationBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ConfigurationBeanDefinition.java 2008-12-10 10:30:38Z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization ConfigurationBeanDefinition class.
 * </pre>
 */
public class ConfigurationBeanDefinition extends AbstractBeanDefinition{
	//~ Static fields =========================================================
	/* configuration bean definition name. */
	public static final String CONFIGURATION_BEAN_DEFINITION_NAME = ConfigurationBeanDefinition.class.getName() ;

	/* context holder. */
	protected static ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>();
	//~ Instance fields =======================================================
	/* configuration setting. */
	protected Properties configuration ;

	/* system configure */
	protected static Map<String, String> configure ;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return CONFIGURATION_BEAN_DEFINITION_NAME;
	}
	
	/**
	 * @return the system configuration.
	 */
	public Properties getConfiguration() {
		return configuration;
	}
	
	/**
	 * Set the system configuration.
	 * 
	 * @param configuration the system configuration.
	 */
	public void setConfiguration(Properties configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Return System configuration.
	 */
	public Map<String, String> getSysConfigure(){
		if(contextHolder.get() == null){
			synchronized(this){
				if(configure == null){
					configure = new Hashtable<String, String>() ;
					// put default value.
					configure.put(EmailDictionaryKey.EMAIL_DOMAIN, configuration.getProperty(EmailDictionaryKey.EMAIL_DOMAIN, "localdomain")) ;
					configure.put(EmailDictionaryKey.EMAIL_SERVER, configuration.getProperty(EmailDictionaryKey.EMAIL_SERVER, "localhost")) ;
					configure.put(EmailDictionaryKey.EMAIL_SERVER_PORT, configuration.getProperty(EmailDictionaryKey.EMAIL_SERVER_PORT, "110")) ;
					configure.put(EmailDictionaryKey.EMAIL_SERVER_SECURITY_DEFAULT, configuration.getProperty(EmailDictionaryKey.EMAIL_SERVER_SECURITY_DEFAULT, "NONE")) ;
					configure.put(EmailDictionaryKey.EMAIL_SERVER_TYPE_DEFAULT, configuration.getProperty(EmailDictionaryKey.EMAIL_SERVER_TYPE_DEFAULT, "POP3")) ;
					configure.put(EmailDictionaryKey.EMAIL_SMTP_SERVER, configuration.getProperty(EmailDictionaryKey.EMAIL_SMTP_SERVER, "localhost")) ;
					configure.put(EmailDictionaryKey.EMAIL_SMTP_SERVER_PORT, configuration.getProperty(EmailDictionaryKey.EMAIL_SMTP_SERVER_PORT, "25")) ;
					configure.put(EmailDictionaryKey.EMAIL_SMTP_SERVER_SECURITY, configuration.getProperty(EmailDictionaryKey.EMAIL_SMTP_SERVER_SECURITY, "NONE")) ;
					
					// create system configuration using unmodified map.
					configure = Collections.unmodifiableMap(configure) ;
				}
				
				// setting completed.
				contextHolder.set(Boolean.TRUE) ;
			}
		}
		
		return configure ;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder() ;
		
		builder.append(this.getClass().getName()).append("[")
			   .append("Name: ").append(CONFIGURATION_BEAN_DEFINITION_NAME)
			   .append("]") ;
		
		return builder.toString();
	}
}
