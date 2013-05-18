/*****************************************************************
   Copyright 2007 by Luong Thi Cao Van (ltcvan@truthinet.com)

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
package com.inet.web.service.addressbook.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.service.StringService;
import com.inet.ldap.PageResult;
import com.inet.lotus.mail.contact.ldap.LdapGlobalContact;
import com.inet.lotus.mail.contact.ldap.LdapPersonalContact;
import com.inet.web.common.json.JSONService;

/**
 * IContactInfoService.
 * 
 * @author <a href="mailto:ltcvan@truthinet.com.vn"> Luong Thi Cao Van</a>
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class IContactInfoService {
	
	/**
	 * JSON object
	 */
	public static final String OBJECT_PARAM = "object";
	
	/**
	 * action mode. 
	 */
	public static final String ACTION_MODE_PARAM = "action";
	
	/**
	 * action search. 
	 */
	public static final String ACTION_SEARCH = "search";
	
	/**
	 * action insert
	 */
	public static final String ACTION_INSERT = "insert";
	
	/**
	 * action update
	 */
	public static final String ACTION_UPDATE = "update";
	
	/**
	 * action Delete
	 */
	public static final String ACTION_DELETE = "delete";
	
	/**
	 * Action load
	 */
	public static final String ACTION_LOAD = "load";
	
	/**
	 * The JSON key for domain name
	 */
	public static final String ACTION_LOAD_ALL_DOMAIN = "loadDomain";
	
	/**
	 * Search mode request param.
	 */
	public static final String SEARCH_MODE_PARAM = "mode";
	
	/**
	 * search cong ty
	 */
	public static final String SEARCH_GLOBAL = "global";
	/**
	 * seach ca nhan
	 */
	public static final String SEARCH_PERSONAL = "personal";
	/**
	 *  tu khoa
	 */
	public static final String KEYWORD_PARAM = "keyword";
	/**
	 *  nhom
	 */
	public static final String GROUP_PARAM = "group";
	
	/**
	 * JSON result key.
	 */
	public static final String RESULT_KEY = "results" ;
	/**
	 * JSON rows key.
	 */
	public static final String ROWS_KEY = "rows" ;
	/**
	 *  id key
	 */
	public static final String CONTACT_ID = "id";
	/**
	 *  ho va ten
	 */
	public static final String FULLNAME_KEY = "name";
	/**
	 * First name
	 */
	public static final String FIRST_NAME_KEY = "fname";
	/**
	 * Last name
	 */
	public static final String LAST_NAME_KEY = "lname";
	
	/**
	 * City name
	 */
	public static final String CITY_NAME_KEY = "city";
	/**
	 *  dia chi email
	 */
	public static final String ADDRESS_EMAIL_KEY = "mail";
	/**
	 * ten hien thi 
	 */
	public static final String DISPLAY_NAME_KEY = "dname";
	/**
	 * so dt di dong
	 */
	public static final String MOBILE_NUMBER_KEY = "mphone";
	/**
	 * nhom
	 */
	public static final String GROUP_NAME_KEY = "group";
	/**
	 *  dien thoai nha
	 */
	public static final String HOME_NUMBER_KEY = "hphone";
	/**
	 * so fax
	 */
	public static final String FAX_NUMBER_KEY = "fax";
	/**
	 * may nhan tin
	 */
	public static final String PAGER_NUMBER_KEY = "pager";
	/**
	 * dia chi
	 */
	public static final String POSTAL_ADDRESS_KEY = "addr";
	/**
	 *  phong ban
	 */
	public static final String DEPARTMENT_KEY = "department";
	/**
	 * dien thoai co quan
	 */
	public static final String OFFICE_PHONE_KEY = "ophone";
	/**
	 * Type: cong ty or ca nhan
	 */
	public static final String TYPE_ADDRESS = "type";
	
	/**
	 * The JSON key for starting position of searching
	 */
	public static final String SEARCHING_START = "start";
	
	/**
	 * The JSON key for limit data of searching
	 */
	public static final String SEARCHING_LIMIT = "limit";
	
	/**
	 * The JSON key for domain name
	 */
	public static final String DOMAIN_NAME = "domain";
	
	/**
	 * The JSON key for description domain 
	 */
	public static final String DOMAIN_DESC = "desc";
	
	/**
	 * The JSON key for starting position of searching
	 */
	public static final String CONTACT_ALIAS = "alias";
	
	/**
	 * The JSON key for starting position of searching
	 */
	public static final String CONTACT_MAIL_DROP = "mailDrop";

	/**
	 * convert IContactInfo to JSON
	 * @param contact
	 * @return JSON data
	 */
	public static JSON convertPersonalContactToJSON(LdapPersonalContact contact){
		if(contact == null) return null;
		
		JSONObject object = new JSONObject();
		object.accumulate(DISPLAY_NAME_KEY, IContactInfoService.getDisplayValue(contact.getDisplayName()))
		      .accumulate(MOBILE_NUMBER_KEY, IContactInfoService.getDisplayValue(contact.getMobileNumber()))
		      .accumulate(GROUP_NAME_KEY, IContactInfoService.getDisplayValue(contact.getGroup()))
		      .accumulate(HOME_NUMBER_KEY, IContactInfoService.getDisplayValue(contact.getHomeNumber()))
		      .accumulate(FAX_NUMBER_KEY, IContactInfoService.getDisplayValue(contact.getFaxNumber()))
		      .accumulate(PAGER_NUMBER_KEY, IContactInfoService.getDisplayValue(contact.getPagerNumber()))
		      .accumulate(POSTAL_ADDRESS_KEY, IContactInfoService.getDisplayValue(contact.getPostalAddress()))
		      .accumulate(OFFICE_PHONE_KEY, IContactInfoService.getDisplayValue(contact.getWorkNumber()))
		      .accumulate(ADDRESS_EMAIL_KEY, IContactInfoService.getDisplayValue(contact.getEmail()))
		      .accumulate(FULLNAME_KEY, IContactInfoService.getDisplayValue(contact.getFullName()))
		      .accumulate(CITY_NAME_KEY, IContactInfoService.getDisplayValue(contact.getCity()));
		return object;
		
	}
	/**
	 * convert the search result to JSON
	 * 
	 * @param contacts PageResult<LdapPersonalContact> - the result search
	 * @return JSON data
	 */
	public static JSON convertPersonalContactToJSON(PageResult<LdapPersonalContact> contacts){
		List<JSON> jsons = new ArrayList<JSON>();
		if(contacts.getData() != null) {
			for(LdapPersonalContact contact : contacts.getData()) {
				// convert contact to JSON and add to list
				jsons.add(convertPersonalContactToJSON(contact));
			}
		}
		
		JSONObject object = new JSONObject();
		object.accumulate(ROWS_KEY, JSONService.toJSONArray(jsons))
			  .accumulate(RESULT_KEY, contacts.getSize());
		return object;
		
	}
	
	/**
	 * convert the search result to JSON
	 * 
	 * @param contacts PageResult<LdapGlobalContact> - the search result
	 * @return JSON - the JSON data
	 */
	public static JSON convertToJSON(PageResult<LdapGlobalContact> contacts) {
		List<JSON> jsons = new ArrayList<JSON>();
		if(contacts.getData() != null) {
			for(LdapGlobalContact contact : contacts.getData()) {
				// convert contact to JSON and add to list
				jsons.add(convertToJSON(contact));
			}
		}
		
		JSONObject object = new JSONObject();
		object.accumulate(ROWS_KEY, JSONService.toJSONArray(jsons))
			  .accumulate(RESULT_KEY, contacts.getSize());
		return object;
	}
	
	/**
	 * convert contact to JSON
	 * 
	 * @param contact LdapContact - the given contact
	 * @return JSON - the JSON data
	 */
	public static JSON convertToJSON(LdapGlobalContact contact) {
		JSONObject object = new JSONObject();
		object.accumulate(FIRST_NAME_KEY, contact.getFirstName())
			  .accumulate(LAST_NAME_KEY, contact.getLastName())
			  .accumulate(DISPLAY_NAME_KEY, contact.getLastName() + " " + contact.getFirstName())			  
			  .accumulate(ADDRESS_EMAIL_KEY, contact.getEmail())
			  .accumulate(CONTACT_ALIAS, contact.isGroup());
		
		if(contact.isGroup()) {
			// get list of mail drop
			List<JSON> jsons = new ArrayList<JSON>();
			JSONObject json;
			for(String email : contact.getMailDrop()) {
				json = new JSONObject();
				json.accumulate(ADDRESS_EMAIL_KEY, email);
				// put JSON object to list
				jsons.add(json);
			}
			object.accumulate(CONTACT_MAIL_DROP, JSONService.toJSONArray(jsons));
		}
		
		return object;	  
		
	}
	
	/**
	 * get the display value
	 * 
	 * @param value String - the value
	 * @return String - the display value
	 */
	private static String getDisplayValue(String value) {
		return StringService.hasLength(value)?value:StringService.EMPTY_STRING;
	}
	
	/**
	 * get the LDAP value
	 * 
	 * @param value String - the GUI value
	 * @return String - the value to save to LDAP 
	 */
	public static String getLdapValue(String value) {
		if(value == null) return value;
		
		return StringService.hasLength(value.trim())?value:null;
	}

}
