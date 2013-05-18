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
package com.inet.mail;

/**
 * MailCommand
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailCommand.java 2009-01-14 11:55:42z nguyen_dv $
 * 
 * Create date: Jan 14, 2009
 * <pre>
 *  Initialization MailCommand class.
 * </pre>
 */
public enum MailCommand {
	/* fetch mail. */
	FETCH,
	/* delete mail. */
	DELETE,
	/* read mail. */
	READ
}
