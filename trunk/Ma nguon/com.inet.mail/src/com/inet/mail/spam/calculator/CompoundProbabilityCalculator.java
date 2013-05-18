/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.spam.calculator;

import com.inet.mail.spam.ProbabilityCalculator;

/**
 * CompoundProbabilityCalculator
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CompoundProbabilityCalculator.java 2009-01-07 16:47:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 * Calculates the combined probability using the standard compound probability
 * algorithm.
 * <p>
 * Specifically:
 * <pre>
 *  P(A v B) = P(A) + P(B) - P(A).P(B)
 * </pre>
 * </p>
 * Where: P(A) and P(B) are discrete probabilities
 * </pre>
 */
public class CompoundProbabilityCalculator implements ProbabilityCalculator {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityCalculator#calculate(double[])
	 */
	public double calculate(double[] probabilities) {
		if(probabilities == null || probabilities.length == 0) return 0.0d ;
		if(probabilities.length == 1) return probabilities[0] ;
		
		// calculate the base result.
		double result = compound(probabilities[0], probabilities[1]) ;
		for(int index = 2; index < probabilities.length; index++){
			result = compound(result, probabilities[index]) ;
		}
		
		// return the compound result.
		return result;
	}
	
	//~ Helper Methods ========================================================
	/**
	 * calculates the base of compound probabilities.
	 * 
	 * @param probabilityA the given probability of event A.
	 * @param probabilityB the given probability of event B.
	 * 
	 * @return the compound probability of two events.
	 */
	private double compound(double probabilityA, double probabilityB){
		return (probabilityA + probabilityB - (probabilityA * probabilityB)) ;
	}
}
