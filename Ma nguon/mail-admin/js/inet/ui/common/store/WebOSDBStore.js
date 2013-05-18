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
 * @class WebOSDBStore
 * @extends Ext.data.Store
 * 
 * Create WebOSDBStore to load data from server database or local database.
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.ui.common.store.WebOSDBStore = function(config){
	// get the connection.
	var readonly = config.readonly || false ;
	var transaction = config.transaction || false ;

	// set table and key name.
	this.table = config.table || 'default' ;
	this.keyName = config.key || 'id' ;
	this.loadParams = config.load || {} ;
	this.addParams = config.add;
	this.updateParams = config.update ;
	this.removeParams = config.remove ;
	this.conn = config.conn || iNet.Database.getConnection() ;
	
	// prepare data.
	this.prepareData = config.prepare || {fn: function(object,dirty){object['dirty'] = !!dirty; return object;}} ;
	
	// set record.
	this.record = Ext.isArray(config.record) ? Ext.data.Record.create(config.record) : config.record ;	
	
	// initialized the object.
	iNet.iwebos.ui.common.store.WebOSDBStore.superclass.constructor.call(this,{
		sortInfo: config.sortInfo,
		baseParams: config.baseParams,
		reader: new Ext.data.JsonReader({
			idProperty: this.keyName
		}, this.record)
	}) ;
	
	// create store proxy.
	this.proxy = new iNet.iwebos.data.db.DBProxy(this.conn, this.table, this.keyName, this, readonly, transaction) ;
	
	// create the table.
	if(window.google && google.gears){
		this.proxy.on('beforeload', this.prepareTable, this) ;
	}
};

Ext.extend(iNet.iwebos.ui.common.store.WebOSDBStore, Ext.data.Store,{
	/**
	 * create table.
	 */
	prepareTable:function(){
		this.conn.createTable({
			name: this.table,
			key: this.keyName,
			fields: this.record.prototype.fields
		})
	},
	
	/**
	 * Load data.
	 */
	load: function(options){
		var loadOptions = options || {} ;
		if(!loadOptions.callback){
			loadOptions['callback'] = this.loadCallback ;
			loadOptions['scope'] = this ;
		}

		// call super class load function.
		iNet.iwebos.ui.common.store.WebOSDBStore.superclass.load.call(this, loadOptions) ;
	},
	
	/**
	 * load callback function.
	 */
	loadCallback: function(){
		// load data from server.
		if(this.getCount() < 1){
			this.loadServer(this.loadParams) ;
		}
	},
	
	/**
	 * Load data from server.
	 * 
	 * @param {Object} params - the given parameter.
	 */
	loadServer : function(params){
		var url = params.url ;
		var baseParams = params.params || {} ;
		var successHandler = params.success || function(response,option){} ;
		var failureHandler = params.failure || function(response, option){} ;
		var method = params.method || 'GET' ;
			
		// create loader.
		Ext.Ajax.request({
			url: url,
			success: this.success,
			successHandler: successHandler,
			handlerScope: params.scope,
			failure: this.failure,
			failureHandler: failureHandler,
			method: method,
			scope: this,
			params: baseParams
		}) ;
	},
	/**
	 * Handle success event.
	 */
	success : function(response, options){
		// get success handler.
		var successHandler = options.successHandler ;
		if(successHandler != undefined){
			var objects = null ;
			if(!options.handlerScope){
				objects = successHandler(response) ;
			}else{
				objects = successHandler.apply(options.handlerScope, [response]) ;
			}
			
			// process data.
			var len = objects.length ;
			for(var index = 0; index < len; index++){
				this.addObject(objects[index], false) ;
			}
		}
	},
	/**
	 * Handle failure event.
	 */
	failure: function(response, options){
		// get failure handler.
		var failureHandler = options.failureHandler ;
		if(failureHandler != undefined){
			if(!options.handlerScope){
				failureHandler(response) ;
			}else{
				failureHandler.apply(options.handlerScope, [response]) ;
			}
		}
	},
	/**
	 * Add object to store.
	 */
	addObject: function(object, dirty){
		this.suspendEvents() ;
		this.clearFilter() ;
		this.resumeEvents() ;
		
		// prepare object to add to database.
		var newObj = null ;
		if(!this.prepareData.scope){
			newObj = this.prepareData.fn(object, !!dirty) ;
		}else{
			newObj = this.prepareData.fn.apply(this.prepareData.scope, [object, !!dirty]) ;
		}
		
		// load data.
		this.loadData([newObj], true) ;		
		this.suspendEvents() ;
		
		// fire data change.
		this.resumeEvents() ;
		this.fireEvent('datachanged',this) ;
	}
});