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
package com.inet.mail.data;

import java.util.ArrayList;
import java.util.List;

import com.inet.base.exception.ConvertException;
import com.inet.base.service.ConvertService;
import com.inet.base.service.DigestService;
import com.inet.base.service.IOService;
import com.inet.base.type.INetField;
import com.inet.base.type.INetObject;

/**
 * MailFilterClause
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Apr 3, 2008
 * <pre>
 *  Initialization MailFilterClause class.
 * </pre>
 */
public class MailFilterClause extends INetObject {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7193275473155398051L;

	//-----------------------------------------------------------------------
	// Filter clause data.
	//
	/**
	 * the clause operator.
	 */
	private static final String CLAUSE_OPERATOR = "CLAUSE_OPERATOR" ;
	/**
	 * the filter clauses.
	 */
	private static final String FILTER_CLAUSES = "FILTER_CLAUSES" ;
	/**
	 * the filter clause object.
	 */
	private static final String FILTER_CLAUSE_OBJECT = "filter-clause" ;
	/**
	 * The filter clause type.
	 */
	private static final String FILTER_CLAUSE_TYPE = "FILTER-CLAUSE" ;
	
	
	/**
	 * Create MailFilterClause instance.
	 */
	public MailFilterClause(){
		super(MailFilterClause.FILTER_CLAUSE_OBJECT, MailFilterClause.FILTER_CLAUSE_TYPE) ;
	}
	
	/**
	 * @return the mail clause operator.
	 */
	public MailClauseOperator getOperator(){
		try{
			return MailClauseOperator.valueOf(ConvertService.toString(this.getField(MailFilterClause.CLAUSE_OPERATOR))) ;
		}catch(ConvertException cex){
			return MailClauseOperator.OR ;
		}catch(Exception ex){
			return MailClauseOperator.OR ;
		}
	}
	
	/**
	 * Set the mail clause operator.
	 * 
	 * @param operator MailClauseOperator - the given mail clause operator.
	 */
	public void setOperator(MailClauseOperator operator){
		this.setField(MailFilterClause.CLAUSE_OPERATOR, operator.toString()) ;
	}
	
	/**
	 * @return the list of filter clauses.
	 */
	public List<MailFilterClauseUnit> getFilterClauses(){
		try{
			return ConvertService.toObject(this.getField(MailFilterClause.FILTER_CLAUSES)) ;
		}catch(ConvertException cex){
			return null ;
		}
	}
	
	/**
	 * Set the filter clauses unit.
	 * 
	 * @param clauses List<MailFilterClauseUnit> - the given list of clause units.
	 */
	public void setFilterClauses(List<MailFilterClauseUnit> clauses){
		// remove the first filter clauses.
		INetField field = this.getField(MailFilterClause.FILTER_CLAUSES) ;
		if(field != null) this.remove(field) ;
		
		// create new filter clauses.
		field = new INetField(MailFilterClause.FILTER_CLAUSES, clauses) ;
		this.add(field) ;
	}
	
	/**
	 * Adds filter clause to system.
	 * 
	 * @param clause MailFilterClauseUnit - the given mail filter clause unit.
	 */
	public void addFilterClauses(MailFilterClauseUnit clause){
		// get the list of filter clauses.
		INetField field = this.getField(MailFilterClause.FILTER_CLAUSES) ;
		List<MailFilterClauseUnit> clauses = null ;
		if(field == null){
			clauses = new ArrayList<MailFilterClauseUnit>() ;
		}else{
			try{
				clauses = ConvertService.toObject(field) ;
			}catch(ConvertException cex){
				clauses = new ArrayList<MailFilterClauseUnit>() ;
			}
		}
		
		// add clause to list of clauses.
		clauses.add(clause) ;
		
		// remove the previous field.
		if(field != null) this.remove(field) ;
		
		// set data to field.
		field = new INetField(MailFilterClause.FILTER_CLAUSES, clauses) ;
		
		// add field to data.
		this.add(field) ;
	}
	
	/**
	 * Removes filter clause out of system.
	 * 
	 * @param clause MailFilterClauseUnit - the given mail filter clause unit.
	 */
	public void removeFilterClauses(MailFilterClauseUnit clause){
		// get the list of filter clauses.
		INetField field = this.getField(MailFilterClause.FILTER_CLAUSES) ;
		List<MailFilterClauseUnit> clauses = null ;
		if(field == null){
			clauses = new ArrayList<MailFilterClauseUnit>() ;
		}else{
			try{
				clauses = ConvertService.toObject(field) ;
			}catch(ConvertException cex){
				clauses = new ArrayList<MailFilterClauseUnit>() ;
			}
		}
		
		// add clause to list of clauses.
		clauses.remove(clause) ;
		
		// remove the previous field.
		if(field != null) this.remove(field) ;
		
		// set data to field.
		field = new INetField(MailFilterClause.FILTER_CLAUSES, clauses) ;
		
		// add field to data.
		this.add(field) ;		
	}
	
	/**
	 * @return the bytes array.
	 */
	public byte[] toBytes(){
		// convert object to bytes array.
		byte[] data = IOService.getStream(this) ;
		
		// encode object data.
		String encodeData = DigestService.base64Encode(data) ;
		
		// return data to user.
		return DigestService.utf8encode(encodeData) ;
	}
	
	/**
	 * Convert the bytes array to MailFilterClause.
	 * 
	 * @param data byte[] - the given bytes array.
	 * @return the MailFilterClause or null.
	 */
	public static MailFilterClause convertTo(byte[] data){
		if(data == null || data.length == 0) return null ;
		
		// get encode data.
		String encodeData = DigestService.utf8decode(data) ;
		
		// get the real data object.
		byte[] realData = DigestService.base64Decode(encodeData) ;
		
		// return the MailFilterClause instance.
		return IOService.getObject(realData, MailFilterClause.class) ;
	}
}
