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
 * @class WebOSGroupingStore
 * @extends Ext.data.GroupingStore
 * 
 * Create WebOSStore, that allow user can be grouping by, filter by the criteria tha
 * user want.
 * 
 * @constructor
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.ui.common.store.WebOSGroupingStore=function(config){
	// configures the filter object.
	config = config || {} ;
	config.filter = config.filter || {data:'all'} ;
	
	// call the superclass constructor to initialization object.
	iNet.iwebos.ui.common.store.WebOSGroupingStore.superclass.constructor.call(this, config) ;	
};

Ext.extend(iNet.iwebos.ui.common.store.WebOSGroupingStore,Ext.data.GroupingStore,{
	/**
	 * Apply filter to store.
	 * 
	 * @param {Object} filterDt - the given filter data.
	 */
	applyFilter:function(filterDt){
		if(filterDt !== undefined){
			if (typeof filterDt == 'object') {
				this.filter.data = filterDt.data;
				this.filter.fn = filterDt.fn ;
				this.filter.scope = filterDt.scope ;
			}else{
				this.filter.data = filterDt ;
			}
		}
		
		// get filter value.
		var value = this.filter.data;
		
		// filter all.
		if(value == 'all' || !this.filter.fn){
			return this.clearFilter() ;
		}
		
		// filter on data.
		return this.filterBy(function(item){
			if(!this.filter.scope){
				return this.filter.fn(item, value) ;
			}else{
				return this.filter.fn.apply(this.filter.scope, [item, value]) ;
			}
		}) ;
	},
	
	/**
	 * Add data to store.
	 */
	addData : function(data){
		var Record = this.reader.recordType ;
		// create record.
		var record = new Record(data, data.id) ;
		record.json = data;
		
		// clear event.
		this.suspendEvents() ;
		this.clearFilter() ;
		this.resumeEvents() ;
		
		// append to store.
		this.loadRecords({success:true,records:[record],totalRecords:1}, {add:true}, true) ;
		
		this.suspendEvents() ;
		this.applyFilter() ;
		this.applyGrouping(true) ;
		this.resumeEvents() ;
		this.fireEvent('datachanged', this) ;
	}	
});
