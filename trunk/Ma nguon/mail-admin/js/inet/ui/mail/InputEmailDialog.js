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
iNet.iwebos.ui.mail.InputEmailDialog = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  
  var __mainPanel = new Ext.form.FormPanel({
    region: 'center',
    frame: true,
    border: false,
    anchor: '100%',
    items: [{
      border: false,
      layout: 'form',
      anchor: '100%',
      labelWidth: 60,
      bodyStyle: 'padding:15px',
      items: [{
        id: 'input-email-dialog-email-id',
        fieldLabel: iwebos.message.mail.email_address,
        xtype: 'textfield',
        vtype: 'email',
        border: false,
        anchor: '95%'
      }]
    }]
  });
  
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  
  iNet.iwebos.ui.mail.InputEmailDialog.superclass.constructor.call(this, {
    id: 'mail-create-domain-dialog',
    title: iwebos.message.mail.alias_input_email_title,
    iconCls: 'icon-email-createacc',
    region: 'center',
    anchor: '100%',
    width: 420,
    height: 270,
    modal: true,
    frame: true,
    layout: 'border',
    hideMode: 'offsets',
    items: [__mainPanel],
    buttons: [{
      text: iwebos.message.mail.ok,
      iconCls: 'ok-btn',
      scope: this,
      handler: this.okHandler
    }, {
      text: iwebos.message.mail.cancel,
      iconCls: 'cancel-btn',
      scope: this,
      handler: this.fnCancelHandler
    }]
  });
};

Ext.extend(iNet.iwebos.ui.mail.InputEmailDialog, iNet.iwebos.ui.common.dialog.TitleDialog, {
  ttitle: iwebos.message.mail.alias_input_email_title,
  
  btitle: iwebos.message.mail.alias_input_email_subtitle,
  /**
   * handle ok button.
   */
  fnOkHandler: function() {
    this.destroy();
  },
  
  /**
   * cancel function
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  }
});
