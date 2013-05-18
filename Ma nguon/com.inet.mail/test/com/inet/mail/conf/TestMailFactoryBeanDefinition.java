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
package com.inet.mail.conf;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.mail.parser.AbstractMessageFactory;

/**
 * TestMailFactoryBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestMailFactoryBeanDefinition.java 2008-12-11 15:46:59Z nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization TestMailFactoryBeanDefinition class.
 * </pre>
 */
public class TestMailFactoryBeanDefinition {
	@Test(threadPoolSize=5, invocationCount=200)
	public void testCreateMessageFactory(){
		// create message instance.
		AbstractMessageFactory messageFactory = AbstractMessageFactory.getInstance() ;
		
		Assert.assertNotNull(messageFactory, "The message factory was set.") ;
	}
}
