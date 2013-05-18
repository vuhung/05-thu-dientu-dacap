/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.mail.config;

import com.inet.mail.AbstractConfigureTestMail;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.data.MailReceiverProtocol;
import com.inet.mail.data.MailSecurity;

public class TestCreateConfigEmail extends AbstractConfigureTestMail {
	
	public void testAddConfig() throws Exception{
		MailReceiverObject receiverObject = new MailReceiverObject("");
		receiverObject.setAccountName("tntan@truthinet.com.vn");
		receiverObject.setAccountPassword("123456");
		receiverObject.setActive(true);
		receiverObject.setDescription("alo alo");
		receiverObject.setEmailAddress("tntan@truthinet.com.vn");
		receiverObject.setProtocol(MailReceiverProtocol.POP3);
		receiverObject.setSecurity(MailSecurity.NONE);
		receiverObject.setServerName("mail.truthinet.com.vn");
		receiverObject.setServerPort(110);
		receiverObject.setSMTPAccountName("abc@truthinet.com.vn");
		receiverObject.setSMTPAccountPassword("tntan");
		receiverObject.setSMTPSecurity(MailSecurity.NONE);
		receiverObject.setSMTPServerName("mail.truthinet.com.vn");
		receiverObject.setSMTPServerPort(25);	
		
		this.bean.createConfig("123456789", "Truong Ngoc Tan", receiverObject);
	}
}
