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
package com.inet.mail.parser.support.sun;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.inet.base.service.CompareService;
import com.inet.base.service.DateService;
import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.exception.MailException;
import com.inet.mail.message.Messages;
import com.inet.mail.parser.AbstractMessageFactory;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.util.MailService;

/**
 * SunMessageFactory
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SunMessageFactory.java Dec 10, 2008 nguyen_dv $
 *
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization SunMessageFactory class.
 * </pre>
 */
public class SunMessageFactory extends AbstractMessageFactory {
	//~ Static fields =========================================================
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.parser.AbstractMessageFactory#reply(com.inet.mail.data.MailHeaderDTO, java.lang.Object[])
	 */
	public IMessageComposer reply(MailHeaderDTO headerDTO, Object... objects)
			throws MailException {
		// get message.
		if(objects.length != 4) throw new MailException("Invalidate arguments") ;

		try{
			Session session = (Session)objects[0] ;
			boolean receipients = ((Boolean)objects[1]).booleanValue() ;
			String msg = (String)objects[2] ;
			String originalMessage = (String)objects[3] ;
			// create reply message.
			Message replyMessage = new MimeMessage(session) ;
			replyMessage.saveChanges() ;

			// create the reply message.
			IMessageComposer reply = new MessageComposer(replyMessage, false, false) ;

			// get subject.
			String subject = headerDTO.getSubject() ;
			if(StringService.hasLength(subject) && !subject.startsWith("Re:")){
				subject = "Re: " + subject ;
			}else if(!StringService.isset(subject) || StringService.isEmpty(subject)){
				subject = "Re: " ;
			}
			reply.setSubject(subject) ;

			// get from address.
			List<Address> froms = headerDTO.getFrom() ;
			Address from = null ;
			if(froms != null && froms.size() > 0){
				from = froms.get(0) ;
			}
			reply.addTo(from.getFullAddress()) ;

			// reply to all recipients.
			if(receipients){
				// build CC address.
				List<Address> ccAddrs = new ArrayList<Address>() ;

				// add to addresses.
				if(headerDTO.getTo() != null && headerDTO.getTo().size() > 0){
					ccAddrs.addAll(headerDTO.getTo()) ;
				}

				if(headerDTO.getCc() != null && headerDTO.getCc().size() > 0){
					ccAddrs.addAll(headerDTO.getCc()) ;
				}

				// create duplicates data.
				List<Address> duplicates = new ArrayList<Address>() ;

				// remove the from address.
				for(Address address : ccAddrs){
					if(CompareService.equals(address.getAddress(), from.getAddress())){
						duplicates.add(address) ;
					}
				}

				// remove duplicate.
				for(Address address : duplicates){
					ccAddrs.remove(address) ;
				}

				// get list of CC addresses.
				String[] cc = MailService.buildAddress(ccAddrs).toArray(new String[ccAddrs.size()]) ;

				// add all address to CC in reply mail.
				reply.addCC(cc) ;
			}

			// build reply body.
			StringBuilder builder = new StringBuilder() ;

			// prepare message.
			builder.append("<br/><br/><br/>")
				   .append("----- " + originalMessage + " -----" + "<br/>")
				   .append(DateService.format(headerDTO.getSent(), DateService.SQL_DATETIME_PATTERN))
				   .append(StringService.BLANK)
				   .append(StringService.hasLength(from.getDisplayName()) ? from.getDisplayName() : from.getAddress())
				   .append(StringService.BLANK)
				   .append(msg)
				   .append(": ")
				   .append("<br/>")
				   .append(headerDTO.getBodyText()) ;

			reply.setBody(builder.toString()) ;

			return reply;
		}catch(MessagingException mex){
			throw new MailException(mex.getMessage(), mex) ;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.inet.mail.parser.AbstractMessageFactory#reply(IMessageComposer, String, String, boolean)
	 */
	public IMessageComposer reply(IMessageComposer composer, String originalMsg, boolean replyAll)
			throws MailException {
		// show information.
		if(logger.isDebugEnabled()) logger.debug("BEGIN reply, composer: [" + composer + "], msg: [" + originalMsg + "], reply all: [" + replyAll + "].") ;

		// create message.
		MessageComposer reply = new MessageComposer(true);

		// set reply subject.
		String subject = composer.getSubject() ;
		if(StringService.hasLength(subject) && !subject.startsWith("Re:")){
			subject = "Re: " + subject ;
		}else if(!StringService.isset(subject) || StringService.isEmpty(subject)){
			subject = "Re: " ;
		}
		reply.setSubject(subject) ;

		// get from address.
		Address from = composer.getFrom() ;
		reply.addTo(from.getFullAddress()) ;

		// reply to all recipients.
		if(replyAll){
			// build CC address.
			List<Address> ccAddrs = new ArrayList<Address>() ;

			// get to address.
			List<Address> tmpAddrs = composer.getTo() ;

			// add to addresses.
			if(tmpAddrs != null && tmpAddrs.size() > 0){
				ccAddrs.addAll(tmpAddrs) ;
			}

			// get CC address.
			tmpAddrs = composer.getCC() ;
			if(tmpAddrs != null && tmpAddrs.size() > 0){
				ccAddrs.addAll(tmpAddrs) ;
			}

			// create duplicates data.
			List<Address> duplicates = new ArrayList<Address>() ;

			// remove the from address.
			for(Address address : ccAddrs){
				if(CompareService.equals(address.getAddress(), from.getAddress())){
					duplicates.add(address) ;
				}
			}

			// remove duplicate.
			for(Address address : duplicates){
				ccAddrs.remove(address) ;
			}

			if(ccAddrs != null && ccAddrs.size() > 0){
				// get list of CC addresses.
				String[] cc = MailService.buildAddress(ccAddrs).toArray(new String[ccAddrs.size()]) ;

				// add all address to CC in reply mail.
				reply.addCC(cc) ;
			}
		}

		// build reply body.
		StringBuilder builder = new StringBuilder() ;

		// prepare message.
		builder.append(createHeader(originalMsg, composer))
			   .append("<BLOCKQUOTE TYPE=CITE> ")
			   .append(composer.getBody())
			   .append("</BLOCKQUOTE>");

		reply.setBody(builder.toString()) ;

		if(logger.isDebugEnabled()) logger.debug("END reply message, reply: [" + reply + "].") ;

		// return reply message.
		return reply;
	}

	/**
	 * Create forward message from the given mail {@link IMessageComposer}
	 * and the forward message.
	 *
	 * @param composer the given {@link IMessageComposer} instance.
	 * @param forwardMsg the given forward message.
	 * @return the forward message.
	 * @throws MailException when error occurs during creating the forward message.
	 */
	public IMessageComposer forward(IMessageComposer composer, String forwardMsg)
		throws MailException{
		// create message.
		MessageComposer forward = new MessageComposer(true);

		// set reply subject.
		String subject = composer.getSubject() ;
		if(StringService.hasLength(subject) && !subject.startsWith("Fwd:")){
			subject = "Fwd: " + subject ;
		}else if(!StringService.isset(subject) || StringService.isEmpty(subject)){
			subject = "Fwd: " ;
		}
		forward.setSubject(subject) ;

		// build the forward body.
		StringBuilder builder = new StringBuilder() ;

		// prepare message.
		builder.append(createHeader(forwardMsg, composer))
			   .append(composer.getBody()) ;

		forward.setBody(builder.toString()) ;

		// set forward attachment.
		if(composer.isAttached()){
			forward.addAttachment(composer.getAttachments()) ;
		}

		// return forward message.
		return forward ;
	}

	//~ Helper methods ========================================================
	/**
	 * Create the message header from the given message and {@link IMessageComposer} instance.
	 *
	 * @param msg the given mail message.
	 * @param composer the given message composer.
	 *
	 * @throws MailException when error occurs during creating header.
	 */
	protected String createHeader(String msg, IMessageComposer composer) throws MailException{
		StringBuffer buffer = new StringBuffer() ;

		buffer.append("<br/><br/><br/>").append("----- ").append(msg).append(" -----").append("<br/>")
			  .append(Messages.getMessage(Messages.mail_message_from)).append(": ").append(composer.getFrom().toUnicodeString()) ;

		// build to header.
		List<Address> addrs = composer.getTo() ;
		StringBuilder addrBuf = null ;
		if(addrs != null && addrs.size() > 0){
			addrBuf = new StringBuilder() ;
			for(Address address : addrs){
				addrBuf.append(address.toUnicodeString()).append(", ") ;
			}

			buffer.append("<br/>").append(Messages.getMessage(Messages.mail_message_to)).append(": ").append(addrBuf.substring(0, addrBuf.length() - 2)) ;
		}

		// build cc header.
		addrs = composer.getCC() ;
		addrBuf = null ;
		if(addrs != null && addrs.size() > 0){
			addrBuf = new StringBuilder() ;
			for(Address address : addrs){
				addrBuf.append(address.toUnicodeString()).append(", ") ;
			}

			buffer.append("<br/>").append(Messages.getMessage(Messages.mail_message_cc)).append(": ").append(addrBuf.substring(0, addrBuf.length() - 2)) ;
		}

		// build subject header.
		String subject = composer.getSubject() ;
		buffer.append("<br/>").append(Messages.getMessage(Messages.mail_message_subject)).append(": ").append(StringService.hasLength(subject) ? subject : StringService.EMPTY_STRING) ;
		buffer.append("<br/><br/><br/>") ;

		// return message header.
		return buffer.toString() ;
	}
}
