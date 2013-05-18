/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.web.service.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.MailPriority;
import com.inet.mail.parser.AddressParser;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailHeader;
import com.inet.mail.persistence.RecipientSender;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.bo.mail.MailHeaderBO;
import com.inet.web.bo.mail.MailRecipientBO;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailComposerHelper;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.utils.DocumentHelper;
import com.inet.web.service.utils.WebCommonService;

/**
 * LoadMailReplyService.
 *
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailComposerService  extends AbstractWebOSService{
	//~ Static fields =========================================================
        /**
         * logger.
         */
        private static final INetLogger logger = INetLogger.getLogger(MailComposerService.class) ;
        private static final String FIELD_NEW_RECIPIENTS = "newRecipients";
	/**
	 * the {@link MailBridgeBO} instance
	 */
	private final MailBridgeBO bridgeBO;
	/**
	 * the {@link MailHeaderBO} instance.
	 */
	private final MailHeaderBO headerBO;
	
	/**
	 * the {@link MailRecipientBO} instance.
	 */
	private final MailRecipientBO recipientBO;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailComposerService</tt> instance from the given
	 * {@link AccountManager} instance, {@link MailBridgeBO} instance
	 * and {@link MailHeaderBO} instance.
	 *
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param bridgeBO the given {@link MailBridgeBO} instance.
	 * @param headerBO the given {@link MailHeaderBO} instance.
	 *  @param recipientBO the given {@link MailRecipientBO} instance.
	 */
	public MailComposerService(AccountManager<Account> accountManager,
			MailBridgeBO bridgeBO,
			MailHeaderBO headerBO,
			MailRecipientBO recipientBO){
		super(accountManager) ;
		this.bridgeBO = bridgeBO;
		this.headerBO = headerBO;
		this.recipientBO = recipientBO;
	}

	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		long headerId = WebCommonService.toLong(getData(request,MailConstants.HEADER_ID));
		String action = getData(request,MailConstants.ACTION);
		if(MailConstants.ACTION_REPLY.equals(action)){
			return reply(request,headerId, false);
		}else if(MailConstants.ACTION_REPLY_ALL.equals(action)){
			return reply(request,headerId, true);
		}else if(MailConstants.ACTION_FORWARD.equals(action)){
			return forward(request,headerId);
		}else if(MailConstants.ACTION_DRAFT.equals(action)){
			return openDraft(headerId);
		}else if(MailConstants.ACTION_SAVE.equals(action)){
			return save(request,headerId);
		}else if(MailConstants.ACTION_SEND.equals(action)){
			return send(request,headerId);
		}
		return FAILURE_JSON;
	}

	/**
	 * @param headerId
	 * @param isReplyAll
	 * @return
	 */
	private JSON reply(HttpServletRequest request,long headerId, boolean isReplyAll){
		MailHeaderDTO headerDTO = bridgeBO.reply(headerId, isReplyAll);
		return MailComposerHelper.replyMail(request,headerDTO, true);
	}

	/**
	 * @param headerId
	 * @return
	 */
	private JSON forward(HttpServletRequest request,long headerId){
		MailHeaderDTO headerDTO = bridgeBO.forward(headerId);
		return MailComposerHelper.forwardMail(request,headerDTO);
	}

	/**
	 * @param headerId
	 * @return
	 */
	private JSON openDraft(long headerId){
		MailHeaderDTO headerDTO = bridgeBO.viewBody(headerId);
		return MailComposerHelper.openDraft(headerDTO);
	}

	/**
	 * @param request
	 * @param headerId
	 * @return
	 */
	private JSON save(HttpServletRequest request,long headerId){
		// get electronic document object.
		JSONObject object = WebCommonService.toJSONObject(request, MailConstants.OBJECT) ;
		IMessageComposer composer = null;
		MailHeader header ;
		if(headerId > 0L){
			header = headerBO.load(headerId);
			composer = createComposer(object,headerId);
			header = bridgeBO.save(header, composer);
		}else{
			composer = createComposer(object,0L);
			header = bridgeBO.save(composer);
		}
		return MailComposerHelper.save(header, composer);
	}

	/**
	 * @param request
	 * @param headerId
	 * @return
	 */
	private JSON send(HttpServletRequest request,long headerId){
		// get electronic document object.
		JSONObject object = WebCommonService.toJSONObject(request, MailConstants.OBJECT) ;
		String smtpAccount = WebCommonService.toString(object, MailConstants.CFG_SMTP_ACCOUNT);
		IMessageComposer composer = null;
		boolean result = false;
		if(headerId > 0L){
			composer = createComposer(object, headerId);
			result = bridgeBO.send(headerId, composer, smtpAccount);
		}else{
			composer = createComposer(object, 0L);
			result = bridgeBO.send(0L,composer, smtpAccount);
		}

		// add new recipient
		this.addRecipient(object);
		
		return result?SUCCESS_JSON:FAILURE_JSON;
	}

	/**
	 * @param object
	 * @return
	 */
	private IMessageComposer createComposer(JSONObject object, long contentId){
		MailAcctConfigInfo configure = bridgeBO.loadMailConfig() ;
		String from = WebCommonService.toString(object, MailConstants.FROM);

		String displayName = StringService.hasLength(configure.getFullname()) ? configure.getFullname():getFullName() ;

		// get mail configuration.
		AbstractMailFactory factory = MailConfigureFactory.createFactory() ;

		// create message composer.

		// priority
		String priorityStr = WebCommonService.toString(object, MailConstants.MAIL_PRIORITY);
		MailPriority priority = MailPriority.valueOf(priorityStr);

		IMessageComposer composer  = factory.createMessage(true, priority) ;

		// set from address.
		composer.setFrom(new Address(displayName, from)) ;

		//set subject
		composer.setSubject(StringUtils.strip(WebCommonService.toString(object, MailConstants.SUBJECT)));

		// set To address.
		String toAddress = WebCommonService.toString(object, MailConstants.TO);
		if(StringService.hasLength(toAddress)){
			// parses to addresses.
			Address[] addresses = AddressParser.parseHeader(toAddress) ;
			for(Address addr : addresses){
				composer.addTo(addr.getFullAddress()) ;
			}
		}
		
		//set CC address.
		String ccAddress = WebCommonService.toString(object, MailConstants.CC);
		if(StringService.hasLength(ccAddress)){
			// parses to addresses.
			Address[] addresses = AddressParser.parseHeader(ccAddress) ;
			for(Address addr : addresses){
				composer.addCC(addr.getFullAddress()) ;
			}
		}

		//set BCC address.
		String bccAddress = WebCommonService.toString(object, MailConstants.BCC);
		if(StringService.hasLength(bccAddress)){
			// parses to addresses.
			Address[] addresses = AddressParser.parseHeader(bccAddress) ;
			for(Address addr : addresses){
				composer.addBCC(addr.getFullAddress()) ;
			}
		}

		//set body text
		String body = WebCommonService.toString(object,MailConstants.BODY_TEXT);

		// convert this html string to TEXT String
		composer.setBody(body) ;

		// set attachment
		addAttchment(composer, object, contentId);

		return composer;
	}

	/**
	 * add new recipient to database
	 * @param object JSONOBJECT
	 */
	private void addRecipient(JSONObject object){
	  try{
	    String newRecipient = WebCommonService.toString(object, FIELD_NEW_RECIPIENTS);
  	    if(StringService.hasLength(newRecipient)){
  	      // parses to addresses.
    	      Address[] addresses = AddressParser.parseHeader(newRecipient) ;
              List<RecipientSender> senders = new ArrayList<RecipientSender>();
              
              for(Address addr : addresses){
                RecipientSender recipientSender = new RecipientSender();
                recipientSender.setOwner(getCode());
                recipientSender.setEmail(addr.getAddress());
                recipientSender.setRecipient(addr.getDisplayName());
                
                senders.add(recipientSender);
              }
              // check to add new recipient
              if(senders.size() > 0)
                this.recipientBO.add(senders);
            }      
	  }catch(WebOSException wex){
	    logger.error(wex);
	  }catch(Exception ex){
	    logger.error(ex);
	  }
	}
	
	/**
	 * @param composer
	 * @param json
	 */
	private void addAttchment(IMessageComposer composer, JSONObject json, long contentId){
		JSONArray jAttach = WebCommonService.toArray(json, MailConstants.ATTACHMENTS) ;

		if(jAttach == null) return;

		if(contentId > 0){
			Map<String, byte[]> mapAttach = bridgeBO.getAttachment(contentId);
			// attch is null
			if(mapAttach == null)
				mapAttach = new HashMap<String, byte[]>();

			for(int index = 0; index < jAttach.size(); index++){
				//get json object
				JSONObject object = jAttach.getJSONObject(index);

				// get key & name
				String key = WebCommonService.toString(object, MailConstants.ATTACHMENT_KEY);
				String name = WebCommonService.toString(object, MailConstants.ATTACHMENT_FILE_NAME);

				byte[] data = null;
				if(mapAttach.containsKey(key)){
					data = mapAttach.get(key);
				}else{
					data = DocumentHelper.getContent(key);
					DocumentHelper.deleteContent(key);
				}

				composer.addAttachment(name, data);
			}
		}else{
			for(int index = 0; index < jAttach.size(); index++){
				//get json object
				JSONObject object = jAttach.getJSONObject(index);
				String file = WebCommonService.toString(object, MailConstants.ATTACHMENT_KEY);
				String name = WebCommonService.toString(object, MailConstants.ATTACHMENT_FILE_NAME);
				byte[] data = DocumentHelper.getContent(file);
				composer.addAttachment(name, data);
			}
		}
	}
}