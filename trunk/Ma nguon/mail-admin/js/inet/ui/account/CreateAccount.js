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
 * @class iNet.iwebos.ui.mail.CreateAccount
 * @extends Ext.Panel
 * @constructor
 * Creates a new Panel
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.account.CreateAccount = function(viewer, config){
	// setting the prefix identifier.
	this.prefix = (!this.prefix) ? 'iwebos-mail-create' : this.prefix ;
	
	this.usernameId = this.prefix + '-uid-id' ;
	this.emailId = this.prefix + '-email-id' ;
	this.passwordId = this.prefix + '-password-id' ;
	this.confirmPasswordId = this.prefix + '-confirm-password-id' ;
	
	this.birthdayId= this.prefix + '-birthdaydate-id' ;
	this.sexId= this.prefix + '-sex-id' ;
	this.cityId= this.prefix + '-city-id' ;
	this.countryId= this.prefix + '-country-id' ;
	
	this.lastNameId= this.prefix + '-lastname-id' ;
	this.midleNameId= this.prefix + '-midlename-id' ;
	this.firstNameId= this.prefix + '-firstname-id' ;

	this.languageId= this.prefix + '-language-id' ;
	this.provinceId= this.prefix + '-province-id' ;

	this.postCodeId= this.prefix + '-postcode-id' ;
	this.timezoneId= this.prefix + '-timezone-id' ;
	this.emailsId= this.prefix + '-emails-information-id' ;
	
	this.infoPanel=this.prefix + '-infor-panel-id' ;
	//this.smdSaveId = this.prefix + '-save-button-id' ;
	
	this.__owner={code:'',action:'save'};
	
	var sortInfo = {
        field: 'uid',
        direction: 'DESC'
    };
	var defaultSort = {
		field: 'uid', 
		direction: "DESC"
	};

    var baseParams = {
        iwct: 'searchMailAccountContent',
        iwa: 'view',
		id: 0,
        action: 'search'
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
		region: 'north',
		anchor: '100%',
		autoScroll: true,
		height: 180,
		url: 'test.txt',
		method: 'POST',
		sortInfo: sortInfo,
		loadFirst: false,
		baseParams: baseParams,
		reader: reader,
		rowClass: fnRowClass,
		dsort: defaultSort,
		cm: columnModel,
		filter: {
			data: 'all',
			fn: filterFn
		},
		view: new Ext.grid.GridView({
			forceFit: true
		}),
		tbar:[{
			id:'btn-create-newacc',
			xtype:'button',
			text:iwebos.message.mail.create.add,
			iconCls: 'icon-mail-add',
			scope:this,
			handler: this._createMailAccount.createDelegate(this)
		}]
	});

	// the actions
    this.actions = {
		'account-creat-new':this.onAdd.createDelegate(this),
		'account-save':this.onSave.createDelegate(this),
		'account-search':this._searchAccount.createDelegate(this)
    };
    // Sex store
    var __sexStore= new Ext.data.SimpleStore({
        fields: ['value', 'text'],
        data: [['male', iwebos.message.mail.create.male], ['female', iwebos.message.mail.create.female]]
    });
    
    // the language store
    var _languageStore = new Ext.data.SimpleStore({
        fields: ['value', 'text'],
        data: [['',''],['vi', iwebos.message.mail.create.vietnameseLanguage],['en', iwebos.message.mail.create.englishLanguage]]
    });
    
    // the country store
    var _countryStore = new Ext.data.SimpleStore({
        fields: ['value', 'text'],
        data: [['vn', iwebos.message.mail.create.vietnamese],['ot', iwebos.message.mail.create.otherCountry]]
    }); 
	
	var showTimez = [['-1200', '(GMT -12:00) Eniwetok, Kwajalein'],
					 ['-1100', '(GMT -11:00) Midway Island, Samoa'],
					 ['-1000', '(GMT -10:00) Hawaii'],
					 ['-0900', '(GMT -9:00) Alaska'],
					 ['-0800', '(GMT -8:00) Pacific Time (US &amp; Canada)'],
					 ['-0700', '(GMT -7:00) Mountain Time (US &amp; Canada)'],
					 ['-0600', '(GMT -6:00) Central Time (US &amp; Canada), Mexico City'],					 
					 ['-0500', '(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima'],
					 ['-0430', '(GMT -4:30) Caracas'],
					 ['-0400', '(GMT -4:00) Atlantic Time (Canada), La Paz, Santiago'],
					 ['-0330', '(GMT -3:30) Newfoundland'],
					 ['-0300', '(GMT -3:00) Brazil, Buenos Aires, Georgetown'],
					 ['-0200', '(GMT -2:00) Mid-Atlantic'],
					 ['-0100', '(GMT -1:00) Azores, Cape Verde Islands'],
					 ['0000',' (GMT) Western Europe Time, London, Lisbon, Casablanca'],
					 ['+0100', '(GMT +1:00) Brussels, Copenhagen, Madrid, Paris'],
					 ['+0200', '(GMT +2:00) Kaliningrad, South Africa'],
					 ['+0300', '(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg'],
					 ['+0330', '(GMT +3:30) Tehran'],
					 ['+0400', '(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi'],
					 ['+0430', '(GMT +4:30) Kabul'],
					 ['+0500', '(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent'],
					 ['+0530', '(GMT +5:30) Mumbai, Kolkata, Chennai, New Delhi'],
					 ['+0545', '(GMT +5:45) Kathmandu'],
					 ['+0600', '(GMT +6:00) Almaty, Dhaka, Colombo'],
					 ['+0630', '(GMT +6:30) Yangon, Cocos Islands'],
					 ['+0700', '(GMT +7:00) Ha Noi, Bangkok, Jakarta'],
					 ['+0800', '(GMT +8:00) Beijing, Perth, Singapore, Hong Kong'],
					 ['+0900', '(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk'],
					 ['+0930', '(GMT +9:30) Adelaide, Darwin'],
					 ['+1000', '(GMT +10:00) Eastern Australia, Guam, Vladivostok'],
					 ['+1100', '(GMT +11:00) Magadan, Solomon Islands, New Caledonia'],
					 ['+1200', '(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka']];
	var __storeTimeZone = new Ext.data.SimpleStore({
        fields: ['value', 'text'],
        data: showTimez
    });
	
    var requiredText = '*';
    
    // create main panel.
    var __topPn = new Ext.form.FormPanel({
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
					fieldLabel: iwebos.message.mail.create.username + requiredText
				},{
					id:this.passwordId,
					xtype: 'textfield',
					inputType: 'password',					
					tabIndex: 3,
					fieldLabel: iwebos.message.mail.create.password + requiredText,
					anchor: '95%'
				}]
			},{
				columnWidth: 0.5,
				layout: 'form',
				anchor: '100%',
				labelWidth: 70,
				border: false,
				items:[{
					id: this.emailId,
					xtype: 'textfield',
					anchor: '95%',
					vtype:'email',					
					tabIndex: 2,
					fieldLabel: iwebos.message.mail.create.email
				},{
					id:this.confirmPasswordId,
					xtype: 'textfield',
					inputType: 'password',					
					tabIndex: 4,
					fieldLabel: iwebos.message.mail.create.confirmpassword + requiredText,
					anchor: '95%'
				}]
			}]
		 },{
			border: false,
			html: iwebos.message.mail.create.personal_info
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
					layout: 'column',
					columnWidth: 1,
					anchor: '95%',
					border: false,
					items:[{
						layout: 'form',
						columnWidth: 0.5,
						anchor: '100%',
						border: false,
						items:[{
							id: this.lastNameId,
							xtype: 'textfield',
							anchor: '98%',
							tabIndex: 5,
							fieldLabel: iwebos.message.mail.create.lastname + requiredText
						}]
					},{
						layout: 'form',
						columnWidth: 0.5,
						anchor: '95%',
						border: false,
						items:[{
							id: this.midleNameId,
							xtype: 'textfield',
							anchor: '100%',
							tabIndex: 6,
							fieldLabel: iwebos.message.mail.create.middlename
						}]
					}]
				},{
					id:this.birthdayId,
					xtype: 'datefield',
					format: 'd/m/Y',
					tabIndex: 8,
					fieldLabel:iwebos.message.mail.create.birthday,
					anchor: '95%'
				},{
					id: this.cityId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 10,
					fieldLabel: iwebos.message.mail.create.city
				}]
			},{
				columnWidth: 0.5,
				layout: 'form',
				anchor: '100%',
				labelWidth: 70,
				border: false,
				items:[{
					id: this.firstNameId,
					xtype: 'textfield',
					anchor: '95%',
					allowBlank:true,
					tabIndex: 7,
					fieldLabel: iwebos.message.mail.create.firstname + requiredText
				},{
					id: this.sexId,
					xtype: 'combo',
					anchor: '95%',
					tabIndex: 9,
					fieldLabel: iwebos.message.mail.create.sex,
					store: __sexStore,
					displayField: 'text',
					valueField: 'text',
					readOnly: true,
					mode: 'local',
					triggerAction: 'all',
					selectOnFocus: true
				
				},{
					id: this.countryId,
					xtype: 'combo',
					anchor: '95%',
					tabIndex: 11,
					fieldLabel: iwebos.message.mail.create.country + requiredText,
					store: _countryStore,
					displayField: 'text',
					valueField: 'value',
					value: 'vn',
					readOnly: true,
					mode: 'local',
					triggerAction: 'all',
					selectOnFocus: true
				}]
			}]
		},{
			border: false,
			html: iwebos.message.mail.create.other_info
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
					id: this.languageId,
					xtype: 'combo',
					anchor: '95%',
					tabIndex: 12,
					fieldLabel: iwebos.message.mail.create.language,
					store: _languageStore,
					displayField: 'text',
					valueField: 'value',
					value: 'vi',
					readOnly: true,
					mode: 'local',
					triggerAction: 'all',
					selectOnFocus: true
				},{
					id: this.postCodeId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 14,
					fieldLabel: iwebos.message.mail.create.postcode
				}]
			},{
				columnWidth: 0.5,
				layout: 'form',
				anchor: '100%',
				labelWidth: 70,
				border: false,
				items:[{
					id: this.provinceId,
					xtype: 'textfield',
					anchor: '95%',
					tabIndex: 13,
					fieldLabel: iwebos.message.mail.create.province
				},{
					id: this.timezoneId,
					xtype: 'combo',
					store : __storeTimeZone,
					readOnly: true,
					displayField: 'text',
					valueField: 'value',
					anchor: '95%',
					tabIndex: 15,
					allowBlank:false,
					value: '+0700',
					fieldLabel:iwebos.message.mail.create.timezone + requiredText,
					mode: 'local',
					triggerAction: 'all',
					selectOnFocus: true
					
				}]
			}]
		 }]
	});

	var __preView = new Ext.Panel({
        id: 'previewPanel',
		region: 'center',
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
	
    
    /**
     * When rowselect
     */
	
    this.grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r){
		    	
		var __record = this.grid.store.getAt(rowIdx) ;
		var __data = __record.data ;
		
		//apply template mail panel					
		var __detailPanel = Ext.getCmp('previewPanel');
		// header
		if (__detailPanel) {
			itemTpl.overwrite(__detailPanel.body, __data);
			var __infoPanel=Ext.getCmp('previewPanel');
			var __main=Ext.getCmp(this.emailsId);
			__infoPanel.setVisible(true);
			__main.setHeight(350);
		}else{
		}
	},this);
	
	var __panelView = new Ext.FormPanel({
		region:'center',
		anchor: '98%',
		width: '98%',
		border: false,
		hideBorders:true,
		labelWidth: 150,
		bodyStyle:'padding:5px 5px 0',
		items: [{
			id:this.emailsId,
			xtype:'fieldset',
			checkboxToggle: true,
			collapsed: true,
			hideBorders :false,
			border: Ext.isIE,
			title:iwebos.message.mail.view_info_account,
			height: 215,
			layout:'border',
			width: '99%',
			items:[this.grid,__preView]
		}]
	});
	
	var __main = new Ext.Panel({
		region:'center',
		autoScroll:true,
		border: false,
        items :[__topPn,{
			border:false,
			html:'<br />'
		},__panelView]
	}); 
    __main.on('resize', this.__resizePanel, this);
    
	//this.grid.selModel.on("selectionchange", this.__fnHandleFunction,this);
    this.grid.on('contextmenu', this._contextMenu, this, {stopEvent:true});
    this.grid.on('rowcontextmenu', this._rowContextMenu, this, {stopEvent:true});
    this.grid.on('rowdblclick', this._onDoubleClick, this);
	
    iNet.iwebos.ui.account.CreateAccount.superclass.constructor.call(this, {
        id: 'new-account',
        iconCls: 'icon-create-account',
        title: iwebos.message.mail.create.atitle,
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

/**
 * Extends Class Ext.Panel
 */
Ext.extend(iNet.iwebos.ui.account.CreateAccount, Ext.Panel, {
	/**
	  * Opem dialog create account
	  */
	 _createMailAccount: function(){
	 	this._onOpenMailAccountDialog(null, true);
	 },
	 
	 /**
	  * validate data
	  */
	 _canCreateEmail: function() {
	 	if(Ext.getCmp(this.lastNameId).getValue() == '') {
	 		return iwebos.message.mail.empty_fname;
	 	}
	 	
	 	if(Ext.getCmp(this.firstNameId).getValue() == '') {
	 		return iwebos.message.mail.empty_lname;
	 	}
	 	
	 	return '';
	 },
	 
	 /**
	  * Handle after click ok button
	  */
	 _okHandler: function(__object) {
	 	var __store = this.grid.store;	 	
	 	// get the old record
	 	var __item = __store.getById(__object.email);	 		
	 	if(__item != null && __item != undefined) {
			__item.data.smtp = __object.smtp;
			__item.data.active = __object.active;
			__item.data.forward = __object.forward;
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
	  * update mail account
	  */
	 _onUpdateMailAccount: function() {
	 	// get the selected record
	 	var __record = this.grid.getSelectionModel().getSelections()[0];
	 	if(!__record) return;
	 	
	 	this._onOpenMailAccountDialog(__record.data, true);
	 },
	 
	 /**
	  * update mail account
	  */
	 _onRemoveMailAccount: function() {
	 	// get the selected record
	 	var __record = this.grid.getSelectionModel().getSelections()[0];
	 	if(!__record) return;
	 	// remove the record
	 	this.grid.store.remove(__record);
	 	this.__fnHandleFunction();
	 },
	 
	 /**
	  * get the editable domains
	  */
	 _getEditDomains: function() {
	 	var __toolbar = Ext.getCmp('account-main-tabs-toolbar');
	 	// get all viewable domain
	 	var __allDomains = __toolbar.getDomains();
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
	 	var __action = {save:{fn:this._okHandler, scope:this}};
	 	var __toolbar = Ext.getCmp('account-main-tabs-toolbar');
	 	
	 	if(mailAccount == null || mailAccount == undefined) {
	 		var __mName = Ext.getCmp(this.midleNameId).getValue();
		 	__mName = (__mName != '')?(__mName + ' '):'';
		 	mailAccount = {};
		 	mailAccount.lname = Ext.getCmp(this.lastNameId).getValue();	 	
		 	mailAccount.fname = __mName + Ext.getCmp(this.firstNameId).getValue();		 	
	 	}
	 	
		__dlg.show();
	 	__dlg._setData(mailAccount, __action, __toolbar.getDomains(), __toolbar.getSelectedDomain(), edit);		
	 },
	 
	 /**
	  * Check user can edit mail account or not
	  * 
	  * @param {} mailAccount - the mail account
	  */
	 _isEdit: function(mailAccount) {
	 	var __editDomains = this.__owner.editDomains; 
	 	if(__editDomains == undefined || __editDomains == null) {
	 		// get the editable domains
	 		__editDomains = this._getEditDomains();	 		
	 		if(__editDomains == null || __editDomains.length == 0) return false;
	 		//cache this domains
	 		this.__owner.editDomains = __editDomains;
	 	}
	 	for(var __index = 0; __index < __editDomains.length; __index++) {
	 		if(mailAccount.domain == __editDomains[__index].domainName) {
	 			// can edit this account
	 			return true;
	 		}
	 	}
	 	
	 	return false;
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
						text:iwebos.message.mail.create.add,
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
	  * Opem dialog to search user for account
	  */
	 _searchAccount: function(){
	 	var dialog = new iNet.iwebos.ui.account.DialogSearchAccount();
		var action = {fn:this._handleSelectUser, scope:this};
		// set action for dialog
		dialog.__setAction(action);
		dialog.show(this);
	 },
	 
	 /**
	  * handle when select user
	  * 
	  * @param {} object
	  */
	 _handleSelectUser: function(object) {
	 	var __mainTab = Ext.getCmp('account-main-tabs');
	 	this.loadData(object.id);
	 },
	 
	/**
     * process action.
     */
    doAction: function(event, target){
    	if(this.actions !== undefined){
    		this.actions[target.id]() ;
    	}
    },
	/**
	 * validat user information
	 */
	validateUser: function() {
		var __code = this.__owner.code;
		var component = Ext.getCmp(this.usernameId);
        if(component.getValue() == '') {
			return iwebos.message.mail.error_input_username;
		}
           
        component = Ext.getCmp(this.passwordId);
		var pwd = component.getValue();
        if(pwd=='' && (__code=='' || __code==null ||__code.length==0)){
			return iwebos.message.mail.error_input_pass;
		}
		    
        component = Ext.getCmp(this.confirmPasswordId);
        if(component.getValue() != pwd) {
			return iwebos.message.mail.error_input_confirm_pass;
		}
		
		if(Ext.getCmp(this.lastNameId).getValue() == '') {
	 		return iwebos.message.mail.empty_fname;
	 	}
	 	
	 	if(Ext.getCmp(this.firstNameId).getValue() == '') {
	 		return iwebos.message.mail.empty_lname;
	 	}
            
        component = Ext.getCmp(this.countryId);
        if(component.getValue()=='') {
			return iwebos.message.mail.error_input_country;
		}
            
        component = Ext.getCmp(this.timezoneId);
		if(component.getValue()=='') {
			return iwebos.message.mail.error_input_gmt;
		}
		
	 	if(this.grid.store.getCount() <= 0) {
	 		return iwebos.message.mail.empty_mail_account;
	 	}
		
		return '';
	},
	/**
	 * clean data on GUI
	 */
	_cleanData: function() {
		// clean data
		this.__fnHandleFunction();
		
		var __disable = false;
		//Ext.getCmp(this.smdSaveId).setDisabled(__disable);
		var userNameCmp = Ext.getCmp(this.usernameId);
		userNameCmp.setValue('');
		// disable the user name field
		userNameCmp.setDisabled(__disable);
		
		var component = Ext.getCmp(this.passwordId);
		component.setDisabled(__disable);
		component.setValue('');
		
		component = Ext.getCmp(this.confirmPasswordId);
		component.setDisabled(__disable);
		component.setValue('');
		
		component = Ext.getCmp(this.emailId);
		component.setValue('');	
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.birthdayId);
		component.setValue(null);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.sexId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.cityId);
		component.setValue('');		
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.countryId);
		component.setValue('vn');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.lastNameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.midleNameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.firstNameId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.languageId);
		component.setValue('vi');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.provinceId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.postCodeId);
		component.setValue('');
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.timezoneId);
		component.setValue('+0700');
		component.setDisabled(__disable);
		
		this.grid.store.removeAll();
		//clean the code
		this.__owner.code = null;
		this.action = 'save';
	},
	/**
	 * fill information on GUI
	 * 
	 * @param {} account - the account information
	 */
	setData: function(account) {
		// clean data
		this.__fnHandleFunction();
		
		var userNameCmp = Ext.getCmp(this.usernameId);
		userNameCmp.setValue(account.uname);
		// disable the user name field
		userNameCmp.setDisabled(true);
		var __disable=true;
		this.__owner.code=account.userCode;
		var __code=this.__owner.code;
		if (__code=='' || __code==null ||__code.length==0){
			__disable=true;
		}else{
			__disable=false;
		}
		var component = Ext.getCmp(this.passwordId);
		component.setValue('');
		component.setDisabled(true);
		component = Ext.getCmp(this.confirmPasswordId);
		component.setValue('');
		component.setDisabled(true);
		
		var component = Ext.getCmp(this.emailId);
		component.setValue(account.email);	
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.birthdayId);
		component.setValue((account.birthday == 0 )?null:new Date(account.birthday));
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.sexId);
		component.setValue(account.gentle);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.cityId);
		component.setValue(account.city);		
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.countryId);
		component.setValue(account.country);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.lastNameId);
		component.setValue(account.lname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.midleNameId);
		component.setValue(account.mname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.firstNameId);
		component.setValue(account.fname);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.languageId);
		component.setValue(account.language);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.provinceId);
		component.setValue(account.state);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.postCodeId);
		component.setValue(account.pcode);
		component.setDisabled(__disable);
		
		component = Ext.getCmp(this.timezoneId)
		component.setValue(account.tzone);
		component.setDisabled(__disable);
		
		var __store = this.grid.store;
		// remove all old record
		__store.removeAll();
		var __mailAccounts = account.list;
		if(__mailAccounts != null && __mailAccounts != undefined) {
			for(var __index = 0; __index < __mailAccounts.length; __index++) {
				var __data = __mailAccounts[__index];
				__data.id = __data.email;
				__store.addData(__data);
			}
		}		
	},	
   /**
	 * return the object  data.
	 */
    getData: function(){
		// create object to store data.
		var object = {} ;		
		object['userCode'] = this.__owner.code;
		var component = Ext.getCmp(this.usernameId);
		var __uname = component.getValue().toLowerCase();
		component.setValue(__uname);
		object['uname'] = __uname;
		
		component = Ext.getCmp(this.passwordId);
		object['pwd'] = component.getValue();
		
		component = Ext.getCmp(this.emailId);
		object['email'] = component.getValue();
		
		component = Ext.getCmp(this.countryId);
		object['country'] = component.getValue();
		
		component = Ext.getCmp(this.timezoneId);
		object['tzone'] = component.getValue();
		
		component = Ext.getCmp(this.birthdayId);
		object['birthday'] = ((component.getValue() == '') ? '' : component.getValue().format('d/m/Y')) ;
		
		component = Ext.getCmp(this.cityId);
		object['city'] = component.getValue();
		
		
		component = Ext.getCmp(this.firstNameId);
		object['fname'] = component.getValue();
		
		component = Ext.getCmp(this.midleNameId);
		object['mname'] = component.getValue();
		
		component = Ext.getCmp(this.lastNameId);
		object['lname'] = component.getValue();
		
		component = Ext.getCmp(this.sexId);
		object['gentle'] = component.getValue();
		
		component = Ext.getCmp(this.provinceId);
		object['state'] = component.getValue();
		
		component = Ext.getCmp(this.languageId);
		object['language'] = component.getValue();
		
		component = Ext.getCmp(this.postCodeId);
		object['pcode'] = component.getValue();
		
		var __store = this.grid.store;
		var __mailAccounts = [];
		var __count = __store.getCount();
		if(__count > 0) {
			for (var __index = 0; __index < __count; __index++) {
				__mailAccounts[__index] = __store.getAt(__index).data;
			}
			object['list'] = __mailAccounts;
		}
		
		return object;
	},
    /**
     * Resize panel
     */
    __resizePanel: function(){
        Ext.EventManager.fireWindowResize();
    },
   /**
    * disable preview panel
    */
	__fnHandleFunction:function(){
			var __infoPanel=Ext.getCmp('previewPanel');
			var __main=Ext.getCmp(this.emailsId);
			__infoPanel.setVisible(false);
			__main.setHeight(215);
	},
	/**
	 * load data
	 * @param {Object} code: usercode
	 */
	loadData:function(code){
		if (code != '') {
			var baseParams = {
				iwct: 'loadAccountContent',
				iwa: 'READ_WRITE',
				action: 'loadFull',
				userCode: code
			};
			this.__owner.code = code;
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
							title: iwebos.message.account.infomationMessage,
							msg: iwebos.message.mail.error_find_account_information,
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
	 * Save data
	 */
	onSave: function(){	
		var __code=this.__owner.code;
		var __action = 'update';
		
    	// validate user information on GUI
		var valid = this.validateUser();
		if(valid != '') {
			Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : valid,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});

			return;
		}
		
		// get user information
		var __data = this.getData();
		
		var __params = {iwct:'accountContent', iwa:'READ_WRITE'} ;
		if (__code=='' || __code==null ||__code.length==0) {
			__action='save';
		}else{
			__action='update';
		}
		__params.action=__action;
		/**
	 	* handle save success.
	 	*/
		var onSaveSuccess = function(response, options){
			var __result = Ext.util.JSON.decode(response.responseText);
			var __success = (__result.success == undefined ? true : __result.success);
			if (__success) {
				if (__action == 'save') {
					if (__result != null) {
						this.__owner.code = __result.userCode;
						
						var component = Ext.getCmp(this.usernameId);
						component.setDisabled(true);
						component = Ext.getCmp(this.passwordId);
						component.setDisabled(true);
						component = Ext.getCmp(this.confirmPasswordId);
						component.setDisabled(true);
					}
				}
				var __code = this.__owner.code;
				if(__code != null && __code != undefined && __code != '') {					
					// get the main tab
				    var __mainTab = Ext.getCmp('account-main-tabs');
				    var __userGridStore = __mainTab.grid.store;
				    var __userItem = __userGridStore.getById(__code);				    
				    var __exist = __userItem != null && __userItem != undefined;
				    var __selectedDomain = Ext.getCmp('account-main-tabs-toolbar').getSelectedDomain(); 
					// get the store
				    var __store = this.grid.store;
				    var __count = __store.getCount();
				    // update for data on grid
				    for(var __index = 0; __index < __count; __index++) {
				    	var __item = __store.getAt(__index);
				    	if(__item.data.code == null || __item.data.code == undefined) {
				    		__item.data.code = __code;
				    		__item.commit();
				    		if(!__exist && __selectedDomain == __item.data.domain) {
				    			var __object = {};
				    			__object.id = __code;
				    			__object.code = __code;
				    			__object.fname = __item.data.lname + ' ' + __item.data.fname;	
				    			__object.email = __item.data.email;
				    			__object.edit = true;
				    			__userGridStore.addData(__object);
				    			__exist = true;
				    		}
				    	}
				    }
				}
				
				Ext.MessageBox.show({
					title: iwebos.message.account.savingTitle,
					msg: iwebos.message.mail.save_data_sucessful,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.INFO
				});
			}
			else {
				Ext.MessageBox.show({
					title: iwebos.message.mail.error,
					msg: iwebos.message.mail.error_create_account,
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
	onAdd:function(){
		this.__owner.code='';
		this._cleanData();
	}
});
