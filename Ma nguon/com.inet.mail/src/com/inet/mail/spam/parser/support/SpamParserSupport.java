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

import com.inet.mail.spam.DNSResolver;
import com.inet.mail.spam.InetAddressResolver;
import com.inet.mail.spam.ProbabilityCalculator;
import com.inet.mail.spam.ReceivedHeaderParser;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPlugin;
import com.inet.mail.spam.SpamPluginParser;
import com.inet.mail.spam.calculator.CompoundProbabilityCalculator;
import com.inet.mail.spam.core.SpamScanEngine;
import com.inet.mail.spam.parser.AbstractSpamParser;
import com.inet.mail.spam.parser.SpamPluginParserException;
import com.inet.mail.spam.support.DNSResolverSupport;
import com.inet.mail.spam.support.InetAddressResolverSupport;

/**
 * SpamParserSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamParserSupport.java 2009-01-10 21:48:42z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamParserSupport class.
 * </pre>
 */
public class SpamParserSupport extends AbstractSpamParser {
	//~ Static fields =========================================================
	/* address resolver. */
	private static final String INET_ADDRESS_RESOLVER = "addr-resolver" ;
	
	/* DNS resolver. */
	private static final String DNS_RESOLVER = "dns-resolver" ;
	
	/* probability calculator. */
	private static final String PROBABILITY_CALCULATOR = "calculator" ;
	
	/* received header parser. */
	private static final String RECEIVED_HEADER_PARSER = "header-parser" ;
	
	/* SPAM plugin. */
	private static final String SPAM_PLUGIN = "plugin" ;
	
	/* class instance. */
	private static final String CLASS_ATTRIBUTE = "class" ;
	
	/* spam level attribute. */
	private static final String SPAM_LEVEL_ATTR = "level" ;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.SpamParser#parse(org.w3c.dom.Element)
	 */
	public ScanEngine parse(Element element) throws SpamException {
		// create scan engine.
		SpamScanEngine scanEngine = new SpamScanEngine() ;
		
		// set spam level.
		scanEngine.setLevel(getDoubleAttribute(element, SPAM_LEVEL_ATTR, 0.5d)) ;
		
		// address resolver.
		InetAddressResolver addressResolver = parseClass(element, INET_ADDRESS_RESOLVER, CLASS_ATTRIBUTE) ;
		if(addressResolver == null){
			addressResolver = new InetAddressResolverSupport() ;
		}
		scanEngine.setAddressResolver(addressResolver) ;
		
		// DNS resolver.
		DNSResolver resolver = parseClass(element, DNS_RESOLVER, CLASS_ATTRIBUTE) ;
		if(resolver == null){
			resolver = new DNSResolverSupport() ;
		}
		scanEngine.setDNSResolver(resolver) ;

		// calculator.
		ProbabilityCalculator calculator = parseClass(element, PROBABILITY_CALCULATOR, CLASS_ATTRIBUTE) ;
		if(calculator == null){
			calculator = new CompoundProbabilityCalculator() ;
		}
		scanEngine.setProbabilityCalculator(calculator) ;
		
		// receiver header parser.
		ReceivedHeaderParser headerParser = parseClass(element, RECEIVED_HEADER_PARSER, CLASS_ATTRIBUTE) ;
		if(headerParser == null){
			headerParser = new GenericReceivedHeaderParserSupport() ;
		}
		scanEngine.setReceivedHeaderParser(headerParser) ;
		
		// parse plugin.
		List<Element> elements = getElements(element, SPAM_PLUGIN) ;

		if(elements != null && elements.size() > 0){
			List<SpamPlugin> plugins = new ArrayList<SpamPlugin>() ;
			SpamPluginParser pluginParser = new SpamPluginParserSupport() ;
			
			// parse SPAM plugin.
			for(Element pluginElement : elements){
				try{
					plugins.add(pluginParser.parse(pluginElement)) ;
				}catch(SpamPluginParserException spex){
					logger.warn("could not parse spam plugin, message: [" + spex.getMessage() + "]") ;
				}catch(SpamException sex){
					logger.warn("could not parse spam plugin, message: [" + sex.getMessage() + "]") ;
				}
			}
			
			// set SPAM plugin.
			if(plugins.size() > 0){
				scanEngine.setSpamPlugins(plugins) ;
			}
		}
		
		// return scan engine.
		return scanEngine;
	}
}
