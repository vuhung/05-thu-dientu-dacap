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
package com.inet.mail.spam.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.inet.mail.spam.InetAddressResolver;
import com.inet.mail.spam.SpamException;

/**
 * InetAddressResolverSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: InetAddressResolverSupport.java 2009-01-08 14:49:42:z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization InetAddressResolverSupport class.
 * </pre>
 */
public class InetAddressResolverSupport implements InetAddressResolver {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.InetAddressResolver#findAllByHost(java.lang.String)
	 */
	public InetAddress[] findAllByHost(String host) throws SpamException {
		try{
			return InetAddress.getAllByName(host);
		}catch(UnknownHostException uhex){
			throw new InetAddressResolverException("could not resolve the host address, message: [" + uhex.getMessage() + "]", uhex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.InetAddressResolver#findByHost(java.lang.String)
	 */
	public InetAddress findByHost(String host) throws SpamException {
		try{
			return InetAddress.getByName(host);
		}catch(UnknownHostException uhex){
			throw new InetAddressResolverException("could not resolve host address, message: [" + uhex.getMessage() + "]", uhex) ;
		}
	}
	
	/**
	 * Return object representation as string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}
}
