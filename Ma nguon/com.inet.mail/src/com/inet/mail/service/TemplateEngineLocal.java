/*****************************************************************
 Copyright 2007 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.service;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.PairValueDTO;

/**
 * 
 * TemplateEngineLocal.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
public interface TemplateEngineLocal {
	/**
	 * Generate data from templete using VELOCITY engine
	 * 
	 * @param templateID
	 * @param templateName
	 * @param params
	 * @return
	 * @throws EJBException
	 */
	public String makeupContent(Long templateID, 
			String templateName,
			List<PairValueDTO<String>> params) throws EJBException;
	
	/**
	 * Generate data from message using VELOCITY engine
	 * 
	 * @param content
	 * @param params
	 * @return
	 * @throws EJBException
	 */
	public String makeupContent(String content,List<PairValueDTO<String>> params) throws EJBException;
}
