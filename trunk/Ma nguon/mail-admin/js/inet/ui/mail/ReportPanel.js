/*****************************************************************
   Copyright 2009 by Duyen Tang (tttduyen@truthinet.com)

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

/**
 * Print data on page
 */
function printDMReport() {
	Ext.getCmp('report-mail-acc-tab').printDomainReport();
}

iNet.iwebos.ui.mail.ReportPanel = function(viewer, config) {
	this.viewer = viewer;
	Ext.apply(this, config);
	
	// The ID of components
	this.prefix = (!this.prefix) ? 'report-mail-acc' : this.prefix;
	this.tabId = this.prefix + '-tab';
	this.exportButtonId = this.prefix + '-export-button-id';
	this.exportExcelButtonId = this.prefix + '-export-excel-button-id';
	this.gridId = this.prefix + '-grid-id';
	this.statisticPanelId = this.prefix + '-statistic-view-id';
	this.pbar = null ;
	var tplMarkup=[
		'<div style="font-size:13px;font-weight:bold;text-align:left;">',iwebos.message.mail.report_statistic_common_title,'</div>',
		'<table width="100%" border="0" cellspacing="0" cellpadding="0">',
			'<tr>',
				'<td align="left" width="175px">',iwebos.message.mail.report_total_domain,':</td>','<td align="left">{domain}</td>',
				'<td align="left" valign="center" width="175px" colspam="2"><img class="icon-printer" src="./images/s.gif"/>&nbsp;<a href="#" onclick="javascript:printDMReport()">',iwebos.message.mail.report_print,'</a></td>',
			'</tr>',
			'<tr>',
				'<td align="left" width="175px">',iwebos.message.mail.report_total_email,':</td>','<td align="left">{emailnumber}</td>',
				'<td align="left" width="175px">',iwebos.message.mail.report_total_alias,':</td>','<td align="left">{aliasnumber}</td>',
			'</tr>',
		'</table>',
		'<br>',
		'<div style="font-size:13px;font-weight:bold;text-align:left;">',iwebos.message.mail.report_statistic_domain_title,'</div>'
	];
	
	this.template = new Ext.XTemplate(tplMarkup.join(''),{});
//	var html=template.apply(data);
	var data = {domain: '', emailnumber: '', aliasnumber: ''};
	var html=this.template.apply(data);
	
	var __statisticView = new Ext.Panel({
        id: this.statisticPanelId,
		region: 'center',
		hidden:false,
		border: false,
		height:80,
		html: html
    });
	
	var __headPanel = new Ext.Panel({
		region:'north',
		anchor: '100%',
		border: false,
		hideBorders:true,
		//frame: true,
		labelWidth: 150,
		height: 80,
		layout: 'border',
		items:[__statisticView]
	});
	
	var sortInfo = {
        field: 'text',
        direction: 'ASC'
    };
	var defaultSort = {
		field: 'text', 
		direction: 'ASC'
	};
	
	// The grid record
	var __record = Ext.data.Record.create([
		{name:'text', mapping:'text', type:'string'},
		{name:'desc', mapping:'desc', type:'string'},
		{name:'tmail', mapping:'tmail', type:'int'},
		{name:'talias', mapping:'talias', type:'int'}
	]);
	
	// the reader
	var __reader = new Ext.data.JsonReader({
		id: 'text'
	}, __record);
	
	var __columnModel = new Ext.grid.ColumnModel([
		{header: iwebos.message.mail.report_domain, dataIndex:'text', sortable: true, width: 250, fixed: true},
		{header: iwebos.message.mail.report_domain_desc, dataIndex:'desc', sortable: true, width: 300},
		{header: iwebos.message.mail.report_email, dataIndex:'tmail', width: 140, sortable: true, fixed: true},
		{header: iwebos.message.mail.report_alias, dataIndex:'talias', width: 140, sortable: true, fixed: true}
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
			id: this.exportButtonId,
		 	xtype: 'button',
			iconCls: 'icon-menu-report-preview',
			text: iwebos.message.mail.report_detail_button,
			disabled: true,
			scope: this,
			handler:this.fnLoadData.createDelegate(this)
		},{
			id: this.exportExcelButtonId,
		 	xtype: 'button',
			iconCls: 'icon-menu-excel',
			text: 'Kết xuất dữ liệu',
			tooltip: 'Kết xuất dữ liệu ra tập tin excel',
			disabled: true,
			scope: this,
			handler:this.fnExportData.createDelegate(this)
		}]
	});
	 //when row is right clicked
    this.grid.on('rowcontextmenu', this._onRowContextMenu, this, {stopEvent:true});
    this.grid.on('contextmenu', this._onContextMenu, this, {stopEvent:true}) ;
    //when a row is double clicked
    this.grid.on('rowdblclick', this.fnLoadData, this);
    this.grid.selModel.on("selectionchange", function(sm){
    	var selects = sm.getSelections();
    	if(selects.length == 1){
    		Ext.getCmp(this.exportButtonId).enable();
    		Ext.getCmp(this.exportExcelButtonId).enable();
    	}else{
    		Ext.getCmp(this.exportButtonId).disable();
    		Ext.getCmp(this.exportExcelButtonId).disable();
    	}
    },this);
	var __main = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        layout: 'border',
		bodyStyle: 'padding: 0px;',
		items:[__headPanel,this.grid]
	});
	
	__main.on('resize', this.__fnResizePanel, this);
	
	iNet.iwebos.ui.mail.ReportPanel.superclass.constructor.call(this, {
        id: this.tabId,
        iconCls: 'icon-menu-report',
        title: iwebos.message.mail.report_statistic_title,
        header: false,
        frame: false,
        enableTabScroll: true,
		autoScroll : true,
        border: false,
        closable: true,
        layout: 'border',
        items: [__main]
    });
    this.__fnLoad();
};
Ext.extend(iNet.iwebos.ui.mail.ReportPanel, Ext.Panel, {
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
     * Statistic data on domain
     */
    __fnLoad: function() {
    	var __loadStatistic = {fn:this.__fnStatistic, scope: this};
    	// handle when load domain success 
    	var __fnLoadDomainSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
			var __success = (__data.success==undefined)?true:__data.success;
			if(__success) {
				if(__data.length == 0) {
					// Show error
				} else {
					__loadStatistic.fn.apply(__loadStatistic.scope, [__data, 0]);
				}
			}
    	};
    	
    	var __loadDomainParams = {iwct:'loadMailDomainContent', iwa:'view', action:'loadAll'};
    	iNet.Ajax.request({
			url: 'jsonajax',
			params: __loadDomainParams,
			method: 'POST',
			success: __fnLoadDomainSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap
		});	
    },
    
    /**
     * Loading statistic information
     * 
     * @param {} domains - list of domian
     * @param {} start - start point for searching
     */
    __fnStatistic: function(domains, start) {
    	var __store = this.grid.store;
    	var __loadStatistic = {fn:this.__fnStatistic, scope: this};
    	var __computeData = {fn: this.__computeData, scope: this};
    	// handle when loading statistic information on domain
    	var __fnLoadStatisticSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
			var __success = (__data.success==undefined)?true:__data.success;
			if(__data.length > 0) {
				var loadMask = new Ext.LoadMask(__computeData.scope.bwrap, {
					msg: Ext.LoadMask.prototype.msg
				});
				// show mask
				loadMask.show();
				// append data to grid
				__store.loadData(__data, true);
				// hide mask
				loadMask.hide();
				// get new starting point
				start = start + iNet.INET_REPORT_DOMAIN_LIMIT;
				if(start < domains.length) {
					// continue loading
					__loadStatistic.fn.apply(__loadStatistic.scope, [domains, start]);
				} else {
					__computeData.fn.apply(__computeData.scope, []);
					__store.sort('text', 'ASC');
				}
			}
    	};
    	
    	// the base parameter for searching
		var __baseParam = {iwct: 'mailDomainContent',iwa: 'view', action: 'report'};		
		var __key = '';
		for(var __index = 0; __index < iNet.INET_REPORT_DOMAIN_LIMIT && (start + __index) < domains.length; __index++) {
			__key = __key + domains[start + __index].id + ';';
		}
		__key.substr(0, __key.length - 1);
		// add domain to request
		__baseParam['domains'] = __key;
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParam,
			method: 'POST',
			success: __fnLoadStatisticSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap
		});	
    	
    },
    
    /**
     * Compute data
     */
    __computeData: function() {
    	// get the total of domain
    	var __numberDomain = this.grid.store.getCount();
    	var __numberEmail = 0;
    	var __numberAlias = 0;
    	var __record;
    	for(var __index = 0; __index < __numberDomain; __index++) {
    		// get record at given index
    		__record = this.grid.store.getAt(__index);
    		__numberEmail = __numberEmail + __record.data.tmail;
    		__numberAlias = __numberAlias + __record.data.talias;
    	}
    	// create data to fill to template
    	var __data = {domain: __numberDomain, emailnumber: __numberEmail, aliasnumber: __numberAlias};
    	var __statisticPanel = Ext.getCmp(this.statisticPanelId);
    	this.template.overwrite(__statisticPanel.body, __data);
    },
	
	/**
	 * Load data for report
	 */
	fnLoadData: function() {
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
		
		// get first selection
		var __selection = __selections[0].data;
		var __viewData = {};
		// transfer data
		__viewData['domain'] = __selection['text'];
		__viewData['desc'] = __selection['desc'];
		__viewData['emailnumber'] = __selection['tmail'];
		__viewData['aliasnumber'] = __selection['talias'];
		
		/**
		 * create store to sort data 
		 */
		// email store
		var __emailStore = new Ext.data.JsonStore({			
			fields: [{name:'email', mapping:'email', type:'string'},
					 {name:'displayname', mapping:'displayname', type:'string'},
					 {name:'capicity', mapping:'capacity', type:'int'}]
		});
		
		// alias store
		var __aliasStore = new Ext.data.JsonStore({			
			fields: [{name:'email', mapping:'email', type:'string'},
					 {name:'displayname', mapping:'displayname', type:'string'}]
		});
		__viewData['emailStore'] = __emailStore;
		__viewData['aliasStore'] = __aliasStore;
		
		var __limit = iNet.INET_REPORT_ACCOUNT_LIMIT;		
		var __count = 0;
		
		// The email params
		var __emailParams = {iwct: 'reportMailAccountContent', iwa:'view', action: 'search', key:''};
		__emailParams['domain'] = __viewData['domain'];		
		__emailParams['limit'] = __limit;
		
		// The alias params
		var __aliasParams = {iwct: 'reportMailAliasAjaxContent', iwa:'view', action: 'search', key:''};
		__aliasParams['domain'] = __viewData['domain'];
		__aliasParams['limit'] = __limit;
						
		// Load data
		this.__fnLoadMailAcc(__emailParams, __aliasParams, __count, __viewData);
	},
	
	
	/**
	 * Export data email to excel file
	 */
	fnExportData: function(key) {
		var __selected = this.grid.getSelectionModel().getSelected();
		// handle when load domain success 
    	var onSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
			if(!!__data && !!__data.success) {
				this.fnPrepareData(__data.key);
			}else{
				
			}
    	};
    	
    	var __params = {iwct:'mailExportContent', 
    							iwa:'view',
    							action:'read',
    							id: __selected.data.text};
    	iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			method: 'POST',
			success: onSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap,
			scope: this
		});
	},
	
	fnPrepareData: function(key){
		// handle when load domain success 
    	var onSuccess = function(response, options) {
    		var __data = Ext.util.JSON.decode(response.responseText);
    		if(!!__data && __data.success != false) {
    			if(!!__data.COMPLETE){
    				// down load
    				this.fnDownloadExportData(key)
    			}else{
    				this.fnPrepareData(key);
    			}
			}else{
				Ext.MessageBox.show({
					title: iwebos.message.mail.error,
					msg: 'Có lỗi xảy ra trong quá trình kết xuất tài khoản email.',
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
			}
    	};
    	
    	var __params = {iwct:'mailExportContent', 
    							iwa:'view',
    							action:'export',
    							key: key};
    	iNet.Ajax.request({
			url: 'jsonajax',
			params: __params,
			method: 'POST',
			success: onSuccess,
			failure: function(result, request){},
			maskEl : this.bwrap,
			scope: this
		});
	},
	/**
	 * Export data email to excel file
	 */
	fnDownloadExportData: function(key) {
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
		__form.id.value = key ;
		__form.target = 'ViewDocument' ;
		__form.bean.value = 'exportEmailBean' ;
		
		// submit form.
		__form.submit() ;
	},
	/**
	 * Load mail account
	 * @param {} params
	 */
	__fnLoadMailAcc: function(params, aliasParams, start, viewData) {
		var loadMask = new Ext.LoadMask(this.body, {
			msg: Ext.LoadMask.prototype.msg
		});
		
		var __fnPrint =  function(data){
			var tplMarkup=[
				'<div style="font-size:16px;font-weight:bold;text-align:center;">',iwebos.message.mail.report_list_account,'</div>',
				'<table width="100%" border="0" cellspacing="0" cellpadding="0">',
					'<tr>',
						'<td align="left" valign="top" width="20%">',iwebos.message.mail.report_domain,':</td>','<td align="left" valign="top" width="30%">{domain}</td>',
						'<td align="left" valign="top" width="20%">',iwebos.message.mail.report_domain_desc,':</td>','<td align="left" valign="top" width="30%">{desc}</td>',
					'</tr>',
					'<tr>',
						'<td align="left" width="20%">',iwebos.message.mail.report_email,':</td>','<td align="left" width="30%">{emailnumber}</td>',
						'<td align="left" width="20%">',iwebos.message.mail.report_alias,':</td>','<td align="left" width="30%">{aliasnumber}</td>',
					'</tr>',
				'</table>',
				'<br>',
				'<table width="100%"  border="0" cellspacing="0" cellpadding="0">',
					'<tr>',
						'<td align="left"><b>',iwebos.message.mail.report_email,'</b><br></td>',
					'</tr>',
					'<tr>',
						'<td align="left">',
							'<table width="100%" border="0" cellspacing="0" cellpadding="2" class="main">',
								'<tr>',
									'<td class="docin-main-head" width=50px>STT</td>',
									'<td class="docin-main-head">',iwebos.message.mail.mail_account,'</td>',
									'<td class="docin-main-head">',iwebos.message.mail.fullname,'</td>',
									'<td class="docin-main-head">',iwebos.message.mail.create.sizelimit,'</td>',
								'</tr>',
								'<tpl for="account">',
									'<tr>',
										'<td align="left">{#}</td>',									
										'<td align="left">{[this.getEmail(values.data)]}</td>',
										'<td align="left">{[this.getName(values.data)]}</td>',
										'<td align="left">{[this.getCapicity(values.data)]}</td>',
									'</tr>',
								'</tpl>',
								
							'</table>',
						'</td>',
					'</tr>',
				'</table>',
				'<br>',
				'<table width="100%"  border="0" cellspacing="0" cellpadding="0" >',
					'<tr>',
						'<td align="left"><b>',iwebos.message.mail.report_alias,'</b><br></td>',
					'</tr>',
					'<tr>',
						'<td align="left">',
							'<table width="100%"  border="0" cellspacing="0" cellpadding="2" class="main">',
								'<tr>',
									'<td class="docin-main-head" width=50px>',iwebos.message.mail.report_order_number,'</td>',
									'<td class="docin-main-head">',iwebos.message.mail.mail_account,'</td>',
									'<td class="docin-main-head">',iwebos.message.mail.fullname,'</td>',
								'</tr>',
								'<tpl for="alias">',
									'<tr>',
										'<td align="left">{#}</td>',
										'<td align="left">{[this.getEmail(values.data)]}</td>',
										'<td align="left">{[this.getName(values.data)]}</td>',
									'</tr>',
								'</tpl>',
								
							'</table>',
						'</td>',
					'</tr>',
				'</table>'
			];
			
			var template=new Ext.XTemplate(tplMarkup.join(''),{
			getEmail:function(data){
				return data.email;
			},
			getName: function(data){
				return data.displayname;
			},
			getCapicity:function(data){
				return data.capicity;
			}
				
			});
			var html=template.apply(data);
			// show dialog
			var dialog= new iNet.iwebos.ui.common.dialog.Dialog({
				width: 750,
				height: 450,
				autoScroll:true,
				bodyStyle: 'padding: 5px;',
				enableTabScroll:true,
				maximizable: true,
				title: iwebos.message.mail.report_list_account_title,
				tools:[{
		        	id: 'print',
		        	qtip: iwebos.message.mail.report_print_tooltip,
		        	handler: function(event, toolEl, panel){
						var printWindow=window.open('','printReport');
						printWindow.document.write('<head>');
						printWindow.document.write('<title>',iwebos.message.mail.report_list_account_title,'</title>');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css" />');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/extjs.css" />');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="css/paperwork.css" />');
                    	printWindow.document.write('</head>');
                    	printWindow.document.write('<html><body>');
						printWindow.document.write(html);
						printWindow.document.write('</body></html>');
						printWindow.document.close();
						printWindow.print();
					}
		        }],
				/*tbar:[{
					xtype: 'button',
					iconCls: 'icon-printer',
					tooltip: 'Print',
					handler: function(){
						var printWindow=window.open('','printReport');
						printWindow.document.write('<head>');
						printWindow.document.write('<title>',iwebos.message.mail.report_list_account_title,'</title>');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css" />');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/extjs.css" />');
						printWindow.document.write('<link rel="stylesheet" type="text/css" href="css/paperwork.css" />');
                    	printWindow.document.write('</head>');
                    	printWindow.document.write('<html><body>');
						printWindow.document.write(html);
						printWindow.document.write('</body></html>');
						printWindow.print();
						//printWindow.document.close();
					}
				}],*/
				html: html
			});
			dialog.show();
			loadMask.hide();

		};
		/**
		 * Load alias account
		 */
		// handle when loading email successfull
		__fnLoadAliasSuccess = function(response, options) {
				var __data = Ext.util.JSON.decode(response.responseText);
				var __success = (__data.success==undefined)?true:__data.success;
				if(__success) {
					// get response data
					var __newDatas = __data.rows;
					// process data
					if(__newDatas != null && __newDatas.length != 0) {
						viewData['aliasStore'].loadData(__newDatas, true);
					}
					// get new offset to get data
					start = start + params.limit;
					if(start < viewData['aliasnumber']) {
						// continue loading data
						__fnLoadAliasAcc(aliasParams, start, viewData);
					} else {
						viewData['emailStore'].sort('email', 'ASC');						
						viewData['aliasStore'].sort('email', 'ASC');
						viewData['account'] = viewData['emailStore'].data.items;
						viewData['alias'] = viewData['aliasStore'].data.items;
						// view dialog
						__fnPrint(viewData);
					}
				} else {
					// show error
					Ext.MessageBox.show({
						title: iwebos.message.mail.error,
						msg: iwebos.message.mail.report_loading_error,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
					// hide mask
					loadMask.hide();
					return;
				}
		};
		var __fnLoadMailAccount = {fn:this.__fnLoadMailAcc, scope: this};
		// load alias account 
		var __fnLoadAliasAcc = function(params, start, viewData) {
			params['start'] = start;
			// send request to server.
			iNet.Ajax.request({
				url: 'jsonajax',
				params: params,
				method: 'POST',
				success: __fnLoadAliasSuccess,
				failure: function(result, request){
					loadMask.hide();
				}
			});	
		};	
		
		/**
		 * Load mail account
		 */
		// handle when loading email successfull
		__fnLoadMailSuccess = function(response, options) {
				var __data = Ext.util.JSON.decode(response.responseText);
				var __success = (__data.success==undefined)?true:__data.success;
				if(__success) {
					// get response data
					var __newDatas = __data.rows;
					// process data
					if(__newDatas != null && __newDatas.length != 0) {
						viewData['emailStore'].loadData(__newDatas, true);
					}
					// get new offset for loading
					start = start + params.limit;
					if(start < viewData['emailnumber']) {
						// continue loading data
						__fnLoadMailAccount.fn.apply(__fnLoadMailAccount.scope, [params, aliasParams, start, viewData]);
					} else {
						// begin loading alias account
						start = 0;
						__fnLoadAliasAcc(aliasParams, start, viewData);
					}
				} else {
					// show error
					Ext.MessageBox.show({
						title: iwebos.message.mail.error,
						msg: iwebos.message.mail.report_loading_error,
						buttons: Ext.MessageBox.OK,
						icon: Ext.MessageBox.ERROR
					});
					// hide mask
					loadMask.hide();
					return;
				}
		};
		params['start'] = start;
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: params,
			method: 'POST',
			success: __fnLoadMailSuccess,
			failure: function(result, request){
				loadMask.hide();
			}
		});	
		//load mask
		loadMask.show();
	},
	
	/**
	 * Get data on GUI
	 */
	_getData: function() {
		var __object = {};
		// get all item on grid
		var __items = this.grid.store.data.items;
		
		// checking data
		if(__items == undefined && __items.length == 0) {
			__object['domainnumber'] = 0;
			__object['emailnumber'] = 0;
			__object['aliasnumber'] = 0;
			__object['domains'] = [];
			return __object;
		}
		
		// initialize variable
		var __emailNumber = 0, __aliasNumber = 0;
		var __domains = [];
		var __domain;
		for(var __index = 0; __index < __items.length; __index++) {
			// get data at current position
			__domain = __items[__index].data;
			// put domain to list
			__domains[__index] = __domain;
			// compute data
			__emailNumber += __domain.tmail;
			__aliasNumber += __domain.talias;
		}
		
		// put data to object
		__object['domainnumber'] = __items.length;
		__object['emailnumber'] = __emailNumber;
		__object['aliasnumber'] = __aliasNumber;
		__object['domains'] = __domains;

		// return data
		return __object;
	},
	
	/**
	 * Print report domain
	 */
	printDomainReport: function() {
		var __htmlTmp = [
			'<div style="font-size:16px;font-weight:bold;text-align:center;">',iwebos.message.mail.report_statistic_system_title,'</div>',
			'<table width="100%" border="0" cellspacing="0" cellpadding="0">',
				'<tr>',
					'<td align="left" width="175px">',iwebos.message.mail.report_total_domain,':</td>','<td align="left" colspam="3">{domainnumber}</td>',				
				'</tr>',
				'<tr>',
					'<td align="left" width="175px">',iwebos.message.mail.report_total_email,':</td>','<td align="left">{emailnumber}</td>',
					'<td align="left" width="175px">',iwebos.message.mail.report_total_alias,':</td>','<td align="left">{aliasnumber}</td>',
				'</tr>',
			'</table>',
			'<br>',
			'<div style="font-size:13px;font-weight:bold;text-align:left;">',iwebos.message.mail.report_statistic_domain_title,'</div>',
			'<table width="100%" border="0" cellspacing="0" cellpadding="2" class="main">',
				'<tr>',
					'<td class="docin-main-head" width="4%">',iwebos.message.mail.report_order_number,'</td>',
					'<td class="docin-main-head" width="30%">',iwebos.message.mail.report_domain,'</td>',
					'<td class="docin-main-head" width="40%">',iwebos.message.mail.report_domain_desc,'</td>',
					'<td class="docin-main-head" width="13%">',iwebos.message.mail.report_email,'</td>',
					'<td class="docin-main-head" width="13%">',iwebos.message.mail.report_alias,'</td>',
				'</tr>',
				'<tpl for="domains">',
					'<tr>',
						'<td align="left" valign="top" width="4%">{#}</td>',
						'<td align="left" valign="top" width="30%">{text}</td>',
						'<td align="left" valign="top" width="40%">{[this.getValue(values.desc)]}</td>',
						'<td align="left" valign="top" width="13%">{tmail}</td>',
						'<td align="left" valign="top" width="13%">{talias}</td>',
					'</tr>',
				'</tpl>',				
			'</table>'
		];
		
		// create template
		var __template = new Ext.XTemplate(__htmlTmp.join(''),{
			getValue: function(value) {
				if(value == '') return '&nbsp;';
				return value;
			}
		});
		var __html = __template.apply(this._getData());
		var printWindow = window.open('','printDMReport');
		printWindow.document.write('<head>');
		printWindow.document.write('<title>',iwebos.message.mail.report_statistic_system_title,'</title>');
		printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/ext-all.css" />');
		printWindow.document.write('<link rel="stylesheet" type="text/css" href="js/ext/resources/css/extjs.css" />');
		printWindow.document.write('<link rel="stylesheet" type="text/css" href="css/paperwork.css" />');
        printWindow.document.write('</head>');
        printWindow.document.write('<html><body>');
		printWindow.document.write(__html);
		printWindow.document.write('</body></html>');
		printWindow.document.close();
		printWindow.print();
	}
	
});