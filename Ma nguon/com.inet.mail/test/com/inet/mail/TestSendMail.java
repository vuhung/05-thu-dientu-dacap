/*****************************************************************
   Copyright 2006 by Tung Luong (lqtung@truthinet.com.vn)

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
package com.inet.mail;

import java.util.HashMap;

import com.inet.base.ejb.ServiceLocator;
import com.inet.base.ejb.ServiceLocatorManager;
import com.inet.base.service.DigestService;
import com.inet.mail.data.DataMailDTO;

/**
 * TestSendMail.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 0.2i
 */
public class TestSendMail extends AbstractTestMail<MailDeliveryRemoteSL>{
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		ServiceLocator locator = ServiceLocatorManager.getInstance().getServiceLocator(this.getProperties());
		bean = locator.getRemoteBean("MailDeliverySLBean", MailDeliveryRemoteSL.class);
	}
	
	/**
	 * Send the email
	 * @throws Exception
	 */
	public void testSendEmail() throws Exception{
		//Sender info
		String[] sender = new String[2];		
		sender[0] = "inet.iwebos@gmail.com";
		sender[1] = "Bao Dai Doan Ket";
		
		//Title name
		String title = "Chuong trinh chung tay vi cong dong";
		
		//Receiver
		HashMap<String , String> listMail = new HashMap<String , String>();
		listMail.put("lqtungk@yahoo.com","Luong Quang Tung");
		DataMailDTO mailData= new DataMailDTO(sender,title,listMail);
		mailData.setBody("Hello 2");
		
		//send email 
		bean.delivery(mailData, false);
	}
	
	/**
	 * Generator password
	 * @throws Exception
	 */
	public void testGenPassword() throws Exception {
		System.out.println(DigestService.base64Encode(DigestService.utf8encode("iwebos123@456")));
		System.out.println(DigestService.base64Encode(DigestService.utf8encode("daidoanket")));
	}
}
