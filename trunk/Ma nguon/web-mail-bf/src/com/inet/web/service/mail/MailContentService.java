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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.mail.utils.MailHeaderHelper;
import com.inet.web.service.utils.WebCommonService;

/**
 * LoadMailContentService.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailContentService extends AbstractWebOSService {
	//~ Instance fields =======================================================
	/**
	 * the {@link MailBridgeBO} instance.
	 */
	private MailBridgeBO bridgeBO;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailContentService</tt> instance from the given
	 * {@link AccountManager} instance and {@link MailBridgeBO} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param bridgeBO the given {@link MailBridgeBO} instance.
	 */
	public MailContentService(
			AccountManager<Account> accountManager, 
			MailBridgeBO bridgeBO){
		super(accountManager) ;
		this.bridgeBO = bridgeBO;
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		String action = getData(request, MailConstants.ACTION);
		if(MailConstants.ACTION_SELECT.equals(action)){
			return loadContent(request);
		}else if(MailConstants.ACTION_CHECK_MAIL.equals(action)){
			return checkMail(request);
		}else if(MailConstants.ACTION_OPEN_EML.equals(action)){
			return openEml(request); 
		}
		return FAILURE_JSON;
	}
	
	
	private JSON loadContent(HttpServletRequest request){
		String headerId = getData(request,MailConstants.HEADER_ID);
		MailHeaderDTO headerDTO = null;
		try{
			headerDTO = bridgeBO.viewBody(WebCommonService.toLong(headerId));
		}catch(WebOSBOException wexp){
			return FAILURE_JSON;
		}
		
		if(headerDTO == null) return FAILURE_JSON;
		return MailHeaderHelper.convertMailHeaderDTO2JSon(headerDTO);
	}
	
	/**
	 * get JSON open attach which has extension EML
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON openEml(HttpServletRequest request){
		String headerId = request.getParameter(MailConstants.CONTENT_ID);
		String key = request.getParameter(MailConstants.ATTACHMENT_KEY);
		
		if(!StringService.hasLength(headerId) && !StringService.hasLength(key))
			return FAILURE_JSON;
		
		long id = Long.valueOf(headerId);
		byte[] data = bridgeBO.getAttachment(id, key).getValue();
		
		if(data != null  && data.length >0){
			try {
				// get mail configuration.
				AbstractMailFactory factory = MailConfigureFactory.createFactory() ;				
				
				// create message composer.
				IMessageComposer composer = factory.createMessage(data, true, true) ;
				
				// create message header and return to user.
				MailHeaderDTO headerDTO = new MailHeaderDTO(composer);
				return MailHeaderHelper.convertMailHeaderDTO2JSon(headerDTO);
			} catch (MailException mex) {
				return FAILURE_JSON;
			}
		}
		
		return FAILURE_JSON;
	}
	
	/**
	 * check new mail 
	 * @return JSON result 
	 */
	private JSON checkMail(HttpServletRequest request){
		String mode = getData(request,MailConstants.MODE);
		String account = getData(request,MailConstants.CFG_SMTP_ACCOUNT);
		try{
			if(MailConstants.MODE_CHECK_FETCH.equals(mode)){
				MailHeader header = bridgeBO.checkMail(account);
				if(header != null){
				  return MailHeaderHelper.convertMailHeader2JSON(header, true);
				}
			}else if(MailConstants.MODE_CHECK_NEW_MESSAGE.equals(mode)){
				int newMessage =  bridgeBO.checkNewMessage(account);
				return new JSONObject().accumulate(MailConstants.CONTENT_NEW_MESSAGE,newMessage);
			}
		}catch(WebOSBOException wex ){
			return FAILURE_JSON;
		}
		
		return FAILURE_JSON;
	}
	
}
