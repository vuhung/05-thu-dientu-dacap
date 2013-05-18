/**
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
 * function load Print page
 */
function loadPrintPage(){
	window.open('print.html','Print','toolbar=no,location=no,directories=no,status=yes,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
}
/**
 * show - hide header
 * @param {Object} val
 */
function showHeaderMailReader(val){
	var imgID = document.getElementById('mail-head-down-reader');
	var tblID = document.getElementById('header-mail-show-reader');
	
	var tagDivShow = '<div class="mail-head-down-reader" onclick="showHeaderMailReader(false);"></div>';
	var tagDivHide = '<div class="mail-head-right-reader" onclick="showHeaderMailReader(true);"></div>';
	
	imgID.innerHTML=(true==val)? tagDivShow : tagDivHide;
	tblID.style.display = (true==val)?'':'none';
	Ext.EventManager.fireWindowResize();
}

/**
 * @class iNet.iwebos.ui.mail.MailReader
 * @extends Ext.Panel
 * @constructor
 * Creates a new Panel 
 * @param {Object} viewer
 * @param {Object} config
 */
/**
 * Function Show - Hide Header mail
 * 
 */
iNet.iwebos.ui.mail.MailReader = function(viewer, config){
	this.viewer = viewer;
	Ext.apply(this, config);
	this.__id = 0;
	
	this.mailToolbar = new iNet.iwebos.ui.mail.Toolbar({
        prefix: 'iwebos-mail-reader-',
		region: 'north',
        width: '100%',
		height:27,
        frame: false,
		showmybutton: true,
        border: false
    });
    
  	// define a template to use for the detail view
    this.itemTplHeaderMarkup = ['<table width=100% border=0 cellspacing=0 cellpadding=0 >', 
	'<tr>',
		'<td width="16">&nbsp;</td>',
		'<td width="84" align="right" class="mail-subject-content" >&nbsp;' +iwebos.message.mail.subject +  ': &nbsp;</td>', 
		'<td class="mail-subject-content" colspan="3" >{subject}</td>',
	'</tr>', 
	'<tr>',
		'<td width="16" align="left" valign="top"><div id="mail-head-down-reader"><div class="mail-head-down-reader" onclick="showHeaderMailReader(false);"></div></div></td>',
		'<td align="right" valign="top" width="84">&nbsp;<b>'+iwebos.message.mail.grid_sender+': &nbsp;</b> </td>',
		'<td valign="top">{from}<br /></td>',
		'<td valign="top" align="right">&nbsp;<b>'+iwebos.message.mail.grid_sent+': &nbsp;</b> </td>',
		'<td valign="top" width="15%" align="right">{[this.format(values.sent)]}</td>', 
	'</tr>',
	'</table>',
	'<table width=100% border=0 cellspacing=0 cellpadding=0 id="header-mail-show-reader" style="display: ;">',
	'<tpl if="this.isCheck(to)">',
	'<tr>', 
		'<td width="100" align="right" valign="top">&nbsp;<b>'+iwebos.message.mail.template_mailto+': &nbsp;</b> </td>',
		'<td valign="top"><tpl for="to"><a onclick="">{[this.formatAddress(values.name,values.address)]},</a> </tpl></td>', 
	'</tr>', 
	'</tpl>',
	'<tpl if="this.isCheck(cc)">',
	'<tr>',
		'<td align="right" valign="top">',
			'<b>CC: &nbsp;</b>',
		'</td>', 
		'<td valign="top">',
			'<tpl for="cc">{[this.formatAddress(values.name,values.address)]}, </tpl>',
		'</td>',
	'</tr>',
	'</tpl>',
	'<tpl if="this.isCheck(attachments)">',
	'<tr>',
		'<td width="96" align="right" valign="top" NOWRAP>',
			'<tpl if="this.isDownloadAll(attachments)">',
				'<a class="icon-email-download-all" style="cursor:pointer;" onclick="MailService.downloadAttach(\'{id}\', \'all\')">&nbsp;&nbsp;&nbsp;&nbsp;</a>',
			'</tpl>',
			'<a onclick="MailService.downloadAttach(\'{id}\', \'all\')"><b>'+iwebos.message.mail.template_attach+': &nbsp;</b></a>',
		'</td>', 
		'<td valign="top" colspan="3">',
			'<tpl if="this.isEml(eml)">',
				'<tpl for="attachments">', 
					'<span class={[MailService.getCssAttachment(values.icon)]}>&nbsp;&nbsp;&nbsp;&nbsp;</span><a style="cursor:pointer;" onclick="MailService.downloadEmlAttach(\'{parent.contentId}\',\'{parent.emlKey}\', \'{fkey}\', \'{icon}\')">&nbsp;{fname}</a>, ',
				'</tpl>',
			'</tpl>',
			
			'<tpl if="this.isNotEml(eml)">',
				'<tpl for="attachments">', 
					'<span class={[MailService.getCssAttachment(values.icon)]}>&nbsp;&nbsp;&nbsp;&nbsp;</span><a style="cursor:pointer;" onclick="MailService.downloadAttach(\'{parent.id}\', \'{fkey}\', \'{icon}\')">&nbsp;{fname}</a>, ',
				'</tpl>',
			'</tpl>',
		'</td>',
	'</tr>',
	'</tpl>',
	'</table>'];
	
	this.itemTplBodyMarkup = ['<table width=100% border=0 cellspacing=0 cellpadding=0 bgcolor="#FFFFFF">',
	'<tr><td valign=top style="padding:10px;font-size:15px;" id="mail-body-document">&nbsp;{body}</td></tr></table>'];
    
    this.itemTpl = new Ext.XTemplate(this.itemTplHeaderMarkup.join(''), {
        format: function(value){
            return (value == 0) ? '' : new Date(value).format('d/m/Y');
        },
		isCheck: function(val){
        	return (false == val) ? '' : val;
     	},
     	isDownloadAll : function(attach){
			return (attach.length > 1? true: false); 
		},
		formatAddress: function(name, address){
			if(name != ''){
				return name + ' &lt;' + address + '&gt;';
			}else{
				return address;
			}
		},
		isEml: function(eml){
			return !!eml;
		},
		isNotEml: function(eml){
			return !eml;
		}
    });
	
	this.itemTpl2 = new Ext.XTemplate(this.itemTplBodyMarkup.join(''), { });
	
	this.viewHeader = new Ext.Panel({
        id: 'readPanelHeader',
        region: 'north',
		autoHeight: true,
		frame:true,
		border: false		
    });
	this.viewBody = new Ext.Panel({
        id: 'readPanelBody',
		region: 'center',
		frame:false,
		border: false,
		autoScroll:true
    });

    this.mainContent = new Ext.Panel({
        layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        autoScroll: true,
		items:[this.viewHeader,this.viewBody]
    });
	
	this.main = new Ext.Panel({
		layout: 'border',
        region: 'center',
        frame: false,
        border: false,
        items: [this.mailToolbar, this.mainContent]
	});

	this.mailToolbar.on('delete',this.fnDelete , this);
    
    this.mailToolbar.on('reply', this.fnReplyEmail,this);
    
    this.mailToolbar.on('replyall', this.fnReplyAllEmail, this);
    
    this.mailToolbar.on('forward', this.fnForwardEmail, this);
    
	iNet.iwebos.ui.mail.MailReader.superclass.constructor.call(this, {
		id: 'mail-reader',
		title: iwebos.message.mail.view_mail,
		iconCls: 'icon-email-read_mail',
		header: false,
		closable:true,
		frame: false,
		border: false,
		layout: 'border',
		items: [this.main]
	});
};

/**
 *
 * Extends Class Ext.Panel
 */
Ext.extend(iNet.iwebos.ui.mail.MailReader, Ext.Panel, {   
	loadInfo : function(data){
		if(data.eml == undefined){
			data.eml = false;
		}
		this.__id = data.id;
		//data = MailService.escape(data);
		this.itemTpl.overwrite(this.viewHeader.body,data);
		
		this.itemTpl2.overwrite(this.viewBody.body,data);
		
		Ext.EventManager.fireWindowResize();
	},
	
	/**
	 * EML info data
	 */
	loadEmlInfo: function(data){
		data.eml = true;
		this.loadInfo(data);
	},
	
	/**
	 * handler delete button click 
	 */
	fnDelete: function(){
 		var __mainTab = Ext.getCmp('main-tabs');
 		var __record = __mainTab.findByData(this.__id);
 		if(__record != null){
 			var mails = [];
 			mails[0] = __record;
 			__mainTab.fnDeleteMail(mails, this);
 		}
	},
	
	/**
	 * handler reply button click 
	 */
	fnReplyEmail: function(){
		var __mainTab = Ext.getCmp('main-tabs');
		__mainTab.fnComposerMail(this.__id, 'reply');
	},
	
	/**
	 * handler reply all button click 
	 */
	fnReplyAllEmail: function(){
		var __mainTab = Ext.getCmp('main-tabs');
		__mainTab.fnComposerMail(this.__id, 'replyA');
	},
	
	/**
	 * handler forward button click 
	 */
	fnForwardEmail: function(){
		var __mainTab = Ext.getCmp('main-tabs');
		__mainTab.fnComposerMail(this.__id, 'forward');
	}
});
