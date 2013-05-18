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
package com.inet.mail.spam.beans;

import org.w3c.dom.Element;

import com.inet.base.service.StringService;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.SpamParser;
import com.inet.mail.spam.parser.SpamParserException;
import com.inet.mail.spam.parser.support.SpamParserSupport;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * SpamMailBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamMailBeanDefinition.java 2009-01-10 21:25:22z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamMailBeanDefinition class.
 * </pre>
 */
public class SpamMailBeanDefinition extends AbstractBeanDefinition {
	//~ Static fields =========================================================
	/* SPAM mail bean definition name. */
	public static final String SPAM_MAIL_BEAN_DEFINITION_NAME = SpamMailBeanDefinition.class.getName() ;
	
	/* context holder. */
	protected static ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>();
	//~ Instance fields =======================================================
	
	/* spam engine. */
	private ScanEngine scanEngine = null;
	
	/* SPAM configuration element. */
	private Element element = null;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.registry.BeanDefinition#getName()
	 */
	public String getName() {
		return SPAM_MAIL_BEAN_DEFINITION_NAME;
	}
	
	/**
	 * Set {@link Element} instance.
	 * 
	 * @param element the {@link Element} instance to set.
	 */
	public void setElement(Element element) {
		this.element = element;
	}
	
	/**
	 * @return the {@link ScanEngine} instance.
	 */
	public ScanEngine getScanEngine() {
		if(contextHolder.get() == null){
			synchronized (this) {
				if(scanEngine == null && element != null){					
					try{
						// create parser.
						SpamParser parserSupport = new SpamParserSupport() ;
						
						// parser element.
						scanEngine = parserSupport.parse(element) ;
					}catch(SpamParserException spex){}
				}
				
				// setting context holder.
				contextHolder.set(Boolean.TRUE) ;
			}	
		}
		
		return scanEngine;		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.registry.AbstractBeanDefinition#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer() ;
		
		buffer.append(getClass().getName()).append("[")
		      .append("Name ").append(SPAM_MAIL_BEAN_DEFINITION_NAME)
		      .append("; Element ").append((element != null) ? element.getClass().getName() : StringService.EMPTY_STRING) 
		      .append("]") ;
		
		return buffer.toString();
	}
}
