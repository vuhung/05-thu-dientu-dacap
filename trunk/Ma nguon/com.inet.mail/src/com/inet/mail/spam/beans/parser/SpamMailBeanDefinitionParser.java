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
package com.inet.mail.spam.beans.parser;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.inet.mail.spam.beans.SpamMailBeanDefinition;
import com.inet.web.parsing.AbstractBeanDefinitionParser;
import com.inet.web.parsing.ParserContext;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * SpamMailBeanDefinitionParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamMailBeanDefinitionParser.java 2009-01-10 22:20:51z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamMailBeanDefinitionParser class.
 * </pre>
 */
public class SpamMailBeanDefinitionParser extends AbstractBeanDefinitionParser {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(SpamMailBeanDefinitionParser.class);	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.parsing.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element, com.inet.web.parsing.ParserContext)
	 */
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		// create bean definition parser.
		SpamMailBeanDefinition beanDefinition = new SpamMailBeanDefinition() ;
		
		// setting bean definition.
		beanDefinition.setElement(element) ;
		
		if(logger.isDebugEnabled()) logger.debug("Bean definition: [" + beanDefinition + "]") ;
		
		return beanDefinition;
	}

}
