/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.web.bf.config;

import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.facade.AbstractEJBFacade;

/**
 * MailAcctConfigInfoBF.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailAcctConfigInfoBF extends AbstractEJBFacade<MailAcctConfigInfoRemoteSL> {
	// The mail account configuration bean name
	private static final String MAIL_ACCOUNT_CONFIG_BEAN_NAME = "MailAcctConfigInfoSLBean";

	/**
	 * Create <tt>MailAcctConfigInfoBF</tt> instance
	 * 
	 * @param provider AbstractApplicationServerProvider- the given provider
	 */
	public MailAcctConfigInfoBF(AbstractApplicationServerProvider provider) {
		super(provider, MAIL_ACCOUNT_CONFIG_BEAN_NAME, BEAN_SCOPE_REMOTE);
	}

	
}
