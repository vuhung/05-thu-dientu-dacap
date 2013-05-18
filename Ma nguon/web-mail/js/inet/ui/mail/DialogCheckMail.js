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
 * @class MsgColumn
 */
iNet.iwebos.ui.mail.grid.MsgColumn = function(){
    Ext.apply(this, {
        width: 24,
        header: '<div class="flag-col-hd"></div>',
        menuDisabled: true,
        fixed: true,
        dataIndex: 'msg',
        renderer: function(value){
            switch (value) {
                case "LOADING":
                    return '<div class="grid-icon-msg-loading"></div>';
                case "DONE":
                    return '<div class="grid-icon-msg-done"></div>';
            }
        },
        init: function(grid){
        }
    });
};


/**
 * @class iNet.iwebos.ui.mail.DialogCheckMail
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCheckMail
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogCheckMail = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    
    // record.
    var mailRecord = Ext.data.Record.create([
		{name: 'id', mapping: 'id', type: 'string'},
		{name: 'msg', mapping: 'msg', type: 'string'},
		{name: 'account', mapping: 'account', type: 'string'},
		{name: 'server', mapping: 'server', type: 'string'}, 
		{name: 'newemail', mapping: 'newemail', type: 'int'}, 
		{name: 'oldemail', mapping: 'oldemail', type: 'int'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    
    // create column model.
    var columnModel = new Ext.grid.ColumnModel([new iNet.iwebos.ui.mail.grid.MsgColumn, {
      header: iwebos.message.mail.account_name,
      dataIndex: 'account',
      menuDisabled: true,
      width: 0.25
    }, {
      header: iwebos.message.mail.server,
      dataIndex: 'server',
      menuDisabled: true,
      width: 0.25
    }, {
      header: iwebos.message.mail.new_mail,
      dataIndex: 'newemail',
      menuDisabled: true,
      width: 0.25
    }, {
      header: iwebos.message.mail.old_mail,
      dataIndex: 'oldemail',
      menuDisabled: true,
      width: 0.2
    }]);
	
	 // create check mail grid
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'mail-grid-checkmail',
        region: 'center',
        anchor: '100%',
        autoScroll: true,
        url: 'test.txt',
        method: 'POST',
        reader: reader,
        cm: columnModel,
        filter: {
            data: 'all'
        }
    });
	
    var __panelDialogCheckMail = new Ext.Panel({
        region: 'center',
        collapsible: false,
        border: false,
		enableTabScroll: true,
		autoScroll:true,
        width: '100%',
        layout: 'column',
        items: [this.grid]
    });
	
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogCheckMail.superclass.constructor.call(this, {
        id: 'mail-dialog-checkmail',
        title: iwebos.message.mail.check_mail,
        iconCls: '',
        region: 'center',
        anchor: '100%',
        width: 600,
        height: 450,
        modal: true,
        frame: true,
        resizable: false,
        layout: 'border',
        hideMode: 'offsets',
        items: [{
	            region: 'center',	            
	            frame: true,	            
	            width: '100%',
	            items: __panelDialogCheckMail}],
        buttons: [{
            text: 'Đồng ý',
            iconCls: 'ok-btn',
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogCheckMail, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.configure_account,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.setup_properties_for_current_account,
    
    /**
     * set data for dialog
     *
     * @param {Object} id - the id of domain name
     * @param {Object} action - the action
     */
    setData: function(object){
		// set server
    	var __comp = Ext.getCmp(this.server);
    	__comp.setValue(object.server);
    	
    	// set protocol
    	var __comp = Ext.getCmp(this.protocol);
    	__comp.setValue(object.protocol);
    	
    	// set port
    	var __comp = Ext.getCmp(this.port);
    	__comp.setValue(object.port);
    	
    	// set security
    	var __comp = Ext.getCmp(this.security);
    	__comp.setValue(object.security);
    	
    	// set user
    	var __comp = Ext.getCmp(this.user);
    	__comp.setValue(object.user);
    	
    	// set pass
    	var __comp = Ext.getCmp(this.pass);
    	__comp.setValue(object.pass);
    	
    	// set leave
    	var __comp = Ext.getCmp(this.leave);
    	__comp.setValue(object.leave);
    },
    
    /**
     * load domain data
     *
     * @param {Object} id - the domain name
     */
    loadData: function(account){
        var __account = account || {};
        this.setData(__account);        
    },
    
    /**
     * return the data object
     */
    getData: function(){
        // create object to save data.
        var __object = {};
        // set server
    	var __comp = Ext.getCmp(this.server);
    	__object.server = __comp.getValue();
    	
    	// set protocol
    	var __comp = Ext.getCmp(this.protocol);
    	__object.protocol = __comp.getValue();
    	
    	// set port
    	var __comp = Ext.getCmp(this.port);
    	__object.port = __comp.getValue();
    	
    	// set security
    	var __comp = Ext.getCmp(this.security);
    	__object.security = __comp.getValue();
    	
    	// set user
    	var __comp = Ext.getCmp(this.user);
    	__object.user = __comp.getValue();
    	
    	// set pass
    	var __comp = Ext.getCmp(this.pass);
    	__object.pass = __comp.getValue();
    	
    	// set leave
    	var __comp = Ext.getCmp(this.leave);
    	__object.leave = __comp.getValue();
    	
    	return __object;
    },
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});
