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
package com.inet.mail.filter;

import com.inet.mail.persistence.MailHeader;

/**
 * Filter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization Filter class.
 * </pre>
 */
public interface Filter {
	/**
	 * Evaluate the mail filter from the given mail header.
	 * 
	 * @param header MailHeader - the given mail header.
	 * @return <code>true</code> when given mail header matching the 
	 * criteria, otherwise <code>false</code>.
	 */
	boolean evaluate(MailHeader header) ;
}
