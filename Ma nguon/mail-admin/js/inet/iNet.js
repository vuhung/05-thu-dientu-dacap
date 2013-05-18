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
 * Define the iNet instance.
 */
iNet = {version: '1.0 BETA'} ;

/**
 * Register namespace.
 * 
 * <pre>
 * The list of namespace:
 * iNet: the main namespace, in this namespace define global information and
 * the application version.<br/>
 * iNet.iwebos: the empty namespace.<br/>
 * iNet.iwebos.data: this namespace contains all data structures. <br/>
 * iNet.iwebos.data.common: this namespace contains common data structures. <br/>
 * iNet.iwebos.ui: this namespace contains all ui components. <br/>
 * iNet.iwebos.ui.common: this namespace contains the common ui components. <br/>
 * iNet.iwebos.ui.paperwork: this namespace contains the paperwork ui components. <br/>
 * </pre>
 */ 
Ext.namespace("iNet", 
			  "iNet.iwebos", 
			  "iNet.iwebos.data",
			  "iNet.iwebos.data.db",
			  "iNet.iwebos.data.common",
			  "iNet.iwebos.ui", 
			  "iNet.iwebos.ui.common",
			  "iNet.iwebos.ui.common.control", 
			  "iNet.iwebos.ui.common.button",
			  "iNet.iwebos.ui.common.dialog",
			  "iNet.iwebos.ui.common.grid",
			  "iNet.iwebos.ui.common.store",
			  "iNet.iwebos.ui.common.tree",
			  "iNet.iwebos.ui.common.search",
			  "iNet.iwebos.ui.paperwork",
			  "iNet.iwebos.ui.mail",
			  "iNet.iwebos.ui.mail.grid",
			  "iNet.iwebos.ui.account",
			  "iNet.iwebos.ui.org"
	) ;
	
//~ Setting blank image =======================================================
Ext.BLANK_IMAGE_URL='images/s.gif';

/**
 * resource namespace.
 */
Ext.namespace(
		"idesk.webos.message",
		"idesk.webos.message.navigate",
		"idesk.webos.message.paperwork",
		"iwebos.message",
		"iwebos.resources",
		"iwebos.resources.paperwork",
		"iwebos.resources.paperwork.dw",
		"iwebos.resources.paperwork.ed",
		"iwebos.message.action",
		"iwebos.message.grouping",
		"iwebos.message.paperwork",
		"iwebos.message.paperwork.ed",
		"iwebos.message.paperwork.dc",
		"iwebos.message.paperwork.file",
		"iwebos.message.paperwork.ed.newdocument",
		"iwebos.message.paperwork.ed.process.docwork",
		"iwebos.message.paperwork.ed.process.newdocwork",
		"iwebos.message.paperwork.ed.process.docin",
		"iwebos.message.paperwork.ed.process.task",
		"iwebos.message.paperwork.ed.process.predoc",
		"iwebos.message.paperwork.ed.process.notification",
		"iwebos.message.paperwork.ed.watch",
		"iwebos.message.paperwork.ed.msg",
		"iwebos.message.paperwork.dw",
		"iwebos.message.doc",
		"iwebos.message.doc.status",
		"iwebos.message.doc.priority",
		"iwebos.message.doc.important",
		"iwebos.message.doc.secure",
		"iwebos.message.doc.book",
		"iwebos.message.dialog",
		"iwebos.message.followup",
		"Ext.ux.UploadDialog.Dialog.i18n",
		"iwebos.message.paperwork.ed.pagecopy",
		"iwebos.message.paperwork.ed.pageexchange",
		"iwebos.message.paperwork.ed.pagesubmit",
		"iwebos.message.paperwork.ed.pageinfo",		
		"iwebos.message.paperwork.ed.report",
		"iwebos.message.mail",
		"iwebos.message.mail.create",
		"iwebos.message.account",
		"iwebos.message.org"
	);
	
// expand the combo box.	
(function(){
    var originalOnLoad = Ext.form.ComboBox.prototype.onLoad;
    Ext.form.ComboBox.prototype.onLoad = function(){
        var padding = 8;
        var ret = originalOnLoad.apply(this,arguments);
        var max = Math.max(this.minListWidth || 0, this.el.getWidth());
        var fw = false;
		
        Ext.each(this.view.getNodes(), function(node){
            if(!fw){ fw = Ext.fly(node).getFrameWidth('lr'); }
            if(node.scrollWidth){ max = Math.max(max, (node.scrollWidth + padding)); }
        });
		
        if( max > 0 && max-fw != this.list.getWidth(true) ){
            this.list.setWidth(max);
            this.innerList.setWidth(max - this.list.getFrameWidth('lr'));
        }
        return ret;
    };  
    /*
    Ext.form.ComboBox.override({
		doQuery : function(q, forceAll){
			q = Ext.isEmpty(q) ? '' : q;
			var qe = {
				query: q,
				forceAll: forceAll,
				combo: this,
				cancel:false
			};
			if(this.fireEvent('beforequery', qe)===false || qe.cancel){
				return false;
			}
			q = qe.query;
			forceAll = qe.forceAll;
			if(forceAll === true || (q.length >= this.minChars)){
				if(this.lastQuery !== q){
					this.lastQuery = q;
					if(this.mode == 'local'){
						this.selectedIndex = -1;
						if(forceAll){
							this.store.clearFilter();
						}else{
							this.store.filter(this.displayField, q, true, false);
						}
						this.onLoad();
					}else{
						this.store.baseParams[this.queryParam] = q;
						this.store.load({
							params: this.getParams(q)
						});
						this.expand();
					}
				}else{
					this.selectedIndex = -1;
					this.onLoad();
				}
			}
		}
	});
	*/
	// FIXED: this.parentNode is null.
	Ext.override(Ext.tree.TreeNode, {
	    ensureVisible : function(callback){
	        var tree = this.getOwnerTree();
			if (tree) {
				tree.expandPath(this.parentNode ? this.parentNode.getPath() : this.getPath(), false, function(){
					var node = tree.getNodeById(this.id);
					tree.getTreeEl().scrollChildIntoView(node.ui.anchor);
					Ext.callback(callback);
				}.createDelegate(this));
			}
	    }
	});	
	
	// FIXED: improve tree performance.
//	Ext.apply(Ext.Element.prototype, {
//	    classReCache: {},
//	
//	    removeClass : function(className){
//	        if(!className){
//	            return this;
//	        }
//	        if(className instanceof Array){
//	            for(var i = 0, len = className.length; i < len; i++) {
//	            	this.removeClass(className[i]);
//	            }
//	        }else{
//	            var re = this.classReCache[className];
//	            if (!re) {
//	            	re = new RegExp('(?:^|\\s+)' + className + '(?:\\s+|$)', "g");
//	            	this.classReCache[className] = re;
//	            }
//	            var c = this.dom.className;
//	            if(re.test(c)){
//	                this.dom.className = c.replace(re, " ");
//	            }
//	        }
//	        return this;
//	    },
//	    hasClass : function(className){
//	    	return (this.dom.className.split(/\s+/).indexOf(className) != -1);
//	    },
//	});	
})();		

// define the default person and group image.
Ext.apply(iNet, {
	/**
	 * Default group image
	 */
	INET_GROUP_IMAGE : 'images/common/group.gif',
	
	/**
	 * Default person image.
	 */
	INET_PERSON_IMAGE : 'images/common/person.png',
	
	/**
	 * The title dialog template.
	 */
	INET_TITLE_DLG_HEIGHT: 66,
	
	/**
	 * The priority data.
	 */
	INET_PRIORITY_DATA: null,

	/**
	 * The security data.
	 */
	INET_SECURITY_DATA: null,
	
	/**
	 * The doc book status.
	 */
	INET_DOC_BOOK_STATUS: null,
	
	/**
	 * The document type.
	 */
	INET_DOC_TYPE: null,
	
	/**
	 * local database name.
	 */
	INET_LOCAL_DB: 'iwebos.db',
	
	/**
	 * local database connection.
	 */
	INET_CONNECTION : null ,
	
	/**
	 * the ok data.
	 */
	INET_DLG_OK: 'ok',
	
	/**
	 * the cancel data.
	 */
	INET_DLG_CANCEL: 'cancel',
	/**
	 * the format date
	 */
	INET_DATE_FORMAT: 'dd/MM/yyyy',
	/**
	 * the page limit
	 */
	INET_PAGE_LIMIT:10,
	/**
	 * the number domain to load in one request 
	 */
	INET_REPORT_DOMAIN_LIMIT:2,
	/**
	 * the number account to load in one request 
	 */
	INET_REPORT_ACCOUNT_LIMIT:200,
	/**
	 * the inet user name.
	 */
	INET_USER_NAME:"",
	/**
	 * the inet user code.
	 */
	INET_USER_CODE:"",
	/**
	 * initialization the iNet class.
	 */
	init:function(){},
	/**
	 * @return date that rendered.
	 */
	dateRenderer: function(format){
		/**
		 * format data.
		 */
		return function(value){
			// create date.
			var date = new Date(value) ;
			return String.format('<div class="inet-date">{0}&nbsp;&nbsp;</div>', date.dateFormat(format)) ;
		};
	},
	
	/**
	 * @return date group renderer. 
	 */
	dateGroupRenderer: function(){
		// create some cache value.
		var today = new Date().clearTime(true) ;
		var year = today.getFullYear() ;
		var todayTime = today.getTime() ;
		var yesterday = today.add(Date.DAY, -1).getTime() ;
		var currentWeekDays = today.getFirstDateOfWeek().getTime();
		var nextWeekDays = today.getFirstDateOfWeek().add(Date.DAY, -7).getTime() ;
		var currentMonthDays = today.getFirstDateOfMonth().getTime() ;
		var lastMonthsDays = today.getFirstDateOfMonth().add(Date.MONTH, -1).getTime() ;
		var lastTwoMonthsDays = today.getFirstDateOfMonth().add(Date.MONTH, -2).getTime() ;
		var currentYearDays = Date.parseDate(year + "/12/31","Y/m/d").getTime() ;
		var lastYearDays = Date.parseDate(year + "/12/31","Y/m/d").add(Date.YEAR, -1).getTime() ;
				
		/**
		 * @return the group data.
		 */
		return function(value){
			if(!value) return iwebos.message.paperwork.other;
			// create date.
			var date = new Date(value) ;
			var notime = date.clearTime(true).getTime() ;
			
			// compute date.
			if(notime == todayTime) return idesk.webos.message.paperwork.today ;
			if(notime > todayTime) return iwebos.message.paperwork.unavailable ;
			if(notime == yesterday) return iwebos.message.paperwork.yesterday ;
			if(notime >= currentWeekDays) return iwebos.message.paperwork.currentweek;
			if(notime >= nextWeekDays) return iwebos.message.paperwork.lastweek ;
			if(notime >= currentMonthDays) return iwebos.message.paperwork.currentmonth;
			if(notime >= lastMonthsDays) return iwebos.message.paperwork.lastmonth ;
			if(notime >= lastTwoMonthsDays) return iwebos.message.paperwork.lasttwomonth;
			if(notime >= currentYearDays) return iwebos.message.paperwork.currentyear;
			if(notime >= lastYearDays) return iwebos.message.paperwork.lastyear;			
			return iwebos.message.paperwork.other ;
		};
	},
	
	/**
	 * @return status format. 
	 */
	statusRenderer: function(value){
		return value ? idesk.webos.message.paperwork.booked : idesk.webos.message.paperwork.notbook;
	},
	
	/**
	 * @return book format
	 */
	bookRenderer: function(value){
		return ((!value || value == '') ? idesk.webos.message.paperwork.notbook : value) ;
	},
	
	/**
	 * @return the important image.
	 */
	importantRenderer: function(value){
		value = (!value ? "0" : value) ;
		return String.format('<img src="images/important/{0}.png" style="vertical-align: middle;width:8px;height:16px;"/>', value) ;
	},
	
	/**
	 * @return the security image.
	 */
	securityRenderer: function(value){
		value = (!value ? "0" : value) ;
		return String.format('<img src="images/security/{0}.png" style="vertical-align:middle;width:16px;height:16px;" />', value) ;
	}	
}) ;

/**
 * @class iNet.Database
 * @extends Object.
 * 
 * Manage iwebos local database.
 */
iNet.Database = {version: '1.0'};
Ext.apply(iNet.Database,{
	/**
	 * initialization the connection.
	 */
	init:function(){
		if(iNet.INET_CONNECTION === null){
			iNet.INET_CONNECTION = iNet.iwebos.data.db.Connection.getInstance() ;
		}
		
		// open the connection to database
		if(iNet.INET_CONNECTION != null && !iNet.INET_CONNECTION.isOpen()){
			iNet.INET_CONNECTION.open(iNet.INET_LOCAL_DB) ;
		}
	},
	/**
	 * the next identifier.
	 */
	nextId : function(len){
		var time = String(new Date().getTime()).substr(len) ;
		var s = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' ;
		for(var index = 0; index < len; index++){
			time += s.charAt(Math.floor(Math.random() * 26)) ;
		}		
		return time ;
	},
	/**
	 * @return the connection.
	 */
	getConnection : function(){
		// return current connection.
		if(iNet.INET_CONNECTION !== null) return iNet.INET_CONNECTION ;
		
		// connect to database and return the current connection.
		this.init() ;
		return iNet.INET_CONNECTION ;
	},
	/**
	 * @return the transaction to user.
	 */
	getTransaction : function(){
		// return current transaction.
		if(iNet.INET_CONNECTION !== null) return iNet.INET_CONNECTION.getTransaction() ;
		
		// connect to database and return the transaction.
		this.init() ;
		return (iNet.INET_CONNECTION != null) ? iNet.INET_CONNECTION.getTransaction() : null ;
	},
	/**
	 * @return the table to sub system.
	 * 
	 * @param {String} name - the given table name.
	 * @param {String} keyName - the given table key name.
	 */
	getTable : function(name, keyName){
		if(iNet.INET_CONNECTION !== null) return iNet.INET_CONNECTION.getTable(name, keyName) ;
		
		// connect to data and return the table.
		this.init() ;
		return (iNet.INET_CONNECTION != null) ? iNet.INET_CONNECTION.getTable(name, keyName) : null;
	}
}) ;

/**
 * @class Ajax.
 * 
 * Support user can be sent ajax request to server.
 */
iNet.Ajax = {} ;
Ext.apply(iNet.Ajax,{
	/**
	 * Before sending request to server.
	 * 
	 * @param {Connection} conn - the connection connect to server.
	 * @param {Object} options - the given request options.
	 */
	onBeforeRequest : function(conn, options){
		if(options.maskEl){
			// destroy load mask if exist.
			if(this.loadMask){
				this.loadMask.destroy() ;
				delete this.loadMask ;
			}
			
			// create load mask.
			var msg = options.msg || idesk.webos.message.load_data ;
			var removeMask = !!options.removeMask;
			this.loadMask = new Ext.LoadMask(options.maskEl, {msg: msg, removeMask: removeMask}) ;
			
			// show load mask.
			this.loadMask.show() ;
		}
	},
	
	/**
	 * Sending completed request to server.
	 * 
	 * @param {Connection} conn - the connection connect to server.
	 * @param {Object} response - the server response.
	 * @param {Object} options - the request options.
	 */
	onRequestCompleted : function(conn, response, options){
		// destroy load mask.
		if(this.loadMask){
			// hide the load mask.
			this.loadMask.hide() ;
			
			// destroy load mask.
			this.loadMask.destroy() ;
			
			// delete load mask.
			delete this.loadMask ;
			
			// completed request, remove all listeners.
			Ext.Ajax.un('beforerequest', this.onBeforeRequest, this) ;
			Ext.Ajax.un('requestcomplete', this.onRequestCompleted, this) ;
			Ext.Ajax.un('requestexception', this.onRequestCompleted, this) ;					
		}		
	},
	
	/**
	 * Preprocess success response.
	 */
	ghostsuccess : function(response, options){
		// redirect request.
		this.redirect(response) ;
		
		// process owner success and failure.
		var __owner = options.ghost || {} ;
		if(__owner.success && typeof __owner.success == 'function'){
			if(!__owner.scope){
				__owner.success(response,options) ;
			}else{
				__owner.success.apply(__owner.scope, [response, options]) ;
			}
		}
	},
	
	/**
	 * Preprocess failure response.
	 */
	ghostfailure : function(response, options){
		// redirect request.
		this.redirect(response) ;
		
		// process owner success and failure.
		var __owner = options.ghost || {} ;
		if(__owner.failure && typeof __owner.failure == 'function'){
			if(!__owner.scope){
				__owner.failure(response, options) ;
			}else{
				__owner.failure.apply(__owner.scope, [response, options]) ;
			}
		}
	},	
	
	/**
	 * Redirect request to the destination.
	 * 
	 * @param {XMLHttpRequest} response the XMLHttpRequest data.
	 */
	redirect : function(response){
		try{
			// process redirect function.
			var __redirect = Ext.util.JSON.decode(response.responseText) || {};
			if(__redirect.type === "redirect"){
				window.location.href = __redirect.target;
			}
		}catch(e){}				
	},
	
	/**
	 * Sending request to server.
	 * 
	 * @param the request options.
	 */
	request : function(options){
		// remove all listeners.
		Ext.Ajax.un('beforerequest', this.onBeforeRequest, this) ;
		Ext.Ajax.un('requestcomplete', this.onRequestCompleted, this) ;
		Ext.Ajax.un('requestexception', this.onRequestCompleted, this) ;							
		
		// register listener.
		Ext.Ajax.on('beforerequest', this.onBeforeRequest, this) ;
		Ext.Ajax.on('requestcomplete', this.onRequestCompleted, this) ;
		Ext.Ajax.on('requestexception', this.onRequestCompleted, this) ;
		
		// append the header.
		var __headers = options.headers || {} ;
		Ext.apply(__headers,{'X-Ghost-Request': window.location.href}) ;
		options.headers = __headers ;
		
		// pre process options.
		var __ghost = {success: options.success, failure: options.failure} ;
		if(options.scope) __ghost.scope = options.scope ;
		
		delete options.scope;
		delete options.failure;
		delete options.success;
		
		options.ghost = __ghost ;
		options.scope = this ;
		options.success = this.ghostsuccess ;
		options.failure = this.ghostfailure ;
		
		// send request to server.
		Ext.Ajax.request(options) ;
	}
}) ;

// initialization array resource.
Ext.onReady(function(){
	/**
	 * The priority data.
	 */
	iNet.INET_PRIORITY_DATA = [[0,iwebos.message.doc.priority.normal],[1,iwebos.message.doc.priority.high],[2,iwebos.message.doc.priority.very_high]] ;

	/**
	 * The security data.
	 */
	iNet.INET_SECURITY_DATA = [[0,iwebos.message.doc.secure.normal],[1,iwebos.message.doc.secure.secure],[2,iwebos.message.doc.secure.very_secure], [3,iwebos.message.doc.secure.extreme_secure]] ;
	
	/**
	 * The doc book status.
	 */
	iNet.INET_DOC_BOOK_STATUS = [['all',iwebos.message.doc.book.allbook],['false',iwebos.message.doc.book.notbook],['true',iwebos.message.doc.book.booked]];
	
	/**
	 * The document type.
	 */
	iNet.INET_DOC_TYPE = [{id:0,value:'DR_ORIGINAL',text:iwebos.message.paperwork.ed.originaldocument},{id:1,value:'DR_COPY_ORIGINAL',text:iwebos.message.paperwork.ed.copydocument},{id:2, value:'DR_DRAFT', text:iwebos.message.paperwork.ed.draftdocument}];
	
	/**
	 * Document work status.
	 */
	iNet.INET_DOC_WORK_STATUS = [['ALL',iwebos.message.paperwork.dw.status_all],['CREATING',iwebos.message.paperwork.dw.status_create],['VIEWONLY',iwebos.message.paperwork.dw.status_view_only],['PROCESSING',iwebos.message.paperwork.dw.status_processing]] ;
	
	/**
	 * The important data.
	 */
	iNet.INET_IMPORTANT_DATA = [[0,iwebos.message.doc.important.low],[1,iwebos.message.doc.important.normal],[2,iwebos.message.doc.important.high]] ;
});
