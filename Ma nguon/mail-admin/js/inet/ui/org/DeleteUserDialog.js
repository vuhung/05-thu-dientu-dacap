/*****************************************************************
 Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)
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
iNet.iwebos.ui.org.DeleteUserDialog = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  this.prefix = (!this.prefix) ? 'delete-user-dialog' : this.prefix;
  this.deleteEmailCheckId = this.prefix + '-delete-email-check-id';
  this.notDeleteEmailCheckId = this.prefix + '-not-delete-email-check-id';
  this.deleteAllEmailCheckId = this.prefix + '-delete-all-email-check-id';
  this._owner = {};
  
  var __selectEmailRadio = new Ext.form.Radio({
    id: this.deleteEmailCheckId,
    boxLabel: iwebos.message.org.user_select_delete_email_from_grid,
    name: 'radio-delete-email-group',
    anchor: '100%',
    checked: false
  });
  var __topPanel = new Ext.form.FormPanel({
    region: 'north',
    frame: true,
    border: false,
    autoHeight: true,
    anchor: '100%',
    layout: 'form',
    bodyStyle: 'padding: 2px;',
    items: [{
      border: false,
      html: iwebos.message.org.user_delete_user_out_org_and_email
    }, {
      xtype: 'radiogroup',
      labelSeparator: '',
      columns: 1,
      items: [{
        id: this.deleteAllEmailCheckId,
        boxLabel: iwebos.message.org.user_delete_all_email,
        name: 'radio-delete-email-group',
        anchor: '100%',
        checked: true
      }, {
        id: this.notDeleteEmailCheckId,
        boxLabel: iwebos.message.org.user_non_delete_email,
        name: 'radio-delete-email-group',
        anchor: '100%',
        checked: false
      }, __selectEmailRadio]
    }]
  });
  
  var sortInfo = {
    field: 'uid',
    direction: 'DESC'
  };
  var defaultSort = {
    field: 'uid',
    direction: "DESC"
  };
  
  // email record.
  var mailRecord = Ext.data.Record.create([{
    name: 'id',
    mapping: 'email',
    type: 'string'
  }, {
    name: 'uid',
    mapping: 'uid',
    type: 'string'
  }, {
    name: 'symbol',
    mapping: 'symbol',
    type: 'string'
  }, {
    name: 'domain',
    mapping: 'domain',
    type: 'string'
  }]);
  
  var reader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'list',
    id: 'uid'
  }, mailRecord);
  
  var sm = new Ext.grid.CheckboxSelectionModel();
  var columnModel = new Ext.grid.ColumnModel([sm, {
    header: iwebos.message.mail.account,
    dataIndex: 'uid',
    fixed: true,
    width: 200
  }, {
    header: '@',
    dataIndex: 'symbol',
    fixed: true,
    width: 24
  }, {
    header: iwebos.message.mail.domain,
    dataIndex: 'domain',
    width: 100
  }]);
  
  // show color in row.
  var fnRowClass = function(record) {
    var read = record.data.read;
    return '';
  };
  
  var __params = {
    iwct: 'loadMailAccountContent',
    iwa: 'READ_WRITE',
    action: 'loadall'
  };
  
  // create email message grid.
  this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
    id: 'delete-user-dialog-grid',
    region: 'center',
    anchor: '100%',
    autoScroll: true,
    border: true,
    url: 'jsonajax',
    method: 'POST',
    baseParams: __params,
    loadFirst: false,
    sortInfo: sortInfo,
    reader: reader,
    rowClass: fnRowClass,
    dsort: defaultSort,
    cm: columnModel,
    sm: sm,
    filter: {
      data: 'all'
    },
    view: new Ext.grid.GridView({
      forceFit: true
    })
  });
  
  var __main = new Ext.Panel({
    layout: 'border',
    region: 'center',
    frame: false,
    border: false,
    items: [__topPanel, this.grid]
  });
  
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  this.cancelhScope = this.cancelhScope || this;
  
  this.grid.setDisabled(true);
  __selectEmailRadio.on('check', this.onSelectEmail, this);
  
  iNet.iwebos.ui.org.DeleteUserDialog.superclass.constructor.call(this, {
    id: 'delete-user-dialog',
    title: iwebos.message.org.user_delete_user,
    iconCls: 'icon-mail-del',
    region: 'center',
    anchor: '100%',
    width: 520,
    height: 470,
    modal: true,
    frame: true,
    layout: 'border',
    hideMode: 'offsets',
    items: [__main],
    buttons: [{
      text: iwebos.message.org.ok,
      iconCls: 'ok-btn',
      handler: this.okHandler,
      scope: this.okhScope
    }, {
      text: iwebos.message.mail.cancel,
      iconCls: 'cancel-btn',
      handler: this.cancelHandler,
      scope: this.cancelhScope
    }]
  });
};

Ext.extend(iNet.iwebos.ui.org.DeleteUserDialog, iNet.iwebos.ui.common.dialog.TitleDialog, {
  ttitle: iwebos.message.org.user_select_email_to_delete,
  btilte: iwebos.message.org.user_select_email_to_delete,
  
  /**
   * load all user's email accounts
   *
   * @param {} userName
   */
  __onLoadEmail: function(userName) {
    if (this._owner.load != true && userName != undefined && userName != '') {
      this._owner.load = true;
      var __param = [{
        key: 'uname',
        value: userName
      }];
      this.grid.handleSearchEvent(__param);
    }
  },
  
  /**
   * Select mail account to load
   *
   * @param {} radio
   * @param {} value
   */
  onSelectEmail: function(radio, value) {
    this.__onLoadEmail(this._owner.uname);
    this.grid.setDisabled(!value);
  },
  
  /**
   * set data for dialog
   * @param {} uname
   */
  setData: function(uname, action) {
    this._owner.uname = uname;
    this._owner.action = action;
  },
  
  /**
   * handle ok button.
   */
  fnOkHandler: function() {
    var emails = [];
    var __all = Ext.getCmp(this.deleteAllEmailCheckId).getValue();
    if (!__all && Ext.getCmp(this.deleteEmailCheckId).getValue()) {
      var selects = this.grid.getSelectionModel().getSelections();
      if (selects !== undefined && selects.length != 0) {
        for (var __index = 0; __index < selects.length; __index++) {
          emails[__index] = {
            email: selects[__index].data.id
          };
        }
      }
    }
    if (this._owner.action.scope) {
      this._owner.action.fn.apply(this._owner.action.scope, [this._owner.uname, __all, emails]);
    }
    this.destroy();
  },
  
  /**
   * handle cancel button.
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  }
});
