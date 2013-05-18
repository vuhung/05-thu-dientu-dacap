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
package com.inet.web.service.utils;

import com.inet.base.jsr305.Nonnull;
import com.inet.base.logging.INetLogger;
import com.inet.base.service.Assert;
import com.inet.web.application.WebApplicationContext;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.upload.UploadContentLocator;

/**
 * DocumentService.
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Aug 24, 2008
 * <pre>
 *  Initialization DocumentService class.
 * </pre>
 */
public abstract class DocumentHelper {
	//~ Static fields =========================================================
	// class logger.
	private static final INetLogger logger = INetLogger.getLogger(DocumentHelper.class) ;
	
	//~ Methods ===============================================================
	/**
	 * Get file content from the given file instance.
	 * 
	 * @param name the given content locator name.
	 * 
	 * @return the file content.
	 * @throws WebOSServiceException when error occurs during getting file content.
	 */
	public static byte[] getContent(@Nonnull String name) throws WebOSServiceException{
		Assert.hasLength(name, "content locator name must not be null or empty. {ASSERTION [line=51, file=com/inet/web/service/utils/DocumentHelper.java]}") ;
		
		try {
			if(logger.isDebugEnabled()) logger.debug("get upload content locator: [" + name + "].") ;
			
			// get content locator.
			UploadContentLocator contentLocator = WebApplicationContext.getInstance().getApplicationContext().getContentLocator() ;
			if(logger.isDebugEnabled()) logger.debug("content locator class: [" + contentLocator.getClass().getCanonicalName() + "]") ;
			
			// get content from name.
			byte[] content = contentLocator.get(name) ;
			if(content == null) return null ;
						
			return content ;
		}catch(Exception ex) {
			logger.error("could not get content locator from name: [" + name + "], message: [" + ex.getMessage() + "]", ex) ;
			
			// throw exception.
			throw (WebOSServiceException)(new WebOSServiceException("could not get content locator from name: [" + name + "], message: [" + ex.getMessage() + "].", ex)
											.setData(82, "com/inet/web/service/utils/DocumentHelper.java")) ;
		}
	}
	
	/**
	 * delete file content from the given file instance.
	 * 
	 * @param name the given content locator name.
	 *
	 * @throws WebOSServiceException when error occurs during delete file content.
	 */
	public static void deleteContent(@Nonnull String name) throws WebOSServiceException{
		Assert.hasLength(name, "content locator name must not be null or empty. {ASSERTION [line=75, file=com/inet/web/service/utils/DocumentHelper.java]}") ;
		
		try {
			if(logger.isDebugEnabled()) logger.debug("delete content locator: [" + name + "].") ;
			
			// get content locator.
			UploadContentLocator contentLocator = WebApplicationContext.getInstance().getApplicationContext().getContentLocator() ;
			if(logger.isDebugEnabled()) logger.debug("content locator class: [" + contentLocator.getClass().getCanonicalName() + "]") ;
			
			// get content from name.
			contentLocator.delete(name) ;			
		}catch(Exception ex) {
			logger.error("could not delete content locator from name: [" + name + "], message: [" + ex.getMessage() + "]", ex) ;
			
			// throw exception.
			throw (WebOSServiceException)(new WebOSServiceException("could not delete content locator from name: [" + name + "], message: [" + ex.getMessage() + "].", ex)
											.setData(82, "com/inet/web/service/utils/DocumentHelper.java")) ;
		}
	}
}
