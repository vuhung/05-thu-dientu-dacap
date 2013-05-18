/*****************************************************************
 Copyright 2007 by Tung Luong (lqtung@truthinet.com)

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
 * DataMailDTO.
 * 
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
public class DataMailDTO extends MailDTO {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5972856743768516729L;
	private List<ByteDataSource> attach = null;
	private String[] sender = null;
	private String subject = null;
	private HashMap<String, String> listTo = null;
	private HashMap<String, String> listCC = null;
	private HashMap<String, String> listBCC = null;
	
	/**
	 * Constructor 
	 * @param sender: 2 string : email and name
	 * @param subject: subject of email
	 * @param listTo : a HashMap people receive email, one pair <email,userName> 
	 */
	public DataMailDTO(String sender[], String subject, HashMap<String, String> listTo) {		
		this.sender = sender;
		this.subject = subject;
		this.listTo = listTo;
	}
	
	/**
	 * Constructor
	 * @param sender : only email
	 * @param subject : subject of email
	 * @param listTo listTo : a hashmap people receive email, one pair <email,userName>
	 */
	public DataMailDTO(String sender, String subject, HashMap<String, String> listTo) {		
		this.sender = new String[2];
		this.sender[0] = sender;
		this.subject = subject;
		this.listTo = listTo;
	}
	
	@Override
	public String[] getSender() {
		return sender;
	}
	
	/**
	 * @see com.inet.mail.data.MailDTO#getSubject()
	 */
	@Override
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @see com.inet.mail.data.MailDTO#getTo()
	 */
	@Override
	public HashMap<String, String> getTo() {
		return listTo;
	}
	/**
	 * @see com.inet.mail.data.MailDTO#getAttached()
	 */
	@Override
	public List<ByteDataSource> getAttached() {
		return attach;
	}

	/**
	 * @see com.inet.mail.data.MailDTO#getBody()
	 */
	@Override
	public String getBody() {
		return super.getBody();
	}

	@Override
	public HashMap<String,String> getCC() {
		return listCC;
	}

	/**
	 * @see com.inet.mail.data.MailDTO#getBCC()
	 */
	@Override
	public HashMap<String,String> getBCC() {
		return listBCC;
	}
	/**
	 * @see com.inet.mail.data.MailDTO#getReceiverInfo()
	 */
	@Override
	public Hashtable<String, ReceiverDTO> getReceiverInfo() {
		return null;
	}

	/**
	 * @see com.inet.mail.data.MailDTO#getVariables()
	 */
	@Override
	public List<PairValueDTO<String>> getVariables() {
		return null;
	}

	public void setAttach(List<ByteDataSource> attach) {
		this.attach = attach;
	}

	public void setListBCC(HashMap<String, String> listBCC) {
		this.listBCC = listBCC;
	}

	public void setListCC(HashMap<String, String> listCC) {
		this.listCC = listCC;
	}

	public void setListTo(HashMap<String, String> listTo) {
		this.listTo = listTo;
	}

	public void setSender(String[] sender) {
		this.sender = sender;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
