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
 * @class Connection
 * @extends Ext.util.Observable
 * 
 * Allow system connect and process data at local system, that mean 
 * system will be create local database and process this data before
 * commit data to server.
 * @constructor
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.data.db.Connection = function(config){
	/**
	 * apply the configuration.
	 */
	Ext.apply(this, config) ;
	
	// create connection object.
	iNet.iwebos.data.db.Connection.superclass.constructor.call(this) ;
	
	// init event.
	this.addEvents({
		/**
		 * open event.
		 * @param {Connection} conn - the current connection.
		 */
		open:true,
		/**
		 * close event.
		 * @param {Connection} conn - the current connection.
		 */
		close:true
	})
};

Ext.extend(iNet.iwebos.data.db.Connection, Ext.util.Observable,{
	/**
	 * set max result.
	 */
	maxResults: 10000,
	/**
	 * connection status.
	 */
	openState: false,	
	/**
	 * Open database from the given configuration.
	 * 
	 * @param {Object} config - the given database configuration.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	open: function(config, callback, scope){},
	/**
	 * close database.
	 */
	close: function(){},
	/**
	 * Execute the SQL statement.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	exec:function(sql,callback,scope){},
	/**
	 * Execute the SQL statement from the given SQL statement and arguments.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Object} args - the given SQL statement arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	execBy:function(sql,args,callback,scope){},
	/**
	 * Query data from the given SQL statement.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	query:function(sql,callback,scope){},
	/**
	 * Query data from the given SQL statement and arguments.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Object} args - the given SQL statement arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	queryBy:function(sql,args,callback,scope){},
	/**
	 * @return true if the database is open, otherwise false.
	 */
	isOpen:function(){
		return this.openState;
	},
	/**
	 * @return the database transaction.
	 */
	getTransaction:function(){
		return new iNet.iwebos.data.db.Transaction(this) ;
	},
	/**
	 * Get the table from database from the given table name
	 * and table key name.
	 * 
	 * @param {String} name - the given table name.
	 * @param {String} keyName - the given table key name.
	 */
	getTable:function(name, keyName){
		return new iNet.iwebos.data.db.Table(this,name,keyName) ;
	},
	/**
	 * Create the table from the given object.
	 * 
	 * @param {Object} object - the given object to be create the table.
	 */
	createTable:function(object){
		// get the table information.
		var tableName = object.name ;
		var keyName = object.key;
		var fs = object.fields;
		var callback = object.callback;
		var scope = object.scope ;
		
		if(!(fs instanceof Array)) fs = fs.items;
		
		// create SQL statement buffer.
		var buf = [] ;
		for(var index = 0; index < fs.length; index++){
			var field = fs[index], s = field.name ;
			switch(field.type){
				case "int":
				case "bool":
				case "boolean":
					s += ' INTEGER' ;
					break;
				case "float":
				case "double":
					s += ' REAL' ;
					break ;
				default:
					s+= ' TEXT' ;
					break;
			}
			
			// setting up the table option.
			if(field.allowNull === false || field.name == keyName){
				s += ' NOT NULL' ;
			}
			if(field.name == keyName){
				s += ' PRIMARY KEY' ;
			}
			if(field.unique === true){
				s += ' UNIQUE' ;
			}
			
			buf[buf.length] = s ;
		}
		
		// create table.
		var sql = ['CREATE TABLE IF NOT EXISTS ', tableName, '(', buf.join(','), ')'].join('') ;
		this.exec(sql, callback, scope) ;
	}
});

/**
 * @param db - the given database.
 * @param config - the given database configuration.
 * 
 * @return the connection.
 */
iNet.iwebos.data.db.Connection.getInstance = function(db, config){
	if(window.google && google.gears){
		return new iNet.iwebos.data.db.GearsConnection(config) ;
	}
	
	// avoid any error.
	return new iNet.iwebos.data.db.Connection(config);
};
