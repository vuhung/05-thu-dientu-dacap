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

import java.util.List;

/**
 * ClientChagePwdData.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class ClientChagePwdData {
	private String userCode;
	private List<String> emails;
	private String password;
	
	/**
	 * Create <tt>ClientChagePwdData</tt> instance
	 * 
	 * @param userCode String - the user code
	 * @param emails List<String> emails - the list of email addresses
	 * @param password String - the new password
	 */
	public ClientChagePwdData(String userCode, List<String> emails,
			String password) {
		this.userCode = userCode;
		this.emails = emails;
		this.password = password;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the emails
	 */
	public List<String> getEmails() {
		return this.emails;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
