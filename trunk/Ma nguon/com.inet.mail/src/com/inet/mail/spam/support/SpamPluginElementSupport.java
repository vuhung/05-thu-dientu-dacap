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

import java.util.List;

import com.inet.mail.spam.ProbabilityCalculator;
import com.inet.mail.spam.ProbabilityElement;
import com.inet.mail.spam.SpamPluginElement;

/**
 * SpamPluginElementSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginElementSupport.java 2009-01-10 10:02:58z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamPluginElementSupport class.
 * </pre>
 */
public class SpamPluginElementSupport implements SpamPluginElement {
	//~ Instance fields =======================================================
	/* max probability value. */
	private double maxProbability = 1.0d ;
	/* min probability value. */
	private double minProbability = 0.0d ;
	/* average probability value. */
	private double aveProbability = 0.5d ;
	/* probability calculator. */
	private ProbabilityCalculator calculator ;
	/* probability element. */
	private List<ProbabilityElement> probabilityElements ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElement#getAveProbability()
	 */
	public double getAveProbability() {
		return aveProbability;
	}

	/**
	 * Set average probability value.
	 * 
	 * @param aveProbability the given average probability value.
	 */
	public void setAveProbability(double aveProbability) {
		this.aveProbability = aveProbability;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElement#getCalculator()
	 */
	public ProbabilityCalculator getCalculator() {
		return calculator;
	}
	
	/**
	 * Set {@link ProbabilityCalculator} instance.
	 * 
	 * @param calculator the given {@link ProbabilityCalculator} instance to set.
	 */
	public void setCalculator(ProbabilityCalculator calculator) {
		this.calculator = calculator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElement#getMaxProbability()
	 */
	public double getMaxProbability() {
		return maxProbability;
	}
	
	/**
	 * Set max probability value.
	 * 
	 * @param maxProbability the given max probability value to set.
	 */
	public void setMaxProbability(double maxProbability) {
		this.maxProbability = maxProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElement#getMinProbability()
	 */
	public double getMinProbability() {
		return minProbability;
	}
	
	/**
	 * Set min probability value.
	 * 
	 * @param minProbability the given min probability value to set.
	 */
	public void setMinProbability(double minProbability) {
		this.minProbability = minProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElement#getProbabilityElements()
	 */
	public List<ProbabilityElement> getProbabilityElements() {
		return probabilityElements;
	}
	
	/**
	 * Set list of {@link ProbabilityElement} instance.
	 * 
	 * @param probabilityElements the given list of {@link ProbabilityElement} to set.
	 */
	public void setProbabilityElements(
			List<ProbabilityElement> probabilityElements) {
		this.probabilityElements = probabilityElements;
	}
	
	/**
	 * Return an object representation as String.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder() ;
		
		builder.append(getClass().getName()).append("[")
			   .append("Max Probability: ").append(maxProbability)
			   .append("; Min Probability: ").append(minProbability)
			   .append("; Average Probability: ").append(aveProbability) ;
		if(calculator != null){
			builder.append("; Calculator: ").append(calculator.getClass()) ;
		}	
		builder.append("]") ;
		
		return builder.toString();
	}
}
