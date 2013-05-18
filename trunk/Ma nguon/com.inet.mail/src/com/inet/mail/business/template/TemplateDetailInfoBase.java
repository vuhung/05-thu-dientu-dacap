/*****************************************************************
 Copyright 2007 by hiennguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.business.template;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.base.BaseMailBusiness;
import com.inet.mail.data.TemplateStatus;
import com.inet.mail.persistence.TemplateDetailInfo;

/**
 * TemplateDetailInfoBase.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public interface TemplateDetailInfoBase extends
		BaseMailBusiness<TemplateDetailInfo> {
	/**
	 * Update template status
	 * @param bizId
	 * @param status
	 * @return
	 * @throws EJBException
	 */
	public TemplateDetailInfo update(Long bizId, TemplateStatus status) throws EJBException;
	
	/**
	 * Load all templates by status
	 * @param status
	 * @return
	 * @throws EJBException
	 */
	public List<TemplateDetailInfo> queryAll(TemplateStatus status) throws EJBException;
	
	/**
	 * Load all templates by status and owner
	 * 
	 * @param own
	 * @param status
	 * @return
	 * @throws EJBException
	 */
	public List<TemplateDetailInfo> query(String own, TemplateStatus status) throws EJBException;
	
	/**
	 * Load all templates by owner
	 * @param own
	 * @return
	 * @throws EJBException
	 */
	public List<TemplateDetailInfo> query(String own) throws EJBException;
	
	/**
	 * Load all system templates 
	 * 
	 * @return
	 * @throws EJBException
	 */
	public List<TemplateDetailInfo> query() throws EJBException;
	
	/**
	 * Load all system templates by status
	 * @param own
	 * @return
	 * @throws EJBException
	 */
	public List<TemplateDetailInfo> query(TemplateStatus status) throws EJBException;
}
