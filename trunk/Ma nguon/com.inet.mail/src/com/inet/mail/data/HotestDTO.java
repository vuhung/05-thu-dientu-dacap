/*****************************************************************
 Copyright 2007 by hiennguyen (hiennguyen@truthinet.com)

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
 * @author hiennguyen
 *
 */
public class HotestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3754179605960145220L;
	private long count;
	private long forumId;
	private String name;
	private char dmzAccess;
	private long parentId;
	
	public HotestDTO(Long count, Long forumId, String name, char dmzAccess, Long parentId)
	{
		this.count = count.intValue();
		this.forumId = forumId;
		this.name = name;
		this.dmzAccess = dmzAccess;
		this.parentId = parentId;
	}
	
	public long getCount() {
		return this.count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getForumId() {
		return this.forumId;
	}

	public void setForumId(long forumId) {
		this.forumId = forumId;
	}	
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dmzAccess
	 */
	public char getDmzAccess() {
		return this.dmzAccess;
	}

	/**
	 * @param dmzAccess the dmzAccess to set
	 */
	public void setDmzAccess(char dmzAccess) {
		this.dmzAccess = dmzAccess;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return this.parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
}
