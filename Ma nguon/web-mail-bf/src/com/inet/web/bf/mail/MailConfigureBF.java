/*****************************************************************
   Copyright 2007 by Truong Ngoc Tan (tntan@truthinet.com)

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
package com.inet.web.bf.mail;

import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.facade.AbstractEJBFacade;

/**
 * MailConfigureBF.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailConfigureBF extends AbstractEJBFacade<MailAcctConfigInfoRemoteSL> {
	
	/**
	 * mail configure bean name
	 */
	private static final String MAIL_CONFIG_BEAN_NAME = "MailAcctConfigInfoSLBean";
	
	/**
	 * @param provider
	 */
	public MailConfigureBF(AbstractApplicationServerProvider provider) {
		super(provider, MAIL_CONFIG_BEAN_NAME, BEAN_SCOPE_REMOTE);
	}
}
