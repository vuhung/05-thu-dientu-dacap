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

import com.inet.mail.spam.ProbabilityElement;
import com.inet.mail.spam.ProbabilityResult;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.plugin.AbstractSpamPlugin;
import com.inet.mail.spam.support.ProbabilityResultSupport;

/**
 * AbstractHeaderSpamPlugin
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractHeaderSpamPlugin.java 2009-01-11 10:56:37z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization AbstractHeaderSpamPlugin class.
 * </pre>
 */
public abstract class AbstractHeaderSpamPlugin extends AbstractSpamPlugin {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AbstractHeaderSpamPlugin</tt> instance from the given 
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 */
	protected AbstractHeaderSpamPlugin(final SpamPluginElement element){
		super(element) ;
	}
	
	/**
	 * Create a new <tt>AbstractHeaderSpamPlugin</tt> instance {@link SpamPluginElement} 
	 * instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 * @param absolute the given flag that mark SPAM plugin is absolute.
	 */
	protected AbstractHeaderSpamPlugin(final SpamPluginElement element, 
			boolean absolute){
		super(element, absolute) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPlugin#evaluate(com.inet.mail.spam.ScanEngine, java.lang.Object)
	 */
	public ProbabilityResult evaluate(ScanEngine engine, Object message)
			throws SpamException {
		// create probability result.
		ProbabilityResultSupport probabilityResult = new ProbabilityResultSupport() ;
		
		// set absolute.
		probabilityResult.setAbsolute(isAbsolute()) ;

		// get list of element.
		List<ProbabilityElement> probabilityElements = (getPluginElement() != null ? 
							getPluginElement().getProbabilityElements() : null );
		if(probabilityElements == null || probabilityElements.size() == 0){
			probabilityResult.setProbability(MIN_PROBABILITY_VALUE) ;
		}else{
			List<Double> probabilities = new ArrayList<Double>() ;
			
			// create SPAM value.
			for(ProbabilityElement probabilityElement : probabilityElements){
				try{
					String headerValue = getHeader(message, probabilityElement.getName())[0] ;					
					if(probabilityElement.evaluate(headerValue)){
						probabilities.add(probabilityElement.getProbability()) ;
					}
				}catch(Exception ex){
					logger.warn("could not retrieved header value: [" + probabilityElement.getName() + "].", ex) ;
				}
			}
			
			if(probabilities.size() > 0){
				double[] rawValues = new double[probabilities.size()] ;
				for(int index = 0; index < probabilities.size(); index++){
					rawValues[index] = probabilities.get(index).doubleValue() ;
				}
				
				// calculate the probablity.
				probabilityResult.setProbability(getPluginElement().getCalculator().calculate(rawValues)) ;				
			}else{
				probabilityResult.setProbability(MIN_PROBABILITY_VALUE) ;
			}
		}
		
		// show spam value.
		if(logger.isDebugEnabled()) logger.debug("SPAM value: [" + probabilityResult + "].") ;
		
		return probabilityResult;
	}
	
	/**
	 * Return list of header from the given message instance.
	 * 
	 * @param message the given message object instance.
	 * @param header the given header name.
	 * 
	 * @return the list of header value.
	 * 
	 * @throws SpamPluginException when error occurs during retrieving header information.
	 */
	protected abstract String[] getHeader(Object message, String header) throws SpamPluginException ;
}
