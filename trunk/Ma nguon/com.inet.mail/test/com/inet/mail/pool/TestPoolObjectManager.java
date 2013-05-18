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
package com.inet.mail.pool;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.inet.mail.PoolObject;
import com.inet.mail.PoolObjectManager;
import com.inet.mail.supports.PoolObjectManagerSupport;

/**
 * TestPoolObjectManager
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestPoolObjectManager.java Jan 13, 2009 nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization TestPoolObjectManager class.
 * </pre>
 */
public class TestPoolObjectManager {
	//~ Static fields =========================================================
	/* pool object manager. */
	private static final String NAME = "PoolObjectManager#String" ;
	
	/**
	 * {@link SimpleObjectManager} instance.
	 */
	private final SimpleObjectManager SIMPLE_OBJECT_MANAGER = SimpleObjectManager.getInstance() ;
	//~ Methods ===============================================================
	/**
	 * Test pool object.
	 */
	@Test(threadPoolSize=100, invocationCount=100)
	public void testPoolObject(){
		PoolObjectManager<String> poolManager = new PoolObjectManagerSupport<String>() ;
		PoolObjectManager<String> manager = SIMPLE_OBJECT_MANAGER.registerPoolManager(NAME, poolManager) ;
		if(manager != null){
			poolManager = manager ;
		}
		
		// put three simple object manager.
		poolManager.put(new SimplePoolObject("hello", "Hello World!")) ;
		poolManager.put(new SimplePoolObject("bye", "Bye bye")) ;
		
		poolManager.start() ;
		
		// get hello pool object.
		PoolObject<String> object = poolManager.get("hello") ;
		System.out.println("Object Value: [" + object.getObject() + "]");
		Assert.assertEquals("Hello World!", object.getObject()) ;
		
		try {
			Thread.sleep(11000L) ;
		} catch (InterruptedException ex) {}
		
		// get goodbye object.
		PoolObject<String> goodbye = poolManager.get("bye") ;
		Assert.assertNull(goodbye) ;
		
		object = poolManager.get("hello") ;
		Assert.assertEquals("Hello World!", object.getObject()) ;
		
		// reset the object.
		object.reset();
				
		try {
			Thread.sleep(20000L) ;
		} catch (InterruptedException ex) {}
		
		object = poolManager.get("hello") ;
		Assert.assertNull(object) ;
		poolManager.stop() ;
	}
}
