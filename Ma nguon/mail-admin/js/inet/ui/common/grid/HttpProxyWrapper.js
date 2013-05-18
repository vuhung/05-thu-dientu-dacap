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
 * @class iNet.iwebos.ui.common.grid.HttpProxyWrapper
 * @extends Ext.data.DataProxy
 * 
 * Prepare the request to send to server.
 */
iNet.iwebos.ui.common.grid.HttpProxyWrapper = function(conn){
    iNet.iwebos.ui.common.grid.HttpProxyWrapper.superclass.constructor.call(this);
    /**
     * The Connection object (Or options parameter to {@link Ext.Ajax#request}) which this HttpProxy uses to make requests to the server.
     * Properties of this object may be changed dynamically to change the way data is requested.
     * @property
     */
    this.conn = conn;
    this.useAjax = !conn || !conn.events;

    /**
     * @event loadexception
     * Fires if an exception occurs in the Proxy during data loading.  This event can be fired for one of two reasons:
     * <ul><li><b>The load call returned success: false.</b>  This means the server logic returned a failure
     * status and there is no data to read.  In this case, this event will be raised and the
     * fourth parameter (read error) will be null.</li>
     * <li><b>The load succeeded but the reader could not read the response.</b>  This means the server returned
     * data, but the configured Reader threw an error while reading the data.  In this case, this event will be 
     * raised and the caught error will be passed along as the fourth parameter of this event.</li></ul>
     * Note that this event is also relayed through {@link Ext.data.Store}, so you can listen for it directly
     * on any Store instance.
     * @param {Object} this
     * @param {Object} options The loading options that were specified (see {@link #load} for details)
     * @param {Object} response The XMLHttpRequest object containing the response data
     * @param {Error} e The JavaScript Error object caught if the configured Reader could not read the data.
     * If the load call returned success: false, this parameter will be null.
     */
};

Ext.extend(iNet.iwebos.ui.common.grid.HttpProxyWrapper, Ext.data.DataProxy,{
    /**
     * Load data from the configured {@link Ext.data.Connection}, read the data object into
     * a block of Ext.data.Records using the passed {@link Ext.data.DataReader} implementation, and
     * process that block using the passed callback.
     * @param {Object} params An object containing properties which are to be used as HTTP parameters
     * for the request to the remote server.
     * @param {Ext.data.DataReader} reader The Reader object which converts the data
     * object into a block of Ext.data.Records.
     * @param {Function} callback The function into which to pass the block of Ext.data.Records.
     * The function must be passed <ul>
     * <li>The Record block object</li>
     * <li>The "arg" argument from the load function</li>
     * <li>A boolean success indicator</li>
     * </ul>
     * @param {Object} scope The scope in which to call the callback
     * @param {Object} arg An optional argument which is passed to the callback as its second parameter.
     */
    load : function(params, reader, callback, scope, arg){
        if(this.fireEvent("beforeload", this, params) !== false){
            var  o = {
                params : params || {},
                request: {
                    callback : callback,
                    scope : scope,
                    arg : arg
                },
                reader: reader,
                callback : this.loadResponse,
                scope: this,
                headers:{'X-Ghost-Request': window.location.href}
            };
            if(this.useAjax){
                Ext.applyIf(o, this.conn);
                if(this.activeRequest){
                    Ext.Ajax.abort(this.activeRequest);
                }
                this.activeRequest = Ext.Ajax.request(o);
            }else{
                this.conn.request(o);
            }
        }else{
            callback.call(scope||this, null, arg, false);
        }
    },
	
    // private function.
    loadResponse : function(o, success, response){
    	// pre process response.
    	this.redirect(response) ;
    	
    	// post process response.
        delete this.activeRequest;
        if(!success){
            this.fireEvent("loadexception", this, o, response);
            o.request.callback.call(o.request.scope, null, o.request.arg, false);
            return;
        }
        var result;
        try {
            result = o.reader.read(response);
        }catch(e){
            this.fireEvent("loadexception", this, o, response, e);
            o.request.callback.call(o.request.scope, null, o.request.arg, false);
            return;
        }
        this.fireEvent("load", this, o, o.request.arg);
        o.request.callback.call(o.request.scope, result, o.request.arg, true);
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
	
    // private
    update : function(dataSet){},
    
    // private
    updateResponse : function(dataSet){}	
}) ;