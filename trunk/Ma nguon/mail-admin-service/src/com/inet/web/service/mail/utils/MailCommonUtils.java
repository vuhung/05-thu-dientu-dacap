/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.web.service.mail.utils;

import java.util.Random;

import com.inet.base.service.DigestService;
import com.inet.base.service.StringService;
import com.inet.ldap.support.DistinguishedName;
import com.inet.lotus.mail.conf.MailConfiguration;

/**
 * MailCommonUtils.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class MailCommonUtils {
	private static final int MB_TO_BYTE = 1024 * 1024;
	
	/**
	 * get associate password 
	 * 
	 * @param password String - the inputed password
	 * @param hashMethod the given password scheme. MD5, SMD5, CRYPT, ...
	 * @return String - the returned password
	 */
	public static String getLdapPassword(String password, String hashMethod) {
		// user don't input new password, so we don't update it
		if(!StringService.hasLength(password)) return StringService.EMPTY_STRING;
		
		// set default password scheme.
		if(!StringService.hasLength(hashMethod)) hashMethod = DigestService.HASH_METHOD_SMD5 ;
		
		// digest password and return
		return DigestService.getPassword(hashMethod, password);
	}
	
	/**
	 * Get the organization from given user DN
	 * 
	 * @param userDN String - the user DN
	 * @return String - the organization
	 */
	public static String getOrganizationFromUserDN(String userDN) {
		if(!StringService.hasLength(userDN)) return null;
		
		// create the distinguished name
		DistinguishedName name = new DistinguishedName(userDN);
		// get the size of RDN in name 
		int size = name.size();
		if(size < 3) return null;
		
		// return the value
		return name.getLdapRdn(size - 3).getValue();
	}
	
	/**
	 * Get the organization from given group DN
	 * 
	 * @param groupDN String - the user DN
	 * @return String - the organization
	 */
	public static String getOrganizationFromGroupDN(String groupDN) {
		return getOrganizationFromUserDN(groupDN);
	}
	
	/**
	 * String generate the new password for user
	 * 
	 * @return String - the new password
	 */
	public static String generatePassword() {
		// create string to contain password
		String password = StringService.EMPTY_STRING;
		Random random = new Random();
		for (int index = 0; index < 10; index++) {
			// random new character and append to password
			password += (char)('a' + random.nextInt(26));
		}
		return password;
	}
	
	/**
	 * get LDAP for new value
	 * 
	 * @param str String - given string
	 * @return String - the LDAP value
	 */
	public static String getLdapValue(String str) {
		/**
		 * TODO shouldn't use function
		 * This bug must be fixed in LDAP library
		 */
		return StringService.hasLength(str)?str:null;
	}
	
	/**
	 * get the string value to display on GUI
	 * 
	 * @param str String - the given string
	 * @return String - the returned string
	 */
	public static String getDisplayValue(String str) {
		return StringService.hasLength(str)?str:StringService.EMPTY_STRING;
	}
	
	/**
	 * get byte from MB
	 * 
	 * @param megaByte int - the given MB
	 * @return long - the byte
	 */
	public static long getByte(long megaByte) {
		return megaByte * MB_TO_BYTE;
	}
	
	/**
	 * get the MB from byte
	 * 
	 * @return long - the number of MB
	 */
	public static long getMByte(long numberByte) {
		return numberByte/MB_TO_BYTE;
	}
	
	/**
	 * Get the full name from given first name, middle name and last name
	 * 
	 * @param firstName String - the given first name
	 * @param middleName String - the given middle name
	 * @param lastName String - the given last name
	 * @return String - the full name
	 */
	public static String getFullName(String firstName, String middleName, String lastName) {
		String fullName = StringService.EMPTY_STRING;
		
		// append last name
		if(StringService.hasLength(lastName)) fullName = lastName;		
		if(StringService.hasLength(middleName)) {
			// append middle name
			fullName = StringService.hasLength(fullName)?(fullName + " " + middleName):middleName;
		}
		if(StringService.hasLength(firstName)) {
			// append first name
			fullName = StringService.hasLength(fullName)?(fullName + " " + firstName):firstName;
		}
		
		return fullName;
	}
	
	/**
	 * Get the user name from given name and organization
	 * 
	 * @param name String - the name
	 * @param org String - the organization
	 * @param configuration MailConfiguration - the configuration
	 * @return String - the user name
	 */
	public static String getUserName(String name, String org, MailConfiguration configuration) {
		// checking data
		if(!StringService.hasLength(name) || !StringService.hasLength(org)
				|| (configuration != null && configuration.isJoinDomain() && name.indexOf('@') != -1)) 
			return null;
		
		// return associate user name
		return configuration.isJoinDomain()?(name + "@" + org):name;
	}
	
	/**
	 * Get the user name from given name and organization
	 * 
	 * @param name String - the name
	 * @param configuration MailConfiguration - the configuration
	 * @return String - the user name
	 */
	public static String getDUserName(String name, MailConfiguration configuration) {		
		// return associate user name
		return configuration.isJoinDomain()?(name.split("@")[0]):name;
	}
	
	/**
	 * Get the organization from given user name
	 * @param name String - the user name
	 * @return String - the organization
	 */
	public static String getOrg(String name){
	    if(StringService.hasLength(name) && name.indexOf("@") > 0){
	      return name.split("@")[1];
	    }
	    return StringService.EMPTY_STRING;
	}
	
	/**
         * Get the name before @ from given email address
         * @param name String - the user name
         * @return String - the organization
         */
        public static String getName(String name){
            if(StringService.hasLength(name) && name.indexOf("@") > 0){
              return name.split("@")[0];
            }
            return name;
        }
	
}
