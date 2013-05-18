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
 * @class iNet.iwebos.ui.mail.DialogConfigAccount
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogConfigAccount
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogConfigAccount = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  
  this.prefix = (!this.prefix) ? 'imail-dialog-config-account-' : this.prefix;
  this.address = this.prefix + 'address';
  this.desc = this.prefix + 'desc';
  
  this.user = this.prefix + 'user';
  this.pass = this.prefix + 'pass';
  this.server = this.prefix + 'server';
  this.port = this.prefix + 'port';
  this.protocol = this.prefix + 'protocol';
  this.security = this.prefix + 'security';
  
  this.smtpuser = this.prefix + 'smtpuser';
  this.smtppass = this.prefix + 'smtppass';
  this.smtpserver = this.prefix + 'smtpserver';
  this.smtpport = this.prefix + 'smtpport';
  this.smtpsecurity = this.prefix + 'smtpsecurity';
  
  var __cbSmtpStore = new Ext.data.SimpleStore({
    fields: ['value', 'text'],
    data: [['POP3', 'POP3'], ['IMAP', 'IMAP']]
  });
  
  var __cbSecrStore = new Ext.data.SimpleStore({
    fields: ['value', 'text'],
    data: [['NONE', 'NONE'], ['SSL', 'SSL'], ['TLS', 'TLS']]
  });
  
  // create main panel.
  var __main = new Ext.form.FormPanel({
    columnWidth: 1,
    anchor: '100%',
    frame: false,
    border: false,
    autoHeight: true,
    autoScroll: true,
    enableTabScroll: true,
    labelAlign: 'right',
    bodyStyle: 'padding: 5px;',
    items: [{
      border: false,
      html: '<b>' + iwebos.message.mail.info_common + ':</b><br/>'
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
          id: this.address,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.email
        }]
      }, {
        columnWidth: 0.5,
        layout: 'form',
        anchor: '100%',
        labelWidth: 90,
        border: false,
        items: [{
          id: this.desc,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.description
        }]
      }]
    }, {
      border: false,
      html: iwebos.message.mail.receive_pop3
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
          id: this.server,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.server
        }, {
          id: this.user,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.account
        }, {
          id: this.pass,
          xtype: 'textfield',
          inputType: 'password',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.pass
        }]
      }, {
        columnWidth: 0.5,
        layout: 'form',
        anchor: '100%',
        labelWidth: 90,
        border: false,
        items: [{
          id: this.protocol,
          xtype: 'combo',
          store: __cbSmtpStore,
          readOnly: true,
          displayField: 'text',
          valueField: 'value',
          anchor: '95%',
          value: 'POP3',
          fieldLabel: iwebos.message.mail.protocol_type,
          mode: 'local',
          triggerAction: 'all',
          selectOnFocus: true
        }, {
          id: this.port,
          xtype: 'numberfield',
          fieldLabel: iwebos.message.mail.port,
          value: '110',
          anchor: '95%'
        }, {
          id: this.security,
          xtype: 'combo',
          store: __cbSecrStore,
          readOnly: true,
          displayField: 'text',
          valueField: 'value',
          anchor: '95%',
          value: 'NONE',
          fieldLabel: iwebos.message.mail.security,
          mode: 'local',
          triggerAction: 'all',
          selectOnFocus: true
        }]
      }]
    }, {
      border: false,
      html: iwebos.message.mail.send_smtp
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
          id: this.smtpserver,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.server
        }, {
          id: this.smtpuser,
          xtype: 'textfield',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.account
        }, {
          id: this.smtppass,
          xtype: 'textfield',
          inputType: 'password',
          anchor: '95%',
          fieldLabel: iwebos.message.mail.pass
        }]
      }, {
        columnWidth: 0.5,
        layout: 'form',
        anchor: '100%',
        labelWidth: 90,
        border: false,
        items: [{
          id: this.smtpport,
          xtype: 'numberfield',
          fieldLabel: iwebos.message.mail.port,
          value: '25',
          anchor: '95%'
        }, {
          id: this.smtpsecurity,
          xtype: 'combo',
          store: __cbSecrStore,
          readOnly: true,
          displayField: 'text',
          valueField: 'value',
          anchor: '95%',
          value: 'NONE',
          fieldLabel: iwebos.message.mail.security,
          mode: 'local',
          triggerAction: 'all',
          selectOnFocus: true
        }]
      }]
    }]
  });
  
  var __panelConfigAccount = new Ext.Panel({
    region: 'center',
    collapsible: false,
    border: false,
    enableTabScroll: true,
    autoScroll: true,
    width: '100%',
    //height: 250,
    layout: 'column',
    items: [__main]
  });
  
  __main.on('render', function(panel) {
    Ext.getCmp(this.address).focus(true, 1000);
    Ext.getCmp(this.address).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.desc).on('specialkey', this._fnSpecialKey, this);
    
    Ext.getCmp(this.user).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.pass).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.server).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.port).on('specialkey', this._fnSpecialKey, this);
    
    Ext.getCmp(this.smtpuser).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.smtppass).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.smtpserver).on('specialkey', this._fnSpecialKey, this);
    Ext.getCmp(this.smtpport).on('specialkey', this._fnSpecialKey, this);
    
  }, this);
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  this.cancelhScope = this.cancelhScope || this;
  
  
  iNet.iwebos.ui.mail.DialogConfigAccount.superclass.constructor.call(this, {
    id: 'mail-config-account-dialog',
    title: iwebos.message.mail.create_account,
    iconCls: 'icon-email-config-account',
    region: 'center',
    anchor: '100%',
    width: 600,
    height: 480,
    modal: true,
    frame: true,
    resizable: false,
    layout: 'border',
    hideMode: 'offsets',
    items: [{
      region: 'center',
      frame: true,
      width: '100%',
      items: __panelConfigAccount
    }],
    buttons: [{
      text: iwebos.message.mail.ok,
      iconCls: 'ok-btn',
      handler: this.okHandler,
      scope: this.okhScope
    }, {
      text: iwebos.message.mail.cancel,
      iconCls: 'cancel-btn',
      handler: this.fnCancelHandler,
      scope: this.cancelhScope
    }]
  });
};
Ext.extend(iNet.iwebos.ui.mail.DialogConfigAccount, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
  ttitle: iwebos.message.mail.configure_account,
  /**
   * bottom title.
   */
  btitle: iwebos.message.mail.setup_properties_for_current_account,
  
  /**
   * set data for dialog
   *
   * @param {Object} id - the id of domain name
   * @param {Object} action - the action
   */
  setData: function(object) {
    // set address
    var __comp = Ext.getCmp(this.address);
    __comp.setValue(object.address);
    
    // set description
    __comp = Ext.getCmp(this.desc);
    __comp.setValue(object.desc);
    
    // set server
    __comp = Ext.getCmp(this.server);
    __comp.setValue(object.server);
    
    // set protocol
    __comp = Ext.getCmp(this.protocol);
    __comp.setValue(object.protocol);
    
    // set port
    __comp = Ext.getCmp(this.port);
    __comp.setValue(object.port);
    
    // set security
    __comp = Ext.getCmp(this.security);
    __comp.setValue(object.security);
    
    // set user
    __comp = Ext.getCmp(this.user);
    __comp.setValue(object.user);
    
    // set pass
    __comp = Ext.getCmp(this.pass);
    __comp.setValue(object.pass);
    
    // set smtp server
    __comp = Ext.getCmp(this.smtpserver);
    __comp.setValue(object.smtpserver);
    
    // set smtp port
    __comp = Ext.getCmp(this.smtpport);
    __comp.setValue(object.smtpport);
    
    // set smtp security
    __comp = Ext.getCmp(this.smtpsecurity);
    __comp.setValue(object.smtpsecurity);
    
    // set smtp user
    __comp = Ext.getCmp(this.smtpuser);
    __comp.setValue(object.smtpuser);
    
    // set smtp pass
    __comp = Ext.getCmp(this.smtppass);
    __comp.setValue(object.smtppass);
    
  },
  
  /**
   * load domain data
   *
   * @param {Object} id - the domain name
   */
  loadData: function(account) {
    var __account = account || {};
    this.setData(__account);
  },
  
  /**
   * return the data object
   */
  getData: function() {
    // create object to save data.
    var __object = {};
    
    var __comp;
    // set address
    __comp = Ext.getCmp(this.address);
    __object.address = __comp.getValue();
    
    // set description
    __comp = Ext.getCmp(this.desc);
    __object.desc = __comp.getValue();
    
    // set server
    __comp = Ext.getCmp(this.server);
    __object.server = __comp.getValue();
    
    // set protocol
    __comp = Ext.getCmp(this.protocol);
    __object.protocol = __comp.getValue();
    
    // set port
    __comp = Ext.getCmp(this.port);
    __object.port = __comp.getValue();
    
    // set security
    __comp = Ext.getCmp(this.security);
    __object.security = __comp.getValue();
    
    // set user
    __comp = Ext.getCmp(this.user);
    __object.user = __comp.getValue();
    
    // set pass
    __comp = Ext.getCmp(this.pass);
    __object.pass = __comp.getValue();
    
    
    // set smtp server
    __comp = Ext.getCmp(this.smtpserver);
    __object.smtpserver = __comp.getValue();
    
    // set smtp port
    __comp = Ext.getCmp(this.smtpport);
    __object.smtpport = __comp.getValue();
    
    // set smtp security
    __comp = Ext.getCmp(this.smtpsecurity);
    __object.smtpsecurity = __comp.getValue();
    
    // set smtp user
    __comp = Ext.getCmp(this.smtpuser);
    __object.smtpuser = __comp.getValue();
    
    // set smtp pass
    __comp = Ext.getCmp(this.smtppass);
    __object.smtppass = __comp.getValue();
    
    return __object;
  },
  
  /**
   * handle ok button.
   */
  validate: function() {
    // get value.
    var __address = Ext.getCmp(this.address).getValue();
    var __desc = Ext.getCmp(this.desc).getValue();
    var __server = Ext.getCmp(this.server).getValue();
    var __user = Ext.getCmp(this.user).getValue();
    var __pass = Ext.getCmp(this.pass).getValue();
    var __port = Ext.getCmp(this.port).getValue();
    
    var __smtpuser = Ext.getCmp(this.smtpuser).getValue();
    var __smtppass = Ext.getCmp(this.smtppass).getValue();
    var __smtpserver = Ext.getCmp(this.smtpserver).getValue();
    var __smtpport = Ext.getCmp(this.smtpport).getValue();
    
    //delete spaces 
    var trim = function(str) {
      return str.replace(/^\s+|\s+$/g, "");
    };
    //check email address
    __address = trim(__address);
    if ('' == __address || __address.length < 0 || __address === null) {
      return iwebos.message.mail.error_empty_mail;
    }
    
    //check Description
    __desc = trim(__desc);
    if ('' == __desc || __desc.length < 0 || __desc === null) {
      return iwebos.message.mail.error_empty_content;
    }
    
    //check Server
    __server = trim(__server);
    if ('' == __server || __server.length < 0 || __server === null) {
      return iwebos.message.mail.error_empty_server;
    }
    
    //check user name
    __user = trim(__user);
    if ('' == __user || __user.length < 0 || __user === null) {
      return iwebos.message.mail.error_empty_receive_mail;
    }
    
    //check password
    __pass = trim(__pass);
    if ('' == __pass || __pass.length < 0 || __pass === null) {
      return iwebos.message.mail.error_empty_password;
    }
    
    //check port
    if ('' == __port || __port.length < 0 || __port === null) {
      return iwebos.message.mail.error_empty_port;
    }
    
    //check username stmp
    __smtpuser = trim(__smtpuser);
    if ('' == __smtpuser || __smtpuser.length < 0 || __smtpuser === null) {
      return iwebos.message.mail.error_empty_send_mail;
    }
    
    //check password stmp
    __smtppass = trim(__smtppass);
    if ('' == __smtppass || __smtppass.length < 0 || __smtppass === null) {
      return iwebos.message.mail.error_empty_password_send;
    }
    
    //check server stmp 
    __smtpserver = trim(__smtpserver);
    if ('' == __smtpserver || __smtpserver.length < 0 || __smtpserver === null) {
      return iwebos.message.mail.error_empty_server_send;
    }
    
    //check port stmp 
    if ('' == __smtpport || __smtpport.length < 0 || __smtpport === null) {
      return iwebos.message.mail.error_empty_port_send;
    }
    
    return null;
  },
  /**
   * handle cancel button.
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  },
  
  _fnSpecialKey: function(field, e) {
    if (e.getKey() == Ext.EventObject.ENTER) {
      this.okHandler();
    }
  }
});
