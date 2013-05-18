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
package com.inet.mail.spam.plugin.support.sun;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.inet.base.service.Assert;
import com.inet.base.service.StringService;
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.plugin.support.AbstractHeaderSpamPlugin;
import com.inet.mail.spam.plugin.support.SpamPluginException;

/**
 * SunXSpamPluginSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SunXSpamPluginSupport.java 2009-01-11 23:22:08z nguyen_dv $
 * 
 * Create date: Jan 11, 2009
 * <pre>
 *  Initialization SunXSpamPluginSupport class.
 * </pre>
 */
public class SunXSpamPluginSupport extends AbstractHeaderSpamPlugin {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SunXSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 */
	public SunXSpamPluginSupport(SpamPluginElement element) {
		super(element);
	}
	
	/**
	 * Create a new <tt>SunXSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance and absolute SPAM engine flag.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 * @param absolute the given absolute SPAM plugin flag.
	 */
	public SunXSpamPluginSupport(SpamPluginElement element, boolean absolute){
		super(element, absolute) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.plugin.support.AbstractHeaderSpamPlugin#getHeader(java.lang.Object, java.lang.String)
	 */
	protected String[] getHeader(Object message, String header) throws SpamPluginException {
		Assert.isNotNull(message, "The given message must not be null.") ;
		if(!(message instanceof Message)){
			throw new SpamPluginException("the given message could not instance of javax.mail.Message class") ;
		}
		
		try{
			// get values.
			String[] values = ((Message)message).getHeader(header) ;		
			return ((values == null || values.length == 0) ? new String[]{StringService.EMPTY_STRING} : values) ;
		}catch(MessagingException mex){
			return new String[]{ StringService.EMPTY_STRING } ;
		}
	}
}
