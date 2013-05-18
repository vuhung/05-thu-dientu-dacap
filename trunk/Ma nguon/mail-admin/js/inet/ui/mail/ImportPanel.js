/*****************************************************************
   Copyright 2009 by Tan Truong(tntan@inetcloud.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/

iNet.iwebos.ui.mail.ImportPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	// The ID of components
	this.prefix = (!this.prefix) ? 'import-mail-acc' : this.prefix;
	this.tabId = this.prefix + '-tab';
	this.exportButtonId = this.prefix + '-export-button-id';
	this.exportExcelButtonId = this.prefix + '-export-excel-button-id';; 
	this.gridId = this.prefix + '-grid-id';
	this.statisticPanelId = this.prefix + '-statistic-view-id';
	this.key = '';
	this.org = config.org;
	
	var tplMarkup=[
		'<div style="font-size:13px;font-weight:bold;text-align:left;">','Kết nhập dữ liệu: {total} tài khoản','</div>',
		'<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-left:15px;">',
			'<tr>',
				'<td align="left" width="110px">', "Tài khoản mới",':</td>','<td align="left">{newuser}</td>',
				'<td align="left" width="220px">', "Tài khoản đã tồn tại người dùng",':</td>','<td align="left">{existuser}</td>',
			'</tr>',
			'<tr>',
				'<td align="left" width="110px">',"Tài khoản lỗi",':</td>','<td align="left">{error}</td>',
				'<td align="left" width="220px">',"Tài khoản đã tồn tại",':</td>','<td align="left">{exist}</td>',
			'</tr>',
			'<tr>',
				'<td align="left" width="110px">',"Tài khoản bị trùng",':</td>','<td align="left">{duplicate}</td>',
				'<td align="left" width="220px"></td><td align="left"></td>',
		'	</tr>',
		'</table>',
		'<br>',
		'<div style="font-size:13px;font-weight:bold;text-align:left;">','Tài khoản bị trùng,bị lỗi','</div>'
	];
	
	this.template = new Ext.XTemplate(tplMarkup.join(''),{});
	this.data = {total: 0, existuser: 0, newuser: 0, exist: 0, error: 0, duplicate: 0};
	var html=this.template.apply(this.data);
	
	var __statisticView = new Ext.Panel({
        id: this.statisticPanelId,
		region: 'center',
		hidden:false,
		border: false,
		height:110,
		html: html
    });
	
	var __headPanel = new Ext.Panel({
		region:'north',
		anchor: '100%',
		border: false,
		frame: true,
		hideBorders:true,
		labelWidth: 150,
		height: 110,
		layout: 'border',
		items:[__statisticView]
	});
	
	var sortInfo = {
        field: 'number',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'number', 
		direction: 'ASC'
	};
	
	// The grid record
	var __record = Ext.data.Record.create([
		{name:'number', mapping:'number', type:'string'},
		{name:'fullname', mapping:'fullname', type:'string'},
		{name:'id', mapping:'id', type:'string'},
		{name:'status', mapping:'status', type:'int'},
		{name:'duplicate', mapping:'duplicate', type:'string'}
	]);
	
	// the reader
	var __reader = new Ext.data.JsonReader({
		id: 'STT'
	}, __record);
	
	var __columnModel = new Ext.grid.ColumnModel([
		{header: "STT", dataIndex:'number', sortable: true, width: 50, fixed: true},
		{header: "Họ và tên", dataIndex:'fullname', sortable: true, width: 300},
		{header: "Email", dataIndex:'id', width: 200, sortable: true, fixed: true},
		{header: "Tình trạng", dataIndex:'status', width: 200, sortable: true, fixed: true,
			renderer: function(value){
				if(value == 1){
					return "Tồn tại người dùng";
				}else if(value == 2){
					return "Tồn tại người dùng và email";
				}else if(value == 3){
					return "Không kết nhập được"
				}else if(value == 4){
					return "Trùng lặp tài khoản"
				}
			}
		},
		{header: "Tài khoản trùng với", dataIndex:'duplicate', width: 250, sortable: true, fixed: true}
	]);
	
	// The grid contain users
	this.grid = new iNet.iwebos.ui.common.grid.WebOSGrid({
		id: this.gridId,
		region: 'center',
		autoScroll: true,
		url: 'jsonajax',
		method: 'POST',		
		loadFirst: false,
		reader: __reader,
		sortInfo: sortInfo,
		dsort: defaultSort,		
		cm: __columnModel,
		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        filter: {
            data: 'all'
        },
        tbar:[{
			id: this.exportExcelButtonId,
		 	xtype: 'button',
			iconCls: 'icon-menu-excel',
			text: 'Kết xuất dữ liệu',
			tooltip: 'Kết xuất dữ liệu ra tập tin excel',
			scope: this,
			disabled: true,
			handler:this.onExportEmail.createDelegate(this)
		}]
	});
	 //when row is right clicked
    this.grid.on('rowcontextmenu', this._onRowContextMenu, this, {stopEvent:true});
    this.grid.on('contextmenu', this._onContextMenu, this, {stopEvent:true}) ;
    this.grid.store.on('load', function(store){
    	if(store.getCount()>0){
    		Ext.getCmp(this.exportExcelButtonId).setDisabled(false);
    	}else{
    		Ext.getCmp(this.exportExcelButtonId).setDisabled(true);
    	}
    }, this, {stopEvent:true});

	iNet.iwebos.ui.mail.ReportPanel.superclass.constructor.call(this, {
        id: this.tabId,
        iconCls: 'icon-menu-import',
        title: "Kết nhập tài khoản email tên miền - " + this.org ,
        header: false,
        frame: false,
        enableTabScroll: true,
		autoScroll : true,
        border: false,
        closable: true,
        layout: 'border',
        items: [__headPanel,this.grid],
        tbar:[{
		 	xtype: 'button',
			iconCls: 'icon-menu-import',
			text: 'Kết nhập dữ liệu',
			tooltip: 'Kết nhập dữ liệu từ tập tin excel',
			scope: this,
			handler:this.fnUploadData.createDelegate(this)
		},'-',{
		 	xtype: 'button',
			iconCls: 'icon-menu-download',
			text: 'Tải mẫu',
			tooltip: 'Tải mẫu danh sách thông tin người dùng cần kết nhập',
			scope: this,
			handler:this.onDownload.createDelegate(this)
		}]
    });
};
Ext.extend(iNet.iwebos.ui.mail.ImportPanel, Ext.Panel, {
	/**
	 * Row context
	 * @param {} grid
	 * @param {} index
	 * @param {} e
	 */
	_onRowContextMenu: function(grid, index, e){
		// stop this event.
		e.stopEvent() ;
	},
	
	/**
	 * @param {} event
	 */
	_onContextMenu: function(event){
    	// does not allow any control to handle this menu.
		event.stopEvent() ;
	},
	
	/**
     * Resize panel
     */
    __fnResizePanel: function(){
        Ext.EventManager.fireWindowResize();
    },
    
    
    /**
     * @param 
     */
    _fnComputeData: function(data, limit) {
    	var error = 0, exist = 0, existuser = 0, duplicate = 0;
    	var value;
    	for(var i = 0; i< data.length;i++){
    		value = data[i].status;
    		if(value == 1){
				existuser++;
			}else if(value == 2){
				exist++;
			}else if(value == 3){
				error++;
			}else if(value == 4){
				duplicate++;
			}
    	}
    	this.data.existuser = this.data.existuser + existuser;
    	this.data.exist = this.data.exist + exist;
    	this.data.error = this.data.error + error;
    	this.data.duplicate = this.data.duplicate + duplicate;
    	this.data.newuser = this.data.newuser + (limit - error - exist - existuser - duplicate);
    },
	
	
	/**
	 * Export data email to excel file
	 */
	fnExportData: function() {
		var __selections = this.grid.getSelectionModel().getSelections();
		if(__selections == null || __selections.length == 0) {
			Ext.MessageBox.show({
				title: iwebos.message.mail.error,
				msg: iwebos.message.mail.report_select_domain,
				buttons: Ext.MessageBox.OK,
				icon: Ext.MessageBox.ERROR
			});
			return;
		}
		
		// get the selected domain
		var __form = document.getElementById('viewdownload_') ;
		__form.action = 'export-email-file' ;
		__form.id.value = __selection['text'] ;
		__form.target = 'ViewDocument' ;
		__form.bean.value = 'exportEmailBean' ;
		
		// submit form.
		__form.submit() ;
	},
	
	/**
	 * Upload data to server
	 */
	fnUploadData: function() {
		if(!this.udialog){
			this.udialog = new Ext.ux.UploadDialog.Dialog({
				url:'mail-upload-file',
				reset_on_hide:false,
				allow_close_on_upload:true,
				upload_autostart:false,
				permitted_extensions: ['xls'],
				post_var_name:'upload'
			}) ;
			
			// handle when uload.
			this.udialog.on('uploadsuccess', this.onUploadSuccess, this) ;
		}
		
		// show dialog.
		this.udialog.show(this) ; 
	},
	
	/**
	 * handle upload successful. 
	 */
	onUploadSuccess : function(dialog, filename, response, record){
		if(response.success){ // upload success.
			// get the file.
			var __files = response.files ;
			var extension = '' ;
			var __file = null, object= null ;
			for(var index = 0; index < __files.length; index++){
				__file = __files[index] ;
				this.data = {total: 0, existuser: 0, newuser: 0, exist: 0, error:0, duplicate: 0};
				this.grid.store.removeAll();
				this._fnReadData(__file.file);
			}
			
			this.udialog.hide();
		}
	},
	
	/**
	 * 
	 */
	_fnReadData: function(key){
		var __store = this.grid.store;
    	var onSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
			var __success = (__data.success==undefined)?true:__data.success;
			if(__success) {
				this.data.total = __data.total;
				var __statisticPanel = Ext.getCmp(this.statisticPanelId);
				this.template.overwrite(__statisticPanel.body, this.data);
				this._fnImportData(__data.key, 0, 5, __data.total);
				this.key = __data.key;
			}else{
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : 'Nội dung tập tin kết nhập không đúng cú pháp.',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
    	}
    	
    	// the base parameter for searching
		var __baseParam = {iwct: 'mailImportContent',
							iwa: 'write',
							action: 'read',
							key: key,
							org: this.org};		
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParam,
			method: 'POST',
			scope: this,
			success: onSuccess,
			failure: function(result, request){
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : 'Nội dung tập tin kết nhập không đúng cú pháp.',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			},
			maskEl : this.bwrap
		});
	},
	
	/**
	 * 
	 */
	_fnImportData: function(key, start, limit, total){
		if(start >= total ){
			// show error
			return;
		}
    	var onSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
			var __success = (__data.success==undefined)?true:__data.success;
			if(__success) {
				this.grid.store.loadData(__data.rows, true);
				this._fnComputeData(__data.rows, limit); 
				var __statisticPanel = Ext.getCmp(this.statisticPanelId);
				this.template.overwrite(__statisticPanel.body, this.data);
				var __start = start + limit;
				var __limit = ((__start + limit) > total)?(total - __start): limit;
				this._fnImportData(key, __start , __limit, total)
			}else{
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : 'Quá trình kết nhập không thành công.',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
    	}
    	
    	// the base parameter for searching
		var __baseParam = {iwct: 'mailImportContent',
						iwa: 'write',
						action: 'import',
						key: key,
						start: start,
						limit: limit};		
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParam,
			method: 'POST',
			scope: this,
			success: onSuccess,
			failure: function(result, request){
				Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : 'Quá trình kết nhập không thành công.',
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			},
			maskEl : this.bwrap
		});
	},
	
	/**
	 * Export address book
	 */
	onExportEmail: function(){
		// get the first selection
		var __form = document.getElementById('viewdownload_') ;
		__form.action = 'export-error-account' ;
		__form.id.value = this.key ;
		__form.target = 'ViewDocument' ;
		__form.bean.value = 'exportErrorBean' ;
		
		// submit form.
		__form.submit() ;
	},
	
	onDownload: function(){
		location.href = '/data/import_template.xls';
	}
});