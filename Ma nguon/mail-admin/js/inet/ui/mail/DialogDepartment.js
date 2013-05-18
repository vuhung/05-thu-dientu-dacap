/**
 * @author tttduyen
 */
iNet.iwebos.ui.mail.DialogDepartment = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
	this.prefix = 'mail-admin-department';
	this.departmentNameId = this.prefix + '-department-name-id';
	this.adminNameId = this.prefix + '-admin-name-id';	
	
    this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [{
            columnWidth: 0.9,
            border: false,
            layout: 'form',
            anchor: '100%',
            items: [{
                id: this.departmentNameId,
				fieldLabel: iwebos.message.mail.create.department,
                xtype: 'textfield',				
                border: false,
                anchor: '97%'
            },{
				layout: 'column',
				columnWidth: 1,
				items: [{layout : 'form',
						border: false,
						columnWidth: 0.9,
						items:[{id: this.adminNameId,
								fieldLabel: iwebos.message.mail.create.manager,
                				xtype: 'textfield',
								readOnly:true,
								border: false,
                				anchor: '98%'}]
						},
						{layout : 'form',
						border: false,
						columnWidth: 0.1,
						items:[{id: 'select-admin-button',
                				xtype: 'button',
								border: false,
								iconCls:'icon-mail-add',
                				handler:this._fnSelectUser.createDelegate(this)}]
						}
						
				]
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
    
    
    iNet.iwebos.ui.mail.DialogDepartment.superclass.constructor.call(this, {
        id: 'mail-admin-department-id',
        title: iwebos.message.mail.create.department,
        iconCls: 'icon-email-adddomain',
        region: 'center',
        anchor: '100%',
        width: 400,
        height: 300,
        modal: true,
        frame: true,
        layout: 'border',
        hideMode: 'offsets',
        items: [this.text],
        buttons: [{
            text: iwebos.message.mail.create.save,
            iconCls: 'ok-btn',
            handler: this.fnOkHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: iwebos.message.mail.cancel,
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogDepartment, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.create.dialog_department_title,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.create.dialog_department_btitle,
	
	/**
	 * set data for dialog
	 * 
	 * @param {Object} id - the id of domain name
	 * @param {Object} action - the action
	 */
	setData: function(id, edit, action) {
		this.__owner = {id:id, edit:edit, action:action};
		/*if(id) {
			this.loadData(id);
		}*/
	},
	
	/**
	 * load domain data
	 * 
	 * @param {Object} id - the domain name
	 */
	loadData: function(id) {
		var baseParams = {iwct:'loadMailDomainContent', iwa:'view', action:'load'} ;
		// the domain name to load
		baseParams['text'] = id;
		
		// handle success event
		fnSuccess = function(result, request) {
			var data = result.responseText;
			data = eval('(' + data + ')');
			if(data) {
				// fill domain data				
				var component = Ext.getCmp(this.departmentNameId);
				component.setValue(data.text);
				component.setDisabled(true);
				
				component = Ext.getCmp(this.adminNameId);
				component.setValue(data.uname);
						
			}
		};
		
		// request data from server
		iNet.Ajax.request({
			url: 'jsonajax',
			params: baseParams,
			method: 'POST',
			scope: this,
			success: fnSuccess
		});
	},
	
	/**
	 * return the data object
	 */
	getData: function() {
		// create object to store data.
		var object = {} ;
		
		object['text'] = this.__owner.id;	
		object['edit'] = this.__owner.edit;
		
		var __comp = Ext.getCmp(this.departmentNameId);
		object['depName'] = __comp.getValue();
		
		object['code'] = this.__userCode;		
						
		return object;		
	},
	
	/**
	 * saving domain
	 */
	__onSave: function() {
		// get the data object
		var object = this.getData();
		
		// get action [save]
		var __save = this.__owner.action['save'];
		if(!__save) return;
		
		if(!__save.scope) {
			__save.fn(object);
		} else {
			__save.fn.apply(__save.scope, [object]);
		}		
	},

    /**
     * handle ok button.
     */
    fnOkHandler: function(){
    	// get value.
		var __depName = Ext.getCmp(this.departmentNameId).getValue();
		var __userName = Ext.getCmp(this.adminNameId).getValue();
		
		if(''===__depName || ''===__userName){
			Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : iwebos.message.mail.error_input_info,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
			return false;
		} 
		
		this.__onSave();
		this.destroy();
    },
	
	_fnSelectUser: function(){
		var dialog = new iNet.iwebos.ui.mail.DialogSearchAccount();
		var action = {fn:this._handleSelectUser.createDelegate(this), scope:this};
		// set action for dialog
		dialog.__setAction(action);
		dialog.show(this);
	},
	
	/**
	 * handle select user
	 */
	_handleSelectUser: function(object) {
		var __object = object || {};
		this.__userCode = __object.id;
		this.__userName = __object.uname;
		var __txtAdminName = Ext.getCmp(this.adminNameId);
		if(!!__txtAdminName)
			__txtAdminName.setValue(object.uname);
	},
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});
