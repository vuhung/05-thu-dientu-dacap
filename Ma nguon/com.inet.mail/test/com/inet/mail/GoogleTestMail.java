/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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

import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import com.inet.mail.exception.MailException;
import com.sun.mail.pop3.POP3Folder;

/**
 * GoogleTestMail.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class GoogleTestMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		POP3Folder folder = null;
		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);

		// create new properties.
		Properties properties = new Properties();

		// get protocol.
		String protoSsl = "pop3";
		
		// set authentication class.
		//Security.setProperty("ssl.SocketFactory.provider",
		//		DataSSLSocketFactory.CLASSNAME);

		// set store properties value.
		// set protocol value.
		properties.setProperty("mail." + protoSsl + ".host", "pop.gmail.com");
		properties.setProperty("mail." + protoSsl + ".port", "995");
		//properties.setProperty("mail." + protoSsl + ".starttls.enable", "true");

		// initialization protocol user.
		properties.setProperty("mail." + protoSsl + ".user","chaolongbanhhoi@gmail.com");
		properties.setProperty("mail." + protoSsl + ".auth", "123456abc");

		// set secure data.
		//properties.setProperty("mail." + protoSsl + ".socketFactory.port","995");
		//properties.setProperty("mail." + protoSsl + ".socketFactory.fallback","false");
		//properties.setProperty("mail." + protoSsl + ".socketFactory.class","javax.net.ssl.SSLSocketFactory");

		properties.put("mail.store.protocol", "pop3");

		// connect to SMTP.
		Session pop3Session = Session.getInstance(properties);

		// get store information.
		try {
			Store store = pop3Session.getStore();
			store.connect();

			// get new message from any mail server
			folder = (POP3Folder) store.getFolder("INBOX");

			// we only load new messages header
			folder.open(Folder.READ_ONLY);

			// count this message
			int totalMessages = folder.getMessageCount();

			// do not have any thing new
			if (totalMessages == 0) {
				folder.close(false);
			}

			// get the pointer to this email server
			Message[] msgs = folder.getMessages();

			// Use a suitable FetchProfile
			folder.fetch(msgs, profile);

		} catch (MailException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			try {
				if (folder != null && folder.isOpen())
					folder.close(false);
			} catch (Exception ex) {
			}
		}

	}

}
