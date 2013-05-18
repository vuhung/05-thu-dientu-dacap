/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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

iNet.iwebos.ui.mail.DialogForwardAddress = function(viewer, config) {
	this.viewer = viewer;
    Ext.apply(this, config);
    this.mailId = "mail-admin-forward-address";
    
     this.text = new Ext.form.FormPanel({
        region: 'center',
        frame: true,
        border: false,
        anchor: '100%',
        items: [{
           border: false,
           layout: 'form',
           anchor: '100%',
           labelWidth: 60,
           bodyStyle:'padding:15px',
           items: [{
               id: this.mailId,
		       fieldLabel: iwebos.message.mail.forward_address,
               xtype: 'textfield',			   
               border: false,
               vtype:'email',
               anchor: '92%'
           }]
        }]   
     });   
     
      // create handler.
    this.okHandler = this.okHandler || this.fnOkHandler;
    this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
    // get handler scope.
    this.okhScope = this.okhScope || this;
    this.cancelhScope = this.cancelhScope || this;
    
    iNet.iwebos.ui.mail.DialogForwardAddress.superclass.constructor.call(this, {
        id: 'mail-forward-address-dialog',
        title: iwebos.message.mail.input_address_need_to_forward,        
        region: 'center',
        anchor: '100%',
        width: 400,
        height: 270,
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
            iconCls: 'cancel-btn',
            handler: this.fnCancelHandler,
            scope: this.cancelhScope
        }]
    });
};

Ext.extend(iNet.iwebos.ui.mail.DialogForwardAddress, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
    ttitle: iwebos.message.mail.input_address_need_to_forward,
    /**
     * bottom title.
     */
    btitle: '',
    
    /**
     * set the action
     * 
     * @param {} action
     */
    setAction: function(action) {
    	this.action = action;
    },
    
    /**
     * handle ok button.
     */
    fnOkHandler: function(){
    	var mail = Ext.getCmp(this.mailId).getValue();
    	if('' == mail) {
    		Ext.MessageBox.show({
					title : iwebos.message.mail.error,
					msg : iwebos.message.mail.warning_input_address_need_to_forward,
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.ERROR
				});
    		return;
    	}
    	// get action [save]
		var __save = this.action['save'];
		if(!__save) return;
		
		if(!__save.scope) {
			__save.fn(mail);
		} else {
			__save.fn.apply(__save.scope, [mail]);
		}		
    	
		this.destroy();
    },
    
    /**
     * handle cancel button.
     */
    fnCancelHandler: function(){
        // close the current dialog.
        this.destroy();
    }
    
});    
