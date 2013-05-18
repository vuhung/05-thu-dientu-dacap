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
package com.inet.mail.data;

/**
 * FolderType
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 * 	Initialization folder type.
 * </pre>
 */
public enum FolderType {
	/**
	 * INBOX folder.
	 */
	INBOX,
	/**
	 * OUTBOX folder.
	 */
	OUTBOX,
	/**
	 * SENT folder.
	 */
	SENT,
	/**
	 * DRAFT folder.
	 */
	DRAFT,
	/**
	 * TRASH folder.
	 */
	TRASH,
	/**
	 * SPAM folder.
	 */
	SPAM,
	/**
	 * CUSTOM folder.
	 */
	CUSTOM,
	/**
	 * UNKNOWN folder.
	 */
	UNKNOWN
	
}