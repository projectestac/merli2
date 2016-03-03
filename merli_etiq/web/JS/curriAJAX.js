var reqLevel;
var reqArea;
var reqContent

var message;

function callbackLevel(){
  	if (reqLevel.readyState == 4){
		if (reqLevel.status == 200){
          parseMessageLevel(); 
		}else{
		  errorCarregant("cLevel");
		}
	}
}
function callbackArea(){
  	if (reqArea.readyState == 4){
		if (reqArea.status == 200){
          parseMessageArea(); 
		}else{
		  errorCarregant("cArea");
		}
	}
}
function callbackContent(){
  	if (reqContent.readyState == 4){
		if (reqContent.status == 200){
          parseMessageContent(); 
		}else{
		  errorCarregant("cContent");
		}
	}
}
 
	function parseMessageLevel()
	{
		mdiv = document.getElementById("cLevel");
		mdiv.innerHTML = reqLevel.responseText;
	}	
	function parseMessageArea()
	{
		mdiv = document.getElementById("cArea");
		mdiv.innerHTML = reqArea.responseText;
	}	
	function parseMessageContent()
	{
		mdiv = document.getElementById("cContent");
		mdiv.innerHTML = reqContent.responseText;
	}	
	
	
	function errorCarregant(elem){
		mdiv = document.getElementById(elem);
		mdiv.innerHTML = errorRequest;
	}
	
	function getCurriculumList(idKey,type){
		var idField = idKey;
		var objw;

	   if (type=="level"){
		   document.etiqForm.curLevel.value = idKey;
		   waitingState(document.getElementById("cLevel"));	
	   }if (type=="area"){
		   document.etiqForm.curArea.value = idKey;
		   waitingState(document.getElementById("cArea"));	
	   }if (type=="content"){
		   document.etiqForm.curContent.value = idKey;
		   waitingState(document.getElementById("cContent"));
	   }

	    var url = "curriculum.do?type="+escape(type);
	    url += "&level="+document.etiqForm.curLevel.value;
	    url += "&area="+document.etiqForm.curArea.value;
	    url += "&content="+document.etiqForm.curContent.value;
	    url += "&curriculum="+document.etiqForm.curriculum.value;

	    if (window.XMLHttpRequest)
	    	{
	        reqAux = new XMLHttpRequest();
	    	}
	    else if (window.ActiveXObject)
	    	{
	        reqAux = new ActiveXObject("Microsoft.XMLHTTP");
	   		}
	   
	   
	   if (type=="level"){
	   	   reqLevel = reqAux;
		   reqLevel.open("GET", url, true);
		   reqLevel.onreadystatechange = callbackLevel;
		   reqLevel.send(null);
		   getCurriculumList(0,"area");
		   getCurriculumList(0,"content");
		   document.etiqForm.curLevel.value = idKey;		  
	   }if (type=="area"){
	   	   reqArea = reqAux;
		   reqArea.open("GET", url, true);
		   reqArea.onreadystatechange = callbackArea;
		   reqArea.send(null);
		   getCurriculumList(0,"content");
		   document.etiqForm.curArea.value = idKey;
	   }if (type=="content"){
	   	   reqContent = reqAux;
		   reqContent.open("GET", url, true);
		   reqContent.onreadystatechange = callbackContent;
		   reqContent.send(null);
		   document.etiqForm.curContent.value = idKey;
	   }
	}
	
	
	function waitingState(obj){
		img = document.createElement("img");
		img.setAttribute("src","web/images/activIndica.gif");
		img.setAttribute("class","wait");
		obj.appendChild(img);
	}
	
	function changeCurriculum(id, obj){
		if (obj.checked == true){
			if(obj.value.indexOf("area")!=-1)			//inserim una area
			{
				document.etiqForm.curArea.value=id;
				document.etiqForm.curContent.value="";
			}
			else if(obj.value.indexOf("content")!=-1)	//inserim un contingut
			{
				document.etiqForm.curContent.value=id;
			}
			if (addSelecContent(obj))
				document.etiqForm.curriculum.value += obj.value+";";
		}else{
			/*list = ","+document.etiqForm.curriculum.value;
			aux = ","+obj.value+",";
			res = list.substring(1, list.indexOf(aux)+1);
			res += list.substring(list.indexOf(aux)+aux.length);
			document.etiqForm.curriculum.value = res;
			*/
			delCurriculumValue(obj.value);
			delSelecContent(obj.value);
		}
	}
	//Elimina el valor "value" dels valors del curriculum.
	function delCurriculumValue(valor){
			list = ";"+document.etiqForm.curriculum.value;
			aux = ";"+valor+";";
			res = list.substring(1, list.indexOf(aux)+1);
			res += list.substring(list.indexOf(aux)+aux.length);
			document.etiqForm.curriculum.value = res;
	}
	
	//Elimina el camp de la taula de Continguts seleccionats.
	function delSelecContent(idElem){
		var elm = new getObj("sel"+idElem);
		if (elm.obj != null){
			elm.obj.parentNode.removeChild(elm.obj);
			elm = new getObj(idElem);
			if (elm.obj != null){
				elm.obj.previousSibling.checked = false ;
			}
		}
	}
	function addSelecContent(cont){
		if (document.etiqForm.curriculum.value.indexOf(cont.value)<1){
			
			//esborro el peu de taula
			var elm = new getObj("final_taula");
			if (elm.obj != null){
				elm.obj.parentNode.removeChild(elm.obj);
			}
			
			//creo la fila amb el nom de l'element
			text = "<tr id=\"sel"+cont.value+"\">";
			idl = document.etiqForm.curLevel.value;
			
			//afegeixo el nivell
			var elm = new getObj("level"+idl);
			nom = elm.obj.innerHTML;
			text +="<td class=\"selecLevel\"><a href=\"#\" onclick=\"getCurriculumList("+idl+",'level')\">"+nom+"</a></td>";
			
			//afegeixo l'area
			ida = document.etiqForm.curArea.value;
			elm = new getObj("area"+ida);
			if (elm.obj != null){
				nom = elm.obj.innerHTML;
				text +="<td class=\"selecArea\"><a href=\"#\" class=\"link\" onclick=\"getCurriculumList("+idl+",'level');getCurriculumList("+ida+",'area')\">"+nom+"</a></td>";
			}else{
				text +="<td class=\"selecArea\"></td>";
			}
			
			//afegeixo el contingut
			id = document.etiqForm.curContent.value;
			elm = new getObj("content"+id);
			if (id!=null && id!=""){
				nom = elm.obj.innerHTML;
				text +="<td class=\"selecContent\"><a href=\"#\" class=\"link\" onclick=\"getCurriculumList("+idl+",'level');getCurriculumList("+ida+",'area');getCurriculumList("+id+",'content');\">"+nom+"</a></td>";
			}else{
				text +="<td class=\"selecContent\"></td>";
			}

			
			//afegeixo l'icone d'esborrar
			text +="<td class=\"selecContent\"><a style=\"cursor:pointer\" class=\"link\" onclick=\"delCurriculumValue('"+cont.value+"');delSelecContent('"+cont.value+"')\">";
			text +="<img align=\"top\" title=\"Esborra\" alt=\"E\" src=\"web/images/elimina.png\" class=\"operacio\"></a></td>";
			text +="<td/>";
			
			//tanco fila
			text +="</tr>";
			
			//afegeixo el peu de taula
			text +="<tr id=\"final_taula\" style=\"height:30px; background-color:#EBEBEB;\"><td></td><td></td><td></td><td></td><td></td></tr>";
			
			//elm = new getObj("bodySeleCur");
			//var t = elm.obj.innerHTML + text;
			//elm.obj.innerHTML = t;
			var t = $('#bodySeleCur').html() + text;
			$('#bodySeleCur').html(t);
			
			
			return true;
		}else{
		alert("L'element ja es troba seleccionat.");
		return false;
		}
	}
