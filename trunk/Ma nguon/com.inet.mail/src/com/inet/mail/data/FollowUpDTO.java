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
package com.inet.mail.data;

import java.io.Serializable;
import java.util.Date;

import com.inet.base.service.StringService;
import com.inet.mail.persistence.FollowUp;
import com.inet.mail.persistence.MailHeader;

public class FollowUpDTO implements Serializable{

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -4438866612190696392L;

	private long id = 0L;
	//the given name of follow up task
	private String name = StringService.EMPTY_STRING;
	
	//The given date will follow-up
	//TODAY,TOMORROW, THIS_WEEK, NEEXT_WEEK, NODATE=NULL,
	private Date			date = new Date();
	
	private long data = 0L;
	
	private MailFlag flag = MailFlag.NOTHING ;

	/**
	 * constructor FollowUpDTO
	 * @param id
	 * @param name
	 * @param date
	 * @param data
	 * @param flag
	 */
	public FollowUpDTO(long id, String name, Date date, long data, MailFlag flag ){
		this.id = id;
		this.name = name;
		this.date = date;
		this.data = data;
		this.flag = flag;
	}
	
	public FollowUpDTO(FollowUp followUp, MailHeader header){
		this.id = followUp.getId();
		this.name = followUp.getName();
		this.date = followUp.getDate();
		this.data = header.getId();
		this.flag = header.getFlag();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public MailFlag getFlag() {
		return flag;
	}

	public void setFlag(MailFlag flag) {
		this.flag = flag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	} 
}
