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
package com.inet.mail;

import java.util.List;

import org.testng.annotations.Test;

import com.inet.mail.conf.beans.PopBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.UIDStore;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.parser.MessageHeader;

/**
 * TestMailFactory
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 2, 2008
 * <pre>
 *  Initialization TestMailFactory class.
 * </pre>
 */
public class TestMailFactory{
	/**
	 * Test connect to server.
	 * 
	 * @throws Exception if an error occurs during connecting to server.
	 */
	@Test(threadPoolSize=1, invocationCount=1)
	public void testConnect() throws Exception{
		// create connect to server from the default setting.
		AbstractMailFactory mailFactory = MailConfigureFactory.createFactory() ;
		
		// connect to mail server.
		mailFactory.connect() ;
		
		// close connection.
		mailFactory.close() ;
	}
	
	/**
	 * Test send mail.
	 * 
	 * @throws Exception if an error occurs during sending mail.
	 */
	@Test(threadPoolSize=1, invocationCount=1)
	public void testSendMail() throws Exception{		
		// create connect to server from the default setting.
		AbstractMailFactory mailFactory = MailConfigureFactory.createFactory() ;
		
		// set message.
		mailFactory.connect() ;
		
		// create composer.
		IMessageComposer composer = mailFactory.createMessage() ;
		
		// set content.
		composer.setFrom(new Address("Nguyen Van Dung","dungnguyen@truthinet.com.vn")) ;
		composer.addTo(new Address("Truong Ngoc Tan","tntan@truthinet.com.vn").getFullAddress()) ;
		composer.setSubject("Test") ;
		composer.setBody("Hello") ;
		
		//send message.
		mailFactory.send(composer.compose()) ;
		
		// close the connection.
		mailFactory.close() ;
	}
	
	/**
	 * Test retrieved mail.
	 * 
	 * @throws Exception if an error occurs during getting mail.
	 */
	@Test(threadPoolSize=1, invocationCount=1)
	public void testPop3() throws Exception{
		 // create connect to server from the default setting.
		AbstractMailFactory mailFactory = MailConfigureFactory.createFactory() ;
		
		// get pop3 configuration.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		if(context == null || context.getContext() == null) return ;
		
		// get pop3 configuration.
		PopBeanDefinition popDefinition = context.getContext().getBean(
						PopBeanDefinition.POP_BEAN_DEFINITION_NAME, 
						PopBeanDefinition.class
					) ;
		
		// connect to pop3.
		mailFactory.connect(
				MailProtocol.POP3, 
				popDefinition.getSecurity(), 
				popDefinition.getHost(), 
				popDefinition.getPort(), 
				popDefinition.getUsername(), 
				popDefinition.getPassword()
			) ;
		
		// create store.
		UIDStore store = new UIDStore() ;
		UIDStore newStore = (UIDStore)store.clone() ;
		
		// get message.
		int count = mailFactory.count(null, newStore);
		
		// show information.
		System.out.println("Thread: [" + Thread.currentThread().getId() + "] new message: [" + count + "]");

		for(int index = 0; index < count; index++){
			// fetch message.
			List<IMessageComposer> composers = mailFactory.fetch(1, store, false) ;
			
			// get the composer.
			IMessageComposer composer = composers.get(0) ;
			
			// message header.
			MessageHeader header = composer.getHeader() ;
			if(header.isSpam()){
				System.err.println("SPAM: [" + header + "]");
			}else{
				System.out.println("NOT SPAM: [" + header + "]");
			}
		}
		
		// close the connection.
		mailFactory.close() ;
	}
}
