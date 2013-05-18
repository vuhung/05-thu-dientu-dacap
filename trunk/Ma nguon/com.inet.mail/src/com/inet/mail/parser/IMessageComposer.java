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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inet.mail.data.Address;
import com.inet.mail.data.MailPriority;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;

/**
 * IMessageComposer
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 11, 2008
 * <pre>
 *  Initialization IMessageComposer class.
 * </pre>
 */
public interface IMessageComposer extends Serializable{	
	//~ Static fields =========================================================
	/**
	 *  From address. 
	 */
	static final String FROM_ADDRESS = "FROM";
	
	/**
	 * To address.
	 */
	static final String TO_ADDRESS = "TO";
	
	/**
	 * CC address.
	 */
	static final String CC_ADDRESS = "CC";
	
	/**
	 * BCC address.
	 */
	static final String BCC_ADDRESS = "BCC";
	
	//~ Methods ===============================================================
	/**
	 * Add attachments.
	 * 
	 * @param attachments HashMap<String, byte[]> - the given list of attachment.
	 */
	void addAttachment(Map<String, byte[]> attachments);
	
	/**
	 * Add attachment to system.
	 * 
	 * @param name String - the given attachment name.
	 * @param data byte[] - the given attachment data.
	 */
	void addAttachment(String name, byte[] data);
	
	/**
	 * @return the list of attachments.
	 * @throws MailException if an error occurs during getting attachment.
	 */
	Map<String, byte[]> getAttachments() throws MailException, MailParserException;		
	
	/**
	 * @return the list of attachments name.
	 * @throws MailException if an error occurs during getting attachment name.
	 */
	Set<String> getAttachName();
		
	/**
	 * Set from address.
	 * 
	 * @param from Address - the given from address.
	 */
	void setFrom(Address from);
	
	/**
	 * @return the form address.
	 */
	Address getFrom() ;	
	
	/**
	 * @return if the message has at least one attachments.
	 */
	boolean isAttached() ;	
	
	/**
	 * @return if the message is SPAM.
	 */
	boolean isSpam() ;
	
	/**
	 * Add list of to addresses to system.
	 * 
	 * @param to String[] - the given list of to addresses.
	 * @throws MailException if an error occurs during adding addresses to system.
	 */
	void addTo(String...to) throws MailException ;
	
	/**
	 * @return the list of to addresses.
	 */
	List<Address> getTo() ;	
	
	/**
	 * Add list of CC addresses to system.
	 * 
	 * @param cc String[] - the given list of CC addresses.
	 * @throws MailException if an error occurs during adding addresses to system.
	 */
	void addCC(String...cc) throws MailException ;
	
	/**
	 * @return the list of CC addresses.
	 */
	List<Address> getCC() ;	
	
	/**
	 * Add list of BCC addresses to system.
	 * 
	 * @param bcc String[] - the given list of BCC addresses. 
	 * @throws MailException if an error occurs during adding addresses to system.
	 */
	void addBCC(String...bcc) throws MailException ; 
	
	/**
	 * @return the list of BCC addresses.
	 */
	List<Address> getBCC() ;	
	
	/**
	 * @return all of mail addresses.
	 */
	Map<String, List<Address>> getAddresses() ;	
	
	/**
	 * @param first boolean - <code>true</code> will be get body at the first part, 
	 * otherwise get all content.
	 * 
	 * @return the body text.
	 * @throws MailException - if an error occurs during getting body content.
	 */
	String getBody();
	
	/**
	 * Set the body content.
	 * 
	 * @param body String - the given body content.
	 */
	void setBody(String body);	
		
	/**
	 * Compose message.
	 * 
	 * @return the message data under input stream.
	 * @throws MailException if errors occur during composing mail.
	 */
	Object compose() throws MailException, MailParserException ;
	
	/**
	 * @return the mail priority.
	 */
	MailPriority getPriority();
	
	/**
	 * @return the date when received email.
	 */
	Date getReceivedDate();
	
	/**
	 * @return the date when sent message.
	 */
	Date getSentDate();
	
	/**
	 * Set the sent date.
	 * 
	 * @param date Date  - the date when sent data.
	 */
	void setSentDate(Date date);
		
	/**
	 * @return the mail size.
	 */
	int getSize();
		
	/**
	 * @return the mail subject.
	 */
	String getSubject();
	
	/**
	 * Set the mail subject.
	 * 
	 * @param subject String - the given mail subject.
	 */
	void setSubject(String subject);
	
	/**
	 * @return <code>true</code> if the message format under HTML otherwise is plain text.
	 */
	boolean isHtmlSupport() ;
	
	/**
	 * Set the message is format under HTML or plain text.
	 * 
	 * @param htmlSupport <code>true</code> will be format under HTML, otherwise plain text.
	 */
	void setHtmlSupport(boolean htmlSupport) ;
	
	/**
	 * @return <code>true</code> if the message composer is calendar, otherwise <code>false</code>
	 */
	boolean isCalendar() ;
	
	/**
	 * Set the message composer is the calendar message.
	 * 
	 * @param calendar the given calendar message flag.
	 */
	void setCalendar(boolean calendar) ;
		
	/**
	 * @return the message universal identifier.
	 */
	String getUid() ;
	
	/**
	 * @return the mail header.
	 */
	MessageHeader getHeader() ;
	
	/**
	 * @return the object data.
	 */
	byte[] toByte() throws MailException, MailParserException;
}
