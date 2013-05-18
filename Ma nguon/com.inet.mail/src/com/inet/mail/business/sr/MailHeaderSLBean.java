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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.CommonService;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.base.service.UUIDGenerator;
import com.inet.grid.business.GridFacadeSL;
import com.inet.grid.core.Application;
import com.inet.grid.data.ContentDTO;
import com.inet.grid.data.DataFormat;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.conf.beans.ConfigurationBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.FolderCountDTO;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailConstant;
import com.inet.mail.data.MailFlag;
import com.inet.mail.data.MailFolderEntry;
import com.inet.mail.data.MailPriority;
import com.inet.mail.data.MailType;
import com.inet.mail.data.SearchResultDTO;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;
import com.inet.search.metadata.EmailHeaderDTO;
import com.inet.web.context.WebOSApplicationContext;

/**
 * MailHeaderSLBean
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 28, 2008
 * 
 *       <pre>
 *  Initialization MailHeaderSLBean class.
 * </pre>
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public class MailHeaderSLBean extends BaseMailBusinessBean<MailHeader>
		implements MailHeaderSL, MailHeaderRemoteSL {
	//~ Instance fields =======================================================
	// define dependency bean.
	@EJB(name = "MailFolderSLBean")
	private MailFolderSL folderSL; // mail folder state-less bean.
	
	@EJB(name = "MailFilterSLBean")
	private MailFilterSL filterSL; // mail filter state-less bean.
	
	@EJB(name = "MailErrorSLBean")
	private MailErrorSL mailErrorSL; // mail error state-less bean

	// this support for distributed database
	private GridFacadeSL gridBean = null;
	
	//~ Methods ===============================================================
	/**
	 * Lookup the {@link GridFacadeSL} instance.
	 */
	@PostConstruct
	protected void init(){
		if (gridBean == null){
			gridBean = this.lookupLocal("GridFacadeSLBean", GridFacadeSL.class);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#buildQuery(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected Criteria buildQuery(MailHeader search) throws EJBException {
		// create criteria.
		Criteria criteria = this.getCriteria(MailHeader.class);

		if (search == null){
			throw new EJBException("The search criteria is not validate !");
		}

		if (search != null) {
			
			// search for user.
			criteria.add(Property.forName("owner").eq(search.getOwner()));

			// add folder identifier.
			if(search.getFolder() != null){
				criteria.add(Property.forName("folder.id")
						.eq(search.getFolder().getId()));
			}
			
			// search on subject.
			if (StringService.hasLength(search.getSubject())) {
				criteria.add(Property.forName("subject").like(
						search.getSubject(), MatchMode.ANYWHERE));
			}

			// search on sender.
			if (StringService.hasLength(search.getSender())) {
				criteria.add(Property.forName("sender").like(
						search.getSender(), MatchMode.ANYWHERE));
			}

			// search on received.
			if (search.getReceived() != null) {
				criteria.add(Property.forName("received").ge(
						search.getReceived()));
			}

			// search on sent.
			if (search.getSent() != null) {
				criteria.add(Property.forName("sent").ge(search.getSent()));
			}
		}

		// sort by received date.
		criteria.addOrder(Order.desc("received"));

		return criteria;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByAttached(long, java.lang.String, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByAttached(long id, String owner,
			boolean attached, int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery(
				"MailHeader.findByAttached");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("attached", CommonService.getAnswer(attached));

		// set start pos and end pos.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);
		List<MailHeader> result = load(query);
		int total = count(query);
		// execute query and return result.
		return new SearchResultDTO<MailHeader>(result,startPos,maxResult,total);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByAttached(long, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByAttached(long id,
			boolean attached, int startPos, int maxResult) throws EJBException {
		return findByAttached(id, getUserCode(), attached, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByFlag(long, java.lang.String, com.inet.mail.data.MailFlag, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByFlag(long id, String owner, MailFlag flag,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery(
				"MailHeader.findByFlag");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("flag", flag);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		List<MailHeader> result = load(query);
		int total = count(query);
		// execute query and return result.
		return new SearchResultDTO<MailHeader>(result,startPos,maxResult,total);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByFlag(long, com.inet.mail.data.MailFlag, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByFlag(long id, MailFlag flag,
			int startPos, int maxResult) throws EJBException {
		return findByFlag(id, getUserCode(), flag, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByFolder(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolder(long id, String owner, int startPos,
			int maxResult) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailHeader.findByFolder");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByFolder(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolder(long id, int startPos, int maxResult)
			throws EJBException {
		return findByFolder(id, getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByFolderOrderBySent(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySent(long id, String owner,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByFolderOrderBySent");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByFolderOrderBySent(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySent(long id, int startPos,
			int maxResult) throws EJBException {
		return findByFolderOrderBySent(id, getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByFolderOrderBySubject(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySubject(long id, String owner,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByFolderOrderBySubject");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query and return result.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByFolderOrderBySubject(long, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByFolderOrderBySubject(long id, int startPos,
			int maxResult) throws EJBException {
		return findByFolderOrderBySubject(id, getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByPriority(long, java.lang.String, com.inet.mail.data.MailPriority, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByPriority(long id, String owner,
			MailPriority priority, int startPos, int maxResult)
			throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByPriority");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("priority", priority);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);
		
		// execute query and return result.
		List<MailHeader> result = load(query);
		int total = count(query);
		
		return new SearchResultDTO<MailHeader>(result,startPos,maxResult,total);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByPriority(long, com.inet.mail.data.MailPriority, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByPriority(long id,
			MailPriority priority, int startPos, int maxResult)
			throws EJBException {
		return findByPriority(id, getUserCode(), priority, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByRead(long, java.lang.String, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByRead(long id, String owner, boolean read,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByRead");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("read", CommonService.getAnswer(read));

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query and return result.
		List<MailHeader> result = load(query);
		int total = count(query);
		
		return new SearchResultDTO<MailHeader>(result,startPos,maxResult,total);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByRead(long, boolean, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> findByRead(long id, boolean read,
			int startPos, int maxResult) throws EJBException {
		return findByRead(id, getUserCode(), read, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findBySubject(long, java.lang.String, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySubject(long id, String owner,
			String subject, int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findBySubject");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("subject", '%' + subject + '%');
		
		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findBySubject(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySubject(long id, String subject,
			int startPos, int maxResult) throws EJBException {
		return findBySubject(id, getUserCode(), subject, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByDate(long, java.lang.String, java.util.Date, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByDate(long id, String owner, Date date,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByDate");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("recv", date, TemporalType.DATE);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByDate(long, java.util.Date, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByDate(long id, Date date, int startPos,
			int maxResult) throws EJBException {
		return findByDate(id, getUserCode(), date, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findBySender(long, java.lang.String, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySender(long id, String owner, String sender,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findBySender");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("sender", sender);

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute query.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findBySender(long, java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findBySender(long id, String sender, int startPos,
			int maxResult) throws EJBException {
		return findBySender(id, getUserCode(), sender, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByType(long, java.lang.String, com.inet.mail.data.MailType, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByType(long id, String owner, MailType type,
			int startPos, int maxResult) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.findByType");

		// set parameter.
		query.setParameter("id", id);
		query.setParameter("owner", owner);
		query.setParameter("type", type.toString());

		// set start position and max result.
		query.setFirstResult(startPos);
		query.setMaxResults(maxResult);

		// execute and return the result.
		return load(query);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findByType(long, com.inet.mail.data.MailType, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailHeader> findByType(long id, MailType type, int startPos,
			int maxResult) throws EJBException {
		return findByType(id, getUserCode(), type, startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(long bizId) throws EJBException {
		MailHeader item = load(bizId);
		if (item != null){
			// 1. we have mail content, delete from grid model
			if (item.getComposeID() != null){
				gridBean.delete(item.getComposeID());
			}
			
			remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderSL#cacheExpired(java.util.Date)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int cacheExpired(Date expired) throws EJBException {
		// delete from header
		int count = getEntityManager().createNamedQuery("MailHeader.deleteCache").setParameter("expired", expired).executeUpdate();
		// delete from database
		gridBean.expired(expired);
		// return this to caller
		return count;
	}	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailHeader load(long bizId) throws EJBException {
		return load(bizId, MailHeader.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#countMessage(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<FolderCountDTO> countMessage(String owner) throws EJBException {
		// create query.
		Query query = getEntityManager().createNamedQuery("MailHeader.countMessage");

		// set parameter.
		query.setParameter("owner", owner);

		try{
			// execute the query and return the result. 
			return query.getResultList();
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not count the total of messages", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not count the total of messages", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#countMessage()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<FolderCountDTO> countMessage() throws EJBException {
		return countMessage(getUserCode());
	}

	/**
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#countUnread(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<FolderCountDTO> countUnread(String owner) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailHeader.countUnreadMessage");

		// set parameter.
		query.setParameter("owner", owner);
		query.setParameter("read", CommonService.NO);

		try{
			// execute the query and return the result.
			return query.getResultList();
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not count the number of unread messages", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not count the number of unread messages", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#countUnread()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<FolderCountDTO> countUnread() throws EJBException {
		return countUnread(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#search(com.inet.mail.data.SearchResultDTO)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> search(
			SearchResultDTO<MailHeader> criteria) throws EJBException {
		// create the criteria.
		Criteria query = this.buildQuery(criteria.getCriteria());

		// the total result.
		int totalResult = criteria.getTotalResult();
		if (criteria.isCount()) {
			totalResult = count(criteria.getCriteria());
		}

		// set result information.
		query.setFirstResult(criteria.getStartPos());
		query.setMaxResults(criteria.getMaxResult());

		// get list of result.
		List<MailHeader> result = query(query);

		// create result.
		return new SearchResultDTO<MailHeader>(result, criteria.getStartPos(),
				criteria.getMaxResult(), totalResult);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#search(java.lang.String, int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> search(String criteria, int startAt,
			int maxItem) throws EJBException {
		// create the criteria.
		Criteria query = buildQuery(criteria) ;
		
		// get the total.
		int totalResult = count(criteria) ;
		
		// set result information.
		query.setFirstResult(startAt) ;
		query.setMaxResults(maxItem) ;
		
		// query data.
		List<MailHeader> result = query(query) ;
		
		// create result.
		return new SearchResultDTO<MailHeader>(result, startAt, maxItem, totalResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#move(long, long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader move(long id, long folderId) throws EJBException {
		// get the mail header.
		MailHeader header = load(id);

		// update mail folder.
		if (header != null) {
			MailFolder folder = this.folderSL.load(folderId);

			// update folder.
			header.setFolder(folder);

			return update(header);			
		}

		// return mail header information.
		return header;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#moveInbox2Folder(long, long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader moveInbox2Folder(long id, long folderId)
			throws EJBException {
		// get the mail header.
		MailHeader header = this.load(id);

		// update mail folder.
		if (header != null) {
			// find the current folder.
			MailFolder curFolder = this.findFolderById(id, header.getOwner()) ;
			
			// does not move the mail difference in other folder.
			if(!FolderType.INBOX.equals(curFolder.getType())) return header;
			
			// move message to folder.
			MailFolder folder = folderSL.load(folderId) ;
			
			// update folder.
			header.setFolder(folder) ;
			
			update(header) ;
		}

		// return mail header information.
		return header;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see com.inet.mail.business.sr.MailHeaderBase#move2Folder(long, long)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void move2Folder(long currentId, long folderId) throws EJBException {
	  Query query = this.getEntityManager().createNativeQuery("UPDATE MAIL_HEADER SET mail_header_folder = :folderId WHERE mail_header_folder = :currentId");
	  query.setParameter("folderId", folderId);
	  query.setParameter("currentId", currentId);
	  
	  query.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findFolderById(long, java.lang.String)
	 */
	@SuppressWarnings({"unchecked"})
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolder findFolderById(long id, String owner) throws EJBException {
		// create query.
		Query query = this.getEntityManager().createNamedQuery("MailHeader.findFolderById");

		// setup query data.
		query.setParameter("owner", owner);
		query.setParameter("id", id);
		
		try{
			// execute query and get result.
			List<MailFolder> folders = (List<MailFolder>) query.getResultList();
	
			// return data
			return (folders == null ? null : folders.get(0));
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not find any mail folders.", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not find any mail folders.", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findFolderById(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolder findFolderById(long id) throws EJBException {
		return findFolderById(id, getUserCode());
	}

	/** 
	 * {@inheritDoc}
         * @see com.inet.mail.business.base.BaseMailBusinessBean#save(java.lang.Object)
         */
        @Override
        public MailHeader save(MailHeader header) throws EJBException {
          if(header.getSubject() != null && header.getSubject().length() > 998){
            header.setSubject(header.getSubject().substring(0, 997));
          }
          
          return super.save(header);
        }

  /**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#update(long, com.inet.mail.data.MailFlag)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(long id, MailFlag flag) throws EJBException {
		// get the mail header.
		MailHeader header = load(id);

		// update mail flag.
		if (header != null) {
			header.setFlag(flag);

			update(header);
		}

		// return mail header information.
		return header;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#update(long, com.inet.mail.data.MailType)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(long id, MailType type) throws EJBException {
		// get the mail header.
		MailHeader header = load(id);

		// update mail flag.
		if (header != null) {
			header.setType(type);

			update(header);
		}

		// return mail header information.
		return header;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#save(com.inet.mail.persistence.MailHeader, byte[], long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader save(MailHeader header, byte[] content, long folderId) throws EJBException {
		// 1. save the email content
		header.setTimeCache(null);
		if (content != null && content.length > 0) {
			
			String application = Application.EMAIL + ":" + header.getOwner();
			// index the email content as well as save 
			if (content != null && content.length > 0){
				// set cache's time ONLY FOR EMAIL FROM SERVER!!
				if (StringService.hasLength(header.getUid())){
					header.setTimeCache(new Date());
					header.setComposeID(gridBean.saveExpired(application,content,DataFormat.EML));
				}else{
					String uuid = UUIDGenerator.getUUID();
					// index this data
					EmailHeaderDTO idxHeader = new EmailHeaderDTO();
					// get headerID of his email
					idxHeader.setHeaderID(header.getComposeID());
					idxHeader.setReceived(header.getReceived());
					idxHeader.setSubject(header.getSubject());
					idxHeader.setSender(header.getSender());
					idxHeader.setUuid(uuid);
					
					header.setComposeID(gridBean.index(application, uuid, IOService.getStream(idxHeader),content,DataFormat.EML));
				}
			}
		}

		// find folder from the given folder type.
		header.setFolder(folderSL.load(folderId));

		// save the mail header.
		return save(header);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#getFolders(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolderEntry getFolders(String owner) throws EJBException {
		// check the mail folder is exist.
		List<MailFolder> folders = this.folderSL.findByUser(owner);
		if (folders == null || folders.size() == 0) {
			return null;
		}
		
		// find the folders.
		MailFolderEntry result = new MailFolderEntry(MailConstant.MAIL_ON_THIS_COMPUTER);

		HashMap<Long, List<Integer>> queue = new HashMap<Long, List<Integer>>();
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
			final MailFolderEntry child = new MailFolderEntry(folder);

			buildRecusive(child, folder.getId(), folders, queue);
			result.addChild(child);
		}

		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#getFolders()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MailFolderEntry getFolders() throws EJBException {
		return getFolders(getUserCode());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#getComposer(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public byte[] getComposer(String contentId) throws EJBException {
		ContentDTO content = gridBean.loadContent(contentId);
		
		// return the composer content.
		return (content == null ? null : content.getContent()) ;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#getDefaultConfigure()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Map<String, String> getDefaultConfigure() throws EJBException {
		// get configuration bean definition.
		WebOSApplicationContext context = MailApplicationContext.getInstance().getContext() ;
		
		// return system configuration.
		return context.getBean(ConfigurationBeanDefinition.CONFIGURATION_BEAN_DEFINITION_NAME, ConfigurationBeanDefinition.class).getSysConfigure();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#update(com.inet.mail.persistence.MailHeader, byte[])
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MailHeader update(MailHeader header, byte[] content) throws EJBException {
		// update the HeaderContent
		header = super.update(header);
		
		// update content
		if (content != null && content.length > 0) {
			String application = Application.EMAIL + ":" + header.getOwner();
			
			if (content != null && content.length > 0) {
				if (header.getComposeID() == null){
					// set cache's time ONLY FOR EMAIL FROM SERVER!!
					if (StringService.hasLength(header.getUid())){
						header.setTimeCache(new Date());
						header.setComposeID(gridBean.saveExpired(application,content,DataFormat.EML));
					}else{
						String uuid = UUIDGenerator.getUUID();
						// index this data
						EmailHeaderDTO idxHeader = new EmailHeaderDTO();
						// get headerID of his email
						idxHeader.setHeaderID(header.getComposeID());
						idxHeader.setReceived(header.getReceived());
						idxHeader.setSubject(header.getSubject());
						idxHeader.setSender(header.getSender());
						idxHeader.setUuid(uuid);
						
						// send this data into indexer engine
						header.setComposeID(gridBean.index(application, uuid, IOService.getStream(idxHeader),content,DataFormat.EML));
					}	
					
					super.update(header);
				}else{
					gridBean.update(header.getComposeID(),content);
				}
			}			
		}

		return header;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findByUser(java.lang.String)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findFilterByUser(String owner) throws EJBException {
		return filterSL.findByUser(owner);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findFilterByUser()
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<MailFilter> findFilterByUser() throws EJBException {
		return filterSL.findByUser(getUserCode());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#searchByFolderDefault(java.lang.String,int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> searchByFolderDefault(String owner,
			int startPos, int maxResult) throws EJBException {
		// get folder default
		MailFolder folder = folderSL.getDefaultFolder(owner, -(FolderType.INBOX.ordinal() + 1));

		// if folder don't exist the return null
		if (folder == null) return null;

		// create search result from folder
		MailHeader header = new MailHeader();
		header.setFolder(folder);
		header.setOwner(owner);

		SearchResultDTO<MailHeader> criteria = new SearchResultDTO<MailHeader>(
				header, startPos, maxResult);

		// perform the search.
		return search(criteria);
	}	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#searchByFoderDefault(int, int)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SearchResultDTO<MailHeader> searchByFolderDefault(int startPos,
			int maxResult) throws EJBException {
		return searchByFolderDefault(getUserCode(), startPos, maxResult);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#deleteByUid(java.lang.String, java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteByUid(String usercode, String account, List<String> uids)
			throws EJBException {
		try{
			// create query.
			Query query = this.getEntityManager().createNamedQuery("MailHeader.deleteByUid");
			
			// set query parameter.
			query.setParameter("account", account) ;
			query.setParameter("uids", uids) ;
			query.setParameter("owner", usercode) ;
			
			// execute the search.
			query.executeUpdate() ;
		}catch(javax.ejb.EJBException ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}catch(Exception ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#deleteByUid(java.lang.String, java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteByUid(String account, List<String> uids)
			throws EJBException {
		deleteByUid(getUserCode(), account, uids) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#deleteNoneReceivedMail(java.lang.String, long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteNoneReceivedMail(String usercode, long folderId)
			throws EJBException {
		try{
			// create query.
			Query query = getEntityManager().createNamedQuery("MailHeader.deleteNoneReceivedMail") ;
			
			// set query parameter.
			query.setParameter("owner", usercode) ;
			query.setParameter("folderId", folderId) ;
			
			// execute query.
			query.executeUpdate() ;
		}catch(javax.ejb.EJBException eex){
			throw new EJBException("Could not delete none received mail", eex) ;
		}catch(Exception ex){
			throw new EJBException("Could not delete none received mail", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#deleteNoneReceivedMail(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteNoneReceivedMail(long folderId) throws EJBException {
		deleteNoneReceivedMail(getUserCode(), folderId) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#emptyFolder(java.lang.String, java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void emptyFolder(String usercode, List<String> uids)
			throws EJBException {
		try{
			// create query.
			Query query = this.getEntityManager().createNamedQuery("MailHeader.emptyFolder");
			
			// set query parameter.
			query.setParameter("uids", uids) ;
			query.setParameter("owner", usercode) ;
			
			// execute the search.
			query.executeUpdate() ;
		}catch(javax.ejb.EJBException ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}catch(Exception ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#emptyFolder(java.util.List)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void emptyFolder(List<String> uids) throws EJBException {
		emptyFolder(getUserCode(), uids) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findUidByAccount(java.lang.String)
	 */
	@SuppressWarnings({"unchecked"})
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> findUidByAccount(String usercode, String account) throws EJBException {
		try{
			// create query.
			Query query = this.getEntityManager().createNamedQuery("MailHeader.findUidByAccount");
			
			// set query parameter.
			query.setParameter("account", account) ;			
			query.setParameter("owner", usercode) ;
			
			// perform the search.
			List<String> uids = (List<String>)query.getResultList();
			
			List<String> uidErrors = mailErrorSL.findUidByAccount(usercode, account);
			
			if(uidErrors != null){
			  uids.addAll(uidErrors);
			}
			
			return uids;
		}catch(javax.ejb.EJBException ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findUidByAccount(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> findUidByAccount(String account) throws EJBException {
		return findUidByAccount(getUserCode(), account);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderBase#findUidByFolder(java.lang.String, long)
	 */
	@SuppressWarnings({"unchecked"})
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> findUidByFolder(String usercode, long folderId) throws EJBException {
		try{
			// create query.
			Query query = this.getEntityManager().createNamedQuery("MailHeader.findUidByFolder");
			
			// set query parameter.
			query.setParameter("owner", usercode) ;
			query.setParameter("folderId", folderId) ;
			
			// perform the search.
			return (List<String>)query.getResultList();
		}catch(javax.ejb.EJBException ex){
			throw new EJBException("Could not execute the query.", ex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderRemoteSL#findUidByFolder(long)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<String> findUidByFolder(long folderId) throws EJBException {
		return findUidByFolder(getUserCode(), folderId);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.sr.MailHeaderSL#markAsReadFolder(long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void markAsReadFolder(long folderId) throws EJBException {
		 // create query.
          Query query = this.getEntityManager().createNamedQuery("MailHeader.markAsReadFolder");
          
          // set query parameter.
          query.setParameter("folderId", folderId) ;                        
          
          // perform the update.
          query.executeUpdate();
	}
	
	/**
	 * @see com.inet.mail.business.sr.MailHeaderBase#getCurrentUsing(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public long getCurrentUsing(String owner, String account) throws EJBException {
		 // create query.
          Query query = this.getEntityManager().createNamedQuery("MailHeader.countCurrentUsing");
          
          // set query parameter.
          query.setParameter("owner", owner);
          query.setParameter("account", account);
          
          Object object = query.getSingleResult(); 

          // perform the query.
          return object != null ?(Long)object : 0;
	}
	//~ Helper methods ========================================================
	/**
	 * Build tree data
	 * 
	 * @param entry the given {@link MailFolderEntry} instance.
	 * @param parent the given parent.
	 * @param folders the given list of {@link MailFolder} instance.
	 * @param queue the list parent position.
	 */
	private void buildRecusive(
					MailFolderEntry entry, Long parent,
					List<MailFolder> folders, 
					HashMap<Long, List<Integer>> queue) {
		// some children maybe
		if (queue.containsKey(parent)) {
			for (int pos : queue.get(parent)) {
				MailFolder folder = folders.get(pos);

				final MailFolderEntry child = new MailFolderEntry(folder);

				buildRecusive(child, folder.getId(), folders, queue);
				if (child != null) entry.addChild(child);
			}
		}
	}

	/**
	 * Count the list of mail header from the given criteria.
	 * 
	 * @param criteria MailHeader - the given mail header.
	 * @return the number of header information.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private int count(MailHeader criteria) throws EJBException {
		// create criteria.
		Criteria query = this.getCriteria(MailHeader.class);
		
		if (criteria == null ) {
			throw new EJBException("The search criteria is not validate !");
		}

		// set projection.
		query.setProjection(Projections.rowCount());
		if (criteria != null) {
			// search for user.
			query.add(Property.forName("owner").eq(criteria.getOwner()));

			if(criteria.getFolder() != null){
				// add folder identifier.
				query.add(Property.forName("folder.id").eq(
								criteria.getFolder().getId()));
			}

			// search on subject.
			if (StringService.hasLength(criteria.getSubject())) {
				query.add(Property.forName("subject").like(
						criteria.getSubject(), MatchMode.ANYWHERE));
			}

			// search on sender.
			if (StringService.hasLength(criteria.getSender())) {
				query.add(Property.forName("sender").like(criteria.getSender(),
						MatchMode.ANYWHERE));
			}

			// search on received.
			if (criteria.getReceived() != null) {
				query.add(Property.forName("received").ge(
						criteria.getReceived()));
			}

			// search on sent.
			if (criteria.getSent() != null) {
				query.add(Property.forName("sent").ge(criteria.getSent()));
			}
		}

		// count the result.
		return this.count(query);
	}
	
	/**
	 * Count the list of mail header from the given criteria.
	 * 
	 * @param criteria the given mail header.
	 * @return the number of header information.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private int count(String criteria) throws EJBException {		
		if (criteria == null) {
			throw new EJBException("The search criteria is not validate !");
		}
		
		// create criteria.
		Criteria query = this.getCriteria(MailHeader.class);		

		// set projection.
		query.setProjection(Projections.rowCount());

		// build the query.
		query.add(Restrictions.and(Restrictions.or(
					Property.forName("subject").like(criteria, MatchMode.ANYWHERE).ignoreCase(),
						Property.forName("sender").like(criteria, MatchMode.ANYWHERE).ignoreCase()),
					
					Property.forName("owner").eq(getUserCode())
				)
			) ;
		
		// count the result.
		return count(query);
	}
	
	/**
	 * Create the search criteria from the given search value.
	 * 
	 * @param criteria the given search criteria.
	 * 
	 * @return the search {@link Criteria} instance.
	 * @throws EJBException when error occurs during building query.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	protected Criteria buildQuery(String criteria) throws EJBException {
		if (criteria == null) {
			throw new EJBException("The search criteria is not validate !");
		}
		
		// create criteria.
		Criteria query = this.getCriteria(MailHeader.class);		

		// build the query.
		query.add(Restrictions.and(Restrictions.or(
						Property.forName("subject").like(criteria, MatchMode.ANYWHERE).ignoreCase(),
							Property.forName("sender").like(criteria, MatchMode.ANYWHERE).ignoreCase()),
						Property.forName("owner").eq(getUserCode()))
			) ;

		// sort by received date.
		query.addOrder(Order.desc("received"));

		return query;
	}  	
}
