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
package com.inet.mail.data;

import junit.framework.TestCase;

/**
 * TestMailSignature
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 */
public class TestMailSignature extends TestCase {
	/**
	 * signature name.
	 */
	private static final String SIGNATURE_NAME = "test" ;
	
	/**
	 * signature data.
	 */
	private static final String SIGNATURE_DATA = "<div><font='hello' size='12pt'>Hello</font></div>" ;
	
	/**
	 * test signature.
	 */
	public void testSetSignature(){
		// create signature.
		MailSignature signature = new MailSignature("dungnguyen@truthinet.com") ;
		
		// set signature.
		signature.setSignature(TestMailSignature.SIGNATURE_NAME, TestMailSignature.SIGNATURE_DATA) ;
			
		// print data.
		assertEquals(TestMailSignature.SIGNATURE_DATA, signature.getSignature(TestMailSignature.SIGNATURE_NAME)) ;
	}
	
	/**
	 * Test convert from.
	 */
	public void testCovertFrom(){
		// create signature.
		MailSignature signature = new MailSignature("dungnguyen@truthinet.com") ;
		
		// set signature.
		signature.setSignature(TestMailSignature.SIGNATURE_NAME, TestMailSignature.SIGNATURE_DATA) ;

		// get signature data.
		byte[] data = signature.getData() ;
		
		// get signature from data.
		MailSignature other = MailSignature.convertFrom(data) ;
		
		// print data.
		assertEquals(TestMailSignature.SIGNATURE_DATA, other.getSignature(TestMailSignature.SIGNATURE_NAME)) ;		
	}
}
