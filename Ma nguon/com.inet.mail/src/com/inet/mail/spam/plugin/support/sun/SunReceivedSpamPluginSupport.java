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
package com.inet.mail.spam.plugin.support.sun;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.inet.base.service.Assert;
import com.inet.base.service.StringService;
import com.inet.mail.spam.InetAddressResolver;
import com.inet.mail.spam.MXResolver;
import com.inet.mail.spam.ProbabilityResult;
import com.inet.mail.spam.ReceivedHeader;
import com.inet.mail.spam.ReceivedHeaderParser;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.parser.ReceivedHeaderParserException;
import com.inet.mail.spam.plugin.support.AbstractHeaderSpamPlugin;
import com.inet.mail.spam.plugin.support.SpamPluginException;
import com.inet.mail.spam.support.MXResolverSupport;
import com.inet.mail.spam.support.ProbabilityResultSupport;

/**
 * SunReceivedSpamPluginSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SunReceivedSpamPluginSupport.java 2009-01-11 23:54:26z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization SunReceivedSpamPluginSupport class.
 * </pre>
 */
public class SunReceivedSpamPluginSupport extends AbstractHeaderSpamPlugin {
	//~ Static fields =========================================================
	/* received header name. */
	private static final String RECEIVED_HEADER_NAME = "Received" ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SunReceivedSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 */
	public SunReceivedSpamPluginSupport(SpamPluginElement element) {
		super(element);
	}
	
	/**
	 * Create a new <tt>SunReceivedSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance and absolute SPAM engine flag.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 * @param absolute the given absolute SPAM plugin flag.
	 */
	public SunReceivedSpamPluginSupport(SpamPluginElement element, boolean absolute){
		super(element, absolute) ;
	}	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.plugin.support.AbstractHeaderSpamPlugin#evaluate(com.inet.mail.spam.ScanEngine, java.lang.Object)
	 */
	@Override
	public ProbabilityResult evaluate(ScanEngine engine, Object message)
			throws SpamException {
		// create probability result.
		ProbabilityResultSupport probabilityResult = new ProbabilityResultSupport() ;
		
		// set absolute.
		probabilityResult.setAbsolute(isAbsolute()) ;

		// get received header.
		String[] receiveds = getHeader(message, RECEIVED_HEADER_NAME) ;
		if(receiveds == null || receiveds.length == 0){
			probabilityResult.setProbability(MIN_PROBABILITY_VALUE) ;
		}else{
			// get received header parser, address resolver.
			ReceivedHeaderParser headerParser = engine.getReceivedHeaderParser() ;
			InetAddressResolver addressResolver = engine.getAddressResolver() ;
			
			// get received header.
			ReceivedHeader receivedHeader = null;
			
			for(String received : receiveds){
				try{
					receivedHeader = headerParser.parse(received, addressResolver) ;
					
					// resolve the first external transfer, check the MX record.
					if(!receivedHeader.isInternalTransfer()){
						break ;
					}else{
						receivedHeader = null ;
					}
				}catch(ReceivedHeaderParserException rex){
					logger.warn("could not parse the header: [" + received + "], message: [" + rex.getMessage() + "]") ;
				}
			}
			
			// check SPAM.
			if(receivedHeader != null){
				// create MXResolver to resolver MX record of sender mail server.
				MXResolver resolver = new MXResolverSupport(engine.getDNSResolver()) ;
				probabilityResult.setProbability(resolver.hasMXRecord(receivedHeader.getSenderHost()) ? 
						MIN_PROBABILITY_VALUE : MAX_PROBABILITY_VALUE) ;
			}
		}
		
		// show spam value.
		if(logger.isDebugEnabled()) logger.debug("SPAM value: [" + probabilityResult + "].") ;
		
		return probabilityResult;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.plugin.support.AbstractHeaderSpamPlugin#getHeader(java.lang.Object, java.lang.String)
	 */
	protected String[] getHeader(Object message, String header)
			throws SpamPluginException {
		Assert.isNotNull(message, "The given message must not be null.") ;
		if(!(message instanceof Message)){
			throw new SpamPluginException("the given message could not instance of javax.mail.Message class") ;
		}
		
		try{
			// get values.
			String[] values = ((Message)message).getHeader(header) ;		
			return ((values == null || values.length == 0) ? new String[]{StringService.EMPTY_STRING} : values) ;
		}catch(MessagingException mex){
			return new String[]{ StringService.EMPTY_STRING } ;
		}
	}
}
