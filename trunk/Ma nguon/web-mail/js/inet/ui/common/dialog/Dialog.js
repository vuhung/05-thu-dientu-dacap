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
 * @class Dialog
 * @extends Ext.Window
 * 
 * The special dialog that allows user can drag dialog with content or not.
 * @constructor {Object} config - the given dialog configuration.
 */
iNet.iwebos.ui.common.dialog.Dialog = Ext.extend(Ext.Window,{
	/**
	 * @cfg {boolean} dragContent
	 * 
	 * Support dragging with copy content.
	 */
	 dragContent:true,
	 
	 /**
	  * private function.
	  * 
	  * This function initialization dialog with your configuration and will
	  * be called by container.
	  */
	 initComponent:function(){
	 	// call super class function.
	 	iNet.iwebos.ui.common.dialog.Dialog.superclass.initComponent.call(this) ;
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
						
		// dragging content.
		if(this.dragContent){
			el.appendChild(this.bwrap.dom.cloneNode(true)) ;			
		}else{ 
		   // only drag dialog frame.
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

// register dialog.
Ext.reg('dialog', iNet.iwebos.ui.common.dialog.Dialog) ;
