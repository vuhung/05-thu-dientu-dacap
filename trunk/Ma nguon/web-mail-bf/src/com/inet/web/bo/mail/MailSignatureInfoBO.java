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
import com.inet.mail.persistence.MailSignatureInfo;
import com.inet.web.bf.mail.MailSignatureInfoBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.exception.WebOSException;

/**
 * MailSignatureInfoBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailSignatureInfoBO extends AbstractWebOSBO<MailSignatureInfoBF> {
	
	/**
	 * Constructor MailHeaderBO
	 * @param businessFacade MailHeaderBF
	 */
	protected MailSignatureInfoBO(MailSignatureInfoBF businessFacade) {
		super(businessFacade);
	}
	
	/**
	 * load signature  
	 * 
	 * @param userCode String - the given user code
	 * @return List<MailSignatureInfo> - list of mail signature
	 * @throws WebOSException 
	 */
	public List<MailSignatureInfo> loadSignature() throws WebOSException{
		try {
			return this.getBusinessFacade().getFacade().query();
		} catch (EJBException ejbEx) {
			throw new WebOSException("Error while load mail signature", ejbEx);	
		}
	}
	
	/**
	 * update, add new, remove signatures 
	 * @param signAddNews List<MailSignatureInfo> - list of mail signature which add new
	 * @param signUpdates List<MailSignatureInfo> - list of mail signature which update
	 * @param idRemoves list<Long> - list of identifier mail signature which delete
	 * @return  List<MailSignatureInfo> - list of mail signature
	 * @throws WebOSException
	 */
	public List<MailSignatureInfo> save(List<MailSignatureInfo> signAddNews, List<MailSignatureInfo> signUpdates, List<Long> idRemoves) throws WebOSException{
		try {
			return this.getBusinessFacade().getFacade().update(signAddNews, signUpdates, idRemoves);
		} catch (EJBException ejbEx) {
			throw new WebOSException("Error while update mail signature", ejbEx);	
		}
	}
}
