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

import com.inet.base.exception.ConvertException;
import com.inet.base.service.Base64Service;
import com.inet.base.service.ConvertService;
import com.inet.base.service.DigestService;
import com.inet.base.service.IOService;
import com.inet.base.service.StringService;
import com.inet.base.type.INetField;
import com.inet.base.type.INetObject;

/**
 * MailSignature
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 28.12.2007
 * <pre>
 *  Initialization mail signature object.
 * </pre>
 */
public class MailSignature extends INetObject {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3852334681347276103L;
	
	//---------------------------------------------------------
	// class members.
	//
	/**
	 * Create MailSignature instance.
	 * 
	 * @param account String - the given account to manage signature.
	 */
	public MailSignature(String account){
		super(account, StringService.EMPTY_STRING) ;
	}
	
	/**
	 * Set signature.
	 * 
	 * @param name String - the given signature name.
	 * @param signature String - the given signature content.
	 */
	public void setSignature(String name, String signature){
		if(StringService.isEmpty(name) || StringService.isEmpty(signature)) return ;
		// encode signature under base 64.
		String base64Content = Base64Service.encodeString(signature) ;
		
		// add data.
		this.setField(name, base64Content) ;
	}
	
	/**
	 * Get signature content.
	 * 
	 * @param name String - the given signature name.
	 * @return the signature content.
	 */
	public String getSignature(String name){
		// get the content.
		INetField content = this.getField(name) ;
		if(content == null) return null ;
		String base64Content = null ;
		try{
			base64Content = ConvertService.toString(content) ;
		}catch(ConvertException ex){}
		
		if(base64Content == null || StringService.EMPTY_STRING.equals(base64Content)) return null;
		return Base64Service.decodeString(base64Content) ;
	}
	
	/**
	 * @return list of signature name.
	 */
	public String[] getSignatures(){
		// get list of signatures.
		INetField[] signatures = this.getFields() ;
		if(signatures == null || signatures.length == 0) return new String[0] ;
		
		// create list of signatures.
		String[] signs = new String[signatures.length] ;
		int index = 0 ;
		for(INetField signature : signatures){
			signs[index++] = signature.getFieldName() ;
		}
		
		// return the list of signature.
		return signs ;
	}
	
	/**
	 * @return the object data.
	 */
	public byte[] getData(){
		// convert object to data.
		byte[] object = IOService.getStream(this) ;
		
		// encodes data under base64.
		String base64Content = Base64Service.encode(object) ;
		
		// encode base64 content before sending to client.
		return DigestService.utf8encode(base64Content) ;
	}
	
	/**
	 * Convert data to MailSignature object.
	 * 
	 * @param data byte[] - the given mail signature.
	 * @return the MailSignature instance.
	 */
	public static MailSignature convertFrom(byte[] data){
		// check the given data is validate.
		if(data == null) return null ;
		
		// get encode data.
		String encode = DigestService.utf8decode(data) ;
		
		// decode object.
		byte[] object = Base64Service.decode(encode) ;
		
		// return the mail signature object.
		return IOService.getObject(object, MailSignature.class) ;
	}
}
