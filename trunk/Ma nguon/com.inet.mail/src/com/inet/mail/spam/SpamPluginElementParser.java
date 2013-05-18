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
package com.inet.mail.spam;

import org.w3c.dom.Element;

/**
 * SpamPluginElementParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginElementParser.java 2009-01-10 10:49:02z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamPluginElementParser class.
 * </pre>
 */
public interface SpamPluginElementParser {
	/**
	 * Parses the given {@link Element} and return the {@link SpamPluginElement} to
	 * the sub system.
	 * 
	 * @param element the given {@link Element} to parse data.
	 * @return the {@link SpamPluginElement} instance.
	 * 
	 * @throws SpamException when error occurs during parsing {@link Element} instance.
	 */
	SpamPluginElement parse(Element element) throws SpamException ;
}
