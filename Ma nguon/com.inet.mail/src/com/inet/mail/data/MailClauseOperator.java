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
 * MailClauseOperator
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Apr 3, 2008
 * <pre>
 *  Initialization MailClauseOperator class.
 * </pre>
 */
public enum MailClauseOperator {
	OR("Or"), // OR clause.
	AND("And"); // AND clause.
	
	/**
	 * The mail clause operator value.
	 */
	private String value ;
	
	/**
	 * Create MailClauseOperator instance.
	 * 
	 * @param value String - the given mail clause operator value.
	 */
	private MailClauseOperator(String value){
		this.value = value ;
	}
	
	/**
	 * @return the mail clause operator value.
	 */
	public String getValue(){
		return this.value ;
	}
}
