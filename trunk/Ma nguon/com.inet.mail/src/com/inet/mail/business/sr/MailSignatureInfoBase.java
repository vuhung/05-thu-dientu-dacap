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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.persistence.MailSignatureInfo;

/**
 * MailSignatureInfoBase.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public interface MailSignatureInfoBase extends BaseMailBusiness<MailSignatureInfo> {	
	/**
	 * Update the {@link MailSignatureInfo} instance.
	 * 
	 * @param bizId the given mail signature information identifier.
	 * @param currentUsed the given flag that mark the signature is default signature.
	 * 
	 * @return the {@link MailSignatureInfo} instance.
	 * @throws EJBException when error occurs during updating {@link MailSignatureInfo} instance.
	 */
	MailSignatureInfo update(long bizId, char currentUsed) throws EJBException;
	
	/**
	 * Update/add new and remove signature
	 * 
	 * @param adds the given list of add new {@link MailSignatureInfo} instance.
	 * @param updates the given list of update {@link MailSignatureInfo} instance.
	 * @param removes the given list of remove mail signature information.
	 * 
	 * @return the list of {@link MailSignatureInfo} instance.
	 * @throws EJBException when error occurs during updating {@link MailSignatureInfo} instance.
	 */
	List<MailSignatureInfo> update(List<MailSignatureInfo> adds, List<MailSignatureInfo> updates , List<Long> removes) throws EJBException;
}
