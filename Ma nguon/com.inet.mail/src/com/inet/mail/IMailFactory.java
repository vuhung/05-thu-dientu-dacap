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

import javax.mail.Message;

import com.inet.base.jsr305.Nullable;
import com.inet.mail.core.UIDStore;
import com.inet.mail.data.MailFactoryType;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;
import com.inet.mail.parser.IMessageComposer;

/**
 * IMailFactory
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: IMailFactory.java 2008-12-11 14:42:52Z nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization IMailFactory class.
 * </pre>
 */
public interface IMailFactory {
	//~ Static fields =========================================================
	/**
	 * INBOX folder.
	 */
	static final String DEFAULT_FOLDER = "INBOX" ;
	/**
	 * SPAM folder.
	 */
	static final String SPAM_FOLDER = "SPAM" ;
	/**
	 * TRASH folder.
	 */
	static final String TRASH_FOLDER = "TRASH" ;
	
	//~ Methods ===============================================================
	/**
	 * Close all connections
	 */
	void close();
	
	/**
	 * @return if the factory is connected to server.
	 */
	boolean isConnected() ;

	/**
	 * Connect to the SMTP mail factory using default username and password.
	 * 
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */
	boolean connect() throws MailException ;	
	
	/**
	 * Connect to the SMTP mail factory.
	 * 
	 * @param username String - the given user name.
	 * @param password String - the given password.
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */
	boolean connect(String username, String password) throws MailException ;
	
	/**
	 * Connect to the SMTP mail factory.
	 * 
	 * @param host String - the given mail server host name.
	 * @param username String - the given user name.
	 * @param password String - the given password.
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */
	boolean connect(String host, String username, String password) throws MailException ;
	
	/**
	 * Connect to the SMTP mail factory.
	 * 
	 * @param host String - the given host.
	 * @param port int - the given port.
	 * @param username String - the given user name.
	 * @param password String - the given password.
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */
	boolean connect(String host, int port, String username, String password) throws MailException ;
	
	/**
	 * Connect to the SMTP mail factory.
	 * 
	 * @param security MailSecurity - the given mail security.
	 * @param host String - the given host.
	 * @param port int - the given port.
	 * @param username String - the given user name.
	 * @param password String - the given password.
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */
	boolean connect(MailSecurity security, String host, int port, String username, String password) throws MailException ;	
	
	/**
	 * Connect to the mail factory.
	 * 
	 * @param protocol MailProtocol - the given mail protocol.
	 * @param security MailSecurity - the given mail security.
	 * @param host String - the given host.
	 * @param port int - the given port.
	 * @param username String - the given user name.
	 * @param password String - the given password.
	 * @return <code>true</code> connect to mail server successful, otherwise <code>false</code>
	 * @throws MailException if an error occurs during connecting to the server.
	 */	
	boolean connect(MailProtocol protocol, MailSecurity security, String host, int port, 
			String username, String password) throws MailException ;
	
	/**
	 *  Send message.
	 * 
	 * @param message the given message to send.
	 * @throws MailException if an error occurs during sending mail.
	 */
	void send(Object message) throws MailException ;
	
	/**
	 * Create message from the given flag that mark support HTML or not.
	 * 
	 * @param htmlSupport boolean - <code>true</code> if message support HTML, otherwise <code>false</code>
	 * @return the message {@link IMessageComposer}.
	 * @throws MailException if an error occurs during creating message.
	 */
	IMessageComposer createMessage(boolean htmlSupport) throws MailException ;
		
	/**
	 * Create message from the message data.
	 * 
	 * @param byte[] the given message data.
	 * @param parseBody the given flag that tell system parse mail body.
	 * @param parseAttach the given flag that tell system parse mail attachment.
	 * 
	 * @return the message {@link IMessageComposer}.
	 * @throws MailException if an error occurs during creating message.
	 */
	IMessageComposer createMessage(byte[] data, boolean parseBody, boolean parseAttach) throws MailException ;	
	
	/**
	 * Create message composer.
	 * 
	 * @return the message {@link IMessageComposer}.
	 * @throws MailException if an error occurs during creating message.
	 */
	IMessageComposer createMessage() throws MailException ;

	/**
	 * Create message from the given flag that mark support HTML or not 
	 * and mail priority.
	 * 
	 * @param htmlSupport boolean - <code>true</code> if message support HTML, otherwise <code>false</code>
	 * @param priority MailPriority - the given mail priority.
	 * @return the message composer.
	 * @throws MailException if an error occurs during creating message.
	 */
	IMessageComposer createMessage(boolean htmlSupport, MailPriority priority) 
		throws MailException ;	
	
	/**
	 * Fetch all mails from server and remove mail if <code>removed</code> flag is enable. 
	 * 
	 * @param store the list of checked mail.
	 * @param removed <code>true</code> will be removed mail on server mail, otherwise, does not remove.
	 * @return the list of new mail.
	 * @throws MailException if an error occurs during sending mail.
	 */
	List<IMessageComposer> fetch(UIDStore store, boolean removed) throws MailException ;
	
	/**
	 * Fetch all mails from server and remove mail if <code>removed</code> flag is enable. 
	 * 
	 * @param store the list of checked mail.
	 * @param maxItems the given max of mails to fetch from server.
	 * @param removed <code>true</code> will be removed mail on server mail, otherwise, does not remove.
	 * @return the list of new mail.
	 * 
	 * @throws MailException if an error occurs during sending mail.
	 */
	List<IMessageComposer> fetch(int maxItems, UIDStore store, boolean removed) throws MailException ;	
	
	/**
	 * Fetch all mails from server and remove mail if <code>removed</code> flag is enable. 
	 * 
	 * @param folder the given mail folder.
	 * @param store the given list of checked mail identifier.
	 * @param removed  <code>true</code> will be removed mail on server mail, otherwise, does not remove.
	 * @return the list of new mail.
	 * 
	 * @throws MailException if an error occurs during sending mail.
	 */
	List<IMessageComposer> fetch(@Nullable String folder, UIDStore store, boolean removed) throws MailException, MailParserException ;		
	
	/**
	 * Fetch all mails from server and remove mail if <code>removed</code> flag is enable. 
	 * 
	 * @param folder the given mail folder.
	 * @param maxItems the given max of mails to fetch from server.
	 * @param store the given list of checked mail identifier.
	 * 
	 * @param removed  <code>true</code> will be removed mail on server mail, otherwise, does not remove.
	 * @return the list of new mail.
	 * 
	 * @throws MailException if an error occurs during sending mail.
	 */
	List<IMessageComposer> fetch(@Nullable String folder, int maxItems, UIDStore store, boolean removed) throws MailException, MailParserException ;			

	/**
	 * Counts the number of new mails in the mail server.
	 * 
	 * @param folder the given mail folder; may be null.
	 * @param store the given checked mail message.
	 * @return the number of new mails.
	 * 
	 * @throws MailException when error occurs during count new mails.
	 */
	int count(@Nullable String folder, UIDStore store) throws MailException ;
	
	/**
	 * Downloads all of attachments from the given mail folder and mail message identifier.
	 * 
	 * @param folder the given mail folder name.
	 * @param uid the given unique mail message.
	 * 
	 * @return the attachments data.
	 * 
	 * @throws MailException when error occurs during downloading mail.
	 */
	byte[] download(String folder, String uid) throws MailException;
	
	/**
	 * Get the mail {@link Message} from the given mail folder, the mail uid,
	 * and flag that tell system can fetch the body and fetch the attachment.
	 * 
	 * @param folder String the given folder name.
	 * @param uid the given mail unique identifier.
	 * @param fetchBody the flag that tell the system can fetch the body mail.
	 * @param fetchAttach the flag that tell the system can fetch the mail attachment.
	 * @return the {@link IMessageComposer} instance.
	 * 
	 * @throws MailException when error occurs during getting message.
	 */
	IMessageComposer getMessage(String folder, String uid, boolean fetchBody, boolean fetchAttach) throws MailException ;

	/**
	 * Get the mail {@link Message} from the given the mail unique identifier,
	 * and flag that tell system can fetch the body and fetch the attachment.
	 * 
	 * @param uid the given mail unique identifier.
	 * @param fetchBody the flag that tell the system can fetch the body mail.
	 * @param fetchAttach the flag that tell the system can fetch the mail attachment.
	 * @return the {@link IMessageComposer} instance.
	 * 
	 * @throws MailException when error occurs during getting message.
	 */	
	IMessageComposer getMessage(String uid, boolean fetchBody, boolean fetchAttach) throws MailException ;
	
	/**
	 * Downloads the attachment from the given mail folder, mail unique identifier
	 * and the attachment name.
	 * 
	 * @param folder the given folder name; may be null.
	 * @param uid the given mail unique identifier.
	 * @param attachment the given mail attachment name.
	 * @return the attachment data.
	 * 
	 * @throws MailException when error occurs during downloading data.
	 */
	byte[] download(@Nullable String folder, String uid, String attachment) throws MailException ;
	
	/**
	 * Get the mail body from the given message unique identifier
	 * and the mail folder.
	 * 
	 * @param uid the given message unique identifier.
	 * @param folder the given mail folder. 
	 * @return the mail body.
	 * 
	 * @throws MailException when error occurs during getting mail body.
	 */
	String getBody(@Nullable String folder, String uid) throws MailException ;
			
	/**
	 * Deletes the list of unique identifier message on the server.
	 * 
	 * @param folder the given mail folder name; may be null.
	 * @param uids the list of messages to delete.
	 * 
	 * @return the list of real deleted mail message.
	 * 
	 * @throws MailException if an error occurs during sending mail.
	 */
	List<String> delete(@Nullable String folder, List<String> uids) throws MailException ;	
	
	/**
	 * @return the mail factory.
	 */
	MailFactoryType getMailFactory() ;
	
	/**
	 * @return the mail session.
	 */
	Object getSession() ;
	
	/**
	 * @return the mail host.
	 */
	String getHost() ;
	
	/**
	 * @return the mail port.
	 */
	int getPort() ;
	
	/**
	 * @return the mail user name.
	 */
	String getUsername() ;
	
	/**
	 * @return the mail password.
	 */
	String getPassword() ;
	
	/**
	 * @return the mail security.
	 */
	MailSecurity getSecurity() ;
	
	/**
	 * @return the mail protocol.
	 */
	MailProtocol getProtocol() ;	
}
