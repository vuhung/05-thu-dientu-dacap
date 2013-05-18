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
 * @class WebOSGridService
 * @extends Object
 *
 * Provides some grid utilities
 */
(function(){
	//~ Defines class =========================================================
	WebOSGridService={};
	
	//~ Class methods =========================================================
	/**
	 * Handle the selection.
	 * 
	 * @param {Ext.grid.GridPanel} grid - the given grid instance.
	 * @param {Number} index - the given index.
	 */
	WebOSGridService.handleSelection = function(grid, index){
		if(!grid.getSelectionModel().hasSelection() || grid.getSelectionModel().singleSelect){
			grid.getSelectionModel().selectRow(index) ;
		}else{
			// get current row.
			var __selectRecord = grid.store.getAt(index) ;
			
			// has a record.
			if(__selectRecord){
				var __keepSelected = grid.getSelectionModel().isSelected(__selectRecord) ;
				
				// select the row.
				grid.getSelectionModel().selectRow(index, __keepSelected) ;
			}
		}
	}
})();