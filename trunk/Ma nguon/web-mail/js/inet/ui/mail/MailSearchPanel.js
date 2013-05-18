/**
 * @author tntan
 */
iNet.iwebos.ui.mail.MailSearchPanel = Ext.extend(Ext.Panel,{
    /**
	 * @cfg {Store} store - the given paging store.
	 */
	initComponent: function(){
    	this.store = this.store || {};
    	this.__owner = {field: 'all'};
    	var __seachMail = new Ext.app.SearchField({
    		id: 'mail-search-field-id',
        	store: this.store,
        	width: '100%',
        	onTrigger2Click: this.search.createDelegate(this)
   		});	
   		
   		var __panelSearch = new Ext.Panel({
   			id: 'mail-search-panel-id',
			height:25,
            layout: 'column',
            anchor: '100%',
            width:'100%',
            items: [{
            	width: 35,
                items: [{
                	width:32,
                    iconCls: 'icon-email-inbox',
                    xtype: 'button',
                    menu: {
                        id: 'email-search',
                        cls: 'form-search-trigger',
                        items: [{
                            text: iwebos.message.mail.search_all,
                            checked: true,
                            group: 'email-search-group',
                            scope: this,
                            iconCls: 'email-icon-searchall',
                            handler:  this.searchAll.createDelegate(this)
                        }, {
                            text: iwebos.message.mail.search_subject,
                            checked: false,
                            group: 'email-search-group',
                            scope: this,
                            iconCls: 'email-icon-searchsubject',
                            handler:  this.searchSubject.createDelegate(this)
                        }, {
                            text: iwebos.message.mail.search_date,
                            checked: false,
                            group: 'email-search-group',
                            scope: this,
                            iconCls: 'email-icon-searchdate',
                            handler: this.searchDate.createDelegate(this)
                        }, {
                            text: iwebos.message.mail.search_sender,
                            checked: false,
                            group: 'email-search-group',
                            scope: this,
                            iconCls: 'email-icon-searchsent',
                            handler : this.searchSender.createDelegate(this)
                        }]
                    }
                }]
            },{
            	columnWidth: 1,
            	anchor: '100%',
            	width:'100%',
            	items: [__seachMail]
            } ]
	       
	    });
   		this.items = [__panelSearch];
   		
   		// create toolbar.
		iNet.iwebos.ui.mail.MailSearchPanel.superclass.initComponent.call(this) ;	
	},
	
	/**
	 * Collects the search data and fire the search event.
	 */
	search: function(){
		// create search parameter.
		var params = [] ;
		var __search = Ext.getCmp('mail-search-field-id');
		var __keyword = __search.getRawValue() || '';
		__keyword=__keyword .replace(/'/g,"");
		params['key'] = __keyword;
		params['field'] = this.__owner.field;
		params['mode'] = 'advanced';
		
		// fire search event.
		
		this.fireEvent('search',params) ;
	},
	
	searchAll : function(){
		this.__owner.field = 'all';
	},
	
	searchSubject : function(){
		this.__owner.field = 'subject';
	},
	
	searchDate : function(){
		this.__owner.field = 'date';
	},
	
	searchSender : function(){
		this.__owner.field = 'sender';
	}
});