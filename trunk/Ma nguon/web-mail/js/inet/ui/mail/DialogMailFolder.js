/***
 Copyright 2008 by Tan Truong (tntan@truthinet.com.vn)
 Licensed under the iNet Solutions Corp.,;
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.truthinet.com.vn/licenses
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
iNet.iwebos.ui.mail.DialogMailFolder = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    var __baseParams = {iwct:'loadMailFolderContent', iwm:'MAIL', iwc:'READ_ONLY', iwa:'build', action:'build'};
	this.treeroot =  new Ext.tree.AsyncTreeNode({
          					 text: iwebos.message.mail.title_mailbox,
           					 draggable:false,
							 expanded:true,
           					 id: -1
       					 });
    
    var __main = new Ext.tree.TreePanel({
			id: 'mail-dialog-tree-folder-panel-id',
	        useArrows: true,
	        autoScroll: true,
	        animate: true,
	        enableDD: true,
	        draggable: true,
	        ddScroll : true,
	        frame: false,
	        height : 200,
	        containerScroll: true,
	        loader: new iNet.iwebos.ui.common.tree.TreeLoaderWrapper({
	            dataUrl: 'jsonajax',
	            baseParams: __baseParams
	        }),
	        root: this.treeroot
        });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogMailFolder.superclass.constructor.call(this, {
        id: 'mail-dialog-mail-folder',
        title: 'Chọn thư mục',
        iconCls: 'icon-email-createfolder',
        region: 'center',
        anchor: '100%',
        width: 300,
        height: 400,
        modal: true,
        autoScroll: true,
        frame: true,
        layout: 'form',
        hideMode: 'offsets',
        items: [__main],
        buttons: [{
            text: iwebos.message.mail.select,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.close,
            iconCls: 'cancel-btn',
            handler: this.cancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogMailFolder, iNet.iwebos.ui.common.dialog.TitleDialog, {
    /**
     * no border.
     */
    bodyBorder: false,
    /**
     * do not drag content.
     */
    dragContent: false,
    /**
     * header image class.
     */
    hdImgCls: '',
    /**
     * top title.
     */
    //ttitle: 'Chọn thư mục cho bộ lọc',
    /**
     * bottom title.
     */
    //btitle: ''/*iwebos.message.mail.dialog_createfolder_btitle*/,

   /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    getData: function(){
    	// get value.
		var __panel = Ext.getCmp('mail-dialog-tree-folder-panel-id');
		var __treeNode = __panel.getSelectionModel().getSelectedNode();
		if(!!__treeNode){
			return __treeNode.attributes;
		}	
		return undefined;
	}
});
