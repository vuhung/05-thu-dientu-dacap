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
package com.inet.mail.spam.parser.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Element;

import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPlugin;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.SpamPluginElementParser;
import com.inet.mail.spam.parser.AbstractSpamPluginParser;
import com.inet.mail.spam.parser.SpamPluginParserException;
import com.inet.mail.spam.plugin.AbstractSpamPlugin;
import com.inet.mail.spam.plugin.support.SpamPluginException;

/**
 * SpamPluginParserSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginParserSupport.java 2009-01-10 16:36:40z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamPluginParserSupport class.
 * </pre>
 */
public class SpamPluginParserSupport extends AbstractSpamPluginParser {
	//~ Static fields =========================================================
	/* element parser. */
	private static final String SPAM_PLUGIN_ELEMENT_PARSER = "element-parser" ;
	
	/* class attribute. */
	private static final String CLASS_ATTRIBUTE = "class" ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginParser#parse(org.w3c.dom.Element)
	 */
	@SuppressWarnings({"unchecked"})
	public SpamPlugin parse(Element element) throws SpamException {
		// get spam plugin class.
		Class<AbstractSpamPlugin> clazz = (Class<AbstractSpamPlugin>)getClass(element, CLASS_ATTRIBUTE) ; 
		if(clazz == null) throw new SpamPluginParserException("could not get SpamPlugin class. ") ;
		
		// get SpamPluginElementParser class.
		SpamPluginElementParser elementParser = parsePluginElement(element) ;
		
		// get spam element parser.
		SpamPluginElement pluginElement = elementParser.parse(element) ;
		boolean absolute = parseAbsolute(element) ;
		
		// create SPAM plugin.
		try {
			Constructor<AbstractSpamPlugin> constructor = clazz.getConstructor(SpamPluginElement.class, boolean.class) ;
			constructor.setAccessible(true) ;
			
			// create spam plugin from constructor.
			return constructor.newInstance(pluginElement, absolute) ;
		} catch (SecurityException sex) {
			throw new SpamPluginException("could not access SpamPlugin constructor", sex) ;
		} catch (NoSuchMethodException nsex) {
			throw new SpamPluginException("no such expected SpamPlugin constructor", nsex) ;
		} catch (IllegalArgumentException iaex) {
			throw new SpamPluginException("the given argument is invalidate", iaex) ;
		} catch (InstantiationException iex) {
			throw new SpamPluginException("could not create SpamPlugin instance", iex) ;
		} catch (IllegalAccessException iex) {
			throw new SpamPluginException("could not access SpamPlugin constructor", iex) ;
		} catch (InvocationTargetException iex) {
			throw new SpamPluginException("could not invocation instance", iex) ;
		}
	}
	
	//~ Helper methods ========================================================
	/**
	 * Parses {@link SpamPluginElementParser} from the given {@link Element} instance.
	 * 
	 * @param element the given {@link Element} instance.
	 * @return the given {@link SpamPluginElementParser} instance.
	 * 
	 * @throws SpamPluginParserException when error occurs during getting 
	 * {@link SpamPluginElementParser} instance.
	 */
	@SuppressWarnings({"unchecked"})
	protected SpamPluginElementParser parsePluginElement(Element element) 
		throws SpamPluginParserException{
		// get element parser.
		Element parserElement = getElement(element, SPAM_PLUGIN_ELEMENT_PARSER) ;
		if(parserElement == null) throw new SpamPluginParserException("The element parser must be existed.") ;
		
		// get SpamPluginElementParser class.
		Class<SpamPluginElementParser> parser = (Class<SpamPluginElementParser>)getClass(parserElement, CLASS_ATTRIBUTE) ;
		if(parser == null) throw new SpamPluginParserException("Could not create SpamPluginElementParser class instance.") ;
		
		try {
			// create instance.
			return parser.newInstance() ;
		} catch (InstantiationException iex) {
			throw new SpamPluginParserException("could not create SpamPluginElementParser instance", iex) ;
		} catch (IllegalAccessException iex) {
			throw new SpamPluginParserException("could not access SpamPluginElementParser constructor", iex) ;
		}
	}
}
