/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@inetcloud.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.inetcloud.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 *****************************************************************/
package com.inet.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.inet.web.service.data.AccountImportInfo;
import com.inet.web.service.mail.utils.ImportUtil;
import com.inet.web.service.mail.utils.MailCommonUtils;

/**
 * ReadFile.
 * 
 * @author <a href="mailto:tntan@inetcloud.vn">Tan Truong</a>
 * @version $Id: ReadFile.java Nov 21, 2012 4:31:39 PM nguyen_dv $
 * 
 * @since 1.0
 */
public class ReadFile {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    File file = new File("/home/tntan/Desktop/customer/daknong/imail/2012/Tong hop/Chi cuc phat trien NT.xls");

    InputStream ios = null;
    try {
      ios = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    try {
      List<AccountImportInfo> infos = ImportUtil.readEmail(ios);
      for(AccountImportInfo info : infos){
        System.out.print(info.getAccount() + "--");
        System.out.print(info.getPassword() + "-----");
        System.out.println(MailCommonUtils.getName(info.getPassword()));
        
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }
}
