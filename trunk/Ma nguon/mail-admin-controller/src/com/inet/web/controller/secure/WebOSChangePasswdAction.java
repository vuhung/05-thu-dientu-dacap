/*****************************************************************
   Copyright 2006 by Tung Luong (lqtung@truthinet.com)

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
package com.inet.web.controller.secure;

import java.util.Map;

import com.inet.web.controller.bean.passwd.WebOSChangePasswdBean;
import com.inet.web.core.WebDataScope;
import com.inet.web.core.struts.action.AbstractWebOSAction;
import com.inet.web.exception.WebOSException;
import com.inet.web.language.LanguageSupport;
import com.inet.web.util.ForwardPath;

/**
 * WebOSChangePasswdAction.
 * 
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 1.0i
 * 
 * @date Dec 2, 2008
 * <pre>
 *  Change the password action class.
 * </pre>
 */
public class WebOSChangePasswdAction extends AbstractWebOSAction {
	/**
	 * info context bean.
	 */
	private static final String CHANGE_PASSWD_BEAN = "changePasswdBean" ;	
	
	/**
	 * the language bean.
	 */
	private static final String LANGUAGE_BEAN = "language" ;
	
	/**
	 * Create <tt>WebOSChangepwAction</tt> instance.
	 */
	public WebOSChangePasswdAction() {
		this(WebDataScope.UNKNOWN) ;
	}
	
	/**
	 * Create <tt>WebOSChangePasswdAction</tt> instance from
	 * the given <tt>WebDataScope</tt> instance.
	 * 
	 * @param scope WebDataScope - the given <tt>WebDataScope</tt> instance.
	 * otherwise <code>false</code>
	 */
	protected WebOSChangePasswdAction(WebDataScope scope) {
		super(scope);
		// add change password info bean.
		addBusiness(getBean(CHANGE_PASSWD_BEAN, WebOSChangePasswdBean.class)) ;
	}
	
	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getLanguageSupport()
	 */
	protected LanguageSupport getLanguageSupport() throws WebOSException {
		return getBean(LANGUAGE_BEAN, LanguageSupport.class);
	}
	
	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getForward(java.util.Map)
	 */
	protected ForwardPath getForward(Map<String, ForwardPath> forwards) {
		return ForwardPath.SUCCESS;
	}
}
