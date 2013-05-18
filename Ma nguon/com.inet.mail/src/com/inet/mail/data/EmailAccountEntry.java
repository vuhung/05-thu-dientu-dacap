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
import java.util.HashMap;

/**
 * EmailAccountEntry.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class EmailAccountEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3413892729409066214L;
	
	private HashMap<String,FolderUIDEntry> folderUids = new HashMap<String, FolderUIDEntry>();
	private HashMap<Long, Long> unread = new HashMap<Long, Long>();
	private HashMap<Long, Long> msgCount = new HashMap<Long, Long>();
	
	public EmailAccountEntry()
	{		
	}

	/**
	 * @return the folderUids
	 */
	public HashMap<String, FolderUIDEntry> getFolderUids() {
		return this.folderUids;
	}

	/**
	 * @param folderUids the folderUids to set
	 */
	public void setFolderUids(HashMap<String, FolderUIDEntry> folderUids) {
		this.folderUids =  folderUids;
	}

	/**
	 * @return the unread
	 */
	public HashMap<Long, Long> getUnread() {
		return this.unread;
	}

	/**
	 * @param unread the unread to set
	 */
	public void setUnread(HashMap<Long, Long> unread) {
		this.unread = unread;
	}

	/**
	 * @return the msgCount
	 */
	public HashMap<Long, Long> getMsgCount() {
		return this.msgCount;
	}

	/**
	 * @param msgCount the msgCount to set
	 */
	public void setMsgCount(HashMap<Long, Long> msgCount) {
		this.msgCount = msgCount;
	}	
}
