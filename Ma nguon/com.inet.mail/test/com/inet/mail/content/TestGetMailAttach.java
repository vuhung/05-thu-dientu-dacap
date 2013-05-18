/*****************************************************************
   Copyright 2008 by Truong Ngoc Tan (tntan@truthinet.com.vn)

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
package com.inet.mail.content;

import java.util.HashMap;

import com.inet.mail.AbstractBridgeTestMail;

public class TestGetMailAttach extends AbstractBridgeTestMail{

	public void testMailAttach() throws Exception{
		try{
//			this.bean.viewAttachment(1172,"1.j-wwj-pdf.pdf");
			HashMap<String, byte[]> attachs = this.bean.viewAttachment(1172);
			System.out.println("Attach size : " + attachs.size());
			for(String item : attachs.keySet()){
				System.out.println(item);
				System.out.println(attachs.get(item));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
