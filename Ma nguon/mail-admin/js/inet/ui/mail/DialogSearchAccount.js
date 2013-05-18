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
 * @class iNet.iwebos.ui.mail.DialogSearchAccount
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateDomain
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogSearchAccount = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
	var sortInfo = {
        field: 'uname',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'uname', 
		direction: "ASC"
	};

    var baseParams = {
        iwct: 'searchAccountContent',       
        iwa: 'view',
		id: 0,
        action: 'search'
    };
    
    // email record.
    var mailRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'userCode', type: 'string'}, 
	{name: 'uname', mapping: 'uname', type: 'string'},
	{name: 'lname', mapping: 'lname', type: 'string'},
	{name: 'mname', mapping: 'mname', type: 'string'},
	{name: 'fname', mapping: 'fname', type: 'string'},
	{name: 'pwd', mapping: 'pwd', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.account,
			dataIndex: 'uname',
			fixed: true,
			width: 150
		},{
			header: 'Họ',
			dataIndex: 'lname',
			fixed: true,
			width: 120
		},{
			header: 'Tên lót',
			dataIndex: 'mname',
			fixed: true,
			width: 150
		},{
			header: iwebos.message.mail.name,
			dataIndex: 'fname',
			fixed: true,
			width: 150
		}
	]);
    
	baseParams['first'] = true;
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'accountSearch',
        region: 'center',
        anchor: '100%',
        autoScroll: true,
		height:250,
		url: 'jsonajax',
        method: 'POST',
        sortInfo: sortInfo,
        baseParams: baseParams,
        loadFirst: false,
        reader: reader,
		dsort: defaultSort,
        cm: columnModel,
        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        filter: {
            data: 'all'
        }
    });
	
    this.search = new iNet.iwebos.ui.mail.ToolbarSearchAccount({
        region: 'north',
        width: '100%',
		autoHeight:true,
        frame: false,
        border: false,
        store: this.grid.store
    });
	
    this.search.setLimitSearchCharacter(3);
	this.search.toolbarSeach.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});

	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [this.search,  this.grid]
    });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogSearchAccount.superclass.constructor.call(this, {
        id: 'mail-dlgsearch-account',
        title: iwebos.message.mail.search_account,
        iconCls: 'icon-email-search',
        region: 'center',
        anchor: '100%',
        width: 600,
        height: 500,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
        buttons: [{
        	id: 'dialog-search-account-ok-id',
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
Ext.extend(iNet.iwebos.ui.mail.DialogSearchAccount, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.search_account,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.search_account,
	
	/**
	 * set the action
	 * 
	 * @param {Object} action
	 */
	__setAction: function(action) {
		this.__action = action;
	},
	
	/**
	 * disabled ok button
	 */
	disabledOkButton: function() {
		Ext.getCmp('dialog-search-account-ok-id').setDisabled(true);
	},
	
	/**
	 * disable delete check
	 */
	disabledDelChk: function() {
		Ext.getCmp('toolbar-search-account-deleted-id').setDisabled(true);
	},
	
	/**
	 * Set enabled and default value for deleting check 
	 * @param {} enabled
	 * @param {} check
	 */
	setEnableDelChk: function(enabled, check) {
		var __component = Ext.getCmp('toolbar-search-account-deleted-id');
		if(enabled != undefined) {
			__component.setDisabled(!enabled);
		}
		if(check != undefined) {
			__component.setValue(check);
		}
	},

    /**
     * handle ok button.
     */
    fnOkHandler: function(){
		var grid = Ext.getCmp("accountSearch");
        var selects = grid.getSelectionModel().getSelections();
        if (selects !== undefined && selects.length != 0) {			
			// get first selection
			var firstSelection = selects[0].data;
            if(this.__action.scope) {
				this.__action.fn.apply(this.__action.scope, [firstSelection]);
			}
			this.destroy();
        } else {
        	Ext.MessageBox.show({
								title : iwebos.message.mail.error,
								msg : iwebos.message.mail.error_select_account,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.ERROR
							});
		}
    },
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
});
