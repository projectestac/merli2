document.getElementsByClassName = function(className) {
  var children = document.getElementsByTagName('*') || document.all;
  var elements = new Array();
 
  for (var i = 0; i < children.length; i++) {
    var child = children[i];
    var classNames = child.className.split(' ');
    for (var j = 0; j < classNames.length; j++) {
      if (classNames[j] == className) {
        elements.push(child);
        break;
      }
    }
  } 
  return elements;
} 

function addEvent(obj, evType, fn) { 
	if (obj.attachEvent) {
		var r = obj.attachEvent("on" + evType, fn);
		return r;
	}
	else {
		return false;
	}
}

function ieTooltips() {
	var tiplinks = document.getElementsByClassName('tooltip');	
	for (var i = 0; i < tiplinks.length; i++) {	
		tiplinks[i].onmouseover = function() { this.className += ' tooltiph' }
		tiplinks[i].onmouseout = function() { this.className = 'tooltip'; }
	}
}

addEvent(window, "load", ieTooltips);