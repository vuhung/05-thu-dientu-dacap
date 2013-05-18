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
package com.inet.mail.sun;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.UIDFolder;

import com.inet.base.concurrent.CollectionFactory;
import com.inet.base.concurrent.ConcurrentMap;
import com.inet.base.jsr305.Nonnull;
import com.inet.base.jsr305.Nullable;
import com.inet.base.service.Assert;
import com.inet.base.service.CompareService;
import com.inet.base.service.StringService;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.MailCommand;
import com.inet.mail.core.UIDStore;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.parser.support.sun.MessageComposer;
import com.inet.mail.security.DataSSLSocketFactory;
import com.inet.mail.security.DataTLSSocketFactory;
import com.inet.mail.util.MailService;
import com.sun.mail.pop3.POP3Folder;

/**
 * SunMailFactory
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 1, 2008
 * 
 *       <pre>
 *  Initialization SunMailFactory class.
 * </pre>
 */
public class SunMailFactory extends AbstractMailFactory {
	//~ Instance fields =======================================================
	/* system properties. */
	private Properties properties; 
	/* SMTP session */
	private Session session;
	/* POP3 session. */
	private Session pop3Session ;
	/* mail box storage. */
	private Store store ;
	/* the current open folder. */
	private final ConcurrentMap folders = CollectionFactory.createConcurrentMap(1);
	
	//~ Constructors ==========================================================
	/**
	 * Create SMTP mail factory.
	 * 
	 * @param host  the given server host.
	 * @throws MailException when an error occurs during creating MailFactory.
	 */
	public SunMailFactory(String host) throws MailException {
		super(host);
	}

	/**
	 * Create mail factory.
	 * 
	 * @param host the given server host.
	 * @param port the given server port.
	 * 
	 * @throws MailException when an error occurs during creating MailFactory.
	 */
	public SunMailFactory(String host, int port) throws MailException {
		super(host, port);
	}

	/**
	 * Create mail factory.
	 * 
	 * @param security the given mail security.
	 * @param host the given server host.
	 * @throws MailException when an error occurs during creating MailFactory.
	 */
	public SunMailFactory(MailSecurity security, String host)
			throws MailException {
		super(security, host);
	}

	/**
	 * Create mail factory.
	 * 
	 * @param security the given mail security.
	 * @param host the given server host.
	 * @param port the given server port.
	 * @throws MailException when an error occurs during creating MailFactory.
	 */
	public SunMailFactory(MailSecurity security, String host, int port)
			throws MailException {
		super(security, host, port);
	}
	
	/**
	 * Create mail factory.
	 * 
	 * @param security the given mail security.
	 * @param host the given server host.
	 * @param port the given server port.
	 * @param username the given username.
	 * @param password the given password.
	 * 
	 * @throws MailException when an error occurs during creating MailFactory.
	 */
	public SunMailFactory(MailSecurity security, String host, int port,
			String username, String password) throws MailException {
		super(security, host, port, username, password);
	}	
	
	//~ Methods ===============================================================
	/**
	 * We handle many connections to many POP3/IMAP4 host. In the stores list,
	 * all this connections are connected and it have to be closed.
	 * 
	 * @see com.inet.mail.IMailFactory#close()
	 */
	@SuppressWarnings({"unchecked"})
	public void close() {
		// close the open folder.
		if(folders != null && !folders.isEmpty()){
			Iterator<Future<Folder>> iterator = folders.values().iterator() ;

			// close and deleted all message mark deleted.
			while(iterator.hasNext()){
				Future<Folder> future = iterator.next() ;
				try {
					Folder folder = future.get() ;
					if(folder != null && folder.isOpen()){
						folder.close((folder.getMode() == Folder.READ_WRITE)) ;
					}
				} catch (MessagingException mex) {
					logger.warn("could not close the folder, message: [" + mex.getMessage() + "]") ;
				} catch (ExecutionException eex){
					logger.warn("could not close the folder, message: [" + eex.getMessage() + "]") ;
				} catch (InterruptedException iex){
					logger.warn("could not close the folder, message: [" + iex.getMessage() + "]") ;
				} catch (Exception ex){
					logger.warn("could not close the store, message: [" + ex.getMessage() + "]") ;
				}
			}
			
			// dispose folder.
			folders.clear() ;			
		}
		
		// dispose store.
		if(store != null && store.isConnected()){
			try {
				store.close() ;
			} catch (MessagingException mex) {
				logger.warn("could not close the store, message: [" + mex.getMessage() + "]") ;
			} catch (Exception ex){
				logger.warn("could not close the store, message: [" + ex.getMessage() + "]") ;
			}
		}
		store = null ;
		
		// dispose session.
		session = null ;
		
		// dispose the pop3 session.
		pop3Session = null ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#isConnected()
	 */
	public boolean isConnected() {
		return (store != null && store.isConnected());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect()
	 */
	public boolean connect() throws MailException {
		// connect to SMTP mail.
		session = initializationSession(properties, getUsername(), getPassword()) ;
		
		// connect to server successful.
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect()
	 */
	public boolean connect(String username, String password) throws MailException {
		// connect to SMTP mail.
		session = initializationSession(properties, username, password) ;

		// connect to server successful.
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean connect(String host, String username, String password)
			throws MailException {
		// initialization properties.
		setHost(host) ;
		postInitialization() ;
		
		// connect to SMTP server.
		session = initializationSession(properties, username, password) ;
		
		// connect to server successful.
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public boolean connect(String host, int port, String username,
			String password) throws MailException {
		// initialization properties.
		setHost(host) ;
		setPort(port) ;
		postInitialization() ;
		
		// connect to SMTP server.
		session = initializationSession(properties, username, password) ;
		
		// connect to server successful.
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect(com.inet.mail.data.MailSecurity, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public boolean connect(MailSecurity security, String host, int port,
			String username, String password) throws MailException {
		// initialization server properties.
		setSecurity(security) ;
		setHost(host) ;
		setPort(port) ;
		postInitialization() ;
		
		// connect to SMTP server.
		session = initializationSession(properties, username, password) ;
		
		// connect to server successful.
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#connect(com.inet.mail.data.MailProtocol, com.inet.mail.data.MailSecurity, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public boolean connect(MailProtocol protocol, MailSecurity security,
			String host, int port, String username, String password)
			throws MailException {
		// SMTP protocol ?
		if(MailProtocol.SMTP.equals(protocol)){
			setProtocol(protocol) ;
			setSecurity(security) ;
			setHost(host) ;
			setPort(port) ;
			
			// re-initialization mail.
			postInitialization() ;
		}else{
			initializationProtocol(protocol, security, username, password, host, port) ;
		}
		
		// initialization session.
		if(this.session == null || MailProtocol.SMTP.equals(protocol)){
			this.session = initializationSession(properties, username, password) ;
		}
		
		// does not connect to SMTP.
		if(!MailProtocol.SMTP.equals(protocol)){
			// create the store properties.
			Properties properties = initializationStoreSession(protocol, security, username, password, host, port) ;
			
			// connect to SMTP.
			pop3Session = Session.getInstance(properties) ;
			
			try {
				// get store information.				
				store = pop3Session.getStore(protocol.getProtocol()) ;
				
				// connect to the given host.
				if(StringService.hasLength(host) || 
						StringService.hasLength(username) ||
						StringService.hasLength(password)){
					store.connect(host, port, username, password) ;
				}else{
					store.connect() ;
				}
			} catch (NoSuchProviderException nspex) {
				throw new MailException(nspex.getMessage(), nspex) ;
			} catch (MessagingException mex) {
				throw new MailException(mex.getMessage(), mex) ;
			}
			
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#send(java.lang.Object)
	 */
	public void send(Object msg) throws MailException {
		// check object.
		Assert.isNotNull(msg, "The mail message must be set.") ;
		Assert.isInstanceOf(Message.class, msg, "The mail message must be a instance of javax.mail.Message.") ;
		
		// convert to javax.mail.Message.
		Message message = (Message)msg ;
		
		if(session == null) throw new MailException("Could not initialization transport mail protocol.") ;

		try {
			// get transport data.
			Transport transport = this.session.getTransport(MailProtocol.SMTP.getProtocol()) ;
			
			// check authentication.
			if(StringService.hasLength(getUsername()) &&  StringService.hasLength(getPassword())){
				transport.connect(getHost(), getPort(), getUsername(), getPassword()) ;
			}else{
				transport.connect() ;
			}
			
			// send message.
			transport.sendMessage(message, message.getAllRecipients()) ;			
		} catch (NoSuchProviderException nspex) {
			// throw exception.
			throw new MailException(nspex.getMessage(), nspex) ;
		} catch (MessagingException mex) {
			// throw exception.
			throw new MailException(mex.getMessage(), mex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#count(java.lang.String, com.inet.mail.core.UIDStore)
	 */
	public int count(String folder, @Nonnull UIDStore uidstore) throws MailException {
		if(store == null || !store.isConnected() ) return 0 ;
		
		// create fetch profile.
		FetchProfile profile = new FetchProfile() ;
		profile.add(UIDFolder.FetchProfileItem.UID) ;
		
		// hold the new message.
		int msgCount = 0 ;
		POP3Folder pop3Folder = null ;
		try{
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.FETCH, false) ;
			
			// folder does not exist.
			if(pop3Folder == null || !pop3Folder.exists()) return 0 ;
			
			// open the folder.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY) ;
			}
			
			// get total message.
			int total = pop3Folder.getMessageCount() ;
			if(total == 0) return 0 ;
			
			// get all message in current folder.
			Message[] messages = pop3Folder.getMessages() ;
			
			// fetch message.
			pop3Folder.fetch(messages, profile) ;
			
			// count new message.
			for(Message message : messages){
				String uid = pop3Folder.getUID(message) ;
				if(uidstore.isNew(uid, false)) msgCount++ ;
			}
		}catch(MessagingException mex){
			// show warning.
			logger.warn("could not connect to folder: [" + folder + "], message: [" + mex.getMessage() + "]") ;
		}finally{}
		
		return msgCount;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#delete(java.lang.String, java.util.List)
	 */
	@SuppressWarnings({"unchecked"})
	public List<String> delete(@Nullable String folder, List<String> uids) throws MailException {
		if(store == null || !store.isConnected() || uids == null || uids.size() == 0) return Collections.EMPTY_LIST;
		
		// create list of real deleted mail message.
		List<String> removeMsgs = new ArrayList<String>() ;
		
		// create fetch profile to fetch data.
		FetchProfile profile = new FetchProfile() ;
		profile.add(UIDFolder.FetchProfileItem.UID) ;
				
		POP3Folder pop3Folder = null ;
		try{
			// get folder.
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.DELETE, true) ;
			
			// the folder does not exist.
			if(pop3Folder == null || !pop3Folder.exists()) return Collections.EMPTY_LIST;
			
			// open the folder to change message status.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_WRITE) ;
			}
			
			// there is not message in this folder.
			int total = pop3Folder.getMessageCount() ;
			if(total == 0) return Collections.EMPTY_LIST;
			
			// get all message and flag delete to delete message.
			Message[] messages = pop3Folder.getMessages() ;
			pop3Folder.fetch(messages, profile) ;
			
			for(Message message : messages){
				String uid = pop3Folder.getUID(message) ;
				if(uids.contains(uid)){
					pop3Folder.getMessage(message.getMessageNumber()).setFlag(Flags.Flag.DELETED, true) ;
					removeMsgs.add(uid) ;
				}
			}
			
			// return list of real deleted mail message.
			return removeMsgs ;
		}catch(MessagingException mex){
			throw new MailException("Could not delete messages.", mex) ;
		}finally{
			try{
				// close to delete the message.
				if(pop3Folder != null && pop3Folder.isOpen()) pop3Folder.close(true) ;
			}catch(Exception ex){}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#fetch(java.lang.String, int, com.inet.mail.core.UIDStore, boolean)
	 */
	public List<IMessageComposer> fetch(@Nullable String folder, int maxItems,
			@Nonnull UIDStore store, boolean removed) throws MailException, MailParserException {
		if(store == null || maxItems == 0 || !isConnected()) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
		
		// create fetch profile to search data.
		FetchProfile profile = new FetchProfile() ;
		profile.add(UIDFolder.FetchProfileItem.UID) ;
		
		// create POP3 folder to fetch mail.		
		POP3Folder pop3Folder = null ;
		try{
			// get folder.
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.FETCH, removed) ;
			if(pop3Folder == null || !pop3Folder.exists()) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(removed ? Folder.READ_WRITE : Folder.READ_ONLY) ;
			}
			
			// there is no message in current folder.
			int total = pop3Folder.getMessageCount() ;
			if(total == 0) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
			
			// get message.
			Message[] messages = pop3Folder.getMessages() ;
			pop3Folder.fetch(messages, profile) ;
			
			// create store to hold message.
			List<IMessageComposer> newMessages = new ArrayList<IMessageComposer>() ;
			
			// get new message.
			String uid = null ;
			for(Message message : messages){
				uid = pop3Folder.getUID(message) ;
				try{
        				if(store.isNew(uid, true)){
        					// parse the mail header.
        					MessageComposer composer = new MessageComposer(message, true, false) ;
        					composer.setUid(uid) ;
        					
        					// add to new message.
        					newMessages.add(composer) ;
        										
        					// mark mail is delete.
        					if(removed) message.setFlag(Flags.Flag.DELETED, true) ;
        					
        					// the message exceed.
        					if(newMessages.size() == maxItems) break ;
        				}
				}catch(MailParserException mpe){
				  throw new MailParserException(uid, mpe.getMessage(), mpe);
				}
			}
			
			// return message.
			return newMessages ;
		}catch(MessagingException mex){
			throw new MailException("Could not fetch mail from server, folder: [" + folder + "]", mex) ;
		}finally{}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.inet.mail.IMailFactory#fetch(java.lang.String, com.inet.mail.core.UIDStore, boolean)
	 */
	public List<IMessageComposer> fetch(String folder, UIDStore store,
			boolean removed) throws MailException, MailParserException {
		if(store == null || !isConnected()) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
		
		// create fetch profile to search data.
		FetchProfile profile = new FetchProfile() ;
		profile.add(UIDFolder.FetchProfileItem.UID) ;
		
		// create POP3 folder.
		POP3Folder pop3Folder = null ;
		try{
			// get folder.
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.FETCH, removed) ;
			if(pop3Folder == null || !pop3Folder.exists()) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(removed ? Folder.READ_WRITE : Folder.READ_ONLY) ;
			}
			
			// there is no message in current folder.
			int total = pop3Folder.getMessageCount() ;
			if(total == 0) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
			
			// get message.
			Message[] messages = pop3Folder.getMessages() ;
			pop3Folder.fetch(messages, profile) ;
			
			// parsed message online.
			List<IMessageComposer> newMessages = new ArrayList<IMessageComposer>() ;
			
			// get new message.
			String uid = null ;
			for(Message message : messages){
				uid = pop3Folder.getUID(message) ;
								
				if(store.isNew(uid, true)){
					// create composer and parse the message header.
					MessageComposer composer = new MessageComposer(message, true, false) ;
					composer.setUid(uid) ;
					
					// add to new list message.
					newMessages.add(composer) ;
					
					// set delete flag.
					if(removed) message.setFlag(Flags.Flag.DELETED, true) ;
				}
			}
			
			// there is no new message.
			if(newMessages.size() == 0) return Collections.unmodifiableList(new ArrayList<IMessageComposer>(0)) ;
			
			// return message.
			return newMessages;
		}catch(MessagingException mex){
			throw new MailException("Could not fetch mail from server, folder: [" + folder + "]", mex) ;
		}finally{}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#getMessage(java.lang.String, java.lang.String, boolean, boolean)
	 */
	public IMessageComposer getMessage(String folder, String uid,
			boolean fetchBody, boolean fetchAttach) throws MailException {
		if(store == null || !store.isConnected() || !StringService.hasLength(uid)) return null ;
		
		// pop3 folder
		POP3Folder pop3Folder = null ;
		try{
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.READ, false) ;
			if(pop3Folder == null || !pop3Folder.exists()) return null ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY) ;
			}
			
			// get message.
			Message message = getMessage(pop3Folder, uid) ;
			
			// fetch the message.
			return (message == null ? null : new MessageComposer(message, fetchBody, fetchAttach)) ;
		}catch(MessagingException mex){
			logger.warn("Could not get the mail with uid: [" + uid + "]", mex) ;
		}finally{	}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#download(java.lang.String, java.lang.String)
	 */
	public byte[] download(String folder, String uid) throws MailException {
		if(store == null || !store.isConnected() || !StringService.hasLength(uid)) return null ;
		
		// pop3 folder
		POP3Folder pop3Folder = null ;
		try{
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.READ, false) ;
			if(pop3Folder == null || !pop3Folder.exists()) return null ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY) ;
			}
			
			// get message.
			Message message = getMessage(pop3Folder, uid) ;
			
			// return attachment.
			return (message == null ? null : MailService.downloadMessage(message)) ;
		}catch(MessagingException mex){
			logger.warn("Could not get the mail with uid: [" + uid + "]", mex) ;
		}finally{}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#download(java.lang.String, java.lang.String, java.lang.String)
	 */
	public byte[] download(String folder, String uid, String attachment)
			throws MailException {
		if(store == null || !store.isConnected() || !StringService.hasLength(uid)) return null ;
		
		// pop3 folder
		POP3Folder pop3Folder = null ;
		try{
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.READ, false) ;
			if(pop3Folder == null || !pop3Folder.exists()) return null ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY) ;
			}			
			// get message.
			Message message = getMessage(pop3Folder, uid) ;
			
			// return attachment.
			return (message == null ? null : MailService.dataAttachment(message, attachment)) ;
		}catch(MessagingException mex){
			logger.warn("Could not get attachment from mail with uid: [" + uid + "]", mex) ;
		}catch(IOException ioex){
			logger.warn("Could not get attachment from mail with uid: [" + uid + "]", ioex) ;
		}finally{}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#getBody(java.lang.String, java.lang.String)
	 */
	public String getBody(String folder, String uid) throws MailException {
		if(store == null || !store.isConnected() || !StringService.hasLength(uid)) return StringService.EMPTY_STRING;
		
		// pop3 folder
		POP3Folder pop3Folder = null ;
		try{
			pop3Folder = (POP3Folder)getFolder(folder, MailCommand.READ, false) ;
			if(pop3Folder == null || !pop3Folder.exists()) return StringService.EMPTY_STRING ;
			
			// open folder to read/write message.
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY) ;
			}
			
			// get message.
			Message message = getMessage(pop3Folder, uid) ;
			
			// return attachment.
			return (message == null ? StringService.EMPTY_STRING : MailService.getBody(message, MailService.HTML_CONTENT_TYPE));
		}catch(MessagingException mex){
			logger.warn("Could not get mail body: [" + uid + "]", mex) ;
		}catch(Exception ex){
			logger.warn("Could not get mail body: [" + uid + "]", ex) ;
		}finally{}
		
		return StringService.EMPTY_STRING;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#createMessage()
	 */
	public IMessageComposer createMessage() throws MailException {
		return new MessageComposer(true, MailPriority.NORMAL);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#createMessage(boolean)
	 */
	public IMessageComposer createMessage(boolean htmlSupport) 
		throws MailException {
		return new MessageComposer(htmlSupport, MailPriority.NORMAL);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#createMessage(boolean, com.inet.mail.data.MailPriority)
	 */
	public IMessageComposer createMessage(boolean htmlSupport,
			MailPriority priority) throws MailException {
		return new MessageComposer(htmlSupport, priority);
	}
		
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.IMailFactory#createMessage(byte[], boolean, boolean)
	 */
	public IMessageComposer createMessage(byte[] data, boolean parseBody,
			boolean parseAttach) throws MailException, MailParserException {
		try {
			return new MessageComposer(data, parseBody, parseAttach);
		} catch (MessagingException mex) {
			throw new MailException("Could not parse mail message", mex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.AbstractMailFactory#getSession()
	 */
	public Object getSession() {
		return session;
	}	
	//~ Override Helper Methods ===============================================

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.AbstractMailFactory#postInitialization()
	 */
	protected void postInitialization() {
		// get protocol.
		String protocol = this.getProtocolValue(getProtocol(), getSecurity()) ;
		
		// set protocol to properties.
		if(MailProtocol.SMTP.equals(getProtocol())){
			setProperty("mail.transport.protocol", protocol) ;
		}
				
		// set protocol value.
		setProperty("mail." + protocol + ".host", getHost()) ;
		setProperty("mail." + protocol + ".port", String.valueOf(getPort())) ;
		setProperty("mail." + protocol + ".starttls.enable", String.valueOf(isAuthenticate(getSecurity()))) ;		
						
		// set authentication class.
		if(MailSecurity.TLS.equals(getSecurity())){
			setSecurityProperty("ssl.SocketFactory.provider", DataTLSSocketFactory.CLASSNAME) ;
		}else if(MailSecurity.SSL.equals(getSecurity())){
			setSecurityProperty("ssl.SocketFactory.provider", DataSSLSocketFactory.CLASSNAME) ;
		}
		
		// update authenticate.
		if(MailSecurity.SSL.equals(getSecurity())){
			setProperty("mail." + protocol + ".socketFactory.port", String.valueOf(getPort()));
			setProperty("mail." + protocol + ".socketFactory.fallback", "false");
			setProperty("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
			// set none secure security.
			String proto = getProtocol().getProtocol();
			setProperty("mail." + proto + ".host", getHost()) ;
			setProperty("mail." + proto + ".port", String.valueOf(getPort())) ;
			setProperty("mail." + proto + ".starttls.enable", String.valueOf(isAuthenticate(getSecurity()))) ;		

			setProperty("mail." + proto  + ".socketFactory.port", String.valueOf(getPort()));
			setProperty("mail." + proto + ".socketFactory.fallback", "false");
			setProperty("mail." + proto + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");				
		}
	}	
	
	/**
	 * Initialization protocol from the given mail protocol, security, host and port.
	 * 
	 * @param protocol the given mail protocol.
	 * @param security the given mail security.
	 * @param username the given user name to be registered.
	 * @param password the given user password.
	 * @param host the given host.
	 * @param port the given port.
	 */
	protected void initializationProtocol(
			MailProtocol protocol, 
			MailSecurity security, 
			String username,
			String password,
			String host, 
			int port){
		// get protocol.
		String proto = getProtocolValue(protocol, security) ;
		
		// get canonical host.
		String canonicalHost = validateHost(host) ;
		
		// set protocol value.
		setProperty("mail." + proto + ".host", canonicalHost) ;
		setProperty("mail." + proto + ".port", String.valueOf(port)) ;
		setProperty("mail." + proto + ".starttls.enable", String.valueOf(isAuthenticate(security))) ;		
		
		// initialization protocol user.
		if(StringService.hasLength(username)){
			setProperty("mail." + proto + ".user", username) ;
			setProperty("mail." + proto + ".auth", String.valueOf((StringService.isset(password)))) ;
		}
		
		// set authentication class.
		if(MailSecurity.TLS.equals(security)){
			setSecurityProperty("ssl.SocketFactory.provider", DataTLSSocketFactory.CLASSNAME) ;
		}else if(MailSecurity.SSL.equals(security)){
			setSecurityProperty("ssl.SocketFactory.provider", DataSSLSocketFactory.CLASSNAME) ;
		}
				
		// update authenticate.
		if(MailSecurity.SSL.equals(security)){
			setProperty("mail." + proto + ".socketFactory.port", String.valueOf(port));
			setProperty("mail." + proto + ".socketFactory.fallback", "false");
			setProperty("mail." + proto + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
			// set none secure.
			setProperty("mail." + protocol + ".socketFactory.port", String.valueOf(port));
			setProperty("mail." + protocol + ".socketFactory.fallback", "false");
			setProperty("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
	}	
	//~ Helper methods ========================================================
	/**
	 * Set pair of (key, value) to system properties.
	 * 
	 * @param key
	 *            String - the given system key.
	 * @param value
	 *            String - the given system value.
	 */
	private void setProperty(String key, String value) {
		// load system properties if does not exist.
		if (properties == null) {
			properties = new Properties();
		}

		// set properties.
		properties.setProperty(key, value);
	}

	/**
	 * Set pair of (key, value) to current security properties.
	 * 
	 * @param key
	 *            String - the given security key.
	 * @param datum
	 *            String - the given security datum.
	 */
	private void setSecurityProperty(String key, String datum) {
		Security.setProperty(key, datum);
	}

	/**
	 * @return the {@link MailProtocol} value.
	 */
	private String getProtocolValue(MailProtocol protocol, MailSecurity security) {
		return (MailSecurity.SSL.equals(security) ? protocol.getSecureProtocol() : protocol.getProtocol());
	}

	/**
	 * @return <code>true</code> if support authenticate, otherwise false.
	 */
	private boolean isAuthenticate(MailSecurity security) {
		return (MailSecurity.SSL.equals(security) || MailSecurity.TLS.equals(security));
	}

	/**
	 * Set SMTP Authenticator from the given user-name and password.
	 * 
	 * @param username the given user-name.
	 * @param password the given password.
	 */
	private void setSmtpAuthenticator(String username, String password) {
		// get SMTP protocol.
		String protocol = getProtocolValue(MailProtocol.SMTP, getSecurity());

		// set SMTP protocol value.
		if (StringService.hasLength(username)) {
			this.setProperty("mail." + protocol + ".user", username);
			this.setProperty("mail." + protocol + ".auth", String
					.valueOf((StringService.isset(password))));
		}

		// set SMTP protocol value.
		if (StringService.hasLength(username)) {
			this.setProperty("mail.smtp.user", username);
			this.setProperty("mail.smtp.auth", String.valueOf((StringService.isset(password))));
		}
	}

	/**
	 * Initialization session.
	 * 
	 * @param username the given user name.
	 * @param password the given password.
	 * @param properties the given mail {@link Properties} instance.
	 * 
	 * @throws MailException when an error occurs during initializing session.
	 */
	private Session initializationSession(Properties properties,
			String username, String password) throws MailException {
		// create SMTP authenticator object.
		Authenticator authenticator = null;
		
		// set user name and password.
		if(!CompareService.equals(username, getUsername())) setUsername(username) ;
		if(!CompareService.equals(password, getPassword())) setPassword(password) ;		

		if (StringService.hasLength(username)) {
			// set smtp authentication.
			setSmtpAuthenticator(username, password);
			authenticator = new SunSMTPSimpleAuthenticator(username, password);
		}

		// create session.
		Session session = Session.getInstance(properties, authenticator);

		// set transport Internet address.
		if (MailSecurity.SSL.equals(getSecurity())) {
			session.setProtocolForAddress("rfc822", getProtocolValue(MailProtocol.SMTP, getSecurity()));
		}

		// return the session.
		return session;
	}

	/**
	 * Initialization the store properties.
	 * 
	 * @param protocol the given store protocol.
	 * @param security the given mail security.
	 * @param username the given user name.
	 * @param password the given password.
	 * @param host the given host.
	 * @param port the given port.
	 * 
	 * @return the store properties.
	 * @throws MailException when errors occur during creating store session.
	 */
	private Properties initializationStoreSession(MailProtocol protocol,
			MailSecurity security, String username, String password,
			String host, int port) throws MailException {
		if (!MailProtocol.SMTP.equals(protocol)) {
			// create new properties.
			Properties properties = new Properties();

			// get protocol.
			String protoSsl = getProtocolValue(protocol, security);
			String proto = protocol.getProtocol();

			// get canonical host.
			String canonicalHost = validateHost(host);

			// set authentication class.
			if (MailSecurity.TLS.equals(security)) {
				this.setSecurityProperty("ssl.SocketFactory.provider", DataTLSSocketFactory.CLASSNAME);
			} else if (MailSecurity.SSL.equals(security)) {
				this.setSecurityProperty("ssl.SocketFactory.provider", DataSSLSocketFactory.CLASSNAME);
			}

			// set store properties value.
			// set protocol value.
			properties.setProperty("mail." + protoSsl + ".host", canonicalHost);
			properties.setProperty("mail." + protoSsl + ".port", String.valueOf(port));
			properties.setProperty("mail." + protoSsl + ".starttls.enable",String.valueOf(isAuthenticate(security)));

			// initialization protocol user.
			if (StringService.hasLength(username)) {
				properties.setProperty("mail." + protoSsl + ".user", username);
				properties.setProperty("mail." + protoSsl + ".auth", String.valueOf((StringService.isset(password))));
			}

			if (MailSecurity.SSL.equals(security)) {
				// set secure data.
				properties.setProperty("mail." + protoSsl + ".socketFactory.port", String.valueOf(port));
				properties.setProperty("mail." + protoSsl + ".socketFactory.fallback", "false");
				properties.setProperty("mail." + protoSsl + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");

				// set none secure.
				properties
						.setProperty("mail." + proto + ".host", canonicalHost);
				properties.setProperty("mail." + proto + ".port", String.valueOf(port));
				properties.setProperty("mail." + proto + ".starttls.enable", String.valueOf(isAuthenticate(security)));

				properties.setProperty("mail." + proto + ".socketFactory.port", String.valueOf(port));
				properties.setProperty("mail." + proto + ".socketFactory.fallback", "false");
				properties.setProperty("mail." + proto + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			}

			// return current properties.
			return properties;
		}

		// could not initialization the store properties.
		return null;
	}
	
	/**
	 * Get the {@link Folder} instance from the given 
	 * folder name.
	 * 
	 * @param folder the given folder name.
	 * @param command the given {@link MailCommand} value.
	 * @param writable the given writable flag.
	 * 
	 * @return the {@link Folder} instance or null if the folder
	 * does not exist.
	 */
	@SuppressWarnings({"unchecked"})
	protected Folder getFolder(String folder, MailCommand command, boolean writable){
		try {
			if(store == null || !store.isConnected()) return null;
			final String folderName = (StringService.hasLength(folder) ? folder : DEFAULT_FOLDER) ;
			
			// create the key name.
			final StringBuilder keyName = new StringBuilder() ;
			keyName.append(folderName)
				  .append('#')
				  .append(command.toString())
				  .append((writable ? '#' : "")) ;
			
			// retrieved the folder.
			Future<Folder> future = (Future<Folder>)folders.get(keyName.toString()) ;
			if(future == null){
				/**
				 * Used to create and return the instance to subsystem.
				 */
				Callable<Folder> callable = new Callable<Folder>(){
					/**
					 * @see java.util.concurrent.Callable#call()
					 */
					public Folder call() throws Exception {
						// retrieved the default folder.
						Folder defaultFolder = store.getDefaultFolder() ;						
						return (defaultFolder == null ? null : defaultFolder.getFolder(folderName));
					}
				} ;
				
				// create task to invoke creating object.
				FutureTask<Folder> futureTask = new FutureTask<Folder>(callable) ;
				
				// put to folders.
				future = (Future<Folder>)folders.putIfAbsent(keyName.toString(), futureTask) ;
				
				// the folder does not existing on holder?
				if(future == null){
					future = futureTask ;
					futureTask.run() ;
				}
			}
						
			// return folder.
			return future.get() ;
		} catch (ExecutionException eex){
			logger.warn("could not get folder: [" + folder + "], message: [" + eex.getMessage() + "]") ;
		} catch (InterruptedException iex){
			logger.warn("could not get folder: [" + folder + "], message: [" + iex.getMessage() + "]") ;
		} catch (Exception ex){
			logger.warn("could not get folder: [" + folder + "], message: [" + ex.getMessage() + "]") ;
		}
		
		// does not find any folder.
		return null ;
	}
	
	/**
	 * Retrieved message from the given message unique identifier.
	 * 
	 * @param folder the given {@link POP3Folder} instance.
	 * @param uid the given message unique identifier.
	 * 
	 * @return the {@link Message} instance.
	 * @throws MailException when error occurs during retrieving mail message.
	 */
	protected Message getMessage(POP3Folder folder, String uid) throws MailException{
		try {
			// count this message
			int totalMessages = folder.getMessageCount();
			
			// do not have any thing new
			if (totalMessages == 0) return null;
			
			// get the setLockmailpointer to this email server
			Message[] messages = folder.getMessages();

			// Use a suitable FetchProfile
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);
			
			// fetch message.
			folder.fetch(messages, profile);

			// find the message.
			String uidl = null ;
			for(Message message : messages){
				uidl = folder.getUID(message) ;
				if(uidl.equals(uid)) return message ;
			}
			
			// there is no message.
			return null ;
		} catch (MessagingException mex) {
			throw new MailException(mex.getMessage(), mex);
		}
	}	
}
