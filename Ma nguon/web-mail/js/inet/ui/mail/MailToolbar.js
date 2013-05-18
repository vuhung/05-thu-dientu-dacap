/*****************************************************************
 Copyright 2008 by Hien Nguyen Van (hiennguyen@truthinet.com.vn)
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
/**
 * @class iNet.iwebos.ui.mail.EmailToolbar
 * @extends Ext.Panel
 *
 * Create the Toolbar, that user allow user works with mail messags
 */
iNet.iwebos.ui.mail.Toolbar = Ext.extend(Ext.Panel, {

  /**
   * @cfg {Store} store - the given paging store.
   */
  /**
   * Initialization the Toolbar from the given configuration.
   */
  initComponent: function() {
    // set the id prefix.
    this.prefix = (!this.prefix) ? 'iwebos-mail-toolbar-' : this.prefix;
    
    // create expand button.
    this.id = this.nextId('cmb', 6);
    
    // create delete button bar.
    //this.chDeleteId = this.nextId('chbtn', 6) ;
    this.chDeleteId = this.prefix + 'mail-toolbar-btn-delete';
    this.menuReply = this.prefix + 'mail-toolbar-menu-reply';
    this.forwardId = this.prefix + 'mail-toolbar-forward';
    this.replyId = this.prefix + 'mail-toolbar-reply';
    this.replyAllId = this.prefix + 'mail-toolbar-reply-all';
    this.spamId = this.prefix + 'mail-toolbar-spam';
    var __deleteButton = {
      xtype: 'button',
      id: this.chDeleteId,
      iconCls: 'icon-email-trash',
      text: iwebos.message.mail.mail_delete,
      tooltip: iwebos.message.mail.mail_delete_tip,
      handler: this._deleteEmail,
      //disabled:true,
      scope: this
    };
    
    // create reply button.
    
    var __replyMenu = {
      id: this.menuReply,
      text: iwebos.message.mail.mail_reply,
      tooltip: iwebos.message.mail.mail_reply_tip,
      iconCls: 'icon-email-reply-button',
      menu: {
        id: 'toolbar-reply-menu',
        cls: 'reading-menu',
        width: 150,
        items: [{
          text: iwebos.message.mail.filter_object_sender,
          handler: this._replyEmail,
          scope: this,
          checked: false,
          group: 'rp-group-reply',
          iconCls: 'icon-email-reply-button'
        }, {
          text: iwebos.message.mail.filter_object_all,
          handler: this._replyAllEmail,
          scope: this,
          checked: false,
          group: 'rp-group-reply',
          iconCls: 'icon-email-reply_all'
        }]
      }
    };
    
    var __replyButton = {
      xtype: 'button',
      id: this.replyId,
      iconCls: 'icon-email-reply-button',
      text: iwebos.message.mail.mail_reply,
      tooltip: iwebos.message.mail.mail_reply_tip,
      handler: this._replyEmail,
      scope: this
    };
    
    var __replyAllButton = {
      xtype: 'button',
      id: this.replyAllId,
      iconCls: 'icon-email-reply_all',
      text: iwebos.message.mail.mail_reply_all,
      tooltip: iwebos.message.mail.mail_reply_all_tip,
      handler: this._replyAllEmail,
      scope: this
    };
    
    // create forward button.
    
    var __forwardButton = {
      xtype: 'button',
      id: this.forwardId,
      iconCls: 'icon-email-forward-button',
      text: iwebos.message.mail.mail_forward,
      tooltip: iwebos.message.mail.mail_forward_tip,
      handler: this._forwardEmail,
      scope: this
    };
    
    // create print button.
    this.printBtnId = this.nextId('printbtn', 6);
    var __printButton = {
      xtype: 'button',
      id: this.printBtnId,
      iconCls: 'icon-email-printer',
      text: iwebos.message.mail.mail_print,
      tooltip: iwebos.message.mail.mail_print_tip,
      handler: loadPrintPage,
      scope: this
    };
    
    // create spam button.
    var __spamButton = {
      xtype: 'button',
      id: this.spamId,
      iconCls: 'icon-email-spam-button',
      text: iwebos.message.mail.mail_spam,
      tooltip: iwebos.message.mail.mail_spam,
      handler: this._fnSpamMail,
      scope: this
    };
    
    this.__viewMenu = new Ext.Toolbar.Button({
      text: iwebos.message.mail.mail_view + '-' + iwebos.message.mail.search_all,
      tooltip: iwebos.message.mail.mail_view,
      iconCls: 'icon-email-view',
      menu: {
        id: 'toolbar-view-menu',
        cls: 'reading-menu',
        width: 150,
        items: [{
          id: 'iwebos-mail-toolbar-view-all',
          text: iwebos.message.mail.search_all,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewAll,
          iconCls: 'icon-email-view',
          scope: this
        }, {
          text: iwebos.message.mail.mail_unread,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewUnread,
          iconCls: 'icon-email-unread',
          scope: this
        
        }, {
          text: iwebos.message.mail.mail_priority_high,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewImportant,
          iconCls: 'icon-email-urgent',
          scope: this
        
        }, {
          text: iwebos.message.mail.mail_attched,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewAttach,
          iconCls: 'icon-email-attachment',
          scope: this
        }, '-', {
          text: iwebos.message.mail.context_flag_work,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewWork,
          iconCls: 'icon-email-work',
          scope: this
        }, {
          text: iwebos.message.mail.context_flag_personal,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewPersonal,
          iconCls: 'icon-email-personal',
          scope: this
        
        }, {
          text: iwebos.message.mail.context_flag_todo,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewToDo,
          iconCls: 'icon-email-todo',
          scope: this
        }, {
          text: iwebos.message.mail.context_flag_late,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewLate,
          iconCls: 'icon-email-late',
          scope: this
        }, {
          text: iwebos.message.mail.context_flag_phone,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewCall,
          iconCls: 'icon-email-phone',
          scope: this
        }, {
          text: iwebos.message.mail.context_flag_reply,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewReply,
          iconCls: 'icon-email-reply',
          scope: this
        }, {
          text: iwebos.message.mail.context_flag_approve,
          checked: false,
          group: 'view-group',
          checkHandler: this._fnViewReview,
          iconCls: 'icon-email-approve_mail',
          scope: this
        }]
      }
    });
    
    var __moreMenu = {
      text: iwebos.message.mail.mail_group,
      tooltip: iwebos.message.mail.mail_group_tip,
      iconCls: 'icon-email-address_book_16',
      handler: this.moreAction,
      menu: {
        id: 'toolbar-reading-menu',
        cls: 'reading-menu',
        width: 150,
        items: [{
          text: iwebos.message.mail.mail_more_action_groupbyday,
          checked: true,
          group: 'rp-group',
          checkHandler: this._groupBySentDate,
          scope: this,
          iconCls: 'preview-bottom'
        }, {
          text: iwebos.message.mail.mail_more_action_groupbyfrom,
          checked: false,
          group: 'rp-group',
          checkHandler: this._groupBySender,
          scope: this,
          iconCls: 'preview-right'
        }, {
          text: iwebos.message.mail.mail_more_action_groupbysubject,
          checked: false,
          group: 'rp-group',
          checkHandler: this._groupBySubject,
          scope: this,
          iconCls: 'preview-right'
        }]
      }
    };
    var tbar = [];
    if (this.showmybutton) {
      // create the search bar.		
      tbar = [__deleteButton, '-', __replyButton, '-', __replyAllButton, '-', __forwardButton, '-', __spamButton, '-', __printButton];
    }
    else {
      tbar = [__deleteButton, '-', __replyButton, '-', __replyAllButton, '-', __forwardButton, '-', __spamButton, '-', __moreMenu, this.__viewMenu];
    }
    
    
    // setting up the paging bar.
    if (this.store) {
      // create paging bar.
      this.pagingBar = new Ext.PagingToolbar({
        pageSize: iNet.INET_PAGE_LIMIT,
        store: this.store,
        displayInfo: false
      });
      
      // user running on firefox 2.
      if (Ext.isGecko2) {
        tbar[tbar.length] = '-';
      }
      else {
        tbar[tbar.length] = '->';
      }
      
      tbar[tbar.length] = this.pagingBar;
    }
    
    // setting up tbar.
    this.tbar = tbar;
    
    // create toolbar.
    iNet.iwebos.ui.mail.Toolbar.superclass.initComponent.call(this);
    
    /**
     * Add change mode event.
     */
    this.addEvents(    /**
     * @event changemode
     *
     * Fires when the mode is changed.
     * @param {boolean} previousMode - the previous mode.
     * @param {boolean} mode - the current mode.
     */
    'changemode',    /**
     * @event search
     *
     * Fires when the user pressed search button.
     * @param {Array} params - the search parameter or empty array.
     */
    'search');
    
    // caputure change mode event.
    this.on('changemode', this.onChangeMode, this);
    this.on('afterlayout', this.onAfterLayout, this);
  },
  /**
   * render the search bar.
   */
  afterRender: function() {
    // call super class.
    iNet.iwebos.ui.mail.Toolbar.superclass.afterRender.call(this);
  },
  
  /**
   * process after layout event.
   */
  onAfterLayout: function(container, layout) {
    //this.bind(this.mode) ;
  },
  
  /**
   * Change mode.
   */
  changeMode: function() {
  
    // fire change mode event.		
    this.fireEvent('changemode', !this.mode, this.mode);
  },
  
  /**
   * Collects the search data and fire the search event.
   */
  search: function() {
    // create search parameter.
    var params = this.getQuickContent(this.mode);
    
    if (this.mode) {
      // push search mode.
      params.push({
        'key': 'mode',
        'value': 'advancedSearch'
      });
      
      // search advanced.
      var details = this.advanced.getSearchContent();
      
      // set empty arrays.
      if (!details) 
        details = [];
      
      // search details params.
      var index = 0;
      for (index = 0; index < details.length; index++) {
        params.push(details[index]);
      }
    }
    else {
      // push search mode.
      params.push({
        'key': 'mode',
        'value': 'quickSearch'
      });
    }
    
    // fire search event.
    this.fireEvent('search', params);
  },
  
  /**
   * Return the identifier from the given initialize identifier.
   *
   * @param {String} gen - the given general identifier.
   * @param {integer} len - the given initialize identifier.
   */
  nextId: function(gen, len) {
    var time = String(new Date().getTime()).substr(len);
    var s = 'abcdefghijklmnopqrstuvwxyz';
    for (var index = 0; index < len; index++) {
      time += s.charAt(Math.floor(Math.random() * 26));
    }
    
    // get identifier.		
    return this.prefix + '-' + gen + '-' + time + '-id';
  },
  
  /**
   * open composer forward email tab
   */
  _forwardEmail: function() {
    this.fireEvent('forward');
  },
  
  /**
   * open composer reply email tab
   */
  _replyEmail: function() {
    this.fireEvent('reply');
  },
  
  /**
   * open composer reply all email tab
   */
  _replyAllEmail: function() {
    this.fireEvent('replyall');
  },
  
  _deleteEmail: function() {
    this.fireEvent('delete');
  },
  
  _fnSpamMail: function() {
    this.fireEvent('spam');
  },
  
  _groupBySentDate: function() {
    Ext.getCmp('email-message-grid-id').store.groupBy('received');
  },
  
  _groupBySender: function() {
    Ext.getCmp('email-message-grid-id').store.groupBy('sender');
  },
  
  _groupBySubject: function() {
    Ext.getCmp('email-message-grid-id').store.groupBy('subject');
  },
  
  /**
   * setting disabled/enabled forward button
   * @param {Object} disabled : boolean
   */
  disabledForward: function(disabled) {
    var component = Ext.getCmp(this.forwardBtnId);
    component.setDisabled(disabled);
  },
  
  _fnViewAll: function() {
    this.updateIconView('icon-email-view');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.search_all);
    
    if (this.__lock) {
      this.__lock = false;
      return;
    }
    
    var __store = this.store;
    var __params = __store.baseParams;
    __params['field'] = 'all';
    __store.baseParams = __params;
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  },
  
  _fnViewUnread: function() {
    this.updateIconView('icon-email-unread');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.mail_unread);
    var __store = this.store;
    var __params = __store.baseParams;
    __params['field'] = 'read';
    __params['key'] = 'N';
    
    __store.baseParams = __params;
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  },
  
  _fnViewImportant: function() {
    this.updateIconView('icon-email-urgent');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.mail_priority_high);
    
    var __store = this.store;
    var __params = __store.baseParams;
    __params['field'] = 'priority';
    __params['key'] = 'HIGH';
    
    __store.baseParams = __params;
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  },
  
  _fnViewWork: function() {
    this.updateIconView('icon-email-work');
    
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_work);
    this._fnViewByFlag('WORK');
  },
  _fnViewPersonal: function() {
    this.updateIconView('icon-email-personal');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_personal);
    this._fnViewByFlag('PERSONAL');
  },
  _fnViewToDo: function() {
    this.updateIconView('icon-email-todo');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_todo);
    this._fnViewByFlag('TODO');
  },
  _fnViewLate: function() {
    this.updateIconView('icon-email-late');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_late);
    this._fnViewByFlag('LATE');
  },
  _fnViewCall: function() {
    this.updateIconView('icon-email-phone');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_phone);
    this._fnViewByFlag('CALL');
  },
  _fnViewReply: function() {
    this.updateIconView('icon-email-reply');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_reply);
    this._fnViewByFlag('REPLY');
  },
  _fnViewReview: function() {
    this.updateIconView('icon-email-approve_mail');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.context_flag_approve);
    this._fnViewByFlag('REVIEW');
  },
  _fnViewByFlag: function(flag) {
    // set active mail tab
    var __store = this.store;
    var __params = __store.baseParams;
    
    __params['field'] = 'flag';
    __params['key'] = flag;
    __store.baseParams = __params;
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  },
  
  _fnViewAttach: function() {
    this.updateIconView('icon-email-attachment');
    this.updateFilterView(iwebos.message.mail.mail_view + '-' + iwebos.message.mail.mail_attched);
    var __store = this.store;
    var __params = __store.baseParams;
    __params['field'] = 'attached';
    __params['key'] = 'Y';
    __store.baseParams = __params;
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  },
  
  updateFilterView: function(text) {
    this.__viewMenu.setText(text);
  },
  
  /**
   * update icon button view
   * @param {} css
   */
  updateIconView: function(css) {
    this.__viewMenu.setIconClass(css);
  },
  
  /**
   * check view all item
   */
  checkSearchAll: function() {
    this.__lock = true;
    var __checkItem = Ext.getCmp('iwebos-mail-toolbar-view-all');
    if (__checkItem) {
      __checkItem.setChecked(true);
    }
  },
  
  searchAdvance: function(params) {
    this.updateIconView('icon-email-attachment');
    var __store = this.store;
    var __params = __store.baseParams;
    Ext.apply(__store.baseParams || {}, params);
    __store.load({
      params: {
        start: 0,
        limit: iNet.INET_PAGE_LIMIT
      }
    });
  }
});
