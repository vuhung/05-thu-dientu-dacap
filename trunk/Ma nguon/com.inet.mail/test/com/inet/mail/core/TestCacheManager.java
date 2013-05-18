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
package com.inet.mail.core;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.inet.web.util.WebOSCacheHelper;

/**
 * TestCacheManager
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: TestCacheManager.java 2008-12-10 14:09:53Z nguyen_dv $
 * 
 * Create date: Dec 10, 2008
 * <pre>
 *  Initialization TestCacheManager class.
 * </pre>
 */
public class TestCacheManager {
	//~ Instance fields =======================================================
	/* cache manager. */
	private CacheManager cache;
	
	/* default cache value. */
	private static final String DEFAULT_VALUE = "Hello World!" ;	
	//~ Methods ===============================================================
	/**
	 * Set up context.
	 */
	@BeforeTest
	public void setUp(){
		cache = CacheManager.getInstance() ;
	}
	
	/**
	 * Put value to cache.
	 */
	@Test(threadPoolSize=10, invocationCount=1000)
	public void testPut(){
		// create entry data.
		StringDataEntry entry = new StringDataEntry(DEFAULT_VALUE) ;
		
		String key = WebOSCacheHelper.combineKey(StringDataEntry.class.getName(), String.valueOf(Thread.currentThread().getId()));
		
		// put to cache.
		cache.setItem(key, entry) ;
		
		// get entry from cache.
		entry  = (StringDataEntry)cache.getItem(key) ;
		Assert.assertNotNull(entry, "Entry set.") ;
		Assert.assertEquals(entry.getContent(), DEFAULT_VALUE) ;
		
		// remove cache.
		cache.removeItem(key) ;
		
		// get cache.
		entry = (StringDataEntry)cache.getItem(key) ;
		Assert.assertNull(entry, "Entry was destroyed.") ;
	}
	
	//~ Internal class ========================================================
	/**
	 * StringDataEntry
	 * 
	 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
	 * @version $Id: TestCacheManager.java 2008-12-10 14:35:28Z nguyen_dv $
	 * 
	 * Create date: Dec 10, 2008
	 * <pre>
	 *  Initialization StringDataEntry class.
	 * </pre>
	 */
	protected static class StringDataEntry extends DataEntry<String>{
		//~ Static fields =====================================================
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -2756348300885989290L;

		//~ Constructors ======================================================
		/**
		 * Create a new <tt>StringDataEntry</tt> instance.
		 * 
		 * @param content the given entry content.
		 */
		public StringDataEntry(String content) {
			super(content);
		}
	}
}
