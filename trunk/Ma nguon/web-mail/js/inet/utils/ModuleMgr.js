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
/**
 * @class iNet.ModuleMgr
 * <p>
 * Provides a registry of all modules on current system so that they
 * can be easily accessed by component identifier.
 * @singleton
 */
 iNet.ModuleMgr=function(){
 	/**
 	 * all modules in current system.
 	 */
 	var all = new Ext.util.MixedCollection() ;
 	
 	/**
 	 * the list of functions.
 	 */
 	return{
 		/**
 		 * Add object to collection.
 		 * 
 		 * @param key - the given object key.
 		 * @param object - the given object value.
 		 */
 		register: function(key, object){
 			all.add({id:key, value:object}) ;
 		},
 		
 		/**
 		 * Remove object from the given object key.
 		 * 
 		 * @param key String - the given object key.
 		 */
 		unregister: function(key){
 			all.removeKey(key) ;
 		},
 		
 		/**
 		 * @return all of the items in current collection.
 		 */
 		all:all,
 		
 		/**
 		 * Return the module from the given module identifier.
 		 */
 		get: function(key){
 			return all.get(key) ;
 		}
 	}
 }() ;