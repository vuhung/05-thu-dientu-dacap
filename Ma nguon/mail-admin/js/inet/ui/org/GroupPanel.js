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

iNet.iwebos.ui.org.GroupPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	// The ID of components
	this.prefix = (!this.prefix) ? 'manage-group' : this.prefix ;
	this.tabId = this.prefix + '-tab';
	this.organizationId = this.prefix + '-organization-id';
	this.gridId = this.prefix + '-grid-id';	
	
	// the toolbar to search
	this._toolbar = new Ext.app.SearchField({
		width: 200,		
		onTrigger2Click:this._fnSearch.createDelegate(this)
	});
	
	// the organization store 
	this._orgStore = new Ext.data.JsonStore({
		fields: [{name:'orgId', mapping:'id', type:'string'},
				 {name:'organization', mapping:'disname', type:'string'}]
	});
		
	// the organization combo
	var __orgSearch = new Ext.form.ComboBox({
		id: this.organizationId,
		store: this._orgStore,
		displayField: 'organization',
		valueField: 'orgId',
		width: 300,
		forceSelection: true,
		readOnly: false,
	    mode: 'local',
	    triggerAction: 'all',
	    emptyText: '',
	    selectOnFocus: false
	});
	
	// The toolbar panel
	var __toolbarPanel = new Ext.Panel({		
		frame: false,
		region: 'north',
		border: false,
		//layout: 'border',
		height: 25,
		tbar: [this._toolbar, __orgSearch]
	});
	
	var sortInfo = {
        field: 'name',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'name', 
		direction: 'ASC'
	};
	
	// The grid record
	var __record = Ext.data.Record.create([
		{name:'name', mapping:'name', type:'string'},
		{name:'description', mapping:'description', type:'string'}
	]);
	
	// the reader
	var __reader = new Ext.data.JsonReader({
		totalProperty: 'results',
		root: 'rows',
		id: 'name'
	}, __record);
	
	var __columnModel = new Ext.grid.ColumnModel([
		{header: iwebos.message.org.group_name, dataIndex:'name', width: 0.4},
		{header: iwebos.message.org.group_description, dataIndex:'description', width: 0.6}
	]);
	
	// the base parameter for searching
	var __baseParam = {iwct: 'loadGroupContent',iwa: 'view', action: 'search'};
	
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
        }
	});
	
	var __mainPanel = new Ext.Panel({
		region: 'center',
		layout: 'border',
		frame: false,
		border: false,
		items: [__toolbarPanel, this.grid]
	});
	__mainPanel.on('resize', this.__fnResizePanel, this);
	this._toolbar.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
	
	this.grid.on('contextmenu', this._contextMenu, this, {stopEvent:true});
    this.grid.on('rowcontextmenu', this._rowContextMenu, this, {stopEvent:true});
    this.grid.on('rowdblclick', this._onDoubleClick, this);
    __orgSearch.on('select', this._fnSearch, this, {stopEvent: true});
	
	// the actions
    this.actions = {
    	'group-create-new': this._onAddGroup.createDelegate(this)
    };
	
	iNet.iwebos.ui.org.GroupPanel.superclass.constructor.call(this, {
		id: this.tabId,
		layout: 'border',
		anchor: '100%',
		iconCls: 'icon-create-group',
		title: iwebos.message.org.group_management,
		//iconCls: '',
		header: false,
		enableTabScroll: true,
		autoScroll : true,
        frame: false,
        border: false,
        closable: true,
        layout: 'border',
        items: [__mainPanel]
	});

	this.on('activate',this.__fnResizePanel,this);
	
};

Ext.extend(iNet.iwebos.ui.org.GroupPanel, Ext.Panel, {

	/**
	 * set the organizations for panel
	 * @param {} organizations
	 */
	setOrganization: function(organizations) {
		// remove all old item
		this._orgStore.removeAll();
		this._orgStore.loadData(organizations);		
		this._orgStore.sort('organization');
		
		// get first record		
		var __first = this._orgStore.getAt(0);
		if(!!__first) {
			var __component = Ext.getCmp(this.organizationId);
			// select first record
			__component.setValue(__first.get('orgId'));
		}
		
		// search group
		this._fnSearch();
	},
	
	/**
	 * Search group
	 */
	_fnSearch: function() {
		var params = [];
		var __key = this._toolbar.getRawValue();
		params.push({'key':'key', 'value':__key});
		var __organization = Ext.getCmp(this.organizationId).getValue();
		params.push({'key':'org', 'value':__organization});
		
		// fire search event.
		this._toolbar.fireEvent('search', params) ;
	},
	
	/**
	 * get the selected organization
	 */
	getSelectedOrg: function() {
		return Ext.getCmp(this.organizationId).getValue();
	},
	
	/**
	 * Add new group 
	 */
	_onAddGroup: function() {
		var __mainTab = Ext.getCmp('manage-user-search-main-tab');
		var __tab = Ext.getCmp('manage-group-create-new-tab');
		if(!__tab) {
			__tab = new iNet.iwebos.ui.org.CreateGroupPanel();
			__mainTab.add(__tab).show();
		}		
		__mainTab.setActiveTab(__tab);
		__tab.loadData(Ext.getCmp(this.organizationId).getValue(), null);
		return __tab;
		//return _onOpenGroupTab();
	},
	
	/**
	 * Open group tab
	 * @param {} data
	 */
	_onOpenGroupTab: function(groupName) {
		var __mainTab = Ext.getCmp('manage-user-search-main-tab');
		var __tab = Ext.getCmp('manage-group-create-new-tab');
		if(!__tab) {
			__tab = new iNet.iwebos.ui.org.CreateGroupPanel();
			__mainTab.add(__tab).show();
		}		
		__mainTab.setActiveTab(__tab);
		__tab.loadData(Ext.getCmp(this.organizationId).getValue(), groupName);
		return __tab;
	},
	
	/**
	 * show context menu
	 * 
	 * @param {} event
	 */
	_contextMenu: function(event){
		event.stopEvent();
		this.mailMenu = new Ext.menu.Menu({
        	 items: [{
				id: 'account-menu-create-email-account',
				text:iwebos.message.org.group_create_group,
	            iconCls: 'icon-menu-group-add-new',
	            scope: this,
	            handler:this._onAddGroup.createDelegate(this)
            	}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
	},
	
	/**
     * DoubleClick on Grid
     * @param {Object} grid
     * @param {Object} index
     * @param {Object} e
     */
    _onDoubleClick: function(grid, index, e){
        e.stopEvent();
		var __selection = grid.store.getAt(index) ;
		var __data = __selection.data;
		this._onOpenGroupTab(__data.name);
    },
	
	/**
	 * update mail account
	 */
	_onUpdate: function() {
		// get the selected record
	 	var __record = this.grid.getSelectionModel().getSelections()[0];
	 	if(!__record) return;
	 	this._onOpenGroupTab(__record.data.name);
	 	
	},
	
	/**
	 * Delete group
	 */
	_onDelete: function() {
		var __store = this.grid.store;
		var __org = Ext.getCmp(this.organizationId).getValue();
    	
    	// get first selection
    	var __selection = this.grid.getSelectionModel().getSelections()[0];
    	if(!__selection) return;
    	
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
								title : iwebos.message.org.message,
								msg : iwebos.message.org.error_group_delete_group,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
    					}
    				};
    				// create the params
    				var params = {iwct:'groupContent', iwa:'READ_WRITE', action:'delete'};
    				var object = {};
    				object['name'] = __data.name;
    				object['org'] = __org;
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
    		};
    		// show confirmation.
       		Ext.MessageBox.confirm( iwebos.message.org.message, iwebos.message.org.message_group_warning_delete_group, onOk);
    	}	
	},
	
	/**
	 * show menu when select item
	 * 
	 * @param {} grid
	 * @param {} index
	 * @param {} event
	 */
	_rowContextMenu: function(grid, index, event){
    	// stop this event.
		event.stopEvent() ;
		grid.getSelectionModel().selectRow(index);
				
		var selection = grid.getSelectionModel().getSelections()[0];
		if(!!selection){
			this.mailMenu = new Ext.menu.Menu({
            items: [{
						id: 'manager-group-menu-add-group',
						text:iwebos.message.org.group_create_group,
		                iconCls: 'icon-menu-group-add-new',
		                scope: this,
		                handler:this._onAddGroup.createDelegate(this)
              		},{
						id: 'manager-group-menu-update-group',
						text:iwebos.message.org.group_update_group,
		                iconCls: 'icon-mail-update',
		                scope: this,
		                handler:this._onUpdate.createDelegate(this)
              		},{
						id: 'manager-group-menu-delete-group',
						text:iwebos.message.org.group_delete_group,
		                iconCls: 'icon-mail-del',
		                scope: this,
		                handler:this._onDelete.createDelegate(this)
              		}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
		}
	},
	
	/**
     * process action.
     */
    fnDoAction: function(event, target){
    	if(this.actions !== undefined && this.actions[target.id] !== undefined
    			&& typeof this.actions[target.id] == 'function'){
    		this.actions[target.id]() ;
    	}
    },
	
	/**
     * Resize panel
     */
    __fnResizePanel: function(panel){
        Ext.EventManager.fireWindowResize();
    }
});