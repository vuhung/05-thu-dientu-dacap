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
package com.inet.mail.persistence;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.base.service.CommonService;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailType;

/**
 * MailHeader
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 * 	Initialization MailHeader bean.
 * </pre>
 */
@Entity
@Table(name="MAIL_HEADER")
@NamedQueries(value={
	@NamedQuery(name="MailHeader.findByFolder", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findByRead", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND m.read=:read ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findByAttached", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND m.attached=:attached ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findByPriority", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND m.priority=:priority ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findByFlag", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND m.flag=:flag ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findByType", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND m.type=:type ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findBySubject", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND LOWER(m.subject) LIKE :subject ORDER BY m.received DESC"),
	@NamedQuery(name="MailHeader.findBySender", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND LOWER(m.sender) LIKE :sender ORDER BY m.subject"),
	@NamedQuery(name="MailHeader.findByDate", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id AND (DATE(m.received)=DATE(:recv)) ORDER BY m.subject"),
	@NamedQuery(name="MailHeader.findByFolderOrderBySubject", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id ORDER BY m.subject"),
	@NamedQuery(name="MailHeader.findByFolderOrderBySent", query="SELECT m FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id ORDER BY m.sent DESC"),
	@NamedQuery(name="MailHeader.countUnreadMessage", query="SELECT NEW com.inet.mail.data.FolderCountDTO(m.folder.id,COUNT(m)) FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND m.read=:read GROUP BY m.folder"),
	@NamedQuery(name="MailHeader.countMessage", query="SELECT NEW com.inet.mail.data.FolderCountDTO(m.folder.id,COUNT(m)) FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner GROUP BY m.folder"),
	@NamedQuery(name="MailHeader.findContentByFolder", query="SELECT m.composeID  FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND f.id=:id"),
	@NamedQuery(name="MailHeader.findFolderById", query="SELECT f FROM MailHeader m JOIN m.folder f WHERE m.owner=:owner AND m.id=:id"),
	@NamedQuery(name="MailHeader.findUidByAccount", query="SELECT NEW java.lang.String(m.uid) FROM MailHeader m WHERE m.account=:account AND m.owner=:owner"),
	@NamedQuery(name="MailHeader.findUidByFolder", query="SELECT NEW java.lang.String(m.uid) FROM MailHeader m JOIN m.folder f WHERE (m.owner=:owner) AND (f.id=:folderId) AND (m.uid IS NOT NULL)"),	
	@NamedQuery(name="MailHeader.deleteByUid", query="DELETE FROM MailHeader m  WHERE (m.account=:account) AND (m.owner=:owner) AND (m.uid IN (:uids))"),
	@NamedQuery(name="MailHeader.emptyFolder", query="DELETE FROM MailHeader m  WHERE (m.owner=:owner) AND (m.uid IN (:uids))"),
	@NamedQuery(name="MailHeader.deleteNoneReceivedMail", query="DELETE FROM MailHeader m  WHERE (m.owner=:owner) AND (m.folder.id=:folderId) AND (m.uid IS NULL)"),
	@NamedQuery(name="MailHeader.deleteCache", query="UPDATE MailHeader SET composeID = NULL, timeCache=NULL  WHERE uid IS NOT NULL AND timeCache IS NOT NULL AND timeCache < :expired"),
	@NamedQuery(name="MailHeader.markAsReadFolder", query="UPDATE MailHeader SET read = 'Y' WHERE folder.id =:folderId"),
	@NamedQuery(name="MailHeader.countCurrentUsing", query="SELECT SUM(m.size) FROM MailHeader m WHERE m.owner=:owner AND m.account=:account")
})
public class MailHeader extends BasePersistence<Long> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8850624164325153334L;

	// header owner.
	private String owner ;
	private String uid;
	private String account;
	// mail sender.
	private String sender ;
	// mail recipients
	private String recipients;
	// mail subject.
	private String subject ;
	// sent date.
	private Date sent ;
	// received date.
	private Date received ;
	// stored data.
	private Date created ;
	// mail size.
	private int size ;
	// mail priority.
	private MailPriority priority = MailPriority.NORMAL;
	// read ? Y/N
	private char read = CommonService.NO;
	// attached.
	private char attached = CommonService.NO ;
	// mail type.
	private MailType type = MailType.NORMAL ;
	// mail flags.
	private MailFlag flag = MailFlag.NOTHING ;
	// mail spam flag.
	private char spam = CommonService.NO ;
	// mail composer content.
	private String composeID;
	// this field updates when this content is read from server
	// ONLY FOR EMAIL FROM SERVER!!
	private Date timeCache = null;
	// mail folder.
	private MailFolder folder ;
	//--------------------------------------------------------------------
	// class informations.
	//
	/**
	 * @return the mail header identifier.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	/**
	 * @return the mail header owner.
	 */
	@Column(name="mail_header_owner", length=42, nullable=false)
	public String getOwner() {
		return this.owner;
	}
	
	/**
	 * Set the mail header owner.
	 * 
	 * @param owner String - the mail header owner.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * @return the mail header sender.
	 */
	@Column(name="mail_header_sender", length=512, nullable=true)
	public String getSender() {
		return this.sender;
	}
	
	/**
	 * Set the mail header sender.
	 * 
	 * @param sender String - the given mail header sender.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * @return the mail header subject.
	 */
	@Column(name="mail_header_subject",length=998, nullable=true)
	public String getSubject() {
		return this.subject;
	}
	
	/**
	 * Set the mail header subject.
	 * 
	 * @param subject String - the given mail header subject.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * @return the sent mail date.
	 */
	@Column(name="mail_header_sent", nullable=true)
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getSent() {
		return this.sent;
	}
	
	/**
	 * Set the sent mail date.
	 * 
	 * @param sent Date - the given sent mail date.
	 */
	public void setSent(Date sent) {
		this.sent = sent;
	}
	
	/**
	 * @return the received mail date.
	 */
	@Column(name="mail_header_received", nullable=true)
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getReceived() {
		return this.received;
	}
	
	/**
	 * Set the received mail date.
	 * 
	 * @param received Date - the given received mail date.
	 */
	public void setReceived(Date received) {
		this.received = received;
	}
	
	/**
	 * @return the stored date.
	 */
	@Column(name="mail_header_created", nullable=false)
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getCreated() {
		return this.created;
	}
	
	/**
	 * Set the stored date.
	 * 
	 * @param created Date - the given stored date.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	
	/**
	 * @return the mail size.
	 */
	@Column(name="mail_header_size", nullable=true)
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Set the mail size.
	 * 
	 * @param size int - the given mail size.
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * @return the mail priority
	 */
	@Column(name="mail_header_priority", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public MailPriority getPriority() {
		return this.priority;
	}
	
	/**
	 * Set the mail priority.
	 * 
	 * @param priority MailPriority - the given mail priority.
	 */
	public void setPriority(MailPriority priority) {
		this.priority = priority;
	}
	
	/**
	 * @return the mail is read
	 */
	@Column(name="mail_header_read", nullable=true)
	public char getRead() {
		return this.read;
	}
	
	/**
	 * Set read mail flag.
	 * 
	 * @param read char - the given read mail flag.
	 */
	public void setRead(char read) {
		this.read = read;
	}
	
	/**
	 * @return the mail is SPAM.
	 */
	@Column(name="mail_header_spam", nullable=true)
	public char getSpam(){
		return this.spam ;
	}
	
	/**
	 * Set SPAM mail flag.
	 * 
	 * @param spam the given SPAM mail flag.
	 */
	public void setSpam(char spam){
		this.spam = spam ;
	}
	
	/**
	 * @return the attached mail flag.
	 */
	@Column(name="mail_header_attached", nullable=false)
	public char getAttached() {
		return this.attached;
	}
	
	/**
	 * Set the attached mail flag.
	 * 
	 * @param attached char - the given attached mail flag.
	 */
	public void setAttached(char attached) {
		this.attached = attached;
	}
	
	/**
	 * @return the mail type (NORMAL/REPLY/...)
	 */
	@Column(name="mail_header_type", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public MailType getType() {
		return this.type;
	}
	
	/**
	 * Set the mail type.
	 * 
	 * @param type MailType - the given mail type.
	 */
	public void setType(MailType type) {
		this.type = type;
	}

	/**
	 * @return the mail flag
	 */
	@Column(name="mail_header_flag", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public MailFlag getFlag() {
		return this.flag;
	}
	
	/**
	 * Set the mail flag.
	 * 
	 * @param flag MailFlag - the given mail flag.
	 */
	public void setFlag(MailFlag flag) {
		this.flag = flag;
	}
	
	/**
	 * @return the uid
	 */
	@Column(name="mail_header_uid", length=48, nullable=true)
	public String getUid() {
		return this.uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Column(name="mail_header_account", length=128,nullable=true)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name="mail_header_content", length=64, nullable=true)
	public String getComposeID() {
		return this.composeID;
	}

	public void setComposeID(String composeID) {
		this.composeID = composeID;
	}	
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="mail_header_cache", nullable=true)
	public Date getTimeCache() {
		return this.timeCache;
	}

	public void setTimeCache(Date timeCache) {
		this.timeCache = timeCache;
	}

	/**
	 * @return the mail folder.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="mail_header_folder", nullable=false)
	public MailFolder getFolder() {
		return this.folder;
	}
	
	/**
	 * The given mail folder.
	 * 
	 * @param folder MailFolder - the given mail folder.
	 */
	public void setFolder(MailFolder folder) {
		this.folder = folder;
	}

        /**
         * @return the recipients
         */
	@Column(name="mail_header_recipients", length=256, nullable=true)
        public String getRecipients() {
          return recipients;
        }
      
        /**
         * @param recipients the recipients to set
         */
        public void setRecipients(String recipients) {
          this.recipients = recipients;
        }
}