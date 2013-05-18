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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.data.FilterObject;
import com.inet.mail.data.FilterOperator;
import com.inet.mail.data.MailClauseOperator;
import com.inet.mail.data.MailFilterClause;
import com.inet.mail.data.MailFilterClauseUnit;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailFolder;
import com.inet.web.bo.mail.MailFilterBO;
import com.inet.web.bo.mail.MailFolderBO;
import com.inet.web.common.json.JSONService;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.mail.utils.Messages;
import com.inet.web.service.utils.WebCommonService;

public class MailFilterService extends AbstractWebOSService {
	//~ Instance fields =======================================================
	/**
	 * {@link MailFilterBO} instance.
	 */
	private MailFilterBO filterBO;	
	/**
	 * {@link MailFolderBO} instance.
	 */
	private MailFolderBO folderBO;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailFilterService</tt> instance from the given
	 * {@link MailFilterBO} instance and {@link MailFolderBO} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param filterBO the given {@link MailFilterBO} instance.
	 * @param folderBO the given {@link MailFolderBO} instance.
	 */
	public MailFilterService(AccountManager<Account> accountManager, 
			MailFilterBO filterBO, 
			MailFolderBO folderBO){
		super(accountManager) ;
		
		this.filterBO = filterBO;
		this.folderBO = folderBO;
	}
	
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		String action = getData(request, MailConstants.ACTION);
		if(MailConstants.ACTION_SAVE.equals(action)){
			return save(request);
		}else if(MailConstants.ACTION_DELETE.equals(action)){
			return deleteFilter(request);	
		}else if(MailConstants.ACTION_LOAD.equals(action)){
			return loadMailFilrer(request); 	
		}else if(MailConstants.ACTION_SEARCH.equals(action)){
			return searchByUser();
		}
		return FAILURE_JSON;
	}
	
	/**
	 * search mail filter by user
	 * @return JSON
	 */
	private JSONObject searchByUser(){
		JSONObject result= new JSONObject();
		// get mail filter 
		List<MailFilter> filters = filterBO.findByUser();
		
		// initialize mail filter JSON list
		List<JSONObject> jsonFilters = new ArrayList<JSONObject>();
		
		for(MailFilter filter: filters){
			JSONObject object = convertMailFilter2Json(filter);

			// add object to list
			jsonFilters.add(object);
		}
		
		result.accumulate(MailConstants.RESULT_KEY, (filters != null)? filters.size(): 0)
				.accumulate(MailConstants.ROWS_KEY, JSONService.toJSONArray(jsonFilters));
		
		return result;
	}
	
	/**
	 * load mail filter 
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON loadMailFilrer(HttpServletRequest request){
		long id = WebCommonService.toLong(getData(request,MailConstants.FILTER_ID));
		MailFilter mailFilter = filterBO.load(id);
		if(mailFilter != null)
			return convertMailFilter2Json(mailFilter);
		return FAILURE_JSON;
	}
	
	/**
	 * convert mail filter to JSON
	 * @param filter MailFilter
	 * @return JSON
	 */
	private JSONObject convertMailFilter2Json(MailFilter filter){
		JSONObject object = new JSONObject();
		object.accumulate(MailConstants.FILTER_ID, filter.getId())
				.accumulate(MailConstants.FILTER_NAME, filter.getName())
				.accumulate(MailConstants.FILTER_FOLDER, filter.getFolder()!= null?
						getFolderName(filter.getFolder()): StringService.EMPTY_STRING)
				.accumulate(MailConstants.FILTER_FOLDER_ID, filter.getFolder()!= null?
						filter.getFolder().getId():0L)		
				.accumulate(MailConstants.FILTER_MAIL_CLAUSE, convertMailFilterClause2Json(filter.getClause()));			
		return object;
	}
	
	/**
	 * convert mail filter clause to JSON
	 * @param clause MailFilterClause
	 * @return JSON
	 */
	private JSONObject convertMailFilterClause2Json(MailFilterClause clause){
		JSONObject object = new JSONObject();
		object.accumulate(MailConstants.FILTER_MAIL_OPERATOR, clause.getOperator().toString())
				.accumulate(MailConstants.FILTER_MAIL_CLAUSE_UNIT,
						convertMailFilterClauseUnit2Json(clause.getFilterClauses()));
		return object;
	}
	
	/**
	 * convert mail filter clause unit to JSON
	 * @param clausese
	 * @return
	 */
	private JSON convertMailFilterClauseUnit2Json(List<MailFilterClauseUnit> clausese){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		for(MailFilterClauseUnit unit : clausese){
			JSONObject object = new JSONObject();
			object.accumulate(MailConstants.FILTER_OBJECT, unit.getObject().toString())
				.accumulate(MailConstants.FILTER_OPERATOR, unit.getOperator().toString())
				.accumulate(MailConstants.FILTER_DATA, unit.getData());
			jsons.add(object);
		}
		return JSONService.toJSONArray(jsons);
	}
	
	/**
	 * save/update mail filter
	 * @param request
	 * @return
	 */
	private JSON save(HttpServletRequest request){
		JSONObject object = WebCommonService.toJSONObject(request, MailConstants.OBJECT) ;
		MailFilter filter = new MailFilter();
		long id = WebCommonService.toLong(object, MailConstants.FILTER_ID,0L);
		if(id > 0L)
			filter.setId(id);
		
		filter.setName(WebCommonService.toString(object, MailConstants.FILTER_NAME));
		filter.setClause(convertJSon2MailFilterClause(
							WebCommonService.toObject(object, MailConstants.FILTER_MAIL_CLAUSE)));
		long folderId = WebCommonService.toLong(object, MailConstants.FILTER_FOLDER_ID,0L);
		
		// check mail folder
		if(folderId> 0){
			MailFolder folder = folderBO.load(folderId);
			filter.setFolder(folder);
		}
		
		// check save/update
		if(id > 0L){
			filter = filterBO.update(filter);
		}else{
			filter = filterBO.save(filter);
		}
		return convertMailFilter2Json(filter);
	}
	
	/**
	 * delete filter
	 * @param request
	 * @return JSON
	 */
	private JSON deleteFilter(HttpServletRequest request){
		long id = WebCommonService.toLong(getData(request, MailConstants.FILTER_ID));
		try{
			filterBO.delete(id);
		}catch(WebOSException wEx){
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	/**
	 * convert JSONObject to mail filter clause
	 * @param object JSONObject
	 * @return MailFilterClause
	 */
	private MailFilterClause convertJSon2MailFilterClause(JSONObject object){
		MailFilterClause clause = new MailFilterClause();
		clause.setOperator(MailClauseOperator.valueOf(WebCommonService.toString(object, MailConstants.FILTER_MAIL_OPERATOR)));
		
		convertJson2MailFilterClauseUnits(clause,object);
		return clause;
	}
	
	/**
	 * convert JSONObject to list of MAilFilerClauseUnits
	 * @param object JSONObject
	 * @param clause MailFilterClause
	 * @return List<MailFilterClauseUnit>
	 */
	private void convertJson2MailFilterClauseUnits(MailFilterClause clause,JSONObject object){
		JSONArray array = WebCommonService.toArray(object, MailConstants.FILTER_MAIL_CLAUSE_UNIT);
		if(array != null && array.size() > 0){
			for(int index = 0; index < array.size(); index++){
				JSONObject jsonUnit = array.getJSONObject(index);
				FilterObject filterObject = FilterObject.valueOf(jsonUnit.getString(MailConstants.FILTER_OBJECT));
				FilterOperator filterOperator = FilterOperator.valueOf(jsonUnit.getString(MailConstants.FILTER_OPERATOR));
				String data = jsonUnit.getString(MailConstants.FILTER_DATA);
				// add to list clause unit
				clause.addFilterClauses(new MailFilterClauseUnit(filterObject,filterOperator,data));
			}
		}
	}
	
	/**
	 * get folder name
	 * @param folder the given folder name
	 * @return String
	 */
	private String getFolderName(MailFolder folder){
		switch (folder.getType()) {
		case INBOX:
			return Messages.getMessage(Messages.const_inbox);
		case OUTBOX:
			return Messages.getMessage(Messages.const_outbox);
		case SENT:
			return Messages.getMessage(Messages.const_sent);
		case DRAFT:
			return Messages.getMessage(Messages.const_draft);
		case TRASH:
			return Messages.getMessage(Messages.const_trash);
		case SPAM:
			return Messages.getMessage(Messages.const_spam);
		default:
			return folder.getName();
		}
	}
}
