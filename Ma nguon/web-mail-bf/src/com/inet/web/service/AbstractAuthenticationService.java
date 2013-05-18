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
package com.inet.web.service;

import com.inet.base.logging.INetLogger;
import com.inet.lotus.account.Account;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.security.AuthenticationService;
import com.inet.web.service.utils.AccountCacheService;

/**
 * AbstractAuthenticationService
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractAuthenticationService.java Nov 11, 2008 nguyen_dv $
 * 
 * Create date: Nov 11, 2008
 * <pre>
 *  Initialization AbstractAuthenticationService class.
 * </pre>
 */
public abstract class AbstractAuthenticationService implements AuthenticationService {
	//~ Static fields =========================================================
	/* class logger */
	protected final INetLogger logger = INetLogger.getLogger(getClass()) ;
	
	//~ Methods ===============================================================
	/**
	 * Put login account to cache.
	 * 
	 * @param account the given login {@link Account}
	 */
	protected void putLoginAccountToCache(Account account){
		try{
			AccountCacheService.put(account) ;
		}catch(CacheException cex){
			logger.warning("Could not put account: [" + account + "] to cache, message: [" + cex.getMessage() + "]") ;
		}
	}
}
