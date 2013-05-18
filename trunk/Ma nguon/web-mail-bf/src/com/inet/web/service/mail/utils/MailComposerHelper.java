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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.parser.AddressParser;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.common.json.JSONService;

/**
 * MailComposerUtils.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailComposerHelper {
	
	/**
	 * convert MailHeaderDTO to JSON in reply email
	 * @param request
	 * @param headerDTO
	 * @param isReplyAll
	 * @return
	 */
	public static JSON replyMail(HttpServletRequest request,MailHeaderDTO headerDTO, boolean isReplyAll){
		
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID, headerDTO.getHeaderId());
		json.accumulate(MailConstants.MAIL_PRIORITY, headerDTO.getPriority().toString());
		// set To address
		if(headerDTO.getTo() != null && headerDTO.getTo().size()> 0){
			json.accumulate(MailConstants.TO,AddressParser.toUnicodeString(headerDTO.getTo()));
		}		
		//set CC address
		if(isReplyAll){
			if(headerDTO.getCc() != null && headerDTO.getCc().size()> 0)
				json.accumulate(MailConstants.CC,AddressParser.toUnicodeString(headerDTO.getCc()));
		}
		// set BCC address 
		if(headerDTO.getBcc() != null && headerDTO.getBcc().size()> 0)
			json.accumulate(MailConstants.BCC,AddressParser.toUnicodeString(headerDTO.getCc()));
		
		json.accumulate(MailConstants.SUBJECT, StringService.hasLength(headerDTO.getSubject())?
											headerDTO.getSubject():StringService.EMPTY_STRING);
		String body = StringService.hasLength(headerDTO.getBodyText())?
									headerDTO.getBodyText():StringService.EMPTY_STRING;
									
		json.accumulate(MailConstants.BODY_TEXT, body);
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
		json.accumulate(MailConstants.MAIL_PRIORITY, headerDTO.getPriority().toString());
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
		
		json.accumulate(MailConstants.ATTACHMENTS, JSONService.toJSONArray(convertAttachments(headerDTO)));
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
		json.accumulate(MailConstants.MAIL_PRIORITY, headerDTO.getPriority().toString());
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
	 * 
	 * @param header
	 * @param composer
	 * @return JSON
	 */
	public static JSON save(MailHeader header, IMessageComposer composer){
		JSONObject object = new JSONObject();
		
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		Map<String,byte[]> map = composer.getAttachments();
		if(map != null && map.size() > 0){
			for(String item : map.keySet()){
				JSONObject json = new JSONObject();
				String fileName =  AttachmentParser.getFileName(item);
				json.accumulate(MailConstants.ATTACHMENT_FILE_NAME, fileName)
					.accumulate(MailConstants.ATTACHMENT_KEY,item)
					.accumulate(MailConstants.ATTACHMENT_ICON, DocumentFormatHelper.getExtension(fileName));
				jsons.add(json);
			}
		}
		object.accumulate(MailConstants.HEADER_ID,header.getId())
			.accumulate(MailConstants.ATTACHMENTS, JSONService.toJSONArray(jsons));
		
		return object;
	}
	
	/**
	 * convert attachment to JSON
	 * @param header MailHeaderDTO
	 * @return List<JSONObject>
	 */
	private static List<JSONObject> convertAttachments(MailHeaderDTO header){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		if(header != null && header.getAttachments() != null && header.getAttachments().size() >0){
			for(String item : header.getAttachments()){
				JSONObject json = new JSONObject();
				String fileName =  AttachmentParser.getFileName(item);
				json.accumulate(MailConstants.ATTACHMENT_FILE_NAME, fileName)
					.accumulate(MailConstants.ATTACHMENT_KEY,item)
					.accumulate(MailConstants.ATTACHMENT_ICON, DocumentFormatHelper.getExtension(fileName));
				jsons.add(json);
			}
		}
		return jsons;
	}
}
