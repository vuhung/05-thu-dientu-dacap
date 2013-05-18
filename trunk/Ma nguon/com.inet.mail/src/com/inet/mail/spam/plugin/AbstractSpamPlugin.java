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
package com.inet.mail.spam.plugin;

import org.apache.log4j.Logger;

import com.inet.mail.spam.SpamPlugin;
import com.inet.mail.spam.SpamPluginElement;

/**
 * AbstractSpamPlugin
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractSpamPlugin.java 2009-01-09 16:19:40z nguyen_dv $
 * 
 * Create date: Jan 9, 2009
 * <pre>
 *  Initialization AbstractSpamPlugin class.
 * </pre>
 */
public abstract class AbstractSpamPlugin implements SpamPlugin {
	//~ Static fields =========================================================
	/* max probability value. */
	protected static final double MAX_PROBABILITY_VALUE = 1.0d ;
	
	/* min probability value. */
	protected static final double MIN_PROBABILITY_VALUE = 0.0d ;
	
	/* average probability value. */
	protected static final double AVE_PROBABILITY_VALUE = 0.5d ;
	//~ Instance fields =======================================================
	/* absolute engine. */
	private boolean absolute ;
	
	/* plugin element. */
	private final SpamPluginElement pluginElement ;
	
	/* class logger. */
	protected final Logger logger = Logger.getLogger(getClass());
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>AbstractSpamPlugin</tt> instance from the given
	 * {@link SpamPluginElement} instance.
	 * 
	 * @param pluginElement the given {@link SpamPluginElement} instance.
	 */
	protected AbstractSpamPlugin(SpamPluginElement pluginElement){
		this(pluginElement, false) ;
	}
	
	/**
	 * Create a new <tt>AbstractSpamPlugin</tt> instance from the given
	 * {@link SpamPluginElement} instance and absolute flag.
	 * 
	 * @param pluginElement the given {@link SpamPluginElement} instance.
	 * @param absolute the absolute SPAM plugin.
	 */
	protected AbstractSpamPlugin(SpamPluginElement pluginElement, boolean absolute){
		this.absolute = absolute ;
		this.pluginElement = pluginElement ;
	}	
	//~ Methods ===============================================================
	/**
	 * @return the absolute SPAM plugin.
	 */
	protected boolean isAbsolute() {
		return absolute;
	}
	
	/**
	 * @return the {@link SpamPluginElement} instance.
	 */
	protected SpamPluginElement getPluginElement() {
		return pluginElement;
	}
}
