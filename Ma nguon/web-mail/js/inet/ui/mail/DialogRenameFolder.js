/***
 Copyright 2008 by Nguyen Hoang Tu (nhtu@truthinet.com.vn)
 Licensed under the iNet Solutions Corp.,;
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.truthinet.com/licenses
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
/**
 * @class iNet.iwebos.ui.mail.DialogRenameFolder
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogRenameFolder
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogRenameFolder = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    
    this.folderName = this.folderName || '';
    
    this.text = new Ext.form.FormPanel({
        region: 'center',
        bodyStyle: 'padding:5px 5px 0',
        frame: true,
        border: false,
        anchor: '100%',
        items: [{
            columnWidth: 0.9,
            border: false,
            layout: 'anchor',
            anchor: '100%',
            items: [{
                id: 'mail-folder-rename-id',
                xtype: 'textfield',
                value: this.folderName,
                border: false,
                anchor: '100%'
            }]
        }]
    });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogRenameFolder.superclass.constructor.call(this, {
        id: 'mail-renamefolder',
        title: iwebos.message.mail.dialog_renamefolder,
        iconCls: 'icon-email-createfolder',
        region: 'center',
        anchor: '100%',
        width: 400,
        height: 250,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
        buttons: [{
            text: iwebos.message.mail.dialog_renamefolder_btn_ok,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.dialog_renamefolder_btn_cancel,
            iconCls: 'cancel-btn',
            handler: this.cancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogRenameFolder, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.dialog_renamefolder_ttitle,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.dialog_renamefolder_btitle,
	
    /**
     * get new folder name
     * @return {string}
     */
    getData : function(){
    	// get value.
		var __newName = Ext.getCmp('mail-folder-rename-id').getValue();
		return __newName;
    },
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});
