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
package com.inet.mail.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailPriority;

/**
 * MessageHeader
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MessageHeader.java 2008-12-12 11:30:10z nguyen_dv $
 * 
 * Create date: Dec 12, 2008
 * 
 * <pre>
 *  Initialization MessageHeader class.
 * </pre>
 */
public class MessageHeader implements Serializable {
  // ~ Static fields =========================================================
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -321576617195554323L;

  // ~ Instance fields =======================================================
  /* mail unique identifier. */
  private String uid;
  /* mail subject. */
  private String subject;
  /* mail sender. */
  private Address from;
  /* mail received date. */
  private Date received;
  /* mail sent date. */
  private Date sent;
  /* mail size. */
  private int size;
  /* mail priority. */
  private MailPriority priority;
  /* mail is a calendar. */
  private boolean calendar;
  /* mail attachment name. */
  private Set<String> attachments = null;
  /* list of TO addresses. */
  private List<Address> to;
  /* list of CC addresses. */
  private List<Address> cc;
  /* list of BCC addresses. */
  private List<Address> bcc;
  /* spam check. */
  private boolean spam;

  // ~ Methods ===============================================================
  /**
   * @return the list of attachments name.
   */
  public Set<String> getAttachments() {
    return Collections.unmodifiableSet(attachments);
  }

  /**
   * Set the list of attachments name.
   * 
   * @param attachments the given list of attachment name.
   */
  public void setAttachments(Set<String> attachments) {
    if (attachments == null)
      return;

    if (this.attachments == null)
      this.attachments = new HashSet<String>();
    for (String attachment : attachments) {
      this.attachments.add(attachment);
    }
  }

  /**
   * @return the list of BCC {@link Address}.
   */
  public List<Address> getBcc() {
    return bcc;
  }

  /**
   * Set the list of BCC {@link Address}.
   * 
   * @param bcc the given list of BCC {@link Address}.
   */
  public void setBcc(List<Address> bcc) {
    this.bcc = bcc;
  }

  /**
   * @return the list of CC addresses.
   */
  public List<Address> getCc() {
    return cc;
  }

  /**
   * Set the list of CC {@link Address}.
   * 
   * @param cc the given list of CC {@link Address}.
   */
  public void setCc(List<Address> cc) {
    this.cc = cc;
  }

  /**
   * @return the list of TO addresses.
   */
  public List<Address> getTo() {
    return to;
  }

  /**
   * Set the list of TO {@link Address}
   * 
   * @param to the given list of TO {@link Address}
   */
  public void setTo(List<Address> to) {
    this.to = to;
  }

  /**
   * @return the mail from {@link Address}
   */
  public Address getFrom() {
    return from;
  }

  /**
   * Set the mail from {@link Address}
   * 
   * @param from the given mail from {@link Address}
   */
  public void setFrom(Address from) {
    this.from = from;
  }

  /**
   * @return the {@link MailPriority} instance.
   */
  public MailPriority getPriority() {
    return priority;
  }

  /**
   * Set the {@link MailPriority}
   * 
   * @param priority the given {@link MailPriority}
   */
  public void setPriority(MailPriority priority) {
    this.priority = priority;
  }

  /**
   * @return the mail received {@link Date}
   */
  public Date getReceived() {
    return received;
  }

  /**
   * Set the mail received {@link Date}
   * 
   * @param received the given mail received {@link Date}
   */
  public void setReceived(Date received) {
    this.received = received;
  }

  /**
   * @return the mail sent {@link Date}
   */
  public Date getSent() {
    return sent;
  }

  /**
   * @return if the mail is spam.
   */
  public boolean isSpam() {
    return spam;
  }

  /**
   * Set the mail is spam.
   * 
   * @param spam the given SPAM flag.
   */
  public void setSpam(boolean spam) {
    this.spam = spam;
  }

  /**
   * Set the mail sent {@link Date}
   * 
   * @param sent the given mail sent {@link Date}
   */
  public void setSent(Date sent) {
    this.sent = sent;
  }

  /**
   * @return the mail size
   */
  public int getSize() {
    return size;
  }

  /**
   * Set the mail size.
   * 
   * @param size the given mail size.
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * @return the mail subject
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Set the mail subject.
   * 
   * @param subject the mail subject.
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * @return the mail unique identifier.
   */
  public String getUid() {
    return uid;
  }

  /**
   * Set the mail unique identifier.
   * 
   * @param uid the given mail unique identifier.
   */
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * @return if the mail has attachment.
   */
  public boolean isAttached() {
    return (attachments != null && attachments.size() > 0);
  }

  /**
   * @return if the given mail is calendar.
   */
  public boolean isCalendar() {
    return calendar;
  }

  /**
   * Set the mail calendar flag.
   * 
   * @param calendar the given mail calendar flag.
   */
  public void setCalendar(boolean calendar) {
    this.calendar = calendar;
  }

  /**
   * Convert message address to mapping address.
   */
  public Map<String, List<Address>> getAddresses() {
    // create map of address.
    Map<String, List<Address>> addresses = new Hashtable<String, List<Address>>();

    // create from address.
    List<Address> from = new ArrayList<Address>();
    from.add(getFrom());

    addresses.put(IMessageComposer.FROM_ADDRESS, from);
    if (getTo() != null) {
      addresses.put(IMessageComposer.TO_ADDRESS, getTo());
    }

    if (getCc() != null) {
      addresses.put(IMessageComposer.CC_ADDRESS, getCc());
    }

    if (getBcc() != null) {
      addresses.put(IMessageComposer.BCC_ADDRESS, getBcc());
    }

    // return map of address.
    return Collections.unmodifiableMap(addresses);
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append(getClass().getName()).append("[")
          .append("UIDL: ").append(uid)
          .append("; Subject: ").append(subject)
          .append("; From: ").append(from != null ? from.toUnicodeString()
                                                  : StringService.EMPTY_STRING)
          .append("; Attached: ").append(isAttached())
          .append("; SPAM: ").append(isSpam() ? "YES" : "NO")
          .append("; Calendar: ").append(calendar).append("]");

    return buffer.toString();
  }
}
