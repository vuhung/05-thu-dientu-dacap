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
package com.inet.web.service.spi.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.StringService;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.org.permission.ldap.LdapGroup;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.data.AccountExport;
import com.inet.web.service.data.AccountExportInfo;
import com.inet.web.service.mail.utils.AccountExportCacheService;
import com.inet.web.spi.download.AbstractContentWriterSPI;
import com.inet.web.spi.download.ContentWriterType;

/**
 * ExportContactWriterSpiService.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ExportContactWriterSpiService.java Apr 6, 2011 6:14:29 PM Tan
 *          Truong $
 * 
 * @since 1.0
 */
public class ExportEmailWriterSpiService extends AbstractContentWriterSPI {
  private static final INetLogger LOGGER = INetLogger.getLogger(ExportEmailWriterSpiService.class);

  private static final int STT = 0;
  private static final int FULL_NAME = 1;
  private static final int EMAIL = 2;
  private static final int LAST_NAME = 3;
  private static final int MIDDLE_NAME = 4;
  private static final int FIRST_NAME = 5;
  private static final int QUOTA = 6;
  private static final int TITLE = 7;
  private static final int TELEPHONE = 8;
  private static final int MOBILE = 9;

  // The domain identify parameter
  private static final String KEY = "id";
  private int index;

  /**
   * Create <code>ExportEmailWriterSpiService</code> instance
   */
  public ExportEmailWriterSpiService() {}

  /*
   * (non-Javadoc)
   * 
   * @seecom.inet.web.spi.download.ContentWriterSPI#write(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse,
   * com.inet.web.spi.download.ContentWriterType)
   */
  @Override
  public void write(HttpServletRequest request, HttpServletResponse response, ContentWriterType type)
      throws WebOSException {
    // get domain name
    String key = request.getParameter(KEY);

    if (!StringService.hasLength(key)) {
      throw new WebOSException("KEY could not be null");
    }

    AccountExport export = AccountExportCacheService.get(key);
    if(export == null){
      throw new WebOSException("Export data could not be null");
    }
    
    this.index = 0;
    try {
      byte[] data = null;
      data = exportDomain(export);

      // write to response.
      prepareResponse(response);

      // write to response.
      writerResponse(response, data.length, "application/vnd.ms-excel", "email_" + export.getDomain() + ".xls",
          true);

      ServletOutputStream outputStream = response.getOutputStream();
      // write data to response.
      outputStream.write(data);
      outputStream.flush();

    } catch (IOException ex) {
      LOGGER.error("Error while export address book", ex);
    } catch (LotusException ex) {
      LOGGER.error("Error while export address book", ex);
    }
  }

  /**
   * Export email of domain
   * 
   * @param contacts
   * @return
   * @throws WebOSException
   */
  private byte[] exportDomain(AccountExport accountExport) throws WebOSException {
    try {
      
      Workbook workbook = new HSSFWorkbook();
      // style
      CellStyle style = workbook.createCellStyle();
      style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
      
      // sheet config
      Sheet sheet = workbook.createSheet("Email list");
      PrintSetup printSetup = sheet.getPrintSetup();
      printSetup.setLandscape(true);
      sheet.setFitToPage(true);
      sheet.setHorizontallyCenter(true);

      writeHeaderEmail(sheet);
      
      Map<LdapGroup, List<AccountExportInfo>> map = accountExport.getMap();
      for(LdapGroup group: map.keySet()){
        writeGroup(sheet, group, accountExport.getDomain(), map.get(group), style);
      }
      
      
      ByteArrayOutputStream output = new ByteArrayOutputStream();
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
   * @param ws
   * @throws WriteException
   */
  private void writeHeaderEmail(Sheet sheet) {
    // header row
    Row headerRow = sheet.createRow(index++);
    headerRow.setHeightInPoints(30);
    
    sheet.setColumnWidth(STT, 5*256);
    headerRow.createCell(STT).setCellValue("STT");
    
    sheet.setColumnWidth(FULL_NAME, 25*256);
    headerRow.createCell(FULL_NAME).setCellValue("FULL NAME");
    
    sheet.setColumnWidth(EMAIL, 15*256);
    headerRow.createCell(EMAIL).setCellValue("EMAIL");
    
    sheet.setColumnWidth(LAST_NAME, 15*256);
    headerRow.createCell(LAST_NAME).setCellValue("LAST NAME");
    
    sheet.setColumnWidth(MIDDLE_NAME, 15*256);
    headerRow.createCell(MIDDLE_NAME).setCellValue("MIDDLE_NAME");
    
    sheet.setColumnWidth(FIRST_NAME, 10*256);
    headerRow.createCell(FIRST_NAME).setCellValue("FIRST_NAME");
    
    sheet.setColumnWidth(QUOTA, 6*256);
    headerRow.createCell(QUOTA).setCellValue("QUOTA");
    
    sheet.setColumnWidth(TITLE, 10*256);
    headerRow.createCell(TITLE).setCellValue("TITLE");
    
    sheet.setColumnWidth(TELEPHONE, 10*256);
    headerRow.createCell(TELEPHONE).setCellValue("TELEPHONE");
    
    sheet.setColumnWidth(MOBILE, 12*256);
    headerRow.createCell(MOBILE).setCellValue("MOBILE");
  }

  /**
   * write group header
   * @param sheet
   * @param group
   */
  private void writeGroupHeader(Sheet sheet, LdapGroup group, CellStyle style) {
    Row headerRow = sheet.createRow(index);
    headerRow.setHeightInPoints(12);
    Cell cell =  headerRow.createCell(STT);
    cell.setCellValue(group.getName() + " - " + group.getDescription());
    cell.setCellStyle(style);
        
    sheet.addMergedRegion(new CellRangeAddress(index, index++, STT , MOBILE));
  }

  /**
   * write group
   * @param sheet
   * @param group
   * @param domain
   */
  private void writeGroup(Sheet sheet, LdapGroup group, String domain, List<AccountExportInfo> data, CellStyle style){
    writeGroupHeader(sheet, group, style);
    writeRecords(sheet, data, domain);
  }
  /**
   * write records
   * @param sheet
   * @param contacts
   */
  private void writeRecords(Sheet sheet, List<AccountExportInfo> contacts, String domain) {
    for (int i = 0; i < contacts.size(); i++) {
      writeRecord(sheet, contacts.get(i), domain, i+1);
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
  private void writeRecord(Sheet sheet, AccountExportInfo contact, String domain, int seq) {
    if(contact == null){
      return;
    }
    
    Row headerRow = sheet.createRow(index++);
    headerRow.setHeightInPoints(12);
    headerRow.createCell(STT).setCellValue(seq);
    headerRow.createCell(FULL_NAME).setCellValue(contact.getFullName());
    headerRow.createCell(EMAIL).setCellValue(contact.getAccount());
    headerRow.createCell(QUOTA).setCellValue(contact.getQuota());
    
    headerRow.createCell(LAST_NAME).setCellValue(contact.getLastName());
    headerRow.createCell(MIDDLE_NAME).setCellValue(contact.getMiddleName());
    headerRow.createCell(FIRST_NAME).setCellValue(contact.getFirstName());

    headerRow.createCell(TITLE).setCellValue(contact.getPosition());
    headerRow.createCell(TELEPHONE).setCellValue(contact.getTelephone());
    headerRow.createCell(MOBILE).setCellValue(contact.getMobile());
  }
}
