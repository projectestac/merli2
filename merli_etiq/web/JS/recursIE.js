/*SOURCE from  http://www.quirksmode.org/js/cross_dhtml.html */
	var DHTML = (document.getElementById || document.all || document.layers);
	var lastElement;
	
	//Variables del cercador
	var id="";
	var cerca="";
	var estat="";
	var data_i="";
	var data_f="";
	var id_unitat="";
	var id_catalogador="";
	var despl="";
	var estatsDisponibles="";
	var descripcio="";
	var vFisic="";
	var ordenacio="";
	

	function getObj(name)
	{
	  if (document.getElementById)
	  {
	  	this.obj = document.getElementById(name);
	  	if (this.obj != null)
		this.style = document.getElementById(name).style;
	  }
	  else if (document.all)
	  {
		this.obj = document.all[name];
	  	if (this.obj != null)
		this.style = document.all[name].style;
	  }
	  else if (document.layers)
	  {
	   	this.obj = document.layers[name];
	  	if (this.obj != null)
	   	this.style = document.layers[name];
	  }
	}
	
function init(){
	list = document.gestorForm.llistat.value;
	id = 0;
	if (list == "" || list == "propis" || list == "1"){
		id = 1;		
		document.getElementById("titol").innerHTML = titpropis;
	}
	if (list == "assign" || list == "2"){
		id = 2;		
		document.getElementById("titol").innerHTML = titassign;
	}
	if (list == "validar" || list == "3"){
		id = 3;		
		document.getElementById("titol").innerHTML = titvalidar;
	}
	if (list == "correct" || list == "4"){
		id = 4;		
		document.getElementById("titol").innerHTML = titcorrect;
	}
	if (list == "tots" || list == "5"){
		id = 5;		
		document.getElementById("titol").innerHTML = tittots;
	}

	document.getElementById("llistat").style.display = "none";
	changeList(id);
	document.gestorForm.reset();
}



function esborrar(idRec){
	if (confirm("segur que vols esborrar el recurs "+idRec+"?")){
		document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "delRec";
		document.gestorForm.submit();
	}
	return false;
}

function nou(){
		document.gestorForm.operation.value = "addRec";
		document.gestorForm.submit();
}

function modificar(){
		recurs = getRadioButtonSelectedValue(document.gestorForm.recurs);	
		document.gestorForm.idRecurs.value = recurs;
		document.gestorForm.operation.value = "setRec";		
		document.gestorForm.submit();
}
function modificar(idRec, llista){
		//recurs = getRadioButtonSelectedValue(document.gestorForm.recurs);	
		document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "setRec";		
		document.gestorForm.llistat.value = llista;		
		document.gestorForm.submit();
}
var actList = null;
function setLlistat(list,elem){;
	if (actList != null){
		actList.style.background="";
		actList.style.fontWeight="";
		actList.style.color="";
		actList.className = "";
	}
	//elem.style.background="#f9f9f9";
	elem.style.fontWeight="bold";
	elem.style.color="#d75007";
	elem.className = "selected";
	actList = elem;
	if (list == "assign"){
		id = 2;		
		document.getElementById("titol").innerHTML = titassign;
	}
	if (list == "validar"){
		id = 3;		
		document.getElementById("titol").innerHTML = titvalidar;
	}
	if (list == "correct"){
		id = 4;		
		document.getElementById("titol").innerHTML = titcorrect;
	}
	if (list == "propis"){
		id = 1;		
		document.getElementById("titol").innerHTML = titpropis;
	}
	if (list == "tots"){
		id = 5;		
		document.getElementById("titol").innerHTML = tittots;
	}
	if (list == "prova"){
		id = 13;		
		document.getElementById("titol").innerHTML = tittots;
	}
	
	document.getElementById("llistat").style.display =  "none";
	changeList(id);
}


function assignar(idRec, llista){
		alert ("Estem treballant per millorar la operacio.");
		veure(idRec, llista);
}

function veure(idRec, llista){
		document.gestorForm.idRecurs.value=idRec;
		document.gestorForm.llistat.value = llista;	
		document.gestorForm.operation.value = "veureRec";
		document.gestorForm.submit();
}
function validar(idRec, llista){
		//recurs = getRadioButtonSelectedValue(document.gestorForm.recurs);	
		document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "valRec";		
		document.gestorForm.llistat.value = llista;		
		document.gestorForm.submit();
}

function corregir(idRec,llista){
		document.gestorForm.idRecurs.value=idRec;
		document.gestorForm.llistat.value = llista;	
		document.gestorForm.operation.value = "veureRec";
		document.gestorForm.submit();
}

function traduir(idRec,llista){
		document.gestorForm.idRecurs.value=idRec;
		document.gestorForm.llistat.value = llista;	
		document.gestorForm.operation.value = "tradRec";
		document.gestorForm.submit();
}

function publicar(idRec,llista){
		document.gestorForm.idRecurs.value=idRec;
		document.gestorForm.llistat.value = llista;	
		document.gestorForm.operation.value = "pubRec";
		document.gestorForm.submit();
}
