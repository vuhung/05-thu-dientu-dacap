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
 * @class iNet.iwebos.ui.mail.CreateAlias
 * @extends Ext.Panel
 * @constructor
 * Creates a new Panel
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.CreateAlias = function(viewer, config){
	// setting the prefix identifier.
	this.prefix = (!this.prefix) ? 'iwebos-mail-create' : this.prefix;
	
	this.usernameId = this.prefix + '-alias-uid';
	this.passwordId = this.prefix + '-alias-password';
	this.confirmPasswordId = this.prefix + '-alias-cfmpassword';
	this.aliasDomainName = this.prefix + '-alias-domain';
	this.lastNameId = this.prefix + '-alias-lastname';
	this.firstNameId = this.prefix + '-alias-fistname';
	this.checkSMTP = this.prefix + '-alias-check-smtp';
	this.checkActive = this.prefix + '-alias-check-active';
	this.checkVirus = this.prefix + '-alias-check-virus';
	this.checkSpam = this.prefix + '-alias-check-spam';
	this.cmdSave = this.prefix + '-alias-save-button';
	this.cmdNew = this.prefix + '-alias-new-alias-button';
	this.addMaildrop = this.prefix + '-add-maildrop-button';
	this.addOutsideEmail = this.prefix + '-add-outside-email-button';
	this.removeMaildrop = this.prefix + '-remove-maildrop-button';
	this.__owner = {};
	
	var sortInfo = {
        field: 'email',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'email', 
		direction: "ASC"
	};
    
    // email record.
    var mailRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'email', type: 'int'}, 
	{name: 'email', mapping: 'email', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.create.username,
			dataIndex: 'email',
			fixed: false,
			width:100
		}
	]);
    
	
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: 'new-alias-grid-id',
		region: 'center',
		anchor: '100%',
		minSize: 150,
		autoScroll: true,
		url: 'jsonajax',
		method: 'POST',
		loadFirst : false,
		sortInfo: sortInfo,		
		tbar: [{
			id: this.addMaildrop,
			xtype: 'button',
			text: iwebos.message.mail.add_email_account,
			iconCls: 'icon-mail-add',
			scope:this,
			handler: this._onOpenSearchDialog.createDelegate(this)
		},{
			id: this.addOutsideEmail,
			xtype: 'button',
			text: iwebos.message.mail.add_outside_email_account,
			iconCls: 'icon-mail-add',
			scope:this,
			handler: this._openInputEmailDialog.createDelegate(this)
		},{
			id: this.removeMaildrop,
			xtype: 'button',
			text: iwebos.message.mail.delete_email_account,
			iconCls: 'icon-mail-del',
			scope:this,
			handler: this._onRemoveEmail.createDelegate(this)
		}],
		reader: reader,
		dsort: defaultSort,
		cm: columnModel,
		filter: {
			data: 'all'
		},
		view: new Ext.grid.GridView({
			forceFit: true
		})
	});
	
    // create main panel.
    var __newAlias = new Ext.form.FormPanel({
		id: 'alias-new-info-id',
        region: 'center',
        frame: false,
        border: false,
		autoHeight: true,
        anchor: '100%',
		labelAlign: 'right',
		bodyStyle: 'padding: 5px;',
		items: [{
			border: false,
			html: iwebos.message.mail.create.account_info
		},{
			layout: 'column',
			columnWidth: 1,
			anchor: '100%',
			border: false,
			items:[{
				columnWidth: 0.5,
				layout: 'form',
				labelWidth: 90,
				anchor: '100%',
				border: false,
				items:[{
					id: this.usernameId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 1,
					fieldLabel: iwebos.message.mail.create.username
				},{
					id:this.passwordId,
					xtype: 'textfield',
					inputType: 'password',
					tabIndex: 2,
					fieldLabel: iwebos.message.mail.create.password,
					anchor: '95%'
				},{
					id: this.lastNameId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 4,
					fieldLabel: iwebos.message.mail.create.lastname
				}]
			},{
				columnWidth: 0.5,
				layout: 'form',
				anchor: '100%',
				labelWidth: 70,
				border: false,
				items:[{
					id: this.aliasDomainName,
					xtype: 'textfield',
					anchor: '98%',
					vtype:'domain',
					disabled:true,					
					fieldLabel: iwebos.message.mail.create.domain
				},{
					id:this.confirmPasswordId,
					xtype: 'textfield',
					inputType: 'password',
					tabIndex: 3,
					fieldLabel: iwebos.message.mail.create.confirmpassword,
					anchor: '98%'
				},{
					id: this.firstNameId,
					xtype: 'textfield',
					anchor: '98%',
					tabIndex: 5,
					fieldLabel: iwebos.message.mail.create.firstname
				}]
			}]
		 },{
			border: false,
			html: iwebos.message.mail.create.role_mail
		},{
			layout: 'column',
			columnWidth: 1,
			anchor: '98%',
			border: false,
			items:[{
				columnWidth: 0.5,
				layout: 'form',
				labelWidth: 90,
				anchor: '100%',
				bodyStyle:'padding-left:90px;',
				border: false,
				items:[{
					id : this.checkSMTP,
					xtype: 'checkbox',
					hideLabel:true,
					tabIndex: 6,
					boxLabel: iwebos.message.mail.create.role_smtp
				},{
					id : this.checkActive,
					xtype: 'checkbox',
					hideLabel:true,
					checked : true,
					tabIndex: 8,
					boxLabel: iwebos.message.mail.create.role_active
				}]
			},{
				columnWidth: 0.5,
				layout: 'form',
				anchor: '100%',
				bodyStyle:'padding-left:70px;',
				border: false,
				items:[{
					id : this.checkVirus,
					xtype: 'checkbox',
					hideLabel:true,
					tabIndex: 7,
					boxLabel: iwebos.message.mail.create.role_virus
				},{
					id : this.checkSpam,
					xtype: 'checkbox',
					hideLabel:true,
					tabIndex: 9,
					boxLabel: iwebos.message.mail.create.role_spam
				}]
			}]
		},{
			border: false,
			html: iwebos.message.mail.create.group_member
		}],
		 tbar:[{
		 		id : this.cmdSave,
		 		xtype: 'button',
				iconCls: 'icon-save',
				text:iwebos.message.mail.create.save,
				scope: this,
				handler: this._onSave.createDelegate(this)
		 	},'|',{
		 		id : this.cmdNew,
		 		xtype: 'button',
				iconCls: 'mail-action-alias',
				text:iwebos.message.mail.create_alias_account,
				scope: this,
				handler: this._cleanData.createDelegate(this)
		 	}]
	});
    
	
    // create panel.
   this.toppanel = new Ext.Panel({
        region: 'north',
		collapsible: false,
		anchor: '100%',
		border: false,
		width: '100%',
		height: 300,
        items: [__newAlias]
    });
	
    this.grid.on('contextmenu', this._contextMenu, this, {stopEvent:true});
    this.grid.on('rowcontextmenu', this._rowContextMenu, this, {stopEvent:true});
    this.grid.on('keypress', this.__handleKeyEvent, this, {stopEvent:true});
    
	//rezise __mainNewAlias panel	
	 this.toppanel.on('resize', this.__resizePanel, this);
	// resize grid.
	this.grid.un('resize', this.__resizePanel, this) ;
	this.grid.on('resize', this.__resizePanel, this) ;
	
    iNet.iwebos.ui.mail.CreateAlias.superclass.constructor.call(this, {
        id: 'new-alias-main-id',
        iconCls: 'mail-action-alias',
        title: iwebos.message.mail.create.alias,
        header: false,
        frame: false,
        border: false,
        closable: true,
		autoScroll:true,
        layout: 'border',
        renderTo: Ext.getBody(),
        items: [this.toppanel,this.grid]
    });
    
};

/**
 * Extends Class Ext.Panel
 */
Ext.extend(iNet.iwebos.ui.mail.CreateAlias, Ext.Panel, {
	/**
     * Resize panel
     */
    __resizePanel: function(){
        Ext.EventManager.fireWindowResize();
    },
    
    /**
	 * Handle key event on grid
	 * 
	 * @param {} event
	 */
	__handleKeyEvent: function(event) {	
		LotusService.handleKeyEventOnGrid(this.grid, event);		
	},
    
    /**
     * get inputed data on GUI
     * 
     * @return {}
     */
    _getData: function(){
    	var __owner = this.__owner;
    	var __object = {};
    	// email
    	var __comp = Ext.getCmp(this.usernameId);
    	var __uname = __comp.getValue().toLowerCase();
    	__comp.setValue(__uname);
    	__object['email'] = __uname + '@' + __owner.domain;
    	
    	// pwd
    	var __comp = Ext.getCmp(this.passwordId);
    	__object['pwd'] = __comp.getValue();
    	
    	// first name
    	var __comp = Ext.getCmp(this.firstNameId);
    	__object['fname'] = __comp.getValue();
    	
    	// last name
    	var __comp = Ext.getCmp(this.lastNameId);
    	__object['lastName'] = __comp.getValue();
    	
    	// smtp
    	var __comp = Ext.getCmp(this.checkSMTP);
    	__object['smtp'] = __comp.getValue();
    	
    	// virus
    	var __comp = Ext.getCmp(this.checkVirus);
    	__object['virus'] = __comp.getValue();
    	
    	// spam
    	var __comp = Ext.getCmp(this.checkSpam);
    	__object['spam'] = __comp.getValue();
    	
    	// active
		var __comp = Ext.getCmp(this.checkActive);
    	__object['active'] = __comp.getValue();
    	
    	__object['isMail'] = false;
    	__object['group'] = this.__owner.department || '';
    	
    	// list email
    	var __store = this.grid.store;
    	var __count = __store.getCount();
    	var __list = [];
    	var __data = {};
		if (__count > 0) {
			for (var __index = 0; __index < __count; __index++) {
				__data = __store.getAt(__index).data;
				__list[__index] = {email:__data.email};
			}
		}
    	
		__object['list'] = __list;
    	
    	return __object;
    },
    
    /**
     * clean data on GUI 
     */
    _cleanData: function() {
    	var __disable = false;
    	// email
		var __comp = Ext.getCmp(this.usernameId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// password
		__comp = Ext.getCmp(this.passwordId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// confirm password
		__comp = Ext.getCmp(this.confirmPasswordId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// last name
		__comp = Ext.getCmp(this.lastNameId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// first name
		__comp = Ext.getCmp(this.firstNameId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// smtp
		__comp = Ext.getCmp(this.checkSMTP);
		__comp.setValue(false);
		__comp.setDisabled(__disable);

		// active
		__comp = Ext.getCmp(this.checkActive);
		__comp.setValue(true);
		__comp.setDisabled(__disable);
		
		// virus
		__comp = Ext.getCmp(this.checkVirus);
		__comp.setValue(false);
		__comp.setDisabled(__disable);

		// spam
		__comp = Ext.getCmp(this.checkSpam);
		__comp.setValue(false);
		__comp.setDisabled(__disable);
		
		// list email
		__store = this.grid.store;
		// remove all old record
		__store.removeAll();
		
	  	Ext.getCmp(this.cmdSave).setDisabled(__disable);	  	
	  	Ext.getCmp(this.addMaildrop).setDisabled(__disable);
		Ext.getCmp(this.removeMaildrop).setDisabled(__disable);
		
		this.__owner.account = '';
    },
    
    /**
     * fill data on GUI
     * 
     * @param {} object - data object
     */
    _setData:function(object){
    	// email
		var __comp = Ext.getCmp(this.usernameId);
		var __email = object.email;
		var __index = __email.indexOf('@'); 
		__comp.setValue(__email.substr(0,__index));
		__comp.setDisabled(true);
		
		// last name
		var __comp = Ext.getCmp(this.lastNameId);
		__comp.setValue(object.lastName);
		__comp.setDisabled(!this.editable);
		
		// first name
		var __comp = Ext.getCmp(this.firstNameId);
		__comp.setValue(object.fname);
		__comp.setDisabled(!this.editable);
		
		// smtp
		var __comp = Ext.getCmp(this.checkSMTP);
		__comp.setValue(object.smtp);
		__comp.setDisabled(!this.editable);

		// active
		var __comp = Ext.getCmp(this.checkActive);
		__comp.setValue(object.active);
		__comp.setDisabled(!this.editable);
		
		// virus
		var __comp = Ext.getCmp(this.checkVirus);
		__comp.setValue(object.virus);
		__comp.setDisabled(!this.editable);

		// spam
		var __comp = Ext.getCmp(this.checkSpam);
		__comp.setValue(object.spam);
		__comp.setDisabled(!this.editable);
		
		// list email
		var __list = object.list;
		var __store = this.grid.store;
		// remove all old record
		__store.removeAll();
		
		var __emails = [];
		var __temp = null;
		if(__list.length > 0){			
			for(var __index = 0; __index < __list.length; __index++ ) {
				__temp = __list[__index];
				// put record to list
				__emails[__index] = new Ext.data.Record({id: __temp.email, email: __temp.email});
			}
			__store.add(__emails);			
			__store.sort('email', 'ASC');
		}
    },
    
    /**
     * open dialog to select mail account
     */
    _onOpenSearchDialog : function(){
    	var __store = this.grid.store;
    	var _fnOkHandler = function(){
    		var __data = this.getData();
    		if (!__data || __data.length == 0) {
					Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_select_email,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
					return;
			}
			var __records = [];
    		// add data to grid
    		for (var index = 0; index < __data.length; index++) {
    			var __email = __data[index];
    			var __record = __store.getById(__email.email);
    			if(!__record)
    				__records[index] = new Ext.data.Record({id: __email.email, email: __email.email});                	
            }
            if(__records.length > 0) {
            	__store.add(__records)
            }
    		// destroy dialog.
			this.destroy();
    	};
    	
    	this.dlg = new iNet.iwebos.ui.mail.DialogSearchAlias(this,{
    					domain:this.__owner.domain,
    					scope: this.dlg,
    					okHandler: _fnOkHandler});
    					
    	this.dlg.show(this);
	},	
	
	/**
	 * open dialog to input outside email address
	 */
	_openInputEmailDialog: function() {
		var __store = this.grid.store;
		var __fnOkHandle = function() {
			var __email = Ext.getCmp('input-email-dialog-email-id').getValue();
			if(__email.trim() != '') {
				var __record = __store.getById(__email);
    			if(!__record)
                	__store.addData({id: __email, email:__email});
                this.destroy();	
			} else {
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : iwebos.message.mail.alias_input_email_to_add_to_alias,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
		};
		// create dialog
		var __dialog = new iNet.iwebos.ui.mail.InputEmailDialog(this, {
			scope: __dialog,
			okHandler: __fnOkHandle
		});
		__dialog.show(this);
	},
	
	/**
	 * validate data on GUI
	 * 
	 * @return {String}
	 */
	_onValidation : function(){
		// valid account name
		var __comp = Ext.getCmp(this.usernameId);
		var __account = __comp.getValue();
		if(__account === '') return iwebos.message.mail.error_input_alias_account;
		
		// password
		__comp = Ext.getCmp(this.passwordId);
		var __pass = __comp.getValue();
		
		// confirm password 
		__comp = Ext.getCmp(this.confirmPasswordId);
		var __passConfirm = __comp.getValue();
		
		
		var __type = (this.__owner.account == ''?'save': 'update') ;
		if(__type == 'update'){
			if(__pass != __passConfirm) return iwebos.message.mail.error_input_confirm_pass;
		}else{
			if(__pass === '' || __pass != __passConfirm) return iwebos.message.mail.error_input_alias_pass;
		}
		
		// last name
		__comp = Ext.getCmp(this.lastNameId);
		var __lastName = __comp.getValue();
		if(__lastName === '') return iwebos.message.mail.error_input_alias_lastname;
		
		// first name
		__comp = Ext.getCmp(this.firstNameId);
		var __firstName = __comp.getValue();
		if(__firstName === '') return iwebos.message.mail.error_input_alias_firstname;
		
		var __store = this.grid.store;
    	var __count = __store.getCount();
    	if(__count == 0) return iwebos.message.mail.error_input_mail_drop;
		
		return '';
	},
	
	/**
	 * Save alias account
	 */
	_onSave: function(){
		// validation data
	 	var __error = this._onValidation();
		if(__error != ''){
			Ext.MessageBox.show({
				title : iwebos.message.mail.error,
				msg : __error,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
			return;
		}
		
		var __data = this._getData();		
		// get owner object.
		var __owner = this.__owner || {};
		var __action = __owner.action || {} ;
		var __account = __owner.account || '';
		var __saveAction = __action.save || {};
		var __type = __account == ''?'save': 'update' ;
		if(!__saveAction.fn) return ;
		if(!__saveAction.scope){
			__saveAction.fn(this,__data, __type) ;
		}else{
			__saveAction.fn.apply(__saveAction.scope, [this,__data, __type]) ;
		}
	 },
	
	 /**
	  * handle GUI after saving data
	  */
	 handleAfterSave: function(){
		// email
		var __comp = Ext.getCmp(this.usernameId);
		__comp.setDisabled(true);
		
		// last name
		var __comp = Ext.getCmp(this.lastNameId);
		__comp.setDisabled(!this.editable);
		
		// first name
		var __comp = Ext.getCmp(this.firstNameId);
		__comp.setDisabled(!this.editable);
		
		// smtp
		var __comp = Ext.getCmp(this.checkSMTP);
		__comp.setDisabled(!this.editable);

		// active
		var __comp = Ext.getCmp(this.checkActive);
		__comp.setDisabled(!this.editable);
		
		// virus
		var __comp = Ext.getCmp(this.checkVirus);
		__comp.setDisabled(!this.editable);

		// spam
		var __comp = Ext.getCmp(this.checkSpam);
		__comp.setDisabled(!this.editable);
		
	},
	
	/**
	 * load alias account
	 * 
	 * @param {} domain
	 * @param {} account
	 * @param {} action
	 * @param {} editable
	 */
	loadInfo: function(domain, department, account, action, editable){
		this.__owner.action = action;
	 	this.__owner.domain = domain;
	 	this.__owner.department = department;
	  	this.editable = editable;
	  	
	  	// disable save button
	  	var __cmdSave = Ext.getCmp(this.cmdSave);
	  	__cmdSave.setDisabled(!this.editable);	  	
	  	
	  	Ext.getCmp(this.addMaildrop).setDisabled(!this.editable);
		Ext.getCmp(this.removeMaildrop).setDisabled(!this.editable);
	  	
	  	var __account = account || '';
	 	this.__owner.account = __account;
	 	
	 	var __txtDomain = Ext.getCmp(this.aliasDomainName);
	 	__txtDomain.setValue('@' + domain);
	 	
	 	var __loadSuccess = function(response, options){
	 		var __data = eval('(' + response.responseText + ')');
	 		var __success = (__data.success == undefined ? true : !!__data.success);
	 		
	 		if(__success){
	 			this._setData(__data);
	 		}
	 	};
	 	
	 	if(__account != ''){
	 		var __params = {iwct:'loadMailAliasContent', iwa:'READ_WRITE',action: 'load'} ;
			
			__params['email'] = __account;
			
			// send request to server.
			iNet.Ajax.request({
				url: 'jsonajax',
				params: __params,
				method: 'POST',
				success: __loadSuccess,
				scope : this,
				failure: function(result, request){},
				maskEl : this.bwrap
			});	
	 	} else {
	 		this._cleanData();
	 	}
	},
	
	/**
	 * show context menu
	 * 
	 * @param {} event
	 */
	_contextMenu: function(event){
		event.stopEvent();
		var __edit = this.editable || false ;
		if(__edit){
			this.mailMenu = new Ext.menu.Menu({
            items: [{
						id: 'mail-menu-create-email',
						text:iwebos.message.mail.add_email_account,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._onOpenSearchDialog.createDelegate(this)
              		},{
						id: 'mail-menu-add-outside-email',
						text:iwebos.message.mail.add_outside_email_account,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._openInputEmailDialog.createDelegate(this)
              		}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
		}
	},
	
	_rowContextMenu: function(grid, index, event){
    	// stop this event.
		event.stopEvent() ;
		grid.getSelectionModel().selectRow(index);
		
		var __edit = this.editable || false ;
		var selection = grid.getSelectionModel().getSelections()[0];
		if(!!selection && __edit){
			this.mailMenu = new Ext.menu.Menu({
            items: [{
						id: 'mail-menu-add-email',
						text:iwebos.message.mail.add_email_account,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._onOpenSearchDialog.createDelegate(this)
              		},{
						id: 'mail-menu-add-outside-email',
						text:iwebos.message.mail.add_outside_email_account,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._openInputEmailDialog.createDelegate(this)
              		},{
						id: 'mail-menu-delete-email',
						text:iwebos.message.mail.delete_email_account,
		                iconCls: 'icon-mail-del',
		                scope: this,
		                handler:this._onRemoveEmail.createDelegate(this)
              		}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
		}
	},
	
	/**
	 * Remove email out of alias
	 */
	_onRemoveEmail: function(){
		var __grid = Ext.getCmp("new-alias-grid-id");
		// get the selection item 
		var __records = __grid.getSelectionModel().getSelections();
		if(!!__records && __records.length > 0)
			for(var __index = 0; __index < __records.length; __index++) {
				__grid.store.remove(__records[__index]);
			}
			 
		else {
			Ext.MessageBox.show({
				title : iwebos.message.mail.error,
				msg : iwebos.message.mail.alias_select_email_to_delete,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	}
});