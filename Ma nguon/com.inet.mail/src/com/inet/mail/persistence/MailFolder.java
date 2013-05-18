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

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.inet.base.ejb.persistence.BasePersistence;
import com.inet.mail.data.FolderType;

/**
 * MailFolder
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 * 	Initialization mail folder persistent.
 * </pre>
 */
@Entity
@Table(name="MAIL_FOLDER")
@NamedQueries(value={
		@NamedQuery(name = "MailFolder.countDefaultByUser", query = "SELECT count(*) FROM MailFolder o WHERE o.owner = :owner AND o.parentId < 0"),
		@NamedQuery(name = "MailFolder.queryDefaultByUser", query = "FROM MailFolder o WHERE o.owner = :owner AND o.parentId < 0"),
		@NamedQuery(name = "MailFolder.queryDefaultFolder", query = "FROM MailFolder o WHERE o.owner = :owner AND o.parentId = :parentId"),
		
		@NamedQuery(name="MailFolder.findByUser", query="SELECT f FROM MailFolder f WHERE f.owner=:owner"),
		@NamedQuery(name="MailFolder.findByType", query="SELECT f FROM MailFolder f WHERE f.type=:type AND f.owner=:owner"),
		@NamedQuery(name="MailFolder.findById", query="SELECT f FROM MailFolder f WHERE f.owner=:owner AND f.id=:id"),
		@NamedQuery(name="MailFolder.findByParentId", query="SELECT f FROM MailFolder f WHERE f.parentId=:parentId AND f.owner=:owner"),
		@NamedQuery(name="MailFolder.findRootByAndUser", query="SELECT f FROM MailFolder f WHERE f.parentId=0 AND f.owner=:owner")
})
public class MailFolder extends BasePersistence<Long> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7826764943650101399L;

	// folder parent identifier.
	private long parentId = 0;
	// folder type, INBOX/OUTBOX/SENT/...
	private FolderType type ;
	// the user code.
	private String owner ;
	// mail filter.
	private Collection<MailFilter> filters ;
	// folder name.
	private String name ;
	
	/**
	 * Create MailFolder instance.
	 */
	public MailFolder(){
		id = 0L;
		parentId = 0L;
	}
	
	/**
	 * Create MailFolder instance.
	 * 
	 * @param name String - the given folder name.
	 * @param type FolderType - the given folder type.
	 * @param owner String - the given folder owner code.
	 */
	public MailFolder(String name, FolderType type, String owner){
		this(0L, name, type, owner) ;
	}
	
	/**
	 * Create MailFolder instance.
	 * 
	 * @param parentId long - the given folder's parent identifier.
	 * @param name String - the given folder name.
	 * @param type FolderType - the given folder type.
	 * @param owner String - the given folder owner.
	 */
	public MailFolder(long parentId, String name, FolderType type, String owner){
		this.parentId = parentId ;
		this.name = name ;
		this.type = type ;
		this.owner = owner ;
	}
	
	/**
	 * @return the mail folder identifier.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId(){
		return this.id ;
	}
	
	/**
	 * @return the folder's parent identifier.
	 */
	@Column(name="mail_folder_parent_id")
	public long getParentId() {
		return this.parentId;
	}
	
	/**
	 * Set the folder's parent identifier.
	 * 
	 * @param parentId long - the given parent identifier to be set.
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * @return the folder name.
	 */
	@Column(name="mail_folder_name", length=255, nullable=false)
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the folder name.
	 * 
	 * @param name String - the given folder name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the folder owner.
	 */
	@Column(name="mail_folder_owner", length=42, nullable=false)
	public String getOwner() {
		return this.owner;
	}
	
	/**
	 * Set the given mail folder owner.
	 * 
	 * @param owner String - the given mail folder owner.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * @return the folder type such as INBOX/OUTBOX/SENT
	 */
	@Column(name="mail_folder_type")
	@Enumerated(value=EnumType.STRING)
	public FolderType getType() {
		return this.type;
	}
	
	/**
	 * Set the folder type.
	 * 
	 * @param type FolderType - the given folder type.
	 */
	public void setType(FolderType type) {
		this.type = type;
	}
	
	/**
	 * @return the list of mail folder filters.
	 */
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="folder")
	@Column(nullable=true)
	public Collection<MailFilter> getFilters() {
		return this.filters;
	}
	
	/**
	 * Set the mail folder filters.
	 * 
	 * @param filters Collection<MailFilter> - the given list of folder filters.
	 */
	public void setFilters(Collection<MailFilter> filters) {
		this.filters = filters;
	}
}
