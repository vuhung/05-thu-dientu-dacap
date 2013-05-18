/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
 * @class FollowUpPanel
 * @extends Ext.grid.GridPanel
 * 
 * Create follow up panel controler.
 * @constructor
 * @param {Object} config - the given follow up configuration.
 */
iNet.iwebos.ui.common.control.FollowUpPanel = Ext.extend(Ext.grid.GridPanel, {
	/**
	 * @cfg {Object} operator - the given follow up operator.
	 */
	/**
	 * @cfg {String} url - the loadding url.
	 */
	/**
	 * @cfg {Object} baseParams - the given base parameter.
	 */
	/**
	 * @cfg {int} width the follow up panel width.
	 */
	width: 180,
	/**
	 * @cfg {int} minSize the follow up panel minimum size.
	 */
	minSize: 180,
	/**
	 * @cfg {bool} collapsible the follow up panel collapsible flag.
	 */
	collapsible: true,
	/**
	 * Initialized the follow up component.
	 */
	initComponent: function(){
		//~ Setting up operator ===============================================
		var __operator = this.operator || {} ;
		
		// setting up the url.
		var __url = this.url || '' ;
		
		// setting up the baseParams.
		var __params = this.baseParams || {} ;
		
		// delete parameters.
		if(this.url) delete this.url ;
		if(this.baseParams) delete this.baseParams ;
		
		// setting up loading information.
		if(this.operator) delete this.operator ;
		if(!__operator.load){
			__operator.load = {url: __url, params: __params} ;
		}
		
		// setting operator.
		this.operator = __operator ;
		
		//~ Setting up store ==================================================
		// select the first row.
		var __selectFirstRow = (this.selectFirstRow == undefined ? true : !!this.selectFirstRow) ;
		delete this.selectFirstRow ;
		this.selectFirstRow = __selectFirstRow ;
		
		// create record.
		var __record = Ext.data.Record.create([
			{name:'id', type: 'int'},
			{name: 'name', type: 'string'},
			{name: 'date', type: 'int'},
			{name: 'data', type: 'int'},
			{name: 'flag', type: 'string'}
		]) ;
		
		// create reader.
		var __reader = new Ext.data.JsonReader({totalProperty:'results', root: 'rows', id:'id'}, __record) ;
		
		// group by date.
		var __groupField = 'date' ;
		
		// create proxy.
		var __proxy = new Ext.data.HttpProxy({url: __operator.load.url, method: 'POST'}) ;
		
		// create sort information.
		var __sortInfo = {field: 'date', direction: "DESC"} ;
		
		// create store.
		var __store = new iNet.iwebos.ui.common.store.WebOSGroupingStore({
			proxy: __proxy,
			sortInfo: __sortInfo,
			groupField: __groupField,
			reader: __reader,
			baseParams: __params
		});
		
		/**
		 * canculate current value and today value.
		 * @param {Object} value - the given current value.
		 */
		var _fncalculate = function(value){
			// get today.
			var __today = new Date() ;
			var __todaytime = __today.clearTime(true).getTime() ;
			var __tomorrow = __today.add(Date.DAY,1).clearTime(true).getTime() ;
			var __week = __today.getLastDateOfWeek().clearTime(true).getTime() ;
			var __nextweek = __today.getLastDateOfNextWeek().clearTime(true).getTime() ;
			
			if(value > 0) value = new Date(value).clearTime(true).getTime();
			
			if(value === -1) return 0 ;
			if(value === 0) return 5;
			if(value === __tomorrow) return 2;
			if(value > __tomorrow && value <= __week) return 3;
			if(value > __week && value <= __nextweek) return 4;
			return 1;
		};
		
		/**
		 * override sort function.
		 */
		__store.sortData = function(field, direction){
			direction = direction | "ASC" ;
			var __st = this.fields.get(field).sortType ;
			var __fn = function(r1, r2){
				var v1 = __st(r1.data[field]), v2 = __st(r2.data[field]) ;
				if(field === 'date'){
					v1 = _fncalculate(v1); v2 = _fncalculate(v2) ;
				}
				
				return (v1 > v2 ? 1 : (v1 < v2) ? -1 : 0) ;
			};
			
			// sort.
			this.data.sort(direction, __fn) ;
			
			// sort snapshot.
			if(this.snapshot && this.snapshot != this.data){
				this.snapshot.sort(direction, __fn) ;
			}
		};
		
		// register current store.
		__store = Ext.StoreMgr.lookup(__store) ;
		
		// bind current store.
		this._bind(__store) ;
		
		//~ Setting up load mask ==============================================
		this.loadMask = {msg: idesk.webos.message.load_data} ;
		
		//~ Setting up collumn model ==========================================		
		var __fnRenderer = function(value){
			// get today.
			var __today = new Date() ;
			var __todaytime = __today.clearTime(true).getTime() ;
			var __tomorrow = __today.add(Date.DAY,1).clearTime(true).getTime() ;
			var __week = __today.getLastDateOfWeek().clearTime(true).getTime() ;
			var __nextweek = __today.getLastDateOfNextWeek().clearTime(true).getTime() ;
			
			// get current date.
			var __current = (value == undefined || value === 0) ? 0 : value ;		
			if(__current > 0) __current = new Date(__current).clearTime(true).getTime() ;
			
			// render function.
			if(__current === -1) return '<div class="task-today"></div>';
			else if(__current === 0) return '<div class="task-nodate"></div>';
			else if(__current === __tomorrow) return '<div class="task-tomorrow"></div>';
			else if(__current > __tomorrow && __current <= __week) return '<div class="task-week"></div>';
			else if(__current > __week && __current <= __nextweek) return '<div class="task-nextweek"></div>'; 
			return '<div class="task-today"></div>';
		} ;		
		// create render function.
		var __fnGroupRenderer = function(value){
			// get today.
			var __today = new Date() ;
			var __todaytime = __today.clearTime(true).getTime() ;
			var __tomorrow = __today.add(Date.DAY,1).clearTime(true).getTime() ;
			var __week = __today.getLastDateOfWeek().clearTime(true).getTime() ;
			var __nextweek = __today.getLastDateOfNextWeek().clearTime(true).getTime() ;
			
			// get current date.
			var __current = (value == undefined || value === 0) ? 0 : value ;
			if(__current > 0) __current = new Date(__current).clearTime(true).getTime() ;
			
			// render function.
			if(__current === -1) return iwebos.message.followup.important;
			else if(__current === 0) return idesk.webos.message.paperwork.weekunavailable;
			else if(__current === __tomorrow) return idesk.webos.message.paperwork.tomorrow;
			else if(__current > __tomorrow && __current <= __week) return idesk.webos.message.paperwork.weekin;
			else if(__current > __week && __current <= __nextweek) return idesk.webos.message.paperwork.weeknext; 
			return idesk.webos.message.paperwork.today;
		} ;
		
		// create date column.
		var __datecol = {
			width: 24,
			fixed:true,
			dataIndex: 'date',
			menuDisabled: true,
			header:'<div class="flag-col-hd"></div>',
			id: 'flag-col',
			renderer: __fnRenderer,
			groupRenderer: __fnGroupRenderer
		};
		
		// create name column.
		var __namecol = {
			sortable: false,
			menuDisabled: true,
			dataIndex: 'name'
		};
		var __flagcol = new iNet.iwebos.ui.mail.grid.StatusColumn({
			header:''
		});
		/**
		 * setting up follup up column model.
		 */
		var __cm = new Ext.grid.ColumnModel([
			__datecol,
			__flagcol,
			__namecol
		]) ;		
		this.cm = __cm ;
		
		//~ Setting up selection model ========================================
		this.sm = new Ext.grid.RowSelectionModel({singleSelect: false}) ;

		// ~ Setting up the view ==============================================
		// create default grouping template.
		var __groupTextTpl = '{group} ({[values.rs.length]} {[values.rs.length > 0 ? \"' + idesk.webos.message.paperwork.task + '\":"No item"]})';
				
		// setting up the grid view.
		this.view = new Ext.grid.GroupingView({
			forceFit:true,
			emptyText: idesk.webos.message.paperwork.emptyresult,
			groupTextTpl: __groupTextTpl
		}) ;
		
		//~ Setting up common information =====================================
		// call initialization data.
		iNet.iwebos.ui.common.control.FollowUpPanel.superclass.initComponent.call(this) ;	
		
		//~ Setting up event ==================================================
		this.addEvents(
			/**
			 * remove event.
			 * 
			 * @param item - the item was removed.
			 */
			'remove',
			/**
			 * update event.
			 * 
			 * @param item - the item was updated.
			 */
			'update',
			/**
			 * view event.
			 * 
			 * @param item - the given item was viewed.
			 */
			'view',
			/**
			 * add event.
			 * 
			 * @param item - the given item was added.
			 */
			'save',
			/**
			 * fire event when database ready.
			 * 
			 * @param {String} task - the given task.
			 * @param {boolean} success - the given success flag.
			 * @param {Object} data - the given database data.
			 */
			'dbevent'
		) ;	
		
		// load store.
		this.store.load();
		
		// handle context menu.
		this.on('contextmenu',function(event){event.stopEvent(true);},this,{stopEvent:true});
		this.on('rowcontextmenu', this._bindContextMenu, this, {stopEvent:true}) ;
		// handle double click menu.
		this.on('rowdblclick', this._rowDoubleClick, this, {stopEvent: true}) ;
		
		// handle dbevent.
		this.on('dbevent', this._storedb, this, {stopEvent:true}) ;
	},
	
	//~ Public method =========================================================
	/**
	 * Add flag.
	 * 
	 * @param {Object} data
	 */
	add : function(data){
		// save data to database.
		this._savedb(data);
	},
	
	/**
	 * Remove flag.
	 */
	remove : function(data){
		// get record from the given data.
		var __record = this._findByData(data) ;
		if(__record == null) return ;
		
		// remove data.
		this._remove(__record) ;
	},
	
	/**
	 * Update data.
	 *  
	 * @param {Object} data - the given data.
	 * @param {String} type - the given type.
	 */
	update : function(data, type){
		// get record from the given data.
		var __record = this._findByData(data) ;
		if(__record == null) return;
		
		// update data.
		this._update(__record, type) ;
	},

	//~ Private method ========================================================	
	/**
	 * Bind store to current store.
	 *  
	 * @param {Object} store - the given store instance.
	 */
	_bind : function(store){
		if(this.selectFirstRow){
			if(this.store){
				this.store.un('load', this._onStoreLoad, this) ;
			}
			
			if(store){
				store.on('load', this._onStoreLoad, this) ;
			}
		}
		
		// setting store.
		this.store = store;
	},
	
	/**
	 * handle event load on store.
	 */
	_onStoreLoad : function(){
		// select the first row.
		if(this.store && this.store.getCount() > 0){
			this.selModel.selectFirstRow() ;
		}
	},
	
	/**
	 * Bind the menu context.
	 * 
	 * @param {Grid} grid  - the given current grid panel.
	 * @param {int} rindex - the given current row index.
	 * @param {Event} event - the given current row event.
	 */
	_bindContextMenu : function(grid, rindex, event){
		// select the row.
		WebOSGridService.handleSelection(grid, rindex) ;
		
		// handle menu.
		if(!this.menu){
			this.menu = new Ext.menu.Menu({
				items:[{
					id: 'fu-menu-ctx-view',
					text:iwebos.message.followup.view,
					iconCls: 'icon-followup-view',
					scope: this,
					handler: this._onView
				},{
					id: 'fu-menu-ctx-remove',
					text:iwebos.message.followup.remove,
					iconCls: 'icon-followup-remove',
					scope: this,
					handler: this._onRemove
				},{
					id:'fu-menu-submnu-id',
					iconCls:'icon-followup-flags',
					text:iwebos.message.followup.flags,
					menu: {
						items: [{
							id: 'fu-menu-ctx-today',
							text: iwebos.message.followup.today,
							iconCls: 'icon-followup-today',
							scope: this,
							handler: this._onToday
						}, {
							id: 'fu-menu-ctx-tomorrow',
							text: iwebos.message.followup.tomorrow,
							iconCls: 'icon-followup-tomorrow',
							scope: this,
							handler: this._onTomorrow
						}, {
							id: 'fu-menu-ctx-week',
							text: iwebos.message.followup.week,
							iconCls: 'icon-followup-week',
							scope: this,
							handler: this._onWeek
						}, {
							id: 'fu-menu-ctx-nextweek',
							text: iwebos.message.followup.nextweek,
							iconCls: 'icon-followup-nextweek',
							scope: this,
							handler: this._onNextWeek
						}, {
							id: 'fu-menu-ctx-others',
							text: iwebos.message.followup.others,
							iconCls: 'icon-followup-others',
							scope: this,
							handler: this._onOthers
						}]
					}
				}]
			}) ;
			
			// process hide event.
			this.menu.on('hide', this._onHideContextMenu, this) ;
		}
		
		// does not allow next control to process event.
		event.stopEvent(true) ;
		
		// handle menu.
		this._handleMenu(this.menu) ;
		
		// show menu.
		this.menu.showAt(event.getXY()) ;
	},
	
	/**
	 * Handle menu enable/disable.
	 * 
	 * @param {Object} menu
	 */
	_handleMenu : function(menu){
		// get selection.
		if(this.getSelectionModel().getCount() <= 0) return;
		var __selected = this.getSelectionModel().getSelections() ;
		var __record = __selected[0] ;
		
		// get today.
		var __today = new Date() ;
		var __todaytime = __today.clearTime(true).getTime() ;
		var __tomorrow = __today.add(Date.DAY,1).clearTime(true).getTime() ;
		var __week = __today.getLastDateOfWeek().clearTime(true).getTime() ;
		var __nextweek = __today.getLastDateOfNextWeek().clearTime(true).getTime() ;
		
		// get date.
		var __date = __record.data.date ;
		if(__date > 0) __date = new Date(__date).clearTime(true).getTime();
					
		// get today time.
		var __mnuothers = (__date === 0);
		var __mnutomorrow = (__date === __tomorrow) ;
		var __mnuweek = (__date > __tomorrow && __date <= __week) ;
		var __mnunextweek = (__date > __week && __date <= __nextweek) ;
		var __mnutoday = !(__mnuothers || __mnunextweek || __mnuweek || __mnutomorrow) ;

		// get submenu.
		var __submenu = menu.items.get('fu-menu-submnu-id').menu ;
		
		// urgent task.
		menu.items.get('fu-menu-submnu-id').setDisabled(__date === -1) ;
		menu.items.get('fu-menu-ctx-remove').setDisabled(__date === -1) ;
			
		// set menu.
		__submenu.items.get('fu-menu-ctx-today').setDisabled(__mnutoday) ;
		__submenu.items.get('fu-menu-ctx-tomorrow').setDisabled(__mnutomorrow) ;
		__submenu.items.get('fu-menu-ctx-week').setDisabled(__mnuweek) ;
		__submenu.items.get('fu-menu-ctx-nextweek').setDisabled(__mnunextweek) ;
		__submenu.items.get('fu-menu-ctx-others').setDisabled(__mnuothers) ;
	},
	
	/**
	 * handle hide context menu.
	 * 
	 * @param {Menu} menu - current menu.
	 */
	_onHideContextMenu : function(menu){
		if(this.ctxRow){
			Ext.fly(this.ctxRow).removeClass('x-node-ctx') ;
			this.ctxRow = null ;
		}
	},
	
	/**
	 * handle today event.
	 */
	_onToday : function(){
		this._onUpdate(iNet.iwebos.ui.common.control.FollowUpPanel.TODAY);
	},
	
	/**
	 * handle row double click.
	 */
	_rowDoubleClick : function(grid, index, event){
		// does not allow any control process this event.
		event.stopEvent(true) ;
		
		// get record.
		var __record = grid.store.getAt(index) || {};
		
		// get record data.
		var __data = __record.data || {} ;
		
		// get view operator.
		var __operator = this.operator || {} ;
		var __view = __operator.view || {} ;
		
		// has view function.
		if(__view.fn){
			if(!__view.scope){
				__data = __view.fn(__data) ;
			}else{
				__data = __view.fn.apply(__view.scope, [__data]) ;
			}
		}
		
		// fire view event.
		this.fireEvent('view', __data || {}) ;
	},
	
	/**
	 * handle view event.
	 */
	_onView : function(){
		// get view operator.
		var __operator = this.operator || {} ;
		var __view = __operator.view || {} ;

		// get selected data.
		if(this.getSelectionModel().getCount() <= 0) return;
		var __selected = this.getSelectionModel().getSelections() ;
		var __record = __selected[0] ;
		var __data = __record.data ;
		
		// prepare data before view.
		if(__view.fn){
			if(!__view.scope){
				__data = __view.fn(__data) ;
			}else{
				__data = __view.fn.apply(__view.scope, [__data]) ;
			}			
		}
				
		// fire view event.
		this.fireEvent('view', __data || {}) ;
	},
	
	/**
	 * handle remove event.
	 */
	_onRemove : function(){
		if(this.getSelectionModel().getCount() <= 0) return;
		// get selected item.
		var __selects = this.getSelectionModel().getSelections() ;
		
		var data = [];
		for(var __index = 0; __index < __selects.length; __index ++){
			data[__index] =  __selects[__index].data.id;
		}
		// remove data.
		this._remove(data) ;
	},
	
	/**
	 * handle tomorrow event.
	 */
	_onTomorrow : function(){
		this._onUpdate(iNet.iwebos.ui.common.control.FollowUpPanel.TOMORROW) ;
	},
	
	/**
	 * handle week event.
	 */
	_onWeek : function(){
		this._onUpdate(iNet.iwebos.ui.common.control.FollowUpPanel.WEEK) ;
	},
	
	/**
	 * handle next week event.
	 */
	_onNextWeek : function(){
		this._onUpdate(iNet.iwebos.ui.common.control.FollowUpPanel.NEXT_WEEK) ;
	},
	
	/**
	 * handle others event.
	 */
	_onOthers : function(){
		this._onUpdate(iNet.iwebos.ui.common.control.FollowUpPanel.OTHERS) ;
	},	
	
	/**
	 * Remove the given record.
	 * 
	 * @param {Object} record - the given record to remove.
	 */
	_remove : function(data){
		if(this.store){
			try{	
				this._removedb(data);			
			}catch(ex){}
		} 
	},
	/**
	 * Update data from the given type.
	 * 
	 * @param {String} type - the given type.
	 */
	_onUpdate : function(type){
		// get selected item.
		if(this.getSelectionModel().getCount() <= 0) return;
		
		// get selection data.
		var __selections = this.getSelectionModel().getSelections() ;
		
						
		// update data.
		if(__selections) this._update(__selections, type) ;
	},
	
	/**
	 * Find the store record from the given flag data.
	 * 
	 * @param {Object} data - the given flag data.
	 */
	_findByData:function(data){
		if(this.store == null || this.store.getCount() == 0) return null ;
		
		// define record.
		var __record = null ;
		for(var index = 0; index < this.store.getCount(); index++){
			__record = this.store.getAt(index) ;
			if(__record.data.data === data) return __record ;
		}
		
		// return null.
		return null ;
	},
	
	/**
	 * Update record from the given type.
	 * 
	 * @param {Object} record - the given record.
	 * @param {Object} type - the given type.
	 */
	_update : function(data, type){
		// update to database.
		this._updatedb(data, type);
	},
	
	/**
	 * Get date from the given type.
	 * 
	 * @param {Object} type - the given type.
	 */
	_getDate : function(type){
		// get today.
		var __today = new Date() ;
		var __date = '' ;
		
		// get type.
		var __type = type || iNet.iwebos.ui.common.control.FollowUpPanel.TODAY ;
		
		switch(__type){
			case iNet.iwebos.ui.common.control.FollowUpPanel.TODAY:
				__date = __today.format('d/m/Y') ;
				break;
			case iNet.iwebos.ui.common.control.FollowUpPanel.TOMORROW:
				__tomorrow = __today.add(Date.DAY, 1) ;
				__date = __tomorrow.format('d/m/Y') ;
				break;
			case iNet.iwebos.ui.common.control.FollowUpPanel.WEEK:
				var __wdate = __today.getLastDateOfWeek() ;
				__date = __wdate.format('d/m/Y');
				break;
			case iNet.iwebos.ui.common.control.FollowUpPanel.NEXT_WEEK:
				var __nwdate = __today.getLastDateOfNextWeek();
				__date = __nwdate.format('d/m/Y');
				break;
			case iNet.iwebos.ui.common.control.FollowUpPanel.OTHERS:
				__date = '';
				break;
		}
		
		// return current date.
		return __date ;		
	},
	//~ Database operator ===================================================
	/**
	 * Remove data from the database.
	 * 
	 * @param {Object} data - the given data to be removed.
	 */
	_removedb : function(data){
		// get remove function.
		var __remove = this.operator.remove || {} ;
		
		// the remove function does not exist.
		if(!__remove.fn) return false;
		
		if(!__remove.scope){
			return __remove.fn(data) ;
		}else{
			return __remove.fn.apply(__remove.scope, [data]) ;
		}
	},
	
	/**
	 * Update data to database.
	 * 
	 * @param {Object} data - the given data to be updated.
	 */
	_updatedb : function(data, type){
		// get update function.
		var __update = this.operator.update || {} ;
		
		// the update function does not exist.
		if(!__update.fn) return false;
		
		// update data.
		if(!__update.scope){
			return __update.fn(data, type) ;
		}else{
			return __update.fn.apply(__update.scope, [data, type]) ;
		}
	},
	
	/**
	 * Save data to database.
	 * 
	 * @param {Object} data - the given data to be saved.
	 */
	_savedb : function(data){
		// get save function.
		var __save = this.operator.save || {};
		
		// the save function does not exist.
		if(!__save.fn) return false;
		
		// save data.
		if(!__save.scope){
			return __save.fn(data) ;
		}else{
			return __save.fn.apply(__save.scope, [data]) ;
		}		
	},
	
	/**
	 * Handle database event.
	 * 
	 * @param {Object} task - the sending task.
	 * @param {boolean} success - the given success flag.
	 * @param {Object} data - the response data.
	 */
	_storedb : function(task, success , data){
		if(!success) return;
		
		if(task === 'save'){
			// add object to store.
			if (data !== null || data !== undefined) {
				for(var __index = 0; __index < data.length; __index++){
					this.store.addData(data[__index]);
				}
			}
			
			// sort data.
			this.store.applyGrouping();			
			
			// fire add event.
			this.fireEvent('save', data || {}) ;
		}else if(task === 'update'){
			// get data.
			var __object = data || [] ;
			
			for(var __index = 0; __index < __object.length; __index++){
				// remove record from store.
				var __record = this.store.getById(__object[__index].id) ;
				if(__record){
					__record.data.date = __object[__index].date ;
					__record.data.flag = __object[__index].flag ;					
					__record.commit();
				}
				
			}
			
			// sort data.
			this.store.applyGrouping();			
			
			// fire update event.
			this.fireEvent('update', __object || {}) ;
		}else if(task === 'delete'){
			// get data.
			var __object = data || [] ;
			
			for(var __index = 0; __index < __object.length; __index++){
				// remove the current cord.
				var __record = this.store.getById(__object[__index]) ;
				if(__record)
					this.store.remove(__record) ;
			}
						
			// fire event.
			this.fireEvent('remove', __object || {}) ;			
		}
	}
}) ;

// defined data.
Ext.apply(iNet.iwebos.ui.common.control.FollowUpPanel,{
	/**
	 * today.
	 */
	TODAY:"today",
	/**
	 * tomorrow.
	 */
	TOMORROW: "tomorrow",
	/**
	 * current week.
	 */
	WEEK: "week",
	/**
	 * next week.
	 */
	NEXT_WEEK: "next-week",
	/**
	 * others.
	 */
	OTHERS: "others"	
});
