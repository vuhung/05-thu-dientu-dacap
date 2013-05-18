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
 * MXResolver
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MXResolver.java 2009-01-09 14:50:25 nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization MXResolver class.
 * </pre>
 */
public interface MXResolver {
	/**
	 * Return if the MX record of the given host exist.
	 * 
	 * @param host the given host.
	 * @return if the MX record of the host exist.
	 */
	boolean hasMXRecord(String host);
}
