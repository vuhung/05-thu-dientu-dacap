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
package com.inet.mail.core;

import com.inet.base.ejb.exception.EJBException;

/**
 * ParserException.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class ParserException extends EJBException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6574313553614797809L;

	public ParserException(String msg)
	{
		super(msg);
	}
	
	public ParserException(Throwable e)
	{		
		super(e);
	}
	
	public ParserException(String msg, Throwable e)
	{		
		super(msg,e);
	}	
}
