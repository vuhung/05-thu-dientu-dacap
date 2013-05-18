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
import net.sf.json.JSONObject;

import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.mail.data.FolderType;
import com.inet.mail.persistence.MailFolder;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.bo.mail.MailFolderBO;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.mail.utils.Messages;
import com.inet.web.service.utils.WebCommonService;

/**
 * MailFolderService.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailFolderService extends AbstractWebOSService {
	//~ private constant 
	private static final String CSS_UNREAD = "mail-folder-unread";
	
	private static final String FOLER_CONTACT = "CONTACT";
	
	private static final String CSS_CONTACT = "mail-folder-contact";
	//~ Instance fields =======================================================
	/**
	 * the {@link MailBridgeBO} instance.
	 */
	private MailBridgeBO bridgeBO;
	/**
	 * the {@link MailFolderBO} instance.
	 */
	private MailFolderBO folderBO;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailFolderService</tt> instance from the given
	 * {@link AccountManager}, the {@link MailBridgeBO} instance and
	 * {@link MailFolderBO} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param bridgeBO the given {@link MailBridgeBO} instance.
	 * @param folderBO the given {@link MailFolderBO} instance.
	 */
	public MailFolderService(AccountManager<Account> accountManager, 
			MailBridgeBO bridgeBO,
			MailFolderBO folderBO){
		super(accountManager) ;
		
		this.bridgeBO = bridgeBO;
		this.folderBO = folderBO;
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		String action = getData(request, MailConstants.ACTION);
		// build mail folder
		if(MailConstants.ACTION_FOLDER_BUILD.equals(action)){
			return buildMailFolder();
		}// create new mail folder
		else if(MailConstants.ACTION_FOLDER_CREATE.equals(action)){
			return createNewFolder(request);
		}// delete mail folder 
		else if(MailConstants.ACTION_FOLDER_DELETE.equals(action)){
			return deleteFolder(request);
		}// rename mail folder
		else if(MailConstants.ACTION_FOLDER_RENAME.equals(action)){
			return renameFolder(request);
		}// move mail folder
		else if(MailConstants.ACTION_FOLDER_MOVE.equals(action)){
			//TODO
		}/*else if(MailConstants.ACTION_FOLDER_UNREAD.equals(action)){
			return countUnread(request);
		}*/else if(MailConstants.ACTION_FOLDER_DEFAULT.equals(action)){
			return findDefaultFolder(request);
		}
		return FAILURE_JSON;
	}
	
	/// BEGIN build mail folder tree 
	/**
	 * create folder default and build tree
	 * @return JSON
	 * @throws WebOSServiceException
	 */
	private JSON buildMailFolder() throws WebOSServiceException{
		// check the mail folder is exist.
		Map<Long, Long> mapUnread = bridgeBO.countUnread();
		List<MailFolder> folders = folderBO.findByUser();
		
		// does not build mail folder.
		if (folders == null || folders.size() == 0) {
			return FAILURE_JSON ;
		}
		return  buildTree(folders, mapUnread);
	}
	
	/**
	 * build tree from given owner
	 * @param owner user code
	 * @return JSONObject
	 */
	private JSON buildTree(List<MailFolder> folders, Map<Long, Long> mapUnread){
		// root
		List<JSON> jroot = new ArrayList<JSON>();
		
		Map<Long, List<Integer>> queue = new HashMap<Long, List<Integer>>();
		List<Integer> roots = new ArrayList<Integer>();

		for (int pos = 0; pos < folders.size(); pos++) {
			MailFolder folder = folders.get(pos);
			// existing parent in the list..
			// but we didn't find at this time
			if (folder.getParentId() > 0) {
				List<Integer> index = null;
				if (!queue.containsKey(folder.getParentId()))
					index = new ArrayList<Integer>();
				else
					index = queue.get(folder.getParentId());
				index.add(pos);

				queue.put(folder.getParentId(), index);
			} else
				roots.add(pos);

			// not in the queue as parent of some children
			// then check the condition to see this node will be
			// added into queue as parent without child
			if (!queue.containsKey(folder.getId()) && folder.getParentId() <= 0){
				queue.put(folder.getId(), new ArrayList<Integer>());
			}
		}

		// process tree result
		for (int index : roots) {
			MailFolder folder = folders.get(index);
			JSONObject child = getJSon(folder,mapUnread);
			buildRecusive(child, folder.getId(), folders, queue,mapUnread);
			jroot.add(child);
		}
		
		// add contact folder
		if(jroot != null && jroot.size() > 0){
			JSONObject json = new JSONObject();
			json.accumulate(MailConstants.FOLDER_ID,-2L)
			.accumulate(MailConstants.FOLDER_PARENT, -7L)
			.accumulate(MailConstants.FOLDER_TYPE, FOLER_CONTACT)
			.accumulate(MailConstants.FOLDER_UNREAD,0)
			.accumulate(MailConstants.FOLDER_NAME, Messages.getMessage(Messages.const_contact))
			.accumulate(MailConstants.FOLDER_LEAF, true)
			.accumulate(MailConstants.FOLDER_ICON_CSS, CSS_CONTACT)
			.accumulate(MailConstants.FOLDER_ORIGINAL, Messages.getMessage(Messages.const_contact));
			
			jroot.add(json);
		}
		
		return JSONService.toJSONArray(jroot);
	}
	
	/**
	 * buildRecusive
	 * @param jParent JSONObject
	 * @param parent long
	 * @param folders List<MailFolder>
	 * @param queue  HashMap<Long, List<Integer>>
	 */
	private void buildRecusive(JSONObject jParent, long parent, List<MailFolder> folders,
				Map<Long, List<Integer>> queue,Map<Long, Long> mapUnread){
		// some children maybe
		if (queue.containsKey(parent)) {
			List<JSONObject> jsons = new ArrayList<JSONObject>();
			for (int pos : queue.get(parent)) {
				MailFolder folder = folders.get(pos);
				
				JSONObject child = getJSon(folder,mapUnread);

				buildRecusive(child, folder.getId(), folders, queue,mapUnread);
				jsons.add(child);
			}
			if(jsons.size() > 0){
				jParent.accumulate(MailConstants.FOLDER_CHILDREN, JSONService.toJSONArray(jsons));
			}else{
				jParent.accumulate(MailConstants.FOLDER_LEAF, true);
			}
			return;
		}
		jParent.accumulate(MailConstants.FOLDER_LEAF, true);
	}
	
	/**
	 * get JSONOBJECT from given mail folder 
	 * @param folder
	 * @return JSONObject
	 */
	private JSONObject getJSon(MailFolder folder, Map<Long, Long> mapUnread){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.FOLDER_ID,folder.getId())
			.accumulate(MailConstants.FOLDER_PARENT, folder.getParentId())
			.accumulate(MailConstants.FOLDER_TYPE, folder.getType().toString())
			.accumulate(MailConstants.FOLDER_UNREAD,mapUnread.get(folder.getId())== null?0:mapUnread.get(folder.getId()));
		
		addFolderName(json, folder,mapUnread);
		return json;
	}
	
	/**
	 * convert folder name to CSS
	 * @param folder MailFolder
	 * @return String - CSS
	 */
	private void addFolderName(JSONObject json, MailFolder folder,Map<Long, Long> mapUnread){
		switch (folder.getType()) {
		case INBOX:
			addCSSFolder(json,"icon-email-inbox",Messages.getMessage(Messages.const_inbox), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L );
			break;
		case OUTBOX:
			addCSSFolder(json,"icon-email-outbox",Messages.getMessage(Messages.const_outbox), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		case SENT:
			addCSSFolder(json,"icon-email-sent",Messages.getMessage(Messages.const_sent), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		case DRAFT:
			addCSSFolder(json,"icon-email-draft",Messages.getMessage(Messages.const_draft), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		case TRASH:
			addCSSFolder(json,"icon-email-trash",Messages.getMessage(Messages.const_trash), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		case SPAM:
			addCSSFolder(json,"icon-email-spam",Messages.getMessage(Messages.const_spam),  mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		case CUSTOM:
			addCSSFolder(json,"icon-email-custom",folder.getName(), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		default:
			addCSSFolder(json,"icon-email-custom",folder.getName(), mapUnread.get(folder.getId())!= null?mapUnread.get(folder.getId()):0L);
			break;
		}
	}
	
	/**
	 * @param json
	 * @param css
	 * @param name
	 */
	private void addCSSFolder(JSONObject json, String css,String name, long unread){
		json.accumulate(MailConstants.FOLDER_ICON_CSS, css)			
			.accumulate(MailConstants.FOLDER_ORIGINAL, name);
		if(unread > 0){
			json.accumulate(MailConstants.FOLDER_NAME, name + "("+ String.valueOf(unread)  + ")");
			json.accumulate(MailConstants.FOLDER_CSS, CSS_UNREAD);
		}else{
			json.accumulate(MailConstants.FOLDER_NAME, name);
			json.accumulate(MailConstants.FOLDER_CSS, "");
		}
	}
	/// END build mail folder tree
	
	/**
	 * create new mail folder
	 * @param request HttpServletRequest
	 * @return JSON
	 * @throws WebOSServiceException if an error occurs during save mail folder
	 */
	private JSON createNewFolder(HttpServletRequest request) throws WebOSServiceException{
		String parentId = getData(request, MailConstants.FOLDER_PARENT);
		String name = getData(request, MailConstants.FOLDER_NAME);
		MailFolder folder = new MailFolder(WebCommonService.toLong(parentId),
											name,FolderType.CUSTOM,getCode());
		folder = folderBO.save(folder);
		
		JSONObject object = new JSONObject();
		object.accumulate(MailConstants.FOLDER_ID, folder.getId());
		return object;
	}
	
	/**
	 * delete mail folder
	 * @param request HttpServletRequest
	 * @return JSON
	 * @throws WebOSServiceException if an error occurs during delete mail folder
	 */
	private JSON deleteFolder(HttpServletRequest request) throws WebOSServiceException{
		String folderId = getData(request,MailConstants.FOLDER_ID);
		folderBO.delete(WebCommonService.toLong(folderId));
		return SUCCESS_JSON;
	}
	
	/**
	 * rename mail folder
	 * @param request HttpServletRequest
	 * @return JSON
	 * @throws WebOSServiceException if an error occurs during rename mail folder
	 */
	private JSON renameFolder(HttpServletRequest request) throws WebOSServiceException{
		// get data from request
		String id = getData(request,MailConstants.FOLDER_ID);
		String newName = getData(request,MailConstants.FOLDER_NAME);
		// rename folder
		folderBO.rename(WebCommonService.toLong(id), newName);
		return SUCCESS_JSON;
	}
	
	/**
	 * @param request
	 * @return
	 */
	private JSON findDefaultFolder(HttpServletRequest request){
		// get data
		long inboxId = folderBO.findFolderDefault(FolderType.INBOX);
		long trashId = folderBO.findFolderDefault(FolderType.TRASH);
		
		JSONObject object = new JSONObject();
		object.accumulate(MailConstants.FOLDER_INBOX, inboxId)
				.accumulate(MailConstants.FOLDER_TRASH, trashId);
		
		return object;
	}
}
