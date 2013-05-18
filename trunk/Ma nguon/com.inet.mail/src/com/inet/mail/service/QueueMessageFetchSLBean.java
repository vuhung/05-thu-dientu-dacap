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
package com.inet.mail.service;

import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.Consumer;
import org.jboss.annotation.ejb.Depends;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.StringService;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.IMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.business.sr.MailHeaderSL;
import com.inet.mail.conf.PersonMailConfig.ConfigDTO;
import com.inet.mail.conf.beans.SmtpBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailType;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.persistence.MailFolder;
import com.inet.mail.persistence.MailHeader;
import com.inet.mail.queue.MessageQueueType;

/**
 * QueueMessageFetchSLBean.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Consumer(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageQueueType.QUEUE_TYPE),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = MessageQueueType.QUEUE_MESSAGE_ASYNCHRONIZE)
})
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QueueMessageFetchSLBean implements MailSynchronizeSL {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	
        /* the mail factory bean definition. */
	private static SmtpBeanDefinition smtpBeanDefinition;
	
	/* the thread local. */
	private static ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>() ;	

	//~ Instance fields =======================================================
	/* template bean. */
	private TemplateEngineManagement serviceTemplate;

	/**
	 * mail header bean.
	 */
	@EJB(name = "MailHeaderSLBean")
	private MailHeaderSL mailBean;
	
	//~ Methods ===============================================================
	/**
	 * Set {@link TemplateEngineManagement} instance.
	 * 
	 * @param serviceTemplete the given {@link TemplateEngineManagement} instance.
	 */
	@Depends("inet:service=ServiceVTE")
	public void setServiceTemplete(TemplateEngineManagement serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.service.MailSynchronizeSL#send(com.inet.mail.conf.PersonMailConfig.ConfigDTO, com.inet.mail.parser.IMessageComposer, long, long)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void send(ConfigDTO smtp, IMessageComposer message, long messageId, long folderId) {
		if (logger.isDebugEnabled()) logger.debug("Send message from [queue/message/fetching] queue");

		// get configuration factory.
		IMailFactory factory = MailConfigureFactory.createFactory(
					smtp.getSecurityMode(), 
					smtp.getServerName(), 
					smtp.getServerPort(),
					smtp.getAccountName(),
					smtp.getAccountPassword()
				);
		
		// send message.
		if (factory != null && factory.connect()) {
			try{
				factory.send(message.compose());
				
				// move this email to sent folder
				MailHeader header = mailBean.load(messageId);
				header.setType(MailType.SENT);
				MailFolder folder = new MailFolder();
				folder.setId(folderId);
				header.setFolder(folder);
				header.setReceived(header.getSent());
				
				mailBean.update(header);
			}catch (MailException mex) {
				logger.warn("could not send message, please see log for more detail.", mex) ;
			} catch (EJBException mex) {
				logger.warn("could not send message, please see log for more detail.", mex) ;
			}catch(Exception ex){
				logger.warn("Could not send message, please see log for more details", ex) ;
			}finally{
				try{
					if(factory != null) factory.close() ;
				}catch(Exception ex){}
			}
		}
		
		// send message successful.
		if(logger.isDebugEnabled()) logger.debug("Send message successfully.") ;		
	}
	
	/**
	 * {@inheritDoc} 
	 * 
	 * @see com.inet.mail.service.MailSynchronizeSL#send(java.lang.Long, java.lang.String, java.util.List, com.inet.mail.parser.sun.MessageComposer)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void send(Long templateID, String templateName, List<PairValueDTO<String>> params, IMessageComposer composer) {
		try {
			// makeup content from template
			String bodyText = serviceTemplate.makeupContent(templateID, templateName, params);
			
			if (bodyText != null){
				composer.setBody(bodyText);
				
				// send message and normally return.
				send(composer);
				
				// send successful.
				if(logger.isDebugEnabled()) logger.debug("send message successful.") ;
			}
		} catch (EJBException eex) {
			logger.warn("Could not send this message with template: [" + templateName+ "]", eex);
		} catch (MailException mex) {
			logger.warn("Could not send this message with tempalte: [" + templateName + "]", mex) ;
		}
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.service.MailSynchronizeSL#send(java.util.List, com.inet.mail.parser.sun.MessageComposer)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void send(List<PairValueDTO<String>> params, IMessageComposer composer) {
		try {
			// makeup content from template
			String bodyText = serviceTemplate.makeupContent(composer.getBody(),params);
			if (bodyText != null) composer.setBody(bodyText);
			
			send(composer);
		} catch (EJBException eex) {
			logger.warn("Could not send this message with the subject: [" + composer.getSubject() + "]", eex);
		} catch (MailException mex){
			logger.warn("Could not send this message, please see log for more details", mex) ;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.service.MailSynchronizeSL#send(com.inet.mail.parser.sun.MessageComposer)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void send(IMessageComposer composer) {
		// get SMTP data.
		String sender = "postmaster@localhost.localdomain" ;

		// get SMTP configuration.
		SmtpBeanDefinition beanDefinition = getSmtpBeanDefinition() ;
		if(beanDefinition != null && StringService.hasLength(beanDefinition.getUsername())){
			sender = beanDefinition.getUsername() ;
		}
		
		// add default sender if we don't have this from message
		if (composer.getFrom() == null){
			composer.setFrom(new Address(sender));
		}
			
		// we compose message from given data
		Object message = composer.compose();
		if (message == null){
			logger.warn("The message is empty!");	
			return;
		}
		
		// send message.
		AbstractMailFactory mailFactory = MailConfigureFactory.createFactory() ;
		try{
			// connect to SMTP server and send mail.			
			if(mailFactory != null){
				if(mailFactory.connect()){
					mailFactory.send(message) ;
				}
			}
		}catch(MailException mex){
			logger.warn("Could not send mail to user, please see message for more details.", mex) ;
		}finally{
			try{
				if(mailFactory != null) mailFactory.close() ;
			}catch(Exception ex){} ;
		}
	}
	
	//~ Helper methods ========================================================
	/**
	 * @return the {@link SmtpBeanDefinition} instance.
	 * 
	 * @throws MailException when error occurs during getting {@link SmtpBeanDefinition} instance.
	 */
	private static SmtpBeanDefinition getSmtpBeanDefinition()
		throws MailException{
		if(contextHolder.get() == null){
			synchronized (contextHolder) {
				if(smtpBeanDefinition == null){
					// mail application context.
					MailApplicationContext context = MailApplicationContext.getInstance() ;
					if(context == null || context.getContext() == null){
						throw new MailException("The WebOSApplicationContext must be set.") ;
					}
					
					smtpBeanDefinition = context.getContext().getBean(
								SmtpBeanDefinition.SMTP_BEAN_DEFINITION_NAME, 
								SmtpBeanDefinition.class
							) ;
				}
				
				contextHolder.set(Boolean.TRUE) ;
			}
		}
		
		// the MailFactoryBeanDefinition bean.
		return smtpBeanDefinition ;
	}	
}
