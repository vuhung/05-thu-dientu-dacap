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
package com.inet.web.controller.secure;

import java.util.Map;

import com.inet.web.core.WebDataScope;
import com.inet.web.core.struts.action.AbstractWebOSAction;
import com.inet.web.exception.WebOSException;
import com.inet.web.language.LanguageSupport;
import com.inet.web.util.ForwardPath;

/**
 * WebOSLoginAction
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: WebOSLoginAction.java 2008-12-02 10:55:05Z nguyen_dv $
 * 
 * Create date: Dec 2, 2008
 * <pre>
 *  Initialization WebOSLoginAction class.
 * </pre>
 */
public class WebOSLoginAction extends AbstractWebOSAction {
	// Static fields ==========================================================
	/* the language bean. */
	private static final String LANGUAGE_BEAN = "language" ;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>WebOSLoginAction</tt> instance. 
	 */
	public WebOSLoginAction(){
		super(WebDataScope.REQUEST);
	}
	
	//~ Methods ===============================================================
	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getForward(java.util.Map)
	 */
	protected ForwardPath getForward(Map<String, ForwardPath> arg0) {
		return ForwardPath.SUCCESS;
	}

	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getLanguageSupport()
	 */
	protected LanguageSupport getLanguageSupport() throws WebOSException {
		return getBean(LANGUAGE_BEAN, LanguageSupport.class);
	}
}
