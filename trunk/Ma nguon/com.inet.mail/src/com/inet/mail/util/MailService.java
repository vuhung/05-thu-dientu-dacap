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
package com.inet.mail.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.inet.base.crypto.blowfish.Blowfish;
import com.inet.base.service.Base64Service;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailPriority;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.parser.HtmlParser;
import com.inet.mail.parser.ParserException;
import com.inet.mail.parser.rfc822.util.Rfc822Helper;
import com.inet.mail.service.PartService;

/**
 * MailService
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 28.12.2007
 * 
 *       <pre>
 * 	Initialization mail service.
 * </pre>
 */
public abstract class MailService {
  public static final String HTML_CONTENT_TYPE = "text/html";
  public static final String TEXT_CONTENT_TYPE = "text/plain";

  // Default character set.
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String ASCII_CHARSET = "ASCII";

  // parsing the received date.
  /* receiver date format. */
  protected static final SimpleDateFormat RECEIVED_DATE_FORMAT = new SimpleDateFormat(
      "EEE, d MMM yyyy HH:mm:ss Z");
  /* receiver date pattern. */
  protected static final Pattern RECEIVED_DATE_PATTERN = Pattern.compile("(;\\s*(.+))");

  // priority pattern.
  private static final Pattern PRIORITY_PATTERN = Pattern.compile("(\\d+)(.*)",
      Pattern.CASE_INSENSITIVE);

  // default encoding.
  public static final String DEFAULT_ENCODING = "B";

  /**
   * the given mail break line.
   */
  public static final String BREAK_LINE = " >>>>>>>>> ";
  /**
   * Application.
   */
  public static final String XAPPLICATION = "iNet Mail";
  /**
   * Version.
   */
  public static final String XVERSION = "1.0";

  /**
   * Undetermined (test not yet performed)
   */
  public static final int FORGERY_UNDETERMINED = -1;

  /**
   * Confirmed forgery
   */
  public static final int FORGERY_CONFIRMED = 1;

  /**
   * Confirmed authentic
   */
  public static final int FORGERY_REJECTED = 0;

  /**
   * Forgery status could not be determined with absolute certainty
   */
  public static final int FORGERY_UNKNOWN = 2;

  public static String[] ATTACHMENT_DISPOSITIONS = { MimeMessage.ATTACHMENT, MimeMessage.INLINE };

  // attachment type.
  private static Map<String, String[]> attachTypes = null;

  /**
   * 
   * @param message
   * @return
   * @throws MessagingException
   */
  @SuppressWarnings("unchecked")
  public static Header[] getAllHeaders(MimeMessage message) throws MessagingException {

    Header[] headers = null;
    Enumeration<Header> enu = message.getAllHeaders();

    // We have to use a Vector here because we can't get the size
    Vector<Header> vHeaders = new Vector<Header>();

    while (enu.hasMoreElements()) {
      vHeaders.add(enu.nextElement());
      headers = (Header[]) vHeaders.toArray(new Header[vHeaders.size()]);
    }
    return headers;
  }

  /**
   * Validate email address
   * 
   * @param address
   * @return
   */
  public static boolean isValidAddress(String address) {
    String regex = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    return address.matches(regex);
  }

  /**
   * Returns the domain component of an email address
   * 
   * @param emailAddress
   * @return The domain of the address (everything after the @)
   */
  public static String getDomainFromAddress(String emailAddress) {
    if (emailAddress != null) {
      return emailAddress.substring(emailAddress.indexOf("@") + 1, emailAddress.length());
    } else {
      return null;
    }

  }

  /**
   * Converts an Address object to an Internet Address with strict address
   * parsing
   * 
   * @param address
   * @return The parsed InternetAddress
   * @throws AddressException
   */
  public static InternetAddress toInternetAddress(javax.mail.Address address)
      throws AddressException {
    return toInternetAddress(address, true);
  }

  /**
   * Converts an Address object to an Internet Address
   * 
   * @param address
   * @param strict
   *          If true, address parsing is strict
   * @return The parsed InternetAddress
   * @throws AddressException
   */
  public static InternetAddress toInternetAddress(javax.mail.Address address, boolean strict)
      throws AddressException {
    if (address instanceof InternetAddress) {
      return (InternetAddress) address;
    } else {
      return InternetAddress.parse(address.toString(), strict)[0];
    }
  }

  /**
   * Encrypt password from the given account name and password.
   * 
   * @param account
   *          String - the given account name.
   * @param password
   *          String - the raw password.
   * @return the encrypted password.
   */
  public static String encrypt(String account, String password) {
    if (!StringService.hasLength(account) || !StringService.hasLength(password))
      return password;
    // create blowfish instance.
    Blowfish blowfish = new Blowfish(account);
    // encrypt password.
    try {
      return Base64Service.encodeString(blowfish.encrypt(password));
    } finally {
      blowfish.destroy();
    }
  }

  /**
   * Descrypt password from the given account name and password .
   * 
   * @param account
   *          String - the given account name.
   * @param password
   *          String - the given encrypted password.
   * @return the raw password.
   */
  public static String decrypt(String account, String password) {
    if (!StringService.hasLength(account) || !StringService.hasLength(password))
      return password;
    // create blowfish instance.
    Blowfish blowfish = new Blowfish(account);

    try {
      return blowfish.decryptString(Base64Service.decodeString(password));
    } finally {
      blowfish.destroy();
    }
  }

  /**
   * Get the attachment type.
   * 
   * @param ext
   *          String - the given attachment data extension.
   * @return the attachment type.
   */
  public static String[] getAttachType(String ext) {
    if (attachTypes == null) {
      // create format message.
      attachTypes = new HashMap<String, String[]>(26);

      // put information.
      attachTypes.put("swf", new String[] { "application", "application/x-shockwave-flash" });
      attachTypes.put("xhtml", new String[] { "application", "application/xhtml+xml" });
      attachTypes.put("html", new String[] { "application", "text/html" });
      attachTypes.put("odt", new String[] { "application",
          "application/vnd.oasis.opendocument.text" });
      attachTypes.put("doc", new String[] { "application", "application/msword" });
      attachTypes.put("rtf", new String[] { "application", "text/rtf" });
      attachTypes.put("wpd", new String[] { "application", "application/wordperfect" });
      attachTypes.put("txt", new String[] { "application", "text/plain" });
      attachTypes.put("ods", new String[] { "application",
          "application/vnd.oasis.opendocument.spreadsheet" });
      attachTypes.put("xls", new String[] { "application", "application/vnd.ms-excel" });
      attachTypes.put("csv", new String[] { "application", "text/csv" });
      attachTypes.put("tsv", new String[] { "application", "text/tab-separated-values" });
      attachTypes.put("odp", new String[] { "application",
          "application/vnd.oasis.opendocument.presentation" });
      attachTypes.put("ppt", new String[] { "application", "application/vnd.ms-powerpoint" });
      attachTypes.put("pdf", new String[] { "application", "application/pdf" });
      attachTypes.put("odg", new String[] { "application",
          "application/vnd.oasis.opendocument.graphics" });
      attachTypes.put("eml", new String[] { "message", "rfc822" });
      attachTypes.put("tar", new String[] { "application", "application/x-compressed-tar" });
      attachTypes.put("zip", new String[] { "application", "application/x-compressed-zip" });
      attachTypes.put("gzip", new String[] { "application", "application/x-compressed-gzip" });
      attachTypes.put("svg", new String[] { "application", "image/svg+xml" });
      attachTypes.put("ldif", new String[] { "application", "text/x-ldif" });
      attachTypes.put("gif", new String[] { "application", "image/gif" });
      attachTypes.put("bmp", new String[] { "application", "image/bmp" });
      attachTypes.put("png", new String[] { "application", "image/png" });
      attachTypes.put("jpg", new String[] { "application", "image/jpg" });
      attachTypes.put("jpeg", new String[] { "application", "image/jpeg" });
    }

    // the attachment is validate.
    if (MailService.attachTypes.containsKey(ext)) {
      return MailService.attachTypes.get(ext);
    }

    // return the attachment type.
    return new String[] { "application", "application/octet-stream" };
  }

  /**
   * Build the mail address to the list of address.
   * 
   * @param addresses
   *          List<Address> - the given list of address.
   * @return the list of the mail addresses as string.
   */
  public static List<String> buildAddress(List<Address> addresses) {
    if (addresses == null || addresses.size() == 0)
      return null;

    // create list of email address as string.
    List<String> data = new ArrayList<String>(addresses.size());
    for (final Address address : addresses) {
      if (StringService.hasLength(address.getDisplayName())) {
        data.add(address.getFullAddress());
      } else {
        data.add(address.getAddress());
      }
    }

    // return list of email.
    return data;
  }
  
  /**
   * Build the mail address to the display address.
   * 
   * @param addresses
   *          List<Address> - the given list of address.
   * @return the mail addresses as string.
   */
  public static String buildRecipientsAddress(List<Address> addresses) {
    if (addresses == null || addresses.size() == 0)
      return null;

    StringBuffer buffer = new StringBuffer();
    boolean flag = false;
    for (final Address address : addresses) {
      buffer.append(flag?", ":StringService.EMPTY_STRING);
      
      if (StringService.hasLength(address.getDisplayName())) {
        buffer.append(address.getDisplayName());
      } else {
        buffer.append(address.getAddress());
      }
      flag = true;
    }

    String recipients = buffer.toString();
    if(recipients.length() > 256){
      recipients = recipients.substring(1, 256);
    }  
    return recipients;
  }

  /**
   * @return the mailer name and version.
   */
  public static String getMailer() {
    return MailService.XAPPLICATION + '-' + MailService.XVERSION;
  }

  /**
   * @param address
   *          {@link Address} - the given address.
   * @return the InternetAddress address.
   * @throws ParserException
   *           if an error occurs during converting address.
   */
  public static InternetAddress getAddress(Address address) throws ParserException {
    try {
      if (StringService.hasLength(address.getDisplayName())) {
        return new InternetAddress(address.getAddress(), address.getDisplayName(),
            MailService.DEFAULT_CHARSET);
      } else {
        return new InternetAddress(address.getAddress());
      }
    } catch (UnsupportedEncodingException ueex) {
      // throw exception.
      throw new ParserException(ueex.getMessage(), ueex);
    } catch (AddressException aex) {
      // throw exception.
      throw new ParserException(aex.getMessage(), aex);
    }
  }

  /**
   * @param address
   *          List<Address> - the given list of address.
   * @return the list of InternetAddress addresses.
   * @throws ParserException
   *           if an error occurs during converting address.
   */
  public static InternetAddress[] getAddress(List<Address> addresses) throws ParserException {
    // create Internet addresses.
    List<InternetAddress> inetAddrs = new ArrayList<InternetAddress>(addresses.size());

    for (Address address : addresses) {
      inetAddrs.add(MailService.getAddress(address));
    }

    // return list of Internet address.
    return inetAddrs.toArray(new InternetAddress[inetAddrs.size()]);
  }

  /**
   * Convert from the sun mail to inet address.
   * 
   * @param addresses
   *          Address[] - the given list of sun mail addresses.
   * @return the list of iNet address.
   * @throws ParserException
   *           if error occur during converting address.
   */
  public static List<Address> getAddress(javax.mail.Address[] addresses) throws ParserException {
    if (addresses == null || addresses.length == 0)
      return null;

    try {
      // create list of iNet mail address.
      List<Address> address = new ArrayList<Address>(addresses.length);
      for (javax.mail.Address addr : addresses) {
        InternetAddress internetAddress = (InternetAddress) addr;
        // add address.
        if (StringService.hasLength(internetAddress.getPersonal())) {
          address.add(new Address(MimeUtility.decodeText(internetAddress.getPersonal()),
              MimeUtility.decodeText(internetAddress.getAddress())));
        } else {
          address.add(new Address(MimeUtility.decodeText(internetAddress.getAddress())));
        }

        // the mail is group, retrieved all mail in group and add to
        // list of mail.
        if (internetAddress.isGroup()) {
          // retrieved the mail address.
          InternetAddress[] internetAddresses = internetAddress.getGroup(false);

          for (InternetAddress inetAddr : internetAddresses) {
            if (StringService.hasLength(inetAddr.getPersonal())) {
              address.add(new Address(MimeUtility.decodeText(inetAddr.getPersonal()), MimeUtility
                  .decodeText(inetAddr.getAddress())));
            } else {
              address.add(new Address(MimeUtility.decodeText(inetAddr.getAddress())));
            }
          }
        }
      }

      // return the list of iNet mail address.
      return address;
    } catch (AddressException aex) {
      // throw exception.
      throw new ParserException(aex.getMessage(), aex);
    } catch (UnsupportedEncodingException ueex) {
      throw new ParserException("Unsupport address encoding, message: [" + ueex.getMessage() + "]",
          ueex);
    }
  }

  /**
   * Convert from the sun mail to list of address in string.
   * 
   * @param addresses
   *          Address[] - the given list of sun mail addresses.
   * @return the list of iNet address.
   * @throws ParserException
   *           when error occur during converting addresses.
   */
  public static String[] getAddressAsString(javax.mail.Address[] addresses) throws ParserException {
    if (addresses == null || addresses.length == 0)
      return null;

    try {
      // create list of iNet mail address.
      List<String> address = new ArrayList<String>(addresses.length);
      for (javax.mail.Address addr : addresses) {
        InternetAddress internetAddress = (InternetAddress) addr;

        // add address.
        if (StringService.hasLength(internetAddress.getPersonal())) {
          address.add(new Address(MimeUtility.decodeText(internetAddress.getPersonal()),
              MimeUtility.decodeText(internetAddress.getAddress())).getFullAddress());
        } else {
          address.add(new Address(MimeUtility.decodeText(internetAddress.getAddress()))
              .getFullAddress());
        }

        // the mail is group, retrieved all mail in group and add to
        // list of mail.
        if (internetAddress.isGroup()) {
          // retrieved the mail address.
          InternetAddress[] internetAddresses = internetAddress.getGroup(false);
          for (InternetAddress inetAddr : internetAddresses) {
            if (StringService.hasLength(inetAddr.getPersonal())) {
              address.add(new Address(MimeUtility.decodeText(inetAddr.getPersonal()), MimeUtility
                  .decodeText(inetAddr.getAddress())).getFullAddress());
            } else {
              address.add(new Address(MimeUtility.decodeText(inetAddr.getAddress()))
                  .getFullAddress());
            }
          }
        }
      }
      // return the list of iNet mail address.
      return address.toArray(new String[address.size()]);
    } catch (AddressException aex) {
      // throw exception.
      throw new ParserException(aex.getMessage(), aex);
    } catch (UnsupportedEncodingException ueex) {
      throw new ParserException("Unsupport address encoding, message: [" + ueex.getMessage() + "]",
          ueex);
    } catch (Exception ex) {
      throw new ParserException("Unknow exception,  message: [" + ex.getMessage() + "]", ex);
    }
  }

  /**
   * Get size of the email
   * 
   * @return
   * @throws MessagingException
   */
  public static int getSize(Message message) throws MessagingException {
    return message.getSize();
  }

  /**
   * 
   * @return
   * @throws MessagingException
   */
  public static Date getSent(Message message) throws MessagingException {
    return message.getSentDate();
  }

  /**
   * 
   * @return
   * @throws MessagingException
   */
  public static Date getReceivedDate(Message message) throws MessagingException {
    Date receiverDate = message.getReceivedDate();
    if (receiverDate == null) {
      try {
        // get lasted received header.
        String header = message.getHeader("Received")[0];
        header = header.replaceAll("[\r\n|\n]", " ");

        // get the received date.
        Matcher matcher = RECEIVED_DATE_PATTERN.matcher(header);
        if (matcher.find()) {
          String receivedDate = matcher.group(2);
          receiverDate = RECEIVED_DATE_FORMAT.parse(receivedDate);
        }
      } catch (MessagingException mex) {
      } catch (RuntimeException rex) {
      } catch (ParseException pex) {
      }
    }

    return (receiverDate == null ? new Date() : receiverDate);
  }

  /**
   * Get priority of the message
   * 
   * @param message
   * @return
   * @throws MessagingException
   */
  @SuppressWarnings("unchecked")
  public static MailPriority getPriority(Message message) throws MessagingException {
    try {
      // from header is most of cases
      String[] priorities = message.getHeader("Importance");
      if (priorities == null || priorities.length == 0) {
        priorities = message.getHeader("X-Priority");
        Enumeration enu = message.getAllHeaders();
        while (enu.hasMoreElements()) {
          Object obj = enu.nextElement();
          if (obj instanceof Header) {
            Header header = (Header) obj;
            if (!header.getName().equalsIgnoreCase("X-Priority")
                && header.getName().indexOf("-Priority") > 0) {
              // this is really in UNIX email server
              priorities = new String[] { header.getValue() };
              break;
            }
          }
        }
      }

      if (priorities == null || priorities.length == 0)
        return MailPriority.NORMAL;

      // match the pattern.
      Matcher matcher = PRIORITY_PATTERN.matcher(priorities[0]);

      if (matcher.find()) {
        // get the priority value.
        String priorityValue = matcher.group(1);

        // convert to priority.
        int priority = Integer.parseInt(priorityValue) - 1;

        if (priority >= MailPriority.values().length)
          priority = MailPriority.values().length - 1;
        else if (priority < 0)
          priority = 0;

        return MailPriority.values()[priority];
      } else {
        int priority = 1;
        if (priorities[0].toLowerCase().indexOf("low") >= 0)
          priority = 0;
        else if (priorities[0].toLowerCase().indexOf("normal") >= 0)
          priority = 1;
        else if (priorities[0].toLowerCase().indexOf("high") >= 0)
          priority = 2;
        return MailPriority.values()[priority];
      }
    } catch (MessagingException mex) {
      // throw exception.
      throw new MailException(mex.getMessage(), mex);
    }
  }

  /**
   * @return the mail subject.
   * 
   * @throws MessagingException when error occurs during retrieving the mail subject.
   */
  public static String getSubject(final Message message) throws MessagingException {
    // get subject value.
    final String[] rawvalues = message.getHeader("Subject");
    final String rawvalue;

    if (rawvalues != null && rawvalues.length > 0) {
      rawvalue = rawvalues[0];
    } else {
      rawvalue = null;
    }

    // get subject content.
    try {
      return (StringService.isset(rawvalue)
             ? Rfc822Helper.decodeSubject(MimeUtility.unfold(rawvalue))
             : StringService.EMPTY_STRING);
    } catch (final Exception ex) {
      return message.getSubject();
    }
  }

  /**
   * Detect a message is body text or attachment
   * 
   * @param part
   * @return
   */
  public static boolean isBodyText(Part part) {
    return isText(part);
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isHtml(Part part) {
    try {
      return part.isMimeType(HTML_CONTENT_TYPE);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isText(Part part) {
    try {
      return part.isMimeType("text/*");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isAlternative(Part part) {
    try {
      return part.isMimeType("multipart/alternative");
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isRelated(Part part) {
    try {
      return part.isMimeType("multipart/related");
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isPlain(Part part) {
    try {
      return part.isMimeType(TEXT_CONTENT_TYPE);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isRFC822(Part part) {
    try {
      return part.isMimeType("message/rfc822");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isMultipart(Part part) {
    try {
      return part.isMimeType("multipart/*");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 
   * @param part
   * @return
   */
  public static boolean isCalendar(Part part) {
    try {
      return part.isMimeType("text/calendar");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get Body content in the content format
   * 
   * @param message
   * @param contentType
   * @return
   * @throws Exception
   */
  public static String getBody(Message message, String contentType) throws Exception {
    Hashtable<String, String> bodyParts = getBody(message);
    for (String key : bodyParts.keySet()) {
      if (key.startsWith(contentType))
        return bodyParts.get(key);
    }
    return null;
  }

  /**
   * Get Body content as HTML format
   * 
   * @param message
   * @return
   * @throws Exception
   */
  public static String getBodyHTML(Message message) throws MessagingException, IOException {
    Hashtable<String, String> bodyParts = getBody(message);
    String bodyText = null;
    for (String key : bodyParts.keySet()) {
      if (bodyText == null && key.startsWith(TEXT_CONTENT_TYPE))
        bodyText = HtmlParser.textToHtml(bodyParts.get(key), "iDesk-View", null, DEFAULT_CHARSET);
      if (key.startsWith(HTML_CONTENT_TYPE))
        bodyText = bodyParts.get(key);
    }
    return bodyText;
  }

  /**
   * Load content
   * 
   * @param message
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static Hashtable<String, String> getBody(Message message) throws MessagingException,
      IOException {
    final Hashtable<String, String> bodyParts = new Hashtable<String, String>();

    if (isBodyText(message)) {
      String ctype = message.getContentType();
      boolean plainText = isPlain(message);

      StringBuffer buffer = new StringBuffer();
      BufferedReader xreader = getTextReader(message);
      for (String xline; (xline = xreader.readLine()) != null;) {
        buffer.append(xline);

        if (plainText)
          buffer.append(IOService.LINE_SEPARATOR);
      }
      bodyParts.put(ctype, buffer.toString());
    } else {
      Multipart mpart = (Multipart) message.getContent();
      for (int cnt = 0; cnt < mpart.getCount(); cnt++) {
        Part part = mpart.getBodyPart(cnt);
        // read alternative content and body text as well
        if (isBodyText(part) || isAlternative(part) || isRelated(part)) {
          bodyPartParser(part, bodyParts);
        }
      }
    }

    return bodyParts;
  }

  /**
   * Get body data in many type of content
   * 
   * @param multipart
   * @param contentType
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  private static void bodyPartParser(Part part, Hashtable<String, String> bodyParts)
      throws MessagingException, IOException {
    // if the content is Plain Text, we read this content
    if (isBodyText(part)) {
      String contentType = part.getContentType();
      if (bodyParts.containsKey(contentType))
        return;

      boolean plainText = isPlain(part);

      StringBuffer buffer = new StringBuffer();
      BufferedReader xreader = getTextReader(part);
      for (String xline; (xline = xreader.readLine()) != null;) {
        if (plainText)
          buffer.append(xline + '\n');
        else
          buffer.append(xline);
      }

      bodyParts.put(contentType, buffer.toString());
    } else if (isMultipart(part) || isAlternative(part)) {
      Multipart mpart = (Multipart) part.getContent();
      int count = mpart.getCount();

      for (int cnt = 0; cnt < count; cnt++) {
        Part bpart = mpart.getBodyPart(cnt);
        // read alternative content and body text as well
        if (isBodyText(bpart) || isAlternative(bpart)) {
          bodyPartParser(bpart, bodyParts);
        }
      }
    }
  }

  /**
   * 
   * @param part
   * @return
   * @throws MessagingException
   */
  public static BufferedReader getTextReader(Part part) throws MessagingException {
    try {
      InputStream xis = part.getInputStream();
      String ctype = part.getContentType();
      String xjcharset = getCharset(ctype);

      if (xjcharset != null) {
        xjcharset = MimeUtility.javaCharset(xjcharset);
      } else {
        // assume ASCII character encoding
        xjcharset = MimeUtility.javaCharset(DEFAULT_CHARSET);
      }

      InputStreamReader inReader = null;

      try {
        inReader = new InputStreamReader(xis, xjcharset);
      } catch (UnsupportedEncodingException ex) {
        inReader = null;
      } catch (IllegalArgumentException ex) {
        inReader = null;
      }

      if (inReader == null) {
        xjcharset = MimeUtility.javaCharset(ASCII_CHARSET);
        inReader = new InputStreamReader(xis, xjcharset);
      }

      BufferedReader xreader = new BufferedReader(inReader);
      return xreader;
    } catch (IOException xex) {
      throw new MessagingException(xex.toString());
    }
  }

  /**
   * Read the content type
   * 
   * @param ctype
   * @return
   */
  public static String getCharset(String ctype) throws MessagingException {
    ContentType xct = new ContentType(ctype.toLowerCase());
    return xct.getParameter("charset");
  }

  /**
   * Get all attachments name (without content)
   * 
   * @param message
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static Set<String> listAttachments(Message message) throws MessagingException, IOException {
    return getAttachments(message, false, null).keySet();
  }

  /**
   * Get all attachments with content
   * 
   * @param data
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static HashMap<String, byte[]> dataAttachment(byte[] data) throws MessagingException,
      IOException {
    Message message = new MimeMessage(null, new ByteArrayInputStream(data));
    return dataAttachment(message);
  }

  /**
   * Get all attachments with content
   * 
   * @param message
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static HashMap<String, byte[]> dataAttachment(Message message) throws MessagingException,
      IOException {
    return getAttachments(message, true, null);
  }

  /**
   * Read file attachment
   * 
   * @param data
   * @param fileindex
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static byte[] dataAttachment(byte[] data, String fileindex) throws MessagingException,
      IOException {
    Message message = new MimeMessage(null, new ByteArrayInputStream(data));
    return dataAttachment(message, fileindex);
  }

  /**
   * Get attachment with content
   * 
   * @param message
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static byte[] dataAttachment(Message message, String fileindex) throws MessagingException,
      IOException {
    HashMap<String, byte[]> attachment = getAttachments(message, true, fileindex);
    if (attachment != null && attachment.size() > 0) {
      if (attachment.containsKey(fileindex)) {
        return attachment.get(fileindex);
      } else {
        // get prefix attachment.
        String prefix = AttachmentParser.getNumberOrder(fileindex);
        prefix += ":";

        for (String key : attachment.keySet()) {
          if (key.startsWith(prefix))
            return attachment.get(key);
        }
      }
    }

    return null;
  }

  /**
   * Get all attachments in this email
   * 
   * @param message
   * @param content
   * @param fileindex
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  public static HashMap<String, byte[]> getAttachments(Message message, boolean content,
      String fileindex) throws MessagingException, IOException {
    HashMap<String, byte[]> attachments = new HashMap<String, byte[]>();
    ByteArrayOutputStream stream = null;
    // attach data or forward!
    if (isMultipart(message)) {
      Multipart mpart = (Multipart) message.getContent();
      int cntAtt = mpart.getCount();
      for (int cnt = 0; cnt < cntAtt; cnt++) {
        Part part = mpart.getBodyPart(cnt);
        // get sub attachments if any
        if (isMultipart(part)) {
          HashMap<String, byte[]> subAttachments = getAttachments(part, content, cntAtt, fileindex);
          if (subAttachments != null && subAttachments.size() > 0)
            attachments.putAll(subAttachments);
        } else if (isRFC822(part)) {
          // this also attached with email message too
          if (content)
            stream = new ByteArrayOutputStream();
          String filename = getRFC822AsAttachment(part, cnt, stream, fileindex);
          if (filename != null)
            attachments.put(filename, (stream == null ? null : stream.toByteArray()));
        } else if (isAttached(part)) {
          // this also attached with email message too
          if (content)
            stream = new ByteArrayOutputStream();
          String filename = getAttachmentOrInline(part, cnt, stream, fileindex);
          if (filename != null)
            attachments.put(filename, (stream == null ? null : stream.toByteArray()));
        }

        // we just copy ONLY ONE file from this email
        if (fileindex != null && attachments.size() > 0)
          return attachments;
      }
    } else if (isRFC822(message)) {
      if (content)
        stream = new ByteArrayOutputStream();
      String filename = getRFC822AsAttachment(message, 0, stream, fileindex);
      if (filename != null)
        attachments.put(filename, (stream == null ? null : stream.toByteArray()));
    } else if (isAttached(message)) {
      if (content)
        stream = new ByteArrayOutputStream();
      String filename = getAttachmentOrInline(message, 0, stream, fileindex);
      if (filename != null)
        attachments.put(filename, (stream == null ? null : stream.toByteArray()));
    }

    return attachments;
  }

  /**
   * Get all attachments in this email
   * 
   * @param part
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  private static HashMap<String, byte[]> getAttachments(Part part, boolean content, int index,
      String fileindex) throws MessagingException, IOException {
    // no attachment
    if (!isMultipart(part))
      return null;

    HashMap<String, byte[]> attachments = new HashMap<String, byte[]>();
    ByteArrayOutputStream stream = null;
    // attach data or forward!
    Multipart mpart = (Multipart) part.getContent();
    int cntAtt = mpart.getCount();
    for (int cnt = 0; cnt < cntAtt; cnt++) {
      Part part0 = mpart.getBodyPart(cnt);
      // get sub attachments if any
      if (isMultipart(part0)) {
        HashMap<String, byte[]> subAttachments = getAttachments(part0, content, index + cntAtt,
            fileindex);
        if (subAttachments != null && subAttachments.size() > 0)
          attachments.putAll(subAttachments);
      } else if (isRFC822(part0)) {
        if (content)
          stream = new ByteArrayOutputStream();
        String filename = getRFC822AsAttachment(part0, index + cnt, stream, fileindex);
        if (filename != null)
          attachments.put(filename, (stream == null ? null : stream.toByteArray()));
      } else if (isAttached(part0)) {
        if (content)
          stream = new ByteArrayOutputStream();
        String filename = getAttachmentOrInline(part0, index + cnt, stream, fileindex);
        if (filename != null)
          attachments.put(filename, (stream == null ? null : stream.toByteArray()));
      }

      // we just copy ONLY ONE file from this email
      if (fileindex != null && attachments.size() > 0)
        return attachments;
    }

    return attachments;
  }

  /**
   * 
   * @param part
   * @param cnt
   * @param stream
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  private static String getAttachmentOrInline(Part part, int cnt, ByteArrayOutputStream stream,
      String fileindex) throws MessagingException, IOException {
    if (isAttached(part)) {
      String filename = PartService.getFilename(part);

      if (!StringService.isset(filename)) {
        filename = "unknown.unk";
      }

      // don't download this content because of not matching as fileindex
      if (fileindex != null && !fileindex.startsWith(cnt + ":")) {
        return null;
      }

      // get content
      if (stream != null) {
        DataHandler dhandle = part.getDataHandler();
        if (dhandle != null)
          dhandle.writeTo(stream);
      }

      return cnt + ":" + filename;
    }

    return null;
  }

  /**
   * Get rfc822 as attachment
   * 
   * @param part
   * @param cnt
   * @param stream
   * @param fileindex
   * @return
   * @throws MessagingException
   * @throws IOException
   */
  private static String getRFC822AsAttachment(Part part, int cnt, ByteArrayOutputStream stream,
      String fileindex) throws MessagingException, IOException {

    // get filename
    String filename = PartService.getFilename(part);
    if (StringService.hasLength(filename)) {
      if (!(filename.toLowerCase()).endsWith(".eml")) {
        filename += ".eml";
      }
    } else {
      filename = "Forwarded.eml";
    }

    // don't download this content because of not matching as file index
    // if (fileindex != null && !fileindex.equals(cnt + ":" + filename)){
    // return null;
    // }

    if (fileindex != null && !fileindex.startsWith(cnt + ":")) {
      return null;
    }

    // get content
    if (stream != null) {
      DataHandler dhandle = part.getDataHandler();
      if (dhandle != null)
        dhandle.writeTo(stream);
    }

    return cnt + ":" + filename;
  }

  /**
   * This is also attachement
   * 
   * @param part
   * @return
   */
  public static boolean isAttached(Part part) {
    try {
      // this is really all cases matched
      if (part.isMimeType("multipart/mixed"))
        return true;

      // but sometime, it is not case above actually
      String disposition = part.getDisposition();
      if (disposition != null) {
        if (disposition.equals(Part.ATTACHMENT))
          return true;

        String filename = PartService.getFilename(part);
        if (disposition.equals(Part.INLINE) && StringService.hasLength(filename))
          return true;

        // the attachment is not text line anyway
        if (isText(part))
          return false;

        // if this is INLINE data anyway
        return disposition.equals(Part.INLINE);
      }
    } catch (Exception ex) {
    }
    return false;
  }

  /**
   * Download content from the message
   * 
   * @param message
   * @return
   * @throws MessagingException
   */

  public static byte[] downloadMessage(Message message) throws MessagingException {
    try {
      // read size of memory to be saved this email content
      ByteArrayOutputStream data = new ByteArrayOutputStream();
      message.writeTo(data);
      return data.toByteArray();
    } catch (IOException e) {
      throw new MessagingException("Bad IO execution.", e);
    }
  }

  /**
   * Build the ticket from the prefix ticket and suffix ticket value.
   * 
   * @param prefix
   *          the prefix ticket value.
   * @param suffix
   *          the suffix ticket value.
   * @return the ticket value.
   */
  public static String createTicket(String prefix, String suffix) {
    StringBuilder builder = new StringBuilder();

    builder.append((!StringService.hasLength(prefix) ? StringService.EMPTY_STRING : prefix))
        .append(':').append(
            (!StringService.hasLength(suffix) ? StringService.EMPTY_STRING : suffix));

    return builder.toString();
  }

  /**
   * Get list of ticket data.
   * 
   * @param ticket
   *          the given ticket.
   * @return the list of ticket data.
   */
  public static String[] getTicketData(String ticket) {
    if (!StringService.hasLength(ticket))
      return new String[] { StringService.EMPTY_STRING, StringService.EMPTY_STRING };

    // return ticket data.
    int index = ticket.indexOf(":");
    if (index <= 0)
      return new String[] { ticket, StringService.EMPTY_STRING };
    return new String[] { ticket.substring(0, index), ticket.substring(index + 1) };
  }

  /**
   * Get user name from ticket
   * 
   * @param ticket
   *          the given ticket value.
   * @return the user name of ticket.
   */
  public static String getUsername(String ticket) {
    return getTicketData(ticket)[1];
  }

  /**
   * Get priority
   * 
   * @param priority
   * @return
   */
  public static int getPriority(String priority) {
    // match the pattern.
    Matcher matcher = PRIORITY_PATTERN.matcher(priority);

    if (matcher.find()) {
      // get the priority value.
      String priorityValue = matcher.group(1);
      // convert to priority.
      return Integer.parseInt(priorityValue);
    } else {
      priority = priority.toLowerCase();
      return ((priority.indexOf("low") >= 0) ? 0 : (priority.indexOf("high") >= 0 ? 2 : 1));
    }
  }

  /**
   * convert size of mail size to valid format size
   * 
   * @param size
   *          the given size of message.
   * @return the valid format
   */
  public static String getByteSize(int size) {
    if (size <= 0)
      return StringService.EMPTY_STRING;
    int bSize = size;
    float kbSize = bSize / 1024.0f;
    if (kbSize < 1) {
      return String.valueOf(bSize) + " bytes";
    } else if (kbSize >= 1 && kbSize < 1024) {
      return String.valueOf(get2Decimal(kbSize)) + " KB";
    } else {
      return String.valueOf(get2Decimal(kbSize / 1024f)) + " MB";
    }
  }

  /**
   * @return the two decimal data.
   */
  private static float get2Decimal(float f) {
    float temp = Math.round(f * 100.0f);
    return temp / 100;
  }
}
