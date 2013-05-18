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

import javax.ejb.Remote;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.persistence.MailAcctConfigInfo;

/**
 * MailAcctConfigInfoRemoteSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 26, 2008
 * <pre>
 *  Initialization MailConfigureRemoteSL class.
 * </pre>
 */
@Remote
public interface MailAcctConfigInfoRemoteSL extends MailAcctConfigInfoBase {
	/**
	 * Find mail configuration by user.
	 * 
	 * @return the {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during finding mail configuration.
	 */
	MailAcctConfigInfo findByUser() throws EJBException ;
	
	/**
	 * Create {@link MailAcctConfigInfo} instance.
	 * 
	 * @param fullName the given full name
	 * @param object the given {@link MailReceiverObject} instance.
	 * 
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during create configure 
	 */
	MailAcctConfigInfo createConfig(String fullName, MailReceiverObject object) throws EJBException;
	
	/**
         * Create {@link MailAcctConfigInfo} instance.
         * 
         * @param fullName the given full name
         * @param code the given user code
         * @param object the given {@link MailReceiverObject} instance.
         * 
         * @return {@link MailAcctConfigInfo} instance.
         * @throws EJBException if an error occurs during create configure 
         */
        MailAcctConfigInfo addAccount(String code, String fullName, MailReceiverObject object) throws EJBException;
	
	/**
	 * Active/ inactive account mail 
	 * 
	 * @param emailAddress the given email address which active/inactive 
	 * @param isActive the given active/inactive
	 *  
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during active/inactive account
	 */
	MailAcctConfigInfo activeAccount(String emailAddress, boolean isActive) throws EJBException;	
	
	/**
	 * Change password for email account
	 * 
	 * @param emailAddress the given {@link List} of email address which change password
	 * @param password the given new password(non encrypt)
	 * 
	 * @return {@link MailAcctConfigInfo} instance.
	 * @throws EJBException if an error occurs during change password 
	 */
	MailAcctConfigInfo changePassword(List<String> emails, String password) throws EJBException;
	
	/**
	 * Remove list of accounts from configure 
	 * 
	 * @param accounts the given {@link List} of accounts
	 * @throws EJBException if an error occurs during remove accounts 
	 */
	void delete(List<String> accounts) throws EJBException;
}
