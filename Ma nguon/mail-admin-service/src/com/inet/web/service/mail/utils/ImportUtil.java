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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.inet.base.service.StringService;
import com.inet.web.service.data.AccountImportInfo;

/**
 * ImportUtil.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ImportUtil.java Apr 7, 2011 11:32:11 AM Tan Truong $
 *
 * @since 1.0
 */
public class ImportUtil {
  // sequence
  private static final int SEQUENCE     = 0;
  // full name
  private static final int FULLNAME     = 1;
  // account abc@xyz.com
  private static final int EMAIL        = 2;
  // last name
  private static final int LASTNAME     = 3;
  // middle name
  private static final int MIDDLENAME   = 4;
  // first name
  private static final int FIRSTNAME    = 5;
  // password
  private static final int PASSWORD     = 6;
  //quota
  private static final int QUOTA        = 7;
  // position
  private static final int POSITION     = 8;
  //telephone
  private static final int TELEPHONE    = 9;
  //mobile
  private static final int MOBILE       = 10;
 
  /**
   * Read content from given file
   * @param fileInputStream
   * @return list of account 
   * @throws BiffException
   * @throws IOException
   */
  public static List<AccountImportInfo> readEmail(InputStream fileInputStream) throws Exception{
    try{
      POIFSFileSystem document = new POIFSFileSystem(fileInputStream);
      HSSFWorkbook workbook = new HSSFWorkbook(document);
      
      // Getting Default Sheet  0
      HSSFSheet sheet = workbook.getSheetAt(0);
      HSSFRow rowData = null;
      String pwd = StringService.EMPTY_STRING;
      List<AccountImportInfo> infos = new ArrayList<AccountImportInfo>();
      for (int i = 1; ; i++) {
        // Get Individual Row
        rowData = sheet.getRow(i);
        if (rowData == null) break;
        
        // read data
        AccountImportInfo info = new AccountImportInfo();
        info.setNumber(getLong(rowData, SEQUENCE));
        info.setFullName(standardized(getString(rowData, FULLNAME)));
        info.setAccount(getString(rowData, EMAIL));
        info.setLastName(standardized(getString(rowData, LASTNAME)));
        info.setMiddleName(standardized(getString(rowData, MIDDLENAME)));
        info.setFirstName(standardized(getString(rowData, FIRSTNAME)));
        
        pwd = getString(rowData, PASSWORD);
        if(!StringService.hasLength(pwd)){
          pwd = info.getAccount();
          info.setPassword(MailCommonUtils.getName(pwd));
        }else{
          info.setPassword(pwd);
        }
        
       
        info.setPosition(getString(rowData, POSITION));
        info.setTelephone(getString(rowData, TELEPHONE));
        info.setMobile(getString(rowData, MOBILE));
        info.setQuota(getLong(rowData, QUOTA));
        infos.add(info);
        info.toString();
      }
      return infos;
    }catch(Exception ex){
      ex.printStackTrace();
      throw ex;
    }
  }
  
  /**
   * get String
   * @param rowData
   * @param index
   * @return
   */
  private static String getString(HSSFRow rowData, int index){
    HSSFCell cell = rowData.getCell(index);
    if(cell == null){
      return StringService.EMPTY_STRING;
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_STRING){
      return cell.getStringCellValue();
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_NUMERIC){
      return String.valueOf((new Double(cell.getNumericCellValue())).longValue());
    }else if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
      return StringService.EMPTY_STRING;
    }else{
      return StringService.EMPTY_STRING;
    }
  }
  
  /**
   * get double
   * @param rowData
   * @param index
   * @param def
   * @return
   */
  @SuppressWarnings("unused")
  private static double getDouble(HSSFRow rowData, int index, int def){
    HSSFCell cell = rowData.getCell(index);
    
    if(cell == null){
      return def;
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_NUMERIC){
      return cell.getNumericCellValue();
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_STRING){
      return Double.valueOf(cell.getNumericCellValue());
    }else{
      return 0;
    }
  }
  
  /**
   * get Long
   * @param rowData
   * @param index
   * @return
   */
  private static long getLong(HSSFRow rowData, int index){
    HSSFCell cell = rowData.getCell(index);
    
    if(cell == null){
      return 0L;
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_NUMERIC){
      return (new Double(cell.getNumericCellValue())).longValue();
    }else if(cell.getCellType() ==  HSSFCell.CELL_TYPE_STRING){
      return (new Double(cell.getStringCellValue())).longValue();
    }else{
      return 0L;
    }
  }
  
  /**
   * standardized string
   * @param s
   * @return
   */
  public static String standardized(String s){
    if(!StringService.hasLength(s)){
      return s;
    }
    
    StringTokenizer stok = new StringTokenizer(s);

    StringBuilder result = new StringBuilder();
    
    while (stok.hasMoreTokens()){
      if(result.length() == 0){
        result.append(standardizedWord(stok.nextToken()));
      }else{
        result.append(" " + standardizedWord(stok.nextToken()));
      }
    }
    
    return result.toString();
  }
  
  /**
   * standardized word
   * @param s
   * @return
   */
  public static String standardizedWord(String s){
    if(!StringService.hasLength(s)){
      return s;
    }
    
    Character c = s.charAt(0);
    Character change = Character.toUpperCase(c);

    StringBuffer ss = new StringBuffer(s);
    ss.insert(0, change);
    ss.deleteCharAt(1);
    return ss.toString();
  }
}
