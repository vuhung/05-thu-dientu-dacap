/**
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
 * Email Message view
 */
Ext.onReady(function() {
  Ext.QuickTips.init();
  Ext.form.Field.prototype.msgTarget = 'side';
  InitContext();
  /**
   * email message panel.
   */
  var __emailPanel = new iNet.iwebos.ui.mail.grid.MailViewPanel();
  
  var __treeFolder = new iNet.iwebos.ui.mail.TreeMailbox();
  
  var __treeMailbox = new Ext.Panel({
    id: 'mail-tree-folder-panel',
    frame: true,
    autoScroll: true,
    layout: 'form',
    border: true,
    items: [__treeFolder]
  });
  
  var __panelSearch = new iNet.iwebos.ui.mail.MailSearchPanel({
    store: __emailPanel.grid.store,
    height: 25
  });
  
  // create task by panel.
  var __taskAction = new Ext.Panel({
    id: 'mail-task-n-search',
    title: iwebos.message.paperwork.task,
    frame: false,
    contentEl: 'task-action',
    titleCollapse: true,
    collapsible: true,
    border: false
  });
  
  var __taskNSearch = new Ext.Panel({
    frame: true,
    border: true,
    items: [__taskAction, __panelSearch]
  });
  /**
   * Action panel.
   */
  var __actionPanel = new Ext.Panel({
    region: 'west',
    id: 'action-panel',
    split: true,
    collapsible: true,
    collapseMode: 'mini',
    width: 220,
    minWidth: 220,
    border: false,
    baseCls: 'x-plain',
    items: [__taskNSearch, __treeMailbox]
  });
  __actionPanel.on('resize', function(panel, adjWidth, adjHeight, rawWidth, rawHeight) {
    // search width
    var __cmp = Ext.getCmp('mail-search-panel-id');
    if (__cmp) 
      __cmp.setWidth(rawWidth - 17);
    
    var __cmp = Ext.getCmp('mail-search-field-id');
    if (__cmp) 
      __cmp.setWidth(rawWidth - 53);
    
  }, {
    stopEvent: true
  }, this);
  
  __panelSearch.on('search', __emailPanel.searchAdvance, __emailPanel, {
    stopEvent: true
  });
  /**
   * create search view port.
   */
  var __viewport = new Ext.Viewport({
    layout: 'border',
    items: [new iNet.iwebos.ui.paperwork.Header(), __actionPanel, __emailPanel]
  });
  
  var __actionPanelBody = __actionPanel.body;
  
  __actionPanelBody.on('mousedown', __emailPanel.doAction, __emailPanel, {
    delegate: 'a'
  });
  __actionPanelBody.on('click', Ext.emptyFn, null, {
    delegate: 'a',
    preventDefault: true
  });
  
  // droptarget on tree
  var __drop = new Ext.dd.DropTarget(__treeFolder.body, {
    ddGroup: 'Mail-drag-drop-group',
    dropAllowed: 'x-dd-drop-ok',
    notifyOver: function(dd, e, data) {
      var __allow = (e.target && e.target.tagName == 'SPAN') ? true : false;
      if (!__allow) 
        return __allow;
      
      var __comp = Ext.getCmp('mail-tree-folder-id');
      var __root = __comp.getRootNode();
      if (__root) {
        var __node = MailService.findTreeNodeByTarget(__root, e.target);
        var __currentFolder = __comp.getFolder();
        if (__node.attributes.id == __currentFolder ||
        __node.attributes.type == 'OUTBOX' ||
        __node.attributes.type == 'SENT' ||
        __node.attributes.type == 'CONTACT') {
          return '';
        }
        e.node = __node;
      }
      
      return 'x-dd-drop-ok';
    },
    
    notifyDrop: function(source, e, data) {
      if (!!e.flag) {
        e.flag = false;
        return false;
      }
      else {
        e.flag = true;
      }
      
      var __node = e.node;
      if (!__node) {
        return false;
      }
      
      // it does not exist
      if (__node == null) {
        return false;
      }
      
      var __comp = Ext.getCmp('mail-tree-folder-id');
      // node is current folder ?? 
      var __currentFolder = __comp.getFolder();
      if (__node.attributes.id == __currentFolder) {
        return false;
      }
      
      var __grid = data.grid;
      var __selects = __grid.getSelectionModel().getSelections();
      var __ids = [];
      if (__selects !== undefined && __selects.length > 0) {
        for (var __index = 0; __index < __selects.length; __index++) {
          __ids.push(__selects[__index].data.id);
        }
      }
      else {
        return false;
      }
      var __listid = __ids.join(';');
      
      // check has length
      if (__listid == null || __listid == '') 
        return false;
      
      var __folder = __node.attributes.id
      var __baseParams = {
        iwct: 'mailHeaderContent',
        iwm: 'MAIL',
        iwc: 'READ_ONLY',
        iwa: 'move',
        id: __folder,
        listid: __listid,
        action: 'move'
      };
      
      
      var __handlerFn = function(response, options) {
        var result = Ext.util.JSON.decode(response.responseText);
        
        if (result != undefined) {
          var __success = (result.success != undefined) && (result.success);
          if (__success) {
            // get removed item.
            var __removeItems = this.getSelectionModel().getSelections();
            var __store = this.store;
            var __unread = 0;
            
            var __folders = Ext.getCmp('main-tabs').config.folder;
            var __isTrash = (__folders.trash == __folder);
            
            var __cmpFollowUp = Ext.getCmp('mail-follow-up-ctx-id');
            var __ids = [];
            // count mail unread
            for (var __index = 0; __index < __removeItems.length; __index++) {
              var __record = __removeItems[__index];
              __store.remove(__record);
              if (__record.data.read == 'N') {
                __unread++;
              }
              
              // collect  follow up which will delete
              if (__isTrash) {
                var __followUp = __cmpFollowUp._findByData(__record.data.id);
                if (__followUp != null) {
                  __ids[__ids.length] = __followUp.data.id;
                }
              }
            }
            
            if (__ids.length > 0) {
              // remove follow up 
              __cmpFollowUp._fnDelete(__ids);
            }
            
            // if unread > 0 then move unread to des folder
            if (__unread > 0) {
            
              __comp.readMail(__unread);
              
              __comp.addMessageUnread2Folder(__unread, __folder);
            }
            
            // if mail move to spam folder then add filter spam
            if (__node.attributes.type == 'SPAM') {
              //__addSpam(__node.attributes.id);
              MailService.addSpam(__node.attributes.id, __selects);
            }
            var __mainTab = Ext.getCmp('main-tabs');
            if (__mainTab.isSpamFolder()) {
              MailService.removeSpam(__selects);
            }
            
            //return true;
            return false;
          }
        }
        
        // show error.
        MailService.showError(iwebos.message.mail.error_task);
      };
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: __handlerFn,
        failure: __handlerFn,
        method: 'POST',
        scope: __grid,
        params: __baseParams,
        removeItems: __selects,
        maskEl: __grid.bwrap
      });
      
      return false;
    }
  });
});
