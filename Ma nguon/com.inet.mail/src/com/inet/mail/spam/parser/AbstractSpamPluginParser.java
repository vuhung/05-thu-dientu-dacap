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

import com.inet.mail.spam.SpamPluginParser;

/**
 * AbstractSpamPlguinParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractSpamPluginParser.java 2009-01-10 16:26:49z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization AbstractSpamPluginParser class.
 * </pre>
 */
public abstract class AbstractSpamPluginParser extends AbstractXMLElementParser
		implements SpamPluginParser {
	//~ Static fields =========================================================
	/* absolute spam plugin. */
	protected static final String ABSOLUTE_SPAM_PLUGIN = "absolute" ;
	
	//~ Instance fields =======================================================
	/* class logger. */
	protected final Logger logger = Logger.getLogger(getClass());
	
	//~ Methods ===============================================================
	/**
	 * Parses absolute value.
	 * 
	 * @param element the given {@link Element} instance.
	 * @return the SPAM plugin absolute flag.
	 */
	protected boolean parseAbsolute(Element element){
		if(element == null) return false ;
		return getBoolAttribute(element, ABSOLUTE_SPAM_PLUGIN, false) ;
	}
}
