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
package com.inet.mail.data;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * 
 * MailDTO.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public abstract class MailDTO implements QueueData {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2456409505597003386L;
	
	private static final String HTML_HEADER= "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">"
	+ "<html>" 
	+ "<head>"
  	+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
  	+ "</head>"
  	+ "<body>";	
	private static final String HTML_END= "</body></html>";
	
	protected String bodyText;
	protected String smtpServer = null;
	
	public MailDTO()
	{		
	}
	
	public MailDTO(String smtpServer)
	{		
		this.smtpServer = smtpServer;
	}
	
	public String getSMTPServer()
	{
		return this.smtpServer;
	}
	public void setSMTPServer(String smtpServer)
	{
		this.smtpServer = smtpServer;
	}
	/**
	 * Get all receivers b
	 * 
	 * @return
	 */
	public Hashtable<String,ReceiverDTO> getReceiverInfo()
	{
		return null;
	}
	
	/**
	 * Get all receivers b
	 * 
	 * @return
	 */
	public List<PairValueDTO<String>> getVariables()
	{
		return null;
	}
	
	/**
	 * The message send to
	 * 
	 * @return
	 */
	public abstract HashMap<String,String> getTo();

	/**
	 * The message copy to
	 * 
	 * @return
	 */
	public HashMap<String,String> getCC()
	{
		return null;
	}

	/**
	 * The message make a blind copy to
	 * 
	 * @return
	 */
	public HashMap<String,String> getBCC()
	{
		return null;
	}

	/**
	 * WHo sends this message
	 * 
	 * @return
	 */
	public abstract String[] getSender();

	/**
	 * The content of message
	 * 
	 * @return
	 */
	public String getBody()
	{
		return this.bodyText;
	}

	/**
	 * Return in HTML format
	 * @return
	 */
	public String getHTML()
	{
		return HTML_HEADER + getBody() + HTML_END;
	}
	
	/**
	 * Set the body into this object
	 * 
	 * @param body
	 * @return
	 */
	public void setBody(String body)
	{
		this.bodyText = body;
	}
	
	/**
	 * Subject of message
	 * 
	 * @return
	 */
	public abstract String getSubject();

	/**
	 * The attached of message
	 * 
	 * @return
	 */
	public List<ByteDataSource> getAttached()
	{
		return null;
	}
}
