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
 * @class iNet.iwebos.ui.mail.DialogEnterPassword
 * @extends Ext.Window
 * @constructor
 * Creates a new DialogCreateFolder
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.mail.DialogEnterPassword = function(viewer, config) {
  this.viewer = viewer;
  Ext.apply(this, config);
  
  this.text = new Ext.form.FormPanel({
    region: 'center',
    bodyStyle: 'padding:5px 5px 0',
    frame: true,
    border: false,
    anchor: '100%',
    labelWidth: 58,
    items: [{
      border: false,
      layout: 'form',
      anchor: '100%',
      items: [{
        html: '<span class="x-form-field">' + iwebos.message.mail.account + ':  <b>' + this.address + '</b></span><br/>'
      }, {
        id: 'dialog-enter-password-txt',
        xtype: 'textfield',
        inputType: 'password',
        fieldLabel: ' ' + iwebos.message.mail.pass,
        border: false,
        anchor: '100%',
        listeners: {
          specialkey: function(field, e) {
            if (e.getKey() == Ext.EventObject.ENTER) {
              this.okHandler();
            }
          },
          scope: this
        }
      }]
    }]
  });
  
  this.text.on('render', function(panel) {
    Ext.getCmp('dialog-enter-password-txt').focus(true, 1000);
  }, this);
  
  // create handler.
  this.okHandler = this.okHandler || this.fnOkHandler;
  this.cancelHandler = this.cancelHandler || this.fnCancelHandler;
  // get handler scope.
  this.okhScope = this.okhScope || this;
  this.cancelhScope = this.cancelhScope || this;
  
  
  iNet.iwebos.ui.mail.DialogEnterPassword.superclass.constructor.call(this, {
    title: iwebos.message.mail.enter_password,
    iconCls: 'icon-email-enterpassword',
    region: 'center',
    anchor: '100%',
    width: 450,
    height: 265,
    modal: true,
    frame: true,
    layout: 'border',
    hideMode: 'offsets',
    items: [this.text],
    buttons: [{
      text: iwebos.message.mail.confirm,
      iconCls: 'ok-btn',
      handler: this.okHandler,
      scope: this.okhScope
    }, {
      text: iwebos.message.mail.dialog_createfolder_btn_cancel,
      iconCls: 'cancel-btn',
      handler: this.cancelHandler,
      scope: this.cancelhScope
    }]
  });
};
Ext.extend(iNet.iwebos.ui.mail.DialogEnterPassword, iNet.iwebos.ui.common.dialog.TitleDialog, {
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
  ttitle: iwebos.message.mail.confirm_password,
  /**
   * bottom title.
   */
  btitle: iwebos.message.mail.error_enter_password,
  
  /**
   * handle cancel button.
   */
  fnCancelHandler: function() {
    // close the current dialog.
    this.destroy();
  },
  
  getPassword: function() {
    var __comp = Ext.getCmp('dialog-enter-password-txt');
    return __comp.getValue().trim();
  }
});
