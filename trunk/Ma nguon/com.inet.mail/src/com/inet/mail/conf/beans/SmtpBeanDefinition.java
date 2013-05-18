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
package com.inet.mail.conf.beans;

import com.inet.base.service.StringService;
import com.inet.mail.data.MailSecurity;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * SmtpBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SmtpBeanDefinition.java Dec 7, 2008 nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization SmtpBeanDefinition class.
 * </pre>
 */
public class SmtpBeanDefinition extends AbstractBeanDefinition {
	//~ Static fields =========================================================
	/* SMTP definition name. */
	public static final String SMTP_BEAN_DEFINITION_NAME = SmtpBeanDefinition.class.getName() ;
	
	//~ Instance fields =======================================================
	/* SMTP host name. */
	private String host ;
	
	/* SMTP port number. */
	private int port = 25;
	
	/* SMTP security. */
	private MailSecurity security = MailSecurity.NONE;
	
	/* SMTP user name. */
	private String username ;
	
	/* SMTP password. */
	private String password ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return SMTP_BEAN_DEFINITION_NAME;
	}

	/**
	 * @return the STMP host name.
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Set the SMTP host name.
	 * 
	 * @param host the given SMTP host name.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return the SMTP port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Set the SMTP port.
	 * 
	 * @param port the SMTP port.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return the SMTP {@link MailSecurity}
	 */
	public MailSecurity getSecurity() {
		return security;
	}
	
	/**
	 * Set the SMTP {@link MailSecurity}.
	 * 
	 * @param security the given SMTP {@link MailSecurity}.
	 */
	public void setSecurity(MailSecurity security) {
		this.security = security;
	}
	
	/**
	 * Set the SMTP password.
	 * 
	 * @param password the given SMTP password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the SMTP password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return the SMTP user name.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set the SMTP user name.
	 * 
	 * @param username the given SMTP user name.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder() ;
		
		// prepare SMTP configuration.
		builder.append(this.getClass().getName()).append("[")
			   .append("Host: ").append(host)
			   .append("; Port: ").append(port)
			   .append("; Security: ").append(security.toString()) ;
	
		if(StringService.hasLength(username)){
			builder.append("; Username: ").append(username)
				   .append("; Password: {PROTECTED}") ;
		}
		
		builder.append("]") ;
		
		// the SMTP data.
		return builder.toString();
	}
}
