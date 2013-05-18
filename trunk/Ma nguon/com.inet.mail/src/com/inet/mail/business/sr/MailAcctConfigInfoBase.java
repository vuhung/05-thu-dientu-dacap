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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.persistence.MailAcctConfigInfo;

/**
 * MailAcctConfigInfoBase
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 26, 2008
 * <pre>
 *  Initialization MailConfigureBase class.
 * </pre>
 */
public interface MailAcctConfigInfoBase extends BaseMailBusiness<MailAcctConfigInfo> {	
	/**
	 * Update mail received DTO to mail configure.
	 * 
	 * @param bizId the given mail configure identifier.
	 * @param receiverDTO the given {@link MailReceiverDTO} instance.
	 * 
	 * @return {@link MailAcctConfigInfo} instance.
	 * 
	 * @throws EJBException if an error occurs during updating object.
	 */
	MailAcctConfigInfo update(long bizId, MailReceiverDTO receiverDTO) throws EJBException ;
	
	/**
	 * Update default SMTP
	 * 
	 * @param bizId the business identifier.
	 * @param defaultSMTP the given default SMTP value.
	 * 
	 * @return the {@link MailAcctConfigInfo} instance.
	 * 
	 * @throws EJBException when error occurs during updating {@link MailAcctConfigInfo} instance.
	 */
	MailAcctConfigInfo update(long bizId, String defaultSMTP) throws EJBException ;
	
	/**
	 * Create {@link MailAcctConfigInfo} instance.
	 * 
	 * @param userCode the given user code
	 * @param fullName the given full name
	 * @param object the given {@link MailReceiverObject} instance.
	 * 
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during create configure 
	 */
	MailAcctConfigInfo createConfig(String userCode,String fullName, MailReceiverObject object) throws EJBException;	
	
	/**
	 * Active/ inactive account mail 
	 * 
	 * @param userCode the given owner user code.
	 * @param emailAddress the given email address which active/inactive 
	 * @param isActive the given active/inactive
	 *  
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during active/inactive account
	 */
	MailAcctConfigInfo activeAccount(String userCode, String emailAddress, boolean isActive) throws EJBException;	
	
	/**
	 * Change password for email account
	 * 
	 * @param userCode the given user code
	 * @param emails the given list of email address which change password
	 * @param password the given new password(non encrypt)
	 * 
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during change password 
	 */
	MailAcctConfigInfo changePassword(String userCode, List<String> emails, String password) throws EJBException;
	
	/**
	 * Remove list of accounts from configure 
	 * 
	 * @param userCode the given user code
	 * @param accounts the given {@link List} of accounts
	 * 
	 * @throws EJBException if an error occurs during remove accounts 
	 */
	void delete(String userCode, List<String> accounts) throws EJBException;		
}
