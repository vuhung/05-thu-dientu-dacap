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
package com.inet.mail.spam.parser;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.inet.base.service.StringService;
import com.inet.mail.spam.SpamParser;

/**
 * AbstractSpamParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractSpamParser.java 2009-01-10 21:32:23z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization AbstractSpamParser class.
 * </pre>
 */
public abstract class AbstractSpamParser extends AbstractXMLElementParser 
	implements SpamParser {
	//~ Instance fields =======================================================
	/* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	//~ Helper methods ========================================================
	/**
	 * Parse the class from the given {@link Element}, class element name and class attribute.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param name the given element name.
	 * @param attr the given class attribute.
	 * 
	 * @return the class instance or null.
	 */
	@SuppressWarnings({"unchecked"})
	protected <T> T parseClass(Element element, String name, String attr){
		if(element == null ||  !StringService.hasLength(name) || !StringService.hasLength(attr)){
			return null ;
		}
		
		// get class element.
		Element clazzElement = getElement(element, name) ;
		if(clazzElement == null) return null ;
		
		// get class attribute.
		Class<T> clazz = (Class<T>)getClass(clazzElement, attr) ;
		if(clazz == null) return null ;
		
		// create instance.
		try {
			return clazz.newInstance() ;
		} catch (InstantiationException iex) {
			logger.warn("could not initialization instance, message: [" + iex.getMessage() + "]") ;
		} catch (IllegalAccessException iex) {
			logger.warn("could not access default constructor , message: [" + iex.getMessage() + "]") ;
		}
		
		return null ;
	}
}
