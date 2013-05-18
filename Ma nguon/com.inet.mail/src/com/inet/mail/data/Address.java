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
import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

import com.inet.base.service.Assert;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.mail.parser.AddressParser;
import com.inet.mail.parser.ParserException;
import com.inet.mail.parser.rfc822.util.Rfc822Helper;
import com.inet.mail.util.MailService;

/**
 * Address
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 *
 * @date Jan 28, 2008
 * <pre>
 *  Initialization Address class.
 * </pre>
 */
public class Address implements Serializable, Comparable<Address>, Cloneable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8966569394675269023L;

	/**
	 * empty array.
	 */
	public static final Address[] EMPTY_ARRAY = new Address[0] ;

	// the mail address display name.
	private String displayName ;

	// the encode display name.
	private String encDisplayName;

	// the mail address.
	private String address ;

	/**
	 * Create Address instance from the given address.
	 *
	 * @param address String - the given mail address.
	 */
	public Address(String address){
		this(StringService.EMPTY_STRING, address) ;
	}

	/**
	 * Create Address instance from the given display name and address.
	 *
	 * @param displayName String - the given display name.
	 * @param address String - the given address.
	 */
	public Address(String displayName, String address){
		Assert.hasLength(address, "The email address must be not null or empty.") ;

		// set address.
		this.address = address ;
		setDisplayName(displayName) ;
	}

	/**
	 * @return the display name.
	 */
	public String getDisplayName(){
		// decode display name.
		if(displayName == null && encDisplayName != null){
			try{
				displayName = MimeUtility.decodeText(encDisplayName) ;
			} catch (final UnsupportedEncodingException ueex) { 
				this.displayName = encDisplayName ;
				encDisplayName = null ;
			}
		}

		return displayName ;
	}

	/**
	 * Set the display name.
	 *
	 * @param displayName String - the given display name.
	 */
	public void setDisplayName(final String displayName){
		if(!StringService.hasLength(displayName)){
			encDisplayName = null ;
			this.displayName = null ;

			return ;
		}

		// un-quote display name.
		encDisplayName = Rfc822Helper.unquote(displayName).toString() ;

		try{
			this.displayName = MimeUtility.decodeText(encDisplayName) ;
		} catch (final UnsupportedEncodingException ueex) {
			this.displayName = encDisplayName ;
			encDisplayName = null ;
		}
	}

	/**
	 * @return the canonical address based on RCF2822.
	 */
	public String getCanonicalAddress(){
		return isGroup() ? address : '<' + address + '>' ;
	}

	/**
	 * @return the mail address.
	 */
	public String getAddress(){
		return address ;
	}

	/**
	 * @return the full email address based on RFC2822.
	 */
	public String getFullAddress(){
		if(displayName != null && encDisplayName == null){
			try{
				encDisplayName = MimeUtility.encodeWord(displayName, MailService.DEFAULT_CHARSET, MailService.DEFAULT_ENCODING) ;
			}catch(UnsupportedEncodingException uex){
				encDisplayName = displayName ;
			}
		}

		if(StringService.hasLength(encDisplayName)){
			return (Rfc822Helper.quote(encDisplayName).toString() + " <" + address + ">") ;
		}

		if(isGroup() || isSimple()) return address ;
		return '<' + address + '>';
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getFullAddress() ;
	}

	/**
	 * @return the e-mail address format under unicode string.
	 */
	public String toUnicodeString(){
		if(displayName == null && encDisplayName != null){
			try{
				displayName = MimeUtility.decodeText(encDisplayName) ;
			} catch (final UnsupportedEncodingException uex) {
				displayName = encDisplayName;
			} 
		}

		if(StringService.hasLength(displayName)){
			return (Rfc822Helper.quote(displayName).toString() + " <" + address + ">") ;
		}

		if(isGroup() || isSimple()) return address ;
		return '<' + address + '>' ;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return address.hashCode() ;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj == null || !(obj  instanceof Address)) return false ;

		// convert to address instance.
		Address address = (Address)obj ;
		return address.equals(address.address) ;
	}

	/**
	 * @return the clonable object.
	 *
	 * @throws CloneNotSupportedException if errors occur during cloning object.
	 */
	public Object clone() throws CloneNotSupportedException {
		return IOService.makeNewObject(this, Address.class);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Address other) {
		return this.address.compareTo(other.getAddress());
	}

	/**
	 * Parses the given input to email address instance.
	 *
	 * @param input CharSequence - the given input data.
	 * @return the email address instance.
	 * @throws ParserException if errors occur during parsing data.
	 */
	public static Address parse(CharSequence input) throws ParserException{
		return AddressParser.parse(input) ;
	}

	/**
	 * @return if the address is simple address.
	 */
	public boolean isSimple(){
		return (indexOfAny(address, "()<>,;:\\\"[]", 0) == -1);
	}

	/**
	 * @return if the address is group.
	 */
	public boolean isGroup(){
		return (address != null && address.endsWith(";") && address.indexOf(":") >= 0) ;
	}

	/**
	 * @return the array of {@link Address}; never null.
	 *
	 * @see AddressParser#parseHeader(CharSequence)
	 */
	public Address[] getGroup(){
		if(!address.endsWith(";")) return EMPTY_ARRAY ;

		// get the starting of address position.
		int startPos = address.indexOf(':') ;
		if(startPos < 0) return EMPTY_ARRAY ;

		// parse array and return array of address.
		return AddressParser.parseHeader(address.substring(startPos + 1, address.length() - 1)) ;
	}

	/**
	 * Check the one of character in <code>any</code> parameter is existing on <code>data</code>
	 * value.
	 *
	 * @param data the given data to check.
	 * @param any the given any character to check.
	 * @param position the given position to check.
	 *
	 * @return the position of first maching leter.
	 */
	private static int indexOfAny(final String data, final String any, int position){
		if(!StringService.hasLength(data) || !StringService.hasLength(any)) return -1;

		try{
			for(int index = position; index < data.length(); index++){
				if(any.indexOf(data.charAt(index)) >= 0){
					return index ;
				}
			}

			return -1; // does not matching data.
		}catch(IndexOutOfBoundsException ioex){
			return -1; // does not matching any character.
		}
	}
}
