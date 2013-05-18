/*****************************************************************
   Copyright 2007 by Luong Thi Cao Van (ltcvan@truthinet.com)

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
package com.inet.web.controller.addressbook.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inet.web.controller.AbstractWebOSAjaxContentController;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.addressbook.SearchAddressBookService;

/**
 * WebOSSearchAddressBookContent.
 * 
 * @author <a href="mailto:ltcvan@truthinet.com.vn"> Luong Thi Cao Van</a>
 * @version 0.2i
 */
public class WebOSSearchAddressBookContent extends AbstractWebOSAjaxContentController<SearchAddressBookService>{
/**
 *  Create <tt>WebOSSearchAddressBookContent <tt> instance from the given.
 * @param addressBook - the given instance
 */
public WebOSSearchAddressBookContent(SearchAddressBookService addressBook){
	super(addressBook);
}

/**
 * @see com.inet.web.core.ajax.AbstractAjaxContent#executeBusiness(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 */
@Override
protected String executeBusiness(HttpServletRequest request,
		HttpServletResponse response) throws WebOSException {
	
	return getBusinessBean().execute(request, response).toString();
}
}
