/**
 * 
 */
package com.inet.web.service.mail.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.inet.base.service.StringService;
import com.inet.ldap.support.DefaultDirObjectFactory;
import com.inet.ldap.support.spring.LdapTemplate;
import com.inet.lotus.account.ldap.LdapAccount;
import com.inet.lotus.account.manage.ldap.LotusLdapAccountManager;

/**
 * @author tntan
 * 
 */
public class UpdateFullnameConfig {

	Connection connection;
	LotusLdapAccountManager accountManager;
	
	private void displaySQLErrors(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState:     " + e.getSQLState());
		System.out.println("VendorError:  " + e.getErrorCode());
	}

	/**
	 * Constructor
	 */
	public UpdateFullnameConfig() {
		initAccountManager();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.err.println("Unable to find and load driver");
			System.exit(1);
		}
	}

	public void connectToDB() {
		try {
			connection = DriverManager
					.getConnection("jdbc:mysql://10.218.189.17:3306/webmail?user=webmail&password=webmail");
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}

	public void executeSQL(String code, String fullname) {
		try {
			Statement statement = connection.createStatement();

			System.out.println(statement.executeUpdate("UPDATE mail_account_configure_info SET mail_configure_acc_fullname = '"+
								fullname + "' WHERE mail_configure_owner = '" + code + "'"));
			
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}

	/**
	 * initialize ldap
	 */
	public void initAccountManager(){
		
		com.inet.ldap.support.LdapContextSource contextSource = new com.inet.ldap.support.LdapContextSource(
				new String[]{"ldap://10.218.189.21/"},
				"cn=admin,dc=gialai,dc=gov,dc=vn",
				"inet@0106",
				"dc=gialai,dc=gov,dc=vn",
				null,
				DefaultDirObjectFactory.class.getName(),
				true,
				null,
				true,
				true);
		
		LdapTemplate accountLdapOperations = new LdapTemplate(contextSource);
		this.accountManager = new LotusLdapAccountManager(accountLdapOperations, "ou=common",2);
	}
	
	/**
	 * Get full name from given user code
	 * @param code
	 * @return
	 */
	public String getFullName(String code){
		if(!StringService.hasLength(code)){
			return StringService.EMPTY_STRING;
		}
		LdapAccount account = this.accountManager.findByCode(code);
		 
		if(account != null){
			 String fullname = account.getLastName();
			 if(StringService.hasLength(account.getMiddleName())){
				 fullname = fullname +  " " + account.getMiddleName();
			 }
			 
			 fullname = fullname + " " + account.getFirstName();
			 
			 return fullname;
		 }
		 
		 return StringService.EMPTY_STRING;
	}
	
	public void update(){
		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM mail_account_configure_info LIMIT 4750,1000");
			int i = 0;
			String code = "";
			String fullname = "";
			while (rs.next()) {
				code = rs.getString("mail_configure_owner");
				fullname = getFullName(code);
				System.out.println(++i + "--" + fullname + "--" + code);
				if(StringService.hasLength(fullname)){
					executeSQL(code, fullname);
				}
			}

			rs.close();
			statement.close();
			
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateFullnameConfig config = new UpdateFullnameConfig();
		//System.out.println(config.getFullName("7EB4B43D-013B-1000-F1E1-001A4D7576CA"));
		System.out.println(config.getFullName("4F3D3D04-013B-1000-ECBC-150813121982"));
		config.connectToDB();
		config.update();
		//config.executeSQL("4F3D3D04-013B-1000-ECBC-150813121982", "Tan Truong");

	}
}
