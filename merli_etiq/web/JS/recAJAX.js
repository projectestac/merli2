var reqCom;
var show=false;

function changeList(list,pagina){
	var url = "ajaxrec.do?operation=" + escape(list)+"&value="+escape(pagina);
    jQuery.get(url, null, parseMessage);    
  	waiting(true);
}

function changeExportacioList(op,id,cerca,estat,estatsDisponibles,data_i,data_f,id_unitat,id_catalogador,despl,descripcio){
	var url = "export.do?operation="+op+"&value=-1&id="+escape(id)+"&cerca="+escape(cerca)+"&estat="+escape(estat)+"&estatsDisponibles="+escape(estatsDisponibles)+"&data_i="+escape(data_i)+"&data_f="+escape(data_f)+"&fisic=2&id_unitat="+escape(id_unitat)+"&id_catalogador="+escape(id_catalogador)+"&despl="+escape(despl)+"&descripcioC="+escape(descripcio);
 	jQuery.get(url, null, realitzaExportacio);    
  	waiting(true);
}

//op=1 --> Cerca normal; op=4 --> Tornada d'una exportacio
function changeCercaList(op,pagina,id,cerca,estat,estatsDisponibles,data_i,data_f,fisic,id_unitat,id_catalogador,despl,descripcio,ordenacio){
	var url = "ajaxrec.do?operation=" + escape(1)+"&value="+escape(pagina)+"&id="+escape(id)+"&cerca="+escape(cerca)+"&estat="+escape(estat)+"&estatsDisponibles="+estatsDisponibles+"&data_i="+escape(data_i)+"&data_f="+escape(data_f)+"&fisic="+escape(fisic)+"&id_unitat="+escape(id_unitat)+"&id_catalogador="+escape(id_catalogador)+"&despl="+escape(despl)+"&descripcioC="+escape(descripcio)+"&ord="+escape(ordenacio);
	if(op==1) jQuery.get(url, null, parseMessage);  
	else
	{
    	var html = $.ajax({url: url,async: false}).responseText;
	    parseMessage(html);
	}
  	waiting(true);
}

function realitzaExportacio(numRec)
{
	if(parseInt(numRec)==0)			alert("No hi ha fitxes publicades de recursos f\u00EDsics per exportar")
	else if(parseInt(numRec)>30000) alert("No es poden exportar m\u00E9s de 30000 fitxes a la vegada")
	else 							llistaExportacio(2);
	
	llistaCerca(0,4);
}

function parseMessage(text)
{
	mdiv = document.getElementById("lisrec");	
		
	pagina = text.substring(text.indexOf("<paginacio>")+11,text.indexOf("</paginacio>"));
	msg = text.substring(text.indexOf("</paginacio>")+12);

	if (msg.length < 10){
		errorLlistat();
	}else{
		mdiv.innerHTML = msg;		
		mdiv = document.getElementById("paginacio");	
		mdiv.innerHTML = pagina;
	}
	setTimeout("waiting(false)",300);		
}	
	
function waiting(yes){
	if (yes){
		mdiv = document.getElementById("lisrec");	
		mdiv.style.display='none';
		mdiv = document.getElementById("paginacio");	
		mdiv.style.display='none';
		mdiv = document.getElementById("waiting");
		mdiv.style.display="block";	
	}else{
		mdiv = document.getElementById("lisrec");	
		mdiv.style.display='block';
		mdiv = document.getElementById("paginacio");	
		mdiv.style.display='block';
		mdiv = document.getElementById("waiting");
		mdiv.style.display="none";	
	}
}
	
function errorLlistat(){
	waiting(false);
	alert ("no s'ha pogut carregar la informacio degut a problemes del servidor");
			
}

function loadInfoRec(item, imatge){
	elem = document.getElementById("item"+item);
	if(elem.style.display=="block"){
		elem.style.display="none";
	}else{
		elem.style.display="block";
	}
	
}

function loadCercaAvancada(imatge){
	if (imatge.src.indexOf("cercaAvancada")>0){		//mostrem
		imatge.src="web/images/cercaSimple.png";
		imatge.title="Cerca simple";
		boto =  document.getElementById("cerca1");
		boto.src="web/images/cercaDisabled.png";
		boto.setAttribute("onclick", "return;");
		capa = document.getElementById("menu_capcalera_cercador");
		capa.style.height="125px";
		capa = document.getElementById("dreta");
		capa.style.top="260px";
		elem = document.getElementById("barra_buscador_avancat");
		elem.style.display="block";
		initHintTextboxesAvancat();
	}else{											//amaguem
		imatge.src="web/images/cercaAvancada.png";
		imatge.title="Cerca avan&ccedil;ada";
		boto =  document.getElementById("cerca1");
		boto.src="web/images/cerca.png";
		boto.setAttribute("onclick", "javascript: llistaCerca(0);");
		
		capa = document.getElementById("menu_capcalera_cercador");
		capa.style.height = "72px";
		capa = document.getElementById("dreta");
		capa.style.top = "205px";
		elem = document.getElementById("barra_buscador_avancat");
		elem.style.display = "none";

		if(document.gestorForm.recursOnline)	document.gestorForm.recursOnline.checked=false;
		if(document.gestorForm.recursFisic)		document.gestorForm.recursFisic.checked=false;
		document.gestorForm.data_i.value="";
		document.gestorForm.data_f.value="";
		if(document.gestorForm.id_unitat)		document.gestorForm.id_unitat.value="";
		if(document.gestorForm.id_catalogador)	document.gestorForm.id_catalogador.value="";
	}
}

function canviarTitol(elem){	
	var capaTitol=document.getElementById("titol");
	/*text="Gesti&oacute; i visualitzaci&oacute; de fitxes ";*/
	ind=elem.value;
	if(ind==0)		text=tittots2;
	else if(ind==1)	text=titpropis;
	else if(ind==2)	text=titpendents;
	else if(ind==6)	text=titenproces;
	else if(ind==3)	text=titrealitzats;
	else if(ind==4)	text=titvalidar;
	else if(ind==7)	text=titnoacceptat;
	else if(ind==5)	text=tittots;
	else if(ind==8)	text=tittraduct;
	else if(ind==9)	text=tittraductes;
	else if(ind==10)text=tittraducten;
	else if(ind==11)text=tittraductoc;
	else if(ind==13)text=titagregapend;
	else if(ind==14)text=titagregafet;
	else if(ind==15)text=titretornades;
	else if(ind==16)text=titdenegades;
	else text=titperdefecte;
	
	capaTitol.innerHTML=text;
}