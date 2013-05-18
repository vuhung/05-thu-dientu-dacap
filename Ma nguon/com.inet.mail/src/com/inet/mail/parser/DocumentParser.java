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
package com.inet.mail.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DocumentParser
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 10.01.2008
 * <pre>
 *  Based on DocumentParser of TSmich.
 * </pre>
 */
public abstract class DocumentParser {
	/**
	 * Mark quotings pattern.
	 */
	private static final Pattern MARK_QUOTINGS_PATTERN = Pattern.compile(
			"(^(&nbsp;)*&gt;[^\\n]*)|\\n((&nbsp;)*&gt;[^\\n]*)",
			Pattern.CASE_INSENSITIVE);

	/**
	 * Mark quotings data with dark-gray color.
	 * 
	 * @param input String - the given data to be mark.
	 * @return the marked data.
	 * @throws Exception if an error occurs during marking quote data.
	 */
	public static String markQuotings(String input) throws Exception {
		Matcher matcher = MARK_QUOTINGS_PATTERN.matcher(input);

		return matcher.replaceAll("\n<font class=\"quoting\">$1$3</font>");
	}
}
