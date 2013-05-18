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
iNet.iwebos.ui.org.SearchUserDialog = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  var sortInfo = {
    field: 'fullname',
    direction: 'ASC'
  };
  var defaultSort = {
    field: 'fullname',
    direction: "ASC"
  };
  
  var __baseParam = {
    iwct: 'searchUserContent',
    iwa: 'view',
    action: 'search'
  };
  var __record = Ext.data.Record.create([{
    name: 'id',
    mapping: 'uname',
    type: 'string'
  }, {
    name: 'uname',
    mapping: 'uname',
    type: 'string'
  }, {
    name: 'fullname',
    mapping: 'fullname',
    type: 'string'
  }, {
    name: 'title',
    mapping: 'title',
    type: 'string'
  }, {
    name: 'email',
    mapping: 'email',
    type: 'string'
  }]);
  // the reader
  var __reader = new Ext.data.JsonReader({
    totalProperty: 'results',
    root: 'rows',
    id: 'uname'
  }, __record);
  
  var sm = new Ext.grid.CheckboxSelectionModel();
  var __columnModel = new Ext.grid.ColumnModel([sm, {
    header: iwebos.message.org.user_account,
    dataIndex: 'uname',
    width: 0.2
  }, {
    header: iwebos.message.org.user_full_name,
    dataIndex: 'fullname',
    width: 0.3
  }, {
    header: iwebos.message.org.user_title,
    dataIndex: 'title',
    width: 0.2
  }, {
    header: iwebos.message.org.user_email,
    dataIndex: 'email',
    width: 0.3
  }]);
  
  // The grid contain users
  this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
    region: 'center',
    autoScroll: true,
    url: 'jsonajax',
    method: 'POST',
    baseParams: __baseParam,
    loadFirst: false,
    reader: __reader,
    sortInfo: sortInfo,
    dsort: defaultSort,
    cm: __columnModel,
    sm: sm,
    filter: {
      data: 'all'
    },
    view: new Ext.grid.GridView({
      forceFit: true
    })
  });
  
  // create the toolbar
  this.toolbar = new iNet.iwebos.ui.org.SearchUserToolbar({
    id: 'search-user-dialog-tool-id',
    region: 'north',
    width: '100%',
    height: 30,
    prefix: 'search-user-dialog-prefix',
    firstLoad: false,
    frame: false,
    border: false,
    store: this.grid.store
  });
  this.toolbar.on('search', this.grid.handleSearchEvent, this.grid, {
    stopEvent: true
  });
  
  var __main = new Ext.Panel({
    layout: 'border',
    region: 'center',
    frame: false,
    border: false,
    items: [this.toolbar, this.grid]
  });
  
  
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  this.cancelhScope = this.cancelhScope || this;
  this.grid.on("keypress", this.__handleKeyEvent, this);
  
  iNet.iwebos.ui.org.SearchUserDialog.superclass.constructor.call(this, {
    id: 'mail-dlgsearch-account',
    title: iwebos.message.org.user_find_user_in_org,
    iconCls: 'icon-email-search',
    region: 'center',
    anchor: '100%',
    width: 600,
    height: 500,
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
  
  this.toolbar.hideOrganization();
  this.toolbar.hideGroup();
};

Ext.extend(iNet.iwebos.ui.org.SearchUserDialog, iNet.iwebos.ui.common.dialog.TitleDialog, {
  ttitle: iwebos.message.org.user_select_user,
  btilte: iwebos.message.org.user_select_user,
  
  /**
   * Handle key event on grid
   *
   * @param {} event
   */
  __handleKeyEvent: function(event) {
    LotusService.handleKeyEventOnGrid(this.grid, event);
  },
  
  /**
   * set the action
   *
   * @param {Object} action
   */
  setData: function(organization, action) {
    this.toolbar.setDefaultOrg(organization);
    this.__action = action;
    this.toolbar.search();
  },
  
  /**
   * handle ok button.
   */
  fnOkHandler: function() {
    var selects = this.grid.getSelectionModel().getSelections();
    if (selects !== undefined && selects.length != 0) {
      if (this.__action.scope) {
        this.__action.fn.apply(this.__action.scope, [selects]);
      }
      this.destroy();
    }
    else {
      Ext.MessageBox.show({
        title: iwebos.message.org.message,
        msg: iwebos.message.org.message_group_select_user_for_group,
        buttons: Ext.MessageBox.OK,
        icon: Ext.MessageBox.ERROR
      });
    }
  },
  
  /**
   * handle cancel button.
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  }
  
});
