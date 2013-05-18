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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.JMSException;

import org.jboss.annotation.JndiInject;
import org.jboss.ejb3.mdb.ProducerManager;
import org.jboss.ejb3.mdb.ProducerObject;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailDTO;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.service.MailSynchronizeSL;

/**
 * MailResetPasswordSLBean.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
@Stateless
public class MailResetPasswordSLBean implements MailResetPasswordRemoteSL,
		MailResetPasswordSL {
	//~ Instance fields =======================================================
	/* producer manager. */
	private ProducerManager localManager;
	
	/* mail synchronized bean. */
	private MailSynchronizeSL synchonizeBean;

	//~ Methods ===============================================================
	/**
	 * Set local bean.
	 * 
	 * @param synchonizeBean the given {@link MailSynchronizeSL} local bean.
	 */
	@JndiInject(jndiName = "com.inet.mail.service.FastMailTempleteSL")
	public void setLocal(MailSynchronizeSL synchonizeBean) {
		this.synchonizeBean = synchonizeBean;
		this.localManager = ((ProducerObject) synchonizeBean).getProducerManager();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.business.delivery.MailResetPasswordRemoteSL#resetPassword(com.inet.mail.data.MailDTO, boolean)
	 */
	@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
	public void resetPassword(MailDTO data, boolean flag) throws EJBException {
		// get mail configuration bean.
		MailApplicationContext applicationContext = MailApplicationContext.getInstance() ;
		if(applicationContext == null || applicationContext.getContext() == null) return ;
		
		// create mail factory.
		AbstractMailFactory factory = MailConfigureFactory.createFactory() ;
		
		// get message composer.
		IMessageComposer composer = factory.createMessage(true) ;
		
		// 1. Body of this message
		composer.setBody(data.getBody());

		// 2. address
		if (data.getSender() != null && data.getSender().length > 0){
			composer.setFrom(new Address(data.getSender()[0]));
		}

		// 3. subject
		composer.setSubject(data.getSubject());

		// 4. To address
		if (data.getTo() != null) {
			for (String key : data.getTo().keySet()) {
				Address addrs = new Address(data.getTo().get(key), key);
				composer.addTo(addrs.getFullAddress());
			}
		}

		// 5. put into MDB queue for sending
		try {
			localManager.connect();
			
			// send message.
			if (data.getVariables() != null && data.getVariables().size() > 0){
				synchonizeBean.send(data.getVariables(), composer);
			}else{
				synchonizeBean.send(composer);
			}
		} catch (JMSException ex) {
			// throw exception.
			throw new EJBException("Could not send this message", ex);
		} finally{
			try{
				localManager.close();
			}catch(JMSException ex){}
		}
	}
}
