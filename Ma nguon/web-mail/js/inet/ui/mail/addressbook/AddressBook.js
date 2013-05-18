/*
   Copyright 2008 by Nguyen Thanh Vy (ntvy@truthinet.com.vn)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/**
 * Main [So dia chi]
 */
Ext.onReady(function(){
	Ext.QuickTips.init();
	
	/**
	 * Header Panel
	 */
	var head = new iNet.iwebos.ui.paperwork.Header(this, {
		region: 'north'
	});
	
	/**
	 * Tab Panel
	 */
	var tab = new iNet.iwebos.ui.mail.AddressBookPanel(this, {
		region: 'center',
		width: '100%'
	});
			
	/**
	 * ViewPort for Page
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [tab]
	});
});