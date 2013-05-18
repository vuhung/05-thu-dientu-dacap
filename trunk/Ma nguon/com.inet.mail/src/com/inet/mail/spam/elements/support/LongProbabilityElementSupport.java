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

import com.inet.mail.spam.ElementType;
import com.inet.mail.spam.elements.AbstractProbabilityElement;
import com.inet.mail.spam.elements.NotSupportOperatorException;
import com.inet.mail.spam.plugin.support.SpamPluginException;

/**
 * LongProbabilityElementSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: LongProbabilityElementSupport.java 2009-01-11 19:41:22z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization LongProbabilityElementSupport class.
 * </pre>
 */
public class LongProbabilityElementSupport extends AbstractProbabilityElement {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getType()
	 */
	public ElementType getType() {
		return ElementType.LONG;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#ge(java.lang.Object)
	 */
	protected boolean ge(Object input) throws NotSupportOperatorException {
		return getValue(input).compareTo(getValue(getData())) >= 0;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#gt(java.lang.Object)
	 */
	protected boolean gt(Object input) throws NotSupportOperatorException {
		return getValue(input).compareTo(getValue(getData())) == 1;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#lt(java.lang.Object)
	 */
	protected boolean lt(Object input) throws NotSupportOperatorException {
		return getValue(input).compareTo(getValue(getData())) == -1;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#le(java.lang.Object)
	 */
	protected boolean le(Object input) throws NotSupportOperatorException {
		return getValue(input).compareTo(getValue(getData())) <= 0;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.elements.AbstractProbabilityElement#eq(java.lang.Object)
	 */
	protected boolean eq(Object input) throws NotSupportOperatorException {
		return getValue(input).compareTo(getValue(getData())) == 0;
	}
	
	//~ Helper methods ========================================================
	/**
	 * @return the value as {@link Double} instance from the given object data.
	 */
	private Long getValue(Object data) throws SpamPluginException {
		if(data == null) return 0L ;
		if(data instanceof String){
			try{
				return Long.parseLong((String)data) ;
			}catch(NumberFormatException nex){}
		}else if(data instanceof Long){
			return (Long)data ;
		}
		return 0L ;
	}
}
