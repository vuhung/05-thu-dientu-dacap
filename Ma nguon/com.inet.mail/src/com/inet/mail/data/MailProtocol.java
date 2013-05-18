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

/**
 * MailProtocol
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 28, 2008
 * <pre>
 *  Initialization MailProtocol class.
 * </pre>
 */
public enum MailProtocol {
	/**
	 * SMTP protocol.
	 */
	SMTP("smtp", 25),
	/**
	 * POP3 protocol.
	 */
	POP3("pop3", 110),
	/**
	 * IMAP4 protocol.
	 */
	IMAP4("imap", 143);
	
	//----------------------------------------------------
	// Enumeration data.
	//
	/**
	 * the protocol value.
	 */
	private String protocol ;
	/**
	 * the port value.
	 */
	private int port ;
	
	/**
	 * Create MailProtocol instance.
	 * 
	 * @param protocol String - the given mail protocol.
	 */
	private MailProtocol(String protocol, int port){
		this.protocol = protocol ;
		this.port = port ;
	}
	
	/**
	 * @return the mail protocol.
	 */
	public String getProtocol(){
		return this.protocol ;
	}
	
	/**
	 * @return the default port.
	 */
	public int getPort(){
		return this.port ;
	}
	
	/**
	 * @return the secure protocol name.
	 */
	public String getSecureProtocol(){
		return this.protocol + 's' ;
	}
}
