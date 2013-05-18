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
package com.inet.web.service.spi.download;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.FileService;
import com.inet.base.service.StringService;
import com.inet.grid.data.DataFormat;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.utils.DocumentHelper;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

/**
 * MailReviewAttachContentWriterSPI.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailReviewAttachContentWriterSPI extends AbstractContentWriterSPI {
	
	/* class logger. */
	private static final INetLogger logger = INetLogger.getLogger(MailReviewAttachContentWriterSPI.class) ;
	private static final String ATTACH_NAME = "name";
	private static final String ATTACH_CODE = "code";
	
	//~ Methods ===============================================================
	/**
	 * Create <tt>DocumentReferenceContentWriterSPI</tt> instance from the given <tt>DocumentReferenceBO</tt>
	 * and <tt>DocumentContentBO</tt> instance.
	 * 
	 * @param referenceBO DocumentReferenceBO - the given <tt>DocumentReferenceBO</tt> instance.
	 * @param contentBO DocumentContentBO - the given <tt>DocumentContentBO</tt> instance.
	 */
	public MailReviewAttachContentWriterSPI() {}
	/**
	 * @see com.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.inet.web.spi.download.ContentWriterType)
	 */
	public void write(HttpServletRequest request, HttpServletResponse response,
			ContentWriterType type) throws WebOSException {
		String name = request.getParameter(ATTACH_NAME);
		String code = request.getParameter(ATTACH_CODE);
		if(!StringService.hasLength(name) || !StringService.hasLength(code)) throw (WebOSException)(new WebOSException("could not load content attach.").setData(66, "com/inet/web/service/spi/download/MailReviewAttachContentWriterSPI.java"));
		ServletOutputStream outputStream = null;
		try{
		// write to response.
			prepareResponse(response) ;
			
			// send data to sub-system.
			
			byte[] data = DocumentHelper.getContent(code);
			if(data == null || (data.length == 0)) throw (WebOSException)(new WebOSException("could not load content attach."));
			// send data to sub-system.
			outputStream = response.getOutputStream() ;
			
			String extension = FileService.getExtension(name);
			DataFormat format = DataFormat.getDataformat(extension);
			if(format.equals(DataFormat.UNKNOWN)){
				// write to response.
				writerResponse(response, data.length,  "application/octet-stream" ,name, true);						
			}else{
				// write to response.
				writerResponse(response, data.length, format.getMime(), name, true);
			}
			
			outputStream.write(data);
			outputStream.flush() ;
		}catch(SocketException sexp){
			logger.warning("could not write data to socket: [" + sexp.getMessage() + "]", sexp) ;
		} catch (IOException ioex) {
			// show logging.
			logger.warning("could not write data to response: [" + ioex.getMessage() + "]", ioex) ;
		}catch(WebOSBOException wbex) {
			// show information.
			logger.error("could not load mail attach : [" + wbex.getMessage() + "]", wbex) ;
			
			// throw exception.
			throw (WebOSException)(new WebOSException("could not load mail attach, message: [" + wbex.getMessage() + "]", wbex)
									 .setData(110, "com/inet/web/service/spi/download/MailAttachContentWriterSPI.java")) ;
		}finally {
			try {
				if(outputStream != null) {
					outputStream.close() ;
				}
			}catch(Exception ex) {}
		}
	}
}
