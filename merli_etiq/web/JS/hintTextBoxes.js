//------ Text gris inicial dels camps dels formularis

// Script by Drew Noakes
// http://drewnoakes.com
// 14 Dec 2006 - Initial release
// 08 Jun 2010 - Added support for password textboxes

var HintClass = "hintTextbox";
var HintActiveClass = "hintTextboxActive";

// define a custom method on the string class to trim leading and training spaces
String.prototype.trim = function() { return this.replace(/^\s+|\s+$/g, ''); };

function initHintTextboxes() {
	  var inputs = document.getElementsByTagName('input');
	  for (i=0; i<inputs.length; i++) {
	    var input = inputs[i];
	    if (input.type!="text" && input.type!="password")
	    {
	      continue;
	    }
	      
	    if (input.className.indexOf(HintClass)!=-1) {
			if (input.name == 'id') {
				input.hintText = 'Id del recurs';
			}
			if (input.name == 'cerca') {
				input.hintText = 'Titol';
			}
	      input.onfocus = onHintTextboxFocus;
	      input.onblur = onHintTextboxBlur;
	    }
	  }
}
function initHintTextboxesAvancat() {
	input = document.getElementById("data_i");
	if (input.value == '') {
		input.value = "Posterior a";
	  	input.className = HintClass;
	}
	input.hintText = "Posterior a";
  	input.onfocus = onHintTextboxFocus;
  	input.onblur = onHintTextboxBlur;
  	
  	input = document.getElementById("data_f");
	if (input.value == '') {
		input.value = "Anterior a";
	  	input.className = HintClass;
	}
	input.hintText = "Anterior a";
  	input.onfocus = onHintTextboxFocus;
  	input.onblur = onHintTextboxBlur;
  	 	
  	if(input = document.getElementById("id_catalogador"))
  	{
	  	//input = document.getElementById("id_catalogador");
  		if (input.value == '') {
  			input.value = "Catalogat per";
  		  	input.className = HintClass;
  		}
		input.hintText = "Catalogat per";
	  	input.onfocus = onHintTextboxFocus;
	  	input.onblur = onHintTextboxBlur;
  	}
  	input = document.getElementById("descripcioC");
	if (input.value == '') {
		input.value = "Resum";
	  	input.className = HintClass;
	}
	input.hintText = "Resum";
  	input.onfocus = onHintTextboxFocus;
  	input.onblur = onHintTextboxBlur;
}

function initHintTextboxesRelacions() {
	input = document.getElementById("recRel");
	if (input.value == '') {
		input.value = "Identificador recurs";
	  	input.className = HintClass;
	}
	input.hintText = "Identificador recurs";
  	input.onfocus = onHintTextboxFocus;
  	input.onblur = onHintTextboxBlur;
}

function onHintTextboxFocus() {
  var input = this;
  if (input.value.trim()==input.hintText) {
    input.value = "";
    input.className = HintActiveClass;
  }
}

function onHintTextboxBlur() {
  var input = this;
  if (input.value.trim().length==0) {
    input.value = input.hintText;
    input.className = HintClass;
  }
}







	
	
	