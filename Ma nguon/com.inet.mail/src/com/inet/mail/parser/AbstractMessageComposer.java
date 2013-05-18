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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.inet.base.service.ErrorService;
import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailPriority;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;

/**
 * AbstractMessageComposer
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractMessageComposer.java 2008-12-10 22:46:57z nguyen_dv $
 * 
 *          Create date: Dec 10, 2008
 * 
 *          <pre>
 *  Initialization AbstractMessageComposer class.
 * </pre>
 */
public abstract class AbstractMessageComposer implements IMessageComposer {
  // ~ Static fields =========================================================
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 6829798461557762122L;

  // ~ Instance fields =======================================================
  /* support HTML or not. */
  private boolean htmlSupported = true;
  /* mail header. */
  private MessageHeader header;
  // the list of attachments.
  private Map<String, byte[]> attachments;
  // the body text.
  private String bodyText;

  // ~ Methods ===============================================================
  /**
   * {@inheritDoc}
   */
  public boolean isAttached() {
    return (header == null ? false : header.isAttached());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#isSpam()
   */
  public boolean isSpam() {
    return (header == null ? false : header.isSpam());
  }

  /**
   * Set the message is SPAM.
   * 
   * @param spam the given SPAM flag to set.
   */
  public void setSpam(boolean spam) {
    if (header == null)
      header = new MessageHeader();
    header.setSpam(spam);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getAddresses()
   */
  public Map<String, List<Address>> getAddresses() {
    return (header == null ? null : header.getAddresses());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getAttachName()
   */
  public Set<String> getAttachName() {
    return (header == null ? null : header.getAttachments());
  }

  /**
   * Set the list of attachment name.
   * 
   * @param attachments the given set of attachment name.
   */
  protected void setAttachName(Set<String> attachments) {
    if (header == null)
      header = new MessageHeader();
    header.setAttachments(attachments);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#addAttachment(java.util.Map)
   */
  public void addAttachment(Map<String, byte[]> attachments) {
    if (attachments == null)
      return;
    if (this.attachments == null)
      this.attachments = new HashMap<String, byte[]>();

    // put attachment to store.
    for (String attachment : attachments.keySet()) {
      this.attachments.put(attachment, attachments.get(attachment));
    }

    // set header attachment name.
    if (attachments != null) {
      setAttachName(attachments.keySet());
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#addAttachment(java.lang.String, byte[])
   */
  public void addAttachment(String name, byte[] data) {
    if (attachments == null)
      attachments = new HashMap<String, byte[]>();
    attachments.put(attachments.size() + ":" + name, data);

    // update attachment name.
    setAttachName(attachments.keySet());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getAttachments()
   */
  public Map<String, byte[]> getAttachments() throws MailException {
    return attachments;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getBCC()
   */
  public List<Address> getBCC() {
    return (header == null ? null : header.getBcc());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#addBCC(java.lang.String[])
   */
  public void addBCC(String... bccAddress) throws MailException {
    if (bccAddress != null && bccAddress.length > 0) {
      List<Address> data = getBCC();
      if (data == null)
        data = new ArrayList<Address>();

      for (String address : bccAddress) {
        try {
          data.add(Address.parse(address));
        } catch (ParserException pex) {
          // avoid this error to parse the next message.
          ErrorService.logWarn("could not parse address: {" + address + "}",
              AbstractMessageComposer.class, pex);
        }
      }

      // set BCC data.
      if (header == null)
        header = new MessageHeader();
      header.setBcc(data);
    }
  }

  /**
   * Adds array of {@link Address} to mail BCC address.
   * 
   * @param addresses the given array of {@link Address}
   * @throws MailException if error occurs during adding BCC address.
   */
  protected void addBcc(Address... addresses) throws MailException {
    if (addresses == null || addresses.length == 0)
      return;
    if (header == null)
      header = new MessageHeader();
    header.setBcc(Arrays.asList(addresses));
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getBody(boolean)
   */
  public String getBody() {
    return bodyText;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setBody(java.lang.String)
   */
  public void setBody(String body) {
    bodyText = body;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getCC()
   */
  public List<Address> getCC() {
    return (header == null ? null : header.getCc());
  }

  /**
   * @see com.inet.mail.parser.IMessageComposer#addCC(java.lang.String[])
   */
  public void addCC(String... ccAddress) throws MailException {
    if (ccAddress != null && ccAddress.length > 0) {
      List<Address> data = getCC();
      if (data == null)
        data = new ArrayList<Address>();

      for (String address : ccAddress) {
        try {
          data.add(Address.parse(address));
        } catch (ParserException pex) {
          // avoid this error to parse the next address.
          // avoid this error to parse the next message.
          ErrorService.logWarn("could not parse address: {" + address + "}",
              AbstractMessageComposer.class, pex);
        }
      }
      // create message header if does not exist.
      if (header == null)
        header = new MessageHeader();
      header.setCc(data);
    }
  }

  /**
   * Adds array of {@link Address} to mail CC address.
   * 
   * @param addresses the given array of {@link Address}
   * @throws MailException if error occurs during adding CC address.
   */
  protected void addCc(Address... addresses) throws MailException {
    if (addresses == null || addresses.length == 0)
      return;
    if (header == null)
      header = new MessageHeader();
    header.setCc(Arrays.asList(addresses));
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setFrom(com.inet.mail.data.Address)
   */
  public void setFrom(Address address) {
    if (header == null)
      header = new MessageHeader();
    header.setFrom(address);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getFrom()
   */
  public Address getFrom() {
    return (header == null ? null : header.getFrom());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getUid()
   */
  public String getUid() {
    return (header == null ? null : header.getUid());
  }

  /**
   * Set the message universal identifier.
   * 
   * @param uid the given message UIDL.
   */
  public void setUid(String uid) {
    if (header == null)
      header = new MessageHeader();
    header.setUid(uid);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getPriority()
   */
  public MailPriority getPriority() {
    return (header == null ? MailPriority.NORMAL : header.getPriority());
  }

  /**
   * Set mail {@link MailPriority} instance.
   * 
   * @param priority the given {@link MailPriority} instance.
   */
  protected void setPriority(MailPriority priority) {
    if (header == null)
      header = new MessageHeader();
    header.setPriority(priority);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getReceivedDate()
   */
  public Date getReceivedDate() {
    return (header == null ? null : header.getReceived());
  }

  /**
   * Set received {@link Date}.
   * 
   * @param receivedDate the given received {@link Date}
   */
  protected void setReceivedDate(Date receivedDate) {
    if (header == null)
      header = new MessageHeader();
    header.setReceived(receivedDate);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getSentDate()
   */
  public Date getSentDate() {
    return (header == null ? null : header.getSent());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setSentDate(java.util.Date)
   */
  public void setSentDate(Date date) {
    if (header == null)
      header = new MessageHeader();
    header.setSent(date);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getSize()
   */
  public int getSize() {
    return (header == null ? null : header.getSize());
  }

  /**
   * Set the message size.
   * 
   * @param size the given message size.
   */
  protected void setSize(int size) {
    if (header == null)
      header = new MessageHeader();
    header.setSize(size);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setSubject(java.lang.String)
   */
  public void setSubject(String subject) {
    if (header == null)
      header = new MessageHeader();
    header.setSubject(subject);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getSubject()
   */
  public String getSubject() {
    return (header == null ? null : header.getSubject());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getTo()
   */
  public List<Address> getTo() {
    return (header == null ? null : header.getTo());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#addTo(java.lang.String[])
   */
  public void addTo(String... toAddress) throws MailException {
    if (toAddress != null && toAddress.length > 0) {
      List<Address> data = getTo();
      if (data == null)
        data = new ArrayList<Address>();

      for (String address : toAddress) {
        try {
          data.add(Address.parse(address));
        } catch (ParserException pex) {
          // avoid this error for parsing next address.
          // avoid this error to parse the next message.
          ErrorService.logWarn("could not parse address: {" + address + "}",
              AbstractMessageComposer.class, pex);
        }
      }

      // create new message header if does not exist.
      if (header == null)
        header = new MessageHeader();
      header.setTo(data);
    }
  }

  /**
   * Adds address to the mail's To recipient.
   * 
   * @param addresses the given array of {@link Address}
   * @throws MailException if there is any error during adding address to header.
   */
  protected void addTo(Address... addresses) throws MailException {
    if (addresses == null || addresses.length == 0)
      return;
    if (header == null)
      header = new MessageHeader();
    header.setTo(Arrays.asList(addresses));
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#isHtmlSupport()
   */
  public boolean isHtmlSupport() {
    return htmlSupported;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setHtmlSupport(boolean)
   */
  public void setHtmlSupport(boolean htmlSupport) {
    this.htmlSupported = htmlSupport;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#isCalendar()
   */
  public boolean isCalendar() {
    return (header == null ? false : header.isCalendar());
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#setCalendar(boolean)
   */
  public void setCalendar(boolean calendar) {
    if (header == null)
      header = new MessageHeader();
    header.setCalendar(calendar);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.parser.IMessageComposer#getHeader()
   */
  public MessageHeader getHeader() {
    return header;
  }

  /**
   * Return the list of recipients {@link Address}.
   * 
   * @param message the given {@link Message} object.
   * @param type the given recipient type such as "to","cc","bcc"
   * @return the array of {@link Address}; never null.
   */
  protected Address[] getRecipients(final Message message, final String type) throws MailException, MailParserException {
    try {
      // get recipient header value.
      final String[] headers = message.getHeader(type);

      final List<Address> addresses = new ArrayList<Address>();
      if (headers != null && headers.length > 0) {
        for (final String header : headers) {
          if (StringService.hasLength(header)) {
            addresses.addAll(AddressParser.parseHeaderAsList(header));
          }
        }
      }

      // convert to arrays and return the result to actor.
      return addresses.toArray(new Address[0]);
    } catch (final MessagingException mex) {
      throw new MailException("Could not get recicipent from message.", mex);
    } catch (final ParserException pex) {
      throw new MailParserException("Could not parse email address", pex);
    }
  }
}
