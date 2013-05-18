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

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.inet.mail.spam.DNSResolver;
import com.inet.mail.spam.DNSType;

/**
 * DNSResolverSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: DNSResolverSupport.java 2009-01-08 12:39:48z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization DNSResolverSupport class.
 * </pre>
 */
public class DNSResolverSupport implements DNSResolver {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.DNSResolver#lookup(java.lang.String, com.inet.mail.spam.DNSType)
	 */
	public Attributes lookup(String host, DNSType type) throws DNSResolverException {
		try{
			// create directory instance and retrieved the expected value.
			DirContext context = new InitialDirContext() ;
			
			// the expected value.
			return context.getAttributes("dns:/" + host, new String[]{ type.toString() });
		}catch(NamingException nex){
			throw new DNSResolverException(
						"Could not lookup the DNS value, message: [" + nex.getMessage() + "]", 
						nex
					) ;
		}
	}
	
	/**
	 * Return object representation as String.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}
}
