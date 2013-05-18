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
package com.inet.mail.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailHeaderSL;
import com.inet.mail.core.RuleFilterManager;
import com.inet.mail.exception.MailException;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailHeader;

/**
 * MailFilterEngine
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date May 8, 2008
 * <pre>
 *  Initialization MailFilterEngine class.
 * </pre>
 */
public class MailFilterEngine implements FilterExecutor {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
	
	//~ Instance fields =======================================================
	/**
	 * The mail business bean.
	 */
	private final MailHeaderSL mailBusiness ;
	
	/**
	 * the list of executors 
	 */
	private List<FilterExecutor> executors ; 
	//~ Constructors ==========================================================
	/**
	 * Create MailFilterEngine instance.
	 * 
	 * @param mailBusiness  the given {@link MailHeaderSL} instance.
	 */
	public MailFilterEngine(MailHeaderSL mailBusiness, String owner){
		this.mailBusiness = mailBusiness ;
		
		// compute executor.
		computeExecutor(owner) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.filter.FilterExecutor#execute(com.inet.mail.persistence.MailHeader)
	 */
	public MailHeader execute(MailHeader header) throws MailException {
		// visit all filter and do filter.
		if(executors == null || executors.size() == 0) return header;
		
		// do filter.
		MailHeader mailHeader = null ;
		for(FilterExecutor executor : executors){
			try{
				mailHeader = executor.execute(header) ;
				
				if(mailHeader != null) return mailHeader ;
			}catch(MailException mex){
				logger.warn("could not perform the filter on the mail header, message: [" + mex.getMessage() + "]") ;
			}catch(Exception ex){
				logger.warn("could not perform the filter on the mail header, message: [" + ex.getMessage() + "]") ;
			}
		}	
		
		// return the given header.
		return header ;
	}
	
	//~ Helper methods ========================================================
	/**
	 * Retrieves all filters from the given owner.
	 * 
	 * @param owner the given owner.
	 */
	private List<MailFilter> findFilters(String owner){
		try {
			// get rule filter manager.
			List<MailFilter> filters = RuleFilterManager.get(owner) ;
			
			// find filter and put to cache.
			if(filters == null || filters.size() == 0){
				filters = mailBusiness.findFilterByUser(owner) ;
				RuleFilterManager.put(owner, filters) ;
			}
			
			return filters ;
		} catch (EJBException eex) {
			logger.warn("could not find any filter, message: [" + eex.getMessage() + "]") ;
		}
		
		// return null error.
		return new ArrayList<MailFilter>() ;
	}
	
	/**
	 * Compute executor from the given owner.
	 * 
	 * @param owner the given owner.
	 */
	private void computeExecutor(String owner){
		// compute the filters.
		List<MailFilter> filters = findFilters(owner) ;
		
		if(filters == null || filters.size() == 0) return ;
		
		// create list of executor.
		executors = new ArrayList<FilterExecutor>(filters.size()) ;
	
		// find the filter.
		for(MailFilter filter : filters){
			// create new executor.
			FilterExecutor executor = new FilterExecutorSupport(mailBusiness, filter) ;
			
			// add to executor.
			executors.add(executor) ;
		}
	}
}
