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
package com.inet.mail.spam;

import java.util.Date;

/**
 * ReceivedHeader
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ReceivedHeader.java 2009-01-07 21:46:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization ReceivedHeader class.
 * </pre>
 */
public interface ReceivedHeader {
	/**
	 * @return the sender host.
	 */
	String getSenderHost() ;
	
	/**
	 * @return the sender IP.
	 */
	String getSenderIP() ;
	
	/**
	 * @return the receiver host.
	 */
	String getReceiverHost() ;
	
	/**
	 * @return the date received email.
	 */
	Date getReceivedDate() ;
	
	/**
	 * @return if received header transfer in locally.
	 */
	boolean isInternalTransfer() ;
}
