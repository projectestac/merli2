var req;
var message;
 
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
		}
	}
}
 
function parseMessage()
	{
		//message = req.responseXML.getElementsByTagName("message")[0];
		mdiv = document.getElementById("thesaurus");
		//Amendez 23/03/2016 https://trello.com/c/bTyrT3wj
		mdiv.innerHTML = decodeURIComponent(req.responseText);
	}	
 
 
 	function navigateTo(idKey){/*
		document.NodeForm.idKey.value =idKey;
		document.NodeForm.entornOperacio.value = document.NodeForm.operacio.value;
		alert(document.NodeForm.entornOperacio.value);
		document.NodeForm.operacio.value ="thesnav";
		document.NodeForm.submit();*/
		addPath(idKey);
		var idField = document.getElementById("userid");
	    var url = "Responder.do?id=" + escape(idKey);
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
	
	
 	function addPath(idKey){
		document.NodeForm.idKey.value =idKey;
		pcm = "#"+document.NodeForm.navPath.value;
		//AFEGIT PER REPARAR THESAURUS
		var elemKey;
		if (pcm.indexOf("#"+idKey+"#")<0){		
			elemKey = new getObj("thesaurus"+idKey);
			if (elemKey.obj == null) 
				elemKey = new getObj("key"+idKey);		
			if (elemKey.obj != null){
				text = elemKey.obj.text;
				document.NodeForm.navPath.value += idKey+"#";         
				key = new getObj('path');    				
				aNav = document.createElement("a");
				aNav.setAttribute("href","#");
				aNav.setAttribute("id","path"+idKey);
				aNav.setAttribute("onclick","navigateTo("+idKey+")");
					textNav = document.createTextNode(text +"/ ");
				aNav.appendChild(textNav);
				key.obj.appendChild(aNav);
			}
		}//else{
		//CANVIAT PER REPARAR THESAURUS  if (elemKey.obj == null) 
		if (elemKey == null || elemKey.obj == null) 
			elemKey = new getObj("path"+idKey);		
			list = new getObj("path");

			list = list.obj.childNodes;
			i =0;
			borrar = false;
			while (list[i] != null){
			 if (borrar){
			 	list[i].parentNode.removeChild(list[i]);
			 	i--;
			 }
			 if (list[i].id == "path"+idKey){
			 	borrar=true; }
			 i++;
			}
			pcm = pcm.substring(1,pcm.indexOf("#"+idKey));
			document.NodeForm.navPath.value = pcm+"#"+idKey;
		//}
	}
	