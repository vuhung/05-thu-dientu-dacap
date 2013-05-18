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
package com.inet.mail.conf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import com.inet.base.service.CommonService;
import com.inet.base.service.StringService;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.data.MailReceiverProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.util.MailService;

/**
 * 
 * PersonMailConfig.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class PersonMailConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4638464890764078839L;

	private String fullname;
	private int refresh;
	private String digitalAlgorithm;
	private char multipart = CommonService.NO;
	private String defaultSMTP;
	private HashMap<String,ConfigDTO[]> accounts = new HashMap<String,ConfigDTO[]>();

	// load all default folderIDS
	private Hashtable<FolderType, Long> folders = new Hashtable<FolderType, Long>();
	
	/**
	 * 
	 * @param fullname
	 * @param refresh
	 * @param digitalAlgorithm
	 * @param multipart
	 */
	public PersonMailConfig(MailAcctConfigInfo config) {
		this.fullname = config.getFullname();
		this.refresh = config.getRefresh();
		this.digitalAlgorithm = config.getDigitalAlgorithm();
		this.multipart = config.getMultipart();
		this.defaultSMTP = config.getDefaultSMTP();
		
		this.parserConfig(config.getReceiverObject());
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public String getDefaultSMTP() {
		return this.defaultSMTP;
	}

	public void setDefaultSMTP(String defaultSMTP) {
		this.defaultSMTP = defaultSMTP;
	}

	public int getRefresh() {
		return this.refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public String getDigitalAlgorithm() {
		return this.digitalAlgorithm;
	}

	public void setDigitalAlgorithm(String digitalAlgorithm) {
		this.digitalAlgorithm = digitalAlgorithm;
	}

	public char getMultipart() {
		return this.multipart;
	}

	public void setMultipart(char multipart) {
		this.multipart = multipart;
	}

	/**
	 * 
	 * @param configs
	 */
	private void parserConfig(MailReceiverDTO configs) {
		for (MailReceiverObject config : configs.getAccounts()) {
			String serverName = config.getServerName();
			String accountName = config.getAccountName();
			String accountPass = config.getAccountPassword();

			// create pop configure information
			ConfigDTO proto = new ConfigDTO(config.getProtocol(), 
					serverName,
					config.getServerPort(), 
					accountName, 
					accountPass, 
					config.getSecurity());

			// create SMTP configure
			String serverSMTP = config.getSMTPServerName();
			String accountSMTP = config.getSMTPAccountName();
			String passwordSMTP = config.getSMTPAccountPassword();
			
			ConfigDTO smtp = new ConfigDTO(MailReceiverProtocol.SMTP, 
					StringService.hasLength(serverSMTP) ? serverSMTP : serverName, 
					config.getSMTPServerPort(), 
					StringService.hasLength(accountSMTP) ? accountSMTP : accountName, 
					StringService.hasLength(passwordSMTP) ? passwordSMTP : accountPass,
					config.getSMTPSecurity());

			accounts.put(MailService.createTicket(serverName, accountName) ,new ConfigDTO[] { proto, smtp });
		}
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String,ConfigDTO[]> getAccounts()
	{
		return accounts;
	}
	
	public Hashtable<FolderType, Long> getFolders() {
		return this.folders;
	}

	public void setFolders(Hashtable<FolderType, Long> folders) {
		this.folders = folders;
	}
	
	/**
	 * 
	 * @param accountName
	 * @return
	 */
	public ConfigDTO getSMTPAccount(String accountName)
	{
		if (accounts.containsKey(accountName))
			return accounts.get(accountName)[1];
		return null;
	}
	
	/**
	 * 
	 * ConfigDTO.
	 * 
	 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
	 * @version 0.2i
	 */
	public class ConfigDTO implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2689980409786918630L;
		
		private MailReceiverProtocol protocol;
		private String serverName;
		private int serverPort;
		private String accountName;
		private String accountPassword;
		private MailSecurity securityMode;

		public ConfigDTO(MailReceiverProtocol protocol, String serverName,
				int serverPort, String accountName, String accountPassword,
				MailSecurity securityMode) {
			this.protocol = protocol;
			this.serverName = serverName;
			this.serverPort = serverPort;
			this.accountName = accountName;
			this.accountPassword = StringService.hasLength(accountPassword) ? MailService.decrypt(accountName, accountPassword) : accountPassword;
			this.securityMode = securityMode;
		}

		public MailReceiverProtocol getProtocol() {
			return this.protocol;
		}

		public void setProtocol(MailReceiverProtocol protocol) {
			this.protocol = protocol;
		}

		public String getServerName() {
			return this.serverName;
		}

		public void setServerName(String serverName) {
			this.serverName = serverName;
		}

		public int getServerPort() {
			return this.serverPort;
		}

		public void setServerPort(int serverPort) {
			this.serverPort = serverPort;
		}

		public String getAccountName() {
			return this.accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountPassword() {
			return this.accountPassword;
		}

		public void setAccountPassword(String accountPassword) {
			this.accountPassword = accountPassword;
		}

		public MailSecurity getSecurityMode() {
			return this.securityMode;
		}

		public void setSecurityMode(MailSecurity securityMode) {
			this.securityMode = securityMode;
		}
	}
}
