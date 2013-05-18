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
 * View Account panel
 */
Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    
    /**
     * Create main panel.
     */
    var __main = new iNet.iwebos.ui.account.AccountPanel();
	
    // create panel.
    var panelTop = new Ext.Panel({
    	title: iwebos.message.account.taskTitle,
        frame: true,
        contentEl: 'panelTop',
        titleCollapse: true,
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
     * Show west panel.
     */
    var actionPanel = new Ext.Panel({
        region: 'west',
        id: 'action-panel',
        split: true,
        collapsible: true,
        collapseMode: 'mini',
        width: 200,
        minWindth: 200,
        border: false,
        baseCls: 'x-plain',
        items: [panelTop]
    });
   
    /**
     * create page
     */
    var viewport = new Ext.Viewport({
        layout: 'border',
        items: [new iNet.iwebos.ui.mail.Header(), actionPanel, __main]
    });
    /**
	 * handle tab change function.
	 */
	var tabChange = function(ctab, activeTab, currentTab){
		var __object = {};
		// handle panel.
		__main.handlePanel(panelTop, activeTab.id, __object);
	};
	__main.on('beforetabchange', tabChange, this, {stopEvent:true}) ;	
	
    var actionPanelBody = actionPanel.body;
    actionPanelBody.on('mousedown', __main.doAction, __main, {
        delegate: 'a'
    });
    actionPanelBody.on('click', Ext.emptyFn, null, {
        delegate: 'a',
        preventDefault: true
    });
	
	__main.handlePanel(panelTop, 'main-manager-account');
	
});
