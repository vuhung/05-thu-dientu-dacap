/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

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
/**
 * @class iNet.iwebos.ui.mail.grid.Active 
 */
iNet.iwebos.ui.mail.grid.Active = function(){
    Ext.apply(this, {
        width: 24,
        fixed: true,
        header: '',
        menuDisabled: true,
        dataIndex: 'active',
        renderer: function(value){
            if (value) 
                return '<div class="icon-email-true"></div>';
            else 
                return '<div class="icon-email-false"></div>';
        },
        init: function(grid){}
    });
};	

/**
 * @class iNet.iwebos.ui.mail.grid.iMail 
 */
iNet.iwebos.ui.mail.grid.isMail = function(){
    Ext.apply(this, {
      header: '<div class="icon-email-ismail"></div>',
        menuDisabled: true,
		width: 24,
        dataIndex: 'isMail',
        fixed: true,
        renderer: function(value){
            if (value) 
                return '<div class="icon-ismail-true"></div>';
            else 
                return '<div class="icon-ismail-false"></div>';
        },
        init: function(grid){}
    });
};