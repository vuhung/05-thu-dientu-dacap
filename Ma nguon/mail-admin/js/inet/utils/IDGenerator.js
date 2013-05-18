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
 * @class IDGenerator
 * @extends Object.
 * 
 * This class used to generate the identifier.
 */
(function(){
	/**
	 * @class IDGeneratorStrategy.
	 * @extends Object.
	 * 
	 * This class used to make decistion to generate the identifier.
	 */
	IDGeneratorStrategy=function(config){
		// apply this configuration.
		Ext.apply(this, config || {}) ;
	};
	
	/**
	 * defined the strategy.
	 */
	IDGeneratorStrategy.prototype={
		/**
		 * Generate the identifier.
		 */
		generateId:function(){},
		
		/**
		 * return the identifier prefix.
		 */
		_getPrefix : function(){
			return this.prefix;
		},
		
		/**
		 * return the identifier suffix.
		 */
		_getSuffix : function(){
			return this.suffix;
		}
	};
	
	/**
	 * @class NumberIDGeneratorStrategy.
	 * @extends IDGeneratorStrategy
	 * 
	 * Generate the identifier as number.
	 */
	NumberIDGeneratorStrategy = function(config){};	
	NumberIDGeneratorStrategy = Ext.extend(IDGeneratorStrategy,{
		/**
		 * generate the identifier.
		 */
		generateId : function(){
			// get the date.
			var __date = new Date() ;
			// get the random number.
			var __number = Math.floor(Math.random() * 1000) ;
			// return the number.
			return (__date.getTime() + __number) ;
		}
	});

	/**
	 * @class AlphaIDGeneratorStrategy.
	 * @extends IDGeneratorStrategy
	 * 
	 * Generate the identifier as alphabet.
	 */	
	AlphaIDGeneratorStrategy = function(config){
		Ext.apply(this, config || {}) ;		
	};
	AlphaIDGeneratorStrategy = Ext.extend(IDGeneratorStrategy, {
		/**
		 * generate the identifier.
		 */
		generateId: function(){
			// get random size.
			var __size = this._getSize() ;
			
			// the time size.
			var __time = String(new Date().getTime()).substr(__size) ;
			
			// the alphabet data.
			var __alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' ;
			for(var index = 0; index < len; index++){
				__time += __alphabet.charAt(Math.floor(Math.random() * 26)) ;
			}		
						
			// append the prefix and suffix if exist.
			var __additional = this._getPrefix() ;
			if(__additional) __time = __additional + '-' + __time ;
			__additional = this._getSuffix() ;
			if(__additional) __time = __time + '-' + __additional ;
			
			// return the identifier.
			return __time ;			
		},
		
		/**
		 * return the random size.
		 */
		_getSize : function(){
			if(this.size == undefined) return 6 ;
			return ((this.size == undefined || this.size <= 0) ?  6 : this.size);
		}
	}) ;
	
	/**
	 * @class IDGenerator
	 * @extends Object.
	 * 
	 * Generate the identifier.
	 */
	IDGenerator=function(config){
		var __config = config || {} ;
		
		// apply configuration.
		Ext.apply(this, __config) ;
		
		/**
		 * getting the strategy.
		 */
		this.Strategy = __config.strategy || NumberIDGeneratorStrategy ;		
	};	
	IDGenerator.prototype = {
		/**
		 * Generate the identifier.
		 */
		generate : function(){
			// create strategy.
			var __strategy = new this.Strategy(this) ;
			
			// generate data.
			return __strategy.generateId() ;
		}
	};
})();
