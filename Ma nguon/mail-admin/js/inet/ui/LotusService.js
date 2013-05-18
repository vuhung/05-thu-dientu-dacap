/*****************************************************************
   Copyright 2009 by Duyen Tang (tttduyen@truthinet.com)

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

(function(){
	LotusService = {};
	Ext.apply(LotusService, {
		/**
		 * handle key event on grid
		 * 
		 * @param {GridPanel} grid : the grid receive event
		 * @param {EventObject} event: the event object
		 */
		handleKeyEventOnGrid: function(grid, event) {
			if(event.ctrlKey && event.getKey() == 97) {
				// select all item on grid
				grid.getSelectionModel().selectAll();
			}
			event.stopEvent();
		}
	});
})();