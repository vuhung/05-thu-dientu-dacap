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
package com.inet.web.service.conf;

import com.inet.base.service.StringService;
import com.inet.web.bo.config.MailAcctConfigInfoBO;
import com.inet.web.exception.WebOSException;

/**
 * MailClientChangePwdService.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailClientConfChangePwdService implements HandleChangePwdMailService {
	private MailAcctConfigInfoBO mailAcctConfigInfoBO;
	
	/**
	 * Create <tt>MailClientChangePwdService</tt> instance
	 * 
	 * @param mailAcctConfigInfoBO MailAcctConfigInfoBO - the mail account configure BO
	 */
	public MailClientConfChangePwdService(MailAcctConfigInfoBO mailAcctConfigInfoBO) {
		this.mailAcctConfigInfoBO = mailAcctConfigInfoBO;
	}

	/**
	 * @see com.inet.web.service.HandleService#execute(java.lang.Object)
	 */
	public void execute(ClientChagePwdData data) throws WebOSException {
		if(data == null || data.getEmails() == null
				 || !StringService.hasLength(data.getUserCode()) 
				 || !StringService.hasLength(data.getPassword()))	return;
		// change password in mail configure
		this.mailAcctConfigInfoBO.changePassword(
				data.getUserCode(),	data.getEmails(), data.getPassword());

	}

}
