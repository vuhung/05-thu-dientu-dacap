/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.MailHeader;

/**
 * MailCommand.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class MailCommand extends AbstractCommand {

	/**
	 * @param headerSL
	 */
	public MailCommand(MailBridgeRemote bridgeSL) {
		super(bridgeSL);
	}

	/**
	 * 
	 * @param messageId
	 * @return
	 * @throws EJBException
	 */
	public MailHeaderDTO forward(Long messageId) throws EJBException
	{
		return this.bridgeSL.fowardMessage(messageId);
	}
	
	/**
	 * 
	 * @param messageId
	 * @return
	 * @throws EJBException
	 */
	public MailHeaderDTO reply(Long messageId) throws EJBException
	{
		return this.bridgeSL.replyMessage(messageId, false);
	}
	
	/**
	 * 
	 * @param messageId
	 * @param allRecps
	 * @return
	 * @throws EJBException
	 */
	public MailHeaderDTO reply(Long messageId,boolean allRecps) throws EJBException
	{
		return this.bridgeSL.replyMessage(messageId, allRecps);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.inet.mail.command.AbstractCommand#drafts(com.inet.mail.parser.sun.MessageComposer)
	 */
	@Override
	public MailHeader drafts(IMessageComposer composer) throws EJBException
	{
		return super.drafts(composer);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.inet.mail.command.AbstractCommand#drafts(com.inet.mail.persistence.MailHeader, com.inet.mail.parser.IMessageComposer)
	 */
	@Override
	public MailHeader drafts(MailHeader header,IMessageComposer composer) throws EJBException
	{
		return super.drafts(header,composer);
	}
	
}
