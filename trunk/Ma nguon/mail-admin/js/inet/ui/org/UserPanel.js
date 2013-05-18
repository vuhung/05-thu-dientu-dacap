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

iNet.iwebos.ui.org.UserPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	this.prefix = (!this.prefix) ? 'manage-user-search' : this.prefix ;
	this.mainId = this.prefix + '-main-tab';
	this.mainContendId = this.prefix + '-main-contend';
	this.toolbarId = this.prefix + '-toolbar';
	this.gridId = this.prefix + '-grid';
	this._owner = {};
	
	var sortInfo = {
        field: 'fullname',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'fullname', 
		direction: "ASC"
	};
	
	var __baseParam = {iwct: 'searchUserContent',iwa: 'view', action: 'search'};
	var __record = Ext.data.Record.create([
		{name:'id', mapping: 'uname', type: 'string'},
		{name:'uname', mapping: 'uname', type: 'string'},
		{name: 'fullname', mapping: 'fullname', type: 'string'},
		{name: 'title', mapping: 'title', type:'string'},
		{name: 'email', mapping: 'email', type: 'string'}
	]);
	// the reader
	var __reader = new Ext.data.JsonReader({
		totalProperty: 'results',
		root: 'rows',
		id: 'uname'
	}, __record);
	
	var __columnModel = new Ext.grid.ColumnModel([
		{header: iwebos.message.org.user_account, dataIndex:'uname', sortable: true, width: 0.2},
		{header: iwebos.message.org.user_full_name, dataIndex:'fullname', sortable: true, width: 0.3},
		{header: iwebos.message.org.user_title, dataIndex:'title', sortable: true, width: 0.2},
		{header: iwebos.message.org.user_alternate_email, dataIndex:'email', sortable: true, width: 0.3}
	]);
	
	// The grid contain users
	this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: this.gridId,
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
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        filter: {
            data: 'all'
        },
		view: new Ext.grid.GridView({
			forceFit: true
		})
	});
	
	// create the toolbar
	this.toolbar = new iNet.iwebos.ui.org.SearchUserToolbar({
		id: this.toolbarId,
		region: 'north',
		width: '100%',
		height: 30,
		frame: false,
		border: false,
		store: this.grid.store
	});
	this.toolbar.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
	
	var __main = new Ext.Panel({
		layout: 'border',
		region: 'center',
		frame: false,
		border: false,
		items: [this.toolbar, this.grid]
	});
	__main.on('resize', this._fnResize, this);
	
	//when row is right clicked
    this.grid.on('rowcontextmenu', this._onRowContext, this);
    this.grid.on('contextmenu', this._onContextMenu, this);
	this.grid.on('rowdblclick', this._onDoubleClick, this);
	
	// the actions
    this.actions = {
    	'user-create-new':this._onAddUser.createDelegate(this),
    	'group-management': this._onManageGroup.createDelegate(this),
    	'view-lotus-account': this._viewLotusAccount.createDelegate(this)
    };
	
	iNet.iwebos.ui.org.UserPanel.superclass.constructor.call(this, {
		id: this.mainId,
		activeTab: 0,
		region: 'center',
		width: '100%',
		enableTabScroll: true,
        autoScroll: true,
        frame: false,
        border: false,
        items: {
        	id: this.mainContendId, 
            title: iwebos.message.org.user_in_org,
            iconCls: 'icon-mail-home',
            layout: 'border',
            hideMode: 'offsets',
            items: [__main]
        }
	});
};
Ext.extend(iNet.iwebos.ui.org.UserPanel, Ext.TabPanel, {
	/**
	 * Open dialog to view lotus accounts
	 */
	_viewLotusAccount: function() {
		// open account dialog
		var dialog = new iNet.iwebos.ui.mail.DialogSearchAccount();
		dialog.show(this);
		dialog.disabledOkButton();
		dialog.setEnableDelChk(true, false);
	},
	
	/**
	 * add new user
	 */
	_onAddUser: function() {
		this.__openNewTab();
	},
	
	/**
	 * open new user tab 
	 */
	__openNewTab: function() {
		var __mainTab = Ext.getCmp(this.mainId);
		var __tab = Ext.getCmp('manage-user-create-new-tab');
		if(!__tab) {
			__tab = new iNet.iwebos.ui.org.CreateUserPanel();
			__mainTab.add(__tab).show();
		}
		__mainTab.setActiveTab(__tab);
		__tab.setInfo(this.toolbar.getSelectedOrg(), this.toolbar.getSelectedGroup());
		return __tab;
	},
	
	/**
	 * Open group management tab 
	 */
	_onManageGroup: function() {
		if(!this._owner.isLoadDROrg) {
			this._owner.isLoadDROrg = true;
			var __fnSuccess = function(result, request) {
				var __data = Ext.util.JSON.decode(result.responseText);
				var __success = (__data.success==undefined)?true:__data.success;
				if(__success && __data.length > 0) {
					this._owner.drOrgs = __data;
					this.__openGroupManagementTab(__data);				
				} else {
					Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.user_not_have_permition_to_user_function,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
				}
			} ;
			var __orgParam = {iwct:'loadMailDomainContent', iwa:'view', action:'loaddrs'};
			// request data
			iNet.Ajax.request({
				url: 'jsonajax',
				method: 'POST',
				params: __orgParam,
				scope: this,
				success: __fnSuccess,
				maskEl: this.bwrap
			});
		} else {
			if(this._owner.drOrgs == undefined) {
				Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.user_not_have_permition_to_user_function,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
				});
			} else {
				this.__openGroupManagementTab(this._owner.drOrgs);
			}
			
		}
		
	},
	
	/**
	 * open the group management tab
	 * @param {} organizations
	 */
	__openGroupManagementTab: function(organizations) {
		var __mainTab = Ext.getCmp(this.mainId);
		var __tab = Ext.getCmp('manage-group-tab');
		if(!__tab) {
			__tab = new iNet.iwebos.ui.org.GroupPanel();
			__mainTab.add(__tab).show();
			__tab.setOrganization(organizations);
		} 
		__mainTab.setActiveTab(__tab);		
		return __tab;
	},
	
	/**
	 * Add new group 
	 */
	_onAddGroup: function() {
		var __mainTab = Ext.getCmp(this.mainId);
		var __tab = Ext.getCmp('manage-group-create-new-tab');
		if(!__tab) {
			__tab = new iNet.iwebos.ui.org.CreateGroupPanel();
			__mainTab.add(__tab).show();
		}
		__mainTab.setActiveTab(__tab);
		return __tab;
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
		var __tab = this.__openNewTab();
		__tab.loadUser(this.toolbar.getSelectedOrg(), __record.data.uname);
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
     * Update exist account
     */
    _onUpdateAccount: function() {
    	var __record = this._getFirstSelection();
    	if(__record != null) {
    		var __tab = this.__openNewTab();
			__tab.loadUser(this.toolbar.getSelectedOrg(), __record.data.uname);
    	}	
    },
    
    /**
	 * open dialog to reset password
	 */
	_openResetPwdDialog: function(){
		var __record = this._getFirstSelection();
		if(__record != null) {
    		// open dialog to create/update domain
			var dlg = new iNet.iwebos.ui.account.DialogResetPwd();
			dlg.setData(this.toolbar.getSelectedOrg(),__record.data.fullname, __record.data.uname);    		
			dlg.show(this);
    	}		
	},
	
    /**
     * Delete account
     */
    _onDeleteAccount: function() {
    	// get first selection
    	var __selection = this._getFirstSelection();
    	var __data = __selection.data;
    	if(__data != null) {
    		this._fnCanDelete(__data);
    	}	
    },
    
    /**
     * check the user can be delete or not
     */
    _fnCanDelete: function(__user) {
    	var __fnSuccess = function(result, request) {
    		var __data = Ext.util.JSON.decode(result.responseText);
    		if(__data.success) {
    			if(__data.error == 'nopermit') {
    				Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.error_user_not_permit_delete_user,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
    			} else if(__data.error == 'noexist') {
    				Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.error_user_not_exist_in_org,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
    			} else {
    				this._fnOpenDeleteDialog(__user);
    			}
    		} else {
    			Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : iwebos.message.org.error_user_check_delete_user,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
    		}
    	};
    	var __params = {iwct: 'userContent',iwa: 'view', action: 'candelete'};
    	__params.uname = __user.uname;
    	__params.org = this.toolbar.getSelectedOrg();
    	// submit the request
    	iNet.Ajax.request({
    		url: 'jsonajax',
    		method: 'POST',
    		params: __params,
    		scope: this,
    		success: __fnSuccess,
    		maskEl: this.bwrap
    	});
    },
    
    /**
     * Open dialog to delete user
     */
    _fnOpenDeleteDialog: function(__data) {
    	// open dialog to delete user
    	var __dialog = new iNet.iwebos.ui.org.DeleteUserDialog();
		var __action = {fn: this.__fnDeleteUser, scope: this};
		__dialog.setData(__data.uname, __action);
		__dialog.show();
    },	
    
    /**
     * delete user
     * 
     * @param {} emails
     */
    __fnDeleteUser: function(uname, all, emails) {
    	var __fnSuccess = function(result, request) {
    		var __data = Ext.util.JSON.decode(result.responseText);
    		if(__data.success) {
    			var __selection = this._getFirstSelection();
    			this.grid.store.remove(__selection);
    		} else if(__data.error == 'manager'){
				Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : String.format(iwebos.message.org.message_user_can_not_delete_manager_group, uname, __data.group),
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				}); 		
    		} else {    				
    			Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : iwebos.message.org.error_user_delete_user,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
    		}
    	};
    	var __params = {iwct: 'userContent',iwa: 'view', action: 'delete'};
    	var __object = {};
    	__object.uname = uname;
    	__object.org = this.toolbar.getSelectedOrg();
    	__object.delall = all;
    	__object.list = emails;
    	__params.object = Ext.util.JSON.encode(__object);
    	// submit the request
    	iNet.Ajax.request({
    		url: 'jsonajax',
    		method: 'POST',
    		params: __params,
    		scope: this,
    		success: __fnSuccess,
    		maskEl: this.bwrap
    	});
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
                	id: 'account-menu-item-add',
					text: iwebos.message.org.user_add_new,
                    iconCls: 'icon-create-account',
                    handler: this._onAddUser.createDelegate(this)
				},{
                	id: 'account-menu-item-add-member',
					text: iwebos.message.org.user_add_member,
                    iconCls: 'icon-create-account',
                    handler: this._onAddMembers.createDelegate(this)
				},'-',{
                	id: 'account-menu-item-remove-member',
					text: iwebos.message.org.user_remove_member,
                    iconCls: 'icon-account-delete',
                    handler: this._onRemoveMember.createDelegate(this)
				},{
                	id: 'account-menu-item-update',
					text: iwebos.message.org.user_update,
                    iconCls: 'icon-account-update',
                    handler: this._onUpdateAccount.createDelegate(this)
				},{
					id: 'account-menu-item-reset-pwd',
					text: iwebos.message.org.user_reset_password,
                    iconCls: 'icon-account-reset-pass',
                    handler: this._openResetPwdDialog.createDelegate(this)
				},{
					id: 'account-menu-item-delete',
					text: iwebos.message.org.user_reset_delete,
                    iconCls: 'icon-account-delete',
                    handler: this._onDeleteAccount.createDelegate(this)
				}]
            });
        }
        
        // get the selected record
        var __selection = grid.store.getAt(index).data;
        //var __disable = !__selection.edit;
        var __disable = this.toolbar.getSelectedGroup() == '---';
        Ext.getCmp('account-menu-item-add-member').setDisabled(__disable);
        Ext.getCmp('account-menu-item-remove-member').setDisabled(__disable);
        
        this.menu.showAt(e.getXY());
    },
	
    /**
	 * handler context menu on grid
	 * 
	 * @param {} event
	 */
	_onContextMenu:function(event){
		event.stopEvent();
		if(!this.contextMenu) {
			this.contextMenu = new Ext.menu.Menu({
                items: [{
                	id: 'account-menu-item-add-ct',
					text: iwebos.message.org.user_add_new,
                    iconCls: 'icon-create-account',
                    handler: this._onAddUser.createDelegate(this)
				},{
                	id: 'account-menu-item-add-member-ct',
					text: iwebos.message.org.user_add_member,
                    iconCls: 'icon-create-account',
                    handler: this._onAddMembers.createDelegate(this)
				}]
            });
		}
		var __disable = this.toolbar.getSelectedGroup() == '---';
        Ext.getCmp('account-menu-item-add-member-ct').setDisabled(__disable);
        
		this.contextMenu.showAt(event.getXY());
	},	
	
	/**
	 * add new member for group
	 */
	_onAddMembers: function() {
		var __dialog = new iNet.iwebos.ui.org.SearchUserDialog();
		var __action = {fn:this._fnHandleAddMembers, scope:this};
		__dialog.setData(this.toolbar.getSelectedOrg(), __action);
		__dialog.show();
	},
	
	/**
	 * Adding new member
	 */
	_fnHandleAddMembers: function(selectionRecords) {
		var __unames = [];
		var __datas = [];
		for(var __index = 0; __index < selectionRecords.length; __index++) {
			var __selection = selectionRecords[__index].data;
			__datas[__index] = __selection; 
			__unames[__index] = {uname: __selection.uname};
		}
		
		var __fnSuccess = function(result, request) {
			var __data = Ext.util.JSON.decode(result.responseText);
			if(__data.success) {
    			this._fnAddDataToGrid(__datas);
    		} else {
    			Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : iwebos.message.org.error_user_add_member_to_group,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
    		}
		};
		var __params = {iwct:'groupContent', iwa:'READ_WRITE', action: 'addmember'};
		__params.members = Ext.util.JSON.encode(__unames);
		__params.name = this.toolbar.getSelectedGroup();
		__params.org = this.toolbar.getSelectedOrg();
		// submit the request
    	iNet.Ajax.request({
    		url: 'jsonajax',
    		method: 'POST',
    		params: __params,
    		scope: this,
    		success: __fnSuccess,
    		maskEl: this.bwrap
    	});
	},	
	
	/**
	 * remove member out of group
	 */
	_onRemoveMember: function() {
		// get first selection
    	var __selection = this._getFirstSelection();
    	var __user = __selection.data
    	if(__user != null) {
    		var __fnSuccess = function(result, request) {
				var __data = Ext.util.JSON.decode(result.responseText);
				if(__data.success) {
					if(__data.error == 'noexist') {
						Ext.MessageBox.show({
							title : iwebos.message.org.message,
							msg : iwebos.message.org.error_user_not_exist_in_group,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
					}else if(__data.error == 'manager') {
						Ext.MessageBox.show({
							title : iwebos.message.org.message,
							msg : iwebos.message.org.message_user_can_not_remove_manager_group_out_group,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
					} else {
						this.grid.store.remove(__selection);
					}
	    		} else {
	    			Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.error_user_remove_member_out_group,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
	    		}
			};
    	}
    	var __params = {iwct:'groupContent', iwa:'READ_WRITE', action: 'removemember'};
		__params.uname = __user.uname;
		__params.name = this.toolbar.getSelectedGroup();
		__params.org = this.toolbar.getSelectedOrg();
		// submit the request
    	iNet.Ajax.request({
    		url: 'jsonajax',
    		method: 'POST',
    		params: __params,
    		scope: this,
    		success: __fnSuccess,
    		maskEl: this.bwrap
    	});
	},	
	
	/**
	 * add data to grid
	 * @param {} datas
	 */
	_fnAddDataToGrid: function(datas) {
		for(var __index= 0; __index < datas.length; __index++) {
			this.grid.store.addData(datas[__index]);
		}	
	},	
	
	/**
	 * handle action panel when tab change 
	 */
	handlePanel: function(panel, active, object) {
		if(!panel) return;		
		
		var __object = object || {};
		var __active = active || this.mainContendId;
		var __bd = panel.body;
		
		// disable panel.
		if (panel._fnDisabledAll && typeof panel._fnDisabledAll == 'function') {
			panel._fnDisabledAll();
		}
		
		if(__active == this.mainContendId) {
			__bd.select('li[id="li-action-create-user"]').setDisplayed(true);
			__bd.select('li[id="li-action-manage-group"]').setDisplayed(true);
			__bd.select('li[id="li-action-view-lotus-account"]').setDisplayed(true);
		} else if(__active == 'manage-user-create-new-tab') {
			__bd.select('li[id="li-action-create-user"]').setDisplayed(true);
			__bd.select('li[id="li-action-user-save"]').setDisplayed(true);
			__bd.select('li[id="li-action-user-search"]').setDisplayed(true);
		} else if(__active == 'manage-group-tab') {
			__bd.select('li[id="li-action-create-group"]').setDisplayed(true);			
		} else if(__active == 'manage-group-create-new-tab') {
			__bd.select('li[id="li-action-create-group"]').setDisplayed(true);
			__bd.select('li[id="li-action-group-save"]').setDisplayed(true);			
		}
	},
	
	/**
	 * Handle action
	 * 
	 * @param {} event
	 * @param {} target
	 */
	fnDoAction: function(event, target){
		var __mainTab = Ext.getCmp(this.mainId);
		event.stopEvent();
		
		// get active tab.
		var tab = __mainTab.getActiveTab(), activeTabId = tab.getId() || null;
		if ((activeTabId != null) && ((activeTabId == 'manage-user-create-new-tab')) 
			|| (activeTabId == 'manage-group-tab') || (activeTabId == 'manage-group-create-new-tab')) {
			tab.fnDoAction(event, target);
		} else if(this.actions !== undefined && this.actions[target.id] !== undefined
    			&& typeof this.actions[target.id] == 'function'){
    		this.actions[target.id]() ;
    	}
    	
    },
    
	/**
	 * handle resize event.
	 */
	_fnResize: function(panel){
		Ext.EventManager.fireWindowResize();
	}
});