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
 * @class iNet.iwebos.ui.account.DialogAddAccount
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogAddAccount
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.account.DialogAddAccount = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
	var sortInfo = {
        field: 'lname',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'lname', 
		direction: "ASC"
	};

    // email record.
    var mailRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'id', type: 'string'}, 
	{name: 'lname', mapping: 'lname', type: 'string'},
	{name: 'mail', mapping: 'mail', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: 'Tên',
			dataIndex: 'lname',
			fixed: true,
			width: 250
		},{
			header: 'Mail',
			dataIndex: 'mail',
			fixed: true,
			width: 200
		}
	]);
    
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'accountSearch',
        region: 'center',
        anchor: '100%',
        autoScroll: true,
		height:220,
		url: 'account.txt',
        method: 'POST',
        sortInfo: sortInfo,
        reader: reader,
		dsort: defaultSort,
        cm: columnModel,
        filter: {
            data: 'all'
        }
    });
	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [this.grid]
    });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.account.DialogAddAccount.superclass.constructor.call(this, {
        id: 'mail-dlgsearch-account',
        title: iwebos.message.mail.select_account,
        iconCls: 'icon-email-search',
        region: 'center',
        anchor: '100%',
        width: 500,
        height: 450,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
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
Ext.extend(iNet.iwebos.ui.account.DialogAddAccount, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.dialog_select_account_title,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.dialog_select_account_btitle,
	
	/**
	 * set the action
	 * 
	 * @param {Object} action
	 */
	__setAction: function(action) {
		this.__action = action;
	},

    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
});
