/*****************************************************************
 Copyright 2007 by Hien Nguyen (hiennguyen@truthinet.com)

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.mail.data.TemplateFormat;
import com.inet.mail.data.TemplateStatus;
import com.inet.mail.data.TemplateType;

/**
 * TemplateDetailInfo.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Entity
@Table(name = "MAIL_TEMPLATE_DETAIL_INFO")
@NamedQueries(value = { 
		@NamedQuery(name = "TemplateDetailInfo.queryAllByStatus", query = "FROM TemplateDetailInfo t WHERE t.status = :status"),
		@NamedQuery(name = "TemplateDetailInfo.queryByOwnerStatus", query = "FROM TemplateDetailInfo t WHERE t.owner = :owner AND t.status = :status"),
		@NamedQuery(name = "TemplateDetailInfo.queryByOwner", query = "FROM TemplateDetailInfo t WHERE t.owner = :owner"),
		@NamedQuery(name = "TemplateDetailInfo.queryByTypeStatus", query = "FROM TemplateDetailInfo t WHERE t.type = :type AND t.status = :status"),
		@NamedQuery(name = "TemplateDetailInfo.queryByType", query = "FROM TemplateDetailInfo t WHERE t.type = :type")
})
public class TemplateDetailInfo extends BasePersistence<Long> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -300528409492883400L;
	
	private String name;
	private String owner;
	private TemplateFormat format = TemplateFormat.TEXT; 
	private TemplateType type = TemplateType.SYSTEM;
	private Date created; // the created this template
	private TemplateStatus status = TemplateStatus.CREATED;
	private String template;
	
	
	public TemplateDetailInfo()
	{	
		id = 0L;
		created = new Date();
	}
	
	/**
	 * @return int.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@Column(name="mail_template_format", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public TemplateFormat getFormat() {
		return this.format;
	}

	public void setFormat(TemplateFormat format) {
		this.format = format;
	}

	@Column(length = 128, name="mail_template_name", nullable=false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	@Column(name="mail_template_type", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public TemplateType getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TemplateType type) {
		this.type = type;
	}
	
	@Column(name="mail_template_owner", length=64, nullable=true)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(length=5120, name="mail_template_content")
	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name="mail_template_create")
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name="mail_template_status", nullable=false)
	@Enumerated(value=EnumType.STRING)
	public TemplateStatus getStatus() {
		return this.status;
	}

	public void setStatus(TemplateStatus status) {
		this.status = status;
	}
}
