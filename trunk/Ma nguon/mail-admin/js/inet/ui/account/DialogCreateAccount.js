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
 * @class iNet.iwebos.ui.account.DialogCreateAccount
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateAccount
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.account.DialogCreateAccount = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    
    this.prefix = (!this.prefix) ? 'iwebos-dialog-mail-create' : this.prefix;
    
    this.accountId = this.prefix + '-account-id' ;
	this.domainId = this.prefix + '-domain-id' ;
	this.smtpId = this.prefix + '-smpt-id';
	this.capacityId = this.prefix + '-capacity-id';
	this.virusId = this.prefix + '-virus-id';
	this.spamId = this.prefix + '-spam-id';
	this.forwardId = this.prefix + '-forward-id';
	this.fwAddressId = this.prefix + '-fw-address-id';
	this.activeId = this.prefix + '-active-id';
	this.department = this.prefix + '-department-id';
	this._owner = {};
    
    this._domainStore = new Ext.data.JsonStore({
        fields: [{name:'domainId', mapping:'id', type:'string'},
	    		 {name:'domainName', mapping:'text', type:'string'}]
    });
    
    var __main = new Ext.form.FormPanel({
        region: 'center',
        anchor: '100%',
        border: false,
        frame: true,
        layout: 'column',
        items: [{
            columnWidth: 1,
            layout: 'column',
            anchor: '100%',
            border: false,
            items: [{
                columnWidth: 0.5,
                labelWidth: 90,
                layout: 'form',
                border: false,
                items: [{
                	id: this.accountId,
                    border: false,
                    xtype: 'textfield',
                    anchor: '98%',
                    fieldLabel: iwebos.message.mail.account
                }]
            }, {
                columnWidth: 0.5,
                labelWidth: 90,
                layout: 'form',
                border: false,
                items: [{
                	id: this.domainId,
                    border: false,
                    anchor: '98%',
                    xtype: 'combo',
                    fieldLabel: '@',
                    store: this._domainStore,
                    displayField: 'domainName',
                    valueField: 'domainId',
                    anchor: '98%',
                    mode: 'local',
                    triggerAction: 'all',
                    forceSelection: true,
                    selectOnFocus: true
                }]
            }]
        },{
            columnWidth: 1,
            layout: 'column',
            anchor: '100%',
            border: false,
            items: [{
                columnWidth: 1,
                anchor: '100%',
                xtype: 'fieldset',
                title: iwebos.message.mail.options,
				height: 140,
				bodyStyle:'padding-left:10px;',
                items: [{
                    columnWidth: 1,
                    layout: 'column',
                    anchor: '100%',
                    border: false,
                    items: [{
                        columnWidth: 0.5,
                        labelWidth: 90,
                        layout: 'form',
                        border: false,
                        items: [{
                        	id: this.smtpId,
							xtype:'checkbox',
							hideLabel:true,
							anchor: '95%',
							checked : true,
							tabIndex: 7,
							boxLabel:iwebos.message.mail.create.role_smtp
						},{
							id: this.spamId,
							xtype:'checkbox',
							hideLabel:true,
							anchor: '95%',
							checked : true,
							tabIndex: 8,
							boxLabel:iwebos.message.mail.create.role_spam
						},{
							id: this.capacityId,
							xtype: 'numberfield',
							allowDecimals: false,
							maxLength: 5,
							anchor: '95%',
							allowBlank:false,
							value: 50,
							tabIndex: 12,
							fieldLabel: iwebos.message.mail.create.sizelimit
						}]
                    }, {
                        columnWidth: 0.5,
                        labelWidth: 90,
                        layout: 'form',
                        border: false,
                        items: [{
							layout:'column',
							border:false,
							columnWidth:1,
							anchor:'100%',
							items:[{
								layout:'form',
								border:false,
								columnWidth:0.6,
								anchor:'80%',
								items:[{									
									id:this.forwardId,
									xtype:'checkbox',
									hideLabel:true,
									tabIndex: 9,							
									boxLabel:iwebos.message.mail.create.role_transfer,
									handler:this.hanlerForwardCheck.createDelegate(this)
								}]
							},{
								layout:'form',
								border:false,
								columnWidth:0.4,
								anchor: '98%',
								items:[{
									id:this.fwAddressId,
									xtype:'textfield',
									anchor: '88%',
									disabled: true,
									hideLabel:true
								}]
								
							}]
						},{
							id: this.virusId,
							xtype:'checkbox',
							hideLabel:true,
							anchor: '100%',
							checked : true,
							tabIndex: 10,
							boxLabel:iwebos.message.mail.create.role_virus
						},{
							id: this.activeId,
							xtype:'checkbox',
							hideLabel:true,
							anchor: '100%',
							checked : true,
							tabIndex: 11,
							boxLabel:iwebos.message.mail.create.role_active
						}]
                    }]
                }]
            }]
        }]
    });    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    //Ext.getCmp(this.domainId).on('select', this._onSelectDomain, this, {stopEvent: true});
    
    
    iNet.iwebos.ui.account.DialogCreateAccount.superclass.constructor.call(this, {
        id: 'mail-dlg-create-account',
        title: iwebos.message.org.user_create_email_account_title,
        iconCls: 'icon-email-createacc',
        region: 'center',
        anchor: '100%',
        width: 600,
        height: 400,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [__main],
        buttons: [{
            text: iwebos.message.mail.select_account,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
            handler: this.cancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.account.DialogCreateAccount, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.org.user_create_email_account_title,
    /**
     * bottom title.
     */
    btitle: iwebos.message.org.user_create_email_account_sub_title,
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    
    /**
     * Handle when click ok button
     * @return {Boolean}
     */
    fnOkHandler: function(){		
		var __validate = this._onValidation(); 
		if(__validate != ''){
			Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : __validate,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return;
		} 
		
		this._onSave();
		this.destroy();
    },
    
    /**
	 * saving domain
	 */
	_onSave: function() {
		// get the data object
		var object = this._getData();
		
		// get action [save]
		var __save = this._owner.action['save'];
		if(!__save) return;
		
		if(!__save.scope) {
			__save.fn(object);
		} else {
			__save.fn.apply(__save.scope, [object]);
		}		
	},
	
	/**
	 * handle event when check on forward check box
	 */
	hanlerForwardCheck: function() {
		var check = Ext.getCmp(this.forwardId).getValue();
		Ext.getCmp(this.fwAddressId).setDisabled(!check);
	},
	
	/**
	 * 
	 * @param {} domains
	 * @param {} departments
	 * @param {} account
	 * @param {} editable
	 */
	_setData: function (__account, __action, __domains, __selectedDomain, __group, __editable) {
		this._owner.account = __account;
		this._owner.action = __action;	
		this._owner.groups = __group;
		this._owner.selectedDomain = __selectedDomain;
		this.editable = __editable;
		if(__account.uname != null && __account.uname != undefined && __account.email != null) {
			// fill data on GUI
			this._fillData(__account);
		} else {
			if(__domains != null) {
				var __item;
				var __select = '';
				for(var index = 0; index < __domains.length; index++) {
					__item = __domains[index];
					if(__item.edit) {
						if(__item.orgId == __selectedDomain) {
							__select = __item.orgId;
						}
						// select first
						__select = __select==''?__item.orgId:__select;
						
						// add domain to store
						this._domainStore.add(new Ext.data.Record({
							domainId: __item.orgId,
							domainName: __item.organization
						}));
					}					
				}
				if(__select != '') {
					Ext.getCmp(this.domainId).setValue(__select);
					// load department
					//this._loadDepartment();
				}
				
				if(__account.email != null && __account.email != undefined) {
					this._fillNewInfo(__account);
				}
				
			}
		}
		
	},

	/**
	 * validation
	 * @return {String} - messages error
	 */
	_onValidation: function(){		
		// valid account name
		var __comp = Ext.getCmp(this.accountId);
		var __account = __comp.getValue();
		if(__account === '') return iwebos.message.mail.create.error_input_email;
		
		__comp = Ext.getCmp(this.domainId);
		if(__comp.getValue() === '') return iwebos.message.mail.create.error_input_domain_for_email;

		// capacity
		__comp = Ext.getCmp(this.capacityId);
		var __capacity = __comp.getValue();
		if(__capacity === '') return iwebos.message.mail.error_input_limit;
		
		// forward
		var forward = Ext.getCmp(this.forwardId).getValue();		
		if(forward && Ext.getCmp(this.fwAddressId).getValue().trim() == '') 
			return iwebos.message.mail.warning_input_address_need_to_forward;
		
		return '';
	},
	
	/**
	 * fill new information
	 */
	_fillNewInfo: function(object) {
		// account ID
		var __comp = Ext.getCmp(this.accountId);
		__comp.setValue(object.uid);
	
		var __domain = object.domain;
		var __component = Ext.getCmp(this.domainId);
		__component.setValue(__domain);
		
		// smtp
		var __comp = Ext.getCmp(this.smtpId);		
		__comp.setValue(object.smtp);
		
		// capacity
		var __comp = Ext.getCmp(this.capacityId);
		__comp.setValue(object.capacity);
		
		// active
		var __comp = Ext.getCmp(this.activeId);
		__comp.setValue(object.active);
		
		// forward
		var __comp = Ext.getCmp(this.forwardId);
		__comp.setValue(object.forward);
		
		if(object.forward) {
			var __comp = Ext.getCmp(this.fwAddressId);
			__comp.setValue(object.fAddress);
		}
		
		// virus
		var __comp = Ext.getCmp(this.virusId);
		__comp.setValue(object.virus);

		// spam
		var __comp = Ext.getCmp(this.spamId);
		__comp.setValue(object.spam);
	},
	
	/**
	 * fill data on GUI
	 */
	_fillData: function(object) {
		this.editable = this.editable==undefined?true:this.editable;
		
		// account ID
		var __comp = Ext.getCmp(this.accountId);
		__comp.setValue(object.uid);
		__comp.setDisabled(true);
	
		var __domain = object.domain;
		// add domain
		this._domainStore.add(new Ext.data.Record({
			domainId: __domain,
			domainName: __domain
		}));
		
		var __component = Ext.getCmp(this.domainId);
		__component.setValue(__domain);
		// disable domain combo
		__component.setDisabled(true);
		
		// smtp
		var __comp = Ext.getCmp(this.smtpId);		
		__comp.setValue(object.smtp);
		__comp.setDisabled(!this.editable);
		
		// capacity
		var __comp = Ext.getCmp(this.capacityId);
		__comp.setValue(object.capacity);
		__comp.setDisabled(!this.editable);
		
		// active
		var __comp = Ext.getCmp(this.activeId);
		__comp.setValue(object.active);
		__comp.setDisabled(!this.editable);
		
		// forward
		var __comp = Ext.getCmp(this.forwardId);
		__comp.setValue(object.forward);
		__comp.setDisabled(!this.editable);
		
		if(object.forward) {
			var __comp = Ext.getCmp(this.fwAddressId);
			__comp.setValue(object.fAddress);
			__comp.setDisabled(!this.editable);
		}
		
		// virus
		var __comp = Ext.getCmp(this.virusId);
		__comp.setValue(object.virus);
		__comp.setDisabled(!this.editable);

		// spam
		var __comp = Ext.getCmp(this.spamId);
		__comp.setValue(object.spam);
		__comp.setDisabled(!this.editable);
		
	},
	
	/**
	 * get data 
	 * @return {object}
	 */
	_getData: function(){
		// create object to store data.
		//var object = this._owner.account;
		var object = {};
		object.lname = this._owner.account.lname;
		object.fname = this._owner.account.fname;
		
		// password
		//var __comp = Ext.getCmp(this.passwordId);
//		object['pwd'] = this.__owner.pwd;
		
		// smtp
		__comp = Ext.getCmp(this.smtpId);
		object['smtp'] = __comp.getValue();
		
		// capacity
		__comp = Ext.getCmp(this.capacityId);
		object['capacity'] = __comp.getValue();
		
		// active
		__comp = Ext.getCmp(this.activeId);
		object['active'] = __comp.getValue();
		
		// forward
		__comp = Ext.getCmp(this.forwardId);
		var forward = __comp.getValue();
		object['forward'] = forward;
		
		if(forward) {
			__comp = Ext.getCmp(this.fwAddressId);
			object['fAddress'] = __comp.getValue();
		}
		
		// virus
		__comp = Ext.getCmp(this.virusId);
		object['virus'] = __comp.getValue();
		
		// spam
		__comp = Ext.getCmp(this.spamId);
		object['spam'] = __comp.getValue();
		
		// email
		__comp = Ext.getCmp(this.accountId);
		var __account = __comp.getValue().toLowerCase();
		__comp.setValue(__account);
		
		// domain
		__comp = Ext.getCmp(this.domainId);
		var __domain = __comp.getValue();
		object['email'] = __account + '@' +__domain ;
		if(__domain == this._owner.selectedDomain) {
			object['groups'] = this._owner.groups;
		}	
		
		return object;
	}
});
