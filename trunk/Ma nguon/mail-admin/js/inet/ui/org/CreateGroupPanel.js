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

iNet.iwebos.ui.org.CreateGroupPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	// The ID of components
	this.prefix = (!this.prefix) ? 'manage-group-create-new' : this.prefix ;
	this.tabId = this.prefix + '-tab';
	this.nameId = this.prefix + '-group-name';
	this.managerPanelId = this.prefix + 'manager-panel-id';
	this.memberPanelId = this.prefix + 'member-panel-id';
	this.descriptionId = this.prefix + '-group-description';
	this.organizationId = this.prefix + '-organization-id';
	this.contentId=this.prefix+ '-content-id';
	this._owner = {};
	
	var __managerPanel = new iNet.iwebos.ui.org.MemberGridPanel({
		id: this.managerPanelId,
		height: 150
	});
	this.managerGrid = __managerPanel.grid;
	
	var __memberPanel = new iNet.iwebos.ui.org.MemberGridPanel({
		id: this.memberPanelId,
		region: 'center'
	});
	this.memberGrid = __memberPanel.grid;
	
	// The group information contend panel
	var __contendPanel = new Ext.Panel({
		id: this.contentId,
		region: 'center',
		frame: false,
		border: false,
		anchor: '100%',
		//bodyStyle: 'padding: 5px;',
		autoHeight: true,
		items: [{
			border: false,
			html: '<div><b>' + iwebos.message.org.group_information + '</b></div>'
		}, {
			layout: 'column',
			frame: false,
			border: false,
			anchor: '100%',
			items: [{
				layout: 'form',
				bodyStyle: 'padding: 5px;',
				anchor: '100%',
				border: false,
				columnWidth: 0.5,
				labelWidth: 75,
				items: [{
					id: this.nameId,
					xtype: 'textfield',
					anchor: '95%',
					fieldLabel: iwebos.message.org.group_name
				}]
			},{
				layout: 'form',
				bodyStyle: 'padding: 5px;',
				anchor: '100%',
				border: false,
				columnWidth: 0.5,
				labelWidth: 75,
				items: [{
					id: this.organizationId,
					xtype: 'textfield',
					anchor: '95%',
					readOnly: true,
					fieldLabel: iwebos.message.org.organization
				}]
			}]
		},{
			layout: 'form',
			bodyStyle: 'padding: 5px;',
			anchor: '100%',
			labelWidth: 75,
			border: false,
			items: [{
				id: this.descriptionId,
				xtype: 'textfield',
				anchor: '97.5%',
				fieldLabel: iwebos.message.org.group_description
			}]
		}, {
			border: false,
			html: '<b>' + iwebos.message.org.group_manager + '</b><br />'
		}, {
			layout: 'form',
			border: false,
			anchor: '100%',			
			items: [__managerPanel]
		},{
			border: false,
			html: '<b>' + iwebos.message.org.group_member + '</b><br />'
		},__memberPanel]
	});

	__contendPanel.on('resize', this.__resizePanel, this);

	// the actions
    this.actions = {
    	'group-create-new': this._onAdd.createDelegate(this),
    	'group-save': this._onSave.createDelegate(this)    	
    };
	
	iNet.iwebos.ui.org.CreateGroupPanel.superclass.constructor.call(this, {
		id: this.tabId,
		layout:'border',
        iconCls: 'icon-menu-group-add-new',
        title: iwebos.message.org.group,
        anchor: '100%',
        header: false,
		enableTabScroll: true,
		autoScroll : true,
        frame: false,
        border: false,
        closable: true,
        items: [__contendPanel]
	});
	
	this.on('activate',this.__resizePanel,this);
};

Ext.extend(iNet.iwebos.ui.org.CreateGroupPanel, Ext.Panel, {
	
	/**
	 * validate the input data
	 */
	__validate: function() {
		// validate organization
		var __component = Ext.getCmp(this.organizationId);
		if(__component.getValue() == '') {
			return iwebos.message.org.message_group_select_org;
		}
		
		// validate the name of group
		__component = Ext.getCmp(this.nameId);
		if(__component.getValue() == '') {
			return iwebos.message.org.message_group_input_group_name;
		}
		
		// validate the manager of group
		if(this.managerGrid.store.getCount() == 0) {
			return iwebos.message.org.message_group_select_group_manager;
		}
		
		return '';
	},
	
	/**
	 * Get the data on GUI
	 */
	__getData: function() {		
		var __object = {};
		
		var __component = Ext.getCmp(this.organizationId);
		__object['org'] = __component.getValue();
		
		__component = Ext.getCmp(this.nameId);
		__object['name'] = __component.getValue();
		
		__component = Ext.getCmp(this.descriptionId);
		__object['description'] = __component.getValue();
		
		var __managers = Ext.getCmp(this.managerPanelId).getDatas();
		if(__managers != null) __object['managers'] = __managers;
		
		var __members = Ext.getCmp(this.memberPanelId).getDatas();
		if(__members != null) __object['members'] = __members;
		
		return __object;
	},
	
	/**
	 * Fill data on GUI
	 * @param {} data
	 */
	__fillData: function(data) {
		var __component = Ext.getCmp(this.organizationId);
		__component.setValue(this._owner.org);
		
		__component = Ext.getCmp(this.nameId);
		__component.setValue(data.name);
		
		__component = Ext.getCmp(this.descriptionId);
		__component.setValue(data.description);
		
		Ext.getCmp(this.managerPanelId).addDatas(data.managers);
		Ext.getCmp(this.memberPanelId).addDatas(data.members);
	},
	
	/**
	 * Fill data on GUI
	 */
	__cleanData: function() {
		var __component = Ext.getCmp(this.organizationId);
		__component.setValue(this._owner.org);
		
		__component = Ext.getCmp(this.nameId);
		__component.setDisabled(false);
		__component.setValue('');
		
		__component = Ext.getCmp(this.descriptionId);
		__component.setValue('');
		
		Ext.getCmp(this.memberPanelId).removeAll();
		Ext.getCmp(this.managerPanelId).removeAll();
	},
	
	/**
	 * Add new group
	 */
	_onAdd: function() {
		this._owner.action = 'save';
		this.__cleanData();
	},
	
	/**
	 * Load group information and fill on GUI
	 * @param {} organization
	 * @param {} groupName
	 */
	loadData:  function(organization, groupName) {
		this._owner.org = organization;
		Ext.getCmp(this.organizationId).setValue(organization);
		Ext.getCmp(this.managerPanelId).setDefaultOrg(organization);
		Ext.getCmp(this.memberPanelId).setDefaultOrg(organization);				
		if(groupName == undefined || groupName == null || groupName == '') {
			// this case: add new group
			this._owner.action = 'save';
			this.__cleanData();
			return;
		}
		
		this._owner.action = 'update';
		Ext.getCmp(this.nameId).setDisabled(true);
		// handle after loading group successfully
		var __fnSuccess = function(result, request) {
			var __data =  Ext.util.JSON.decode(result.responseText);
			var __success = (__data.success == undefined) ? true : __data.success;
			if (__success) {
				// fill data on GUI
				this.__fillData(__data);
			} else {
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.error_group_error_load_group,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}
		};
		
		var __baseParams = {iwct: 'loadGroupContent',iwa: 'READ_WRITE',action: 'load'};
		__baseParams.name = groupName;
		__baseParams.org = organization;
		// submit the request
		iNet.Ajax.request({
	 		url: 'jsonajax',
	 		params: __baseParams,
	 		method: 'POST',
	 		scope: this,
	 		success: __fnSuccess,
	 		maskEl: this.bwrap
	 	});
	},
	
	/**
	 * saving group
	 */
	_onSave: function() {
		var __action = (this._owner.action != undefined && this._owner.action != null)?this._owner.action:'save';
		// validate user information on GUI
		var valid = this.__validate();		
		if(valid != '') {
			Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : valid,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return;
		}
		// get user information
		var __data = this.__getData();		
		var __fnSuccess = function(result, request) {
			var __result =  Ext.util.JSON.decode(result.responseText);			
			var __success = (__result.success == undefined) ? true : __result.success;
			if (__success) {				
				this.__handleAfterSaving();
				// get the group management tab
				var __groupManagementTab = Ext.getCmp('manage-group-tab');
				if(__groupManagementTab != undefined && __groupManagementTab != null) {
					var __org = Ext.getCmp(this.organizationId).getValue();
					if(__groupManagementTab.getSelectedOrg() == __org) {
						var __store = __groupManagementTab.grid.store;
						if(__action == 'save') {
							// add group to grid on group management tab
							__store.addData(__data);							
						} else {
							// get the old record on group management tab
							var __oldRecord = __store.getById(__data.name);
							if(__oldRecord != undefined && __oldRecord != null) {
								// update information
								__oldRecord.data.name = __data.name;
								__oldRecord.data.description = __data.description;
								__oldRecord.commit();
							}
						}	
					}
					
				}
				// show successful message
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.message_group_saving_successful,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.INFO
				});
			} else {
				// show error message
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.error_group_error_saving_data,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}
		};
		
		var __params = {iwct:'groupContent', iwa:'READ_WRITE', action: __action} ;
		__params.object = Ext.util.JSON.encode(__data);
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			scope: this,
			method: 'POST',
			success: __fnSuccess,
			failure: function(result, request){
			},
			maskEl: this.bwrap
		});
		
	},
	
	/**
	 * Handle GUI after saving
	 */
	__handleAfterSaving: function() {
		this._owner.action = 'update';
		Ext.getCmp(this.nameId).setDisabled(true);
	},
	
	/**
	 * set the default org
	 * @param {} org
	 */
	setDefaultOrg: function(org) {
		this._owner.defaultOrg = org;
		Ext.getCmp(this.organizationId).setValue(org);
		Ext.getCmp(this.managerPanelId).setDefaultOrg(org);
		Ext.getCmp(this.memberPanelId).setDefaultOrg(org);
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
    __resizePanel: function(panel){
        Ext.EventManager.fireWindowResize();
		var component=Ext.getCmp(this.memberPanelId);
		component.setHeight(this.getSize().height-285);
		component=Ext.getCmp(this.contentId);
    }
});