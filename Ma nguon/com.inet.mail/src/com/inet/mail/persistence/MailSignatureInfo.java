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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.base.service.CommonService;

/**
 * 
 * MailSignatureInfo.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Entity
@Table(name="MAIL_SIGNATURE")
@NamedQueries(
	value={
		@NamedQuery(name="MailSignatureInfo.findByUser", query="FROM MailSignatureInfo m WHERE m.owner=:owner")	
	}
)
public class MailSignatureInfo extends BasePersistence<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1344014896396288298L;

	// the mail configure owner.
	private String owner;
	private String name;
	private String content;
	private char currentUsed = CommonService.NO;
	
	public MailSignatureInfo()
	{
		id = 0L;	
		currentUsed = CommonService.NO;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	/**
	 * @return the mail configuration owner.
	 */
	@Column(name="mail_signature_owner", length=42, nullable=false)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name="mail_signature_name", length=64, nullable=false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="mail_signature_data", length=1024, nullable=false)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="mail_signature_default", nullable=false)
	public char getCurrentUsed() {
		return this.currentUsed;
	}

	public void setCurrentUsed(char currentUsed) {
		this.currentUsed = currentUsed;
	}	
}
