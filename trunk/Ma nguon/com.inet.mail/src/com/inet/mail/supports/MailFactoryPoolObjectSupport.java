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
package com.inet.mail.supports;

import com.inet.base.service.CompareService;
import com.inet.mail.AbstractPoolObject;
import com.inet.mail.IMailFactory;
import com.inet.mail.exception.MailException;

/**
 * MailFactoryPoolObjectSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailFactoryPoolObjectSupport.java 2009-01-13 17:15:59z nguyen_dv $
 * 
 * Create date: Jan 13, 2009
 * <pre>
 *  Initialization MailFactoryPoolObjectSupport class.
 * </pre>
 */
public class MailFactoryPoolObjectSupport extends AbstractPoolObject<IMailFactory> {
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailFactoryPoolObjectSupport</tt> instance from
	 * the given {@link IMailFactory} instance.
	 * 
	 * @param name the given object name.
	 * @param mailFactory the given {@link IMailFactory} instance.
	 */
	public MailFactoryPoolObjectSupport(String name, IMailFactory mailFactory) {
		super(name, mailFactory) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.PoolObject#dispose()
	 */
	public void dispose() {
		// get the mail object.
		IMailFactory factory = getObject() ;
		
		// disposed the object.
		if(factory != null){
			try{
				factory.close() ;
				factory = null ;
			}catch(MailException mex){
				logger.warn("Could not close mail factory, message: [" + mex.getMessage() + "]") ;
			}catch(Exception ex){
				logger.warn("Could not close mail factory, message: [" + ex.getMessage() + "]") ;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof MailFactoryPoolObjectSupport)) return false ;
		MailFactoryPoolObjectSupport another = (MailFactoryPoolObjectSupport)obj ;
		
		if(!CompareService.equals(name, another.name)) return false ;
		if(used != another.used) return false ;
		return CompareService.equals(object, another.object) ;
	}
}
