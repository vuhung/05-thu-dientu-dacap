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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.inet.base.service.StringService;

/**
 * ImportCSV.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: ImportCSV.java Apr 13, 2011 9:44:41 AM Tan Truong $
 *
 * @since 1.0
 */
public class ImportCSV {
  private static final int SEQUENCE     = 0;
  private static final int FULLNAME     = 1;
  private static final int EMAIL        = 2;
  private static final int LASTNAME     = 3;
  private static final int MIDDLENAME   = 5;
  private static final int FIRSTNAME    = 4;
  
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
  
  public static void contentReading(FileInputStream fs) throws IOException{
    BufferedReader bufRdr = new BufferedReader(new FileReader("/home/tntan/Desktop/gialai.csv"));
    String line = null;
    int row = 0;
    int col = 0;
    String temp = StringService.EMPTY_STRING;
    //read each line of text file
    while((line = bufRdr.readLine()) != null){
      StringTokenizer st = new StringTokenizer(line,",");
      System.out.println("Count>>" + st.countTokens());
      System.out.println("+++++++++++++++");
      col = 0;
      while (st.hasMoreTokens()){
        //get next token and store it in the array
        temp = st.nextToken();
        System.out.println(temp.replaceAll("\"", StringService.EMPTY_STRING));
        col++;
      }
      row++;
    }
     
    //close the file
    bufRdr.close();
  }
  
  public static void main(String[] args) {
    try {
      contentReading(null);
//      ImportXls xlReader = new ImportXls();
//      xlReader.init("/home/tntan/Desktop/gialai.csv");
//      File file = new File("/home/tntan/Desktop/gialai.csv");
//      FileInputStream fs = new FileInputStream(file);
//      ByteArrayInputStream inputStream = new ByteArrayInputStream();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
