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
package com.inet.mail.business.sr;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusinessBean;
import com.inet.mail.persistence.MailError;

/**
 * MailErrorSLBean.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailErrorSLBean.java Feb 19, 2011 11:23:37 AM Tan Truong $
 * 
 * @since 1.0
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
public class MailErrorSLBean extends BaseMailBusinessBean<MailError> implements MailErrorSL {

  /**
   * @see com.inet.mail.business.sr.MailErrorBase#findUidByAccount(java.lang.String, java.lang.String)
   */
  @Override
  @SuppressWarnings({"unchecked"})
  public List<String> findUidByAccount(String usercode, String account) throws EJBException {
    try {
      // create query.
      Query query = this.getEntityManager().createNamedQuery("MailError.findUidByAccount");

      // set query parameter.
      query.setParameter("account", account);
      query.setParameter("owner", usercode);

      // perform the search.
      return (List<String>) query.getResultList();
    } catch (javax.ejb.EJBException ex) {
      throw new EJBException("Could not execute the query.", ex);
    }
  }

  /**
   * @see com.inet.mail.business.base.BaseMailBusinessBean#delete(long)
   */
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void delete(long bizId) throws EJBException {
    MailError item = load(bizId);
    if (item != null) {
      remove(item);
    }
  }

  /**
   * @see com.inet.mail.business.base.BaseMailBusinessBean#load(long)
   */
  @Override
  public MailError load(long bizId) throws EJBException {
    return load(bizId, MailError.class);
  }
}
