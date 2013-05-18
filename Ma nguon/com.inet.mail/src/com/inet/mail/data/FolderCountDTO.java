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
 * CountMessageDTO.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class FolderCountDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4855806382946529082L;

	private Long folderID;
	private Long count;
	
	public FolderCountDTO(Long folderID, Long count)
	{
		this.folderID = folderID;
		this.count = count;
	}

	public Long getFolderID() {
		return this.folderID;
	}

	public void setFolderID(Long folderID) {
		this.folderID = folderID;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}	
}
