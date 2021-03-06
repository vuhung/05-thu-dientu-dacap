/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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

import java.util.Map;

import com.inet.web.util.ForwardPath;

/**
 * WebOSAction.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class WebOSAction extends AbstractWebOSActionController{

	/**
	 * @see com.inet.web.core.struts.action.AbstractAction#getForward(java.util.Map)
	 */
	@Override
	protected ForwardPath getForward(Map<String, ForwardPath> arg0) {
		return ForwardPath.SUCCESS;
	}

}
