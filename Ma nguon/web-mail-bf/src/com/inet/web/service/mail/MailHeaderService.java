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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

import com.inet.base.service.DateService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.bo.mail.MailHeaderBO;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.mail.utils.MailHeaderHelper;
import com.inet.web.service.utils.WebCommonService;

/**
 * MailHeaderService.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailHeaderService extends AbstractWebOSService {
	//~ Instance fields =======================================================
	/**
	 * {@link MailHeaderBO} instance.
	 */
	private MailHeaderBO headerBO;
	/**
	 * {@link MailBridgeBO} instance.
	 */
	private MailBridgeBO bridgeBO;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailHeaderService</tt> instance from the given
	 * {@link AccountManager} instance, {@link MailHeaderBO} instance
	 * and {@link MailBridgeBO} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param headerBO the given {@link MailHeaderBO} instance.
	 * @param bridgeBO the given {@link MailBridgeBO} instance.
	 */
	public MailHeaderService(AccountManager<Account> accountManager, 
			MailHeaderBO headerBO, 
			MailBridgeBO bridgeBO){
		super(accountManager) ;
		
		this.headerBO = headerBO;
		this.bridgeBO = bridgeBO;
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		try{
			String action = getData(request, MailConstants.ACTION);
			if(MailConstants.ACTION_SEARCH.equals(action)){
				return searchMailHeader(request);
			}else if(MailConstants.ACTION_HEADER_DELETE.equals(action)){
				return deleteMailHeader(request);
			}else if(MailConstants.ACTION_HEADER_MOVE.equals(action)){
				return moveMailHeader(request);
			}else if(MailConstants.ACTION_MARK_FLAG.equals(action)){
				return markFlag(request);
			}else if(MailConstants.ACTION_MARK_PRIORITY.equals(action)){
				return markPriority(request);
			}else if(MailConstants.ACTION_MARK_READ.equals(action)){
				return markRead(request);
			}else if(MailConstants.ACTION_EMPTY_FOLDER.equals(action)){
				return emptyFolder(request);
			}else if(MailConstants.ACTION_MARK_AS_READ_FOLDER.equals(action)){
			        return markAsReadFolder(request);
			}
			
		}catch(WebOSServiceException ex){
		}catch(Exception ex){}
		
		return FAILURE_JSON;
	}
	
	/**
	 * search MailHeader 
	 * @param request HttpServletRequest
	 * @return JSON
	 * @throws WebOSServiceException if an error occurs during while search mail header
	 */
	private JSON searchMailHeader(HttpServletRequest request) throws WebOSServiceException{
		
		String mode = getData(request,MailConstants.MODE);
		String start_para = getData(request, MailConstants.START_PARAM);
		String limit_para = getData(request, MailConstants.LIMIT_PARAM);
		int start = Integer.parseInt(start_para);
		int limit = Integer.parseInt(limit_para);	
		
		String field = getData(request, MailConstants.FIELD);
		String key = getData(request, MailConstants.KEYWORD);
		
		SearchResultDTO<MailHeader>  result = null ;
		try{
			if(MailConstants.MODE_BASIC.equals(mode)){
				// get parameter
				String folderId_para = getData(request, MailConstants.FOLDER_ID);
				
				// get object
				long folderId = WebCommonService.toLong(folderId_para);
				
				if(MailConstants.FIELD_ALL.equals(field)){
					result = headerBO.search(folderId,	getCode(),start, limit);
				}else if(MailConstants.READ.equals(field)){
					result = headerBO.findByRead(folderId, MailConstants.READ_YES.equals(key)?true:false,start, limit);
				}else if(MailConstants.MAIL_PRIORITY.equals(field)){
					result = headerBO.findByPriority(folderId, MailPriority.valueOf(key),start, limit);
				}else if(MailConstants.FLAG.equals(field)){
					result = headerBO.findByFlag(folderId, MailFlag.valueOf(key),start, limit);
				}else if(MailConstants.ATTACHED.equals(field)){
					result = headerBO.findByAttached(folderId, MailConstants.READ_YES.equals(key)?true:false,start, limit);
				}
			}else if(MailConstants.MODE_ADVANCED.equals(mode)){
				
				if(MailConstants.FIELD_ALL.equals(field)){
					result = headerBO.search(key, start, limit);				
				}else{
					MailHeader condition = new MailHeader();
					condition.setOwner(getCode());
					if(MailConstants.FIELD_DATE.equals(field)){
						Date date = null;
						date = DateService.getDate(key, DateService.VIETNAM_DATE_PATTERN);
										
						if(date == null){
							return MailHeaderHelper.getNone();
						}
						condition.setSent(date);	
					}else if(MailConstants.FIELD_SUBJECT.equals(field)){
						condition.setSubject(key);					
					}else if(MailConstants.FIELD_SENDER.equals(field)){
						condition.setSender(key);						
					}
					
					result = new SearchResultDTO<MailHeader>(condition,start,limit);
					result = headerBO.search(result);
				}
				 
			}
			
			if(result != null){
				return MailHeaderHelper.convertListHeaders2JSON(result);
			}
		}catch(WebOSBOException wexp){
			return MailHeaderHelper.getNone();
		}catch(Exception wexp){
			return MailHeaderHelper.getNone();
		}
		
		return MailHeaderHelper.getNone();
	}
	
	/**
	 * @param request
	 * @return
	 * @throws WebOSServiceException
	 */
	private JSON deleteMailHeader(HttpServletRequest request){
		List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.HEADER_LIST_ID));
		try{
			this.bridgeBO.delete(listId);
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * @param request
	 * @return
	 */
	private JSON moveMailHeader(HttpServletRequest request){
		List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.HEADER_LIST_ID));
		String folderId = getData(request, MailConstants.FOLDER_ID);
		try{
			headerBO.move(listId,WebCommonService.toLong(folderId));
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * mark flag for mail
	 * @param request HttpServletRequest
	 * @return JSON
	 * @throws WebOSServiceException if a error during mark flag for mail
	 */
	private JSON markFlag(HttpServletRequest request){
		try{
			List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.HEADER_LIST_ID));
			String flag = getData(request, MailConstants.FLAG);
			headerBO.markFlag(listId, MailFlag.valueOf(flag));
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * @param request
	 * @return
	 * @throws WebOSServiceException
	 */
	private JSON markPriority(HttpServletRequest request) throws WebOSServiceException{
		try{
			List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.HEADER_LIST_ID));
			String priority = getData(request, MailConstants.MAIL_PRIORITY);
			headerBO.markPriority(listId, MailPriority.valueOf(priority));
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * @param request
	 * @return
	 * @throws WebOSServiceException
	 */
	private JSON markRead(HttpServletRequest request) throws WebOSServiceException{
		try{
			List<Long> listId = WebCommonService.toArray(getData(request, MailConstants.HEADER_LIST_ID));
			String read = getData(request, MailConstants.READ);
			//check parameter
			headerBO.markRead(listId,MailConstants.READ_YES.equals(read)?true:false);
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * empty folder 
	 * @param request HttpServletRequest
	 * @return JSON
	 */ 
	private JSON emptyFolder(HttpServletRequest request){
		try{
			long folderId = WebCommonService.toLong(request, MailConstants.FOLDER_ID, 0L);
			bridgeBO.emptyFolder(folderId);
		}catch(WebOSServiceException ex){
			return FAILURE_JSON;
		}catch(Exception ex){
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * mark as read folder
	 * @param request HttpServletRequest
	 * @return JSON
	 */
	private JSON markAsReadFolder(HttpServletRequest request){
	  try{
            long folderId = WebCommonService.toLong(request, MailConstants.FOLDER_ID, 0L);
            this.headerBO.markAsReadFolder(folderId);
          }catch(WebOSServiceException ex){
            return FAILURE_JSON;
          }catch(Exception ex){
            return FAILURE_JSON;
          }
          
          return SUCCESS_JSON;
	}
}
