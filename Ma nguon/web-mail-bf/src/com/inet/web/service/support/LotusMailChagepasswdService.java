/*****************************************************************
   Copyright 2008 by Tung Luong (lqtung@truthinet.com.vn)

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
package com.inet.web.service.support;

import com.inet.base.service.Assert;
import com.inet.lotus.core.LotusException;
import com.inet.lotus.mail.manage.MailAccountManager;
import com.inet.web.exception.WebOSException;
import com.inet.web.security.ChangePasswdService;
import com.inet.web.service.AbstractService;

/**
 * LotusChagepwService.
 * 
 * @author <a href="mailto:lqtung@truthinet.com.vn">Tung Luong</a>
 * @version 3.2i
 */
public class LotusMailChagepasswdService extends AbstractService implements ChangePasswdService{
//~ Instance fields =======================================================
        /* mail account manager. */
        protected MailAccountManager<?> accountManager ;
        
        ///~ Constructors ==========================================================
        /**
         * Create a new LotusChagepwService instance from the given {@link MailAccountManager} instance.
         * 
         * @param accountManager the given {@link MailAccountManager} instance.
         */
        public LotusMailChagepasswdService(MailAccountManager<?> accountManager) {
                Assert.isNotNull(accountManager, "AccountManager must be not null.") ;
                this.accountManager = accountManager ;
        }
        
        /**
         * @see com.inet.web.security.ChangePasswdService#verifyPassword(java.lang.String)
         */
        public boolean verifyPassword(String password) throws WebOSException {
                return true;
        }
        
        /**
         * @see com.inet.web.security.ChangePasswdService#changePassword(java.lang.String, java.lang.String, java.lang.String)
         */
        public void changePassword(String oldPassword, String newPassword)
                        throws WebOSException {
                try{
                        String code = this.getCode();
                        this.accountManager.resetPassword(code, newPassword);
                } catch(LotusException ex){
                        throw new WebOSException("Change password fail "+ ex.getMessage(), ex);
                }
        }
      
        /**
         * @see com.inet.web.security.ChangePasswdService#resetPassword(java.lang.String, java.lang.String)
         */
        public void resetPassword(String userName, String newPassword) throws WebOSException {
                throw new WebOSException("This function does not support");
        }
}
