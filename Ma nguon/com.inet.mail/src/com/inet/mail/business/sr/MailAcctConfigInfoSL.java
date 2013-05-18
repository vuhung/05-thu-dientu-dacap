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

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.persistence.MailAcctConfigInfo;

/**
 * MailAcctConfigInfoSL
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 26, 2008
 * <pre>
 *  Initialization MailConfigureSL class.
 * </pre>
 */
@Local
public interface MailAcctConfigInfoSL extends MailAcctConfigInfoBase {
	/**
	 * Find mail configuration by user.
	 * 
	 * @param code String - the given user code.
	 * @return the given {@link MailAcctConfigInfo} instance.
	 * 
	 * @throws EJBException if an error occurs during finding mail configuration.
	 */
	MailAcctConfigInfo findByUser(String code) throws EJBException ;
}
