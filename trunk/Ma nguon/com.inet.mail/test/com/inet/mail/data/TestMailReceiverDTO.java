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
 * TestMailReceiverDTO
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 */
public class TestMailReceiverDTO extends TestCase {	
	// create receiver DTO.
	private MailReceiverDTO receiverDTO ;
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// initialization truthinet
		MailReceiverObject truthinet = new MailReceiverObject("dungnguyen@truthinet.com") ;
		truthinet.setAccountPassword("hello") ;
		truthinet.setProtocol(MailReceiverProtocol.POP3) ;
		truthinet.setSecurity(MailSecurity.TLS) ;
		truthinet.setServerName("mail.truthinet.com") ;
		truthinet.setServerPort(110) ;
		
		MailReceiverObject truthinetvn = new MailReceiverObject("dungnguyen@truthinet.com.vn") ;
		truthinetvn.setAccountPassword("hello1") ;
		truthinetvn.setProtocol(MailReceiverProtocol.POP3) ;
		truthinetvn.setSecurity(MailSecurity.NONE) ;
		truthinetvn.setServerName("mail.truthinet.com.vn") ;
		truthinetvn.setServerPort(995) ;
		
		// create mail receiver DTO.
		this.receiverDTO = new MailReceiverDTO() ;
		this.receiverDTO.addAccount(truthinet) ;
		this.receiverDTO.addAccount(truthinetvn) ;		
	}
	
	/**
	 * test account.
	 */
	public void testAccount(){		
		// validate the content.
		assertEquals(2, this.receiverDTO.getAccounts().size()) ;
		assertEquals("hello", this.receiverDTO.getAccount("dungnguyen@truthinet.com").getAccountPassword()) ;
	}
	
	/**
	 * test convert from instance.
	 */
	public void testConvertFrom(){
		// convert the object to data.
		byte[] data = this.receiverDTO.getData() ;
		
		// convert to mail receiver DTO again.
		MailReceiverDTO object = MailReceiverDTO.convertFrom(data) ;
		assertNotNull(object);
		
		// validate the content.
		assertEquals(2, object.getAccounts().size()) ;
		assertEquals("hello", object.getAccount("dungnguyen@truthinet.com").getAccountPassword()) ;		
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		this.receiverDTO = null ;
	}
}
