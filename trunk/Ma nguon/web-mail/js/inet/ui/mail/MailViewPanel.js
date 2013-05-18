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
function newComposerWAddress(address) {
  var __compPanel = Ext.getCmp('main-tabs');
  if (!!__compPanel) {
    _divContext.style.display = 'none';
    __compPanel.composeWithAddress(address);
  }
}

function newContact(address) {
  var __compPanel = Ext.getCmp('main-tabs');
  if (!!__compPanel) {
    _divContext.style.display = 'none';
    __compPanel.addContact(address);
  }
}

/**
 *
 * @param {boolean} isCheck
 * @param {[]} errors
 */
function checkMailLoading(isCheck, errors) {
  var __getEmailAddress = function(account) {
    var __index = account.indexOf(':');
    if (__index == -1) 
      return account;
    
    return account.substring(__index + 1);
  };
  
  if (isCheck) {
    document.getElementById('action-check').className = 'task-loading';
  }
  else {
    var __compPanel = Ext.getCmp('main-tabs');
    
    if (__compPanel.tooltipError != null) {
      __compPanel.tooltipError.destroy();
    }
    
    if (errors.length > 0) {
      var __html = '';
      for (var i = 0; i < errors.length; i++) {
        __html = __html + __getEmailAddress(errors[i]) + '<br/>';
      }
      
      __compPanel.tooltipError = new Ext.ToolTip({
        id: 'mail-error-tooltip',
        target: 'action-check',
        html: __html,
        autoHeight: true,
        closable: true,
        title: iwebos.message.mail.check_mail_error_tootip,
        draggable: true,
        animCollapse: true,
        trackMouse: true
      });
      document.getElementById('action-check').className = 'icon-email-error';
    }
    else {
      document.getElementById('action-check').className = 'task-action-viewall';
    }
  }
}

Ext.grid.RowSelectionModel.prototype.rsmHandleMouseDown = Ext.grid.RowSelectionModel.prototype.handleMouseDown;
Ext.grid.CheckboxSelectionModel.override({
  handleMouseDown: function(g, rowIndex, e) {
  }
});
Ext.grid.RowSelectionModel.override({
  initEvents: function() {
    if (!this.grid.enableDragDrop && !this.grid.enableDrag) {
      this.grid.on("rowmousedown", this.rsmHandleMouseDown, this);
    }
    else { // allow click to work like normal
      this.grid.on("rowclick", function(grid, rowIndex, e) {
        var target = e.getTarget();
        if (target.className !== 'x-grid3-row-checker') {
          this.rsmHandleMouseDown(grid, rowIndex, e);
          grid.view.focusRow(rowIndex);
        }
      }, this);
    }
    
    this.rowNav = new Ext.KeyNav(this.grid.getGridEl(), {
      "up": function(e) {
        if (!e.shiftKey) {
          this.selectPrevious(e.shiftKey);
        }
        else 
          if (this.last !== false && this.lastActive !== false) {
            var last = this.last;
            this.selectRange(this.last, this.lastActive - 1);
            this.grid.getView().focusRow(this.lastActive);
            if (last !== false) {
              this.last = last;
            }
          }
          else {
            this.selectFirstRow();
          }
      },
      "down": function(e) {
        if (!e.shiftKey) {
          this.selectNext(e.shiftKey);
        }
        else 
          if (this.last !== false && this.lastActive !== false) {
            var last = this.last;
            this.selectRange(this.last, this.lastActive + 1);
            this.grid.getView().focusRow(this.lastActive);
            if (last !== false) {
              this.last = last;
            }
          }
          else {
            this.selectFirstRow();
          }
      },
      scope: this
    });
    
    var view = this.grid.view;
    view.on("refresh", this.onRefresh, this);
    view.on("rowupdated", this.onRowUpdated, this);
    view.on("rowremoved", this.onRemove, this);
  }
});

/**
 * Function Show - Hide Header mail
 *
 */
function showHeaderMail(val) {
  var imgID = document.getElementById('mail-head-down');
  var tblID = document.getElementById('header-mail-show');
  
  var tagDivShow = '<div class="mail-head-down" onclick="showHeaderMail(false);"></div>';
  var tagDivHide = '<div class="mail-head-right" onclick="showHeaderMail(true);"></div>';
  
  imgID.innerHTML = (true == val) ? tagDivShow : tagDivHide;
  tblID.style.display = (true == val) ? '' : 'none';
  Ext.EventManager.fireWindowResize();
}

function openConfigPanel() {
  var __compPanel = Ext.getCmp('main-tabs');
  if (__compPanel) 
    __compPanel.configAccount();
}

/**
 *
 *
 * @class iNet.iwebos.ui.mail.MailView
 * @extends Ext.TabPanel
 * @constructor
 * Creates a new TabPanel for email
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.grid.MailViewPanel = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  
  this.__owner = {};
  this.fetchInfo = {
    fetching: false,
    status: 0,
    total: 0,
    fetched: 0,
    accounts: [],
    errors: []
  };
  /*
   * Array the recipients data
   *
   */
  this.recipients = null;
  this.lastIndex = 0;
  this.config = {};
  this.tooltipError = null;
  //
  // initialization the grid.
  //
  var __sortInfo = {
    field: 'received',
    direction: 'DESC'
  };
  var __defaultSort = {
    field: 'received',
    direction: "DESC"
  };
  var __groupField = 'received';
  var __baseParams = {
    iwct: 'loadMailHeaderContent',
    iwm: 'MAIL',
    iwc: 'READ_ONLY',
    iwa: 'search',
    id: 0,
    action: 'search',
    mode: 'basic',
    field: 'all'
  };
  
  // email record.
  var __mailRecord = Ext.data.Record.create([{
    name: 'id',
    mapping: 'hid',
    type: 'int'
  }, {
    name: 'priority',
    mapping: 'priority',
    type: 'string'
  }, {
    name: 'sender',
    mapping: 'sender',
    type: 'string'
  }, {
    name: 'recipient',
    mapping: 'recipient',
    type: 'string'
  }, {
    name: 'subject',
    mapping: 'subject',
    type: 'string'
  }, {
    name: 'attached',
    mapping: 'attached',
    type: 'boolean'
  }, {
    name: 'size',
    mapping: 'size',
    type: 'string'
  }, {
    name: 'read',
    mapping: 'read',
    type: 'string'
  }, {
    name: 'sent',
    mapping: 'sent',
    type: 'int'
  }, {
    name: 'received',
    mapping: 'received',
    type: 'int'
  }, {
    name: 'flag',
    mapping: 'flag',
    type: 'string'
  }, {
    name: 'type',
    mapping: 'type',
    type: 'string'
  }, {
    name: 'contentId',
    mapping: 'contentId',
    type: 'string'
  }]);
  
  var __reader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'rows',
    id: 'id'
  }, __mailRecord);
  
  // create some plugins.
  var __priorityColumn = new iNet.iwebos.ui.common.grid.EmailImportantColumn();
  var __fileAttachment = new iNet.iwebos.ui.mail.grid.AttachmentColumn();
  var __statusColumn = new iNet.iwebos.ui.mail.grid.StatusColumn({});
  var __typeColumn = new iNet.iwebos.ui.mail.grid.TypeColumn();
  var __checkbox = new Ext.grid.CheckboxSelectionModel();
  // create column model.
  var __columnModel = new Ext.grid.ColumnModel([__checkbox, __priorityColumn, __statusColumn, __fileAttachment, __typeColumn, {
    header: iwebos.message.mail.grid_sender,
    dataIndex: 'sender',
    sortable: true,
    width: 150,
    renderer: function(value, cell) {
      return "<p style='white-space:normal'>" + value + "</p>";
    }
  }, {
    dataIndex: 'recipient',
    header: 'Người nhận',
    width: 150,
    hidden: true
  }, {
    header: iwebos.message.mail.grid_subject,
    dataIndex: 'subject',
    sortable: true,
    renderer: function(value, cell) {
      return "<p style='white-space:normal'>" + value + "</p>";
    }
  }, {
    dataIndex: 'received',
    header: iwebos.message.mail.filter_object_date_received,
    sortable: true,
    width: 110,
    fixed: true,
    renderer: iNet.dateRenderer('H:i d/m/Y'),
    groupRenderer: iNet.dateGroupRenderer()
  }, {
    header: iwebos.message.mail.grid_size,
    dataIndex: 'size',
    sortable: true,
    width: 70,
    fixed: true
  }]);
  
  // create filter by function, filter by status.
  var _filterFn = function(item, value) {
    return (item.data.status === value);
  };
  
  // show color in row.
  var __fnRowClass = function(record) {
    var __read = record.data.read;
    
    // email is mask read
    if (__read === 'Y') {
      return '';
    }
    else {
      return 'mail-grid-unread';
    }
  };
  
  // create email message grid.
  this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
    id: 'email-message-grid-id',
    ddGroup: 'Mail-drag-drop-group',
    region: 'center',
    anchor: '100%',
    autoScroll: true,
    url: 'jsonajax',
    method: 'POST',
    sortInfo: __sortInfo,
    baseParams: __baseParams,
    loadFirst: false,
    reader: __reader,
    enableDrag: true,
    groupLabel: iwebos.message.mail.grid_groupLabel,
    groupField: __groupField,
    rowClass: __fnRowClass,
    dsort: __defaultSort,
    cm: __columnModel,
    sm: __checkbox,
    filter: {
      data: 'all',
      fn: _filterFn
    },
    viewConfig: {
      forceFit: true
    },
    plugins: [__priorityColumn, __fileAttachment]
  });
  
  //when row is right clicked
  this.grid.on('contextmenu', this._onGridContext, this, {
    stopEvent: true
  });
  this.grid.on('rowcontextmenu', this._onGridRowContextMenu, this, {
    stopEvent: true
  });
  //when a row is double clicked
  this.grid.on('rowdblclick', this._onDoubleClick, this);
  this.grid.on('rowclick', function(grid, rowIndex, e) {
    var __records = grid.getSelectionModel().getSelections();
    
    if (__records.length > 1) 
      return;
    
    var __record = grid.store.getAt(rowIndex);
    var __hId = __record.data.id;
    
    if (this.lastIndex == __hId) {
      // update data 
      if (__record.data.read === 'N') {
        // mark read
        this._markRead('Y');
      }
      
      var __sPanel = Ext.getCmp('preview-mail-panel');
      // show preview panel
      if (!__sPanel.isVisible()) {
        __sPanel.setVisible(true);
        Ext.EventManager.fireWindowResize();
      }
    }
  }, this);
  
  this.grid.selModel.on("selectionchange", this._fnHandleFunction, this);
  this.grid.store.on('load', this._onStoreLoad, this);
  this.grid.store.on('remove', this._onStoreRemove, this);
  this.grid.store.on('add', this._fnOnStoreInsert, this);
  
  /**
   * create advancedsearch control.
   */
  this.adSearch = new iNet.iwebos.ui.mail.Toolbar({
    region: 'north',
    width: '100%',
    height: 30,
    frame: false,
    border: false,
    showmybutton: false,
    store: this.grid.store
  });
  
  this.adSearch.on('delete', this.onDelete, this);
  
  this.adSearch.on('reply', this.replyEmail, this);
  
  this.adSearch.on('replyall', this.replyAllEmail, this);
  
  this.adSearch.on('forward', this.forwardEmail, this);
  
  this.adSearch.on('spam', this.spamEmail, this);
  
  // process action when user click in action panel.
  this.actions = {
    'action-create': this._newEmail,
    'action-check': this._checkEmail.createDelegate(this),
    'action-config-account': this.configAccount,
    'action-address-book': this.openAddressBook
  };
  
  this.preViewHeader = new Ext.Panel({
    id: 'previewPanelHeader',
    autoHeight: true,
    region: 'north',
    cls: 'w3c',
    frame: true
  });
  this.preViewBody = new Ext.Panel({
    id: 'previewPanelBody',
    region: 'center',
    frame: false,
    cls: 'w3c',
    border: false,
    autoScroll: true
  });
  
  // create preview panel.
  this.south = new Ext.Panel({
    id: 'preview-mail-panel',
    region: 'south',
    layout: 'border',
    height: 50,
    split: true,
    border: true,
    autoScroll: true,
    baseCls: 'preview',
    collapseMode: 'mini',
    minSize: 80,
    hidden: true,
    items: [this.preViewHeader, this.preViewBody]
  });
  
  // define a template to use for the detail view
  var itemTplHeaderMarkup = ['<table width=100% border=0 cellspacing=0 cellpadding=0>', '<tr>', '<td width="16">&nbsp;</td>', '<td width="84" align="right" valign="top" class="mail-subject-content" >&nbsp;' + iwebos.message.mail.subject + ':&nbsp;</td>', '<td class="mail-subject-content" colspan="2" >{subject}</td>', '</tr>', '<tr>', '<td width="16" align="left" valign="top"><div id="mail-head-down"><div class="mail-head-right" onclick="showHeaderMail(true);"></div></div></td>', '<td align="right" valign="top" width="84">&nbsp;<b>' + iwebos.message.mail.grid_sender + ':&nbsp;</td>', '<td valign="top"><a class="aaaaa" style="cursor:pointer;">{from}</a></td>', '<td valign="top" align="right">&nbsp;<b>' + iwebos.message.mail.grid_sent + ': &nbsp;</b> {[this.format(values.sent)]}</td>', '</tr>', '</table>', '<table width=100% border=0 cellspacing=0 cellpadding=0 id="header-mail-show" style="display:NONE ;">', '<tpl if="this.isCheck(to)">', '<tr>', '<td width="100" align="right" valign="top">&nbsp;<b>' + iwebos.message.mail.template_mailto + ':&nbsp;</b> </td>', '<td valign="top"><tpl for="to"><a class="aaaaa" style="cursor:pointer;">{[this.formatAddress(values.name,values.address)]},</a> </tpl></td>', '</tr>', '</tpl>', '<tpl if="this.isCheck(cc)">', '<tr>', '<td align="right" valign="top"><b>CC:&nbsp;</b></td>', '<td valign="top">', '<tpl for="cc"><a class="aaaaa" style="cursor:pointer;">{[this.formatAddress(values.name,values.address)]}</a>, </tpl>', '</td>', '</tr>', '</tpl>', '<tpl if="this.isCheck(bcc)">', '<tr>', '<td align="right" valign="top"><b>BCC:&nbsp;</b></td>', '<td valign="top">', '<tpl for="bcc"><a style="cursor:pointer;" onclick="newComposerWAddress(\'{[this.formatAddress(values.name,values.address)]}\')">{[this.formatAddress(values.name,values.address)]}</a>, </tpl>', '</td>', '</tr>', '</tpl>', '</table>', '<tpl if="this.isCheck(attachments)">', '<table width=100% border=0 cellspacing=0 cellpadding=0 id="attach-panel" style="display: ;">', '<tr>', '<td width="100" align="right" valign="top" NOWRAP>', '<tpl if="this.isDownloadAll(attachments)">', '<a class="icon-email-download-all" style="cursor:pointer;" onclick="MailService.downloadAttach(\'{id}\', \'all\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>', '</tpl>', '<a onclick="MailService.downloadAttach(\'{id}\', \'all\')"><b>' + iwebos.message.mail.template_attach + ':</b></a> &nbsp;', '</td>', '<td valign="top" colspan="3">', '<tpl for="attachments">', '<span class={[MailService.getCssAttachment(values.icon)]}>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a style="cursor:pointer;" onclick="MailService.downloadAttach(\'{parent.id}\', \'{fkey}\',\'{icon}\')">&nbsp;{fname}</a>  ', '</tpl>', '</td>', '</tr>', '</table>', '</tpl>'];
  
  var itemTplBodyMarkup = ['<table width=100% border=0 cellspacing=0 cellpadding=0 bgcolor="#FFFFFF">', '<tr><td valign=top style="padding:10px;font-size:15px;">&nbsp;{body}</td></tr></table>'];
  
  var itemTpl = new Ext.XTemplate(itemTplHeaderMarkup.join(''), {
    format: function(value) {
      return (value == 0) ? '' : new Date(value).format('d/m/Y');
    },
    isCheck: function(val) {
      return (false == val) ? '' : val;
    },
    
    isDownloadAll: function(attach) {
      return (attach.length > 1 ? true : false);
    },
    formatAddress: function(name, address) {
      if (name != '') {
        return name + ' &lt;' + address + '&gt;';
      }
      else {
        return address;
      }
    },
    getDownloadName: function(text) {
      text = text.replace(/'/g, '');
      text = text.replace(/"/g, '');
      return text;
    },
    
    getFkey: function(text) {
      return escape(text);
    }
  });
  
  var itemTpl2 = new Ext.XTemplate(itemTplBodyMarkup.join(''), {});
  
  /**
   * When rowselect
   */
  this.grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r) {
    // check select more one row
    var __records = sm.getSelections();
    if (__records.length > 1) 
      return;
    
    var __record = r;
    var __hId = __record.data.id;
    
    if (this.lastIndex == __hId) {
      return;
    }
    
    this.lastIndex = __hId;
    var __sPanel = Ext.getCmp('preview-mail-panel');
    var __baseParams = {
      iwct: 'mailContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'select',
      action: 'select'
    };
    __baseParams['hid'] = __hId;
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: function(result, request) {
        var __data = Ext.util.JSON.decode(result.responseText);
        var __success = (__data.success == undefined ? true : false);
        //south panel
        if (__success) {
          __data.id = __hId;
          __data = MailService.escape(__data);
          //apply template mail panel					
          var __detailPanel = Ext.getCmp('previewPanelHeader');
          // header
          if (__detailPanel) 
            itemTpl.overwrite(__detailPanel.body, __data);
          
          var __detailPanel2 = Ext.getCmp('previewPanelBody');
          
          // body
          if (__detailPanel2) 
            itemTpl2.overwrite(__detailPanel2.body, __data);
          
          this.__owner.data = __data;
          
          
          // update data 
          if (__record.data.read === 'N') {
            // update data read
            __record.data.read = 'Y';
            __record.commit();
            
            // update mail folder
            Ext.getCmp('mail-tree-folder-id').readMail(1);
            
          }
          if (!__sPanel.isVisible()) 
            __sPanel.setVisible(true);
          Ext.EventManager.fireWindowResize();
          
        }
        else {
          this.__owner.data = null;
          __sPanel.setVisible(false);
          Ext.EventManager.fireWindowResize();
          Ext.MessageBox.show({
            title: iwebos.message.mail.announcement,
            msg: iwebos.message.mail.view_mail_error,
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR
          });
        }
      },
      failure: function(result, request) {
      },
      maskEl: this.grid.bwrap
    });
  }, this);
  
  //main grid
  var __gridMain = new Ext.Panel({
    id: 'mail-gridmain-id',
    layout: 'border',
    region: 'center',
    frame: false,
    border: false,
    items: [this.adSearch, this.grid, this.south]
  });
  
  // create follow up panel.
  /**
   * Setting up follow up panel.
   */
  var __fuPanel = new iNet.iwebos.ui.mail.MailFollowUpPanel({
    id: 'mail-follow-up-ctx-id',
    region: 'east',
    anchor: '100%'
  });
  
  // handle view data from follow up panel.
  __fuPanel.on('view', this.onViewFollowUp, this, {
    stopEvent: true
  });
  
  __fuPanel.on('render', function(panel) {
    // droptarget on tree
    var __drop = new Ext.dd.DropTarget(__fuPanel.getEl(), {
      ddAppendOnly: true,
      appendOnly: true,
      ddGroup: 'Mail-drag-drop-group',
      dropAllowed: true,
      appendOnly: true,
      notifyDrop: function(source, e, data) {
        var __grid = data.grid;
        var __selects = __grid.getSelectionModel().getSelections();
        var __ids = [];
        var __data = [];
        if (__selects !== undefined) {
          for (var __index = 0; __index < __selects.length; __index++) {
            __data[__index] = {
              data: __selects[__index].data.id,
              name: __selects[__index].data.subject
            };
          }
          panel.save(__data, iNet.iwebos.ui.common.control.FollowUpPanel.TODAY);
        }
        else {
          return false;
        }
        return true;
      }
    });
  }, this);
  var __main = new Ext.Panel({
    layout: 'border',
    region: 'center',
    frame: false,
    border: false,
    items: [__gridMain, __fuPanel]
  });
  __main.on('render', this._checkMailAuto.createDelegate(this), this);
  this.south.on('render', function(panel) {
    var __cmp = Ext.getCmp('mail-gridmain-id');
    var __gridMain = __cmp.getSize().height;
    
    // preview panel email-message-grid-id
    __cmp = Ext.getCmp('email-message-grid-id');
    __cmp.setHeight(__gridMain / 2);
    
    __cmp = Ext.getCmp('preview-mail-panel');
    __cmp.setHeight(__gridMain / 2);
    
    
    
    
    ///////////////////Convert byte to megabyte/////////////////////////////
    var __convertMB = function(size) {
      return Math.round(size / 1048576);
    };
    ///////////////////////////////////////////////
    // success.
    var _handlerFn = function(response, options) {
      var __result = eval('(' + response.responseText + ')');
      if (__result.success == undefined) {
        var __quotaDiv = document.getElementById('quota-id');
        var __used = __result.used;
        var __quota = __result.quota;
        var __percentage = Math.round((__used * 100) / __quota);
        var __content = '';
        if (__percentage <= 100) {
          if (__percentage > 90) {
            __quotaDiv.className = "quota-warning";
          }
          else {
            __quotaDiv.className = "quota";
          }
          __content = 'Hiện tại bạn đã sử dụng ' + __convertMB(__used) +
          ' MB (' +
          __percentage +
          '%)' +
          ' trên tổng số ' +
          __convertMB(__quota) +
          ' MB';
        }
        else {
          __content = 'Hiện tại bạn đã sử dụng ' + __convertMB(__used) +
          ' MB (' +
          __percentage +
          '%)' +
          ' vượt so với hạn mức ' +
          __convertMB(__quota) +
          ' MB';
          __quotaDiv.className = "quota-greater";
        }
        
        
        __quotaDiv.innerHTML = __content;
      }
    };
    
    var __baseParams = {
      iwct: 'quotacontent',
      iwm: 'MAIL',
      iwc: 'READ_ONLY',
      iwa: 'loadQuota',
      action: 'loadQuota'
    };
    
    
    // send request to server.
    iNet.Ajax.request({
      url: 'jsonajax',
      success: _handlerFn,
      failure: _handlerFn,
      method: 'POST',
      params: __baseParams
    });
    
  }, this);
  
  iNet.iwebos.ui.mail.grid.MailViewPanel.superclass.constructor.call(this, {
    id: 'main-tabs',
    activeTab: 0,
    region: 'center',
    anchor: '100%',
    enableTabScroll: true,
    autoScroll: true,
    frame: false,
    border: false,
    items: {
      id: 'main-mailbox',
      title: iwebos.message.mail.title_mailbox,
      iconCls: 'icon-mail-home',
      layout: 'border',
      hideMode: 'offsets',
      items: [__main]
    }
  });
  
};
Ext.extend(iNet.iwebos.ui.mail.grid.MailViewPanel, Ext.TabPanel, {
  /**
   * Call tab Config Account
   * @param {Object} type
   */
  configAccount: function(type) {
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('mail-config-account');
    if (!__tab) {
      __tab = new iNet.iwebos.ui.mail.ConfigAccount();
      __mainTab.add(__tab).show();
      __tab.loadInfo();
    }
    __mainTab.setActiveTab(__tab);
  },
  
  /**
   * add new tab
   * @param {Object} type
   */
  _fnNewTab: function(type) {
    // get current tab.
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('mail-compose');
    var __type = type || 'composer';
    if (!__tab) {
      __tab = (__type === 'composer') ? new iNet.iwebos.ui.mail.MailCompose() : new iNet.iwebos.ui.mail.MailCompose();
      __mainTab.add(__tab).show();
    }
    __mainTab.setActiveTab(__tab);
    
    // return tab.		
    return __tab;
  },
  
  /**
   * fetch mail
   * @param {Object} smtpAccount
   */
  _fetchMail: function(index) {
    var handlerFn = function(response, options) {
      var __result = Ext.util.JSON.decode(response.responseText);
      
      if (__result.success == undefined) {
        __header = __result;
        __header.id = __header.hid;
        var __folder = __header.folder;
        var __treeFolder = Ext.getCmp('mail-tree-folder-id');
        var __inboxFolder = __treeFolder.getInboxFolder();
        var __currentFolder = __treeFolder.getFolder();
        
        // check folder mail header is current folder
        if ((__folder === __currentFolder)) {
          var __store = this.grid.store;
          // add to grid
          if (__store.getCount() > 0) {
            __store.insert(0, new Ext.data.Record(__header));
          }
          else {
            __store.addData(__header);
          }
        }
        
        __treeFolder.addMessageUnread2Folder(1, __folder);
        
        // update new message info
        if (this.fetchInfo.accounts[index].newMessage <= 0) {
          this.fetchInfo.accounts[index].newMessage = 0;
        }
        else {
          this.fetchInfo.fetched++;
          this.fetchInfo.accounts[index].newMessage--;
          this._updateFetchMessage();
        }
        // check to fetch the next email	
        if (this.fetchInfo.accounts[index].newMessage == 0) {
          this.fetchInfo.accounts[index].fetched = true;
          this._fetchMail(++index);
        }
        else {
          this._fetchMail(index);
        }
      }
      else {
        this.fetchInfo.accounts[index].newMessage = 0;
        this.fetchInfo.accounts[index].fetched = true;
        
        this.fetchInfo.errors[this.fetchInfo.errors.length] = this.fetchInfo.accounts[index].smtpAccount;
        this._fetchMail(++index);
        
        // TODO add to note
      }
    };
    
    // check status fetch mail
    if (!this.fetchInfo.fetching) 
      return;
    
    if ((index < this.fetchInfo.accounts.length)) {
      if (this.fetchInfo.accounts[index].newMessage > 0) {
        var __baseParams = {
          iwct: 'mailContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'check',
          action: 'check',
          mode: 'fetch',
          newMessage: this.fetchInfo.accounts[index].newMessage,
          smtpAccount: this.fetchInfo.accounts[index].smtpAccount
        };
        
        // start icon loading
        iNet.Ajax.request({
          url: 'jsonajax',
          params: __baseParams,
          scope: this,
          method: 'POST',
          success: handlerFn,
          failure: function(response, options) {
            this.stopFetchMail();
          }
        });
      }
      else {
        this._fetchMail(++index);
      }
    }
    else {
      this.stopFetchMail();
    }
  },
  
  /**
   * fetch new mail
   * @param {} index
   */
  _fetchNewMessage: function(index) {
    var handlerFn = function(response, options) {
      var __newMessage = 0;
      
      var __result = eval('(' + response.responseText + ')');
      
      // failure
      if (__result.success == undefined) {
        __newMessage = __result.newMessages;
      }
      else {
        __newMessage = 0;
        
        this.fetchInfo.errors[this.fetchInfo.errors.length] = this.fetchInfo.accounts[index].smtpAccount;
        //TODO note to configure
      }
      
      this.fetchInfo.total = this.fetchInfo.total + __newMessage;
      this._updateFetchMessage();
      this.fetchInfo.accounts[index].fetchNew = true;
      this.fetchInfo.accounts[index].newMessage = __newMessage;
      this._fetchNewMessage(++index);
      
    };
    
    // check status fetch mail
    if (!this.fetchInfo.fetching) 
      return;
    
    if (index < this.fetchInfo.accounts.length) {
      // password is not null ????
      if (this.fetchInfo.accounts[index].pass) {
        var __baseParams = {
          iwct: 'mailContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'check',
          action: 'check',
          mode: 'newMessage',
          index: index,
          smtpAccount: this.fetchInfo.accounts[index].smtpAccount
        };
        
        // start icon loading
        iNet.Ajax.request({
          url: 'jsonajax',
          params: __baseParams,
          scope: this,
          method: 'POST',
          success: handlerFn,
          failure: function(response, options) {
            this.stopFetchMail();
          }
        });
      }
      else {
        // update password
        this._updatePassword(index);
      }
      
    }
    else {
      this._fetchMail(0);
    }
  },
  
  _updateFetchMessage: function() {
    var __fetchInfo = this.fetchInfo;
    
    // check fetch mail status 
    if (!__fetchInfo.fetching) {
      // the fetched email was stop
      document.getElementById("result-check-mail").innerHTML = iwebos.message.mail.receive_mail;
      return;
    }
    
    if (__fetchInfo.total == 0 && __fetchInfo.fetched == 0) {
      document.getElementById("result-check-mail").innerHTML = iwebos.message.mail.receive_mail;
    }
    else {
      document.getElementById("result-check-mail").innerHTML = '(' + __fetchInfo.fetched + '/' + __fetchInfo.total + ')';
    }
  },
  
  /**
   * stop check mail & update fetch info
   */
  stopFetchMail: function() {
    checkMailLoading(false, this.fetchInfo.errors);
    this.fetchInfo.fetching = false;
    this.fetchInfo.total = 0;
    this.fetchInfo.fetched = 0;
    this._updateFetchMessage();
    var __refresh = this.config.refresh || 0;
    
    // clear time out 
    if (!!this.timeout) 
      clearTimeout(this.timeout);
    
    // set time out
    if (__refresh != 0) 
      this.timeout = setTimeout(this._checkAuto.createDelegate(this), __refresh * 60000);
  },
  
  _checkMailAuto: function(panel) {
  
    var __handlerFn = function(response, options) {
      if (!!response) {
        var __result = eval('(' + response.responseText + ')');
        
        // failure
        if (__result.success != undefined) {
          return;
        }
        
        // set configure
        this.config = __result;
        // load mail folder
        Ext.getCmp('mail-tree-folder-id').reloadMailBox();
        
        // load mail inn INBOX
        if (this.config.folder) {
          var __inbox = this.config.folder.inbox;
          var __store = this.grid.store;
          var __params = __store.baseParams;
          __params['id'] = __inbox;
          __params['mode'] = 'basic';
          __store.baseParams = __params;
          __store.load({
            params: {
              start: 0,
              limit: iNet.INET_PAGE_LIMIT
            }
          });
        }
        // check email auto             	
        this._checkEmail();
      }
    };
    
    var __baseParams = {
      iwct: 'mailConfigureContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'load',
      action: 'load',
      mode: 'personal'
    };
    
    // start icon loading
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: __handlerFn,
      failure: function(response, options) {
        this.stopFetchMail();
      }
    });
  },
  
  /**
   * auto check email
   */
  _checkAuto: function() {
    if (this.config.refresh != 0) {
      this._checkEmail();
    }
    else {
      if (this.timeout) {
        clearTimeout(this.timeout);
      }
    }
    
  },
  
  loadPersonalConfig: function() {
    var __handlerFn = function(response, options) {
      this.flag = true;
      if (!!response) {
        var __result = eval('(' + response.responseText + ')');
        
        // failure
        if (__result.success != undefined) {
          return;
        }
        
        // set configure
        this.config = __result;
        // check email auto             	
        this._checkEmail();
      }
    };
    
    
    var __baseParams = {
      iwct: 'mailConfigureContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'load',
      action: 'load',
      mode: 'personal'
    };
    
    // start icon loading
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: __handlerFn,
      failure: function(response, options) {
        this.stopFetchMail();
      }
    });
  },
  
  _checkEmail: function() {
    if (this.fetchInfo.fetching) 
      return;
    
    checkMailLoading(true, []);
    this.fetchInfo.fetching = true;
    
    var __config = this.config || {};
    
    var __smtpAccount = __config.smtpAccount;
    
    if (__smtpAccount == undefined) {
      // load personal config
      if (this.flag) {
        MailService.showInfo(iwebos.message.mail.error_config_fetch_mail);
      }
      else {
        this.flag = false;
        this.loadPersonalConfig();
      }
      
      this.stopFetchMail();
      return;
    }
    
    if (__smtpAccount.smtps.length == 0) {
      MailService.showInfo(iwebos.message.mail.error_config_fetch_mail);
      this.stopFetchMail();
      return;
    }
    
    var __smtps = __smtpAccount.smtps;
    
    var __accounts = [];
    // fetch new message
    for (var __index = 0; __index < __smtps.length; __index++) {
      __accounts[__index] = {
        smtpAccount: __smtps[__index].smtpAccount,
        pass: __smtps[__index].pass,
        newMessage: 0,
        address: __smtps[__index].address,
        fetchNew: false,
        fetched: false
      };
    }
    
    this.fetchInfo.accounts = __accounts;
    this.fetchInfo.errors = [];
    
    if (__accounts.length > 0) {
      this._fetchNewMessage(0);
    }
    else {
      this.stopFetchMail();
    }
  },
  
  
  /**
   * open composer new email tab
   */
  _newEmail: function() {
  
    // get current tab.
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('mail-compose');
    if (!__tab) {
      __tab = new iNet.iwebos.ui.mail.MailCompose();
      __mainTab.add(__tab).show();
      __tab.loadInfo(0, '');
    }
    else {
      var __owner = __tab.__owner || {};
      
      if (__owner.close) {
        __tab.clearData();
        __tab.loadInfo(0, '');
      }
    }
    __mainTab.setActiveTab(__tab);
    
  },
  
  /**
   *
   * @param {Object} task
   */
  _composerEmail: function(task) {
    // get selection.
    var __selections = this.grid.getSelectionModel().getSelections();
    if (__selections == undefined || __selections.length == 0) 
      return;
    this.fnComposerMail(__selections[0].data.id, task);
  },
  
  /**
   * compose email from given task and identifier mail
   * @param {} id
   * @param {} task
   */
  fnComposerMail: function(id, task) {
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('mail-compose');
    if (!__tab) {
      __tab = new iNet.iwebos.ui.mail.MailCompose();
      __mainTab.add(__tab).show();
    }
    
    __mainTab.setActiveTab(__tab);
    // create identifier.
    
    __tab.loadInfo(id, task);
  },
  /**
   * open composer reply email tab
   */
  replyEmail: function() {
    this._composerEmail('reply');
  },
  
  /**
   * open composer reply all email tab
   */
  replyAllEmail: function() {
    this._composerEmail('replyA');
  },
  
  /**
   * open composer forward email tab
   */
  forwardEmail: function() {
    this._composerEmail('forward');
  },
  
  spamEmail: function() {
    var __selects = this.grid.getSelectionModel().getSelections();
    if (!!__selects) {
      // get SPAM FOLDER
      var __folder = this.config.folder;
      // folder undefined
      if (__folder === undefined) {
        return;
      }
      
      // add SPAM
      MailService.addSpam(__folder.spam, __selects);
      
      
      // TODO move record to SPAM folder
      var __store = this.grid.store;
      var __handlerFn = function(response, options) {
        var __result = Ext.util.JSON.decode(response.responseText);
        if (!!__result) {
          for (var __index = 0; __index < __selects.length; __index++) {
            __store.remove(__selects[__index]);
          }
        }
      };
      var __ids = [];
      for (var __index = 0; __index < __selects.length; __index++) {
        __ids.push(__selects[__index].data.id);
      }
      
      var __selectedIds = __ids.join(';');
      
      if (__selectedIds == '') 
        return;
      
      var __baseParams = {
        iwct: 'mailHeaderContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'move',
        action: 'move',
        id: __folder.spam,
        listid: __selectedIds
      };
      
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: __handlerFn,
        failure: __handlerFn,
        method: 'POST',
        scope: this,
        params: __baseParams,
        maskEl: this.grid.bwrap
      });
      
    }
  },
  
  spam2Email: function() {
    var __selects = this.grid.getSelectionModel().getSelections();
    if (!!__selects) {
      // get INBOX FOLDER
      var __folder = this.config.folder;
      // folder undefined
      if (__folder === undefined) {
        return;
      }
      
      // add SPAM
      MailService.removeSpam(__selects);
      
      
      // TODO move record to INBOX folder
      var __store = this.grid.store;
      var __handlerFn = function(response, options) {
        var __result = Ext.util.JSON.decode(response.responseText);
        if (!!__result) {
          for (var __index = 0; __index < __selects.length; __index++) {
            __store.remove(__selects[__index]);
          }
        }
      };
      var __ids = [];
      for (var __index = 0; __index < __selects.length; __index++) {
        __ids.push(__selects[__index].data.id);
      }
      
      var __selectedIds = __ids.join(';');
      
      if (__selectedIds == '') 
        return;
      
      var __baseParams = {
        iwct: 'mailHeaderContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'move',
        action: 'move',
        id: __folder.inbox,
        listid: __selectedIds
      };
      
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: __handlerFn,
        failure: __handlerFn,
        method: 'POST',
        scope: this,
        params: __baseParams,
        maskEl: this.grid.bwrap
      });
      
    }
  },
  /**
   * compose new email with address
   * @param {} address
   */
  composeWithAddress: function(address) {
    // get current tab.
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('mail-compose');
    if (!__tab) {
      __tab = new iNet.iwebos.ui.mail.MailCompose();
      __mainTab.add(__tab).show();
      __tab.loadInfo(0, '');
    }
    else {
      var __owner = __tab.__owner || {};
      
      if (__owner.close) {
        __tab.clearData();
        __tab.loadInfo(0, '');
      }
    }
    __mainTab.setActiveTab(__tab);
    
    __tab.setToAddress(address);
    
  },
  /**
   * Create grid menu.
   */
  _fnCreateMenu: function() {
    if (!this.menu) {
      this.menu = new Ext.menu.Menu({
        id: 'grid-ctx',
        items: [{
          id: 'grid-ctx-mnu-reply-id',
          text: iwebos.message.mail.context_reply,
          iconCls: 'icon-email-reply',
          scope: this,
          disabled: true,
          handler: this.replyEmail
        }, {
          id: 'grid-ctx-mnu-replyall-id',
          text: iwebos.message.mail.context_replyall,
          iconCls: 'icon-email-reply_all',
          scope: this,
          disabled: true,
          handler: this.replyAllEmail
        }, {
          id: 'grid-ctx-mnu-forward-id',
          text: iwebos.message.mail.context_forward,
          iconCls: 'icon-email-forward',
          scope: this,
          disabled: true,
          handler: this.forwardEmail
        }, '-', {
          id: 'grid-ctx-mnu-delete-id',
          text: iwebos.message.mail.context_delete,
          iconCls: 'icon-email-delete_mail',
          scope: this,
          disabled: true,
          handler: this.onDelete
        }, {
          id: 'grid-ctx-mnu-move-id',
          text: iwebos.message.mail.context_move,
          iconCls: 'icon-email-folder',
          scope: this,
          disabled: true,
          handler: this._onMove2Folder
        },        /*{
         id:'grid-ctx-mnu-filter-id',
         text: iwebos.message.mail.context_filter,
         iconCls: 'icon-email-filter',
         scope: this,
         disabled: true,
         handler: this._onCreateFilter
         },*/
        '-', {
          id: 'grid-ctx-mnu-mark-unread-id',
          text: iwebos.message.mail.context_mark_unread,
          iconCls: 'icon-email-unread_mail',
          scope: this,
          disabled: true,
          handler: this._markEmailUnRead
        }, {
          id: 'grid-ctx-mnu-mark-read-id',
          text: iwebos.message.mail.context_mark_read,
          iconCls: 'icon-email-read_mail',
          scope: this,
          disabled: true,
          handler: this._markEmailRead
        }, '-', {
          id: 'grid-ctx-mnu-mark-priority-id',
          text: iwebos.message.mail.context_mark_priority,
          iconCls: 'icon-email-urgent',
          scope: this,
          disabled: true,
          handler: this._markEmailPriority
        }, {
          id: 'grid-ctx-mnu-mark-unpriority-id',
          text: iwebos.message.mail.context_mark_unpriority,
          iconCls: 'icon-email-',
          scope: this,
          disabled: true,
          handler: this._markEmailNormal
        }, '-', {
          id: 'grid-ctx-mnu-flag-id',
          text: iwebos.message.mail.context_flag,
          disabled: true,
          menu: {
            items: [{
              text: iwebos.message.mail.context_flag_normal,
              scope: this,
              handler: this._markFlagNothing
            }, {
              text: iwebos.message.mail.context_flag_work,
              iconCls: 'icon-email-work',
              scope: this,
              handler: this._markFlagWork
            }, {
              text: iwebos.message.mail.context_flag_personal,
              iconCls: 'icon-email-personal',
              scope: this,
              handler: this._markFlagPersonal
            }, {
              text: iwebos.message.mail.context_flag_todo,
              iconCls: 'icon-email-todo',
              scope: this,
              handler: this._markFlagTodo
            }, {
              text: iwebos.message.mail.context_flag_late,
              iconCls: 'icon-email-late',
              scope: this,
              handler: this._markFlagLate
            }, {
              text: iwebos.message.mail.context_flag_phone,
              iconCls: 'icon-email-phone',
              scope: this,
              handler: this._markFlagCall
            }, {
              text: iwebos.message.mail.context_flag_reply,
              iconCls: 'icon-email-reply',
              scope: this,
              handler: this._markFlagReply
            }, {
              text: iwebos.message.mail.context_flag_approve,
              iconCls: 'icon-email-approve_mail',
              scope: this,
              handler: this._markFlagReview
            }]
          }
        }, {
          id: 'grid-ctx-mnu-followup-id',
          iconCls: 'icon-followup-flags',
          text: iwebos.message.followup.flags,
          menu: {
            items: [{
              text: iwebos.message.followup.today,
              iconCls: 'icon-followup-today',
              scope: this,
              handler: this._fnOnToday
            }, {
              text: iwebos.message.followup.tomorrow,
              iconCls: 'icon-followup-tomorrow',
              scope: this,
              handler: this._fnOnTomorrow
            }, {
              text: iwebos.message.followup.week,
              iconCls: 'icon-followup-week',
              scope: this,
              handler: this._fnOnWeek
            }, {
              text: iwebos.message.followup.nextweek,
              iconCls: 'icon-followup-nextweek',
              scope: this,
              handler: this._fnOnNextWeek
            }, {
              text: iwebos.message.followup.others,
              iconCls: 'icon-followup-others',
              scope: this,
              handler: this._fnOnOthers
            }]
          }
        }, '-', {
          id: 'grid-ctx-mnu-filter-id',
          text: iwebos.message.mail.filter_create,
          iconCls: 'icon-email-filter',
          scope: this,
          handler: this._callDialogFilter
        }]
      });
    }
    // return grid menu.
    return this.menu;
  },
  /**
   * Handle button and grid menu.
   *
   * @param {Object} menu - the given menu to handle.
   */
  _fnHandleMenu: function(menu, record) {
    var __record = record || {};
    
    if (!__record.data) {
      menu.items.get('grid-ctx-mnu-reply-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-replyall-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-forward-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-delete-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-move-id').setDisabled(true);
      //menu.items.get('grid-ctx-mnu-filter-id').setDisabled(true) ;
      menu.items.get('grid-ctx-mnu-mark-unread-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-mark-read-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-mark-priority-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-mark-unpriority-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-flag-id').setDisabled(true);
      menu.items.get('grid-ctx-mnu-followup-id').setDisabled(true);
    }
    else {
      var __show = !(__record.data.id > 0);
      var __type = __record.data.type;
      var __reply = (__type == 'DRAFT');
      var __filter = (__type == 'INBOX' || __type == 'CUSTOM')
      // trash folder 
      var __isTrash = this._isTrashFolder();
      var __allowFollowUp = (__type !== 'DRAFT' && __type !== 'SENT' && !__isTrash);
      
      menu.items.get('grid-ctx-mnu-reply-id').setDisabled(__reply);
      menu.items.get('grid-ctx-mnu-replyall-id').setDisabled(__reply);
      menu.items.get('grid-ctx-mnu-forward-id').setDisabled(__reply);
      menu.items.get('grid-ctx-mnu-delete-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-move-id').setDisabled(false);
      //menu.items.get('grid-ctx-mnu-filter-id').setDisabled(!__filter) ;
      menu.items.get('grid-ctx-mnu-mark-unread-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-mark-read-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-mark-priority-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-mark-unpriority-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-flag-id').setDisabled(__show);
      menu.items.get('grid-ctx-mnu-followup-id').setDisabled(!__allowFollowUp);
    }
  },
  /**
   * Right click
   * @param {Object} grid
   * @param {Object} index
   * @param {Object} e
   */
  _onGridContext: function(event) {
    // does not allow any control handle this event.
    event.stopEvent();
    
    // create combine menu.
    var __menu = this._fnCreateMenu();
    
    // handle the grid menu item.
    this._fnHandleMenu(__menu);
    
    // show menu.
    __menu.showAt(event.getXY());
  },
  
  /**
   * Handle grid row context menu.
   *
   * @param {Object} grid - the given current grid instance.
   * @param {Number} index - the given row index.
   * @param {Object} event - the given grid event.
   */
  _onGridRowContextMenu: function(grid, index, event) {
    // stop this event.
    event.stopEvent();
    
    // handle selection.
    WebOSGridService.handleSelection(grid, index);
    
    // create grid menu.
    var __menu = this._fnCreateMenu();
    
    // handle the menu.
    this._fnHandleMenu(__menu, grid.store.getAt(index));
    
    // show menu.
    __menu.showAt(event.getXY());
  },
  /**
   * Call dialog filter
   */
  _callDialogFilter: function() {
    var __dlgFilter = new iNet.iwebos.ui.mail.DialogMailFilter();
    __dlgFilter.show();
  },
  
  /**
   * DoubleClick on Grid
   * @param {Object} grid
   * @param {Object} index
   * @param {Object} e
   */
  _onDoubleClick: function(grid, index, e) {
    e.stopEvent();
    var __mainTab = Ext.getCmp('main-tabs');
    var __store = grid.store;
    var __data = __store.getAt(index).data;
    var __type = __data.type;
    if (this.__owner.data == null) {
      Ext.MessageBox.show({
        title: iwebos.message.mail.announcement,
        msg: iwebos.message.mail.view_mail_error,
        buttons: Ext.MessageBox.OK,
        icon: Ext.MessageBox.ERROR
      });
      return;
    }
    
    if (__type == 'DRAFT' || __type == 'SENT') {
      var __tab = this._fnNewTab('composer');
      if (!__tab) {
        __tab = new iNet.iwebos.ui.mail.MailCompose();
        __mainTab.add(__tab).show();
      }
      __mainTab.setActiveTab(__tab);
      __tab.loadInfo(__data.id, 'draft');
    }
    else {
    
      var __loadData = function() {
        var __tab = Ext.getCmp('mail-reader');
        
        if (!__tab) {
          __tab = new iNet.iwebos.ui.mail.MailReader();
          __mainTab.add(__tab).show();
        }
        
        __mainTab.setActiveTab(__tab);
        var __data = __mainTab.__owner.data || {};
        __tab.loadInfo(__data);
      }
      
      setTimeout(__loadData, 1500);
    }
  },
  
  /**
   * handle click action on action panel.
   *
   * @param {Object} event - the given event to be handle.
   * @param {Object} target - the target raised event.
   */
  doAction: function(event, target) {
    if (this.actions !== undefined &&
    this.actions[target.id] !== undefined &&
    typeof this.actions[target.id] == 'function') {
      this.actions[target.id]();
    }
  },
  
  /**
   * delete mail.
   */
  onDelete: function() {
    var __owner = this.owner;
    var __panel = this;
    var __onSuccess = function(btn) {
      if (btn === 'ok' || btn === 'yes') {
        // get selection item.
        var __grid = __panel.grid;
        var __selects = __grid.getSelectionModel().getSelections();
        __panel.fnDeleteMail(__selects, null);
        
      }
    };
    if (this._isTrashFolder()) {
      // show confirmation.
      Ext.MessageBox.confirm(iwebos.message.mail.announcement, iwebos.message.mail.confirm_delete_email, __onSuccess, this);
    }
    else {
      __onSuccess('ok');
    }
  },
  
  /**
   *
   * @param {[int]} mails - array email
   */
  fnDeleteMail: function(mails, panel) {
    // success.
    var __handlerFn = function(response, options) {
      var __result = eval('(' + response.responseText + ')');
      
      if (__result == undefined) {
        // show error.
        MailService.showError(iwebos.message.mail.error_delete_mail);
        return;
      }
      
      if (__result.success) {
        // get removed item.
        var __removeItems = options.removeItems;
        var __unread = 0;
        var __store = this.grid.store;
        for (var __index = 0; __index < __removeItems.length; __index++) {
          var __record = __removeItems[__index];
          
          if (!__record) {
            continue;
          }
          
          if (__record.data.read === 'N') {
            __unread++;
          }
          // remove follow up
          var __cmpFollowUp = Ext.getCmp('mail-follow-up-ctx-id');
          if (!!__cmpFollowUp) {
            var __followUp = __cmpFollowUp._findByData(__record.data.id);
            if (__followUp != null) {
              __cmpFollowUp.store.remove(__followUp);
            }
          }
          
          // remove record from grid
          __store.remove(__record);
        }
        
        if (__unread > 0) {
          var __treeFolder = Ext.getCmp('mail-tree-folder-id');
          __treeFolder.readMail(__unread);
          
          // is not trash folder
          if (!this._isTrashFolder()) {
            var __folderTrash = this.config.folder.trash;
            __treeFolder.addMessageUnread2Folder(__unread, __folderTrash);
          }
        }
        
        // remove panel
        if (panel != null) {
          this.remove(panel);
        }
      }
      else {
        // show error.
        MailService.showError(iwebos.message.mail.error_delete_mail);
      }
    };
    
    var __ids = [];
    if (mails !== undefined) {
      for (var __index = 0; __index < mails.length; __index++) {
        __ids.push(mails[__index].data.id);
      }
    }
    
    // set parameters.
    var __baseParams = {
      iwm: 'MAIL',
      iwct: 'mailHeaderContent',
      iwc: 'READ_WRITE',
      iwa: 'delete',
      action: 'delete'
    };
    
    var __selectedIds = __ids.join(';');
    if (__selectedIds !== '') {
      __baseParams['listid'] = __selectedIds;
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: __handlerFn,
        failure: __handlerFn,
        method: 'POST',
        scope: this,
        params: __baseParams,
        removeItems: mails,
        maskEl: this.grid.bwrap
      });
    }
  },
  //~ Private method ======================================================
  /**
   * Mark email from given
   * @param {Object} flag - value of flag
   */
  _markFlag: function(flag) {
  
    var __grid = this.grid;
    // success.
    var handlerFn = function(response, options) {
      var result = eval('(' + response.responseText + ')');
      if (result.success) {
        // get update item.
        var __updateItems = options.updateItems;
        var __indexRecord = 0;
        var __followpCmp = Ext.getCmp('mail-follow-up-ctx-id');
        for (var index = 0; index < __updateItems.length; index++) {
          var __mail = __updateItems[index];
          __mail.data.flag = flag;
          __mail.commit();
          
          // update follow up
          __followpCmp.updateFlag(__mail.data.id, __mail.data.flag);
        }
      }
      else {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_task,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      }
    };
    
    var __baseParams = {
      iwct: 'mailHeaderContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'flag',
      action: 'flag'
    };
    
    // get selection item.         
    var __selects = __grid.getSelectionModel().getSelections();
    var __ids = [];
    if (__selects !== undefined) {
      for (var index = 0; index < __selects.length; index++) {
        __ids.push(__selects[index].data.id);
      }
    }
    
    __baseParams['flag'] = flag;
    
    var __selectedIds = __ids.join(';');
    if (__selectedIds !== '') {
      __baseParams['listid'] = __selectedIds;
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: handlerFn,
        failure: handlerFn,
        method: 'POST',
        params: __baseParams,
        updateItems: __selects,
        maskEl: __grid.bwrap
      });
    }
  },
  
  /**
   *  mark flag work
   */
  _markFlagWork: function() {
    this._markFlag('WORK');
  },
  
  /**
   *  mark flag personal
   */
  _markFlagPersonal: function() {
    this._markFlag('PERSONAL');
  },
  
  /**
   *  mark flag todo
   */
  _markFlagTodo: function() {
    this._markFlag('TODO');
  },
  
  /**
   *  mark flag late
   */
  _markFlagLate: function() {
    this._markFlag('LATE');
  },
  
  /**
   *  mark flag reply
   */
  _markFlagReply: function() {
    this._markFlag('REPLY');
  },
  
  /**
   *  mark flag call
   */
  _markFlagCall: function() {
    this._markFlag('CALL');
  },
  
  /**
   *  mark flag review
   */
  _markFlagReview: function() {
    this._markFlag('REVIEW');
  },
  
  /**
   *  mark flag nothing
   */
  _markFlagNothing: function() {
    this._markFlag('NOTHING');
  },
  
  /**
   *
   * @param {String} read
   */
  _markRead: function(read) {
    var __grid = this.grid;
    // success.
    var _handlerFn = function(response, options) {
      var __result = eval('(' + response.responseText + ')');
      if (__result.success) {
        // get update item.
        var __updateItems = options.updateItems;
        var __indexRecord = 0;
        for (var index = 0; index < __updateItems.length; index++) {
          var __mail = __updateItems[index];
          var __oldStatus = __mail.data.read;
          
          __mail.data.read = read;
          __mail.commit();
          
          // update tree mail folder
          if (read == 'Y' && __oldStatus == 'N') {
            Ext.getCmp('mail-tree-folder-id').readMail(1);
          }
          else 
            if (read == 'N' && __oldStatus == 'Y') {
              Ext.getCmp('mail-tree-folder-id').addMessageUnread(1);
            }
        }
      }
      else {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_task,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      }
    };
    
    var __baseParams = {
      iwct: 'loadMailHeaderContent',
      iwm: 'MAIL',
      iwc: 'READ_ONLY',
      iwa: 'read',
      action: 'read',
      read: read
    };
    
    // get selection item.
    var __selects = __grid.getSelectionModel().getSelections();
    var __ids = [];
    if (__selects !== undefined) {
      for (var index = 0; index < __selects.length; index++) {
        __ids.push(__selects[index].data.id);
      }
    }
    
    var __selectedIds = __ids.join(';');
    if (__selectedIds !== '') {
      __baseParams['listid'] = __selectedIds;
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: _handlerFn,
        failure: _handlerFn,
        method: 'POST',
        params: __baseParams,
        updateItems: __selects,
        scope: this,
        maskEl: __grid.bwrap
      });
    }
  },
  
  /**
   * mark email is read
   */
  _markEmailRead: function() {
    this._markRead('Y');
  },
  
  /**
   * mark email is unread
   */
  _markEmailUnRead: function() {
    this._markRead('N');
  },
  /**
   *
   * @param {String} important
   */
  _markPriority: function(important) {
    var __grid = this.grid;
    // success.
    var _handlerFn = function(response, options) {
      var __result = eval('(' + response.responseText + ')');
      if (__result.success) {
      
        // get update item.
        var updateItems = options.updateItems;
        var __indexRecord = 0;
        for (var __index = 0; __index < updateItems.length; __index++) {
          var __mail = updateItems[__index];
          // update data priority
          __mail.data.priority = important;
          __mail.commit();
        }
      }
      else {
        // show error.
        MailService.showError(iwebos.message.mail.error_task);
      }
    };
    
    var __baseParams = {
      iwct: 'mailHeaderContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'priority',
      action: 'priority',
      priority: important
    };
    
    // get selection item.
    var __selects = __grid.getSelectionModel().getSelections();
    var __ids = [];
    if (__selects !== undefined) {
      for (var __index = 0; __index < __selects.length; __index++) {
        __ids.push(__selects[__index].data.id);
      }
    }
    
    var __selectedIds = __ids.join(';');
    if (__selectedIds !== '') {
      __baseParams['listid'] = __selectedIds;
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        success: _handlerFn,
        failure: _handlerFn,
        method: 'POST',
        params: __baseParams,
        updateItems: __selects,
        maskEl: __grid.bwrap
      });
    }
  },
  
  _markEmailPriority: function() {
    this._markPriority('HIGH');
  },
  
  _markEmailNormal: function() {
    this._markPriority('NORMAL');
  },
  
  /**
   * open address book tab
   */
  openAddressBook: function() {
    // get current tab.
    var __mainTab = Ext.getCmp('main-tabs');
    var __tab = Ext.getCmp('main-addressbook-panel');
    if (!__tab) {
      __tab = new iNet.iwebos.ui.mail.AddressBookPanel();
      __mainTab.add(__tab).show();
    }
    __mainTab.setActiveTab(__tab);
  },
  
  _updatePassword: function(index) {
    var __panel = this;
    var __fetchInfo = __panel.fetchInfo;
    var __okHandler = function() {
      // validate data 
      var __password = this.getPassword();
      if (__password == '') {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_empty_password,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      
      
      var __param = {
        iwct: 'mailConfigureContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'updatePW',
        action: 'updatePW'
      };
      var __data = {
        address: __fetchInfo.accounts[index].address,
        pass: __password
      };
      
      __param['object'] = Ext.util.JSON.encode(__data);
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __param,
        scope: __panel,
        method: 'POST',
        success: function(response, options) {
          if (!!response) {
            var __result = eval('(' + response.responseText + ')');
            
            if (__result.success) {
              __panel.fetchInfo.accounts[index].pass = true;
              __panel.config.smtpAccount.smtps[index].pass = true;
              __panel._fetchNewMessage(index);
              
              // update to tab config account
              var __configPanel = Ext.getCmp('mail-config-account');
              if (__configPanel) {
                __configPanel.updatePassword2Account(__panel.config.smtpAccount.smtps[index].smtpAccount, __password);
              }
              
              return;
            }
          }
          this._fetchNewMessage(++index);
        },
        failure: function(response, options) {
          __panel._fetchNewMessage(++index);
        }
      });
      this.destroy();
    };
    
    
    var __dlg = new iNet.iwebos.ui.mail.DialogEnterPassword(this, {
      address: __fetchInfo.accounts[index].address,
      okhScope: __dlg,
      okHandler: __okHandler,
      cancelHandler: function() {
        this.destroy();
        __panel._fetchNewMessage(++index);
      }
    });
    __dlg.show(this);
  },
  /**
   * handle function or disabled button
   * @param {Object} sm :SelectionModel
   */
  _fnHandleFunction: function(sm) {
    var selects = sm.getSelections();
    var __prefix = 'iwebos-mail-toolbar-';
    var __mailType = '';
    if (selects.length > 0) 
      __mailType = selects[0].data.type;
    
    //action
    var __delete = Ext.getCmp(__prefix + 'mail-toolbar-btn-delete');
    var __reply = Ext.getCmp(__prefix + 'mail-toolbar-reply');
    var __replyAll = Ext.getCmp(__prefix + 'mail-toolbar-reply-all');
    var __forward = Ext.getCmp(__prefix + 'mail-toolbar-forward');
    var __spam = Ext.getCmp(__prefix + 'mail-toolbar-spam');
    var __allowForward = false;
    //__allowForward=!(__mailType=='NORMAL'||__mailType=='REPLY');
    __allowForward = (__mailType == 'DRAFT');
    if (selects.length > 0) {
      __delete.setDisabled(false);
      __reply.setDisabled(__allowForward);
      __replyAll.setDisabled(__allowForward);
      __forward.setDisabled(__allowForward);
      __spam.setDisabled(false);
    }
    else {
      __delete.setDisabled(true);
      __reply.setDisabled(true);
      __replyAll.setDisabled(true);
      __forward.setDisabled(true);
      __spam.setDisabled(true);
    }
  },
  
  _onStoreLoad: function(store, records, options) {
    var __sPanel = Ext.getCmp('preview-mail-panel');
    
    if (store.getCount() <= 0) {
      //visible preview panel
      __sPanel.setVisible(false);
      Ext.EventManager.fireWindowResize();
    }
    else {
      if (!__sPanel.isVisible()) {
        __sPanel.setVisible(true);
        Ext.EventManager.fireWindowResize();
      }
    }
  },
  
  /**
   * handle event when store remove record
   * @param {} store
   * @param {} record
   * @param {} index
   */
  _onStoreRemove: function(store, record, index) {
    var __sPanel = Ext.getCmp('preview-mail-panel');
    
    if (this.lastIndex === record.data.id) {
      this.fnVisiblePreviewPanel(false);
    }
    
    if (store.getCount() <= 0) {
      //visible preview panel
      this.fnVisiblePreviewPanel(false);
    }
  },
  
  /**
   * handle event when store remove record
   * @param {} store
   * @param {} records
   * @param {} index
   */
  _fnOnStoreInsert: function(store, records, index) {
    this.fnVisiblePreviewPanel(true);
  },
  
  /**
   * handler move mail to folder
   */
  _onMove2Folder: function() {
    // get selection item.
    var __panel = this;
    var __grid = __panel.grid;
    var __selects = __grid.getSelectionModel().getSelections();
    
    if (__selects == undefined) 
      return;
    
    var __ids = [];
    for (var __index = 0; __index < __selects.length; __index++) {
      __ids.push(__selects[__index].data.id);
    }
    
    var __selectedIds = __ids.join(';');
    
    if (__selectedIds == '') 
      return;
    
    // ok henaler
    var __okHandler = function() {
      // get data
      var __folder = this.getData();
      
      if (__folder == undefined) {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.filter_error_input_folder,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      
      // get current folder 
      var __current = Ext.getCmp('mail-tree-folder-id').getFolder();
      var __inbox = __panel.config.folder.inbox;
      
      // move mail to current folder ????
      if (__current == __folder.id) {
        this.destroy();
        return;
      }
      
      
      var __baseParams = {
        iwct: 'mailHeaderContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'move',
        action: 'move',
        id: __folder.id,
        listid: __selectedIds
      };
      // submit request 
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __baseParams,
        scope: __panel,
        method: 'POST',
        success: function(response, options) {
          if (!response) 
            return;
          
          var __result = eval('(' + response.responseText + ')');
          
          if (__result.success) {
            var __store = this.grid.store;
            var __folders = this.config.folder;
            
            var __isTrash = (__folders.trash == __folder.id);
            var __cmpFollowUp = Ext.getCmp('mail-follow-up-ctx-id');
            var __ids = [];
            // get removed item.
            var __itemsRemove = options.itemsRemove;
            for (var i = 0; i < __itemsRemove.length; i++) {
              __store.remove(__itemsRemove[i]);
              // remove follow up
              if (__isTrash) {
                var __followUp = __cmpFollowUp._findByData(__itemsRemove[i].data.id);
                if (__followUp != null) {
                  __ids[__ids.length] = __followUp.data.id;
                }
              }
            }
            
            if (__ids.length > 0) {
              __cmpFollowUp._fnDelete(__ids);
            }
          }
        },
        itemsRemove: __selects,
        failure: function(response, options) {
        }
      });
      
      this.destroy();
    };
    
    var __dlg = new iNet.iwebos.ui.mail.DialogMailFolder(this, {
      okHandler: __okHandler,
      okhScope: __dlg,
      ttitle: iwebos.message.mail.select_folder_to_move
    });
    __dlg.show(this);
  },
  
  /**
   * create filter menu handle
   */
  _onCreateFilter: function() {
    var __panel = this;
    var __grid = __panel.grid;
    var __selects = __grid.getSelectionModel().getSelections();
    
    if (__selects == undefined) 
      return;
    
    var __okHandler = function() {
      // validate data
      var __error = this.validate();
      if (__error.length > 0) {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: __error,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      
      var __fnSuccess = function(response, options) {
        if (!!response) {
          var __data = Ext.util.JSON.decode(response.responseText);
          var __success = __data.success == undefined ? true : false;
          if (__success) {
            Ext.MessageBox.show({
              title: iwebos.message.mail.announcement,
              msg: 'Bộ lọc thư đã được tạo thành công.',
              buttons: Ext.MessageBox.OK,
              icon: Ext.MessageBox.INFO
            });
            return;
          }
        }
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.filter_error_save,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      };
      // get data
      var __filter = this.getData();
      
      // base param
      var __param = {
        iwct: 'mailFilterContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'save',
        action: 'save'
      };
      
      __param['object'] = Ext.util.JSON.encode(__filter);
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __param,
        scope: this,
        method: 'POST',
        success: __fnSuccess,
        failure: __fnSuccess
      });
      this.destroy();
    };
    
    var __dlg = new iNet.iwebos.ui.mail.DialogMailAddFilter(this, {
      okHandler: __okHandler,
      okhScope: __dlg,
      data: {
        data: __selects.data.sender
      }
    });
    
    __dlg.show(this);
  },
  
  /**
   * check current folder is trash
   */
  _isTrashFolder: function() {
    var __folder = this.config.folder;
    var __current = Ext.getCmp('mail-tree-folder-id').getFolder();
    var __trash = __folder.trash || -1;
    
    return (__current == __trash);
  },
  
  /**
   * current folder is spam folder ???
   */
  isSpamFolder: function() {
    var __folder = this.config.folder;
    var __current = Ext.getCmp('mail-tree-folder-id').getFolder();
    var __spam = __folder.spam || -1;
    
    return (__current == __spam);
  },
  
  /**
   * Add selected record to follow up data.
   *
   * @param {String} type - the given record type.
   */
  _fnAddFlag: function(type) {
    // lookup record.
    var __records = this._fnGetSelectedRecord();
    if (__records == null) 
      return;
    
    // create data to save to database.
    var __data = [];
    for (var __index = 0; __index < __records.length; __index++) {
      __data[__index] = {
        data: __records[__index].data.id,
        name: __records[__index].data.subject
      };
    }
    
    var __type = type || iNet.iwebos.ui.common.control.FollowUpPanel.TODAY;
    
    // get follow up content.
    var __followup = Ext.getCmp('mail-follow-up-ctx-id');
    if (!__followup) 
      return;
    // add data to database.
    __followup.save(__data, __type);
  },
  
  /**
   * get selected record.
   */
  _fnGetSelectedRecord: function() {
    // get select data.
    var __selModel = this.grid.getSelectionModel();
    if (__selModel.getCount() < 0) 
      return null;
    
    // return selected item.
    return __selModel.getSelections();
  },
  
  /**
   * Add to follow up today.
   */
  _fnOnToday: function() {
    this._fnAddFlag(iNet.iwebos.ui.common.control.FollowUpPanel.TODAY);
  },
  
  /**
   * Add to follow up tomorrow.
   */
  _fnOnTomorrow: function() {
    this._fnAddFlag(iNet.iwebos.ui.common.control.FollowUpPanel.TOMORROW);
  },
  
  /**
   * Add to follow up current week.
   */
  _fnOnWeek: function() {
    this._fnAddFlag(iNet.iwebos.ui.common.control.FollowUpPanel.WEEK);
  },
  
  /**
   * Add to follow up next week.
   */
  _fnOnNextWeek: function() {
    this._fnAddFlag(iNet.iwebos.ui.common.control.FollowUpPanel.NEXT_WEEK);
  },
  
  /**
   * Add to follow up others.
   */
  _fnOnOthers: function() {
    this._fnAddFlag(iNet.iwebos.ui.common.control.FollowUpPanel.OTHERS);
  },
  
  onViewFollowUp: function(data) {
    var __data = data || {};
    if (!__data.data) 
      return;
    
    // set action.
    var __baseParams = {
      iwct: 'mailContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'select',
      action: 'select'
    };
    __baseParams['hid'] = __data.data;
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: function(result, request) {
        var __data = Ext.util.JSON.decode(result.responseText);
        var __success = (__data.success == undefined ? true : false);
        //south panel
        if (__success) {
          var __tab = Ext.getCmp('mail-reader');
          var __mainTab = Ext.getCmp('main-tabs');
          
          if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.MailReader();
            __mainTab.add(__tab).show();
          }
          
          __mainTab.setActiveTab(__tab);
          var __data = __mainTab.__owner.data || {};
          __tab.loadInfo(__data);
          
        }
        else {
          this.__owner.data = null;
          __sPanel.setVisible(false);
          Ext.EventManager.fireWindowResize();
          Ext.MessageBox.show({
            title: iwebos.message.mail.announcement,
            msg: iwebos.message.mail.view_mail_error,
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR
          });
        }
      },
      failure: function(result, request) {
      },
      maskEl: this.grid.bwrap
    });
  },
  
  /**
   * invisible/visible preview panel
   */
  fnVisiblePreviewPanel: function(visible) {
    var __sPanel = Ext.getCmp('preview-mail-panel');
    
    if (visible && !__sPanel.isVisible()) {
      __sPanel.setVisible(true);
      Ext.EventManager.fireWindowResize();
    }
    else 
      if (!visible && __sPanel.isVisible()) {
        __sPanel.setVisible(false);
        Ext.EventManager.fireWindowResize();
      }
    
  },
  
  /**
   * Find record from given id
   * @param {int} id
   * @return {Record}
   */
  findByData: function(id) {
    var __store = this.grid.store;
    if (__store.getCount() == 0) 
      return null;
    
    // define record.
    var __record = null;
    for (var index = 0; index < __store.getCount(); index++) {
      __record = __store.getAt(index);
      if (__record.data.id === id) 
        return __record;
    }
    
    // return null.
    return __record;
  },
  
  /**
   * get domain default
   * @return {String} domain default
   */
  getDomainDefault: function() {
    var __smtpAccount = this.config.smtpAccount || {};
    var __smtpDefault = __smtpAccount.smtp;
    
    if (__smtpDefault === '' || __smtpDefault === undefined) 
      return null;
    
    var __index = __smtpDefault.indexOf(':');
    if (__index == -1) 
      return null;
    
    var __userName = __smtpDefault.substring(__index + 1);
    if (__userName === '' || __userName === null || __userName === undefined) 
      return null;
    
    var __index = __userName.indexOf('@');
    if (__index == -1) 
      return null;
    
    var __domain = __userName.substring(__index + 1);
    if (__domain === '' || __domain === null || __domain === undefined) {
      return null;
    }
    
    return __domain;
  },
  
  /**
   * @return {array} the list of recipients
   */
  getRecipients: function() {
    return this.recipients;
  },
  
  /**
   * set list of recipient to cache
   * @param {array} recipients - the list of recipients
   */
  setRecipients: function(recipients) {
    this.recipients = recipients;
  },
  
  /**
   * @method public
   * Handle advance search
   */
  searchAdvance: function(params) {
    this.adSearch.searchAdvance(params);
    var __mainTab = Ext.getCmp('main-tabs');
    var __mailBoxPanel = Ext.getCmp('main-mailbox');
    
    // set active mail tab
    __mainTab.setActiveTab(0);
    __mailBoxPanel.setTitle(iwebos.message.mail.search);
    
    var __treeFolder = Ext.getCmp('mail-tree-folder-id');
    var __sm = __treeFolder.getSelectionModel();
    var __currentNode = __sm.getSelectedNode();
    if (!!__currentNode) {
      __currentNode.unselect();
    }
    
    this.changeColumn(true);
  },
  
  /**
   * @method public
   * @param boolean flag
   * Change column
   */
  changeColumn: function(flag) {
    var __columnModel = this.grid.getColumnModel();
    if (flag) {
      if (__columnModel.isHidden(5)) {
        __columnModel.setHidden(5, false);
        __columnModel.setHidden(6, true);
      }
    }
    else {
      if (__columnModel.isHidden(6)) {
        __columnModel.setHidden(6, false);
        __columnModel.setHidden(5, true);
      }
    }
  },
  
  changeSpamButton: function(flag) {
    var __prefix = 'iwebos-mail-toolbar-';
    var __spam = Ext.getCmp(__prefix + 'mail-toolbar-spam');
    if (flag) {
      __spam.setText(iwebos.message.mail.mail_spam);
      __spam.setIconClass('icon-email-spam-button');
      __spam.setHandler(this.spamEmail.createDelegate(this));
    }
    else {
      __spam.setText(iwebos.message.mail.mail_not_spam);
      __spam.setIconClass('icon-email-not-spam-button');
      __spam.setHandler(this.spam2Email.createDelegate(this));
    }
  },
  
  addContact: function(address) {
    // parse address
    if (address == undefined || address == '') {
      MailService.showError('Tài khoản email không hợp l');
    }
    // check add has full name
    var index = address.indexOf('<');
    var fullname = '';
    if (index > 0) {
      fullname = address.substring(0, index);
      address = address.substring(index + 1, address.length - 1);
    }
    
    // save
    
    var _okHandler = function() {
      var __data = this.getData();
      if (__data.mail === '' || __data.name === '' || __data.dname === '') {
        // check valid form.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.contact.error_valid_data,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      var __action = 'insert';
      /**
       * handle save success.
       */
      var onSaveSuccess = function(response, options) {
        var __result = Ext.util.JSON.decode(response.responseText);
        var __success = (__result.success === undefined ? true : __result.success);
        if (!__success) {
          Ext.MessageBox.show({
            title: iwebos.message.mail.announcement,
            msg: iwebos.message.mail.contact.add_error,
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR
          });
        }
        return;
      };
      var __params = {
        iwct: 'addressBookContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'insert',
        action: 'insert'
      };
      __params['object'] = Ext.util.JSON.encode(__data);
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __params,
        scope: this,
        method: 'POST',
        success: onSaveSuccess,
        failure: function(result, request) {
        },
        maskEl: this.bwrap
      });
      // destroy the dialog.
      Ext.form.Field.prototype.msgTarget = 'side';
      this.destroy();
    };
    
    // show dialog
    /**
     * create unit dialog.
     */
    var __dlg = new iNet.iwebos.ui.mail.ContactDialog({
      title: iwebos.message.mail.contact.dialog_title,
      ttitle: iwebos.message.mail.contact.dialog_desc,
      btitle: '',
      iconCls: 'icon-contact-users',
      okHandler: _okHandler,
      width: 500,
      height: 470,
      resizable: true,
      modal: true,
      hscope: __dlg
    });
    __dlg.show(this);
    __dlg.setEmail({
      fullname: fullname,
      email: address
    });
  }
});
