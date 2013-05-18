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
package com.inet.mail.spam.support;

import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.mail.spam.ScanResult;

/**
 * ScanResultSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: ScanResultSupport.java Jan 7, 2009 nguyen_dv $
 * 
 * Create date: Jan 7, 2009
 * <pre>
 *  Initialization ScanResultSupport class.
 * </pre>
 */
public class ScanResultSupport implements ScanResult {
	//~ Static instances ======================================================
	/* the result probability. */
	private double probability = 0.0d;
	/* error values. */
	private StringBuffer errors ;
	
	//~ Methods ===============================================================	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanResult#getErrors()
	 */
	public String getErrors() {
		return (errors == null ? StringService.EMPTY_STRING : errors.toString());
	}
	
	/**
	 * Add errors message.
	 * 
	 * @param errors the given errors message.
	 */
	public void addErrors(String error) {
		if(errors == null){
			errors = new StringBuffer() ;
			errors.append(error) ;
		}else{
			errors.append(IOService.LINE_SEPARATOR).append(error) ;
		}
	}
	
	/**
	 * Clear errors value.
	 */
	public void clearErrors(){
		errors = null ;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanResult#getProbability()
	 */
	public double getProbability() {
		return probability;
	}
	
	/**
	 * Set the scan probability value.
	 * 
	 * @param probability the given probability value.
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanResult#hasErrors()
	 */
	public boolean hasErrors() {
		return (errors != null && errors.length() > 0);
	}
	
	/**
	 * Return the object representation as a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer() ;
		
		buffer.append(getClass().getName()).append("[")
			  .append("Probability: ").append(probability)
			  .append("; Has error: ").append((hasErrors() ? "YES" : "NO")) ;
		
		if(hasErrors()){
			buffer.append("; Errors: ").append(getErrors()) ;
		}
		
		buffer.append("]") ;
		
		// return value.
		return buffer.toString();
	}
}
