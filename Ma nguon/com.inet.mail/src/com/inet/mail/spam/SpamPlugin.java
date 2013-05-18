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

/**
 * SpamPlugin
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamPlugin.java 2009-01-09 15:53:35z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization SpamPlugin class.
 * </pre>
 */
public interface SpamPlugin {
	/**
	 * Evaluate the message and calculate the SPAM probability.
	 * 
	 * @param engine the given {@link ScanEngine} instance.
	 * @param message the given message object.
	 * @return the {@link ProbabilityResult} instance.
	 * 
	 * @throws SpamException when error occurs during evaluating message.
	 */
	ProbabilityResult evaluate(final ScanEngine engine, Object message) throws SpamException ;
}
