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
 * @class iNet.iwebos.ui.account.AccountPanel
 * @extends Ext.TabPanel
 * @constructor
 * Creates a Tab Panel for calendar
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.account.AccountPanel = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
    var sortInfo = {
        field: 'fname',
        direction: 'DESC'
    };
	var defaultSort = {
		field: 'fname', 
		direction: "DESC"
	};
	
	// The base params
	var baseParams = {
        iwct: 'searchMailAsAccountContent',       
        iwa: 'view',
		id: 0,
        action: 'search'
    };

    // email record.
    var _record = Ext.data.Record.create([
		{name: 'id', mapping: 'code', type: 'string'}, 
		{name: 'code', mapping: 'code', type: 'string'},
		{name: 'fname', mapping: 'displayname', type: 'string'},		 
		{name: 'email', mapping: 'email', type: 'string'},
		{name: 'edit', mapping: 'edit', type: 'boolean'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'code'
    }, _record);
    
    var columnModel = new Ext.grid.ColumnModel([{
        header: iwebos.message.mail.create.firstname,
        dataIndex: 'fname',
        width: 0.35
    },
	{
        header: iwebos.message.mail.create.email,
        dataIndex: 'email',
        width: 0.4
    }]);
    
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'account-grid',
        region: 'center',
        anchor: '100%',
        autoScroll: true,       
        url: 'jsonajax',        
        method: 'POST',
        baseParams: baseParams,
        loadFirst: false,
        sortInfo: sortInfo,
        reader: reader,
		dsort: defaultSort,
        cm: columnModel,
        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        filter: {
            data: 'all'
        },
		view: new Ext.grid.GridView({
			forceFit: true
		})
    });
    
    //when row is right clicked
    this.grid.on('rowcontextmenu', this._onRowContext, this);
    this.grid.on('contextmenu', this._onContextMenu, this);
    this.grid.on('rowdblclick', this._onDoubleClick, this);
	
	//var _toolBar = new iNet.iwebos.ui.mail.ToolbarSearchAccount({
	this.toolBar = new iNet.iwebos.ui.account.AccountToolbar({
		id: 'account-main-tabs-toolbar',
        region: 'north',
        width: '100%',
        height : 30,
        frame: false,
        border: false,
        store: this.grid.store
    });
    
    this.toolBar.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
	
    //main grid
    this.gridMain = new Ext.Panel({
        layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        items: [this.toolBar,this.grid]
    });
    
    this.main = new Ext.Panel({
        layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        items: [this.gridMain]
    });
    
    // the actions
    this.actions = {
    	'account-creat-new':this._onAddAccount.createDelegate(this),    	
    	'account-reset-pass': this._openResetPwdDialog.createDelegate(this)
    };
    
    iNet.iwebos.ui.account.AccountPanel.superclass.constructor.call(this, {
        id: 'account-main-tabs',
        activeTab: 0,
        region: 'center',
        anchor: '100%',
        enableTabScroll: true,
        autoScroll: true,
        frame: false,
        border: false,
        items: {
			id:'main-manager-account',
            title: iwebos.message.account.manageAccount,
            iconCls: 'icon-mail-home',
            layout: 'border',
            hideMode: 'offsets',
            items: [this.main]
        }
    });
};
Ext.extend(iNet.iwebos.ui.account.AccountPanel, Ext.TabPanel, {
	/***
	 * handle
	 * @param {Object} panel
	 * @param {Object} active
	 * @param {Object} object
	 */
	handlePanel: function(panel, active, object){
		if (!panel) 
			return;
		
		// setup the data.
		var __object = object ||
		{};
		var __active = active || 'main-manager-account';
		//var __id = __object.id;
		var __bd = panel.body;
		
		// disable panel.
		if (panel._fnDisabledAll && typeof panel._fnDisabledAll == 'function') {
			panel._fnDisabledAll();
		}
		__bd.select('li[id="li-action-create"]').setDisplayed(true);
		__bd.select('li[id="li-action-statistics"]').setDisplayed(true);
		if (__active === 'new-account') {
			__bd.select('li[id="li-action-search"]').setDisplayed(true);
			__bd.select('li[id="li-action-save"]').setDisplayed(true);
		}
	},
	/**
	 * active panel
	 * @param {Object} panel
	 */
	onActive : function(panel){
		this.active = true;
	},
	
	/**
	 * handler context menu on grid
	 * 
	 * @param {} event
	 */
	_onContextMenu:function(event){
		event.stopEvent();
	},	
	
	/**
     * Handle even when right click on grid
     * 
     * @param {Object} grid - the grid
     * @param {Object} index - the selected index
     * @param {Object} e - the event
     */
    _onRowContext: function(grid, index, e){
    	e.stopEvent();
    	grid.getSelectionModel().selectRow(index);
    	
        if (!this.menu) {
            this.menu = new Ext.menu.Menu({
                id: 'grid-ctx',
                items: [{
                	id: 'account-menu-item-update',
					text: iwebos.message.account.updateAccount,
                    iconCls: 'icon-account-update',
                    handler: this._onUpdateAccount.createDelegate(this)
				},{
					id: 'account-menu-item-reset-pwd',
					text: iwebos.message.account.resetPassword,
                    iconCls: 'icon-account-reset-pass',
                    handler: this._openResetPwdDialog.createDelegate(this)
				},'-',{
					id: 'account-menu-item-lock',
					text: iwebos.message.account.lockAccount,
                    iconCls: 'icon-account-lock',
                	handler: this._fnLockAccount.createDelegate(this)		
				},{
					id: 'account-menu-item-unlock',
					text: iwebos.message.account.unlockAccount,
                    iconCls: 'icon-account-unlock',
                	handler: this._fnUnlockAccount.createDelegate(this)		
				},{
					id: 'account-menu-item-delete',
					text: iwebos.message.account.deleteAccount,
                    iconCls: 'icon-account-delete',
                    handler: this._onDeleteAccount.createDelegate(this)
				}]
            });
            this.menu.on('hide', this.onContextHide, this);
        }
        
        // get the selected record
        var __selection = grid.store.getAt(index).data;
        //var __disable = !__selection.edit;
        var __disable = false;
        Ext.getCmp('account-menu-item-update').setDisabled(__disable);
        Ext.getCmp('account-menu-item-delete').setDisabled(__disable);
        Ext.getCmp('account-menu-item-reset-pwd').setDisabled(__disable);
        
        this.menu.showAt(e.getXY());
    },
    
    /**
     * Handle mouse double click on table
     * 
     * @param {} grid - the grid 
     * @param {} index - the index
     * @param {} e - the event
     */
    _onDoubleClick: function(grid, index, e){
    	e.stopEvent();
		var __record = grid.store.getAt(index);
		var __tab = this._fnNewTab();
		__tab.loadData(__record.id);
    },	
    /**
     * Add new account
     */
    _onAddAccount: function() {
    	var __tab = this._fnNewTab();
    },	
    
    /**
     * Update exist account
     */
    _onUpdateAccount: function() {
    	var __record = this._getFirstSelection();
    	if(__record != null) {
    		var __tab = this._fnNewTab();
			__tab.loadData(__record.id);
    	}	
    },
    
    /**
     * get first selection on table
     */
    _getFirstSelection: function() {
    	var selections = this.grid.getSelectionModel().getSelections();
    	if(selections == null || selections.length == 0) return null; 
    	return selections[0];
    },
    /**
	 * open dialog to reset password
	 */
	_openResetPwdDialog: function(){
		var __record = this._getFirstSelection();
		if(__record != null) {
    		// open dialog to create/update domain
			var dlg = new iNet.iwebos.ui.account.DialogResetPwd();
			dlg.setData(__record.data.fname, __record.data.code);    		
			dlg.show(this);
    	}		
	},
	
	/**
	 * Handle lock account
	 */
	_fnLockAccount: function() {
		this._fnHandlerAccount(false);
	},
	
	/**
	 * Handle unlock account
	 */
	_fnUnlockAccount: function() {
		this._fnHandlerAccount(true);
	},
	
	/**
	 * Handle lock/unlock account
	 */
	_fnHandlerAccount: function(active) {
		// get first selection
    	var __selection = this._getFirstSelection();
    	var __data = __selection.data;
    	if(__data != null) {
    		var __onOk = function(answer) {
    			// when accept warning
    			if(answer == 'ok' || answer == 'yes') {
    				// handle when locking success
    				var __fnSuccess = function(response, options) {
    					var __result = eval('(' + response.responseText + ')');
    					if(__result.success) {
    						Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.account.taskSuccessfull,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
    					} else {
    						Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.account.taskError,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
    					}
    				};
    			}
    			// create base param
    			var __baseParams = {iwct:'mailAccountContent', iwa:'READ_WRITE', action:'activeAll'};
    			var object = {};
    			object['code'] = __data.code;
    			object['active'] = active;
    			__baseParams['object'] = Ext.util.JSON.encode(object);
    			
    			// submit request
    			iNet.Ajax.request({
    				url: 'jsonajax',
    				method: 'POST',
    				params: __baseParams,
    				success: __fnSuccess,
    				scope: this    						
    			});
    		};
    		var __warning = active?iwebos.message.account.unlockAccountConfirm:iwebos.message.account.lockAccountConfirm;
    		// show confirmation.
       		Ext.MessageBox.confirm( iwebos.message.account.infomationMessage, __warning, __onOk);
    	}
	},
	
    /**
     * Delete account
     */
    _onDeleteAccount: function() {
    	var __store = this.grid.store;
    	
    	// get first selection
    	var __selection = this._getFirstSelection();
    	var __data = __selection.data
    	if(__data != null) {
    		var onOk = function(answer) {
    			if(answer == 'ok' || answer == 'yes') {
    				// handle delete successfully
    				var fnSuccess = function(response, options) {
    					// get the response text from server
    					var result = eval('(' + response.responseText + ')');
    					if(result.success) {
    						// remove the selected record
    						__store.remove(__selection);
    					} else {
    						Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.account.deleteAccountError,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
    					}
    				};
    				// create the params
    				var params = {iwct:'mailAccountContent', iwa:'READ_WRITE', action:'deleteAll'};
    				var object = {};
    				object['code'] = __data.code;
    				params['object'] = Ext.util.JSON.encode(object);
    					
    				// submit request
    				iNet.Ajax.request({
    					url: 'jsonajax',
    					method: 'POST',
    					params: params,
    					success: fnSuccess,
    					scope: this
    				});
    			}	
    		}	
    		 // show confirmation.
       Ext.MessageBox.confirm( iwebos.message.account.infomationMessage,iwebos.message.account.deleteAccountConfirm, onOk);
    	}	
    },
    
	/**
     * hide context menu
     */
    onContextHide: function(){
        if (this.ctxRow) {
            Ext.fly(this.ctxRow).removeClass('x-node-ctx');
            this.ctxRow = null;
        }
    },
    
	/**
     * handle click action on action panel.
     *
     * @param {Object} event - the given event to be handle.
     * @param {Object} target - the target raised event.
     */
    doAction: function(event, target){
       var mainTab = Ext.getCmp('account-main-tabs');
		// stop event.
		event.stopEvent();
		
		// get active tab.
		var tab = mainTab.getActiveTab(), activeTabId = tab.getId() || null;
		if ((activeTabId != null) && (activeTabId == 'new-account')) {
			tab.doAction(event, target);
		}
		else { // process action on main tab.    	
			if (this.actions !== undefined) {
				this.actions[target.id]();
			}
		}
    },
    
	/**
	 * open dialog to add user to organization
	 */
	_dlgAdd : function(){
		var dialog = new iNet.iwebos.ui.account.DialogAddAccount();
		dialog.show(this);
	},
	//-----------------------------------------------------------
	_fnNewTab: function(){
		// get current tab.
		var __mainTab = Ext.getCmp('account-main-tabs');
		var __tab = Ext.getCmp('new-account');
		if (!__tab) {
			__tab = new iNet.iwebos.ui.account.CreateAccount();
			__mainTab.add(__tab).show();
		}
		__mainTab.setActiveTab(__tab);
		
		// return tab.
		return __tab;
	}
	
});