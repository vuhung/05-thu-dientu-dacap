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
Ext.grid.CheckColumn = function(config) {
  Ext.apply(this, config);
  if (!this.id) {
    this.id = Ext.id();
  }
  this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
  init: function(grid) {
    this.grid = grid;
    this.grid.on('render', function() {
      var view = this.grid.getView();
      view.mainBody.on('mousedown', this.onMouseDown, this);
    }, this);
  },
  
  onMouseDown: function(e, t) {
    if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
      e.stopEvent();
      var index = this.grid.getView().findRowIndex(t);
      var record = this.grid.store.getAt(index);
      record.set(this.dataIndex, !record.data[this.dataIndex]);
    }
  },
  
  renderer: function(v, p, record) {
    p.css += ' x-grid3-check-col-td';
    return '<div class="x-grid3-check-col' + (v ? '-on' : '') + ' x-grid3-cc-' + this.id + '">&#160;</div>';
  }
};
/**
 * @class iNet.iwebos.ui.mail.ConfigAccount
 * @extends Ext.Panel
 * @constructor
 * Creates a Config Account Panel
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.ConfigAccount = function(viewer, config) {
  // setting the prefix identifier.
  this.prefix = (!this.prefix) ? 'imail-config-account-commnon' : this.prefix;
  this.fullName = this.prefix + 'fullname';
  this.address = this.prefix + 'address';
  this.signDefault = this.prefix + 'sign';
  this.signature = this.prefix + 'signature';
  this.smtp = this.prefix + 'smtp';
  this.autoCheck = this.prefix + 'autocheck';
  this.autoMinutes = this.prefix + 'minutes';
  this.__owner = {
    check: '',
    idRemove: []
  };
  
  //////////////////////////
  var sortInfo = {
    field: 'user',
    direction: 'DESC'
  };
  var defaultSort = {
    field: 'user',
    direction: "DESC"
  };
  
  var __mailRecord = Ext.data.Record.create([{
    name: 'id',
    mapping: 'user',
    type: 'string'
  }, {
    name: 'active',
    mapping: 'active',
    type: 'bool'
  }, {
    name: 'address',
    mapping: 'address',
    type: 'string'
  }, {
    name: 'desc',
    mapping: 'desc',
    type: 'string'
  }, {
    name: 'server',
    mapping: 'server',
    type: 'string'
  }, {
    name: 'protocol',
    mapping: 'protocol',
    type: 'string'
  }, {
    name: 'security',
    mapping: 'security',
    type: 'string'
  }, {
    name: 'user',
    mapping: 'user',
    type: 'string'
  }, {
    name: 'pass',
    mapping: 'pass',
    type: 'string'
  }, {
    name: 'smtpserver',
    mapping: 'smtpserver',
    type: 'string'
  }, {
    name: 'smtpsecurity',
    mapping: 'smtpsecurity',
    type: 'string'
  }, {
    name: 'smtpuser',
    mapping: 'smtpuser',
    type: 'string'
  }, {
    name: 'smtppass',
    mapping: 'smtppass',
    type: 'string'
  }]);
  
  var __reader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'rows',
    id: 'id'
  }, __mailRecord);
  
  // custom column plugin example
  var __checkColumn = new Ext.grid.CheckColumn({
    header: iwebos.message.mail.active,
    dataIndex: 'active',
    width: 70,
    fixed: true,
    resizable: true
  });
  
  // check active column plugin example
  var __columnModel = new Ext.grid.ColumnModel([__checkColumn, {
    header: iwebos.message.mail.description,
    dataIndex: 'desc',
    fixed: true,
    width: 200
  }, {
    header: iwebos.message.mail.account_name,
    dataIndex: 'user',
    fixed: true,
    width: 200
  }, {
    header: iwebos.message.mail.server,
    dataIndex: 'server',
    fixed: false,
    width: 200
  }]);
  // create email message grid.
  this.grid = new iNet.iwebos.ui.common.grid.WebOSEditorGrid({
    id: 'grid-config-account',
    region: 'center',
    anchor: '100%',
    minSize: 250,
    autoScroll: true,
    method: 'POST',
    loadFirst: false,
    sortInfo: sortInfo,
    reader: __reader,
    dsort: defaultSort,
    plugins: __checkColumn,
    clicksToEdit: 1,
    selModel: new Ext.grid.RowSelectionModel({
      singleSelect: true
    }),
    cm: __columnModel,
    filter: {
      data: 'all'
    },
    view: new Ext.grid.GridView({
      forceFit: true
    }),
    tbar: [{
      xtype: 'button',
      text: iwebos.message.mail.add_configuration,
      iconCls: 'icon-mail-add',
      handler: this._addAccount.createDelegate(this)
    }]
  });
  
  this.grid.store.on('update', this._onUpdateStore, this);
  // setting up sort information.
  var __signRecord = Ext.data.Record.create([{
    name: 'sid',
    mapping: 'sid',
    type: 'int'
  }, {
    name: 'sname',
    mapping: 'name',
    type: 'string'
  }, {
    name: 'scontent',
    mapping: 'content',
    type: 'string'
  }, {
    name: 'used',
    mapping: 'used',
    type: 'string'
  }]);
  
  // setting up reader.
  var __signReader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'rows',
    id: 'sname'
  }, __signRecord);
  
  var __signatureStore = new iNet.iwebos.ui.common.store.WebOSSimpleStore({
    reader: __signReader
  });
  
  // setting up sort information.
  var __smtpRecord = Ext.data.Record.create([{
    name: 'id',
    mapping: 'id',
    type: 'string'
  }, {
    name: 'smtpAccount',
    mapping: 'smtpAccount',
    type: 'string'
  }]);
  
  // setting up reader.
  var __smtpReader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'rows',
    id: 'id'
  }, __signRecord);
  
  var __smtpStore = new iNet.iwebos.ui.common.store.WebOSSimpleStore({
    reader: __signReader
  });
  
  // create main panel.
  var __main = new Ext.form.FormPanel({
    columnWidth: 1,
    anchor: '100%',
    frame: false,
    border: false,
    autoHeight: true,
    labelAlign: 'right',
    bodyStyle: 'padding: 5px;',
    items: [{
      border: false,
      html: '<b>' + iwebos.message.mail.info_common + '</b><br/><br/>'
    }, {
      layout: 'column',
      columnWidth: 1,
      anchor: '100%',
      border: false,
      items: [{
        columnWidth: 0.5,
        layout: 'form',
        labelWidth: 90,
        anchor: '100%',
        border: false,
        items: [{
          id: this.fullName,
          xtype: 'textfield',
          anchor: '95%',
          allowBlank: false,
          fieldLabel: iwebos.message.mail.fullname
        }, {
          id: this.smtp,
          triggerAction: 'all',
          mode: 'local',
          fieldLabel: iwebos.message.mail.smtp_default,
          xtype: 'combo',
          displayField: 'id',
          valueField: 'smtpAccount',
          store: __smtpStore,
          readOnly: true,
          selectOnFocus: true,
          forceSelection: true,
          typeAhead: true,
          border: false,
          anchor: '95%'
        }]
      }, {
        columnWidth: 0.5,
        layout: 'form',
        anchor: '100%',
        labelWidth: 90,
        border: false,
        items: [{
          layout: 'column',
          columnWidth: 1,
          anchor: '95%',
          border: false,
          items: [{
            layout: 'form',
            border: false,
            columnWidth: 0.8,
            labelWidth: 90,
            anchor: '95%',
            items: [{
              id: this.signature,
              triggerAction: 'all',
              mode: 'local',
              fieldLabel: iwebos.message.mail.signature,
              xtype: 'combo',
              displayField: 'sname',
              valueField: 'sname',
              store: __signatureStore,
              selectOnFocus: true,
              forceSelection: true,
              readOnly: true,
              border: false,
              anchor: '100%'
            }]
          }, {
            layout: 'form',
            border: false,
            columnWidth: 0.1,
            anchor: '100%',
            items: [{
              xtype: 'button',
              iconCls: 'icon-mail-signature-edit',
              border: false,
              handler: this._openSignatureDialog.createDelegate(this)
            }]
          }, {
            layout: 'form',
            border: false,
            columnWidth: 0.1,
            anchor: '100%',
            items: [{
              xtype: 'button',
              iconCls: 'icon-mail-signature-del',
              border: false,
              handler: this._deleteSignature.createDelegate(this)
            }]
          }]
        }, {
          anchor: '100%',
          layout: 'column',
          columnWidth: 1,
          border: false,
          items: [{
            columnWidth: 0.7,
            anchor: '98%',
            border: false,
            items: [{
              layout: 'form',
              anchor: '100%',
              border: false,
              bodyStyle: 'padding-left: 95px;',
              items: [{
                id: this.autoCheck,
                xtype: 'checkbox',
                border: false,
                anchor: '100%',
                hideLabel: true,
                boxLabel: iwebos.message.mail.auto_check
              }]
            }]
          }, {
            columnWidth: 0.1,
            anchor: '100%',
            border: false,
            items: [{
              layout: 'form',
              anchor: '80%',
              border: false,
              items: [{
                id: this.autoMinutes,
                xtype: 'numberfield',
                border: false,
                anchor: '95%',
                hideLabel: true
              }]
            }]
          }, {
            columnWidth: 0.2,
            layout: 'form',
            border: false,
            anchor: '100%',
            bodyStyle: 'padding-left: 5px;',
            items: [{
              border: false,
              html: iwebos.message.mail.minute
            }]
          }]
        }]
      }]
    }],
    tbar: [{
      xtype: 'button',
      iconCls: 'icon-save',
      text: iwebos.message.mail.save,
      scope: this,
      handler: this.onSave
    }, {
      xtype: 'button',
      text: iwebos.message.mail.get_config,
      scope: this,
      iconCls: 'icon-panel-config-account',
      handler: this._getConfig
    }]
  });
  
  var __panelConfigAccount = new Ext.Panel({
    region: 'north',
    collapsible: false,
    border: false,
    width: '100%',
    height: 145,
    layout: 'column',
    items: [__main]
  });
  
  __panelConfigAccount.on('resize', this.__resizePanel, this);
  this.grid.on('rowdblclick', this._onDoubleClick, this);
  this.grid.on('rowcontextmenu', this._onRowContextMenu, this);
  this.grid.on('contextmenu', this._onContextmenu, this);
  
  // add listener to autocheck check box
  Ext.getCmp(this.autoCheck).on('check', this._onAutoCheckChange, this);
  
  var __signsID = Ext.getCmp(this.signature);
  __signsID.on('select', this.onSelectGroup, this);
  
  iNet.iwebos.ui.mail.ConfigAccount.superclass.constructor.call(this, {
    id: 'mail-config-account',
    iconCls: 'icon-panel-config-account',
    title: iwebos.message.mail.configure_account,
    header: false,
    frame: false,
    border: false,
    closable: true,
    layout: 'border',
    renderTo: Ext.getBody(),
    items: [__panelConfigAccount, this.grid]
  });
  
};

/**
 * Extends Class Ext.Panel
 */
Ext.extend(iNet.iwebos.ui.mail.ConfigAccount, Ext.Panel, {
  /**
   * Event when select combo
   * @param {Object} combo
   * @param {Object} record
   * @param {Object} index
   */
  onSelectGroup: function(combo, record, index) {
    var __store = combo.store;
    var __index = __store.indexOf(record);
    
    this.__owner.check = record.data.sname;
    
  },
  /**
   * load configure
   */
  loadInfo: function() {
    var __baseParams = {
      iwct: 'loadMailConfigureContent',
      iwm: 'MAIL',
      iwc: 'READ_ONLY',
      iwa: 'load',
      action: 'load',
      mode: 'detail'
    };
    
    var __success = function(result, request) {
      if (!!result) {
        var __data = Ext.util.JSON.decode(result.responseText);
        var __success = (__data.success === undefined ? true : false);
        if (__success) {
          this._setData(__data);
          this.__owner.firstTime = false;
          return;
        }
        else {
          this.__owner.firstTime = true;
        }
      }
      else {
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: iwebos.message.mail.error_load_configure,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
      }
    };
    
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: __success,
      failure: function(result, request) {
      },
      maskEl: this.bwrap
    });
  },
  /**
   * validat user information
   */
  _validateData: function() {
    return '';
  },
  
  /**
   * set data configure email
   */
  _setData: function(object) {
    var __common = object.common || {};
    
    // set full name
    __comp = Ext.getCmp(this.fullName);
    __comp.setValue(__common.name);
    
    // set autocheck
    __comp = Ext.getCmp(this.autoCheck);
    __comp.setValue(__common.refresh > 0);
    
    // set auto minutes
    __comp = Ext.getCmp(this.autoMinutes);
    __comp.setValue(__common.refresh);
    
    __comp = Ext.getCmp(this.smtp);
    // set accounts to grid
    var __accounts = object.accounts;
    var __smtpConfig = [];
    var __store = this.grid.store;
    __store.removeAll();
    var __smtpStore = __comp.store;
    __smtpStore.removeAll();
    if (__accounts.length > 0) {
      for (var index = 0; index < __accounts.length; index++) {
        var __temp = __accounts[index];
        __store.addData(__temp);
        
        if (__temp.active) {
          __smtpStore.add(new Ext.data.Record({
            id: __temp.smtpuser,
            smtpAccount: __temp.smtpserver + ':' + __temp.smtpuser
          }));
          __smtpConfig[__smtpConfig.length] = {
            smtpAccount: __temp.smtpserver + ':' + __temp.smtpuser,
            address: __temp.smtpuser,
            pass: (__temp.pass.length > 0) && (__temp.smtppass.length > 0)
          };
        }
        
      }
      // fire event
      __smtpStore.suspendEvents();
      __smtpStore.clearFilter();
      __smtpStore.resumeEvents();
    }
    
    __comp.setValue(__common.smtp);
    
    
    //set signatures into combo box
    __comp = Ext.getCmp(this.signature);
    var __signs = object.signs;
    
    var __storeCombo = __comp.store;
    var __dataSign;
    __storeCombo.removeAll();
    if (__signs.length > 0) {
      for (var i = 0; i < __signs.length; i++) {
        __dataSign = __signs[i];
        if (this.emptyText && this.el && v !== undefined && v !== null && v !== '') {
          this.el.removeClass(this.emptyClass);
        }
        
        if (Ext.isIE && this.firstValue == '') {
          this.firstValue = value;
          return;
        }
        
        this.setRawValue(value);
        this.autoSize();
        __storeCombo.add(new Ext.data.Record({
          sid: __dataSign.id,
          sname: __dataSign.name,
          scontent: __dataSign.content,
          used: __dataSign.used
        }));
        if (__dataSign.used == 'Y') {
          // set signature default
          __comp.setValue(__dataSign.name);
        }
      }
      // fire event
      __storeCombo.suspendEvents();
      __storeCombo.clearFilter();
      __storeCombo.resumeEvents();
    }
    // update
    var __config = {};
    __config.smtp = __common.smtp;
    // set refresh
    __config.refresh = __common.refresh;
    __config.smtpAccount = {};
    
    // set smtp account
    __config.smtpAccount.smtps = __smtpConfig;
    // set default smtp
    __config.smtpAccount.smtp = __common.smtp;
    
    // set signature
    __config.signs = __signs;
    
    this._updatePersonalConfig(__config);
  },
  /**
   * return the object  data.
   */
  _getData: function() {
    var __object = {};
    var __common = {};
    
    // set full name
    var __comp = Ext.getCmp(this.fullName);
    __common.name = __comp.getValue();
    
    // set smtp default
    __comp = Ext.getCmp(this.smtp);
    __common.smtp = __comp.getValue();
    
    // set autocheck
    __comp = Ext.getCmp(this.autoCheck);
    var __compMinute = Ext.getCmp(this.autoMinutes);
    __common.refresh = (__comp.getValue() ? __compMinute.getValue() : 0);
    
    // set accounts to grid
    var __accounts = [];
    var __store = this.grid.store;
    if (__store.getCount() > 0) {
      for (var index = 0; index < __store.getCount(); index++) {
        __accounts[index] = __store.getAt(index).data;
      }
      
      // chose first smtp
      if (__common.smtp === null || __common.smtp === '') {
        var __data = __store.getAt(0).data;
        __common.smtp = __data.smtpserver + ':' + __data.smtpuser;
      }
    }
    // add common 
    __object.common = __common;
    
    // add accounts
    __object.accounts = __accounts;
    
    // signature 
    var __storeSign = Ext.getCmp(this.signature).store;
    var __signs = [];
    var __used = 'N';
    if (__storeSign.getCount() > 0) {
      for (var i = 0; i < __storeSign.getCount(); i++) {
        var __temp = __storeSign.getAt(i).data;
        if (__temp.sname == this.__owner.check) {
          __used = 'Y';
        }
        else {
          __used = 'N';
        }
        __signs[i] = {
          id: __temp.sid,
          name: __temp.sname,
          content: __temp.scontent,
          used: __used
        };
      }
    }
    
    __object.signs = __signs;
    
    __object.signRemove = this.__owner.idRemove;
    
    return __object;
  },
  
  /**
   * saving data
   */
  onSave: function() {
    var __error = this._validateData();
    if (__error !== '') {
      // show error.
      Ext.MessageBox.show({
        title: iwebos.message.mail.announcement,
        msg: __error,
        buttons: Ext.MessageBox.OK,
        icon: Ext.MessageBox.ERROR
      });
      return;
    }
    // handle successfull
    var __fnSuccess = function(response, options) {
      if (!!response) {
        var __data = Ext.util.JSON.decode(response.responseText);
        var __success = (__data.success === undefined);
        if (__success) {
          if (this.__owner.firstTime) {
            var __mailBox = Ext.getCmp('mail-tree-folder-id');
            __mailBox.reloadMailBox();
            
            Ext.getCmp('main-tabs')._checkMailAuto();
            
          }
          
          this._setData(__data);
          
          this.__owner.firstTime = false;
        }
      }
    };
    
    var __data = this._getData();
    var __param = {
      iwct: 'mailConfigureContent',
      iwm: 'MAIL',
      iwc: 'READ_WRITE',
      iwa: 'save',
      action: 'save'
    };
    __param['object'] = Ext.util.JSON.encode(__data);
    
    // send request to server.
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __param,
      scope: this,
      method: 'POST',
      success: __fnSuccess,
      failure: __fnSuccess,
      maskEl: this.bwrap
    });
  },
  
  /**
   * add account receive email
   */
  _addAccount: function() {
    var __panel = this;
    var __store = this.grid.store;
    var __okHandler = function() {
      //check null
      var __msg = this.validate();
      if (null != __msg) {
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: __msg,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      
      // add data to grid
      var __object = this.getData();
      __object.id = __object.user;
      __object.active = true;
      __store.addData(__object);
      
      // add to smtp default
      __panel.addSmtpDefault({
        id: __object.smtpuser,
        smtpAccount: __object.smtpserver + ':' + __object.smtpuser
      });
      this.destroy();
    };
    
    var __dlg = new iNet.iwebos.ui.mail.DialogConfigAccount(this, {
      okHandler: __okHandler,
      okhScope: __dlg
    });
    __dlg.show(this);
  },
  /**
   * delete account
   */
  _deleteAccount: function() {
    // get record which is selected
    var selects = this.grid.getSelectionModel().getSelections();
    if (selects.length === 0) {
      return;
    }
    // get store
    var __store = this.grid.store;
    var __record = selects[0];
    var __user = __record.data.user;
    
    //remove record in grid
    __store.remove(__record);
    
    //remove sign combobox
    var __component = Ext.getCmp(this.smtp);
    var __smtpStore = __component.store;
    if (__smtpStore.getCount() > 0) {
    
      for (var __index = 0; __index < __smtpStore.getCount(); __index++) {
        var __temp = __smtpStore.getAt(__index);
        var __smtp = __component.getValue();
        if (__temp.data.id == __user) {
          //remove combobox
          __smtpStore.remove(__temp);
          // smtp default has been remove	
          if (__smtp == __temp.data.smtpAccount) {
            if (__smtpStore.getCount() > 0) {
              var __tempRecord = __smtpStore.getAt(0);
              __component.setValue(__tempRecord.data.smtpAccount);
            }
            else {
              __component.setValue('');
            }
          }
          return;
        }
      }
    }
  },
  
  /**
   * Resize panel
   */
  __resizePanel: function() {
    Ext.EventManager.fireWindowResize();
  },
  
  /**
   * handler double click on grid
   * @param {Grid} grid
   * @param {Number} rowIndex
   * @param {Event} event
   */
  _onDoubleClick: function(grid, rowIndex, event) {
    event.stopEvent();
    var __store = grid.store;
    var __record = __store.getAt(rowIndex);
    var __okHandler = function() {
      var __msg = this.validate();
      if (null != __msg) {
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: __msg,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
      }
      var __oldData = __record.data;
      
      // get new data
      var __object = this.getData();
      __object.id = __object.user;
      __object.active = __record.data.active;
      
      // add data to record
      __record.data = __object;
      
      // update smtp default
      Ext.getCmp('mail-config-account').updateSmtpDefault(__oldData, __record.data);
      
      __record.commit();
      this.destroy();
    };
    var __dlg = new iNet.iwebos.ui.mail.DialogConfigAccount(this, {
      okHandler: __okHandler,
      okhScope: __dlg
    });
    __dlg.show(this);
    __dlg.loadData(__record.data);
  },
  
  /**
   * handler row context menu on grid
   * @param {} grid
   * @param {} rowIndex
   * @param {} event
   */
  _onRowContextMenu: function(grid, rowIndex, event) {
    event.stopEvent();
    // handle context menu.
    WebOSGridService.handleSelection(grid, rowIndex);
    
    var __menu = new Ext.menu.Menu({
      items: [{
        text: iwebos.message.mail.delete_account,
        canActivate: true,
        scope: this,
        iconCls: 'icon-email-account-delete',
        handler: this._deleteAccount
      }]
    });
    
    __menu.showAt(event.getXY());
  },
  
  /**
   * handler context menu on grid
   * @param {} event
   */
  _onContextmenu: function(event) {
    event.stopEvent();
  },
  
  /**
   * open signature dialog
   */
  _openSignatureDialog: function() {
    var __comp = Ext.getCmp(this.signature);
    var __store = __comp.store;
    
    var __record;
    var __indexSelect;
    
    var __okHandler = function() {
      // validate data 
      var __error = this.validate();
      if (__error !== '') {
        // show error.
        Ext.MessageBox.show({
          title: iwebos.message.mail.announcement,
          msg: __error,
          buttons: Ext.MessageBox.OK,
          icon: Ext.MessageBox.ERROR
        });
        return;
        
      }
      if (__indexSelect !== undefined) {
        __record = __store.getAt(__indexSelect);
      }
      // get data 
      var __data = this.getData();
      var __signName = __data.sname.trim();
      for (var index = 0; index < __store.getCount(); index++) {
        var __temp = __store.getAt(index).data;
        if (__signName == __temp.sname) {
          // show error.
          Ext.MessageBox.show({
            title: iwebos.message.mail.announcement,
            msg: iwebos.message.mail.error_signature_duplicate,
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR
          });
          return;
        }
      }
      
      __indexSelect = index;
      
      if (!!__record) {
        var __index = __store.indexOf(__record);
        __store.remove(__record);
        if (__store.getCount() === 0) {
          __data.used = 'Y';
          __store.insert(__index, new Ext.data.Record(__data));
        }
      }
      else {
        __store.add(new Ext.data.Record(__data));
      }
      
      // fire event
      __store.suspendEvents();
      __store.clearFilter();
      __store.resumeEvents();
      
      // set value combobox
      __comp.setValue(__data.sname);
      
      this.destroy();
    };
    
    
    var __dlg = new iNet.iwebos.ui.mail.DialogSignature(this, {
      okhScope: __dlg,
      okHandler: __okHandler
    });
    __dlg.show(this);
    __dlg.loadData(__record);
  },
  
  /**
   * handler check event of auto check email
   * @param {} checkBox
   * @param {} checked
   */
  _onAutoCheckChange: function(checkBox, checked) {
    var __comp = Ext.getCmp(this.autoMinutes);
    __comp.setDisabled(!checked);
  },
  
  /**
   * handler action delete signature
   */
  _deleteSignature: function() {
    var __comp = Ext.getCmp(this.signature);
    var __store = __comp.store;
    // get select signature name
    var __signName = __comp.getValue().trim();
    var __indexSelect;
    var __remove = [];
    
    for (var index = 0; index < __store.getCount(); index++) {
      var __temp = __store.getAt(index).data;
      
      __remove.push(__temp.sid);
      this.__owner.idRemove = __remove.join(';');
      
      if (__signName == __temp.sname) {
        __indexSelect = index;
        break;
      }
    }
    
    // remove signature
    if (__indexSelect !== undefined) {
      var __record = __store.getAt(__indexSelect);
      __store.remove(__record);
    }
    
    if (__store.getCount() > 0) {
      for (var i = 0; i < __store.getCount(); i++) {
        __temp = __store.getAt(i).data;
        __comp.setValue(__temp.sname);
      }
    }
    else {
      __comp.setValue('');
    }
    
  },
  
  _updatePersonalConfig: function(data) {
    var __panel = Ext.getCmp('main-tabs');
    __panel.config.refresh = data.refresh;
    __panel.config.smtpAccount = data.smtpAccount;
    
    var __panelCompose = Ext.getCmp('mail-compose');
    
    if (__panelCompose) {
      // update smtp accounts in compose tab
      __panelCompose.setSmtpAccount(data.smtpAccount.smtps, data.smtp);
      
      // update signature in compose tab
      __panelCompose.setSignature(data.signs);
      
    }
    
    // clear time out
    if (__panel.timeout) {
      clearTimeout(__panel.timeout);
    }
    
    // set time out
    if (data.refresh !== 0) {
      __panel.timeout = setTimeout(__panel._checkAuto.createDelegate(__panel), data.refresh * 60000);
    }
  },
  
  _getConfig: function() {
  
    var __baseParams = {
      iwct: 'loadMailConfigureContent',
      iwm: 'MAIL',
      iwc: 'READ_ONLY',
      iwa: 'loadDefault',
      action: 'loadDefault',
      mode: 'loadDefault'
    };
    
    var __success = function(response, options) {
      if (!!response) {
        var __data = Ext.util.JSON.decode(response.responseText);
        var __success = (__data.success === undefined ? true : false);
        if (__success) {
          if (__data.length > 0) {
            var __store = this.grid.store;
            for (var __index = 0; __index < __data.length; __index++) {
              var __account = __data[__index];
              for (var i = 0; i < __store.getCount(); i++) {
                var __temp = __store.getAt(i);
                var __oldData = __temp.data;
                // compare address
                if (__account.address == __temp.data.address ||
                __account.user == __temp.data.user ||
                __account.smtpuser == __temp.data.smtpuser) {
                
                  //									// remove record 
                  
                  // update account
                  if (!__account.active) {
                    __temp.data.active = __account.active;
                  }
                  __temp.data.address = __account.address;
                  
                  __temp.data.user = __account.user;
                  __temp.data.server = __account.server;
                  __temp.data.protocol = __account.protocol;
                  __temp.data.port = __account.port;
                  __temp.data.security = __account.security;
                  
                  __temp.data.smtpuser = __account.smtpuser;
                  __temp.data.smtpserver = __account.smtpserver;
                  __temp.data.smtpport = __account.smtpport;
                  __temp.data.smtpsecurity = __account.smtpsecurity;
                  
                  // insert record
                  __temp.commit();
                  this.updateSmtpDefault(__oldData, __temp.data);
                  __account.exist = true;
                  break;
                }
              }
              
              // check account exist
              if (__account.exist === undefined) {
                // do not exist
                // add to grid
                __store.addData(__account);
                this.addSmtpDefault({
                  id: __account.smtpuser,
                  smtpAccount: __account.smtpserver + ':' + __account.smtpuser
                });
              }
            }
          }
          
          return;
        }
      }
      
      MailService.showError(iwebos.message.mail.error_load_configure_default);
    };
    // submit request
    iNet.Ajax.request({
      url: 'jsonajax',
      params: __baseParams,
      scope: this,
      method: 'POST',
      success: __success,
      failure: function(result, request) {
      },
      maskEl: this.bwrap
    });
  },
  
  /**
   * update password to account
   * @param {String} smtp
   * @param {String} pass
   */
  updatePassword2Account: function(smtp, pass) {
    var __store = this.grid.store;
    if (__store.getCount() > 0) {
      var __tempData;
      var __tempSmtp;
      var __record;
      for (var __index = 0; __index < __store.getCount(); __index++) {
        __record = __store.getAt(__index);
        __tempData = __record.data;
        __tempSmtp = __tempData.smtpserver + ':' + __tempData.smtpuser;
        if (smtp === __tempSmtp) {
          __record.data.pass = pass;
          __record.data.smtppass = pass;
          __record.commit();
          return;
        }
      }
    }
  },
  
  /**
   * add data to smtp default combo box
   * @param {id, smtpAccount} data
   */
  addSmtpDefault: function(data) {
    var __comp = Ext.getCmp(this.smtp);
    var __smtpStore = __comp.store;
    
    var __record = new Ext.data.Record(data);
    
    if (__smtpStore.indexOf(__record) >= 0) 
      return;
    
    // add data to smtp combobox
    __smtpStore.add(__record);
    // fire event
    __smtpStore.suspendEvents();
    __smtpStore.clearFilter();
    __smtpStore.resumeEvents();
    
    if (__smtpStore.getCount() == 1) {
      __comp.setValue(data.smtpAccount);
    }
  },
  
  updateSmtpDefault: function(oldData, newData) {
    var __comp = Ext.getCmp(this.smtp);
    var __smtpStore = __comp.store;
    
    var __record = MailService.findById(__smtpStore, oldData.smtpuser);
    
    if (__record == null) 
      return;
    
    // inactive -> remove
    if (!newData.active) {
      __smtpStore.remove(__record);
      return;
    }
    
    // add data to smtp combo box
    __record.data.id = newData.smtpuser;
    __record.data.smtpAccount = newData.smtpserver + ':' + newData.smtpuser;
    __record.commit();
    
    // fire event
    __smtpStore.suspendEvents();
    __smtpStore.clearFilter();
    __smtpStore.resumeEvents();
    
    // check smtp default
    var __smtpDefault = __comp.getValue();
    if (__smtpDefault === (oldData.smtpserver + ':' + oldData.smtpuser)) {
      __comp.setValue(newData.smtpserver + ':' + newData.smtpuser);
    }
  },
  
  _onUpdateStore: function(store, record, operation) {
    if (Ext.data.Record.EDIT === operation) {
      var __data = record.data;
      var __comp = Ext.getCmp(this.smtp);
      var __smtpStore = __comp.store;
      
      // active -> add new
      if (__data.active) {
        this.addSmtpDefault({
          id: __data.smtpuser,
          smtpAccount: __data.smtpserver + ':' + __data.smtpuser
        });
      }
      else {// inactive -> remove
        var __smtpDefault = __comp.getValue();
        
        
        
        var __record = MailService.findById(__smtpStore, __data.smtpuser);
        if (__record != null) {
          // record will remove which is smtp default
          if (__smtpDefault === (__data.smtpserver + ':' + __data.smtpuser)) {
            var __index = __smtpStore.indexOf(__record);
            
            if (__smtpStore.getCount() === 1) {
              __comp.setValue('');
            }
            else {
              var __newIndex = 0;
              
              if (__index === 0) {
                __newIndex++;
              }
              
              var __newRecord = __smtpStore.getAt(__newIndex);
              __comp.setValue(__newRecord.data.smtpAccount);
            }
          }
          
          __smtpStore.remove(__record);
        }
      }
    }
  }
});
