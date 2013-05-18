/*****************************************************************
   Copyright 2008 by iNet Solutions (info@truthinet.com.vn)

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
(function(){if(window.google&&google.gears){return}var a=null;if(typeof GearsFactory!=="undefined"){a=new GearsFactory()}else{try{a=new ActiveXObject("Gears.Factory")}catch(b){if(navigator.mimeTypes["application/x-googlegears"]){a=document.createElement("object");a.style.display="none";a.width=0;a.height=0;a.type="application/x-googlegears";document.documentElement.appendChild(a)}}}if(!a){return}if(!window.google){window.google={}}if(!google.gears){google.gears={factory:a}}})(); 
iNet={version:"1.0 BETA"};Ext.namespace("iNet","iNet.iwebos","iNet.iwebos.data","iNet.iwebos.data.db","iNet.iwebos.data.common","iNet.iwebos.ui","iNet.iwebos.ui.common","iNet.iwebos.ui.common.control","iNet.iwebos.ui.common.button","iNet.iwebos.ui.common.dialog","iNet.iwebos.ui.common.grid","iNet.iwebos.ui.common.store","iNet.iwebos.ui.common.tree","iNet.iwebos.ui.common.search","iNet.iwebos.ui.paperwork","iNet.iwebos.ui.mail","iNet.iwebos.ui.mail.grid","iNet.iwebos.ui.calendar","iNet.iwebos.ui.admin","iNet.iwebos.ui.common.form.HtmlEditorPlugins");Ext.BLANK_IMAGE_URL="images/s.gif";Ext.namespace("idesk.webos.message","idesk.webos.message.navigate","idesk.webos.message.paperwork","iwebos.message","iwebos.resources","iwebos.resources.paperwork","iwebos.resources.paperwork.dw","iwebos.resources.paperwork.ed","iwebos.message.action","iwebos.message.grouping","iwebos.message.paperwork","iwebos.message.paperwork.ed","iwebos.message.paperwork.dc","iwebos.message.paperwork.file","iwebos.message.paperwork.ed.newdocument","iwebos.message.paperwork.ed.process.docwork","iwebos.message.paperwork.ed.process.newdocwork","iwebos.message.paperwork.ed.process.docin","iwebos.message.paperwork.ed.process.task","iwebos.message.paperwork.ed.process.predoc","iwebos.message.paperwork.ed.process.notification","iwebos.message.paperwork.ed.watch","iwebos.message.paperwork.ed.msg","iwebos.message.paperwork.dw","iwebos.message.doc","iwebos.message.doc.status","iwebos.message.doc.priority","iwebos.message.doc.important","iwebos.message.doc.secure","iwebos.message.doc.book","iwebos.message.dialog","iwebos.message.followup","Ext.ux.UploadDialog.Dialog.i18n","iwebos.message.paperwork.ed.pagecopy","iwebos.message.paperwork.ed.pageexchange","iwebos.message.paperwork.ed.pagesubmit","iwebos.message.paperwork.ed.pageinfo","iwebos.message.mail","iwebos.message.mail.create","iwebos.message.paperwork.ed.report","iwebos.message.paperwork.report","iwebos.message.paperwork.ed.search","iwebos.message.mail.contact","iwebos.message.admin","iwebos.message.calendar");(function(){var b=Ext.form.ComboBox.prototype.onLoad;Ext.form.ComboBox.prototype.onLoad=function(){var g=8;var e=b.apply(this,arguments);var d=Math.max(this.minListWidth||0,this.el.getWidth());var f=false;Ext.each(this.view.getNodes(),function(h){if(!f){f=Ext.fly(h).getFrameWidth("lr")}if(h.scrollWidth){d=Math.max(d,(h.scrollWidth+g))}});if(d>0&&d-f!=this.list.getWidth(true)){this.list.setWidth(d);this.innerList.setWidth(d-this.list.getFrameWidth("lr"))}return e};Ext.override(Ext.tree.TreeNode,{ensureVisible:function(e){var d=this.getOwnerTree();if(d){d.expandPath(this.parentNode?this.parentNode.getPath():this.getPath(),false,function(){var f=d.getNodeById(this.id);d.getTreeEl().scrollChildIntoView(f.ui.anchor);Ext.callback(e)}.createDelegate(this))}}});var c=navigator.userAgent.toLowerCase();var a=function(d){return d.test(c)};Ext.apply(Ext,{isGecko2:Ext.isGecko&&a(/rv:1\.8/),isGecko4:Ext.isGecko&&a(/rv:2\.0/)})})();Ext.apply(iNet,{INET_GROUP_IMAGE:"../../../images/common/group.gif",INET_PERSON_IMAGE:"../../../images/common/person.png",INET_TITLE_DLG_HEIGHT:66,INET_PRIORITY_DATA:null,INET_SECURITY_DATA:null,INET_RATIFY_DATA:null,INET_FILTER_DATA:null,INET_DOC_BOOK_STATUS:null,INET_DOC_TYPE:null,INET_COPY_TYPE:null,INET_LOCAL_DB:"iwebos.db",INET_CONNECTION:null,INET_DLG_OK:"ok",INET_DLG_CANCEL:"cancel",INET_DATE_FORMAT:"dd/MM/yyyy",INET_PAGE_LIMIT:10,INET_USER_NAME:"",INET_USER_CODE:"",init:function(){},dateRenderer:function(a){return function(c){var b=new Date(c);return String.format('<div class="inet-date">{0}&nbsp;&nbsp;</div>',b.dateFormat(a))}},dateGroupRenderer:function(){var i=new Date().clearTime(true);var h=i.getFullYear();var d=i.getTime();var e=i.add(Date.DAY,-1).getTime();var a=i.getFirstDateOfWeek().getTime();var g=i.getFirstDateOfWeek().add(Date.DAY,-7).getTime();var b=i.getFirstDateOfMonth().getTime();var c=i.getFirstDateOfMonth().add(Date.MONTH,-1).getTime();var j=i.getFirstDateOfMonth().add(Date.MONTH,-2).getTime();var k=Date.parseDate(h+"/01/01","Y/m/d").getTime();var f=Date.parseDate(h+"/01/01","Y/m/d").add(Date.YEAR,-1).getTime();return function(n){if(!n){return iwebos.message.paperwork.other}var l=new Date(n);var m=l.clearTime(true).getTime();if(m==d){return idesk.webos.message.paperwork.today}if(m>d){return iwebos.message.paperwork.unavailable}if(m==e){return iwebos.message.paperwork.yesterday}if(m>=a){return iwebos.message.paperwork.currentweek}if(m>=g){return iwebos.message.paperwork.lastweek}if(m>=b){return iwebos.message.paperwork.currentmonth}if(m>=c){return iwebos.message.paperwork.lastmonth}if(m>=j){return iwebos.message.paperwork.lasttwomonth}if(m>=k){return iwebos.message.paperwork.currentyear}if(m>=f){return iwebos.message.paperwork.lastyear}return iwebos.message.paperwork.other}},statusRenderer:function(a){return a?idesk.webos.message.paperwork.booked:idesk.webos.message.paperwork.notbook},bookRenderer:function(a){return((!a||a=="")?idesk.webos.message.paperwork.notbook:a)},importantRenderer:function(a){a=(!a?"0":a);return String.format('<img src="../../../images/important/{0}.png" style="vertical-align: middle;width:8px;height:16px;"/>',a)},securityRenderer:function(a){a=(!a?"0":a);return String.format('<img src="../../../images/security/{0}.png" style="vertical-align:middle;width:16px;height:16px;" />',a)}});iNet.Database={version:"1.0"};Ext.apply(iNet.Database,{init:function(){if(iNet.INET_CONNECTION===null){iNet.INET_CONNECTION=iNet.iwebos.data.db.Connection.getInstance()}if(iNet.INET_CONNECTION!=null&&!iNet.INET_CONNECTION.isOpen()){iNet.INET_CONNECTION.open(iNet.INET_LOCAL_DB)}},nextId:function(a){var d=String(new Date().getTime()).substr(a);var c="ABCDEFGHIJKLMNOPQRSTUVWXYZ";for(var b=0;b<a;b++){d+=c.charAt(Math.floor(Math.random()*26))}return d},getConnection:function(){if(iNet.INET_CONNECTION!==null){return iNet.INET_CONNECTION}this.init();return iNet.INET_CONNECTION},getTransaction:function(){if(iNet.INET_CONNECTION!==null){return iNet.INET_CONNECTION.getTransaction()}this.init();return(iNet.INET_CONNECTION!=null)?iNet.INET_CONNECTION.getTransaction():null},getTable:function(a,b){if(iNet.INET_CONNECTION!==null){return iNet.INET_CONNECTION.getTable(a,b)}this.init();return(iNet.INET_CONNECTION!=null)?iNet.INET_CONNECTION.getTable(a,b):null}});iNet.Ajax={};Ext.apply(iNet.Ajax,{onBeforeRequest:function(c,a){if(a.maskEl){if(this.loadMask){this.loadMask.destroy();delete this.loadMask}var d=a.msg||idesk.webos.message.load_data;var b=!!a.removeMask;this.loadMask=new Ext.LoadMask(a.maskEl,{msg:d,removeMask:b});this.loadMask.show()}},onRequestCompleted:function(c,a,b){if(this.loadMask){this.loadMask.hide();this.loadMask.destroy();delete this.loadMask;Ext.Ajax.un("beforerequest",this.onBeforeRequest,this);Ext.Ajax.un("requestcomplete",this.onRequestCompleted,this);Ext.Ajax.un("requestexception",this.onRequestCompleted,this)}},ghostsuccess:function(a,b){this.redirect(a);var c=b.ghost||{};if(c.success&&typeof c.success=="function"){if(!c.scope){c.success(a,b)}else{c.success.apply(c.scope,[a,b])}}},ghostfailure:function(a,b){this.redirect(a);var c=b.ghost||{};if(c.failure&&typeof c.failure=="function"){if(!c.scope){c.failure(a,b)}else{c.failure.apply(c.scope,[a,b])}}},redirect:function(b){try{var a=Ext.util.JSON.decode(b.responseText)||{};if(a.type==="redirect"){window.location.href=a.target}}catch(c){}},request:function(a){Ext.Ajax.un("beforerequest",this.onBeforeRequest,this);Ext.Ajax.un("requestcomplete",this.onRequestCompleted,this);Ext.Ajax.un("requestexception",this.onRequestCompleted,this);Ext.Ajax.on("beforerequest",this.onBeforeRequest,this);Ext.Ajax.on("requestcomplete",this.onRequestCompleted,this);Ext.Ajax.on("requestexception",this.onRequestCompleted,this);var c=a.headers||{};Ext.apply(c,{"X-Ghost-Request":window.location.href});a.headers=c;var b={success:a.success,failure:a.failure};if(a.scope){b.scope=a.scope}delete a.scope;delete a.failure;delete a.success;a.ghost=b;a.scope=this;a.success=this.ghostsuccess;a.failure=this.ghostfailure;Ext.Ajax.request(a)}});Ext.onReady(function(){iNet.INET_PRIORITY_DATA=[[0,iwebos.message.doc.priority.normal],[1,iwebos.message.doc.priority.high],[2,iwebos.message.doc.priority.very_high]];iNet.INET_SECURITY_DATA=[[0,iwebos.message.doc.secure.normal],[1,iwebos.message.doc.secure.secure],[2,iwebos.message.doc.secure.very_secure],[3,iwebos.message.doc.secure.extreme_secure]];iNet.INET_DOC_BOOK_STATUS=[["all",iwebos.message.doc.book.allbook],["false",iwebos.message.doc.book.notbook],["true",iwebos.message.doc.book.booked]];iNet.INET_DOC_TYPE=[{id:0,value:"DR_ORIGINAL",text:iwebos.message.paperwork.ed.originaldocument},{id:1,value:"DR_COPY_ORIGINAL",text:iwebos.message.paperwork.ed.copydocument},{id:2,value:"DR_DRAFT",text:iwebos.message.paperwork.ed.draftdocument}];iNet.INET_DOC_WORK_STATUS=[["ALL",iwebos.message.paperwork.dw.status_all],["CREATING",iwebos.message.paperwork.dw.status_create],["VIEWONLY",iwebos.message.paperwork.dw.status_view_only],["PROCESSING",iwebos.message.paperwork.dw.status_processing]];iNet.INET_IMPORTANT_DATA=[[0,iwebos.message.doc.important.low],[1,iwebos.message.doc.important.normal],[2,iwebos.message.doc.important.high]];iNet.INET_COPY_TYPE=[[0,iwebos.message.paperwork.dc.copy_card_type_cf],[1,iwebos.message.paperwork.dc.copy_card_type_cp],[2,iwebos.message.paperwork.dc.copy_card_type_ci]];iNet.INET_RATIFY_DATA=[[0,iwebos.message.dialog.ratify_return],[1,iwebos.message.dialog.ratify_select_user],[2,iwebos.message.dialog.ratify_submit]];iNet.INET_FILTER_DATA=[[0,iwebos.message.dialog.filter_all],[1,iwebos.message.dialog.filter_group],[2,iwebos.message.dialog.filter_position],[3,iwebos.message.dialog.filter_leader],[4,iwebos.message.dialog.filter_document]]}); 
iNet.DocumentFormat={NEXT_FORMAT:6,OO_WORD:0,OO_EXCEL:1,OO_POWERPOINT:2,OO_GRAPH:3,OO_MATH:4,OO_DATABASE:5,MS_WORD:6,MS_EXCEL:7,MS_POWERPOINT:8,MS_ACCESS:9,MS_PROJECT:10,MS_VISIO:11,DOC_TEXT:12,DOC_HTML:13,DOC_XML:14,DOC_PDF:15,DOC_IMG:16,DOC_INET:17,DOC_OTHER:18,FORMAT:["oo-word","oo-excel","oo-powerpoint","oo-graph","oo-math","oo-database","ms-word","ms-excel","ms-powerpoint","ms-access","ms-project","ms-visio","doc-text","doc-html","doc-xml","doc-pdf","doc-img","doc-inet","doc-other"],EXT_FORMAT:["odt","ods","odp","odg","odf","odb","doc","xls","ppt","mdb","mpp","vsd","txt","html","xml","pdf","jpg","dtt","oth"],FORMAT_NAME:["Open Office Word","Open Office Excel","Open Office Powerpoint","Open Office Graph","Open Office Math","Open Office Database","Microsoft Word","Microsoft Excel","Microsoft Powerpoint","Microsoft Access","Microsoft Project","Microsoft Visio","Text Document","HTML Document","XML Document","PDF Document","Image Document","iNet Document","Others"],STATUS_NAME:[iwebos.message.doc.status.update,iwebos.message.doc.status.review,iwebos.message.doc.status.publish,iwebos.message.doc.status.notuse],isOpenOffice:function(a){return(a>=0)&&(a<=5)},isMSOffice:function(a){return(a>=6)&&(a<=11)},getExtension:function(b){if(b==undefined||b==""){return""}var a=b.lastIndexOf(".");if(a==-1){return""}return b.substr(a+1,b.length)},getFormat:function(b){for(var a=0;a<this.EXT_FORMAT.length;a++){if(b==this.EXT_FORMAT[a]){return a}}return this.DOC_OTHER}}; 