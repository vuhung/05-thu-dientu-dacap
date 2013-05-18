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
package com.inet.mail.spam.calculator;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.mail.spam.ProbabilityCalculator;

/**
 * TestAverageProbabilityCalculator
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestAverageProbabilityCalculator.java 2009-01-07 17:30:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization TestAverageProbabilityCalculator class.
 * </pre>
 */
public class TestAverageProbabilityCalculator {
	/**
	 * Test the calculator function.
	 */
	@Test
	public void testCalculator(){
		// create calculator.
		ProbabilityCalculator calculator = new AverageProbabilityCalculator() ;
		
		Assert.assertEquals(0.45d, calculator.calculate(new double[]{0.4d, 0.5d})) ;
		Assert.assertEquals(0.4d, calculator.calculate(new double[]{0.4d, 0.4d})) ;
		Assert.assertEquals(0.7d, calculator.calculate(new double[]{0.9d, 0.5d})) ;
		Assert.assertEquals(0.8d, calculator.calculate(new double[]{0.9d, 0.7d})) ;
	}
}
