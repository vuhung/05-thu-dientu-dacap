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
package com.inet.mail.spam.support;

import com.inet.mail.spam.ProbabilityResult;

/**
 * ProbabilityResultSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ProbabilityResultSupport.java 2009-01-07 18:09:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization ProbabilityResultSupport class.
 * </pre>
 */
public class ProbabilityResultSupport implements ProbabilityResult {
	//~ Instance fields =======================================================
	/* the probability value. */
	private double probability = 0.0d ;
	
	/* the absolute value that make the result is enough.*/
	private boolean absolute = false;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityResult#getProbability()
	 */
	public double getProbability() {
		return probability;
	}
	
	/**
	 * Set the probability value.
	 * 
	 * @param probability the given probability value to set.
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityResult#isAbsolute()
	 */
	public boolean isAbsolute() {
		return absolute;
	}
	
	/**
	 * Set the absolute value.
	 * 
	 * @param absolute the given absolute value to set
	 */
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
	
	/**
	 * Return the object representation as a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer() ;
		
		buffer.append(getClass().getName()).append("[")
			  .append("Probability: ").append(probability)
			  .append("; Absolute: ").append(absolute)
			  .append("]") ;
		
		return buffer.toString();
	}
}
