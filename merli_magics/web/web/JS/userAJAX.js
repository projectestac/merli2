var req;
var reqSet
var message;
var userAct;
var listnode="";
var newUser;

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
 
function parseMessage()
	{ 
		//message = req.responseXML.getElementsByTagName("message")[0];
		mdiv = document.getElementById("perm"+userAct+"op");		
		mdiv.innerHTML = req.responseText;		
	}	

/**
* Parseja el missatge rebut de resposta al realitzar una modificaci?.<b> 
* Si ?s ok, treu un missatge positiu, altrement informa de l'error.
**/
function parseMessageSet()
	{ 
		//message = req.responseXML.getElementsByTagName("message")[0];
		mdiv = document.getElementById("perm"+userAct+"op");	
		mdiv.removeAttribute("modificat");	
		
		vmiss = document.getElementById("missperm"+userAct+"op").innerHTML;
		miss.className ="inform";
		miss.innerHTML = reqSet.responseText;
	
		setTimeout("treureMissatge('"+userAct+"')",3000);
	}	

/**
*Elimina el missatge.
**/
function treureMissatge(username){
		mdiv = document.getElementById("missperm"+username+"op");	
		if (mdiv.className == "inform")
			mdiv.innerHTML ="";
}
 

	
/*Mostra la informaci? del perm?s i permet modificar-la.*/
function operationInfo(imatge,username){
	/*mostra la informaci?*/
	if (imatge.src.indexOf("avall")>0){
		document.getElementById("perm"+username+"op").style.display = "block";
		if (listnode.indexOf("#"+username+"#")<0){
			getPermisos(username);
			listnode += "#"+username+"#";
		}
		imatge.src="web/images/amunt.png";	
	/*Amaga la informaci?*/
	}else{
		imatge.src="web/images/avall.png";
		document.getElementById("perm"+username+"op").style.display = "none";
	}
}

/*Printa un missatge de informaci? modificada*/
function operModified(username){
	object = document.getElementById("perm"+username+"op");
	
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

function cancelModif(username){
	nom = "#"+username+"#";
	l1 = listnode.substring(0,listnode.indexOf(nom));
	l2 = listnode.substring(listnode.indexOf("#"+username+"#")+nom.length,listnode.length);
	listnode = l1+l2;
	object = document.getElementById("img"+username);
	operationInfo(object,username);
	object = document.getElementById("missperm"+username+"op");	
	object.innerHTML ="";
	object = document.getElementById("perm"+username+"op");	
	object.removeAttribute("modificat");		
}



/**
* Recupera la infromaci? dels permisos de l'usuari donat. 
* Crea un formulari per editar la infromaci? de l'usuari. 
* Permet canviar els permisos i el mail de l'usuari.
**/
function getPermisos(username){		
		var nouu = new getObj(username);
		nouu = nouu.obj;
		lastElement = nouu;
		
		list = nouu.childNodes;
		email = list[2].firstChild.nodeValue;

	    var url = "permisosuser.do?username=" + escape(username)+"&email="+email;
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
	   userAct = username;
}
	

function modificaAJAX(username){
		listPerm ="";
		formul = document.getElementById("img"+username); 
		operationInfo(formul,username);
		
		miss = document.getElementById("missperm"+username+"op");
		miss.className ="notice";
		miss.innerHTML = "* Enviant informacio";

		//Recuperaci? de la informacio a enviar.
		if (document.getElementsByName("mailset"+username)[0] != null)
			mail = document.getElementsByName("mailset"+username)[0].value;		
		else
			mail ="";
		list = document.getElementsByName("perm"+username);
		i = 0;
		while (list[i] != null){ 
			if (list[i].checked){
				listPerm += list[i].value;
				if (list[i+1] != null) listPerm += ",";
			}
			i++;
		}
		if (document.getElementsByName("unitatset"+username)[0] != null){
			unitat = "&unitat="+document.getElementsByName("unitatset"+username)[0].value;	
		}else
			unitat ="";
		/* ull!!! */	
		if (document.getElementsByName("us_merli"+username)[0] != null){
			us_merli = "&us_merli="+document.getElementsByName("us_merli"+username)[0].checked;	
		}else
			us_merli ="";
		if (document.getElementsByName("canv_pw"+username)[0] != null){
			canv_pw = "&canv_pw="+document.getElementsByName("canv_pw"+username)[0].checked;	
		}else
			canv_pw ="";
		if (document.getElementsByName("pw"+username)[0] != null){
			pw = "&pw="+document.getElementsByName("pw"+username)[0].value;	
		}else
			pw ="";
		if (document.getElementsByName("repw"+username)[0] != null){
			repw = "&repw="+document.getElementsByName("repw"+username)[0].value;	
		}else
			repw ="";

		//Execució de la modificació.
		var url = "modificaruser.do?username=" + escape(username)+"&email="+escape(mail)+"&listPermissions="+escape(listPerm)+unitat+us_merli+canv_pw+pw+repw;
	   // alert(url);
	    
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
	   
	   p = document.getElementById(username);
	   p.childNodes[2].firstChild.nodeValue=mail;
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