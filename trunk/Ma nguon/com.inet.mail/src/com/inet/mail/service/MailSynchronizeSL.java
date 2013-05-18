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
package com.inet.mail.service;

import java.util.List;

import javax.ejb.Local;

import org.jboss.annotation.ejb.Producer;

import com.inet.mail.conf.PersonMailConfig.ConfigDTO;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.parser.IMessageComposer;

/**
 * MailSynchronizeSL.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
@Local
@Producer
public interface MailSynchronizeSL {
	/**
	 * Send the message from the given SMTP configuration, {@link IMessageComposer} instance,
	 * the message identifier and folder identifier.
	 * 
	 * @param smtp the given SMTP configuration.
	 * @param composer the given {@link IMessageComposer} instance.
	 * @param messageId the given message identifier.
	 * @param folderId the given folder identifier.
	 */
	void send(ConfigDTO smtp, IMessageComposer composer, long messageId, long folderId);
	
	/**
	 * Send the mail message from the template, template parameter and {@link IMessageComposer} instance.
	 * 	  
	 * @param id the given template identifier.
	 * @param name the given template name.
	 * @param params the given list of template parameters.
	 * @param composer the given {@link IMessageComposer} instance.
	 */
	void send(Long id, String name, List<PairValueDTO<String>> params, IMessageComposer composer);
	
	/**
	 * Send the message from the given list of template parameters 
	 * and {@link IMessageComposer} instance.
	 * 
	 * @param params the given list of template parameters.
	 * @param composer the given {@link IMessageComposer} instance.
	 */
	void send(List<PairValueDTO<String>> params, IMessageComposer composer);
	
	/**
	 * Send message without template! this will get default connection the send it.
	 * 
	 * @param composer the given {@link IMessageComposer} instance.
	 */
	void send(IMessageComposer composer);
}
