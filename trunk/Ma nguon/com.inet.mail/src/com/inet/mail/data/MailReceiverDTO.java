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
package com.inet.mail.data;

import java.util.ArrayList;
import java.util.List;

import com.inet.base.msg.MessageDOM;
import com.inet.base.service.Base64Service;
import com.inet.base.service.DigestService;
import com.inet.base.service.IOService;
import com.inet.base.type.INetObject;

/**
 * MailReceiverDTO
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 28.12.2007
 * <pre>
 * 	Initialization mail receiver data transfer object.
 * </pre>
 */
public class MailReceiverDTO extends MessageDOM {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 5780338744084619023L;
		
	/**
	 * @return list of accounts.
	 */
	public List<MailReceiverObject> getAccounts(){
		// get list of receiver objects.
		INetObject[] objects = this.getObjects() ;
		if(objects == null) return null ;
		
		// create list of accounts.
		List<MailReceiverObject> accounts = new ArrayList<MailReceiverObject>(objects.length) ;
		for(INetObject object : objects){
			accounts.add(new MailReceiverObject(object)) ;
		}
		
		return accounts;
	}
	
	/**
	 * Set mail receiver object.
	 * 
	 * @param account MailReceiverObject - the given mail account.
	 */
	public void addAccount(MailReceiverObject account){
		if(account == null) return;
		
		// set account object.
		this.setObject(account) ;
	}
	
	/**
	 * @return the object data.
	 */
	public byte[] getData(){
		// convert object to byte.
		byte[] object = IOService.getStream(this) ;
		
		// encodes under base64
		String encode = Base64Service.encode(object) ;
		
		// covert to byte array data.
		return DigestService.utf8encode(encode) ;
	}
	
	/**
	 * Convert object data to MailReceiverDTO object.
	 * 
	 * @param data byte[] - the receiver data object.
	 * @return the MailReceiverDTO instance or null.
	 */
	public static MailReceiverDTO convertFrom(byte[] data){
		if(data == null) return null ;
		
		// get encode data.
		String encode = DigestService.utf8decode(data) ;
		
		// decode object.
		byte[] object = Base64Service.decode(encode) ;
		
		// return the MailReceiverDTO.
		return IOService.getObject(object, MailReceiverDTO.class) ;
	}
}
