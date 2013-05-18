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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.command.MailCommand;
import com.inet.mail.command.MailDataQuery;
import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.data.FollowUpDTO;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.FollowUp;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailHeader;
import com.inet.web.application.exception.ApplicationServerException;
import com.inet.web.bf.mail.MailBridgeBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;

/**
 * MailBridgeBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version 1.0i
 * 
 * @date Sep 13, 2008
 * <pre>
 *  Initialization MailBridgeBO class.
 * </pre>
 */
public class MailBridgeBO extends AbstractWebOSBO<MailBridgeBF> {
	/**
	 * logger.
	 */
        protected final Logger logger =  Logger.getLogger(getClass()); 
	
	/**
	 * MailBridgeBO constructor
	 * 
	 * @param businessFacade
	 *            MailBridgeBF
	 */
	protected MailBridgeBO(MailBridgeBF businessFacade) {
		super(businessFacade);
	}

	/**
	 * View mail body.
	 * 
	 * @param headerId
	 *            Long - the given mail header identifier.
	 * @return MailHeaderDTO the mail header data.
	 * @throws WebOSBOException
	 *             if an error occurs during loading body.
	 */
	public MailHeaderDTO viewBody(long headerId) throws WebOSBOException {
		try {
			return getBusinessFacade().getFacade().viewBody(headerId);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("error while load mail header DTO",ejbEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}

	/**
	 * @param headerId
	 * @return
	 * @throws WebOSBOException
	 *             if an error occurs during draft forward message.
	 */
	public MailHeaderDTO forward(long headerId) throws WebOSBOException {
		MailCommand command = new MailCommand(getBusinessFacade().getFacade());
		try {
			return command.forward(headerId);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException(
					"error while load view temp messages forward", ejbEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}

	/**
	 * reply messages
	 * 
	 * @param headerId
	 * @param isReplyAll
	 * @return MailHeaderDTO
	 * @throws WebOSBOException
	 *             if an error occurs during draft reply message.
	 */
	public MailHeaderDTO reply(long headerId, boolean isReplyAll)
			throws WebOSBOException {
		MailCommand command = new MailCommand(getBusinessFacade().getFacade());
		try {
			return command.reply(headerId, isReplyAll);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException(
					"error while load view temp message reply", ejbEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}

	/**
	 * 
	 * @param composer
	 * @return
	 * @throws WebOSBOException
	 */
	public MailHeader save(IMessageComposer composer) throws WebOSBOException {
		MailCommand command = new MailCommand(getBusinessFacade().getFacade());
		try {
			return command.drafts(composer);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException(
					"error while save email", ejbEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}

	/**
	 * @param header
	 * @param composer
	 * @return
	 * @throws WebOSBOException
	 */
	public MailHeader save(MailHeader header, IMessageComposer composer)
			throws WebOSBOException {
		MailCommand command = new MailCommand(getBusinessFacade().getFacade());
		try {
			return command.drafts(header, composer);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException(
					"error while load view temp message reply" + ejbEx.getMessage(), ejbEx);
		} catch (Exception ex) {
			throw translateException(ex);
		}
	}

	/**
	 * @param header
	 * @param composer
	 * @return
	 */
	public boolean send(long headerId , IMessageComposer composer, String account) throws WebOSBOException {
		try {
			if (headerId > 0) {
				getBusinessFacade().getFacade().sendMessage(composer, headerId,account);
			} else {
				//MailHeader header = this.save(composer);
				getBusinessFacade().getFacade().sendMessage(composer, account);
			}			
			
		} catch (ApplicationServerException e) {
			logger.error("ERROR while send email", e);
			return false;
		} catch (EJBException e) {
			logger.error("ERROR while send email", e);
			return false;
		}catch (Exception e) {
			logger.error("ERROR while send email", e);
			return false;
		}
		return true;
	}

	/**
	 * check new email
	 */
	public MailHeader checkMail(String account) throws WebOSBOException {
		try {
			return  getBusinessFacade().getFacade().fetch(account);
		} catch (EJBException ejbex) {
			throw new WebOSBOException("EJB exception ****" + ejbex.getMessage(), ejbex);
		} catch (Exception ex){
			throw translateException(ex);
		}
	}
	
	public int checkNewMessage(String account) throws WebOSBOException{
		try {
			return  getBusinessFacade().getFacade().fetchNewMessage(account);
		} catch (EJBException ejbex) {
			throw new WebOSBOException("EJB exception ****" + ejbex.getMessage(), ejbex);
		} catch (Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * get list of SMTP server: account
	 * @return Map<String,Boolean>
	 * @throws WebOSBOException
	 */
	public List<String> getSmtpAccount() throws WebOSBOException{
		try{
			return getBusinessFacade().getFacade().getSmtpAccount();
		}catch(EJBException ejbex){
			throw new WebOSBOException("EJB exception ****" + ejbex.getMessage(), ejbex);
		}catch (Exception ex){
			throw translateException(ex);
		}		
	}
	/**
	 * get data attachment from given key and content identifier
	 * @param contentId
	 * @param key
	 * @return
	 */
	public PairValueDTO<byte[]> getAttachment(long id, String key) throws WebOSBOException{
		MailDataQuery dataQuery = new MailDataQuery(this.getBusinessFacade().getFacade());
		try {
			return dataQuery.viewAttachment(id, key);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("error while load attachment", ejbEx);
		}catch (Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * get data attachment from given key and content identifier
	 * @param contentId
	 * @param key
	 * @return
	 */
	public Map<String, byte[]> getAttachment(long contentId) throws WebOSBOException{
		MailDataQuery dataQuery = new MailDataQuery(this.getBusinessFacade().getFacade());
		try {
			return dataQuery.viewAttachment(contentId);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("error while load attachment", ejbEx);
		}
	}
	
	/**
	 * @return
	 * @throws WebOSBOException
	 */
	public Map<Long, Long> countUnread() throws WebOSBOException {
		try {
			return this.getBusinessFacade().getFacade().countUnread();
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException(
					"error while count unread message", asEx);
		} catch (EJBException ejbEx) {
			throw translateException(ejbEx);
		}catch (Exception ex){
			throw translateException(ex);
		}		
	}
	
	/**
	 * delete mail
	 * @param id - the given identifier mail header
	 * @throws WebOSBOException
	 */
	public void delete(long id) throws WebOSBOException{
		try {
			this.getBusinessFacade().getFacade().delete(id);
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException("delete email", asEx);
		} catch (EJBException ejbEx) {
			throw translateException(ejbEx);
		} catch(Exception ex){
			throw translateException(ex) ;
		}
	}
	
	/**
	 * delete mail headers 
	 * @param ids List<Long>
	 * @throws WebOSBOException
	 */
	public void delete(List<Long> ids) throws WebOSBOException{
		for(long id: ids) delete(id);
	}
	
	/**
	 * get personal configuration
	 * @param userCode
	 * @return PersonMailConfig
	 */
	public PersonMailConfig getPersonalConfig(String userCode){
		try {
			return this.getBusinessFacade().getFacade().getMailConfig(userCode);
		} catch (ApplicationServerException asEx) {
			logger.error("error while get personal config ", asEx);
			throw new WebOSBOException("error while get personal config ", asEx);
		} catch (EJBException ejbEx) {
			logger.error("error while get personal config ", ejbEx);
			throw translateException(ejbEx);
		}
	}
	
	/**
	 * @return the  {@link MailAcctConfigInfo} of current login user.
	 */
	public MailAcctConfigInfo loadMailConfig(){
		try {
			return this.getBusinessFacade().getFacade().loadMailConfig();
		} catch (ApplicationServerException asEx) {
			logger.error("error while get personal config ", asEx);
			throw new WebOSBOException("error while get personal config ", asEx);
		} catch (EJBException ejbEx) {
			logger.error("error while get personal config ", ejbEx);
			throw translateException(ejbEx);
		}
	}	
	
	/**
	 * empty folder from given folder identifier 
	 * @param folderId long - the given folder identifier
	 * @return
	 */
	public void emptyFolder(long folderId){
		try {
			this.getBusinessFacade().getFacade().emptyFolder(folderId);
		} catch (ApplicationServerException asEx) {
			logger.error("error while get personal config ", asEx);
			throw new WebOSBOException("error while get personal config ", asEx);
		} catch (EJBException ejbEx) {
			logger.error("error while get personal config ", ejbEx);
			throw translateException(ejbEx);
		} catch (Exception ex){
			throw translateException(ex) ;
		}
	}
	
	/**
	 * save follow up
	 * @param followUps List<FollowUp> -the given list of follow up 
	 * @return List<FollowUp>
	 * @throws WebOSBOException if an error occurs during save follow up.
	 */
	public List<FollowUpDTO> saveFollowUp(List<FollowUp> followUps) throws WebOSBOException{
		try {
			return this.getBusinessFacade().getFacade().saveFollowUp(followUps);
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException("Error while save follow up", asEx);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("Error while save follow up", ejbEx);
		} catch (Exception ex){
			throw translateException(ex);
		}
	}
	
	/**
	 * update follow up
	 * @param followUp FollowUp - the given follow up 
	 * @return FollowUp
	 * @throws WebOSBOException if an error occurs during update follow up.
	 */
	public List<FollowUpDTO> updateFollowUp(List<Long> listId, Date date) throws WebOSBOException{
		try {
			return this.getBusinessFacade().getFacade().updateFollowUp(listId, date);
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException("Error while update follow up", asEx);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("Error while update follow up", ejbEx);
		}catch (Exception ex){
			throw translateException(ex);
		}
	}
}
