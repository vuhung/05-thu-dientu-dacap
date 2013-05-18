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
package com.inet.mail;

import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.conf.PersonMailConfig.ConfigDTO;
import com.inet.mail.conf.beans.SmtpBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.AbstractMessageFactory;

/**
 * MailConfigureFactory
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailConfigureFactory.java 2008-12-11 22:11:08Z nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization MailConfigureFactory class.
 * </pre>
 */
public abstract class MailConfigureFactory {
	//~ Static instance =======================================================
	/* the mail factory bean definition. */
	private static SmtpBeanDefinition smtpBeanDefinition;
	
	/* the thread local. */
	private static ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>() ;
	
	//~ Methods ===============================================================
	
	/**
	 * Create factory from the given mail configuration.
	 * 
	 * @param configure the given {@link PersonMailConfig} instance
	 * @return the {@link AbstractMailFactory} instance.
	 * 
	 * @throws MailException when error occurs during creating mail factory.
	 */
	public static AbstractMailFactory createFactory(PersonMailConfig configure)
			throws MailException {
		// configuration must be set.
		if (configure == null) throw new MailException("The given mail configuration is null.");
		
		// create mail factory.
		return createFactory(configure,configure.getDefaultSMTP());
	}

	/**
	 * Create factory from the given mail configuration.
	 * 
	 * @param configure the given {@link PersonMailConfig} instance.
	 * @param smtp the given STMP default.
	 * @return the {@link AbstractMailFactory} instance.
	 * 
	 * @throws MailException when error occurs during creating mail factory.
	 */
	public static AbstractMailFactory createFactory(PersonMailConfig configure, String smtp)
			throws MailException {
		// configuration must be set.
		if (configure == null) throw new MailException("The given mail configuration is null.");
		
		// get configuration DTO.
		ConfigDTO smtpDefault = configure.getSMTPAccount(smtp);
		
		// create mail factory.
		return MailConfigureFactory.createFactory(
				smtpDefault.getSecurityMode(), 
				smtpDefault.getServerName(), 
				smtpDefault.getServerPort()
			);
	}
	
	/**
	 * Create mail factory from the given mail configuration.
	 * 
	 * @return the {@link AbstractMailFactory} instance.
	 * @throws MailException if an error occurs during creating mail factory.
	 */
	public static AbstractMailFactory createFactory() throws MailException {
		// get SMTP configuration.
		SmtpBeanDefinition definition = getSmtpBeanDefinition() ;
		
		// return mail configuration.
		return MailConfigureFactory.createFactory(
				definition.getSecurity(),
				definition.getHost(), 
				definition.getPort(),
				definition.getUsername(),
				definition.getPassword()
			);
	}

	/**
	 * Create abstract mail factory from the given data.
	 * 
	 * @param security the given mail security.
	 * @param host the given host.
	 * @param port the given port.
	 * @return the {@link AbstractMailFactory} instance.
	 * 
	 * @throws MailException when error occurs during creating mail factory.
	 */
	public static AbstractMailFactory createFactory(MailSecurity security, String host, int port)
			throws MailException {
		// create mail factory from the given factory.
		return AbstractMailFactory.getInstance(security, host, port);
	}
	
	/**
	 * Create abstract mail factory from the given data.
	 * 
	 * @param security the given mail security.
	 * @param host the given host.
	 * @param port the given port.
	 * @param username the given SMTP user name.
	 * @param password the given SMTP password.
	 * 
	 * @return the {@link AbstractMailFactory} instance.
	 * @throws MailException when error occurs during creating mail factory.
	 */
	public static AbstractMailFactory createFactory(MailSecurity security,
			String host, int port, String username, String password) throws MailException{
		// create mail factory from the given factory.
		AbstractMailFactory mailFactory = AbstractMailFactory.getInstance(
					security,
					host,
					port,
					username,
					password
				) ;
		
		// connect to mail server.
		if(mailFactory.connect()){
			return mailFactory ;
		}else{
			throw new MailException("Could not connect to the mail server") ;
		}
	}	

	/**
	 * Create MessageFactory from the given default configuration.
	 * 
	 * @return the message factory.
	 * @throws MailException when error occurs during creating message factory.
	 */
	public static AbstractMessageFactory createMessageFactory()
			throws MailException {
		return AbstractMessageFactory.getInstance();
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
