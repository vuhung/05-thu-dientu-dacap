/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.web.bo.mail;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.data.FollowUpDTO;
import com.inet.web.application.exception.ApplicationServerException;
import com.inet.web.bf.mail.FollowUpBF;
import com.inet.web.bo.AbstractWebOSBO;
import com.inet.web.bo.exception.WebOSBOException;

/**
 * FollowUpBO.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class FollowUpBO extends AbstractWebOSBO<FollowUpBF> {

	/**
	 * FollowUpBO constructor
	 * 
	 * @param businessFacade FollowUpBF
	 */
	protected FollowUpBO(FollowUpBF businessFacade) {
		super(businessFacade);
	}
	
	/**
	 * load all follow up
	 * @return List<FollowUpDTO>
	 * @throws WebOSBOException if an error occurs during load all follow up.
	 */
	public List<FollowUpDTO> loadAll() throws WebOSBOException{
		try {
			return this.getBusinessFacade().getFacade().loadAll();
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException("Error while load follow up", asEx);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("Error while load follow up", ejbEx);
		}catch (Exception ex){
			throw translateException(ex);
		}
	}
	
	
	/**
	 * delete follow up
	 * @param id - the given identifier of follow up 
	 * @throws WebOSBOException if an error occurs during delete follow up.
	 */
	public void delete(List<Long> ids) throws WebOSBOException{
		try {
			this.getBusinessFacade().getFacade().delete(ids);
		} catch (ApplicationServerException asEx) {
			throw new WebOSBOException("Error while delete follow up", asEx);
		} catch (EJBException ejbEx) {
			throw new WebOSBOException("Error while delete follow up", ejbEx);
		}catch (Exception ex){
			throw translateException(ex);
		}
	}
}
