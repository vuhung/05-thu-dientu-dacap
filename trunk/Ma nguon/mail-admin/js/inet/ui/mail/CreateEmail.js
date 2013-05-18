/*****************************************************************
   Copyright 2008 by Nguyen Thanh Vy (ntvy@truthinet.com.vn)

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
 * @class iNet.iwebos.ui.mail.CreateEmail
 * @extends Ext.Panel
 * @constructor
 * Creates a new Panel
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.CreateEmail = function(viewer, config){
	// setting the prefix identifier.
	this.prefix = (!this.prefix) ? 'iwebos-mail-create-email' : this.prefix;
	this.accountId = this.prefix + '-account-id' ;
	this.domainId = this.prefix + '-domain-id' ;
	this.fullNameId= this.prefix + '-fullname-id' ;
	this.lastNameId= this.prefix + '-lastname-id' ;
	this.ownerAccountId= this.prefix + '-owner-account-id'; 
	this.smtpId = this.prefix + '-smpt-id';
	this.capacityId = this.prefix + '-capacity-id';
	this.virusId = this.prefix + '-virus-id';
	this.spamId = this.prefix + '-spam-id';
	this.forwardId = this.prefix + '-forward-id';
	this.fwAddressId = this.prefix + '-fw-address-id';
	this.activeId = this.prefix + '-active-id';
	this.userButtonId = this.prefix + '-select-user-button-id';
	this.groupId = this.prefix + '-group-id';
	this.cmdSave = this.prefix + '-mail-save-button';
	this.cmdNew = this.prefix + '-mail-new-mail-button';
	this.editable = true;
	this.__owner = {};
	
	var __main = new Ext.form.FormPanel({
        region: 'center',
        frame: false,
        border: false,
		autoHeight: true,
        anchor: '100%',
		labelAlign: 'right',
		bodyStyle: 'padding: 5px;',
		items:[{
			border: false,
			html: iwebos.message.mail.create.email_info
		},{
			layout: 'column',
			columnWidth: 1,
			anchor: '100%',
			border: false,
			items:[{
				layout: 'form',
				columnWidth: 0.5,
				anchor: '100%',
				border: false,
				items:[{
					id:this.accountId,
					xtype: 'textfield',
					anchor: '95%',					
					tabIndex: 1,
					fieldLabel:iwebos.message.mail.create.account
				},
				{	layout: 'column',
					columnWidth: 1,
					anchor: '100%',
					border: false,
					items:[{
						layout:'form',
						columnWidth:0.9,
						anchor: '95%',
						border: false,
						items:[{
							id:this.ownerAccountId,
							xtype: 'textfield',
							readOnly:true,							
							fieldLabel: iwebos.message.mail.create.owner_account,
							anchor: '95%'
						}]
					},{
						layout:'form',
						columnWidth:0.1,
						border: false,
						items:[{
							id : this.userButtonId,
		                   	xtype: 'button',
						   	border: false,
							iconCls:'icon-mail-add',
							tabIndex: 4,
		                   	handler:this._fnSelectUser.createDelegate(this)
						}]
					   
					}]
					
				}]
			},{
				layout: 'form',
				columnWidth: 0.5,
				anchor: '100%',
				border: false,
				items:[{
					id:this.domainId,
					xtype: 'textfield',
					anchor: '95%',
					fieldLabel:iwebos.message.mail.create.domain,					
					disabled:true
				},{
                	id: this.groupId,
                    xtype: 'textfield',
                    readOnly: true,
                    anchor: '95%',
                    fieldLabel: iwebos.message.mail.create.department
                }]
			}]
		},{
			border: false,
			html: iwebos.message.mail.create.role_mail
		},{
			layout: 'column',
			columnWidth: 1,
			border: false,
			anchor: '100%',
			labelWidth: 55,
			labelAlign: 'left',
			items:[{
				layout: 'form',
				columnWidth: 0.5,
				anchor: '100%',
				bodyStyle:'padding-left:30px;',
				border: false,
				items:[{
					id: this.lastNameId,
					xtype: 'textfield',
					anchor: '95%',					
					tabIndex: 5,
					fieldLabel: iwebos.message.mail.create.lastname
				},{
					id : this.smtpId,
					xtype:'checkbox',
					hideLabel:true,
					anchor: '95%',
					checked : true,
					tabIndex: 7,
					boxLabel:iwebos.message.mail.create.role_smtp
				},{
					id: this.spamId,
					xtype:'checkbox',
					hideLabel:true,
					anchor: '95%',
					checked : true,
					tabIndex: 8,
					boxLabel:iwebos.message.mail.create.role_spam
				},{
					id: this.capacityId,
					xtype: 'numberfield',
					allowDecimals: false,
					maxLength: 5,
					anchor: '95%',
					allowBlank:false,
					value: 50,
					tabIndex: 12,
					fieldLabel: iwebos.message.mail.create.sizelimit
				}]
			},{
				layout: 'form',
				columnWidth: 0.5,
				anchor: '100%',
				bodyStyle:'padding-left:30px;',
				border: false,
				items:[{
					id:this.fullNameId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 6,
					fieldLabel: iwebos.message.mail.create.firstname
				},{
					layout:'column',
					border:false,
					columnWidth:1,
					anchor:'100%',
					items:[{
						layout:'form',
						border:false,
						columnWidth:0.6,
						anchor:'80%',
						items:[{
							id: this.forwardId,
							xtype:'checkbox',
							hideLabel:true,
							tabIndex: 9,							
							boxLabel:iwebos.message.mail.create.role_transfer,
							handler:this.hanlerForwardCheck.createDelegate(this)
						}]
					},{
						layout:'form',
						border:false,
						columnWidth:0.4,
						anchor: '98%',
						items:[{
							id: this.fwAddressId,
							xtype:'textfield',
							anchor: '88%',
							disabled: true,
							hideLabel:true
						}]
						
					}]
				},{
					id: this.virusId,
					xtype:'checkbox',
					hideLabel:true,
					anchor: '100%',
					checked : true,
					tabIndex: 10,
					boxLabel:iwebos.message.mail.create.role_virus
				},{
					id: this.activeId,
					xtype:'checkbox',
					hideLabel:true,
					anchor: '100%',
					checked : true,
					tabIndex: 11,
					boxLabel:iwebos.message.mail.create.role_active
				}]
			}]
		},{
			border: false,
			html: iwebos.message.mail.create.account_desc
		}],
		tbar:[{
				id: this.cmdSave,
		 		xtype: 'button',
				iconCls: 'icon-save',
				text:iwebos.message.mail.create.save,
				scope: this,
				handler: this._onSave
		 },'|',{
				id: this.cmdNew,
		 		xtype: 'button',
				iconCls: 'icon-email-createacc',
				text:iwebos.message.mail.create_email_account,
				scope: this,
				handler: this.cleanData
		 }]
	});
	
	iNet.iwebos.ui.mail.CreateEmail.superclass.constructor.call(this, {
        id: 'new-email-panel',
        iconCls: 'icon-email-createacc',
        title: iwebos.message.mail.create.title,
        header: false,
        frame: false,
        border: false,
        closable: true,
        layout: 'border',
        renderTo: Ext.getBody(),
        items: [__main]
    });
};
Ext.extend(iNet.iwebos.ui.mail.CreateEmail, Ext.Panel, {
	/**
	 * handle event when check on forward check box
	 */
	hanlerForwardCheck: function() {
		var check = Ext.getCmp(this.forwardId).getValue();
		Ext.getCmp(this.fwAddressId).setDisabled(!check);
	},
	
	 /**
	  * validation
	  * @return {String} - messages error
	  */
	_onValidation: function(){
		// valid account name
		var __comp = Ext.getCmp(this.accountId);
		var __account = __comp.getValue().trim();
		if(__account === '') return iwebos.message.mail.create.error_input_email;
		if(__account.indexOf('@') >= 0) return iwebos.message.mail.create.input_invalid_mail_uid;
		
		var __type = (this.__owner.account == ''?'save': 'update');
		
		// owner account
		__comp = Ext.getCmp(this.ownerAccountId);
		var __ownerName = __comp.getValue();
		if(__ownerName === '' && __type === 'save' ) return iwebos.message.mail.create.error_input_owner;
		
		// full name
		__comp = Ext.getCmp(this.fullNameId);
		var __fullName = __comp.getValue();
		if(__fullName === '') return iwebos.message.mail.error_input_alias_firstname;
		
		// first name
		__comp = Ext.getCmp(this.lastNameId);
		var __firstName = __comp.getValue();
		if(__firstName === '') return iwebos.message.mail.error_input_alias_lastname;
		
		// capacity
		__comp = Ext.getCmp(this.capacityId);
		var __capacity = __comp.getValue();
		if(__capacity === '') return iwebos.message.mail.error_input_limit;
		
		// forward
		var forward = Ext.getCmp(this.forwardId).getValue();		
		if(forward && Ext.getCmp(this.fwAddressId).getValue() == '') 
			return iwebos.message.mail.warning_input_address_need_to_forward;
		
		return '';
	},
	
	/**
	 * get data 
	 * @return {object}
	 */
	_getData: function(){
		// create object to store data.
		var object = {} ;
		
		// password
		object['pwd'] = this.__owner.pwd;
		
		// full name
		var __comp = Ext.getCmp(this.fullNameId);
		object['fname'] = __comp.getValue();
		
		// last name
		__comp = Ext.getCmp(this.lastNameId);
		object['lname'] = __comp.getValue();
		
		// smtp
		__comp = Ext.getCmp(this.smtpId);
		object['smtp'] = __comp.getValue();
		
		// capacity
		__comp = Ext.getCmp(this.capacityId);
		object['capacity'] = __comp.getValue();
		
		// active
		__comp = Ext.getCmp(this.activeId);
		object['active'] = __comp.getValue();
		
		// forward
		__comp = Ext.getCmp(this.forwardId);
		var forward = __comp.getValue();
		object['forward'] = forward;
		
		if(forward) {
			__comp = Ext.getCmp(this.fwAddressId);
			object['fAddress'] = __comp.getValue();
		}
		
		// virus
		__comp = Ext.getCmp(this.virusId);
		object['virus'] = __comp.getValue();
		
		// spam
		__comp = Ext.getCmp(this.spamId);
		object['spam'] = __comp.getValue();
		
		// code
		object['code'] = this.__owner.userCode;
		
		//username (it mean uid infomation of account)
		object['uname'] = this.__owner.uname;
		
		// email
		__comp = Ext.getCmp(this.accountId);
		var __account = __comp.getValue().toLowerCase();
		__comp.setValue(__account);
		
		// domain
		__comp = Ext.getCmp(this.domainId);
		var __domain = __comp.getValue();
		object['email'] = __account + __domain ;
		
		object['isMail'] = true;
		var __group = Ext.getCmp(this.groupId).getValue().trim();
		if(__group != '') {
			var __groups = __group.split(',');
			var __groupObjects = [];
			for(var __index = 0; __index < __groups.length; __index++) {
				__groupObjects[__index] = {group: __groups[__index]};
			}
			object['groups'] = __groupObjects;			
		}		
		
		return object;
	},
	
	/**
	 * clean data on GUI
	 */
	cleanData: function() {
		var __disable = false;
		// email
		var __comp = Ext.getCmp(this.accountId);		
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		Ext.getCmp(this.ownerAccountId).setValue('');		
		
		// button select user
		__comp = Ext.getCmp(this.userButtonId);
		__comp.setDisabled(__disable); 
		
		// full name
		__comp = Ext.getCmp(this.fullNameId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// first name
		__comp = Ext.getCmp(this.lastNameId);
		__comp.setValue('');
		__comp.setDisabled(__disable);
		
		// smtp
		__comp = Ext.getCmp(this.smtpId);
		__comp.setValue(true);
		__comp.setDisabled(__disable);

		// capacity
		__comp = Ext.getCmp(this.capacityId);
		__comp.setValue('50');
		__comp.setDisabled(__disable);
		
		// active
		__comp = Ext.getCmp(this.activeId);
		__comp.setValue(true);
		__comp.setDisabled(__disable);
		
		// forward
		__comp = Ext.getCmp(this.forwardId);
		__comp.setValue(false);
		__comp.setDisabled(__disable);
		
		// virus
		__comp = Ext.getCmp(this.virusId);
		__comp.setValue(true);
		__comp.setDisabled(__disable);

		// spam
		__comp = Ext.getCmp(this.spamId);
		__comp.setValue(true);
		__comp.setDisabled(__disable);
		
		this.__owner.account = '';
	},	
	
	/**
	 * 
	 * @param {Object} object
	 */
	_setData:function(object){
		this.__owner.department = object.group;
		// email
		var __comp = Ext.getCmp(this.accountId);
		var __email = object.email;
		var __index = __email.indexOf('@'); 
		__comp.setValue(__email.substr(0,__index));
		__comp.setDisabled(true);
		
		var __groups = object.groups; 
		if(__groups != null && __groups.length > 0) {
			var __display = __groups[0].group;
			for(var __index = 1; __index < __groups.length; __index++) {
				__display = __display + ', ' + __groups[__index].group ;
			}
			// fill group name
			Ext.getCmp(this.groupId).setValue(__display);
		}	
			
		Ext.getCmp(this.ownerAccountId).setValue(object.uname);		
		
		// button select user
		var __comp = Ext.getCmp(this.userButtonId);
		__comp.setDisabled(!(this.editable && object.chgOwner)); 
		
		// full name
		var __comp = Ext.getCmp(this.fullNameId);
		__comp.setValue(object.fname);
		__comp.setDisabled(!this.editable);
		
		// first name
		var __comp = Ext.getCmp(this.lastNameId);
		__comp.setValue(object.lname);
		__comp.setDisabled(!this.editable);
		
		// smtp
		var __comp = Ext.getCmp(this.smtpId);
		__comp.setValue(object.smtp);
		__comp.setDisabled(!this.editable);

		// capacity
		var __comp = Ext.getCmp(this.capacityId);
		__comp.setValue(object.capacity);
		__comp.setDisabled(!this.editable);
		
		// active
		var __comp = Ext.getCmp(this.activeId);
		__comp.setValue(object.active);
		__comp.setDisabled(!this.editable);
		
		// forward
		var __comp = Ext.getCmp(this.forwardId);
		__comp.setValue(object.forward);
		__comp.setDisabled(!this.editable);
		
		if(object.forward) {
			var __comp = Ext.getCmp(this.fwAddressId);
			__comp.setValue(object.fAddress);
			__comp.setDisabled(!this.editable);
		}
		
		
		// virus
		var __comp = Ext.getCmp(this.virusId);
		__comp.setValue(object.virus);
		__comp.setDisabled(!this.editable);

		// spam
		var __comp = Ext.getCmp(this.spamId);
		__comp.setValue(object.spam);
		__comp.setDisabled(!this.editable);
		
	},
	/**
	 * Save account
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
	 
	 
	handleAfterSave: function(){
		// email
		var __comp = Ext.getCmp(this.accountId);
		__comp.setDisabled(true);
		
		// button select user
		var __comp = Ext.getCmp(this.userButtonId);
		__comp.setDisabled(true); 
		
		// full name
		var __comp = Ext.getCmp(this.fullNameId);
		__comp.setDisabled(!this.editable);
		
		// first name
		var __comp = Ext.getCmp(this.lastNameId);
		__comp.setDisabled(!this.editable);
		
		// smtp
		var __comp = Ext.getCmp(this.smtpId);
		__comp.setDisabled(!this.editable);

		// capacity
		var __comp = Ext.getCmp(this.capacityId);
		__comp.setDisabled(!this.editable);
		
		// active
		var __comp = Ext.getCmp(this.activeId);
		__comp.setDisabled(!this.editable);
		
		// forward
		var __comp = Ext.getCmp(this.forwardId);
		__comp.setDisabled(!this.editable);
		
		// virus
		var __comp = Ext.getCmp(this.virusId);
		__comp.setDisabled(!this.editable);

		// spam
		var __comp = Ext.getCmp(this.spamId);
		__comp.setDisabled(!this.editable);
	},
	
	 /**
	  * load data 
	  * @param {String} domain
	  * @param {String} account
	  * @param {object} action
	  */
	 loadInfo: function(domain, departments, selectedDep, account, action, editable){
	 	this.__owner.action = action;
	 	this.__owner.domain = domain;
	 	this.__owner.department = selectedDep;
	  	this.editable = editable;
	  	
	  	// disable save button
	  	var __cmdSave = Ext.getCmp(this.cmdSave);
	  	__cmdSave.setDisabled(!this.editable);
	  	Ext.getCmp(this.cmdNew).setDisabled(!this.editable);
	  	
	 	__account = account || '';
	 	this.__owner.account = __account;
	 	
	 	var __txtDomain = Ext.getCmp(this.domainId);
	 	__txtDomain.setValue('@' + domain);
	 	
	 	Ext.getCmp(this.groupId).setValue('');	 	
	 	
	 	var __loadSuccess = function(response, options){
	 		var __data = eval('(' + response.responseText + ')');
	 		var __success = (__data.success == undefined ? true : !!__data.success);
	 		if(__success){
	 			this._setData(__data);
	 		}
	 	};
	 	
	 	if(__account != ''){
	 		var __params = {iwct:'loadMailAccountContent', iwa:'READ_WRITE',action: 'load'} ;
			
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
	 		this.cleanData();
	 	}	
	 },
	 
	 /**
	  * Opem dialog to search user for account
	  */
	 _fnSelectUser: function(){
	 	var dialog = new iNet.iwebos.ui.mail.DialogSearchAccount();
		var action = {fn:this._loadAccount.createDelegate(this), scope:this};
		// set action for dialog
		dialog.__setAction(action);
		dialog.show(this);
		dialog.disabledDelChk();
	 },
	 
	 /**
	  * Load the account information
	  * 
	  * @param {} uname - the user name
	  */
	 _loadAccount: function(object) {
	 	if (object != undefined && object != null && object.uname != '') {
	 		this.__owner.code = object.id;
	 		var __fnSuccess = function(result, request){
					var __data =  Ext.util.JSON.decode(result.responseText);
					var __success = (__data.success == undefined) ? true : __data.success;
					if (__success) {
						// fill data on GUI
						this._fillUserInfo(__data);
					}
					else {
						Ext.MessageBox.show({
							title: iwebos.message.org.message,
							msg: iwebos.message.org.error_user_load_account,
							buttons: Ext.MessageBox.OK,
							icon: Ext.MessageBox.ERROR
						});
					}
	 		};
			var baseParams = {
				iwct: 'loadUserContent',
				iwa: 'READ_WRITE',
				action: 'loadlotus',
				uname: object.uname,
				org: this.__owner.domain
			};
			// send request to server.
			iNet.Ajax.request({
				url: 'jsonajax',
				params: baseParams,
				scope: this,
				method: 'POST',
				success: __fnSuccess,
				failure: function(result, request){
				},
				maskEl: this.bwrap
			});
		}else{
			this._cleanData();
		}
	 },	
	 
	/**
	 * fill the user information
	 */
	_fillUserInfo: function(object) {
		var __object = object || {};
		
		if(this.__owner.domain == object.org) {
			var __groups = object.groups; 
			if(__groups != null && __groups.length > 0) {
				var __display = __groups[0].group;
				for(var __index = 1; __index < __groups.length; __index++) {
					__display = __display + ', ' + __groups[__index].group ;
				}
				// fill group name
				Ext.getCmp(this.groupId).setValue(__display);
			} else {
				Ext.getCmp(this.groupId).setValue('');
			}
		} else {
			Ext.getCmp(this.groupId).setValue('');
		}
		
		if(!object.edit) {
			Ext.MessageBox.show({
				title: iwebos.message.org.message,
				msg: iwebos.message.org.warning_user_can_not_manage_user,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.WARNING
			});
		}	
		
		
		var __txtOwnerName = Ext.getCmp(this.ownerAccountId);
		if(!!__txtOwnerName){
			__txtOwnerName.setValue(__object.uname);
			this.__owner.userCode = this.__owner.code;
			this.__owner.uname	= __object.uname;
			this.__owner.pwd = __object.pwd;
		}		
		
		var __fname = __object.fname || '';
		var __name = '';
		if(__object.mname) {
			__name = __object.mname + ' ' + __fname;
		} else {
			__name = __fname;
		}	
		
		Ext.getCmp(this.fullNameId).setValue(__name);
		Ext.getCmp(this.lastNameId).setValue(__object.lname);
	}
});