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
 * @class iNet.iwebos.ui.mail.DialogMailAddFilter
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogMailAddFilter
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogMailAddFilter = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    this.prefix = 'iwebos-mail-filter-';
    this.clausese = this.prefix + 'calusese';
    this.name = this.prefix + 'name';
    this.filterOperator = this.prefix + 'filter-operator';
    this.cmdFolder = this.prefix + 'button-folder';
    this.filterActionCombo = this.prefix + 'action-combo';
    this.__owner = {fId: 0};
    var __filterOperatorStore = new Ext.data.SimpleStore({
	        fields: ['text','value'],
	        data: [ [iwebos.message.mail.filter_operator_and, 'AND'], 
	        		[iwebos.message.mail.filter_operator_or, 'OR']	        		
	        	  ]
	    });
	var __filterActionStore = new Ext.data.SimpleStore({
	        fields: ['text','value'],
	        data: [ [iwebos.message.mail.filter_action_send_to_folder, 'MOVE']/*, 
	        		[iwebos.message.mail.filter_action_delete, 'DELETE']	*/        		
	        	  ]
	    });
    var __itemAddMore = new iNet.iwebos.ui.mail.FilterClauseUnitPanel({
									    layout: 'column',
									    border: false,
									    anchor: '100%',
									    columnWidth: 1,
									    actionRemove : {scope: this,fn: this._removeClause}
    				});
       				
    var __panelMain = new Ext.form.FormPanel({
        frame: false,
        border: false,
        anchor: '100%',
        items: [{
            layout: 'column',
            border: false,
            anchor: '100%',
            items: [{
                columnWidth: 1,
                border: false,
                anchor: '100%',
                items: [{
                    layout: 'form',
                    border: false,
                    labelWidth: 90,
                    items: [{
                    	id : this.name,
                        xtype: 'textfield',
                        border: false,
                        fieldLabel: iwebos.message.mail.filter_name,
                        anchor: '100%'
                    }]
                }]
            }]
        }, {
            layout: 'column',
            border: false,
            anchor: '100%',
            items: [{
                columnWidth: 1,
                xtype: 'fieldset',
                title: iwebos.message.mail.filter_search_follow_standar,
                autoHeight: true,
                anchor: '100%',
                collapsed: false,
                items: [{
                    layout: 'column',
                    border: false,
                    anchor: '100%',
                    columnWidth: 1,
                    items: [{
                        columnWidth: 0.2,
                        border: false,
                        anchor: '100%',
                        bodyStyle: 'padding:10px 10px;',
                        items: [{
                            xtype: 'button',
                            border: false,
                            iconCls: 'icon-email-filter-add',
                            text: iwebos.message.mail.filter_unit_add,
                            handler: this._addClause.createDelegate(this)
                        }]
                    }, {
                        columnWidth: 0.8,
                        border: false,
                        anchor: '100%',
                        bodyStyle: 'padding:10px 10px;',
                        items: [{
                            layout: 'form',
                            border: false,
                            anchor: '100%',
                            labelWidth: 90,
                            items: [{
                            	id : this.filterOperator,
                                xtype: 'combo',
                                border: false,
                                anchor: '100%',
                                store : __filterOperatorStore,
                                mode: 'local',
			                    displayField : 'text',
			                    valueField : 'value',
			                    value: 'OR',
                                forceSelection : true,
					            selectOnFocus: true,
					            triggerAction: 'all',
					            readOnly: true,
                                fieldLabel: iwebos.message.mail.filter_search_email
                            }]
                        }]
                    }]
                }, {
                	layout: 'form',
                    border: false,
                    anchor: '100%',
                    items: 	new Ext.Panel({
                    	id : this.clausese,
						layout: 'column',
	                    border: false,
	                    anchor: '100%',
	                    columnWidth: 1,
						height:150,
						frame:true,
						autoDestroy : true,
						autoScroll:true,
						items:[__itemAddMore]
					})
                }]
            }]
        },{
            layout: 'column',
            border: false,
            anchor: '100%',
			bodyStyle: 'padding:10px 0px;',
            items: [{
                columnWidth: 0.4,
                border: false,
                anchor: '100%',
                items: [{
                    layout: 'form',
                    border: false,
                    items: [{
                    	id: this.filterActionCombo,
                        xtype: 'combo',
                        border: false,
                        anchor: '100%',
                        store :__filterActionStore,
                        mode: 'local',
	                    displayField : 'text',
	                    valueField : 'value',
	                    value: 'MOVE',
	                    forceSelection : true,
	                    selectOnFocus: true,
	                    triggerAction: 'all',
	                    readOnly: true,
	                    hideLabel: true
                    }]
                }]
            },{
                columnWidth: 0.6,
                border: false,
                anchor: '100%',
                items: [{
                    layout: 'form',
                    border: false,
                    items: [{
                    	id: this.cmdFolder,
                        xtype: 'button',
                        border: false,
						text:'...',
						handler: this._selectFolder.createDelegate(this)
                    }]
                }]
            }]
		}, {
            layout: 'fit',
            border: false,
            anchor: '100%',
            items: [{}]
        }]
    });
   
    __itemAddMore.setEnableRemove(false);
    Ext.getCmp(this.filterActionCombo).on('beforeselect',this._onBeforeSelectAction,this);
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogMailAddFilter.superclass.constructor.call(this, {
        id: 'mail-add-filter-dialog',
        title: iwebos.message.mail.filter_add_new,
        iconCls: 'icon-email-filter',
        region: 'center',
        anchor: '100%',
        width: 500,
        height: 500,
        modal: true,
        frame: true,
        resizable: false,
        layout: 'border',
        hideMode: 'offsets',
        items: [{
            region: 'center',
            frame: true,
            width: '100%',
            items: __panelMain
        }],
        buttons: [{
            text: iwebos.message.mail.save,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogMailAddFilter, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.filter_create,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.filter_create_new_to_management_email,
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    _addClause : function(){
    	var __newClause = new iNet.iwebos.ui.mail.FilterClauseUnitPanel({
									    layout: 'column',
									    border: false,
									    anchor: '100%',
									    columnWidth: 1,
									    actionRemove : {scope: this,fn: this._removeClause}});
		// get panel clause								    
    	var __comp = Ext.getCmp(this.clausese);
    	
    	// get total clause unit
    	var array = __comp.findByType(iNet.iwebos.ui.mail.FilterClauseUnitPanel);
    	if(array.length == 1){
    		// diable button remove
    		array[0].setEnableRemove(true);
    	}
    	// add new clause unit
    	__comp.add(__newClause);
    	
		Ext.EventManager.fireWindowResize();
		return __newClause;
    },
    
    _removeClause : function(component){
    	var __comp = Ext.getCmp(this.clausese);
    	__comp.remove(component,true);
    	
    	var array = __comp.findByType(iNet.iwebos.ui.mail.FilterClauseUnitPanel);
    	if(array.length == 1){
    		// diable button remove
    		array[0].setEnableRemove(false);
    	}
    },
    
    /**
     * get data submit
     */
    getData: function(){
    	var __object = {};
    	var __owner = this.__owner || {};
    	//1. get name filter 
    	__object['id'] = this.__owner.id || 0;
    	var __comp = Ext.getCmp(this.name);
    	__object['name'] = __comp.getValue();
    	
    	//2. get clause =========
    	//2.1 get filter operator
    	var __clause = {};
    	__comp = Ext.getCmp(this.filterOperator);
    	__clause['operator'] = __comp.getValue();
    	
    	//2.2 get clause units
    	__comp = Ext.getCmp(this.clausese);
    	var __units = [];
    	var __array = __comp.findByType(iNet.iwebos.ui.mail.FilterClauseUnitPanel);
    	for(var index = 0;index <__array.length; index++){
    		__units[index]= __array[index].getData();
    	}
    	__clause['units'] = __units;
    	__object['clause'] = __clause;
    	
    	//3. get folfer
    	__object['fId'] = __owner.fId;
    	
    	return __object;
    },
    
    setData : function(data){
    	var __object = data || {};
    	//1. get name filter 
    	this.__owner.id = __object.id;
    	var __comp = Ext.getCmp(this.name);
    	__comp.setValue(__object.name);
    	
    	//2. get clause =========
    	//2.1 get filter operator
    	var __clause = __object.clause;
    	__comp = Ext.getCmp(this.filterOperator);
    	__comp.setValue(__clause.operator);
    	
    	//2.2 get clause units
    	__comp = Ext.getCmp(this.clausese);
    	var __units = __clause.units;
    	var __array = __comp.findByType(iNet.iwebos.ui.mail.FilterClauseUnitPanel);
    	for(var index = 0;index <__units.length; index++){
    		if(index == 0){
    			var __firstClauseCmp = __array[index];
    			if(!!__firstClauseCmp) 
    				__firstClauseCmp.setData(__units[index]);
    		}else{
    			var __clause = this._addClause();
    			__clause.setData(__units[index]);	
    		}
    	}
    	
    	//3. get folfer
    	if(__object.fId == 0){
    		__comp = Ext.getCmp(this.filterActionCombo);
    		__comp.setValue('DELETE');
    		// invisible button select folder
    		__comp = Ext.getCmp(this.cmdFolder);
    		__comp.setVisible(false);
    	}else{
    		__comp = Ext.getCmp(this.cmdFolder);
    		__comp.setText(__object.folder);
    	}
    	
    	this.__owner.fId = __object.fId;
    	
    	return __object;
    },
    
    /**
     * validate data
     * @return {String}
     */
    validate : function(){
    	// valid filter names
    	var __comp = Ext.getCmp(this.name);
    	if(__comp.getValue().trim() == ''){
    		__comp.focus(true);
    		return iwebos.message.mail.filter_error_input_name;
    	}
    	
    	__comp = Ext.getCmp(this.filterActionCombo);
    	var __action = __comp.getValue();
    	if(__action == 'MOVE' && this.__owner.fId == 0){
    		return iwebos.message.mail.filter_error_input_folder;
    	}
    	return '';	
    },

    _selectFolder: function(){
    	var __panel = this;
    	var __okHandler = function(){
			// get data
			var __folder = this.getData();
			
			if(__folder == undefined){
				// show error.
				Ext.MessageBox.show({
			            	title: iwebos.message.mail.announcement,
			                msg: iwebos.message.mail.filter_error_input_folder,
			                buttons: Ext.MessageBox.OK,
			                icon: Ext.MessageBox.ERROR
			    });
			    return;
			}
			
			// set text 
			var __button = Ext.getCmp(__panel.cmdFolder);
			__button.setText(__folder.original);
			__panel.__owner.fId = __folder.id;
			
	        this.destroy();
    	};
    	
    	var __dlg = new iNet.iwebos.ui.mail.DialogMailFolder(this,{
    												okHandler: __okHandler,
			    									okhScope: __dlg,
			    									ttitle:iwebos.message.mail.select_folder_for_filter
			    								});
		__dlg.show(this);		
    },
    
    _onBeforeSelectAction : function(combo,record,index){
    	var __action = record.data.value;
    	
    	var __button = Ext.getCmp(this.cmdFolder);
    	if(__action == 'MOVE'){
    		__button.setVisible(true);	
    	}else{
    		__button.setVisible(false);
    	}
    }
});