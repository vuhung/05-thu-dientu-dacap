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
package com.inet.mail.filter.object;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.inet.base.service.ErrorService;
import com.inet.mail.data.FilterObject;
import com.inet.mail.persistence.MailHeader;

/**
 * AbstractFilterObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization AbstractFilterObject class.
 * </pre>
 */
public abstract class AbstractFilterObject {
	/**
	 * The given filter object class.
	 */
	private static final String FILTER_OBJECT_CLASS = "com.inet.mail.filter.object.?FilterObject" ;
	
	/**
	 * Retrieved the data from the given mail header.
	 * 
	 * @param header MailHeader - the given mail header data instance.
	 * @return the data.
	 */
	public abstract String getData(MailHeader header) ;
	
	/**
	 * Get the filter object instance from the given FilterObject data.
	 * 
	 * @param filterObject FilterObject - the given FilterObject data.
	 * @return the AbstractFilterObject instance.
	 */
	public static AbstractFilterObject getFilterObject(final FilterObject filterObject){
		if(System.getSecurityManager() == null){
			return AbstractFilterObject.createFilterObject(filterObject) ;
		}
		
		// perform create AbstractFilterObject instance in security environment.
		return AccessController.doPrivileged(new PrivilegedAction<AbstractFilterObject>(){
			/**
			 * @see java.security.PrivilegedAction#run()
			 */
			public AbstractFilterObject run() {
				return AbstractFilterObject.createFilterObject(filterObject);
			}
		}) ;
	}
	
	//---------------------------------------------------------------------------
	// Helper function.
	//
	/**
	 * Create AbstractFilterObject instance from the given FilterObject instance.
	 * 
	 * @param filterObject FilterObject - the given FilterObject instance.
	 */
	@SuppressWarnings("unchecked")
	private static AbstractFilterObject createFilterObject(FilterObject filterObject){
		// get real filter object class.
		String filterClass = AbstractFilterObject.FILTER_OBJECT_CLASS.replaceAll("[?]", filterObject.getValue()) ;
		
		try {
			// get filter object class.
			Class<AbstractFilterObject> filterObjClass = (Class<AbstractFilterObject>)Class.forName(filterClass) ;
			
			// get constructor and create new instance.
			Constructor<AbstractFilterObject> constructor = filterObjClass.getConstructor(new Class[0]) ;
			
			// create new object and return to sub system.
			return constructor.newInstance(new Object[0]) ;
		} catch (ClassNotFoundException clnfex) {
			// log error.
			ErrorService.logError(clnfex.getMessage(), AbstractFilterObject.class, clnfex) ;
		} catch (SecurityException sex) {
			// log error.
			ErrorService.logError(sex.getMessage(), AbstractFilterObject.class, sex) ;
		} catch (NoSuchMethodException nsmex) {
			// log error.
			ErrorService.logError(nsmex.getMessage(), AbstractFilterObject.class, nsmex) ;
		} catch (IllegalArgumentException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), AbstractFilterObject.class, iaex) ;
		} catch (InstantiationException iex) {
			// log error.
			ErrorService.logError(iex.getMessage(), AbstractFilterObject.class, iex) ;
		} catch (IllegalAccessException iaex) {
			// log error.
			ErrorService.logError(iaex.getMessage(), AbstractFilterObject.class, iaex);
		} catch (InvocationTargetException itex) {
			// log error.
			ErrorService.logError(itex.getMessage(), AbstractFilterObject.class, itex) ;
		}
		
		// return all filter object.
		return new AllFilterObject() ;
	}
}
