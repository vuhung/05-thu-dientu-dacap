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
package com.inet.web.bo;

import com.inet.web.facade.AbstractEJBFacade;

/**
 * AbstractWebOSBO.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 9, 2008
 * <pre>
 *  Initialization AbstractWebOSBO class.
 * </pre>
 */
public abstract class AbstractWebOSBO<T extends AbstractEJBFacade<?>>{
	private T businessFacade ;
	
	/**
	 * Create <tt>AbstractWebOSBO</tt> from the given business facade.
	 * 
	 * @param businessFacade T - the given business facade.
	 */
	protected AbstractWebOSBO(T businessFacade) {
		this.businessFacade = businessFacade ;
	}
	
	/**
	 * @return the business facade instance.
	 */
	protected T getBusinessFacade() {
		return businessFacade ;
	}
}
