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
 * ProbabilityCalculator
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ProbabilityCalculator.java 2009-01-07 16:38:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization ProbabilityCalculator class.
 * </pre>
 */
public interface ProbabilityCalculator {
	//~ Methods ===============================================================
	/**
	 * Return the probability of the given list of probabilities value.
	 * 
	 * @param probabilities the given list of probabilities value.
	 * @return the result of calculating.
	 */
	double calculate(double[] probabilities) ;
}
