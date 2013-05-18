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

import com.inet.mail.data.FilterObject;
import com.inet.mail.data.FilterOperator;
import com.inet.mail.data.MailClauseOperator;
import com.inet.mail.data.MailFilterClauseUnit;

import junit.framework.TestCase;

/**
 * TestCompareFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization TestCompareFilter class.
 * </pre>
 */
public class TestCompareFilter extends TestCase {
	/**
	 * Test CompareFilter instance.
	 */
	public void testCompareFilter(){
		// create mail filter clause unit.
		MailFilterClauseUnit filterClause = 
			new MailFilterClauseUnit(FilterObject.SUBJECT, FilterOperator.STARTS_WITH, "hello") ;
		
		// create mail filter from the given filter clause.
		CompareFilter compareFilter = CompareFilter.getFilter(filterClause) ;
		
		// the filter must be start with filter.
		assertEquals(StartsWithFilter.class.getSimpleName(), compareFilter.getClass().getSimpleName()) ;
		
		// set the operator again.
		filterClause.setOperator(FilterOperator.IS_NOT) ;
		
		// get the filter.
		compareFilter = CompareFilter.getFilter(filterClause) ;
		
		// the filter must be is not filter.
		assertEquals(IsNotFilter.class.getSimpleName(), compareFilter.getClass().getSimpleName()) ;
	}
	
	/**
	 * Test operator filter.
	 */
	public void testOperatorFilter(){
		// create list of mail filter.
		List<AbstractFilter> filters = new ArrayList<AbstractFilter>() ;
		
		// get the OR operator filter.
		NaryOperatorFilter operatorFilter = NaryOperatorFilter.getFilter(MailClauseOperator.OR) ;
		
		// the filter must be OrOperatorFilter.
		assertEquals(OrOperatorFilter.class.getSimpleName(), operatorFilter.getClass().getSimpleName()) ;
		
		// get the AND operator filter.
		operatorFilter = NaryOperatorFilter.getFilter(MailClauseOperator.AND, filters) ;
		
		// the filter must be AndOperatorFilter.
		assertEquals(AndOperatorFilter.class.getSimpleName(), operatorFilter.getClass().getSimpleName()) ;
	}
}
