


function setRecursFisic(esfisic){
	if (esfisic){
		$("#c-idFisic").fadeIn();
		$("#c-url").removeClass("mandatory");	
		$("#c-caractRFisic").css('display', 'block');
		$("#c-disp").css('display', 'block');
	}else{
		$("#c-idFisic").fadeOut();
		$("#c-url").addClass("mandatory");
		$("#c-caractRFisic").css('display', 'none');
		$("#c-disp").css('display', 'none');
	}
	canviaComboFormats(esfisic);
}

function canviaComboFormats(esfisic){
	$("select[name='format']").val("");
	$("select[name='format']").load('web/etiq/comboFormats.jsp',{esfisic: ''});
	$("select[name='formatTots']").load('web/etiq/comboFormats.jsp',{esfisic: esfisic});
}

function addIdentificadorFisic(){
	var cl = $("#c-idfisic-copy").clone();

	$(cl).attr("id","");
	$("input", cl).val("");
	$("select", cl).val("");
	
	$("#c-idFisic").append(cl);
}


function delIdentificadoFisic (elem){
	if ($(elem).parent().parent().attr("id") == ""){
		$(elem).parent().parent().remove();
	}else{
		$("#c-idfisic-copy input").val("");
		$("#c-idfisic-copy select").val("");
	}	
}

function addRelacioRecurs(){
	var cl = $("#c-idrelacions-copy").clone();

	$(cl).attr("id","");
	$("input", cl).val("");
	$("#titRecRel", cl).text("");
	$("#tipusRelSel", cl).css("color", "#aaa");
	
	var idrec=$("input", cl);
	idrec.val("Identificador recurs");
	idrec.attr('class',HintClass);
	idrec.attr('hintText',"Identificador recurs");
	//idrec.hintText = "Identificador recurs";
  	idrec.focus(function() {
		  if (idrec.val().trim()==idrec.attr("hintText")) {
		    idrec.val("");
		    idrec.attr('class',HintActiveClass);
		  }
  	});
  	idrec.blur(function() {
		  if (idrec.val().trim().length==0) {
		    idrec.val(idrec.attr("hintText"));
		    idrec.attr('class',HintClass);
		  }
  	});
  	
	$("select", cl).val("");
	
	$("#c-idRelacions").append(cl);
	
}


function delRelacioRecurs (elem){
	if ($(elem).parent().parent().attr("id") == ""){
		$(elem).parent().parent().remove();
	}else{
		$("#c-idrelacions-copy input").val("");
		$("#c-idrelacions-copy input").val("");
		$("#c-idrelacions-copy select").val("");
		$("#c-idrelacions-copy select").css("color","#aaa");
		$("#c-idrelacions-copy #titRecRel").text("");
	}	
}


function checkButtons(){
	if (ultima == 1){
		$("#buttons-nav .last").hide();
		$("#buttons-nav .comprova").show();
	}else{
		$("#buttons-nav .last").show();
		$("#buttons-nav .comprova").hide();
	}
	if (ultima == pagina.length-1){
		$("#buttons-nav .next").hide();
	}else{
		$("#buttons-nav .next").show();
	}
	setStepSelected();
}

function setStepSelected(){
	$("#top_menu_varios .menuCatalogadorPassos .selectedMenu").removeClass("selectedMenu");
	$($("#top_menu_varios .menuCatalogadorPassos li a")[ultima-1]).addClass("selectedMenu");
}

function lastPage(){ 
	veure(ultima-1);	
}


function nextPage(){
	veure(ultima+1);
//	setStepSelected();	
}