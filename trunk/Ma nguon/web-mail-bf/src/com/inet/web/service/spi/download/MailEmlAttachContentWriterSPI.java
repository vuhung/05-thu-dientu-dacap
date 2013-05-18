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
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.jsr305.Nonnull;
import com.inet.base.logging.INetLogger;
import com.inet.base.service.FileService;
import com.inet.base.service.StringService;
import com.inet.grid.data.DataFormat;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.MailConfigureFactory;
import com.inet.mail.parser.AttachmentParser;
import com.inet.mail.parser.IMessageComposer;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

/**
 * MailEmlAttachContentWriterSPI.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailEmlAttachContentWriterSPI.java Aug 23, 2011 3:03:15 PM Tan Truong $
 *
 * @since 1.0
 */
public class MailEmlAttachContentWriterSPI extends AbstractContentWriterSPI {
  /* class logger. */
  private static final INetLogger logger = INetLogger.getLogger(MailEmlAttachContentWriterSPI.class) ;
  private MailBridgeBO bridgeBO;
  /* document style */
  
  //~ Methods ===============================================================
  /**
   * Create <tt>MailEmlAttachContentWriterSPI</tt> instance from the given <tt>MailBridgeBO</tt>
   * 
   * @param bridgeBO bridgeBO - the given <tt>MailBridgeBO</tt> instance.
   */
  public MailEmlAttachContentWriterSPI(@Nonnull MailBridgeBO bridgeBO) {
          this.bridgeBO = bridgeBO;
  }
  
  /*
   * @see com.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.inet.web.spi.download.ContentWriterType)
   */
  @Override
  public void write(HttpServletRequest request, HttpServletResponse response, ContentWriterType arg2)
      throws WebOSException {
    String headerId = request.getParameter(MailConstants.CONTENT_ID);
    String fileindex = request.getParameter(MailConstants.ATTACHMENT_KEY);
    String emlkey = request.getParameter(MailConstants.EML_ATTACHMENT_KEY);
    
    System.out.println(headerId + "--" + fileindex + "--" + emlkey );
    if(!StringService.hasLength(headerId) 
        || !StringService.hasLength(fileindex)
        || !StringService.hasLength(emlkey)){ 
      throw (new WebOSException("could not load content attach."));
    }
    
    long id = Long.valueOf(headerId);
    // get attach EML
    byte[] emlData = bridgeBO.getAttachment(id, emlkey).getValue();
    
    if(emlData != null  && emlData.length >0){
      // get mail configuration.
      AbstractMailFactory factory = MailConfigureFactory.createFactory();                            
      
      // create message composer.
      IMessageComposer composer = factory.createMessage(emlData, true, true);
      
      Map<String, byte[]> map = composer.getAttachments();
      
      for(String s: map.keySet()){
    	  System.out.println(s);
      }
      
      // data attachment
      byte[] data = map.get(fileindex); 
      
      // write to response.
      prepareResponse(response) ;
      
      // send data to sub-system.
      ServletOutputStream outputStream = null;
      
      if(data == null ){
          throw (WebOSException)(new WebOSException("could not load content attach."));
      }
      
      String fileName = AttachmentParser.getFileName(fileindex);
      String extension = FileService.getExtension(fileName);
      DataFormat format = DataFormat.getDataformat(extension);
      
      if(format.equals(DataFormat.UNKNOWN)){
        // write to response.
        writerResponse(response, data.length,  "application/octet-stream" ,fileName, true);                                         
      }else{
        // write to response.
        writerResponse(response, data.length, format.getMime(), fileName, true);
      }
    
      try {
        outputStream = response.getOutputStream() ;
        // write data to response.
        outputStream.write(data) ;
        outputStream.flush() ; 
      } catch (IOException ioex) {
        // show logging.
        logger.warning("could not write data to response: [" + ioex.getMessage() + "]", ioex) ;
      }finally {
        try {
          if(outputStream != null) {
                  outputStream.close() ;
          }
        }catch(Exception ex) {}
      }
    }
  }
}