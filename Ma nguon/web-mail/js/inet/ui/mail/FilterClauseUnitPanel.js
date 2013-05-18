/***
 Copyright 2008 by Tan Truong (tntan@truthinet.com.vn)
 Licensed under the iNet Solutions Corp.,;
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 	http://www.truthinet.com.vn/licenses
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
 
iNet.iwebos.ui.mail.FilterClauseUnitPanel = Ext.extend(Ext.Panel,{
    /**
	 * @cfg {object} actionRemove - the given remove action.
	 */
	initComponent: function(){
		var __objectStore = new Ext.data.SimpleStore({
	        fields: ['text','value'],
	        data: [ [iwebos.message.mail.filter_object_sender, 'SENDER'], 
	        		[iwebos.message.mail.filter_object_recipient, 'RECIPIENTS'],
	        		[iwebos.message.mail.filter_object_subject, 'SUBJECT'],
	        		//[iwebos.message.mail.filter_object_content, 'CONTENT'],
	        		[iwebos.message.mail.filter_object_date_sent, 'DATE_SENT'],
	        		[iwebos.message.mail.filter_object_date_received, 'DATE_RECEIVED'],
	        		[iwebos.message.mail.filter_object_all, 'ALL']
	        	  ]
	    });
	    
	    var __operatorStore = new Ext.data.SimpleStore({
	        fields: ['text','value'],
	        data: [ [iwebos.message.mail.filter_operator_contain, 'CONTAINS'], 
	        		[iwebos.message.mail.filter_operator_does_not_contain, 'DOES_NOT_CONTAIN'],
	        		[iwebos.message.mail.filter_operator_is, 'IS'],
	        		[iwebos.message.mail.filter_operator_is_not, 'IS_NOT'],
	        		[iwebos.message.mail.filter_operator_start_with, 'STARTS_WITH'],
	        		[iwebos.message.mail.filter_operator_does_not_start_with, 'DOES_NOT_START_WITH'],
	        		[iwebos.message.mail.filter_operator_end_with, 'ENDS_WITH'],
	        		[iwebos.message.mail.filter_operator_does_not_end_with, 'DOES_NOT_END_WITH']
	        	  ]
	    }); 
    	this.__objectCombo = new Ext.form.ComboBox({
					                    xtype: 'combo',
					                    border: false,
					                    anchor: '100%',
					                    store: __objectStore,
					                    mode: 'local',
					                    displayField : 'text',
					                    valueField : 'value',
					                    value: 'SENDER',
					                    forceSelection : true,
					                    selectOnFocus: true,
					                    triggerAction: 'all',
					                    readOnly: true,
					                    hideLabel: true
	                					});
		this.__operatorCombo = new Ext.form.ComboBox({
					                    xtype: 'combo',
					                    border: false,
					                    anchor: '100%',
					                    store: __operatorStore,
					                    mode: 'local',
					                    displayField : 'text',
					                    valueField : 'value',
					                    value: 'CONTAINS',
					                    forceSelection : true,
					                    selectOnFocus: true,
					                    triggerAction: 'all',
					                    readOnly: true,
					                    hideLabel: true
	                					});  
	    this.__txtData = new Ext.form.TextField({
					                    xtype: 'textfield',
					                    border: false,
					                    anchor: '95%',
					                    hideLabel: true
					                });
	    this.__cmdRemove = new Ext.Button({
					    				xtype: 'button',
					                    border: false,
					                    iconCls: 'icon-email-filter-remove',
					                    text: iwebos.message.mail.filter_remove,
										handler:this.remove.createDelegate(this)
	    							});
    	var __main = new Ext.Panel({
			layout: 'column',
	        border: false,
	        anchor: '100%',
	        columnWidth: 1,
	        items: [{
	            columnWidth: 0.25,
	            border: false,
	            anchor: '100%',
	            bodyStyle: 'padding:10px 5px;',
	            items: [{
	                layout: 'form',
	                border: false,
	                anchor: '100%',
	                items: [this.__objectCombo]
	            }]
	        }, {
	            columnWidth: 0.25,
	            border: false,
	            anchor: '100%',
	            bodyStyle: 'padding:10px 5px;',
	            items: [{
	                layout: 'form',
	                border: false,
	                anchor: '100%',
	                items: [this.__operatorCombo]
	            }]
	        }, {
	            columnWidth: 0.33,
	            border: false,
	            anchor: '100%',
	            bodyStyle: 'padding:10px 5px;',
	            items: [{
	                layout: 'form',
	                border: false,
	                anchor: '100%',
	                items: [this.__txtData]
	            }]
	        }, {
	            columnWidth: 0.17,
	            border: false,
	            anchor: '100%',
	            bodyStyle: 'padding:10px 5px;',
	            items: [{
	                layout: 'form',
	                border: false,
	                anchor: '100%',
	                items: [this.__cmdRemove]
	            }]
	        }]
	    });
    	
   		var __panel = [__main];
   		this.items = __panel;
   		// create toolbar.
		iNet.iwebos.ui.mail.FilterClauseUnitPanel.superclass.initComponent.call(this) ;	
	},
	/**
	 * remove this from parent
	 */
	remove : function(){
		var __remove = this.actionRemove;
		
		if(!__remove.scope){
			__remove.fn(__owner.cardId) ;
		}else{
			__remove.fn.apply(__remove.scope, [this]) ;
		}
    },
    
    /**
     * get data clause unit
     * @return {object}
     */
    getData: function(){
    	var __unit = {};
    	__unit['object'] = this.__objectCombo.getValue();
    	__unit['operator'] = this.__operatorCombo.getValue();
    	__unit['data'] = this.__txtData.getValue();
    	
    	return __unit;
    },
    /**
     * enable button remove
     * @param {boolean} enable
     */
    setEnableRemove : function(enable){
    	this.__cmdRemove.setDisabled(!enable);
    },
    
    setData : function(unit){
    	var __unit = unit || {};
    	this.__objectCombo.setValue(__unit.object);
    	this.__operatorCombo.setValue(__unit.operator);
    	this.__txtData.setValue(__unit.data);
    }
});
