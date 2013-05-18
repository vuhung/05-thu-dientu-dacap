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

import com.inet.base.ejb.exception.EJBException;
import com.inet.mail.business.sr.MailHeaderSL;
import com.inet.mail.exception.MailException;
import com.inet.mail.persistence.MailFilter;
import com.inet.mail.persistence.MailHeader;

/**
 * FilterExecutorSupport
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * Create date May 8, 2008
 * <pre>
 *  Initialization FilterExecutorSupport class.
 * </pre>
 */
public class FilterExecutorSupport implements FilterExecutor{
	//~ Instance fields =======================================================
	/**
	 * The mail business bean.
	 */
	private final MailHeaderSL mailBusiness ;

	/**
	 * The mail filter.
	 */
	private final MailFilter mailFilter ;
	
	/**
	 * The given clause filter to be evaluate the data.
	 */
	private ClauseFilter clauseFilter ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>FilterExecutorSupport</tt> from the given mail business and mail filter instance.
	 * 
	 * @param mailBusiness the given {@link MailHeaderSL} bean.
	 * @param filter the given {@link MailFilter} instance.
	 */
	public FilterExecutorSupport(MailHeaderSL mailBusiness, MailFilter filter){
		this.mailBusiness = mailBusiness ;
		this.mailFilter = filter ;
		
		// initialization clause filter.
		initialization(filter) ;
	}
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.filter.FilterExecutor#execute(com.inet.mail.persistence.MailHeader)
	 */
	public MailHeader execute(MailHeader header) throws MailException {
		try{
			if(clauseFilter.evaluate(header)){
				// move the mail.
				return mailBusiness.move(
						header.getId(), 
						mailFilter.getFolder().getId()
					) ;
			}
		}catch(EJBException eex){
			throw new MailException("Could not evaluate the header, message: [" + eex.getMessage() + "]", eex) ;
		}
		
		// does not match the filter.
		return null ;
	}
	
	//----------------------------------------------------------------------
	// Helper functions.
	//
	/**
	 * Initialization the filter executor from the given filter.
	 * 
	 * @param filter MailFilter - the given mail filter to be parsed.
	 */
	private void initialization(MailFilter filter){
		clauseFilter = new ClauseFilter(filter.getClause()) ;
	}
}
