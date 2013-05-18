/*****************************************************************
   Copyright 2006 by Nguyen Thi My Hien (ntmhien@truthinet.com.vn)

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

import com.inet.web.controller.AbstractWebOSTxAjaxContentController;
import com.inet.web.exception.WebOSException;
import com.inet.web.service.addressbook.AddressBookService;

/**
 * WebOSAddressBookContent.
 * 
 * @author <a href="mailto:ntmhien@truthinet.com.vn">Nguyen Thi My Hien</a>
 * @version 0.2i
 */
public class WebOSAddressBookContent extends AbstractWebOSTxAjaxContentController<AddressBookService>{

	/**
	 * 
	 * @param addressBookService
	 */
	public WebOSAddressBookContent(AddressBookService addressBookService){
		super(addressBookService);
	}
	
	/**
	 * @see com.inet.web.core.ajax.AbstractAjaxContent#executeBusiness(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String executeBusiness(HttpServletRequest request,
			HttpServletResponse response) throws WebOSException {
		return getBusinessBean().execute(request, response).toString();
	}
	
}
