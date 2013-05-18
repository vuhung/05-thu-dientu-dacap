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
package com.inet.mail.spam.parser.support;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.inet.base.service.StringService;
import com.inet.mail.spam.ElementOperator;
import com.inet.mail.spam.ElementType;
import com.inet.mail.spam.ProbabilityElement;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.elements.AbstractProbabilityElement;
import com.inet.mail.spam.elements.support.DoubleProbabilityElementSupport;
import com.inet.mail.spam.elements.support.LongProbabilityElementSupport;
import com.inet.mail.spam.elements.support.StringProbabilityElementSupport;
import com.inet.mail.spam.parser.AbstractSpamPluginElementParser;
import com.inet.mail.spam.parser.SpamPluginElementParserException;
import com.inet.mail.spam.support.SpamPluginElementSupport;

/**
 * SpamPluginElementParserSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPluginElementParserSupport.java 2009-01-11 22:46:00z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization SpamPluginElementParserSupport class.
 * </pre>
 */
public class SpamPluginElementParserSupport extends
		AbstractSpamPluginElementParser {
	//~ Static fields =========================================================
	/* constraint probability. */
	private static final String CONSTRAINT_PROBABILITY = "probability" ;
	/* constraint value. */
	private static final String CONSTRAINT_VALUE = "value" ;
	/* constraint type. */
	private static final String CONSTRAINT_TYPE = "type" ;
	/* constraint operator. */
	private static final String CONSTRAINT_OPERATOR = "operator" ;
	/* constraint name. */ 
	private static final String CONSTRAINT_NAME = "name" ;
	/* constraint class. */
	private static final String CONSTRAINT_CLASS = "class" ;
	
	//~ Methods ===============================================================	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.parser.AbstractSpamPluginElementParser#parseInternal(com.inet.mail.spam.SpamPluginElement, org.w3c.dom.Element)
	 */
	protected void parseInternal(SpamPluginElement pluginElement,
			Element element) throws SpamPluginElementParserException {
		// get spam constraints.
		Element constraintsElement = getElement(element, SPAM_CONSTRAINTS) ;
		List<ProbabilityElement> elements = new ArrayList<ProbabilityElement>() ;
		
		// parse probability element.
		if(constraintsElement != null){
			List<Element> constraintElements = getElements(constraintsElement, SPAM_CONSTRAINT) ;
			
			if(constraintElements != null){
				for(Element constrantElement : constraintElements){
					ProbabilityElement probabilityElement = parseElement(constrantElement) ;
					if(probabilityElement != null){
						elements.add(probabilityElement) ;
					}
				}
			}
		}
		
		// setting plugin element.
		((SpamPluginElementSupport)pluginElement).setProbabilityElements(elements) ;		
	}
	
	//~ Helper methods ========================================================
	/**
	 * Parse the element from the given {@link Element} instance.
	 * 
	 * @param element the given {@link Element} instance.
	 */
	@SuppressWarnings({"unchecked"})
	private ProbabilityElement parseElement(Element element){
		if(element == null) return null ;
		
		// get element type.
		String type = getAttribute(element, CONSTRAINT_TYPE, ElementType.STRING.toString()) ;
		ElementType elementType = ElementType.STRING ;
		try{
			elementType = ElementType.valueOf(type.toUpperCase()) ;
		}catch(Exception ex){}
		
		// create probability element.
		AbstractProbabilityElement probabilityElement = null ;
		Class<AbstractProbabilityElement> classProbabilityElement = (Class<AbstractProbabilityElement>)getClass(element, CONSTRAINT_CLASS) ;
		if(classProbabilityElement == null){
			switch(elementType){
			case DOUBLE:
				probabilityElement = new DoubleProbabilityElementSupport() ;
				break ;
			case LONG:
				probabilityElement = new LongProbabilityElementSupport() ;
				break ;
			default:
				probabilityElement = new StringProbabilityElementSupport() ;
			}
		}else{
			try{
				probabilityElement = classProbabilityElement.newInstance() ;
			}catch(IllegalAccessException iaex){	
			}catch(Exception ex){}
		}
		
		// setting probability value.
		probabilityElement.setProbability(getDoubleAttribute(element, CONSTRAINT_PROBABILITY, AVE_PROBABILITY_VALUE)) ;
		
		// set element operator.
		String operator = getAttribute(element, CONSTRAINT_OPERATOR, ElementOperator.CT.toString()) ;
		try{
			probabilityElement.setOperator(ElementOperator.valueOf(operator)) ;
		}catch(Exception ex){
			probabilityElement.setOperator(ElementOperator.CT) ;
		}
		
		// set probability name.
		probabilityElement.setName(getAttribute(element, CONSTRAINT_NAME, StringService.EMPTY_STRING)) ;
		
		// set probability element.
		probabilityElement.setData(getAttribute(element, CONSTRAINT_VALUE, StringService.EMPTY_STRING)) ;
		
		// return probability element.
		return probabilityElement;
	}	
}
