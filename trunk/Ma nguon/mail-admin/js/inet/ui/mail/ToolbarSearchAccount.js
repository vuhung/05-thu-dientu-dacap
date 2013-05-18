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
iNet.iwebos.ui.mail.ToolbarSearchAccount = Ext.extend(Ext.Panel,{
	/**
	 * @cfg {Store} store - the given paging store.
	 */
	/**
	 * Initialization the Toolbar from the given configuration.
	 */
	initComponent: function(){
		//
		this.toolbarSeach = new Ext.app.SearchField({
	        width: 200,
	        onTrigger2Click: this.search
	    });
		// set the id prefix.
		this.prefix = (!this.prefix) ? 'mail-search' : this.prefix ;
		
		// create expand button.
		this.id = this.nextId('cmb', 1) ;
		
		// the delete check box
		this.deleteChk = new Ext.form.Checkbox({
			id: 'toolbar-search-account-deleted-id',
			boxLabel: 'Tài khoản đã bị xoá',
			checked: false,
			disabled: true
		});
		
		// create the search bar.		
		var tbar = [this.toolbarSeach, ' ',this.deleteChk] ;	
		
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
		iNet.iwebos.ui.mail.ToolbarSearchAccount.superclass.initComponent.call(this) ;
		
	},
	
	/**
	 * Collects the search data and fire the search event.
	 */
	search: function(){
		// create search parameter.
		var params = [] ;
		var __keyword = this.getRawValue() || '';
		if(__keyword.length < this._limit) {
			Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.mail.error_input_search_key_first + this._limit +  iwebos.message.mail.error_input_search_key_second,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
			return;
		}
		params.push({'key':'deleted', 'value': Ext.getCmp('toolbar-search-account-deleted-id').getValue()});
		params.push({'key':'key', 'value':__keyword});
		
				
		// fire search event.
		this.fireEvent('search', params) ;
	},
	
	/**
	 * Set limit character to search
	 * 
	 * @param {} number - the number character
	 */
	setLimitSearchCharacter: function(number) {
		this.toolbarSeach.setLimitSearchCharacter(number);
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
	}			
});