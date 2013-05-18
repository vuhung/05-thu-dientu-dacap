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
import java.util.List;

import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.parser.rfc822.Token;
import com.inet.mail.parser.rfc822.Token.TokenType;
import com.inet.mail.parser.rfc822.util.Rfc822Helper;

/**
 * AddressParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date Jan 28, 2008
 * 
 * <pre>
 *  <p>;
 *  Date: 28.01.2008, version 1.0
 *  <ul>
 *      <li> Create the parser to parse e-mail address based on regex pattern.</li>;
 *  </ul>
 *  <p>
 *  Date: 20.04.2009, version 1.01
 *  <ul>
 *      <li> Change the parser to RFC 822 parser. </li>
 *      <li> Mark function parseMailboxList deprecated(replaced by AddressParser.parseHeader).</li>;
 *      <li> Mark function parseMailbox2List deprecated(replaced by AddressParser.parseHeaderAsList).</li>
 *  </ul>
 * </pre>
 */
public final class AddressParser {
  /* mail separator. */
  private final static String MAIL_SEPARATOR_CHAR = ",";

  /**
   * Parses the list of e-mail address from the given input character sequence.
   * 
   * @param input the given input character sequence.
   * @return the array of {@link Address}.
   * 
   * @throws ParserException if there is any error email syntax in the input character sequence.
   * @deprecated since version 1.01, replaced by {@link AddressParser#parseHeader(CharSequence)}
   */
  @Deprecated
  public static Address[] parseMailboxList(final CharSequence input) throws ParserException {
    return parseHeader(input);
  }

  /**
   * Parse email address.
   * 
   * <pre>
   * This implementation will parse the character sequence to e-mail address. If there are many
   * (one or more) e-mails in this input character sequence, return the first one otherwise
   * return the null email address.
   * </pre>
   * 
   * @param input the given character sequence.
   * @return the {@link Address} or null.
   * 
   * @throws ParserException if there is any errors syntax in the input character sequence.
   * @see AddressParser#parseHeader(CharSequence)
   */
  public static Address parse(final CharSequence input) throws ParserException {
    final Address[] addresses = parseHeader(input);

    if (addresses.length == 0) {
      return null;
    }
    return addresses[0];
  }

  /**
   * Parses the list of e-mail address from the given input character sequence.
   * 
   * @param input the given input character sequence.
   * @return the array of {@link Address}.
   * 
   * @throws ParserException if there is any error email syntax in the input character sequence.
   * @see Rfc822Helper#analyzeAddress(CharSequence)
   * 
   * 16062009 - FIXED {@link NullPointerException} when addr does not exist.
   */
  public static Address[] parseHeader(final CharSequence input) throws ParserException {
    if (input == null || input.length() == 0)
      return Address.EMPTY_ARRAY;

    // analyze the given character sequence.
    final List<Token> tokens = Rfc822Helper.analyzeAddress(input);
    if (tokens == null || tokens.size() == 0)
      return Address.EMPTY_ARRAY;

    // convert token to address.
    final List<Address> addresses = new ArrayList<Address>(tokens.size());
    for (final Token token : tokens) {
      final Address address;
      if (token.getType() == TokenType.PHRASE) {
        final Token nextToken = token.getNextToken();
        String addr = nextToken.getValue();
        if (nextToken.getType() == TokenType.ROUTE) {
          addr = addr.substring(1, addr.length() - 1);
        }

        // 16062009 - TODO: FIXED NullPointerException when addr does not exist.
        if (StringService.hasLength(addr)) {
          address = new Address(token.getValue(), addr);
        } else {
          address = null;
        }
      } else {
        String addr = token.getValue();
        if (token.getType() == TokenType.ROUTE) {
          addr = addr.substring(1, addr.length() - 1);
        }

        // 16062009 - TODO: FIXED NullPointerException when addr does not exist.
        if (StringService.hasLength(addr)) {
          address = new Address(addr);
        } else {
          address = null;
        }
      }

      // put address into list of address.
      // 16062009 - TODO: FIXED NullPointerException when address does not exist.
      if (address != null) {
        addresses.add(address);
      }
    }

    // return data.
    return addresses.toArray(new Address[0]);
  }

  /**
   * Parses the list of e-mail address from the given input character sequence.
   * 
   * @param input the given input character sequence.
   * @return the {@link List} of {@link Address}.
   * 
   * @throws ParserException if there is any error email syntax in the input character sequence.
   * @see Rfc822Helper#analyzeAddress(CharSequence)
   * 
   * 16062009 - FIXED {@link NullPointerException} when addr does not exist.
   */
  public static List<Address> parseHeaderAsList(final CharSequence input) throws ParserException {
    if (input == null || input.length() == 0)
      return new ArrayList<Address>();

    // analyze the given character sequence.
    final List<Token> tokens = Rfc822Helper.analyzeAddress(input);
    if (tokens == null || tokens.size() == 0)
      return new ArrayList<Address>();

    // convert token to address.
    final List<Address> addresses = new ArrayList<Address>(tokens.size());
    for (final Token token : tokens) {
      // Internet address.
      Address address;
      if (token.getType() == TokenType.PHRASE) {
        final Token nextToken = token.getNextToken();
        String addr = nextToken.getValue();
        if (nextToken.getType() == TokenType.ROUTE) {
          addr = addr.substring(1, addr.length() - 1);
        }

        // TODO: 16062009 - FIXED NullPointerException when addr does not exist.
        if (StringService.hasLength(addr)) {
          address = new Address(token.getValue(), addr);
        } else {
          address = null;
        }
      } else {
        String addr = token.getValue();
        if (token.getType() == TokenType.ROUTE) {
          addr = addr.substring(1, addr.length() - 1);
        }

        // TODO: 16062009 - FIXED NullPointerException when addr does not exist.
        if (StringService.hasLength(addr)) {
          address = new Address(addr);
        } else {
          address = null;
        }
      }

      // put address into list of address.
      // TODO: 16062009 - FIXED NullPointerException when address does not exist.
      if (address != null) {
        addresses.add(address);
      }
    }

    // return data.
    return addresses;
  }

  /**
   * Converts array of {@link Address} to string.
   * 
   * <pre>
   * This implementation converts arrays of {@link Address} to string by calling the
   * {@link Address#getFullAddress()} method, each address will be separated by ',' character.
   * </pre>
   * 
   * @param addresses the given array of {@link Address}
   * @return the string representation as array of {@link Address}; never null.
   */
  public static String toString(Address[] addresses) {
    if (addresses == null || addresses.length == 0)
      return StringService.EMPTY_STRING;

    // create buffer.
    StringBuilder addrBuf = new StringBuilder();
    for (final Address address : addresses) {
      if (address != null) {
        addrBuf.append(address.getFullAddress()).append(AddressParser.MAIL_SEPARATOR_CHAR);
      }
    }

    // convert to string and remove the lasted separator character.
    return (addrBuf.length() > 0 ? addrBuf.substring(0, addrBuf.length() - 1) : "");
  }

  /**
   * Converts array of {@link Address} to string.
   * 
   * <pre>
   * This implementation converts arrays of {@link Address} to string by calling the
   * {@link Address#getFullAddress()} method, each address will be separated by ',' character.
   * </pre>
   * 
   * @param addresses the given array of {@link Address}
   * @return the string representation as array of {@link Address}; never null.
   */
  public static String toUnicodeString(Address[] addresses) {
    if (addresses == null || addresses.length == 0)
      return StringService.EMPTY_STRING;

    // create buffer.
    StringBuilder addrBuf = new StringBuilder();
    for (final Address address : addresses) {
      if (address != null) {
        addrBuf.append(address.toUnicodeString()).append(AddressParser.MAIL_SEPARATOR_CHAR);
      }
    }

    // convert to string and remove the lasted separator character.
    return (addrBuf.length() > 0 ? addrBuf.substring(0, addrBuf.length() - 1) : "");
  }

  /**
   * Converts {@link List} of e-mail {@link Address} to string.
   * 
   * <pre>
   * This implementation converts the {@link List} of {@link Address} to string by calling the
   * {@link Address#getFullAddress()} method, each address will be separated by ',' character.
   * </pre>
   * 
   * @param addresses the given {@link List} of e-mail {@link Address}
   * @return the string representation as list of {@link Address}; never null
   */
  public static String toString(List<Address> addresses) {
    if (addresses == null || addresses.isEmpty())
      return StringService.EMPTY_STRING;

    // create buffer.
    StringBuilder addrBuf = new StringBuilder();
    for (final Address address : addresses) {
      if (address != null) {
        addrBuf.append(address.getFullAddress()).append(AddressParser.MAIL_SEPARATOR_CHAR);
      }
    }

    // convert to string, and remove the lasted separator character.
    return (addrBuf.length() > 0 ? addrBuf.substring(0, addrBuf.length() - 1) : "");
  }

  /**
   * Converts {@link List} of e-mail {@link Address} to string.
   * 
   * <pre>
   * This implementation converts the {@link List} of {@link Address} to string by calling the
   * {@link Address#getFullAddress()} method, each address will be separated by ',' character.
   * </pre>
   * 
   * @param addresses the given {@link List} of e-mail {@link Address}
   * @return the string representation as list of {@link Address}; never null
   */
  public static String toUnicodeString(List<Address> addresses) {
    if (addresses == null || addresses.isEmpty())
      return StringService.EMPTY_STRING;

    // create buffer.
    StringBuilder addrBuf = new StringBuilder();
    for (final Address address : addresses) {
      if (address != null) {
        addrBuf.append(address.toUnicodeString()).append(AddressParser.MAIL_SEPARATOR_CHAR);
      }
    }

    // convert to string, and remove the lasted separator character.
    return (addrBuf.length() > 0 ? addrBuf.substring(0, addrBuf.length() - 1) : "");
  }

  /**
   * Parses the list of e-mail address from the given input character sequence.
   * 
   * @param input the given input character sequence.
   * @return the {@link List} of {@link Address}.
   * 
   * @throws ParserException if there is any error email syntax in the input character sequence.
   * @deprecated since version 1.01, replaced by {@link AddressParser#parseHeaderAsList(CharSequence)}
   */
  @Deprecated
  public static List<Address> parseMailbox2List(final CharSequence input) throws ParserException {
    return parseHeaderAsList(input);
  }

  /**
   * Only Java Virtual Machine can create object form class.
   */
  private AddressParser() {
    // TODO Nothing as of yet.
  }
}
