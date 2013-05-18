/*****************************************************************
   Copyright 2006 by Tung Luong (lqtung@truthinet.com.vn)

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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.base.service.StringService;

/**
 * FollowUp.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version 0.2i
 */
@Entity
@Table(name="FOLLOW_UP")
@NamedQueries(value={
	@NamedQuery(name="FollowUp.loadAll", query="SELECT NEW com.inet.mail.data.FollowUpDTO(fu.id, fu.name, fu.date, fu.mail.id, fu.mail.flag) FROM FollowUp fu WHERE fu.userCode = :userCode ORDER BY fu.date"),
	@NamedQuery(name="FollowUp.deleteByData", query="DELETE FROM FollowUp fu WHERE fu.mail.id = :id"),
	@NamedQuery(name="FollowUp.deleteByUID", query="DELETE FROM FollowUp fu WHERE fu.mail.id IN (SELECT NEW java.lang.Long(hd.id) FROM com.inet.mail.persistence.MailHeader hd WHERE hd.uid IN (:uids))")
})
public class FollowUp extends BasePersistence<Long>{
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//// Declare Variable 
	//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3942706923011805773L;

	//The given code of author
	private String userCode = StringService.EMPTY_STRING;
	
	//the given name of follow up task
	private String name = StringService.EMPTY_STRING;
	
	//The given date will follow-up
	//TODAY,TOMORROW, THIS_WEEK, NEEXT_WEEK, NODATE=NULL,
	private Date			date = new Date();
	
	private MailHeader mail; 
	
	/************************************************************************
	 * Empty Constructor
	 ************************************************************************/
	public FollowUp(){}

	
	/**
	 * Constructor
	 * 
	 * @param userCode	: the given user code
	 * @param name		: the given name of task will follow up
	 */
	public FollowUp(String userCode, String name) {
		this.userCode 	= userCode;
		this.name 		= name;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//// Declare Method 
	//////////////////////////////////////////////////////////////////////////////////////////////////
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
	@Column(name="mail_follow_up_owner",length=64)
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
	 * @return the name
	 */
	@Column(name="mail_follow_up_name")
	public String getName() {
		return this.name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the date
	 */
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="mail_follow_up_date")
	public Date getDate() {
		return this.date;
	}


	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne(cascade={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinColumn(name="mail_follow_up_mail_header", nullable=false)
	public MailHeader getMail() {
		return mail;
	}

	public void setMail(MailHeader mail) {
		this.mail = mail;
	}
}
