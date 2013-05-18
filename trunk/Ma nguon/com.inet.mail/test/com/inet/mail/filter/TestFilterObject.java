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
package com.inet.mail.filter;

import com.inet.mail.data.FilterObject;
import com.inet.mail.filter.object.AbstractFilterObject;
import com.inet.mail.filter.object.ContentFilterObject;
import com.inet.mail.filter.object.SubjectFilterObject;

import junit.framework.TestCase;

/**
 * TestFilterObject
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 7, 2008
 * <pre>
 *  Initialization TestFilterObject class.
 * </pre>
 */
public class TestFilterObject extends TestCase {
	/**
	 * Test create filter object.
	 */
	public void testFilterObject(){
		// create content filter object.
		AbstractFilterObject filterObject = AbstractFilterObject.getFilterObject(FilterObject.CONTENT) ;
		
		// check the class name must be ContentFilterObject.
		assertEquals(ContentFilterObject.class.getSimpleName(), filterObject.getClass().getSimpleName()) ;
		
		// create subject filter object.
		filterObject = AbstractFilterObject.getFilterObject(FilterObject.SUBJECT) ;
		
		// check the class name must be SubjectFilterObject.
		assertEquals(SubjectFilterObject.class.getSimpleName(), filterObject.getClass().getSimpleName()) ;
	}
}
