var req;
var reqUrl;
var reqUrlC;
var message;
var idTerme;
 
function validate()
	{
    var idField = document.getElementById("userid");
    var url = "Responder.do?id=" + escape(idField.value);
    if (window.XMLHttpRequest)
    	{
        req = new XMLHttpRequest();
    	}
    else if (window.ActiveXObject)
    	{
        req = new ActiveXObject("Microsoft.XMLHTTP");
   		}
   req.open("GET", url, true);
   req.onreadystatechange = callback;
   req.send(null);
	}
 
function callback(){
  	if (req.readyState == 4){
		if (req.status == 200){
          parseMessage(); 
		}else{
			mdiv = document.getElementById("thesaurus");
			mdiv.innerHTML = errorRequest;
		}
	}
}
function callbackMT(){
  	if (req.readyState == 4){
		if (req.status == 200){
          parseMessageMT();  
		}else{
			mdiv = document.getElementById("listThes");
			mdiv.innerHTML = errorRequest;
		}
	}
}

function callbackURL(){
  	if (reqUrl.readyState == 4){
		if (reqUrl.status == 200){
          parseMessageURL(); 
		}else{}
	}
}

function callbackTermsContent(){
  	if (reqCont.readyState == 4){
		if (reqCont.status == 200){
          parseMessageTermsContent(); 
		}else{}
	}
 }
 
function parseMessage()
	{
		mdiv = document.getElementById("thesaurus");
		mdiv.innerHTML = req.responseText;
	}	
	
function parseMessageMT()
	{ 
		mdiv = document.getElementById("listThes");
		mdiv.innerHTML = req.responseText;
	}	

function parseMessageURL()
	{
		mdiv = document.getElementById("validateUrl");
		mdiv.innerHTML = reqUrl.responseText;
	}		
	
function parseMessageTermsContent()
	{	res = "";
		mis = reqCont.responseText;
		while (mis.indexOf(',') > 0){
			id = mis.substring(mis.indexOf('{')+1,mis.indexOf(','));
			text = mis.substring(mis.indexOf(';')+1,mis.indexOf('}'));
			mis = mis.substring(mis.indexOf('}')+1,mis.length);

			if (!addContentTerme(id,text)){
				if (res != "")
					res += "; ";				
				res += text;
			}
		}
		if (res!="")
			alert("Els seguents descriptors ETB ja es troben seleccionats:\n"+res);
	}

	
function navigateTo(idKey){/*
	document.NodeForm.idKey.value =idKey;
	document.NodeForm.entornOperacio.value = document.NodeForm.operacio.value;
	alert(document.NodeForm.entornOperacio.value);
	document.NodeForm.operacio.value ="thesnav";
	document.NodeForm.submit();*/
	//addPath(idKey);
	if(idKey == null)		 {comprovaTerme();idKey=idTerme;idTerme=null;}//per posar l'idTerme que toca
	if (idKey != null){
		//Posa el nom de la paraula linkada en la part superior de la navegaci? del thesaurus
		if (idKey < 10000){
		elemKey = new getObj("thesaurus"+ idKey);
		}else{
		elemKey = new getObj(idKey); 
		}
	
		if (elemKey.obj == "" || elemKey.obj == null){
			elemKey = new getObj("key"+idKey);
		}
		//Vol dir que s'enlla?a desdel cercador de paraules.
		if (elemKey.obj == "" || elemKey.obj == null){
			text = document.etiqForm.consulta.value;	
		}else
			text = elemKey.obj.innerHTML;
		elemKey = new getObj("parClauActual");
		elemKey.obj.innerHTML = text;
	
		var idField = idKey;//document.getElementById("userid");
	    var url = "responder.do?id=" + escape(idKey);
	    if (window.XMLHttpRequest)
	    	{
	        req = new XMLHttpRequest();
	    	}
	    else if (window.ActiveXObject)
	    	{
	        req = new ActiveXObject("Microsoft.XMLHTTP");
	   		}
	   req.open("GET", url, true);
	   req.onreadystatechange = callback;
	   req.send(null);
	}
}


function loadMicrothes(){
    var url = "responder.do?id=0";
    if (window.XMLHttpRequest)
    	{
        req = new XMLHttpRequest();
    	}
    else if (window.ActiveXObject)
    	{
        req = new ActiveXObject("Microsoft.XMLHTTP");
   		}
   req.open("GET", url, true);
   req.onreadystatechange = callbackMT;
   req.send(null);
}


function loadTermsContent(idContent){
		value = idContent.substring(7);
	    var url = "extra.do?operation=contentTerms&value=" + escape(value);
	    if (window.XMLHttpRequest)
	    	{
	        reqCont = new XMLHttpRequest();
	    	}
	    else if (window.ActiveXObject)
	    	{
	        reqCont = new ActiveXObject("Microsoft.XMLHTTP");
	   		}
	   reqCont.open("GET", url, true);
	   reqCont.onreadystatechange = callbackTermsContent;
	   reqCont.send(null);
	   
	   //Mostrar activitat..
}

function comprova(){
	var titol=document.etiqForm.titol.value;
	var enllac=document.etiqForm.url.value;
	var idRec=document.etiqForm.idRecurs.value;
	var ids="";
	
	tipus=document.getElementsByName("tipusIdFisicSel");
	valors=document.getElementsByName("idFisic");
	for(i=0;i<tipus.length;i++)
	{
		if(tipus[i].value!="" && valors[i]!=null && valors[i].value!="")
			ids+=tipus[i].value+";"+valors[i].value+";";
	}
		
    var url = "extra.do?height=300&width=600&operation=comprova&modal=true&idRecurs="+idRec+"&value=" + escape(enllac)+"&cerca="+escape(titol)+"&fisic="+escape(ids);	
	var link = document.getElementById('comprovaLink');
	link.href=url;
    /*
	if (window.XMLHttpRequest)
        reqUrlC = new XMLHttpRequest();
    else if (window.ActiveXObject)
        reqUrlC = new ActiveXObject("Microsoft.XMLHTTP");
	reqUrlC.open("GET", url, true);
	reqUrlC.onreadystatechange = callbackComprova;
	reqUrlC.send(null);
  //  waitingComprova(true);
  	*/
  	//validateUrl();
	if(!(titol.length<3 && enllac.length<5 && ids.length<1)) {
		actualitzaPagina();
	}
}

function actualitzaPagina()
{
	$("#menuPas2").click(function(){veure(2,this);});
	$("#menuPas3").click(function(){veure(3,this);});
	$("#menuPas4").click(function(){veure(4,this);});
	$("#menuPas5").click(function(){veure(5,this);});
	document.getElementById("b_seg").style.visibility="visible";
	document.getElementById("desa").style.display="inline";
	document.getElementById("barradesa").style.display="inline";
	
	document.getElementById("comprovaHidden").value="1";
}

function sortirComprova()
{
	if(document.getElementById("compr_continua").checked) {
		document.getElementById("comprova").innerHTML="";
		tb_remove();
	}
	else if(document.getElementById("compr_cancela").checked) {
		cancela();
		tb_remove();
	}
	else document.getElementById("comprova").innerHTML=document.getElementById("comprova").innerHTML;
}


function callbackComprova(){
  	if (reqUrlC.readyState == 4){
		if (reqUrlC.status == 200)
         	parseMessageComprova(reqUrlC); 
		else
			alert ("no s'ha pogut fer la comprovacio degut a problemes del servidor");
	}
}
 
function parseMessageComprova(requ)
{
	mdiv = document.getElementById("comprova");	
	msg = requ.responseText;
	if (msg.length < 10)
		alert ("no s'ha pogut fer la comprovacio degut a problemes del servidor");
	else
		mdiv.innerHTML = msg;
}	

function waitingComprova(yes){
	if (yes){
		mdiv = document.getElementById("comprova");	
		mdiv.style.display='none';		
		mdiv = document.getElementById("waitingComprova");
		mdiv.style.display="block";	
	}else{
		mdiv = document.getElementById("comprova");	
		mdiv.style.display='block';
		mdiv = document.getElementById("waitingComprova");
		mdiv.style.display="none";	
	}
}

function validateUrl(){
	if(document.etiqForm.url.value!="")
	{
	    var url = "extra.do?operation=validateUrl&value=" + escape(document.etiqForm.url.value)+"&idRecurs=" + escape(document.etiqForm.idRecurs.value);
	    if (window.XMLHttpRequest)
	        reqUrl = new XMLHttpRequest();
	    else if (window.ActiveXObject)
	        reqUrl = new ActiveXObject("Microsoft.XMLHTTP");
	   reqUrl.open("GET", url, true);
	   reqUrl.onreadystatechange = callbackURL;
	   reqUrl.send(null);
	  // document.getElementById("validateUrl").innerHTML = "Comprovant...";
	}
	else validateClear();
}

function validateClear(){
		document.getElementById("validateUrl").innerHTML = "";
}

/**
 * aquesta funcio es crida al fer click damunt un terme del tesaurus del desplegable
 * 
 */		
function putValueTerm(idParaula, node){
	idTerme = idParaula;
	//document.getElementById("a_addGo").onclick = function(){addGeneral('terme'); };		//ie
	//document.getElementById("a_addGo").setAttribute("onclick","addGeneral('terme')");		//ff
	document.getElementById("a_addGo").onclick = new Function("addGeneral('terme')");
	
	var par = node.firstChild.nodeValue;
	par = par.substring(0,par.lastIndexOf("(")-1);
	document.etiqForm.consulta.value = par;
	
	document.getElementById("resultTermes").style.display="none";
	document.getElementById("a_showGo").setAttribute("class", "a_showGo_on");
}

/**
 * aquesta funcio es crida al fer click damunt una paraula oberta del desplegable
 * 
 */	
function putValueParaula(idParaula,node){
	idTerme = idParaula;
	//document.getElementById("a_addGo").setAttribute("onclick","addGeneral('paraula')");
	document.getElementById("a_addGo").onclick = new Function("addGeneral('paraula')");
		
	var par = node.firstChild.nodeValue;
	par = par.substring(0,par.lastIndexOf("(")-1);
	document.etiqForm.consulta.value = par;
	
	document.getElementById("resultTermes").style.display="none";
	/*document.getElementById("addGo").style.display="none";
	document.getElementById("addParaula").style.display="inline";*/
}

function addGeneral(op)
{
	if(document.etiqForm.buscaTermesInput.value!="")
	{
		if(idTerme==null)		op = comprovaTerme();
		
		if(op=="terme")			addTerme(idTerme);
		else if(op=="paraula")	addParaula(idTerme);
		else if(op=="nova")		addParaulaNova(null);
	}
}


/**
 * Comprova si el terme esta al desplegable de termes
 * retorna "nova" si la paraula no està al deplegable
 * retorna "terme" si la paraula esta al desplegable i es un terme del tesaurus
 * retorna "paraula" si la paraula esta al deplegable i es una paraula oberta
 */
function comprovaTerme()
{
	var paraula = document.etiqForm.consulta.value;
	var cont="";
	var op="";
	var trobada="false";
	var soc="";
	var tam=$("#resultTermes td[onclick^='putValue']").size();
	
	$("#resultTermes td").each(function(){    
	     cont=this.innerHTML;
	     cont=cont.substring(0,cont.lastIndexOf("(")-1);
	     if(paraula==cont)
	     {
	         trobada="true";
	         soc = new String(this.onclick);
	         if(soc.lastIndexOf("Paraula")>0)    op="paraula";
	         else if(soc.lastIndexOf("Term")>0)  op="terme";
	         idTerme=soc.substring(soc.lastIndexOf("(")+1,soc.lastIndexOf(","));
	     }
	 });

	if(trobada=="true")
	{
	     if(op=="paraula")  return "paraula";
	     else return "terme";
	}
	else return "nova";
}



function addTerme(idKey){
	pcm = ";"+document.etiqForm.selecTerm.value;	
	text = document.etiqForm.consulta.value;
	if(idKey==null)		idKey = comprovaTerme();
	else{
		if (pcm.indexOf(";"+idKey+";")<0){		
			document.etiqForm.selecTerm.value += idKey+";";    
			document.etiqForm.selecLabel.value += text+";";
			key = new getObj('keys');    
			aDel = document.createElement("a");
			aDel.setAttribute("href","#");
			aDel.setAttribute("id","delkey"+idKey);
			aDel.setAttribute("onclick","delKey("+idKey+")");
				textDel = document.createTextNode(" - ");
			aDel.appendChild(textDel);
			
			aNav = document.createElement("a");
			aNav.setAttribute("href","#");
			aNav.setAttribute("id","key"+idKey);
			aNav.setAttribute("onclick","navigateTo("+idKey+")");
				//textNav = document.createTextNode(" "+text+"; ");
				textNav = document.createTextNode(text);
			aNav.appendChild(textNav);
			
			
			spaKey = document.createElement("span");
			spaKey.setAttribute("class","keyValue");
		//	spaKey.setAttribute("onmouseover","marcarea(this)");
		//	spaKey.setAttribute("onmouseout","dismarcarea(this)");
			spaKey.setAttribute("id","keyValue"+idKey);			
			spaKey.appendChild(aDel);		
			spaKey.appendChild(aNav);
			link = '<span class="keyValue" id="keyValue'+idKey+'">';// onmouseover="marcarea(this)" onmouseout="dismarcarea(this)">';			
			link += ' <a href="#" id="key'+idKey+'" onclick="navigateTo('+idKey+')">'+text+' </a>';
			link += ' <a href="#" id="delkey'+idKey+'" onclick="delKey('+idKey+')"> <img src="web/images/elimina.png"  title="Elimina el terme '+text+'" alt="Elimina el terme '+text+'" /> </a>';
			link +='&nbsp;</span>';
			//key.obj.appendChild(spaKey);
			key.obj.innerHTML = key.obj.innerHTML + link;
			
			document.etiqForm.buscaTermesInput.value="";								//buido el textbox						
			document.getElementById("a_showGo").setAttribute("class", "a_showGo_off");	//deshabilito el 'Mostra al tesaurus'
		}
		else{
			alert(thesSelectedTerm);
		}
	}
}

function addParaula(idKey){
	pcm = ";"+document.etiqForm.selecParaulaId.value;	
	text = document.etiqForm.consulta.value;
	if (idKey != null){
		if (pcm.indexOf(";"+idKey+";")<0){		
			document.etiqForm.selecParaulaId.value += idKey+";";    
			document.etiqForm.selecParaula.value += text+";";
			key = new getObj('paraules');    
			link = '<span class="keyValue" id="paraulaValue'+idKey+'">';// onmouseover="marcarea(this)" onmouseout="dismarcarea(this)">';			
			link += ' <a id="paraula'+idKey+'" class="paraulaOberta">'+text+' </a>';
			link += ' <a href="#" id="delParaula'+idKey+'" onclick="delParaula('+idKey+')"> <img src=\"web/images/elimina.png\" title="Elimina la paraula '+text+'" alt="Elimina la paraula '+text+'" /> </a>';
			link +='&nbsp;</span>';
			//key.obj.appendChild(spaKey);
			key.obj.innerHTML = key.obj.innerHTML + link;
			
			//buido el textbox
			document.etiqForm.buscaTermesInput.value="";
		}else{
			alert(thesSelectedTerm);
		}
	}		
}

var novaparaula=0;
function addParaulaNova(idTerme){
	if(idTerme==null)
	{
		text = document.etiqForm.consulta.value;
		while (text.indexOf(";")>=0)
			text=text.replace(';',' ');
		pcm = ";"+document.etiqForm.selecParaula.value;	
	
		var np = novaparaula++;
		if (pcm.indexOf(";"+text+";")<0){
			document.etiqForm.selecParaulaId.value += text+";";    
			document.etiqForm.selecParaula.value += text+";";
			key = new getObj('paraules');    
			link = '<span class="keyValue" id="paraulaValue'+np+'">';// onmouseover="marcarea(this)" onmouseout="dismarcarea(this)">';			
			link += ' <a id="paraula'+np+'" class="paraulaOberta">'+text+' </a>';
			link += ' <a href="#" id="delParaula'+np+'" onclick="delParaulaNova('+np+',\''+text+'\')"> <img src=\"web/images/elimina.png\" title="Elimina la paraula '+text+'" alt="Elimina la paraula '+text+'" /> </a>';
			link +='&nbsp;</span>';
			//key.obj.appendChild(spaKey);
			key.obj.innerHTML = key.obj.innerHTML + link;
			
			//buido el textbox
			document.etiqForm.buscaTermesInput.value="";
			document.getElementById("buscaTermesInput").style.backgroundImage="url(web/images/lupa.png)";
		}else{
			alert(thesSelectedTerm);
		}
	}
}

function addContentTerme(idKey,text){
		pcm = ";"+document.etiqForm.selecTerm.value;
		//elemKey = new getObj("thesaurus"+idKey); 
		//text = elemKey.obj.innerHTML;
		if (pcm.indexOf(";"+idKey+";")<0){		
			document.etiqForm.selecTerm.value += idKey+";";    
    		document.etiqForm.selecLabel.value += text+";";
			key = new getObj('termesContent');    

			link = '<li class="keyValue" id="tc'+idKey+'">';// onmouseover="marcarea(this)" onmouseout="dismarcarea(this)">';			
			link += ' '+text;
			link += " <a href=\"#\" id=\"delkey"+idKey+"\" onclick=\"delContentTerme("+idKey+",'"+escape(text)+"')\"> <img src=\"web/images/elimina.png\" title=\"Elimina el terme "+text+"\" alt=\"Elimina el terme "+text+"\" /> &nbsp;</a>";
			link +='</li>';
			//key.obj.appendChild(spaKey);
			if (key.obj.innerHTML.indexOf("termesContentBuit")>0)
				key.obj.innerHTML = link;
			else
				key.obj.innerHTML = key.obj.innerHTML + link;
			return true;
		}
		return false;
	}
	
function delContentTerme(idElem,text){
	var elm = new getObj('tc'+idElem);
		if (elm.obj != null){
			elm.obj.parentNode.removeChild(elm.obj);
			
		pcm = ";"+document.etiqForm.selecTerm.value;
		pcl = ";"+document.etiqForm.selecLabel.value;
			aux = ";"+text+",";
			pcd1 = pcl.substring(1,pcl.indexOf(aux)+1); 
            pcd1 += pcl.substring(pcl.indexOf(aux)+aux.length);
            document.etiqForm.selecLabel.value = pcd1; 
			aux = ";"+idElem+";";
			pcd1 = pcm.substring(1,pcm.indexOf(aux)+1); 
            pcd1 += pcm.substring(pcm.indexOf(aux)+aux.length);
            document.etiqForm.selecTerm.value = pcd1;
		}
		
		key = new getObj('termesContent'); 
		if (key.obj.innerHTML.indexOf("<li")<0){
			link = '<li class="keyValue" id="termesContentBuit">';			
			link += noTermesETBDUC;
			link +='</li>';
			key.obj.innerHTML = link;
		}
}