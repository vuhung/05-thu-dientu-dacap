/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.web.service.lotus.org.permission;

import java.util.List;

/**
 * AlterGroupData.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AlterGroupData {
	public enum ACTION {ADD, UPDATE, DELETE};
	private ACTION action;
	private String organization;
	private String group;
	private List<String> removeMembers;
	private List<String> insertMembers;
	private List<String> newManagers;
	
	/**
	 * The default constructor 
	 * 
	 * @param action ACTION - the action
	 */
	public AlterGroupData(ACTION action) {
		this.action = action;
	}

	/**
	 * Create <tt>AlterGroupData</tt> instance
	 * 
	 * @param organization String - the organization name
	 * @param group String - the group name
	 * @param action ACTION - the action 
	 */
	public AlterGroupData(String organization, String group, ACTION action) {
		this.group = group;
		this.organization = organization;
		this.action = action;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return this.group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the removeMembers
	 */
	public List<String> getRemoveMembers() {
		return this.removeMembers;
	}

	/**
	 * @param removeMembers the removeMembers to set
	 */
	public void setRemoveMembers(List<String> removeMembers) {
		this.removeMembers = removeMembers;
	}

	/**
	 * @return the insertMembers
	 */
	public List<String> getInsertMembers() {
		return this.insertMembers;
	}

	/**
	 * @param insertMembers the insertMembers to set
	 */
	public void setInsertMembers(List<String> insertMembers) {
		this.insertMembers = insertMembers;
	}
	
	/**
	 * @return the newManagers
	 */
	public List<String> getNewManagers() {
		return this.newManagers;
	}

	/**
	 * @param newManagers the newManagers to set
	 */
	public void setNewManagers(List<String> newManagers) {
		this.newManagers = newManagers;
	}

	/**
	 * @return the action
	 */
	public ACTION getAction() {
		return this.action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(ACTION action) {
		this.action = action;
	}
	
}
