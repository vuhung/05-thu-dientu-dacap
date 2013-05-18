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

Ext.onReady(function(){
	Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    
    var __main = new iNet.iwebos.ui.org.UserPanel();
    var __taskPanel = new Ext.Panel({
    	title: iwebos.message.org.user_task,
    	frame: true,
		id: 'user-task-action-id',
    	contentEl: 'action-user',
		//iconCls:'mail-action-mail',
        titleCollapse: false,
        collapsible: false,
        _fnDisabledAll: function(){
			if (this.body) {
				try {
					this.body.select('li').setDisplayed(false);
				} 
				catch (e) {
				}
			}
		}
    });
    
	
	/**
     * Action panel the panel contain action
     */
    var __leftPanel = new Ext.Panel({
    	region: 'west',
		id: 'action-panel',
    	split: true,
        collapsible: true,
        collapseMode: 'mini',
        width: 200,
        minWindth: 200,
        border: false,
        baseCls: 'x-plain',
        items: [__taskPanel]        
    });
    
    // The view port
    var viewport = new Ext.Viewport({
        layout: 'border',
        items: [new iNet.iwebos.ui.mail.Header(), __leftPanel, __main]
    });
    
    /**
	 * handle tab change function.
	 */
	var tabChange = function(ctab, activeTab, currentTab){
		var __object = {};
		// handle panel.
		__main.handlePanel(__taskPanel, activeTab.id, __object);
	};
	__main.on('beforetabchange', tabChange, this, {stopEvent:true}) ;	
    
    var actionPanelBody = __leftPanel.body;
    actionPanelBody.on('mousedown', __main.fnDoAction, __main, {
        delegate: 'a'
    });
    actionPanelBody.on('click', Ext.emptyFn, null, {
        delegate: 'a',
        preventDefault: true
    });
    
    __main.handlePanel(__taskPanel, 'manage-user-search-main-contend');
});