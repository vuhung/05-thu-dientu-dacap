/*
 Copyright 2008 by Nguyen Thanh Vy (ntvy@truthinet.com.vn)
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
 * @class iNet.iwebos.ui.mail.ContactDialog
 * @extends iNet.iwebos.ui.common.dialog.TitleDialog
 *
 * Create the Contact dialog, that allow user can show/update the info addressbook
 * @constructor
 * @param {Object} config - the given configuration.
 */
iNet.iwebos.ui.mail.ContactDialog = Ext.extend(iNet.iwebos.ui.common.dialog.TitleDialog, {
  /**
   * @cfg {String} top title data.
   */
  ttitle: iwebos.message.mail.contact.dialog_title,
  /**
   * @cfg {String} bottom title data.
   */
  btitle: iwebos.message.mail.contact.dialog_desc,
  /**
   * @cfg {String} ok button title.
   */
  okTitle: Ext.MessageBox.buttonText.ok,
  /**
   * @cfg {String} cancel button title.
   */
  cancelTitle: Ext.MessageBox.buttonText.cancel,
  /**
   * @cfg {String} allow system show border to body eare or not.
   */
  bodyBorder: false,
  /**
   * @cfg {boolean} dragContent - does not drag content.
   */
  dragContent: false,
  /**
   * @cfg {String} closeAction - the action when user click close button at the top dialog.
   */
  closeAction: 'hide',
  /**
   * @cfg {boolean} resizable - does not allow user resize the component.
   */
  resizable: true,
  /**
   * @cfg {Number} minWidth - the minimize dialog width.
   */
  minWidth: 530,
  /**
   * @cfg {Number} minHeight - the minimize dialog height.
   */
  minHeight: 600,
  /**
   * @cfg {String} header image class.
   */
  hdImgCls: 'icon-contact-infor',
  /**
   * @cfg {String} bodyStyle - apply body style to component body.
   */
  bodyStyle: 'padding:1px;',
  resizable: true,
  /**
   * @cfg {function} okHandler - the ok handler that perfom when ok button clicked.
   */
  /**
   * @cfg {function} cancelHandler - the cancel handler that perform when cancel button clicked.
   */
  /**
   * @cfg {Object} hScope - the handler scope.
   */
  /**
   * @cfg: {String} dataUrl - the url point to the site that receiver and return the result.
   */
  /**
   * @cfg: {Object} baseParams - the given list of parameter.
   */
  /**
   * @cfg {String} components - the given components user want to listing.
   */
  /**
   * Init the user dialog from the given list of configuration.
   */
  initComponent: function() {
    // create identifier.
    Ext.form.Field.prototype.msgTarget = 'qtip';
    var __prefix = this.prefix || 'iwebos-contact-dialog';
    delete this.prefix;
    
    // setting up identifier.
    this.nameId = __prefix + '-name-id';
    this.dNameId = __prefix + '-displayname-id';
    this.emailId = __prefix + '-email-id';
    this.hphoneId = __prefix + '-home-phone-id';
    this.mphoneId = __prefix + '-mobile-phone-id';
    this.faxId = __prefix + '-fax-id';
    this.siteId = __prefix + '-site-id';
    this.groupId = __prefix + '-group-id';
    this.addressId = __prefix + '-address-id';
    // create form panel.
    var __main = new Ext.form.FormPanel({
      region: 'center',
      frame: false,
      border: false,
      anchor: '100%',
      labelWidth: 75,
      autoScroll: false,
      bodyStyle: 'padding:5px;',
      items: [{
        xtype: 'fieldset',
        title: iwebos.message.mail.contact.group_info,
        collapsible: false,
        autoHeight: true,
        items: [{
          layout: 'form',
          frame: false,
          border: false,
          items: [{
            id: this.nameId,
            xtype: 'textfield',
            fieldLabel: iwebos.message.mail.fullname,
            allowBlank: false,
            disabled: this.editor,
            anchor: '100%'
          }, {
            id: this.dNameId,
            xtype: 'textfield',
            fieldLabel: iwebos.message.mail.contact.dname,
            allowBlank: false,
            anchor: '100%'
          }, {
            id: this.emailId,
            xtype: 'textfield',
            vtype: 'email',
            fieldLabel: iwebos.message.mail.contact.email,
            allowBlank: false,
            anchor: '100%'
          }]
        }]
      }, {
        xtype: 'fieldset',
        title: iwebos.message.mail.contact.group_phone,
        collapsible: true,
        autoHeight: true,
        items: [{
          layout: 'form',
          frame: false,
          border: false,
          items: [{
            xtype: 'panel',
            layout: 'column',
            columnWidth: 1,
            frame: false,
            border: false,
            anchor: '100%',
            items: [{
              columnWidth: 0.5,
              layout: 'form',
              border: false,
              items: [{
                id: this.hphoneId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.hphone,
                anchor: '98%'
              }, {
                id: this.mphoneId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.mphone,
                anchor: '98%'
              }]
            }, {
              columnWidth: 0.5,
              layout: 'form',
              border: false,
              items: [{
                id: this.faxId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.fax,
                anchor: '100%'
              }, {
                id: this.siteId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.pager,
                anchor: '100%'
              }]
            }, {
              columnWidth: 1,
              layout: 'form',
              border: false,
              items: [{
                id: this.groupId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.group,
                anchor: '100%'
              }, {
                id: this.addressId,
                xtype: 'textfield',
                fieldLabel: iwebos.message.mail.contact.addr,
                anchor: '100%'
              }]
            }]
          }]
        }]
      }]
    });
    // create item.
    this.items = [__main];
    // set handler.
    var __okHandler = this.okHandler || this._fnOkHandler;
    var __cancelHandler = this.cancelHandler || this._fnCancelHandler;
    
    // get handler scope.
    this.hScope = this.hScope || this;
    
    // create button.
    this.buttons = [{
      text: this.okTitle,
      iconCls: 'ok-btn',
      handler: __okHandler,
      scope: this.hScope
    }, {
      text: this.cancelTitle,
      iconCls: 'cancel-btn',
      handler: __cancelHandler,
      scope: this.hScope
    }];
    
    this.layout = 'border';
    this.hideMode = 'offsets';
    // call superclass function.
    iNet.iwebos.ui.mail.ContactDialog.superclass.initComponent.call(this);
    
  },
  
  /**
   * Destroy dialog.
   */
  _fnOkHandler: function() {
    Ext.form.Field.prototype.msgTarget = 'side';
    this.destroy();
  },
  
  /**
   * Destroy dialog.
   */
  _fnCancelHandler: function() {
    Ext.form.Field.prototype.msgTarget = 'side';
    this.destroy();
  },
  /**
   * Set data in current panel.
   */
  setData: function(object) {
  
    var __component = Ext.getCmp(this.dNameId);
    __component.setValue(object.dname);
    
    __component = Ext.getCmp(this.nameId);
    __component.setValue(object.name);
    
    __component = Ext.getCmp(this.emailId);
    __component.setValue(object.mail);
    
    __component = Ext.getCmp(this.hphoneId);
    __component.setValue(object.hphone);
    
    __component = Ext.getCmp(this.mphoneId);
    __component.setValue(object.mphone);
    
    __component = Ext.getCmp(this.faxId);
    __component.setValue(object.fax);
    
    __component = Ext.getCmp(this.siteId);
    __component.setValue(object.pager);
    
    __component = Ext.getCmp(this.groupId);
    __component.setValue(object.group);
    
    __component = Ext.getCmp(this.addressId);
    __component.setValue(object.addr);
  },
  /**
   * Get data in current panel.
   */
  getData: function() {
    var __component = Ext.getCmp(this.dNameId);
    var __dname = __component.getValue() || '';
    
    __component = Ext.getCmp(this.nameId);
    var __name = __component.getValue() || '';
    
    __component = Ext.getCmp(this.emailId);
    var __email = __component.getValue() || '';
    
    __component = Ext.getCmp(this.hphoneId);
    var __hphone = __component.getValue() || '';
    
    __component = Ext.getCmp(this.mphoneId);
    var __mphone = __component.getValue() || '';
    
    __component = Ext.getCmp(this.siteId);
    var __site = __component.getValue() || '';
    
    __component = Ext.getCmp(this.faxId);
    var __fax = __component.getValue() || '';
    
    __component = Ext.getCmp(this.groupId);
    var __group = __component.getValue() || '';
    
    __component = Ext.getCmp(this.addressId);
    var __address = __component.getValue() || '';
    
    // return data.
    return {
      dname: __dname,
      name: __name,
      mail: __email,
      hphone: __hphone,
      mphone: __mphone,
      fax: __fax,
      pager: __site,
      group: __group,
      addr: __address
    };
  },
  
  /**
   * after render tree.
   */
  afterRender: function() {
    iNet.iwebos.ui.mail.ContactDialog.superclass.afterRender.call(this);
    
    //hander window
    this.on("hide", this.onHide, this);
  },
  /**
   * Destroy after the component is hidden.
   */
  onHide: function() {
    Ext.form.Field.prototype.msgTarget = 'side';
    this.destroy(this);
  },
  
  /**
   * set email address
   */
  setEmail: function(object) {
    __component = Ext.getCmp(this.nameId);
    __component.setValue(object.fullname);
    
    __component = Ext.getCmp(this.dNameId);
    __component.setValue(object.fullname);
    
    __component = Ext.getCmp(this.emailId);
    __component.setValue(object.email);
  }
});
