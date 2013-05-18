/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
package com.inet.mail.business.sr;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import org.hibernate.Criteria;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.data.FolderType;
import com.inet.mail.persistence.MailFolder;

/**
 * MailFolderSLBean
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date Jan 25, 2008
 * 
 * <pre>
 *  Initialization MailFolderSLBean class.
 * </pre>
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class MailFolderSLBean extends BaseMailBusinessBean<MailFolder>
		implements MailFolderSL, MailFolderRemoteSL {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderBase#findByParentId(java.lang.String,long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByParentId(String code, long parentId)
			throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailFolder.findByParentId");

		// set parameter.
		query.setParameter("owner", code);
		query.setParameter("parentId", parentId);

		// return list of mail folders.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderRemoteSL#findByParentId(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByParentId(long parentId) throws EJBException {
		return findByParentId(getUserCode(), parentId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderBase#findByType(java.lang.String, com.inet.mail.data.FolderType)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByType(String code, FolderType type)
			throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailFolder.findByType");

		// set parameter.
		query.setParameter("owner", code);
		query.setParameter("type", type);

		// return list of mail folders.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderRemoteSL#findByType(com.inet.mail.data.FolderType)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByType(FolderType type) throws EJBException {
		return findByType(getUserCode(), type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderBase#findByUser(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByUser(String code) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailFolder.findByUser");

		// set parameter.
		query.setParameter("owner", code);

		// return list of mail folders.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderRemoteSL#findByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFolder> findByUser() throws EJBException {
		return findByUser(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderBase#findRootByUser(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public MailFolder findRootByUser(String code) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailFolder.findRootByAndUser");

		// set parameter.
		query.setParameter("owner", code);

		// load data.
		List<MailFolder> data = load(query);

		// return root folder.
		return (data == null || data.size() == 0 ? null : data.get(0));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderRemoteSL#findRootByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolder findRootByUser() throws EJBException {
		return findRootByUser(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderBase#findById(java.lang.String,long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public MailFolder findById(String code, long id) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailFolder.findById");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", code);

		// load data.
		List<MailFolder> data = load(query);

		// return root folder.
		return (data == null || data.size() == 0 ? null : data.get(0));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderRemoteSL#findById(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolder findById(long id) throws EJBException {
		return findById(getUserCode(), id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusiness#delete(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		this.remove(bizId, MailFolder.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusiness#load(long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public MailFolder load(long bizId) throws EJBException {
		return this.load(bizId, MailFolder.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	protected Criteria buildQuery(MailFolder search) throws EJBException {
		throw new EJBException("This function has not been implemented yet.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderSL#countDefaultFolder(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public boolean countDefaultFolder(String author) {
		// create query.
		Long folderDefault = (Long) this.getEntityManager()
										.createNamedQuery("MailFolder.countDefaultByUser")
										.setParameter("owner", author)
										.getSingleResult();
		
		if (folderDefault == null) return false;
		return (folderDefault.longValue() > 0L);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderSL#getDefaultFolder(java.lang.String, long)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public MailFolder getDefaultFolder(String author, long index) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailFolder.queryDefaultFolder") ;
		
		// set query parameter.
		query.setParameter("owner", author) ;
		query.setParameter("parentId", index) ;
		
		// get result.
		List<MailFolder> result = load(query) ;
		return (result != null && result.size() == 1) ? result.get(0) : null ;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailFolderSL#queryDefaultFolder(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<MailFolder> queryDefaultFolder(String author)
			throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailFolder.queryDefaultByUser") ;
		
		// set query parameter.
		query.setParameter("owner", author) ;
		
		// execute query and return data to user.
		return load(query) ;
	}
}
