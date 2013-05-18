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
 * @class iNet.iwebos.ui.mail.TreeMailbox
 * @extends Ext.TreePanel
 */
iNet.iwebos.ui.mail.TreeMailbox = function(){    
	var baseParams = {iwct:'loadMailDomainContent', iwa:'view', action:'loadAll'} ;
	this.__owner = {
		load: false
	};
	this.treeroot = new Ext.tree.AsyncTreeNode({
		text: iwebos.message.mail.create.domains,
		iconCls: 'icon-email-treeview',
		draggable: false,
		expanded: true,
		id: -1
	});
	
    // construction of this class to be started
    iNet.iwebos.ui.mail.TreeMailbox.superclass.constructor.call(this, {
        id: 'domain-id',
		region: 'center',
		border:true,
		frame:false,
        useArrows:true,
        animate:true,
        enableDD:true,
		width :'100%',
		draggable:false,
        loader: new iNet.iwebos.ui.common.tree.TreeLoaderWrapper({
			dataUrl: 'jsonajax',
            baseParams: baseParams
        }),
        root: this.treeroot
    });
    
	//Sort tree by the name of domain
	this.treesort = new Ext.tree.TreeSorter(this, {
	    folderSort: true,
	    dir: 'asc',
		property: 'text'
	});
	
    this.getSelectionModel().on({
        'beforeselect': function(sm, node){
            return node.id != "-1";
        },
        'selectionchange': this.loadmail.createDelegate(this)
    });
    this.treeroot.on('contextmenu',function(event){event.stopEvent();},this,{stopEvent:true}) ;
    this.on('contextmenu', this.onTreeContextClick, this);
	this.on('dblclick', this.onDoubleClick, this);
	this.on('load',function(node){	
		if(!node) return;
		//sort text node a-->b
		node.sort(function(a, b) {return a.text > b.text;});
		//select first child
		var __node=node.firstChild;
		if (__node) {
			__node.ensureVisible();
			__node.select();
		}
	},this);
};

Ext.extend(iNet.iwebos.ui.mail.TreeMailbox, Ext.tree.TreePanel, {
    init: function(){
        this.render();
        this.root.expand();

    },
	treeResize:function(){
		var __load=false;
		var component=Ext.getCmp('domain-id');
		__load=component.__owner.load;
		component=Ext.getCmp('action-panel');
		var __total=component.getSize().height;
		component=Ext.getCmp('mail-panel-action-id');
		var __menu=component.getSize().height;
		component=Ext.getCmp('tree-mailbox-panel');
		if(__load){
			component.setHeight(__total-__menu);
		}else{
			component.setHeight(__total-__menu-50);
			component=Ext.getCmp('domain-id');
			component.__owner.load=true;
		}
		Ext.EventManager.fireWindowResize();
		
	},
    afterRender: function(){
		iNet.iwebos.ui.mail.TreeMailbox.superclass.afterRender.call(this) ;
		this.treeResize();
		var component=Ext.getCmp('main-tabs');
		component.on('resize',this.treeResize,this);
	},
    /**
     * Handle selection on node
     * 
     * @param {} sm
     * @param {} node
     */
    loadmail: function(sm, node){
    	if (node) {			
			// handle after load department of domain
			var __fnSuccess = function(result, request) {
				var __data =  Ext.util.JSON.decode(result.responseText);			
				var __success = (__data.success == undefined) ? true : __data.success;
				//var __data = result.responseText;
				//__data = eval('(' + __data + ')');
				if(__success) {					
					node.attributes.role = __data.role;					
					node.attributes.departments = __data.list;
					this._fillDomainInfo(node, __data.list);
				} else {
					// show error message
					Ext.MessageBox.show({
						title : iwebos.message.org.message,
						msg : iwebos.message.org.error_group_load_all_group_in_org,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
				}
			};
			
			if(!node.attributes.role || node.attributes.role == undefined) {
				// load department
				var loadParams = {iwct:'loadGroupContent', iwa:'READ_WRITE', action:'loadall'} ;
				loadParams['org'] = node.id;
				iNet.Ajax.request({
					url: 'jsonajax',
					params: loadParams,
					scope: this,
					method: 'POST',
					success: __fnSuccess
					//failure: fnFailure,
				});
			} else {
				this._fillDomainInfo(node, node.attributes.departments);
			}
       	}
    },
    
    /**
     * fill the information for main tab
     * 
     * @param {} node
     * @param {} departments
     */
    _fillDomainInfo: function(node, departments) {
    	var __mainTab = Ext.getCmp('main-tabs');
        var __store = __mainTab.grid.store;
        var __params = __store.baseParams;
        __params['domain'] = node.id;
        __store.baseParams = __params;
       /* __store.load({
        	params: {
	            start: 0,
	            limit: iNet.INET_PAGE_LIMIT
            }
       });*/
	   __mainTab.setData(node.id, node.attributes.edit, departments);			
	   __mainTab.setActiveTab(0);			
    },
       
    
    /**
     * set permission of user
     * 
     *  @param {} edit: the flag to know 
     */
    setEditDomain: function(edit) {
    	this.edit = edit;
    },
    
    /**
     * menu context
     * @param {} node
     * @param {} e
     */
	onTreeContextClick: function(node,e){
		node.select();
		e.stopEvent();
		if(this.edit) {
			// create context menu on first right click
	        if (!this.menu) {
	            this.menu = new Ext.menu.Menu({
	                id: 'tree-ctx',
	                items: [
						// update domain
	                	{
	                		id: 'doman-update',
		                    text: iwebos.message.mail.update_domain,
		                    iconCls: 'icon-mail-update',
		                    scope: this,
		                    handler: this.updateDomain
	                	}, 
	                	// active/ inactive domain
	                	{
	                		id: 'domain-active-menu',
	                		text: iwebos.message.mail.active_domain,                		
	                		scope: this,
	                		handler: this.activeDomain
	                	},
	                	// delete domain
						{
							id: 'domain-delete',
		                    text: iwebos.message.mail.delete_domain,
		                    iconCls: 'icon-mail-del',
		                    scope: this,
		                    handler: this.deleteDomain
	                	},'-',
	                	// export address book with utf-8
	                	{
	                		id: 'domain-export-address-book',
		                    text: 'Xuất sổ địa chỉ',
		                    iconCls: 'icon-menu-export',
		                    scope: this,
		                    handler: this.onExportUnicode
	                	},
	                	// export address book with non utf-8
	                	{
	                		id: 'domain-export-address-book-nonutf8',
		                    text: 'Xuất sổ địa chỉ không dấu',
		                    iconCls: 'icon-menu-export-non-utf',
		                    scope: this,
		                    handler: this.onExportNonUnicode
	                	},'-',
	                	
	                	// standardized full nameonImport
	                	{
	                		id: 'domain-standardized',
		                    text: 'Chuẩn hóa tên người dùng',
		                    iconCls: 'icon-menu-standardized',
		                    scope: this,
		                    handler: this.onStandardized
	                	},
	                	// Import email
	                	{
	                		id: 'domain-import',
		                    text: 'Kết nhập tài khoản',
		                    iconCls: 'icon-menu-import',
		                    scope: this,
		                    handler: this.onImport
	                	}
	                ]
	            });
	        }
	        this.setEnableMenu(node);
//	        this.menu.showAt(e.getXY());
		} else {
			if (!this.menu) {
	            this.menu = new Ext.menu.Menu({
	                id: 'tree-ctx',
	                items: [
	                	// export address book with utf-8
	                	{
	                		id: 'domain-export-address-book',
		                    text: 'Xuất sổ địa chỉ',
		                    iconCls: 'icon-menu-export',
		                    scope: this,
		                    handler: this.onExportUnicode
	                	},
	                	// export address book with non utf-8
	                	{
	                		id: 'domain-export-address-book-nonutf8',
		                    text: 'Xuất sổ địa chỉ không dấu',
		                    iconCls: 'icon-menu-export-non-utf',
		                    scope: this,
		                    handler: this.onExportNonUnicode
	                	},'-',
	                	// standardized full nameonImport
	                	{
	                		id: 'domain-standardized',
		                    text: 'Chuẩn hóa tên người dùng',
		                    iconCls: 'icon-menu-standardized',
		                    scope: this,
		                    handler: this.onStandardized
	                	},
	                	// Import email
	                	{
	                		id: 'domain-import',
		                    text: 'Kết nhập tài khoản',
		                    iconCls: 'icon-menu-import',
		                    scope: this,
		                    handler: this.onImport
	                	}
	                ]
	            });
			}    
		}
	    this.menu.showAt(e.getXY());
	},
	
	/**
	 * set enable for domain menu
	 */
	setEnableMenu: function(domain) {
		var activeItem = this.menu.items.get('domain-active-menu'); 
		activeItem.setText(domain.attributes.active==true?iwebos.message.mail.inactive_domain : iwebos.message.mail.active_domain);
		activeItem.setIconClass(domain.attributes.active==true?'icon-email-false':'icon-email-true');
	},
	
	/**
	 * insert given node to root
	 * 
	 * @param {Object} node - the given node to insert
	 */
	insertNode: function(node) {
		this.treeroot.appendChild(node);
	},	
	
	/**
	 * saving domain
	 */
	__onSave: function(object) {
		var params = {iwct:'mailDomainContent', iwa:'READ_WRITE', action:'save'} ;
		params['object'] = Ext.util.JSON.encode(object);
		
		var action = (object.id == null)?'save':'update';		
		params['action'] = action;
		
		fnSuccess = function(response, options) {
			var result = eval('(' + response.responseText + ')') ;
			if(result.success) {
				if(!object.id) {
					// create new tree node
					var node = new Ext.tree.TreeNode({
						text: object['text'],
			           	draggable:false,
						expanded:false,
			           	id: object['text'],
						iconCls: 'icon-email-treeview-br',
						cls: 'email-treeview-text',
						leaf: true			
					});
					// put node to root
					this.insertNode(node);
				}
			} else {
				if(!object.id) {
					Ext.MessageBox.show({
							title : iwebos.message.mail.error,
							msg : iwebos.message.mail.error_create_domain,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
				} else {
					Ext.MessageBox.show({
							title : iwebos.message.mail.error,
							msg : iwebos.message.mail.error_update_domain,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
				}
			}
		};
		
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			scope: this,
			method: 'POST',
			success: fnSuccess
			//failure: fnFailure,
		});		
	},
	
	/**
	 * active/inactive domain
	 * 
	 * @param {} domain - the given domain
	 */
	activeDomain: function() {
		var node = this.getFirstSelection();
		if(node != null) {
			fnSuccess = function(response, options) {
				var result = eval('(' + response.responseText + ')') ;
				if(result.success) {
					node.attributes.active = !node.attributes.active;
				} else {
					Ext.MessageBox.show({
							title : iwebos.message.mail.error,
							msg : iwebos.message.mail.error_update_data,
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						});
				}
			};
			
			var params = {iwct:'mailDomainContent', iwa:'READ_WRITE', action:'active'} ;
			var object = {};
			object['text'] = node.id;
			object['active'] = !node.attributes.active;
			params['object'] = 	Ext.util.JSON.encode(object);		
			
			iNet.Ajax.request({
				url: 'jsonajax',
				params: params,
				scope: this,
				method: 'POST',
				success: fnSuccess
			});
		}
	},	
	
	/**
	 * get first selection
	 * 
	 * @return {}
	 */
	getFirstSelection: function() {
		return this.getSelectionModel().getSelectedNode();
	},
	
	/**
	 * delete domain
	 */
	deleteDomain: function() {
		var node = this.getFirstSelection();
		if(node != null) {
			agree = function(answer) {
				if(answer == 'yes' || answer == 'ok') {
					fnSuccess = function(response, options) {
						var result = eval('(' + response.responseText + ')') ;
						if(result.success) {
							// remove domain out of tree
							this.treeroot.removeChild(node);
							// remove account on grid
							var __mainTab = Ext.getCmp('main-tabs');
            				var __store = __mainTab.grid.store;
            				__store.removeAll();
						} else {
							Ext.MessageBox.show({
									title : iwebos.message.mail.error,
									msg : iwebos.message.mail.error_delete_domain,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
						}
					};
					var params = {iwct:'mailDomainContent', iwa:'READ_WRITE', action:'delete'} ;
					var object = {};
					object['text'] = node.id;
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
			
			// show confirm dialog
			Ext.Msg.confirm(iwebos.message.mail.create.delete_domain_title,
						iwebos.message.mail.create.warning_delele_domain, agree, this);
		}
	},
	
	/**
	 * update domain
	 */
	updateDomain: function() {
		var node = this.getFirstSelection();
		if(node != null) {
			this.openUpdateDialog(node.id)
		}
	},
	
	/**
	 * open dialog to update domain
	 */
	openUpdateDialog: function(domain) {
		// open dialog to create/update domain
		var dialog = new iNet.iwebos.ui.mail.DialogCreateDomain();		
		
		// create the action
		var action = {save:{fn:this.__onSave,scope:this}};
		
		dialog.setData(domain, action);
		dialog.show(this);
	}, 
	
	/**
	 * double click on tree
	 * 
	 * @param {Object} node - the selected node
	 * @param {Object} event - the event
	 */
	onDoubleClick: function(node, event) {
		event.stopEvent();
		if(!this.edit) return;
		
		this.openUpdateDialog(node.id);
	},
	
	/**
	 * Export address book with unicode format
	 */
	onExportUnicode: function() {
		this.onExport('unicode');
	},
	
	/**
	 * Export address book with non unicode format
	 */
	onExportNonUnicode: function() {
		this.onExport('nonunicode');
	},
	
	/**
	 * Export address book
	 * 
	 * @param {} domain
	 */
	onExport: function(encode) {
		// get the first selection
		var __node = this.getFirstSelection();
		if(__node != undefined && __node != null) {
			// get the selected domain
			var __domain =  __node.attributes.text;
		
			var __form = document.getElementById('viewdownload_') ;
			__form.action = 'exportab-download-file' ;
			__form.id.value = __domain ;
			__form.encode.value = encode ;
			__form.type.value='ORIGINAL' ;
			__form.style.value = 'view' ;
			__form.target = 'ViewDocument' ;
			__form.bean.value = 'addressBookBean' ;
			
			// submit form.
			__form.submit() ;
		}
	},
	
	onStandardized: function(){
		var __node = this.getFirstSelection();
		if(__node != undefined && __node != null) {
			var __mainTab = Ext.getCmp('main-tabs');
	        var __bwrap = __mainTab.grid.bwrap;
			this._fnStandardized(__node.id, 0, 50,__bwrap);
		}
	},
	
	/**
	 * 
	 */
	_fnStandardized: function(domain, start, limit,bwrap ){
		fnSuccess = function(response, options) {
			var __result = Ext.util.JSON.decode(response.responseText);
			if(!__result.success) {
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : 'Có lỗi xảy ra trong quá trình chuẩn hóa',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			} else {
				if(__result.total > (start + limit)){
					this._fnStandardized(domain, start + limit, limit, bwrap);
				}else{
					Ext.MessageBox.show({
						title : iwebos.message.mail.error,
						msg : 'Quá trình chuẩn hóa <strong>' + __result.total  + '</strong> tài khoản đã thành công',
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
				}
			}
		};
		
		var params = {iwct:'standardizedContent', 
					iwa:'READ_WRITE',
					action:'active',
					domain: domain,
					start: start,
					limit: limit} ;
		
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			scope: this,
			method: 'POST',
			success: fnSuccess,
			maskEl: bwrap
		});
	},
	
	/**
	 * Open Import Panel
	 */
	onImport: function(){
		// get the first selection
		var __node = this.getFirstSelection();
		if(__node != undefined && __node != null) {
			// get current tab.
	        var __mainTab = Ext.getCmp('main-tabs');
	        var __tab = Ext.getCmp('import-mail-acc-tab');
	        if (!__tab) {
	            __tab = new iNet.iwebos.ui.mail.ImportPanel(this, {org: __node.id});
	            __mainTab.add(__tab).show();
	        }
	        __mainTab.setActiveTab(__tab);
		}	
	}
});