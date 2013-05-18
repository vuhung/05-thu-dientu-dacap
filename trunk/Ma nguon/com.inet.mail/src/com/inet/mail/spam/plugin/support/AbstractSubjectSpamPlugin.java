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
package com.inet.mail.spam.plugin.support;

import java.util.ArrayList;
import java.util.List;

import com.inet.base.service.StringService;
import com.inet.mail.spam.ProbabilityElement;
import com.inet.mail.spam.ProbabilityResult;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.plugin.AbstractSpamPlugin;
import com.inet.mail.spam.support.ProbabilityResultSupport;

/**
 * AbstractSubjectSpamPlugin
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractSubjectSpamPlugin.java 2009-01-09 17:54:00z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization AbstractSubjectSpamPlugin class.
 * </pre>
 */
public abstract class AbstractSubjectSpamPlugin extends AbstractSpamPlugin {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AbstractSubjectSpamPlugin</tt> instance from the given 
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 */
	protected AbstractSubjectSpamPlugin(final SpamPluginElement element){
		super(element) ;
	}
	
	/**
	 * Create a new <tt>AbstractSubjectSpamPlugin</tt> instance {@link SpamPluginElement} 
	 * instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 * @param absolute the given flag that mark SPAM plugin is absolute.
	 */
	protected AbstractSubjectSpamPlugin(final SpamPluginElement element, 
			boolean absolute){
		super(element, absolute) ;
	}
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPlugin#evaluate(com.inet.mail.spam.ScanEngine, java.lang.Object)
	 */
	public ProbabilityResult evaluate(final ScanEngine engine, Object message) throws SpamException {
		// create probability result.
		ProbabilityResultSupport probabilityResult = new ProbabilityResultSupport() ;
		
		// set absolute.
		probabilityResult.setAbsolute(isAbsolute()) ;
		
		String subject = getSubject(message) ;		
		
		// get plugin element.
		SpamPluginElement elements = getPluginElement() ;
		if(elements == null || elements.getProbabilityElements() == null || elements.getProbabilityElements().size() == 0) {
			probabilityResult.setProbability(MIN_PROBABILITY_VALUE) ;
		}else{
			List<Double> probabilities = new ArrayList<Double>() ;
			
			// compute SPAM value.
			if(StringService.hasLength(subject)){
				for(ProbabilityElement element : elements.getProbabilityElements()){
					if(element.evaluate(subject)){
						probabilities.add(element.getProbability()) ;
					}
				}
			}
			
			if(probabilities.size() > 0){
				double[] rawValues = new double[probabilities.size()] ;
				for(int index = 0; index < probabilities.size(); index++){
					rawValues[index] = probabilities.get(index).doubleValue() ;
				}
				
				// calculate the probablity.
				probabilityResult.setProbability(elements.getCalculator().calculate(rawValues)) ;
			}else{
				probabilityResult.setProbability(MIN_PROBABILITY_VALUE) ;
			}
		}
		
		// show information.
		if(logger.isDebugEnabled()) logger.debug("Subject: [" + subject + "] has SPAM value: [" + probabilityResult + "].") ;
		
		return probabilityResult;
	}
	
	//~ Abstract Methods ======================================================
	/**
	 * Return the subject from the given message instance.
	 * 
	 * @param message the given message Object.
	 * 
	 * @return the message subject or empty.
	 * @throws SpamPluginException when error occurs during getting subject.
	 */
	protected abstract String getSubject(Object message) throws SpamPluginException ;
}
