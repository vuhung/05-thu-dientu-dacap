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

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * UpdateQuota.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: UpdateQuota.java Nov 15, 2011 3:24:13 PM Tan Truong $
 * 
 * @since 1.0
 */
public class UpdateQuota {
  private StringBuffer ldap = new StringBuffer();
  private static final int FIELD_EMAIL = 2; 
  
  public void init(String filePath, String filename,  String domain) {
    FileInputStream fs = null;
    try {
      fs = new FileInputStream(new File(filePath + filename));
      contentReading(fs, filePath, domain);
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

  public void contentReading(InputStream fileInputStream, String path, String domain) {
    WorkbookSettings ws = null;
    Workbook workbook = null;
    Sheet s = null;
    Cell rowData[] = null;
    int rowCount = 0;

    try {
      ws = new WorkbookSettings();
      ws.setLocale(new Locale("vn", "VN"));
      ws.setEncoding("UTF-8");
      workbook = Workbook.getWorkbook(fileInputStream, ws);

      // Getting Default Sheet i.e. 0
      s = workbook.getSheet(0);

      // Total Total No Of Rows in Sheet, will return you no of rows that are
      // occupied with some data
      System.out.println("Total Rows inside Sheet:" + s.getRows());
      rowCount = s.getRows();

      // Total Total No Of Columns in Sheet
      System.out.println("Total Column inside Sheet:" + s.getColumns());
      // Reading Individual Row Content
      for (int i = 2; i < rowCount; i++) {
        // Get Individual Row
        rowData = s.getRow(i);
        
        if (rowData.length > 2 &&  rowData[0].getContents().length() != 0) {
          String email = rowData[UpdateQuota.FIELD_EMAIL].getContents();
          System.out.println(email);
          add2Ldap(email, domain);
        }
      }

      try {
        BufferedWriter out = new BufferedWriter(new FileWriter(path + domain + ".ldif"));
        out.write(ldap.toString());
        out.close();
      } catch (IOException e) {
        System.out.println("Exception ");
      }

      workbook.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BiffException e) {
      e.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // add account to LDAP
  public void add2Ldap(String email, String domain) {
    StringBuffer template = new StringBuffer("");
    template.append("dn: mail=email,vd=domain,ou=mail,dc=binhdinh,dc=gov,dc=vn").append('\n');
    template.append("changetype: modify").append('\n');
    template.append("replace: quota").append('\n');
    template.append("quota: 524288000").append('\n').append('\n');

    String nguoidung = template.toString();
    nguoidung = nguoidung.replaceAll("email", email);
    nguoidung = nguoidung.replaceAll("domain", domain);
    

    ldap.append(nguoidung);
  }

  public static String readFileAsString(String filePath) throws java.io.IOException {
    StringBuffer fileData = new StringBuffer(1000);
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    char[] buf = new char[1024];
    int numRead = 0;
    while ((numRead = reader.read(buf)) != -1) {
      fileData.append(buf, 0, numRead);
    }
    reader.close();
    return fileData.toString();
  }

  public static void main(String[] args) {
    try {
      UpdateQuota xlReader = new UpdateQuota();
      xlReader.init("/home/tntan/Desktop/update_quota/",
                    "email_taphuan.itacenter.vn.xls", 
                  "taphuan.itacenter.vn");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
