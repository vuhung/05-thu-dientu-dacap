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
package com.inet.web.service.mail.utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.inet.lotus.org.permission.ldap.LdapUser;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.data.AccountImport;
import com.inet.web.service.data.AccountImportInfo;

/**
 * ExportUtils.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ExportUtils.java Apr 6, 2011 6:21:34 PM Tan Truong $
 * 
 * @since 1.0
 */
public class ExportUtils {
  private static final int STT          = 0;
  private static final int FULL_NAME    = 1;
  
  private static final int USER         = 2;
  private static final int EMAIL        = 2;
  
  private static final int LAST_NAME    = 3;
  
  private static final int MIDDLE_NAME  = 4;
  private static final int FIRST_NAME   = 5;
  
  private static final int STATUS       = 6;
  
  private static final int DUPLICATE    = 7;
  
  /**
   * 
   * @param users
   * @param domain
   * @return
   * @throws WebOSException
   */
  public static byte[] exportAccount(List<LdapUser> users, String domain) throws WebOSException {
    try {
      ByteArrayOutputStream output = new ByteArrayOutputStream();

      Workbook workbook = new HSSFWorkbook();
      //Map<String, CellStyle> styles = createStyles(workbook);

      Sheet sheet = workbook.createSheet("Email list");
      PrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);
      sheet.setFitToPage(true);
      sheet.setHorizontallyCenter(true);

      for (int i = 0; i < users.size(); i++) {
        writeRecordAccount(sheet, users.get(i), i+1);
      }
      workbook.write(output);
      output.close();

      return output.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebOSException(e.getMessage(), e);
    }
  }
  
  /**
   * 
   * @param key
   * @return
   * @throws WebOSException
   */
  public static byte[] exportErrorAccount(String key) throws WebOSException {
    AccountImport accountImport = AccountImportCacheService.get(key);
    if (accountImport == null) {
      return null;
    }
    try {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      Workbook workbook = new HSSFWorkbook();

      Sheet sheet = workbook.createSheet("Email list");
      PrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);
      sheet.setFitToPage(true);
      sheet.setHorizontallyCenter(true);

      Row headerRow = sheet.createRow(0);
      headerRow.setHeightInPoints(30);
      headerRow.createCell(STT).setCellValue("STT");
      headerRow.createCell(FULL_NAME).setCellValue("FULL NAME");
      headerRow.createCell(USER).setCellValue("USER");
      headerRow.createCell(LAST_NAME).setCellValue("LAST NAME");
      headerRow.createCell(MIDDLE_NAME).setCellValue("MIDDLE NAME");
      headerRow.createCell(FIRST_NAME).setCellValue("FIRST NAME");
      headerRow.createCell(STATUS).setCellValue("STATUS");
      headerRow.createCell(DUPLICATE).setCellValue("DUPLICATE");
      
      for (int i = 0; i < accountImport.getError().size(); i++) {
        AccountImportInfo account = accountImport.getError().get(i);
        
        Row row = sheet.createRow(i+1);
        row.setHeightInPoints(40);
        row.createCell(STT).setCellValue(account.getNumber());
        row.createCell(FULL_NAME).setCellValue(account.getFullName());
        row.createCell(USER).setCellValue(account.getAccount());
        row.createCell(LAST_NAME).setCellValue(account.getLastName());
        row.createCell(MIDDLE_NAME).setCellValue(account.getMiddleName());
        row.createCell(FIRST_NAME).setCellValue(account.getFirstName());
        row.createCell(STATUS).setCellValue(getStatus(account.getStatus()));
        row.createCell(DUPLICATE).setCellValue(account.getExistAccount());
      }

      workbook.write(output);
      output.close();

      return output.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      throw new WebOSException(e.getMessage(), e);
    }
  }

  /**
   * @param status
   * @return
   */
  private static String getStatus(byte status) {
    switch (status) {
    case 1:
      return "Exist User";
    case 2:
      return "Exist user and email";
    case 3:
      return "Error";
    case 4:
      return "Duplicate";
    default:
      return "";
    }
  }
  
  /**
   * 
   * @param ws
   * @param cf
   * @param contact
   * @param index
   * @throws WriteException
   */
  private static void writeRecordAccount(Sheet sheet, LdapUser contact, int index){
    int r = index + 1;
    
    Row headerRow = sheet.createRow(r);
    headerRow.setHeightInPoints(12);
    headerRow.createCell(STT).setCellValue(index);
    headerRow.createCell(FULL_NAME).setCellValue(contact.getFullName());
    headerRow.createCell(EMAIL).setCellValue(contact.getEmail());
    headerRow.createCell(LAST_NAME).setCellValue(contact.getLastName());
    headerRow.createCell(MIDDLE_NAME).setCellValue(contact.getMiddleName());
    headerRow.createCell(FIRST_NAME).setCellValue(contact.getFirstName());
  }
}
