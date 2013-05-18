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
 * @class iNet.iwebos.ui.mail.TreeMailbox
 * @extends Ext.TreePanel
 */
iNet.iwebos.ui.mail.TreeMailbox = function() {
  this.__owner = {
    path: '',
    allowLoad: true
  };
  this.folderTemp = [];
  var __baseParams = {
    iwct: 'loadMailFolderContent',
    iwm: 'MAIL',
    iwc: 'READ_ONLY',
    iwa: 'build',
    action: 'build'
  };
  this.treeroot = new Ext.tree.AsyncTreeNode({
    text: iwebos.message.mail.title_mailbox,
    draggable: false,
    id: -1
  });
  
  // construction of this class to be started
  iNet.iwebos.ui.mail.TreeMailbox.superclass.constructor.call(this, {
    id: 'mail-tree-folder-id',
    ddGroup: 'Mail-drag-drop-group',
    useArrows: true,
    autoScroll: true,
    animate: true,
    rootVisible: false,
    lines: true,
    enableDrop: true,
    frame: false,
    collapsible: true,
    containerScroll: true,
    loader: new iNet.iwebos.ui.common.tree.TreeLoaderWrapper({
      dataUrl: 'jsonajax',
      baseParams: __baseParams
    }),
    root: this.treeroot
  });
  
  this.getSelectionModel().on({
    'beforeselect': function(sm, node) {
      if (node.id > 0) {
        return true;
      }
      else {
        return (node.id = -2);
      }
      return false;
    },
    'selectionchange': this._fnLoadmail.createDelegate(this)
  });
  
  this.on('contextmenu', this.onContextMenu, this);
  this.on('expandnode', this.onExpandNode, this);
  
};
Ext.extend(iNet.iwebos.ui.mail.TreeMailbox, Ext.tree.TreePanel, {
  init: function() {
    this.render();
    this.root.expand();
    
  },
  treeResize: function() {
    var __load = true;
    var component = Ext.getCmp('mail-tree-folder-id');
    __load = component.__owner.load;
    component = Ext.getCmp('action-panel');
    var __total = component.getSize().height;
    component = Ext.getCmp('mail-task-n-search');
    var __task = component.getSize().height;
    component = Ext.getCmp('mail-tree-folder-panel');
    if (__load) {
      component.setHeight(__total - __task - 8);
    }
    else {
      component.setHeight(__total - __task - 10);
      component = Ext.getCmp('mail-tree-folder-id');
      component.__owner.load = true;
    }
    Ext.EventManager.fireWindowResize();
    
  },
  afterRender: function() {
    iNet.iwebos.ui.mail.TreeMailbox.superclass.afterRender.call(this);
    this.treeResize();
    var component = Ext.getCmp('main-tabs');
    component.on('resize', this.treeResize, this);
  },
  
  _fnLoadmail: function(sm, node) {
    if (!node) 
      return;
    
    if (!this.__owner.allowLoad) {
      this.__owner.allowLoad = true;
      return;
    }
    
    if (node.id > 0) {
      // save Node path
      this.__owner.path = node.getPath();
      
      var __panel = Ext.getCmp('email-message-grid-id');
      var __mainTab = Ext.getCmp('main-tabs');
      var __mailBoxPanel = Ext.getCmp('main-mailbox');
      
      // set active mail tab
      __mainTab.setActiveTab(0);
      __mailBoxPanel.setTitle(node.attributes.original);
      
      var __store = __panel.store;
      var __params = __store.baseParams;
      __params['id'] = node.id;
      __params['mode'] = 'basic';
      __params['field'] = 'all';
      __store.baseParams = __params;
      this.folder = node.id;
      __store.load({
        params: {
          start: 0,
          limit: iNet.INET_PAGE_LIMIT
        }
      });
      
      var __type = node.attributes.type;
      if (__type == 'SENT' ||
      __type == 'DRAFT') {
        __mainTab.changeColumn(false);
      }
      else 
        if (__type == 'SPAM') {
          __mainTab.changeSpamButton(false);
        }
        else {
          __mainTab.changeColumn(true);
          __mainTab.changeSpamButton(true);
        }
      
      // update label filder in mail toolbar
      __mainTab.adSearch.checkSearchAll();
    }
    else 
      if (node.id === -2) { // contact node
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('main-addressbook-panel');
        if (!__tab) {
          __tab = new iNet.iwebos.ui.mail.AddressBookPanel();
          __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
        
        var __path = this.__owner.path;
        if (this.__owner.path == '') {
          var __inbox = this.getInboxFolder();
          var __inboxNode = this.getNodeById();
          
          // node is exist ????
          if (!__inboxNode) 
            return;
          __path = __inboxNode.getPath();
          this.__owner.path = __path;
        }
        
        // set false to load-flag
        this.__owner.allowLoad = false;
        this.selectPath(__path);
      }
  },
  
  init: function() {
    this.render();
    this.root.expand();
  },
  
  /**
   * menu context
   * @param {} node
   * @param {} e
   */
  onContextMenu: function(node, event) {
    event.stopEvent();
    // create context menu on first right click
    var __type = node.attributes.type;
    if (__type === undefined) 
      return;
    var __create = (__type == 'INBOX' || __type == 'DRAFT' ||
    __type == 'CUSTOM' ||
    __type == 'TRASH');
    var __delete = (__type == 'CUSTOM');
    var __filter = (__type == 'CUSTOM' || __type == 'INBOX');
    var __empty = true;
    var __markRead = true;
    Ext.apply(this.__owner, node.attributes);
    
    if (__create || __delete || __filter || __empty || __markRead) {
      var __items = [];
      // create new folder
      if (__create) {
        __items.push({
          text: iwebos.message.mail.create_folder,
          iconCls: 'icon-email-folder-new',
          scope: this,
          handler: this._createFolder
        });
      }
      
      // delete folder folder menu and rename folder menu
      if (__delete) {
        __items.push({
          text: iwebos.message.mail.delete_folder,
          iconCls: 'icon-email-folder-delete',
          scope: this,
          handler: this._deleteFolder
        });
        
        __items.push({
          text: iwebos.message.mail.rename_folder,
          iconCls: 'icon-email-clean',
          scope: this,
          handler: this._renameFolder
        });
      }
      // mask as read
      
      if (__markRead) {
        __items.push({
          text: iwebos.message.mail.context_mark_read,
          iconCls: 'icon-email-read_mail',
          scope: this,
          handler: this._fnMarkAsRead
        });
      }
      
      // filter menu
      if (__filter) {
        __items.push({
          text: iwebos.message.mail.filter_create,
          iconCls: 'icon-email-filter',
          scope: this,
          handler: this._openDialogFilter
        });
      }
      
      // empty trash folder
      if (__empty) {
        __items.push({
          text: iwebos.message.mail.context_empty_folder,
          iconCls: 'icon-email-clean',
          scope: this,
          handler: this._fnEmtyFolder
        });
      }
      
      
      // create menu
      var __menu = new Ext.menu.Menu({
        id: 'tree-ctx',
        items: __items
      });
      
      __menu.showAt(event.getXY());
    }
  },
  
  /**
   * handle tree expand node
   * @param {TreeNode} node
   */
  onExpandNode: function(node) {
    if (this.folderTemp.length === 0) {
      return;
    }
    var __nodes = node.childNodes;
    
    for (var __index = 0; __index < __nodes.length; __index++) {
      var __child = __nodes[__index];
      for (var __indexTemp = 0; __indexTemp < this.folderTemp.length; __indexTemp++) {
        var __folder = this.folderTemp[__indexTemp];
        if (!__folder.update && __child.attributes.id === __folder.id) {
          this.addMessageUnread2Folder(__folder.message, __folder.id);
          // mark field update is true 
          this.folderTemp[__indexTemp].update = true;
        }
      }
    }
  },
  
  /**
   * update folder id
   * @param {} id
   * @param {} unread
   */
  updateUnreadFolder: function(id, unread) {
    var __node = this.getNodeById(id);
    if (__node) {
      __node.attributes.unread = unread;
      __node.setText(__node.attributes.original + (unread > 0 ? '(' + unread + ')' : ''));
      __node.attributes.cls = unread > 0 ? 'mail-folder-unread' : '';
    }
  },
  
  /**
   * @private
   * create new folder
   */
  _createFolder: function() {
    var __owner = this.__owner;
    var __panel = this;
    var __fnOkHandler = function() {
      // get new name
      var __newName = this.getData();
      // handler success submit
      var __onSuccess = function(response, request) {
        var __result = Ext.util.JSON.decode(response.responseText);
        // get node rename
        if (!!__result) {
          var __newId = __result.id;
          // id not exist
          if (!__newId) 
            return;
          
          var __node = __panel.getNodeById(__owner.id);
          if (!!__node) {
            // node is leaf
            if (__node.leaf) {
              __node.leaf = false;
              __node.child = [];
            }
            
            // create new node
            var __newNode = new Ext.tree.TreeNode({
              id: __newId,
              parent: __owner.id,
              type: 'CUSTOM',
              unread: 0,
              iconCls: 'icon-email-custom',
              original: __newName,
              text: __newName,
              leaf: true
            });
            // add new node to parent node
            __node.appendChild(__newNode);
            
            // expand parent node
            __node.expand();
          }
        }
      };
      
      
      if (__newName.length > 0) {
        var __baseParams = {
          iwct: 'mailFolderContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'create',
          action: 'create',
          text: __newName,
          parent: __owner.id
        };
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          success: __onSuccess,
          failure: __onSuccess,
          method: 'POST',
          params: __baseParams,
          scope: this
        });
        this.destroy();
      }
      else {
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_blank_folder_name,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      }
      
    };
    
    var __dlg = new iNet.iwebos.ui.mail.DialogCreateFolder(this, {
      fnOkHandler: __fnOkHandler,
      parentName: __owner.original
    });
    __dlg.show(__dlg);
  },
  
  /**
   * @private
   * delete folder
   */
  _deleteFolder: function() {
    var __owner = this.__owner;
    var __panel = this;
    var __onSuccess = function(response, request) {
      var __result = Ext.util.JSON.decode(response.responseText);
      // successful.
      if (__result.success) {
        var __node = __panel.getNodeById(__owner.id);
        if (!!__node) {
          __node.remove();
        }
      }
    };
    
    var __okHandler = function(btn) {
      if (btn == 'yes' || btn == 'ok') {
        var __baseParams = {
          iwct: 'mailFolderContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'delete',
          action: 'delete',
          id: __owner.id
        };
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          success: __onSuccess,
          failure: __onSuccess,
          method: 'POST',
          params: __baseParams,
          scope: this
        });
      }
    };
    // show confirmation.
    Ext.MessageBox.confirm(iwebos.message.mail.announcement, iwebos.message.mail.confirm_delete_folder, __okHandler.createDelegate(this));
  },
  
  /**
   * @private
   * rename folder
   */
  _renameFolder: function() {
    var __owner = this.__owner;
    var __panel = this;
    var __okHandlerDlg = function() {
      // get new name
      var __newName = this.getData();
      // handler success submit
      var __onSuccess = function(response, request) {
        var __result = Ext.util.JSON.decode(response.responseText);
        if (__result !== undefined && __result.success) {
          // get node rename
          var __node = __panel.getNodeById(__owner.id);
          if (!!__node) {
            __node.setText(__newName);
          }
        }
      };
      
      if (__newName.length > 0) {
        var __baseParams = {
          iwct: 'mailFolderContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'rename',
          action: 'rename',
          text: __newName,
          id: __owner.id
        };
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          success: __onSuccess,
          failure: __onSuccess,
          method: 'POST',
          params: __baseParams,
          scope: this
        });
        this.destroy();
      }
      else {
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_blank_folder_name,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      }
    };
    
    this.dlg = new iNet.iwebos.ui.mail.DialogRenameFolder(this, {
      okHandler: __okHandlerDlg,
      folderName: __owner.text
    });
    this.dlg.show(this.dlg);
  },
  
  /**
   * @public
   * set current folder identifier
   * @param {} id
   */
  setFolder: function(folders) {
    this.folder = folders.inbox;
  },
  /**
   * @public
   * get current folder identifier
   * @return {long}
   */
  getFolder: function() {
    if (!this.folder) {
      this.folder = this.getInboxFolder();
    }
    
    return this.folder;
  },
  
  /**
   * get inbox folder
   * @return {}
   */
  getInboxFolder: function() {
    var __mainPanel = Ext.getCmp('main-tabs');
    var __config = __mainPanel.config || {};
    var __folder = __config.folder;
    // folder undefined
    if (__folder === undefined) {
      return 0;
    }
    return __folder.inbox;
  },
  
  /**
   * @public update INBOX folder
   * @param {}
   *            unread
   */
  updateInbox: function(unread) {
    var root = this.getRootNode();
    var inboxNode = root.childNodes[0];
    if (inboxNode) {
      this.updateUnreadFolder(inboxNode.id, unread);
    }
  },
  
  /**
   * @public
   * read email
   */
  readMail: function(total) {
    var __currentFolder = this.getFolder();
    
    if (__currentFolder === 0) 
      return;
    
    // get node by id
    var __node = this.getNodeById(__currentFolder);
    if (__node) {
      // asc unread
      var __unread = __node.attributes.unread - total;
      __node.attributes.unread = (__unread < 0) ? 0 : __unread;
      __node.attributes.cls = __unread > 0 ? 'mail-folder-unread' : '';
      
      if (__unread <= 0) {
        __node.setText(__node.attributes.original);
        __node.getUI().removeClass('mail-folder-unread');
      }
      else {
        __node.setText(__node.attributes.original + ' (' + __unread + ')');
        __node.getUI().addClass('mail-folder-unread');
      }
    }
  },
  
  
  
  /**
   * add new email to inbox
   */
  addNewMessage: function() {
    var __inboxFolder = this.getInboxFolder();
    
    // get node by id
    var __node = this.getNodeById(__inboxFolder);
    if (__node) {
      var __unread = __node.attributes.unread + 1;
      __node.attributes.unread = __unread;
      if (__unread <= 0) {
        __node.setText(__node.attributes.original);
        __node.getUI().removeClass('mail-folder-unread');
      }
      else {
        __node.setText(__node.attributes.original + ' (' + __unread + ')');
        __node.getUI().addClass('mail-folder-unread');
      }
      __node.attributes.cls = __unread > 0 ? 'mail-folder-unread' : '';
    }
  },
  
  /**
   * update tree node new message unread
   */
  addMessageUnread: function(total) {
  
    var __currentFolder = this.getFolder();
    
    this.addMessageUnread2Folder(total, __currentFolder);
  },
  
  /**
   * add total new message to folder
   * @param {int} total
   * @param {int} folder
   */
  addMessageUnread2Folder: function(total, folder) {
    // get node by id
    var __node = this.getNodeById(folder);
    if (__node) {
      var __unread = __node.attributes.unread + total;
      __node.attributes.unread = __unread < 0 ? 0 : __unread;
      if (__unread <= 0) {
        __node.setText(__node.attributes.original);
        __node.getUI().removeClass('mail-folder-unread');
      }
      else {
        __node.setText(__node.attributes.original + ' (' + __unread + ')');
        __node.getUI().addClass('mail-folder-unread');
      }
      __node.attributes.cls = __unread > 0 ? 'mail-folder-unread' : '';
    }
    else {
      this._fnPutUnreadMessage2Temp(total, folder);
    }
  },
  
  _fnPutUnreadMessage2Temp: function(total, folder) {
    if (this.folderTemp.length === 0) {
      this.folderTemp[0] = {
        id: folder,
        message: total,
        update: false
      };
    }
    else {
      var __folders = this.folderTemp;
      for (var __index = 0; __index < __folders.length; __index++) {
        if (__folders[__index].id === folder) {
          __folders[__index].message = __folders[__index].message + total;
          return;
        }
      }
      // folder is not exist
      __folders[__folders.length] = {
        id: folder,
        message: total,
        update: false
      };
    }
  },
  
  /**
   * reload mail folder
   */
  reloadMailBox: function() {
    this.treeroot.reload();
  },
  
  /**
   * Delete all message in folder
   */
  _fnEmtyFolder: function() {
    var __owner = this.__owner;
    var __panel = this;
    var __grid = Ext.getCmp('email-message-grid-id');
    var __onSuccess = function(response, request) {
      if (response) {
        var __result = Ext.util.JSON.decode(response.responseText);
        // successful.
        if (__result != undefined && __result.success) {
          // update unread 
          this.updateUnreadFolder(__owner.id, 0);
          // update grid: remove all message
          if (this.getFolder() == __owner.id) {
            __grid.store.removeAll();
          }
          return;
        }
      }
      // show error.
      MailService.showError(iwebos.message.mail.error_delete_mail);
    };
    
    var __okHandlerDelete = function(btn) {
      if (btn == 'yes' || btn == 'ok') {
        var __baseParams = {
          iwct: 'mailHeaderContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'emptyFolder',
          action: 'emptyFolder',
          id: __owner.id
        };
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          success: __onSuccess,
          failure: __onSuccess,
          method: 'POST',
          params: __baseParams,
          scope: this,
          maskEl: __grid.bwrap
        });
      }
    };
    
    var __okHandlerMove = function(btn) {
      if (btn == 'yes' || btn == 'ok') {
        var __baseParams = {
          iwct: 'mailHeaderContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'emptyFolder',
          action: 'emptyFolder',
          id: __owner.id
        };
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          success: __onSuccess,
          failure: __onSuccess,
          method: 'POST',
          params: __baseParams,
          scope: this,
          maskEl: __grid.bwrap
        });
      }
    };
    
    // show confirmation.
    Ext.MessageBox.confirm(iwebos.message.mail.announcement, iwebos.message.mail.confirm_empty_folder, __okHandlerDelete.createDelegate(this));
  },
  
  /**
   * open filter manager dialog
   */
  _openDialogFilter: function() {
    var __dlgFilter = new iNet.iwebos.ui.mail.DialogMailFilter();
    __dlgFilter.show();
  },
  
  /**
   * mask read folder
   */
  _fnMarkAsRead: function() {
    var __owner = this.__owner;
    var __panel = this;
    var __grid = Ext.getCmp('email-message-grid-id');
    var __onSuccess = function(response, request) {
      if (response) {
        var __result = Ext.util.JSON.decode(response.responseText);
        // successful.
        if (__result != undefined && __result.success) {
          // update unread 
          this.updateUnreadFolder(__owner.id, 0);
          // update grid: remove all message
          if (this.getFolder() == __owner.id) {
            __grid.store.reload();
          }
          return;
        }
      }
      // show error.
      MailService.showError(iwebos.message.mail.error_mark_as_read_folder);
    };
    
    
    var __baseParams = {
      iwct: 'mailHeaderContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'markAsReadFolder',
      action: 'markAsReadFolder',
      id: __owner.id
    };
    
    // send request to server.
    iNet.Ajax.request({
      url: 'jsonajax',
      success: __onSuccess,
      failure: __onSuccess,
      method: 'POST',
      params: __baseParams,
      scope: this,
      maskEl: __grid.bwrap
    });
  }
});
