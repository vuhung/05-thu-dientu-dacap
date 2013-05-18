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
package com.inet.mail.command;

import java.util.Map;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.PairValueDTO;

/**
 * MailDataQuery
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 15, 2008
 * 
 *       <pre>
 *  Initialization MailDataQuery class.
 * </pre>
 */
public class MailDataQuery {
	// mail business bean.
	private MailBridgeRemote bridgeSL;

	/**
	 * Create mail data query from the given mail business bean.
	 * 
	 * @param headerSL
	 *            MailBridgeRemote - the given mail business bean.
	 */
	public MailDataQuery(MailBridgeRemote bridgeSL) {
		this.bridgeSL = bridgeSL;
	}

	/**
	 * View mail body.
	 * 
	 * @param header
	 *            MailHeader - the given mail header instance.
	 * @return the mail header data.
	 * @throws EJBException
	 *             if an error occurs during loading body.
	 */
	public MailHeaderDTO viewBody(long headerId) throws EJBException {
		return this.bridgeSL.viewBody(headerId);
	}
	
	/**
	 * View attachment from the given attachment key.
	 * 
	 * @param bizId
	 * @param fileindex
	 * @return
	 * @throws EJBException
	 */
	public  PairValueDTO<byte[]> viewAttachment(long bizId, String fileindex)
			throws EJBException {
		return bridgeSL.viewAttachment(bizId,fileindex);
	}
	
	/**
	 * View all attachment.
	 * 
	 * @param bizId the given mail business identifier.
	 * @return the map of mail attachments.
	 * @throws EJBException when errors occurs during view attachments.
	 */
	public Map<String, byte[]> viewAttachment(long bizId) throws EJBException{
		return bridgeSL.viewAttachment(bizId);
	}
}
