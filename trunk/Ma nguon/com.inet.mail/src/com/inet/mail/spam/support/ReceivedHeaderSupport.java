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
package com.inet.mail.spam.support;

import java.util.Date;

import com.inet.base.service.StringService;
import com.inet.mail.spam.ReceivedHeader;

/**
 * ReceivedHeaderSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ReceivedHeaderSupport.java 2009-01-08 10:39:00z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization ReceivedHeaderSupport class.
 * </pre>
 */
public class ReceivedHeaderSupport implements ReceivedHeader {
	//~ Static field ==========================================================
	/* the local address. */
	private static final String LOCALHOST = "localhost" ;
	
	/* local IP. */
	private static final String LOCAL_IP = "127.0.0.1" ;
	
	//~ Instance fields =======================================================
	/* receiver host*/
	private String receiverHost ;
	/* received date. */
	private Date receivedDate ;
	/* sender host */
	private String senderHost ;
	/* sender IP address. */
	private String senderIP ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeader#getReceivedDate()
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	/**
	 * Set the received date.
	 * 
	 * @param receivedDate the given received date to set.
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeader#getReceiverHost()
	 */
	public String getReceiverHost() {
		return receiverHost;
	}
	
	/**
	 * Sets the receiver host.
	 * 
	 * @param receiverHost the given receiver host to set.
	 */
	public void setReceiverHost(String receiverHost) {
		this.receiverHost = receiverHost;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeader#getSenderHost()
	 */
	public String getSenderHost() {
		return senderHost;
	}
	
	/**
	 * Sets the sender host.
	 * 
	 * @param senderHost the given sender host to set.
	 */
	public void setSenderHost(String senderHost) {
		this.senderHost = senderHost;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeader#getSenderIP()
	 */
	public String getSenderIP() {
		return senderIP;
	}
	
	/**
	 * Sets the sender IP address.
	 * 
	 * @param senderIP the sender IP address to set.
	 */
	public void setSenderIP(String senderIP) {
		this.senderIP = senderIP;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeader#isInternalTransfer()
	 */
	public boolean isInternalTransfer() {
		// check sender host and sender ip.
		if(!StringService.hasLength(senderHost)) return true ;
		String lsh = senderHost.toLowerCase() ;		
		if(lsh.contains(LOCALHOST)) return true ;
		if(StringService.hasLength(senderIP) && LOCAL_IP.equals(senderIP.trim())){
			return true ;
		}
		
		// check received host.
		if(StringService.hasLength(receiverHost) 
				&& receiverHost.toLowerCase().contains(LOCALHOST)){
			return true ;
		}
		
		// external transfer.
		return false;
	}
	
	/**
	 * Returns the object representation as string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer() ;
		
		// prepare data to return to sub system.
		buffer.append(getClass().getName()).append("[") 
			  .append("Sender Host: ").append(senderHost).append("[").append(senderIP).append("]")
			  .append("; Receiver Host: ").append(receiverHost)
			  .append("; Received Date: ").append(receivedDate)
			  .append("; Internal Transfer: ").append(isInternalTransfer() ? "YES" : "NO")
			  .append("]") ;
		
		return buffer.toString();
	}
}
