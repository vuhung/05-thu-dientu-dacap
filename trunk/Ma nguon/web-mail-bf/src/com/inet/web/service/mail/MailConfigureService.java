/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.web.service.mail;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.CommonService;
import com.inet.base.service.StringService;
import com.inet.lotus.account.Account;
import com.inet.lotus.account.manage.AccountManager;
import com.inet.lotus.account.manage.ldap.LdapAccountManager;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.ldap.LdapMailAccount;
import com.inet.lotus.mail.manage.ldap.LdapMailAccountManager;
import com.inet.mail.conf.PersonMailConfig;
import com.inet.mail.data.FolderType;
import com.inet.mail.data.MailReceiverDTO;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.data.MailReceiverProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.persistence.MailAcctConfigInfo;
import com.inet.mail.persistence.MailSignatureInfo;
import com.inet.mail.util.MailService;
import com.inet.web.bo.exception.WebOSBOException;
import com.inet.web.bo.mail.MailBridgeBO;
import com.inet.web.bo.mail.MailConfigureBO;
import com.inet.web.bo.mail.MailSignatureInfoBO;
import com.inet.web.common.json.JSONService;
import com.inet.web.service.AbstractWebOSService;
import com.inet.web.service.exception.WebOSServiceException;
import com.inet.web.service.mail.utils.MailConstants;
import com.inet.web.service.utils.WebCommonService;

/**
 * MailConfigureService.
 * 
 * @author <a href="mailto:tntan@truthinet.com"> Truong Ngoc Tan</a>
 * @version 1.0b
 */
public class MailConfigureService extends AbstractWebOSService{
	//~ Static fields =========================================================
	/* class logger. */
	private static final INetLogger logger = INetLogger.getLogger(MailConfigureService.class);
	
	//~ Instance fields =======================================================
	/**
	 * the {@link MailConfigureBO} instance.
	 */
	private MailConfigureBO mailConfgBO;
	/**
	 * the {@link MailBridgeBO} instance.
	 */
	private MailBridgeBO mailBridgeBO;
	/**
	 * the {@link MailSignatureInfoBO} instance.
	 */
	private MailSignatureInfoBO mailSignatureBO;
	/**
	 * the {@link LdapMailAccountManager} instance.
	 */
	private LdapMailAccountManager mailAccountManager;
	/**
	 * the {@link MailReceiverObject} instance.
	 */
	private MailReceiverObject mailReceiverObject;

	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailConfigureService</tt> instance from
	 * the given {@link AccountManager} instance, 
	 * {@link MailConfigureBO} instance, {@link MailBridgeBO} instance,
	 * {@link MailSignatureInfoBO} instance, the {@link LdapAccountManager} instance
	 * and {@link MailReceiverObject} instance.
	 *  
	 * @param accountManager the given {@link AccountManager} instance.
	 * @param mailConfgBO the given {@link MailConfigureBO} instance.
	 * @param mailBridgeBO the given {@link MailBridgeBO} instance.
	 * @param mailSignatureBO the given {@link MailSignatureInfoBO} instance.
	 * @param mailAccountManager the given {@link LdapMailAccountManager} instance.
	 * @param mailReceiverObject the given {@link MailReceiverObject} instance.
	 */
	public MailConfigureService (
			AccountManager<Account> accountManager,
			MailConfigureBO mailConfgBO,
			MailBridgeBO mailBridgeBO, MailSignatureInfoBO mailSignatureBO, 
			LdapMailAccountManager mailAccountManager, MailReceiverObject mailReceiverObject){
		super(accountManager) ;
		
		this.mailConfgBO = mailConfgBO;
		this.mailBridgeBO = mailBridgeBO;
		this.mailSignatureBO = mailSignatureBO;
		this.mailAccountManager = mailAccountManager;
		this.mailReceiverObject = mailReceiverObject;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.service.AbstractWebOSService#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public JSON execute(HttpServletRequest request, HttpServletResponse response)
			throws WebOSServiceException {
		String action = getData(request, MailConstants.ACTION);
		if(MailConstants.ACTION_CFG_LOAD.equals(action)){
			return loadMailConfig(request);
		}else if(MailConstants.ACTION_SAVE.equals(action)){
			return saveConfig(request);
		}else if(MailConstants.ACTION_CFG_UPDATE_PASSWORD.equals(action)){
			return updatePW(request);
		}else if(MailConstants.ACTION_CFG_LOAD_DEFAULT.equals(action)) {
			return this.loadDefaultConfig();
		}
		return FAILURE_JSON;
	}
	/////////////////////////////////////////////////
	//// Load mail configure 
	//
	private JSON loadMailConfig(HttpServletRequest request){
		String mode = getData(request, MailConstants.MODE);
		MailAcctConfigInfo config = mailBridgeBO.loadMailConfig() ;
		if(config == null) return FAILURE_JSON;
		
		if(MailConstants.CFG_MODE_DETAIL.equals(mode)){
			return getJSONConfig(config);
		}else if(MailConstants.CFG_MODE_SMTP.equals(mode)){
			return getJSONSmtpDefault(config);
		}else if(MailConstants.CFG_MODE_SMTP_ACCOUNT.equals(mode)){
			return getJSONSmtpAccount();
		}else if(MailConstants.CFG_MODE_PERSONAL.equals(mode)){
			return getJSONPersonalConfig();
		}
		
		return FAILURE_JSON;
	}
	
	/**
	 * get JSON from given mail configure
	 * @param config MailConfigure
	 * @return JSON
	 */
	private JSON getJSONConfig(MailAcctConfigInfo config){
		if(config == null) return FAILURE_JSON;
		
		List<MailSignatureInfo> signatures = this.mailSignatureBO.loadSignature();
		
		config.setReceiverObject(MailReceiverDTO.convertFrom(config.getReceiverServer()));
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.CFG_COMMON_INFO, getJSONCommon(config))
			.accumulate(MailConstants.CFG_ACCOUNTS, JSONService.toJSONArray(getJsonAccounts(config.getReceiverObject())))
			.accumulate(MailConstants.CFG_SIGNATURES, JSONService.toJSONArray(getJsonSignature(signatures)));
		
		return json;
	}
	
	private JSON getJSONConfig(MailAcctConfigInfo config, List<MailSignatureInfo> signatures){
		if(config == null) return FAILURE_JSON;
				
		config.setReceiverObject(MailReceiverDTO.convertFrom(config.getReceiverServer()));
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.CFG_COMMON_INFO, getJSONCommon(config))
			.accumulate(MailConstants.CFG_ACCOUNTS, JSONService.toJSONArray(getJsonAccounts(config.getReceiverObject())))
			.accumulate(MailConstants.CFG_SIGNATURES, JSONService.toJSONArray(getJsonSignature(signatures)));
		
		return json;
	} 
	
	private JSON getJSONSmtpDefault(MailAcctConfigInfo config){
		if(config == null) return FAILURE_JSON;
		config.setReceiverObject(MailReceiverDTO.convertFrom(config.getReceiverServer()));
		JSONObject json = new JSONObject();
		
		// add SMTP account
		json.accumulate(MailConstants.CFG_DEFAULT_SMTP, config.getDefaultSMTP());
		
		MailReceiverDTO receiverDTO = config.getReceiverObject(); 
		List<JSONObject> smtpJons = new ArrayList<JSONObject>();
		if(receiverDTO!= null && receiverDTO.getAccounts() != null){
			for(MailReceiverObject item : receiverDTO.getAccounts()){
				if(item.getActive()){
					JSONObject smtp = new JSONObject();
					smtp.accumulate(MailConstants.CFG_ADDRESS, item.getEmailAddress())
						.accumulate(MailConstants.CFG_SMTP_ACCOUNT, MailService.createTicket(item.getSMTPServerName(), item.getSMTPAccountName()));
					smtpJons.add(smtp);
				}
			}
		}
		
		json.accumulate(MailConstants.CFG_SMTPS, JSONService.toJSONArray(smtpJons));
		
		// add signature
		List<MailSignatureInfo> signs = this.mailSignatureBO.loadSignature();
		List<JSONObject> signJons = new ArrayList<JSONObject>();
		for(MailSignatureInfo item : signs){
			JSONObject sign = new JSONObject();
			sign.accumulate(MailConstants.CFG_SIGN_NAME, item.getName())
				.accumulate(MailConstants.CFG_SIGN_CONTENT, item.getContent())
				.accumulate(MailConstants.CFG_SIGN_USED, String.valueOf(item.getCurrentUsed()));
			
			signJons.add(sign);
		}
		
		json.accumulate(MailConstants.CFG_SIGNATURES, JSONService.toJSONArray(signJons));
		return  json;
	}
	
	/**
	 * get JSON common info  
	 * @param config MailConfigure
	 * @return JSONObject
	 */
	private JSONObject getJSONCommon(MailAcctConfigInfo config){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.CFG_ID, config.getId())
			.accumulate(MailConstants.CFG_OWNER, config.getOwner())
			.accumulate(MailConstants.CFG_FULL_NAME, config.getFullname())
			.accumulate(MailConstants.CFG_DEFAULT_SMTP, config.getDefaultSMTP())
			.accumulate(MailConstants.CFG_AUTO_CHECK, config.getRefresh());			
		return json;
	}
	
	/**
	 * get JSON accounts  
	 * @param receivers MailReceiverDTO
	 * @return List<JSONObject>
	 */
	private List<JSONObject> getJsonAccounts(MailReceiverDTO receiverDTO ){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		if(receiverDTO != null && receiverDTO.getAccounts() != null){
			for(MailReceiverObject item : receiverDTO.getAccounts()){
				JSONObject json = new JSONObject();
				json.accumulate(MailConstants.CFG_ACTIVE, item.getActive())
					.accumulate(MailConstants.CFG_ADDRESS, item.getEmailAddress())
					.accumulate(MailConstants.CFG_DESCRIPTION, item.getDescription())
					.accumulate(MailConstants.CFG_SERVER, item.getServerName())
					.accumulate(MailConstants.CFG_PROTOCOL, item.getProtocol().toString())
					.accumulate(MailConstants.CFG_PORT, item.getServerPort())
					.accumulate(MailConstants.CFG_SECURITY, item.getSecurity().toString())
					.accumulate(MailConstants.CFG_USER, item.getAccountName())
					.accumulate(MailConstants.CFG_PASSWORD, MailService.decrypt(item.getAccountName(), item.getAccountPassword()))
					.accumulate(MailConstants.CFG_SMTP_SERVER, item.getSMTPServerName())
					.accumulate(MailConstants.CFG_SMTP_PORT, item.getSMTPServerPort())
					.accumulate(MailConstants.CFG_SMTP_SECURITY, item.getSMTPSecurity().toString())
					.accumulate(MailConstants.CFG_SMTP_USER, item.getSMTPAccountName())
					.accumulate(MailConstants.CFG_SMTP_PASSWORD, MailService.decrypt(item.getSMTPAccountName(), item.getSMTPAccountPassword()));
				jsons.add(json);
			}
		}		
		return jsons;
	}
	
	/**
	 * @param sign
	 * @return
	 */
	private List<JSONObject> getJsonSignature(List<MailSignatureInfo> signs){
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		if(signs != null){
			for(MailSignatureInfo sign : signs){
				JSONObject json = new JSONObject();
				json.accumulate(MailConstants.CFG_SIGN_ID, sign.getId())
					.accumulate(MailConstants.CFG_SIGN_NAME, sign.getName())
					.accumulate(MailConstants.CFG_SIGN_CONTENT, sign.getContent())
					.accumulate(MailConstants.CFG_SIGN_USED, String.valueOf(sign.getCurrentUsed()));
					
				jsons.add(json);
			}
		}		
		return jsons;
	}
	
	////////////////////////////////////
	//// Save mail configure
	//
	
	/**
	 * @param request
	 * @return
	 */
	private JSON saveConfig(HttpServletRequest request){
		JSONObject jConfig = WebCommonService.toJSONObject(request, MailConstants.OBJECT);
		if(jConfig == null) return FAILURE_JSON;
		
		MailAcctConfigInfo config = mailBridgeBO.loadMailConfig() ;
		if(config == null){
			config = new MailAcctConfigInfo();
		}
		// common information
		convertJSON2Common(jConfig, config);		
		// accounts
		convertJSON2Account(jConfig, config);
		
		List<MailSignatureInfo> signs = convertJSON2MailSignature(jConfig);
		List<MailSignatureInfo> signUpdate = new ArrayList<MailSignatureInfo>();
		List<MailSignatureInfo> signAddnew = new ArrayList<MailSignatureInfo>();
		List<Long> ids = WebCommonService.toArray(jConfig.getString(MailConstants.CFG_SIGN_REMOVE));
		
		// 
		for(MailSignatureInfo item : signs){
			if(item.getId() > 0L){
				signUpdate.add(item);
			}else{
				signAddnew.add(item);
			}
		}
		
		signs = mailSignatureBO.save(signAddnew, signUpdate, ids);

		try{
			if(config.getId() != null && config.getId() > 0 ){
				return getJSONConfig(mailConfgBO.update(config),signs);
			}else{
				
				return getJSONConfig(mailConfgBO.save(config),signs);
			}
		}catch(WebOSBOException ex){
			return FAILURE_JSON;
		}
	}	
	
	private JSON updatePW(HttpServletRequest request){
		JSONObject object = WebCommonService.toJSONObject(request, MailConstants.OBJECT);
		if(object == null) return FAILURE_JSON;
		
		String address = WebCommonService.toString(object, MailConstants.CFG_ADDRESS);
		String password = WebCommonService.toString(object, MailConstants.CFG_PASSWORD);
				
		MailAcctConfigInfo configure = mailBridgeBO.loadMailConfig() ;
		if(configure == null) return FAILURE_JSON;
		
		MailReceiverDTO receiverDTO =  configure.getReceiverObject();
		MailReceiverDTO newReceiverDTO = new MailReceiverDTO(); 
		for(MailReceiverObject item : receiverDTO.getAccounts()){
			if(item.getSMTPAccountName().equals(address)){
				MailReceiverObject newMailReceiverObject = item;
				newMailReceiverObject.setAccountPassword(MailService.encrypt(newMailReceiverObject.getAccountName(), password));
				newMailReceiverObject.setSMTPAccountPassword(MailService.encrypt(newMailReceiverObject.getSMTPAccountName(), password));
				newReceiverDTO.addAccount(newMailReceiverObject);
			}else{
				newReceiverDTO.addAccount(item);
			}
		}
		
		configure.setReceiverObject(newReceiverDTO );
		
		try{
			this.mailConfgBO.update(configure);
		}catch(WebOSBOException wexp){
			return FAILURE_JSON;
		}
		
		return SUCCESS_JSON;
	}
	
	/**
	 * @param jConfig
	 * @param config
	 */
	private void convertJSON2Common(JSONObject jConfig, MailAcctConfigInfo config){
		//email address
		JSONObject jCommon = WebCommonService.toObject(jConfig, MailConstants.CFG_COMMON_INFO); 
		// email configure
		config.setOwner(getCode());
		// email full name
		config.setFullname(jCommon.getString(MailConstants.CFG_FULL_NAME));
		//email default server SMTP
		config.setDefaultSMTP(jCommon.getString(MailConstants.CFG_DEFAULT_SMTP));
		//email port
		config.setRefresh(WebCommonService.toInt(jCommon.getString(MailConstants.CFG_AUTO_CHECK),0));
	}
	
	/**
	 * @param jConfig
	 * @param config
	 */
	private void convertJSON2Account(JSONObject jConfig, MailAcctConfigInfo config){
		JSONArray jaccounts = jConfig.getJSONArray(MailConstants.CFG_ACCOUNTS);
		MailReceiverDTO receiverDTO = new MailReceiverDTO();
		
		for(int i = 0; i< jaccounts.size() ; i++){
			JSONObject json = jaccounts.getJSONObject(i);
			MailReceiverObject account = new MailReceiverObject(json.getString(MailConstants.CFG_USER)); 
			
			// active 
			account.setActive(Boolean.valueOf(json.getString(MailConstants.CFG_ACTIVE)));
			// email address 
			account.setEmailAddress(json.getString(MailConstants.CFG_ADDRESS));
			// description
			account.setDescription(json.getString(MailConstants.CFG_DESCRIPTION));
			// server 
			account.setServerName(json.getString(MailConstants.CFG_SERVER));
			// protocol
			account.setProtocol(MailReceiverProtocol.valueOf(json.getString(MailConstants.CFG_PROTOCOL)));
			// port
			account.setServerPort(json.getInt(MailConstants.CFG_PORT));
			// security
			account.setSecurity(MailSecurity.valueOf(json.getString(MailConstants.CFG_SECURITY)));
			// user
			account.setAccountName(json.getString(MailConstants.CFG_USER));
			// pass
			account.setAccountPassword(MailService.encrypt(account.getAccountName(),
										json.getString(MailConstants.CFG_PASSWORD)));
			
			
			// SMTP server 
			account.setSMTPServerName(json.getString(MailConstants.CFG_SMTP_SERVER));
			// SMTP port
			account.setSMTPServerPort(json.getInt(MailConstants.CFG_SMTP_PORT));
			// SMTP security
			account.setSMTPSecurity(MailSecurity.valueOf(json.getString(MailConstants.CFG_SMTP_SECURITY)));
			// SMTP user
			account.setSMTPAccountName(json.getString(MailConstants.CFG_SMTP_USER));
			// SMTP pass
			account.setSMTPAccountPassword(MailService.encrypt(account.getSMTPAccountName(),
										json.getString(MailConstants.CFG_SMTP_PASSWORD)));
			
			
			
			receiverDTO.addAccount(account);
		}
		config.setReceiverObject(receiverDTO);
	}
	
	/**
	 * @param jSigns
	 * @return
	 */
	private List<MailSignatureInfo> convertJSON2MailSignature(JSONObject object){
		List<MailSignatureInfo> signs = new ArrayList<MailSignatureInfo>();
		JSONArray jSigns = object.getJSONArray(MailConstants.CFG_SIGNATURES);
		for(int i= 0; i<jSigns.size();i++){
			JSONObject json = jSigns.getJSONObject(i);
			MailSignatureInfo mailSignatureInfo = new MailSignatureInfo();
			
			mailSignatureInfo.setName(json.getString(MailConstants.CFG_SIGN_NAME));
			
			mailSignatureInfo.setContent(json.getString(MailConstants.CFG_SIGN_CONTENT));
			
			mailSignatureInfo.setOwner(getCode());
			
			mailSignatureInfo.setId(json.getLong(MailConstants.CFG_SIGN_ID));
			
			mailSignatureInfo.setCurrentUsed(json.getString(MailConstants.CFG_SIGN_USED).equals("Y")?CommonService.YES:CommonService.NO);
			
			signs.add(mailSignatureInfo);
		}
		return signs;
	}
	
	/**
	 * get SMTP accounts and password
	 * @return JSON
	 */
	private JSON getJSONSmtpAccount(){
		MailAcctConfigInfo configInfo = mailBridgeBO.loadMailConfig() ;
		
		JSONObject jsonConfig = new JSONObject();
		
		List<JSONObject> jsons = new ArrayList<JSONObject>(); 
		if(configInfo != null){
			MailReceiverDTO receiverDTO = configInfo.getReceiverObject();
			if(receiverDTO != null && receiverDTO.getAccounts() != null && receiverDTO.getAccounts().size() > 0){
				for(MailReceiverObject item: configInfo.getReceiverObject().getAccounts()){
					if(item.getActive()){
						JSONObject object = new JSONObject();
						object.accumulate(MailConstants.CFG_SMTP_ACCOUNT, item.getServerName() + ":" + item.getAccountName())
							  .accumulate(MailConstants.CFG_ADDRESS, item.getSMTPAccountName())
							  .accumulate(MailConstants.CFG_PASSWORD, StringService.hasLength(item.getAccountPassword()) && StringService.hasLength(item.getSMTPAccountPassword()));	 
						jsons.add(object);
					}
				}
			}
			
			// smtp default
			if(StringService.hasLength(configInfo.getDefaultSMTP())){
				jsonConfig.accumulate(MailConstants.CFG_DEFAULT_SMTP , configInfo.getDefaultSMTP());
			}
		}
		
		jsonConfig.accumulate(MailConstants.CFG_SMTPS, JSONService.toJSONArray(jsons));
				
		return jsonConfig;
	}
	
	/**
	 * get JSON personal configure
	 * @return JSON
	 */
	private JSON getJSONPersonalConfig(){
		PersonMailConfig config = this.mailBridgeBO.getPersonalConfig(getCode());
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.FOLDER, getJSONFolderDefault(config))
			.accumulate(MailConstants.CFG_SMTP_ACCOUNT, getJSONSmtpAccount())
			.accumulate(MailConstants.CFG_AUTO_CHECK, config.getRefresh());
		return json;
	}
	
	/**
	 * get JSON folder default
	 * @param config PersonMailConfig
	 * @return JSON
	 */
	private JSON getJSONFolderDefault(PersonMailConfig config){
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.FOLDER_INBOX, config.getFolders().get(FolderType.INBOX))
			.accumulate(MailConstants.FOLDER_OUTBOX, config.getFolders().get(FolderType.OUTBOX))
			.accumulate(MailConstants.FOLDER_SENT, config.getFolders().get(FolderType.SENT))
			.accumulate(MailConstants.FOLDER_DRAFT, config.getFolders().get(FolderType.DRAFT))
			.accumulate(MailConstants.FOLDER_SPAM, config.getFolders().get(FolderType.SPAM))
			.accumulate(MailConstants.FOLDER_TRASH, config.getFolders().get(FolderType.TRASH));
			
		return json;
	}
	
	/**
	 * Reload the default configuration for login user
	 * 
	 * @return JSON - the return JSON object
	 */
	private JSON loadDefaultConfig() {
		try {
			// find all user's mail account
			List<LdapMailAccount> mailAccounts = this.mailAccountManager.findByUserCode(this.getCode());			
			if(mailAccounts != null && mailAccounts.size() > 0) {
				List<JSON> jsons = new ArrayList<JSON>();
				for(LdapMailAccount mailAccount: mailAccounts) {
					// fill the information
					this.mailReceiverObject.setEmailAddress(mailAccount.getEmail());
					this.mailReceiverObject.setDescription(mailAccount.getEmail());
					this.mailReceiverObject.setAccountName(mailAccount.getEmail());		
					this.mailReceiverObject.setSMTPAccountName(mailAccount.getEmail());		
					this.mailReceiverObject.setActive(mailAccount.isAccountActive());					
					
					// convert mail receiver object to JSON
					jsons.add(this.convertToJSON(this.mailReceiverObject));
				}
				return JSONService.toJSONArray(jsons);
			}
		} catch (LotusException e) {
			logger.error("ERROR while load default config");
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}
	
	/**
	 * convert mail receiver object to JSON
	 * 
	 * @param receiverObject MailReceiverObject - the mail receiver object
	 * @return JSON - the JSON object
	 */
	private JSON convertToJSON(MailReceiverObject receiverObject) {
		JSONObject json = new JSONObject();
		json.accumulate(MailConstants.CFG_ACTIVE, receiverObject.getActive())
			.accumulate(MailConstants.CFG_ADDRESS, receiverObject.getEmailAddress())
			.accumulate(MailConstants.CFG_DESCRIPTION, receiverObject.getDescription())
			.accumulate(MailConstants.CFG_SERVER, receiverObject.getServerName())
			.accumulate(MailConstants.CFG_PROTOCOL, receiverObject.getProtocol().toString())
			.accumulate(MailConstants.CFG_PORT, receiverObject.getServerPort())
			.accumulate(MailConstants.CFG_SECURITY, receiverObject.getSecurity().toString())
			.accumulate(MailConstants.CFG_USER, receiverObject.getAccountName())
			.accumulate(MailConstants.CFG_PASSWORD, MailService.decrypt(receiverObject.getAccountName(), receiverObject.getAccountPassword()))
			.accumulate(MailConstants.CFG_SMTP_SERVER, receiverObject.getSMTPServerName())
			.accumulate(MailConstants.CFG_SMTP_PORT, receiverObject.getSMTPServerPort())
			.accumulate(MailConstants.CFG_SMTP_SECURITY, receiverObject.getSMTPSecurity().toString())
			.accumulate(MailConstants.CFG_SMTP_USER, receiverObject.getSMTPAccountName())
			.accumulate(MailConstants.CFG_SMTP_PASSWORD, MailService.decrypt(receiverObject.getSMTPAccountName(), receiverObject.getSMTPAccountPassword()));
		
		return json;
	}
}