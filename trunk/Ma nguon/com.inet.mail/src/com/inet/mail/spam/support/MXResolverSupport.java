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

import java.util.regex.Pattern;

import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;

import com.inet.base.service.StringService;
import com.inet.mail.spam.DNSResolver;
import com.inet.mail.spam.DNSType;
import com.inet.mail.spam.MXResolver;

/**
 * MXResolverSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MXResolverSupport.java 2009-01-09 14:52:30z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization MXResolverSupport class.
 * </pre>
 */
public class MXResolverSupport implements MXResolver {
	//~ Static fields =========================================================
	/* logger class. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	/* IP pattern. */
	private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}") ;
	
	//~ Instance fields =======================================================
	/* DNS resolver. */
	private final DNSResolver resolver ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MXResolverSupport</tt> instance from the given
	 * {@link DNSResolver} instance.
	 * 
	 * @param resolver the given {@link DNSResolver} instance.
	 */
	public MXResolverSupport(DNSResolver resolver){
		this.resolver = resolver ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.MXResolver#hasMXRecord(java.lang.String)
	 */
	public boolean hasMXRecord(String host) {
		// check the host.
		if(IP_PATTERN.matcher(host).find()) return false ;
		
		String[] domains = getDomain(host) ;
		if(domains.length == 0) return false ;
		
		// check domain.
		for(String domain : domains){
			try{
				Attributes attributes = resolver.lookup(domain, DNSType.MX) ;
				if(attributes != null && attributes.get(DNSType.MX.toString()) != null) return true ;
			}catch(DNSResolverException drex){
				logger.warn("could not resolve MX record at domain: [" + domain + "], message: [" + drex.getMessage() + "]") ;
			}
		}
		
		return false;
	}
	
	//~ Helper methods ========================================================
	/**
	 * Returns the domain from the given host.
	 * 
	 * @param host the given host to extract domain name.
	 */
	private String[] getDomain(String host){
		if(!StringService.hasLength(host)) return new String[0];
		if(host.indexOf('.') > 0){
			return new String[]{
				host.substring(host.indexOf('.') + 1),
				host
			};
		}else{
			return new String[]{host} ;
		}
	}
}
