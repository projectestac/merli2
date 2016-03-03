/*SOURCE from  http://www.quirksmode.org/js/cross_dhtml.html */
	var DHTML = (document.getElementById || document.all || document.layers);
	var lastElement;
	
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
	

function esborrar(){
	permis = getRadioButtonSelectedValue(document.permForm.permisos);
	if (confirm("do you want to delete permission "+permis+"?")){
		document.permForm.idPermission.value = permis;
		document.permForm.operation.value = "delPerm";
		document.permForm.submit();
		}
	return false;
}


function nou(){
	if (document.permForm.operation.value == "seting"){
		cancelSet();
	}
	if (document.permForm.operation.value != "adding"){
		var nouu = new getObj("nouperm");
		nouu.style.display ="block";
		nouu = nouu.obj;

		/*<SPAN class="user">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","user");
			/*username: <INPUT type="text" name="permnou">*/
			newText = document.createTextNode("permission:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","permnou");
				newINP.setAttribute("type","text");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		/*<SPAN class="url">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","url");
			/*email: <INPUT type="text" name="descnou">*/
			newText = document.createTextNode("description:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","descnou");
				newINP.setAttribute("type","text");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<INPUT type="button" value="envia" onclick="javascript:addNou()">*/
		newBTN = document.createElement("input");
		newBTN.setAttribute("value","Envia");
		newBTN.setAttribute("type","button");
		newBTN.setAttribute("class","buto");
		newBTN.setAttribute("onclick","javascript:addNou();");
		nouu.appendChild(document.createElement("br"));
		nouu.appendChild(newBTN);
		/*<INPUT type="button" value="cancel" onclick="javascript:cancelAdd()">*/
		newBTN2 = document.createElement("input");
		newBTN2.setAttribute("value","Cancel");
		newBTN2.setAttribute("type","button");
		newBTN2.setAttribute("class","buto");
		newBTN2.setAttribute("onclick","javascript:cancelAdd();");
		nouu.appendChild(newBTN2);
		document.permForm.operation.value = "adding";
	}
}

function addNou(){
	if (document.permForm.operation.value == "adding"){
		document.permForm.description.value = document.permForm.descnou.value;
		document.permForm.permission.value = document.permForm.permnou.value;
		document.permForm.operation.value = "addPerm";
		
		list = document.getElementsByName("operation-1");
		listOper="";
		i = 0;
		while (list[i] != null){ 
			if (list[i].checked){
				listOper += list[i].value;
				if (list[i+1] != null) listOper += ",";
			}
			i++;
		}
		document.permForm.listOperations.value = listOper;
		
		document.permForm.submit();
	}
}

function cancelAdd(){
		var nouu = new getObj("nouperm");
		nouu.style.display ="none";
		/*nouu = nouu.obj;
		newP = document.createElement("p");
			newP.setAttribute("id","nouperm");			
		nouu.parentNode.replaceChild(newP,nouu);
		*/
		document.permForm.operation.value = "";
}

function cancelSet(){
		var nouu = new getObj(lastElement.id);
		nouu = nouu.obj;
		nouu.parentNode.replaceChild(lastElement,nouu);
		document.permForm.operation.value = "";
}


function modificar(){
	var idPermission=getRadioButtonSelectedValue(document.permForm.permisos);
	if (document.permForm.operation.value == "adding"){
		cancelAdd();
	}
	
	if (document.permForm.operation.value == "seting"){
		cancelSet();
		
	}
	var nouu = new getObj(idPermission);
	nouu = nouu.obj;
	lastElement = nouu;
	
	list = nouu.childNodes;
	permis = list[1].firstChild.nodeValue;
	desc = "";
	if (list[2].firstChild != null)
		desc = list[2].firstChild.nodeValue;
	
	/*<P id=*permission>*/
	newP = document.createElement("P");
		newP.setAttribute("id",idPermission);
		/*<SPAN class="user">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","user");
			/*permission: *permission*/
			newText = document.createTextNode("Perm?s: ");
			newSPAN.appendChild(newText);
			newINP = document.createElement("input");
				newINP.setAttribute("name","permset");
				newINP.setAttribute("type","text");
				newINP.setAttribute("value",permis);
			newSPAN.appendChild(newINP);
		newP.appendChild(newSPAN);
		/*<SPAN class="url">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","url");
			/*email: <INPUT type="text" name="mailset">*/
			newText = document.createTextNode("descripci?:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","descset");
				newINP.setAttribute("type","text");
				newINP.setAttribute("value",desc);
			newSPAN.appendChild(newText);
			newSPAN.appendChild(newINP);
		newP.appendChild(newSPAN);
		/*<BR/>*/
		newP.appendChild(document.createElement("br")); alert(idPermission+"--"+document.getElementById("perm"+idPermission+"op").style.display);
	if (document.getElementById("perm"+idPermission+"op").style.display == "block"){	
	}else{
		/*<INPUT type="button" value="envia" onclick="setUser()"; class="buto">*/
		newBTN = document.createElement("input");
			newBTN.setAttribute("value","Envia");
			newBTN.setAttribute("name","Envia");
			newBTN.setAttribute("type","button");
			newBTN.setAttribute("class","buto");
			newBTN.setAttribute("onclick","setPerm();");
		/*<INPUT type="button" value="cancel" onclick="cancelSet();" class="buto">*/
		newP.appendChild(newBTN);
			newBTN2 = document.createElement("input");
			newBTN2.setAttribute("value","Cancel");
			newBTN.setAttribute("name","Cancel");
			newBTN2.setAttribute("type","button");
			newBTN2.setAttribute("class","buto");
			newBTN2.setAttribute("onclick","cancelSet();");
		newP.appendChild(newBTN2);
		}
	nouu.parentNode.replaceChild(newP,nouu);
	document.permForm.idPermission.value = idPermission;
	document.permForm.operation.value = "seting";
}

function setPerm(){
	if (document.permForm.operation.value == "seting"){
		document.permForm.permission.value = document.permForm.permset.value;
		document.permForm.description.value = document.permForm.descset.value;
		document.permForm.operation.value = "setPerm";		
		document.permForm.submit();
	}
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