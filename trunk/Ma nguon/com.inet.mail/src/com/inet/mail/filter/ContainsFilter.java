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

import com.inet.mail.data.FilterOperator;
import com.inet.mail.data.MailFilterClauseUnit;

/**
 * ContainsFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization ContainsFilter class.
 * </pre>
 */
public class ContainsFilter extends CompareFilter {
	/**
	 * Create ContainsFilter from the given filter clause.
	 * 
	 * @param filterClause MailFilterClauseUnit - the given filter clause.
	 */
	public ContainsFilter(MailFilterClauseUnit filterClause){
		super(filterClause) ;
	}
	
	/**
	 * @see com.inet.mail.filter.CompareFilter#performCompare(java.lang.String)
	 */
	protected boolean performCompare(String data) {
		if(!FilterOperator.CONTAINS.equals(this.getFilterClause().getOperator())) return false ;
		
		// get the filter data.
		String filterData = this.getFilterClause().getData() ;
		
		// check the data.
		return data.contains(filterData);
	}
}
