/*
   Copyright 2008 by Nguyen Thanh Vy (ntvy@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

iNet.iwebos.ui.mail.AddressBookPanel = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	//prefix
	this.prefix = (!this.prefix) ? 'iwebos-addressbook' : this.prefix ;
	this.gridId = this.prefix + '-personal-grid-id' ;
	this.globalGridId = this.prefix + '-global-grid-id' ;
	this.previewId = this.prefix + '-preview-id' ;
	this.searchBtnId= this.prefix + '-btn-search-id' ;
	this.btnAdd= this.prefix + '-btn-add-id' ;
	this.btnDelete= this.prefix + '-btn-delete-id' ;
	this.btnImport= this.prefix + '-btn-import-id' ;
	this.btnExport= this.prefix + '-btn-export-id' ;
	this.txtWordId= this.prefix + '-txt-word-id' ;
	this.departmentId= this.prefix + '-department-id' ;
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
	// create column model.
	var columnModel = new Ext.grid.ColumnModel([
		{
			fixed:true,
			width:24,
			menuDisabled:true,
			renderer:function(value){
				return '<div class="icon-contact-account"></div>' ;
			}
		},{
			dataIndex:'name',
			menuDisabled:true,
			sortable: true,
			renderer: function(value,metadata,record){
				var __mail = record.data.mail;
				if(__mail == '' ){
					return value;
				}else{
					return value + '&lt;' + __mail +'&gt;';
				}
			}
		},{
			header:iwebos.message.mail.contact.group,
			dataIndex:'group',
			menuDisabled:true,
			hidden:true
		}
	]) ;
	var columnModelGlobal = new Ext.grid.ColumnModel([
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
			sortable: true,
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
		cm: columnModelGlobal,
		dsort: defaultSort,
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
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
		loadFirst:false,
		dsort: defaultSort,
		filter: {data: 'all',fn: true},
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
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
	// process event
	this.grid.on('rowdblclick', this.onDoubleClick, this, {stopEvent:true}) ;
	this.grid.on('contextmenu', this._onGridContext, this, {stopEvent:true});
	this.grid.on('rowcontextmenu', this._onGridRowContextMenu, this,{stopEvent:true});
	
	this.globalGrid.on('contextmenu', function(event){event.stopEvent(true);}, this, {stopEvent: true}) ;
	this.globalGrid.on('rowcontextmenu', this._onGlobalGridRowContextMenu, this);
	
	this.personalPanel.on('beforeexpand', this.onClickExpand, this, {stopEvent:true}) ;
	this.globalPanel.on('beforeexpand', this.onClickExpand, this, {stopEvent:true}) ;
	
	this.personalPanel.on('collapse', this.onClickCollapse, this, {stopEvent:true}) ;
	this.globalPanel.on('collapse', this.onClickCollapse, this, {stopEvent:true}) ;

	// create preview panel.
	this.previewPanel = new Ext.Panel({
			id: this.previewId,
			region: 'center',
			frame:false,
			autoScroll:true,
			border:true		
	});
	var __left=new Ext.Panel({
		border: false,
		frame:false,
		region: 'center',
		layout: 'accordion',
		anchor: '100%',
		items: [this.personalPanel, this.globalPanel]
		
	});
	// main grid
	var __gridMain=new Ext.Panel({
			region: 'west',
			border: false,
			frame:false,
			layout: 'border',
			minSize: 250,
			maxSize: 800,
			width:400,
			split: true,
			items:[__left]
	});	

	// create main panel.
	this.main = new Ext.Panel({
			layout:'border',
			region: 'center',
			frame:false,
			border:false,
			items:[__gridMain,this.previewPanel]
	});

	//department
	this.cboDepartment = {
		id: this.departmentId,
		xtype: 'combo',
		store: this.DOMAIN_STORE,
		valueField: 'value',
		displayField: 'text',
		mode: 'local',
		triggerAction: 'all',
		width: 300,
		selectOnFocus: true,
		forceSelection: true,
		value:''
	};
	//keyword
	var __txtWord= new Ext.form.TextField({
		id: this.txtWordId,
		xtype: 'textfield',
		width: 200,
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
	            displayInfo: false,
	            hidden:true
	 });
	 this.pagingBar2 = new Ext.PagingToolbar({
	            pageSize: iNet.INET_PAGE_LIMIT,
	            store: this.globalGrid.store,
	            displayInfo: false
	 });
	 
	function newComposerWAddress(address){
		var __compPanel = Ext.getCmp('main-tabs');
		if (!__compPanel) {
			__compPanel.composeWithAddress(address);
		}
	}
	 // define a template to use for the detail view
	var itemTplMarkup = [
		'<table class="preview-contact-table" boder=0 width=100% cellspacing=1>',
			'<tr class="preview-contact-hd"><td width=48px><img class="icon-contact-infor" src="images/s.gif"/></td><td align=left>\t\t{dname}</td></tr>',
			'<tr><td align="right"><div class="icon-contact-mail" /></td><td><a style="cursor:pointer;" onclick="newComposerWAddress(\'{mail}\')">{mail}</a></td></tr>',
			'<tr class="preview-contact-head"><td colspan=2>',iwebos.message.mail.contact.group_phone,'</td></tr>',
			'<tr><td></td><td>',
				'<table  boder=0 width=100%>',
					'<tr><td width=150px>',iwebos.message.mail.contact.hphone,':</td><td align=left>{hphone}</td><td align="left">',iwebos.message.mail.contact.fax,':</td><td align=left>{fax}</td></tr>',
					'<tr><td width=150px>',iwebos.message.mail.contact.mphone,':</td><td align=left>{mphone}</td><td align="left">',iwebos.message.mail.contact.pager,':</td><td align=left><a href="http://{pager}" target="_blank">{pager}</a></td></tr>',
				'</table>',
			'</td></tr>',
			'<tr class="preview-contact-head"><td colspan=2>',iwebos.message.mail.contact.group_other,'</td></tr>',
			'<tr><td></td><td>',
				'<table  boder=0 width=100%>',
					'<tr><td width=150px>',iwebos.message.mail.contact.addr,':</td><td align=left>{addr}</td></tr>',
					'<tr><td width=150px>',iwebos.message.mail.contact.group,':</td><td align=lef>{group}</td></tr>',
				'</table>',
			'</td></tr>',
		'</table>'
	];
	var itemTpl = new Ext.XTemplate(itemTplMarkup.join(''),{
		
	});
	 // define a template to use for the detail view
	var itemTplMarkup2 = [
		'<table class="preview-contact-table" boder=0 width=100% cellspacing=1>',
			'<tr class="preview-contact-hd"><td width=48px>',
				'<tpl if="alias==true">',
					'<img class="icon-contact-office" src="images/s.gif"/>',
				'</tpl>',
				'<tpl if="alias==false">',
					'<img class="icon-contact-infor" src="images/s.gif"/>',
				'</tpl>',
			'</td><td align=left>\t\t{dname}</td></tr>',
			'<tr><td align="right"><div class="icon-contact-mail" /></td><td><a style="cursor:pointer;" onclick="newComposerWAddress(\'{mail}\')">{mail}</a></td></tr>',
			'<tpl if="alias==true">',
				'<tr class="preview-contact-head"><td colspan=2>',iwebos.message.mail.contact.mail_member,'</td></tr>',
				'<tpl for="mailDrop">',
					'<tr><td></td><td>',
					'<table boder=0 width=100%>',
						'<tr><td><a style="cursor:pointer;" onclick="newComposerWAddress(\'{mail}\')">{mail}</a></td></tr>',
					'</table>',
				'</tpl>',
			'</tpl>',
		'</table>'
	];
	var itemTpl2 = new Ext.XTemplate(itemTplMarkup2.join(''),{
		
	});
	/**
	 * When rowselect
	 */
	var detailPanel = Ext.getCmp(this.previewId);
	this.grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r) {
		//apply template
		itemTpl.overwrite(detailPanel.body, r.data);
	},this);  

	this.globalGrid.getSelectionModel().on('rowselect', function(sm, rowIdx, r) {
		//apply template
		itemTpl2.overwrite(detailPanel.body, r.data);
	},this);  

	this.grid.store.on('beforeload',this.__fnOnStore,this);
	this.globalGrid.store.on('beforeload',this.__fnOnStore,this);
	
	this.loadDomain();
	
	//toolbar
	iNet.iwebos.ui.mail.AddressBookPanel.superclass.constructor.call(this, {
		id: 'main-addressbook-panel',
		title: iwebos.message.mail.contact.title,
		iconCls: 'icon-contact-users',
		layout: 'border',
		header: false,
		closable:true,
		frame: false,
		border: false,
		renderTo: Ext.getBody(),
		tbar: [this.cboDepartment, __txtWord, this.searchButton, this.pagingBar,this.pagingBar2],
		items: [this.main]
	});
	
	this.on('activate',function(){
		Ext.EventManager.fireWindowResize();
	},this);
	
	__txtWord.on('specialkey',function(field, e) {
			if (e.getKey() == Ext.EventObject.ENTER) {
				this.search();
			}
	},this);
};

Ext.extend(iNet.iwebos.ui.mail.AddressBookPanel, Ext.Panel,{
	/**
	 * Create parameter object from the given module, control, component and action.
	 */
	createBaseParams : function(mo,ct,comp,action){
		return {iwm:mo,iwct:ct,iwc:comp,iwa:action} ;
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
			action: 'search',
			keyword: __word,
			group: __group,
			mode: this.__owner.mode
		};
	},
	/**
	 * search
	 */
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
				var __combo = Ext.getCmp(this.departmentId);
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
				if (data !== null) {
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
						__combo.store.loadData(this.DOMAIN_STORE,true);
						// set domain default
						
						if(__domainDefault != null ){
							if(__flag){
								__combo.setValue(__domainDefault);
							}else{
								__combo.setValue('');
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
     * handle double click on row.
     */
    onDoubleClick: function(grid, index, event){
		// does not process event againt.
        event.stopEvent();	
		
		// get selection item.
		var __record = grid.store.getAt(index);
		var __data = __record.data;	
		this.onUpdate(__data);
		
	},
	onClickExpand:function(p,a){
		var __cbo=Ext.getCmp(this.departmentId);
		if (p.id == this.personalPanelId) {
			this.__owner.mode = 'personal';
			__cbo.setDisabled(true);
			this.pagingBar.setVisible(true);
			this.pagingBar2.setVisible(false);
			if(!this.grid.loadFirst){
				this.grid.loadFirst = true;
				this.grid.store.load({params:{start:0, limit:iNet.INET_PAGE_LIMIT}});
			}
		}else {
			this.__owner.mode = 'global';
			__cbo.setDisabled(false);
			this.pagingBar.setVisible(false);
			this.pagingBar2.setVisible(true);
		}

	},
	
	/**
	 * handler 
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
	 * Add info
	 */
	onAdd: function(){
		// create request parameter.
		var __params = this.createBaseParams('MAIL', 'addressBookContent', 'READ_WRITE', 'insert');
		var grid = Ext.getCmp(this.gridId);

		var _okHandler = function(){
			var __data = this.getData();
			if (__data.mail ==='' || __data.name ===''  ||__data.dname==='') {
				// check valid form.
				Ext.MessageBox.show({
					title: iwebos.message.mail.announcement,
					msg: iwebos.message.mail.contact.error_valid_data,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
				return;
			}
			var __action = 'insert';
			/**
			 * handle save success.
		 	*/
			var onSaveSuccess = function(response, options){
				var __result = Ext.util.JSON.decode(response.responseText);
				var __success = (__result.success === undefined ? true : __result.success);
				if (__success) {
				// add new record.
					grid.store.addData(__data) ;
				}else {
					Ext.MessageBox.show({
						title: iwebos.message.mail.announcement,
						msg: iwebos.message.mail.contact.add_error,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
					return;
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
			// destroy the dialog.
			Ext.form.Field.prototype.msgTarget = 'side';
			this.destroy();
		};
		/**
	 * create unit dialog.
	 */
		var __dlg = new iNet.iwebos.ui.mail.ContactDialog({
			title: iwebos.message.mail.contact.dialog_title,
			ttitle: iwebos.message.mail.contact.dialog_desc,
			btitle: '',
			iconCls: 'icon-contact-users',
			okHandler: _okHandler,
			width: 500,
			height: 470,
			resizable: false,
			modal: true,
			hscope: __dlg
		});
		__dlg.show(this);
	},
	/**
	 * Update info on personal
	 */
	onUpdate: function(data){
		var grid = Ext.getCmp(this.gridId) ;
		// create request parameter.
		var __params = this.createBaseParams('MAIL', 'addressBookContent', 'READ_WRITE', 'update');	
		
		var _okHandler = function(){
			var __data = this.getData();
			if (__data.mail ==='' || __data.name ==='' ||__data.dname==='' ) {
				// check valid form.
				Ext.MessageBox.show({
					title: iwebos.message.mail.announcement,
					msg: iwebos.message.mail.contact.error_valid_data,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
				return;
			}
			var __action ='update';
			/**
			 * handle save success.
			 */
			var onSaveSuccess = function(response, options){
				var __result = Ext.util.JSON.decode(response.responseText);
				var __success = (__result.success === undefined ? true : __result.success);
				var __orecord = grid.store.getById(data.id) ;
				var __selects = grid.getSelectionModel().getSelections();
				if (__success) {
					grid.store.remove(__selects[0]) ;		
					// add new record.
					grid.store.addData(__data) ;
				}else {
					Ext.MessageBox.show({
						title: iwebos.message.mail.announcement,
						msg: iwebos.message.mail.contact.update_error,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
					return;
				}
				/*
				var __orecord = grid.store.getById(data.id) ;
				//remove  old record
				if (__orecord) {
					grid.store.remove(__orecord);
				}else {
					Ext.MessageBox.show({
						title: iwebos.message.mail.announcement,
						msg: iwebos.message.mail.contact.update_error,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
					return;
				}*/

			};
			__data['id']=data.id;
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
			// destroy the dialog.
			Ext.form.Field.prototype.msgTarget = 'side';
			this.destroy();
		};
		/**
		 * create unit dialog.
		 */
		var __dlg=new iNet.iwebos.ui.mail.ContactDialog({
			title: iwebos.message.mail.contact.dialog_title,
			ttitle: iwebos.message.mail.contact.dialog_desc,
			btitle:'',
			iconCls: 'icon-contact-users',
			okHandler: _okHandler,
			width:500,
			height:470,
			resizable:false,
			modal:true,
			editor:true,
			hscope: __dlg
		});
		__dlg.setData(data);
		__dlg.show(this);
	},
	/**
	 * store document information.
	 */
	onDelete: function(){
		var grid = Ext.getCmp(this.gridId);
		// get selection item.
		var __selects = grid.getSelectionModel().getSelections();
		var __data = __selects[0].data;
		var onSuccess = function(btn){
			if (btn === 'ok' || btn === 'yes') {
				// success.
				var handlerFn = function(response, options){
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.success) {
						// get removed item.
						var removeItems = options.removeItems ;
						grid.store.remove(removeItems[0]);
					}else {
						//show error.
						Ext.MessageBox.show({
							title: iwebos.message.mail.announcement,
							msg: iwebos.message.mail.contact.update_error,
							buttons: Ext.MessageBox.OK,
							icon: Ext.MessageBox.ERROR
						});
					}
				};
				
				// set parameters.
				__data=Ext.util.JSON.encode(__data);
				var __baseParams = {
					iwct: 'addressBookContent',
					iwm: 'MAIL',
					iwc: 'READ_WRITE',
					iwa: 'delete',
					action: 'delete',
					object:__data				
				};
				// send request to server.
				iNet.Ajax.request({
					url: 'jsonajax',
					success: handlerFn,
					failure: handlerFn,
					method: 'POST',
					params: __baseParams,
					removeItems: __selects,
					maskEl: grid.bwrap
				});
				
			}
		};
		// show confirmation.
		Ext.MessageBox.confirm(iwebos.message.paperwork.ed.msg.title, iwebos.message.mail.contact.delete_confirm, onSuccess);
	},
	/**
	 * Create grid menu.
	 */
	_fnCreateMenu: function(){
		if (!this.menu) {
			this.menu = new Ext.menu.Menu({
				id: 'grid-personal-ctx',
				items: [{
					id: 'grid-ctx-mnu-add-id',
					text: iwebos.message.mail.contact.add,
					iconCls: 'icon-contact-add',
					scope: this,
					handler: this.onAdd
				}, {
					id: 'grid-ctx-mnu-remove-id',
					text: iwebos.message.mail.contact.remove,
					iconCls: 'icon-contact-delete',
					scope: this,
					disabled: true,
					handler: this.onDelete
				}, {
					id:'grid-ctx-mnu-send-message-id',
					type: 'button',
					text: iwebos.message.mail.contact.send_message,
					iconCls: 'icon-email-new-mail',
					scope: this,
					handler: this.onSendMessage,
					disabled: true
				}]
			});
		}
		// return grid menu.
		return this.menu;
	},	
	/**
	 * Handle button and grid menu.
	 * 
	 * @param {Object} menu - the given menu to handle.
	 */
	_fnHandleMenu : function(menu, record){
		var __record = record || {} ;
		if (!__record.data) {
			menu.items.get('grid-ctx-mnu-remove-id').setDisabled(true) ;
			menu.items.get('grid-ctx-mnu-send-message-id').setDisabled(true) ;
		}else{
			menu.items.get('grid-ctx-mnu-add-id').setDisabled(false) ;
			menu.items.get('grid-ctx-mnu-remove-id').setDisabled(false) ;
			menu.items.get('grid-ctx-mnu-send-message-id').setDisabled(false) ;
		}
	},
	/**
     * Right click
     * @param {Object} grid
     * @param {Object} index
     * @param {Object} e
     */
    _onGridContext: function(event){
		// does not allow any control handle this event.
		event.stopEvent();

		// create combine menu.
		var __menu = this._fnCreateMenu() ;
					
		// handle the grid menu item.
		this._fnHandleMenu(__menu) ;
		
		// show menu.
		__menu.showAt(event.getXY());
    },
	/**
     * Handle grid row context menu.
	 * 
	 * @param {Object} grid - the given current grid instance.
	 * @param {Number} index - the given row index.
	 * @param {Object} event - the given grid event.
	 */
	_onGridRowContextMenu : function(grid, index, event){
		// stop this event.
		event.stopEvent() ;

		// handle selection.
		WebOSGridService.handleSelection(grid, index) ;
		
		// create grid menu.
		var __menu = this._fnCreateMenu() ;
		
		// handle the menu.
		this._fnHandleMenu(__menu, grid.store.getAt(index)) ;
		
		// show menu.
		__menu.showAt(event.getXY()) ;
	},

	_onGlobalGridRowContextMenu: function(grid, index, event){
		// stop this event.
		event.stopEvent() ;
		
		// select the row where user right click on.
		WebOSGridService.handleSelection(grid, index) ;
		
		if (!this.globalMenu) {
			this.globalMenu = new Ext.menu.Menu({
				items: [{
					text: iwebos.message.mail.contact.send_message,
					iconCls: 'icon-email-new-mail',
					scope: this,
					handler: this.onSendMessageGlobal
				}]
			});
		}
		
		this.globalMenu.showAt(event.getXY()); 
	},
	
	/**
	 * send message with address personal
	 */
	onSendMessage: function(){
		// get selection item.
		var __selected = this.grid.getSelectionModel().getSelected();
		this.composeWithAddress(__selected.data.mail);
	},
	
	/**
	 * send message with address global
	 */
	onSendMessageGlobal: function(){
		// get selection item.
		var __selected = this.globalGrid.getSelectionModel().getSelected();
		this.composeWithAddress(__selected.data.mail);
	},
	
	/**
	 * the given email address
	 * @param {string} address - the given email address
	 */
	composeWithAddress: function(address){
		// get current tab.
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('mail-compose');
        if (!__tab) {
            __tab = new iNet.iwebos.ui.mail.MailCompose();
            __mainTab.add(__tab).show();
            __tab.loadInfo(0,'');
        }else{
        	var __owner = __tab.__owner || {};
			
        	if(__owner.close){
        		__tab.clearData();
        		__tab.loadInfo(0,'');
        	}
		}
        __mainTab.setActiveTab(__tab);
        
		__tab.setToAddress(address);
	}
});