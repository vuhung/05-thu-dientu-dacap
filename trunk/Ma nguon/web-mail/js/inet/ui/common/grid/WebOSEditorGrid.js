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
 * @class WebOSEditorGrid 
 * @extend Ext.grid.EditorGridPanel
 * 
 * Create grid that allow user can be edited.
 * @constructor
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.ui.common.grid.WebOSEditorGrid = Ext.extend(Ext.grid.EditorGridPanel,{
	/**
	 * @cfg {Object} proxy - the retrieve data proxy.
	 */
	/**
	 * @cfg {String} url - the URL to be retrieved data.
	 */
	/**
	 * @cfg {String} method - the given method to submit data.
	 */
	/**
	 * @cfg {Object} sortInfo - the sort object.
	 */
	/**
	 * @cfg {String} groupField - group field.
	 */
	/**
	 * @cfg {String} groupLabel - group label.
	 */
	/**
	 * @cfg {Object} baseParams - the base parameters to submit to server.
	 */
	/**
	 * @cfg {Object} reader - the grid reader.
	 */
	/**
	 * @cfg {Object} groupTextTpl - the given group text template.
	 */
	/**
	 * @cfg {Object} view - the grid view
	 */
	/**
	 * @cfg {int} start - the start position.
	 */
	/**
	 * @cfg {Object} filter - the given filter object.
	 */
	/**
	 * @cfg {boolean} loadFirst - the store will be load at the first time.
	 */
	loadFirst: true,
	/**
	 * @cfg {int} limit - the limit position.
	 */
	/**
	 * @cfg {Function} rowClass - the given row class function.
	 * the function template function(row).
	 */
	/**
	 * @cfg {boolean} selectFirstRow - the select first row flag. that mark
	 * selected first row in current grid, default true.
	 */
	/**
	 * Initialized the grid component.
	 */
	initComponent:function(){
		// setting method.
		this.method = (!this.method) ? 'GET' : this.method ;
		
		// setting up the proxy.
		this.proxy = (!this.proxy) ? new iNet.iwebos.ui.common.grid.HttpProxyWrapper({url: this.url, method: this.method}) : this.proxy ;
		
		// setting up the sort information. (sorted by id).
		this.sortInfo = (!this.sortInfo) ? {field: 'id', direction: 'ASC'} : this.sortInfo ;
		
		// setting up selection model.
		this.sm = (!this.sm) ? new Ext.grid.RowSelectionModel() : this.sm ;
		
		// setting selectFirstRow flag.
		this.selectFirstRow = (!this.selectFirstRow) ? true : !!this.selectFirstRow ;
		
		// setting up group label.
		this.groupLabel = (!this.groupLabel) ? idesk.webos.message.paperwork.ppwitem : this.groupLabel ;
		
		// create default grouping template.
		this.groupTextTpl = (!this.groupTextTpl) ? '{text} ({[values.rs.length]} {[values.rs.length > 0 ? \"' + this.groupLabel + '\":"No item"]})' : this.groupTextTpl ;
		
		// setting up row class.
		this.rowClass = (!this.rowClass) ? function(row){return '';} : this.rowClass ;
				
		// setting auto load.
		this.loadFirst = !!this.loadFirst ;
		
		// setting up start and limit data.
		this.start = (!this.start) ? 0 : this.start ;
		this.limit = (!this.limit) ? iNet.INET_PAGE_LIMIT : this.limit ;

		var store ;
		// setting up store.
		if(!this.store){
			store = new iNet.iwebos.ui.common.store.WebOSGroupingStore({
				proxy: this.proxy,
				sortInfo: this.sortInfo,
				groupField: this.groupField,
				baseParams: this.baseParams,
				reader: this.reader,
				filter: this.filter
			}) ;
		}else{
			store = this.store ;
			delete this.store ;
		}
				
		// lookup store.
		store = Ext.StoreMgr.lookup(store) ;

		// bind store.
		this.bind(store) ;

		// loading store.
		if(this.loadFirst){
			this.store.load({params:{start:this.start, limit: this.limit}});
		}
						
		// set shadow
		this.shadow = false ;

		// setting up the grid view.
		if(!this.view){
			this.view = new Ext.grid.GroupingView({
				forceFit:true,
				emptyText: idesk.webos.message.paperwork.emptyresult,
				groupTextTpl: this.groupTextTpl,
				getRowClass: this.rowClass
			}) ;
		}
		
		// set default load mask.
		if(!this.loadMask){
			this.loadMask = {msg: idesk.webos.message.load_data} ;
		}
		
		// initialization the grid.
		iNet.iwebos.ui.common.grid.WebOSEditorGrid.superclass.initComponent.call(this) ;
	},
	
	/**
	 * Capture the search when user press search button.
	 * 
	 * @param {Object} params - the list of search parameters.
	 */
	onSearch : function(params){
		// create object.
    	var index = 0;
    	var buf = [] ;
    	var param = null ; // current parameter.
    	for(index = 0; index < params.length; index++){
    		param = params[index] ;
    		buf.push(param.key + ':\'' + param.value + '\'') ;
    	}
    	
    	// get object value.
    	var paramValue = buf.join(',') ;
		var searchParams = eval('({' + paramValue + '})') ;
		
		// search param.
		this.store.baseParams = Ext.apply(this.baseParams || {}, searchParams) ;
		
		// reload store.
		this.store.load() ;		
	},
	
	/**
	 * bind the current store.
	 */
	bind : function(store){
		if(this.selectFirstRow){
			if(this.store){
				this.store.un('load', this.onStoreLoad, this) ;
			}
			
			if(store){
				store.on('load', this.onStoreLoad, this) ;
			}
		}
		
		// setting grid store.
		this.store = store; 
	},
	
	/**
	 * select first row.
	 */
	onStoreLoad : function(){
		// select first row.
		if(this.store && this.store.getCount() > 0){
			this.selModel.selectFirstRow();
		}				
	}
});