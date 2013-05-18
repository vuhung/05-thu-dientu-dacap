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
package com.inet.mail.filter.object;

import com.inet.base.service.DateService;
import com.inet.base.service.StringService;
import com.inet.mail.persistence.MailHeader;

/**
 * DateReceivedFilterObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization DateReceivedFilterObject class.
 * </pre>
 */
public class DateReceivedFilterObject extends AbstractFilterObject {
	/**
	 * @see com.inet.mail.filter.object.AbstractFilterObject#getData(com.inet.mail.persistence.MailHeader)
	 */
	public String getData(MailHeader header) {
		/**
		 * Return the date when mail received.
		 */
		return (header != null ?
					DateService.format(header.getReceived(), DateService.VIETNAM_DATE_PATTERN) :
					StringService.EMPTY_STRING
				);
	}
}
