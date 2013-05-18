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
package com.inet.web.bo.mail;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.CommonService;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailFolderEntry;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.bf.mail.MailHeaderBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.exception.WebOSException;

public class MailHeaderBO extends AbstractWebOSBO<MailHeaderBF>{
	/**
	 * logger.
	 */
        protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * Constructor MailHeaderBO
	 * @param businessFacade MailHeaderBF
	 */
	protected MailHeaderBO(MailHeaderBF businessFacade) {
		super(businessFacade);
	}
	
	/**
	 * Find all mails in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail headers.
	 * @throws WebOSBOException if an error occurs during finding mail header.
	 */
	public List<MailHeader> findByFolder(long id, int startPos, int maxResult) 
			throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().findByFolder(id, startPos, maxResult);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search mail header by folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}

	/**
	 * @param id
	 * @param owner
	 * @param startPos
	 * @param maxResult
	 * @return
	 * @throws WebOSBOException
	 */
	public SearchResultDTO<MailHeader> search(long id, String owner, int startPos, int maxResult) throws WebOSBOException{
		try {
			if(id > 0L){
				MailHeader header = new MailHeader();
				MailFolder folder = new MailFolder();
				folder.setId(id);
				header.setFolder(folder);
				header.setOwner(owner);
				
				// create criteria.
				SearchResultDTO<MailHeader> search = new SearchResultDTO<MailHeader>(header,startPos,maxResult);
				
				return getBusinessFacade().getFacade().search(search);
			}else{
				return this.searchByFolderDefault(startPos, maxResult);
			}
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search mail header by folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 *  search mail header 
	 * @param condition
	 * @return
	 * @throws WebOSBOException
	 */
	public SearchResultDTO<MailHeader> search(SearchResultDTO<MailHeader> condition) throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().search(condition);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search mail header by folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * Search the given criteria.
	 * 
	 * @param criteria the given search criteria.
	 * @param startAt the given start position.
	 * @param maxItem the given max items to get.
	 * 
	 * @return the search result.
	 * @throws WebOSBOException when error occurs during searching data.
	 */
	public SearchResultDTO<MailHeader> search(String criteria, int startAt,int maxItem) throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().search(criteria, startAt, maxItem);
		} catch(EJBException ejbEx){
			ejbEx.printStackTrace();
			logger.error("ERROR while send email", ejbEx);
			throw new WebOSBOException("error while search mail header ",ejbEx);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("ERROR while send email", ex);
			throw translateException(ex);
		}
	}
	/**
	 * search mail header from given parameter
	 * @param owner String
	 * @param startPos Integer
	 * @param maxResult Integer
	 * @return search DTO
	 * @throws WebOSBOException if an error occurs during search.
	 */
	private SearchResultDTO<MailHeader> searchByFolderDefault(int startPos, int maxResult) throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().searchByFolderDefault(startPos, maxResult);
		}catch(EJBException ejbEx){
			logger.error("ERROR while search email", ejbEx);
			throw new WebOSBOException("error while search mail header by folder",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while search email", ex);
			throw translateException(ex);
		}
	}
	/**
	 * Find all read/unread mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param owner String - the given user code.
	 * @param read boolean - If <code>true</code> user want to view read mail, otherwise view unread mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws WebOSBOException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByRead(long id, boolean read, int startPos, int maxResult)
		throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().findByRead(id, read,startPos,maxResult);
		}catch(EJBException ejbEx){
			logger.error("ERROR while find by read", ejbEx);
			throw new WebOSBOException("error while search mail header by read",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while find by read", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * Find all attached/unattached mails header in the current folder.
	 * 
	 * @param id long - the given folder identifier.
	 * @param attached boolean - If <code>true</code> user want to view attached mail, otherwise 
	 * view unattached mail.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * @return the list of mail header.
	 * @throws WebOSBOException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByAttached(long id, boolean attached, int startPos, int maxResult)	
		throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().findByAttached(id,attached,startPos,maxResult);
		}catch(EJBException ejbEx){
			logger.error("ERROR while find by attached", ejbEx);
			throw new WebOSBOException("error while search mail header by attached",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while find by attached", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * Finds all mails from the given priority.
	 * 
	 * @param id long - the given folder identifier.
	 * @param priority MailPriority - the given mail priority.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 	 
	 * @return the list of mail header.
	 * @throws WebOSBOException if an error occurs during finding mail header.
	 */
	public SearchResultDTO<MailHeader> findByPriority(long id, MailPriority priority, int startPos, int maxResult)
		throws WebOSBOException{
			try {
				return getBusinessFacade().getFacade().findByPriority(id, priority,startPos,maxResult);
			}catch(EJBException ejbEx){
				logger.error("ERROR find by priority", ejbEx);
				throw new WebOSBOException("error while search mail header by priority",ejbEx);
			}catch(Exception ex){
				logger.error("ERROR find by priority", ex);
				throw translateException(ex);
			}
	}
	
	/**
	 * Finds all mails from the given mail flag.
	 * 
	 * @param id long - the given folder identifier.
	 * @param flag MailFlag - the given mail flag.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given end position.
	 * 
	 * @return the list of mail header.
	 * @throws WebOSBOException if an error occurs during finding mail header
	 */
	public SearchResultDTO<MailHeader> findByFlag(long id, MailFlag flag, int startPos, int maxResult) 
		throws WebOSBOException{
		try {
			return getBusinessFacade().getFacade().findByFlag(id, flag,startPos,maxResult);
		}catch(EJBException ejbEx){
			logger.error("ERROR while find by flag", ejbEx);
			throw new WebOSBOException("error while search mail header by priority",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while find by flag", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * Create mail folder.
	 * 
	 * @param owner String - the given user who own the tree.
	 * @return the root folder.
	 * @throws WebOSBOException if an error occurs during creating mail folder.
	 */
	public MailFolderEntry createMailFolder(String owner) throws WebOSBOException{
		try{
			return null;
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	
	
	
	/**
	 * mark mail is read/ unread 
	 * @param headerId - Long mail header id
	 * @param read - char Y/N
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	private void markRead(long headerId,boolean read) throws WebOSBOException{
		MailHeader header =  this.load(headerId);
		header.setRead(read?CommonService.YES:CommonService.NO);
		update(header);
	}
	
	/**
	 * mark mail is read/ unread 
	 * @param headerId - Long mail header id
	 * @param read - char Y/N
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	public void markRead(List<Long> listId,boolean read) throws WebOSBOException{
		for(Long id :listId){
			this.markRead(id, read);
		}
	}
	
	/**
	 * mark mail flag
	 * @param headerId - Long mail header id
	 * @param flag - MailFlag
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	private void markFlag(long headerId,MailFlag flag){
		MailHeader header =  this.load(headerId);
		header.setFlag(flag);
		this.update(header);
	}
	
	/**
	 * mark mail flag
	 * @param headerId - Long mail header id
	 * @param flag - MailFlag
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	public void markFlag(List<Long> listId,MailFlag flag) throws WebOSBOException{
		for(Long id : listId){
			this.markFlag(id, flag);
		}
	}
	/**
	 * mark mail priority   
	 * @param headerId - Long mail header id
	 * @param priority - MailPriority
	 * @return
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	private void markPriority(long headerId,MailPriority priority) throws WebOSBOException{
			MailHeader header =  this.load(headerId);
			header.setPriority(priority);
			this.update(header);
	}
	
	/**
	 * mark mail priority   
	 * @param headerId - Long mail header id
	 * @param priority - MailPriority
	 * @return
	 * @throws WebOSBOException if an error occurs during update mail header. 
	 */
	public void markPriority(List<Long> listId,MailPriority priority) throws WebOSBOException{
		for(Long id : listId){
			this.markPriority(id,priority);
		}
	}
	
	/**
	 * update mail header
	 * @param header MailHeader
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header.
	 */
	public MailHeader update(MailHeader header) throws WebOSBOException{
		try{
			return  getBusinessFacade().getFacade().update(header);
		}catch(EJBException ejbEx){
			logger.error("ERROR while update email", ejbEx);
			throw new WebOSBOException("error while mark priority for email",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while update email", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * update mail header
	 * @param header MailHeader
	 * @return MailHeader
	 * @throws WebOSBOException if an error occurs during update mail header.
	 */
	public MailHeader load(long headerId) throws WebOSBOException{
		try{
			return  getBusinessFacade().getFacade().load(headerId);
		}catch(EJBException ejbEx){
			logger.error("ERROR while load mail", ejbEx);
			throw new WebOSBOException("error while mark priority for email",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while load mail", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * delete mail header
	 * @param headerId Long
	 * @throws WebOSBOException if an error occurs during delete mail header
	 */
	private void delete(long headerId) throws WebOSBOException{
		try{
			getBusinessFacade().getFacade().delete(headerId);
		}catch(EJBException ejbEx){
			logger.error("ERROR while delete email", ejbEx);
			throw new WebOSBOException("error while delete email",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while delete email", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * delete mail header
	 * @param headerId Long
	 * @throws WebOSBOException if an error occurs during delete mail header
	 */
	public void delete(List<Long> listId) throws WebOSBOException{
		for(Long item : listId){
			delete(item);
		}							
	}
	
	/**
	 * 
	 * @return
	 * @throws WebOSBOException
	 */
	public Map<String, String> getDefaultConfigure() throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().getDefaultConfigure();
		}catch(EJBException ejbEx){
			logger.error("ERROR while get default configure", ejbEx);
			throw new WebOSBOException("error while load default configure",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while get default configure", ex);
			throw translateException(ex);
		}
	}
	
	/**
	 * @param headerId
	 * @param folderId
	 * @return
	 * @throws WebOSBOException
	 */
	private void move(long headerId, long folderId) throws WebOSBOException{
		try{
			getBusinessFacade().getFacade().move(headerId, folderId);
		}catch(EJBException ejbEx){
			logger.error("ERROR while move email", ejbEx);
			throw new WebOSBOException("error while move header to folde",ejbEx);
		}catch(Exception ex){
			logger.error("ERROR while move email", ex);
			throw translateException(ex);
		}

	}
	
	/**
	 * @param headerId
	 * @param folderId
	 * @return
	 * @throws WebOSBOException
	 */
	public void move(List<Long> listId, long folderId) throws WebOSBOException{
		for(Long id : listId)
			this.move(id, folderId);
	}
	
	/**
	 * mark as read from the given folder identifier.
	 * @param folderId the given folder identifier.
	 * @throws WebOSException when error occurs during mark as read folder
	 */
	public void markAsReadFolder(long folderId) throws WebOSException{
	  try{
	    this.getBusinessFacade().getFacade().markAsReadFolder(folderId);
	  }catch(EJBException ejbEx){
	    logger.error("ERROR while mark as read folder", ejbEx);
	    throw new WebOSBOException("error while mark as read folder",ejbEx);
	  }catch(Exception ex){
	    logger.error("ERROR while mark as read folder", ex);
            throw translateException(ex);
	  }
	}
	
	/**
         * Get total current using from account
         * @param owner the given user code  
         * @param account the given account SMTP default
         * @return 
         * @throws WebOSException when error occurs during count current using space
         */
        public long getCurrentUsing(String owner, String account) throws WebOSException{
          try{
            return this.getBusinessFacade().getFacade().getCurrentUsing(owner, account);
          }catch(EJBException ejbEx){
            logger.error("ERROR while count current using space", ejbEx);
            throw new WebOSBOException("error count current using space",ejbEx);
          }catch(Exception ex){
            logger.error("ERROR while count current using space", ex);
            throw translateException(ex);
          }
        }
}