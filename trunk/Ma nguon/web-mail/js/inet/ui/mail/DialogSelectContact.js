/*
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
*/

/**
 * @class iNet.iwebos.ui.mail.DialogSelectContact
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogSelectContact
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogSelectContact = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    this.prefix = 'mail-dialog-select-contact';
    this.toButton = this.prefix + 'to';
    this.ccButton = this.prefix + 'cc';
    this.bccButton = this.prefix + 'bcc';
    this.searchCombo = this.prefix + 'search-combo';
    this.keyword = this.prefix + 'keyword';
  	this.txtWordId= this.prefix + '-txt-word-id' ;
	this.departmentId= this.prefix + '-department-id' ;
	this.searchBtnId= this.prefix + '-btn-search-id' ;
	
   	this.gridId = this.prefix + '-personal-grid-id' ;
	this.globalGridId = this.prefix + '-global-grid-id' ;
	this.personalPanelId= this.prefix + '-personal-panel-id' ;
	this.globalPanelId= this.prefix + '-global-panel-id' ;
	this.__owner={mode:'global',id: 0,load:false};
		
	this.DOMAIN_STORE = new Ext.data.JsonStore({		
		fields: [
			{name:'text', type: 'string'},
			{name:'value', type: 'string'},
			{name:'desc', type: 'string'}
		]
	});
	//
	// initialization the grid.
	//
	var sortInfo = {field: 'dname', direction: "ASC"} ;
	var defaultSort ={field: 'dname', direction: "ASC"} ;
	var baseParams = {iwct:'searchAddressBookContent', iwm:'MAIL', iwc:'READ_ONLY', iwa:'search',mode:'personal', action:'search'} ;
	var baseParamsGlobal = {iwct:'searchAddressBookContent', iwm:'MAIL', iwc:'READ_ONLY', iwa:'search',mode:'global', action:'search'} ;
	
		
	// create record.
	var __record = Ext.data.Record.create([
				{name: 'id',mapping: 'name',type:'string'},
				{name: 'name',mapping: 'name',type:'string'},
				{name: 'dname',mapping: 'dname',type:'string'},
				{name: 'mail',mapping: 'mail',type:'string'},
				{name: 'hphone',mapping: 'hphone',type:'string'},
				{name: 'mphone',mapping: 'mphone',type:'string'},
				{name: 'fax',mapping: 'fax',type:'string'},
				{name: 'pager',mapping: 'pager',type:'string'},
				{name: 'addr',mapping: 'addr',type:'string'},
				{name: 'group',mapping: 'group',type:'string'}
	]);
	var __record2 = Ext.data.Record.create([
				{name: 'id',mapping: 'id',type:'int'},
				{name: 'fname',mapping: 'fname',type:'string'},
				{name: 'lname',mapping: 'lname',type:'string'},
				{name: 'dname',mapping: 'dname',type:'string'},
				{name: 'mail',mapping: 'mail',type:'string'},
				{name: 'alias',mapping: 'alias',type:'boolean'},
				{name: 'mailDrop',mapping: 'mailDrop'}
	]);

	// create filter by function, filter by status.
	var filterFn = function(item,value){
		return false;
	};
	var reader = new Ext.data.JsonReader({totalProperty: 'results',root: 'rows', id: 'id' }, __record2);
	var readerPersonal = new Ext.data.JsonReader({totalProperty: 'results',root: 'rows', id: 'id' }, __record);
	var columnModel = new Ext.grid.ColumnModel([
		{
			fixed:true,
			width:24,
			menuDisabled:true,
			renderer:function(value){
				return '<div class="icon-contact-account"></div>' ;
			}
		},{
			dataIndex:'dname',
			menuDisabled:true,
			renderer: function(value,metadata,record){
				var __mail = record.data.mail;
				if(__mail == '' ){
					return value;
				}else{
					return value + '&lt;' + __mail +'&gt;';
				}
			}
		},{
			header:iwebos.message.mail.contact.department,
			dataIndex:'group',
			menuDisabled:true,
			hidden:true
		}
	]) ;
	var columnModel2 = new Ext.grid.ColumnModel([
		{
			dataIndex:'alias',
			fixed:true,
			width:24,
			menuDisabled:true,
			renderer:function(value){
				if(value){
					return '<div class="icon-contact-group"></div>' ;
				}else{
					return '<div class="icon-contact-account"></div>' ;
				}
			}
		},{
			dataIndex:'dname',
			menuDisabled:true,
			renderer: function(value,metadata,record){
				var __mail = record.data.mail;
				if(__mail == '' ){
					return value;
				}else{
					return value + '&lt;' + __mail +'&gt;';
				}
			}
		}
	]) ;

	// setting up row.
	var _fnRowClass = function(record){
		return '' ;
	};			

	// create global grid.
	this.globalGrid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id:this.globalGridId,
		region: 'center',
		anchor: '100%',
		autoScroll:true,
		url:'jsonajax',
		method:'POST',
		sortInfo: sortInfo,
		baseParams: baseParamsGlobal,
		reader: reader,
		cm: columnModel2,
		dsort: defaultSort,		
		filter: {data: 'all',fn: true},
		loadFirst:false,
		rowClass:_fnRowClass
	}) ;

	// create personal grid.
	this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: this.gridId,
		region: 'center',
		anchor: '100%',
		autoScroll:true,
		url:'jsonajax',
		method:'POST',
		sortInfo: sortInfo,
		baseParams: baseParams,
		reader: readerPersonal,
		groupLabel: iwebos.message.mail.contact.grid_group_label,
		groupField: 'group',
		cm: columnModel,
		dsort: defaultSort,
		loadFirst:false,
		filter: {data: 'all',fn: true},
		rowClass:_fnRowClass
	}) ;
	this.personalPanel = new Ext.Panel({
		id: this.personalPanelId,
		title: iwebos.message.mail.contact.personal,
		border: false,
		collapsed: true,
		frame:false,
		layout: 'border',
		filter:null,
		iconCls:'icon-contact-personal',
		items:[this.grid]
	});
	this.globalPanel = new Ext.Panel({
		id:this.globalPanelId,
		title: iwebos.message.mail.contact.global,
		border: false,
		frame:false,
		layout: 'border',
		iconCls:'icon-contact-global',
		items:[this.globalGrid]
	});	
	var __colModel = new Ext.grid.ColumnModel([
		{
			fixed: false,
			header:false,
			menuDisabled:true,
			width: 120,
			dataIndex: 'name'
		}
	]);
	
	var __recordTo = Ext.data.Record.create([
		{name: 'id', mapping: 'email', type: 'string'},
		{name: 'name', mapping: 'name', type: 'string'}				
	]);
  	var __left=new Ext.Panel({
		border: false,
		frame:false,
		region: 'center',
		layout: 'accordion',
		anchor: '100%',
		items: [this.personalPanel, this.globalPanel]
		
	});
	// main addressbook
	var __leftPanel=new Ext.Panel({
			region: 'west',
			border: false,
			frame:false,
			layout: 'border',
			minSize: 275,
			maxSize: 600,			
			width:350,
			split: true,
			items:[__left]
	});	
	//department
	this.cboDepartment = {
		id: this.departmentId,
		xtype: 'combo',
		width: 300,
		store: this.DOMAIN_STORE,
		valueField: 'value',
		displayField: 'text',
		mode: 'local',
		triggerAction: 'all',
		selectOnFocus: true,
		forceSelection: true,
		value:''
	};
	
	//keyword
	var __txtWord= new Ext.form.TextField({
		id: this.txtWordId,
		xtype: 'textfield',
		width: 130,
		itemCls: 'required'
	});
	this.searchButton = {
		xtype: 'button',
		id: this.searchBtnId,
		iconCls: 'icon-search',
		text: iwebos.message.mail.search,
		tooltip: iwebos.message.mail.search,
		handler: this.search,
		scope: this
	} ;
	// create paging bar.
	this.pagingBar = new Ext.PagingToolbar({
        pageSize: iNet.INET_PAGE_LIMIT,
        store: this.grid.store,
        hidden:true,
        displayInfo: false
			
	 });
	 this.pagingBar2 = new Ext.PagingToolbar({
        pageSize: iNet.INET_PAGE_LIMIT,
        store: this.globalGrid.store,
        displayInfo: false
	 });
	 
	 this.loadDomain();
	 //toolbar
	var __right='->';
	if (Ext.isGecko2) {
		__right = '-';
	}
	else {
		__right = '->';
	}
	//---------------------------------------------
    var __reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'name'
    }, __recordTo);
    
	this.__gridTo = new iNet.iwebos.ui.common.grid.WebOSGrid({
		anchor: '100%',
		height:90,
		frame:false,
		border:false,
		autoScroll: true,
		loadFirst : false,
		cm: __colModel,
		reader: __reader,
		filter: {data: 'all'}
	});

	this.__gridCc = new iNet.iwebos.ui.common.grid.WebOSGrid({
		anchor: '100%',
		height:90,
		autoScroll: true,
		loadFirst : false,
		frame:false,
		border:false,
		cm: __colModel,
		reader: __reader,
		filter: {data: 'all'}
	});

	this.__gridBcc = new iNet.iwebos.ui.common.grid.WebOSGrid({
		anchor: '100%',
		height:90,
		frame:false,
		border:false,
		autoScroll: true,
		reader: __reader,
		loadFirst : false,
		cm: __colModel,
		filter: {data: 'all'}
	});
	//when row is right clicked
    this.__gridTo.on('rowcontextmenu', this._onRowContextTo, this);
    this.__gridTo.on('contextmenu', this._onContextmenu, this);
	
	//when row is right clicked
    this.__gridCc.on('rowcontextmenu', this._onRowContextCc, this);
    this.__gridCc.on('contextmenu', this._onContextmenu, this);
	
	//when row is right clicked
    this.__gridBcc.on('rowcontextmenu', this._onRowContextBcc, this);
    this.__gridBcc.on('contextmenu', this._onContextmenu, this);

    // create preview panel.
	var __rightPanel = new Ext.form.FormPanel({
		region: 'center',
		frame: true,
		border: false,
		minSize: 300,
		anchor: '100%',
		items: [{
			frame: false,
			layout: 'column',
			anchor: '100%',
			columnWidth: 1,
			items: [{
				layout: 'form',
				width: 70,
				anchor: '100%',
				bodyStyle: 'padding: 1px;',
				frame:false,
				border:false,
				items: [{
					frame: false,
					border: false,
					bodyStyle: 'padding-top: 20px; padding-bottom: 10px;',
					items:[{
						id: this.toButton,
						xtype: 'button',
						hideLabel: true,
						text: 'To: ->',
						minWidth: 65,
						handler: this._handlerTo.createDelegate(this)
					}]					
				},{
					xtype: 'button',
					hideLabel: true,
					minWidth: 65,
					text: '<- ' + iwebos.message.mail.remove,
					handler: this._removeEmailTo.createDelegate(this)
				}]
			
			},{
				layout: 'form',
				columnWidth: 1,
				anchor: '100%',
				items: [this.__gridTo]
			}]
		}, {
			frame: false,
			layout: 'column',
			anchor: '100%',
			columnWidth: 1,
			items: [{
				layout: 'form',
				width: 70,
				anchor: '100%',
				bodyStyle: 'padding: 1px;',
				frame:false,
				border:false,
				items: [{
					frame: false,
					border: false,
					bodyStyle: 'padding-top: 20px; padding-bottom: 10px;',
					items:[{
						id: this.ccButton,
						xtype: 'button',
						hideLabel: true,
						text: 'Cc: ->',
						minWidth: 65,
						handler: this._handlerCC.createDelegate(this)
					}]					
				},{
					xtype: 'button',
					hideLabel: true,
					text: '<- ' + iwebos.message.mail.remove,
					minWidth: 65,
					handler: this._removeEmailCc.createDelegate(this)
				}]
			
			}, {
				layout: 'form',
				columnWidth: 1,
				anchor: '100%',
				items: [this.__gridCc]
			
			}]
		}, {
			frame: false,
			layout: 'column',
			anchor: '100%',
			columnWidth: 1,
			items: [{
				layout: 'form',
				width: 70,
				bodyStyle: 'padding: 1px;',
				frame:false,
				border:false,
				anchor: '100%',
				items: [{
					frame: false,
					border: false,
					bodyStyle: 'padding-top: 20px; padding-bottom: 10px;',
					items:[{
						id: this.bccButton,					
						xtype: 'button',
						hideLabel: true,
						text: 'Bcc: ->',
						minWidth: 65,
						handler: this._handlerBCC.createDelegate(this)
					}]					
				},{
					xtype: 'button',
					hideLabel: true,
					text: '<- ' +iwebos.message.mail.remove,
					minWidth: 65,
					handler: this._removeEmailBcc.createDelegate(this)
				}]
			}, {
				layout: 'form',
				columnWidth: 1,
				anchor: '100%',
				items: [this.__gridBcc]
			
			}]
		}]
	});
	    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
	this.personalPanel.on('beforeexpand', this.onClickExpand, this, {stopEvent:true}) ;
	this.globalPanel.on('beforeexpand', this.onClickExpand, this, {stopEvent:true}) ;
	
	this.personalPanel.on('collapse', this.onClickCollapse, this, {stopEvent:true}) ;
	this.globalPanel.on('collapse', this.onClickCollapse, this, {stopEvent:true}) ;
	
	this.grid.on('contextmenu', function(event){event.stopEvent(true);}, this, {stopEvent: true}) ;
	this.globalGrid.on('contextmenu', function(event){event.stopEvent(true);}, this, {stopEvent: true}) ;
	
	this.grid.store.on('beforeload',this.__fnOnStore,this);
	this.globalGrid.store.on('beforeload',this.__fnOnStore,this);
	
	// create main panel.
	this.main = new Ext.Panel({
			layout:'border',
			region: 'center',
			frame:false,
			border:false,
			tbar: [this.cboDepartment, __txtWord, this.searchButton, __right,this.pagingBar,this.pagingBar2],
			items:[__leftPanel,__rightPanel]
	});
	
	__leftPanel.on('resize',function(){Ext.EventManager.fireWindowResize();},this);
	__txtWord.on('specialkey',function(field, e) {
			if (e.getKey() == Ext.EventObject.ENTER) {
				this.search();
			}
	},this);
    iNet.iwebos.ui.mail.DialogSelectContact.superclass.constructor.call(this, {
        id: 'email-more-id',
        title: iwebos.message.mail.select_contact,
        iconCls: 'icon-email-group',
        region: 'center',
        anchor: '100%',
        width: 750,
        height: 515,
        modal: true,
        frame: true,
        resizable: true,
      	layout: 'border',
        hideMode: 'offsets',
        items: [this.main],
        buttons: [{
            text: iwebos.message.mail.ok,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogSelectContact, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    hdImgCls: 'icon-email-select-group',
    /**
     * top title.
     */
    ttitle: iwebos.message.mail.dialog_select_contact,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.setup_add_email,
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    
    /**
     * add contact to grid to
     */
    _handlerTo: function(){
    	this._addDataGrid(this.__gridTo.store);
    },
    
    /**
     * add contact to grid cc
     */
    _handlerCC: function(){
    	this._addDataGrid(this.__gridCc.store);
    },
    
    /**
     * add contact to grid bcc
     */
    _handlerBCC: function(){
    	this._addDataGrid(this.__gridBcc.store);
    },
    
    _onContextmenu : function(event){
    	event.stopEvent();
    },
    /**
     * add contact selected into the given store
     * @param {Store} store
     */
   	_addDataGrid: function(store){
		var grid=null;
		if(this.__owner.mode=='personal'){
			grid = Ext.getCmp(this.gridId);
		}else{
			grid = Ext.getCmp(this.globalGridId);
		}
		// get selection item.
		var selects = grid.getSelectionModel().getSelections();

    	if (selects.length === 0) {
			return;
		}
    	var __mail='',__dname='';var __mailDrop={};
   		for (var index = 0; index < selects.length; index++) {
			__mail=selects[index].data.mail;
			__dname=selects[index].data.dname;
			//mail drop
			/*
			if(this.__owner.mode=='global' && selects[index].data.alias){
				__mailDrop=selects[index].data.mailDrop;
			}
			*/
    		if(store.getById(__mail)){
    			var __record = store.getById(__mail);
    			if(__dname != __record.data.name){
    				var __indexRecord = store.indexOf(__record);
    				// create new record
    				var __newRecord = __record;
    				__newRecord.data.name = '"' + __dname + '" &lt;' +__mail + '&gt;';
    				// remove old record
    				store.remove(__record);
    				// add new record
    				store.insert(__indexRecord, __newRecord);
    			}
    		}else{
    			store.addData({id:__mail,name: '"' + __dname + '" &lt;' +__mail + '&gt;'});
    		}
			/*
			//add mail drop to store
			if (__mailDrop!=null && __mailDrop.length>0 && this.__owner.mode=='global' && selects[index].data.alias) {
				for (var j = 0; j < __mailDrop.length; j++) {
					__mail = __mailDrop[j].mail;
					if (store.getById(__mail)) {
						var __record = store.getById(__mail);
						if (__mail != __record.data.name) {
							var __indexRecord = store.indexOf(__record);
							// create new record
							var __newRecord = __record;
							__newRecord.data.name = __mail;
							// remove old record
							store.remove(__record);
							// add new record
							store.insert(__indexRecord, __newRecord);
						}
					}
					else {
						store.addData({
							id: __mail,
							name: __mail
						});
					}
				}
			}
			*/
    			
    	}	
	
   	},
   	
   	/**
   	 * get all email address
   	 */
   	getData: function(){
   		var __object = {};
   		var __temp = {};
   		// To
   		var __store = this.__gridTo.store;
   	   	var __to = [];	
    	if(__store.getCount() > 0){
    		for(var index = 0;index < __store.getCount(); index++){
    			__temp = __store.getAt(index).data;
    			__temp.name = __temp.name.replace('&lt;','<' );
 				__temp.name = __temp.name.replace('&gt;','>');
    			__to[index] = __temp;
    		}
    	}
    	
   		// CC
   		__store = this.__gridCc.store;
   	   	var __cc = [];	
    	if(__store.getCount() > 0){
    		for(var index = 0;index < __store.getCount(); index++){
    			__temp = __store.getAt(index).data;
    			__temp.name = __temp.name.replace('&lt;','<' );
 				__temp.name = __temp.name.replace('&gt;','>');
    			__cc[index] = __temp;
    		}
    	}
    	
   		// BCC
    	__store = this.__gridBcc.store;
   	   	var __bcc = [];	
    	if(__store.getCount() > 0){
    		for(var index = 0;index < __store.getCount(); index++){
    			__temp = __store.getAt(index).data;
    			__temp.name = __temp.name.replace('&lt;','<' );
 				__temp.name = __temp.name.replace('&gt;','>');
    			__bcc[index] = __temp;
    		}
    	}
    	// add to object
    	
    	__object['to'] = __to;
    	__object['cc'] = __cc;
    	__object['bcc'] = __bcc;
    	
    	return __object;
   	},
   	/**
   	 * fill email address from given data email
   	 * @param {Object} data 
   	 */
   	loadData: function(data){
   		this.addData2Store(this.__gridTo.store, data.to);
   		this.addData2Store(this.__gridCc.store, data.cc);
   		this.addData2Store(this.__gridBcc.store, data.bcc);
   	},
   	
   	/**
   	 * add data to store 
   	 */
   	addData2Store: function(store,emails){
   		var __temp = {};
 		for(var index= 0;index < emails.length; index++){
 			__temp = emails[index];
 			__temp.name = __temp.name.replace('<','&lt;' );
 			__temp.name = __temp.name.replace('>','&gt;' );
 			store.addData(__temp);
 		}
   	},
   	
   	_onRowContextTo : function(grid, rowIndex, event){
   		event.stopEvent();
   		// handle context menu.
		WebOSGridService.handleSelection(grid, rowIndex) ;
   		var __menu = new Ext.menu.Menu({
   			 id: 'grid-to-ctx',
   			 items: [{
                    text: iwebos.message.mail.remove,
                    iconCls: 'icon-email-folder-delete',
                    scope: this,
                    handler: this._removeEmailTo.createDelegate(this)
             }]
   		});
   		__menu.showAt(event.getXY());
   	},
   	
   	_onRowContextCc : function(grid, rowIndex, event){
   		event.stopEvent();
   		// handle context menu.
		WebOSGridService.handleSelection(grid, rowIndex) ;
   		var __menu = new Ext.menu.Menu({
   			 id: 'grid-cc-ctx',
   			 items: [{
                    text: iwebos.message.mail.remove,
                    iconCls: 'icon-email-folder-delete',
                    scope: this,
                    handler: this._removeEmailCc.createDelegate(this)
             }]
   		});
   		__menu.showAt(event.getXY());
   	},
   	
    _onRowContextBcc : function(grid, rowIndex, event){
   		event.stopEvent();
   		// handle context menu.
		WebOSGridService.handleSelection(grid, rowIndex) ;
   		var __menu = new Ext.menu.Menu({
   			 id: 'grid-bcc-ctx',
   			 items: [{
                    text: iwebos.message.mail.remove,
                    iconCls: 'icon-email-folder-delete',
                    scope: this,
                    handler: this._removeEmailBcc.createDelegate(this)
             }]
   		});
   		__menu.showAt(event.getXY());
   	},
   	
   	_removeEmailTo : function(){
		var __selected = this.__gridTo.getSelectionModel().getSelections();
		if (__selected.length === 0) {
			return;
		}
   		for (var index = 0; index < __selected.length > 0; index++) {
			 this.__gridTo.store.remove(__selected[index]);
		}
   	},
   	
   	_removeEmailCc : function(){
		var __selected = this.__gridCc.getSelectionModel().getSelections();
		if (__selected.length === 0) {
			return;
		}
   		for (var index = 0; index < __selected.length > 0; index++) {
			this.__gridCc.store.remove(__selected[index]);
		}
   	},
    _removeEmailBcc : function(){
		var __selected = this.__gridBcc.getSelectionModel().getSelections();
		if (__selected.length === 0) {
			return;
		}
   		for (var index = 0; index < __selected.length > 0; index++) {
			this.__gridBcc.store.remove(__selected[index]);
		}
   	},
	//-------------------------------------------------------------------------
   	/**
	 * Create parameter object from the given module, control, component and action.
	 */
	createBaseParams : function(mo,ct,comp,action){
		return {iwm:mo,iwct:ct,iwc:comp,iwa:action} ;
	},
	search: function(){

		if (this.__owner.mode == 'personal') {
			this.grid.store.load({params:{start:0, limit:iNet.INET_PAGE_LIMIT}});
		}else{
			this.globalGrid.store.load({params:{start:0, limit:iNet.INET_PAGE_LIMIT}});
		}
	},
	/**
	 * Load domain to combobox
	 */
	loadDomain:function(){
		var __params = this.createBaseParams('MAIL', 'searchAddressBookContent', 'READ_ONLY', 'loadDomain');
		__params['action'] = 'loadDomain';
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			scope: this,
			method: 'POST',
			success: function(result, request){
				var data = Ext.util.JSON.decode(result.responseText);
				var __domain = Ext.getCmp(this.departmentId) ;
				// remove all data 
			
				this.DOMAIN_STORE.removeAll();
				this.DOMAIN_STORE.add(new Ext.data.Record({text: iwebos.message.mail.filter_object_all, 
														   value: '',
														   desc: ''}));
				
				var __convertDomain = function(domain,desc){
					if(desc == ' '){
						return domain; 
					}else{
						return domain + ' [' + desc +']'; 
					}
				};
				
				if (data !==null) {
					if (data.length > 0) {
						var __mainTab = Ext.getCmp('main-tabs');
						var __domainDefault = __mainTab.getDomainDefault();
						var __flag = false;
						
						for (index = 0; index < data.length; index++) {
							if(data[index].domain == __domainDefault){
								__flag= true;
							}
							this.DOMAIN_STORE.add(new Ext.data.Record({text: __convertDomain(data[index].domain,data[index].desc) , 
																		value: data[index].domain,
																		desc: data[index].desc}));
						}
						this.DOMAIN_STORE.sort('value');
						__domain.store.loadData(this.DOMAIN_STORE,true);
						// set domain default
						
						if(__domainDefault != null ){
							if(__flag){
								__domain.setValue(__domainDefault);
							}else{
								__domain.setValue('');
							}
							
						}
					}
				}
								
				this.globalGrid.store.load({params:{start:0, limit:iNet.INET_PAGE_LIMIT}});
			},
			failure: function(result, request){},
			maskEl: this.bwrap
		});
	},
	/**
	 * 
	 * @param {Object} p
	 * @param {Object} a
	 */
	onClickExpand:function(p,a){
		var __cbo=Ext.getCmp(this.departmentId);
		if (p.id == this.personalPanelId) {
			this.__owner.mode = 'personal';
			__cbo.setDisabled(true);
			this.pagingBar.setVisible(true);
			this.pagingBar2.setVisible(false);
			
			// check load personal contact
			if(!this.grid.loadFirst){
				this.grid.loadFirst = true;
				this.grid.store.load({params:{start:0, limit:iNet.INET_PAGE_LIMIT}});
			}
		}else{
			this.__owner.mode = 'global';
			__cbo.setDisabled(false);
			this.pagingBar.setVisible(false);
			this.pagingBar2.setVisible(true);
		}
	},
	
	/**
	 * handler collapse event
	 * @param {Ext.Panel} panel
	 * @param {Boolean} animate
	 */
	onClickCollapse : function(panel){
		if(panel){
			var __id = (panel.id == this.personalPanelId? this.globalPanelId:this.personalPanelId); 
			var __cmp = Ext.getCmp(__id);
			if(__cmp){
				__cmp.expand(true);
			}
				
		}
	},
	
	/**
	 * beforeload event 
	 * @param {Object} store
	 * @param {Object} options
	 */
	__fnOnStore:function(store,options){
		var __txtWord=Ext.getCmp(this.txtWordId);
		var __word=__txtWord.getValue();
		var __cboGroup=Ext.getCmp(this.departmentId);
		var __group=__cboGroup.getValue();
		__group=(__group === '' || __group === null ? '' : __group);
		store.baseParams = {
			iwct: 'searchAddressBookContent',
			iwm: 'MAIL',
			iwc: 'READ_ONLY',
			iwa: 'search',
			mode: 'global',
			action: 'search',
			keyword: __word,
			group: __group,
			mode: this.__owner.mode
		};
	}
});
