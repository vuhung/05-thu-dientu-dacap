Ext.namespace('Ext.ux');

Ext.ux.BoxSelect = Ext.extend(Ext.form.ComboBox, {
/**
     * @cfg {Number} growMin The minimum height to allow when grow = true (defaults to 60)
     */
    growMin : 20,
    /**
     * @cfg {Number} growMax The maximum height to allow when grow = true (defaults to 1000)
     */
    growMax: 60,
    growAppend : '&#160;\n&#160;',
    growPad : 0,
	grow: true,
    enterIsSpecial : false,
	hideTrigger: true,
	firstValue: '',
	
    /**
     * @cfg {Boolean} preventScrollbars True to prevent scrollbars from appearing regardless of how much text is
     * in the field (equivalent to setting overflow: hidden, defaults to false)
     */
    preventScrollbars: false,
    
	initComponent:function() {
		Ext.apply(this, {
			current: false,
			options: {
				className: 'bit',
				separator: ','
			}
		});
			
		Ext.ux.BoxSelect.superclass.initComponent.call(this);
	},
	
	initEvents : function(){
		Ext.form.ComboBox.superclass.initEvents.call(this);
        
        this.keyNav = new Ext.KeyNav(this.el, {
            "up" : function(e){
                this.inKeyMode = true;
                this.selectPrev();
            },

            "esc" : function(e){
                this.collapse();
            },

            "tab" : function(e){
                this.onViewClick(false);
                return true;
            },

            scope : this,

            doRelay : function(foo, bar, hname){
                if(hname == 'down' || this.scope.isExpanded()){
                   return Ext.KeyNav.prototype.doRelay.apply(this, arguments);
                }
                return true;
            },

            forceKeyDown : true
        });
        
        this.queryDelay = Math.max(this.queryDelay || 10,
                this.mode == 'local' ? 10 : 250);
        this.dqTask = new Ext.util.DelayedTask(this.initQuery, this);
        if(this.typeAhead){
            this.taTask = new Ext.util.DelayedTask(this.onTypeAhead, this);
        }
        if(this.editable !== false){
            this.el.on("keyup", this.onKeyUp, this);
            this.el.on("keypress", this.onKeyPress, this);
        }
        if(this.forceSelection){
            this.on('blur', this.doForce, this);
        }
    },
    
	// private
    onRender : function(ct, position){
        if(!this.el){
            this.defaultAutoCreate = {
                tag: "textarea",
                style:"width:100px;height:20px;",
                autocomplete: "off"
            };
        }
        Ext.ux.BoxSelect.superclass.onRender.call(this, ct, position);
        if(this.grow){
            this.textSizeEl = Ext.DomHelper.append(document.body, {
                tag: "pre", cls: "x-form-grow-sizer"
            });
            if(this.preventScrollbars){
                this.el.setStyle("overflow", "hidden");
            }
            this.el.setHeight(this.growMin + 2);
        }
    },
    
    afterRender : function(){
        Ext.app.SearchField.superclass.afterRender.call(this);
		
        // correct top position.
        if (Ext.isIE) {
        	this.el.setStyle("top","1px");
        	if(this.firstValue != undefined && this.firstValue != ''){
        		if(this.emptyText && this.el && v !== undefined && v !== null && v !== ''){
		            this.el.removeClass(this.emptyClass);
		        }
        		this.setRawValue(this.firstValue);
        		this.autoSize();
        	} 
        }
        
        this.firstValue = undefined;
    },
    
	onSelect: function(record, index) {
		this.collapse();
		var current = this.getRawValue();
		
		current = current.replace(/;/g,',');
        	// Semi-colon ;
        var lastSemi = current.lastIndexOf(',');
        var recipients = '';
        if(lastSemi > 0){
        	recipients = current.substring(0,lastSemi + 1) + ' ';	
        }
		var selected = record.data.text;
		// replace special character
		selected = selected.replace('&lt;','<');
		selected = selected.replace('&gt;','>');
		
		this.setValue(recipients + selected + ', ');
	},

	getValue: function(){
		return this.getRawValue();
	},
	
	setValue: function(value){
		if(this.emptyText && this.el && v !== undefined && v !== null && v !== ''){
            this.el.removeClass(this.emptyClass);
        }
        
        if(Ext.isIE && this.firstValue == ''){
        	this.firstValue = value;
        	return;
        }
        
       	this.setRawValue(value);
       	this.autoSize();
	},
	
	onKeyPress: function(e){
		if(e.getKey() == e.ENTER){
        	if(!this.isExpanded()){
        		e.stopEvent();
        	}
        }
	},
	
	// private
    onKeyUp : function(e){
        if(!e.isNavKeyPress()){
            this.autoSize();
        }else if(e.getKey() == e.DOWN){
            if(!this.isExpanded()){
            	this.onTriggerClick();
            }else{
                this.inKeyMode = true;
                this.selectNext();
            }
        }else if(e.getKey() == e.ENTER){
        	if(!this.isExpanded()){
        		return;
        	}else{
        		this.onViewClick();
        		this.delayedCheck = true;
                this.unsetDelayCheck.defer(10, this);
        	}
        }
        
        if(this.editable !== false && !e.isSpecialKey()){
            this.lastKey = e.getKey();
            this.dqTask.delay(this.queryDelay);
        }
    },
    
	/**
     * Automatically grows the field to accomodate the height of the text up to the maximum field height allowed.
     * This only takes effect if grow = true, and fires the {@link #autosize} event if the height changes.
     */
    autoSize : function(){
    	Ext.ux.BoxSelect.superclass.autoSize.call();
        if(!this.grow || !this.textSizeEl){
            return;
        }
        var el = this.el;
        var v = el.dom.value;
        var ts = this.textSizeEl;
        ts.innerHTML = '';
        ts.appendChild(document.createTextNode(v));
        v = ts.innerHTML;
		
        Ext.fly(ts).setWidth(this.el.getWidth(),true);
        
        if(v.length < 1){
            v = "&#160;&#160;";
        }else{
            if(Ext.isIE){
                v = v.replace(/\n/g, '<p>&#160;</p>');
            }
            v += this.growAppend;
        }
        ts.innerHTML = v;
        var h = this.lastHeight; 
        if(Ext.isIE){
        	if(ts.offsetHeight < 40){
        		h = 23;        	
	        }else if(ts.offsetHeight > 40 && ts.offsetHeight < 50){
	        	h = 40;
	        }else if(ts.offsetHeight > 70){
	        	h = 60;
	        }
        	
        }else{
        	if(ts.offsetHeight < 50){
        		h = 23;        	
	        }else if(ts.offsetHeight > 50 && ts.offsetHeight < 65){
	        	h = 40;
	        }else if(ts.offsetHeight > 70){
	        	h = 60;
	        }
        }
        
        
        if(h != this.lastHeight){
        	var adjust = h - this.lastHeight;
            this.lastHeight = h;
            this.el.setHeight(h);
            //return;
            if(this.firstTime)
            	this.fireEvent("autosize", this, h, adjust);
            else
            	this.firstTime = true;
        }
    },
	/**
	 * overide
	 * @param {} q
	 * @param {} forceAll
	 * @return {Boolean}
	 */
	doQuery : function(q, forceAll){
        if(q === undefined || q === null){
            q = '';
        }
        
        var __getKeyword = function(data){
        	if(data == '') return data;
        	
        	data = data.replace(/;/g,',');
        	// Semi-colon ;
        	var lastSemi = data.lastIndexOf(',');
        	if(lastSemi > 0){
        		var keyword = data.substring(lastSemi+1,data.length);
        		if(keyword.length> 0){
        			return keyword;
        		}else{
        			return '';
        		}
        	}else{
        		return data;
        	}
        };
        var qe = {
            query: __getKeyword(q),
            forceAll: forceAll,
            combo: this,
            cancel:false
        };
        if(this.fireEvent('beforequery', qe)===false || qe.cancel){
            return false;
        }
        q = qe.query.trim();
        forceAll = qe.forceAll;
        if(forceAll === true || (q.length >= this.minChars)){
            if(this.lastQuery !== q){
                this.lastQuery = q;
                if(this.mode == 'local'){
                    this.selectedIndex = -1;
                    if(forceAll){
                        this.store.clearFilter();
                    }else{
                    	if(q.length > 0){
                        	this.store.filter(this.displayField, q,true);
                    	}else{ 
                        	this.store.filter(this.displayField, '#$%&*',true);
                    	}
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
    },
    
    /**
	 * load data for store
	 * @param [{value,text}] data
	 */
	loadData: function(data){
		if(Ext.isArray(data)){
	 		if (Ext.isArray(data[0])){
	                this.store = new Ext.data.SimpleStore({
	                    fields: ['value','text'],
	                    data: data
	                });
	                this.valueField = 'value';
	            }else{
	                this.store = new Ext.data.SimpleStore({
	                    fields: ['text'],
	                    data: data,
	                    expandData: true
	                });
	                this.valueField = 'text';
	            }
	            this.displayField = 'text';
		}
	}
});

Ext.reg('boxselect', Ext.ux.BoxSelect);
