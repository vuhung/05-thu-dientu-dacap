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
package com.inet.mail.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.inet.base.service.StringService;
import com.inet.mail.parser.HtmlParser;
import com.inet.mail.parser.ParserException;
import com.inet.mail.util.MailService;

/**
 * PartService.
 * 
 * <pre>
 *   + Support encoding 7bit.
 * </pre>
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PartService.java 2008-12-10 21:39:32Z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * 
 * <pre>
 *  Initialization PartService class.
 * </pre>
 */
public abstract class PartService {
  // ~ Static fields =========================================================
  /**
   * the message attachment extension.
   */
  private static final String MESSAGE_EXTENSION = ".eml";
  /**
   * Default message attachment name.
   */
  private static final String DEF_MSG_NAME = "Forwarded message.eml";
  /**
   * The unknown message attachment.
   */
  private static final String UNKNOWN_ATTACHMENT = "unknown.unk";
  /**
   * Default view.
   */
  private static final String DEFAULT_VIEW = "iMail-View";

  /* disposition file name style. */
  private static final String FILENAME_STYLE = "filename*";
  /* disposition content name. */
  private static final String DISPOSITION_CONTENT_NAME = "Content-Disposition";
  /* content type name style. */
  private static final String NAME_STYLE = "name*";

  // ~ Methods ===============================================================
  /**
   * Parses the given part.
   *
   * @param part Part the given mail part.
   * @param htmlSupported if {@code true} will be formatted data under HTML, otherwise
   * {@code false} will return the normal format.
   * @param first if {@code true} will be get the first part content, otherwise try to get the
   * next part content.
   *
   * @return the part message under string formatted.
   * @throws ParserException if an error occurs during parsing data.
   */
  public static String parse(final Part part, final boolean htmlSupported, final boolean first)
      throws ParserException {
    if (PartService.isBodyText(part)) {
      // the part is text.
      return PartService.parseText(part, htmlSupported);
    } else if (PartService.isAlternative(part)) {
      // the part is alternative.
      return PartService.parseAlternative(part, htmlSupported, first);
    } else if (PartService.isMultipart(part)) {
      // the part is multi-part but not alternative part.
      return PartService.parseMultipart(part, htmlSupported, first);
    }

    // return result.
    return StringService.EMPTY_STRING;
  }

  /**
   * Appends the break-line to the data.
   * 
   * @param data String - the given data.
   * @return the formatted message.
   */
  public static String enterLine(String data) {
    return MailService.BREAK_LINE + (StringService.isset(data) ? data : StringService.EMPTY_STRING)
        + MailService.BREAK_LINE;
  }

  /**
   * Parses the message mail part.
   * 
   * @param part Part - the given message mail part.
   * @param stream OutputStream - the given output stream.
   * @return the attachment data.
   * @throws ParserException if an error occurs during parsing data.
   */
  public static String parseMessage(Part part, OutputStream stream) throws ParserException {
    try {
      // check the stream.
      if (stream != null) {
        // get attachment.
        DataHandler dataHandler = part.getDataHandler();

        // write to output stream.
        if (dataHandler != null) {
          dataHandler.writeTo(stream);
        }
      }

      // get attachment name.
      String filename = getFilename(part);
      if (StringService.hasLength(filename)) {
        // check the file is ended with EML.
        if (!filename.toLowerCase().endsWith(PartService.MESSAGE_EXTENSION)) {
          filename += PartService.MESSAGE_EXTENSION;
        }

        return "0:" + filename;
      } else {
        return "0:" + PartService.DEF_MSG_NAME;
      }
    } catch (MessagingException mex) {
      // throw exception.
      throw new ParserException(mex.getMessage(), mex);
    } catch (IOException ioex) {
      // throw exception.
      throw new ParserException(ioex.getMessage(), ioex);
    }
  }

  /**
   * Parses the attachment from the given part, the attachment index and the stream content this
   * attachment.
   * 
   * @param part Part - the given part to be parsed attachment.
   * @param cnt int - the given attachment index.
   * @param stream OutputStream - the given storage that store attachment content.
   * @return the attachment file name.
   * @throws ParserException if an error occurs during parsing attachment.
   */
  public static String parseAttachment(Part part, int cnt, OutputStream stream)
      throws ParserException {
    try {
      // get file name.
      String filename = getFilename(part);

      if (!StringService.hasLength(filename)) {
        filename = PartService.UNKNOWN_ATTACHMENT;
      }

      // get attachment content.
      if (stream != null) {
        // get data handler.
        DataHandler dataHandler = part.getDataHandler();
        if (dataHandler != null) {
          dataHandler.writeTo(stream);
        }
      }

      // return attachment information.
      return String.valueOf(cnt) + ":" + filename;
    } catch (MessagingException mex) {
      // throw parser exception.
      throw new ParserException(mex.getMessage(), mex);
    } catch (IOException ioex) {
      // throw parser exception.
      throw new ParserException(ioex.getMessage(), ioex);
    }
  }

  /**
   * Parses the message mail part.
   * 
   * @param part Part - the given message mail part.
   * @param htmlSupported boolean - <code>true</code> will be formatted data under HTML, otherwise
   *          <code>false</code>.
   * @param first boolean - <code>true</code> will be get part content, otherwise <code>false</code>
   *          .
   * @return the mail part content.
   * @throws ParserException if an error occurs during parsing data.
   */
  @Deprecated
  public static String parseMessage(Part part, boolean htmlSupported, boolean first)
      throws ParserException {
    try {
      // get the body part and parse event.
      BodyPart bodyPart = (BodyPart) part.getContent();
      return PartService.parse(bodyPart, htmlSupported, first);
    } catch (MessagingException mex) {
      // throw exception.
      throw new ParserException(mex.getMessage(), mex);
    } catch (IOException ioex) {
      // throw exception.
      throw new ParserException(ioex.getMessage(), ioex);
    }
  }

  /**
   * Checks the part is calendar part.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> if the given part is calendar part, otherwise <code>false</code>
   */
  public static boolean isCalendar(Part part) {
    if (part == null)
      return false;
    try {
      return part.isMimeType("text/calendar");
    } catch (MessagingException mex) {
      // does not calendar part.
      return false;
    }
  }

  /**
   * Checks the part is attachment part.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> if the given part is attachment part, otherwise <code>false</code>
   */
  public static boolean isAttached(Part part) {
    if (part == null)
      return false;
    try {
      // get part disposition.
      String disposition = part.getDisposition();
      if (!StringService.hasLength(disposition))
        return false;

      // the disposition is attachment.
      if (Part.ATTACHMENT.equals(disposition))
        return true;

      // the attachment is text.
      if (PartService.isText(part))
        return false;

      // the part is inline attachment.
      return Part.INLINE.equals(disposition);
    } catch (MessagingException mex) {
      // does not attachment part.
      return false;
    }
  }

  /**
   * Checks the part is multipart.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> if the given part is attachment part, otherwise <code>false</code>
   */
  public static boolean isMultipart(Part part) {
    if (part == null)
      return false;
    try {
      return part.isMimeType("multipart/*");
    } catch (MessagingException mex) {
      // does not multipart.
      return false;
    }
  }

  /**
   * Checks the part is message attachment.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> if the given part is message attachment, otherwise <code>false</code>
   */
  public static boolean isRFC822(Part part) {
    if (part == null)
      return false;

    try {
      return part.isMimeType("message/rfc822");
    } catch (MessagingException mex) {
      // does not message attachment.
      return false;
    }
  }

  /**
   * Checks the part is plain text.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> If the given part is plain text, otherwise <code>false</code>
   */
  public static boolean isPlain(Part part) {
    if (part == null)
      return false;

    try {
      return part.isMimeType("text/plain");
    } catch (MessagingException mex) {
      // does not message attachment.
      return false;
    }
  }

  /**
   * Checks the part is alternative part.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> If the given part is alternative part, otherwise <code>false</code>
   */
  public static boolean isAlternative(Part part) {
    if (part == null)
      return false;

    try {
      return part.isMimeType("multipart/alternative") || part.isMimeType("multipart/related");
    } catch (MessagingException mex) {
      // does not message attachment.
      return false;
    }
  }

  /**
   * Checks the part is text part.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> If the given part is text part, otherwise <code>false</code>
   */
  public static boolean isText(Part part) {
    if (part == null)
      return false;

    try {
      return part.isMimeType("text/*");
    } catch (MessagingException mex) {
      // does not message attachment.
      return false;
    }
  }

  /**
   * Checks the part is html part.
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> If the given part is html part, otherwise <code>false</code>
   */
  public static boolean isHtml(Part part) {
    if (part == null)
      return false;

    try {
      return part.isMimeType("text/html");
    } catch (MessagingException mex) {
      // does not message attachment.
      return false;
    }
  }

  /**
   * Checks the part is body part
   * 
   * @param part Part - the given part to be checked.
   * @return <code>true</code> If the given part is body part, otherwise <code>false</code>
   */
  public static boolean isBodyText(Part part) {
    return (!PartService.isAttached(part) && (PartService.isHtml(part) || PartService.isPlain(part)));
  }

  /**
   * Return the disposition file name from the given {@link Part} instance.
   * 
   * @param part the given {@link Part} instance.
   * @return the disposition file name.
   */
  public static String getDispositionName(Part part) {
    try {
      // get disposition value.
      String[] dispositions = part.getHeader(DISPOSITION_CONTENT_NAME);

      // get dispositions.
      if (dispositions != null && dispositions.length > 0) {
        String disposition = dispositions[0];

        if (StringService.hasLength(disposition)) {
          // split the values.
          String[] values = disposition.split(";");

          // has file name.
          if (values.length > 1) {
            StringBuffer buffer = new StringBuffer();
            if (values.length == 2) {
              buffer.append(StringService.getPartValue(values[1], FILENAME_STYLE, '='));
            } else {
              for (int index = 1; index < values.length; index++) {
                buffer.append(StringService.getPartValue(values[index], FILENAME_STYLE
                    + String.valueOf(index - 1) + '*', '='));
              }
            }

            // get file name.
            String filename = buffer.toString();
            if (StringService.hasLength(filename))
              return decodeValue(filename);
          }
        }
      }
    } catch (MessagingException mex) {
    }

    return null;
  }

  /**
   * Return the content file name from the given {@link Part} instance.
   * 
   * @param part the given {@link Part} instance.
   * @return the content file name.
   */
  public static String getContentName(Part part) {
    try {
      // get content type value.
      String content = part.getContentType();

      if (StringService.hasLength(content)) {
        // split the values.
        String[] values = content.split(";");

        // has file name.
        if (values.length > 1) {
          StringBuffer buffer = new StringBuffer();
          if (values.length == 2) {
            buffer.append(StringService.getPartValue(values[1], NAME_STYLE, '='));
          } else {
            for (int index = 1; index < values.length; index++) {
              buffer.append(StringService.getPartValue(values[index], NAME_STYLE
                  + String.valueOf(index - 1) + '*', '='));
            }
          }

          // get file name.
          String filename = buffer.toString();
          if (StringService.hasLength(filename))
            return decodeValue(filename);
        }
      }
    } catch (MessagingException mex) {
    }

    return null;
  }

  /**
   * Return the file name from the given part.
   * 
   * @param part the given {@link Part} instance.
   * @return the file name.
   */
  public static String getFilename(Part part) {
    try {
      // get file name from parse.
      String filename = part.getFileName();

      if (StringService.hasLength(filename)) {
        return MimeUtility.decodeText(filename);
      } else {
        // get file name from the disposition type.
        filename = PartService.getDispositionName(part);
        if (StringService.hasLength(filename)) {
          return filename;
        }

        // get content from the content type.
        return PartService.getContentName(part);
      }
    } catch (MessagingException mex) {
      return null;
    } catch (UnsupportedEncodingException ueex) {
      return null;
    }
  }

  // ----------------------------------------------------------------------
  // Helper functions.
  //
  /**
   * Parses text from the given mail part.
   *
   * @param part the given mail part.
   * @param htmlSupported {@code true} will be formatted data under HTML, otherwise return normal
   * format.
   *
   * @return the part message under string formatted.
   * @throws ParserException if an error occurs during parsing data.
   */
  private static String parseText(Part part, boolean htmlSupported)
      throws ParserException {
    try {
      String data = null;
      try {
        data = (String) part.getContent();
      } catch (final Exception ex) {
        // decode input stream.
        InputStream stream = ((MimeMessage) part).getRawInputStream();
        InputStream decodeStream = MimeUtility.decode(stream, "7bit");

        // create new mime message.
        final Part decodePart = new MimeMessage(null, decodeStream);

        // get data.
        data = (String) decodePart.getContent();
      }

      if (PartService.isPlain(part) && StringService.hasLength(data) && htmlSupported) {
        data = HtmlParser.textToHtml(data, PartService.DEFAULT_VIEW, null,
            MailService.DEFAULT_CHARSET);
      }

      return data;

//      //create result.
//      StringBuilder builder = new StringBuilder();
//      if (appendBreakLine) {
//        if (PartService.isHtml(part)) {
//          builder.append("<br/>").append(PartService.enterLine(part.getDisposition())).append(
//              "<br/>");
//        } else {
//          builder.append("\n\r").append(PartService.enterLine(part.getDisposition()))
//              .append("\n\r");
//        }
//      }
//
//      // append data.
//      builder.append(data);
//
//      // return data to user.
//      return builder.toString();
    } catch (final MessagingException mex) {
      throw new ParserException("Could not parse the message content.", mex);
    } catch (final IOException ioex) {
      throw new ParserException("Could not get the message content from message.", ioex);
    }
  }

  /**
   * Parses alternative mail part.
   * 
   * @param part Part - the given mail part.
   * @param htmlSupported {@code true} will be formatted data under HTML, otherwise return normal
   * format.
   * @param first {@code true} will be get first text part content, otherwise find the next
   * next content.
   * @return the part message under string formatted.
   * @throws ParserException if an error occurs during parsing data.
   */
  private static String parseAlternative(Part part, boolean htmlSupported, boolean first)
      throws ParserException {
    try {
      String message = null;

      // get the part.
      final Multipart multipart = (Multipart) part.getContent();
      for (int index = 0; index < multipart.getCount(); index++) {
        // get the part at the current index.
        final BodyPart bPart = multipart.getBodyPart(index);
        if (!isAttached(bPart) && isPlain(bPart)) {
          if (!StringService.isset(message)) {
            message = parseText(bPart, htmlSupported);
          }

          if (first && message != null) {
            return message;
          } 
        } else if (!isAttached(bPart) && isHtml(bPart)) {
          final String text = parseText(bPart, htmlSupported);
          if (text != null) {
            return text;
          }
        } else if (isAlternative(bPart)) {
          final String text = parseAlternative(bPart, htmlSupported, first);
          if (text != null) {
            return text;
          }
        } else if (isMultipart(bPart)) {
          final String text = parseMultipart(bPart, htmlSupported, first);
          if (text != null) {
            return text;
          }
        }
      }

      return message;
    } catch (final MessagingException mex) {
      throw new ParserException("Could not parse the message content.", mex);
    } catch (final IOException ioex) {
      throw new ParserException("Could not read message content from the message.", ioex);
    }
  }

  /**
   * Parses the multi mail part.
   *
   * @param part Part - the given multi mail part.
   * @param htmlSupported {@code true} will be formatted data under HTML, otherwise return normal
   * format.
   * @param first {@code true} will be get first text part content, otherwise find the expect part
   * content.
   *
   * @return the mail part content.
   * @throws ParserException if an error occurs during parsing data.
   */
  private static String parseMultipart(Part part, boolean htmlSupported, boolean first)
      throws ParserException {
    try {
      // get multi part.
      final Multipart multipart = (Multipart) part.getContent();

      // build the result.
      String message = null;

      // build content.
      for (int index = 0; index < multipart.getCount(); index++) {
        // get the body part.
        final Part bPart = multipart.getBodyPart(index);

        if (!isAttached (bPart) && isPlain(bPart)) {
          if ( message == null ) {
            message = parseText(bPart, htmlSupported);
          }

          if (first && message != null) {
            return message;
          }
        } else if (!isAttached(bPart) && isHtml(bPart)) {
          final String text = parseText(bPart, htmlSupported);
          if (text != null) {
            return text;
          }
        } else if (isAlternative(bPart)) {
          final String text = parseAlternative(bPart, htmlSupported, first);
          if (text != null) {
            return text;
          }
        } else if (isMultipart(bPart)){
          final String text = parseMultipart(bPart, htmlSupported, first);
          if (text != null) {
            return text;
          }
        }
      }

      // return data to user.
      return message;
    } catch (final MessagingException mex) {
      throw new ParserException("Could not parse the message.", mex);
    } catch (final IOException ioex) {
      throw new ParserException("Could not get the message content from the message.", ioex);
    }
  }

  /**
   * Decode data.
   * 
   * @param value the given data format under RFC2231.
   * @return the encode value.
   */
  private static String decodeValue(String value) {
    try {
      // get first format value.
      int index = value.indexOf('\'');
      if (index <= 0)
        return null;

      // get character set.
      String charset = value.substring(0, index);

      // get last format value.
      int lastIndex = value.indexOf('\'', index + 1);
      if (lastIndex <= 0)
        return null;

      // get real value.
      value = value.substring(lastIndex + 1);

      /*
       * Decode character.
       */
      byte[] data = new byte[value.length()];
      int bi = 0;
      for (index = 0; index < value.length(); index++) {
        char ch = value.charAt(index);
        if (ch == '%') {
          String hex = value.substring(index + 1, index + 3); // two hex character.
          ch = (char) Integer.parseInt(hex, 16);
          index += 2;
        }
        data[bi++] = (byte) ch;
      }

      // return data.
      return new String(data, 0, bi, MimeUtility.javaCharset(charset));
    } catch (UnsupportedEncodingException ueex) {
    } catch (NumberFormatException nfex) {
    } catch (StringIndexOutOfBoundsException sibex) {
    }

    // does not extract value.
    return null;
  }
}
