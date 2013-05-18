/*****************************************************************
   Copyright 2009 by Truong Ngoc Tan (tntan@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
package com.inet.mail.business.sr;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.persistence.RecipientSender;

/**
 * MailRecipientBase.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 0.2i
 */
public interface MailRecipientBase {

	long add(RecipientSender recipient) throws EJBException;
	
	List<RecipientSender> findByOwner(String owner) throws EJBException;
	
	void add(List<RecipientSender> recipients) throws EJBException;
}
