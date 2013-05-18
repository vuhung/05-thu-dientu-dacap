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
 * @class Transaction
 * @extends Object.
 * 
 * Support transaction for local database.
 * @constructor
 * @param {Connection} conn - the given connection.
 */
iNet.iwebos.data.db.Transaction = function(conn){
	// setting the connection.
	this.conn = conn ;
} ;

iNet.iwebos.data.db.Transaction.prototype = {
	/**
	 * begin the transaction.
	 * 
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	begin : function(callback, scope){
		if(this.conn.isOpen()) this.conn.exec('BEGIN',callback,scope) ;
	},
	/**
	 * commit the transaction.
	 * 
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	commit : function(callback, scope){
		if(this.conn.isOpen()) this.conn.exec('COMMIT',callback,scope) ;
	},
	/**
	 * rollback the transaction.
	 * 
	 * @param {Function} callback - the given callback function.
	 * @param {Object} scope - the given callback function scope.
	 */
	rollback:function(callback, scope){
		if(this.conn.isOpen()) this.conn.exec('ROLLBACK',callback,scope) ;
	},
	/**
	 * Apply the the transaction to the given function.
	 * 
	 * @param fn - the given function to apply the transaction.
	 * @param callback - the given callback function.
	 * @param scope - the given callback function.
	 */
	transact:function(fn, callback, scope){
		// begin the transaction.
		this.begin(callback, scope) ;
		try{
			// execute the function.
			fn() ;
		}catch(ex){
			// rollback the transaction.
			this.rollback(callback, scope) ;
			throw ex ;
		}
		
		// commit the transaction.
		this.commit(callback, scope) ;
	}
} ;
