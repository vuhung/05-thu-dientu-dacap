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
 * @class iNet.iwebos.ui.paperwork.Header
 * @extends Ext.Panel
 * @constructor
 * Creates a new Header <br>
 * Add a div element with id=header for html page
 * @param {Object} viewer
 * @param {Object} config
 */
iNet.iwebos.ui.paperwork.Header = function(viewer, config){
	this.viewer = viewer;
	Ext.apply(this, config);
	
	iNet.iwebos.ui.paperwork.Header.superclass.constructor.call(this, {
		region: 'north',
		contentEl: 'header',
		border:false,frame:false,
		height: 55,
		width: '100%'
	});
};
Ext.extend(iNet.iwebos.ui.paperwork.Header, Ext.Panel,{});
 
