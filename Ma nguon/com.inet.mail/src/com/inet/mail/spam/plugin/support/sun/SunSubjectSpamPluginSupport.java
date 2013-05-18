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
import com.inet.mail.spam.SpamPluginElement;
import com.inet.mail.spam.plugin.support.AbstractSubjectSpamPlugin;
import com.inet.mail.spam.plugin.support.SpamPluginException;
/**
 * SunSubjectSpamPluginSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SunSubjectSpamPluginSupport.java Jan 9, 2009 nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization SunSubjectSpamPluginSupport class.
 * </pre>
 */
public class SunSubjectSpamPluginSupport extends AbstractSubjectSpamPlugin {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>SunSubjectSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 */
	public SunSubjectSpamPluginSupport(SpamPluginElement element) {
		super(element);
	}
	
	/**
	 * Create a new <tt>SunSubjectSpamPluginSupport</tt> instance from the given
	 * {@link SpamPluginElement} instance and absolute SPAM engine flag.
	 * 
	 * @param element the given {@link SpamPluginElement} instance.
	 * @param absolute the given absolute SPAM plugin flag.
	 */
	public SunSubjectSpamPluginSupport(SpamPluginElement element, boolean absolute){
		super(element, absolute) ;
	}
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.plugin.support.AbstractSubjectSpamPlugin#getSubject(java.lang.Object)
	 */
	protected String getSubject(Object message) throws SpamPluginException {
		Assert.isNotNull(message, "The given message must not be null.") ;
		if(!(message instanceof Message)){
			throw new SpamPluginException("the given message could not instance of javax.mail.Message class") ;
		}
		
		try{
			return ((Message)message).getSubject();
		}catch(MessagingException mex){
			throw new SpamPluginException("could not get subject from the given message, message: [" + mex.getMessage() + "]", mex) ;
		}
	}
}
