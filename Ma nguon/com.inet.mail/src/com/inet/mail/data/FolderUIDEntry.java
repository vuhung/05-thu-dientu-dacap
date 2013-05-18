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
package com.inet.mail.data;

import java.io.Serializable;



/**
 * FolderUIDEntry.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class FolderUIDEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9143449272765789039L;
	
	private String emailhost;
	private String emailaddress;
	private String uid;
	private Long headerID = 0L;
	
	public FolderUIDEntry(String uid,String emailhost,String emailaddress)
	{
		this.uid = uid;
		this.emailhost = emailhost;
		this.emailaddress = emailaddress;
		this.headerID = 0L;
	}
	
	public FolderUIDEntry(String uid,String emailhost,String emailaddress,Long headerID)
	{
		this.uid = uid;
		this.emailhost = emailhost;
		this.emailaddress = emailaddress;
		this.headerID = headerID;
	}

	/**
	 * @return the emailaddress
	 */
	public String getEmailaddress() {
		return this.emailaddress;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @param emailaddress the emailaddress to set
	 */
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public String getEmailhost() {
		return this.emailhost;
	}

	public void setEmailhost(String emailhost) {
		this.emailhost = emailhost;
	}

	public Long getHeaderID() {
		return this.headerID;
	}

	public void setHeaderID(Long headerID) {
		this.headerID = headerID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (emailhost + "://" + emailaddress).hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof FolderUIDEntry))
			return false;
		FolderUIDEntry myObj = (FolderUIDEntry)obj;
		return emailhost.equals(myObj.getEmailhost()) && emailaddress.equals(myObj.getEmailaddress());
	}
}
