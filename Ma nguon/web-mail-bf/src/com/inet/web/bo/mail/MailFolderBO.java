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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FolderType;
import com.inet.mail.persistence.MailFolder;
import com.inet.web.bf.mail.MailFolderBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;

/**
 * MailFolderBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailFolderBO extends AbstractWebOSBO<MailFolderBF> {

	/**
	 * MailFolderBO constructor
	 * @param businessFacade
	 */
	protected MailFolderBO(MailFolderBF businessFacade) {
		super(businessFacade);
	}
	
	
	/**
	 * save mail folder
	 * @param folder MailFolder - the given mail folder.
	 * @return the list of mail folders.
	 * @throws WebOSBOException if an error occurs during save mail folder.
	 */
	public MailFolder save(MailFolder folder) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().save(folder);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while save mail folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * delete mail folder
	 * @param folderId Long - the given mail folder identifier.
	 * @throws WebOSBOException if an error occurs during delete mail folder.
	 */
	public void delete(long folderId) throws WebOSBOException{
		try{
			getBusinessFacade().getFacade().delete(folderId);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while delete mail folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * update mail folder
	 * @param folderId Long - the given mail folder identifier.
	 * @throws WebOSBOException if an error occurs during update mail folder.
	 */
	
	public MailFolder update(MailFolder folder) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().update(folder);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search mail folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * load mail folder
	 * @param folderId MailFolder
	 * @return MailFolder
	 * @throws WebOSBOException if an error occurs during load mail folder.
	 */
	public MailFolder load(long folderId) throws WebOSBOException{
		try{
			return  getBusinessFacade().getFacade().load(folderId);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while load mail folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * update mail folder
	 * @param folderId Long - the given mail folder identifier.
	 * @throws WebOSBOException if an error occurs during update mail folder.
	 */
	
	public MailFolder rename(long folderId, String newName) throws WebOSBOException{
			MailFolder folder = load(folderId);
			folder.setName(newName);
			return update(folder);
	}
	
	/**
	 * Find mail folder by folder type.
	 * 
	 * @param code String - the given user code.
	 * @param type FolderType - the given folder type.
	 * @return the list of mail folders.
	 * @throws WebOSBOException if an error occurs during finding mail folder.
	 */
	public List<MailFolder> findByType(FolderType type) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().findByType(type);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search mail folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	
	/**
	 * Find mail folder by parent identifier.
	 * 
	 * @param code String - the given user code.
	 * @param parentId long - the given parent identifier.
	 * @return the list of mail folders.
	 * @throws WebOSBOException if an error occurs during finding mail folder.
	 */
	public List<MailFolder> findByParentId(long parentId) throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().findByParentId(parentId);
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while find folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * Find mail folder by user code.
	 * 
	 * @param code String - the given user code.
	 * @return the list of mail folders.
	 * @throws WebOSBOException if an error occurs during finding mail folder.
	 */
	public List<MailFolder> findByUser() throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().findByUser();
		}catch(EJBException ejbEx){
			throw new WebOSBOException("error while search folder by user",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * @param userCode
	 * @return
	 * @throws WebOSBOException
	 */
	public long findFolderDefault(FolderType type) throws WebOSBOException{
		List<MailFolder> folders;
		try {
			folders = this.getBusinessFacade().getFacade().findByType(type);
			if(folders != null && folders.size()>0){
				return folders.get(0).getId();
			}
		} catch(EJBException ejbEx){
			throw new WebOSBOException("error while search default folder",ejbEx);
		}catch(Exception ex){
			throw translateException(ex);
		}
		
		return 0L;
	}
}
