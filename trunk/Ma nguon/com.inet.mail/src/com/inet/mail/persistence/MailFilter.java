/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.mail.data.MailFilterClause;

/**
 * MailFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 *  Initialization MailFilter bean.
 * </pre>
 */
@Entity
@Table(name="MAIL_FILTER")
@NamedQueries(value={
	@NamedQuery(name="MailFilter.findByMailFolder",query="SELECT m FROM MailFilter m WHERE m.folder.id=:id"),
	@NamedQuery(name="MailFilter.findAll", query="SELECT m FROM MailFilter m"),
	@NamedQuery(name="MailFilter.findByUser", query="SELECT m FROM MailFilter m WHERE m.folder.owner =:owner")
})
public class MailFilter extends BasePersistence<Long> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5568328255931914606L;

	// the filter name.
	private String name ;
	// the filter clause data.
	private byte[] data ;
	// the mail folder.
	private MailFolder folder ;
	
	//-----------------------------------------------------------------------
	// Class members.
	//
	/**
	 * Create MailFilter instance.
	 */
	public MailFilter(){}
	
	/**
	 * Create MailFilter instance.
	 * 
	 * @param name String - the mail filter name.
	 * @param clause MailFilterClause - the mail filter clause.
	 * @param folder MailFolder - the given mail folder.
	 */
	public MailFilter(
			String name, 
			MailFilterClause clause, 
			MailFolder folder){
		this.name = name ;
		
		// convert clause to byte object.
		if(clause != null){
			this.data = clause.toBytes() ;
		}
		
		this.folder = folder ;
	}
	
	/**
	 * @return the mail filter identifier.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	/**
	 * @return the filter name.
	 */
	@Column(name="mail_filter_name", nullable=false)
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the filter name.
	 * 
	 * @param name String - the given filter name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the filter clause.
	 */
	@Column(name="mail_filter_clause", nullable=false, length=16777216)
	@Lob
	public byte[] getData() {
		return this.data;
	}
	
	/**
	 * Set the filter clause.
	 * 
	 * @param data byte[] - the given filter clause data.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	/**
	 * @return the filter clause.
	 */
	@Transient
	public MailFilterClause getClause() {
		return (this.data == null ? null : MailFilterClause.convertTo(this.data)) ;
	}
	
	/**
	 * Set the mail filter clause.
	 * 
	 * @param clause MailFilterClause - the given mail filter clause.
	 */
	public void setClause(MailFilterClause clause) {
		// convert to filter clause data.
		this.data = clause.toBytes() ;
	}
	
	/**
	 * @return the mail filter folder.
	 */
	@ManyToOne
	@JoinColumn(name="mail_folder_filter", nullable=true)
	public MailFolder getFolder() {
		return this.folder;
	}
	
	/**
	 * Set the mail folder to filter.
	 * 
	 * @param folder MailFolder - the given mail folder.
	 */
	public void setFolder(MailFolder folder) {
		this.folder = folder;
	}
}
