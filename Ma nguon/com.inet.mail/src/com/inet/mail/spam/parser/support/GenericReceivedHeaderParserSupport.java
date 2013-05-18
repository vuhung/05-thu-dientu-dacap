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

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.inet.base.service.StringService;
import com.inet.mail.spam.InetAddressResolver;
import com.inet.mail.spam.ReceivedHeader;
import com.inet.mail.spam.parser.AbstractReceivedHeaderParser;
import com.inet.mail.spam.parser.ReceivedHeaderParserException;
import com.inet.mail.spam.support.InetAddressResolverException;
import com.inet.mail.spam.support.ReceivedHeaderSupport;

/**
 * GenericReceivedHeaderParserSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: GenericReceivedHeaderParserSupport.java 2009-01-08 12:15:08z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization GenericReceivedHeaderParserSupport class.
 * </pre>
 */
public class GenericReceivedHeaderParserSupport extends AbstractReceivedHeaderParser {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(GenericReceivedHeaderParserSupport.class);
	
	/* sender parser. */
	private static final Pattern SENDER_HOST_PATTERN ;
	/* sender IP. */
	private static final Pattern SENDER_IP_PATTERN ;
	/* received host. */
	private static final Pattern RECEIVED_HOST_PATTERN ;
	/* received date pattern. */
	private static final Pattern RECEIVED_DATE_PATTERN; 
	
	/* initialization data. */
	static{
		StringBuilder builder = new StringBuilder() ;
		builder.append("((from\\s+)\\[?(")
			   .append(DOMAIN_PATTERN)
			   .append("|")
			   .append(IP_PATTERN)
			   .append(")\\]?)"); 
		
		// hold the sender parser. (sender host at group 3)
		SENDER_HOST_PATTERN = Pattern.compile(builder.toString()) ;
		
		builder = new StringBuilder() ;
		builder.append("(.*\\(.*\\[(")
			   .append(IP_PATTERN)
			   .append(").*\\].*\\).*)") ;
		
		// hold the sender IP pattern. (sender ip at group 2)
		SENDER_IP_PATTERN = Pattern.compile(builder.toString()) ;
		
		builder = new StringBuilder() ;
		builder.append("((\\sby\\s+)\\[?(")
			   .append(DOMAIN_PATTERN)
			   .append("|")
			   .append(IP_PATTERN)			   
			   .append(")\\]?)") ;
		
		// assign the received host pattern. (received host at group 3)
		RECEIVED_HOST_PATTERN = Pattern.compile(builder.toString()) ;
		
		// assign received date pattern. (at group 2).
		RECEIVED_DATE_PATTERN = Pattern.compile("(;\\s*(.+))") ;
	}
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>GenericReceivedHeaderParserSupport</tt> instance.
	 */
	public GenericReceivedHeaderParserSupport(){}
	
	//~ Methods ===============================================================	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ReceivedHeaderParser#parse(java.lang.String, com.inet.mail.spam.InetAddressResolver)
	 */
	public ReceivedHeader parse(String header, InetAddressResolver resolver) throws ReceivedHeaderParserException {
		// remove line separator in header.
		header = header.replaceAll("[\r\n|\n]", " ") ;
		if(logger.isDebugEnabled()) logger.debug("Received: [" + header + "].") ;
		
		// create header.
		ReceivedHeaderSupport receivedHeader = new ReceivedHeaderSupport() ;
		
		// get received host.
		receivedHeader.setReceiverHost(getReceivedHost(header)) ;
		receivedHeader.setReceivedDate(getReceivedDate(header)) ;
		
		// getting sender host and IP.
		String senderHost = getSenderHost(header) ;
		if(StringService.hasLength(senderHost) && senderHost.matches(IP_PATTERN)){
			receivedHeader.setSenderIP(senderHost) ;
		}else{
			String senderIP = getSenderIP(header) ;
			if(StringService.hasLength(senderIP)){
				receivedHeader.setSenderIP(senderIP) ;
			}else if(StringService.hasLength(senderHost)){
				try{
					receivedHeader.setSenderIP(resolver.findByHost(senderHost).getHostAddress()) ;
				}catch(InetAddressResolverException iex){
					logger.warn("could not resolve host address: [" + senderHost + "], message: [" + iex.getMessage() + "]") ;
				}catch(RuntimeException rex){
					logger.warn("could not resolve host address: [" + senderHost + "], message: [" + rex.getMessage() + "]") ;
				}
			}
		}
		
		// set sender host.
		receivedHeader.setSenderHost(senderHost) ;
		
		// return header to user.
		return receivedHeader;
	}

	//~ Helper methods ========================================================
	/**
	 * Returns the sender host from the given header.
	 * <pre>
	 * the sender host format:
	 * from<space><domain>|[<IP>]
	 * </pre>
	 * 
	 * @param header the header value.
	 * 
	 * @return the sender host or null if does not exist.
	 * @throws ReceivedHeaderParserException when error occurs during getting sender host.
	 */
	private String getSenderHost(String header) throws ReceivedHeaderParserException{
		return parser(header, SENDER_HOST_PATTERN, 3) ;
	}
	
	/**
	 * Return the sender IP from the given header.
	 * <pre>
	 *  the sender IP format.
	 *  (<char>*[<IP>]<char>*)
	 * </pre>
	 * 
	 * @param header the given received header.
	 * @return the IP address.
	 * @throws ReceivedHeaderParserException when error occurs during getting sender ip.
	 */
	private String getSenderIP(String header) throws ReceivedHeaderParserException{
		return parser(header, SENDER_IP_PATTERN, 2) ;
	}
	
	
	/**
	 * Return the received host from the given header information.
	 * 
	 * @param header the given header information.
	 * @return the receiver host.
	 * @throws ReceivedHeaderParserException when error occurs during getting received host.
	 */
	private String getReceivedHost(String header) throws ReceivedHeaderParserException{
		return parser(header, RECEIVED_HOST_PATTERN, 3) ;
	}
	
	/**
	 * Return the received date from the given header information.
	 * 
	 * @param header the header information.
	 * @return the received date.
	 * @throws ReceivedHeaderParserException when error occurs during getting received date.
	 */
	private Date getReceivedDate(String header) throws ReceivedHeaderParserException{
		String date = parser(header, RECEIVED_DATE_PATTERN, 2) ;
		
		// parse the date.
		return parseDate(date) ;
	}
}
