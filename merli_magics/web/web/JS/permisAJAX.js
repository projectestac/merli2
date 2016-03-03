var req;
var reqSet
var message;
var idPermis;
var listnode="";
var newPerm;

function callback(){
  	if (req.readyState == 4){
		if (req.status == 200){
          parseMessage();
		}
	}
}
function callbackSet(){
  	if (reqSet.readyState == 4){
		if (reqSet.status == 200){
          parseMessageSet();
		}
	}
}
function callbackDefPerm(){
  	if (req.readyState == 4){
		if (req.status == 200){
          parseMessageDefPerm();
		}
	}
}
 
function parseMessage()
	{ 
		//message = req.responseXML.getElementsByTagName("message")[0];
		mdiv = document.getElementById("perm"+idPermis+"op");		
		mdiv.innerHTML = req.responseText;		
	}	
 
function parseMessageSet()
	{ 
		//message = req.responseXML.getElementsByTagName("message")[0];
		mdiv = document.getElementById("perm"+idPermis+"op");	
		mdiv.removeAttribute("modificat");	
		
		miss = document.getElementById("missperm"+idPermis+"op");
		miss.className ="inform";
		miss.innerHTML = "* Modificació realitzada";
	
	setTimeout("treureMissatge("+idPermis+")",2000);
	}	

function parseMessageDefPerm()
	{ 
		document.getElementById('resultDefPerm').innerHTML=req.responseText;
		setTimeout("document.getElementById('resultDefPerm').style.display='none';",2000);	
	}	
 
function treureMissatge(idPermis){
		mdiv = document.getElementById("missperm"+idPermis+"op");	
		if (mdiv.className == "inform")
			mdiv.innerHTML ="";
}
 
 
function getOperations(idPerm){
		
		var nouu = new getObj(idPerm);
		nouu = nouu.obj;
	
		list = nouu.childNodes;
		permis="";
		if (list[1].firstChild != null)
			permis = list[1].firstChild.nodeValue;
		desc = "";
		if (list[2].firstChild != null)
			desc = list[2].firstChild.nodeValue;

	    var url = "operacions.do?idPermission=" + escape(idPerm)+"&permission="+permis+"&description="+desc;
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
	   idPermis = idPerm;
}
	
	
/*Mostra la informaci? del perm?s i permet modificar-la.*/
function operationInfo(imatge,idPerm){
	/*mostra la informaci?*/
	if (imatge.src.indexOf("avall")>0){
		document.getElementById("perm"+idPerm+"op").style.display = "block";
		if (listnode.indexOf("#"+idPerm+"#")<=0){
			getOperations(idPerm);
			listnode += "#"+idPerm+"#";
		}
		imatge.src="web/images/amunt.png";	
	/*Amaga la informaci?*/
	}else{
		imatge.src="web/images/avall.png";
		document.getElementById("perm"+idPerm+"op").style.display = "none";
	}
}

/*Printa un missatge de informaci? modificada*/
function operModified(idPermis){
	object = document.getElementById("perm"+idPermis+"op");
	if (object != null && !object.getAttribute("modificat")){
		object.setAttribute("modificat","true");
		mis = document.getElementById("miss"+object.id);
		if (mis == null){
			newSPAN = document.createElement("span");
			newSPAN.setAttribute("class","alert");
			newSPAN.setAttribute("id","miss"+object.id);
				newText = document.createTextNode("* Modificacio pendent d'enviar");
			newSPAN.appendChild(newText);
			object.parentNode.insertBefore(newSPAN,object);
		}else{
			mis.className="alert";
			mis.innerHTML = "* Modificacio pendent d'enviar";
		}
	}
}

function cancelModif(idPermis){
	object = document.getElementById("img"+idPermis);
	operationInfo(object,idPermis);
	object = document.getElementById("missperm"+idPermis+"op");	
	object.innerHTML ="";
	object = document.getElementById("perm"+idPermis+"op");	
	object.removeAttribute("modificat");		
}

/*
permission
idPermission
operation
description
listOperations
*/

function modificaAJAX(idPermis){
		listOper ="";
		formul = document.getElementById("img"+idPermis); 
		operationInfo(formul,idPermis);
		
		miss = document.getElementById("missperm"+idPermis+"op");
		miss.className ="notice";
		miss.innerHTML = "* Enviant informaci?";

		//Recuperaci? de la informacio a enviar.
		if (permis = document.getElementsByName("permset"+idPermis)[0]!=null)
			permis = document.getElementsByName("permset"+idPermis)[0].value;
		else
			alert ("El permis no pot ser buit");
		if (document.getElementsByName("mailset"+idPermis)[0] != null)
			desc = document.getElementsByName("mailset"+idPermis)[0].value;		
		else
			desc ="";
		list = document.getElementsByName("operation"+idPermis);
		i = 0;
		while (list[i] != null){ 
			if (list[i].checked){
				listOper += list[i].value;
				if (list[i+1] != null) listOper += ",";
			}
			i++;
		}

		//Execuci? de la modificaci?.
		var url = "modificarperm.do?idPermission=" + escape(idPermis)+"&permission="+escape(permis)+"&description="+escape(desc)+"&listOperations="+escape(listOper);
	    
	    
	    if (window.XMLHttpRequest)
	    	{
	        reqSet = new XMLHttpRequest();
	    	}
	    else if (window.ActiveXObject)
	    	{
	        reqSet = new ActiveXObject("Microsoft.XMLHTTP");
	   		}
	   reqSet.open("GET", url, true);
	   reqSet.onreadystatechange = callbackSet;
	   reqSet.send(null);
	   
	   p = document.getElementById(idPermis);

	   p.childNodes[1].firstChild.nodeValue=permis;
	   p.childNodes[2].firstChild.nodeValue=desc;
}





function nouAJAX(){
	if (document.permForm.operation.value != "adding"){
		var nouu = new getObj("nouperm");
		nouu.style.display ="block";
		nouu = nouu.obj;	
		if (newPerm != "ready"){
		    var url = "operacions.do?idPermission=-1&permission=&description=";
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
		   idPermis = "nou";
			nouu = new getObj("permnouop");
			nouu.style.display ="block";
		}
	}

	document.permForm.operation.value = "adding";
	
}



/*function from:
http://www.eslomas.com/index.php/archives/2005/09/05/obtener-el-valor-de-un-radiobutton-seleccionado-con-javascript/
*/
function getRadioButtonSelectedValue(ctrl)
{
//    for(i=0;i<ctrl.length;i++)
i=0;
	
    while(ctrl[i] != null){
        if(ctrl[i].checked) return ctrl[i].value;
        i++;
        }
}

function defaultPermAJAX(){
	var value=document.getElementsByName("defaultPerm")[0].value;
	document.getElementById('operacioDefPerm').style.display='none';
	document.getElementById('resultDefPerm').innerHTML="Enviant petició...";
	document.getElementById('resultDefPerm').style.display='block';
	var url = "operacions.do?idPermission=" + escape(value)+"&permission=defaultPermission";
	    if (window.XMLHttpRequest)
	    	{
	        req = new XMLHttpRequest();
	    	}
	    else if (window.ActiveXObject)
	    	{
	        req = new ActiveXObject("Microsoft.XMLHTTP");
	   		}
	   req.open("GET", url, true);
	   req.onreadystatechange = callbackDefPerm;
	   req.send(null);
}