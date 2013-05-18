/**
 * 
 * @class EmailImportantColumn
 */
iNet.iwebos.ui.common.grid.EmailImportantColumn = function(){
	// apply default configuration.
	Ext.apply(this, {
		width: 24,
		header: '<div class="icon-email-urgent"></div>',
		menuDisabled: true,
		fixed: true,
		dataIndex: 'priority',
		id: 'important-col',
		renderer: function(value){
			switch(value){
				case "LOW": return '<div class="icon-email-urgent-low"></div>';
				case "NORMAL": return '';
				case "HIGH": return '<div class="icon-email-urgent"></div>';
				default: return '' ;
			}
		},
		init:function(grid){}
	}) ;
} ;

/**
 * @class AttachmentColumn
 */
iNet.iwebos.ui.mail.grid.AttachmentColumn = function(){
    Ext.apply(this, {
        width: 24,
        header: '<div class="icon-email-attachment"></div>',
        menuDisabled: true,
        fixed: true,
        dataIndex: 'attached',
        id: 'attached-col',
        renderer: function(value){
            if (value) 
                return '<div class="icon-email-attachment"></div>';
            else 
                return '';
        },
        init: function(grid){
        }
    });
};


/**
 * @class StatusColumn
 */
iNet.iwebos.ui.mail.grid.StatusColumn = function(config){
    Ext.apply(this, {
        width: 24,
        header: '<div class="flag-col-hd"></div>',
        menuDisabled: true,
        fixed: true,
        dataIndex: 'flag',
        id: 'flag-col',
        renderer: function(value){
            switch (value) {
                case "WORK":
                    return '<div class="icon-email-work"></div>';
                case "PERSONAL":
                    return '<div class="icon-email-personal"></div>';
                case "TODO":
                    return '<div class="icon-email-todo"></div>';
				case "LATE":
                    return '<div class="icon-email-late"></div>';
				case "REPLY":
                    return '<div class="icon-email-reply"></div>';
				case "CALL":
                    return '<div class="icon-email-phone"></div>';
				case "FU":
                    return '<div class="icon-email-fu"></div>';
				case "FYI":
                    return '<div class="icon-email-fyi"></div>';
				case "REVIEW":
                    return '<div class="icon-email-approve_mail"></div>';
				case "CUSTOM":
                    return '<div class="icon-email-custom"></div>';
				case "NOTHING":
                    return '';
                default:
                    return '';
            }
        },
        init: function(grid){
        }
    });
    // apply user configuration.
	Ext.apply(this, config || {}) ;
};


/**
 * @class StatusColumn
 */
iNet.iwebos.ui.mail.grid.TypeColumn = function(){
    Ext.apply(this, {
        width: 24,
        header: '<div class="icon-email-unread_mail"></div>',
        menuDisabled: true,
        fixed: true,
        dataIndex: 'type',
        id: 'type-col',
        renderer: function(value){
            switch (value) {
                case "FORWARD":
                    return '<div class="icon-email-forward"></div>';
				case "REPLY":
                    return '<div class="icon-email-reply"></div>';
                default:
                    return '';
            }
        },
        init: function(grid){
        }
    });
};

/**
 * @class FlagColumn
 */
iNet.iwebos.ui.common.grid.FlagColumn = function(config){
	var grid;

	/**
	 * handle on mouse over event.
	 */
	function onMouseOver(event,target){
		if(Ext.fly(target).hasClass('task-check') || 
		   Ext.fly(target).hasClass('task-today') || 
		   Ext.fly(target).hasClass('task-tomorrow') || 
		   Ext.fly(target).hasClass('task-week') ||
		   Ext.fly(target).hasClass('task-nodate')){
			Ext.fly(target.parentNode).addClass('task-over') ;
		}
	};
	
	/**
	 * handle on mouse out event.
	 */
	function onMouseOut(event, target){
		if(Ext.fly(target).hasClass('task-check') || 
		   Ext.fly(target).hasClass('task-today') || 
		   Ext.fly(target).hasClass('task-tomorrow') || 
		   Ext.fly(target).hasClass('task-week') ||
		   Ext.fly(target).hasClass('task-nodate')){
			Ext.fly(target.parentNode).removeClass('task-over') ;
		}		
	};
	
	// apply default configuration.
	Ext.apply(this, {
		width: 24,
		header: '<div class="flag-col-hd"></div>',
		menuDisabled: true,
		fixed: true,
		id: 'flag-col',
		renderer: function(value){
			switch(value){
				case 0: return '<div class="task-today"></div>' ;
				case 1: return '<div class="task-tomorror"></div>';
				case 2: return '<div class="task-week"></div>' ;
				case 3: return '<div class="task-nodate"></div>' ;
				case 4: return '<div class="task-completed"></div>' ;
				default: return '<div class="task-check"></div>' ;
			}
			
			return '<div class="task-check"></div>' ;
		},
		init:function(gd){
			grid = gd;
			grid.on('render', function(){
				var view = grid.getView() ;
				view.mainBody.on('mouseover', onMouseOver) ;
				view.mainBody.on('mouseout', onMouseOut) ;
			}) ;
		}
	}) ;	
	
	// apply user configuration.
	Ext.apply(this, config || {}) ;
};