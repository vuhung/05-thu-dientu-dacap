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
 * @class iNet.iwebos.ui.account.DialogChangePass
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateDomain
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.account.DialogResetPwd = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [{
        	region: 'center',
            columnWidth: 0.9,
            border: false,
            layout: 'form',
            anchor: '100%',
			labelWidth:60,
            items: [{
            	id: 'reset-label-uname',
            	xtype: 'label',            	
            	height: 20,
            	border: false,
                anchor: '97%'
            },{
            	id: 'reset-new-pwd',
				fieldLabel: iwebos.message.mail.create.password,
                xtype: 'textfield',
                inputType: 'password',
                border: false,
                anchor: '97%'
            },{
            	id: 'reset-confirm-pwd',
				fieldLabel: iwebos.message.mail.create.confirmpassword,
                xtype: 'textfield',
                inputType: 'password',
                border: false,
                anchor: '97%'
            }]
        }, {
            layout: 'fit',
            border: false,
            anchor: '100%',
            items: [{}]
        }]
		
    });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.account.DialogResetPwd.superclass.constructor.call(this, {
        id: 'mailadmin-dialog-changepass',
        title: iwebos.message.account.resetPwdDialogTitle,
        iconCls: 'icon-menu-account-change-pass',
        region: 'center',
        anchor: '100%',
        width: 375,
        height: 320,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
        buttons: [{
        	id: 'reset-pwd-save_id',
            text: iwebos.message.mail.create.save,
            iconCls: 'ok-btn',
            handler: this._fnOkHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
            handler: this._fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.account.DialogResetPwd, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.account.resetPwdDialogTitle,
    /**
     * bottom title.
     */
    btitle: iwebos.message.account.inputPwdDialogTitle,
	
	/**
	 * set data for dialog
	 * 
	 * @param {Object} uname - the user name
	 * @param {Object} action - the action
	 */
	/*setData: function(fname, code) {
		this._owner = {code: code};
		if(code != null && code != '') {
			Ext.getCmp('reset-label-uname').setText(iwebos.message.account.resetPwdText + ' \'' + fname + '\'');
			// create params
			var __params = {iwct:'loadAccountContent', iwa:'READ_WRITE',action: 'load'};
			__params.userCode = code;
			
			// handle when load data successfully
			var __fnSuccess = function(response, options) {
				var __data = eval('(' + response.responseText + ')')
				var __success = (__data.success == undefined)?true:__data.success;
				if(__success) {
					var uname = __data.uname;
					Ext.getCmp('reset-account-name').setValue(uname);	
					var component = Ext.getCmp('reset-new-pwd'); 
					component.setValue(uname);
					component.focus(true);
					Ext.getCmp('reset-confirm-pwd').setValue(uname);				
				} else {
					Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.mail.error_find_account_information,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
					Ext.getCmp('reset-pwd-save_id').setDisabled(true);		
							
				}
			};
				
			// load accoun from database
			iNet.Ajax.request({
				url: 'jsonajax',
				params: __params,
				method: 'POST',
				scope: this,
				success: __fnSuccess,
				failure: function(result, request){}
			});
		}
	},*/
    
    /**
     * set the data for dialog
     * @param {} fname
     * @param {} code
     */
    setData: function(org, fname, uname) {
    	this._owner = {uname: uname, org: org};
    	Ext.getCmp('reset-label-uname').setText(iwebos.message.account.resetPwdText + ' \'' + fname + '\'');
		var component = Ext.getCmp('reset-new-pwd');
		var __index = uname.indexOf('@');
		var __pwd = (__index>0)?uname.substr(0, __index):uname;
		component.setValue(__pwd);
		component.focus(true);
		Ext.getCmp('reset-confirm-pwd').setValue(__pwd);
    },
	
	/**
	 * Handle when click on OK button
	 */
	_fnOkHandler: function() {
		// handle when reset password
    	var __fnSuccess = function(response, results) {
    		var result = eval('(' + response.responseText + ')');
    		if(result.success) {
    			Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.account.resetPwdSuccessful,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
    		} else {
    			Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.account.resetPwdError,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
    		}
    	};
    	
    	var pwd = Ext.getCmp('reset-new-pwd').getValue();
    	if(pwd == '') {
    		Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.account.inputNewPwd,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
			return;				
    	} else if(pwd != Ext.getCmp('reset-confirm-pwd').getValue()) {
    		Ext.MessageBox.show({
								title : iwebos.message.account.infomationMessage,
								msg : iwebos.message.mail.error_input_confirm_pass,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.INFO
							});
			return;				
    	}
    	
    	// get data to submit on server
    	var object = {};
    	object['uname'] = this._owner.uname;
    	object['org'] = this._owner.org;
    	object['pwd'] = pwd;
    	
    	// create params
    	var __params = {iwct:'userContent', iwa:'READ_WRITE', action:'resetpwd'};
    	__params['object'] = Ext.util.JSON.encode(object);
    	// submit request
    	iNet.Ajax.request({
    		url: 'jsonajax',
    		params: __params,
    		method: 'POST',
    		success: __fnSuccess,
    		failure: function(result, request){}
    	});
    	
        // close the current dialog.
        this.destroy();
	},
	
    /**
     * handle cancel button.
     */
    _fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});
