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
import com.inet.base.service.ConvertService;
import com.inet.base.service.StringService;
import com.inet.base.type.INetObject;
import com.inet.mail.util.MailService;

/**
 * MailReceiverObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date 28.12.2007
 * <pre>
 * 	Initialization mail receiver object.
 * </pre>
 */
public class MailReceiverObject extends INetObject {

	/**
	 * serialVersionUID
	 */
	static final long serialVersionUID = 167212868427763607L;

	/**
	 * mail active.
	 */
	static final String MAIL_ACTIVE = "MAIL_ACTIVE" ;
	
	/**
	 * mail address.
	 */
	static final String MAIL_ADDRESS = "MAIL_ADDRESS" ;
	
	/**
	 * mail account description.
	 */
	static final String MAIL_DESC = "MAIL_DESC" ;
	
	/**
	 * mail receiver protocol.
	 */
	static final String MAIL_PROTOCOL = "MAIL_PROTOCOL" ;
	
	/**
	 * mail receiver security mode.
	 */
	static final String MAIL_SECURITY = "MAIL_SECURITY" ;
	
	/**
	 * mail receiver server name.
	 */
	static final String MAIL_SERVER_NAME = "MAIL_SERVER_NAME" ;
	
	/**
	 * mail receiver server port.
	 */
	static final String MAIL_SERVER_PORT = "MAIL_SERVER_PORT" ;
	
	/**
	 * mail receiver account name.
	 */
	static final String ACCOUNT_NAME = "ACCOUNT_NAME" ;
	
	/**
	 * mail receiver account password (encrypt under base 64).
	 */
	static final String ACCOUNT_PASSWORD = "ACCOUNT_PASSWORD" ;
		
	/**
	 * Default server port.
	 */
	static final int DEFAULT_SERVER_PORT = 110 ;
	
	/**
	 * mail receiver security mode.
	 */
	static final String MAIL_SMTP_SECURITY = "MAIL_SMTP_SECURITY" ;
	
	/**
	 * mail receiver server name.
	 */
	static final String MAIL_SMTP_SERVER_NAME = "MAIL_SMTP_SERVER_NAME" ;
	
	/**
	 * mail receiver server port.
	 */
	static final String MAIL_SMTP_SERVER_PORT = "MAIL_SMTP_SERVER_PORT" ;
	
	/**
	 * mail receiver account name.
	 */
	static final String ACCOUNT_SMTP_NAME = "ACCOUNT_SMTP_NAME" ;
	
	/**
	 * mail receiver account password (encrypt under base 64).
	 */
	static final String ACCOUNT_SMTP_PASSWORD = "ACCOUNT_SMTP_PASSWORD" ;
		
	/**
	 * Default server port.
	 */
	static final int DEFAULT_SMTP_SERVER_PORT = 25 ;
	//-----------------------------------------------------------------
	// Class members.
	//	
	/**
	 * Create MailReceiverObject instance.
	 */
	public MailReceiverObject(String account){
		super(account, StringService.EMPTY_STRING) ;
		this.setAccountName(account) ;
	}
	
	/**
	 * Create MailReceiverObject instance.
	 * 
	 * @param object INetObject - the given inet object.
	 */
	public MailReceiverObject(INetObject object){
		super(object.getObjName(), StringService.EMPTY_STRING) ;
		
		// set value.
		this.add(object.getField(MailReceiverObject.MAIL_ACTIVE)) ;
		this.add(object.getField(MailReceiverObject.MAIL_ADDRESS)) ;
		this.add(object.getField(MailReceiverObject.MAIL_DESC)) ;
		
		this.add(object.getField(MailReceiverObject.MAIL_SERVER_NAME)) ;
		this.add(object.getField(MailReceiverObject.MAIL_SERVER_PORT)) ;
		this.add(object.getField(MailReceiverObject.ACCOUNT_NAME)) ;
		this.add(object.getField(MailReceiverObject.ACCOUNT_PASSWORD)) ;
		this.add(object.getField(MailReceiverObject.MAIL_SECURITY)) ;
		this.add(object.getField(MailReceiverObject.MAIL_PROTOCOL)) ;
		
		this.add(object.getField(MailReceiverObject.MAIL_SMTP_SERVER_NAME)) ;
		this.add(object.getField(MailReceiverObject.MAIL_SMTP_SERVER_PORT)) ;
		this.add(object.getField(MailReceiverObject.ACCOUNT_SMTP_NAME)) ;
		this.add(object.getField(MailReceiverObject.ACCOUNT_SMTP_PASSWORD)) ;
		this.add(object.getField(MailReceiverObject.MAIL_SMTP_SECURITY)) ;
	}
	
	
	/**
	 * @return the active mail
	 */
	public boolean getActive(){
		try{
			return Boolean.valueOf(getField(MailReceiverObject.MAIL_ACTIVE).getFieldValue());
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){
		}catch(Exception ex){}	
		return false;
	}
	
	/**
	 * set mail active
	 * @param active boolean - the given mail active
	 */
	public void setActive(boolean active){
		this.setField(MailReceiverObject.MAIL_ACTIVE, String.valueOf(active)) ;
	}
	/** 
	 * @return the email address.
	 */
	public String getEmailAddress(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.MAIL_ADDRESS)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set email address.
	 * 
	 * @param email String - the given email address.
	 */
	public void setEmailAddress(String email){
		this.setField(MailReceiverObject.MAIL_ADDRESS, email) ;
	}
	
	/** 
	 * @return the email address.
	 */
	public String getDescription(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.MAIL_DESC)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set description .
	 * 
	 * @param desc String - the given description.
	 */
	public void setDescription(String desc){
		this.setField(MailReceiverObject.MAIL_DESC, desc) ;
	}
	
	
	/** 
	 * @return the server name.
	 */
	public String getServerName(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.MAIL_SERVER_NAME)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set server name to current mail receiver object.
	 * 
	 * @param server String - the given server name.
	 */
	public void setServerName(String server){
		this.setField(MailReceiverObject.MAIL_SERVER_NAME, server) ;
	}
	
	/** 
	 * @return the server name.
	 */
	public String getSMTPServerName(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.MAIL_SMTP_SERVER_NAME)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set server name to current mail receiver object.
	 * 
	 * @param server String - the given server name.
	 */
	public void setSMTPServerName(String server){
		this.setField(MailReceiverObject.MAIL_SMTP_SERVER_NAME, server) ;
	}
	
	/**
	 * @return the server port.
	 */
	public int getServerPort(){
		try{
			// get port value.
			String portValue = ConvertService.toString(this.getField(MailReceiverObject.MAIL_SERVER_PORT)) ;
			
			// convert to port value.
			return Integer.parseInt(portValue) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return MailReceiverObject.DEFAULT_SERVER_PORT ;
	}
	
	/**
	 * Set the server port.
	 * 
	 * @param port int - the given server port.
	 */
	public void setServerPort(int port){
		// add server port.
		this.setField(MailReceiverObject.MAIL_SERVER_PORT, String.valueOf(port)) ;
	}
	
	/**
	 * @return the server port.
	 */
	public int getSMTPServerPort(){
		try{
			// get port value.
			String portValue = ConvertService.toString(this.getField(MailReceiverObject.MAIL_SMTP_SERVER_PORT)) ;
			
			// convert to port value.
			return Integer.parseInt(portValue) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return MailReceiverObject.DEFAULT_SMTP_SERVER_PORT ;
	}
	
	/**
	 * Set the server port.
	 * 
	 * @param port int - the given server port.
	 */
	public void setSMTPServerPort(int port){
		// add server port.
		this.setField(MailReceiverObject.MAIL_SMTP_SERVER_PORT, String.valueOf(port)) ;
	}
	
	/**
	 * @return the account name.
	 */
	public String getAccountName(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.ACCOUNT_NAME)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}

	/**
	 * Set the given account name to receiver object.
	 * 
	 * @param account String - the given account name.
	 */
	public void setAccountName(String account){
		// get the first password.
		String password = this.getAccountPassword() ;
	
		// set account.
		this.setField(MailReceiverObject.ACCOUNT_NAME, account) ;

		// set password again.
		if(StringService.hasLength(password)) this.setAccountPassword(password) ;
	}
	
	/**
	 * @return the account name.
	 */
	public String getSMTPAccountName(){
		try{
			return ConvertService.toString(this.getField(MailReceiverObject.ACCOUNT_SMTP_NAME)) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}

	/**
	 * Set the given account name to receiver object.
	 * 
	 * @param account String - the given account name.
	 */
	public void setSMTPAccountName(String account){
		// get the first password.
		String password = this.getSMTPAccountPassword() ;
	
		// set account.
		this.setField(MailReceiverObject.ACCOUNT_SMTP_NAME, account) ;

		// set password again.
		if(StringService.hasLength(password)) this.setSMTPAccountPassword(password) ;
	}
	
	/**
	 * @return the account password.
	 */
	public String getAccountPassword(){
		try{
			// get the encrypt password content.
			String password = ConvertService.toString(this.getField(MailReceiverObject.ACCOUNT_PASSWORD)) ;			
			return MailService.decrypt(this.getAccountName(), password) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set the given account password to receiver object.
	 * 
	 * @param password String - the given account password.
	 */
	public void setAccountPassword(String password){
		if(!StringService.isset(password)) return;
		
		// encrypted password.
		password = MailService.encrypt(this.getAccountName(), password) ;
		
		// set password.
		this.setField(MailReceiverObject.ACCOUNT_PASSWORD, password) ;
	}
	
	/**
	 * @return the account password.
	 */
	public String getSMTPAccountPassword(){
		try{
			// get the encrypt password content.
			String password = ConvertService.toString(this.getField(MailReceiverObject.ACCOUNT_SMTP_PASSWORD)) ;			
			return MailService.decrypt(this.getSMTPAccountName(), password) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		return StringService.EMPTY_STRING ;
	}
	
	/**
	 * Set the given account password to receiver object.
	 * 
	 * @param password String - the given account password.
	 */
	public void setSMTPAccountPassword(String password){
		if(!StringService.isset(password)) return;
		
		// encrypted password.
		password = MailService.encrypt(this.getSMTPAccountName(), password) ;
		
		// set password.
		this.setField(MailReceiverObject.ACCOUNT_SMTP_PASSWORD, password) ;
	}
	
	/**
	 * @return the mail receiver protocol.
	 */
	public MailReceiverProtocol getProtocol(){
		try{
			// return the protocol.
			String protocol = ConvertService.toString(this.getField(MailReceiverObject.MAIL_PROTOCOL)) ;			
			return MailReceiverProtocol.valueOf(protocol) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}

		// return default mail receiver protocol.
		return MailReceiverProtocol.POP3 ; 
	}
	
	/**
	 * Set the given mail receiver protocol.
	 * 
	 * @param protocol MailReceiverProtocol - the given mail receiver protocol.
	 */
	public void setProtocol(MailReceiverProtocol protocol){
		this.setField(MailReceiverObject.MAIL_PROTOCOL, protocol.toString()) ;
	}
	
	/**
	 * @return the mail security.
	 */
	public MailSecurity getSecurity(){
		try{
			// get mail security.
			String security = ConvertService.toString(this.getField(MailReceiverObject.MAIL_SECURITY)) ;
			return MailSecurity.valueOf(security) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		// return default mail receiver security.
		return MailSecurity.NONE ;
	}
	
	/**
	 * Set the given mail security.
	 * 
	 * @param security MailSecurity - the given mail security.
	 */
	public void setSecurity(MailSecurity security){
		this.setField(MailReceiverObject.MAIL_SECURITY, security.toString()) ;
	}
	
	/**
	 * @return the mail security.
	 */
	public MailSecurity getSMTPSecurity(){
		try{
			// get mail security.
			String security = ConvertService.toString(this.getField(MailReceiverObject.MAIL_SMTP_SECURITY)) ;
			return MailSecurity.valueOf(security) ;
		}catch(ConvertException ex){
		}catch(IllegalArgumentException iaex){}
		
		// return default mail receiver security.
		return MailSecurity.NONE ;
	}
	
	/**
	 * Set the given mail security.
	 * 
	 * @param security MailSecurity - the given mail security.
	 */
	public void setSMTPSecurity(MailSecurity security){
		this.setField(MailReceiverObject.MAIL_SMTP_SECURITY, security.toString()) ;
	}
	
	/**
	 * Copy mail receiver object to current object.
	 * 
	 * @param receiver MailReceiverObject - the given receiver object.
	 */
	public void copy(MailReceiverObject receiver){
		// clear all value.
		this.removeFields() ;
		
		// set value.
		this.add(receiver.getField(MailReceiverObject.MAIL_SERVER_NAME)) ;
		this.add(receiver.getField(MailReceiverObject.MAIL_SERVER_PORT)) ;
		this.add(receiver.getField(MailReceiverObject.ACCOUNT_NAME)) ;
		this.add(receiver.getField(MailReceiverObject.ACCOUNT_PASSWORD)) ;
		this.add(receiver.getField(MailReceiverObject.MAIL_SECURITY)) ;
		this.add(receiver.getField(MailReceiverObject.MAIL_PROTOCOL)) ;
		
		this.add(receiver.getField(MailReceiverObject.MAIL_SMTP_SERVER_NAME)) ;
		this.add(receiver.getField(MailReceiverObject.MAIL_SMTP_SERVER_PORT)) ;
		this.add(receiver.getField(MailReceiverObject.ACCOUNT_SMTP_NAME)) ;
		this.add(receiver.getField(MailReceiverObject.ACCOUNT_SMTP_PASSWORD)) ;
		this.add(receiver.getField(MailReceiverObject.MAIL_SMTP_SECURITY)) ;
	}
}
