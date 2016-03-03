var reqCom;
var show=false;

function doCompletion(){
	var url = "extra.do?operation=autocomplete&value=" + escape(document.etiqForm.consulta.value);
    $.get(url, null,callbackComp);
    }
 
function callbackComp(text){
          parseMessageComp(text);
          
	//habilito deshabilito el link de 'Mostra al tesaurus'
	var a = comprovaTerme();
	if(a=="terme") {
		document.getElementById("a_showGo").setAttribute("class", "a_showGo_on");	
		document.getElementById("a_showGo").style.color="#FFF";	
	}
	else
	{
		document.getElementById("a_showGo").setAttribute("class", "a_showGo_off");	
		document.getElementById("a_showGo").style.color="grey";	
	}
}
 
 
function parseMessageComp(text)
	{
		mdiv = document.getElementById("resultTermes");
		if (text.length < 10){
			mdiv.style.display='none';
			notAvailableTerm();
		}else{	
			document.getElementById("buscaTermesInput").style.backgroundImage="url(web/images/lupa.png)";
			var alc=findPos(document.getElementById("buscaTermesInput"));
			alc+=26;
			mdiv.style.top=alc+"px";
			mdiv.style.display='block';
			mdiv.innerHTML = text;
		}
	}	

//torna la posicio absoluta d'un element (nomes la vertical)
function findPos(obj) {
	var curtop = 0;
	if (obj.offsetParent) {
		do {
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	return curtop;
}
			
	
	
	
function notAvailableTerm(){
	document.getElementById("buscaTermesInput").style.backgroundImage="url(web/images/lupa2.png)";
}
			
function mostraTitolRecRel(elem) {
	var idRecRel=$($(elem).parent().children('#recRel')).val();
	$($(elem).parent().children('#titRecRel')).load('web/etiq/nomTitolRecRel.jsp',{idRecRel: idRecRel})
}





	
	
	