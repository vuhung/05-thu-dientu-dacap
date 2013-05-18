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
 * @class iNet.iwebos.ui.mail.TreeSelectContact
 * @extends Ext.TreePanel
 */
iNet.iwebos.ui.mail.TreeSelectContact = function() {
  var baseParams = {
    iwct: 'addressBookManagementContent',
    iwm: 'MAIL',
    iwc: 'READ_ONLY',
    iwa: 'search',
    action: 'search',
    mode: 'global',
    key: ''
  };
  this.treeroot = new Ext.tree.AsyncTreeNode({
    text: iwebos.message.mail.fullname,
    draggable: false,
    expanded: true,
    id: -1
  });
  // construction of this class to be started
  iNet.iwebos.ui.mail.TreeSelectContact.superclass.constructor.call(this, {
    id: 'mail-tree-select-user',
    useArrows: false,
    autoScroll: true,
    border: false,
    animate: true,
    selModel: new Ext.tree.MultiSelectionModel(),
    enableDD: true,
    draggable: true,
    containerScroll: true,
    loader: new iNet.iwebos.ui.common.tree.TreeLoaderWrapper({
      dataUrl: 'jsonajax',
      baseParams: baseParams
    }),
    root: this.treeroot
  });
  
};

Ext.extend(iNet.iwebos.ui.mail.TreeSelectContact, Ext.tree.TreePanel, {

  loadData: function(mode, key) {
    var __loader = this.getLoader();
    if (__loader) {
      __loader.baseParams.mode = mode;
      __loader.baseParams.key = key;
      __loader.load(this.treeroot, function() {
      });
    }
  }
});
