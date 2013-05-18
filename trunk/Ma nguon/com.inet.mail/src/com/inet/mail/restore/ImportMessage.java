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

import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.UIDFolder;
import javax.mail.URLName;

import net.fortuna.mstor.MStorStore;
import net.fortuna.mstor.util.CapabilityHints;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.ErrorService;
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.parser.support.sun.MessageComposer;
import com.inet.mail.util.MailService;
import com.sun.mail.pop3.POP3Folder;

/**
 * ImportMessage.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class ImportMessage {
	private static Properties props = new Properties();
	static {
		CapabilityHints.setHint(CapabilityHints.KEY_METADATA,
				CapabilityHints.VALUE_METADATA_DISABLED);
		CapabilityHints.setHint(CapabilityHints.KEY_MBOX_CACHE_BUFFERS,
				CapabilityHints.VALUE_MBOX_CACHE_BUFFERS_DISABLED);

		props.setProperty(CapabilityHints.KEY_METADATA,
				CapabilityHints.VALUE_METADATA_DISABLED);
	}

	private MailBridgeRemote mailHeaderRemote = null;

	/**
	 * @param ctx
	 * @param jndi
	 * @param reference
	 */
	public ImportMessage(MailBridgeRemote mailHeaderRemote) {
		this.mailHeaderRemote = mailHeaderRemote;
	}

	public void execute(String path, String username, String password)
			throws EJBException {
		MStorStore mboxStore = null;
		POP3Folder folder = null;
		try {
			Session session = Session.getDefaultInstance(props);
			if (session != null) {
				URLName storeUrl = new URLName("mstor:" + path);
				mboxStore = new MStorStore(session, storeUrl);

				if (username != null && password != null)
					mboxStore.connect(username, password);
				else
					mboxStore.connect();

				if (mboxStore.isConnected()) {
					FetchProfile profile = new FetchProfile();
					profile.add(UIDFolder.FetchProfileItem.UID);

					folder = (POP3Folder) mboxStore.getDefaultFolder();
					folder.open(Folder.READ_ONLY);

					final int total = folder.getMessageCount();
					if (total > 0 && mailHeaderRemote != null) {
						// get the pointer to this email server
						Message[] msgs = folder.getMessages();

						// Use a suitable FetchProfile
						folder.fetch(msgs, profile);

						for (Message message : msgs) {
							MessageComposer composer = new MessageComposer(message, false, false) ;
							composer.setUid(folder.getUID(message)) ;
							
							MailHeaderDTO header = new MailHeaderDTO(composer);
							byte[] msgContent = MailService.downloadMessage(message);
							
							// try to save data.
							try {
								this.mailHeaderRemote.save(header.getMailHeader(), msgContent, FolderType.INBOX);
							} catch (EJBException ex) {
								// log error.
								ErrorService.logError("***** ERROR: " + ex.getMessage(), ImportMessage.class, ex);
							}
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
	}
}
