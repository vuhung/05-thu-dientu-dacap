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
package com.inet.mail.restore;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import net.fortuna.mstor.MStorStore;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailHeaderRemoteSL;

/**
 * ExportMessage.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class ExportMessage {
	private final int MAX_SIZE_PROCESS = 1024 * 1024;
	private final int MAX_MSGS_PROCESS = 64;

	private MailHeaderRemoteSL mailHeaderRemoteSL = null;
	private List<String> messageIds;

	/**
	 * @param ctx
	 * @param jndi
	 * @param reference
	 */
	public ExportMessage(MailHeaderRemoteSL mailHeaderRemoteSL,
			List<String> messageIds) {
		this.mailHeaderRemoteSL = mailHeaderRemoteSL;
		this.messageIds = messageIds;
	}

	/**
	 * 
	 * @param path
	 * @param username
	 * @param password
	 * @return
	 * @throws EJBException
	 */
	public int execute(String path, String username, String password)
			throws EJBException {
		// don't want to throw this exception
		if (mailHeaderRemoteSL == null || messageIds == null
				|| messageIds.size() == 0)
			return 0;

		MStorStore mboxStore = null;
		Folder folder = null;
		int count = 0;
		try {
			Session session = Session.getDefaultInstance(new Properties());
			if (session != null) {
				URLName storeUrl = new URLName("mstor:" + path);
				mboxStore = new MStorStore(session, storeUrl);

				if (username != null && password != null)
					mboxStore.connect(username, password);
				else
					mboxStore.connect();

				if (mboxStore.isConnected()) {
					folder = mboxStore.getDefaultFolder();

					// create a new message file if we didn't find this folder
					if (!folder.exists())
						folder.create(Folder.HOLDS_MESSAGES);
					folder.open(Folder.READ_WRITE);
					// get message from database first
					while (count < messageIds.size()) {
						int stepCnt = 0;
						int maxSize = 0;
						List<Message> message = new ArrayList<Message>();
						while (stepCnt < MAX_MSGS_PROCESS
								&& count < messageIds.size()
								&& maxSize < MAX_SIZE_PROCESS) {
							Message composer = new MimeMessage(null,new ByteArrayInputStream(mailHeaderRemoteSL.getComposer(messageIds.get(count)))									);
							message.add(composer);
							maxSize += composer.getSize();
							stepCnt++;
							count++;
						}
						// export message into this folder
						if (message.size() > 0) {
							folder.appendMessages(message.toArray(new Message[message.size()])) ;
						}
					}
				}
			}
		} catch (MessagingException e) {
			throw new EJBException("MessagingException:", e);
		} finally {
			try {
				if (folder != null && folder.isOpen())
					folder.close(false);
			} catch (MessagingException e) {
			}
			folder = null;

			try {
				if (mboxStore != null && mboxStore.isConnected())
					mboxStore.close();

			} catch (MessagingException e) {
			}
			mboxStore = null;
		}

		return count;
	}
}
