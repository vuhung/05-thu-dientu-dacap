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
package com.inet.web.controller;

import java.util.Map;

import com.inet.web.util.ForwardPath;

/**
 * WebOSAction.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date Jul 31, 2008
 * <pre>
 *  Initialization WebOSAction class.
 * </pre>
 */
public class WebOSAction extends AbstractWebOSActionController {
	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getForward(java.util.Map)
	 */
	protected ForwardPath getForward(Map<String, ForwardPath> forward) {
		return ForwardPath.SUCCESS;
	}
}
