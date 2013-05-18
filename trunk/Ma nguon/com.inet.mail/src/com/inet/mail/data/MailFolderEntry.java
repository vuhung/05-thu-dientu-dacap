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
package com.inet.mail.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.inet.base.service.CompareService;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailFolder;

/**
 * MailFolderEntry
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 13, 2008
 * <pre>
 *  Initialization MailFolderEntry class.
 * </pre>
 */
public class MailFolderEntry implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8987641896484924185L;

	// folder parent.
	MailFolderEntry parent ;
	// folder instance.
	MailFolder folder ;
	// mail children entry.
	List<MailFolderEntry> children;
	// folder name.
	String name ;
	// the number of unread email.
	long unread = 0;
	// the total email on current folder.
	long total = 0;
	
	/**
	 * Create mail folder entry instance.
	 * 
	 * @param folder MailFolder - the given mail folder instance.
	 */
	public MailFolderEntry(MailFolder folder){
		this(null, folder) ;
	}
	
	/**
	 * Create mail folder entry instance.
	 * 
	 * @param parent MailFolderEntry - the given folder parent.
	 * @param folder MailFolder - the given mail folder.
	 */
	public MailFolderEntry(MailFolderEntry parent, MailFolder folder){
		this.parent = parent ;
		this.folder = folder ;
		
		// set folder name.
		this.name = this.folder.getName() ;
	}
	
	/**
	 * Create mail folder entry from the given mail folder name.
	 * 
	 * @param name String - the given folder name.
	 */
	public MailFolderEntry(String name){
		this.name = name ;
	}
	
	/**
	 * Create mail folder entry from the given mail folder name and its parent.
	 * 
	 * @param parent MailFolderEntry - the given mail folder parent.
	 * @param name String - the given mail folder name.
	 */
	public MailFolderEntry(MailFolderEntry parent, String name){
		this.parent = parent ;
		this.name = name ;
	}
	
	/**
	 * @return the mail folder parent.
	 */
	public MailFolderEntry getParent(){
		return this.parent ;
	}
	
	/**
	 * Set mail folder entry parent.
	 * 
	 * @param parent MailFolderEntry - the given mail folder entry parent.
	 */
	public void setParent(MailFolderEntry parent){
		this.parent = parent ;
		// set parent identifier.
		this.setParentId(parent == null ? 0L : parent.getId()) ;
	}
	
	/**
	 * @return the folder identifier.
	 */
	public long getId(){
		return (this.folder == null ? 0L : this.folder.getId()) ;
	}
	
	/**
	 * Set the parent identifier.
	 * 
	 * @param parentId long - the given parent identifier.
	 */
	protected void setParentId(long parentId){
		if(this.folder != null) this.folder.setParentId(parentId) ;
	}
	
	/**
	 * @return the parent identifier.
	 */
	public long getParentId(){
		return (this.parent == null ? 0L : parent.getId()) ;
	}
	
	/**
	 * @return the folder name.
	 */
	public String getName(){
		return this.name ;
	}
	
	/**
	 * Set the folder name.
	 * 
	 * @param name String - the folder name.
	 */
	public void setName(String name){
		// set the name.
		this.name = name ;
		
		// set folder name.
		if(this.folder != null) this.folder.setName(name) ;
	}
	
	/**
	 * @return the folder type.
	 */
	public FolderType getType(){
		return (this.folder == null ? FolderType.UNKNOWN : this.folder.getType()) ;
	}
	
	/**
	 * Add the mail filter.
	 * 
	 * @param filter MailFilter - the given mail filter.
	 */
	public void addFilter(MailFilter filter){
		// the folder is null.
		if(this.folder == null) return;
		
		// get mail filter.
		if(this.folder.getFilters() == null){
			this.folder.setFilters(new ArrayList<MailFilter>()) ;
		}
		
		this.folder.getFilters().add(filter) ;
	}
	
	/**
	 * Remove mail filter.
	 * 
	 * @param filter MailFilter - the given mail filter.
	 */
	public void removeFilter(MailFilter filter){
		if(this.folder == null || this.folder.getFilters() == null) return ;
		
		// remove mail filter.
		this.folder.getFilters().remove(filter) ;
	}
	
	/**
	 * @return list of mail filter.
	 */
	public Collection<MailFilter> getFilters(){
		return (this.folder == null ? null : this.folder.getFilters()) ;
	}
	
	/**
	 * Add child to current branch.
	 * 
	 * @param child MailFolderEntry - the given mail folder entry.
	 */
	public void addChild(MailFolderEntry child){
		if(this.children == null){
			this.children = new ArrayList<MailFolderEntry>() ;
		}
		
		// add child.
		this.children.add(child) ;
	}
	
	/**
	 * Removes child out of current branch.
	 * 
	 * @param child MailFolderEntry - the given mail folder entry.
	 */
	public void removeChild(MailFolderEntry child){
		if(this.children == null) return;
		this.children.remove(child) ;
		
		// remove child.
		if(this.children.size() == 0) this.children = null ;
	}
	
	/**
	 * @return the list of children.
	 */
	public MailFolderEntry[] getChildren(){
		if(this.children == null) return new MailFolderEntry[0];
		return this.children.toArray(new MailFolderEntry[this.children.size()]) ;
	}
	
	/**
	 * @return the mail folder.
	 */
	public MailFolder getFolder(){
		return this.folder ;
	}
	
	/**
	 * Set total unread mail in current folder.
	 * 
	 * @param unread int - the given number of unread mail.
	 */
	public void setUnread(long unread){
		this.unread = unread ;
	}
	
	/**
	 * @return the number of unread mail.
	 */
	public long getUnread(){
		return this.unread ;
	}
	
	/**
	 * Set the total email in current folder.
	 * 
	 * @param total long - the total email.
	 */
	public void setTotal(long total){
		this.total = total ;
	}
	
	/**
	 * @return the total email on current folder.
	 */
	public long getTotal(){
		return this.total ;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof MailFolderEntry)) return false ;
		
		// get mail folder entry.
		MailFolderEntry entry = (MailFolderEntry)obj ;
		return ( CompareService.equals(entry.getName(), this.getName()) && 
			   ( entry.getId() == this.getId() ));
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.folder.getName() + '(' + this.unread + ')';
	}
}
