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
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.DateService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.persistence.FollowUp;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.bo.mail.FollowUpBO;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.bo.mail.MailHeaderBO;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.utils.WebCommonService;

/**
 * FollowUpService.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class FollowUpService extends AbstractWebOSService{
	/**
	 * logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(FollowUpService.class) ;
	
	private FollowUpBO followUpBO;
	
	private MailBridgeBO bridgeBO;
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>FollowUpService</tt> instance from the given
	 * {@link AccountManager} instance, {@link MailBridgeBO} instance
	 * and {@link MailHeaderBO} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param FollowUpBO the given {@link FollowUpBO} instance.
	 */
	public FollowUpService(AccountManager<Account> accountManager, 
			FollowUpBO followUpBO,
			MailBridgeBO bridgeBO){
		super(accountManager) ;
		this.followUpBO = followUpBO;
		this.bridgeBO = bridgeBO;
	}
	
	//~ Methods ===============================================================	
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		String action = getData(request,MailConstants.ACTION);
		if(MailConstants.ACTION_LOAD.equals(action)){
			return loadAll();
		}else if(MailConstants.ACTION_FOLLOWUP_ADD.equals(action)){
			return save(request);
		}else if(MailConstants.ACTION_UPDATE.equals(action)){
			return update(request);
		}else if(MailConstants.ACTION_DELETE.equals(action)){
			return delete(request);
		}
		return FAILURE_JSON;
	}

	/**
	 * load all follow up
	 * @return JSON
	 */
	private JSON loadAll(){
		try{
			List<FollowUpDTO> followups = this.followUpBO.loadAll();
			return convertFollowUps2Json(followups);
		}catch(WebOSBOException webEx){
			logger.error(webEx.getMessage(), webEx);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		
		return FAILURE_JSON;
	}
	
	/**
	 * save follow up
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON save(HttpServletRequest request){
		JSONArray array = WebCommonService.toJSONArray(request, MailConstants.OBJECT);
		
		List<FollowUp> followups = new ArrayList<FollowUp>();
		for(int index = 0; index < array.size(); index ++){
			JSONObject object = array.getJSONObject(index);
			// get follow up data.
			String name = WebCommonService.toString(object,MailConstants.FOLLOWUP_NAME);
			long data = WebCommonService.toLong(object, MailConstants.FOLLOWUP_MAIL);
			Date date = WebCommonService.toDate(object, MailConstants.FOLLOWUP_DATE, null);
			
			// create follow up data.
			FollowUp followUp = new FollowUp() ;
			MailHeader header = new MailHeader();
			header.setId(data);
			followUp.setMail(header);
			followUp.setDate(date);
			followUp.setName(name);	
			
			followups.add(followUp);
		}
		
		return convertListFollowUpDTO2Json(bridgeBO.saveFollowUp(followups));
	}
	
	/**
	 * update follow up
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON update(HttpServletRequest request){
		List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.FOLLOWUP_IDS));
		
		Date date = WebCommonService.toDate(request, MailConstants.FOLLOWUP_DATE,DateService.VIETNAM_DATE_PATTERN, null);
		
		return convertListFollowUpDTO2Json(bridgeBO.updateFollowUp(listId, date));
	}
	
	/**
	 * delete follow up
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON delete(HttpServletRequest request){
		List<Long> ids = WebCommonService.toArray(getData(request, MailConstants.FOLLOWUP_IDS));
		
		followUpBO.delete(ids);
		return SUCCESS_JSON;
	}
	
	/**
	 * convert list of Follow up to JSON
	 * @param followups List<FollowUp>
	 * @return JSON
	 */
	private JSON convertFollowUps2Json(List<FollowUpDTO> followups){
		List<JSONObject> objects = new ArrayList<JSONObject>();
		for(FollowUpDTO item : followups){
			objects.add(convertFollowUpDTO2Json(item));
		}
		
		JSONObject object= new JSONObject();
		object.accumulate(MailConstants.RESULT_KEY, followups == null ? 0:followups.size())
	      	.accumulate(MailConstants.ROWS_KEY, JSONService.toJSONArray(objects));
		
		return object;
	}
	
	/**
	 * convert list of Follow up to JSONArray
	 * @param followups List<FollowUp>
	 * @return JSON
	 */
	private JSON convertListFollowUpDTO2Json(List<FollowUpDTO> followups){
		List<JSONObject> objects = new ArrayList<JSONObject>();
		for(FollowUpDTO item : followups){
			objects.add(convertFollowUpDTO2Json(item));
		}
		
		return JSONService.toJSONArray(objects);
	}
	
	/**
	 * convert follow up DTO to JSONObject
	 * @param followup FollowUpDTO
	 * @return JSONObject
	 */
	private JSONObject convertFollowUpDTO2Json(FollowUpDTO followup){
		JSONObject object = new JSONObject();
		object.accumulate(MailConstants.FOLLOWUP_ID, followup.getId())
			  .accumulate(MailConstants.FOLLOWUP_NAME, followup.getName())
			  .accumulate(MailConstants.FOLLOWUP_DATE, followup.getDate() == null ? 0:followup.getDate().getTime())
			  .accumulate(MailConstants.FOLLOWUP_MAIL, followup.getData())
			  .accumulate(MailConstants.FLAG, followup.getFlag().toString());

		return object;
	}
}
