/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.util.MailService;

/**
 * ViewContentServlet.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public abstract class ViewContentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6640966478688904416L;

	/**
     * Returns a PDF, RTF or HTML document.
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
		// we retrieve the content view type
        String contentType = request.getParameter("type");
        String[] type = MailService.getAttachType(contentType);
        
        // 1. set data type return to client in the browser
        response.setContentType(type[1]);
        
        // 2. set some cache parameter
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        // 3. get content by the parameter
        try {
        	byte[] data = getContent(request);
			
        	// return this data back to client
	        ByteArrayOutputStream bytsOutput = new ByteArrayOutputStream();
	        response.setContentLength(data.length);
	        ServletOutputStream out = response.getOutputStream();
	        bytsOutput.writeTo(out);
	        out.flush();
	        
	        bytsOutput.close();
		} catch (EJBException e) {
			// TODO Auto-generated catch block
			// the error will be display here;
		}    
    }
	
	/**
	 * Implement this function to get your right content view
	 * @return
	 * @throws EJBException
	 */
	protected abstract byte[] getContent(HttpServletRequest request) throws EJBException;
}
