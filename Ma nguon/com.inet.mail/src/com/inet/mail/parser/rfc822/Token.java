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
package com.inet.mail.parser.rfc822;

import com.inet.base.service.CompareService;

/**
 * Token
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Token.java 2009-04-18 11:30:15z nguyen_dv $
 *
 * Create date: Apr 18, 2009
 * <pre>
 *  This class implementation RFC 822 token.
 * </pre>
 */
public final class Token {
	/* token type. */
	private TokenType type ;

	/* token value. */
	private String value ;

	/* the next token. */
	private Token nextToken ;

	/* the previous token. */
	private Token prevToken ;

	/**
	 * Creates <tt>Token</tt> instance.
	 */
	public Token(){}

	/**
	 * Creates <tt>Token</tt> instance from the given {@link TokenType} and token value as tring.
	 *
	 * @param type the given {@link TokenType} value.
	 * @param value the given token value.
	 */
	public Token(final TokenType type, final String value){
		this(type, value, null) ;
	}

	/**
	 * Creates <tt>Token</tt> instance from the given {@link TokenType}, token value and the parent
	 * of token.
	 *
	 * @param type the given {@link TokenType} value.
	 * @param value the given token value.
	 * @param parent the given parent of current {@link Token}
	 */
	public Token(final TokenType type, final String value, final Token parent){
		this.type = type ;
		this.value = value ;

		if(parent != null){
			parent.setNextToken(this) ;
			setPrevToken(parent) ;
		}
	}

	/**
	 * @return the {@link TokenType} value.
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Sets the {@link TokenType} value.
	 *
	 * @param type the given {@link TokenType} value to set.
	 */
	public void setType(final TokenType type) {
		this.type = type;
	}

	/**
	 * @return the token value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the token value.
	 *
	 * @param value the given token value.
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the next RFC 822 {@link Token}
	 */
	public Token getNextToken() {
		return nextToken;
	}

	/**
	 * Sets the next RFC 822 {@link Token}.
	 *
	 * @param nextToken the given the next RFC 822 {@link Token} to set.
	 */
	public void setNextToken(final Token nextToken) {
		this.nextToken = nextToken;
	}

	/**
	 * Sets the previous RFC 822 {@link Token}
	 *
	 * @param prevToken the given previous RFC 822 {@link Token} to set.
	 */
	public void setPrevToken(final Token prevToken) {
		this.prevToken = prevToken;
	}

	/**
	 * @return the previous RFC 822 token.
	 */
	public Token getPrevToken() {
		return prevToken;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if(this == obj) return true;
		if(obj == null || !(obj instanceof Token)) return false ;

		final Token another = (Token)obj ;

		return CompareService.equals(type, another.type) &&
			   CompareService.equals(value, another.value) &&
			   CompareService.equals(nextToken, another.nextToken) ;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// compute hash code.
		int hashCode = (type.hashCode() * 31 + value.hashCode()) ^ 17 ;

		// check the next token.
		if(nextToken != null){
			hashCode = (hashCode * 31 + nextToken.hashCode()) ^ 17 ;
		}

		// return the hash code.
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder token = new StringBuilder() ;

		token.append(type).append('{').append(value).append('}') ;
		if(nextToken != null){
			token.append(nextToken.toString()) ;
		}

		return token.toString() ;
	}

	/**
	 * TokenType
	 *
	 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
	 * @version $Id: Token.java 2009-08-14 11:33:19z nguyen_dv $
	 *
	 * Create date: Apr 18, 2009
	 * <pre>
	 *  The RFC 822 token type.
	 * </pre>
	 */
	public enum TokenType{
		ATOM,
		QSTRING,
		QCHAR,
		SPECIAL,
		GROUP,
		ROUTE,
		ADDR,
		PHRASE,
		SPACE
	}
}
