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
package com.inet.mail.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inet.base.service.IOService;

/**
 * UIDStore
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: UIDStore.java 2008-12-11 13:05:25Z nguyen_dv $
 * 
 * Create date: Dec 11, 2008
 * <pre>
 *  Initialization UIDStore class.
 * </pre>
 */
public class UIDStore implements Serializable, Cloneable{
	//~ Static fields =========================================================
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2471437764753049143L;
	
	//~ Instance fields =======================================================
	/* list of unique identifier. */
	private final Map<String, Boolean> cache;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>UIDStore</tt> instance.
	 */
	public UIDStore(){
		cache = new HashMap<String, Boolean>(16) ;
	}
	
	/**
	 * Create a new <tt>UIDStore</tt> from the given 
	 * list of unique identifier.
	 * 
	 * @param uids the given list of unique identifiers.
	 */
	public UIDStore(List<String> uids){
		cache = new HashMap<String, Boolean>(16) ;
		if(uids != null){
			for(String uid : uids){
				cache.put(uid, Boolean.FALSE) ;
			}
		}
	}
	
	//~ Methods ===============================================================
	/**
	 * Load list of unique identifier and put to map.
	 * 
	 * @param uids the list of unique identifier.
	 */
	public void load(List<String> uids){
		if(uids != null){
			for(String uid : uids){
				cache.put(uid, Boolean.FALSE) ;
			}
		}
	}
	
	/**
	 * Optimize the UID store.
	 */
	public List<String> optimize(){
		List<String> uids = new ArrayList<String>() ;
		Map<String, Boolean> result = new HashMap<String, Boolean>() ;
		
		if(cache.size() > 0){
			Set<String> keys = cache.keySet() ;
			for(String uid : keys){
				if(Boolean.FALSE.equals(cache.get(uid))){
					uids.add(uid) ;
				}else{
					result.put(uid, Boolean.FALSE) ;
				}
			}
		}
		
		// clean the cache.
		cache.clear() ;
		cache.putAll(result) ;
		
		return uids ;
	}
	
	/**
	 * Check the mail with the given unique identifier is existing
	 * or new mail.
	 * 
	 * @param uid the given mail unique identifier.
	 * @param putable the given flag that put data to cache or not.
	 * @return if the mail is new mail.
	 */
	public boolean isNew(String uid, boolean putable){
		boolean exist = cache.containsKey(uid) ;
		
		// put to cache.
		if(putable || exist){
			cache.put(uid, Boolean.TRUE) ;
		}
		
		return !exist ;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return IOService.makeNewObject(this, UIDStore.class);
	}
}
