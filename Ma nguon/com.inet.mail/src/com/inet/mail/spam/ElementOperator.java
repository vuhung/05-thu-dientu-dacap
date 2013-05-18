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
 * ElementOperator
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ElementOperator.java 2009-01-10 15:41:30z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization ElementOperator class.
 * </pre>
 */
public enum ElementOperator {
	EQ, // equals
	LT, // lesser than
	LE, // lesser than or equal
	GT, // greater than
	GE, // greater than or equal
	CT, // contains
	SW, // start with
	EW  // end with
}
