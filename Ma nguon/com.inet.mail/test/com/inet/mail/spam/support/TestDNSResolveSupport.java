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

import javax.naming.directory.Attributes;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.mail.spam.DNSResolver;
import com.inet.mail.spam.DNSType;

/**
 * TestDNSResolveSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestDNSResolveSupport.java Jan 9, 2009 nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization TestDNSResolveSupport class.
 * </pre>
 */
public class TestDNSResolveSupport {
	//~ Static fields =========================================================
	/* yahoo domain. */
	private static final String YAHOO_DOMAIN = "yahoo.com" ;
	
	/* DOM domain. */
	private static final String DOM_DOMAIN = "dom" ;
	
	//~ Methods ===============================================================
	/**
	 * Test DNSResolve feature.
	 */
	@Test(threadPoolSize=1, invocationCount=1)
	public void testDNSResolve(){
		DNSResolver resolver = new DNSResolverSupport() ;
		
		Attributes attributes = null;
		try{
			// get MX attribute.
			attributes = resolver.lookup(YAHOO_DOMAIN, DNSType.MX) ;
			
		}catch(DNSResolverException dex){}
		Assert.assertNotNull(attributes.get(DNSType.MX.toString()));
		
		try{
			attributes = resolver.lookup(DOM_DOMAIN, DNSType.MX) ;
		}catch(DNSResolverException dex){
			attributes = null ;
		}
		Assert.assertNull(attributes) ;
	}
}
