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
package com.inet.mail.spam.elements;

import com.inet.mail.spam.ElementOperator;
import com.inet.mail.spam.ProbabilityElement;
import com.inet.mail.spam.SpamException;

/**
 * AbstractProbabilityElement
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractProbabilityElement.java 2009-01-11 18:13:22z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization AbstractProbabilityElement class.
 * </pre>
 */
public abstract class AbstractProbabilityElement implements
		ProbabilityElement {
	//~ Instance fields =======================================================
	/* probability value. */
	private double probability ;
	/* element name. */
	private String name ;
	/* element data. */
	private Object data ;
	/* element operator. */
	private ElementOperator operator ;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getProbability()
	 */
	public double getProbability() {
		return probability;
	}
	
	/**
	 * Set the element probability value.
	 * 
	 * @param probability the given probability value to set.
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getData()
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Sets the probability data.
	 * 
	 * @param data the given probability data to set.
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the element name.
	 * 
	 * @param name the given element name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#getOperator()
	 */
	public ElementOperator getOperator() {
		return operator;
	}
	
	/**
	 * Set the {@link ElementOperator} instance.
	 * 
	 * @param operator the given {@link ElementOperator} instance to set.
	 */
	public void setOperator(ElementOperator operator) {
		this.operator = operator;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityElement#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object input) throws SpamException{
		if(operator == null) return false ;
		
		switch(operator){
		case CT: return ct(input) ;
		case EQ: return eq(input) ;
		case EW: return ew(input) ;
		case GE: return ge(input) ;
		case GT: return gt(input) ;
		case LE: return le(input) ;
		case LT: return lt(input) ;
		case SW: return sw(input) ;
		default: return false ;
		}
	}
	//~ Helper methods ========================================================	
	/**
	 * @return if the input is greater than the configuration value.
	 */
	protected boolean gt(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support GT operator.") ;
	}
	
	/**
	 * @return if the input is greater than or equals the configuration value.
	 */
	protected boolean ge(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support GE operator.") ;
	}
	
	/**
	 * @return if the input is lesser than or equals the configuration value.
	 */
	protected boolean le(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support LE operator.") ;
	}
	
	/**
	 * @return if the input is lesser than the configuration value.
	 */
	protected boolean lt(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support LT operator.") ;
	}
	
	/**
	 * @return if the input is equals the configuration value.
	 */
	protected boolean eq(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support EQ operator.") ;
	}
	
	/**
	 * @return if the input contains the configuration value.
	 */
	protected boolean ct(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support CT operator.") ;
	}	
	
	/**
	 * @return if the input start with the configuration value.
	 */
	protected boolean sw(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support SW operator.") ;
	}	
	
	/**
	 * @return if the input end with the configuration value.
	 */
	protected boolean ew(Object input) throws NotSupportOperatorException{
		throw new NotSupportOperatorException("Class: [" + getClass().getName() + "] not support EW operator.") ;
	}	
	
	/**
	 * Returns an object representation as string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder() ;
		
		builder.append(getClass().getName()).append("[")
			   .append("Name: ").append(name)
			   .append("; Probability: ").append(probability)
			   .append("; Value: ").append(data)
			   .append("; Operator: ").append(operator)
			   .append("; Type: ").append(getType())
			   .append("]") ;
		
		return builder.toString();
	}
}
