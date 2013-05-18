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
 * @class Date.
 * 
 * Add some utilities function.
 */
Ext.override(Date, {
	/**
	 * @return the first date of week from the current date.
	 */
	getFirstDateOfWeek : function(){
		// get the first week date.
		var __date = this.getDate() - this.getDay() ;

		// return the first date of week.		
		if(__date > 0){
			return new Date(this.getFullYear(), this.getMonth(), __date) ; 
		}else{
			var __month = this.getMonth() - 1;
			var __year = this.getFullYear() ;
			
			// the month is less than 0.
			if(__month < 0){
				__month = 0;
				__year = __year - 1;
			}
			
			// get the maximum day in current month.
			__date = this._getDaysInMonth(__month, __year) + __date ;			
			return new Date(__year, __month, __date) ;
		}
	},
	
	/**
	 * @return the first date of week from the current date.
	 */
	getLastDateOfWeek : function(){
		// get the last week date.
		var __date = this.getDate() - this.getDay() + 6 ;
		
		// get maximum day in current month.
		var __daysInMonth = this.getDaysInMonth();

		// the last date of week.		
		if(__date <= __daysInMonth){
			return new Date(this.getFullYear(), this.getMonth(), __date) ;
		}else{
			// compute month and year.
			var __month = this.getMonth() + 1;
			var __year = this.getFullYear() ;
			if(__month > 11){
				__month = __month - 12 ;
				__year = __year + 1;
			} 
			
			__date = __date - __daysInMonth ;
			return new Date(__year, __month, __date) ;
		}
	},
	
	/**
	 * @return the last date of next week from the current date.
	 */
	getLastDateOfNextWeek : function(){
		// get the last date of week from the current date.
		var __lastDateOfNextWeek = this.getLastDateOfWeek() ;
		
		// compute the last date of next week.
		__lastDateOfNextWeek = __lastDateOfNextWeek.add(Date.DAY, 7) ;
		
		// return the last date of next week.		
		return __lastDateOfNextWeek;
	},
	
	/**
	 * the days in the given month and years.
	 * 
	 * @param {int} month - the given month.
	 * @param {int} year - the given full year.
	 */
	_getDaysInMonth : function(month, year){
		// the given year is leap year.
		var __isLeapYear = !!((year & 3) == 0 && (year % 100 || (year % 400 == 0 && year))) ;
		if(month == 1) return __isLeapYear ? 29 : 28 ;
		return Date.daysInMonth[month] ;
	}
});
