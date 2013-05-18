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
package com.inet.web.controller.welcome;

import java.util.Map;

import com.inet.web.controller.mail.AbstractWebOSActionController;
import com.inet.web.util.ForwardPath;

/**
 * WebOSWelcomeAction.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Jul 20, 2008
 * <pre>
 *  Initialization WebOSWelcomeAction class.
 * </pre>
 */
public class WebOSWelcomeAction extends AbstractWebOSActionController {
	/**
	 * Create <tt>WebOSWelcomeAction</tt> instance.
	 */
	public WebOSWelcomeAction() {
		super();
	}
	
	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getForward(java.util.Map)
	 */
	protected ForwardPath getForward(Map<String, ForwardPath> forwards) {
		return ForwardPath.SUCCESS;
	}
}
