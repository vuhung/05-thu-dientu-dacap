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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.jsr305.Nonnull;
import com.inet.base.logging.INetLogger;
import com.inet.base.service.Assert;
import com.inet.base.service.DateService;
import com.inet.base.service.FileService;
import com.inet.base.service.StringService;
import com.inet.grid.data.DataFormat;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.parser.AttachmentParser;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

public class MailAttachContentWriterSPI extends AbstractContentWriterSPI {
	/* class logger. */
	private static final INetLogger logger = INetLogger.getLogger(MailAttachContentWriterSPI.class) ;
	private MailBridgeBO bridgeBO;
	/* document style */
	private static final String STYLE_PARAM = "style" ;
	//~ Methods ===============================================================
	/**
	 * Create <tt>DocumentReferenceContentWriterSPI</tt> instance from the given <tt>DocumentReferenceBO</tt>
	 * and <tt>DocumentContentBO</tt> instance.
	 * 
	 * @param referenceBO DocumentReferenceBO - the given <tt>DocumentReferenceBO</tt> instance.
	 * @param contentBO DocumentContentBO - the given <tt>DocumentContentBO</tt> instance.
	 */
	public MailAttachContentWriterSPI(@Nonnull MailBridgeBO bridgeBO) {
		Assert.isNotNull(bridgeBO, "the MailBridgeBO instance must not be null. {ASSERTION [line=50, file=com/inet/web/service/spi/download/MailAttachContentWriterSPI.java]}") ;
		this.bridgeBO = bridgeBO;
	}
	/**
	 * @see com.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.inet.web.spi.download.ContentWriterType)
	 */
	public void write(HttpServletRequest request, HttpServletResponse response,
			ContentWriterType type) throws WebOSException {
		
		String headerId = request.getParameter(MailConstants.CONTENT_ID);
		String key = request.getParameter(MailConstants.ATTACHMENT_KEY);
		if(!StringService.hasLength(headerId) || !StringService.hasLength(key)) throw (WebOSException)(new WebOSException("could not load content attach.").setData(59, "com/inet/web/service/spi/download/MailAttachContentWriterSPI.java"));
		
		long id = Long.valueOf(headerId);
		// get attach style.
		String style = request.getParameter(STYLE_PARAM) ;
		boolean download = !("view".equalsIgnoreCase(style)) ;
		
		try{
			// show information.
			if(logger.isDebugEnabled()) logger.debug("mail contentn identifier: [" + headerId + "].") ;
			// write to response.
			prepareResponse(response) ;
			
			// send data to sub-system.
			ServletOutputStream outputStream = null;
			if(key.equalsIgnoreCase("all")){
				Map<String, byte[]> mapData =  bridgeBO.getAttachment(id);
				if(mapData == null ) throw (WebOSException)(new WebOSException("could not load content attach."));
				// byte array out stream 
				ByteArrayOutputStream outByte = new ByteArrayOutputStream();
				ZipOutputStream outZip = new ZipOutputStream(outByte); 
				try {
					for(String fileName : mapData.keySet()){
						// zip entry
						ZipEntry zipAdd = new ZipEntry(AttachmentParser.getFileName(fileName));
				        //zipAdd.setTime(new Date().getTime());
						outZip.putNextEntry(zipAdd);
						outZip.write(mapData.get(fileName));
					}
					outZip.close();
					outByte.close();
					byte[] datazip = outByte.toByteArray();
					if(datazip == null || (datazip.length == 0)) throw (WebOSException)(new WebOSException("could not load content attach."));
					// send data to sub-system.
					outputStream = response.getOutputStream() ;
					// write to response.
					writerResponse(response,datazip.length , DataFormat.ZIP.getMime(), DateService.format(new Date(), DateService.VIETNAM_DATE_PATTERN) + ".zip", download) ;
					outputStream.write(datazip);
					outputStream.flush() ;
				}catch(SocketException sexp){
					logger.warning("could not write data to socket: [" + sexp.getMessage() + "]", sexp) ;
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
			}else{
				
				PairValueDTO<byte[]> pairValueDTO  = bridgeBO.getAttachment(id, key);
				
				if(pairValueDTO == null ) throw (WebOSException)(new WebOSException("could not load content attach."));
				
				byte[] data = pairValueDTO.getValue();
				
				if(data == null ) throw (WebOSException)(new WebOSException("could not load content attach."));
				// show information.
				if(logger.isDebugEnabled()) logger.debug("content length: [" + (data != null ? data.length : 0) + "]") ;
				
				try {
					String fileName = AttachmentParser.getFileName(key);
					String extension = FileService.getExtension(fileName);
					DataFormat format = DataFormat.getDataformat(extension);
					if(format.equals(DataFormat.UNKNOWN)){
						// write to response.
						writerResponse(response, data.length,  "application/octet-stream" ,fileName, download);						
					}else{
						// write to response.
						writerResponse(response, data.length, format.getMime(), fileName, download);
					}

					outputStream = response.getOutputStream() ;
					// write data to response.
					outputStream.write(data) ;
					outputStream.flush() ;				
				}catch(SocketException sexp){
					logger.warning("could not write data to socket: [" + sexp.getMessage() + "]", sexp) ;
				}catch(IOException ioex) {
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
		}catch(WebOSBOException wbex) {
			// show information.
			logger.error("could not load mail attach : [" + wbex.getMessage() + "]", wbex) ;
			
			// throw exception.
			throw (WebOSException)(new WebOSException("could not load mail attach, message: [" + wbex.getMessage() + "]", wbex)
									 .setData(167, "com/inet/web/service/spi/download/MailAttachContentWriterSPI.java")) ;
		}
		
	}
}
