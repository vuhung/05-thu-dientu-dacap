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
 * @class iNet.iwebos.ui.mail.DialogTreeMailBox
 */
iNet.iwebos.ui.mail.DialogTreeMailBox = function() {
  var __treeroot = new Ext.tree.AsyncTreeNode({
    text: iwebos.message.mail.title_mailbox,
    draggable: false,
    id: -1
  });
  var __baseParams = {
    iwct: 'mailFolderContent',
    iwm: 'MAIL',
    iwc: 'READ_ONLY',
    iwa: 'build',
    action: 'build'
  };
  // construction of this class to be started
  iNet.iwebos.ui.mail.DialogTreeMailBox.superclass.constructor.call(this, {
    id: 'mailbox-id',
    useArrows: true,
    autoScroll: true,
    animate: true,
    enableDD: true,
    draggable: true,
    containerScroll: true,
    loader: new iNet.iwebos.ui.common.tree.TreeLoaderWrapper({
      dataUrl: 'jsonajax',
      baseParams: __baseParams
    }),
    root: __treeroot
  });
};
Ext.extend(iNet.iwebos.ui.mail.DialogTreeMailBox, Ext.tree.TreePanel, {
  loadmail: function(sm, node) {
    if (node) {
      var __panel = Ext.getCmp('emailMessageId');
      var __store = __panel.store;
      var __params = __store.baseParams;
      __params['id'] = node.id;
      __store.baseParams = __params;
      __store.load({
        params: {
          start: 0,
          limit: iNet.INET_PAGE_LIMIT
        }
      });
    }
  },
  
  init: function() {
    this.render();
    this.root.expand();
  }
});

/**
 * @class iNet.iwebos.ui.mail.DialogCreateFolder
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateFolder
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogCreateFolder = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  
  this.parentName = this.parentName || '';
  this.text = new Ext.form.FormPanel({
    region: 'center',
    bodyStyle: 'padding:5px 5px 0',
    frame: true,
    border: false,
    anchor: '100%',
    items: [{
      border: false,
      layout: 'form',
      anchor: '100%',
      items: [{
        id: 'mail-folder-parent-text-id',
        xtype: 'textfield',
        fieldLabel: iwebos.message.mail.folder_parent,
        value: this.parentName,
        readOnly: true,
        border: false,
        anchor: '100%',
        indexTab: 100
      }, {
        id: 'mail-folder-text-id',
        xtype: 'textfield',
        fieldLabel: iwebos.message.mail.folder_name,
        border: false,
        anchor: '100%',
        indexTab: 101,
        listeners: {
          specialkey: function(field, e) {
            if (e.getKey() == Ext.EventObject.ENTER) {
              this.okHandler.apply(this.okhScope);
            }
          },
          scope: this
        }
      }]
    }]
  });
  
  
  this.text.on('render', function(panel) {
    Ext.getCmp('mail-folder-text-id').focus(true, 1000);
  }, this);
  
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  this.cancelhScope = this.cancelhScope || this;
  
  
  iNet.iwebos.ui.mail.DialogCreateFolder.superclass.constructor.call(this, {
    id: 'mail-createfolder',
    title: iwebos.message.mail.dialog_createfolder,
    iconCls: 'icon-email-createfolder',
    region: 'center',
    anchor: '100%',
    width: 400,
    height: 270,
    modal: true,
    frame: true,
    layout: 'border',
    hideMode: 'offsets',
    items: [this.text],
    buttons: [{
      text: iwebos.message.mail.dialog_createfolder_btn_ok,
      iconCls: 'ok-btn',
      handler: this.okHandler,
      scope: this.okhScope,
      indexTab: 102
    }, {
      text: iwebos.message.mail.dialog_createfolder_btn_cancel,
      iconCls: 'cancel-btn',
      handler: this.cancelHandler,
      scope: this.cancelhScope,
      indexTab: 103
    }]
  });
};
Ext.extend(iNet.iwebos.ui.mail.DialogCreateFolder, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
  ttitle: iwebos.message.mail.dialog_createfolder_ttitle,
  /**
   * bottom title.
   */
  btitle: iwebos.message.mail.dialog_createfolder_btitle,
  
  /**
   * handle cancel button.
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  },
  getData: function() {
    // get value.
    var __folderName = Ext.getCmp('mail-folder-text-id').getValue();
    
    return __folderName;
  }
});
