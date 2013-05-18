/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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

import javax.ejb.Local;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.persistence.MailSignatureInfo;

/**
 * MailSignatureInfoSL.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Local
public interface MailSignatureInfoSL extends MailSignatureInfoBase {
	/**
	 * Search all {@link MailSignatureInfo} from the given owner user.
	 * 
	 * @param owner the given owner user information.
	 * @return the list of {@link MailSignatureInfo} instance.
	 * 
	 * @throws EJBException when error occurs during querying signature information.
	 */
	List<MailSignatureInfo> query(String owner) throws EJBException;	
}
