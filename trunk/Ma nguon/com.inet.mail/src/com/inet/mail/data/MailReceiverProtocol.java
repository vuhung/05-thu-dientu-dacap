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
 * MailReceiverProtocol
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 27.12.2007
 * <pre>
 * 	Initialization mail receiver protocol.
 * </pre>
 */
public enum MailReceiverProtocol {
	POP3,	// POP3 protocol.
	IMAP4,	// IMAP4 protocol.
	SMTP, // SMTP protocol
	UNKNOWN;	// Unknown protocol.
	
	//-------------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Return mail protocol.
	 */
	public MailProtocol getProtocol(){
		try{
			return MailProtocol.valueOf(this.name()) ;
		}catch(Exception ex){
			// default POP3 protocol.
			return MailProtocol.POP3 ;
		}
	}
}
