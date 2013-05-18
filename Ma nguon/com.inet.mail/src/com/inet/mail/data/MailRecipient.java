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

import java.util.List;

import com.inet.base.service.Base64Service;
import com.inet.base.service.DigestService;
import com.inet.base.service.IOService;
import com.inet.base.type.INetField;
import com.inet.base.type.INetObject;

/**
 * MailRecipient
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 29.12.2007
 * <pre>
 * 	Initialization mail recipient.
 * </pre>
 */
public class MailRecipient extends INetObject {
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 3828012055452548197L;
	
	//-----------------------------------------------------------------
	// Class constants.
	//
	/**
	 * To addresses.
	 */
	public static final String RECIPIENT_TO = "TO" ;
	
	/**
	 * CC addresses.
	 */
	public static final String RECIPIENT_CC = "CC" ;
	
	/**
	 * BCC addresses.
	 */
	public static final String RECIPIENT_BCC = "BCC" ;
	
	/**
	 * @return list of to addresses.
	 */
	public List<Address> getTo(){
		return this.getContent(MailRecipient.RECIPIENT_TO) ;
	}
	
	/**
	 * Set to addresses.
	 * 
	 * @param to List<Address> - the given list of to addresses.
	 */
	public void setTo(List<Address> to){
		this.setContent(MailRecipient.RECIPIENT_TO, to) ;
	}
	
	/**
	 * @return the list of CC addresses.
	 */
	public List<Address> getCC(){
		return this.getContent(MailRecipient.RECIPIENT_CC) ;
	}
	
	/**
	 * Set CC addresses.
	 * 
	 * @param cc List<Address> - the given list of CC addresses.
	 */
	public void setCC(List<Address> cc){
		this.setContent(MailRecipient.RECIPIENT_CC, cc) ;
	}
	
	/**
	 * @return the list of BCC addresses.
	 */
	public List<Address> getBCC(){
		return this.getContent(MailRecipient.RECIPIENT_BCC) ;
	}
	
	/**
	 * Set the list of BCC addresses.
	 * 
	 * @param bcc List<Address> - the given list of BCC addresses.
	 */
	public void setBCC(List<Address> bcc){
		this.setContent(MailRecipient.RECIPIENT_BCC, bcc) ;
	}
	
	/**
	 * @return the object data.
	 */
	public String getData(){
		// convert object to data.
		byte[] object = IOService.getStream(this) ;
		
		// encodes data under base64.
		return Base64Service.encode(object) ;
	}
	
	/**
	 * Convert data to MailRecipient object.
	 * 
	 * @param data String - the given mail signature.
	 * @return the MailRecipient instance.
	 */
	public static MailRecipient convertFrom(String data){
		// check the given data is validate.
		if(data == null) return null ;
		
		// decode object.
		byte[] object = Base64Service.decode(data) ;
		
		// return the mail signature object.
		return IOService.getObject(object, MailRecipient.class) ;
	}
	//-----------------------------------------------------------------------
	// Helper function.
	//
	/**
	 * Get field content from the given field name.
	 * 
	 * @param fieldName String - the given field name to get value.
	 * @return the field content.
	 */
	@SuppressWarnings("unchecked")
	private List<Address> getContent(String fieldName){
		// get the content field.
		INetField field = this.getField(fieldName) ;
		if(field == null) return null ;

		// get encode value.
		String encode = field.getFieldName() ;
		
		// decode the value.
		byte[] object = DigestService.base64Decode(encode) ;
		
		// convert to list of address.
		return IOService.getObject(object, List.class) ;
	}
	
	/**
	 * Set the field value to field name.
	 * 
	 * @param fieldName String - the given field name.
	 * @param fieldValue String - the given field value.
	 */
	private void setContent(String fieldName, List<Address> fieldValue){
		// does not need store empty or null value.
		if(fieldValue == null || fieldValue.size() == 0) return;
		
		// get the serialization value.
		byte[] value = IOService.getStream(fieldName) ;
		
		// get field value.
		String encode = DigestService.base64Encode(value) ;

		// set field value.
		this.setField(fieldName, encode) ;
	}
}
