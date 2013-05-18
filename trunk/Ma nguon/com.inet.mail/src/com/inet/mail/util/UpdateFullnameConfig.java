/**
 * 
 */
package com.inet.mail.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.ldap.LdapContext;

import com.inet.base.service.StringService;
import com.inet.ldap.support.spring.LdapContextSource;
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

	public void executeSQL() {
		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM mail_account_configure_info");
			int i = 0;
			while (rs.next() && i++ < 50) {
				System.out.println(rs.getString("mail_configure_acc_fullname"));
			}

			rs.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			displaySQLErrors(e);
		}
	}

	/**
	 * initialize ldap
	 */
	public void initAccountManager(){
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrls(new String[]{"ldap://mail.truthinet.com.vn/"});
		//contextSource.setPooled(true);
		contextSource.setBase("dc=truthinet,dc=com,dc=vn");
		contextSource.setUserName("cn=admin,dc=truthinet,dc=com,dc=vn");
		contextSource.setPassword("inet@0106");
		contextSource.setDirObjectFactory(com.inet.ldap.support.DefaultDirObjectFactory.class);
		contextSource.setAuthenticatedReadOnly(true);
		System.out.println(contextSource.getReadOnlyContext());
		
		LdapTemplate accountLdapOperations = new LdapTemplate(contextSource);
		this.accountManager = new LotusLdapAccountManager(accountLdapOperations,"ou=common");
	}
	
	/**
	 * Get full name from given user code
	 * @param code
	 * @return
	 */
	public String getFullName(String code){
		 LdapAccount account = this.accountManager.findByName(code, true);
		 
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UpdateFullnameConfig config = new UpdateFullnameConfig();
		//System.out.println(config.getFullName("7EB4B43D-013B-1000-F1E1-001A4D7576CA"));
		System.out.println(config.getFullName("tntan@inetcloud.vn"));
//		config.connectToDB();
//		config.executeSQL();

	}
}
