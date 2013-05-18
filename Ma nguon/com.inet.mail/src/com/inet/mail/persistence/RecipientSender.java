/*****************************************************************
   Copyright 2009 by Truong Ngoc Tan (tntan@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

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

/**
 * RecipientSender.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 0.2i
 */
@Entity
@Table(name="RECIPIENT_SENDER")
@NamedQueries(value={
	@NamedQuery(name="RecipientSender.findByOwner", query="SELECT r FROM RecipientSender r WHERE r.owner=:owner"),
	@NamedQuery(name="RecipientSender.findByEmails", query="SELECT r FROM RecipientSender r WHERE r.owner=:owner AND r.email IN (:emails)"),
	@NamedQuery(name="RecipientSender.findByEmail", query="SELECT r FROM RecipientSender r WHERE r.owner=:owner AND r.email=:email")
})
public class RecipientSender extends BasePersistence<Long>{
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//// Declare Variable 
	//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8248998333779005581L;

	private String owner;
	private String recipient;
	private String email;

	/************************************************************************
	 * Empty Constructor
	 ************************************************************************/
	public RecipientSender(){}
	
	public RecipientSender(String owner, String recipient){
		this.owner = owner;
		this.recipient = recipient; 
	}
	/**
	 * Generate identifier
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	/************************************************************************
	 * Common information
	 ************************************************************************/
	/**
	 * @return the userCode
	 */
	@Column(name="owner",length=64)
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the user code to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * @return the recipient
	 */
	@Column(length=64)
	public String getRecipient() {
		return recipient;
	}
	
	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	/**
	 * @return the email address
	 */
	@Column(length=48)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
