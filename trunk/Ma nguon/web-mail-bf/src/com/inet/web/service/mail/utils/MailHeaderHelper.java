/*****************************************************************
   Copyright 2007 by Truong Ngoc Tan (tntan@truthinet.com)

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

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;

import com.inet.base.service.CommonService;
import com.inet.base.service.StringService;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.parser.AddressParser;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.persistence.MailHeader;
import com.inet.mail.util.MailService;
import com.inet.web.common.json.JSONService;


/**
 * MailHeaderService.
 *
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 0.2i
 */
public class MailHeaderHelper {
	/**
	 * convert to JSON from given Mail Header
	 * @param header MailHeader
	 * @return JSON
	 */
	public static JSONObject convertMailHeader2JSON(MailHeader header, boolean isFetch){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.HEADER_ID, header.getId())
			.accumulate(MailConstants.MAIL_PRIORITY, header.getPriority().toString())
			.accumulate(MailConstants.SENDER, StringEscapeUtils.escapeHtml(StringService.hasLength(header.getSender())?header.getSender().replace("\"", StringService.EMPTY_STRING):
																StringService.EMPTY_STRING))
			.accumulate(MailConstants.RECIPIENT, StringService.hasLength(header.getRecipients())?header.getRecipients():StringService.EMPTY_STRING)
			.accumulate(MailConstants.SUBJECT, StringEscapeUtils.escapeHtml(header.getSubject()))
			.accumulate(MailConstants.ATTACHED, header.getAttached() == CommonService.YES ? true:false)
			.accumulate(MailConstants.SIZE, MailService.getByteSize(header.getSize()))
			.accumulate(MailConstants.READ, header.getRead() == CommonService.YES ?
											MailConstants.READ_YES:MailConstants.READ_NO)
			.accumulate(MailConstants.RECEIVED_DATE, header.getReceived() != null?header.getReceived().getTime():header.getCreated().getTime())
			.accumulate(MailConstants.SENT_DATE, header.getSent() != null?header.getSent().getTime():header.getCreated().getTime())
			.accumulate(MailConstants.FLAG, String.valueOf(header.getFlag()))
			.accumulate(MailConstants.TYPE, header.getType().toString())
			.accumulate(MailConstants.CONTENT_ID, header.getComposeID());

		if(isFetch)
			json.accumulate(MailConstants.FOLDER, header.getFolder().getId());


		return json;
	}

	/**
	 * convert to JSON from given mail header DTO
	 * @param header MailHeaderDTO
	 * @return JSON
	 */
	public static JSON convertMailHeaderDTO2JSon(MailHeaderDTO header){
		JSONObject json = new JSONObject();

		json.accumulate(MailConstants.CONTENT_ID, header.getContentId())
			.accumulate(MailConstants.SUBJECT, StringEscapeUtils.escapeHtml(StringService.hasLength(header.getSubject())?header.getSubject():StringService.EMPTY_STRING))
			.accumulate(MailConstants.FROM, (header.getFrom()!= null)?StringEscapeUtils.escapeHtml(AddressParser.toUnicodeString(header.getFrom())
											.replace("\"", StringService.EMPTY_STRING)):StringService.EMPTY_STRING)
			.accumulate(MailConstants.SENT_DATE,header.getSent()!= null?header.getSent().getTime():0L)
			.accumulate(MailConstants.TO, JSONService.toJSONArray(convertAddress(header.getTo())))
			.accumulate(MailConstants.CC, JSONService.toJSONArray(convertAddress(header.getCc())))
			.accumulate(MailConstants.BCC,JSONService.toJSONArray(convertAddress(header.getBcc())))
			.accumulate(MailConstants.ATTACHMENTS, JSONService.toJSONArray(convertAttachments(header)))
			.accumulate(MailConstants.BODY_TEXT,StringService.hasLength(header.getBodyText())?header.getBodyText():StringService.EMPTY_STRING );

		return json;
	}

	/**
	 * convert to JSON from given list of mail header
	 * @param headers List<MailHeader>
	 * @return JSON
	 */
	public static JSONObject convertListHeaders2JSON(SearchResultDTO<MailHeader> result){
		List<JSONObject> jmailHeaders = new ArrayList<JSONObject>();
		for(MailHeader item : result.getResultSet()){
			if(item != null)
				jmailHeaders.add(convertMailHeader2JSON(item, false));
		}

		JSONObject object= new JSONObject();
		object.accumulate(MailConstants.RESULT_KEY, result.getTotalResult())
		      .accumulate(MailConstants.ROWS_KEY, JSONService.toJSONArray(jmailHeaders));

		return object;
	}

	/**
	 * get JSON empty result
	 * @return JSONObject
	 */
	public static JSONObject getNone(){
		JSONObject object= new JSONObject();
		object.accumulate(MailConstants.RESULT_KEY, 0)
		      .accumulate(MailConstants.ROWS_KEY, JSONService.toJSONArray(new ArrayList<JSONObject>()));

		return object;

	}

	/**
	 * convert list of address to list of JSON
	 * @param adds DISPLAY_NAME
	 * @return List<JSONObject>
	 */
	private static List<JSONObject> convertAddress(List<Address> adds){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		if(adds != null){
			for(Address add : adds){
				if(add != null){
					JSONObject json = new JSONObject();
					json.accumulate(MailConstants.ADDRESS, StringService.hasLength(add.getAddress())?
							add.getAddress().replace("\"", StringService.EMPTY_STRING):StringService.EMPTY_STRING)
						.accumulate(MailConstants.DISPLAY_NAME, StringService.hasLength(add.getDisplayName())?
																		add.getDisplayName(): StringService.EMPTY_STRING);
					jsons.add(json);
				}
			}
		}
		return jsons;
	}

	/**
	 * convert attachment to JSON
	 * @param header MailHeaderDTO
	 * @return List<JSONObject>
	 */
	private static List<JSONObject> convertAttachments(MailHeaderDTO header){
		List<JSONObject> jsons = new ArrayList<JSONObject>();

		if(header.getAttachments() != null && header.getAttachments().size() >= 0){
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
