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
		document.getElementById("titol").innerHTML = titpendents;
	}
	if (list == "validar" || list == "3"){
		id = 3;		
		document.getElementById("titol").innerHTML = titvalidar;
	}
	if (list == "correct" || list == "4"){
		id = 4;		
		document.getElementById("titol").innerHTML = titcorrect;
	}
	if (list == "traduct" || list == "8"){
		id = 8;		
		document.getElementById("titol").innerHTML = tittraduct;
	}
	if (list == "traductes" || list == "9"){
		id = 9;		
		document.getElementById("titol").innerHTML = tittraductes;
	}
	if (list == "traducten" || list == "10"){
		id = 10;		
		document.getElementById("titol").innerHTML = tittraducten;
	}
	if (list == "traductoc" || list == "11"){
		id = 11;		
		document.getElementById("titol").innerHTML = tittraductoc;
	}
	if (list == "perenviar" || list == "6"){
		id = 6;		
		document.getElementById("titol").innerHTML = titperenviar;
	}
	if (list == "noacceptat" || list == "7"){
		id = 7;		
		document.getElementById("titol").innerHTML = titnoacceptat;
	}
	if (list == "tots" || list == "5"){
		id = 5;		
		document.getElementById("titol").innerHTML = tittots;
	}
	if (list == "agrega_pend" || list == "13"){
		id = 13;		
		document.getElementById("titol").innerHTML = titagregapend;
	}
	if (list == "agrega_fet" || list == "14"){
		id = 14;		
		document.getElementById("titol").innerHTML = titagregafet;
	}
	if (list == "15"){
		id = 15;		
		document.getElementById("titol").innerHTML = titretornades;
	}
	if (list == "16"){
		id = 16;		
		document.getElementById("titol").innerHTML = titdenegades;
	}

	document.getElementById("lisrec").style.display = "none";
	//changeList(id);
	llistaCerca(null,1);
	
	document.gestorForm.reset();
}


function esborrar(nom,idRec){
	if (confirm("Est\u00e0s segur/a que vols esborrar el recurs \""+nom+"\" ("+idRec+")?")){
		document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "delRec";
		document.gestorForm.submit();
	}
	return false;
}

function enviarAgrega(idRec,llista){
	if (confirm("do you want to send this recurs "+idRec+" to Agrega?")){
		document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "sendAgregaRec";
		document.gestorForm.llistat.value = llista;	
		document.gestorForm.submit();
	}
	return false;
}

function disponible(idRec){
	document.gestorForm.idRecurs.value = idRec;
	document.gestorForm.operation.value = "dispRec";
	document.gestorForm.submit();
	return false;
}

function no_disponible(idRec){
	var url = "web/etiq/disponibilitats.jsp?idRec="+idRec;
	jQuery.get(url, null, treuDisponibilitat);  
}

function treuDisponibilitat(text)
{
	var mis="";
	var idRec = text.substring(text.indexOf("<idRec>")+7,text.indexOf("</idRec>")).trim();
	var disp = text.substring(text.indexOf("<disp>")+6,text.indexOf("</disp>")).trim();
	
	if(disp=="true")
	{
		mis="La teva \u00e9s la \u00fanica unitat que t\u00e9 aquest recurs disponible, est\u00e0s segur que vols eliminar aquesta disponibilitat?";
	}
	else
	{
		mis="Est\u00e0s segur que vols eliminar la disponibilitat d'aquest recurs a la teva unitat?";
	}
	if (confirm(mis)){
    	document.gestorForm.idRecurs.value = idRec;
		document.gestorForm.operation.value = "noDispRec";
		document.gestorForm.submit();
	}
	return false;
}

function nou(){
		document.gestorForm.operation.value = "addRec";
		document.gestorForm.idRecurs.value = 0;
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


function setLlistat(list,elem){
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
	if (list == "traduct"){
		id = 8;		
		document.getElementById("titol").innerHTML = tittraduct;
	}
	if (list == "traductes"){
		id = 9;		
		document.getElementById("titol").innerHTML = tittraductes;
	}
	if (list == "traducten"){
		id = 10;		
		document.getElementById("titol").innerHTML = tittraducten;
	}
	if (list == "traductoc"){
		id = 11;		
		document.getElementById("titol").innerHTML = tittraductoc;
	}
	if (list == "propis"){
		id = 1;		
		document.getElementById("titol").innerHTML = titpropis;
	}
	if (list == "tots"){
		id = 5;		
		document.getElementById("titol").innerHTML = tittots;
	}
	if (list == "perenviar"){
		id = 6;		
		document.getElementById("titol").innerHTML = titperenviar;
	}
	if (list == "noacceptat"){
		id = 7;		
		document.getElementById("titol").innerHTML = titnoacceptat;
	}
	if (list == "prova"){
		id = 123;		
		document.getElementById("titol").innerHTML = tittots;
	}
	if (list == "agrega_pend"){
		id = 13;		
		document.getElementById("titol").innerHTML = titagregapend;
	}
	if (list == "agrega_fet"){
		id = 14;		
		document.getElementById("titol").innerHTML = titagregafet;
	}
	if (list == "15"){
		id = 15;		
		document.getElementById("titol").innerHTML = titretornades;
	}
	if (list == "16"){
		id = 16;		
		document.getElementById("titol").innerHTML = titdenegades;
	}
	
	document.getElementById("lisrec").style.display =  "none";
	changeList(id,0);
}

var leme;
function setLlistatOption(elem){
	/*if (actList != null){
		actList.style.background="";
		actList.style.fontWeight="";
		actList.style.color="";
		actList.className = "";
	}
	
	//elem.style.background="#f9f9f9";
	elem.style.fontWeight="bold";
	elem.style.color="#d75007";
	elem.className = "selected";
	*/
	id = parseInt(elem.value,10);
	actList = elem;
	switch(id){
	case 1:
		document.getElementById("titol").innerHTML = titenproces;
		break;
	case 2:
		document.getElementById("titol").innerHTML = titpendents;
		break;
	case 3:
		document.getElementById("titol").innerHTML = titrealitzats;
		break;
	case 4:
		document.getElementById("titol").innerHTML = titvalidar;
		break;
	case 5:		
		document.getElementById("titol").innerHTML = tittots;
		break;
	case 6:
		document.getElementById("titol").innerHTML = titenproces;
		break;
	case 7:	
		document.getElementById("titol").innerHTML = titnoacceptat;
		break;
	case 8:		
		document.getElementById("titol").innerHTML = tittraduct;
		break;
	case 9:		
		document.getElementById("titol").innerHTML = tittraductes;
		break;
	case 10:		
		document.getElementById("titol").innerHTML = tittraducten;
		break;
	case 11:		
		document.getElementById("titol").innerHTML = tittraductoc;
		break;
	case 13:		
		document.getElementById("titol").innerHTML = titagregapend;
		break;
	case 14:		
		document.getElementById("titol").innerHTML = titagregafet;
	}
	if (list == "15"){
		id = 15;		
		document.getElementById("titol").innerHTML = titretornades;
	}
	if (list == "16"){
		id = 16;		
		document.getElementById("titol").innerHTML = titdenegades;
	}
	
	document.getElementById("lisrec").style.display =  "none";
	changeList(id,0);
}

//op=1 --> Cerca normal; op=4 --> Tornada d'una exportacio
function llistaCerca(pag, op){
	document.getElementById("titol").innerHTML = titcercador;
	document.getElementById("lisrec").style.display =  "none";
	canviarTitol(document.getElementById("selestat"));

	getCampsCercador();
	if (pag == null) {
		if (document.gestorForm.valueSession) {
			pag = document.gestorForm.valueSession.value;
		} else {
			pag = 0;
		}
	}
	
	changeCercaList(op,pag,id,cerca,estat, estatsDisponibles, data_i, data_f, vFisic, id_unitat,id_catalogador,despl,descripcio,ordenacio);
}

//op=3 --> count num recursos a exportar; op=2 --> exportacio
function llistaExportacio(op){
	actionvell=document.gestorForm.action;
	
	getCampsCercador();

	if(document.gestorForm.recursOnline)		//si m'han seleccionat els online, no s'exporta res
	{
		online = document.gestorForm.recursOnline.checked;
		fisic = document.gestorForm.recursFisic.checked;
		if(online && !fisic)	id="-1";
	}
	
	if(op==3) changeExportacioList(op, id, cerca, estat, estatsDisponibles, data_i, data_f,id_unitat,id_catalogador,despl,descripcio);
	if(op==2)
	{
		document.gestorForm.action= "export.do?operation="+op+"&value=-1&id="+escape(id)+"&cerca="+escape(cerca)+"&estat="+escape(estat)+"&estatsDisponibles="+escape(estatsDisponibles)+"&data_i="+escape(data_i)+"&data_f="+escape(data_f)+"&fisic=2&id_unitat="+escape(id_unitat)+"&id_catalogador="+escape(id_catalogador)+"&despl="+escape(despl)+"&descripcioC="+escape(descripcio);
		document.gestorForm.submit();
		//window.open("export.do?operation="+op+"&value=-1&id="+escape(id)+"&cerca="+escape(cerca)+"&estat="+escape(estat)+"&estatsDisponibles="+escape(estatsDisponibles)+"&data_i="+escape(data_i)+"&data_f="+escape(data_f)+"&fisic=2&id_unitat="+escape(id_unitat)+"&id_catalogador="+escape(id_catalogador)+"&despl="+escape(despl)+"&descripcioC="+escape(descripcio));
	}
	
	document.gestorForm.action=actionvell;
}

function setPagina(pag, elem, escerca, maxpag){
	if(pag > maxpag) pag=maxpag;
	
	op=1;		//Cerca
	if (actList != null){
		actList.style.background="";
		actList.style.fontWeight="";
		actList.style.color="";
		actList.className = "";
	}
	elem.style.fontWeight="bold";
	elem.style.color="#d75007";
	elem.className = "selected";
	actList = elem;
	
	document.getElementById("lisrec").style.display =  "none";
	if (!escerca)
		changeList(id,pag);
	else{
		getCampsCercador();		
		changeCercaList(op,pag,id,cerca,estat,estatsDisponibles, data_i, data_f, vFisic, id_unitat,id_catalogador,despl,descripcio,ordenacio);
	}
}

function exporta()
{
	llistaExportacio(3);		//OPERACIO_COUNT
}

function getCampsCercador()
{
	id="";cerca="";estat="";data_i="";data_f="";id_unitat="";id_catalogador="";despl="";estatsDisponibles="";descripcio="";vFisic="0";ordenacio="";
	
	//camps
	if(document.gestorForm.id.className.indexOf("hintTextboxActive")!=-1){id = document.gestorForm.id.value;}
	if(document.gestorForm.cerca.className.indexOf("hintTextboxActive")!=-1){cerca = document.gestorForm.cerca.value;}
	estat = document.gestorForm.estat.value;
	if(document.gestorForm.data_i.className.indexOf("hintTextboxActive")!=-1){data_i = document.gestorForm.data_i.value;}
	if(document.gestorForm.data_f.className.indexOf("hintTextboxActive")!=-1){data_f = document.gestorForm.data_f.value;}
	if(document.gestorForm.id_unitat){id_unitat = document.gestorForm.id_unitat.value;}
	if(document.gestorForm.id_catalogador && document.gestorForm.id_catalogador.className.indexOf("hintTextboxActive")!=-1){id_catalogador = document.gestorForm.id_catalogador.value;}
	if(document.gestorForm.despl){despl = document.gestorForm.despl.value;}
	if(document.gestorForm.descripcioC.className.indexOf("hintTextboxActive")!=-1){descripcio = document.gestorForm.descripcioC.value;}
	if(document.gestorForm.recursOnline && document.gestorForm.recursFisic)
	{
		online = document.gestorForm.recursOnline.checked;
		fisic = document.gestorForm.recursFisic.checked;
		if(online && !fisic)	vFisic="1";
		if(fisic && !online)	vFisic="2";
		if(fisic && online)		vFisic="3";
	}

	//estats disponibles
	if(estat.charAt(0)=='0')
		for(i=1;i<document.gestorForm.estat.options.length;i++)
			estatsDisponibles+=document.gestorForm.estat.options[i].value+",";
			
	//ordenacio
	var selection = document.gestorForm.ordenacio;
	for (i=0; i<selection.length; i++) if (selection[i].checked == true) ordenacio=selection[i].value;	
}

function assignar(idRec, llista){
	alert("Estem treballant per millorar la operacio.");
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

function execMigracio(){
		document.gestorForm.operation.value = "migracio";
		document.gestorForm.submit();
}

function imprimir(){
	window.print(); 
	return false;
}
