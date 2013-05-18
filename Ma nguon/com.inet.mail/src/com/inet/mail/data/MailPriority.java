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

/**
 * MailPriority
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 *  Initialization mail priority.
 * </pre>
 */
public enum MailPriority {
	/**
	 * Low priority.
	 */
	LOW(5, "low"),
	/**
	 * Normal priority.
	 */
	NORMAL(3, "normal"),
	/**
	 * High priority.
	 */
	HIGH (1, "high");
	
	//~ Instance fields =======================================================
	/* priority value. */
	private int priority ;
	
	/* importance value. */
	private String important ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailPriority</tt> instance from
	 * the given priority value and importance value.
	 * 
	 * @param priority the given priority value.
	 * @param important the given important value.
	 */
	private MailPriority(int priority, String important) {
		this.priority = priority ;
		this.important = important ;
	}
	
	//~ Methods ===============================================================	
	/** 
	 * @return the priority value.
	 */
	public int getPriority(){
		return priority ;
	}
	
	/**
	 * @return the important value.
	 */
	public String getImportant(){
		return important ;
	}
}
