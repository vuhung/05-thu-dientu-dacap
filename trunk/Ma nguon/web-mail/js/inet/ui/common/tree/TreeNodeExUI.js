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
 * @class TreeNodeExUI
 * @extends Ext.tree.TreeNodeUI
 * 
 * Modified the ordinary TreeNodeUI by display the checkbox previous 
 * image in the treeview.
 * @constructor
 * @param {Ext.tree.TreeNode} node - the given tree node to be render.
 */
iNet.iwebos.ui.common.tree.TreeNodeExUI = Ext.extend(Ext.tree.TreeNodeUI,{
	/**
	 * Render the tree element.
	 * 
	 * @param {Ext.tree.TreeNode} node - the given tree node to be rendered.
	 * @param {Element} data - the given element to be display.
	 * @param {Ext.tree.TreeNode} targetNode - the given target node to be append 
	 * render node into
	 * @param {boolean} bulkRender - bulk render or not.
	 */
	renderElements: function(node, data, targetNode, bulkRender){
		// get the indent.
		this.indentMarkup = node.parentNode ? node.parentNode.ui.getChildIndent() : '' ;
		
		// the node has check box.
		var cb = typeof data.checked == 'boolean' ;
		
		// get the http reference.
		var href = data.href ? data.href : Ext.isGecko ? "" : "#" ;
		
		// prepare data before render.
		var buf = [
			'<li class="x-tree-node"><div ext:tree-node-id="',node.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ',data.cls,'" unselectable="on">',
				'<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
				'<img src="',this.emptyIcon,'" class="x-tree-ec-icon x-tree-elbow"/>',
				cb ? ('<input class="x-tree-node-cb" type="checkbox" ' + (data.checked ? 'checked="checked"/>' : '/>')) : '',
				'<img src="',data.icon || this.emptyIcon,'" class="x-tree-node-icon ',(data.icon? " x-tree-node-inline-icon ":""),(data.iconCls? " " + data.iconCls:""),'" unselectable="on"/>',
				'<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ', data.hrefTarget ? ' target="' + data.hrefTarget+'"' : "", '>',
					'<span unselectable="on">', node.text, '</span>',
				'</a></div>',
				'<ul class="x-tree-node-ct" style="display:none;"></ul>',
			'</li>'
		] ;
		
		// get the next sibling node.
		var nel ;
		if(bulkRender !== true && node.nextSibling && (nel = node.nextSibling.ui.getEl())){
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
		
		// get the element child node.
		var cs = this.elNode.childNodes ;
		
		// get the detail node data.
		this.indentNode = cs[0] ;
		this.ecNode = cs[1] ;
		
		// hold the current position.
		var index = 2 ;
		if(cb){
			this.checkbox = cs[index] ;
			
			// display checkbox on the IE6.
			this.checkbox.defaultChecked = this.checkbox.checked ;
			
			// increase index.
			index++ ;
		}
		
		// get the image node.
		this.iconNode = cs[index] ;
		this.anchor = cs[index+ 1] ;
		this.textNode = cs[index + 1].firstChild ; 
	},
	
	/**
	 * handle selected change.
	 */
	onSelectedChange : function(state){		
		if(this.checkbox && !state){
			this.checkbox.checked = state ;
		}
		
		// call super class function.
		iNet.iwebos.ui.common.tree.TreeNodeExUI.superclass.onSelectedChange.call(this, state) ;		
	}
}) ;
