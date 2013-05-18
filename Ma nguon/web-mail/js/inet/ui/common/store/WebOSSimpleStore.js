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
 * @class WebOSSimpleStore
 * @extend Ext.data.Store
 * 
 * Create WebOSStore allow user can be store data to local database.
 * @constructor
 * @param {Object} config - the given store configuration.
 */
iNet.iwebos.ui.common.store.WebOSSimpleStore = function(config){
	// create data.
	var data = config.data || [] ;
	
	if(config.data){
		delete config.data ;
	}
	
	// create store.
	iNet.iwebos.ui.common.store.WebOSSimpleStore.superclass.constructor.call(this, config) ;
	
	// create proxy.
	this.proxy = new Ext.data.MemoryProxy(data) ;
};

Ext.extend(iNet.iwebos.ui.common.store.WebOSSimpleStore, Ext.data.Store,{
	/**
	 * Add data to store.
	 */
	addData : function(data){
		this.suspendEvents() ;
		this.clearFilter() ;
		this.resumeEvents() ;
		
		// load data.
		this.loadData([data], true) ;		
		this.suspendEvents() ;
		
		// fire data change.
		this.resumeEvents() ;
		this.fireEvent('datachanged',this) ;	
	}
}) ;
