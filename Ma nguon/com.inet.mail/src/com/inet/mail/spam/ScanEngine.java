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
 * ScanEngine
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ScanEngine.java 2009-01-09 16:06:21z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization ScanEngine class.
 * </pre>
 */
public interface ScanEngine {
	/**
	 * @return the {@link InetAddressResolver} instance.
	 */
	InetAddressResolver getAddressResolver() ;
	
	/**
	 * @return the {@link DNSResolver} instance.
	 */
	DNSResolver getDNSResolver() ;
	
	/**
	 * @return the {@link ReceivedHeaderParser} instance.
	 */
	ReceivedHeaderParser getReceivedHeaderParser() ;
	
	/**
	 * @return the {@link ProbabilityCalculator} instance.
	 */
	ProbabilityCalculator getProbabilityCalculator() ;
	
	/**
	 * @return the list of {@link SpamPlugin} instance.
	 */
	List<SpamPlugin> getSpamPlugins() ;
	
	/**
	 * @return the maximize SPAM probability value.
	 */
	double getLevel() ;
	
	/**
	 * Scan the message and evaluate the result.
	 * 
	 * @param message the given message object.
	 * @return the {@link ScanResult} instance.
	 * 
	 * @throws SpamException when error occurs during scaning message.
	 */
	ScanResult scan(Object message) throws SpamException ;
}
