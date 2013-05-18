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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.inet.base.service.ErrorService;
import com.inet.mail.data.FilterObject;
import com.inet.mail.data.FilterOperator;
import com.inet.mail.data.MailFilterClauseUnit;
import com.inet.mail.filter.object.AbstractFilterObject;
import com.inet.mail.persistence.MailHeader;

/**
 * CompareFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization CompareFilter class.
 * </pre>
 */
public abstract class CompareFilter extends AbstractFilter {
	//--------------------------------------------------------------
	// Class members.
	//
	/**
	 * The compare filter class.
	 */
	private static final String COMPARE_FILTER_CLASS = "com.inet.mail.filter.?Filter" ;
	
	/**
	 * The mail filter clause unit.
	 */
	private MailFilterClauseUnit filterClause ;
	
	/**
	 * Create CompareFilter from the given filter clause.
	 * 
	 * @param filterClause MailFilterClauseUnit - the given mail filter clause unit instance.
	 */
	protected CompareFilter(MailFilterClauseUnit filterClause){
		this.filterClause = filterClause ;
	}
	
	/**
	 * @see com.inet.mail.filter.AbstractFilter#evaluate(com.inet.mail.persistence.MailHeader)
	 */
	@Override
	public boolean evaluate(MailHeader header) {
		// get the data from the given filter clause.
		String data = this.retrievedData(header, this.filterClause.getObject()) ;
		
		// perform compare object.
		return this.performCompare(data) ;
	}
	
	/**
	 * @return the mail filter clause unit.
	 */
	public MailFilterClauseUnit getFilterClause() {
		return this.filterClause;
	}	
	
	/**
	 * Get filter instance from the given filter clause.
	 * 
	 * @param filterClause MailFilterClauseUnit - the given MailFilterClauseUnit instance.
	 * @return the CompareFilter instance.
	 */
	public static CompareFilter getFilter(final MailFilterClauseUnit filterClause){
		// create compare filter instance from the none security environment.
		if(System.getSecurityManager() == null){
			return CompareFilter.createCompareFilter(filterClause) ;
		}
		
		// create compare filter in security environment.
		return AccessController.doPrivileged(new PrivilegedAction<CompareFilter>(){
			/**
			 * @see java.security.PrivilegedAction#run()
			 */
			public CompareFilter run() {
				return CompareFilter.createCompareFilter(filterClause);
			}
		}) ;
	}
	
	//--------------------------------------------------------------
	// Override functions.
	//
	/**
	 * Perform compare data and return value to subsystem.
	 * 
	 * @param data String - the given data value.
	 * @return <code>true</code> when the given data is matched the condition, otherwise <code>false</code>
	 */
	protected abstract boolean performCompare(String data) ;
	
	//---------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Retrieved the header data from the given filter object.
	 * 
	 * @param header MailHeader - the given mail header.
	 * @param object FilterObject - the given mail filter object.
	 */
	private String retrievedData(MailHeader header, FilterObject object){
		// get the AbstractFilterObject instance from the given FilterObject.
		AbstractFilterObject filterObject = AbstractFilterObject.getFilterObject(object) ;
		
		// return the data.
		return filterObject.getData(header) ;
	}
	
	/**
	 * Create CompareFilter from the given MailFilterClauseUnit instance.
	 * 
	 * @param filterClause MailFilterClauseUnit - the given MailFilterClauseUnit instance.
	 * @return the CompareFilter instance.
	 */
	@SuppressWarnings("unchecked")
	private static CompareFilter createCompareFilter(MailFilterClauseUnit filterClause){
		// get filter operator from the given filter clause unit.
		FilterOperator operator = filterClause.getOperator() ;
		
		// get the filter class name.
		String filterClass = CompareFilter.COMPARE_FILTER_CLASS.replaceAll("[?]", operator.getValue()) ;
		
		try {
			// get the compare filter class instance.
			Class<CompareFilter> compareFilterClass = (Class<CompareFilter>)Class.forName(filterClass) ;
			
			// get the constructor.
			Constructor<CompareFilter> constructor = compareFilterClass.getConstructor(
						new Class[]{MailFilterClauseUnit.class}
					) ;
			
			// create instance.
			return constructor.newInstance(new Object[]{filterClause}) ;
		} catch (ClassNotFoundException cnfex) {
			// log error.
			ErrorService.logError(cnfex.getMessage(), CompareFilter.class, cnfex) ;
		} catch (SecurityException sex) {
			// log error.
			ErrorService.logError(sex.getMessage(), CompareFilter.class, sex) ;
		} catch (NoSuchMethodException nsmex) {
			// log error.
			ErrorService.logError(nsmex.getMessage(), CompareFilter.class, nsmex) ;
		} catch (IllegalArgumentException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), CompareFilter.class, iaex) ;
		} catch (InstantiationException iex) {
			// log error.
			ErrorService.logError(iex.getMessage(), CompareFilter.class, iex) ;
		} catch (IllegalAccessException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), CompareFilter.class, iaex) ;
		} catch (InvocationTargetException itex) {
			// log error.
			ErrorService.logError(itex.getMessage(), CompareFilter.class, itex) ;
		}
		
		// return default instance.
		return new IsFilter(filterClause) ;
	}
}
