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
package com.inet.mail.filter;

import java.util.List;

import com.inet.mail.persistence.MailHeader;

/**
 * AndOperatorFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 8, 2008
 * <pre>
 *  Initialization AndOperatorFilter class.
 * </pre>
 */
public class AndOperatorFilter extends NaryOperatorFilter {
	/**
	 * Create AndOperatorFilter
	 */
	public AndOperatorFilter() {}

	/**
	 * Create AndOperatorFilter from the given list of filter.
	 * 
	 * @param filters List<AbstractFilter> - the given list of filters.
	 */
	public AndOperatorFilter(List<AbstractFilter> filters) {
		super(filters);
	}
	
	/**
	 * @see com.inet.mail.filter.AbstractFilter#evaluate(com.inet.mail.persistence.MailHeader)
	 */
	public boolean evaluate(MailHeader header) {
		if(this.getFilters() == null) return false ;
		
		// visit all filter and check and condition.
		for(AbstractFilter filter : this.getFilters()){
			if(!filter.evaluate(header)) return false ;
		}
		
		return true ;
	}
}
