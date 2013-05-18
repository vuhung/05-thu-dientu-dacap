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
package com.inet.mail;

import com.inet.base.ejb.ServiceLocator;
import com.inet.base.ejb.ServiceLocatorManager;
import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;

public class AbstractConfigureTestMail extends AbstractTestMail<MailAcctConfigInfoRemoteSL>{

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws java.lang.Exception{
		ServiceLocator locator = ServiceLocatorManager.getInstance().getServiceLocator(this.getProperties());
		bean = locator.getRemoteBean("MailAcctConfigInfoSLBean", MailAcctConfigInfoRemoteSL.class);
	}
}
