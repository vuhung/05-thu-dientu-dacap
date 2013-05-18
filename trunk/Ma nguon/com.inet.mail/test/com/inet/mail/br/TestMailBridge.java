/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
package com.inet.mail.br;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.AbstractTestCase;
import com.inet.mail.business.sr.MailBridgeRemote;

/**
 * TestMailBridge
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestMailBridge.java Dec 13, 2008 nguyen_dv $
 * 
 * Create date: Dec 13, 2008
 * <pre>
 *  Initialization TestMailBridge class.
 * </pre>
 */
public class TestMailBridge extends AbstractTestCase {
	/**
	 * Lookup mail bridge bean.
	 */
	@Test
	public void testLookup() throws Exception{
		// validate.
		MailBridgeRemote bridgeRemote = (MailBridgeRemote)context.lookup("MailBridgeSLBean/remote") ;
		
		Assert.assertNotNull(bridgeRemote, "Lookup bridge bean.") ;
		
		try{
			// test.
			bridgeRemote.fetch("mail.truthinet.com.vn:tttduyen@truthinet.com.vn") ;
		}catch(EJBException ex){
			ex.printStackTrace() ;
		}
	}
}
