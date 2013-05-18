/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

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
 * @class MailFollowUpPanel
 * @extends iNet.iwebos.ui.common.control.FollowUpPanel
 * 
 * Create MailFollowUpPanel.
 * @constructor
 * @param {Object} config - the given mail follow up panel configuration.
 */
iNet.iwebos.ui.mail.MailFollowUpPanel = function(config){	
	
	// setting up configuration.
	var __aconfig = {
		title: iwebos.message.mail.follow_up_make_work,
		url: 'jsonajax',
		baseParams:{iwct:'loadFollowUpContent', iwm:'MAIL', iwc:'READ_ONLY', iwa:'load', action:'load'},
		autoScroll: true,
		collapsible: true,
		titleCollapse: true,
		frame: false,
		border: true,
		collapsed: true,
		split: true,
		collapseFirst: true,
		width: 230,
		minSize: 230
	};	
	
	// create operator.
	this.operator = config.operator || {} ;

	// create operator.
	Ext.apply(this.operator,{save:{fn:this._fnSave,scope:this},update:{fn:this._fnUpdate, scope:this},remove:{fn:this._fnDelete,scope:this}}) ;	
	
	// append aditional configuration.
	var __config = config || {};
	Ext.apply(__config, __aconfig) ;
			
	// initialization paperwork follow up panel.
	iNet.iwebos.ui.mail.MailFollowUpPanel.superclass.constructor.call(this, __config) ;
};

// extend follow up panel.
Ext.extend(iNet.iwebos.ui.mail.MailFollowUpPanel, iNet.iwebos.ui.common.control.FollowUpPanel, {
	/**
	 * save data to database.
	 * 
	 * @param {Array} data
	 */
	save : function(data, type){
		// get data.
		var __data = data || [] ;
		
		// get date from the given type.
		var __date = this._getDate(type) ;
		// filter to save or update
		var __save = [] ; 
		var __update = [];
		for(var __index = 0; __index < data.length; __index++){
			// find data in current store.
			var __item = __data[__index];
			var __record = this._findByData(__item.data) ;
			// the data does not exist. save to database.
			if(__record == null){
				// get type.
				__item.date = __date ;
				__item.fdate = iNet.INET_DATE_FORMAT;
				__item.type = this.type ;
				
				__save[__save.length] = __item;
			}else{ // the data is existing.
				var __name = __item.name || __record.data.name;
				__item = __record.data ;
				__item.date = __date ;
				__item.name = __name ;
				__item.fdate = iNet.INET_DATE_FORMAT;
				
				__update[__update.length] =  __item;
			}
		}
		
		// save to database.
		if(__save.length > 0)
			this._savedb(__save) ;
		
		// update to db.
		if(__update.length > 0)
			this._updatedb(__update, type) ;
	},
	
	/**
	 * Process ajax response.
	 * 
	 * @param {Object} response - the ajax response.
	 * @param {Object} options - the request options.
	 */
	_fnRequestSuccess : function(response, options){
		// get object.
		var __object = null ;
		try{
			__object = Ext.util.JSON.decode(response.responseText) ;
		}catch(ex){}
		
		// get data.
		var __success = (__object == null || __object.success == undefined) ? true : __object.success ;
		
		// process object.
		if(__success){
			if(options.params.action === 'delete') __object = Ext.util.JSON.decode(options.params.data) ;
			// database ready.
			this.fireEvent('dbevent', options.params.action, __success, __object) ;			
		}else{
			var __msg = iwebos.message.followup.none_saved;
			if(options.params.action === 'update'){
				__msg = iwebos.message.followup.none_updated ;
			}else if(options.params.action === 'remove'){
				__msg = iwebos.message.followup.none_deleted;
			}
		
			// database ready.
			this.fireEvent('dbevent', options.params.action, false, Ext.util.JSON.decode(options.params.data)) ;			
	
			// show error.
			Ext.MessageBox.show({title:iwebos.message.paperwork.ed.error,msg:__msg,buttons:Ext.MessageBox.OK,icon:Ext.MessageBox.ERROR}) ;			
		}		
	},
	
	/**
	 * Save data to database.
	 * 
	 * @param {Object} data - the given data to save.
	 */
	_fnSave : function(data){
				
		// create ajax request.
		var __params = {iwct:'followUpContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'save'};
		__params['action'] = 'save' ;
		
		// add data.
		__params['object'] = Ext.util.JSON.encode(data) ;
		
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			success: this._fnRequestSuccess,
			failure: function(response, options){},
			scope: this,
			method: 'POST',
			params: __params,
			maskEl: this.bwrap
		});
	},
	
	/**
	 * Save data to database.
	 * 
	 * @param {Array} data - the given data to save.
	 * @param {Object} type - the given type.
	 */
	_fnUpdate : function(data, type){
		// create ajax request.
		var __params = {iwct:'followUpContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'update'};
		__params['action'] = 'update' ;
		
		
		
		var __ids = [];
     	if (data !== undefined) {
         	for (var __index = 0; __index < data.length; __index++) {
         		__ids.push(data[__index].id);
            }
        }else{
         	return;
        }
		
        __params['date'] = this._getDate(type);
        __params['fdate'] = iNet.INET_DATE_FORMAT;;
		// add data.
		__params['ids'] = __ids.join(';');
		
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			success: this._fnRequestSuccess,
			failure: function(response, options){},
			scope: this,
			method: 'POST',
			params: __params,
			maskEl: this.bwrap
		});
	},
	
	/**
	 * Save data to database.
	 * 
	 * @param {Object} data - the given data to save.
	 */
	_fnDelete : function(data){
				
		// create ajax request.
		var __params = {iwct:'followUpContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'delete'};
		__params['action'] = 'delete' ;
		
		// add data.
		__params['ids'] = data.join(';'); ;
		__params['data'] = Ext.util.JSON.encode(data);
		
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			success: this._fnRequestSuccess,
			failure: function(response, options){},
			scope: this,
			method: 'POST',
			params: __params,
			maskEl: this.bwrap
		});
	},
	
	/**
	 * update flag 
	 * @param {int} data
	 * @param {string} flag
	 */
	updateFlag : function(data, flag){
		var __record = this._findByData(data);
		if(__record != null ){
			__record.data.flag = flag;
			__record.commit();
		}
	}
}) ;
