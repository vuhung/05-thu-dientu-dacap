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

import java.util.ArrayList;
import java.util.List;

import com.inet.mail.data.MailFilterClause;
import com.inet.mail.data.MailFilterClauseUnit;
import com.inet.mail.persistence.MailHeader;

/**
 * ClauseFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 8, 2008
 * <pre>
 *  Initialization ClauseFilter class.
 * </pre>
 */
public class ClauseFilter extends AbstractFilter {
	// the given mail filter clause.
	private AbstractFilter filter ;
	
	/**
	 * Create ClauseFilter from the given MailFilterClause instance.
	 * 
	 * @param filterClause MailFilterClause - the given mail clause filter.
	 */
	public ClauseFilter(MailFilterClause filterClause){
		this.analyzerFilter(filterClause) ;
	}
	
	/**
	 * @see com.inet.mail.filter.AbstractFilter#evaluate(com.inet.mail.persistence.MailHeader)
	 */
	public boolean evaluate(MailHeader header) {
		if(this.filter == null) return true ;
		
		// evaluate the filter.
		return this.filter.evaluate(header) ;
	}
	
	//---------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Analyzer filter.
	 * 
	 * @param filterClause MailFilterClause - the given mail filter clause.
	 */
	private void analyzerFilter(MailFilterClause filterClause){
		// get list of MailFilterClauseUnit.
		List<MailFilterClauseUnit> clauseUnits = filterClause.getFilterClauses() ;
		
		// create list of filter.
		List<AbstractFilter> filters = new ArrayList<AbstractFilter>(clauseUnits.size()) ;
		
		// parse mail filter.
		for(MailFilterClauseUnit clauseUnit : clauseUnits){
			// create compare filter.
			CompareFilter filter = CompareFilter.getFilter(clauseUnit) ;
			
			// add filter to storage.
			filters.add(filter) ;
		}
		
		// create operator filter.
		this.filter = NaryOperatorFilter.getFilter(filterClause.getOperator(), filters) ;
	}
}
