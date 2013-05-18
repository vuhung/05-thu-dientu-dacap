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
 * @class iNet.iwebos.ui.mail.DialogMailFilter
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogMailFilter
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogMailFilter = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    this.prefix = 'iwebos-mail-filter-';
    this.btnDeleteId = this.prefix + 'delete-button';
    
	var __sortInfo = {
        field: 'name',
        direction: 'DESC'
    };
	var __defaultSort = {
		field: 'name', 
		direction: "DESC"
	};
    
    // email record.
    var __mailRecord = Ext.data.Record.create([
			{name: 'id', mapping: 'id', type: 'int'}, 
			{name: 'name', mapping: 'name', type: 'string'},
			{name: 'folder', mapping: 'folder', type: 'string'},
			{name: 'clause', mapping: 'clause'},
			{name: 'fId', mapping: 'fId', type: 'int'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, __mailRecord);
    

    var __columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.filter_name,
			dataIndex: 'name',
			fixed: false,
			menuDisabled:true,
			width:100
		},{
			header: iwebos.message.mail.filter_folder,
			dataIndex: 'folder',
			fixed: false,
			menuDisabled:true,
			width:100
		}
	]);
    
	var __baseParams = {
        iwct: 'loadMailFilterContent',
        iwm: 'MAIL',
        iwc: 'READ_ONLY',
        iwa: 'search',		
        action: 'search'
    };
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		region: 'north',
		anchor: '100%',
		minSize: 150,
		baseParams: __baseParams,
		autoScroll: true,
		height:150,
		url: 'jsonajax',
		method: 'POST',
		loadFirst : true,
		sortInfo: __sortInfo,		
		reader: reader,
		dsort: __defaultSort,
		cm: __columnModel,
		tbar:[{xtype: 'button',
            border: false,
			iconCls:'icon-email-filter-add',
			text:iwebos.message.mail.filter_add,
			handler:this.__addFilter.createDelegate(this)
		},{
			id: this.btnDeleteId,
            xtype: 'button',
            border: false,
            disabled: true,
			iconCls:'icon-email-filter-remove',
			text:iwebos.message.mail.filter_delete,
			handler:this._deleteFilter.createDelegate(this)
		}]
	});
	
	this.grid.on('contextmenu',this._contextMenu,this);
	this.grid.on('rowcontextmenu',this._rowContextMenu,this);
	this.grid.on('rowdblclick', this._rowDoubleClick, this);
	
	this.preViewFilter = new Ext.Panel({
			        id: 'preViewFilter',
					region: 'center',
					frame:false,
					border: false,
					header: true,
					height: 300,
					autoScroll:true,
					title: iwebos.message.mail.filter_detail
			    });
	
	var __panelMain = new Ext.Panel({
        frame: false,
        border: false,
        anchor: '100%',
        layout: 'border',
        items: [this.grid,this.preViewFilter]
    });
	
	var __html =  ['<div style="overflow:visible; height:100px; background:#fff;">',
	'<table border=0 cellspacing=0 cellpadding=0 bgcolor="#FFFFFF">',
	'<tr>',
		'<td valign=top width="100%" style="padding-top:3px;">&nbsp;<span class="mail-icon-dialog-filter-filter">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<b style="color:blue;">{name}</b></td>',
	'</tr>',
	'<tr>',
		'<td valign=top style="padding-top:5px;">&nbsp;',
		'<tpl for="clause"><span>&nbsp;&nbsp;&nbsp;&nbsp;</span> <b>{[this.repl(values.operator)]} :</b><br>',
			'<tpl for="units">',
				'&nbsp;<span>&nbsp;&nbsp;&nbsp;&nbsp;</span> {[this.repl(values.object)]} <span>&nbsp;&nbsp;&nbsp;&nbsp;</span> {[this.repl(values.operator)]} <span>&nbsp;&nbsp;&nbsp;&nbsp;</span> {data} <br />',
			'</tpl>',
		'</tpl>',
		'</td>',
	'</tr>',
	'<tr>',
		'<td valign=top style="padding-top:5px;padding-bottom:5px;">&nbsp;<span class="mail-icon-dialog-filter-folder">&nbsp;&nbsp;&nbsp;&nbsp;</span> ' + iwebos.message.mail.filter_folder + ': &nbsp;{folder}</td>',
	'</tr>',
	'</table></div>'];
	
	var __tpl = new Ext.XTemplate(__html.join(''), {
		repl: function(str){
			switch (str) {
				// __filterOperatorStore
                case "AND":
                    return iwebos.message.mail.filter_operator_and;
                case "OR":
                    return iwebos.message.mail.filter_operator_or;
				
				// 	__objectStore
                case "SENDER":
                    return iwebos.message.mail.filter_object_sender;
				case "RECIPIENTS":
                    return iwebos.message.mail.filter_object_recipient;
				case "SUBJECT":
                    return iwebos.message.mail.filter_object_subject;
				case "DATE_SENT":
                    return iwebos.message.mail.filter_object_date_sent;
				case "DATE_RECEIVED":
                    return iwebos.message.mail.filter_object_date_received;
				case "ALL":
                    return iwebos.message.mail.filter_object_all;
					
				//__operatorStore	
				case "CONTAINS":
                    return iwebos.message.mail.filter_operator_contain;
				case "DOES_NOT_CONTAIN":
                    return iwebos.message.mail.filter_operator_does_not_contain;
				case "IS":
                    return iwebos.message.mail.filter_operator_is;
				case "IS_NOT":
                    return iwebos.message.mail.filter_operator_is_not;
				case "STARTS_WITH":
                    return iwebos.message.mail.filter_operator_start_with;
				case "DOES_NOT_START_WITH":
                    return iwebos.message.mail.filter_operator_does_not_start_with;
				case "ENDS_WITH":
                    return iwebos.message.mail.filter_operator_end_with;
				case "DOES_NOT_END_WITH":
                    return iwebos.message.mail.filter_operator_does_not_end_with;	
            }

        }
	});
	
    /**
     * Event when rowselect
     */
    this.grid.getSelectionModel().on('rowselect', function(sm, rowIdx, r){
		    	
		var __record = this.grid.store.getAt(rowIdx) ;
    	//apply template to panel view filer
		var __panelView = Ext.getCmp('preViewFilter');
		
		// add to template
		if(__panelView){ 
			__tpl.overwrite(__panelView.body,__record.data);
		
			Ext.EventManager.fireWindowResize();
		}
	},this);
    
    /**
     * Event when selection change
     */
    this.grid.getSelectionModel().on('selectionchange', function(sm){
    	if(!!sm.getSelected()){
    		Ext.getCmp(this.btnDeleteId).setDisabled(false);
    	}else{
    		Ext.getCmp(this.btnDeleteId).setDisabled(true);
    	}
    }, this);
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogMailFilter.superclass.constructor.call(this, {
        id: 'mail-filter-dialog',
        title: iwebos.message.mail.filter_mail,
        iconCls: 'icon-email-filter',
        region: 'center',
        anchor: '100%',
        width: 500,
        height: 510,
        modal: true,
        frame: true,
        resizable: false,
        layout: 'border',
        items: [this.grid,this.preViewFilter],
        buttons: [{
            text: iwebos.message.mail.close,
            iconCls: 'ok-btn',
            handler: this.fnCancelHandler,
            scope: this.okhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogMailFilter, iNet.iwebos.ui.common.dialog.TitleDialog, {
    /**
     * no border.
     */
    bodyBorder: false,
    /**
     * do not drag content.
     */
    dragContent: false,
    /**
     * header image class.
     */
    hdImgCls: '',
    /**
     * top title.
     */
    ttitle: iwebos.message.mail.filter_mail,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.filter_info,
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
	
	/**
	 * call dialog Add filter
	 */
	__addFilter: function(){
		var __store = this.grid.store;
		var __okHandler = function(){
			// validate data
			var __error = this.validate();
			if(__error.length > 0){
				// show error.
	            Ext.MessageBox.show({
	                	title: iwebos.message.mail.announcement,
	                    msg: __error,
	                    buttons: Ext.MessageBox.OK,
	                    icon: Ext.MessageBox.ERROR
	            });
	            return;
			}
			
			var __fnSuccess = function(response, options){
				if(!!response){
					var __data = Ext.util.JSON.decode(response.responseText);
					var __success = __data.success == undefined ? true: false;
					if(__success){
						__store.addData({id: __data.id, name: __data.name, folder: __data.folder});						
						return;
					}	
				}
				// show error.
			    Ext.MessageBox.show({
			            	title: iwebos.message.mail.announcement,
			                msg: iwebos.message.mail.filter_error_save,
			                buttons: Ext.MessageBox.OK,
			                icon: Ext.MessageBox.ERROR
			    });
			};
			// get data
			var __filter = this.getData();
			
			// base param
			var __param = {
		        iwct: 'mailFilterContent',
		        iwm: 'MAIL',
		        iwc: 'READ_WRITE',
		        iwa: 'save',		
		        action: 'save'};
		        
		    __param['object'] = Ext.util.JSON.encode(__filter);
		        
		    // send request to server.
	        iNet.Ajax.request({
	            url: 'jsonajax',
	            params: __param,
	            scope: this,
	            method: 'POST',
	            success: __fnSuccess,
	            failure: __fnSuccess
	        });    
	        this.destroy();
    	};
			
			
		var __dlg = new iNet.iwebos.ui.mail.DialogMailAddFilter(this,{
			    									okHandler: __okHandler,
			    									okhScope: __dlg
			    								});
		__dlg.show(this);
	},
	_deleteFilter: function(){
		var __record = this.grid.getSelectionModel().getSelected();
		var __store = this.grid.store;
		if(!__record) return;
		
		// base param
		var __fnSuccess = function(response, options){
			if(!!response){
				var __data = Ext.util.JSON.decode(response.responseText);
				if(__data.success){
					__store.remove(__record);						
					return;
				}	
			}
			// show error.
			Ext.MessageBox.show({
			           	title: iwebos.message.mail.announcement,
			            msg: iwebos.message.mail.filter_error_delete,
			            buttons: Ext.MessageBox.OK,
			           	icon: Ext.MessageBox.ERROR
			});
		};		
		var __param = {
	        iwct: 'mailFilterContent',
	        iwm: 'MAIL',
	        iwc: 'READ_WRITE',
	        iwa: 'delete',		
	        action: 'delete',
	        id: __record.data.id};
	        
	    // send request to server.
        iNet.Ajax.request({
            url: 'jsonajax',
            params: __param,
            scope: this,
            method: 'POST',
            success: __fnSuccess,
            failure: __fnSuccess
        });
	},
	_contextMenu: function(event){
		event.stopEvent();
	},
	
	_rowContextMenu: function(grid, index, event){
		event.stopEvent();
		// handle context menu.
		WebOSGridService.handleSelection(grid, index) ;
		var __menu = new Ext.menu.Menu({
		                items: [{
		                    text: iwebos.message.mail.filter_delete,
		                    iconCls: 'icon-email-filter-remove',
		                    scope: this,
		                    handler: this._deleteFilter.createDelegate(this)
		                }]
		});
		__menu.showAt(event.getXY());
	},
	
	_rowDoubleClick: function(grid, index, event){
		event.stopEvent();
		WebOSGridService.handleSelection(grid, index) ;
		var __store = grid.store;
		var __record = grid.getSelectionModel().getSelected();
		
		var __okHandler = function(){
			// validate data
			var __error = this.validate();
			if(__error.length > 0){
				// show error.
	            Ext.MessageBox.show({
	                	title: iwebos.message.mail.announcement,
	                    msg: __error,
	                    buttons: Ext.MessageBox.OK,
	                    icon: Ext.MessageBox.ERROR
	            });
	            return;
			}
			
			var __fnSuccess = function(response, options){
				if(!!response){
					var __data = Ext.util.JSON.decode(response.responseText);
					var __success = __data.success == undefined ? true: false;
					if(__success){
						Ext.apply(__record.data, __data);
						__record.commit();
						grid.getSelectionModel().selectRecords([__record]);
						return;
					}	
				}
				// show error.
			    Ext.MessageBox.show({
			            	title: iwebos.message.mail.announcement,
			                msg: iwebos.message.mail.filter_error_save,
			                buttons: Ext.MessageBox.OK,
			                icon: Ext.MessageBox.ERROR
			    });
			};
			// get data
			var __filter = this.getData();
			
			// base param
			var __param = {
		        iwct: 'mailFilterContent',
		        iwm: 'MAIL',
		        iwc: 'READ_WRITE',
		        iwa: 'save',		
		        action: 'save'};
		        
		    __param['object'] = Ext.util.JSON.encode(__filter);
		        
		    // send request to server.
	        iNet.Ajax.request({
	            url: 'jsonajax',
	            params: __param,
	            scope: this,
	            method: 'POST',
	            success: __fnSuccess,
	            failure: __fnSuccess
	        });    
	        this.destroy();
    	};
    	
		var __dlg = new iNet.iwebos.ui.mail.DialogMailAddFilter(this,{
			    									okHandler: __okHandler,
			    									okhScope: __dlg
			    								});
		__dlg.show(this);
		__dlg.setData(__record.data);
	}
});
