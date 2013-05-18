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
import java.util.ArrayList;
import java.util.List;

import com.inet.base.service.ErrorService;
import com.inet.mail.data.MailClauseOperator;

/**
 * NaryOperatorFilter
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 8, 2008
 * <pre>
 *  Initialization NaryOperatorFilter class.
 * </pre>
 */
public abstract class NaryOperatorFilter extends AbstractFilter {
	//-------------------------------------------------------------
	// Class members.
	//
	/**
	 * Operator filter class name.
	 */
	private static final String OPERATOR_FILTER_CLASS = "com.inet.mail.filter.xOperatorFilter" ;
	
	/**
	 * The list of filter.
	 */
	private List<AbstractFilter> filters = null ;
	
	/**
	 * Create NaryOperatorFilter instance.
	 */
	protected NaryOperatorFilter(){}
	
	/**
	 * Create NaryOperatorFilter instance.
	 * 
	 * @param filters List<AbstractFilter> - the given list of filter.
	 */
	protected NaryOperatorFilter(List<AbstractFilter> filters){
		this.filters = filters ;
	}
	
	/**
	 * @return the list of filter.
	 */
	public List<AbstractFilter> getFilters() {
		return this.filters ;
	}
	
	/**
	 * Add new filter.
	 * 
	 * @param filter AbstractFilter - the given filter to be added to.
	 */
	public void addFilter(AbstractFilter filter){
		if(this.filters == null){
			this.filters = new ArrayList<AbstractFilter>() ;
		}
		
		// add filter to store.
		this.filters.add(filter) ;
	}
	
	/**
	 * Add filters.
	 * 
	 * @param filters List<AbstractFilter> - the given list of filters.
	 */
	public void addFilter(List<AbstractFilter> filters){
		if(this.filters == null){
			this.filters = new ArrayList<AbstractFilter>() ;
		}
		
		// add all filters.
		this.filters.addAll(filters) ;
	}	
	
	/**
	 * Get filter from the given MailClauseOperator instance.
	 * 
	 * @param operator MailClauseOperator - the given mail clause operator.
	 * @return the NaryOperatorFilter.
	 */
	public static NaryOperatorFilter getFilter(final MailClauseOperator operator){
		if(System.getSecurityManager() == null){
			return NaryOperatorFilter.createFilter(operator) ;
		}
		
		// create filter under security environment.
		return AccessController.doPrivileged(new PrivilegedAction<NaryOperatorFilter>(){
			/**
			 * @see java.security.PrivilegedAction#run()
			 */
			public NaryOperatorFilter run() {
				return NaryOperatorFilter.createFilter(operator);
			}
		});
	}

	/**
	 * Get filter from the given MailClauseOperator instance and list of filters.
	 * 
	 * @param operator MailClauseOperator - the given mail clause operator.
	 * @param filters List<AbstractFilter> - the given list of filters.
	 * @return the NaryOperatorFilter.
	 */
	public static NaryOperatorFilter getFilter(final MailClauseOperator operator, 
						final List<AbstractFilter> filters){
		if(System.getSecurityManager() == null){
			return NaryOperatorFilter.createFilter(operator, filters) ;
		}
		
		// create filter under security environment.
		return AccessController.doPrivileged(new PrivilegedAction<NaryOperatorFilter>(){
			/**
			 * @see java.security.PrivilegedAction#run()
			 */
			public NaryOperatorFilter run() {
				return NaryOperatorFilter.createFilter(operator, filters);
			}
		});		
	}
	//---------------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Create NaryOperatorFilter from the given MailClauseOperator.
	 * 
	 * @param operator MailClauseOperator - the given MailClauseOperator instance.
	 */
	@SuppressWarnings("unchecked")
	private static NaryOperatorFilter createFilter(MailClauseOperator operator){
		// get operator filter class.
		String operatorClass = NaryOperatorFilter.OPERATOR_FILTER_CLASS.replaceAll("[x]", operator.getValue()) ;
		
		try {
			// find operator class instance.
			Class<NaryOperatorFilter> nOperatorFilterClass 
				= (Class<NaryOperatorFilter>)Class.forName(operatorClass) ;
			
			// get default constructor.
			Constructor<NaryOperatorFilter> constructor
				= nOperatorFilterClass.getConstructor(new Class[0]) ;
			
			// create new instance.
			return constructor.newInstance(new Object[0]) ;
		} catch (ClassNotFoundException cnfex) {
			// log error.
			ErrorService.logError(cnfex.getMessage(), NaryOperatorFilter.class, cnfex) ;
		} catch (SecurityException sex) {
			// log error.
			ErrorService.logError(sex.getMessage(), NaryOperatorFilter.class, sex) ;
		} catch (NoSuchMethodException nsmex) {
			// log error.
			ErrorService.logError(nsmex.getMessage(), NaryOperatorFilter.class, nsmex) ;
		} catch (IllegalArgumentException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), NaryOperatorFilter.class, iaex) ;
		} catch (InstantiationException iex) {
			// log error.
			ErrorService.logError(iex.getMessage(), NaryOperatorFilter.class, iex);
		} catch (IllegalAccessException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), NaryOperatorFilter.class, iaex) ;
		} catch (InvocationTargetException itex) {
			// log error.
			ErrorService.logError(itex.getMessage(), NaryOperatorFilter.class, itex) ;
		}
		
		// return or operator.
		return new OrOperatorFilter() ;
	}
	
	/**
	 * Create NaryOperatorFilter from the given MailClauseOperator.
	 * 
	 * @param operator MailClauseOperator - the given MailClauseOperator instance.
	 * @param filters List<AbstractFilter> - the given list of filters.
	 */
	@SuppressWarnings("unchecked")
	private static NaryOperatorFilter createFilter(MailClauseOperator operator,
			List<AbstractFilter> filters){
		// get operator filter class.
		String operatorClass = NaryOperatorFilter.OPERATOR_FILTER_CLASS.replaceAll("[x]", operator.getValue()) ;
		
		try {
			// find operator class instance.
			Class<NaryOperatorFilter> nOperatorFilterClass 
				= (Class<NaryOperatorFilter>)Class.forName(operatorClass) ;
			
			// get default constructor.
			Constructor<NaryOperatorFilter> constructor
				= nOperatorFilterClass.getConstructor(new Class[]{List.class}) ;
			
			// create new instance.
			return constructor.newInstance(new Object[]{filters}) ;
		} catch (ClassNotFoundException cnfex) {
			// log error.
			ErrorService.logError(cnfex.getMessage(), NaryOperatorFilter.class, cnfex) ;
		} catch (SecurityException sex) {
			// log error.
			ErrorService.logError(sex.getMessage(), NaryOperatorFilter.class, sex) ;
		} catch (NoSuchMethodException nsmex) {
			// log error.
			ErrorService.logError(nsmex.getMessage(), NaryOperatorFilter.class, nsmex) ;
		} catch (IllegalArgumentException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), NaryOperatorFilter.class, iaex) ;
		} catch (InstantiationException iex) {
			// log error.
			ErrorService.logError(iex.getMessage(), NaryOperatorFilter.class, iex);
		} catch (IllegalAccessException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), NaryOperatorFilter.class, iaex) ;
		} catch (InvocationTargetException itex) {
			// log error.
			ErrorService.logError(itex.getMessage(), NaryOperatorFilter.class, itex) ;
		}
		
		// return or operator.
		return new OrOperatorFilter(filters) ;		
	}	
}
