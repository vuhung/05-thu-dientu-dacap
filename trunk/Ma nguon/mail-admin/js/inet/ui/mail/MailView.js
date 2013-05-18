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
Ext.onReady(function() {
  Ext.QuickTips.init();
  Ext.form.Field.prototype.msgTarget = 'side';
  
  /**
   * email message panel.
   */
  var emailMessagePanel = new iNet.iwebos.ui.mail.grid.MailViewPanel();
  
  // create Action Mail panel.
  var actionMail = new Ext.Panel({
    id: 'mail-panel-action-id',
    frame: true,
    contentEl: 'action-mail',
    iconCls: 'mail-action-mail',
    titleCollapse: false,
    collapsible: false
  });
  
  
  this.treeFolder = new iNet.iwebos.ui.mail.TreeMailbox();
  
  var treeMailbox = new Ext.Panel({
    id: 'tree-mailbox-panel',
    title: iwebos.message.mail.create.domains,
    frame: true,
    layout: 'form',
    autoScroll: true,
    width: '100%',
    iconCls: 'tree-mail-box',
    items: [this.treeFolder]
  });
  
  /**
   * Action panel.
   */
  var actionPanel = new Ext.Panel({
    id: 'action-panel',
    region: 'west',
    split: true,
    collapsible: true,
    collapseMode: 'mini',
    width: 200,
    minSize: 200,
    border: false,
    baseCls: 'x-plain',
    items: [actionMail, treeMailbox]
  });
  
  
  /**
   * create search view port.
   */
  var viewport = new Ext.Viewport({
    layout: 'border',
    items: [new iNet.iwebos.ui.mail.Header(), actionPanel, emailMessagePanel]
  });
  
  var actionPanelBody = actionPanel.body;
  actionPanelBody.on('mousedown', emailMessagePanel.doAction, emailMessagePanel, {
    delegate: 'a'
  });
  actionPanelBody.on('click', Ext.emptyFn, null, {
    delegate: 'a',
    preventDefault: true
  });
});
