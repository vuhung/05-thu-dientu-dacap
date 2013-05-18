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
 * @class iNet.iwebos.ui.mail.DialogSelectedAlias
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogSelectedAlias
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogSelectedAlias = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	
	var sortInfo = {
        field: 'email',
        direction: 'DESC'
    };
	var defaultSort = {
		field: 'email', 
		direction: "DESC"
	};

    var baseParams = {
        iwct: 'mailHeaderContent',
        iwm: 'MAIL',
        iwc: 'MAIL',
        iwa: 'view',
		id: 0,
        action: 'search'
    };
    
    // email record.
    var mailRecord = Ext.data.Record.create([
	{name: 'id', mapping: 'emailID', type: 'int'}, 
	{name: 'email', mapping: 'email', type: 'string'},
	{name: 'name', mapping: 'name', type: 'string'}
	]);
    
    var reader = new Ext.data.JsonReader({
        totalProperty: 'results',
        root: 'rows',
        id: 'id'
    }, mailRecord);
    

    var columnModel = new Ext.grid.ColumnModel([
		{
			header: iwebos.message.mail.create.username,
			dataIndex: 'email',
			fixed: true,
			width: 250
		},{
			header: iwebos.message.mail.create.firstname,
			dataIndex: 'name',
			fixed: true,
			width: 200
		}
	]);
    
	
    // create email message grid.
    this.emailAccountGrid = new iNet.iwebos.ui.common.grid.WebOSGrid({
        id: 'accountSearch',
        region: 'center',
        anchor: '100%',
        autoScroll: true,
		height:200,
		url: 'jsonajax',
        method: 'POST',
        sortInfo: sortInfo,
        baseParams: baseParams,
        reader: reader,
		dsort: defaultSort,
        cm: columnModel,
        filter: {
            data: 'all'
        }
    });
	
    this.ToolbarSearchAccount = new iNet.iwebos.ui.mail.ToolbarSearchAccount({
        region: 'north',
        width: '100%',
		autoHeight:true,
        frame: false,
        border: false,
        store: this.emailAccountGrid.store
    });

	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [this.ToolbarSearchAccount,  this.emailAccountGrid]
    });
    
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogSelectedAlias.superclass.constructor.call(this, {
        id: 'mail-dlgsearch-account',
        title: 'Tìm kiếm tài khoản',
        iconCls: 'icon-email-search-account',
        region: 'center',
        anchor: '100%',
        width: 500,
        height: 400,
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
Ext.extend(iNet.iwebos.ui.mail.DialogSelectedAlias, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    btitle: iwebos.message.mail.dialog_search_user_btitle,

    /**
     * handle ok button.
     */
    fnOkHandler: function(){
		var grid = Ext.getCmp("accountSearch");
        var selects = grid.getSelectionModel().getSelections();
        if (selects !== undefined) {
            for (var index = 0; index < selects.length; index++) {
                alert(selects[index].data.id);
            }
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
