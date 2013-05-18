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
package com.inet.web.service.addressbook;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.ldap.PageResult;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.contact.ldap.LdapGlobalContact;
import com.inet.lotus.mail.contact.ldap.LdapPersonalContact;
import com.inet.lotus.mail.data.search.ldap.LdapContactSearchBean;
import com.inet.lotus.mail.data.search.ldap.LdapPersonalContactSearchBean;
import com.inet.lotus.mail.domain.ldap.LdapMailDomain;
import com.inet.lotus.mail.manage.contact.ldap.LdapGlobalContactManager;
import com.inet.lotus.mail.manage.contact.ldap.LdapPersonalContactManager;
import com.inet.lotus.mail.manage.domain.ldap.LdapMailDomainManager;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.lotus.org.permission.manage.ldap.ILdapGroupManager;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.addressbook.utils.IContactInfoService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.utils.WebCommonService;

/**
 * SearchAddressBookService.
 * 
 * @author <a href="mailto:ltcvan@truthinet.com.vn"> Luong Thi Cao Van</a>
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class SearchAddressBookService extends AbstractWebOSService{
	//~ Static fields =========================================================
	/**
	 *  class logger
	 */
	private static final INetLogger logger = INetLogger.getLogger(SearchAddressBookService.class) ;
	
	private static final String GROUP = "GROUP";
	private static final String DOMAIN = "DOMAIN";
	/**
	 * The cache key to cache the cookie of searching
	 */
	private final String SEARCH_CACHE_KEY = "contact-search";
	
	//~ Instance fields =======================================================
	/**
	 *  Address Book business object
	 */
	private LdapMailDomainManager domainManager;
	private LdapGlobalContactManager contactManager;
	private LdapPersonalContactManager personalContactManager;
	private ILdapGroupManager groupManager;
	
	/*Domain name or group name*/
	private String category;
	
	//~ Constructors ==========================================================
	/**
	 * Create <tt>addressBookManagementBO</tt> instance from the given business object.
	 * @param addressBookManagementBO AddressBookManagementBO - the address book business object
	 * @param domainManager LdapMailDomainManager - the mail domain manager
	 * @param contactManager LdapContactManager - the global contact manager 
	 * @param category String - the given DOMAIN or GROUP value
	 */
	public SearchAddressBookService(
			AccountManager<Account> accountManager,
			LdapMailDomainManager domainManager,
			LdapGlobalContactManager contactManager, 
			LdapPersonalContactManager personalContactManager,
			ILdapGroupManager groupManager,
			String category){
		super(accountManager) ;
		
		this.domainManager = domainManager;
		this.contactManager = contactManager;
		this.personalContactManager = personalContactManager;
		this.groupManager = groupManager;
		this.category = category;
	}
	//~ Methods ===============================================================

	/**
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response) throws WebOSServiceException {
		String action=getData(request, IContactInfoService.ACTION_MODE_PARAM);	
		
		if(IContactInfoService.ACTION_SEARCH.equals(action)){
			//search contact
			String searchMode= getData(request, IContactInfoService.SEARCH_MODE_PARAM);
			if(IContactInfoService.SEARCH_PERSONAL.equals(searchMode))	{	
				return	searchPersonalContact(request);
			}else if(IContactInfoService.SEARCH_GLOBAL.equals(searchMode)){
				return searchGlobalContact(request);
			}
		} else if(IContactInfoService.ACTION_LOAD_ALL_DOMAIN.equals(action)) {
			return this.loadDomain(request);
		}
		
		return FAILURE_JSON; 
	}
	
	/**
	 * @see com.inet.web.service.AbstractService#getCacheSuffix()
	 */
	@Override
	protected String getCacheSuffix() {
		return this.getCode();
	}
	
	/**
	 *  search personal contact
	 * @param request
	 * @return JSON data
	 * @throws WebOSServiceException
	 */
	protected JSON  searchPersonalContact(HttpServletRequest request)throws WebOSServiceException{
		try{
			if(logger.isDebugEnabled())logger.debug("search Personal contact");
			
			// search personal contact information
			PageResult<LdapPersonalContact> results = this.personalContactManager.pagination(this.getPersonalSearchBean(request));
			// invalidate return data.
			if(results == null) return FAILURE_JSON;
			
			// put token to cache.
			setDataToCache(SEARCH_CACHE_KEY, results.getToken());
			
			return  IContactInfoService.convertPersonalContactToJSON(results);
		}catch(WebOSBOException exx){
			logger.error("error when search personal contact" + exx.getMessage(), exx);
			return FAILURE_JSON;
		}
	}
	
	/**
	 * create the search bean from given request
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return LdapContactSearchBean - the search bean
	 */
	private LdapPersonalContactSearchBean getPersonalSearchBean(HttpServletRequest request) {
		// get the data from request
		int start = WebCommonService.toInt(getData(request, IContactInfoService.SEARCHING_START), 0);
		int limit = WebCommonService.toInt(getData(request, IContactInfoService.SEARCHING_LIMIT), this.getDefaultLimit());
		String keyword= getData(request, IContactInfoService.KEYWORD_PARAM);
		
		// get the token
		String token = null;
		if(start == 0) {
			// put data to cache again
			removeDataFromCache(SEARCH_CACHE_KEY);
		} else {
			// get the tokens from cache
			token = getDataFromCache(SEARCH_CACHE_KEY, String.class);
		}
		
		// create the search bean
		LdapPersonalContactSearchBean searchBean = new LdapPersonalContactSearchBean(start, limit, token);
		searchBean.setKey(keyword);
		searchBean.setOwner(getUserName());
		
		// return the search bean.
		return searchBean;
	}
	
	/**
	 * Search  global contact
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the search result
	 * @throws WebOSServiceException - when there is any error happens
	 */
	protected JSON searchGlobalContact(HttpServletRequest request) throws WebOSServiceException{
		try{
			if(logger.isDebugEnabled()) logger.debug("search global contact");
			// begin search data
			PageResult<LdapGlobalContact> results = contactManager.pagination(getSearchBean(request));
			
			// invalidate return data.
			if(results == null) return FAILURE_JSON;
			
			// put token to cache.
			setDataToCache(SEARCH_CACHE_KEY, results.getToken());
			
			return IContactInfoService.convertToJSON(results);
		}catch(WebOSBOException exx){
			logger.warning("error when search global contact, message: [" + exx.getMessage() + "]", exx);
		}
		
		return FAILURE_JSON;
	}
	
	/**
	 * create the search bean from given request
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return LdapContactSearchBean - the search bean
	 */
	private LdapContactSearchBean getSearchBean(HttpServletRequest request) {
		// get the data from request
		int start = WebCommonService.toInt(getData(request, IContactInfoService.SEARCHING_START), 0);
		int limit = WebCommonService.toInt(getData(request, IContactInfoService.SEARCHING_LIMIT), this.getDefaultLimit());
		String keyword= getData(request, IContactInfoService.KEYWORD_PARAM);
		String domain = getData(request, IContactInfoService.GROUP_PARAM);
		
		// get the token
		String token = null;
		if(start == 0) {
			// put data to cache again
			removeDataFromCache(SEARCH_CACHE_KEY);
		} else {
			// get the tokens from cache
			token = getDataFromCache(SEARCH_CACHE_KEY, String.class);
		}
		
		// create the search bean
		LdapContactSearchBean searchBean = new LdapContactSearchBean(start, limit, token);
		searchBean.setKey(keyword);
		if(DOMAIN.equals(this.category)) {
		  this.getCode();
		  searchBean.setDomainName(domain); 
		}else if(GROUP.equals(this.category)) {
		  searchBean.setDomainName(getDomain());
		  searchBean.setDepartment(domain);
		}
		
				
		// return the search bean.
		return searchBean;
	}
	
	/**
	 * Load all domain or group in system
	 * 
	 * @param request HttpServletRequest - the given HTTP request
	 * @return JSON - the search result
	 * @throws WebOSServiceException - when there is any error happens
	 */
	protected JSON loadDomain(HttpServletRequest request) throws WebOSBOException {
	  try {
            List<JSON> jsons = new ArrayList<JSON>();
            JSONObject json = null;
            
            if(this.category.equals(DOMAIN)) {
              List<LdapMailDomain> domains = this.domainManager.findActiveDomain();
              
              // there is no active domain
              if(domains == null) return SUCCESS_JSON;
              // create the list to contain JSON object
              for(LdapMailDomain domain : domains) {
                json = new JSONObject();
                json.accumulate(IContactInfoService.DOMAIN_NAME, domain.getDomainName())
                        .accumulate(IContactInfoService.DOMAIN_DESC, StringService.hasLength(domain.getDescription())?domain.getDescription(): " ");
                jsons.add(json);
              }
            }else if(this.category.equals(GROUP)) {
               List<LdapGroup> groups = this.groupManager.findAllRealGroups(getDomain());
              
              // there is no active domain
              if(groups == null) return SUCCESS_JSON;
              // create the list to contain JSON object
              for(LdapGroup group : groups) {
                json = new JSONObject();
                json.accumulate(IContactInfoService.DOMAIN_NAME, group.getName())
                        .accumulate(IContactInfoService.DOMAIN_DESC, 
                            StringService.hasLength(group.getDescription())
                                    ?group.getDescription(): " ");
                jsons.add(json);
              }
            }
            
            // convert to JSON array
            return JSONService.toJSONArray(jsons);        
          } catch (LotusException ex) {
                  logger.error("ERROR while load domain", ex);
                  return FAILURE_JSON;
          }
	}
}
