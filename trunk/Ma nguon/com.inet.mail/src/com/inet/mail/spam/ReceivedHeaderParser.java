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


/**
 * ReceivedHeaderParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ReceivedHeaderParser.java 2009-01-08 10:56:21z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization ReceivedHeaderParser class.
 * </pre>
 */
public interface ReceivedHeaderParser {
	/**
	 * Parsers the header from the given header value.
	 * <p>
	 *  The header format:
	 * </p>
	 * <pre>
	 * received    =  &quot;Received&quot;    &quot;:&quot;          ; one per relay
	 *                 [&quot;from&quot; domain]           ; sending host
	 *                 [&quot;by&quot;   domain]           ; receiving host
	 *                 [&quot;via&quot;  atom]             ; physical path
	 *                *(&quot;with&quot; atom)             ; link/mail protocol
	 *                 [&quot;id&quot;   msg-id]           ; receiver msg id
	 *                 [&quot;for&quot;  addr-spec]        ; initial form
	 * </pre>
	 * 
	 * @param resolver the given {@link InetAddressResolver} instance.
	 * 
	 * @param header the given header value.
	 * @return the {@link ReceivedHeader} instance.
	 * 
	 * @throws SpamException when error occurs during parsing the header.
	 */
	ReceivedHeader parse(String header, InetAddressResolver resolver) throws SpamException ;
}
