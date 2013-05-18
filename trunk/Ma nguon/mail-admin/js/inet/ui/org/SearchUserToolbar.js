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
iNet.iwebos.ui.org.SearchUserToolbar = Ext.extend(Ext.Panel, {
	firstLoad: true,
	initComponent : function() {
		this.prefix = (!this.prefix) ? 'search-user-toolbar' : this.prefix ;
		this.organizationId = this.prefix + '-organization';
		this.groupId = this.prefix + '-group';
		
		this._owner = {};
		this._owner.defaultGroup = '';
		this._owner.defaultOrg = '';
		
		//this._first = true;
		
		this.toolbarSearch = new Ext.app.SearchField({
			width: 200,
			onTrigger2Click:this.search.createDelegate(this) 
		});
		
		// create expand button.
		this.id = this.nextId('cmb', 1) ;
		
		var __orgParam = {iwct:'loadMailDomainContent', iwa:'view', action:'loadAll'};
		// the organization store 
		this._orgStore = new Ext.data.JsonStore({
			url: 'jsonajax',
			baseParams: __orgParam,
			fields: [{name:'orgId', mapping:'id', type:'string'},
		    		 {name:'organization', mapping:'disname', type:'string'},
		    		 {name:'edit', mapping:'edit', type:'boolean'}]
		});
		
		// the organization combo
		var __orgSearch = new Ext.form.ComboBox({
			id: this.organizationId,
			store: this._orgStore,
			displayField: 'organization',
			valueField: 'orgId',
			width: 240,
			forceSelection: true,
	        //typeAhead: true,
			readOnly: false,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: true
		});
		
		// the group store
		this._groupStore = new Ext.data.JsonStore({
			fields: [{name: 'name', mapping: 'name', type: 'string'}]
		});
		
		// the group search
		var __groupSearch = new Ext.form.ComboBox({
			id: this.groupId,
			store: this._groupStore,
			displayField: 'name',
			valueField: 'name',
			width: 180,
			forceSelection: true,
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: false
		});
		
		// create the search bar.		
		var tbar = [this.toolbarSearch, __orgSearch, __groupSearch] ;	
		
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
		iNet.iwebos.ui.org.SearchUserToolbar.superclass.initComponent.call(this) ;
		if(this.firstLoad) {
			__orgSearch.on('select', this._onLoadGroup, this, {stopEvent: true});
			this._orgStore.on('load', this._onLoadDomain, this, {stopEvent: true});
			this._orgStore.load();
			this._orgStore.sort('organization');
		}
	},
	
	/**
	 * Handle after load domain
	 * 
	 * @param {} store
	 * @param {} records
	 * @param {} option
	 */
	_onLoadDomain: function(store, records, option) {
		var __component = Ext.getCmp(this.organizationId);
		// get first record
		var __first = this._orgStore.getAt(0);
		if(__first) {
			// select first record
			__component.setValue(__first.get('orgId'));
			// load group
			this._onLoadGroup(Ext.getCmp(this.organizationId));
		}
	},
	
	/**
	 * Load group of organization
	 * 
	 * @param {} combo
	 */
	_onLoadGroup: function(combo) {
		var __org = combo.getValue();
		// remove all old group
		this._groupStore.removeAll();
		if(__org == null || __org == '') return;
		
		// handle after loading group sucessfull
		var __fnSuccess = function(result, request) {
			var __data = result.responseText;
			__data = eval('(' + __data + ')');
			if(__data.list) {
				var __groups = __data.list;
				this._groupStore.loadData(__groups);
				this._groupStore.sort('name', 'ASC');
				if(__groups.length > 0) {
					// select first item
					Ext.getCmp(this.groupId).setValue(__groups[0].name);
					this.search();
				}	
			} else {
				Ext.MessageBox.show({
					title : iwebos.message.org.message,
					msg : iwebos.message.org.error_group_load_all_group_in_org,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			}	
		};
		// create parameter to load group
		var __param = {iwct:'loadGroupContent', iwa:'READ_WRITE', action:'loadall'} ;
		__param.org = __org;
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __param,
			method: 'POST',
			scope: this,
			success: __fnSuccess
		});
		
	},
	
	/**
	 * search user information
	 */
	search:function() {
		// create search parameter.
		var params = [];		 
		var v = this.toolbarSearch.getRawValue();
		params.push({'key':'key', 'value':v});
		
		var __groupComponent = Ext.getCmp(this.groupId);
		if(__groupComponent.isVisible()) {
			var __group = __groupComponent.getValue();
			params.push({'key':'group', 'value':__group});
		} else {
			params.push({'key':'group', 'value':this._owner.defaultGroup});
		}
		
		var __orgComponent = Ext.getCmp(this.organizationId);
		if(__orgComponent.isVisible()) {
			var __organization = __orgComponent.getValue();
			params.push({'key':'org', 'value':__organization});
		} else {
			params.push({'key':'org', 'value':this._owner.defaultOrg});
		}
		
		// fire search event.
		this.fireEvent('search', params) ;
	},
	
	/**
	 * get all current organizations
	 */
	getOrgs: function() {
		// get the number domains
		var __count = this._orgStore.getCount();
		var __orgs = [];
		if(__count > 0) {
			for(var __index = 0; __index < __count; __index++) {
				__orgs[__index] = this._orgStore.getAt(__index).data;
			}
		}
		return __orgs;
	},
	
	/**
	 * Return the selected organization
	 */
	getSelectedOrg: function() {
		return Ext.getCmp(this.organizationId).getValue();
	},	
	
	/**
	 * Return the selected group
	 */
	getSelectedGroup: function() {
		return Ext.getCmp(this.groupId).getValue();
	},	
	
	/**
	 * Set the default organization
	 * @param {} org
	 */
	setDefaultOrg: function(org) {
		this._owner.defaultOrg = org;
		Ext.getCmp(this.organizationId).setValue(org);
	},
	
	/**
	 * Set the default group
	 * @param {} group
	 */
	setDefaultGroup: function(group) {
		this._owner.defaultGroup = group;
		Ext.getCmp(this.groupId).setValue(group);
	},
	
	/**
	 * Hide organization combo
	 */
	hideOrganization: function() {
		Ext.getCmp(this.organizationId).hide();
	},
	
	/**
	 * Hide group combo
	 */
	hideGroup: function() {
		Ext.getCmp(this.groupId).hide();
	},
	
	nextId : function(gen, len){
		var time = String(new Date().getTime()).substr(len) ;
		var s = 'abcdefghlmnopqrstuvwxyzacsdsdsssc' ;
		for(var index = 0; index < len; index++){
			time += s.charAt(Math.floor(Math.random() * 26)) ;
		}		
		
		// get identifier.		
		return this.prefix + '-' + gen + '-' + time + '-id' ;
	}
});