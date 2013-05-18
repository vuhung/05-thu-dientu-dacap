/*****************************************************************
 Copyright 2007 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.mail.business.delivery;

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.MailDTO;

/**
 * MailResetPasswordSL.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Local
public interface MailResetPasswordSL {

	/**
	 * Reset the password of user, and function will send the email to user
	 * 
	 * @param data : the given information about password and email address of user
	 * @param flag : Indicates send this email one by one from TO address 
	 * @throws EJBException
	 */
	void resetPassword(MailDTO data, boolean flag) throws EJBException;
}
