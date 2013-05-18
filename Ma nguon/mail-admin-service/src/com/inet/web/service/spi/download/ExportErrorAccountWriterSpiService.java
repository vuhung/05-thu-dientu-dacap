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
import com.inet.lotus.core.LotusException;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.utils.ExportUtils;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

/**
 * ExportAccountErrorWriterSpiService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ExportAccountErrorWriterSpiService.java Apr 22, 2011 12:59:24 AM Tan Truong $
 *
 * @since 1.0
 */
public class ExportErrorAccountWriterSpiService extends AbstractContentWriterSPI {
  private static final String KEY = "id";
  private static INetLogger LOGGER = INetLogger.getLogger(ExportErrorAccountWriterSpiService.class);
  /**
   * @see com.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.inet.web.spi.download.ContentWriterType)
   */
  @Override
  public void write(HttpServletRequest request, HttpServletResponse response, ContentWriterType type)
      throws WebOSException {
    // get org name
    String key = request.getParameter(KEY);
    if(!StringService.hasLength(key)){
      throw new WebOSException("KEY could not be null");
    }

    try {
      byte[] data = ExportUtils.exportErrorAccount(key);
      
      // write to response.
      prepareResponse(response);
      
      // write to response.
      writerResponse(response, data.length,  "application/vnd.ms-excel" ,"ErrorAccount.xls", true);                                         
     
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
