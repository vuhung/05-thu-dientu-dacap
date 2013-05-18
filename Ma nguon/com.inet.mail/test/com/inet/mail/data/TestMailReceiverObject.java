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
 * TestMailReceiverObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 */
public class TestMailReceiverObject extends TestCase {
	// account name.
	private static final String ACCOUNT_NAME = "dungnguyen@truthinet.com.vn" ;
	// server name.
	private static final String SERVER_NAME = "mail.truthinet.com.vn" ;
	// server protocol.
	private static final MailReceiverProtocol PROTOCOL = MailReceiverProtocol.POP3 ;
	// server security.
	private static final MailSecurity SECURITY = MailSecurity.SSL ;
	// account password.
	private static final String ACCOUNT_PASSWORD = "hello" ;
	// server port.
	private static final int SERVER_PORT = 112 ;
	
	/**
	 * Test account name.
	 */
	public void testAccountName() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set account name.
		object.setAccountName(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// check the account name.
		assertEquals(TestMailReceiverObject.ACCOUNT_NAME, object.getAccountName()) ;
	}
	
	/**
	 * Test server name.
	 */
	public void testServerName() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set server name.
		object.setServerName(TestMailReceiverObject.SERVER_NAME) ;
		
		// check the server name value.
		assertEquals(TestMailReceiverObject.SERVER_NAME, object.getServerName()) ;
	}	
	
	/**
	 * Test server port.
	 */
	public void testServerPort() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set server port.
		object.setServerPort(TestMailReceiverObject.SERVER_PORT) ;
		
		// check the server port value.
		assertEquals(TestMailReceiverObject.SERVER_PORT, object.getServerPort()) ;
	}
	
	/**
	 * Test server port.
	 */
	public void testServerSecurity() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set server security.
		object.setSecurity(TestMailReceiverObject.SECURITY) ;
		
		// check the security value.
		assertEquals(TestMailReceiverObject.SECURITY, object.getSecurity()) ;
	}	
	
	/**
	 * Test server protocol.
	 */
	public void testProtocol() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set server protocol.
		object.setProtocol(TestMailReceiverObject.PROTOCOL) ;
		
		// check the server.
		assertEquals(TestMailReceiverObject.PROTOCOL, object.getProtocol()) ;		
	}
	
	/**
	 * Test server protocol.
	 */
	public void testPassowrd() throws Exception{
		// create mail receiver object.
		MailReceiverObject object = new MailReceiverObject(TestMailReceiverObject.ACCOUNT_NAME) ;
		
		// set server port.
		object.setAccountPassword(TestMailReceiverObject.ACCOUNT_PASSWORD) ;
		
		// check the account name.
		assertEquals(TestMailReceiverObject.ACCOUNT_PASSWORD, object.getAccountPassword()) ;
	}
}
