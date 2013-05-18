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
 * AverageProbabilityCalculator
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AverageProbabilityCalculator.java 2009-01-07 16:49:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Calculates the average of the probabilities.
 *  
 *  Specifically:
 *  <pre>
 *   Avg(A v B) = {P(A) + P(B)} / 2
 *  </pre>
 *  
 *  With P(A) and P(B) are discrete probabilities.
 * </pre>
 */
public class AverageProbabilityCalculator implements ProbabilityCalculator {
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ProbabilityCalculator#calculate(double[])
	 */
	public double calculate(double[] probabilities) {
		if(probabilities == null || probabilities.length == 0) return 0.0d;
		
		// calculates average of probabilities value.
		double result = probabilities[0] ;
		for(int index = 1; index < probabilities.length; index++){
			result += probabilities[index] ;
		}
		
		return (result / probabilities.length) ;
	}
}
