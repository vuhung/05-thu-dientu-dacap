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

iNet.iwebos.ui.org.MemberGridPanel = Ext.extend(Ext.Panel, {
	initComponent : function() {
		this.prefix = (!this.prefix) ? 'member-grid' : this.prefix ;
		this.gridId = this.prefix + '-grid-id'; 
		
		var sortInfo = {
	        field: 'fullname',
	        direction: 'ASC'
	    };
		var __defaultSort = {
			field: 'fullname',
			direction: 'ASC'
		}
		
		this._owner = {};
		
		var __memberRecord = new Ext.data.Record.create([
			{name: 'id', mapping: 'uname', type: 'string'},
			{name: 'uname', mapping: 'uname', type: 'string'},
			{name: 'fullname', mapping: 'fullname', type: 'string'}
		]);
		
		var __memberReader = new Ext.data.JsonReader({
			totalProperty: 'results',
			root: 'rows',
			id: 'uname'		
		}, __memberRecord);
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		var __columnModel = new Ext.grid.ColumnModel([
			sm,
			{header: iwebos.message.org.user_account, dataIndex: 'uname', width: 0.3},
			{header: iwebos.message.org.user_full_name, dataIndex: 'fullname', width: 0.7}
		]);
		
		this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
			region: 'center',
			autoScroll: true,
			url: 'jsonajax',
			method: 'POST',
			loadFirst: false,
			reader: __memberReader,
			sortInfo: sortInfo,
			dsort: __defaultSort,
			cm: __columnModel,
			sm: sm,
	        filter: {
	            data: 'all'
	        },
			view: new Ext.grid.GridView({
				forceFit: true
			}),
			tbar:[{
				id:'btn-add-new_user',
				xtype:'button',
				text:iwebos.message.org.group_select_new_user,
				iconCls: 'icon-mail-add',
				scope:this,
				handler: this._openSearchUserDialog.createDelegate(this)
			},{
				id:'btn-delete-current_user',
				xtype:'button',
				text:iwebos.message.org.group_remove_user,
				iconCls: 'icon-mail-del',
				scope:this,
				handler: this._onRemove.createDelegate(this)
			}]
		});		
		this.layout = 'border';
		this.items = [this.grid];
		
		this.grid.on('contextmenu', this._contextMenu, this, {stopEvent:true});
    	this.grid.on('rowcontextmenu', this._rowContextMenu, this, {stopEvent:true});
    	this.grid.on('keypress', this.__handleKeyEvent, this, {stopEvent:true});
		
		iNet.iwebos.ui.org.MemberGridPanel.superclass.initComponent.call(this);
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
	 * Open search user dialog
	 */
	_openSearchUserDialog: function() {
		var __dialog = new iNet.iwebos.ui.org.SearchUserDialog();
		var __action = {fn:this._handleSelectUser, scope:this};
		__dialog.setData(this._owner.defaultOrg, __action);
		__dialog.show();
	},
	
	/**
	 * set the default org
	 * @param {} org
	 */
	setDefaultOrg: function(org) {
		this._owner.defaultOrg = org;
	},
	
	/**
	 * handle when select user
	 * 
	 * @param {} object
	 */
	_handleSelectUser: function(selectionRecords) {
		for(var __index = 0; __index < selectionRecords.length; __index++) {
			var __selection = selectionRecords[__index].data;
			var __object = {};
			__object.id = __selection.uname;
			__object.uname = __selection.uname;
			__object.fullname = __selection.fullname;
			this.grid.store.addData(__object);
		}		
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
						id: 'group-menu-add-member',
						text: iwebos.message.org.group_select_new_user,
		                iconCls: 'icon-mail-add',
		                scope: this,
		                handler:this._openSearchUserDialog.createDelegate(this)
              		},{
						id: 'group-menu-remove-member',
						text: iwebos.message.org.group_remove_user,
		                iconCls: 'icon-mail-del',
		                scope: this,
		                handler:this._onRemove.createDelegate(this)
              		}]
        	 });
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
				text:iwebos.message.org.group_select_new_user,
	            iconCls: 'icon-mail-add',
	            scope: this,
	            handler:this._openSearchUserDialog.createDelegate(this)
            	}]
        });
		// show menu.
		this.mailMenu.showAt(event.getXY());
	},
	
	/**
	 * update mail account
	 */
	_onRemove: function() {
	 	// get the selected record
	 	var __records = this.grid.getSelectionModel().getSelections();
	 	if(!__records || __records.length == 0)  {
	 		Ext.MessageBox.show({
				title: iwebos.message.org.message,
				msg: iwebos.message.org.message_group_select_user_delete_out_of_group,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
	 		return;
	 	}
	 	for(var __index = 0; __index < __records.length; __index++) {
	 		// remove the record
	 		this.grid.store.remove(__records[__index]);
	 	}
	 	
	},
	
	/**
	 * remove all item on grid
	 */
	removeAll: function() {
		this.grid.store.removeAll();
	},
	
	/**
	 * get the data on grid
	 */
	getDatas: function() {
		var __store = this.grid.store;
		if(__store.getCount() > 0) {
			var __datas = [];
			for(var __index = 0; __index < __store.getCount(); __index++) {
				__datas[__index] = __store.getAt(__index).data;
			}
			return __datas;
		}
		return null;
	},
	
	/**
	 * add list of data to group
	 * @param {} datas
	 */
	addDatas: function(datas) {
		var __store = this.grid.store;
		__store.removeAll();		
		if(datas != undefined && datas != null && datas.length > 0) {						
			var __users = [];
			for(var __index = 0; __index < datas.length; __index++) {
				var __data = datas[__index];
				__users[__index] = new Ext.data.Record({id: __data.uname, uname: __data.uname, fullname: __data.fullname});
			}
			__store.add(__users);
			__store.sort('fullname', 'ASC');
		}
	}
});