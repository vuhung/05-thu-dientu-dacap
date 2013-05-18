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

import java.net.InetAddress;

/**
 * InetAddressResolver
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: InetAddressResolver.java 2009-01-08 12:06:42z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization InetAddressResolver class.
 * </pre>
 */
public interface InetAddressResolver {
	/**
	 * Finds {@link InetAddress} from the given host name.
	 * 
	 * @param host the given host name.
	 * @return the {@link InetAddress} instance.
	 * 
	 * @throws SpamException when error during finding the address from host.
	 */
	InetAddress findByHost(String host) throws SpamException ;
	
	/**
	 * Finds all valid {@link InetAddress} from the given host name.
	 * 
	 * @param host the given host name.
	 * @return the list of valid {@link InetAddress} instances.
	 * 
	 * @throws SpamException when error occurs during finding addresses from host.
	 */
	InetAddress[] findAllByHost(String host) throws SpamException ;
}
