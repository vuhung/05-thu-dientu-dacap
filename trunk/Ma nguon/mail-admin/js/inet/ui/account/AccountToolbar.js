/*****************************************************************
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
*****************************************************************/

/**
 * @class iNet.iwebos.ui.calendar.AccountToolbar
 * @extends Ext.Panel
 * 
 * Create the Toolbar, that user allow user works with mail messags
 */
iNet.iwebos.ui.account.AccountToolbar = Ext.extend(Ext.Panel,{
	/**
	 * @cfg {Store} store - the given paging store.
	 */
	/**
	 * Initialization the Toolbar from the given configuration.
	 */
	initComponent: function(){
		this._first = true;
		// The tool bar for searching
		this.toolbarSeach = new Ext.app.SearchField({
	        width: 200,
	        onTrigger2Click: this.search.createDelegate(this)
	    });
		// set the id prefix.
		this.prefix = (!this.prefix) ? 'manage-account-search' : this.prefix ;
		this.depSearchId = this.prefix + '-departmentSearch';
		this.domainSearchId = this.prefix + '-domainSearch';
		
		// create expand button.
		this.id = this.nextId('cmb', 1) ;
		
		var __domainParams = {iwct:'loadMailDomainContent', iwa:'view', action:'loadAll'};
		
		// the domain store
		this._domainStore = new Ext.data.JsonStore({
	        //root: 'rows',
	        url: 'jsonajax',
	        baseParams: __domainParams,
	    	fields: [{name:'domainId', mapping:'id', type:'string'},
	    			 {name:'domainName', mapping:'text', type:'string'},
	    			 {name:'edit', mapping:'edit', type:'boolean'}]	    			 
	    });
	    
	    // the domain search combo
	    var __domainSearch = new Ext.form.ComboBox({
	        id: this.domainSearchId,
	        fieldLabel:iwebos.message.mail.org,
	        store: this._domainStore,
	        forceSelection: true,
	        displayField: 'domainId',
			valueField: 'domainName',			
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: false
	    });	    
		
	    // the department store
		this._departmentStore = new Ext.data.JsonStore({
	        root: 'rows',
	    	fields: [{name:'depName', type:'string'},
	    			 {name:'depNumber', type:'string'}]	    			 
	    });
	    
	    // the department search combo
	    var __departmentSearch = new Ext.form.ComboBox({
	        id: this.depSearchId,
	        fieldLabel:'Dep',
	        store: this._departmentStore,
	        displayField: 'depName',
			valueField: 'depNumber',			
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: false
	    });
		
		// create the search bar.		
		var tbar = [this.toolbarSeach, __domainSearch, __departmentSearch] ;	
		
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
		iNet.iwebos.ui.account.AccountToolbar.superclass.initComponent.call(this) ;
		__domainSearch.on('select', this._onSelectDomain, this, {stopEvent: true});
		this._domainStore.on('load', this._onLoadDomain, this, {stopEvent: true});
		this._domainStore.load();
	},
	
	/**
	 * Collects the search data and fire the search event.
	 */	
	search: function(){		
		// create search parameter.
		var params = [];
		var v = this.toolbarSeach.getRawValue();
		params.push({'key':'key', 'value':v});
		//params.push({'key': 'iwct','value': 'searchMailAccountContent'});
		var __departmentNumber = Ext.getCmp(this.depSearchId).getValue();
		params.push({'key':'depNumber', 'value':__departmentNumber});
		var __domainNumber = Ext.getCmp(this.domainSearchId).getValue();
		params.push({'key':'domain', 'value':__domainNumber});
		
		// fire search event.
		this.fireEvent('search', params) ;
	},
	
	/**
	 * Handle after load domain
	 * 
	 * @param {} store
	 * @param {} records
	 * @param {} option
	 */
	_onLoadDomain: function(store, records, option) {		
		var __component = Ext.getCmp(this.domainSearchId);
		// get first record
		var __first = this._domainStore.getAt(0);
		if(__first) {
			// select first record
			__component.setValue(__first.get('domainId'));
			// load department
			this._loadDepartment(null);
		}
		
	},
	
	/**
	 * Handle even when select on domain component
	 * 
	 * @param {} combo
	 * @param {} record
	 * @param {} index
	 */
	_onSelectDomain: function(combo, record, index) {
		this._loadDepartment(combo);
	},
	
	/**
	 * load the department of domain
	 */
	_loadDepartment: function(combo) {
		var __component = combo || Ext.getCmp(this.domainSearchId);
		var __domain = __component.getValue();
		// remove all department
		this._departmentStore.removeAll();
		if(__domain == null || __domain == '') return;
		
		// handle after load department of domain
		var __fnSuccess = function(result, request) {
			var data = result.responseText;
			data = eval('(' + data + ')');
			if(data) {					
				var __departments = data.list;
				var __item;		
				for(var index = 0; index < __departments.length; index++) {
					__item = __departments[index];
					this._departmentStore.add(new Ext.data.Record({
						depNumber: __item.depNumber,
						depName: __item.depName
					}));
				}
				if(__departments.length > 0) {
					// select first department
					Ext.getCmp(this.depSearchId).setValue(__departments[0].depNumber);
					if(this._first) {
						this._first = false;
						this.search();
					}
				}
			}
		};
		
		// load department
		var loadParams = {iwct:'loadMailDomainContent', iwa:'READ_WRITE', action:'loadDeps'} ;
			loadParams['text'] = __domain;
			iNet.Ajax.request({
				url: 'jsonajax',
				params: loadParams,
				scope: this,
				method: 'POST',
				success: __fnSuccess
				//failure: fnFailure,
		});
	},
	
	/**
	 * get all current domains
	 */
	getDomains: function() {
		// get the number domains
		var __count = this._domainStore.getCount();
		var __domains = [];
		if(__count > 0) {
			for(var __index = 0; __index < __count; __index++) {
				__domains[__index] = this._domainStore.getAt(__index).data;
			}
		}
		return __domains;
	},
	
	/**
	 * get the selected domain
	 */
	getSelectedDomain: function() {
		return Ext.getCmp(this.domainSearchId).getValue();
	},
	
	/**
	 * Return the identifier from the given initialize identifier.
	 * 
	 * @param {String} gen - the given general identifier.
	 * @param {integer} len - the given initialize identifier.
	 */
	nextId : function(gen, len){
		var time = String(new Date().getTime()).substr(len) ;
		var s = 'abcdefghijklmnopqrstuvwxyzacc' ;
		for(var index = 0; index < len; index++){
			time += s.charAt(Math.floor(Math.random() * 26)) ;
		}		
		
		// get identifier.		
		return this.prefix + '-' + gen + '-' + time + '-id' ;
	}
});