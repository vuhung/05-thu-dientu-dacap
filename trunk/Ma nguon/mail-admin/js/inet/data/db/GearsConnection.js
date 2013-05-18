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
 * @class GearsConnection
 * @extend iNet.iwebos.data.db.Connection
 * 
 * Create local database from the given configuration using google gears technology.
 * @constructor
 * @param {Object} config - the given database configuration.
 */
iNet.iwebos.data.db.GearsConnection = Ext.extend(iNet.iwebos.data.db.Connection,{
	/**
	 * Open database from the given configuration.
	 * 
	 * @param {String} db - the given database name.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	open: function(db, callback, scope){
		// create database connection.
		this.conn = google.gears.factory.create('beta.database', '1.0') ;
		
		// open database.
		this.conn.open(db) ;
		this.openState = true ;
		
		// callback function.
		Ext.callback(callback, scope, [this]) ;
		
		// fire open event.
		this.fireEvent('open', this) ;
	},
	/**
	 * close database.
	 */
	close: function(){
		// close current connection.
		this.conn.close() ;
		this.openState = false ;
		
		// fire close event.
		this.fireEvent('close', this) ;
	},
	/**
	 * Execute the SQL statement.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	exec:function(sql,callback,scope){
		// execute the SQL statement.
		this.conn.execute(sql).close() ;
		
		// call callback function.
		Ext.callback(callback, scope, [true]) ;
	},
	/**
	 * Execute the SQL statement from the given SQL statement and arguments.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Object} args - the given SQL statement arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	execBy:function(sql,args,callback,scope){
		// execute the SQL statement.
		this.conn.execute(sql, args).close() ;
		
		// call callback function.
		Ext.callback(callback, scope, [true]) ;
	},
	/**
	 * Query data from the given SQL statement.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	query:function(sql,callback,scope){
		// execute the query.
		var rs = this.conn.execute(sql) ;
		
		// read data.
		var results = this.readResults(rs) ;
		
		// call callback function.
		Ext.callback(callback, scope, [results]) ;
		
		// return results.
		return results;
	},
	/**
	 * Query data from the given SQL statement and arguments.
	 * 
	 * @param {String} sql - the given SQL statement.
	 * @param {Object} args - the given SQL statement arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	queryBy:function(sql,args,callback,scope){
		// execute the query.
		var rs = this.conn.execute(sql, args) ;
		
		// read data.
		var results = this.readResults(rs) ;
		
		// call callback function.
		Ext.callback(callback, scope, [results]) ;
		
		// return results.
		return results ;
	},
	/**
	 * private function.
	 * read result from the query.
	 * 
	 * @param {ResultSet} rs - the given result set.
	 */	
	readResults: function(rs){
		var results = [] ;
		
		// read data from result set.
		if(rs){
			var count = rs.fieldCount() ;
			
			// precache the field.
			var fs = [] ;
			for(var index = 0; index < count; index++){
				fs[index] = rs.fieldName(index) ;
			}
			
			// read data.
			while(rs.isValidRow()){
				var object = {} ;
				for(var index = 0; index < count; index++){
					object[fs[index]] = rs.field(index) ;
				}
				
				// set data.
				results[results.length] = object;
				
				// next data.
				rs.next() ;
			}
			
			// close result set.
			rs.close() ;
		}
		
		// return results.
		return results ;
	},
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
	}
}) ;
