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
 * @class TreeColumnNodeUI
 * @extends Ext.tree.TreeNodeUI
 * 
 * Create treeview that has any column. Support checkbox with build in data.
 * @constructor
 * @param {Ext.tree.TreeNode} node the treeview node.
 */

iNet.iwebos.ui.common.tree.TreeColumnNodeUI = Ext.extend(Ext.tree.TreeNodeUI,{
	/**
	 * Render the tree elements.
	 * 
	 * @param {Ext.tree.TreeNode} n - the given tree node.
	 * @param {Element} a - the anchor node element.
	 * @param {Ext.tree.TreeNode} targetNode - the target node.
	 * @param {boolean} bulkRender - the given bulk render or not.
	 */
	renderElements: function(n, a, targetNode, bulkRender){
		// get indent markup.
		this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '' ;
		
		// get comopent that owner tree.
		var ownerTree = n.getOwnerTree() ;
		// get list of column.
		var cols = ownerTree.columns ;
		// get border width.
		var bw = ownerTree.borderWidth ;
		// show the checkbox.
		var cb = typeof a.checked == 'boolean' ;
		
		// get the first column and draw the tree.
		var column = cols[0] ;
		
		// get the number of columns.
		var columnSize = cols.length ;
		
		// get the http reference.
		var href = a.href ? a.href : Ext.isGecko ? "" : "#" ;
		
		// prepare data render object.
		var buf = [
			'<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf ',a.cls,'">',
				'<div class="x-tree-col" style="width:',column.width-bw,'px;">',
					'<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
					'<img src="',this.emptyIcon,'" class="x-tree-ec-icon x-tree-elbow"/>',
					cb ? ('<input class="x-tree-node-cb" type="checkbox" ' + (a.checked ? 'checked="checked"/>' : '/>')) : '',
					'<img src="',a.icon || this.emptyIcon,'" class="x-tree-node-icon',(a.icon? " x-tree-node-inline-icon":""),(a.iconCls? " " + a.iconCls:""),'" unselectable="on"/>',
					'<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ', a.hrefTarget ? ' target="' + a.hrefTarget+'"' : "", '>',
						'<span unselectable="on">', n.text || (column.renderer ? column.renderer(a[column.dataIndex], n, a) : a[column.dataIndex]), '</span>',					
					'</a>',
				'</div>'		
		];
		
		var index = 1;
		// prepare data of other node.
		for(index=1; index < columnSize; index++){
			// get the next column.
			column = cols[index] ;
			
			// prepare data.
			buf.push(
					'<div class="x-tree-col" style="width:',column.width-bw,'px;">',
						'<div class="x-tree-col-text">',(column.renderer ? column.renderer(a[column.dataIndex], n, a) : a[column.dataIndex]),'</div>',
					'</div>'
				);
		}
				
		// draw the end of tree.
		buf.push(
				'<div class="x-clear"></div>','</div>',
				'<ul class="x-tree-node-ct" style="display:none;"></ul>',
				'</li>'
			);
		
		// define next sibling element.
		var nel ;	
		// user want to bulk render.
		if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){
			this.wrap = Ext.DomHelper.insertHtml(
									"beforeBegin",
									nel,
									buf.join("")
								) ;
		}else{
			this.wrap = Ext.DomHelper.insertHtml(
									"beforeEnd",
									targetNode,
									buf.join("")
								) ;
		}
		
		// draw data.
		this.elNode = this.wrap.childNodes[0] ;
		this.ctNode = this.wrap.childNodes[1] ;
		
		// get children node.
		var cs = this.elNode.firstChild.childNodes ;
		this.indentNode = cs[0] ;
		this.ecNode = cs[1] ;
		
		// hold the current position.
		index = 2;		
		// user want to draw checkbox.
		if(cb){
			this.checkbox = cs[index] ;
			
			// check checkbox on IE6.
			this.checkbox.defaultChecked = this.checkbox.checked ;
			
			// increase index.
			index++ ;
		}		
		
		this.iconNode = cs[index] ;		
		// set anchor tag and text node tag.
		this.anchor = cs[index + 1] ;
		this.textNode = cs[index + 1].firstChild ;
 	}
}) ;
