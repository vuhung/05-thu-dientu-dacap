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

import java.util.List;

/**
 * SpamPluginElement
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginElement.java 2009-01-09 18:10:30z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization SpamPluginElement class.
 * </pre>
 */
public interface SpamPluginElement {
	/**
	 * @return the max probability value.
	 */
	double getMaxProbability() ;
	
	/**
	 * @return the min probability value.
	 */
	double getMinProbability() ;
	
	/**
	 * @return the average probability value.
	 */
	double getAveProbability() ;
	
	/**
	 * @return the list of {@link ProbabilityElement} instance.
	 */
	List<ProbabilityElement> getProbabilityElements() ;
	
	/**
	 * @return the {@link ProbabilityCalculator} instance.
	 */
	ProbabilityCalculator getCalculator() ;
}
