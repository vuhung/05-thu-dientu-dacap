/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import com.inet.base.service.FileService;
import com.inet.mail.util.MailService;

/**
 * ParsingBodyTest.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class ParsingBodyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ByteArrayOutputStream input = new ByteArrayOutputStream();
			FileService
					.copyFile(
							new File(
									"C:\\Documents and Settings\\hiennguyenvan\\Desktop\\123.dat"),
							input);
			Message message = new MimeMessage(null, new ByteArrayInputStream(
					input.toByteArray()));
			System.out.println(MailService.getBodyHTML(message));
			System.out.println(">>>>>");
			Hashtable<String, String> ref = MailService.getBody(message);
			
			for (String key : ref.keySet())
			{
				System.out.println(">>>>=============");
				System.out.println(key);
				System.out.println(ref.get(key));
				System.out.println("=============>>>>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
