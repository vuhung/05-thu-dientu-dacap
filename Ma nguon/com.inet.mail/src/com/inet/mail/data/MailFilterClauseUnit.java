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

/**
 * MailFilterClauseUnit
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Apr 3, 2008
 * <pre>
 *  Initialization MailFilterClauseUnit class.
 * </pre>
 */
public class MailFilterClauseUnit implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3880708036220320732L;

	// the filter object.
	private FilterObject object ;
	// the filter operator.
	private FilterOperator operator ;
	// the filter clause data.
	private String data ;
	
	/**
	 * Create MailFilterClauseUnit instance.
	 * 
	 * @param object FilterObject - the given filter clause unit object.
	 * @param operator FilterOperator - the given filter operator.
	 * @param data String - the given filter data.
	 */
	public MailFilterClauseUnit(
				FilterObject object, 
				FilterOperator operator, 
				String data){
		this.object = object ;
		this.operator = operator ;
		this.data = data ;
	}
	
	/**
	 * @return the filter object.
	 */
	public FilterObject getObject() {
		return this.object;
	}
	
	/**
	 * @param object FilterObject - the given filter object to set.
	 */
	public void setObject(FilterObject object) {
		this.object = object;
	}
	
	/**
	 * @return the filter operator
	 */
	public FilterOperator getOperator() {
		return this.operator;
	}
	
	/**
	 * @param operator FilterOperator - the given filter operator to set.
	 */
	public void setOperator(FilterOperator operator) {
		this.operator = operator;
	}
	
	/**
	 * @return the filter data.
	 */
	public String getData() {
		return this.data;
	}
	
	/**
	 * @param data String - the given filter data to set.
	 */
	public void setData(String data) {
		this.data = data;
	}
}
