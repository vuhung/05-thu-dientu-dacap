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

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inet.base.service.CommonService;
import com.inet.base.service.StringService;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.parser.MessageHeader;
import com.inet.mail.parser.support.sun.MessageComposer;
import com.inet.mail.persistence.MailHeader;

/**
 * MailHeaderDTO
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 *
 * Create date: Jan 21, 2008
 *
 * <pre>
 *  Initialization MailHeaderDTO class.
 * </pre>
 */
public class MailHeaderDTO implements Serializable {
	//~ Static fields =========================================================
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 5241629016803149989L;

	//~ Instance fields =======================================================
	private String username;
	private String uid;
	private String subject;
	private Address sender;
	private Date received;
	private Date sent;
	private int size;
	private MailPriority priority;
	private boolean attached;
	private boolean calendar;
	private Map<String, List<Address>> addresses;
	private Set<String> attachments = null;
	private String bodyText;
	private long headerId = 0L;
	private long folderId = 0L;

        private String mailhost;
	private String contentId = null;
	private String bodyId = null;
	// mail spam flag.
	private boolean spam = false;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MessageHeaderDTO<tt> instance from the given
	 * {@link MailHeader} instance.
	 *
	 * @param header the given {@link MailHeader} instance.
	 */
	public MailHeaderDTO(MailHeader header) {
		this(header.getUid(), header.getSubject(), header.getSender(), header
				.getReceived(), header.getSent(), header.getSize(), header
				.getPriority(), header.getAttached() == CommonService.YES);
		this.headerId = header.getId();
		this.contentId = header.getComposeID();
	}

	/**
	 * Create a new <tt>MailHeaderDTO</tt> instance from the given
	 * {@link IMessageComposer} and {@link MailHeader} instance.
	 *
	 * @param composer the given mail {@link MessageComposer} instance.
	 * @param header the given {@link MailHeader} instance.
	 */
	public MailHeaderDTO(IMessageComposer composer, MailHeader header) {
		this(header);
		this.setFolderId(header.getFolder().getId());
		// create message addresses and message body.
		addresses = composer.getAddresses();
		bodyText = composer.getBody();

		// build message attachment.
		if(composer.isAttached()){
		        setAttached(true);
			attachments = new HashSet<String>() ;
			attachments.addAll(composer.getAttachName()) ;
		}
	}

	/**
	 * Create a new <tt>MailHeaderDTO</tt> instance from the given
	 * message composer and message universal identifier.
	 *
	 * @param composer the given {@link IMessageComposer} instance.
	 */
	public MailHeaderDTO(IMessageComposer composer){
		// get message header.
		MessageHeader header = composer.getHeader() ;

		// setting message identifier.
		uid = header.getUid();

		// set external data.
		attached = header.isAttached();
		subject = header.getSubject();
		priority = header.getPriority();
		size = header.getSize();
		received = header.getReceived();
		addresses = header.getAddresses() ;
		bodyText = composer.getBody();

		// get from address (get the top one)
		sender = header.getFrom() ;
		sent = header.getSent() ;
		calendar = header.isCalendar() ;
		attachments = header.getAttachments() ;
		spam = header.isSpam() ;
	}

	/**
	 * Create a new <tt>MailHeaderDTO</tt> instance from the given
	 * folder uid, subject, sender, received, sent, size, priority
	 * and attached.
	 *
	 * @param uid the given message universal identifier.
	 * @param subject the given message subject.
	 * @param sender the given message sender.
	 * @param received the given message received date.
	 * @param sent the given message sent date.
	 * @param size the message size.
	 * @param priority the message priority.
	 * @param attached the message with attachment.
	 */
	public MailHeaderDTO(String uid, String subject, String sender,
			Date received, Date sent, int size, MailPriority priority,
			boolean attached) {
		this.uid = uid;
		this.subject = subject;

		if(StringService.hasLength(sender))
			this.sender = new Address(sender);

		this.received = received;
		this.sent = sent;
		this.size = size;
		this.priority = priority;
		this.attached = attached;
	}

	//~ Methods ===============================================================
	/**
	 * @return if the mail is SPAM.
	 */
	public boolean isSpam() {
		return spam;
	}

	/**
	 * Set the mail is SPAM.
	 *
	 * @param spam the given SPAM mail flag.
	 */
	public void setSpam(boolean spam) {
		this.spam = spam;
	}

	/**
	 * @return the mailhost
	 */
	public String getMailhost() {
		return this.mailhost;
	}

	/**
	 * @param mailhost the mailhost to set
	 */
	public void setMailhost(String mailhost) {
		this.mailhost = mailhost;
	}

	/**
	 * @return
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return
	 */
	public Address getSender() {
		return this.sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(Address sender) {
		this.sender = sender;
	}

	/**
	 * @return
	 */
	public Date getReceived() {
		return this.received;
	}

	/**
	 * @param received
	 */
	public void setReceived(Date received) {
		this.received = received;
	}

	/**
	 * @return
	 */
	public Date getSent() {
		return this.sent;
	}

	/**
	 * @param sent
	 */
	public void setSent(Date sent) {
		this.sent = sent;
	}

	/**
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return
	 */
	public MailPriority getPriority() {
		return this.priority;
	}

	/**
	 * @param priority
	 */
	public void setPriority(MailPriority priority) {
		this.priority = priority;
	}

	/**
	 * @return
	 */
	public boolean isAttached() {
		return this.attached;
	}

	/**
	 * @param attached
	 */
	public void setAttached(boolean attached) {
		this.attached = attached;
	}

	/**
	 * @return
	 */
	public boolean isCalendar() {
		return this.calendar;
	}

	/**
	 * @param calendar
	 */
	public void setCalendar(boolean calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the attachments
	 */
	public Set<String> getAttachments() {
		return this.attachments;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return this.bodyText;
	}

	/**
	 * @return
	 */
	public List<Address> getCc() {
		if (addresses.containsKey(IMessageComposer.CC_ADDRESS))
			return addresses.get(IMessageComposer.CC_ADDRESS);
		else
			return null;
	}

	/**
	 * @return
	 */
	public List<Address> getBcc() {
		if (addresses.containsKey(IMessageComposer.BCC_ADDRESS))
			return addresses.get(IMessageComposer.BCC_ADDRESS);
		else
			return null;
	}

	/**
	 * @return
	 */
	public List<Address> getFrom() {
		if (addresses.containsKey(IMessageComposer.FROM_ADDRESS))
			return addresses.get(IMessageComposer.FROM_ADDRESS);
		else
			return null;
	}

	/**
	 * @return
	 */
	public List<Address> getTo() {
		if (addresses.containsKey(IMessageComposer.TO_ADDRESS))
			return addresses.get(IMessageComposer.TO_ADDRESS);
		else
			return null;
	}

	/**
	 * Return mail header
	 *
	 * @return
	 */
	public MailHeader getMailHeader() {
		MailHeader header = new MailHeader();

		// set data information.
		header.setId(headerId);
		header.setAttached(CommonService.getAnswer(attached));
		header.setCreated(new Date());
		header.setFlag(MailFlag.NOTHING);
		header.setPriority(priority);
		header.setRead(CommonService.NO);
		header.setReceived(received);
		// FIXED NullPointerException when sending mail with null from address.
		header.setSender((sender == null ? StringService.EMPTY_STRING : sender.toUnicodeString()));
		header.setSent(sent);
		header.setSubject(subject);
		header.setType(MailType.NORMAL);
		header.setSpam(CommonService.getAnswer(spam)) ;

		return header;
	}

	/**
	 * @return
	 */
	public long getHeaderId() {
		return headerId;
	}

	/**
	 * @param headerId
	 */
	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}

	/**
	 * @return
	 */
	public String getContentId() {
		return contentId;
	}

	/**
	 * @param contentId
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getBodyId() {
		return this.bodyId;
	}

	public void setBodyId(String bodyId) {
		this.bodyId = bodyId;
	}
	
	public long getFolderId() {
	    return folderId;
	}

	public void setFolderId(long folderId) {
	    this.folderId = folderId;
	}
}
