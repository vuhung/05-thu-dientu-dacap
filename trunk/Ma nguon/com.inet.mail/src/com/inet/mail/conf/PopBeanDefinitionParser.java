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

import com.inet.mail.conf.beans.PopBeanDefinition;
import com.inet.mail.data.MailSecurity;
import com.inet.web.parsing.AbstractBeanDefinitionParser;
import com.inet.web.parsing.ParserContext;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * PopBeanDefinitionParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PopBeanDefinitionParser.java 2008-12-07 23:20:28Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization PopBeanDefinitionParser class.
 * </pre>
 */
public class PopBeanDefinitionParser extends AbstractBeanDefinitionParser {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	/* host name attribute. */
	private static final String HOST_ATTR = "host" ;
	
	/* port attribute. */
	private static final String PORT_ATTR = "port" ;
	
	/* security attribute. */
	private static final String SECURITY_ATTR = "security" ;
	
	/* user name attribute. */
	private static final String USERNAME_ATTR = "username" ;
	
	/* password attribute. */
	private static final String PASSWORD_ATTR = "password" ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		// create PopBeanDefinition instance.
		PopBeanDefinition definition = new PopBeanDefinition() ;
		
		definition.setHost(getAttribute(element, HOST_ATTR, "localhost")) ;
		definition.setPort(getIntAttribute(element, PORT_ATTR, 110)) ;
		
		// set security.
		MailSecurity security = MailSecurity.NONE ;
		try{
			String secure = getAttribute(element, SECURITY_ATTR, "NONE") ;
			security = MailSecurity.valueOf(secure.toUpperCase()) ;
		}catch(Exception ex){}
		definition.setSecurity(security) ;
		
		definition.setUsername(getAttribute(element, USERNAME_ATTR, null)) ;
		definition.setPassword(getAttribute(element, PASSWORD_ATTR, null)) ;
		
		// show bean definition.
		if(logger.isDebugEnabled()) logger.debug("Bean definition: [" + definition + "].") ;
		
		return definition;
	}

}
