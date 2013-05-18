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
 * @class iNet.iwebos.ui.mail.DialogCreateDomain
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateDomain
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogCreateDomain = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	this.prefix = 'mail-admin';
	this.domainNameId = this.prefix + '-domain-name-id';
	this.domainDescId = this.prefix + '-domain-desc-id';
	this.editAccountId = this.prefix + '-edit-account-id';
	
	var sortInfo = {
        field: 'uname',
        direction: 'DESC'
    };
	var defaultSort = {
		field: 'uname', 
		direction: "DESC"
	};
	
    // owner domain record.
    var ownerRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'uname', type: 'string'}, 
	{name: 'uname', mapping: 'uname', type: 'string'},
	{name: 'lname', mapping: 'lname', type: 'string'},
	{name: 'mname', mapping: 'mname', type: 'string'},
	{name: 'fname', mapping: 'fname', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, ownerRecord);
	
	var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.create.username,
			dataIndex: 'uname',
			fixed: true,
			width: 150
		},{
			header: iwebos.message.mail.create.lastname,
			dataIndex: 'lname',
			fixed: true,
			width: 120
		},{
			header: iwebos.message.mail.create.middlename,
			dataIndex: 'mname',
			fixed: true,
			width: 150
		},{
			header: iwebos.message.mail.create.firstname,
			dataIndex: 'fname',
			fixed: true,
			width: 150
		}
	]);
	
	this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: 'new-admin-grid-id',
		region: 'center',
		anchor: '100%',
		minSize: 150,
		autoScroll: true,
		height:170,
		url: 'jsonajax',
		method: 'POST',
		loadFirst : false,
		sortInfo: sortInfo,		
		tbar: [{
			id: 'account-add-button',
            xtype: 'button',
			text: iwebos.message.mail.domain_add_manager,
			border: false,
			iconCls:'icon-mail-add',
            handler:this._fnSelectUser.createDelegate(this)
		}, {
			id: 'account-delete-button',
            xtype: 'button',
			text: iwebos.message.mail.domain_delete_manager,
			border: false,
			iconCls:'icon-mail-del',
            handler:this._onRemoveUser.createDelegate(this)
		}],
		reader: reader,
		dsort: defaultSort,
		cm: columnModel,
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
		filter: {
			data: 'all'
		}
	});
	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [{
            columnWidth: 0.9,
            border: false,
            layout: 'form',
            anchor: '100%',
            items: [{
                id: this.domainNameId,
				fieldLabel: iwebos.message.mail.domain,
                xtype: 'textfield',
                border: false,
                anchor: '100%'
            },{
                id: this.domainDescId,
				fieldLabel: iwebos.message.mail.domain_desc,
                xtype: 'textfield',
                border: false,
                anchor: '100%'
            },{
				border: false,
				html: '<br/><b>' + iwebos.message.mail.domain_list_domain_manager + '</b><br/><br/>'
			}, this.grid
			,{
				id : this.editAccountId,
				xtype: 'checkbox',
				boxLabel: iwebos.message.mail.enable_modify_email,
				hideLabel: true,
				checked: true
			}]
        }, {
            layout: 'fit',
            border: false,
            anchor: '100%',
            items: [{}]
        }]
    });
    
	
	//when row is right clicked
	this.grid.on('rowcontextmenu', this._onRowContext, this, {stopEvent:true});
	this.grid.on('contextmenu', this._onContextMenu, this , {stopEvent:true});
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogCreateDomain.superclass.constructor.call(this, {
        id: 'mail-create-domain-dialog',
        title: iwebos.message.mail.create_domain,
        iconCls: 'icon-email-domain',
        region: 'center',
        anchor: '100%',
        width: 600,
        height: 500,
        modal: true,
        frame: true,
		resizable: false,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
        buttons: [{
            text: iwebos.message.mail.create.save,
            iconCls: 'ok-btn',
			scope: this,
            handler: this.fnOkHandler
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
			scope: this,
            handler: this.fnCancelHandler
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogCreateDomain, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.create_domain,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.create.input_domain_info,
	
	/**
	 * set data for dialog
	 * 
	 * @param {Object} id - the id of domain name
	 * @param {Object} action - the action
	 */
	setData: function(id, action) {
		this.__owner = {id:id,action:action};
		if(id) {
			this.loadData(id);
		}
	},
	
	/**
	 * load domain data
	 * 
	 * @param {Object} id - the domain name
	 */
	loadData: function(id) {
		var baseParams = {iwct:'loadMailDomainContent', iwa:'view', action:'load'} ;
		// the domain name to load
		baseParams['text'] = id;
		
		// handle success event
		fnSuccess = function(result, request) {
			var data = result.responseText;
			data = eval('(' + data + ')');
			if(data) {
				// fill domain data				
				var component = Ext.getCmp(this.domainNameId);
				component.setValue(data.text);
				component.setDisabled(true);
				
				//fill description for this domain
				component = Ext.getCmp(this.domainDescId);
				component.setValue(data.desc);
				
				//load all info of owner into grid
				var __store = this.grid.store;
				// remove all old record
				__store.removeAll();
				var __owners = data.list;
				
				if(__owners != null && __owners != undefined) {
					for(var __index = 0; __index < __owners.length; __index++) {
						var __owner = __owners[__index];
						__owner.id  = __owner.uname;
						__store.addData(__owner);
					}
				}		
								
				component = Ext.getCmp(this.editAccountId);
				component.setValue(data.edit);				
			}
		};
		
		// request data from server
		iNet.Ajax.request({
			url: 'jsonajax',
			params: baseParams,
			method: 'POST',
			scope: this,
			success: fnSuccess
		});
	},
	
	/**
	 * return the data object
	 */
	getData: function() {
		// create object to store data.
		var object = {} ;
		
		object['id'] = this.__owner.id;	
		
		var __comp = Ext.getCmp(this.domainNameId);
		// get the domain name
		var __domainName = __comp.getValue().trim().toLowerCase();
		__comp.setValue(__domainName);
		object['text'] = __domainName;
		
		//get description for this domain
		__comp = Ext.getCmp(this.domainDescId);
		object['desc'] = __comp.getValue();
		
		// list owner domain
    	var __store = this.grid.store;
		var __count = __store.getCount();
    	var __list = [];
    	var __item = {};
		if(__count > 0){
			for(var __index = 0; __index < __count; __index++){
				__item = __store.getAt(__index).data;
				__list[__index] = {uname: __item.uname}; 
			}
		}
		
		//set the list onwer domain into the object data
		object['list'] = __list;
		
		//all owner can edit/create email
		__comp = Ext.getCmp(this.editAccountId);
		object['edit'] = __comp.getValue();
		
		return object;		
	},
	
	/**
	 * saving domain
	 */
	__onSave: function() {
		// get the data object
		var object = this.getData();
		
		// get action [save]
		var __save = this.__owner.action['save'];
		if(!__save) return;
		
		if(!__save.scope) {
			__save.fn(object);
		} else {
			__save.fn.apply(__save.scope, [object]);
		}		
	},
	
	/**
	 * Remove select account
	 */
	_onRemoveUser: function(){
		// get the selection item 
		var __record = this.grid.getSelectionModel().getSelections()[0];
		if(!!__record)
			this.grid.store.remove(__record); 
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
    	//check the menu was created, if no then new instance
        //if ( this.menuRowAdmin == undefined || this.menuRowAdmin == null) {
		if (! this.menuRowAdmin){
            this.menuRowAdmin = new Ext.menu.Menu({
                id: 'grid-ctx',
                items: [{
                	id: 'account-add-menu',
					text: iwebos.message.mail.domain_add_manager,
                    iconCls: 'icon-mail-add',
					 scope: this,
                    handler: this._fnSelectUser.createDelegate(this)
				},{
					id: 'account-delete-menu',
					text: iwebos.message.mail.domain_delete_manager,
                    iconCls: 'icon-mail-del',
					scope: this,
                    handler: this._onRemoveUser.createDelegate(this)
				}]
            });
        }
        this.menuRowAdmin.showAt(e.getXY());
    },
	
    /**
	 * handler context menu on grid
	 * 
	 * @param {} event
	 */
	_onContextMenu:function(event){
		event.stopEvent();
		//check the menu was created, if no then new instance
		if (! this.menuContextAdmin) {
			this.menuContextAdmin = new Ext.menu.Menu({
				items: [{
					id: 'account-add-menu-context',
					text: iwebos.message.mail.domain_add_manager,
					iconCls: 'icon-mail-add',
					scope: this,
					handler: this._fnSelectUser.createDelegate(this)
				}]
			});
		}
		// show menu.
		this.menuContextAdmin.showAt(event.getXY());
	},

    /**
     * handle ok button.
     */
    fnOkHandler: function(){
    	// get value.
		var __domainName = Ext.getCmp(this.domainNameId).getValue();
		var __count = this.grid.store.getCount();
		
		if(''===__domainName|| 0 ==__count){
			Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : iwebos.message.mail.error_input_info,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return false;
		} 
		
		this.__onSave();
		this.destroy();
    },
	
	_fnSelectUser: function(){
		var dialog = new iNet.iwebos.ui.mail.DialogSearchAccount();
		var action = {fn:this._handleSelectUser.createDelegate(this), scope:this};
		// set action for dialog
		dialog.__setAction(action);
		dialog.show(this);
	},
	
	/**
	 * handle select user
	 */
	_handleSelectUser: function(__object) {
		//check data
		if (__object==null || __object ==  undefined){
			return;
		}
		
		//store of grid
		var __store = this.grid.store;
		
		// get the old record
	 	var __item = __store.getById(__object.uname);
		
		//this item is not contained in this grid
		if (__item == null || __item == undefined){
			__object.id = __object.uname;
			__store.addData(__object);
		}
	},
	
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});
