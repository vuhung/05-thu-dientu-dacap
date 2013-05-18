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
package com.inet.web.service.mail.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.inet.web.service.conf.Configuration;

/**
 * ConfigurationService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public abstract class ConfigurationService {
	private static final String CONFIG_FILE = "/META-INF/mailconfig.xml";
	private static final String CONFIG_BEAN = "configuration";
	private static Configuration configuration;
	
	/**
	 * get the configuration information
	 * 
	 * @return Configuration - the configuration
	 */
	public static Configuration getConfiguration() {
		if(configuration == null) {
			ApplicationContext beanFactory = new ClassPathXmlApplicationContext(CONFIG_FILE);
			configuration = (Configuration) beanFactory.getBean(CONFIG_BEAN);
		}
		return configuration;
	}
	
	/**
	 * get the max result for searching
	 * 
	 * @return int - the default limited data.
	 */
	public static int getDefaultLimit() {
		return (ConfigurationService.getConfiguration() != null ?
				ConfigurationService.getConfiguration().getMaxSearchResult():
				10);
	}

}
