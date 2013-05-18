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
 * @class DBProxy
 * @extends Ext.data.DataProxy
 * 
 * Create Database Proxy.
 * @constructor 
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.data.db.DBProxy = function(conn,table,keyName,store,readonly,transaction){
	// call constructor.
	iNet.iwebos.data.db.DBProxy.superclass.constructor.call(this) ;
	
	// setting up parameters.
	this.conn = conn ;
	this.table = this.conn.getTable(table, keyName) ;
	this.keyName = keyName ;
	this.store = store ;
	this.transaction = transaction || false ;
	
	// setting up data change event.
	if (readonly !== true) {
		this.store.on('add', this.onAdd, this);
		this.store.on('update', this.onUpdate, this);
		this.store.on('remove', this.onRemove, this);
	}
	
	// add event to synchronized data to server.
	this.addEvents({
		/**
		 * add data to database.
		 * 
		 * @param {Object} data - the data add to database.
		 */
		synchadd:true,
		/**
		 * update data to database.
		 * 
		 * @param {Object} data - the data update to database.
		 */
		synchupdate:true,
		/**
		 * remove data from database.
		 * 
		 * @param {Object} data - the list of data to delete.
		 */
		synchremove:true
	});
};

Ext.extend(iNet.iwebos.data.db.DBProxy, Ext.data.DataProxy, {
	/**
	 * Load data.
	 * 
	 * @param {Object} params - the given parameters.
	 * @param {Object} reader - the given reader.
	 * @param {Object} callback - the given callback handler.
	 * @param {Object} scope - the given scope.
	 * @param {Object} args - the given arguments.
	 */
	load: function(params, reader, callback, scope, arg){
		if(!this.conn.isOpen()){ // assume that the connection is in the process of openning.
			this.conn.on('open', function(){
				this.load(params, reader, callback, scope, arg) ;
			},this, {single:true}) ;
			return ;
		}
		
		// load data.
		if(this.fireEvent("beforeload", this, params, reader, callback, scope, arg) !== false){
			var clause = params.where || '' ;
			var args = params.args || [] ;
			var groupBy = params.groupBy;
			var sort = params.sort;
			var dir = params.dir ;
			
			if(groupBy || sort){
				clause += ' ORDER BY ' ;
				if(groupBy && groupBy != sort){
					clause += groupBy + ' ASC, ' ;
				}
				clause += sort + ' ' + (dir || 'ASC') ;
			}
			
			// load data.
			this.table.selectBy(clause, args, this.onLoad.createDelegate(this, [{callback:callback, scope:scope, arg: arg, reader:reader}], 0)) ;
		}else{
			callback.call(scope || this, null, arg, false) ;
		}
	},
	
	onLoad : function(trans, rs, e, stmt){
		if(rs === false){
			this.fireEvent("loadexception", this, null, trans.arg, e) ;
			trans.callback.call(trans.scope || window, null, trans.arg, false) ;
			return;
		}
		
		var result = trans.reader.readRecords(rs) ;
		this.fireEvent("load", this, rs, trans.arg) ;
		trans.callback.call(trans.scope || window, result, trans.arg, true) ;
	},
	
	/**
	 * Process data from the given object.
	 * 
	 * @param {Object} object - the given object to process.
	 */
	processData : function(object){
		var fs = this.store.fields;
		var result = {} ;
		
		for(var key in object){
			var field = fs.key(key), value = object[key] ;
			if(field){
				if(field.type == 'date'){
					result[key] = value ? value.format(Ext.sql.Proxy.DATE_FORMAT,10) : '' ;
				}else if(field.type == 'boolean'){
					result[key] = value ? 1 : 0 ;
				}else{
					result[key] = value ;
				}
			}
		}
		
		return result;
	},
	
	onAdd : function(ds, records, index){
		var transact = (this.transaction) ? this.conn.getTransaction() : null ;
		for(var i = 0; i < records.length; i++){
			try{
				if(transact !== null) transact.begin() ;
				this.table.insert(this.processData(records[i].data)) ;
				if(transact !== null) transact.commit() ;
				
				// raise event add data to server.
				if(records[i].data.dirty){
					this.fireEvent("synchadd", records[i].data) ;
					
					// add data to server.
					records[i].data.dirty = false ;
				}
			}catch(ex){
				if(transact !== null) transact.rollback() ;
			}
		}
	},
	
	onUpdate:function(ds, record){
		var changes = record.getChanges() ;
		var kn = this.table.keyName;
		
		var transact = (this.transaction) ? this.conn.getTransaction() : null ;
		
		try{
			// begin the transaction.
			if(transact !== null) transact.begin() ;
			
			// update data.
			this.table.updateBy(this.processData(changes), kn + ' = ?', [record.data[kn]]) ;
			// commit data.
			record.commit(true) ;
			
			// commit the transaction.
			if(transact !== null) transact.commit() ;
			
			// raise event update data to server.
			if(record.data.dirty){
				fireEvent('synchupdate', record.data) ;
				
				// updated data to server
				record.dirty = false ;
			}
		}catch(ex){
			if(transact !== null) transact.rollback() ;
		}
	},
	onRemove:function(ds,record,index){
		// get key name.
		var kn = this.table.keyName ;
		
		var transact = (this.transaction) ? this.conn.getTransaction() : null ;
		try{
			// begin the transaction.
			if(transact !== null) transact.begin() ;
			
			this.table.removeBy(kn + ' = ?', [record.data[kn]]) ;
			
			// commit the transaction.
			if(transact !== null) transact.rollback() ;
			
			// raise event delete data from server.
			if(record.data.dirty){
				fireEvent('synchdelete',[record.data]) ;
				
				// deleted data from server
				record.data.dirty = false ;
			}
		}catch(ex){
			// rollback the transaction.
			if(transact !== null) transact.rollback() ;
		}
	}
}) ;
