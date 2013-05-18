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
package com.inet.mail.parser.rfc822.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.inet.mail.parser.ParserException;
import com.inet.mail.parser.rfc822.Token;
import com.inet.mail.parser.rfc822.Token.TokenType;

/**
 * Rfc822Helper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Rfc822Helper.java 2009-04-17 12:30:59z nguyen_dv $
 * 
 * Create date: Apr 17, 2009
 * 
 * <pre>
 *  Initialization Rfc822Helper class.
 * </pre>
 */
public final class Rfc822Helper {
  /* class header. */
  private static final Logger LOG = Logger.getLogger(Rfc822Helper.class);

  /* RFC 822 special character. */
  private static final String RFC822_SPECIAL_CHAR = "()<>@,;:\\\".[]";

  /**
   * Hidden constructor.
   */
  private Rfc822Helper() {
  }

  /**
   * Checks the given character is RFC 822 control character.
   * 
   * <pre>
   * The RFC 822 control character is the character has value between 0 to 31
   * </pre>
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 control character.
   */
  public static boolean isCtl(final char ch) {
    return (ch >= 0 && ch <= 31);
  }

  /**
   * Checks the given character is RFC 822 special character.
   * 
   * <pre>
   * The RFC 822 special character is the one of character: &quot;(&quot;,&quot;)&quot;,
   * &quot;&lt;&quot;,&quot;&gt;&quot;,&quot;@&quot;,&quot;,&quot;,&quot;;&quot;,&quot;:&quot;,
   * &quot;\&quot;,&lt;&quot;&gt;,&quot;.&quot;,&quot;[&quot;,&quot;]&quot;
   * </pre>
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 special character.
   */
  public static boolean isSpecial(final char ch) {
    return (RFC822_SPECIAL_CHAR.indexOf(ch) >= 0);
  }

  /**
   * Checks the given character is RFC 822 carry return character.
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 carry return character.
   */
  public static boolean isCr(final char ch) {
    return (ch == 13);
  }

  /**
   * Checks the given character is RFC 822 line feed character.
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 line feed character.
   */
  public static boolean isLf(final char ch) {
    return (ch == 10);
  }

  /**
   * Check the given character is RFC 822 space character.
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 space character.
   */
  public static boolean isSpace(final char ch) {
    return (ch == 32);
  }

  /**
   * Check the given character is RFC 822 horizontal tab character.
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 horizontal character.
   */
  public static boolean isHorizontalTab(final char ch) {
    return (ch == 9);
  }

  /**
   * Checks the given character is RFC 822 linear white space character.
   * 
   * <pre>
   * The RFC 822 linear white space character is either space or horizontal
   * tab character.
   * </pre>
   * 
   * @param ch the given character to check.
   * @return if the given character is RFC 822 linear white space character.
   */
  public static boolean isLinearWhitespaceChar(final char ch) {
    return isSpace(ch) || isHorizontalTab(ch);
  }

  /**
   * Quote the phrase from the given quote character.
   * 
   * <pre>
   * This implementation scan through the phrase and find the quote value, each quote value
   * will be replace with the '\\' character at the first quote.
   * </pre>
   * 
   * @param phrase the given phrase to quote.
   * 
   * @return the quoted phrase.
   */
  public static CharSequence quote(final CharSequence phrase) {
    int length = phrase.length();

    // hold the quoted phrase.
    final StringBuilder builder = new StringBuilder();
    builder.append('"');
    char ch = '\0';

    for (int index = 0; index < length; index++) {
      ch = phrase.charAt(index);
      if (ch == '\\') {
        builder.append("\\\\").append('\\').append(phrase.charAt(++index));
      } else if (ch == '"') {
        builder.append('\\').append('"');
      } else {
        builder.append(ch);
      }
    }
    builder.append('"');

    return builder.toString();
  }

  /**
   * Unquote the quote phrase.
   * 
   * <pre>
   * This implementation check the given phrase is quote phrase before removing the quote value.
   * The quote phrase is the phrase has leading and ending with single or double quote character.
   * </pre>
   * 
   * @param quotePhrase the given quote phrase.
   * @return the phare.
   */
  public static CharSequence unquote(final CharSequence quotePhrase) {
    if (quotePhrase.length() == 0)
      return quotePhrase;
    int length = quotePhrase.length();

    // check the valid quote phrase.
    if ((quotePhrase.charAt(0) == '"' && quotePhrase.charAt(length - 1) == '"')
        || (quotePhrase.charAt(0) == '\'' && quotePhrase.charAt(length - 1) == '\'')) {
      // unquote the phrase.
      final StringBuilder builder = new StringBuilder();
      char ch = '\0';

      for (int index = 1; index < length - 1; index++) {
        ch = quotePhrase.charAt(index);
        if (ch == '\\') {
          builder.append(quotePhrase.charAt(++index));
        } else {
          builder.append(ch);
        }
      }

      return builder.toString();
    } else {
      // the quote phrase is in
      return quotePhrase;
    }
  }

  /**
   * This implementation will analyze the input to any token and avoid any error in subject, then
   * decode the subject to the right subject.
   *
   * @param input the given input value to analyze.
   * @return the subject decode text.
   * @throws ParserException if the given input data has any errors that could not be avoided
   * from engine.
   */
  public static String decodeSubject(final CharSequence input) throws ParserException {
    // tokenize the subject.
    Token token = tokenize(input);

    final StringBuilder subBuf = new StringBuilder();
    while (token != null) {
      switch (token.getType()) {
      case SPECIAL:
      case SPACE:
      case QCHAR:
        subBuf.append(token.getValue());
        break;
      case ATOM:
        final String value = token.getValue();
        try {
          subBuf.append(MimeUtility.decodeText(value));
        } catch (final UnsupportedEncodingException ueex) {
          subBuf.append(value);
        }
        break;
      case QSTRING:
        subBuf.append("\"");
        final String qvalue = token.getValue().substring(1, token.getValue().length() -1);
        try {
          subBuf.append(MimeUtility.decodeText(qvalue));
        } catch (final UnsupportedEncodingException ueex) {
          subBuf.append(qvalue);
        }
        subBuf.append("\"");
        break;
      }

      token = token.getNextToken();
    }

    // return the subject data.
    return subBuf.toString();
  }

  /**
   * This method will analyze the input into any address token and void any error in address.
   * 
   * <pre>
   * This implementation will be split the input into any character sequence, each of them
   * will tokenize and analyze to the address following RFC 822 format.
   * During parsing character input, this implementation avoided any error contain in the given
   * input data, by using heuristic algorithm.
   * </pre>
   * 
   * @param input the given input value.
   * @return the list of {@link Token} data.
   * 
   * @throws ParserException if the given input data has any errors could not be avoided from engine.
   */
  public static List<Token> analyzeAddress(final CharSequence input) throws ParserException {
    // first split the token to any address sequence character.
    CharSequence[] addresses = preProcess(input);
    if (addresses == null)
      return Collections.unmodifiableList(new ArrayList<Token>());

    // tokenize the given address into raw token, and then put to analyzed engine.
    List<Token> tokens = new ArrayList<Token>(addresses.length);
    for (final CharSequence address : addresses) {
      final Token rawToken = tokenize(address);
      if (rawToken != null) {
        try {
          tokens.add(analyze(rawToken));
        } catch (final ParserException pex) {
          LOG.warn("The token {" + rawToken + "} is not internet address, avoid this", pex);
        }
      }
    }

    // return the list of token to user.
    return Collections.unmodifiableList(tokens);
  }

  /**
   * Analyzes the given {@link Token} into the real {@link Token}.
   * 
   * <pre>
   * This implementation will be analyze the given raw {@link Token} into the special token
   * that carry the semantic. This step is the last step to parse address.
   * This implementation will avoid any error at the runtime.
   * </pre>
   * 
   * @param token the given raw {@link Token}.
   * @return the semantic {@link Token}
   * 
   * @throws ParserException if the error occurs during analyze the token.
   */
  private static Token analyze(final Token token) throws ParserException {
    // process token.
    Token currentToken = token, endPhrase = null;
    Token startToken = null;

    int state = 0;

    // next the token.
    while (currentToken != null) {
      // get the token value.
      final String tokenValue = currentToken.getValue();

      switch (state) {
      case 0:
        if (" ".equals(tokenValue)) { // avoid space.
          break;
        }
        if ("<".equals(tokenValue)) {
          return route(currentToken, null);
        } else {
          switch (currentToken.getType()) {
          case ATOM:
          case QCHAR:
          case QSTRING:
            endPhrase = token;
            startToken = token;
            state = 3;
            break;
          default:
            throw new ParserException("invalid email address syntax: {" + token.toString() + "}");
          }
        }
        break;
      case 3:
        if ("<".equals(tokenValue)) {
          // create phrase token and route token.
          final Token phrase = phrase(token, currentToken.getPrevToken());
          route(currentToken, phrase);

          return phrase;
        } else if (":".equals(tokenValue)) { // address is group.
          return group(token, endPhrase, currentToken.getNextToken());
        } else if (".".equals(tokenValue) || "@".equals(tokenValue)) {
          // may be local part of address.
          if (startToken.equals(currentToken.getPrevToken()) && (startToken.getPrevToken() != null)) {
            while ((startToken != null) && (startToken.getType() == TokenType.SPACE)) {
              startToken = startToken.getPrevToken();
            }

            if (startToken != null) {
              while (startToken != null && startToken.getType() != TokenType.SPACE) {
                startToken = startToken.getPrevToken();
              }
              if (startToken != null && startToken.getType() == TokenType.SPACE) {
                startToken = startToken.getNextToken();
              }
            }

            if (startToken == null)
              startToken = token;
          }

          if (endPhrase.equals(currentToken.getPrevToken())) {
            // move to previous token.
            while (endPhrase != null && endPhrase.getType() == TokenType.SPACE) {
              endPhrase = endPhrase.getPrevToken();
            }

            if (endPhrase != null) {
              while (endPhrase != null && endPhrase.getType() != TokenType.SPACE) {
                endPhrase = endPhrase.getPrevToken();
              }
            }
          }

          state = (".".equals(currentToken) ? 4 : 5); // move to next state.

          // good, this is e-mail address.
          if (state == 5) {
            if (endPhrase == null) { // does not have the phrase.
              return addr(startToken, null, null);
            } else {
              final Token phrase = phrase(token, endPhrase);
              addr(startToken, null, phrase);
              return phrase;
            }
          }
        } else {
          switch (currentToken.getType()) {
          case SPACE:
          case ATOM:
          case QCHAR:
          case QSTRING:
            // move the end phrase and start token to current token.
            while (!endPhrase.equals(currentToken))
              endPhrase = endPhrase.getNextToken();
            while (!startToken.equals(currentToken))
              startToken = startToken.getNextToken();
            break;
          default:
            throw new ParserException("invalid email address syntax: {" + token.toString() + "}");
          }
        }
        break;
      case 4:
        if ("<".equals(tokenValue)) {
          // create phrase, route token.
          final Token phrase = phrase(token, currentToken.getPrevToken());
          route(currentToken, phrase);

          return phrase;
        } else {
          switch (currentToken.getType()) {
          case SPACE:
          case ATOM:
          case QCHAR:
          case QSTRING:
            state = 3;
            break;
          default:
            throw new ParserException("invalid email address syntax: {" + token.toString() + "}");
          }
        }
        break;
      default:
        break;
      }

      // move to next token.
      currentToken = currentToken.getNextToken();
    }

    // the given token is not validated with e-mail address syntax.
    throw new ParserException("Invalid email address syntax: {" + token.toString() + "}");
  }

  /**
   * Route the collection of {@link Token} to route token.
   * 
   * @param token the given root of token.
   * @param parent the given the parent of route {@link Token}.
   * @return the route token.
   */
  private static Token route(final Token token, final Token parent) {
    Token currentToken = token;
    final StringBuilder route = new StringBuilder();

    while (currentToken != null) {
      if (currentToken.getType() != TokenType.SPACE) {
        route.append(currentToken.getValue());
      }

      currentToken = currentToken.getNextToken();
    }

    // append the end of route.
    if (route.charAt(route.length() - 1) != '>')
      route.append('>');
    return new Token(TokenType.ROUTE, route.toString(), parent);
  }

  /**
   * Create the phrase token from the given start phrase and end phrase.
   * 
   * @param startToken the given start phrase {@link Token}
   * @param endToken the given end phrase {@link Token}
   * 
   * @return the phrase {@link Token}
   */
  private static Token phrase(final Token startToken, final Token endToken) {
    final StringBuilder phrase = new StringBuilder();
    boolean space = false, special = false;

    Token curToken = startToken;
    while ((curToken != null) && (!curToken.equals(endToken))) {
      switch (curToken.getType()) {
      case ATOM:
      case QCHAR:
      case QSTRING:
        if (space && !special)
          phrase.append(' ');
        phrase.append(curToken.getValue());
        special = false;
        space = false;
        break;
      case SPECIAL:
        phrase.append(curToken.getValue());
        special = true;
        space = false;
        break;
      case SPACE:
        space = true;
        break;
      }
      curToken = curToken.getNextToken();
    }

    // append the end token value.
    if (endToken != null && endToken.getType() != TokenType.SPACE) {
      if (space && !special)
        phrase.append(' ');
      phrase.append(endToken.getValue());
    }

    // create phrase token.
    return new Token(TokenType.PHRASE, phrase.toString());
  }

  /**
   * Create group {@link Token} from the given start phrase token, end phrase token and group token.
   * 
   * @param startToken the given start phrase {@link Token} object.
   * @param endToken the given end phrase {@link Token} object.
   * @param token the given group {@link Token} object.
   * 
   * @return the group {@link Token} object.
   */
  private static Token group(final Token startToken, final Token endToken, final Token token) {
    // create phrase token.
    final Token phraseToken = phrase(startToken, endToken);

    // create part of group token.
    Token currentToken = token;
    final StringBuilder pgroup = new StringBuilder();

    while (currentToken != null) {
      if (currentToken.getType() != TokenType.SPACE) {
        pgroup.append(currentToken.getValue());
      }
      currentToken = currentToken.getNextToken();
    }

    // append the end of group.
    if (pgroup.charAt(pgroup.length() - 1) != ';')
      pgroup.append(';');

    return new Token(TokenType.GROUP, phraseToken.getValue() + ':' + pgroup.toString());
  }

  /**
   * Create address {@link Token} from the given start token and end token.
   * 
   * @param startToken the given start {@link Token} object.
   * @param endToken the given end {@link Token} object.
   * @param parent the given parent of {@link Token}.
   * 
   * @return the address {@link Token} value.
   */
  private static Token addr(final Token startToken, final Token endToken, final Token parent) {
    final StringBuilder addr = new StringBuilder();
    Token curToken = startToken;

    while ((curToken != null) && !(curToken.equals(endToken))) {
      if (curToken.getType() != TokenType.SPACE) {
        addr.append(curToken.getValue());
      }

      curToken = curToken.getNextToken();
    }

    if (endToken != null && endToken.getType() != TokenType.SPACE) {
      addr.append(endToken.getValue());
    }

    return new Token(TokenType.ADDR, addr.toString(), parent);
  }

  /**
   * Tokenize the input into the {@link Token} object.
   * 
   * @param input the given input data.
   * @return the {@link Token} object.
   */
  private static Token tokenize(final CharSequence input) {
    // tokenize the stream.
    Object[] nextToken = nextToken(input, 0, null);
    if (nextToken == null)
      return null;

    final Token rootToken = (Token) nextToken[0];
    Token currentToken = rootToken;
    int position = ((Integer) nextToken[1]).intValue();

    // parse the stream.
    while ((nextToken = nextToken(input, position, currentToken)) != null) {
      currentToken = (Token) nextToken[0];
      position = ((Integer) nextToken[1]).intValue();
    }

    return rootToken;
  }

  /**
   * Return the next token from the given input data and the position where to read the data.
   * 
   * <pre>
   * This implementation read the data and stop when face the special character or the SPACE
   * value.
   * The method return the array of token and position where stop scanning input data.
   * </pre>
   * 
   * @param input the given data.
   * @param position the given position where to read the data.
   * @param parent the given parent of current token.
   * 
   * @return the structure of {@link Token} and the position to get this token.
   */
  private static Object[] nextToken(final CharSequence input, final int position, final Token parent) {
    // does not get next token.
    if (position >= input.length())
      return null;

    // the token value.
    final StringBuilder tokenValue = new StringBuilder();
    char ch;
    final int length = input.length();
    int previousIndex = -1;

    for (int index = position; index < length; index++) {
      ch = input.charAt(index); // get next character.
      switch (ch) {
      case '"':
        if (tokenValue.length() > 0) {
          final Token token = new Token(TokenType.ATOM, tokenValue.toString(), parent);
          return new Object[] { token, index };
        } else {
          previousIndex = index;
          boolean quoting = true;
          for (index++; (index < length) && quoting; index++) {
            ch = input.charAt(index);
            switch (ch) {
            case '"':
              quoting = false;
              break;
            }
          }
          if (!quoting) {
            // create the token.
            tokenValue.append(input.subSequence(previousIndex, index).toString());
            final Token token = new Token(TokenType.QSTRING, tokenValue.toString(), parent);
            return new Object[] { token, index };
          } else {
            index = previousIndex;
            break;
          }
        }
      case '<':
      case '>':
      case '@':
      case ',':
      case ';':
      case ':':
      case '.':
      case ']':
      case '[':
        // has token value.
        if (tokenValue.length() > 0) {
          final Token token = new Token(TokenType.ATOM, tokenValue.toString(), parent);
          return new Object[] { token, index };
        } else {
          tokenValue.append(ch);
          final Token token = new Token(TokenType.SPECIAL, tokenValue.toString(), parent);
          return new Object[] { token, (index + 1) };
        }
      case '\\':
        // has token value.
        if (tokenValue.length() > 0) {
          final Token token = new Token(TokenType.ATOM, tokenValue.toString(), parent);
          return new Object[] { token, index };
        } else {
          // move the next character.
          if (index < input.length() - 1) {
            tokenValue.append(ch).append(input.charAt(++index));
          } else {
            tokenValue.append(ch);
          }
          final Token token = new Token(TokenType.QCHAR, tokenValue.toString(), parent);
          return new Object[] { token, (index + 1) };
        }
      case ' ':
        // has token value.
        if (tokenValue.length() > 0) {
          final Token token = new Token(TokenType.ATOM, tokenValue.toString(), parent);
          return new Object[] { token, index };
        } else {
          final Token token = new Token(TokenType.SPACE, " ", parent);
          return new Object[] { token, (index + 1) };
        }
      default:
        tokenValue.append(ch);
        break;
      }
    }

    // has token value.
    if (tokenValue.length() > 0) {
      final Token token = new Token(TokenType.ATOM, tokenValue.toString(), parent);
      return new Object[] { token, length };
    }

    // end of stream.
    return null;
  }

  /**
   * Tokenizes the input to list of address character sequence.
   * 
   * <pre>
   * This implementation split the input into any valid address and avoid any error occurs during
   * parsing data and then normalization the address to avoid any linear white space, carry
   * return, line feed and any control character from the raw address.
   * </pre>
   * 
   * @param input the given input {@link CharSequence}
   * 
   * @return the list of address {@link CharSequence}
   * @see Rfc822Helper#normAddress(CharSequence)
   */
  private static final CharSequence[] preProcess(CharSequence input) {
    // list of address character sequence.
    List<CharSequence> charSequences = new ArrayList<CharSequence>();

    // starting and ending point.
    int index = 0, previousIndex = index;
    int length = input.length();

    // create builder.
    StringBuilder builder = new StringBuilder();

    for (; index < length; index++) {
      // get current character.
      char ch = input.charAt(index);

      switch (ch) {
      case '(':
        int nesting = 1;
        previousIndex = index;
        for (index++; (index < length) && (nesting > 0); index++) {
          ch = input.charAt(index);
          switch (ch) {
          case '\\': // escape the next character.
            index++;
            break;
          case ')':
            nesting--;
            break;
          case '(':
            nesting++;
            break;
          default:
            break;
          }
        }

        // any error occurs.
        if (nesting > 0) {
          index = previousIndex;
          break;
        }
        index--; // point to the parent character.
        break;
      case ')':
        break;
      case '"':
        // keep the data.
        previousIndex = index;
        boolean inquote = true;
        for (index++; (index < length) && inquote; index++) {
          ch = input.charAt(index);
          switch (ch) {
          case '\\': // escape the next character.
            index++;
            break;
          case '"':
            inquote = false;
            break;
          default:
            break;
          }
        }

        // any error occurs.
        if (inquote) {
          index = previousIndex;
          break;
        }

        // append the character.
        builder.append(input.subSequence(previousIndex, index));

        index--; // back to the end of close quote.
        break;
      case '<':
        // keep the previous index.
        previousIndex = index;
        boolean inroute = true,
        nested = false;
        int quotes = 0;
        for (index++; (index < length) && inroute && !nested; index++) {
          ch = input.charAt(index);
          switch (ch) {
          case '\\': // escape the next character.
            index++;
            break;
          case '<':
            if (quotes % 2 == 1)
              continue; // in quote.
            else
              nested = true;
            break;
          case '"':
            quotes++;
            break;
          case '>':
            if (quotes % 2 == 1)
              continue;
            else
              inroute = false;
            break;
          default:
            break;
          }
        }

        // does not match the quote, trying to remove the quote.
        if (quotes % 2 == 1) {
          int quoteIndex = previousIndex;
          inroute = true;
          int jndex = 0;

          for (jndex = (previousIndex + 1); jndex < length && inroute; jndex++) {
            ch = input.charAt(jndex);
            switch (ch) {
            case '\\':
              index++;
              break;
            case '"':
              quotes--;
              quoteIndex = jndex;
              break;
            case '>':
              if (quotes == 0 || expectedNextMatch(input, jndex, ','))
                inroute = false;
              else
                continue;
            }
          }

          // does not match route again, skip begin route.
          if (!inroute) {
            // removing the quote and move to end of route value.
            if (quotes % 2 == 0) {
              builder.append(input.subSequence(previousIndex, quoteIndex)).append(
                  input.subSequence(quoteIndex + 1, jndex));
            } else {
              builder.append(input.subSequence(previousIndex, jndex));
            }

            index = (jndex - 1);
            break;
          }
        } else if (nested) {
          inroute = true;
          int jndex = 0;
          int state = 0;
          for (jndex = (previousIndex + 1); jndex < length && inroute; jndex++) {
            ch = input.charAt(jndex);
            while (isCr(ch) || isLf(ch) || isLinearWhitespaceChar(ch) || isCtl(ch)) {
              ch = input.charAt(++jndex);
            }
            switch (ch) {
            case '\\':
              index++;
              break;
            case '@':
              if (state == 0)
                state = 1; // have route information.
              break;
            case ':':
              if (state == 1)
                state = 2; // move to the address specification.
              break;
            case ',':
              if (state == 2 || state == 3)
                inroute = false;
              break;
            default:
              state = 3;
              break;
            }
          }

          // matching data.
          if (!inroute) {
            builder.append(input.subSequence(previousIndex, (jndex - 1))).append('>');
            index = (jndex - 2);
            break;
          }
        }

        // not matching the route and matching the quote.
        if (inroute) {
          index = previousIndex;
          break;
        }

        builder.append(input.subSequence(previousIndex, index));
        index--; // move to next character.
        break;

      case '>':
        break;
      case ':': // grouping address.
        boolean ingroup = true;
        previousIndex = index;
        for (; (index < length) && ingroup; index++) {
          ch = input.charAt(index);
          switch (ch) {
          case ';':
            ingroup = false;
            break;
          }
        }

        // matching group.
        if (!ingroup) {
          builder.append(input.subSequence(previousIndex, index));
        } else {
          index = previousIndex;
          break;
        }

        index--; // moving to the next character.
        break;

      case '[': // domain literal
        boolean indomain = true;
        quotes = 0;
        previousIndex = index;
        for (index++; (index < length) && indomain; index++) {
          ch = input.charAt(index);
          switch (ch) {
          case '\\': // escape the next character.
            index++;
            break;
          case '"':
            quotes++;
            break;
          case ']':
            if (quotes % 2 == 1)
              continue;
            else
              indomain = false;
            break;
          default:
            break;
          }
        }

        // does not match the quote, trying to remove the quote.
        if (quotes % 2 == 1) {
          int quoteIndex = previousIndex;
          indomain = true;
          int jndex = 0;

          for (jndex = (previousIndex + 1); jndex < length && indomain; jndex++) {
            ch = input.charAt(jndex);
            switch (ch) {
            case '\\':
              index++;
              break;
            case '"':
              quotes--;
              quoteIndex = jndex;
              break;
            case ']':
              if (quotes == 0 || expectedNextMatch(input, jndex, ','))
                indomain = false;
              else
                continue;
            }
          }

          // does not match domain literal again, skip begin route.
          if (!indomain) {
            // removing the quote and move to end of route value.
            builder.append(input.subSequence(previousIndex, quoteIndex)).append(
                input.subSequence(quoteIndex + 1, jndex));
            index = (jndex - 1);
            break;
          }
        }

        // not matching the domain literal and matching the quote.
        if (indomain) {
          index = previousIndex;
          break;
        }

        builder.append(input.subSequence(previousIndex, index));
        index--; // move to next character.
        break;

      case ',': // ending mail address.
        charSequences.add(normAddress(builder.toString()));
        builder = new StringBuilder();
        break;

      case '\\': // avoid next character.
        builder.append(ch).append(input.charAt(++index));
        // index++ ;
        break;

      default:
        builder.append(ch);
        break;
      }
    }

    // adding the end of address.
    if (builder.length() > 0) {
      charSequences.add(normAddress(builder.toString()));
    }

    // return list of character sequence.
    return charSequences.toArray(new CharSequence[0]);
  }

  /**
   * Normalize the given address to RFC 822 address format.
   * 
   * <pre>
   * This implementation will be remove any space from the raw address and then normalize the
   * address in any case such as missing route character at the personalize and address.
   * </pre>
   * 
   * @param address the given raw address {@link CharSequence} value.
   * @return the normalize RFC 822 address.
   */
  private static CharSequence normAddress(final CharSequence address) {
    // trim and avoid any space of address.
    CharSequence normAddress = trim(address);
    normAddress = avoidAnySpace(normAddress);

    // removing single quote.
    normAddress = unquote(normAddress);

    // missing the route data.
    final StringBuilder addrBuf = new StringBuilder();
    int encodingState = -1, length = normAddress.length();
    char ch, nextCh;
    int index = -1, previousIndex = index, errorIndex = -1;
    int corressState = 0;

    for (index = 0; index < length; index++) {
      ch = normAddress.charAt(index);
      switch (ch) {
      case '=':
        previousIndex = index;
        encodingState = 0;

        nextCh = (index < length - 1 ? normAddress.charAt(index + 1) : '\0');
        if (nextCh == '?') { // may be starting encoding.
          index++;
          for (; (index < length) && (encodingState != 4); index++) {
            ch = normAddress.charAt(index);
            switch (ch) {
            case '?':
              if (encodingState == 0 || encodingState == 2) {
                if ((encodingState == 2) && (corressState == 0)) {
                  corressState = 1;
                }

                encodingState++;
              }

              if (encodingState == 3) {
                nextCh = (index < length - 1 ? normAddress.charAt(index + 1) : '\0');
                if ((nextCh == '=') && (corressState != 1)) {
                  encodingState = 4; // ending encoding.
                  index++;
                } else {
                  corressState = 0;
                }
              }
              break;
            case 'Q':
            case 'q':
            case 'B':
            case 'b':
              if (encodingState == 1) {
                encodingState++;
              }
              break;
            default:
              if (corressState == 1) {
                corressState = 0;
              }
              break;
            }
          }
        }

        if (encodingState != 4) { // not encoding data.
          index = previousIndex;
          break;
        }

        errorIndex = index;
        addrBuf.append(normAddress.subSequence(previousIndex, index));
        index--;
        break;
      case '"':
        if (errorIndex != -1) {
          addrBuf.append(' ').append('"');
          errorIndex = -1;
        } else {
          addrBuf.append('"');
        }
        break;
      default:
        if (errorIndex != -1) {
          if (!isSpecial(ch) || !isSpace(ch)) {
            addrBuf.append(' ');
            errorIndex = -1; // process next error.
          }
        }

        addrBuf.append(ch);
        break;
      }
    }

    // return the normalize data.
    return addrBuf.toString();
  }

  /**
   * Trim the given sequence by removing the space, tab, newline character at the first and last of
   * sequence.
   * <pre>
   * 22072009 - FIXED CRITICAL BUG 21 http://www.truthinet.com.vn/bugs/show_bug.cgi?id=21
   * </pre>
   * 
   * @param data the given {@link CharSequence} to trim.
   * @return the trimmed {@link CharSequence}.
   */
  private static CharSequence trim(final CharSequence data) {
    if (data == null) {
      return null;
    }

    int start = 0;
    int end = data.length();

    // data is empty.
    if (start == end) {
      return data;
    }

    // remove the space, tab, newline character at the first sequence.
    char ch = data.charAt(start);
    while (isLinearWhitespaceChar(ch) || isCr(ch) || isLf(ch) || isCtl(ch)) {
      start++;

      // return empty data.
      if (start == end) {
        break;
      } else {
        ch = data.charAt(start);
      }
    }

    ch = data.charAt(end - 1);
    while (isLinearWhitespaceChar(ch) || isCr(ch) || isLf(ch) || isCtl(ch)) {
      end--;
      if (end <= start) {
        break;
      } else {
        ch = data.charAt(end - 1);
      }
    }

    // return correct data.
    if (start <= end) {
      return data.subSequence(start, end);
    } else {
      return data.subSequence(data.length(), data.length());
    }
  }

  /**
   * Avoid the address space.
   * 
   * <pre>
   * Replace any space, horizontal tab, carry return, line feed, control character existing in
   * the address to single space value. for example:
   * input: &quot;iNet{SPACE}{HTAB}Solutions{CR}{LF}Company&quot;
   * output: &quot;iNet{SPACE}Solutions{SPACE}Company&quot;
   * </pre>
   * 
   * @param address the given raw address.
   * @return the address after removing any space.
   */
  private static CharSequence avoidAnySpace(final CharSequence data) {
    // remove the carry return and line feed character.
    final StringBuilder builder = new StringBuilder();
    boolean avoidSpace = false;
    char ch;
    for (int index = 0; index < data.length(); index++) {
      ch = data.charAt(index);
      if (isLinearWhitespaceChar(ch) || isCr(ch) || isLf(ch) || isCtl(ch)) {
        if (!avoidSpace) {
          builder.append(' ');
          avoidSpace = true;
        }
      } else {
        builder.append(ch);
        avoidSpace = false;
      }
    }

    // get the new sequence value.
    return builder.toString();
  }

  /**
   * Return <code>true</code> if the next matching is the given character.
   * 
   * <pre>
   * This implementation to scan through the input, get the valid character and compare to the
   * given character if matching will return &lt;code&gt;true&lt;/code&gt;, otherwise &lt;code&gt;false&lt;/code&gt;.
   * In this implementation, the engine will avoid any special value such as SPACE, HTAB, CR, LF.
   * </pre>
   * 
   * @param input the given input data to scan.
   * @param index the given the entry point where to reading token.
   * @param expected the given character to compare.
   * 
   * @return if the next character is the expected character.
   */
  private static boolean expectedNextMatch(final CharSequence input, int index, final char expected) {
    char nextCh = ((index < input.length()) ? input.charAt(index) : '\0');
    while (nextCh != '\0'
        && (isLinearWhitespaceChar(nextCh) || isCr(nextCh) || isLf(nextCh) || isCtl(nextCh))) {
      nextCh = ((++index < input.length()) ? input.charAt(index) : '\0');
    }

    return (nextCh != '\0' && nextCh == expected);
  }
}
