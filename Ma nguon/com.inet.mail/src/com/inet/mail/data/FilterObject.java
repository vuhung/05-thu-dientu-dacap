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
 * FilterObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 * 	Initialization FilterObject data.
 * </pre>
 */
public enum FilterObject {
	SENDER("Sender"),
	RECIPIENTS("Recipients"),
	SUBJECT("Subject"),
	CONTENT("Content"),
	DATE_SENT("DateSent"),
	DATE_RECEIVED("DateReceived"),
	ALL("All") ;
	
	/**
	 * The enumeration value.
	 */
	private String value ;
	
	/**
	 * Create FilterObject instance from the given 
	 * enumeration data.
	 * 
	 * @param value String - the given enumeration data.
	 */
	private FilterObject(String value){
		this.value = value ;
	}
	
	/**
	 * @return the enumeration value.
	 */
	public String getValue(){
		return this.value ;
	}
}
