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

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.inet.mail.conf.beans.ConfigurationBeanDefinition;
import com.inet.web.parsing.AbstractBeanDefinitionParser;
import com.inet.web.parsing.ParserContext;
import com.inet.web.registry.AbstractBeanDefinition;
import com.inet.web.xml.XmlService;

/**
 * ConfigurationBeanDefinitionParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ConfigurationBeanDefinitionParser.java Dec 10, 2008 nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization ConfigurationBeanDefinitionParser class.
 * </pre>
 */
public class ConfigurationBeanDefinitionParser extends
		AbstractBeanDefinitionParser {
	//~ Static fields =========================================================
	/* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	/* configuration element name. */
	private static final String CONFIGURATION_ELEMENT_NAME = "configuration" ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		
		// create definition.
		ConfigurationBeanDefinition definition = new ConfigurationBeanDefinition() ;
		
		Properties properties = null ;
		try{
			properties = XmlService.readPropertiesContents(element, CONFIGURATION_ELEMENT_NAME) ;
		}catch(IOException ioex){
			logger.warn("could not load properties from the element: [" + CONFIGURATION_ELEMENT_NAME + "], message: [" + ioex.getMessage() + "].") ;
			properties = new Properties() ;
		}
		definition.setConfiguration(properties) ;
		
		if(logger.isDebugEnabled()) logger.debug("Bean definition: [" + definition + "].") ;
		
		return definition;
	}

}
