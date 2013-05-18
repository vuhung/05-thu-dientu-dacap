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
package com.inet.mail.concurrent;

import java.util.List;
import java.util.Vector;

import javolution.context.ConcurrentContext;
import javolution.context.PoolContext;
import javolution.context.ConcurrentContext.Logic;

import com.inet.base.concurrent.ProcessBiz;
import com.inet.base.exception.ConcurrentException;
import com.inet.base.service.ErrorService;

/**
 * MailProcessContext
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Apr 22, 2008
 * <pre>
 *  Initialization MailProcessContext class.
 * </pre>
 */
public class MailProcessContext {
	/**
	 * Execute the list of process and return the list of result.
	 * @param <T> - the given result of business process.
	 * 
	 * @param process List<ProcessBiz<T>> - the given list of process business.
	 * @return the list of result.
	 * @throws ConcurrentException when error occurs during executing process.
	 */
	public static <T> List<T> execute(List<ProcessBiz<T>> process)
			throws ConcurrentException {
		if (process != null && process.size() > 0) {
			// prepare the environment.
			PoolContext.enter(); 
			try {
				// create list of result.
				final List<T> result = new Vector<T>(process.size());
				
				// enter the concurrent environment to be executed.
				ConcurrentContext.enter();
				try {
					for (final ProcessBiz<T> proc : process) {
						// execute the business process.
						ConcurrentContext.execute(new Logic() {
							/**
							 * @see javolution.context.ConcurrentContext.Logic#run()
							 */
							public void run() {
								// execute the process and put the result.
								try {
									result.add(proc.execute());
								} catch (ConcurrentException cex) {
									// log eror.
									ErrorService.logError("*****ERROR: " + cex.getMessage(), MailProcessContext.class, cex) ;
								}catch(Exception ex){
									// log eror.
									ErrorService.logError("*****ERROR: " + ex.getMessage(), MailProcessContext.class, ex) ;
								}
							}
						});
					}
				} finally {
					// destroy the context when the completed processing.
					ConcurrentContext.exit();
				}
				
				// return the result.
				return result;
			} finally {
				// destroy the environment.
				PoolContext.exit();
			}			
		}
		
		// none of process is executed.
		throw new ConcurrentException("None of proces is executed.");
	}
}
