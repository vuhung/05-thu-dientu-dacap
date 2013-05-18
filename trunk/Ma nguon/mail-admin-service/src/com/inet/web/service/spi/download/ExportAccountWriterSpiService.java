/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *****************************************************************/
package com.inet.web.service.spi.download;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.ldap.PageResult;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.lotus.org.permission.manage.ldap.ILdapUserManager;
import com.inet.lotus.org.permission.manage.ldap.LdapUserSearchBean;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.utils.ExportUtils;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

/**
 * ExportAccountWriterSpiService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ExportAccountWriterSpiService.java Apr 6, 2011 9:21:02 PM Tan Truong $
 *
 * @since 1.0
 */
public class ExportAccountWriterSpiService extends AbstractContentWriterSPI {
  private static INetLogger LOGGER = INetLogger.getLogger(ExportAccountWriterSpiService.class);
  //The domain identify parameter
  private static final String ORG = "id";
  private static final String GROUP = "group";
  
  private ILdapUserManager userManager;
  
  /**
   * Create <code>ExportContactWriterSpiService</code> instance
   * 
   * @param globalContactManager LdapGlobalContactManager - the LDAP global contact manager
   */
  public ExportAccountWriterSpiService(ILdapUserManager userManager) {
    this.userManager = userManager;
  }

  /* (non-Javadoc)
   * @see com.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.inet.web.spi.download.ContentWriterType)
   */
  @Override
  public void write(HttpServletRequest request, HttpServletResponse response,
                          ContentWriterType type) throws WebOSException {
    // get org name
    String org = request.getParameter(ORG);
    String group = request.getParameter(GROUP);
    if(StringService.hasLength(org)){
      throw new WebOSException("Organization could not be null");
    }

    try {
      LdapUserSearchBean searchBean = new LdapUserSearchBean(0, 10, null);
      searchBean.setOrganization(org);
      searchBean.setGroup(StringService.hasLength(group)?group:null);
      
      // find all user of given domain
      PageResult<LdapUser> wrapper = this.userManager.pagination(searchBean);
      
      if(wrapper.getSize() > 10){
        searchBean = new LdapUserSearchBean(0, wrapper.getSize(), null);
        searchBean.setOrganization(org);
        searchBean.setGroup(StringService.hasLength(group)?group:null);
        
        wrapper = this.userManager.pagination(searchBean);
      }
      
      byte[] data = ExportUtils.exportAccount(wrapper.getData(), org);
      
      // write to response.
      prepareResponse(response);
      
      // write to response.
      writerResponse(response, data.length,  "application/vnd.ms-excel" ,"user_"+ org + ".xls", true);                                         
     
      ServletOutputStream outputStream = response.getOutputStream() ;
      // write data to response.
      outputStream.write(data) ;
      outputStream.flush() ;
    } catch (IOException ex) {
      LOGGER.error("Error while export account", ex);
    } catch (LotusException ex) {
      LOGGER.error("Error while export account", ex);
    }
  }

}
