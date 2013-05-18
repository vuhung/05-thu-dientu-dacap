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
 * @class iNet.iwebos.ui.mail.grid.MailViewPanel
 * @extends Ext.TabPanel
 *
 * @constructor
 * Creates a new TabPanel for email
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.grid.MailViewPanel = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    this.__owner = {};
    
    this._superRole = 'ROLE_SUPER';
    this._domainRole = 'ROLE_DOMAIN';
    //
    // initialization the grid.
    //
    var sortInfo = {
        field: 'email',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'email', 
		direction: "ASC"
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
	{name: 'isMail', mapping: 'isMail', type: 'boolean'}, 
	{name: 'email', mapping: 'email', type: 'string'},
	{name: 'displayname', mapping: 'displayname', type: 'string'},
	{name: 'smtp', mapping: 'smtp', type: 'boolean'},
	{name: 'capacity', mapping: 'capacity', type: 'string'},
	{name: 'active', mapping: 'active', type: 'boolean'},
	{name: 'forward', mapping: 'forward', type: 'boolean'},
	{name: 'virus', mapping: 'virus', type: 'boolean'},
	{name: 'spam', mapping: 'spam', type: 'boolean'},
	{name: 'date', mapping: 'date', type: 'int'}	
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'email'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
			new iNet.iwebos.ui.mail.grid.isMail(),
		{
			header: iwebos.message.mail.mail_account,
			dataIndex: 'email',
			sortable: true,
			width: 200
		},{
			header: iwebos.message.mail.fullname,
			dataIndex: 'displayname',
			sortable: true,
			width: 180
		},{
			header: iwebos.message.mail.create.sizelimit,
			dataIndex: 'capacity',
			sortable: true,
			fixed: true,
			width: 100
		},{
			header: iwebos.message.mail.create_date,
			dataIndex: 'date',
			sortable: true,
			width: 80,
			fixed: true,
			renderer: iNet.dateRenderer('d/m/Y'),
			groupRenderer: iNet.dateGroupRenderer()
		}, new iNet.iwebos.ui.mail.grid.Active()
	]);
    
    // create filter by function, filter by status.
    var filterFn = function(item, value){
        return (item.data.status === value);
    };
    
	// show color in row.
	var fnRowClass = function(record){
		var read = record.data.read ;	
		return 'grid-show-rows';
	}
	
    // create email message grid.
    this.grid= new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: 'emailMessageId',
		region: 'center',
		anchor: '100%',
		autoScroll: true,
		url: 'jsonajax',
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
		})
	});
    
    //when row is right clicked
    this.grid.on('rowcontextmenu', this._onRowContextMenu, this, {stopEvent:true});
    this.grid.on('contextmenu', this._onContextMenu.createDelegate(this), this, {stopEvent:true}) ;
    //when a row is double clicked
    this.grid.on('rowdblclick', this.onDoubleClick, this);
	// resize grid.
	this.grid.un('resize', this.onGridResize, this) ;
	
    this.adSearch = new iNet.iwebos.ui.mail.Toolbar({
        region: 'north',
        width: '100%',
        frame: false,
        border: false,
        height: 25,
        store: this.grid.store
    });
	
	this.adSearch.toolbarSeach.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
	this.adSearch.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
    
	var __params = {iwct:'loadPermissionContent', iwa:'READ_WRITE', action : 'load'} ;
	var __permissionOk = function(response, options){
		var __result = eval('(' + response.responseText + ')');
        if (!!__result) {
        	var __role = __result.role || this._domainRole; 
        	this.__owner.role = __role;  
	    
	    
	    
		    var __taskPanel = Ext.getCmp('mail-panel-action-id') || {} ;
		    var __roleSystem = (__role === this._superRole? true : false);
		    Ext.getCmp('domain-id').setEditDomain(__roleSystem);
		    //this.adSearch.showDepartment(!__roleSystem);
			// enable panel.
			if(__taskPanel && __taskPanel.body){
				var __bd = __taskPanel.body ;
				__bd.select('li[id="li-task-view-create-domain"]').setDisplayed(__roleSystem) ;
				__bd.select('li[id="li-task-report"]').setDisplayed(true) ;
				__bd.select('li[id="li-task-import"]').setDisplayed(true) ;
	 		}
        }
	};
	
	iNet.Ajax.request({
                        url: 'jsonajax',
                        success: __permissionOk,
                        failure: function(response, options){},
                        method: 'POST',
                        params: __params,
                        scope : this,
                        maskEl: this.bwrap
                 	});
    // process action when user click in action panel.
    this.actions = {
		'task-view-create-domain':this._fnCreateDomain.createDelegate(this),
		'task-report': this._fnReport.createDelegate(this),
		'task-import': this._fnImport.createDelegate(this)
    };
    
    // create column model.
    var followupColumn = new Ext.grid.ColumnModel([ {
        fixed: true,
        width: '100%',
        sortable: true,
        dataIndex: 'subject'
    }]);
    
	this.groupBy = new Ext.Panel({
        frame: true,
		//layout: 'border',
		region: 'center',
		height:50
    });
	
    //main grid
    this.gridMain = new Ext.Panel({
        layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        items: [this.adSearch,  this.grid]
    });
        
    this.main = new Ext.Panel({
        layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        items: [this.groupBy,this.gridMain]
    });
	    	
    iNet.iwebos.ui.mail.grid.MailViewPanel.superclass.constructor.call(this, {
        id: 'main-tabs',
        activeTab: 0,
        region: 'center',
        anchor: '100%',
        enableTabScroll: true,
        autoScroll: true,
        frame: false,
        border: false,
        items: {
            title: iwebos.message.mail.manage_account,
            iconCls: 'icon-mail-home',
            layout: 'border',
            hideMode: 'offsets',
            items: [this.main]
        }
    });
};

Ext.extend(iNet.iwebos.ui.mail.grid.MailViewPanel, Ext.TabPanel, {
	/**
	 * Call new Dialog Create Domain
	 */
	_fnCreateDomain:function(){
		var tree = Ext.getCmp('domain-id');
		if(!tree) return;
		
		// open dialog to create/update domain
		var dialog = new iNet.iwebos.ui.mail.DialogCreateDomain();		
		
		// create the action
		var action = {save:{fn:tree.__onSave,scope:tree}};
		
		dialog.setData(null, action);
		dialog.show(this);
	},
	
	/**
	 * Open report tab
	 */
	_fnReport: function() {
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('report-mail-acc-tab');
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.ReportPanel();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
	},
	
	/**
	 * Open import data dialog
	 */
	_fnImport: function() {
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('import-mail-acc-tab');
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.ImportPanel();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
	},
	
	/**
	 * add new tab
	 * @param {Object} type
	 */
	_newAlias : function(type){
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('new-alias-main-id');
        var __domain = this.__owner.domain || '';
        var __account = '';
        var __action = {save: {fn:this._onSaveAlias, scope: this}};
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.CreateAlias();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
        __tab.loadInfo(__domain, this.adSearch.getDepartmentName(),__account,__action,true);
	},
		
	/**
	 * add new tab
	 * @param {Object} type
	 */
	_newAccountEmail : function(){
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('new-email-panel');
        var __domain = this.__owner.domain || '';
        var __account = '';
        var __action = {save: {fn:this._onSaveAccount, scope: this}};
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.CreateEmail();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
        // fill data for account tab
		__tab.loadInfo(__domain, this.adSearch.getDepartments(), 
					this.adSearch.getDepartmentName(),__account,__action, true);		
	},
	
	/**
	 * save mail account
	 * 
	 * @param {Object} object - the email information
	 * @param {Object} userObject - the user information
	 */
	_onSaveAlias: function(panel, object, action) {
		var __store = this.grid.store;
		var __object = object || {};
		var __action = action || 'save';
		var saveSuccess = function(response, options) {
			var __data = eval('(' + response.responseText + ')');
			if(__data.success) { // saving success
				if(__action == 'save'){
					// add create date to object
					__object.date = (new Date()).getTime();
					__object.displayname = __object.lastName + ' ' + __object.fname;
					__object.id = __object.email;
					__store.addData(__object) ;
					//update account variable
					panel.__owner.account = __object.email;
					panel.handleAfterSave();
				} else {
					// update data
					var __oldRecord = __store.getById(__object.email) ;
					if(__oldRecord != null && __oldRecord != undefined) {
						__oldRecord.data.displayname = __object.lastName + ' ' + __object.fname;
						__oldRecord.data.smtp = __object.smtp;
						__oldRecord.data.active = __object.active;
						__oldRecord.data.forward = __object.forward;
						__oldRecord.data.virus = __object.virus;
						__oldRecord.data.spam = __object.spam;
						__oldRecord.commit();
					}
				}	
				Ext.MessageBox.show({
								title : iwebos.message.mail.saving_mail_alias,
								msg : iwebos.message.mail.save_data_sucessful,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
			} else {
				Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_create_account,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
			}
		};
		
		var __params = {iwct:'mailAliasContent', iwa:'READ_WRITE'} ;
		__params['action'] = __action;
		__params['object'] = Ext.util.JSON.encode(__object);
			
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			method: 'POST',
			success: saveSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap
		});	
	},
	
	/**
	 * save mail account
	 * 
	 * @param {Object} object - the email information
	 * @param {Object} userObject - the user information
	 */
	_onSaveAccount: function(panel, object, action) {
		var __store = this.grid.store;
		var __object = object || {};
		var __action = action || 'save';
		var saveSuccess = function(response, options) {
			var __data = eval('(' + response.responseText + ')');
			if(__data.success) { // saving success
				if(__action == 'save'){
					// add create date to object
					__object.date = (new Date()).getTime();
					__object.displayname = __object.lname + ' ' + __object.fname;
					__object.id = __object.email;
					__store.addData(__object) ;
					// update account variable
					panel.__owner.account = __object.email;
					panel.handleAfterSave();
				} else {
					// update data
					var __oldRecord = __store.getById(__object.email) ;
					if(__oldRecord != null && __oldRecord != undefined) {
						__oldRecord.data.displayname = __object.lname + ' ' + __object.fname;
						__oldRecord.data.smtp = __object.smtp;
						__oldRecord.data.capacity = __object.capacity;
						__oldRecord.data.active = __object.active;
						__oldRecord.data.forward = __object.forward;
						__oldRecord.data.virus = __object.virus;
						__oldRecord.data.spam = __object.spam;
						__oldRecord.commit();
					}	
				}	
				Ext.MessageBox.show({
								title : iwebos.message.mail.saving_mail_account,
								msg : iwebos.message.mail.save_data_sucessful,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
			} else {
				Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_create_account,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
			}
		};
		
		var __params = {iwct:'mailAccountContent', iwa:'READ_WRITE'} ;
		__params['action'] = __action;
		__params['object'] = Ext.util.JSON.encode(__object);
			
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			method: 'POST',
			success: saveSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap
		});	
	},
	
	
    /**
     * Right click
     * @param {Object} grid
     * @param {Object} index
     * @param {Object} e
     */
    _onRowContextMenu: function(grid, index, e){
    	// stop this event.
		e.stopEvent() ;
		// select index       
		grid.getSelectionModel().selectRow(index);
		
		var __owner = this.__owner ||{};
		var __edit = __owner.edit ;		
		// check edit permission on account
		if(!__edit){ 
			return;
		}
		
	    // get first selected
		var selection = grid.getSelectionModel().getSelections()[0].data;
		if(selection.isMail) {
			this.createEmailMenu();
			this.mailMenu.on('hide', this.onContextHide, this);
			
			this.enableMailMenu(this.mailMenu,selection);
			e.stopEvent();				
        	this.mailMenu.showAt(e.getXY());
		} else {
			if(!this.aliasMenu) {
				this.createAliasMenu();
				this.aliasMenu.on('hide', this.onContextHide, this);
			}
			this.enableAliasMenu(this.aliasMenu, selection);
			e.stopEvent();				
        	this.aliasMenu.showAt(e.getXY());
		}
    },
    
    _onContextMenu: function(event){
    	// does not allow any control to handle this menu.
		event.stopEvent() ;
		var __owner = this.__owner ||{};
		var __edit = __owner.edit ;
		if(__edit){
			this.mailMenu = new Ext.menu.Menu({
            items: [{
					id: 'mail-menu-create-email',
					text:iwebos.message.mail.create_email_account,
	                iconCls: 'icon-ismail-true',
	                scope: this,
	                handler:this._newAccountEmail.createDelegate(this)
              		}, {
				  	id: 'mail-menu-create-alias',
				  	text:iwebos.message.mail.create_alias_account,
	                iconCls: 'icon-ismail-false',
	                scope: this,
	                handler: this._newAlias
              		}]
        	 });
			// show menu.
			this.mailMenu.showAt(event.getXY());
		}		
    },
	
	/**
	 * set text for menu depend on email account
	 * 
	 * @param {Object} email
	 */
	enableMailMenu: function(mailMenu, email) {
		mailMenu.items.get('mailActive').setText(email.active==true?iwebos.message.mail.inactive_account : iwebos.message.mail.active_account);
		mailMenu.items.get('mailActive').setIconClass(email.active==true?'icon-email-false':'icon-email-true');
		
		mailMenu.items.get('mailSMTP').setText(email.smtp==true?iwebos.message.mail.autheticate_smtp_no : iwebos.message.mail.autheticate_smtp_yes);
		mailMenu.items.get('mailSMTP').setIconClass(email.smtp==true?'icon-email-false':'icon-email-true');
		
		mailMenu.items.get('mailForward').setText(email.forward==true?iwebos.message.mail.forward_no : iwebos.message.mail.forward_yes);
		mailMenu.items.get('mailForward').setIconClass(email.forward==true?'icon-email-false':'icon-email-true');
		
		mailMenu.items.get('mailVirus').setText(email.virus==true?iwebos.message.mail.uncheck_virus : iwebos.message.mail.check_virus);
		mailMenu.items.get('mailVirus').setIconClass(email.virus==true?'icon-email-false':'icon-email-true');
		
		mailMenu.items.get('mailSPAM').setText(email.spam==true?iwebos.message.mail.uncheck_spam : iwebos.message.mail.check_spam);
		mailMenu.items.get('mailSPAM').setIconClass(email.spam==true?'icon-email-false':'icon-email-true');
	},
	
	/**
	 * set text for menu depend on alias account
	 * 
	 * @param {Object} alias
	 */
	enableAliasMenu: function(aliasMenu, alias) {
		aliasMenu.items.get('aliasActive').setText(alias.active==true?iwebos.message.mail.inactive_account : iwebos.message.mail.active_account);
		aliasMenu.items.get('aliasActive').setIconClass(alias.active==true?'icon-email-false':'icon-email-true');
		
		aliasMenu.items.get('aliasSMTP').setText(alias.smtp==true?iwebos.message.mail.autheticate_smtp_no : iwebos.message.mail.autheticate_smtp_yes);
		aliasMenu.items.get('aliasSMTP').setIconClass(alias.smtp==true?'icon-email-false':'icon-email-true');
		
		aliasMenu.items.get('aliasVirus').setText(alias.virus==true?iwebos.message.mail.uncheck_virus : iwebos.message.mail.check_virus);
		aliasMenu.items.get('aliasVirus').setIconClass(alias.virus==true?'icon-email-false':'icon-email-true');
		
		aliasMenu.items.get('aliasSpam').setText(alias.spam==true?iwebos.message.mail.uncheck_spam : iwebos.message.mail.check_spam);
		aliasMenu.items.get('aliasSpam').setIconClass(alias.spam==true?'icon-email-false':'icon-email-true');
	},
	
	/**
	 * create manu for email account
	 */
	createEmailMenu: function() {
		this.mailMenu = new Ext.menu.Menu({
            items: [{
				id: 'mailCreateNewAccount',
				text:iwebos.message.mail.create_email_account,
                iconCls: 'icon-ismail-true',
                scope: this,
                handler:this._newAccountEmail
              },{
				id: 'mailCreateNewAlias',
				text:iwebos.message.mail.create_alias_account,
	            iconCls: 'icon-ismail-false',
	            scope: this,
	            handler: this._newAlias
              },{
			  	id: 'mailDel',
				text:iwebos.message.mail.delete_account,
                iconCls: 'icon-mail-del',
                scope: this,
                handler: this.onDeleteMail
              },{
			  	id: 'mailUpdate',
				text:iwebos.message.mail.update_account,
                iconCls: 'icon-mail-update',
                scope: this,
                handler: this.onUpdate
              },'-',{
				id: 'mailActive',
                iconCls: 'icon-email-active_mail',
                scope: this,
                handler: this._onActiveMail
              }, {
			  	id: 'mailSMTP',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onSmtpMail
              }, {
			  	id: 'mailForward',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onForwardMail
              }, {
			  	id: 'mailVirus',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onVirusMail
              }, {
			  	id: 'mailSPAM',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onSpamMail
              }]
         });
	},
	
	/**
	 * create menu for alias account
	 */
	createAliasMenu: function() {
		this.aliasMenu = new Ext.menu.Menu({
            items: [{
				id: 'mailCreateNewAccount',
				text:iwebos.message.mail.create_email_account,
                iconCls: 'icon-ismail-true',
                scope: this,
                handler:this._newAccountEmail
              },{
				id: 'mailCreateNewAlias',
				text:iwebos.message.mail.create_alias_account,
                iconCls: 'icon-ismail-false',
                scope: this,
                handler:this._newAlias
              },{
				id: 'aliasDel',
				text:iwebos.message.mail.delete_alias,
                iconCls: 'icon-mail-del',
                scope: this,
                handler: this.onDeleteAlias
              },{
				id: 'aliasUpdate',
				text:iwebos.message.mail.update_account,
                iconCls: 'icon-mail-update',
                scope: this,
                handler: this.onUpdate
              },'-',{
				id: 'aliasActive',
                scope: this,
                handler: this._onActiveAlias
              },{
				id: 'aliasSMTP',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onSmtpAlias
              },{
				id: 'aliasVirus',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onVirusAlias
              },{
				id: 'aliasSpam',
                iconCls: 'icon-email-delete_mail',
                scope: this,
                handler: this._onSpamAlias
            }]
         });
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
     * DoubleClick on Grid
     * @param {Object} grid
     * @param {Object} index
     * @param {Object} e
     */
    onDoubleClick: function(grid, index, e){
        e.stopEvent();
		var __selection = grid.store.getAt(index) ;
		var __data = __selection.data;
		if(__data.isMail){
			this.openEmailTab(__data);
		}else{
			this.openAliasTab(__data)
		}
    },
	
	/**
	 * Open mail account tab 
	 * 
	 * @param {Object} varselection
	 */
	openEmailTab: function(selection) {
        // get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('new-email-panel');
        var __domain = this.__owner.domain || '';
        var __action = {save: {fn:this._onSaveAccount, scope: this}};
        var __edit = this.__owner.edit || false;
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.CreateEmail();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
		__tab.loadInfo(this.__owner.domain, this.adSearch.getDepartments()
				,'',selection.email,__action,__edit);		
	},
	
	/**
	 * Open mail account tab 
	 * 
	 * @param {Object} varselection
	 */
	openAliasTab: function(selection) {
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('new-alias-main-id');
        var __domain = this.__owner.domain || '';
        var __account = selection.email;
        var __edit = this.__owner.edit || false;
        var __action = {save: {fn:this._onSaveAlias, scope: this}};
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.CreateAlias();
            __mainTab.add(__tab).show();
        }
        __mainTab.setActiveTab(__tab);
        __tab.loadInfo(__domain, '',__account,__action,__edit);
	},
    
	/**
	 * update email
	 */
	onUpdate: function() {
		var grid = Ext.getCmp("emailMessageId");
		var selections = grid.getSelectionModel().getSelections();
		var __data = selections[0].data;
		if(__data.isMail){
			this.openEmailTab(__data);
		}else{
			this.openAliasTab(__data)
		}
	},
	
	/**
	 * active/inactive account
	 */
	_onActiveMail: function() {
		this._fnHandlerUpdateMail('active','active');
	},
	
	/**
	 * 
	 */
	__onSaveForward: function(forwardAddress) {
		var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		
		var fnSuccess = function(response, options) {
			var data = eval('(' + response.responseText + ')');
			if(data.success) {
				// update attribute
				__record.data['forward'] = !__record.data['forward'];
				__record.commit();
			} else {
				Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_perform_task,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
			}	
		};
		var params = {iwct:'mailAccountContent', iwa:'READ_WRITE', action:'forward'} ;		
		var object = {};
		object['email'] = selection.email;
		object['fAddress'] = forwardAddress;
		object['forward'] = !selection['forward'];
		params['object'] = Ext.util.JSON.encode(object);
			
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			method: 'POST',
			scope: this,
			success: fnSuccess,
			failure: function(result, request){}
		});	
	},
	
	_onSmtpMail:function(){
		this._fnHandlerUpdateMail('smtpAuth','smtp');
	},
	
	_onForwardMail: function(){
		var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		
		if(selection.forward) {
			this.__onSaveForward(null);
		} else {
			// open dialog to create/update domain
			var dialog = new iNet.iwebos.ui.mail.DialogForwardAddress();		
			
			// create the action
			var action = {save:{fn:this.__onSaveForward,scope:this}};
			
			dialog.setAction(action);
			dialog.show(this);
		}
	},
	
	_onVirusMail: function(){
		this._fnHandlerUpdateMail('checkVirus','virus');
	},
	
	_onSpamMail: function(){
		this._fnHandlerUpdateMail('checkSpam','spam');
	},
	
	/**
	 * active/inactive alias
	 */
	_onActiveAlias: function() {
		this._fnHandlerUpdateAlias('active','active');
	},
	
	_onSmtpAlias:function(){
		this._fnHandlerUpdateAlias('smtpAuth','smtp');
	},
	
	_onVirusAlias: function(){
		this._fnHandlerUpdateAlias('checkVirus','virus');
	},
	
	_onSpamAlias: function(){
		this._fnHandlerUpdateAlias('checkSpam','spam');
	},
	
	_fnHandlerUpdateMail: function(action,attribute){
		var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		var fnSuccess = function(response, options) {
			var data = eval('(' + response.responseText + ')');
			if(data.success) {
				// update attribute
				__record.data[attribute] = !__record.data[attribute];
				__record.commit();
			} else {
				Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_perform_task,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
			}	
		};
		var params = {iwct:'mailAccountContent', iwa:'READ_WRITE', action:action} ;		
		var object = {};
		object['email'] = selection.email;
		object[attribute] = !selection[attribute];
		params['object'] = Ext.util.JSON.encode(object);
			
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			method: 'POST',
			scope: this,
			success: fnSuccess,
			failure: function(result, request){}
		});	
	},
	
	_fnHandlerUpdateAlias: function(action,attribute){
		var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		var fnSuccess = function(response, options) {
			var data = eval('(' + response.responseText + ')');
			if(data.success) {
				var __indexRecord = __grid.store.indexOf(__record) ;
				// remove record
				__grid.store.remove(__record);
				// update attribute
				__record.data[attribute] = !__record.data[attribute];
				// add record
				__grid.store.insert(__indexRecord,__record);
			} else {
				Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_perform_task,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
			}	
		};
		var params = {iwct:'mailAliasContent', iwa:'READ_WRITE', action:action} ;		
		var object = {};
		object['email'] = selection.email;
		object[attribute] = !selection[attribute];
		params['object'] = Ext.util.JSON.encode(object);
			
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			method: 'POST',
			scope: this,
			success: fnSuccess,
			failure: function(result, request){}
		});	
	},
    /**
     * handle click action on action panel.
     *
     * @param {Object} event - the given event to be handle.
     * @param {Object} target - the target raised event.
     */
    doAction: function(event, target){
        var mainTab = Ext.getCmp('main-tabs');
        
        // stop event.
        event.stopEvent();
        
        // get active tab.
        var tab = mainTab.getActiveTab(), activeTabId = tab.getId() || null;
        if (activeTabId != null && activeTabId == 'main-newdocin') {
            tab.doAction(event, target);
        }
        else { // process action on main tab.    	
        	if (this.actions !== undefined && target.id != undefined && target.id != '') {
                this.actions[target.id]();
            }
        }
    },
	
    /**
     * delete mail.
     */
    onDeleteMail: function(){
        var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		if(selection != null) {
			agree = function(answer) {
				if(answer == 'yes' || answer == 'ok') {
					fnSuccess = function(response, options) {
						var result = eval('(' + response.responseText + ')') ;
						if(result.success) {
							__grid.store.remove(__record);
						} else {
							Ext.MessageBox.show({
									title : iwebos.message.mail.error,
									msg : iwebos.message.mail.error_delete_mail,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
						}
					}
					var params = {iwct:'mailAccountContent', iwa:'READ_WRITE', action:'delete'} ;
					var object = {};
					object['email'] = selection.id;
					params['object'] = Ext.util.JSON.encode(object);
					
					iNet.Ajax.request({
						url: 'jsonajax',
						params: params,
						scope: this,
						method: 'POST',
						success: fnSuccess
					});
				}
			};	
		}		
        // show confirmation.
        Ext.MessageBox.confirm( iwebos.message.mail.error, iwebos.message.mail.confirm_delete_account_mail, agree);
    },
    
    /**
     * delete mail.
     */
    onDeleteAlias: function(){
        var __grid = Ext.getCmp("emailMessageId");
		// get the selection item 
		var __record = __grid.getSelectionModel().getSelections()[0];
		var selection = __record.data;
		if(selection != null) {
			agree = function(answer) {
				if(answer == 'yes' || answer == 'ok') {
					fnSuccess = function(response, options) {
						var result = eval('(' + response.responseText + ')') ;
						if(result.success) {
							__grid.store.remove(__record);
						} else {
							Ext.MessageBox.show({
									title : iwebos.message.mail.error,
									msg : iwebos.message.mail.error_delete_alias,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
						}
					}
					var params = {iwct:'mailAliasContent', iwa:'READ_WRITE', action:'delete'} ;
					var object = {};
					object['email'] = selection.id;
					params['object'] = Ext.util.JSON.encode(object);
					
					iNet.Ajax.request({
						url: 'jsonajax',
						params: params,
						scope: this,
						method: 'POST',
						success: fnSuccess
					});
				}
			};	
		}		
        // show confirmation.
        Ext.MessageBox.confirm( iwebos.message.mail.error, iwebos.message.mail.confirm_delete_account_alias, agree);
    },
    
	/**
	 * Process when send request to server successful.
     */
	onRequestSuccess : function(response, options){
		// get result object.
		var result = Ext.util.JSON.decode(response.responseText);
		
		if(result.success){
			// get list of data.
			var removeItems = options.removeItems;
			for(var index = 0; index < removeItems.length; index++){
				// remove from grid.
				this.grid.store.remove(removeItems[index]) ;
			}
		}

		// var panel identifier.
		var panel = Ext.getCmp('pro-in-task-action-id');
		
		// handle the panel.
		if(!options.active){
			this.handlePanel(panel, false, {id:options.params['doclistid']});		
		}else{
			this.handlePanel(panel, true, {});		
		}
	},
	
	/**
	 * ( Grid this, Number rowIndex, Number columnIndex, Ext.EventObject e ) 
	 * @param {Grid} grid
	 * @param {Number} rowIndex
	 * @param {Object} columnIndex
	 * @param {Object} e
	 */
	onCellClick: function(grid, rowIndex, columnIndex, e ) {
		grid.getSelectionModel().selectRow(rowIndex);
	},
    /**
     * handle grid resize event.
     */
    onGridResize : function(panel){
    	Ext.EventManager.fireWindowResize();
    },
    
    /**
     * fill data for this tab
     */
    setData: function(domain, edit, departments){
    	this.__owner.domain = domain;
    	this.adSearch.setData(domain, departments);
    	var __role = this.__owner.role || this._domainRole;
    	if(__role == this._superRole){
    		this.__owner.edit = true;
    	}else{
    		this.__owner.edit = edit;
    	}
    	
    }
});