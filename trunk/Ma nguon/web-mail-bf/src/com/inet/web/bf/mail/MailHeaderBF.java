/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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

import com.inet.mail.business.sr.MailHeaderRemoteSL;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.facade.AbstractEJBFacade;

/**
 * MailHeaderBF.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailHeaderBF extends AbstractEJBFacade<MailHeaderRemoteSL> {
	/**
	 * mail filter bean name
	 */
	private static final String MAIL_HEADER_BEAN_NAME = "MailHeaderSLBean";
	
	/**
	 * @param provider AbstractApplicationServerProvider - the given application 
	 * server provider instance.
	 */
	public MailHeaderBF(AbstractApplicationServerProvider provider) {
		super(provider, MAIL_HEADER_BEAN_NAME, BEAN_SCOPE_REMOTE);
	}
}
