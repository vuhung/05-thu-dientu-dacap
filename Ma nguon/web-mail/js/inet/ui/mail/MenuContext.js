var _divContext;
var _writeMailMenu;
var _addContactMenu;

var _replaceContext = false; // replace the system context menu?
var _mouseOverContext = false; // is the mouse over the context menu?
var _noContext = false;

function InitContext() {
	_divContext = $('divAddressContext');
	_writeMailMenu =$('ctx-write-mail');
	_addContactMenu =$('ctx-add-contact');
	
	_divContext.onmouseover = function() {
		_mouseOverContext = true;
	};
	_divContext.onmouseout = function() {
		_mouseOverContext = false;
	};
	document.body.onmousedown = ContextMouseDown;
}

function ContextMouseDown(event) {
	if (_noContext || _mouseOverContext)
		return;
	// IE is evil and doesn't pass the event object
	if (event == null)
		event = window.event;
	// we assume we have a standards compliant browser, but check if we have IE
	var target = event.target != null ? event.target : event.srcElement;
	// only show the context menu if the right mouse button is pressed
	// and a hyperlink has been clicked (the code can be made more selective)
	var button = (event.button == 0);
	if(Ext.isIE){
		button = (event.button == 5);
	}
	
	if (button && target.tagName.toLowerCase() == 'a'
			&& target.className == 'aaaaa') {
		_replaceContext = true;
		ContextShow(event);
	} else if (!_mouseOverContext) {
		_divContext.style.display = 'none';
	}
}

// call from the onContextMenu event, passing the event
// if this function returns false, the browser's context menu will not show up
function ContextShow(event) {
	if (_noContext || _mouseOverContext)
		return;
	// IE is evil and doesn't pass the event object
	if (event == null)
		event = window.event;
	// we assume we have a standards compliant browser, but check if we have IE
	var target = event.target != null ? event.target : event.srcElement;

	// document.body.scrollTop does not work in IE
	var scrollTop = document.body.scrollTop ? document.body.scrollTop
			: document.documentElement.scrollTop;
	var scrollLeft = document.body.scrollLeft ? document.body.scrollLeft
			: document.documentElement.scrollLeft;
	// hide the menu first to avoid an "up-then-over" visual effect
	_divContext.style.left = event.clientX + scrollLeft + 'px';
	_divContext.style.top = event.clientY + scrollTop + 'px';
	_divContext.style.display = 'block';
	_replaceContext = false;
	
	// ctx-write-mail
	_writeMailMenu.href = 'javascript:newComposerWAddress("' + event.target.text + '");';
	// ctx-write-mail
	_addContactMenu.href = 'javascript:newContact("' + event.target.text + '");';
	
	return false;
}

// comes from prototype.js; this is simply easier on the eyes and fingers
function $(id) {
	return document.getElementById(id);
}