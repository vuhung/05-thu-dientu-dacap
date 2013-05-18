package com.inet.mail.demo;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.inet.mail.data.ByteDataSource;
import com.inet.mail.data.MailDTO;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.data.ReceiverDTO;

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

/**
 * @author hiennguyen
 *
 */
public class DataDTO extends MailDTO {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5972856743768516729L;
	List<ByteDataSource> attach = null;
	public DataDTO()
	{		
		//attach = new ArrayList<ByteDataSource>();
		//attach.add(new ByteDataSource(IOService.readToBytes(new File("/home/hiennguyen/Interface.txt")),"Interface0.txt"));
		//attach.add(new ByteDataSource(IOService.readToBytes(new File("/home/hiennguyen/Interface.txt")),"Interface1.txt"));
	}
	
	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getAttached()
	 */
	@Override
	public List<ByteDataSource> getAttached() {
		return attach;
	}

	
	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getBody()
	 */
	@Override
	public String getBody() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<p>Yes, here is picture...");
		buffer.append("<img src=\"http://localhost:8080/zfocus/review/test.vsf\" width=1 height=1></p>");
		
		setBody(buffer.toString());
		return super.getBody();
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getCC()
	 */
	@Override
	public HashMap<String,String> getCC() {
		HashMap<String,String> list = new HashMap<String,String>(1);		
		list.put("hiennguyen@truthinet.com",null);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getSender()
	 */
	public String[] getSender() {
		String[] sender = new String[2];
		sender[0] = "hiennguyen@truthinet.com";
		sender[1] = "Hien Nguyen";
		
		return sender;
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getSubject()
	 */
	public String getSubject() {		
		return "Testing...";
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getTo()
	 */
	public HashMap<String,String> getTo() {
		HashMap<String,String> list = new HashMap<String,String>(1);		
		list.put("hiennguyen@truthinet.com",null);
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getReceiverInfo()
	 */
	@Override
	public Hashtable<String, ReceiverDTO> getReceiverInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.inet.mail.data.MailDTO#getVariables()
	 */
	@Override
	public List<PairValueDTO<String>> getVariables() {
		return null;
	}
}
