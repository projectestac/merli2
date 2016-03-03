/*SOURCE from  http://www.quirksmode.org/js/cross_dhtml.html */
	var DHTML = (document.getElementById || document.all || document.layers);
	var lastElement;
	var comencem="si";
	var catA;
	var pubA;
	
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
	
    var estat = false;
    var cercaAC = false;
    var ultima = "";
    var pcm = "";
    var idmNou = "";
    var error = 0;
    var tMovCapa = 10;
    var movCapa = 5;
    if (navigator.appName != "Microsoft Internet Explorer")
    {tMovCapa = 2;
     movCapa = 8;}

	var lastMenu;
    function veure(nomCapa, elem)
    { 
//    	if (elem != null){
//    		elem.className="selectedMenu";
//		    if (lastMenu!=null)
//			    lastMenu.className="";
//      		lastMenu=elem;
//      	}
      if (nomCapa != ultima){
      		if (estat) { //accions per amagar la capa
      		 	document.getElementById(pagina[ultima]).style.display = "none";
	            estat = false;
	            document.getElementById("tit"+ultima).style.display = "none";
	            veure(nomCapa);
      		}
      		else { //accions per a mostrar la capa
      		 	document.getElementById(pagina[nomCapa]).style.display = "block";
	            document.getElementById("tit"+nomCapa).style.display = "block";
	            estat = true;
	            ultima = nomCapa;
	            checkButtons();
	            if (ultima =="enviant") {setTimeout("acDades()",900);}
		            if (ultima =="fet") {
		                 if(error == 0){
		                   setTimeout("location='"+adBase+"r_gestor.g_operacions?usuari=acanals5'",1100);
		                 }else{
		                   setTimeout("veure(1)",4000);                         
		                 }
		            }
	      		}
    	}                            
    }
    
    function colorSelect(elem)
    {
    	if(elem.value=="")	elem.style.color="#aaa";
    	else				elem.style.color="#000";	
    }
    
    function comprovarEdats(){
        if ( (document.getElementsByName('edatMax')[0].value > 0) &&
            (parseInt(document.getElementsByName('edatMin')[0].value) > parseInt(document.getElementsByName('edatMax')[0].value))){
          alert(edatMinBiggerMax);
         }
        //Comprova si els valors son v?lids. Es donara en el cas en que s'hagi guardat el recurs sense especificar nivell educatiu ni edat.
        if (document.getElementsByName('edatMax')[0].value == 0 && document.getElementsByName('edatMax')[0].value != "") {document.getElementsByName('edatMax')[0].value = ""}
        if (document.getElementsByName('edatMin')[0].value > 99 && document.getElementsByName('edatMin')[0].value != "") {document.getElementsByName('edatMin')[0].value = ""}
        
        if (document.getElementsByName('edatMax')[0].value > 90 && document.getElementsByName('edatMax')[0].value != "") {document.getElementsByName('edatMax')[0].value = "U"}
        if (document.getElementsByName('edatMin')[0].value < 1 && document.getElementsByName('edatMin')[0].value != "") {document.getElementsByName('edatMin')[0].value = "U"}
    }           
    
    function afegirEdats(elem){ 
       var minim = 1313;
       var maxim = -1;
       i = 1;
	   //get info about elem, max, min and checked.	
       selec = elem.checked;
       emx = parseInt(elem.getAttribute("max"));
       emn = parseInt(elem.getAttribute("min"));
       name = elem.name;
       
       //selec=true Senyala que el boto ha estat activat.
       if (selec == true) {
          if ((document.getElementsByName('edatMax')[0].value < emx) 
             || (document.getElementsByName('edatMax')[0].value == "")){
              if (emx > 90) emx = "U";
              document.getElementsByName('edatMax')[0].value = emx;
          }
          if ((document.getElementsByName('edatMin')[0].value > emn)
             || (document.getElementsByName('edatMin')[0].value == "")) {
              if(emn < 1) emn="U";
              document.getElementsByName('edatMin')[0].value = emn;
          }
       //selec= false Senyala que hem desactivat el boto.
       }else{ 
           //Comprova totes les opcions seleccionades i recull el valor
           //maxim i minim de la configuracio de nivells actuals.
           i = 0;
           niv = document.getElementsByName(name)[i];
     //  maxim = parseInt(document.getElementsByName('edatMax')[0].value);
     //  minim = parseInt(document.getElementsByName('edatMin')[0].value);
           while (niv != null){
             // niv = document.getElementsByName("niv_"+i);
        		if (niv.checked == true){ 
                    // niv = document.getElementsByName("niv_"+i+"_max");
                     if (parseInt(niv.getAttribute("max")) > maxim){
                        maxim = parseInt(niv.getAttribute("max"));
                     }
                    // niv = document.getElementsByName("niv_"+i+"_min");
                     if (parseInt(niv.getAttribute("min")) < minim){
                        minim = parseInt(niv.getAttribute("min"));
                     }                      
              	}
        		i++;	
           		niv = document.getElementsByName(name)[i];
      		}
         // Si el valor actual del camp edatMin es buit, o "U"
         // o es + petit que el valor "minim" trobat s actualiztara.
         if ((document.getElementsByName('edatMin')[0].value == "") ||
              (document.getElementsByName('edatMin')[0].value == "U")||
              (parseInt(document.getElementsByName('edatMin')[0].value) < minim) 
              ){
              if (minim < 1) minim = "U";
              if (minim == 1313) minim = "";
              document.getElementsByName('edatMin')[0].value = minim;
          }
          //Idem per maxim.
          if ((document.getElementsByName('edatMax')[0].value == "") ||
              (document.getElementsByName('edatMax')[0].value == "U")||
              (parseInt(document.getElementsByName('edatMax')[0].value) > maxim) 
              ){
              if (maxim == -1) maxim = "";
              if (maxim > 90) maxim = "U";     
              document.getElementsByName('edatMax')[0].value = maxim;
          }
       }
    }
	
	/*retorna cert si la tecla pulsada en l'event e es un caracter numeric, una fletxa o esborrar*/
	function noLetters(e) 
	{ 
		var keynum; 
		var keychar; 
		var numcheck; 
		if(window.event) // IE 
			keynum = e.keyCode; 
		else if(e.which) // Netscape/Firefox/Opera 
			keynum = e.which; 
		keychar = String.fromCharCode(keynum); 
		numcheck = /\d/; 
		fletxae=(keynum==37);
		fletxad=(keynum==39);
		backspace=(keynum==8);
		suprimir=(keynum==46);
		numteclat=(keynum>95 && keynum<106);
		algun=(numcheck.test(keychar)||fletxae||fletxad||backspace||suprimir||numteclat);
		if(!algun) return false;
		else return true;
	} 

    /* Monta tots els params que caldr? enviar, per crear/modificar
       o a l hora de navegar per paraules claus sense perdre la info modif.
    */
    function montaParams(){ 
		if(document.getElementsByName('edatMin') == null || document.getElementsByName('edatMin')[0] == null){
			document.etiqForm.edatMin.value="0";
		}
		else if (document.getElementsByName('edatMin')[0].value == "U"){
        	document.getElementsByName('edatMin')[0].value = "0";
     	}
     	
		//Idem per maxim.
        if(document.getElementsByName('edatMax') == null || document.getElementsByName('edatMax')[0] == null){
			document.etiqForm.edatMax.value="99";
		}
		else if (document.getElementsByName('edatMax')[0].value == "U"){
        	document.getElementsByName('edatMax')[0].value = "99";
		}		 
	}
       
   /* Funci? que crea ladre?a per insertar o per modficar un recurs segons
      el que calgui, si id_mud > 0 sera modificar, crear altrament.
   */          
    function enviar(estat){
	//	alert ("estat antic: "+estat+", operacio: "+document.etiqForm.operacio.value+", estat nou: "+document.etiqForm.estatSel.value);
    	if ( document.etiqForm.idRecurs.value > 0){               
          text = misModificar;//"És a punt de modificar el recurs, n'està segur?";
        }else{
          text = misCrear;//"És a punt de crear un nou recurs, n'està segur?";
    	}
    	if (estat=='2'){
    		text = misEnviar;
    	}
    	if (document.etiqForm.operacio.value == "validarRec"){
   			if (estat == 0)
   				estat = document.etiqForm.estat.value;
   			else{
   				estat = estatValidat;
          		text = misValidar;//"És a punt de validar el recurs, n'està segur?";	
   			}	
   		}
   		
    //	document.etiqForm.estatSel.value = estat;
    	    	
    //	montaParams();

    	//if (confirm(text))
    	
    	seleccionaCampsMultiples('rolUser');
    	seleccionaCampsMultiples('ambit');
    	seleccionaCampsMultiples('llengues');
    	seleccionaCampsMultiples('format');
    	seleccionaCampsMultiples('unitats');
    	
  		rels = document.getElementsByName("recRel");
  		for(i=0;i<rels.length;i++)
  			if(rels[i].className.indexOf("hintTextboxActive")<0) rels[i].value="";
  		
		document.etiqForm.submit();
    }
    
    function seleccionaCampsMultiples(nomSelect){
    	sel=document.getElementById(nomSelect);
    	for (i=0; i<sel.options.length; i++) 
    	 	sel.options[i].selected=true;
    }
    
    function cancela(){
   		document.etiqForm.operacio.value = "cancel";
    	document.etiqForm.submit();   
    }
    
    function desarAdmin(){
    	//montaParams();  
    	seleccionaCampsMultiples('rolUser');
    	seleccionaCampsMultiples('ambit');
    	seleccionaCampsMultiples('llengues');
    	seleccionaCampsMultiples('format');
    	seleccionaCampsMultiples('unitats');
    	
    	//if (document.etiqForm.idRecurs.value > 0) text = misModificar;	//"És a punt de modificar el recurs, n'està segur?";
        //else text = misCrear;		//"És a punt de crear un nou recurs, n'està segur?";
    	//if (confirm(text))
    	
    	var rels = document.getElementsByName("recRel");
  		for(i=0;i<rels.length;i++)
  			if(rels[i].className.indexOf("hintTextboxActive")<0) rels[i].value="";
        
        document.etiqForm.submit();

    
    }
    
    /*
    function eliminarMud(){
        if (confirm("Eliminar el recurs, n'est? segur?")){
            //dir = adhome+"id_mud=&mspc=5";      
            //location = dir;             
            document.dadesBones.id_mud.value = "";
            document.dadesBones.mspc.value =5;  
            document.dadesBones.submit();
        }
    }*/
    
    /*Obre una nova finestra amb l'adre?a donada.
    */
    function obrir(obj,x, y)
     { /* 
      if (obj=="Calendari") {
          x = 270;
          y = 215;
          var opc = 'menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width='+x+',height='+y+',screenX=305,screenY=50,left=305,top=50';                                            
          opc ='scrollbars=no,resizable=yes,width='+x+',height='+y+',screenX=165,screenY=50,left=300,top=175';
          var avui = new Date();
          any = avui.getFullYear();
          mes = avui.getMonth()+1;
          nomF ='Calendari';
          direccio = adBase + 'r_etiquetador.calendari?anys='+any+'&mes='+mes;
          finTemes = window.open(direccio, 'calendari' , opc);
      }
      if(obj=="compURL") {
          urln = document.getElementsByName("url")[0].value;
          x = 270;
          y = 215;
          var opc = 'menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width='+x+',height='+y+',screenX=305,screenY=50,left=305,top=50';                                            
          opc ='scrollbars=no,resizable=yes,width='+x+',height='+y+',screenX=165,screenY=50,left=300,top=175';

          direccio = "http://www.edu365.com/pls/cerca_mud/r_etiquetador.comprovaUrl?urlnou="+urln+"&idrec=0"; 
          finTemes = window.open(direccio, "comprovarURL" , opc);
      }
      */
      if(obj=="fitxa") {
          urln = document.getElementsByName("url")[0].value;
          x = 800;
          y = 550;
          var opc = 'menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width='+x+',height='+y+',screenX=145,screenY=50,left=145,top=50';                                            
          opc ='scrollbars=no,resizable=yes,width='+x+',height='+y+',screenX=165,screenY=50,left=140,top=75';
          idm = idmNou;
          if (idm == "") {idm = 0;}
          direccio = "http://www.edu365.com/pls/cerca_mud/r_etiquetador.fitxa?idrec="+idm; 
          finTemes = window.open(direccio, "comprovarURL", opc);
      }
    }
/*
    function posaReferencia(){
           text = " (nou recurs)";
           if (idmNou > 0) text = " (recurs:" +idmNou+")";
           titolref = document.getElementsByName('titol')[0].value
           if (titolref.length > 45) {
              titolref = titolref.substr(0,42) +"...";
           }else{
             titolref = titolref;
           }
           document.getElementsByName('titRef')[0].value = titolref + text;
    }
    
    */
    function prepara(){
           text = " (nou recurs)";
           if (idmNou > 0) text = " (recurs: "+idmNou+")";
           document.getElementsByName('titRef')[0].value = document.getElementsByName('titol')[0].value + text;
           comprovarEdats();
    }
    
    function controlReturn(e) {
             var myChar;

            if (document.all) {
                e = window.event;
        	      myChar = (e.keyCode);
            }
            else {
                if (document.layers)
         	      var myChar = (e.which);
            }
            
            if (myChar == 13 || myChar == 3) {
               cercaAC = true; 
               anar_a('cerca', '0');
            }
            if (e.which == 13 || e.which == 3) {
               cercaAC = true; 
               anar_a('cerca', '0');
            }
    }

    
    function accioMenu(url){
            // if (confirm(misExitEtiq))
                location = url;
    }
    
    	
	function swapDisplay(win){return true;}
	
	
	function delKey(idKey){
		pcm = ";"+document.etiqForm.selecTerm.value;
		pcl = ";"+document.etiqForm.selecLabel.value;

		if (pcm.indexOf(";"+idKey+";")>=0){		
			//pcm = document.NodeForm.selecPath.value;// += idKey+"#";     
			aux = ";"+idKey+";";
			pcd1 = pcm.substring(1,pcm.indexOf(aux)+1); 
            pcd1 += pcm.substring(pcm.indexOf(aux)+aux.length);
            document.etiqForm.selecTerm.value = pcd1;

			key = new getObj("delkey"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	
		
			key = new getObj("key"+idKey); 
			aux = ";"+key.obj.textContent+";";
			pcd1 = pcl.substring(1,pcl.indexOf(aux)+1); 
            pcd1 += pcl.substring(pcl.indexOf(aux)+aux.length);
            document.etiqForm.selecLabel.value = pcd1;	
            	
			key.obj.parentNode.removeChild(key.obj);  
			
			key = new getObj("keyValue"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	 	
		}
	}
	
	function addKey(idKey){
		pcm = ";"+document.etiqForm.selecTerm.value;
		elemKey = new getObj("thesaurus"+idKey); 
		text = elemKey.obj.innerHTML;
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
			//spaKey.setAttribute("onmouseover","marcarea(this)");
			//spaKey.setAttribute("onmouseout","dismarcarea(this)");
			spaKey.setAttribute("id","keyValue"+idKey);			
			spaKey.appendChild(aDel);		
			spaKey.appendChild(aNav);
			link = '<span class="keyValue" id="keyValue'+idKey+'">';// onmouseover="marcarea(this)" onmouseout="dismarcarea(this)">';			
			link += ' <a href="#" id="key'+idKey+'" onclick="navigateTo('+idKey+')">'+text+' </a>';
			link += ' <a href="#" id="delkey'+idKey+'" onclick="delKey('+idKey+')"> <img src="web/images/elimina.png" title="Elimina el terme '+text+'" alt="Elimina el terme '+text+'" /> </a>';
			link +='&nbsp;</span>';
			//key.obj.appendChild(spaKey);
			key.obj.innerHTML = key.obj.innerHTML + link;
		}else{
			alert(thesSelectedTerm);
		}		
	}
	
	function delParaula(idKey){
		pcm = ";"+document.etiqForm.selecParaulaId.value;
		pcl = ";"+document.etiqForm.selecParaula.value;
		var textParaula="";

		if (pcm.indexOf(";"+idKey+";")>=0){		
			//pcm = document.NodeForm.selecPath.value;// += idKey+"#";     
			aux = ";"+idKey+";";
			pcd1 = pcm.substring(1,pcm.indexOf(aux)+1); 
            pcd1 += pcm.substring(pcm.indexOf(aux)+aux.length);
            document.etiqForm.selecParaulaId.value = pcd1;

			key = new getObj("delParaula"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	
			
			key = new getObj("paraula"+idKey);
			if (key.obj.innerText)	textParaula = key.obj.innerText.trim();
			else					textParaula = key.obj.textContent.trim();
			aux = ";"+textParaula+";";
			pcd1 = pcl.substring(1,pcl.indexOf(aux)+1); 
            pcd1 += pcl.substring(pcl.indexOf(aux)+aux.length);
            document.etiqForm.selecParaula.value = pcd1;	
            	
			key.obj.parentNode.removeChild(key.obj);  
			
			key = new getObj("paraulaValue"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	 	
		}
	}
	
	function delParaulaNova(idKey, text){
		pcm = ";"+document.etiqForm.selecParaulaId.value;
		pcl = ";"+document.etiqForm.selecParaula.value;
		var textParaula="";

		if (pcm.indexOf(";"+text+";")>=0){		
			//pcm = document.NodeForm.selecPath.value;// += idKey+"#";     
			aux = ";"+text+";";
			pcd1 = pcm.substring(1,pcm.indexOf(aux)+1); 
            pcd1 += pcm.substring(pcm.indexOf(aux)+aux.length);
            document.etiqForm.selecParaulaId.value = pcd1;

			key = new getObj("delParaula"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	
		
			key = new getObj("paraula"+idKey); 
			if (key.obj.innerText)	textParaula = key.obj.innerText.trim();
			else					textParaula = key.obj.textContent.trim();
			aux = ";"+textParaula+";";
			pcd1 = pcl.substring(1,pcl.indexOf(aux)+1); 
            pcd1 += pcl.substring(pcl.indexOf(aux)+aux.length);
            document.etiqForm.selecParaula.value = pcd1;	
            	
			key.obj.parentNode.removeChild(key.obj);  
			
			key = new getObj("paraulaValue"+idKey); 
			key.obj.parentNode.removeChild(key.obj);   	 	
		}
	}
	
	function marcarea(obj){
		obj.style.backgroundColor="#872000";
	}
	
	function dismarcarea(obj){
		if (obj.tagName == "LI")
			obj.style.backgroundColor="";
		else
			obj.style.backgroundColor="#d75007";
	}
	
	function posaReferencia(){
		if (document.etiqForm.idRecurs.value > 0)
			aux = " ("+document.etiqForm.idRecurs.value+")";
		else
			aux = "";
		var title = document.etiqForm.titol.value;
		if (title.length > 130)
			title = title.substr(0,130)+" [...]";
		document.getElementById("resourceTitle").getElementsByTagName("span")[0].firstChild.nodeValue = title + aux;
	/*	Forma antiga, eliminar i crear l'elemnt cada cop. 6/9/6
		document.getElementById("resourceTitle").removeChild(document.getElementById("resourceTitle").firstChild);
		if (document.getElementById("resourceTitle").firstChild != null){
			document.getElementById("resourceTitle").firstChild.nodeValue = document.etiqForm.titol.value;
		}else{
			document.getElementById("resourceTitle").appendChild(document.createTextNode(document.etiqForm.titol.value + aux)) 		
	}
	*/	

	}
	
	function focusError(error){
		if (error == "titol"){
			veure(1);
			document.etiqForm.titol.focus();	
			document.etiqForm.titol.style.borderColor="#ad2114";
		}		
		if (error == "recRecurs"){
			veure(1);
			document.etiqForm.recFisic.focus();
			document.etiqForm.recFisic.style.borderColor="#ad2114";
		}		
		if (error == "url"){
			veure(1);
			document.etiqForm.url.focus();
			document.etiqForm.url.style.borderColor="#ad2114";
		}	
		if (error == "curriculum"){
			veure(2);
		}		
		if (error == "thesaurus"){
			veure(3);
		}	
		if (error == "descripcio"){//resum
			veure(4);
			document.etiqForm.descripcio.focus();
			document.etiqForm.descripcio.style.borderColor="#ad2114";
		}			
		if (error == "idioma"){
			veure(4);
			document.etiqForm.llengues.focus();
			document.etiqForm.llengues.style.borderColor="#ad2114";
		}		
		if (error == "format"){
			veure(4);
			document.etiqForm.format.focus();
			document.etiqForm.format.style.borderColor="#ad2114";
		}	
		if (error == "autor"){
			veure(5);
			document.etiqForm.autor.focus();
			document.etiqForm.autor.style.borderColor="#ad2114";
		}	
		if (error == "editor"){
			veure(5);
			document.etiqForm.editor.focus();
			document.etiqForm.editor.style.borderColor="#ad2114";
		}									
		if (error == "responsable"){//catalogador
			veure(5);
			document.etiqForm.responsable.focus();
			document.etiqForm.responsable.style.borderColor="#ad2114";
		}		
		if (error == "validador"){
			veure(5);
			document.etiqForm.validador.focus();
			document.etiqForm.validador.style.borderColor="#ad2114";
		}		
		if (error == "corrector"){
			veure(5);
			document.etiqForm.corrector.focus();
			document.etiqForm.corrector.style.borderColor="#ad2114";
		}		
		if (error == "descDrets"){
			veure(5);
			document.etiqForm.descDrets.focus();
			document.etiqForm.descDrets.style.borderColor="#ad2114";	
		}	
		if (error == "recRel"){
			veure(4);
			var r=document.getElementsByName("recRel");
			if(r.length>0)	r[0].focus();
			//document.etiqForm.recRel[0].style.borderColor="#ad2114";
		}	
		if (error == "edat"){
			veure(4);
			document.etiqForm.edatmin.focus();
			document.etiqForm.edatmin.style.borderColor="#ad2114";
		}		
		if (error == "caractRFisic"){
			veure(4);
			document.etiqForm.caractRFisic.focus();
			document.etiqForm.caractRFisic.style.borderColor="#ad2114";
		}		
	}
	
	function setDataFormat(y,m,d) 
	{
	     document.etiqForm.data.value=d+"-"+m+"-"+y;
    }
     
	function focusPrimerError()
	{
		capa_errors=document.getElementById("errors");
		catxets=capa_errors.innerHTML.split("'");
		if(catxets[1])
			focusError(catxets[1]);
	}
	
	function carregaEtiquetador(usuari,perm)
	{
		veure(1, document.getElementById('menuPasDefault'));
		loadMicrothes();
		/*navigateTo(0);*/
		getCurriculumList(0,'level');
		focusPrimerError();
		comptadorLletres();
		comencem="si";
		initHintTextboxesRelacions();
		setEstat(usuari,perm);
	}
	
	function disableEnterKey(e)
	{
	     if(window.event) key = window.event.keyCode; //IE
	     else key = e.which; //firefox  
	     return (key != 13);
	}
	
	function controlLlengues(elem){
		var maxim = 10;

		i=0;
		res = 0;
			
		while (elem.options[i]){
			if (elem.options[i].selected)
				res++;
			if (res > 10)
				elem.options[i].selected=false;
			i++
		}
		if (res > 10)
			alert (maxLlengues);
	}
	
	function imprimir(){
	alert ("Aquesta operació encara no es pot realitzar, disculpeu les molesties.");	
	}
	
	function comptadorLletres() {
  		var desc=document.etiqForm.descripcio;
  		var compt=document.getElementById("comptador");
  		if(n=desc.value.length) compt.innerHTML=n+" car\u00E0cters";
  		else  					compt.innerHTML="0 car\u00E0cters";
	}
	
	function setEstat(usuariActual,perm)
	{
		if(comencem=="si")
		{
			if(document.getElementById("capaVal") && document.etiqForm.validador.value=="") document.etiqForm.validador.value=usuariActual; 
			comencem="no";
		}
		
		if(document.etiqForm.responsable.value!=" ")
			catA=document.etiqForm.responsable.value;
		if(document.getElementById("capaVal") && document.etiqForm.validador.value!="")
			pubA=document.etiqForm.validador.value;

		switch(document.etiqForm.estatSel.value)
		{
			case "0":							//Esborrany
			case "2":							//Pendent de publicar
			case "-1":							//Retornada
			case "-2":							//Denegada
				
				// catalogador, el que hi havia (i es pot editar)
				document.etiqForm.responsable.value=catA;
				if(perm=="si") document.etiqForm.responsable.removeAttribute('readOnly');
				
				// validador, ningun (i no es pot editar)
				if(document.getElementById("capaVal"))
				{
					document.etiqForm.validador.value="";
					document.etiqForm.validador.setAttribute('readOnly','readonly');
				}
				break;
			case "4":							//Publicat
				
				// catalogador, el que hi havia (i es pot editar)
				document.etiqForm.responsable.value=catA;
				if(perm=="si") document.etiqForm.responsable.removeAttribute('readOnly');
				
				// validador, l'usuari actual (i es pot editar)
				if(document.getElementById("capaVal"))
				{
					//document.etiqForm.validador.value=pubA; // validador, el que hi havia (i es pot editar)
					document.etiqForm.validador.value=usuariActual;
					if(perm=="si") document.etiqForm.validador.removeAttribute('readOnly');
				}
				break;			
			case "10":							//Pendent
			
				// catalogador, ningun (i no es pot editar)
				document.etiqForm.responsable.value=" ";
				document.etiqForm.responsable.setAttribute('readOnly','readonly');
				
				// validador, ningun (i no es pot editar)
				if(document.getElementById("capaVal"))
				{
					document.etiqForm.validador.setAttribute('readOnly','readonly');
					document.etiqForm.validador.value="";
				}
				break;
			default: break;
		}
	}
	
	//
	function selecciona(idSel,idSel2,ordenacio)
	{
		  var selOrigen = document.getElementById(idSel);
		  var selDesti = document.getElementById(idSel2);
		  pos=0;		//conte la ultima posicio valida de la llista de l'esquerra
		  i=0;		//es recorre la llista de l'esquerra
		  for (; i<selOrigen.options.length; i++) {
		    if (selOrigen.options[i].selected)	//afegeixo l'entrada a la llista de la dreta
		    {
		    	selDesti.options[selDesti.options.length] = new Option(selOrigen.options[i].innerHTML, selOrigen.options[i].value);	   
		    }
		    else							//moc l'entrada a la posicio de la llista de l'esquerra q li toca
		    {								//i incremento l'index de la posicio
		    	selOrigen.options[pos]=new Option(selOrigen.options[i].innerHTML, selOrigen.options[i].value);
		    	pos++;
		    }
		  }
		  ultim=pos;
		  for(;pos< i;pos++)					//esborro totes les posicions sobrants
		  {
		  	selOrigen.options[ultim]=null;
		  }	
		if(ordenacio.indexOf("no")<0)
		{
			//ordeno la llista desti  
			var tmpArray = new Array(selDesti.options.length);
			for (i=0;i<selDesti.options.length;i++)	tmpArray[i] = new Option(selDesti.options[i].text,selDesti.options[i].value);
			if(ordenacio.indexOf("alfabetic")>=0)		tmpArray.sort(ordreAlfabeticOpcions);
			if(ordenacio.indexOf("valor")>=0)			tmpArray.sort(ordreValueOpcions);
			for (i=0;i<selDesti.options.length;i++)	selDesti.options[i] = new Option(tmpArray[i].text,tmpArray[i].value);
		}

	}
	
	function ordreAlfabeticOpcions(a,b)
	{
		var a_pla= a.text.replace(translate_re, function(match) { 
	      return translate[match]; 
		});
		var b_pla= b.text.replace(translate_re, function(match) { 
	      return translate[match]; 
		});
		return a_pla > b_pla;	
	}
	function ordreValueOpcions(a,b)
	{
		return a.value > b.value;	
	}
	
	 var translate = {
		  "ä": "a", "ö": "o", "ü": "u",
	      "Ä": "A", "Ö": "O", "Ü": "U",
	      "á": "a", "à": "a", "â": "a",
	      "é": "e", "è": "e", "ê": "e",
	      "ú": "u", "ù": "u", "û": "u",
	      "ó": "o", "ò": "o", "ô": "o",
	      "Á": "A", "À": "A", "Â": "A",
	      "É": "E", "È": "E", "Ê": "E",
	      "Ú": "U", "Ù": "U", "Û": "U",
	      "Ó": "O", "Ò": "O", "Ô": "O",
	      "ß": "s"
	  };
	  var translate_re = /[öäüÖÄÜáàâéèêúùûóòôÁÀÂÉÈÊÚÙÛÓÒÔß]/g;
	  

	var actMT;
	function setMTKey(elem, id){
		if (actMT != null)
			actMT.className = "";
		actMT = elem;
		actMT.className = "selected";		
	}