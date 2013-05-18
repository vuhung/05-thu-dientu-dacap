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

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.inet.mail.conf.beans.CacheExpireBeanDefinition;
import com.inet.web.parsing.AbstractBeanDefinitionParser;
import com.inet.web.parsing.ParserContext;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * CacheExpireBeanDefinitionParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CacheExpireBeanDefinitionParser.java 2008-12-25 15:40:00z nguyen_dv $
 * 
 * Create date: Dec 25, 2008
 * <pre>
 *  Initialization CacheExpireBeanDefinitionParser class.
 * </pre>
 */
public class CacheExpireBeanDefinitionParser extends AbstractBeanDefinitionParser {
	//~ Static fields =========================================================
	/* class logger. */
	private static final Logger logger = Logger.getLogger(CacheExpireBeanDefinitionParser.class) ;
	
	/* expire content time. */
	private static final String EXPIRE_CONTENT = "content" ;
	
	/* time unit (d|h|m) */
	private static final String TIME_UNIT = "time-unit" ;
	
	/* day unit. */
	private static final String DAYS_UNIT = "d" ;
	
	/* minutes unit. */
	private static final String MINUTES_UNIT = "m" ;
	
	/* hours unit. */
	private static final String HOURS_UNIT = "h" ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.parsing.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element, com.inet.web.parsing.ParserContext)
	 */
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext context) {
		// hold cache expire configuration.
		CacheExpireBeanDefinition definition = new CacheExpireBeanDefinition() ;
		
		// set expire value.
		definition.setExpire(getIntAttribute(element, EXPIRE_CONTENT, 30)) ;
		
		// get expire time unit.
		String timeUnit = getAttribute(element, TIME_UNIT, DAYS_UNIT) ;
		
		// extract expire type.
		int[] expireType = new int[2] ;
		if(MINUTES_UNIT.equalsIgnoreCase(timeUnit)){
			expireType[0] = TimeUnit.MINUTES.ordinal() ;
			expireType[1] = Calendar.MINUTE ;
		}else if(HOURS_UNIT.equalsIgnoreCase(timeUnit)){
			expireType[0] = TimeUnit.HOURS.ordinal() ;
			expireType[1] = Calendar.HOUR_OF_DAY ;
		}else{
			expireType[0] = TimeUnit.DAYS.ordinal() ;
			expireType[1] = Calendar.DAY_OF_YEAR ;
		}
		
		// set expire type.
		definition.setExpireType(expireType) ;
		
		if(logger.isDebugEnabled()) logger.debug("Bean definition: [" + definition + "].") ;
		
		// expire configuration.
		return definition;
	}
}
