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
 * @class ColumnTreePanel
 * @extends Ext.tree.TreePanel
 * 
 * Render tree with column.
 * @constructor
 * @param {Object} config - the given column tree configuration.
 */
iNet.iwebos.ui.common.tree.ColumnTreePanel=Ext.extend(Ext.tree.TreePanel,{
	/**
	 * @cfg {boolean} lines
	 * 
	 * Draw row separator with lines.
	 */
	lines: false,
	
	/**
	 * @cfg {Number} borderWidth
	 * 
	 * The border width value.
	 */
	borderWidth: Ext.isBorderBox ? 0 : 2, // the combined left/right border for each cell.
	
	/**
	 * @cfg {String} cls
	 * 
	 * The tree class.
	 */
	cls: 'x-column-tree',
	
	/**
	 * private function.
	 * 
	 * Render the tree.
	 */
	onRender : function(){
		/**
		 * Call super class render function.
		 */
		iNet.iwebos.ui.common.tree.ColumnTreePanel.superclass.onRender.apply(this, arguments) ;
		
		// create header.
		this.headers = this.body.createChild(
				{cls: 'x-tree-headers'},
				this.innerCt.dom
			);
			
		// get the list of column.
		var cols = this.columns ;
		var columnSize = cols.length ;
		
		// define the column.
		var column ;
		
		// calculate the total width.
		var totalWidth = 0 ;
		
		// create header.
		for(var index = 0; index < columnSize; index++){
			// get the current column.
			column = cols[index] ;
			
			// calculate the tree width.
			totalWidth += column.width ;
			
			// create column header.
			this.headers.createChild({
				cls: 'x-tree-hd ' + (column.cls ? column.cls + '-hd' : ''),
				cn: {
					cls: 'x-tree-hd-text',
					html: column.header
				},
				style: 'width:' + (column.width - this.borderWidth) + 'px;' 
			}) ;
		}
		
		// create the lastest data.
		this.headers.createChild({cls: 'x-clear',style: 'width:*'}) ;
		
		// set tree-width and dom width.
		//this.headers.setWidth(totalWidth) ;
		this.innerCt.setWidth(totalWidth) ;
	}
}) ;
