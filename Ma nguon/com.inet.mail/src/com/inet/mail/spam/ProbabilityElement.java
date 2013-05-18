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
package com.inet.mail.spam;

/**
 * ProbabilityElement
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ProbabilityElement.java 2009-01-09 18:18:22z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization ProbabilityElement class.
 * </pre>
 */
public interface ProbabilityElement {
	/**
	 * @return the probability value.
	 */
	double getProbability() ;
	
	/**
	 * @return the element name.
	 */
	String getName() ;
	
	/**
	 * @return the probability data.
	 */
	Object getData() ;
	
	/**
	 * @return the probability {@link ElementType} instance.
	 */
	ElementType getType() ;
	
	/**
	 * @return the probability {@link ElementOperator} instance.
	 */
	ElementOperator getOperator() ;
	
	/**
	 * Return if the given input data is matched configuration data.
	 * 
	 * @param input the given input data.
	 * @return if the given input data is matched configuration data.
	 * @throws SpamException when error occurs during evaluating data.
	 */
	boolean evaluate(Object input) throws SpamException;
}
