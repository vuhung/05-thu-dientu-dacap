/*****************************************************************
   Copyright 2006 by Nguyen Thi My Hien (ntmhien@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
package com.inet.web.service.addressbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.contact.ldap.LdapPersonalContact;
import com.inet.lotus.mail.manage.contact.ldap.LdapPersonalContactManager;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.addressbook.utils.IContactInfoService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.utils.WebCommonService;

/**
 * AddressBookService.
 * 
 * @author <a href="mailto:ntmhien@truthinet.com.vn">Nguyen Thi My Hien</a>
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class AddressBookService extends AbstractWebOSService{
	//~ Static fields =========================================================
	/**
	 * Class logger
	 */
	private static final INetLogger logger = INetLogger.getLogger(AddressBookService.class);
	
	/**
	 * {@link LdapPersonalContactManager} instance.
	 */
	private LdapPersonalContactManager personalContactManager;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AddressBookService</tt> instance from the given
	 * {@link AccountManager} and {@link LdapPersonalContactManager} instance.
	 * 
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param personalContactManager the given {@link LdapPersonalContactManager} instance.
	 */
	public AddressBookService(AccountManager<Account> accountManager, 
			LdapPersonalContactManager personalContactManager){
		super(accountManager) ;
		this.personalContactManager = personalContactManager;
	}
	
	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		//get action mode
		String actionMode = getData(request, IContactInfoService.ACTION_MODE_PARAM);
		
		if(IContactInfoService.ACTION_INSERT.equals(actionMode)){
			//add new contact
			return addNewContact(request);
		}else if(IContactInfoService.ACTION_UPDATE.equals(actionMode)){
			//update contact
			return updateContact(request);
		}else if(IContactInfoService.ACTION_DELETE.equals(actionMode)){
			// delete contact
			return deleteContact(request);
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * Add new contact
	 * 
	 * @param request HttpServletRequest
	 * @return JSON data
	 */
	private JSON addNewContact(HttpServletRequest request) throws WebOSServiceException{
		//get object
		JSONObject object = WebCommonService.toJSONObject(request, IContactInfoService.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// create new contact instance
		LdapPersonalContact contact = new LdapPersonalContact();
		String fullName = WebCommonService.toString(object, IContactInfoService.FULLNAME_KEY);
		if(!StringService.hasLength(fullName)) return FAILURE_JSON;
		
		contact.setFullName(fullName);
		// refresh the contact information
		this.refreshContact(contact, object);
		
		try{
			logger.debug("Save new contact info");
			// add new contact
			this.personalContactManager.addContact(getUserName(), contact);
			return SUCCESS_JSON;
		}catch (LotusException ex) {
			logger.error("error when save new contact info: " + ex.getMessage(), ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * refresh the personal contact information
	 * 
	 * @param contact LdapPersonalContact - the contact
	 * @param object JSONObject - the JSON object
	 */
	private void refreshContact(LdapPersonalContact contactInfo, JSONObject object) {
		contactInfo.setDisplayName(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.DISPLAY_NAME_KEY)));
		contactInfo.setEmail(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.ADDRESS_EMAIL_KEY)));
		contactInfo.setHomeNumber(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.HOME_NUMBER_KEY)));
		contactInfo.setMobileNumber(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.MOBILE_NUMBER_KEY)));
		contactInfo.setFaxNumber(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.FAX_NUMBER_KEY)));
		contactInfo.setPagerNumber(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.PAGER_NUMBER_KEY)));
		contactInfo.setGroup(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.GROUP_NAME_KEY)));
		contactInfo.setPostalAddress(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.POSTAL_ADDRESS_KEY)));
		contactInfo.setCity(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.CITY_NAME_KEY)));
		contactInfo.setWorkNumber(IContactInfoService.getLdapValue(WebCommonService.toString(object, IContactInfoService.OFFICE_PHONE_KEY)));
	}
	
	/**
	 * update contact info
	 * @param request HttpServletRequest
	 * @return JSON data
	 */
	private JSON updateContact(HttpServletRequest request) throws WebOSServiceException{
		//get object
		JSONObject object = WebCommonService.toJSONObject(request, IContactInfoService.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the full name
		String fullName = WebCommonService.toString(object, IContactInfoService.FULLNAME_KEY);
		if(!StringService.hasLength(fullName)) return FAILURE_JSON;
		
		try {
			logger.debug("Begin load personal contact: " + fullName);
			// load the contact			
			LdapPersonalContact contact = this.personalContactManager.findByName(getUserName(), fullName);			
			if(contact == null) return FAILURE_JSON;
			
			// refresh the contact information
			this.refreshContact(contact, object);
			
			logger.debug("Begin update personal contact");
			// begin updating contact
			this.personalContactManager.updateContact(getUserName(), contact);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error while update contact", ex);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * Delete contact info
	 * 
	 * @param request HttpServletRequest
	 * @return the JSON data
	 */
	private JSON deleteContact(HttpServletRequest request) throws WebOSServiceException{
		//get object
		JSONObject object = WebCommonService.toJSONObject(request, IContactInfoService.OBJECT_PARAM);
		if(object == null) return FAILURE_JSON;
		
		// get the full name
		String fullName = WebCommonService.toString(object, IContactInfoService.FULLNAME_KEY);
		if(!StringService.hasLength(fullName)) return FAILURE_JSON;
		
		try {
			logger.debug("Delete contact with given name: " + fullName);
			// delete personal contact contact
			this.personalContactManager.deleleContact(getUserName(), fullName);
			return SUCCESS_JSON;
		} catch (LotusException ex) {
			logger.error("Error during deleting contact", ex);
			return FAILURE_JSON;
		}
	}

}
