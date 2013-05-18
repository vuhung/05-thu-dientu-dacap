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
package com.inet.web.service.mail.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.service.DateService;
import com.inet.base.service.FileService;
import com.inet.base.service.StringService;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.parser.AddressParser;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.common.json.JSONService;

/**
 * MailComposerUtils.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailComposerUtils {
	
	/**
	 * convert MailHeaderDTO to JSON in reply email
	 * @param request
	 * @param headerDTO
	 * @param isReplyAll
	 * @return
	 */
	public static JSON replyMail(HttpServletRequest request,MailHeaderDTO headerDTO, boolean isReplyAll){
		String msgReply = request.getParameter(MailConstants.MSG_REPLY);
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID, headerDTO.getHeaderId());
		// set To address
		if(headerDTO.getTo() != null && headerDTO.getTo().size()> 0){
			json.accumulate(MailConstants.TO,AddressParser.toString(headerDTO.getTo()));
		}		
		//set CC address
		if(isReplyAll){
			if(headerDTO.getCc() != null && headerDTO.getCc().size()> 0)
				json.accumulate(MailConstants.CC,AddressParser.toString(headerDTO.getCc()));
		}
		// set BCC address 
		if(headerDTO.getBcc() != null && headerDTO.getBcc().size()> 0)
			json.accumulate(MailConstants.BCC,AddressParser.toString(headerDTO.getCc()));
		
		json.accumulate(MailConstants.SUBJECT, StringService.hasLength(headerDTO.getSubject())?
											headerDTO.getSubject():StringService.EMPTY_STRING);
		String body = StringService.hasLength(headerDTO.getBodyText())?
									headerDTO.getBodyText():StringService.EMPTY_STRING;
		msgReply = String.format(msgReply, DateService.format(headerDTO.getSent(),"HH:mm:ss dd-MM-yyyy") ,headerDTO.getSender());							
		json.accumulate(MailConstants.BODY_TEXT, msgReply + body);
		return json;
	}
	
	/**
	 * convert MailHeaderDTO to JSON in forward email
	 * @param request
	 * @param headerDTO
	 * @return
	 */
	public static JSON forwardMail(HttpServletRequest request,MailHeaderDTO headerDTO){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID, headerDTO.getHeaderId());
		// set To address
		if(headerDTO.getTo() != null && headerDTO.getTo().size()> 0){
			json.accumulate(MailConstants.TO,AddressParser.toString(headerDTO.getTo()));
		}		
		
		// set BCC address 
		if(headerDTO.getBcc() != null && headerDTO.getBcc().size()> 0)
			json.accumulate(MailConstants.BCC,AddressParser.toString(headerDTO.getCc()));
		
		json.accumulate(MailConstants.SUBJECT, StringService.hasLength(headerDTO.getSubject())?
											headerDTO.getSubject():StringService.EMPTY_STRING);
		json.accumulate(MailConstants.BODY_TEXT,  StringService.hasLength(headerDTO.getBodyText())?
				headerDTO.getBodyText():StringService.EMPTY_STRING);
		return json;
	}
	
	/**
	 * convert MailHeaderDTO to JSON in open draft
	 * @param headerDTO
	 * @return
	 */
	public static JSON openDraft(MailHeaderDTO headerDTO){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID, headerDTO.getHeaderId());
		// set To address
		if(headerDTO.getFrom() != null && headerDTO.getFrom().size()> 0){
			json.accumulate(MailConstants.TO,AddressParser.toString(headerDTO.getTo()));
		}else{
			json.accumulate(MailConstants.TO,StringService.EMPTY_STRING);
		}
		//set CC address
		if(headerDTO.getCc() != null && headerDTO.getCc().size()> 0){
				json.accumulate(MailConstants.CC,AddressParser.toString(headerDTO.getCc()));
		}else{
			json.accumulate(MailConstants.CC,StringService.EMPTY_STRING);
		}
		// set BCC address
		if(headerDTO.getBcc() != null && headerDTO.getBcc().size()> 0){
			json.accumulate(MailConstants.BCC,AddressParser.toString(headerDTO.getBcc()));
		}else{
			json.accumulate(MailConstants.BCC,StringService.EMPTY_STRING);
		}
		json.accumulate(MailConstants.SUBJECT,StringService.hasLength(headerDTO.getSubject())?
				headerDTO.getSubject():StringService.EMPTY_STRING);
		json.accumulate(MailConstants.BODY_TEXT,StringService.hasLength(headerDTO.getBodyText())?
				headerDTO.getBodyText():StringService.EMPTY_STRING);
		json.accumulate(MailConstants.ATTACHMENTS, JSONService.toJSONArray(convertAttachments(headerDTO)));
		
		return json;
	}
	
	/**
	 * @param header
	 * @return
	 */
	public static JSON save(MailHeader header){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID,header.getId());
		return json;	
	}
	
	/**
	 * convert attachment to JSON
	 * @param header MailHeaderDTO
	 * @return List<JSONObject>
	 */
	private static List<JSONObject> convertAttachments(MailHeaderDTO header){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		for(String item : header.getAttachments()){
			JSONObject json = new JSONObject();
			String fileName =  AttachmentParser.getFileName(item);
			json.accumulate(MailConstants.ATTACHMENT_FILE_NAME, fileName)
				.accumulate(MailConstants.ATTACHMENT_KEY,item)
				.accumulate(MailConstants.ATTACHMENT_ICON,FileService.getExtension(fileName));
			jsons.add(json);
		}
		return jsons;
	}
}
