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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.inet.base.service.StringService;
import com.inet.mail.spam.ReceivedHeaderParser;
import com.inet.mail.spam.SpamException;

/**
 * AbstractReceivedHeaderParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractReceivedHeaderParser.java 2009-01-08 20:21:03z nguyen_dv $
 * 
 * Create date: Jan 8, 2009
 * <pre>
 *  Initialization AbstractReceivedHeaderParser class.
 * </pre>
 */
public abstract class AbstractReceivedHeaderParser implements ReceivedHeaderParser {
	//~ Static fields =========================================================
	/* received data parser. */
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z") ;
	
	/* domain pattern. */
	protected static final String DOMAIN_PATTERN = "[^ @\\(\\)\\[\\]\\|\\{\\}\\,\\<\\>\\?~!@#$%^&*+/:;'\"`]+" ;
	
	/* mail pattern. */
	protected static final String MAIL_PATTERN = "[^ @\\(\\)\\[\\]\\|\\{\\}\\,\\<\\>\\?~!@#$%^&*/:;'\"`]+@[^ @\\(\\)\\[\\]\\|\\{\\}\\,\\<\\>\\?~!@#$%^&*+/:;'\"`]+" ;
	
	/* IP pattern. */
	protected static final String IP_PATTERN = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" ;
	
	//~ Instance fields =======================================================
	protected final Logger logger = Logger.getLogger(getClass());
	
	//~ Helper methods ========================================================
	/**
	 * Parsers the {@link Date} from the given date as string.
	 * 
	 * @param value the given date as string.
	 * @return the {@link Date} value never null.
	 * 
	 * @throws SpamException if the date value is not valid.
	 */
	protected Date parseDate(String value) throws ReceivedHeaderParserException{
		if(!StringService.hasLength(value)) throw new ReceivedHeaderParserException("The date value must not be null.") ;
		
		try{
			return DATE_FORMAT.parse(value) ;
		}catch(ParseException pex){
			throw new ReceivedHeaderParserException("The date: [" + value + "] value is invalidate", pex) ;
		}catch(NullPointerException nex){
			throw new ReceivedHeaderParserException("The date: [" + value + "] value is invalidate", nex) ;
		}
	}
	
	/**
	 * Parse the header and extract the expected value.
	 * 
	 * @param header the given header value.
	 * @param pattern the given pattern to extracted expected value.
	 * @param group the expected value position.
	 * @return the expected value or null.
	 * @throws ReceivedHeaderParserException when error occurs during parsing data.
	 */
	protected String parser(String header, Pattern pattern, int group) throws ReceivedHeaderParserException{
		try{
			Matcher matcher = pattern.matcher(header) ;
			
			return (matcher.find() ? matcher.group(group) : null) ;
		}catch(RuntimeException rex){
			throw new ReceivedHeaderParserException("could not retreived expected value from header: [" + header + "], message: [" + rex.getMessage() + "]", rex) ;
		}
	}
}
