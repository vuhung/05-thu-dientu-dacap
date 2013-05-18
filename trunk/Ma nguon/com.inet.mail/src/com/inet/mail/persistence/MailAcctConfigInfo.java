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
package com.inet.mail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.base.service.CommonService;
import com.inet.mail.data.MailReceiverDTO;

/**
 * 
 * MailAcctConfigInfo.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Entity
@Table(name="MAIL_ACCOUNT_CONFIGURE_INFO")
@NamedQueries(
	value={
		@NamedQuery(name="MailAcctConfigInfo.findByUser", query="FROM MailAcctConfigInfo m WHERE m.owner=:owner")	
	}
)
public class MailAcctConfigInfo extends BasePersistence<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5085988148115916686L;
	// the mail configure owner.
	private String owner;
	// this is general information
	private String fullname;
	private String defaultSMTP;
	// the mail refresh time (s).
	private int refresh;
	// digital algorithm (using encrypt/descrypt mail content).
	private String digitalAlgorithm;
	// support multipart.
	private char multipart = CommonService.NO;
	// the receiver mail.
	private byte[] receiverServer ;
	// the receiver object.
	private MailReceiverDTO receiverObject;
	
	public MailAcctConfigInfo()
	{
		id = 0L;		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	@Column(name="mail_configure_owner", length=42, nullable=false)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the fullname
	 */
	@Column(name="mail_configure_acc_fullname", length=64, nullable=true)
	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Column(name="mail_configure_smtp_default", length=128, nullable=false)
	public String getDefaultSMTP() {
		return this.defaultSMTP;
	}

	public void setDefaultSMTP(String defaultSMTP) {
		this.defaultSMTP = defaultSMTP;
	}

	/**
	 * @return the refresh time.
	 */
	@Column(name="mail_configure_refresh")
	public int getRefresh() {
		return this.refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	/**
	 * @return the mail digital algorithm
	 */
	@Column(name="mail_configure_digital_alg", length=255, nullable=true)
	public String getDigitalAlgorithm() {
		return this.digitalAlgorithm;
	}

	public void setDigitalAlgorithm(String digitalAlgorithm) {
		this.digitalAlgorithm = digitalAlgorithm;
	}

	/**
	 * @return the mail multi-part supported.
	 */
	@Column(name="mail_configure_multipart", length=1, nullable=false)
	public char getMultipart() {
		return this.multipart;
	}

	public void setMultipart(char multipart) {
		this.multipart = multipart;
	}	
	
	/**
	 * @return the receiver server data.
	 */
	@Column(name="mail_configure_receiver", length=4194304, nullable=true)
	@Lob
	public byte[] getReceiverServer() {
		return this.receiverServer;
	}
	
	/**
	 * Set the given receiver data.
	 * 
	 * @param receiverServer byte[] - the given receiver server data.
	 */
	public void setReceiverServer(byte[] receiverServer) {
		this.receiverServer = receiverServer;
	}
	
	/**
	 * @return the receiver server object.
	 */
	@Transient
	public MailReceiverDTO getReceiverObject() {
		return this.receiverObject;
	}
	
	/**
	 * Set mail receiver server object.
	 * 
	 * @param receiverObject MailReceiverDTO - the given mail receiver object.
	 */
	public void setReceiverObject(MailReceiverDTO receiverObject) {
		this.receiverObject = receiverObject;
		
		if(this.receiverObject != null){
			this.receiverServer = this.receiverObject.getData() ;
		}
	}
}
