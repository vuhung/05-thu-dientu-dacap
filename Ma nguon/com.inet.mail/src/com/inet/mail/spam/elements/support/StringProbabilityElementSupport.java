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
package com.inet.mail.spam.elements.support;

import com.inet.base.service.CompareService;
import com.inet.base.service.StringService;
import com.inet.mail.spam.ElementType;
import com.inet.mail.spam.elements.AbstractProbabilityElement;
import com.inet.mail.spam.elements.NotSupportOperatorException;
import com.inet.mail.spam.plugin.support.SpamPluginException;

/**
 * StringProbabilityElementSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: StringProbabilityElementSupport.java 2009-01-11 19:48:49z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization StringProbabilityElementSupport class.
 * </pre>
 */
public class StringProbabilityElementSupport extends
		AbstractProbabilityElement {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getType()
	 */
	public ElementType getType() {
		return ElementType.STRING;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#ct(java.lang.Object)
	 */
	protected boolean ct(Object input) throws NotSupportOperatorException {
		return getValue(input).contains(getValue(getData())) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#eq(java.lang.Object)
	 */
	protected boolean eq(Object input) throws NotSupportOperatorException {
		return CompareService.equals(input, getData());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#sw(java.lang.Object)
	 */
	protected boolean sw(Object input) throws NotSupportOperatorException {
		return getValue(input).startsWith(getValue(getData())) ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#ew(java.lang.Object)
	 */
	protected boolean ew(Object input) throws NotSupportOperatorException {
		return getValue(input).endsWith(getValue(getData())) ;
	}
	
	//~ Helper methods ========================================================
	/**
	 * @return the value as {@link Double} instance from the given object data.
	 */
	private String getValue(Object data) throws SpamPluginException {
		if(data == null) return StringService.EMPTY_STRING ;
		if(data instanceof String){
			return (String)data ;
		}
		
		return StringService.EMPTY_STRING ;
	}	
}
