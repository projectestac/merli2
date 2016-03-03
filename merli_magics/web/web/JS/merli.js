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
	user = getRadioButtonSelectedValue(document.userForm.usuaris);
	if (confirm("do you want to delete user "+user+"?")){
		document.userForm.username.value = user;
		document.userForm.operation.value = "delUser";
		document.userForm.submit();
		}
	return false;
}

function tancaMod(){
	for(i=0;i<document.userForm.usuaris.length;i++)
	{
		username=document.userForm.usuaris[i].value;
		imatge=document.getElementById("img"+username);
		imatge.src="web/images/avall.png";
		document.getElementById("perm"+username+"op").style.display = "none";
	}
}

function nou(){
    tancaMod();
    var url = "unitats.do";
    if (window.XMLHttpRequest)
    {
        req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        req = new ActiveXObject("Microsoft.XMLHTTP");
   	}
	req.open("GET", url, true);
	req.onreadystatechange = callbackUnitats;
	req.send(null);
}

function callbackUnitats(){
  	if (req.readyState == 4){
		if (req.status == 200){
          parseMessageUnitat();
		}
	}
}
function parseMessageUnitat()
{ 
	//message = req.responseXML.getElementsByTagName("message")[0];
	//alert( req.responseText);
	nou2(req.responseText)		
}	


function nou2(unitats){
	if (document.userForm.operation.value == "seting"){
		cancelSet();
	}
	if (document.userForm.operation.value == "permisos"){
		cancelPermisos();
		
	}
	if (document.userForm.operation.value != "adding"){
		var nouu = new getObj("nouuser");
		nouu.style.display ="block";
		nouu = nouu.obj;

		/*<SPAN class="user">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","user");
			/*username: <INPUT type="text" name="usernou">*/
			newText = document.createTextNode("username:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","usernou");
				newINP.setAttribute("maxlength","8");
				newINP.setAttribute("type","text");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		
		/*<SPAN class="url">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","url");
			/*email: <INPUT type="text" name="mailnou">*/
			newText = document.createTextNode("email:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","mailnou");
				newINP.setAttribute("maxlength","40");
				newINP.setAttribute("type","text");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		
		var paraules = unitats.split("#");
		
		/*<SPAN class="unitat">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","unitatnou");
			/*unitat: <select name="unitatnou">*/
			newText = document.createTextNode("unitat:");
			newINP = document.createElement("select");
				newINP.setAttribute("name","unitatnou");
				newINP.setAttribute("width","100px");
					newOption=document.createElement("option");
					newOption.setAttribute("value","");
					newOption.textContent="";
				newINP.appendChild(newOption);
				for(i=0;i<paraules.length-1;i=i+2)
				{
						newOption=document.createElement("option");
						newOption.setAttribute("value",paraules[i]);
						newOption.textContent=paraules[i+1];
					newINP.appendChild(newOption);
				}
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/><br/>*/
		nouu.appendChild(document.createElement("br"));
		nouu.appendChild(document.createElement("br"));
		
		/*<SPAN class="us_merli">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","us_merli");
			/*usuari MeRLi: <INPUT type="checkbox" name="us_merli">*/
			newText = document.createTextNode("usuari MeRLi");
			newINP = document.createElement("input");
				newINP.setAttribute("name","us_merli");
				newINP.setAttribute("type","checkbox");
				newINP.setAttribute("onClick","javascript:activa_pw(this.form,this.form.us_merli,this.form.pw,this.form.repw)");
				newINP.setAttribute("defaultChecked","false");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		
		/*<SPAN class="pw">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","pw");
			/*Contrasenya: <INPUT type="password" name="pw">*/
			newText = document.createTextNode("Contrasenya:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","pw");
				newINP.setAttribute("type","password");
				newINP.setAttribute("maxlength","15");
				newINP.setAttribute("disabled","true");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		
		/*<SPAN class="repw">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","repw");
			/*Repetir contrasenya: <INPUT type="password" name="repw">*/
			newText = document.createTextNode("Repetir contrasenya:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","repw");
				newINP.setAttribute("type","password");
				newINP.setAttribute("maxlength","15");
				newINP.setAttribute("disabled","true");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(newINP);
		nouu.appendChild(newSPAN);
		/*<br/>*/
		nouu.appendChild(document.createElement("br"));
		
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
		document.userForm.operation.value = "adding";
	}
}

function activa_pw(userForm, checkbox, textbox1, textbox2)
{
	if(checkbox.checked){
		textbox1.disabled=false;
		textbox2.disabled=false;
	}
	else{
		textbox1.value="";
		textbox2.value="";
		textbox1.disabled=true;
		textbox2.disabled=true;
	}
}

function activa_canv_pw(userForm, checkbox1, checkbox2, textbox1, textbox2)
{
	if(checkbox1.checked){
		checkbox2.disabled=false;
		checkbox2.checked=false;
		textbox1.disabled=true;
		textbox2.disabled=true;
	}
	else{
		textbox1.value="";
		textbox2.value="";
		checkbox2.checked=false;
		checkbox2.disabled=true;
		textbox1.disabled=true;
		textbox2.disabled=true;
	}
}

function addNou(){
	if (document.userForm.operation.value == "adding"){
		document.userForm.email.value = document.userForm.mailnou.value;
		document.userForm.username.value = document.userForm.usernou.value;
		document.userForm.unitat.value = document.userForm.unitatnou.value;
		if(document.userForm.pw.value!=document.userForm.repw.value)
		{
			//alert("contrasenya incorrecta!");
			afegeixError();
		}
		else
		{
			document.userForm.operation.value = "addUser";
			document.userForm.submit();
		}
	}
}

function afegeixError()
{
		var nouu = new getObj("nouuser");
		nouu.style.display ="block";
		nouu = nouu.obj;

		newSPAN = document.createElement("SPAN");
		newSPAN.appendChild(document.createElement("br"));
		newSPAN.appendChild(document.createElement("br"));
			//newSPAN.setAttribute("class","errors");
			newSPAN.style.color="red";		
			newSPAN.style.fontWeight="bold";
			newText = document.createTextNode("**** Les contrasenyes no coincideixen ****");
		newSPAN.appendChild(newText);
		newSPAN.appendChild(document.createElement("br"));
		nouu.appendChild(newSPAN);
}

function cancelAdd(){
		var nouu = new getObj("nouuser");
		nouu.style.display ="none";
		nouu = nouu.obj;
		newP = document.createElement("p");
			newP.setAttribute("id","nouuser");			
		nouu.parentNode.replaceChild(newP,nouu);
		document.userForm.operation.value = "";
}

function cancelSet(){
		var nouu = new getObj(lastElement.id);
		nouu = nouu.obj;
		nouu.parentNode.replaceChild(lastElement,nouu);
		document.userForm.operation.value = "";
}

function cancelPermisos(){
		var nouu = new getObj("permisos");
		nouu = nouu.obj;
		nouu.parentNode.removeChild(nouu);
		document.userForm.operation.value = "";
}


function modificar(){
	var username=getRadioButtonSelectedValue(document.userForm.usuaris);
	if (document.userForm.operation.value == "adding"){
		cancelAdd();
	}
	
	if (document.userForm.operation.value == "seting"){
		cancelSet();
		
	}
	if (document.userForm.operation.value == "permisos"){
		cancelPermisos();
		
	}
	var nouu = new getObj(username);
	nouu = nouu.obj;
	lastElement = nouu;
	
	list = nouu.childNodes;
	email = list[2].firstChild.nodeValue;
	
	/*<P id=*username>*/
	newP = document.createElement("P");
		newP.setAttribute("id",username);
		/*<SPAN class="user">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","user");
			/*username: *username*/
			newText = document.createTextNode("username: ");
			newSPAN.appendChild(newText);
			newText = document.createTextNode(username+"  ");
			newSPAN.appendChild(newText);
		newP.appendChild(newSPAN);
		/*<SPAN class="url">*/
		newSPAN = document.createElement("SPAN");
			newSPAN.setAttribute("class","url");
			/*email: <INPUT type="text" name="mailset">*/
			newText = document.createTextNode("email:");
			newINP = document.createElement("input");
				newINP.setAttribute("name","mailset");
				newINP.setAttribute("type","text");
				newINP.setAttribute("value",email);
			newSPAN.appendChild(newText);
			newSPAN.appendChild(newINP);
		newP.appendChild(newSPAN);
		/*<BR/>*/
		newP.appendChild(document.createElement("br"));
		/*<INPUT type="button" value="envia" onclick="setUser(); class="buto">*/
		newBTN = document.createElement("input");
			newBTN.setAttribute("value","Envia");
			newBTN.setAttribute("name","Envia");
			newBTN.setAttribute("type","button");
			newBTN.setAttribute("class","buto");
			newBTN.setAttribute("onclick","setUser();");
		/*<INPUT type="button" value="cancel" onclick="cancelSet();" class="buto">*/
		newP.appendChild(newBTN);
			newBTN2 = document.createElement("input");
			newBTN2.setAttribute("value","Cancel");
			newBTN.setAttribute("name","Cancel");
			newBTN2.setAttribute("type","button");
			newBTN2.setAttribute("class","buto");
			newBTN2.setAttribute("onclick","cancelSet();");
		newP.appendChild(newBTN2);
	nouu.parentNode.replaceChild(newP,nouu);
	document.userForm.username.value = username;
	document.userForm.operation.value = "seting";
}

function setUser(){
	if (document.userForm.operation.value == "seting"){
		document.userForm.email.value = document.userForm.mailset.value;
		document.userForm.operation.value = "setUser";		
		document.userForm.submit();
	}
}


function permisos(){
var username=getRadioButtonSelectedValue(document.userForm.usuaris);
	if (document.userForm.operation.value == "adding"){
		cancelAdd();
	}
	
	if (document.userForm.operation.value == "seting"){
		cancelSet();
			
	}
	if (document.userForm.operation.value == "permisos"){
		cancelPermisos();
		
	}
	var nouu = new getObj(username);
	nouu = nouu.obj;
	lastElement = nouu;
	
	list = nouu.childNodes;
	email = list[2].firstChild.nodeValue;
	
	/*<P id=*username>*/
	newP = document.createElement("P");
		newP.setAttribute("id","permisos");
		newP.setAttribute("id","permisos");
		/*<BR/>*/
		//newP.appendChild(document.createElement("br"));
		/*<INPUT type="checkbox" value="Permis 1" class="buto">*/
		newCHK = document.createElement("input");
			newCHK.setAttribute("value","Permis 1");
			newCHK.setAttribute("name","Permis 1");
			newCHK.setAttribute("type","checkbox");
			newCHK.setAttribute("class","buto");
		newP.appendChild(newCHK);
		newP.appendChild(document.createTextNode("Permis1"));
		/*<BR/>*/
		newP.appendChild(document.createElement("br"));
		/*<INPUT type="checkbox" value="Permis 2" class="buto">*/
		newCHK = document.createElement("input");
			newCHK.setAttribute("value","Permis 2");
			newCHK.setAttribute("name","Permis 2");
			newCHK.setAttribute("type","checkbox");
			newCHK.setAttribute("class","buto");
		newP.appendChild(newCHK);
		newP.appendChild(document.createTextNode("Permis2"));
		/*<BR/>*/
		newP.appendChild(document.createElement("br"));
		/*<INPUT type="button" value="envia" onclick="setUser(); class="buto">*/
		newBTN = document.createElement("input");
			newBTN.setAttribute("value","Envia");
			newBTN.setAttribute("name","Envia");
			newBTN.setAttribute("type","button");
			newBTN.setAttribute("class","buto");
			newBTN.setAttribute("onclick","setUser();");
		/*<INPUT type="button" value="cancel" onclick="cancelPermisos();" class="buto">*/
		newP.appendChild(newBTN);
			newBTN2 = document.createElement("input");
			newBTN2.setAttribute("value","Cancel");
			newBTN2.setAttribute("name","Cancel");
			newBTN2.setAttribute("type","button");
			newBTN2.setAttribute("class","buto");
			newBTN2.setAttribute("onclick","cancelPermisos();");
		newP.appendChild(newBTN2);
	nouu.appendChild(newP);
	document.userForm.username.value = username;
	document.userForm.operation.value = "permisos";
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