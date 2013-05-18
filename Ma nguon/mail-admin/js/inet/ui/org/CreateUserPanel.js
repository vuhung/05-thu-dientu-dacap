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
function checking() {
	Ext.getCmp('manage-user-create-new-tab')._isExist();
}
iNet.iwebos.ui.org.CreateUserPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	// The ID of components
	this.prefix = (!this.prefix) ? 'manage-user-create-new' : this.prefix ;
	this.tabId = this.prefix + '-tab';
	this.unameId = this.prefix + '-user-name';
	this.checkExistId = this.prefix + '-check-exist';
	this.checkExistHtmlId = this.prefix + '-check-exist-html';
	this.orgId = this.prefix + '-org-name';
	this.pwdId = this.prefix + '-password';
	this.confirmPwdId = this.prefix + '-confirm-pwd';
	this.fnameId = this.prefix + '-first-name';
	this.mnameId = this.prefix + '-middle-name';
	this.lnameId = this.prefix + '-last-name';
	this.employeeNumberId = this.prefix + '-employee-number';
	this.titleId = this.prefix + '-user-title';
	this.emailId = this.prefix + '-email';
	this.workPhoneId = this.prefix + '-work-phone';
	this.mobileId = this.prefix + '-mobile';
	this.homePhoneId = this.prefix + '-home-phone';
	this.emailsId = this.prefix + '-email-accounts'	;
	this.panelViewId= this.prefix + '-panel-view-id';
	
	this.emailCheckId = this.prefix + '-email-check-id';
	this.emailForwardCheckId = this.prefix + '-email-forward-check-id';
	this.emailForwardId = this.prefix + '-email-forward-id';
	this.emailForwardPanelId = this.prefix + '-email-fwd-panel-id';
	
	this._owner = {};
	
	var __requireChar = '*';
	
	var __unameText = new Ext.form.TextField({
		id: this.unameId,
		fieldLabel: iwebos.message.org.user_name + __requireChar,
		tabIndex: 1,
		enableKeyEvents: true,
		labelWidth: 80,
		anchor: '99%'
	});
	
	var __maskRe = /^\+|(\d{1,})|\s/;
	this.buttonIconCls = 'icon-account-check-valid';
	this.originalIcon = true;
	this.imageHtml = '<div id=' + this.checkExistId + ' class="{0}"></div>';
	this.linkHtml = '<a href="#" onclick="checking()">' + this.imageHtml + '</a>';
	
	// The panel contain all user information
	var __personalInfoPanel = new Ext.Panel({
		region: 'center',
		frame: false,
		border: false,
		autoHeight: true,
		anchor: '100%',
		labelAlign: 'right',
		bodyStyle: 'padding: 5px;',
		items: [{
			border: false,
			html: '<b>' + iwebos.message.org.user_account_information + '</b>'
		},{
			layout: 'column',
			columnWidth: 1,
			border: false,
			anchor: '100%',			
			items:[{
				layout: 'column',
				anchor: '100%',
				border: false,
				columnWidth: 0.5,
				items: [{
					layout: 'form',
					border: false,
					columnWidth: 0.9,
					labelWidth: 100,
					anchor: '100%',
					items:[__unameText]
				},{
					layout: 'form',
					border: false,
					columnWidth: 0.1,
					anchor: '98%',
					items: [{
						html: '<div id=' + this.checkExistHtmlId + ' >' 
								+ String.format(this.linkHtml, this.buttonIconCls) + '</div>',
						border: false		
					}]
				}]
			},{
				layout: 'form',
				border: false,
				columnWidth: 0.5,
				labelWidth: 80,
				anchor: '100%',
				items:[{
					id: this.orgId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.organization,
					readOnly: true,
					labelWidth: 80,
					anchor: '95%'
				}]
			}]
		},{
			layout: 'column',
			columnWidth: 1,
			border: false,
			anchor: '100%',			
			items:[{
				layout: 'form',
				border: false,
				columnWidth: 0.5,
				labelWidth: 100,
				anchor: '100%',
				items: [{
					id: this.pwdId,				
					xtype: 'textfield',
					inputType: 'password',
					fieldLabel: iwebos.message.org.user_password,
					tabIndex: 2,
					anchor: '95%'				
				},{
					id:this.emailCheckId,
					xtype: 'checkbox',
					boxLabel: iwebos.message.org.user_email_check,
					checked: true,
					hideLabel: true,
					listeners:{
						check: function(control,value){
							var component=Ext.getCmp(this.emailForwardPanelId);
							if (component) component.setVisible(value);
						},scope: this
					}
				}]
			},{
				layout: 'form',
				border: false,
				columnWidth: 0.5,
				labelWidth: 80,
				anchor: '100%',
				items: [{
					id: this.confirmPwdId,
					xtype: 'textfield',
					inputType: 'password',
					fieldLabel: iwebos.message.org.user_confirm_password,
					tabIndex: 3,
					anchor: '95%'
				},{
					id:this.emailForwardPanelId,
					layout: 'column',
					columnWidth: 1,
					border: false,
					anchor: '95%',
					items: [{
						width: 185,
						layout: 'form',
						anchor: '100%',
						border: false,
						items: [{
							id: this.emailForwardCheckId,
							xtype: 'checkbox',
							boxLabel: iwebos.message.org.user_email_forward_check,
							hideLabel: true,
							listeners: {
								check: function(control, value){
									var component = Ext.getCmp(this.emailForwardId);
									if (component) {
										component.setDisabled(!value);
										component.focus(true,true);
									}
								},
								scope: this
							}
						}]
					},{
						columnWidth: 1,
						lyout: 'form',
						anchor: '100%',
						border:false,
						items:[{
							id:this.emailForwardId,
							width: '97%',
							xtype: 'textfield',
							vtype: 'email',
							disabled:true,
							hideLabel: true
						}]
					}]
				}]
			}]
		},{
			border: false,
			html: '<br /><b>' + iwebos.message.org.user_information + '</b>'
		},{
			layout: 'column',
			columnWidth: 1,
			border: false,
			anchor: '100%',	
			items: [{
				layout: 'form',
				border: false,
				columnWidth: 0.5,
				labelWidth: 100,
				anchor: '100%',
				items: [{
					layout: 'column',
					columnWidth: 1,
					border: false,
					anchor: '100%',
					items: [{
						layout: 'form',
						border: false,
						columnWidth: 0.45,
						labelWidth: 100,
						anchor: '100%',
						items: [{
							id: this.lnameId,
							xtype: 'textfield',
							fieldLabel: iwebos.message.org.user_last_name + __requireChar,
							tabIndex: 4,
							anchor: '95%'
						}]
					}, {
						layout: 'form',
						border: false,
						columnWidth: 0.55,
						labelWidth: 80,
						anchor: '100%',
						items: [{
							id: this.mnameId,
							xtype: 'textfield',
							fieldLabel: iwebos.message.org.user_middle_name,
							tabIndex: 5,
							anchor: '92.5%'
						}]
					}]
				}, {
					id: this.employeeNumberId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_employee_number,
					tabIndex: 7,
					anchor: '95%'
				},{
					id: this.emailId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_alternate_email,
					vtype:'email',
					tabIndex: 9,
					anchor: '95%'
				},{
					id: this.mobileId,
					xtype: 'textfield',					
					fieldLabel: iwebos.message.org.user_mobile,					
					tabIndex: 11,
					maskRe: __maskRe,
					anchor: '95%'
				}]
			}, {
				layout: 'form',
				border: false,
				columnWidth: 0.5,
				labelWidth: 80,
				anchor: '100%',
				items: [{
					id: this.fnameId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_first_name + __requireChar,
					tabIndex: 6,
					anchor: '95%'
				},{
					id: this.titleId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_title,
					tabIndex: 8,
					anchor: '95%'
				},{
					id: this.workPhoneId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_word_phone,
					tabIndex: 10,
					maskRe: __maskRe,
					anchor: '95%'
				},{
					id: this.homePhoneId,
					xtype: 'textfield',
					fieldLabel: iwebos.message.org.user_home_phone,
					tabIndex: 12,
					maskRe: __maskRe,
					anchor: '95%'
				}]
			}]
		}]
	});
	
	var sortInfo = {
        field: 'uid',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'uid', 
		direction: "ASC"
	};
    
    // email record.
    var mailRecord = Ext.data.Record.create([
	    {name: 'id', mapping: 'email', type: 'string'},
	    {name: 'email', mapping: 'email', type: 'string'},
	    {name: 'code', mapping: 'code', type: 'string'},	 
		{name: 'uid', mapping: 'uid', type: 'string'},
		{name: 'symbol', mapping: 'symbol', type: 'string'}, 
		{name: 'domain', mapping: 'domain', type: 'string'},
		{name: 'fname', mapping: 'fname', type: 'string'},
		{name: 'lname', mapping: 'lname', type: 'string'},
		{name: 'depNumber', mapping: 'depNumber', type: 'string'},
		{name: 'smtp', mapping: 'smtp', type: 'boolean'},
		{name: 'capacity', mapping: 'capacity', type: 'int'},
		{name: 'active', mapping: 'active', type: 'boolean'},
		{name: 'forward', mapping: 'forward', type: 'boolean'},
		{name: 'fAddress', mapping: 'fAddress', type: 'string'},
		{name: 'virus', mapping: 'virus', type: 'boolean'},
		{name: 'spam', mapping: 'spam', type: 'boolean'}	
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'email'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.account,
			dataIndex: 'uid',
			fixed: true,
			width: 200
		},{
			header: '@',
			dataIndex: 'symbol',
			fixed: true,
			width: 24
		},{
			header: iwebos.message.mail.domain,
			dataIndex: 'domain',
			width: 100
		}
	]);
    
    // create filter by function, filter by status.
    var filterFn = function(item, value){
        return (item.data.status === value);
    };
    
	// show color in row.
	var fnRowClass = function(record){
		var read = record.data.read ;	
		return '';
	};

	
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: 'mail-admin-create-gird',
		region: 'center',
		anchor: '100%',
		autoScroll: true,
		border: true,
		url: 'jsonajax',
		method: 'POST',
		sortInfo: sortInfo,
		loadFirst: false,
		reader: reader,
		rowClass: fnRowClass,
		dsort: defaultSort,
		cm: columnModel,
		filter: {
			data: 'all',
			fn: filterFn
		},
		tbar: [{
			id: 'btn-create-new-email',
			xtype: 'button',
			text: iwebos.message.org.user_add_email,
			iconCls: 'icon-mail-add',
			scope: this,
			handler: this._createMailAccount.createDelegate(this)
		}, {
			id: 'btn-delete-email',
			xtype: 'button',
			text: iwebos.message.mail.delete_email_account,
			iconCls: 'icon-mail-del',
			scope: this,
			handler: this._onRemoveMailAccount.createDelegate(this)
		}]
	});
	
	/**
     * When rowselect
     */
    this.grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r){		    	
		var __record = this.grid.store.getAt(rowIdx) ;
		var __data = __record.data ;
		this.openId=__data.id;
		//apply template mail panel					
		var __detailPanel = Ext.getCmp('previewPanel');
		// header
		if (__detailPanel) {
			itemTpl.overwrite(__detailPanel.body, __data);
			__detailPanel.setVisible(true);
		}
	},this);

			
	var __preView = new Ext.Panel({
        id: 'previewPanel',
		width: '97%',
		region: 'south',
		frame: true,
		hidden:true,
		border: false
    });
	
	var __html =  [
	'<table width=100% border=0 cellspacing=0 cellpadding=0>',
		'<tr><td valign=top align="left" colspan="7"><b><font color="#404040">',iwebos.message.account.detail_info,':</font></b></td></tr>',
		'<tr>',
			'<td width="125px"/><td align="left" width="125px" class="preview-account-text">\t',iwebos.message.account.detail_info_owner_account,':</td><td align="left" width="200px">{lname}&nbsp;{mname}&nbsp;{fname}</td>',
			'<td valign=top align="left" width="165px" class="preview-account-text">',iwebos.message.account.detail_info_username,':</td><td align="left" colspan="2"><a href="#">{uid}{symbol}{domain}</a></td>',
		'</tr>',
		'<tr><td valign=top align="left" colspan="7"><b><font color="#404040">',iwebos.message.account.detail_info_options,':</font></b> </td></tr>',
		'<tr>',
			'<td width="125px"/><td align="left" width="125px" class="preview-account-text">\t',iwebos.message.account.detail_info_sizelimit,':</td><td align="left" width="200px">{capacity}(MB)</td>',
			'<td valign=top align="left" width="150px" class="preview-account-text">\t',iwebos.message.account.detail_info_virus,':</td><td align="left" width="75px">{[this.repl(values.virus)]}</td>',
			'<td valign=top align="left" width="175px" class="preview-account-text">\t',iwebos.message.account.detail_info_spam,':</td><td align="left">{[this.repl(values.spam)]}</td>',
		'</tr>',
		'<tr>',
			'<td width="125px"/><td align="left" width="125px"  class="preview-account-text">\t',iwebos.message.account.detail_info_smtp,':</td><td align="left" width="200px">{[this.repl(values.smtp)]}</td>',
			'<td valign=top align="left" width="150px"  class="preview-account-text">\t',iwebos.message.account.detail_info_active,':</td><td align="left" width="75px">{[this.repl(values.active)]}</td>',
			'<td valign=top align="left" width="175px"  class="preview-account-text">\t',iwebos.message.account.detail_info_transfer,':</td><td align="left">{[this.repl(values.forward)]}</td>',
		'</tr>',
	'</table>'];
    
    var itemTpl = new Ext.XTemplate(__html.join(''), {
		repl: function(val){
			return (val === true ? iwebos.message.account.accept_value : iwebos.message.account.non_accept_value);
     	}
    });
	
	var __panelView = new Ext.FormPanel({
		id: this.panelViewId,
		region:'center',
		anchor: '100%',
		border: false,
		hideBorders:true,
		labelWidth: 150,
		items: [{
			id:this.emailsId,
			xtype:'fieldset',
			checkboxToggle: true,
			collapsed: true,
			hideBorders :false,
			border: Ext.isIE,
			title:iwebos.message.mail.view_info_account,
			layout:'border',
			width: '95%',
			items:[this.grid]
		},{
			layout: 'form',
			bodyStyle: 'padding: 10px;',
			items: [__preView]
		}]
	});
	
	this.grid.on('contextmenu', this._contextMenu, this, {stopEvent:true});
    this.grid.on('rowcontextmenu', this._rowContextMenu, this, {stopEvent:true});
    this.grid.on('rowdblclick', this._onDoubleClick, this);
    __unameText.on('keypress', this._fnUNamePress, this, {stopEvent: true});
	
	// create the panel to contain all component
	var __main = new Ext.Panel({
		region: 'center',
		autoScroll: true,
		frame: false,
		border: false,
		items: [__personalInfoPanel,__panelView]
	});
	__main.on('resize', this.__resizePanel, this);
	
	var __field=Ext.getCmp(this.emailsId);
	
	__field.on('expand',function(p){
		if(this.openId){
			__preView.setVisible(true);
		}
	},this);
	__field.on('collapse',function(p){
		__preView.setVisible(false);
	},this);

	// the actions
    this.actions = {
		'user-create-new':this.onAdd.createDelegate(this),
		'user-save':this.onSave.createDelegate(this),
		'user-search':this._searchAccount.createDelegate(this)
    };
	
	iNet.iwebos.ui.org.CreateUserPanel.superclass.constructor.call(this, {
		id: this.tabId,
        iconCls: 'icon-create-account',
        title: iwebos.message.org.user,
        anchor: '100%',
        header: false,
		enableTabScroll: true,
		autoScroll : true,
        frame: false,
        border: false,
        closable: true,
        layout: 'border',
        items: [__main]
	});	
};

Ext.extend(iNet.iwebos.ui.org.CreateUserPanel, Ext.Panel, {
	/**
	 * open tab to create user
	 */
	onAdd: function(){
		this.update = true;
		this._cleanData();
	},
	
	/**
	 * Set the information for tab
	 * 
	 * @param {} org
	 */
	setInfo: function(org, group) {
		this._owner.group = group;
		this._owner.groups = [{group: group}];
		this._owner.org = org;
		Ext.getCmp(this.orgId).setValue(org);
	},
	
	/**
	 * press on user name field
	 * 
	 * @param {} field
	 * @param {} event
	 */
	_fnUNamePress: function(field, event) {
		if(!this.originalIcon && event.getKey() != event.ENTER) {			
			this.originalIcon = true;
			// reset icon
			document.getElementById(this.checkExistHtmlId).innerHTML 
						= String.format(this.linkHtml, this.buttonIconCls);			
		} else if(this.originalIcon && event.getKey() == event.ENTER) {
			this._isExist();
		}
	},
	
	/**
	 * check the user is exist or not
	 */
	_isExist: function() {		
		var __component = Ext.getCmp(this.unameId);
		// get the user name
		var __uname = __component.getValue().trim().toLowerCase();
		if(__uname == '') {
			Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.warning_user_input_user_name_to_check,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			return;
		}
		__component.setValue(__uname);
		var __button = document.getElementById(this.checkExistId); 
		this.originalIcon = false;
		// handler when checking successful
		var __fnSuccess = function(result, request) {
			var __data =  Ext.util.JSON.decode(result.responseText);
			var __success = (__data.success == undefined) ? true : __data.success;
			if (__success) {				
				// reset icon on button
				var __iconCls = __data.exist?'icon-account-invalid':'icon-account-valid';
				var __textComponent = __data.exist?Ext.getCmp(this.unameId):Ext.getCmp(this.pwdId);
				__textComponent.focus(true);
				document.getElementById(this.checkExistHtmlId).innerHTML = String.format(this.imageHtml, __iconCls);
			} else {
				// reset icon on button
				__button.className = this.buttonIconCls;
				// show error
				if(__data.error == 'invaliduname') {
					Ext.MessageBox.show({
						title: iwebos.message.org.message,
						msg: iwebos.message.org.message_user_invalid_user_name,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
				} else {
					Ext.MessageBox.show({
						title: iwebos.message.org.message,
						msg: iwebos.message.org.error_user_cheking_user_name_to_check,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
				}
				
			}
		};
		
		// reset waiting icon on button
		__button.className = 'icon-account-waiting';
		var __params = {iwct: 'userContent',iwa: 'READ_WRITE', action: 'checkuname'};
		__params['uname'] = __uname;
		__params['org'] = Ext.getCmp(this.orgId).getValue();
		// submit request
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			scope: this,
			method: 'POST',
			success: __fnSuccess
			//maskEl: this.bwrap
		});
	},
	
	/**
	 * validat user information
	 */
	validateUser: function() {		
		var component = Ext.getCmp(this.unameId);
		// validate user name
        if(component.getValue().trim() == '') {
			return iwebos.message.org.message_user_input_user_name;
		}
		
        component = Ext.getCmp(this.pwdId);
		var pwd = component.getValue().trim();
        if(pwd=='' && (this.update == undefined || this.update == false)){
			return iwebos.message.org.message_user_input_user_password;
		}
		    
        component = Ext.getCmp(this.confirmPwdId);
        if(component.getValue() != pwd) {
			return iwebos.message.org.message_user_password_not_match;
		}
		
		// validate the last name
		if(Ext.getCmp(this.lnameId).getValue().trim() == '') {
	 		return iwebos.message.org.message_user_input_last_name;
	 	}
	 	
	 	// validate the first name
	 	if(Ext.getCmp(this.fnameId).getValue().trim() == '') {
	 		return iwebos.message.org.message_user_input_first_name;
	 	}
	 	
	 	// validate the valid of email
	 	if(!Ext.getCmp(this.emailId).validate()) {
	 		return iwebos.message.org.message_user_input_email_invalid;
	 	}
	 	
	 	if(!this.isPhoneNumber(Ext.getCmp(this.workPhoneId).getValue())) {
	 		return iwebos.message.org.message_user_invalid_work_number;
	 	}
	 	
	 	if(!this.isPhoneNumber(Ext.getCmp(this.mobileId).getValue())) {
	 		return iwebos.message.org.message_user_invalid_mobile_number;
	 	}
	 	
	 	if(!this.isPhoneNumber(Ext.getCmp(this.homePhoneId).getValue())) {
	 		return iwebos.message.org.message_user_invalid_home_number;
	 	}
	 	
	 	component=Ext.getCmp(this.emailCheckId);
		if(component.isVisible() && !!(component.getValue())){
			var __forward = Ext.getCmp(this.emailForwardCheckId).getValue(); 
			if(__forward) {
				var emailFwd = Ext.getCmp(this.emailForwardId).getValue();
				if(emailFwd.trim() == '') return iwebos.message.mail.warning_input_address_need_to_forward;
			}	
		}	
	 	
		return '';
	},
	
	/**
	 * Checking the given value whether is a phone number
	 * 
	 * @param {} value String - the value
	 * @return boolean - true if value is valid otherwise return false
	 */
	isPhoneNumber: function(value) {
		if(value == '') return true;
		var __addIndex = value.lastIndexOf('+');
		// The '+' character must at the begining of phone or not occure
		if(__addIndex >= 1) {
			return false;
		}		
		
		if(value.indexOf('  ') != -1 || value.indexOf(' ') == 0 
			|| value.lastIndexOf(' ') == value.length -1) {				
			// make sure it don't contain 2 space continue or a space in the first or end
			return false;
		}
		
		if(__addIndex < 0) {
			// there is no '+' character
			return !isNaN(value);
		}
		
		// split given string with space character
		var __groups = value.split(' ');
		var __length = __groups.length; 
		if(__length != 1 && __length != 4) return false;
		for(var __index = 0; __index < __length; __index++) {
			// check each group is a number
			if(isNaN(__groups[__index])) return false;
		}
		return true;
	},
	
	
	/**
	 * Set the user information for GUI
	 * @param {} user
	 */
	setData: function(user) {
		// clean data
		this.__fnHandleFunction();
		
		var userNameCmp = Ext.getCmp(this.unameId);
		userNameCmp.setValue(user.duname);
		// disable the user name field
		userNameCmp.setDisabled(true);
		
		// enable for button
		this.originalIcon = true;
		document.getElementById(this.checkExistHtmlId).innerHTML 
				= String.format(this.imageHtml, this.buttonIconCls);
		
		var __edit = user.edit==undefined?true:user.edit;
		this._owner.edit = __edit
		this._owner.uname = user.uname;
		var __disable = !__edit;
		
		if(user.org != undefined && user.org != null && user.org != '') {
			Ext.getCmp(this.orgId).setValue(user.org);
		 	this._owner.groups = user.groups;
		} else {
			Ext.getCmp(this.orgId).setValue(this._owner.org);
			this._owner.groups = [{group: this._owner.group}];
		}
		
		var component = Ext.getCmp(this.pwdId);
		component.setValue('');
		component.setDisabled(true);
		
		component = Ext.getCmp(this.confirmPwdId);
		component.setValue('');
		component.setDisabled(true);
		
		component = Ext.getCmp(this.emailId);
		component.setValue(user.email);	
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.lnameId);
		component.setValue(user.lname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.mnameId);
		component.setValue(user.mname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.fnameId);
		component.setValue(user.fname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.employeeNumberId);
		component.setValue(user.employee);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.titleId);
		component.setValue(user.title);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.workPhoneId);
		component.setValue(user.wphone);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.mobileId);
		component.setValue(user.mobile);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.homePhoneId);
		component.setValue(user.hphone);
		component.setDisabled(__disable);
		
		var __mailAccounts = user.list;
		this._fillDataToGrid(__mailAccounts);
		this.grid.setDisabled(__disable);
		
		component=Ext.getCmp(this.emailCheckId);
		if (component) component.setVisible(false);
		
		component=Ext.getCmp(this.emailForwardPanelId);
		if (component) component.setVisible(false);
	},	
	
	/**
	 * Fill data to grid
	 * @param {} __mailAccounts
	 */
	_fillDataToGrid: function(__mailAccounts) {
		var __store = this.grid.store;
		// remove all old record
		__store.removeAll();
		var __records = [];
		if(__mailAccounts != null && __mailAccounts != undefined) {
			for(var __index = 0; __index < __mailAccounts.length; __index++) {
				var __data = __mailAccounts[__index];				
				__data.id = __data.email;
				__records[__index] = new Ext.data.Record(__data);
			}
			__store.add(__records);
			__store.sort('uid', 'ASC');
		}
	},
	
	/**
	 * Get user information from GUI
	 * 
	 * @return {}
	 */
	getData: function(){
		// create object to store data.
		var object = {} ;		
		var component = Ext.getCmp(this.unameId);
		var __uname = component.getValue().trim().toLowerCase();
		component.setValue(__uname);
		object['duname'] = __uname;
		if(this._owner.uname != undefined && this._owner.uname != null) {
			object['uname'] = this._owner.uname;
		}		
		
		component = Ext.getCmp(this.pwdId);
		object['pwd'] = component.getValue();
		
		component = Ext.getCmp(this.emailId);
		object['email'] = component.getValue();
		
		component = Ext.getCmp(this.fnameId);
		object['fname'] = component.getValue();
		
		component = Ext.getCmp(this.mnameId);
		object['mname'] = component.getValue();
		
		component = Ext.getCmp(this.lnameId);
		object['lname'] = component.getValue();
		
		component = Ext.getCmp(this.employeeNumberId);
		object['employee'] = component.getValue();
		
		component = Ext.getCmp(this.titleId);
		object['title'] = component.getValue();
		
		component = Ext.getCmp(this.workPhoneId);
		object['wphone'] = component.getValue();
		
		component = Ext.getCmp(this.mobileId);
		object['mobile'] = component.getValue();
		
		component = Ext.getCmp(this.homePhoneId);
		object['hphone'] = component.getValue();
		
		component = Ext.getCmp(this.orgId);
		object['org'] = component.getValue();
		
		object['groups'] = this._owner.groups;
		
		object['list']=[];
		var __store = this.grid.store;
		var __mailAccounts = [];
		var __count = __store.getCount();
		if(__count > 0) {
			for (var __index = 0; __index < __count; __index++) {
				__mailAccounts[__index] = __store.getAt(__index).data;
			}
			object['list'] = __mailAccounts;
		}
		
		component=Ext.getCmp(this.emailCheckId);
		
		if(component.isVisible() && !!(component.getValue())){
			var __symbol='@';
			var __email = String.format('{0}{1}{2}',__uname,__symbol,object['org']);
			var emailFwd = null;
			var __forward = Ext.getCmp(this.emailForwardCheckId).getValue();
			if(__forward) {
				var __com = Ext.getCmp(this.emailForwardId);
				emailFwd = __com.getValue().trim();
				if(emailFwd == '') __forward = false;
			}
			
			// get exist email in list
			var _existEmail = this._getExisted(object['list'], __email);
			if(_existEmail == null) {
				var __mName = Ext.getCmp(this.mnameId).getValue();
		 		__mName = (__mName != '')?(__mName + ' '):'';
				object['list'].push({
					active: true,
					capacity: 50,
					code: '',
					domain: object['org'],
					groups: this._owner.groups,
					email: __email,
					fAddress: emailFwd,
					fname: __mName + object['fname'],
					forward: __forward,
					id: __email,
					lname: object['lname'],
					smtp: true,
					spam: true,
					symbol:__symbol,
					uid: __uname,
					virus: true
				});
			} else {
				_existEmail.forward = __forward;
				_existEmail.fAddress = emailFwd;
			}
			
		}
		return object;
	},
	
	/**
	 * Get email object with given email in list of email object
	 * 
	 * @param {} emails
	 * @param {} email
	 */
	_getExisted: function(emails, email) {
		var __object;
		for(var __index = 0; __index < emails.length; __index++) {
			__object = emails[__index]; 
			if(__object.email == email) return __object;
		}
		return null;
	},
	
	/**
	 * Opem dialog create account
	 */
	_createMailAccount: function(){
		this._onOpenMailAccountDialog(null, true);
	},	
	 
	/**
	 * update mail account
	 */
	_onUpdateMailAccount: function() {
		// get the selected record
	 	var __record = this.grid.getSelectionModel().getSelections()[0];
	 	if(!__record) return;
	 	
	 	this._onOpenMailAccountDialog(__record.data, this._isEdit(__record.data));
	},
	
	 /**
	  * validate data
	  */
	 _canCreateEmail: function() {
	 	if(Ext.getCmp(this.lnameId).getValue() == '') {
	 		return iwebos.message.mail.empty_fname;
	 	}
	 	
	 	if(Ext.getCmp(this.fnameId).getValue() == '') {
	 		return iwebos.message.mail.empty_lname;
	 	}
	 	
	 	return '';
	 },
	
	/**
	 * open dialog to update mail account 
	 */
	_onOpenMailAccountDialog: function(mailAccount, edit) {
	 	// validate data	
	 	var __error = this._canCreateEmail();
	 	if(__error != '') {
	 		Ext.MessageBox.show({
					title : iwebos.message.account.infomationMessage,
					msg : __error,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return;	
	 	}
	 	// create dialog to add mail account
	 	var __dlg = new iNet.iwebos.ui.account.DialogCreateAccount();
	 	var __action = {save:{fn:this._okHandler, scope:this}}
	 	var __toolbar = Ext.getCmp('manage-user-search-toolbar');
	 	
	 	if(mailAccount == null || mailAccount == undefined) {
	 		var __mName = Ext.getCmp(this.mnameId).getValue();
		 	__mName = (__mName != '')?(__mName + ' '):'';
		 	mailAccount = {};
		 	mailAccount.lname = Ext.getCmp(this.lnameId).getValue();	 	
		 	mailAccount.fname = __mName + Ext.getCmp(this.fnameId).getValue();		 	
	 	}
	 	
		__dlg.show();
	 	__dlg._setData(mailAccount, __action, __toolbar.getOrgs(), 
	 				__toolbar.getSelectedOrg(), this._owner.groups, edit);		
	},
	
	/**
	 * Handle after click ok button
	 */
	_okHandler: function(__object) {
	 	var __store = this.grid.store;
	 	// get the old record
	 	var __item = __store.getAt(__store.find('email',__object.email));
	 	if(__item != null && __item != undefined) {
			__item.data.smtp = __object.smtp;
			__item.data.active = __object.active;
			__item.data.forward = __object.forward;
			if(__item.data.forward) {
				__item.data.fAddress = __object.fAddress;
			}
			__item.data.virus = __object.virus;
			__item.data.spam = __object.spam;
			__item.data.capacity = __object.capacity;
			__item.data.depNumber = __object.depNumber;
			__item.commit();
	 	} else {
	 		var __email = __object.email;
			var __index = __email.indexOf('@'); 
			__object.uid = __email.substr(0,__index);
			__object.symbol = '@';
			__object.domain = __email.substr(__index + 1, __email.length);
	 		__object.id = __email;
	 		__store.addData(__object);
	 	}
	},
	 
	/**
	 * Opem dialog to search user for account
	 */
	_searchAccount: function(){
	 	var dialog = new iNet.iwebos.ui.mail.DialogSearchAccount();
		var action = {fn:this._handleSelectUser, scope:this};
		// set action for dialog
		dialog.__setAction(action);		
		dialog.show(this);
		dialog.setEnableDelChk(true, true);
	},
	
	/**
	 * handle when select account
	 * 
	 * @param {} object
	 */
	 _handleSelectUser: function(object) {
	 	this.loadAccount(object.uname);
	 },
	
	/**
	 * Load user information
	 * 
	 * @param {} uname
	 */ 
	loadUser: function(org, uname) {
	 	if(uname != '') {
	 		var __baseParams = {iwct: 'loadUserContent',iwa: 'READ_WRITE', action: 'loadfull'};
	 		__baseParams.uname = uname;
	 		__baseParams.org = org;
	 		this.update = true;
	 		var __fnSuccess = function(result, request) {
	 			var __data =  Ext.util.JSON.decode(result.responseText);
				var __success = (__data.success == undefined) ? true : __data.success;
				if (__success) {
					// fill data on GUI
					__data.org = org;
					this.setData(__data);
				} else {
					Ext.MessageBox.show({
							title: iwebos.message.org.message,
							msg: iwebos.message.org.error_user_load_user,
							buttons: Ext.MessageBox.OK,
							icon: Ext.MessageBox.ERROR
						});
				}
	 		};
	 		iNet.Ajax.request({
	 			url: 'jsonajax',
	 			params: __baseParams,
	 			method: 'POST',
	 			scope: this,
	 			success: __fnSuccess,
	 			maskEl: this.bwrap
	 		});
	 	} else {
	 		this._cleanData();
	 	}
	},
	 
	/**
	 * load account information
	 * 
	 * @param {Object} code: usercode
	 */
	loadAccount:function(uname){
		if (uname != '') {
			var baseParams = {
				iwct: 'loadUserContent',
				iwa: 'READ_WRITE',
				action: 'loadfulllotus',
				uname: uname
			};
			this.update = true;
			// send request to server.
			iNet.Ajax.request({
				url: 'jsonajax',
				params: baseParams,
				scope: this,
				method: 'POST',
				success: function(result, request){
					var __data =  Ext.util.JSON.decode(result.responseText);
					var __success = (__data.success == undefined) ? true : __data.success;
					if (__success) {
						// fill data on GUI
						this.setData(__data);
					}
					else {
						Ext.MessageBox.show({
							title: iwebos.message.org.message,
							msg: iwebos.message.org.error_user_load_account,
							buttons: Ext.MessageBox.OK,
							icon: Ext.MessageBox.ERROR
						});
					}
				},
				failure: function(result, request){
				},
				maskEl: this.bwrap
			});
		}else{
			this._cleanData();
		}

	},
	 
	/**
	 * save user information
	 */
	onSave: function(){	
		if(this._owner.edit == false) {
			Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : iwebos.message.org.message_user_not_permit_update_user,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return;
		}
		var __action = 'update';
		
    	// validate user information on GUI
		var valid = this.validateUser();
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
		var __data = this.getData();
		
		var __params = {iwct:'userContent', iwa:'READ_WRITE'} ;
		if (!this.update) {
			__action='save';
		}else{
			__action='update';
		}
		__params.action =__action;
		
		/**
	 	* handle save success.
	 	*/
		var onSaveSuccess = function(response, options){
			var __result = Ext.util.JSON.decode(response.responseText);
			var __success = (__result.success == undefined ? true : __result.success);
			if (__success && __result.uname != undefined) {
				// update user name
				__data.uname = __result.uname;
				this._owner.uname = __result.uname;
				if (__action == 'save') {
					this.update = true;
					this._fnHandleAfterSaving();
					// get the main tab
					var __mainTab = Ext.getCmp('manage-user-search-main-tab');
					var __userGridStore = __mainTab.grid.store;
					__data.fullname = __data.lname + ' ' + __data.mname + ' ' + __data.fname;
					__data.id = __data.uname;
					__userGridStore.addData(__data);
				} else {
					 var __mainTab = Ext.getCmp('manage-user-search-main-tab');
					 var __userGridStore = __mainTab.grid.store;					 
					 // get old record
					 var __oldRecord = __userGridStore.getById(__data.uname);
					 if(__oldRecord) {
					 	 // update information
						 __oldRecord.data.fullname = __data.lname + ' ' + __data.mname + ' ' + __data.fname;
						 __oldRecord.data.email = __data.email;
						 __oldRecord.data.title = __data.title;
						 __oldRecord.commit();
					 } else {
					 	__data.fullname = __data.lname + ' ' + __data.mname + ' ' + __data.fname;
					 	__data.id = __data.uname;
					 	 __userGridStore.addData(__data);
					 }
				}
				
				// fill data to grid
				this._fillDataToGrid(__result.list);				
				
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.message_user_saving_successful,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.INFO
				});
			} else if(__result.error == 'exist') {
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.message_user_add_exist_user,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			} else if(__result.error == 'invaliduname') {
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.message_user_invalid_user_name,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}else {
				Ext.MessageBox.show({
					title: iwebos.message.org.message,
					msg: iwebos.message.org.error_user_saving_user,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}
			
		};
		__params['object'] = Ext.util.JSON.encode(__data);
		__params['action'] = __action;
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			scope: this,
			method: 'POST',
			success: onSaveSuccess,
			failure: function(result, request){
			},
			maskEl: this.bwrap
		});
	},
	
	/**
	  * Opem dialog create account
	  */
	 _createMailAccount: function(){
	 	this._onOpenMailAccountDialog(null, true);
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
				text:iwebos.message.mail.create.add,
	            iconCls: 'icon-mail-add',
	            scope: this,
	            handler:this._createMailAccount.createDelegate(this)
            	}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
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
						id: 'account-menu-add-email-account',
						text:iwebos.message.org.user_add_email,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._createMailAccount.createDelegate(this)
              		},{
						id: 'account-menu-update-email-account',
						text:iwebos.message.mail.update_email_account,
		                iconCls: 'icon-mail-update',
		                scope: this,
		                handler:this._onUpdateMailAccount.createDelegate(this)
              		},{
						id: 'account-menu-delete-email-account',
						text:iwebos.message.mail.delete_email_account,
		                iconCls: 'icon-mail-del',
		                scope: this,
		                handler:this._onRemoveMailAccount.createDelegate(this)
              		}]
        	 });
        	 // check whether user can edit mail account or not
        	 var __edit = this._isEdit(selection.data);
        	 Ext.getCmp('account-menu-update-email-account').setDisabled(!__edit);
        	 Ext.getCmp('account-menu-delete-email-account').setDisabled(!__edit);
			// show menu.
			this.mailMenu.showAt(event.getXY());
		}
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
		this._onOpenMailAccountDialog(__data, this._isEdit(__data));
    },
    
	/**
	 * Check user can edit mail account or not
	 * 
	 * @param {} mailAccount - the mail account
	 */
	_isEdit: function(mailAccount) {
	 	/*var __editDomains = this._owner.editDomains; 
	 	if(__editDomains == undefined || __editDomains == null) {
	 		// get the editable domains
	 		__editDomains = this._getEditDomains();	 		
	 		if(__editDomains == null || __editDomains.length == 0) return false;
	 		//cache this domains
	 		this._owner.editDomains = __editDomains;
	 	}
	 	for(var __index = 0; __index < __editDomains.length; __index++) {
	 		if(mailAccount.domain == __editDomains[__index].organization) {
	 			// can edit this account
	 			return true;
	 		}
	 	}
	 	
	 	return false;*/
		return true;
	},
	
	/**
	 * get the editable domains
	 */
	_getEditDomains: function() {
	 	var __toolbar = Ext.getCmp('manage-user-search-toolbar');
	 	// get all viewable domain
	 	var __allDomains = __toolbar.getOrgs();
	 	var __domains = [];
	 	var __domain; 
	 	var __count = 0;
	 	for(var __index = 0; __index < __allDomains.length; __index++) {
	 		__domain = __allDomains[__index];
	 		if(__domain.edit) { // get the editable domain
	 			// add domain to array
	 			__domains[__count] = __domain;
	 			__count++;
	 		}
	 	}
	 	return __domains;
	},
	 
	/**
	 * update mail account
	 */
	_onRemoveMailAccount: function() {
	 	// get the selected record
	 	var __record = this.grid.getSelectionModel().getSelections()[0];
	 	if(!__record) {
	 		Ext.MessageBox.show({
				title: iwebos.message.org.message,
				msg: iwebos.message.org.message_user_delete_email,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
	 		return;
	 	}
	 	// remove the record
	 	this.grid.store.remove(__record);
	 	this.__fnHandleFunction();
	},
	
	/**
	 * clean data on GUI
	 */
	_cleanData: function() {
		// clean data
		this.__fnHandleFunction();
		
		var __disable = false;
		var userNameCmp = Ext.getCmp(this.unameId);
		userNameCmp.setValue('');
		// disable the user name field
		userNameCmp.setDisabled(__disable);
		
		this.originalIcon = true;
		// reset icon
		document.getElementById(this.checkExistHtmlId).innerHTML 
					= String.format(this.linkHtml, this.buttonIconCls);
		
		var component = Ext.getCmp(this.pwdId);
		component.setDisabled(__disable);
		component.setValue('');
		
		component = Ext.getCmp(this.confirmPwdId);
		component.setDisabled(__disable);
		component.setValue('');
		
		component=Ext.getCmp(this.emailCheckId);
		component.setValue(true);
		
		component = Ext.getCmp(this.emailId);
		component.setValue('');	
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.lnameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.mnameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.fnameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.employeeNumberId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.titleId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.workPhoneId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.homePhoneId)
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.mobileId);
		component.setValue('');
		component.setDisabled(__disable);
		
		Ext.getCmp(this.orgId).setValue(this._owner.org);
		
		this.grid.setDisabled(__disable);
		this.grid.store.removeAll();
		this.action = 'save';
		this.update = false;
		this._owner.uname = undefined;
		this._owner.edit = true;
		
		component=Ext.getCmp(this.emailCheckId);
		if (component) component.setVisible(!__disable);
		
		component=Ext.getCmp(this.emailForwardPanelId);
		if (component) component.setVisible(!__disable);
	},
	
	/**
	 * Handle after saving user
	 */
	_fnHandleAfterSaving: function() {		
		var component = Ext.getCmp(this.unameId);
		component.setDisabled(true);
		component = Ext.getCmp(this.pwdId);
		component.setDisabled(true);
		component = Ext.getCmp(this.confirmPwdId);
		component.setDisabled(true);
		component = Ext.getCmp(this.emailCheckId);
		component.setValue(false);
		component.setVisible(false);
		component = Ext.getCmp(this.emailForwardPanelId);
		component.setVisible(false);
	},
	
	/**
    * disable preview panel
    */
	__fnHandleFunction:function(){
		var __infoPanel=Ext.getCmp('previewPanel');
		var __main=Ext.getCmp(this.emailsId);
		__infoPanel.setVisible(false);
		//__main.setHeight(215);
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
    __resizePanel: function(){
        Ext.EventManager.fireWindowResize();
		var __component=Ext.getCmp(this.panelViewId);
		var h=this.getSize().height;
		//__component.setHeight(h-100);
		__component=Ext.getCmp(this.emailsId);
		__component.setHeight(h-225);
    }
});