/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

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
package com.inet.web.service.mail.utils;

import java.util.Date;

import com.inet.base.service.StringService;
import com.inet.web.cache.exception.CacheException;
import com.inet.web.service.data.AccountExport;

/**
 * AccountExportCacheService.
 *
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: AccountExportCacheService.java Aug 19, 2011 9:24:31 AM Tan Truong $
 *
 * @since 1.0
 */
public abstract class AccountExportCacheService {
  /**
   * Account Import cache key.
   */
  private static final String CACHE_KEY = AccountExportCacheService.class.getCanonicalName();

  /**
   * @return the account export data in cache.
   * @throws CacheException
   *           when error occurs during getting data from cache.
   */
  public static AccountExport get(String key) throws CacheException {
    return LongLiveRegionService.get(key, AccountExport.class);
  }

  /**
   * Put the user to cache.
   * 
   * @param account
   *          AccountExport - the given account data.
   * @throws CacheException
   *           when error occurs during putting data to cache.
   */
  public static String put(AccountExport account, String username) throws CacheException {
    String key = getKey(username);
    LongLiveRegionService.put(key, account);
    return key;
  }
  
  /**
   * Remove data from the cache.
   * 
   * @throws CacheException
   *           when error occurs during removing data from cache.
   */
  public static void remove(String key) throws CacheException {
    LongLiveRegionService.remove(key);
  }
  
  /**
   * Remove data from the cache.
   * 
   * @throws CacheException
   *           when error occurs during removing data from cache.
   */
  public static void update(String key, AccountExport accountExport) throws CacheException {
    LongLiveRegionService.update(key, accountExport);
  }

  /**
   * @return the cache key from the given user name.
   */
  protected static String getKey(String username) {
    if (!StringService.hasLength(username))
      return CACHE_KEY + "#" + new Date().getTime() ;
    return CACHE_KEY + '#' + username + "@" + new Date().getTime();
  }
}
