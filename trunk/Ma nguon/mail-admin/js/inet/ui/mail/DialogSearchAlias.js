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
 * @class iNet.iwebos.ui.mail.DialogSearchAlias
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogSearchAlias
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogSearchAlias = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
	var sortInfo = {
        field: 'email',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'email', 
		direction: "ASC"
	};

	var baseParams = {
        iwct: 'searchMailAccountContent',
        iwa: 'view',
		id: 0,
        action: 'search'
    };   
    
    // email record.
    var mailRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'email', type: 'string'}, 
	{name: 'email', mapping: 'email', type: 'string'},
	{name: 'displayname', mapping: 'displayname', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.account,
			dataIndex: 'email',
			fixed: true,
			width: 250
		},{
			header: iwebos.message.mail.name,
			dataIndex: 'displayname',
			fixed: true,
			width: 330
		}
	]);
    
	
    // create email message grid.
    this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'emailAliasSearchGrid',
        region: 'center',
        anchor: '100%',
        autoScroll: true,        
		height: 270,
		url: 'jsonajax',
        method: 'POST',
        sortInfo: sortInfo,
        loadFirst: false,
        baseParams: baseParams,
        reader: reader,
		dsort: defaultSort,
        cm: columnModel,
        filter: {
            data: 'all'
        }
    });
    this.toolbarSearchAccount = new iNet.iwebos.ui.mail.AliasMailToolbar({
        region: 'north',
        width: '100%',
		autoHeight:true,
        frame: false,
        border: false,
        store: this.grid.store,
        selDomain: this.domain
    });

    this.toolbarSearchAccount.on('search', this.grid.handleSearchEvent, this.grid, {stopEvent: true});
    
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [this.toolbarSearchAccount,  this.grid]
    });
    
    this.grid.on('keypress', this.__handleKeyEvent, this, {stopEvent:true});
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogSearchAlias.superclass.constructor.call(this, {
        id: 'mail-dlgsearch-alias',
        title: iwebos.message.mail.search_alias,
        iconCls: 'icon-email-search',
        region: 'center',
        anchor: '100%',
        width: 630,
        height: 520,
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
Ext.extend(iNet.iwebos.ui.mail.DialogSearchAlias, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.search_alias,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.select_mail_account,   
    
    /**
	 * Handle key event on grid
	 * 
	 * @param {} event
	 */
	__handleKeyEvent: function(event) {		
		LotusService.handleKeyEventOnGrid(this.grid, event);
	},
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    
    getData: function(){
    	var __grid = Ext.getCmp("emailAliasSearchGrid");
        var __selects = __grid.getSelectionModel().getSelections();
        var __data =  [] ;
        if (__selects !== undefined) {
            for (var index = 0; index < __selects.length; index++) {
                __data[index] = __selects[index].data;
            }
        }
        return __data;
    }
});
