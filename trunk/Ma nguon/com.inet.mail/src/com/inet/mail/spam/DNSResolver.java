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

import javax.naming.directory.Attributes;

/**
 * DNSResolver
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: DNSResolver.java 2009-01-08 12:18:26z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization DNSResolver class.
 *  
 *  <p>
 *  Support the utility that acting the DNS role.
 *  </p>
 * </pre>
 */
public interface DNSResolver {
	/**
	 * Lookup the expected value from the given host name and 
	 * {@link DNSType} value.
	 * 
	 * @param host the given host name.
	 * @param type the {@link DNSType} value.
	 * 
	 * @return the expected value as {@link Attributes} instance or empty {@link Attributes}.
	 * 
	 * @throws SpamException when error occurs during looking up the value.
	 */
	Attributes lookup(String host, DNSType type) throws SpamException ;
}
