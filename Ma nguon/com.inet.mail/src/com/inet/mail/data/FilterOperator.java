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
 * FilterOperator
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 *  Initialization filter operation.
 * </pre>
 */
public enum FilterOperator {
	CONTAINS("Contains"),
	DOES_NOT_CONTAIN("DoesNotContain"),
	IS("Is"),
	IS_NOT("IsNot"),
	STARTS_WITH("StartsWith"),
	DOES_NOT_START_WITH("DoesNotStartWith"),
	ENDS_WITH("EndsWith"),
	DOES_NOT_END_WITH("DoesNotEndWith");
	
	/**
	 * The enumeration value.
	 */
	private String value ;
	
	/**
	 * Create FilterOperator instance from the given 
	 * enumeration data.
	 * 
	 * @param value String - the given enumeration data.
	 */
	private FilterOperator(String value){
		this.value = value ;
	}
	
	/**
	 * @return the enumeration value.
	 */
	public String getValue(){
		return this.value ;
	}	
}
