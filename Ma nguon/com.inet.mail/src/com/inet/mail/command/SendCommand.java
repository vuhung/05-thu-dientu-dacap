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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailBridgeRemote;
import com.inet.mail.parser.support.sun.MessageComposer;
import com.inet.mail.persistence.MailHeader;

/**
 * SendCommand
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 14, 2008
 * <pre>
 *  Initialization SendCommand class.
 * </pre>
 */
public class SendCommand extends AbstractCommand {
	/**
	 * Create SendCommand from the given mail business and mail configure.
	 * 
	 * @param bridgeSL MailBridgeRemote - the given mail business bean.
	 */
	public SendCommand(MailBridgeRemote bridgeSL) {
		super(bridgeSL);
	}

	/**
	 * Sends draft mail.
	 * 
	 * @param composer MessageComposer - the given message composer.
	 * @param mailId long - the given mail identifier.
	 * @throws EJBException if an error occurs during sending mail.
	 */
	public void send(MessageComposer composer, long mailId, String host, String account) throws EJBException{
		super.send(composer,mailId, host, account) ;
	}
	
	/**
	 * 
	 * @param composer
	 * @throws EJBException
	 */
	public void send(MessageComposer composer, String host, String account) throws EJBException{
		MailHeader header = super.drafts(composer);
		send(composer,header.getId(), host, account);
	}
	/**
	 * Send a message
	 * @param mailId
	 * @throws EJBException
	 */
	public void send(long mailId, String host, String account) throws EJBException{
		super.send(mailId,host,account) ;
	}
}
