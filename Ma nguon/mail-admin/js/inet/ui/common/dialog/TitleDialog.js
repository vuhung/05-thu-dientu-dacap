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
 * @class TitleDialog
 * @extends iNet.iwebos.ui.common.dialog.Dialog
 * 
 * The specialized dialog that has the title at the top.
 * @constructor
 * @param {Object} config the config object.
 */
iNet.iwebos.ui.common.dialog.TitleDialog=Ext.extend(iNet.iwebos.ui.common.dialog.Dialog,{
	/**
	 * @cfg {String} ttitle
	 * 
	 * The top title, this value will be displayed at the top of title panel.<br/>
	 * The default value is null.
	 */	
	/**
	 * @cfg {String} btitle
	 * 
	 * The bottom title, this value will be displayed at the bottom of title panel. <br/>
	 */		
	/**
	 * @cfg {String} tcls
	 * 
	 * @the top text stylesheet class.
	 */
	/**
	 * @cfg {String} bcls
	 * 
	 * The bottom text stylesheet class.
	 */
	/**
	 * @cfg {String} hdImgCls
	 * 
	 * The header image stylesheet class.
	 */
	/**
	 * @cfg {String} hdCls
	 * 
	 * The title dialog header stylesheet class.
	 */			 
	/**
	 * private function.
	 * 
	 * This function used to initialization the dialog and will be called automatically 
	 * by container.
	 */
	initComponent : function(){
		// recalculate dialog height.
		if(typeof this.height == 'number'){
			this.height = this.height - iNet.INET_TITLE_DLG_HEIGHT;			
		}
		
		// call superclass function.
		iNet.iwebos.ui.common.dialog.TitleDialog.superclass.initComponent.call(this) ;		
	},
	
	/**
	 * private function.
	 * 
	 * Render the dialog.
	 * @param {Object} ct - the control to be render.
	 * @param {Object} position - the position where dialog display.
	 */
	onRender: function(ct, position){
		// call superclass function.
		iNet.iwebos.ui.common.dialog.TitleDialog.superclass.onRender.call(this,ct, position);
		
		// prepare title data.
		var buf = [		
			'<table class="x-dlg-title-hd ',(this.hdCls ? this.hdCls : ''),'" border="0" cellspacing="0" cellpading="0" style="width:100%;">',
				'<tr>',
					'<td class="x-dlg-title-inner-hd-txt">',
						'<div class="x-dlg-title-inner-hd-txt-ttext ',(this.tcls ? this.tcls : ''),'">',
							(this.ttitle ? this.ttitle : '&#160;'),
						'</div>',
						'<div class="x-dlg-title-inner-hd-txt-btext ',(this.bcls ? this.bcls : ''),'">',
							(this.btitle ? this.btitle : '&#160;'),
						'</div>',					
					'</td>',
					'<td class="x-dlg-title-inner-hd-img ', (this.hdImgCls ? this.hdImgCls : ''), '"/>',
				'</tr>',
			'</table>'	
		] ;				
						
		// create dialog title.
		this.dtitle = this.body.createChild(
				{
					tag: 'div',
					id: 'dlg-title',
					cls: 'x-dlg-title-hd-ec',
					html: buf.join("")
				},
				this.bwrap.dom
			);	
	},
	
    /**
     * Returns the height in pixels of the framing elements of this panel (including any top and bottom bars and
     * header and footer elements, but not including the body height).  To retrieve the body height see {@link #getInnerHeight}.
     * @return {Number} The frame height
     */
    getFrameHeight : function(){
		// call superclass function.
		var sHeight = iNet.iwebos.ui.common.dialog.TitleDialog.superclass.getFrameHeight.call(this) ;
		
		// the title exist?
		sHeight += (this.dtitle ? this.dtitle.getHeight() : 0) ;
		
		// return the real height.		
        return sHeight;
    },
	
	/**
	 * destroy the element before destroy the dialog.
	 */ 
	beforeDestroy : function(){
		// call superclass function.
		iNet.iwebos.ui.common.dialog.TitleDialog.superclass.beforeDestroy.call(this) ;
		
		// distroy dialog title.
		Ext.Element.uncache(this.dtitle) ;
	},
	
    /**
     * Create the ghost frame that uses dragging control.
     * 
     * @param {Object} cls - the given ghost class.
     * @param {boolean} useShim - use shim or not.
     * @param {Object} appendTo - the given object to be append to.
     */
    createGhost : function(cls, useShim, appendTo){
		// create the ghost element.
        var el = document.createElement('div');
        el.className = 'x-panel-ghost ' + (cls ? cls : '');
		
		// append header.
        if(this.header){
            el.appendChild(this.el.dom.firstChild.cloneNode(true));
        }
				
		// append dialog title.
        if(this.dtitle){
			// dragging content.
			if(this.dragContent){
				el.appendChild(this.dtitle.dom.cloneNode(true));	
			}else{
				// otherwise, drag dialog frame.
				Ext.fly(el.appendChild(document.createElement('ul'))).setHeight(this.dtitle.getHeight()) ;
			}
        }		
		
		// the user want to drag content.
		if(this.dragContent){
			el.appendChild(this.bwrap.dom.cloneNode(true)) ;			
		}else{ 
			// otherwise,only drag dialog frame.
			Ext.fly(el.appendChild(document.createElement('ul'))).setHeight(this.bwrap.getHeight());
		}
		
        // set the ghost width.		
		el.style.width = this.el.dom.offsetWidth + 'px';

		// append content.
        if(!appendTo){
            this.container.dom.appendChild(el);
        }else{
            Ext.getDom(appendTo).appendChild(el);
        }
		
		// dont create shadow and constrain.
        if(useShim !== false && this.el.useShim !== false){
            var layer = new Ext.Layer({shadow:false, useDisplay:true, constrain:false}, el);
            layer.show();
            return layer;
        }else{
            return new Ext.Element(el);
        }
    }
}) ;

// register the title dialog to system.
Ext.reg('tdialog', iNet.iwebos.ui.common.dialog.TitleDialog) ;
