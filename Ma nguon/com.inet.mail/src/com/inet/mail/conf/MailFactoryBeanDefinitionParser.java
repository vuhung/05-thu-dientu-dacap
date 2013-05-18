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

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.inet.mail.conf.beans.MailFactoryBeanDefinition;
import com.inet.mail.data.MailFactoryType;
import com.inet.mail.spam.beans.parser.SpamMailBeanDefinitionParser;
import com.inet.web.parsing.AbstractBeanDefinitionParser;
import com.inet.web.parsing.ParserContext;
import com.inet.web.registry.AbstractBeanDefinition;
import com.inet.web.xml.XmlService;

/**
 * MailFactoryBeanDefinitionParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailFactoryBeanDefinitionParser.java Dec 7, 2008 nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization MailFactoryBeanDefinitionParser class.
 * </pre>
 */
public class MailFactoryBeanDefinitionParser extends
		AbstractBeanDefinitionParser {
	//~ Static fields =========================================================
	/* logger class. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	/* factory attribute. */
	private static final String FACTORY_ATTR = "factory" ;
	
	/* expire time element. */
	private static final String CACHE_EXPIRE = "cache-expire" ;
	
	/* SMTP element name. */
	private static final String SMTP_ELEMENT_NAME = "smtp" ;
	
	/* POP element name. */
	private static final String POP_ELEMENT_NAME = "pop" ;
	
	/* Configuration element name. */
	private static final String CONFIG_ELEMENT_NAME = "config" ;
	
	/* spam configuration. */
	private static final String SPAM_ELEMENT_NAME = "spam" ;
	
	/* default factory name. */
	private static final String DEFAULT_FACTORY = "SUN" ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		// mail factory bean definition.
		MailFactoryBeanDefinition definition = new MailFactoryBeanDefinition() ;
		
		String factoryType = getAttribute(element, FACTORY_ATTR, DEFAULT_FACTORY) ;
		try{
			factoryType = factoryType.toUpperCase() ;
			definition.setFactory(MailFactoryType.valueOf(factoryType)) ;
		}catch(Exception ex){}
		
		// get expire time element.
		Element cacheExpire = XmlService.getSubElement(element, CACHE_EXPIRE) ;
		if(cacheExpire != null){
			new CacheExpireBeanDefinitionParser().parse(cacheExpire, context) ;
		}
		
		// get SMTP element.
		Element smtpElement = XmlService.getSubElement(element, SMTP_ELEMENT_NAME) ;
		if(smtpElement != null){
			new SmtpBeanDefinitionParser().parse(smtpElement, context) ;
		}
		
		// get Pop element.
		Element popElement = XmlService.getSubElement(element, POP_ELEMENT_NAME) ;
		if(popElement != null){
			new PopBeanDefinitionParser().parse(popElement, context) ;
		}
		
		// get configuration element name.
		Element configElement = XmlService.getSubElement(element, CONFIG_ELEMENT_NAME) ;
		if(configElement != null){
			new ConfigurationBeanDefinitionParser().parse(configElement, context) ;
		}
		
		// get spam configuration element.
		Element spamElement = XmlService.getSubElement(element, SPAM_ELEMENT_NAME) ;
		if(spamElement != null){
			new SpamMailBeanDefinitionParser().parse(spamElement, context) ;
		}
		
		// show information.
		if(logger.isDebugEnabled()) logger.debug("Bean definition: [" + definition + "].") ;
		
		return definition;
	}

}
