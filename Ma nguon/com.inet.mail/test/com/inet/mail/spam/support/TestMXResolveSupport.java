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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.mail.spam.MXResolver;

/**
 * TestMXResolveSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestMXResolveSupport.java Jan 9, 2009 nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization TestMXResolveSupport class.
 * </pre>
 */
public class TestMXResolveSupport {
	//~ Static fields =========================================================
	/* truthinet host. */
	private static final String TRUTHINET_HOST = "mail.truthinet.com.vn" ;
	
	/* daklak host. */
	private static final String DAKLAK_HOST = "mail.daklak.gov.vn" ;
	
	/* gmail host. */
	private static final String GMAIL_HOST = "gmail.com" ;

	/* gmail host. */
	private static final String DOM_HOST = "dom" ;
	
	//~ Test methods ==========================================================
	/**
	 * Test MX record.
	 */
	@Test(threadPoolSize=1, invocationCount=1)
	public void testMXRecord(){
		// create MXResolve.
		MXResolver resolver = new MXResolverSupport(new DNSResolverSupport()) ;
		
		Assert.assertEquals(resolver.hasMXRecord(TRUTHINET_HOST), true) ;
		Assert.assertEquals(resolver.hasMXRecord(DAKLAK_HOST), true) ;
		Assert.assertEquals(resolver.hasMXRecord(GMAIL_HOST), true) ;
		Assert.assertEquals(resolver.hasMXRecord(DOM_HOST), false) ;
	}
}
