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
package com.inet.mail.parser;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.log4j.Logger;

import com.inet.mail.conf.beans.MailFactoryBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.MailHeaderDTO;
import com.inet.mail.exception.MailException;

/**
 * AbstractMessageFactory
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jan 30, 2008
 * <pre>
 *  Initialization AbstractMessageFactory class.
 * </pre>
 */
public abstract class AbstractMessageFactory {
        /* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * Create reply message from the given mail header DTO, and list of objects.
	 * 
	 * @param headerDTO MailHeaderDTO - the given mail header DTO.
	 * @param objects Object[] - the list of objects.
	 * @return the message composer.
	 * @throws MailException when error occurs during creating reply message.
	 */
	public abstract IMessageComposer reply(MailHeaderDTO headerDTO, Object...objects) throws MailException ;

	/**
	 * Create reply message from the given mail message composer the original message and the
	 * flag that marks reply all.
	 * 
	 * @param composer the given {@link IMessageComposer} instance.
	 * @param originalMsg the given original message.
	 * @param replyAll the flag that mark reply all or normal reply.
	 * 
	 * @return the reply {@link IMessageComposer} instance.
	 * @throws MailException when error occurs during creating reply message.
	 */
	public abstract IMessageComposer reply(IMessageComposer composer, String originalMsg, boolean replyAll) throws MailException ;
	
	/**
	 * Create forward message from the given mail composer.
	 * 
	 * @param composer the given {@link IMessageComposer} instance.
	 * @param forwardMsg the given forward message.
	 * 
	 * @return the reply {@link IMessageComposer} instance.
	 * @throws MailException when error occurs during creating forward message.
	 */
	public abstract IMessageComposer forward(IMessageComposer composer, String forwardMsg) throws MailException ;	
	
	/**
	 * Create AbstractMessageFactory from the given factory.
	 * 
	 * @return the AbstractMessageFactory that corresponding with mail factory.
	 * @throws MailException if an error occurs during getting instance.
	 */
	public static AbstractMessageFactory getInstance() throws MailException{
		// does not running under security.
		if(System.getSecurityManager() == null){
			return AbstractMessageFactory.createInstace() ;
		}
		
		// get instance.
		return AccessController.doPrivileged(new PrivilegedAction<AbstractMessageFactory>(){
			/**
			 * @see java.security.PrivilegedAction#run()
			 */
			public AbstractMessageFactory run(){
				return AbstractMessageFactory.createInstace();
			}
		}) ;
	}
	
	//-----------------------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Create a new <tt>AbstractMessageFactory</tt> instance.
	 * 
	 * @return the AbstractMessageFactory that corresponding with mail factory.
	 * 
	 * @throws MailException if an error occurs during getting instance.
	 */
	private static AbstractMessageFactory createInstace() throws MailException{
		// get bean definition.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		if(context == null || context.getContext() == null){
			throw new MailException("The WebOSApplicationContext must be set.") ;
		}
		
		// get bean definition.
		MailFactoryBeanDefinition definition = context.getContext().getBean(
						MailFactoryBeanDefinition.MAIL_FACTORY_BEAN_DEFINITION_NAME,
						MailFactoryBeanDefinition.class
					) ;
		
		if(definition == null){
			throw new MailException("The MailFactoryBeanDefinition must be set.") ;
		}
				
		try {
			// get message factory class.			
			Class<AbstractMessageFactory> msgFactory = definition.createMessageFactory() ;
			
			// message is null.
			if(msgFactory == null) throw new MailException("Could not create message factory.") ;
			
			// create message factory class.
			return msgFactory.newInstance() ;
		} catch (InstantiationException iex) {
			// throw exception.
			throw new MailException(iex.getMessage(), iex) ;
		} catch (IllegalAccessException iaex) {
			// throw exception.
			throw new MailException(iaex.getMessage(), iaex) ;
		}
	}
}
