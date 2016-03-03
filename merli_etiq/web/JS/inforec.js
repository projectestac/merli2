/*SOURCE from  http://www.quirksmode.org/js/cross_dhtml.html */
	var DHTML = (document.getElementById || document.all || document.layers);
	var lastElement;
	
function assignar(user){
	document.veuRecForm.usuari.value = user;
	document.veuRecForm.operation.value = "assign";		
	document.veuRecForm.submit();
}

var lastUser = null;
function chooseUser(elem, user){
	if (lastUser != null){
		lastUser.style.background = "";
		lastUser.style.color = "";
	}
	document.veuRecForm.usuari.value = user;
	elem.style.background="white";
	elem.style.color="#d75007";
	lastUser = elem;
}


var actOper = null;
function setOperation(oper, elem){
	if (actOper != null){
		actOper.style.background="";
		actOper.style.fontWeight="";
		actOper.style.color="";
		actOper.className = "";
	}
	//elem.style.background="#f9f9f9";
	//elem.style.fontWeight="bold";
	elem.style.color="#eee";
	elem.className = "selected";
	actOper = elem;

	if (oper == "assign"){
		document.getElementById('negar').style.display="none";
		document.veuRecForm.operation.value = oper;
		document.getElementById('assignar').style.display="block";
	}
	if (oper == "validar"){
		document.getElementById('negar').style.display="none";
		document.veuRecForm.operation.value = oper;
		document.getElementById('assignar').style.display="none";
		if (confirm("Segur que el vols validar?"))
			document.veuRecForm.submit();
	}
	if (oper == "denegar"){
		document.getElementById('assignar').style.display="none";
		document.getElementById('negar').style.display="block";
		document.veuRecForm.operation.value = oper;
	}
	if (oper == "retornar"){
		document.getElementById('assignar').style.display="none";
		document.getElementById('negar').style.display="block";
		document.veuRecForm.operation.value = oper;
	}
	if (oper == "editar"){
		document.getElementById('negar').style.display="none";
		document.veuRecForm.operation.value = oper;
		document.getElementById('assignar').style.display="none";
		document.veuRecForm.submit();
	}
	if (oper == "titolRec"){
		document.getElementById('descripcio').style.display="none";
		document.getElementById('rights').style.display="none";
		document.getElementById('titolRec').style.display="block";
		document.veuRecForm.operation.value = "corregir";
	}
	if (oper == "descripcio"){
		document.getElementById('descripcio').style.display="block";
		document.getElementById('rights').style.display="none";
		document.getElementById('titolRec').style.display="none";
		document.veuRecForm.operation.value = "corregir";
	}
	if (oper == "rights"){
		document.getElementById('descripcio').style.display="none";
		document.getElementById('rights').style.display="block";
		document.getElementById('titolRec').style.display="none";
		document.veuRecForm.operation.value = "corregir";
	}
}

function setMissatge(op, idrec, titrec)
{
	document.getElementById('assmis').value="[Merl\u00ED][Recurs "+op+"][id:"+idrec+"] "+titrec;
}

function cancel(elem){
	if (elem == "titolRecTrad"){
		document.veuRecForm.title.value = titleCaOrig;
		document.veuRecForm.titleEs.value = titleEsOrig;
		document.veuRecForm.titleEn.value = titleEnOrig;	
	}
	if (elem == "descripcioTrad"){
		document.veuRecForm.description.value = descriptionCaOrig;
		document.veuRecForm.descriptionEs.value = descriptionEsOrig;
		document.veuRecForm.descriptionEn.value = descriptionEnOrig;	
	}
	if (elem == "rightsTrad"){
		document.veuRecForm.rightsDesc.value = rightsCaOrig;
		document.veuRecForm.rightsEs.value = rightsEsOrig;
		document.veuRecForm.rightsEn.value = rightsEnOrig;	
	}
	if (elem == "negar"){
		document.veuRecForm.operation.value = "pubRec";	
	}

		document.getElementById(elem).style.display="none";
		if (actOper != null){
			actOper.style.background="";
			actOper.style.fontWeight="";
			actOper.style.color="";
			actOper.className = "";
			actOper = null;
		
	}
}

function enviaMissatge()
{
	if((document.veuRecForm.missatge.value!="" || confirm("Segur que el vols enviar el missatge sense cos?")) &&
	   (document.veuRecForm.subject.value!=""  || confirm("Segur que el vols enviar el missatge sense assumpte?")))
	{
		document.veuRecForm.submit();
	}
	else
	{
		document.veuRecForm.operation.value = "pubRec";	
	}
}

/*function missatge(elem){
	//document.veuRecForm.submit();
	if (confirm("Segur que el vols enviar?")){
		try{
				elem.href+="?subject="+document.veuRecForm.subject.value+
				"&body="+document.veuRecForm.missatge.value;
		}catch(e){}
		document.veuRecForm.submit();
	}else{
		return false;
	}
}*/


function corregir(iden, value){
	if (actOper != null){
		actOper.style.background="";
		actOper.style.fontWeight="";
		actOper.style.color="#1CBF00";
		actOper.className = "";
		actOper = null;
	}
	elem = document.getElementById(iden);
	elem.style.display="none";
	
	elem = document.getElementById(iden+"Val");
	elem.focus();
	elem.innerHTML = value;
	elem.style.color = "#1CBF00";	
}

function traduir(iden){
	if (iden == "titolRecTrad"){
		titleCaOrig = document.veuRecForm.title.value;
		titleEsOrig = document.veuRecForm.titleEs.value;
		titleEnOrig = document.veuRecForm.titleEn.value;
		titleOcOrig = document.veuRecForm.titleOc.value;
		document.getElementById("titolRecVal").innerHTML = document.veuRecForm.title.value;
		document.getElementById("titolRecVal").style.color = "#1CBF00";
	}
	if (iden == "descripcioTrad"){
		descriptionCaOrig = document.veuRecForm.description.value;
		descriptionEsOrig = document.veuRecForm.descriptionEs.value;
		descriptionEnOrig = document.veuRecForm.descriptionEn.value;
		descriptionOcOrig = document.veuRecForm.descriptionOc.value;
		document.getElementById("descripcioVal").innerHTML = document.veuRecForm.description.value;
		document.getElementById("descripcioVal").style.color = "#1CBF00";
	}
	if (iden == "rightsTrad"){
		rightsCaOrig = document.veuRecForm.rightsDesc.value;
		rightsEsOrig = document.veuRecForm.rightsEs.value;
		rightsEnOrig = document.veuRecForm.rightsEn.value;
		rightsOcOrig = document.veuRecForm.rightsOc.value;
		document.getElementById("rightsVal").innerHTML = document.veuRecForm.rightsDesc.value;
		document.getElementById("rightsVal").style.color = "#1CBF00";
	}
	renovarTitol();
	elem = document.getElementById(iden);
	elem.style.display="none";
}


function enviar(op){

	document.veuRecForm.operation.value= op;
	document.veuRecForm.submit();
}


function goto(on){
	if (on == "propi"){
		location ="gesrecurs.do?llistat=1";
	}
	if (on == "tornar"){
		location ="gesrecurs.do?llistat="+document.veuRecForm.llistat.value;
	}
}


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
	if (list == "propis"){
		id = 1;		
		document.getElementById("titol").innerHTML = titpropis;
	}
	if (list == "tots"){
		id = 5;		
		document.getElementById("titol").innerHTML = tittots;
	}
	if (list == "agerga_pend"){
		id = 13;		
		document.getElementById("titol").innerHTML = titagregapend;
	}
	if (list == "agrega_fet"){
		id = 14;		
		document.getElementById("titol").innerHTML = titagregafet;
	}
	
	document.getElementById("llistat").style.display =  "none";
	changeList(id);
}


function imprimir(){
	window.print(); 
	return false;
}

function changeTraduccio(title,prop,value,idCa,idEs,idEn,idOc){
	html = "<p>"+title+"<br>";
	if (idCa == "titCa")
	{
		idCa2="traductTitle";
		html += "<input type='text' id='tit' style='width:299px; color:grey; border-color: #AAA;' maxlength='950' value=\""+value+"\"/></p>";
	}
	if (idCa == "descCa")
	{
		idCa2="traductDesc";
		html += "<input type='text' id='desc' style='width:299px; color:grey; border-color: #AAA;' maxlength='950' value=\""+value+"\"/></p>";
	}
	if (idCa == "rightCa")
	{
		idCa2="traductRight";
		html += "<input type='text' id='right' style='width:299px; color:grey; border-color: #AAA;' maxlength='950' value=\""+value+"\"/></p>";
	}

	pestAct = prop;
	if (prop == "title" || prop == "description" || prop == "rightsDesc"){	
		document.getElementById(idCa).className="selec";
		document.getElementById(idEs).className="noselec";
		document.getElementById(idEn).className="noselec";
		document.getElementById(idOc).className="noselec";
		document.getElementById(idCa2+'Ca').className="noselec";
		document.getElementById(idCa2+'Es').className="selec";
		document.getElementById(idCa2+'En').className="noselec";
		document.getElementById(idCa2+'Oc').className="noselec";
	}
	if (prop == "titleEs" || prop == "descriptionEs" || prop == "rightsEs"){
		document.getElementById(idCa).className="noselec";
		document.getElementById(idEs).className="selec";
		document.getElementById(idEn).className="noselec";
		document.getElementById(idOc).className="noselec";		
	}
	if (prop == "titleEn" || prop == "descriptionEn" || prop == "rightsEn"){
		document.getElementById(idCa).className="noselec";
		document.getElementById(idEs).className="noselec";
		document.getElementById(idEn).className="selec";
		document.getElementById(idOc).className="noselec";	
	}
	if (prop == "titleOc" || prop == "descriptionOc" || prop == "rightsOc"){
		document.getElementById(idCa).className="noselec";
		document.getElementById(idEs).className="noselec";
		document.getElementById(idEn).className="noselec";
		document.getElementById(idOc).className="selec";	
	}
	if (idCa == "titCa")
		document.getElementById("titleProperty").innerHTML = html; 
	if (idCa == "descCa")	
		document.getElementById("descProperty").innerHTML = html; 
	if (idCa == "rightCa")	
		document.getElementById("rightsProperty").innerHTML = html; 
}

function renovarTitol(){
	var ct;
	var cont="";
	if(pestAct=="title" || pestAct=="titleEn" || pestAct=="titleEs" || pestAct=="titleOc")
		ct=document.getElementById("tit");
	if(pestAct=="description" || pestAct=="descriptionEs" || pestAct=="descriptionEn" || pestAct=="descriptionOc")
		ct=document.getElementById("desc");
	if(pestAct=="rightsDesc" || pestAct=="rightsEs" || pestAct=="rightsEn" || pestAct=="rightsOc")
		ct=document.getElementById("right");
	
	if (pestAct == "title")
		cont = document.veuRecForm.title.value;
	if (pestAct == "titleEn")
		cont = document.veuRecForm.titleEn.value;		
	if (pestAct == "titleEs")
		cont = document.veuRecForm.titleEs.value;		
	if (pestAct == "titleOc")
		cont = document.veuRecForm.titleOc.value;
	
	if (pestAct == "description")
		cont = document.veuRecForm.description.value;
	if (pestAct == "descriptionEs")
		cont = document.veuRecForm.descriptionEs.value;		
	if (pestAct == "descriptionEn")
		cont = document.veuRecForm.descriptionEn.value;		
	if (pestAct == "descriptionOc")
		cont = document.veuRecForm.descriptionOc.value;
	
	if (pestAct == "rightsDesc")
		cont = document.veuRecForm.rightsDesc.value;
	if (pestAct == "rightsEs")
		cont = document.veuRecForm.rightsEs.value;		
	if (pestAct == "rightsEn")
		cont = document.veuRecForm.rightsEn.value;		
	if (pestAct == "rightsOc")
		cont = document.veuRecForm.rightsOc.value;
		
	if (navigator.appName=="Microsoft Internet Explorer")
		ct.innerText = cont;
	else
		ct.innerHTML =cont;
}

function tradOperation(oper,elem){
	if (oper == "titolRecTrad"){
		pestAct = "title";
		document.getElementById('titolRecTrad').style.display="block";
		document.getElementById('descripcioTrad').style.display="none";
		document.getElementById('rightsTrad').style.display="none";
		document.getElementById('traduccioTitleCa').style.display="none";
		document.getElementById('traduccioTitleEs').style.display="block";
		document.getElementById('traduccioTitleEn').style.display="none";
		document.getElementById('traduccioTitleOc').style.display="none";
		document.getElementById('idiomesTitle').style.display="block";
		var ver = $("#tit").offset().top+27;
		var hor = $("#idiomesT").offset().left;
		$("#idiomesT").offset({ top: ver, left: hor })
		ver = $("#titleEs").offset().top+27;
		$("#idiomesTitle").offset({ top: ver, left: hor })		
		document.veuRecForm.operation.value = "traduir";
		changeTraduccio(elem,'title',document.veuRecForm.title.value,'titCa','titEs','titEn','titOc');
	}
	if (oper == "descripcioTrad"){
		pestAct = "description";
		document.getElementById('titolRecTrad').style.display="none";
		document.getElementById('descripcioTrad').style.display="block";
		document.getElementById('rightsTrad').style.display="none";
		document.getElementById('idiomesDesc').style.display="none";
		document.getElementById('traduccioDescCa').style.display="none";
		document.getElementById('traduccioDescEs').style.display="block";
		document.getElementById('traduccioDescEn').style.display="none";
		document.getElementById('traduccioDescOc').style.display="none";
		document.getElementById('idiomesDesc').style.display="block";
		var ver = $("#desc").offset().top+27;
		var hor = $("#idiomesDG").offset().left;
		$("#idiomesDG").offset({ top: ver, left: hor })
		ver = $("#descriptionEs").offset().top+115;
		$("#idiomesDesc").offset({ top: ver, left: hor })	
		document.veuRecForm.operation.value = "traduir";
		changeTraduccio(elem,'description',document.veuRecForm.description.value,'descCa','descEs','descEn','descOc');
	}
	if (oper == "rightsTrad"){
		pestAct = "rightsDesc";
		document.getElementById('titolRecTrad').style.display="none";
		document.getElementById('descripcioTrad').style.display="none";
		document.getElementById('rightsTrad').style.display="block";
		document.getElementById('idiomesRight').style.display="none";
		document.getElementById('traduccioRightCa').style.display="none";
		document.getElementById('traduccioRightEs').style.display="block";
		document.getElementById('traduccioRightEn').style.display="none";
		document.getElementById('traduccioRightOc').style.display="none";
		document.getElementById('idiomesRight').style.display="block";
		var ver = $("#right").offset().top+27;
		var hor = $("#idiomesDD").offset().left;
		$("#idiomesDD").offset({ top: ver, left: hor })
		ver = $("#rightsEs").offset().top+115;
		$("#idiomesRight").offset({ top: ver, left: hor })	
		document.veuRecForm.operation.value = "traduir";
		changeTraduccio(elem,'rightsDesc',document.veuRecForm.rightsDesc.value,'rightCa','rightEs','rightEn','rightOc');
	}
	if (oper == "traduccio"){
		document.getElementById('idiomes'+elem).style.display="block";
		if (pestAct == "title" || pestAct == "description" || pestAct == "rightsDesc"){
			document.getElementById("traduccio"+elem+"Ca").style.display="none";
			document.getElementById("traduccio"+elem+"Es").style.display="block";
			document.getElementById("traduccio"+elem+"En").style.display="none";
			document.getElementById("traduccio"+elem+"Oc").style.display="none";
			document.getElementById("traduct"+elem+"Ca").className="noselec";
			document.getElementById("traduct"+elem+"Es").className="selec";
			document.getElementById("traduct"+elem+"En").className="noselec";
			document.getElementById("traduct"+elem+"Oc").className="noselec";
		}
		if (pestAct == "titleEs" || pestAct == "descriptionEs" || pestAct == "rightsEs"){
			//document.getElementById("traduccio"+elem+"Ca").style.display="none";
			$("#traduccioTitleEs").css('display','none');
			document.getElementById("traduccio"+elem+"Es").style.display="none";
			document.getElementById("traduccio"+elem+"En").style.display="block";
			document.getElementById("traduccio"+elem+"Oc").style.display="none";
			document.getElementById("traduct"+elem+"Ca").className="noselec";
			document.getElementById("traduct"+elem+"Es").className="noselec";
			document.getElementById("traduct"+elem+"En").className="selec";
			document.getElementById("traduct"+elem+"Oc").className="noselec";
		}
		if (pestAct == "titleEn" || pestAct == "descriptionEn" || pestAct == "rightsEn"){
			document.getElementById("traduccio"+elem+"Ca").style.display="none";
			document.getElementById("traduccio"+elem+"Es").style.display="none";
			document.getElementById("traduccio"+elem+"En").style.display="none";
			document.getElementById("traduccio"+elem+"Oc").style.display="block";
			document.getElementById("traduct"+elem+"Ca").className="noselec";
			document.getElementById("traduct"+elem+"Es").className="noselec";
			document.getElementById("traduct"+elem+"En").className="noselec";
			document.getElementById("traduct"+elem+"Oc").className="selec";
		}
		if (pestAct == "titleOc" || pestAct == "descriptionOc" || pestAct == "rightsOc"){
			document.getElementById("traduccio"+elem+"Ca").style.display="block";
			document.getElementById("traduccio"+elem+"Es").style.display="none";
			document.getElementById("traduccio"+elem+"En").style.display="none";
			document.getElementById("traduccio"+elem+"Oc").style.display="none";
			document.getElementById("traduct"+elem+"Ca").className="selec";
			document.getElementById("traduct"+elem+"Es").className="noselec";
			document.getElementById("traduct"+elem+"En").className="noselec";
			document.getElementById("traduct"+elem+"Oc").className="noselec";
		}
		document.veuRecForm.operation.value = "traduir";		
	}
	if (oper == "traduccioFinalCa"){
		renovarTitol();
		document.getElementById("traduccio"+elem+"Ca").style.display="block";
		document.getElementById("traduccio"+elem+"Es").style.display="none";
		document.getElementById("traduccio"+elem+"En").style.display="none";
		document.getElementById("traduccio"+elem+"Oc").style.display="none";
		document.getElementById("traduct"+elem+"Ca").className="selec";
		document.getElementById("traduct"+elem+"Es").className="noselec";
		document.getElementById("traduct"+elem+"En").className="noselec";
		document.getElementById("traduct"+elem+"Oc").className="noselec";
	}
	if (oper == "traduccioFinalEs"){
		renovarTitol();		
	   	document.getElementById("traduccio"+elem+"Ca").style.display="none";
		document.getElementById("traduccio"+elem+"Es").style.display="block";
		document.getElementById("traduccio"+elem+"En").style.display="none";
		document.getElementById("traduccio"+elem+"Oc").style.display="none";
		document.getElementById("traduct"+elem+"Ca").className="noselec";
		document.getElementById("traduct"+elem+"Es").className="selec";
		document.getElementById("traduct"+elem+"En").className="noselec";
		document.getElementById("traduct"+elem+"Oc").className="noselec";		
	}
	if (oper == "traduccioFinalEn"){
		renovarTitol();
		document.getElementById("traduccio"+elem+"Ca").style.display="none";
		document.getElementById("traduccio"+elem+"Es").style.display="none";
		document.getElementById("traduccio"+elem+"En").style.display="block";
		document.getElementById("traduccio"+elem+"Oc").style.display="none";
		document.getElementById("traduct"+elem+"Ca").className="noselec";
		document.getElementById("traduct"+elem+"Es").className="noselec";
		document.getElementById("traduct"+elem+"En").className="selec";
		document.getElementById("traduct"+elem+"Oc").className="noselec";
	}
	if (oper == "traduccioFinalOc"){
		renovarTitol();
		document.getElementById("traduccio"+elem+"Ca").style.display="none";
		document.getElementById("traduccio"+elem+"Es").style.display="none";
		document.getElementById("traduccio"+elem+"En").style.display="none";
		document.getElementById("traduccio"+elem+"Oc").style.display="block";
		document.getElementById("traduct"+elem+"Ca").className="noselec";
		document.getElementById("traduct"+elem+"Es").className="noselec";
		document.getElementById("traduct"+elem+"En").className="noselec";
		document.getElementById("traduct"+elem+"Oc").className="selec";
	}
	if (oper == "tancarTraduct"){
		renovarTitol();
		document.getElementById("traduccio"+elem+"Ca").style.display="none";
		document.getElementById("traduccio"+elem+"Es").style.display="none";
		document.getElementById("traduccio"+elem+"En").style.display="none";
		document.getElementById("traduccio"+elem+"Oc").style.display="none";
		document.getElementById('idiomes'+elem).style.display="none";
	}
}

