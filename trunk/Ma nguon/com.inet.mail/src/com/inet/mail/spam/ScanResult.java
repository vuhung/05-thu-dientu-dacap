/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
 * ScanResult
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ScanResult.java 2009-01-07 18:02:00z nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization ScanResult class.
 * </pre>
 */
public interface ScanResult{
	/**
	 * Returns the spam probability.
	 * 
	 * @return A spam probability between 0.0 and 1.0
	 */
	double getProbability();
	
	/**
	 * @return if has some error occurs during scan spam.
	 */
	boolean hasErrors() ;
	
	/**
	 * @return the error message.
	 */
	String getErrors() ;
}
