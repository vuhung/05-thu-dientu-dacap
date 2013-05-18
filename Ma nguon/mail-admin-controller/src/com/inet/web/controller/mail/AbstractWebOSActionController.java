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
package com.inet.web.controller.mail;

import com.inet.web.controller.bean.mail.WebOSLoginInfoBean;
import com.inet.web.core.WebDataScope;
import com.inet.web.core.struts.action.AbstractWebOSAction;
import com.inet.web.exception.WebOSException;
import com.inet.web.language.LanguageSupport;

/**
 * AbstractWebOSActionController.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 20, 2008
 * <pre>
 *  Initialization AbstractWebOSActionController class.
 * </pre>
 */
public abstract class AbstractWebOSActionController extends AbstractWebOSAction {
	/**
	 * info context bean.
	 */
	private static final String LOGIN_INFO_BEAN = "webosLoginInfoBean" ;	
	
	/**
	 * the language bean.
	 */
	private static final String LANGUAGE_BEAN = "language" ;
	
	/**
	 * Create <tt>AbstractWebOSActionController</tt> instance.
	 */
	protected AbstractWebOSActionController() {
		this(WebDataScope.UNKNOWN) ;
	}
	
	/**
	 * Create <tt>AbstractWebOSActionController</tt> instance from
	 * the given <tt>WebDataScope</tt> instance.
	 * 
	 * @param scope WebDataScope - the given <tt>WebDataScope</tt> instance.
	 * otherwise <code>false</code>
	 */
	protected AbstractWebOSActionController(WebDataScope scope) {
		super(scope);
		// add get login info bean.
		addBusiness(getBean(LOGIN_INFO_BEAN, WebOSLoginInfoBean.class)) ;
	}

	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getLanguageSupport()
	 */
	protected LanguageSupport getLanguageSupport() throws WebOSException {
		return getBean(LANGUAGE_BEAN, LanguageSupport.class);
	}
}
