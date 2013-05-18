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
package com.inet.mail.parser.support.sun;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.inet.base.service.FileService;
import com.inet.base.service.StringService;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.ParserException;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailPriority;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;
import com.inet.mail.parser.AbstractMessageComposer;
import com.inet.mail.parser.HtmlParser;
import com.inet.mail.service.PartService;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.ScanResult;
import com.inet.mail.util.MailService;

/**
 * MessageComposer
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MessageComposer.java 2008-12-11 11:41:57Z nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization MessageComposer class.
 * </pre>
 */
public class MessageComposer extends AbstractMessageComposer implements Serializable {
  // ~ Static fields =========================================================
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -8947526158635452212L;

  // ~ Constructors ==========================================================
  /**
   * Create a new <tt>MessageComposer</tt> from the given html supported flag.
   * 
   * @param htmlSupported
   *          boolean - <code>true</code> the mail message format under HTML,
   *          otherwise plain text.
   */
  public MessageComposer(boolean htmlSupported) {
    this(htmlSupported, MailPriority.NORMAL);
  }

  /**
   * Create a new <tt>MessageComposer</tt> instance from the given HTML
   * supported flag and {@link MailPriority} instance.
   * 
   * @param htmlSupported
   *          the given HTML priority supported flag.
   * @param priority
   *          the given {@link MailPriority} instance.
   */
  public MessageComposer(boolean htmlSupported, MailPriority priority) {
    setHtmlSupport(htmlSupported);
    setPriority(priority);
  }

  /**
   * Create MailComposer from the given mail data.
   * 
   * @param data
   *          the given mail data.
   * @param parseBody
   *          the given parse body flag.
   * @param parseAttach
   *          the given flag that mark system parse the attachment.
   * 
   * @throws MailException
   *           when error occurs during creating message composer.
   * @throws MessagingException
   *           when error occurs during parsing mail message.
   */
  public MessageComposer(byte[] data, boolean parseBody, boolean parseAttach) throws MailException,
      MessagingException, MailParserException {
    this(new MimeMessage(null, new ByteArrayInputStream(data)), parseBody, parseAttach);
  }

  /**
   * Create a new <tt>MessageComposer</tt> from the given mail {@link Session},
   * and the mail {@link Message} instance.
   * 
   * @param message
   *          the given mail {@link Message} instance.
   * @param parseBody
   *          the given flag that mark user need parse body data or not.
   * @param parseAttach
   *          the given flag that mark system parse the attachment.
   * 
   * @throws MailException
   *           when error occurs during creating <tt>MessageComposer</tt>
   *           instance.
   */
  public MessageComposer(Message message, boolean parseBody, boolean parseAttach)
      throws MailException, MailParserException {
    // this.session = session ;
    try {
      // get HTML supported format
      if (parseBody) {
        try {
          setBody(PartService.parse(message, true, false));
        } catch (Exception ex) {
        }
      }

      // get subject
      setSubject(MailService.getSubject(message));

      // get Receiver date
      setReceivedDate(MailService.getReceivedDate(message));

      // sent date
      setSentDate(MailService.getSent(message));

      // set message size.
      setSize(MailService.getSize(message));

      // set message priority.
      setPriority(MailService.getPriority(message));

      // get mail attachment.
      Map<String, byte[]> attachments = MailService.getAttachments(message, parseAttach, null);
      addAttachment(attachments);

      // set calendar.
      setCalendar(MailService.isCalendar(message));

      // set priority.
      setPriority(MailService.getPriority(message));

      // get all address from message
      Address[] addresses = getRecipients(message, "From");
      if (addresses != null && addresses.length > 0) {
        setFrom(addresses[0]);
      }

      // get to address.
      addresses = getRecipients(message, RecipientType.TO.toString());
      if (addresses != null && addresses.length > 0) {
        addTo(addresses);
      }

      // get CC address.
      addresses = getRecipients(message, RecipientType.CC.toString());
      if (addresses != null && addresses.length > 0) {
        addCc(addresses);
      }

      // get BCC address.
      addresses = getRecipients(message, RecipientType.BCC.toString());
      if (addresses != null && addresses.length > 0) {
        addBcc(addresses);
      }

      // check spam.
      MailApplicationContext context = MailApplicationContext.getInstance();
      ScanEngine engine = (context != null ? context.getSpamEngine() : null);
      if (engine != null) {
        ScanResult result = context.getSpamEngine().scan(message);
        setSpam((result.getProbability() - engine.getLevel()) >= 0.0001d);
      }
    } catch (IOException ioex) {
      throw new MailParserException("The message content is not formated as email message", ioex);
    } catch (MessagingException mex) {
      throw new MailParserException(mex.getMessage(), mex);
    }
  }

  // ~ Methods ===============================================================
  /**
   * Compose message
   * 
   * @see com.inet.mail.parser.IMessageComposer#compose()
   */
  public Message compose() throws MailException, MailParserException {
    try {

      // build header of this message
      final Message message = new MimeMessage((Session) null);
      message.setHeader("X-Mailer", MailService.getMailer());
      message.setHeader("Mime-Version", "1.0");

      // set priority.
      MailPriority priority = getPriority();
      if (priority != MailPriority.NORMAL) {
        // it is windows email
        message.setHeader("Importance", priority.getImportant());
        // it is UNIX email
        message.setHeader("X-Priority", String.valueOf(priority.getPriority()));
      }

      // add all address into this email before sending
      buildAddress(message);

      // set subject
      message.setSubject(MimeUtility.encodeText(getSubject(), MailService.DEFAULT_CHARSET,
          MailService.DEFAULT_ENCODING));

      // create the send date
      message.setSentDate(new Date());

      // get message attachment.
      Map<String, byte[]> attachments = getAttachments();

      // create the body (attachment)
      if (attachments != null && attachments.size() > 0) {
        addAttachment(message, isHtmlSupport(), getBody(), attachments);
      } else {
        String body = getBody();
        if (!isHtmlSupport()) {
          message.setContent(StringService.hasLength(body) ? HtmlParser.htmlToText(body)
              : StringService.EMPTY_STRING, "text/plain;charset=UTF-8");
        } else {
          message.setContent(StringService.hasLength(body) ? body : StringService.EMPTY_STRING,
              "text/html;charset=UTF-8");
        }
      }

      message.saveChanges();

      return message;
    } catch (ParserException pex) {
      throw new MailParserException("The address is bad format", pex);
    } catch (UnsupportedEncodingException ueex) {
      throw new MailException("The message is not supported UTF-8 endcoding", ueex);
    } catch (MessagingException mex) {
      throw new MailException(mex.getMessage(), mex);
    }
  }

  /**
   * @see com.inet.mail.parser.IMessageComposer#enterLine(java.lang.String)
   */
  public String enterLine(String data) {
    return PartService.enterLine(data);
  }

  /**
   * {@inheritDoc}
   * @see com.inet.mail.parser.IMessageComposer#toByte()
   */
  public byte[] toByte() throws MailException, MailParserException {
    // first compose the message.
    Message message = compose();
    if (message == null)
      return null;

    try {
      // write message to data.
      ByteArrayOutputStream data = new ByteArrayOutputStream();
      message.writeTo(data);

      // return the data to user.
      return data.toByteArray();
    } catch (IOException ioex) {
      throw new MailException("Could not get the mail message.", ioex);
    } catch (MessagingException mex) {
      throw new MailException("Could not get the mail message.", mex);
    }
  }

  // ~ Helper methods ========================================================
  /**
   * Build address before sending
   * 
   * @param message
   *          the given message to attach address.
   * 
   * @throws ParserException
   *           when error occurs during building message.
   * @throws MessagingException
   *           when error occurs during build message.
   */
  private void buildAddress(Message message) throws ParserException, MessagingException {
    // add all address into this email before sending
    Map<String, List<Address>> addresses = getAddresses();
    if (addresses != null && addresses.size() > 0) {
      for (String key : addresses.keySet()) {
        if (FROM_ADDRESS.equals(key)) {
          for (Address address : addresses.get(key)) {
            message.setFrom(MailService.getAddress(address));
          }

          // add the reply address
          message.setReplyTo(message.getFrom());
        } else if (TO_ADDRESS.equals(key)) {
          List<Address> listToAddress = addresses.get(key);
          if (listToAddress.size() > 0) {
            InternetAddress[] toAddress = new InternetAddress[listToAddress.size()];
            for (int index = 0; index < listToAddress.size(); index++) {
              toAddress[index] = MailService.getAddress(listToAddress.get(index));
            }
            // ToAddress
            message.addRecipients(Message.RecipientType.TO, toAddress);
          }
        } else if (CC_ADDRESS.equals(key)) {
          List<Address> listCcAddress = addresses.get(key);
          if (listCcAddress.size() > 0) {
            InternetAddress[] ccAddress = new InternetAddress[listCcAddress.size()];

            for (int index = 0; index < listCcAddress.size(); index++) {
              ccAddress[index] = MailService.getAddress(listCcAddress.get(index));
            }
            // CCAddress
            message.addRecipients(Message.RecipientType.CC, ccAddress);
          }
        } else if (BCC_ADDRESS.equals(key)) {
          List<Address> listBccAddress = addresses.get(key);
          if (listBccAddress.size() > 0) {
            InternetAddress[] bccAddress = new InternetAddress[listBccAddress.size()];

            // build BCC address.
            for (int index = 0; index < listBccAddress.size(); index++) {
              bccAddress[index] = MailService.getAddress(listBccAddress.get(index));
            }

            // BCCAddress
            message.addRecipients(Message.RecipientType.BCC, bccAddress);
          }
        }
      }
    }
  }

  /**
   * Add attachment and data to message.
   * 
   * @param message
   *          the given mail {@link Message}
   * @param htmlSupported
   *          the given HTML supported flag.
   * @param bodyText
   *          the given body text.
   * @param attachments
   *          the given list of attachment.
   * 
   * @throws MessagingException
   *           when error occurs during adding attachment.
   * @throws UnsupportedEncodingException
   *           when error occurs during formating message.
   */
  private void addAttachment(Message message, boolean htmlSupported, String bodyText,
      Map<String, byte[]> attachments) throws MessagingException, UnsupportedEncodingException {
    // Create the message part
    BodyPart messageBodyPart = new MimeBodyPart();

    // Fill the message
    if (!htmlSupported) {
      messageBodyPart.setContent(StringService.hasLength(bodyText) ? HtmlParser
          .htmlToText(bodyText) : StringService.EMPTY_STRING, "text/plain;charset=UTF-8");
    } else {
      messageBodyPart.setContent(StringService.hasLength(bodyText) ? bodyText
          : StringService.EMPTY_STRING, "text/html;charset=UTF-8");
    }

    // create mail message.
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);

    // add attachment.
    if (attachments != null && attachments.size() > 0) {
      for (String filename : attachments.keySet()) {
        // create attachment part.
        BodyPart attachBodyPart = new MimeBodyPart();

        int index = filename.indexOf(":");
        String filereal = (index > 0 ? filename.substring(index + 1) : filename);

        // set attachment type.
        String extFile = FileService.getExtension(filereal);
        String fType[] = MailService.getAttachType(extFile);

        if (fType[1].contains("/")) {
          attachBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachments
              .get(filename), fType[1])));
        } else {
          attachBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachments
              .get(filename), fType[0] + "/" + fType[1])));
        }

        // UTF-8 the file name
        attachBodyPart.setFileName(MimeUtility.encodeText(filereal, MailService.DEFAULT_CHARSET,
            MailService.DEFAULT_ENCODING));

        // add this part into this email
        multipart.addBodyPart(attachBodyPart);
      }
    }

    // Put parts in message
    message.setContent(multipart);
  }
}
