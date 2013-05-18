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
 * @class AdvancedSearch
 * @extends Ext.Panel
 * 
 * Create the AdvancedSearch, that user allow user can expand/collapsed the search.
 * @constructor
 * @param {Object} configure - the given configuration.
 */
iNet.iwebos.ui.common.search.AdvancedSearch = Ext.extend(Ext.Panel,{
	/**
	 * @cfg {boolean} mode - the advanced mode(true) or quick search(false) mode.
	 */
	/**
	 * @cfg {String} expandIcon - the expand button icon.
	 */
	/**
	 * @cfg {String} collapseIcon - the collapse button icon.
	 */
	/**
	 * @cfg {String} prefix - the given id prefix.
	 */	
	/**
	 * @cfg {Object} quick - the given list of quick search control.
	 */
	/**
	 * @cfg {String} advanced - the given search detail component.
	 */
	/**
	 * @cfg {Store} store - the given paging store.
	 */
	/**
	 * Initialization the advanced search from the given configuration.
	 */
	initComponent: function(){
		// set search mode.
		this.mode = (!this.mode) ? false : !!this.mode ;
		
		// set the expand icon.
		this.expandIcon = (!this.expandIcon) ? 'icon-expand' : this.expandIcon ;
		
		// set collapse icon.
		this.collapseIcon = (!this.collapseIcon) ? 'icon-collapse' : this.collapseIcon ;
				
		// set the id prefix.
		this.prefix = (!this.prefix) ? 'iwebos-as' : this.prefix ;
		
		// setting the quick search toolbar.
		this.quick = this.quick || [] ;
				
		// create expand button.
		this.id = this.nextId('cmb', 6) ;
		
		// create search button bar.
		this.chModeId = this.nextId('chbtn', 6) ;
		var chMode = {
			xtype: 'button',
			id: this.chModeId,
			iconCls: (this.mode ? this.expandIcon : this.collapseIcon),
			tooltip: (!this.mode ? idesk.webos.message.paperwork.search_basic : idesk.webos.message.paperwork.search_more),
			handler: this.changeMode,
			scope: this
		} ;	
				
		// create search button.
		this.searchBtnId = this.nextId('sbtn', 6);
		this.searchButton = {
			xtype: 'button',
			id: this.searchBtnId,
			iconCls: 'icon-search',
			text: idesk.webos.message.paperwork.search_title,
			tooltip: idesk.webos.message.paperwork.search_title,
			handler: this.search,
			scope: this
		} ;
				
		// create the search bar.		
		var tbar = [chMode,'-'] ;	
		
		/**
		 * setting up quick search control.
		 */
		for(var index = 0; index < this.quick.length; index++){
			if(typeof this.quick[index].ct !== 'string'){
				// setting up identifier.
				if(!this.quick[index].ct.id){
					this.quick[index].ct['id'] = this.nextId('qs',6) ;
				}
				
				// setting the identifier.
				this.quick[index]['id'] = this.quick[index].ct['id'] ;			
			}
			// push control to tbar.
			tbar[tbar.length] = this.quick[index].ct ;
		}		
		tbar[tbar.length] = this.searchButton;
				
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
		
		// create advanced search content.		
		this.adSearchId = this.nextId('ads', 6);
		this.advanced = (!this.advanced ? new iNet.iwebos.ui.paperwork.EDSearchDetails({
															id: this.adSearchId,
															labelAlign:'right',
															labelWidth:90,
															frame:false,
															border: false,
															anchor:'100%',
															bodyStyle:'padding: 5px;'
														}) : this.advanced) ;
		
		// set search identifier.		
		if(!this.advanced.id){
			this.advanced.id = this.adSearchId;
		}else{
			this.adSearchId = this.advanced.id ;
		}

		// set item.
		this.items = this.advanced;
				
		// create search bar.
		iNet.iwebos.ui.common.search.AdvancedSearch.superclass.initComponent.call(this) ;
		
		/**
		 * Add change mode event.
		 */
		this.addEvents(
			/**
			 * @event changemode
			 * 
			 * Fires when the mode is changed.
			 * @param {boolean} previousMode - the previous mode.
			 * @param {boolean} mode - the current mode.
			 */
			'changemode',
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
		iNet.iwebos.ui.common.search.AdvancedSearch.superclass.afterRender.call(this) ;

		// bind the search mode.
		this.bind(this.mode) ;		
	},
	
	/**
	 * process after layout event.
	 */
	onAfterLayout: function(container, layout){
		this.bind(this.mode) ;
	},
	
	/**
	 * Change mode.
	 */
	changeMode : function(){
		// change the mode.
		this.mode = !this.mode ;
		
		// bind current mode.
		this.bind(this.mode) ;
		
		// fire change mode event.		
		this.fireEvent('changemode', !this.mode, this.mode) ;
	},
		
	/**
	 * Collects the search data and fire the search event.
	 */
	search: function(){
		// create search parameter.
		var params = this.getQuickContent(this.mode) ;
		
		if(this.mode){
			// push search mode.
			params.push({'key':'mode','value':'advancedSearch'}) ;
			
			// search advanced.
			var details = this.advanced.getSearchContent() ;
			
			// set empty arrays.
			if(!details) details = [] ;
			
			// search details params.
			var index = 0;
			for(index = 0; index < details.length; index++){
				params.push(details[index]) ;
			}
		}else{
			// push search mode.
			params.push({'key':'mode','value':'quickSearch'}) ;			
		}
		
		// fire search event.
		this.fireEvent('search', params) ;
	},
	
	/**
	 * handel panel event to redraw the advanced search panel.
	 * 
	 * @param {boolean} previous - the given previous search mode.
	 * @param {boolean} current - the current search mode.
	 */
	onChangeMode : function(previous, current){
		Ext.EventManager.fireWindowResize();
	},
	
	/**
	 * Handle the panel aftercollapse and afterexpand event.
	 * 
	 * @param {Object} panel Panel - the current panel event.
	 */
	handlePanelEvent: function(panel){
		// current is expand mode.
		if (this.mode) Ext.EventManager.fireWindowResize();
	},
	
	/**
	 * bind the mode.
	 * 
	 * @param {boolean} mode - the given search mode. [true - advanced mode, false - quick mode].
	 */
	bind : function(mode){
		// get change mode button.
		var chMode = Ext.getCmp(this.chModeId) ;
		// get body content.
		var bodyContent = Ext.getCmp(this.adSearchId) ;
		
		// apply tooltip.
		if(chMode){
			// get element.
			var __btn = chMode.el ;
			if (__btn) {
				var __btnEl = __btn.child(chMode.buttonSelector) || {};
				__btnEl.dom[chMode.tooltipType] = (this.mode ? idesk.webos.message.paperwork.search_basic : idesk.webos.message.paperwork.search_more);
			}
		}
		
		// quick search mode.
		if(mode === false){
			// show some items on quick search bar.
			for(var index = 0; index < this.quick.length; index++){
				if (!this.quick[index].show && !!this.quick[index].id) {
					Ext.getCmp(this.quick[index].id).show();
				}
			}
			
			// change the button icon to advanced search mode.
			chMode.setIconClass(this.expandIcon) ;
			
			// hide advanced bar.
			bodyContent.hide() ;
			
			// set content height.
			this.setHeight(0) ;			
		}else{ // advanced search mode.
			// hide some items on quick search bar.
			for(var index = 0; index < this.quick.length; index++){
				if (!this.quick[index].show && !!this.quick[index].id) {
					Ext.getCmp(this.quick[index].id).hide();
				}
			}
			
			// change the button icon to quick search mode.
			chMode.setIconClass(this.collapseIcon) ;			
						
			// show advanced search bar.
			bodyContent.show() ;			
			
			// set content height.
			this.setHeight(bodyContent.el.getComputedHeight() + this.tbar.getHeight()) ;															
		}
	},
	
	/**
	 * Return the quick content depend on the search mode.
	 * 
	 * @param {boolean} mode - the given search mode.
	 */
	getQuickContent : function(mode){
		var params = [] ;
		var cmp ;
		
		// quick search mode.
		if(mode === false){
			for(var index = 0; index < this.quick.length; index++){
				if(!!this.quick[index].id){
					cmp = Ext.getCmp(this.quick[index].id) ;
					
					// get component value.
					var __value = cmp.getValue() ;
					if(cmp.getXType() === 'datefield'){
						__value = (__value == '' || __value == null ? '' : __value.format('d/m/Y')) ;
					}
					
					params[params.length] = {'key': this.quick[index].key,'value': __value} ;
				}
			}
		}else{ // advanced mode.
			for(var index = 0; index < this.quick.length; index++){
				if(!!this.quick[index].show && !!this.quick[index].id){
					cmp = Ext.getCmp(this.quick[index].id) ;
					
					// get component value.
					var __value = cmp.getValue() ;
					if(cmp.getXType() === 'datefield'){
						__value = (__value == '' || __value == null ? '' : __value.format('d/m/Y')) ;
					}

					params[params.length] = {'key': this.quick[index].key, 'value': __value}
				}
			}
		}
		
		// return parameters.
		return params ;
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
