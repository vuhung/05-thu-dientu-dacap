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
 * @class iNet.iwebos.ui.mail.EmailToolbar
 * @extends Ext.Panel
 * 
 * Create the Toolbar, that user allow user works with mail messags
 */
iNet.iwebos.ui.mail.Toolbar = Ext.extend(Ext.Panel,{
	/**
	 * @cfg {Store} store - the given paging store.
	 */
	/**
	 * Initialization the Toolbar from the given configuration.
	 */
	initComponent: function(){
		//
		this.toolbarSeach = new Ext.app.SearchField({			
	        width: 250,
			onTrigger2Click: this.search,
			top:0
	    });

		
		// set the id prefix.
		this.prefix = (!this.prefix) ? 'iwebos-as' : this.prefix ;
		
		// create expand button.
		this.id = this.nextId('cmb', 6) ;
		
		var storeMoreSearch = new Ext.data.SimpleStore({
	        fields: ['val', 'searchAction'],
	        data: [['EMAIL', iwebos.message.mail.create.searching_mail], ['ALIAS', iwebos.message.mail.create.searching_alias]]
	    });
	    var moreSearchAction = new Ext.form.ComboBox({
	        id: 'moreSearch',
	        store: storeMoreSearch,
	        displayField: 'searchAction',
			valueField: 'val',
			value: 'EMAIL',
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        selectOnFocus: false
	    });

    
	   this.departmentStore = new Ext.data.JsonStore({
	   		root: 'rows',
	   		fields: [{name: 'depName', type: 'string'}]   			 
	    });
	    
	    var __departmentSearch = new Ext.form.ComboBox({
	        id: 'departmentSearch',
	        store: this.departmentStore,
	        displayField: 'depName',
			valueField: 'depName',			
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: false
	    });
			
		// create the search bar.		
		var tbar = [this.toolbarSeach, __departmentSearch, moreSearchAction] ;	
		
		// setting up the paging bar.
		if(this.store){
			// create paging bar.
			this.pagingBar = new Ext.PagingToolbar({
	            pageSize: iNet.INET_PAGE_LIMIT,
	            store: this.store,
		        displayInfo: false
	        });
			
			// user running on firefox 2.
			if(Ext.isGecko2){
				tbar[tbar.length] = '-' ;
			}else{
				tbar[tbar.length] = '->' ;
			}
			
			tbar[tbar.length] = this.pagingBar ;
		}
		
		// setting up tbar.
		this.tbar = tbar ;
		
		// create toolbar.
		iNet.iwebos.ui.mail.Toolbar.superclass.initComponent.call(this) ;
		
		/**
		 * Add change mode event.
		 */
		this.addEvents(			
			/**
			 * @event search
			 * 
			 * Fires when the user pressed search button.
			 * @param {Array} params - the search parameter or empty array.
			 */
			'search'
		) ;
	
		// caputure change mode event.
		this.on('changemode', this.onChangeMode, this) ;
		this.on('afterlayout', this.onAfterLayout, this) ;		
	},
	/**
	 * render the search bar.
	 */
	afterRender: function(){
		// call super class.
		iNet.iwebos.ui.mail.Toolbar.superclass.afterRender.call(this) ;

		// bind the search mode.
		// this.bind(this.mode) ;		
	},
	
	/**
	 * process after layout event.
	 */
	onAfterLayout: function(container, layout){
		//this.bind(this.mode) ;
	},	
	
	/**
	 * set visible for department field search
	 * @param {} visible
	 */
	showDepartment: function(visible) {
		Ext.getCmp('departmentSearch').setVisible(visible);
	},
	
	/**
	 * set domain for search
	 * 
	 * @param {} doman
	 */
	setData: function(domain, departments) {
		this.toolbarSeach.setData(domain);
		//this.departmentStore.data = departments;		
		this.departmentStore.removeAll();
		var __item;		
		for(var index = 0; index < departments.length; index++) {
			__item = departments[index];
			this.departmentStore.add(new Ext.data.Record({
				depName: __item.name
			}));
		}
		this.departmentStore.sort('depName', 'ASC');
		this.departmentStore.suspendEvents() ;
		this.departmentStore.clearFilter() ;
		this.departmentStore.resumeEvents() ;
		if(departments.length > 0) {			
			Ext.getCmp('departmentSearch').setValue(departments[0].name);
			// searching data
			var params = [];
			var v = this.toolbarSeach.getRawValue();
			params.push({'key':'key', 'value':v});
			var filter = Ext.getCmp('moreSearch').getValue();		
			if('ALIAS' == filter) { // search mail alias
				params.push({'key': 'iwct',	'value': 'searchMailAliasContent'});
			} else { //default is search mail account			
				params.push({'key': 'iwct','value': 'searchMailAccountContent'});
			}
			var __departmentName = Ext.getCmp('departmentSearch').getValue();
			params.push({'key':'group', 'value':__departmentName});
			
			// fire search event.
			this._fireEvent('search', params);
		}
	},
	

	/**
	 * fire event
	 * 
	 * @param {} event
	 * @param {} params
	 */
	_fireEvent: function(event, params) {
		this.fireEvent(event, params) ;
	},
		
	/**
	 * Collects the search data and fire the search event.
	 */
	search: function(){
		if(!this.canSearch()) {
			Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : iwebos.message.mail.error_select_domain_to_view,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return;
		}
		/*if(!this.toolbarSeach.canSearch()) {
			return;
		}*/
		// create search parameter.
		var params = [];
		var v = this.getRawValue();
		params.push({'key':'key', 'value':v});
		var filter = Ext.getCmp('moreSearch').getValue();		
		if('ALIAS' == filter) { // search mail alias
			params.push({'key': 'iwct',	'value': 'searchMailAliasContent'});
		} else { //default is search mail account			
			params.push({'key': 'iwct','value': 'searchMailAccountContent'});
		}
		var __departmentName = Ext.getCmp('departmentSearch').getValue();
		params.push({'key':'group', 'value':__departmentName});
		
		// fire search event.
		this.fireEvent('search', params) ;
	},
	
	/**
	 * get the selected department number
	 */
	getDepartmentName: function() {
		return Ext.getCmp('departmentSearch').getValue();
	},
	
	/**
	 * get all department of current domain
	 */
	getDepartments: function() {
		// get the number domains
		var __count = this.departmentStore.getCount();
		var __departments = [];
		if(__count > 0) {
			for(var __index = 0; __index < __count; __index++) {
				__departments[__index] = this.departmentStore.getAt(__index).data;
			}
		}
		return __departments;
	},
	
	/**
	 * Return the identifier from the given initialize identifier.
	 * 
	 * @param {String} gen - the given general identifier.
	 * @param {integer} len - the given initialize identifier.
	 */
	nextId : function(gen, len){
		var time = String(new Date().getTime()).substr(len) ;
		var s = 'abcdefghijklmnopqrstuvwxyz' ;
		for(var index = 0; index < len; index++){
			time += s.charAt(Math.floor(Math.random() * 26)) ;
		}		
		
		// get identifier.		
		return this.prefix + '-' + gen + '-' + time + '-id' ;
	},
	
	/**
	 * 
	 */	
	_deleteEmail: function(){
		var mailComposer = Ext.getCmp('main-tabs');
		mailComposer.onDelete();
	}			
});