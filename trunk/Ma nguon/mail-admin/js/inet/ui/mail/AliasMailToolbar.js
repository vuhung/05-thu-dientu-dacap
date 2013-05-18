/*****************************************************************
   Copyright 2009 by Duyen Tang (tttduyen@truthinet.com)

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

iNet.iwebos.ui.mail.AliasMailToolbar = Ext.extend(Ext.Panel,{
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
		this.prefix = (!this.prefix) ? 'mails-for-alias-search' : this.prefix ;
		this.domainSearchId = this.prefix + '-domainSearch';
		
		// create expand button.
		this.id = this.nextId('cmb', 1) ;
		
		var __domainParams = {iwct:'loadMailDomainContent', iwa:'view', action:'loadallsys'};
		
		// the domain store
		this._domainStore = new Ext.data.JsonStore({
	        //root: 'rows',
	        url: 'jsonajax',
	        baseParams: __domainParams,
	    	fields: [{name:'domainId', mapping:'id', type:'string'},
	    			 {name:'domainName', mapping:'disname', type:'string'},
	    			 {name:'edit', mapping:'edit', type:'boolean'}]	    			 
	    });
	    
	    // the domain search combo
	    var __domainSearch = new Ext.form.ComboBox({
	        id: this.domainSearchId,
	        fieldLabel:iwebos.message.mail.org,
	        store: this._domainStore,
	        forceSelection: true,
	        displayField: 'domainName',
			valueField: 'domainId',			
			width: 210,
	        typeAhead: true,
			readOnly:true,
	        mode: 'local',
	        triggerAction: 'all',
	        emptyText: '',
	        selectOnFocus: false
	    });	    
		
		// create the search bar.		
		var tbar = [this.toolbarSeach, ' ', __domainSearch] ;	
		
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
		iNet.iwebos.ui.mail.AliasMailToolbar.superclass.initComponent.call(this) ;
		this._domainStore.on('load', this._onLoadDomain, this, {stopEvent: true});
		this._domainStore.load();
		this._domainStore.sort('domainName');
	},
	
	/**
	 * Collects the search data and fire the search event.
	 */	
	search: function(){		
		// create search parameter.
		var params = [];
		var v = this.toolbarSeach.getRawValue();
		params.push({'key':'key', 'value':v});
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
		if(__first && this.selDomain != undefined) {
			// select selected domain
			__component.setValue(this.selDomain);
			this.search();
		}
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