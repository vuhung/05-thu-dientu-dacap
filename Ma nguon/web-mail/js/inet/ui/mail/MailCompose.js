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
 * @class iNet.iwebos.ui.mail.MailCompose
 * @extends Ext.Panel
 * @constructor
 * Creates a new Panel
 * @param {Object} viewer
 * @param {Object} config
 */
 
/**
 * remove attachment
 * @param {Object} val
 */
function removeAttach(fkey){
	var __comp = Ext.getCmp('mail-compose');
	// component not exist
	if(!__comp) return;
	var __store = __comp.store;
	var __newStore = [];
	var __temp = {};
	if(!!__store){
		for(var index=0; index < __store.length ; index++){
			__temp = __store[index];
			if(!!__temp){
				if(__store[index].fkey !== fkey){
					// do nothing
					__newStore[__newStore.length] = __store[index]; 
				}
			}
		}
	}
	__comp.store = __newStore;
	__comp._showListAttach();
};
iNet.iwebos.ui.mail.MailCompose = function(viewer, config){
    this.viewer = viewer;
	// apply configuration.
    Ext.apply(this, config);
    
	var __headerId = 0;
	this.store = [];
	this.ieLoad=false;								
	// create identifier.
	var __config = config || {} ;
	var __prefix = __config.prefix || 'iwebos' ;
	delete __config.prefix;
		
	//~ Setting up identifier =================================================
	this.subjectId = __prefix + '-mail-comp-subject-id' ;
	this.toId = __prefix + '-mail-comp-to-id' ;
	this.ccId = __prefix + '-mail-comp-cc-id' ;
	this.bccId =  __prefix + '-mail-comp-bcc-id' ;
	this.bccPanelId = __prefix + 'mail-comp-bcc-panel-id';
	this.contentId = __prefix + '-mail-comp-content-id' ;
	this.attachId = __prefix + '-mail-attach-panel-id';
	this.topPanelId=__prefix + '-mail-top-panel-id';
	this.signId=__prefix + '-mail-sign-id';
	this.priorityId = __prefix + '-mail-priority-id';
	this.btShowBccId = __prefix + '-mail-visible-bcc-button';
	this.__toStore = [];
	this.__ccStore = [];
	this.__bccStore = [];
		
	this.__fullAddressPattern = /((?:<)([^ @<,>!#$%&*]+@[^ @<,>!#$%&*]+)(?:>))/;
    this.__addressPattern = /(^\s*[^ @<,>!#$%&*]+@[^ @<,>!#$%&*]+)/;
	var __priorityStore = new Ext.data.SimpleStore({
        fields: ['value', 'text'],
        data: [['HIGH', iwebos.message.mail.priority_high], 
        	   ['NORMAL', iwebos.message.mail.priority_normal],
        	   ['LOW',iwebos.message.mail.priority_low]]
    });
    
    var recipientStore = new Ext.data.SimpleStore({
		fields: ['recipient'],
		sortInfo: {field: 'recipient', direction: 'ASC'}
	});
	var __infoPanel = new Ext.form.FormPanel({
		region: 'center',
		labelAlign: 'left',
		collapsible: false,
		anchor: '100%',
		bodyStyle: 'padding: 2px;',
		autoHeight: true,
		border: true,
		width: '100%',
		items: [{
			layout: 'form',
			anchor: '100%',
			border: false,
			items: [{
				layout: 'form',
				anchor: '100%',
				border: false,
				items: [{
					layout: 'column',
					bodyStyle: 'padding:4px 4px 0px',
					anchor: '100%',
					border: false,
					columnWidth: 1,
					items: [{
						width: 80,
						border: false,
						items: [{
							xtype: 'button',
							minWidth:75,
							text: iwebos.message.mail.compose_to,
							handler: this.__chMoreMail.createDelegate(this)
						}]
					}, {
						columnWidth: 1,
						layout: 'form',
						border: false,
						anchor: '100%',
						items: [{
							id: this.toId,
							tabIndex: 150,
							xtype: 'boxselect',
							resizable: true,
							hideLabel:true,
							anchor:'100%',
							store: recipientStore,
							mode: 'local',
							displayField: 'text',
							displayFieldTpl: 'text',
							tpl: '<tpl for="."><div class="x-combo-list-item">{text}</div></tpl>',
							valueField: 'recipient'
						}]
					}]
				}]
			},{
				layout: 'form',
				anchor: '100%',
				border: false,
				items: [{
					layout: 'column',
					bodyStyle: 'padding:4px 4px 0px',
					anchor: '100%',
					border: false,
					items: [{
						width: 80,
						layout: 'form',
						border: false,
						items: [{
							xtype: 'button',
							minWidth:75,
							text: iwebos.message.mail.compose_cc,
							handler: this.__chMoreMail.createDelegate(this)
						}]
					}, {
						columnWidth: 1,
						layout: 'form',
						border: false,
						anchor: '100%',
						items: [{
							id: this.ccId,
							tabIndex: 151,
							xtype: 'boxselect',
							resizable: true,
							hideLabel:true,
							anchor:'100%',
							store: recipientStore,
							mode: 'local',
							displayField: 'text',
							displayFieldTpl: 'text',
							tpl: '<tpl for="."><div class="x-combo-list-item">{text}</div></tpl>',
							valueField: 'recipient'		
						}]
					}]
				}]
			},{
				id: this.bccPanelId,
				layout: 'form',
				anchor: '100%',
				border: false,
				height: 0,
				items: [{
					layout: 'column',
					bodyStyle: 'padding:4px 4px 0px',
					anchor: '100%',
					border: false,
					items: [{
						width: 80,
						layout: 'form',
						border: false,
						items: [{
							xtype: 'button',
							minWidth:75,
							text: iwebos.message.mail.compose_bcc,
							handler: this.__chMoreMail.createDelegate(this)
						}]
					}, {
						columnWidth: 1,
						layout: 'form',
						border: false,
						anchor: '100%',
						items: [{
							id: this.bccId,
							xtype: 'boxselect',
							resizable: true,
							hideLabel:true,
							anchor:'100%',
							store: recipientStore,
							mode: 'local',
							displayField: 'text',
							displayFieldTpl: 'text',
							tpl: '<tpl for="."><div class="x-combo-list-item">{text}</div></tpl>',
							valueField: 'recipient'		
						}]
					}]
				}]
			},{
				layout: 'form',
				anchor: '100%',
				border: false,
				items: [{
					layout: 'column',
					bodyStyle: 'padding:4px 4px 0px',
					anchor: '100%',
					border: false,
					items: [{
						width: 80,
						layout: 'form',
						border: false,
						items: [{
							xtype: 'button',
							minWidth:75,
							text: iwebos.message.mail.compose_subject
						}]
					}, {
						columnWidth: 0.7,
						layout: 'form',
						anchor: '100%',
						border: false,
						items: [{
							id: this.subjectId,
							tabIndex: 153,
							hideLabel:true,
							xtype: 'textfield',
							anchor: '98%'
						}]
					},{
						columnWidth: 0.3,
						layout: 'form',
						anchor: '100%',
						border: false,
						labelWidth: 50,
						items:[{
							id: this.priorityId,
							xtype: 'combo',
							fieldLabel: iwebos.message.mail.priority,
							triggerAction:'all',
				        	mode:'local',
				        	readOnly: true,
				            displayField:'text',
				            valueField:'value',
				            value: 'NORMAL',
				            store: __priorityStore,
				            selectOnFocus: true,
				            forceSelection : true,
				            border: false,
				            anchor: '100%'
						}]
					}]
				}]
			}, {
				id: this.attachId,
				layout: 'fit',
				height: 70,
				autoScroll: true,
				bodyStyle: 'padding:5px 5px 5px 5px',
				border: false
			}]
		}]
	});

	var __htmlEditorPanel = new Ext.form.FormPanel({
		region: 'center',
		labelAlign: 'left',
		collapsible: false,
		anchor: '100%',
		bodyStyle: 'padding: 0px;',
		renderHidden: true,
		autoHeight: true,
		border: false,
		width: '100%',
		items: [{
			layout: 'fit',
			border: false,
			anchor: '100%',
			bodyStyle: 'padding:5px 5px 5px 5px',
			items: [{
				id: this.contentId,
				tabIndex: 154,
				xtype: 'htmleditor',
				enableColors: true,
				enableAlignments: true,
				enableSourceEdit : true,
				enableFontSize: true,
				plugins: [new iNet.iwebos.ui.common.form.HtmlEditorPlugins.Table()],
				onFirstFocus: function(){
					this.activated = true;
					this.tb.items.each(function(item){
						item.enable();
					});
					
					// check content contain '&nbsp;'
					var __content = this.getValue();
					if(__content === '&nbsp;' || __content === '' ){
						this.setValue('');
					}
					
					if (Ext.isGecko) {
						try {
							this.win.focus();
							var s = this.win.getSelection();
							if (!s.focusNode || s.focusNode.nodeType != 3) {
								var r = s.getRangeAt(0);
								r.selectNodeContents(this.getEditorBody());
								r.collapse(true);
								this.deferFocus();
							}
						} 
						catch (e) {
						}
						
						// apply CSS
						try {
							this.execCmd('useCSS', true);
							this.execCmd('styleWithCSS', false);
						} 
						catch (e) {
						}
					};
					
					this.fireEvent('activate', this);
				}
			}]
		}]
	});
	
	var __btnSend = {
			xtype: 'button',
			iconCls: 'icon-email-sendmail',
			text: iwebos.message.mail.mail_send_tip,
			tooltip: iwebos.message.mail.mail_send_tip,
			handler: this._sendEmail,
			scope: this
		} ;
		
	// create attachment button bar.
	var __btnAttachment = {
		xtype: 'button',
		iconCls: 'icon-email-attachment-button',
		text: iwebos.message.mail.mail_attachment,
		tooltip: iwebos.message.mail.mail_attachment_tip,
		handler: this._attachFile,
		scope: this
	} ;
	
	// create Save draft button bar.
	var __btnSaveDraft = {
		xtype: 'button',
		id: this.chSaveDraftId,
		iconCls: 'icon-email-save-draft',
		text: iwebos.message.mail.mail_save_draf,
		tooltip: iwebos.message.mail.mail_save_draf_tip,
		handler: this._saveEmail,
		scope: this
	} ;		
	
	
	// setting up sort information.
	var __smtpRecord = Ext.data.Record.create([
					{name: 'id',mapping: 'id',type: 'string'},
					{name: 'smtpAccount',mapping: 'smtpAccount',type: 'string'}]
			) ;
			
	// setting up reader.
	var __smtpReader = new Ext.data.JsonReader({
					totalProperty: 'results',
        			root: 'rows',
					id:'id'},__smtpRecord) ;
    
    var __smtpStore = new iNet.iwebos.ui.common.store.WebOSSimpleStore({
    	reader: __smtpReader    	
    });
    
    // setting up sort information.
	var __signRecord = Ext.data.Record.create([
					{name: 'name',mapping: 'name',type: 'string'},
					{name: 'content',mapping: 'content',type:'string'}
					]
			) ;
			
		// setting up reader.
	var __signReader = new Ext.data.JsonReader({
					totalProperty: 'results',
        			root: 'rows',
					id:'name'},__signRecord) ;
	
	//sign store
	var __signStore = new iNet.iwebos.ui.common.store.WebOSSimpleStore({
    	reader: __signReader    	
    });				
					
	this.signCombo =new Ext.form.ComboBox({
        triggerAction:'all',
    	mode:'local',
		store: __signStore,
		displayField:'name',
        valueField:'content',
		typeAhead: true,
        selectOnFocus: true,
        forceSelection : true,
        border: false,
		readOnly:true,
        anchor: '95%'
    });
	
    this.comboSmtp = new Ext.form.ComboBox({
        triggerAction:'all',
    	mode:'local',
        displayField:'id',
        valueField:'smtpAccount',
        store: __smtpStore,
        selectOnFocus: true,
        forceSelection : true,
        border: false,
		readOnly:true,
        anchor: '95%'
    });
   
    var __btnVisibleBcc = {
    	id : this.btShowBccId,
		xtype: 'button',
		text: iwebos.message.mail.show_bcc,
		tooltip: iwebos.message.mail.show_bcc_tip,
		handler: this._visibleBcc,
		scope: this
	} ;
	
    var __text = {
    	xtype:'label',
    	text: iwebos.message.mail.account+":"
    }; 
	var __signLabel = {
    	xtype:'label',
    	text: iwebos.message.mail.signature_name+":"
    }; 
	//toolbar
	var __char='->';
	if (Ext.isGecko2) {
		__char = '-';
	}else {
		__char = '->';
	};	
	// create the search bar.		
	var __tbar = [__btnSend,'-',__text,this.comboSmtp,'-',__btnSaveDraft,'-',__btnAttachment,'-',__btnVisibleBcc,__char,__signLabel,this.signCombo] ;	
	
	var topPanel = new Ext.Panel({
		id: this.topPanelId,
		region: 'north',
		anchor: '100%',
		border: false,
		width: '100%',
		height: 95,
		items: [__infoPanel]
	});
	this.main = new Ext.Panel({
       	layout: 'border',
		region: 'center',
		frame: true,
		border: false,
        items: [topPanel,__htmlEditorPanel]
    });
	
    this.signCombo.on('beforeSelect', this._changeSignature.createDelegate(this), {scope: this});
    
	// create data.
    iNet.iwebos.ui.mail.MailCompose.superclass.constructor.call(this, {
        id: 'mail-compose',
		iconCls: 'icon-email-composer',
        title: iwebos.message.mail.title_mailcompose,
      	closable:true,
		frame: false,
		border: false,
		layout: 'border',
		tbar:__tbar,
        items: [this.main]
    });
	this.on('resize', this._onResize, this);
	this.on('close', function(panel){this.__owner.close = true}, {scope: this});
	this.on('render', function(panel){
		var __mainTab = Ext.getCmp('main-tabs');
		var __recipients = __mainTab.getRecipients();
		var __topPanelId = this.topPanelId;
		var __onResize = function(id){
			Ext.getCmp(id).on('autosize', function(selectbox, h, adjust){
				var cmp = Ext.getCmp(__topPanelId);
				var height = cmp.getSize().height;
				
				cmp.setHeight(height + adjust);
				
				Ext.EventManager.fireWindowResize();
			},this);	
		};
		
		__onResize(this.toId);
		__onResize(this.ccId);
		// bcc
		Ext.getCmp(this.bccId).on('autosize', function(selectbox, h, adjust){
			var cmp = Ext.getCmp(__topPanelId);
			var height = cmp.getSize().height;
			cmp.setHeight(height + adjust);
			var bccHeight = Ext.getCmp(this.bccPanelId).getInnerHeight();
			Ext.getCmp(this.bccPanelId).setHeight(bccHeight + adjust);
			Ext.EventManager.fireWindowResize();
		},this);
		
		if (__recipients != null && __recipients != undefined ){
			Ext.getCmp(this.toId).loadData(__recipients);
			Ext.getCmp(this.ccId).loadData(__recipients);
			Ext.getCmp(this.bccId).loadData(__recipients);
		}else{
			var __param = {iwct:'recipientContent', 
							iwm:'MAIL', 
							iwc:'READ_ONLY', 
							iwa:'loadRecipient', 
							action:'loadRecipient'};
        
	        // send request to server.
	        iNet.Ajax.request({
	            url: 'jsonajax',
	            params: __param,
	            scope: this,
	            method: 'POST',
	            success: function(response, options){
					if(!!response){
						var __result = Ext.util.JSON.decode(response.responseText);
		        		    		
			    		if (__result.success == undefined) {
			    			__mainTab.setRecipients(__result.results);
			    			Ext.getCmp(this.toId).loadData(__result.results);
			    			Ext.getCmp(this.ccId).loadData(__result.results);
			    			Ext.getCmp(this.bccId).loadData(__result.results);
			    		}
					}						
	            },
	            failure: function(response, options){
	            	// do nothing
	            }
	        });			  
		}
	}, this);
};

/**
 * Extends Class Ext.Panel
 */
Ext.extend(iNet.iwebos.ui.mail.MailCompose, Ext.Panel, {
	/**
	 * show dialog More Email
	 */
	__chMoreMail: function(){
		var __toComp = Ext.getCmp(this.toId); 
		var __ccComp = Ext.getCmp(this.ccId);
		var __bccComp = Ext.getCmp(this.bccId);
		var __panel = this;
		/**
		 * parse email address and add to the given component
		 */
		var __addData = function(comp,data){
			if(data.length > 0){
				var __address = '';
				
				for(var index = 0; index < data.length; index++){
					if(index == data.length - 1){
						__address += data[index].name;
					}else{
						__address += data[index].name + ', ';
					}
				}				
				comp.setValue(__address);
			}
		};
		/**
		 * handler ok
		 */
		var __okHandler = function(){
			var __object = this.getData();
			// to
			__panel.__toStore = __object.to;
			__addData(__toComp,__object.to);
			
			// cc
			__panel.__ccStore = __object.cc;
			__addData(__ccComp,__object.cc);
			
			// bcc
			__panel.__bccStore = __object.bcc;
			__addData(__bccComp,__object.bcc);
			// check bcc is visible
			if(__object.bcc.length > 0 && !__panel._fnHasBcc()){
				__panel._visibleBcc();
			}
			
			this.destroy();			
		};
		var __data = this._getAddressObject(this.__toStore,this.__ccStore,this.__bccStore);
		var __dlg = new iNet.iwebos.ui.mail.DialogSelectContact(this,{
			    									okHandler: __okHandler,
			    									okhScope: __dlg
			    								});
		__dlg.show(this);		
		__dlg.loadData(__data);
	},
	
	_getAddressObject:function(toStore, ccStore, bccStore){
		var __object = {};
		__object['to'] = this._getAddress(this.toId,toStore);
		__object['cc'] = this._getAddress(this.ccId,ccStore);
		if(this._fnHasBcc()){
			__object['bcc'] = this._getAddress(this.bccId,bccStore);
		}else{
			__object['bcc'] = [];
		}
		
		return __object;
	},
	
	_getAddress:function(idCmp,store){
		var __object = [];
		var __comp = Ext.getCmp(idCmp);
		var __email = '';
		var __name = '';
		var __fullEmail = '';
		var __add = __comp.getValue();
		__add = __add.replace(/;/g,',');
		
		var __emails = __add.split(',');
		for(var index = 0; index < __emails.length;index++){
			
			__fullEmail = __emails[index].trim();
			if(__fullEmail === '') continue;
			
			__name = __fullEmail;

			// short address
			var __emailArray = __fullEmail.match(this.__addressPattern);
			
			// full address
			if(__emailArray == null){
				__emailArray = __fullEmail.match(this.__fullAddressPattern);
			}
			
			if(__emailArray == null) continue;
			
			__email = __emailArray[0];
			for(var j = 0; j < store.length; j++){
				if(store[j].id == __email){
					__name = store[j].name;
					break;					
				}				
			}
			if(__email !== ''){
				__object[index] = {id:__email, name:__name};
			}
		}
		
		return __object;
	},
	/**
	 * resize the content.
	 */
	_onResize : function(component, width, height, rwidth, rheight){
		// fire resize event.
		Ext.EventManager.fireWindowResize();
		if (!Ext.isIE) {	
			var __component = Ext.getCmp(this.topPanelId);
			var __htop = __component.getInnerHeight();
			// set height for editor control
			var __component = Ext.getCmp(this.contentId);
			__component.setHeight(this.getSize().height - __htop - 50);
		}else{
			var __component = Ext.getCmp(this.topPanelId);
			var __htop =0;
			if(!this.ieLoad){
				__htop = 95;
				this.ieLoad=true;
			}else{
				__htop = __component.getSize().height;
			}
			//__component.getSize().height;
			var __component = Ext.getCmp(this.contentId);
			__component.setHeight(this.getSize().height - __htop - 50);
		}

	},
	
	 /**
     * Set mail data.
     * 
     * @param {Object} mailObj - the given mail content object.
     */
	setData: function(mailObj){
		// handle task panel.
    	this.setHeaderId(mailObj.hid);				
		
    	// set component TO value.		
		var toCmp = Ext.getCmp(this.toId);
		if(mailObj.to != undefined){
			toCmp.setValue(mailObj.to);
		}else{
			toCmp.setValue('');
		}
		
		// set component CC value.		
		var ccCmp = Ext.getCmp(this.ccId);
		if(mailObj.cc != undefined){
			ccCmp.setValue(mailObj.cc);
		}else{
			ccCmp.setValue('');
		}
		
		// set component BCC value.
		var bccCmp = Ext.getCmp(this.bccId);
		if(mailObj.bcc != undefined && mailObj.bcc !== ''){
			if(!this._fnHasBcc()){
				this._visibleBcc();	
			}
			
			bccCmp.setValue(mailObj.bcc );
			
		}else{
			if(this._fnHasBcc()){
				this._visibleBcc();	
			}
			bccCmp.setValue('');
		}
		
		// set component SUBJECT
		var subjectCmp = Ext.getCmp(this.subjectId);
		subjectCmp.setValue(mailObj.subject);
		
		// set priority 
		var pCmp = Ext.getCmp(this.priorityId);
		pCmp.setValue(mailObj.priority);
		
		var __attachments = mailObj.attachments;
		if(!!__attachments){
			for(var i=0; i< __attachments.length; i++){
				__attachments[i].hid = mailObj.hid;
			}
		}
		
		// set component ATTACHMENT
		this.store = (__attachments == undefined)? []:__attachments;
		// draw attach
		this._showListAttach();
		
		// set component body text
		var contentCmp = Ext.getCmp(this.contentId);
		contentCmp.setValue(mailObj.body);
	},
	
	/**
	 * clear data composer tab
	 */
	clearData : function(){
		this.setHeaderId(0);				
		
    	// set component TO value.		
		var component = Ext.getCmp(this.toId);
		component.setValue('');
		
		// set component CC value.		
		component = Ext.getCmp(this.ccId);
		component.setValue('');
		
		// set component BCC value.		
		component = Ext.getCmp(this.bccId);
		component.setValue('');
		
		// set component SUBJECT
		component = Ext.getCmp(this.subjectId);
		component.setValue('');
		
		// set component ATTACHMENT
		this.store = [];
		// draw attach
		this._showListAttach();
		
		// set component body text
		component = Ext.getCmp(this.contentId);
		component.setValue('');
	},
	
	/**
	 * 
	 * @param {Object} hid  Long  -Header identifier
	 * @param {Object} task String - reply,replyall,forward, opendraft  
	 * @param {Object} action
	 */
    loadInfo: function(hid, task){
		// load smtp default
		this._loadSmtp();
    	
    	if(hid == 0) return;
    	
		// create base params.
		var baseParams = {iwct:'mailComposerContent', iwm:'MAIL',  iwa:task, action:task};
		
		if(task ==='draft'){
			baseParams['iwc'] = 'READ_ONLY';
			baseParams.iwct = 'loadMailComposerContent';
		}else{
			baseParams['iwc'] = 'READ_WRITE'; 
		}	
				
		// set mail header identifier.
		baseParams['hid'] = hid ;
		
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: baseParams,
			scope: this,
			method: 'POST',
			success: function(result, request){
				var data = Ext.util.JSON.decode(result.responseText);

				if (this.setData !== undefined) {
					this.setData(data);
				}
			},
			failure: function(result, request){
			},
			maskEl: this.bwrap
		});
	},
	
	/**
	 * set to address 
	 * @param {} address
	 */
	setToAddress: function(address){
		var __comp = Ext.getCmp(this.toId);
		__comp.setValue(address);
	},
	/**
	 * return the object  data.
	 */
	getData : function(){
		// create object to store data.
		var object = {} ;
		
    	// set to address value.
		var __comp = Ext.getCmp(this.toId);
		object['to'] = __comp.getValue().replace(/;/g,',');
		
		// set cc address value
		__comp = Ext.getCmp(this.ccId);
		object['cc'] = __comp.getValue().replace(/;/g,',') ;
		
		// set bcc address value
		if(this._fnHasBcc()){
			__comp = Ext.getCmp(this.bccId);
			object['bcc'] = __comp.getValue().replace(/;/g,',') ;
		}else{
			object['bcc'] = '' ;
		}
		
		// set subject value
		__comp = Ext.getCmp(this.subjectId);
		object['subject'] = __comp.getValue() ;
		
		// get priority value
		__comp = Ext.getCmp(this.priorityId);
		object['priority'] = __comp.getValue() ;
		
		// set body text value
		__comp = Ext.getCmp(this.contentId);
		object['body'] = __comp.getValue() ;
		
		// set attachment data.
		var __files = [] ;
		if(this.store){
			__files = this.store;
		}	
		
		// set object attachment.
		object['attachments'] = __files ;	
		
		return object;
	},
	
	/**
	 * valid data
	 * @param {boolean} isSend
	 * @return {String}
	 */
	validateData: function(isSend){
		if(isSend){
			var __comp = Ext.getCmp(this.toId);
			var __toAddress = __comp.getValue().trim();
			// check to email address is not empty
			if(__toAddress.length == 0 ){
				__comp.focus();
				return iwebos.message.mail.error_email_address_empty;
			}
			
			// check subject email is not empty
			__comp = Ext.getCmp(this.subjectId);
			var __subject = __comp.getValue().trim();
			if(__subject.length == 0){
				__comp.focus();
				return iwebos.message.mail.error_subject_empty;
			}			
		}
		
		// valid To address
		var __error = this.validAddresComp(this.toId);
		if(__error.length > 0) return __error;
		
		// valid CC adderss
		__error = this.validAddresComp(this.ccId);
		if(__error.length > 0) return __error;
		
		// valid BCC address
		if(this._fnHasBcc()){
			__error = this.validAddresComp(this.bccId);
			if(__error.length > 0) return __error;
		}
		
		
		return '';
	},
	validAddresComp: function(idComp){
		var __comp = Ext.getCmp(idComp);
		
		var __address = __comp.getValue().trim() ;
		if(__address.length == 0) return '';
		
		// replace ';' -> ','
		__address = __address.replace(/;/g,',');

		// split data to array
		var __toArr = __address.split(',');
		
		var __email = '';
		for(var index=0;index <__toArr.length;index++){
			__email = __toArr[index];
			var __valid = this.checkValidation(__email.trim());
			if(!__valid){
				__comp.focus(true,true);
				__email = __email.replace(/</, '&lt;');
				__email = __email.replace(/>/, '&gt;');
				return iwebos.message.mail.error_email_address + __email + iwebos.message.mail.error_email_address_invalid;
			}
		}
		return '';
	},
	
	getHeaderId : function(){
		if(this.__headerId == undefined){
			this.__headerId = 0;
		}
		return this.__headerId;
	},
	
	setHeaderId: function(id){
		this.__headerId = id;
	},
	
	/**
	 * handle external document.
	 */
	onExternalDocument:function(){
		if(!this.udialog){
			this.udialog = new Ext.ux.UploadDialog.Dialog({
				url:'mail-upload-file',
				reset_on_hide:false,
				allow_close_on_upload:true,
				upload_autostart:false,
				post_var_name:'upload'
			}) ;
			
			// handle when uload.
			this.udialog.on('uploadsuccess', this.onUploadSuccess, this) ;
		}
		
		// show dialog.
		this.udialog.show(this) ;
	},
	
	/**
	 * set data attach
	 * @param {Object} data
	 */
	setDataAttach: function(data){
		if(!this.store){
			this.store = [];	
		}
		
		this.store[this.store.length] = data;
	},
	 
	/**
	 * handle upload successful. 
	 */
	onUploadSuccess : function(dialog, filename, response, record){
		if(response.success){ // upload success.
			// convert function.
			var __fnConvertObj = function(file, extension){
				var __fName = file.original;
				
	        	var index = __fName.lastIndexOf('\\');
        	 	if(index > 0){
        	 		__fName = __fName.substring(index + 1,__fName.length);
        	 	
        	 	}else{
        	 		index = __fName.lastIndexOf('\/');
        	 		if(index > 0){
        	 			__fName = __fName.substring(index + 1,__fName.length);
        	 		}
        	 	}
				return {hid: 0,icon: extension, fname: __fName,fkey:file.file} ;
			}			
			
			// get the file.
			var __files = response.files ;
			var extension = '' ;
			var __file = null, object= null ;
			for(var index = 0; index < __files.length; index++){
				__file = __files[index] ;
				extension = iNet.DocumentFormat.getExtension(__file.original) ;
				// get object data.
				object = __fnConvertObj(__file, extension) ;
				this.setDataAttach(object);	
			}
			
			this._showListAttach();
		}
	},
	
	_showListAttach: function(){
		// define a template attach
		var tplAttach = [
			'<tpl if="this.isCheck(attachments)">',
				'<table width=100% border=0 cellspacing=0 cellpadding=0>', 
					'<tr>',
						'<td width=5% NOWRAP valign="top">',
							'<b>'+iwebos.message.mail.template_attach+': &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>',
						'</td>',
						'<td align="left">',
							'<tpl for="attachments"><span class={[MailService.getCssAttachment(values.icon)]}>&nbsp;&nbsp;&nbsp;&nbsp;</span><a style="cursor:pointer;" onclick="MailService.download(\'{hid}\',\'{fname}\', \'{fkey}\',\'{icon}\')">{[this.formatName(values.fname)]}</a> ' +
								'<a onclick="removeAttach(\'{fkey}\')">[' + iwebos.message.mail.remove + ']{[xindex % 2 === 0 ? "<br>" : ","]}</a> </tpl>',
						'</td>', 
					'</tr>',
				'</table>',
			'</tpl>'];
		var itemTpl = new Ext.XTemplate(tplAttach.join(''), {
				isCheck: function(val){
		        	return (false == val) ? '' : val;
		        },
		        
		        formatName: function(name){
		        	 return name;
		        }
		});
		//
		var data = [];
		data['attachments'] = this.store;
		//apply template
		var __component= Ext.getCmp(this.topPanelId); 
		var __compBcc = Ext.getCmp(this.bccPanelId);
		var __heightBcc = __compBcc.getInnerHeight();
		var __height= (__heightBcc > 0 ? 125:95);
		if(this.store!=null && this.store.length>0){
			var __count=this.store.length;
			
			var line=1;
			line=Math.round(__count/2);
			if (line > 3)line = 3;
			__height = __height + line * 25;
			
			if (Math.round(__count/2) == 2) __height=__height-10;
			if (Math.round(__count/2) == 3) __height=__height-20;
			if (Math.round(__count/2) == 4) __height=__height-10;
			__component.setHeight(__height);
			
		}else{
			__component.setHeight(__height);
		}
		this._onResize();	
		var previewAttach = Ext.getCmp(this.attachId);
		itemTpl.overwrite(previewAttach.body,data);
	},
	
	checkValidation: function (email) {
		if(email == '') return true;
		
		var __result = false;
   		__result = this.__fullAddressPattern.test(email);
   		if(__result) return __result;
   		
   		__result = this.__addressPattern.test(email);
   		return __result;
	},

	_noAtSign :  function( address ) {
	    // CHECK THAT THERE IS AN '@' CHARACTER IN THE STRING
	    if ( address.indexOf ( '@', 0 ) == -1 ) {
	        return ( true )
	    } else {
	        return ( false );
	    }
	},
	
	_nothingBeforeAt: function( address ) {
	    // CHECK THERE IS AT LEAST ONE CHARACTER BEFORE THE '@' CHARACTER
	    if ( address.indexOf ( '@', 0 ) < 1 ) {
	        return ( true )
	    } else {
	        return ( false );
	    }
	},
	
	_noLeftBracket : function( address ) {
	    // IF EMAIL ADDRESS IN FORM 'user@[255,255,255,0]', THEN CHECK FOR LEFT BRACKET
	    if ( address.indexOf ( '[', 0 ) == -1 && address.charAt ( address.length - 1 ) == ']' ) {
	        return ( true )
	    } else {
	        return ( false );
	    }
	},
	
	_noRightBracket: function( address ) {
	    // IF EMAIL ADDRESS IN FORM 'user@[255,255,255,0]', THEN CHECK FOR RIGHT BRACKET
	    if ( address.indexOf ( '[', 0 ) > -1 && address.charAt ( address.length - 1 ) != ']' ) {
	        return ( true );
	    } else {
	        return ( false );
	    }
	},
	
	_noValidPeriod: function( address ) {
	    // IF EMAIL ADDRESS IN FORM 'user@[255,255,255,0]', THEN WE ARE NOT INTERESTED
	    if ( address.indexOf ( '@', 0 ) > 1 && address.charAt ( address.length - 1 ) == ']' )
	        return ( false );
	
	    // CHECK THAT THERE IS AT LEAST ONE PERIOD IN THE STRING
	    if ( address.indexOf ( '.', 0 ) == -1 )
	        return ( true );
	
	    return ( false );
	},
	
	_noValidSuffix: function (address ) {
	    // IF EMAIL ADDRESS IN FORM 'user@[255,255,255,0]', THEN WE ARE NOT INTERESTED
	    if ( address.indexOf ( '@', 0 ) > 1 && address.charAt ( address.length - 1 ) == ']' )
	        return ( false );
	
	    // CHECK THAT THERE IS A TWO OR THREE CHARACTER SUFFIX AFTER THE LAST PERIOD
	    var len = address.length;
	    var pos = address.lastIndexOf ( '.', len - 1 ) + 1;
	    if ( ( len - pos ) < 2 || ( len - pos ) > 3 ) {
	        return ( true );
	    } else {
	        return ( false );
	    }
	},
	
	// send email
	_sendEmail : function(){
		var __error = this.validateData(true);
		
		if(__error.length > 0){
			// show error.
        	MailService.showError(__error);
        	return;
		}
		
		// create base params.
		var __baseParams = {iwct:'mailComposerContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'send', action: 'send'};
		
		// get data from mail composer panel
		var __object = this.getData();
		
		var __smtpAccount = this.comboSmtp.getValue();
		//  
		if(__smtpAccount == ''){
			MailService.showError(iwebos.message.mail.error_smtp);
        	return;
		}
		
		var __mainTab = Ext.getCmp('main-tabs');
		var __accounts = __mainTab.fetchInfo.accounts;
		var __pass = false;
		if(__accounts.length > 0){
			for(var __index = 0;__index < __accounts.length; __index++){
				var __temp = __accounts[__index];
				if(__temp.smtpAccount == __smtpAccount){
					if(__temp.pass){
						__pass = true;
						break;
					}else{
						this._updatePassword(__mainTab,__index);					
					}
				}
			}
		}else{
			MailService.showError(iwebos.message.mail.error_smtp);
		}
		if(!__pass) return;
		
		__object['smtpAccount'] = __smtpAccount;
		
		var __store = this.comboSmtp.store;
		__object['from'] = this._getEmailAddress(__store, __smtpAccount);
		//analytic recipient
		__object['newRecipients'] = this.analyticAddress(__object);
		// set header identifier
		__baseParams['hid'] = this.getHeaderId();
		// set object
		__baseParams['object'] = Ext.util.JSON.encode(__object);
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParams,
			scope: this,
			method: 'POST',
			success: function(result, request){
				this._removeEmail(this.getHeaderId());
				var __mainTab = Ext.getCmp('main-tabs');
				__mainTab.remove(this);
			},
			failure: function(result, request){				
			},
			maskEl: this.bwrap
		});
	},
	
	/**
	 * 
	 * @param {to,cc,bcc} data
	 */
	analyticAddress: function(data){
		var __object = this._getAddressObject(this.__toStore,this.__ccStore,this.__bccStore);
		var __recipient = Ext.getCmp('main-tabs').getRecipients() || [];
		var __newRecipient = [];
		
		var __analytic = function(array){
			for(var i = 0; i< array.length; i++){
				var temp = array[i].name;
				temp = temp.replace('<','&lt;');
				temp = temp.replace('>','&gt;');
				var j = 0;
				for(j = 0; j < __recipient.length; j++){
					if(temp == __recipient[j]) break;
				}
				
				if(j == __recipient.length)
					__newRecipient.push(array[i].name);
			}
		}
		
		//analytic
		__analytic(__object.to);
		__analytic(__object.cc);
		__analytic(__object.bcc);
		
		for(var index = 0; index < __newRecipient.length; index++){
			__recipient.push(__newRecipient[index]);
		}
		
		return __newRecipient.join(',');
	},
	
	_attachFile : function(){
		this.onExternalDocument();
	}, 
	
	/**
	 * handle visible BCC address 
	 */
	_visibleBcc: function(){
		var __compBcc = Ext.getCmp(this.bccPanelId);
		var __compInfo = Ext.getCmp(this.topPanelId);
		var __height = __compBcc.getInnerHeight();
		var __bcc = Ext.getCmp(this.bccId);
		var __heightInfo = __compInfo.getInnerHeight();
		var __compButton = Ext.getCmp(this.btShowBccId); 
		if(__height === 0){
			__compInfo.setHeight(__heightInfo + __bcc.getSize().height + 5);
			__compBcc.setHeight(__bcc.getSize().height + 5);
			__compButton.setText(iwebos.message.mail.hide_bcc);
			//set tabindex
			try{
				__bcc.el.dom.setAttribute('tabIndex', 152);
			}catch(ex){
				
			}

		}else{
			__compInfo.setHeight(__heightInfo - __compBcc.getSize().height);
			__compBcc.setHeight(0);
			__compButton.setText(iwebos.message.mail.show_bcc);
			try{
				__bcc.el.dom.setAttribute('tabIndex', "");
			}catch(ex){
				
			}
		}
		this._onResize();
		Ext.EventManager.fireWindowResize();
	},
	
	// save draft email
	_saveEmail : function(){
		//mail-compose
		var __error = this.validateData(false);
		
		if(__error.length > 0){
			// show error.
        	MailService.showError(__error);
        	return;
		}
		// create base params.
		var __baseParams = {iwct:'mailComposerContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'save', action: 'save'};
		
		var __object = this.getData();
		
		var __smtpAccount = this.comboSmtp.getValue();
		// 
		if(__smtpAccount == ''){
        	MailService.showError(iwebos.message.mail.error_smtp);
        	return;
		}	
		__object['smtpAccount'] = __smtpAccount;
		
		var __store = this.comboSmtp.store;
		__object['from'] = this._getEmailAddress(__store, __smtpAccount);
		
		// set header identifier
		__baseParams['hid'] = this.getHeaderId();
		// set object
		__baseParams['object'] = Ext.util.JSON.encode(__object);
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParams,
			scope: this,
			method: 'POST',
			success: function(result, request){
				var __data = Ext.util.JSON.decode(result.responseText);

				if (this.setHeaderId !== undefined) {
					this.setHeaderId(__data.hid);
					var __attachments = __data.attachments;
					//for(var i=0; i< this.store; i++){
					for(var j=0; j < __attachments.length; j++){
						__attachments[j].hid = __data.hid;
					}
						
					// set component ATTACHMENT
					this.store = __attachments;
					this._showListAttach();
				}
			},
			failure: function(result, request){},
			maskEl: this.bwrap
		});
	},
	
	/**
	 * load smtp account to combo box
	 */
	_loadSmtp: function(){
		// create base params.
		var __baseParams = {iwct:'loadMailConfigureContent', iwm:'MAIL', iwc:'READ_ONLY', iwa:'load', action:'load', mode: 'smtp'};
		// send request to server.
		iNet.Ajax.request({
			url: 'jsonajax',
			params: __baseParams,
			scope: this,
			method: 'POST',
			success: function(result, request){
				//check data response
				if(result == undefined || result === '')
					return;
				var __data = result.responseText;
				__data = eval('(' + __data + ')');
				
				var __success = (__data.success == undefined);
				
				if(__success){
					// set smtp account
					this.setSmtpAccount(__data.smtps,__data.smtp);
					
					// set signature
					this.setSignature(__data.signs);
					
				}else{
					MailService.showError(iwebos.message.mail.error_smtp);
				}	
				
			},
			failure: function(result, request){}			
		});		
	},
	
	/**
	 * get email address from give smtpAccount
	 * @param {Store} store
	 * @param {String} smtpAccount
	 * @return {String}
	 */
	_getEmailAddress : function(store, smtpAccount){
		if(store.getCount() > 0){
			var __data = {}; 
			for(var __index = 0; __index < store.getCount(); __index++){
				__data = store.getAt(__index).data;
				if(__data.smtpAccount == smtpAccount)
					return __data.id;
			}
		}
		return '';
	},
	
	_changeSignature: function(combo, record, index ){
		this._addSignature(record.data.content);		
	},
	
	_addSignature: function(signature){
		var __comp = Ext.getCmp(this.contentId);
		var __content = __comp.getValue();
		var __startSign = '<!--Start Signature-->';
		var __endSign = '<!--End Signature-->';
		
		var __start = __content.lastIndexOf(__startSign);
		var __end = __content.lastIndexOf(__endSign);
		
		// the first time
		if(__start <= 0 || __end <=0){
			__content = __content +  '<br/><br/><br/>' +  __startSign +  signature + __endSign;
			
			__comp.setValue(__content);
		}else if(__start > 0 || __end >0) {
			
			var __firstContent = __content.substring(0,__start);
			var __secondContent = __content.substring(__end,__content.length);
			
			__comp.setValue(__firstContent +  __startSign + signature + __secondContent);
		}
	},
	
	/**
	 * remove mail header after perform task
	 * @param {} id
	 */
	_removeEmail: function(id){
		var __grid = Ext.getCmp('email-message-grid-id') ;
		
		// grid is undefined
		if(!__grid) return;
		
		var __store = __grid.store;
		if(__store.getCount() > 0){
			var __record ; 
			for(var __index = 0; __index < __store.getCount(); __index++){
				__record = __store.getAt(__index);
				if(__record.data.id == id){
					__store.remove(__record);
					return;
				}
			}
		}
	},
	
	_updatePassword: function(mainTab,index){
		var __panel = this;
		var __fetchInfo = mainTab.fetchInfo;
        var __okHandler = function(){
			// validate data 
			var __password = this.getPassword();
			if (__password == '') {
				// show error.
				Ext.MessageBox.show({
					title: iwebos.message.mail.announcement,
					msg: iwebos.message.mail.error_empty_password,
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				});
				return;
			}
			
			
			var __param = {iwct:'mailConfigureContent', iwm:'MAIL', iwc:'READ_WRITE', iwa:'updatePW', action:'updatePW'};
			var __data = {address: __fetchInfo.accounts[index].address,
						  pass: __password};			
			
			 __param['object'] = Ext.util.JSON.encode(__data);
        
	        // send request to server.
	        iNet.Ajax.request({
	            url: 'jsonajax',
	            params: __param,
	            scope: __panel,
	            method: 'POST',
	            success: function(response, options){
					if(!!response){
						var __result = eval('(' + response.responseText + ')');
		        		    		
			    		if (__result.success) {
			    			mainTab.fetchInfo.accounts[index].pass = true;
			    			mainTab.config.smtpAccount.smtps[index].pass = true;
							__panel._sendEmail();							
			    		}
					}						
	            },
	            failure: function(response, options){
	            	// do nothing
	            }
	        });			  
			this.destroy();
		}
	
		
		var __dlg = new iNet.iwebos.ui.mail.DialogEnterPassword(this,{
									address : __fetchInfo.accounts[index].address,
									okhScope: __dlg,
									okHandler: __okHandler,
									cancelHandler: function(){
										this.destroy();										
									}
								});
		__dlg.show(this);
	}, 
	
	setSmtpAccount: function(smtps, smtpDefault){
		var __smtps = smtps || [];
		
		var __smtpStore = this.comboSmtp.store;
		__smtpStore.removeAll();
		
		// add smtp account
		if(__smtps.length > 0){
			// add smtp account to combo box
			for(var __index= 0; __index < __smtps.length; __index++){
				__smtpStore.add(new Ext.data.Record({id: __smtps[__index].address, smtpAccount:__smtps[__index].smtpAccount}));								
			}
			
			// set smtp default
			this.comboSmtp.setValue(smtpDefault);						
		}else{
			this.comboSmtp.setValue('');
		}
	},
	
	setSignature: function(signs){
		var __signs = signs || [];
		
		var __signStore = this.signCombo.store;
		__signStore.removeAll();
		
		// add signature
		if(__signs.length > 0){
			// add smtp account to combo box
			for(var __index= 0; __index < __signs.length; __index++){
				var __temp =  __signs[__index];
				__signStore.add(new Ext.data.Record({name: __temp.name, content: __temp.content}));								
				
				// set sign default
				if(__temp.used == 'Y')
					this.signCombo.setValue(__temp.content);
			}
		}else{
			this.signCombo.setValue('');
		}
	},
	
	/**
	 * check has bcc
	 * @return {boolean}
	 */
	_fnHasBcc: function(){
		var __comp = Ext.getCmp(this.bccPanelId);
		var __height = __comp.getInnerHeight();
		return (__height > 0);
	}
});
