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
 * @class iNet.iwebos.ui.mail.DialogSignature
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogSignature
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogSignature = function(viewer, config){
    this.viewer = viewer;
    Ext.apply(this, config);
    this.prefix = 'iwebos-mail-signature-dialog';
	this.signatureName = this.prefix + '-name';
	this.signatureContent = this.prefix + '-content';
	
    var __panelMain = new Ext.form.FormPanel({
        frame: false,
        border: false,
        anchor: '100%',
        items: [{
            layout: 'column',
            border: false,
            anchor: '100%',
            items: [{
                columnWidth: 1,
                border: false,
                anchor: '100%',
                items: [{
                    layout: 'form',
                    border: false,
                    labelWidth: 70,
                    items: [{
                    	id : this.signatureName,
                        xtype: 'textfield',
                        border: false,
                        fieldLabel: iwebos.message.mail.signature_name,
                        anchor: '100%'
                    }]
                }]
            },{
                columnWidth: 1,
                border: false,
				layout: 'fit',
                    items: [{
                    	id: this.signatureContent,
                        xtype: 'htmleditor',
                        border: false,
						height: 160
                }]
            }]
        }]
    });
    
    // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    
    iNet.iwebos.ui.mail.DialogSignature.superclass.constructor.call(this, {
        id: 'mail-signature-dialog',
        title: iwebos.message.mail.create_signature,
        iconCls: 'icon-email-signature',
        region: 'center',
        anchor: '100%',
        width: 528,
        height: 400,
        modal: true,
        frame: true,
        resizable: false,
        layout: 'border',
        hideMode: 'offsets',
        items: [{
            region: 'center',
            frame: true,
            width: '100%',
            items: __panelMain
        }],
        buttons: [{
            text: iwebos.message.mail.ok,
            iconCls: 'ok-btn',
            handler: this.okHandler,
            scope: this.okhScope
        }, {
            text: iwebos.message.mail.cancel,
            iconCls: 'cancel-btn',
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};
Ext.extend(iNet.iwebos.ui.mail.DialogSignature, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.create_signature,
    /**
     * bottom title.
     */
    btitle: iwebos.message.mail.create_signature_for_user,
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    },
    
    /**
     * validate signature
     * @return {String}
     */
    validate : function(){
    	var __comp = Ext.getCmp(this.signatureName);
    	var __name = __comp.getValue().trim();
    	if(__name.length === 0){
    		return iwebos.message.mail.error_input_signature_name;
    	}
    	return '';
    },
    
    /**
     * get data signature
     * @return {}
     */
    getData: function(){
    	var __object = {};
		
		var	__comp;
    	// get siganture name
    	__comp = Ext.getCmp(this.signatureName);
    	__object['sname'] = __comp.getValue().trim();
    	
    	// get signature content
    	__comp = Ext.getCmp(this.signatureContent);
    	__object['scontent'] = __comp.getValue();
		
		__object['sid'] = 0;
		__object['used']='N';
		
    	return __object;
    },
    
    /**
     * load data signature 
     * @param {} record
     */
    loadData: function(record){
    	if(!record) return;
    	
    	var __data = record.data;
    
    	// set signature name
    	var __comp = Ext.getCmp(this.signatureName);
    	__comp.setValue(__data.sname);
    	
    	// set signature content
    	__comp = Ext.getCmp(this.signatureContent);
    	__comp.setValue(__data.scontent);
    }
});
