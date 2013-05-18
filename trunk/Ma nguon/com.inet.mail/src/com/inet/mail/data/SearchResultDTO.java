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

import java.io.Serializable;
import java.util.List;

/**
 * SearchResultDTO
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 25, 2008
 * <pre>
 *  Initialization SearchResultDTO class.
 * </pre>
 */
public class SearchResultDTO<T> implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2846116634877635247L;

	//------------------------------------------------------------------------
	// Class properties.
	//
	// the given result set.
	private List<T> resultSet;
	// start position.
	private int startPos;
	// max result.
	private int maxResult;
	// max count.
	private int totalResult = 0;
	// the search criteria.
	private T criteria;
	
	//------------------------------------------------------------------------
	// Class members.
	//
	/**
	 * Create SearchResultDTO instance.
	 * 
	 * @param criteria T - the given search criteria.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given max result item.
	 */
	public SearchResultDTO(T criteria, int startPos, int maxResult){
		this.criteria = criteria ;
		this.startPos = startPos ;
		this.maxResult = maxResult ;
	}
	
	/**
	 * Create SearchResultDTO instance.
	 * 
	 * @param criteria T - the given search criteria.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given max result items.
	 * @param totalResult int - the given total result items.
	 */
	public SearchResultDTO(T criteria, int startPos, int maxResult, int totalResult){
		this.criteria = criteria ;
		this.startPos = startPos ;
		this.maxResult = maxResult ;
		this.totalResult = totalResult ;
	}
	
	/**
	 * Create SearchResultDTO instance.
	 * 
	 * @param resultSet List<T> - the given set of result set.
	 * @param startPos int - the given start position.
	 * @param maxResult int - the given max result item.
	 * @param totalResult int - the given total result items.
	 */
	public SearchResultDTO(List<T> resultSet, int startPos, int maxResult, int totalResult){
		this.resultSet = resultSet ;
		this.startPos = startPos;
		this.maxResult = maxResult ;
		this.totalResult = totalResult ;
	}
	
	/**
	 * @return the list of result set.
	 */
	public List<T> getResultSet(){
		return this.resultSet ;
	}
	
	/**
	 * Set the result set to the given search result transfer object.
	 * 
	 * @param resultSet List<T> - the given result transfer object.
	 */
	public void setResultSet(List<T> resultSet){
		this.resultSet = resultSet ;
		this.criteria = null ;
	}
	
	/**
	 * @return the criteria.
	 */
	public T getCriteria(){
		return this.criteria ;
	}
	
	/**
	 * Set the search criteria.
	 * 
	 * @param criteria T - the given search criteria.
	 */
	public void setCriteria(T criteria){
		this.criteria = criteria ;
		this.resultSet = null ;
	}
	
	/**
	 * @return the start position.
	 */
	public int getStartPos(){
		return this.startPos ;
	}
	
	/**
	 * Set the start position.
	 * 
	 * @param startPos int - the given start position.
	 */
	public void setStartPos(int startPos){
		this.startPos = startPos ;
	}
	
	/**
	 * @return the max result items.
	 */
	public int getMaxResult(){
		return this.maxResult ;
	}
	
	/**
	 * Set the max result items.
	 * 
	 * @param maxResult int - the given max result items.
	 */
	public void setMaxResult(int maxResult){
		this.maxResult = maxResult ;
	}
	
	/**
	 * @return the total result items.
	 */
	public int getTotalResult(){
		return this.totalResult ;
	}
	
	/**
	 * Set the total result items.
	 * 
	 * @param totalResult int - the given total result items.
	 */
	public void setTotalResult(int totalResult){
		this.totalResult = totalResult ;
	}
	
	/**
	 * @return <code>true</code> if user want to count the total result, otherwise <code>false</code>
	 */
	public boolean isCount(){
		return (this.totalResult <= 0) ;
	}
}
