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
package com.inet.mail.spam.parser;

import org.w3c.dom.Element;

import com.inet.mail.spam.ProbabilityCalculator;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.SpamPluginElementParser;
import com.inet.mail.spam.calculator.CompoundProbabilityCalculator;
import com.inet.mail.spam.support.SpamPluginElementSupport;

/**
 * AbstractSpamPluginElementParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractSpamPluginElementParser.java 2009-01-10 10:52:16z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization AbstractSpamPluginElementParser class.
 * </pre>
 */
public abstract class AbstractSpamPluginElementParser extends AbstractXMLElementParser implements
		SpamPluginElementParser {
	//~ Static fields =========================================================
	/* max probability value. */
	protected static final double MAX_PROBABILITY_VALUE = 1.0d ;
	
	/* min probability value. */
	protected static final double MIN_PROBABILITY_VALUE = 0.0d ;
	
	/* average probability value. */
	protected static final double AVE_PROBABILITY_VALUE = 0.5d ;
	
	/* list of SPAM constraints. */
	protected static final String SPAM_CONSTRAINTS = "constraints" ;

	/* list of SPAM constraint. */
	protected static final String SPAM_CONSTRAINT = "constraint" ;

	/* constraint max probability value. */
	protected static final String CONSTRAINTS_MAX_PROBABILITY = "max" ;

	/* constraint min probability value. */
	protected static final String CONSTRAINTS_MIN_PROBABILITY = "min" ;

	/* constraint average probability value. */
	protected static final String CONSTRAINTS_AVE_PROBABILITY = "average" ;
	
	/* constraint calculate. */
	protected static final String CONSTRAINTS_CALCULATE = "calculator" ;	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamPluginElementParser#parse(org.w3c.dom.Element)
	 */
	@SuppressWarnings({"unchecked"})
	public final SpamPluginElement parse(Element element) throws SpamException {
		// Plugin element.
		SpamPluginElementSupport pluginElement = new SpamPluginElementSupport() ;
		
		// parse internal information.
		Element constraints = getElement(element, SPAM_CONSTRAINTS) ;
		if(constraints != null){
			// set probability value.
			pluginElement.setAveProbability(getDoubleAttribute(constraints, CONSTRAINTS_AVE_PROBABILITY, AVE_PROBABILITY_VALUE)) ;
			pluginElement.setMaxProbability(getDoubleAttribute(constraints, CONSTRAINTS_MAX_PROBABILITY, MAX_PROBABILITY_VALUE)) ;
			pluginElement.setMinProbability(getDoubleAttribute(constraints, CONSTRAINTS_MIN_PROBABILITY, MIN_PROBABILITY_VALUE)) ;
			
			// set calculator class.
			Class<ProbabilityCalculator> calculatorClass = (Class<ProbabilityCalculator>)getClass(constraints, CONSTRAINTS_CALCULATE) ;
			if(calculatorClass != null){
				ProbabilityCalculator probabilityCalculator = null ;
				try {
					probabilityCalculator = calculatorClass.newInstance() ;					
				} catch (InstantiationException iex) {
					probabilityCalculator = new CompoundProbabilityCalculator() ;
				} catch (IllegalAccessException iex) {
					probabilityCalculator = new CompoundProbabilityCalculator() ;
				}
				
				// setting calculator.
				pluginElement.setCalculator(probabilityCalculator) ;
			}
		}
		
		// parse internal.
		parseInternal(pluginElement, element) ;
		
		// return plugin element.
		return pluginElement;
	}
	
	/**
	 * Parses the internal element from the given {@link SpamPluginElement} instance
	 * and the {@link Element} instance.
	 * 
	 * @param pluginElement the given {@link SpamPluginElement} instance.
	 * @param element the given {@link Element} instance.
	 * 
	 * @throws SpamPluginElementParserException when error occur during parsing {@link Element} instance.
	 */
	protected abstract void parseInternal(SpamPluginElement pluginElement, Element element) 
		throws SpamPluginElementParserException ;
}
