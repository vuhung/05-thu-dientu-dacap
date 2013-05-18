/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

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
package com.inet.web.bo.mail;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.persistence.RecipientSender;
import com.inet.web.bf.mail.MailRecipientBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.exception.WebOSException;

/**
 * MailRecipientBO.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailRecipientBO.java Jan 11, 2010 11:56:51 AM Tan Truong $
 *
 * @since 1.0
 */
public class MailRecipientBO extends AbstractWebOSBO<MailRecipientBF>{

  /**
   * Constructor MailRecipientBO
   * @param businessFacade MailRecipientBF
   */
  protected MailRecipientBO(MailRecipientBF businessFacade) {
    super(businessFacade);
  }
  
  /**
   * Add list of recipient sender.
   * @param senders the given list of recipient sender
   * @throws WebOSException  if an error occurs during add recipient sender.
   */
  public void add(List<RecipientSender> senders) throws WebOSException{
    try {
      this.getBusinessFacade().getFacade().add(senders);    
    } catch (EJBException ejbEx) {
      throw new WebOSException("Error while add list of recipient sender", ejbEx);   
    }
  }

  /**
   * Find mail recipient from given user code
   * @param owner the given user code
   * @return the list of mail recipient
   * @throws WebOSException  if an error occurs during find mail recipient.
   */
  public List<RecipientSender> findByOwner(String owner) throws WebOSException{
    try {
      return this.getBusinessFacade().getFacade().findByOwner(owner);    
    } catch (EJBException ejbEx) {
          throw new WebOSException("Error while update mail signature", ejbEx);   
    }
  }
}
