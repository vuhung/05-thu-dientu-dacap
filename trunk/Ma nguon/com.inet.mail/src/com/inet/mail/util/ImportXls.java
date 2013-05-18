/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

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
package com.inet.mail.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.naming.Context;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import com.inet.base.ejb.ServiceLocator;
import com.inet.base.ejb.ServiceLocatorManager;
import com.inet.base.service.StringService;
import com.inet.base.uuid.UUID;
import com.inet.base.uuid.UUIDGenerator;
import com.inet.base.uuid.UUIDGeneratorFactory;
import com.inet.mail.business.sr.MailAcctConfigInfoRemoteSL;
import com.inet.mail.data.MailReceiverObject;
import com.inet.mail.data.MailReceiverProtocol;
import com.inet.mail.data.MailSecurity;

public class ImportXls {
  private static final int FIELD_EMAIL = 2;
  private static final int FIELD_FULLNAME = 1;
  private static final int FIELD_ACCOUNT = 3;
  private static final int FIELD_HO = 4;
  private static final int FIELD_LOT = 5;
  private static final int FIELD_TEN = 6;
  private static final UUIDGenerator UUID_GENERATOR = UUIDGeneratorFactory.getInstance(UUID.UUID_VERSION_3);
  private StringBuffer ldap = new StringBuffer();
  /**
   * Get the default properties
   * 
   * @return - Properties
   */
  protected Properties getProperties() {
    Properties env = new Properties();
    env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
    env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    env.setProperty(Context.PROVIDER_URL, "tntan:1099");
//    env.setProperty(Context.SECURITY_PRINCIPAL, "tntan@truthinet.com.vn");
//    env.setProperty(Context.SECURITY_CREDENTIALS, "{MD5}MEdtj4mQdRJfP4TCp1SssQ==");
//
//    SecurityAssociation.setPrincipal(new SimplePrincipal("tntan@truthinet.com.vn"));
//    SecurityAssociation.setCredential("{MD5}MEdtj4mQdRJfP4TCp1SssQ==".toCharArray());
    return env;
  }

  public void init(String filePath) {
    FileInputStream fs = null;
    try {
      fs = new FileInputStream(new File(filePath));
      contentReading(fs);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        fs.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Returns the Headings used inside the excel sheet
  public void getHeadingFromXlsFile(Sheet sheet) {
    int columnCount = sheet.getColumns();
    for (int i = 0; i < columnCount; i++) {
      System.out.println(sheet.getCell(i, 0).getContents());
    }
  }

  public void contentReading(InputStream fileInputStream) {
    WorkbookSettings ws = null;
    Workbook workbook = null;
    Sheet s = null;
    Cell rowData[] = null;
    int rowCount = '0';
    @SuppressWarnings("unused")
    int columnCount = 0;
    int totalSheet = 0;
    
    //ServiceLocator locator = ServiceLocatorManager.getInstance().getServiceLocator(this.getProperties());
    //MailAcctConfigInfoRemoteSL bean = locator.getRemoteBean("MailAcctConfigInfoSLBean",MailAcctConfigInfoRemoteSL.class);

    try {
      ws = new WorkbookSettings();
      ws.setLocale(new Locale("vn", "VN"));
      ws.setEncoding("UTF-8");
      workbook = Workbook.getWorkbook(fileInputStream, ws);

      totalSheet = workbook.getNumberOfSheets();
      if (totalSheet > 0) {
        System.out.println("Total Sheet Found:" + totalSheet);
        for (int j = 0; j < totalSheet; j++) {
          System.out.println("Sheet Name:" + workbook.getSheet(j).getName());
        }
      }

      // Getting Default Sheet i.e. 0
      s = workbook.getSheet(0);

      // Reading Individual Cell
      getHeadingFromXlsFile(s);

      // Total Total No Of Rows in Sheet, will return you no of rows that are
      // occupied with some data
      System.out.println("Total Rows inside Sheet:" + s.getRows());
      rowCount = s.getRows();

      // Total Total No Of Columns in Sheet
      System.out.println("Total Column inside Sheet:" + s.getColumns());
      columnCount = s.getColumns();
      // Reading Individual Row Content
      for (int i = 1; i < rowCount; i++) {
        // Get Individual Row
        rowData = s.getRow(i);
        if (rowData[0].getContents().length() != 0) { // the first date column
                                                      // must not null
          String email = rowData[ImportXls.FIELD_EMAIL].getContents();
          email = StringService.toLowerCase(email, 0);
          String fullName = new String(rowData[ImportXls.FIELD_FULLNAME].getContents().getBytes(),"UTF-8");
          System.out.println(rowData[ImportXls.FIELD_FULLNAME].getContents().getBytes());
          //String fullName = rowData[ImportXls.FIELD_FULLNAME].getContents();
          String account = rowData[ImportXls.FIELD_ACCOUNT].getContents();
          account = StringService.toLowerCase(account, 0);
          String ho = rowData[ImportXls.FIELD_HO].getContents();
          String lot = rowData[ImportXls.FIELD_LOT].getContents();
          String ten = rowData[ImportXls.FIELD_TEN].getContents();
          String userCode = UUID_GENERATOR.generate(email).toString();
          System.out.println("==================================");
          System.out.println("FullName>>>" + ho + " " + lot + " " + ten);
          //System.out.println("FullName>>>" + fullName);
          //MailReceiverObject object = initMailReceiver(email, "123456");
          /*TODO:: TAN TRUONG OPEN COMMENT HERE*/
          //add configuration to database
          //bean.addAccount(userCode, fullName, object);
          
          //add2Ldap(account, ho , lot, ten, userCode);
        }
      }
      
      try {
        BufferedWriter out = new BufferedWriter(new FileWriter("/home/tntan/Desktop/gialai.ldif"));
        out.write(ldap.toString());
        out.close();
       } catch (IOException e){ 
        System.out.println("Exception ");
       }
      
      workbook.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BiffException e) {
      e.printStackTrace();
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }
  
  /**
   * Initialize Mail Receiver Object
   * @param email email address
   * @return
   */
  public MailReceiverObject initMailReceiver(String email, String password){
    MailReceiverObject object = new MailReceiverObject(email);
    object.setActive(true);
    object.setProtocol(MailReceiverProtocol.POP3);
    object.setSecurity(MailSecurity.NONE);
    object.setServerName("mail.tayninh.gov.vn");
    object.setServerPort(110);
    object.setAccountPassword(MailService.encrypt(email,password));
    object.setAccountName(email);
    object.setEmailAddress(email);
    object.setDescription(email);
    
    object.setSMTPAccountName(email);
    object.setSMTPSecurity(MailSecurity.TLS);
    object.setSMTPServerName("mail.tayninh.gov.vn");
    object.setSMTPServerPort(25);
    object.setSMTPAccountPassword(MailService.encrypt(email,password));
    return object;
  }
  
  // add account to LDAP
  public void add2Ldap(String account, String aho, String btenlot, String cten, String dma){
    StringBuffer template = new StringBuffer("");
    template.append("# zaccount@tayninh.gov.vn, common, tayninh.gov.vn").append('\n');
    template.append("dn: uid=zaccount@tayninh.gov.vn,ou=common,dc=tayninh,dc=gov,dc=vn").append('\n');
    template.append("deleted: FALSE").append('\n');
    template.append("initials: zbtenlot").append('\n');
    template.append("givenName: zcten").append('\n');
    template.append("sn: zaho").append('\n');
    template.append("userPassword:: e1NNRDV9MmhxMkRpOVlFMXlVUzBzM1JvUmZRNTlXdU50d0ZLOUc=").append('\n');
    template.append("mail: zaccount@tayninh.gov.vn").append('\n');
    template.append("userCode: zdma").append('\n');
    template.append("objectClass: top").append('\n');
    template.append("objectClass: iNetPerson").append('\n');
    template.append("objectClass: iNetCommonPerson").append('\n');
    template.append("timeZone: +0700").append('\n');
    template.append("uid: zaccount@tayninh.gov.vn").append('\n');
    template.append("c: vn").append('\n');
    template.append('\n');

    template.append("# zaccount@tayninh.gov.vn, people, tayninh.gov.vn, org, tayninh.gov.vn").append('\n');
    template.append("dn: uid=zaccount@tayninh.gov.vn,ou=people,ou=tayninh.gov.vn,ou=org,dc=tayninh,dc=gov,dc=vn").append('\n');
    template.append("mail: zaccount@tayninh.gov.vn").append('\n');
    template.append("userPassword:: e1NNRDV9MmhxMkRpOVlFMXlVUzBzM1JvUmZRNTlXdU50d0ZLOUc=").append('\n');
    template.append("uid: zaccount@tayninh.gov.vn").append('\n');
    template.append("givenName: zcten").append('\n');
    template.append("objectClass: top").append('\n');
    template.append("objectClass: person").append('\n');
    template.append("objectClass: organizationalPerson").append('\n');
    template.append("objectClass: inetOrgPerson").append('\n');
    template.append("initials: zbtenlot").append('\n');
    template.append("sn: zaho").append('\n');
    template.append("cn: zaho zbtenlot zcten").append('\n');
    template.append('\n');

    template.append("# zaccount@tayninh.gov.vn, tayninh.gov.vn, mail, tayninh.gov.vn").append('\n');
    template.append("dn: mail=zaccount@tayninh.gov.vn,vd=tayninh.gov.vn,ou=mail,dc=tayninh,dc=gov,dc=vn").append('\n');
    template.append("vdHome: /home/vmail/domains").append('\n');
    template.append("forwardActive: FALSE").append('\n');
    template.append("objectClass: top").append('\n');
    template.append("objectClass: amavisAccount").append('\n');
    template.append("objectClass: VirtualForward").append('\n');
    template.append("objectClass: VirtualMailAccount").append('\n');
    template.append("objectClass: uniqueObject").append('\n');
    template.append("lastChange: 1299889523").append('\n');
    template.append("userCode: zdma").append('\n');
    template.append("userPassword:: e1NNRDV9MmhxMkRpOVlFMXlVUzBzM1JvUmZRNTlXdU50d0ZLOUc=").append('\n');
    template.append("accountActive: TRUE").append('\n');
    template.append("amavisBypassVirusChecks: TRUE").append('\n');
    template.append("uid: zaccount@tayninh.gov.vn").append('\n');
    template.append("mail: zaccount@tayninh.gov.vn").append('\n');
    template.append("cn: zbtenlot zcten").append('\n');
    template.append("amavisBypassSpamChecks: TRUE").append('\n');
    template.append("amavisSpamTagLevel: -9999").append('\n');
    template.append("maildrop: null").append('\n');
    template.append("mailbox: tayninh.gov.vn/zaccount/").append('\n');
    template.append("otherTransport: phamm:").append('\n');
    template.append("amavisSpamKillLevel: 6.0").append('\n');
    template.append("smtpAuth: TRUE").append('\n');
    template.append("sn: zaho").append('\n');
    template.append("creationDate: 20110312002523Z").append('\n');
    template.append("amavisSpamTag2Level: 5.5").append('\n');
    template.append("quota: 52428800").append('\n');
    template.append('\n');
    
    
    String nguoidung = template.toString(); 
    nguoidung = nguoidung.replaceAll("zaccount", account);
    nguoidung = nguoidung.replaceAll("zaho", aho);
    nguoidung = nguoidung.replaceAll("zbtenlot", btenlot);
    nguoidung = nguoidung.replaceAll("zcten", cten);
    nguoidung = nguoidung.replaceAll("zdma", dma);
    
    ldap.append(nguoidung);
  }
  
  public static void main(String[] args) {
    try {
      ImportXls xlReader = new ImportXls();
      xlReader.init("/home/tntan/Desktop/gialaiss.xls");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String readFileAsString(String filePath) throws java.io.IOException{
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(
              new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead=0;
      while((numRead=reader.read(buf)) != -1){
          fileData.append(buf, 0, numRead);
      }
      reader.close();
      return fileData.toString();
  }
}