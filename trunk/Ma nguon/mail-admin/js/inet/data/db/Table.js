/***************************************************************
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
 * @class Table
 * @extends Object
 * 
 * Create database table, and allow user can be query, process data from table.
 * @constructor
 * @param conn {Connection} - the given database connection.
 * @param name {String} - the given table name.
 * @param keyName {String} - the given table key name.
 */
iNet.iwebos.data.db.Table = function(conn,name,keyName){
	this.conn = conn ;
	this.name = name ;
	this.keyName = keyName ;
};

iNet.iwebos.data.db.Table.prototype={
	/**
	 * Update object to database.
	 * 
	 * @param {Object} object - the given object to be update.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	update : function(object,callback,scope){
		var clause = this.keyName + ' = ?' ;
		return this.updateBy(object, clause, [object[this.keyName]],callback,scope) ;
	},
	/**
	 * Update to database from the given object, clause and arguments.
	 * 
	 * @param {Object} object - the given update object.
	 * @param {String} clause - the given query clause.
	 * @param {Object} args - the query arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	updateBy:function(object, clause, args,callback,scope){
		var sql = "UPDATE " + this.name + " SET " ;
		
		// set of fields, arguments.
		var fs = [], a = [] ;
		
		for(var key in object){
			if(object.hasOwnProperty(key)){
				fs[fs.length] = key + ' = ?';
				a[a.length] = object[key] ;
			}
		}
		
		for(var key in args){
			if(args.hasOwnProperty(key)){
				a[a.length] = args[key] ;
			}
		}
		
		// create statement.
		sql = [sql, fs.join(','),' WHERE ', clause].join('') ;
		return this.conn.execBy(sql, a, callback, scope) ;
	},
	/**
	 * Insert object to database.
	 * 
	 * @param {Object} object - the given object to insert to database.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	insert:function(object, callback, scope){
		var sql = "INSERT INTO " + this.name + " " ;
		
		// set of fields, values and arguments.
		var fs = [], vs=[], a = [] ;
		for(var key in object){
			if(object.hasOwnProperty(key)){
				fs[fs.length] = key ;
				vs[vs.length] = '?' ;
				a[a.length] = object[key] ;
			}
		}
		
		// create SQL statement.
		sql = [sql, '(', fs.join(','), ') VALUES (', vs.join(','), ')'].join('') ;
		return this.conn.execBy(sql, a, callback, scope) ;
	},
	
	/**
	 * Select data from the database from the given clause and arguments.
	 * 
	 * @param {String} clause - the given SQL clause.
	 * @param {Object} args - the given list of arguments or null.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	selectBy:function(clause, args, callback, scope){
		var sql = "SELECT * FROM " + this.name ;
		if(clause){
			sql += ' WHERE ' + clause ;
		}		
		args = args || {} ;
		
		// execute the query.
		return this.conn.queryBy(sql, args, callback, scope) ;
	},
	
	/**
	 * Select data from the database from the given clause.
	 * 
	 * @param {Object} clause - the given SQL clause.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	select:function(clause, callback, scope){
		return this.selectBy(clause, null, callback, scope) ;
	},
	
	/**
	 * Remove the data from database from the given SQL clause and arguments.
	 * 
	 * @param {Object} clause - the given SQL clause.
	 * @param {Object} args - the arguments.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	removeBy:function(clause, args, callback, scope){
		var sql = "DELETE FROM " + this.name ;
		if(clause){
			sql += ' WHERE ' + clause ;
		}
		
		args = args || {} ;
		
		// execute the query.
		this.conn.execBy(sql, args, callback, scope) ;
	},
	
	/**
	 * Remove the data from database from the given SQL clause.
	 * 
	 * @param {Object} clause - the given SQL clause.
	 */
	remove:function(clause, callback, scope){
		this.removeBy(clause, null, callback, scope) ;
	},
	
	/**
	 * Save object to database, if the object is exist, update it otherwise insert it.
	 * 
	 * @param {Object} object - the given object to save to database.
	 */
	save:function(object, callback, scope){
		if(this.exists(object[this.keyName])) {
			this.update(object, callback, scope) ;
		}else{
			this.insert(object, callback, scope) ;
		}
	},
	
	/**
	 * Check the object exist or not.
	 * 
	 * @param {Object} id - the given object identifier.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	exists:function(id, callback, scope){
		return !!this.lookup(id, callback, scope) ;
	},
	
	/**
	 * Lookup the object from the given object identifier.
	 * 
	 * @param {Object} id - the given object identifier.
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	lookup:function(id, callback, scope){
		return this.selectBy(this.keyName + ' = ?', [id], callback, scope)[0] || null ;
	}
};
