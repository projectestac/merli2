<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.DucObject" %>
<%@ page import="simpple.xtec.web.util.ResultGeneratorUtil" %>
<%@ page import="org.apache.log4j.Logger, java.util.Enumeration, java.util.Hashtable, java.util.Locale, java.util.ArrayList, simpple.xtec.web.util.TipusFitxer, simpple.xtec.web.util.XMLCollection, simpple.xtec.web.util.Idioma" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%
    Connection myConnection = null;
    Logger logger = Logger.getLogger("cercaCompleta.jsp");
    String sLang = XMLCollection.getLang(request);
    try {
        session.setAttribute("lastUrl", UtilsCercador.getLastUrl(request));
        if (Configuracio.isVoid()) {
            Configuracio.carregaConfiguracio();
        }

        String usuari = request.getParameter("nomUsuari");
        String usuariNomComplet = "";
        int comentarisSuspesos = 0;
        logger.debug("Usuari 1: " + usuari);
        if (usuari == null) {
            //usuari = (String)request.getRemoteUser();
            usuari = (String) session.getAttribute("user");
            logger.debug("Usuari null!");
        } else {
            session.setAttribute("nomUsuari", usuari);
        }

        String userGeneric = request.getParameter("userGeneric");
        if (userGeneric == null) {
            userGeneric = (String) session.getAttribute("userGeneric");
        }

        if ((userGeneric == null) || (!userGeneric.equals("edu365") && !userGeneric.equals("XTEC"))) {
            userGeneric = "";
        }
        logger.debug("Setting userGeneric -> " + userGeneric);
        session.setAttribute("userGeneric", userGeneric);

        logger.debug("SSO: " + Configuracio.sso);

        logger.debug("Usuari 2: " + usuari);
        usuariNomComplet = (String) session.getAttribute("usuariNomComplet");
        if (usuariNomComplet == null) {
            usuariNomComplet = UtilsCercador.getNomComplet(usuari);
            session.setAttribute("usuariNomComplet", usuariNomComplet);
        }
        comentarisSuspesos = UtilsCercador.getComentarisSuspesos(usuari);
        logger.debug("Usuari nom complet: " + usuariNomComplet);
        String idioma = (String) session.getAttribute("idioma");
        if (idioma == null) {
            idioma = Configuracio.idioma;
        }

        myConnection = UtilsCercador.getConnectionFromPool();
        ArrayList allLevels = (ArrayList) session.getAttribute("levels");
        Hashtable allCicles = (Hashtable) session.getAttribute("cicles");
        Hashtable allAreas = (Hashtable) session.getAttribute("areas");
        if (allLevels == null) {
            logger.debug("Loading DUC (Levels) ...");
            allLevels = UtilsCercador.getAllLevels(myConnection);
            logger.debug("Loading DUC (Cicles) ...");
            allCicles = UtilsCercador.getAllCicles(myConnection, allLevels);
            logger.debug("Loading DUC (Areas) ...");
            allAreas = UtilsCercador.getAllAreas(myConnection, allLevels);
            session.setAttribute("levels", allLevels);
            session.setAttribute("cicles", allCicles);
            session.setAttribute("areas", allAreas);
        } else {
            logger.debug("Cached!");
        }

        String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;
        logger.debug("Url local: " + urlLocal);

        String sheetId = request.getParameter("sheetId");
        if ((sheetId == null) || sheetId.equals("null")) {
            sheetId = (String) session.getAttribute("sheetId");
            if ((sheetId == null) || sheetId.equals("null")) {
                sheetId = "";
                session.setAttribute("sheetId", sheetId);
            }
        } else {
            session.setAttribute("sheetId", sheetId);
        }
        logger.debug("Sheet Id: " + sheetId);

        String inxtec = request.getParameter("inxtec");
        int inx = 0;
        if (inxtec != null && !"".equals(inxtec)) {
            inx = Integer.parseInt(inxtec);
        }
        if (inx == ResultGeneratorUtil.SHOW_SEARCH) {
            inx = ResultGeneratorUtil.SHOW_SEARCHRESULTS;
        }
%> 

<html lang="ca">
    <HEAD>
        <link rel="shortcut icon" href="<%=urlLocal%>/imatges/merli.ico" />
        <script type="text/javascript" src="<%=urlLocal%>/scripts/jquery-1.4.2.min.js"></script> 
        <!--<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-main.js"></script>-->
        <!-- import the calendar script -->
        <!--<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar.js"></script>-->
        <!-- import the language module -->
        <!--<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-ca.js"></script>-->
        <!-- import the setup module -->
        <!--<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-setup.js"></script>-->

<!--<script type="text/javascript" src="<%=urlLocal%>/scripts/date-validation.js"></script>-->

        <title><%=XMLCollection.getProperty("cerca.cercaCompleta.titol", sLang)%></title>

        <script type="text/javascript" src="<%=urlLocal%>/scripts/liveUpdater.js">
        </script>




        <script type="text/javascript">

            function checkCR(evt) {

                var evt = (evt) ? evt : ((event) ? event : null);

                var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);

                if ((evt.keyCode == 13) && (node.type == "text")) {
                    doSubmit();
                    return false;
                }

            }

            document.onkeypress = checkCR;

        </script>

        <script type="text/javascript">
            <% int i = 0;
                while (i < allLevels.size()) {
                    DucObject ducLevel = (DucObject) allLevels.get(i);
            %>
            var areas_<%=ducLevel.id%> = new Array("<%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%>", "-1"
            <%
                ArrayList allAreasLevel = (ArrayList) allAreas.get(new Integer(ducLevel.id));
                if (allAreasLevel != null) {
                    int j = 0;
                    while (j < allAreasLevel.size()) {
                        DucObject ducArea = (DucObject) allAreasLevel.get(j);
            %>
            , "<%=ducArea.getTerm(sLang)%>", "<%=ducArea.id%>"
            <%
                        j++;
                    }
                }
            %>
            );
            <%
                    i++;
                }
                i = 0;
                while (i < allLevels.size()) {

                    DucObject ducLevel = (DucObject) allLevels.get(i);
            %>
            var cicles_<%=ducLevel.id%> = new Array("<%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%>", "-1"
            <%
                ArrayList cicles = (ArrayList) allCicles.get(new Integer(ducLevel.id));
                if (cicles == null) {
                    cicles = new ArrayList();
                }
                int j = 0;
                while (j < cicles.size()) {
                    DucObject ducCicle = (DucObject) cicles.get(j);
            %>
            , "<%=ducCicle.getTerm(idioma)%>", "<%=ducCicle.id%>"
            <%
                    j++;
                }
            %>
            );
            <% //  } 
                    i++;
                }%>


            function change_area() {

                var nivell_educatiu
                var cicle
                nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value
                cicle = document.cerca.cicle[document.cerca.cicle.selectedIndex].value
                setCookie("cicle", cicle);
                document.cerca.area_curricular.disabled = false;
                if (nivell_educatiu > 0) {
                    mis_areas = eval("areas_" + nivell_educatiu)
                    num_areas = (mis_areas.length / 2)
                    document.cerca.area_curricular.length = num_areas
                    for (i = 0; i < (num_areas * 2); i = i + 2) {
                        document.cerca.area_curricular.options[i / 2].text = mis_areas[i]
                        document.cerca.area_curricular.options[i / 2].value = mis_areas[i + 1]
                    }
                } else {
                    document.cerca.area_curricular.length = 1
                    document.cerca.area_curricular.options[0].value = "-1"
                    document.cerca.area_curricular.options[0].text = "<%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%>"
                }

                document.cerca.area_curricular.options[0].selected = true
                getProfileArea();
            }


            function change_cicle() {

                var nivell_educatiu
                nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value
                var profileSection = document.getElementById("profileSection");
                profileSection.innerHTML = '<br/>';
                var area_curricular = document.cerca.area_curricular.options[document.cerca.area_curricular.selectedIndex].value;

                if (nivell_educatiu > 0) {
                    my_cicles = eval("cicles_" + nivell_educatiu)

                    num_cicles = (my_cicles.length / 2)

//			alert (getProfile (8590, 2219, 'area'));
                    document.cerca.cicle.length = num_cicles
                    if (num_cicles == 1) {
                        document.cerca.cicle.disabled = true;
                        //change_area();  
                    } else {
                        document.cerca.cicle.disabled = false;
                        document.cerca.area_curricular.disabled = true;
                    }
                    for (i = 0; i < (num_cicles * 2); i = i + 2) {
                        document.cerca.cicle.options[i / 2].text = my_cicles[i]
                        document.cerca.cicle.options[i / 2].value = my_cicles[i + 1]
                    }
//	       alert(num_cicles);
                    change_area();

                } else {
                    document.cerca.area_curricular.disabled = true;
                    document.cerca.cicle.disabled = true;
                    document.cerca.cicle.length = 1
                    document.cerca.cicle.options[0].value = "-1"
                    document.cerca.cicle.options[0].text = "<%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%>"
                }

                document.cerca.cicle.options[0].selected = true
                document.cerca.area_curricular.options[0].selected = true
                getProfileArea();
            }

            function doSubmit() {
                if ($('#dataIniciPublicacio').val() == '<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioPosterior", sLang)%>') {
                    $('#dataIniciPublicacio').val('');
                }
                if ($('#dataFinalPublicacio').val() == '<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioAnterior", sLang)%>') {
                    $('#dataFinalPublicacio').val('');
                }
                document.cerca.submit();
            }


            var httpRequest;

            function getProfileArea() {
                var area_curricular = document.cerca.area_curricular.options[document.cerca.area_curricular.selectedIndex].value
                var area_curricular_text = document.cerca.area_curricular.options[document.cerca.area_curricular.selectedIndex].innerHTML;
                setCookie("area_curricular", area_curricular);
                //alert(area_curricular);
                var nivell = document.cerca.cicle.options[document.cerca.cicle.selectedIndex].value;
                //alert(nivell);       
                if (nivell == -1 || area_curricular_text.indexOf('Compet�ncies') >= 0) {
                    nivell = document.cerca.nivell_educatiu.options[document.cerca.nivell_educatiu.selectedIndex].value;
                }
                //alert("nivell="+nivell+"  area="+area_curricular);                 
                getProfile(area_curricular, nivell, 'area');
            }

            /**
             * This method is called when the author is selected
             * It creates XMLHttpRequest object to communicate with the 
             * servlet 
             */
            function getProfile(ducId, levelId, tipus)
            {
                if (tipus == 'content') {
                    // alert (document.getElementById("" + ducId + "Text").innerHTML);
                    var operacio = document.getElementById("" + ducId + "Text").innerHTML;
                    if (operacio == '+') {
                        document.getElementById("" + ducId + "Text").innerHTML = '-&nbsp;';
                        var url = '/<%=Configuracio.contextWebAplicacio%>/ServletDUC?ducId=' + ducId + '&levelId=' + levelId + '&tipus=' + tipus;

                        if (window.ActiveXObject)
                        {
                            httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                        }
                        else if (window.XMLHttpRequest)
                        {
                            httpRequest = new XMLHttpRequest();
                        }

                        httpRequest.open("GET", url, true);
                        httpRequest.onreadystatechange = function() {
                            processRequest(ducId, tipus);
                        };
                        httpRequest.send(null);
                    } else {
                        document.getElementById("" + ducId).innerHTML = '';
                        document.getElementById("" + ducId + "Text").innerHTML = '+';
                    }
                } else {
                    var url = '/<%=Configuracio.contextWebAplicacio%>/ServletDUC?ducId=' + ducId + '&levelId=' + levelId + '&tipus=' + tipus;
                    if (window.ActiveXObject)
                    {
                        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    else if (window.XMLHttpRequest)
                    {
                        httpRequest = new XMLHttpRequest();
                    }

                    httpRequest.open("GET", url, true);
                    httpRequest.onreadystatechange = function() {
                        processRequest(ducId, tipus);
                    };
                    httpRequest.send(null);

                }
            }

            /**
             * This is the call back method
             * If the call is completed when the readyState is 4
             * and if the HTTP is successfull when the status is 200
             * update the profileSection DIV
             */
            function processRequest(ducId, tipus)
            {
                if (httpRequest.readyState == 4)
                {
                    if (httpRequest.status == 200)
                    {
                        //get the XML send by the servlet
                        var profileXML = httpRequest.responseXML.getElementsByTagName("Resultat")[0];

                        //Update the HTML
                        updateHTML(profileXML, ducId, tipus);
                    }
                    else
                    {
                        alert("Error loading page\n" + httpRequest.status + ":" + httpRequest.statusText);
                    }
                }
            }

            /**
             * This function parses the XML and updates the 
             * HTML DOM by creating a new text node is not present
             * or replacing the existing text node.
             */
            function updateHTML(profileXML, ducId, tipus)
            {

                //The node valuse will give actual data
                var profileText = profileXML.childNodes[0].nodeValue;

                //Create the Text Node with the data received
                var profileBody = document.createTextNode(profileText);
                if (tipus == 'area') {
                    //Get the reference of the DIV in the HTML DOM by passing the ID
                    var profileSection = document.getElementById("profileSection");

                    //Check if the TextNode already exist
                    if (profileSection.childNodes[0])
                    {
                        profileSection.innerHTML = profileText;
                    }
                    else
                    {

                        //If not then append the new Text node
                        profileSection.appendChild(profileBody);
                    }
                }

                if (tipus == 'content') {
                    var profileSection = document.getElementById("" + ducId);
                    profileSection.innerHTML = profileText;

                    // profileSection.appendChild(profileBody);
                }


                if (profileText.indexOf('input') > 0) {
                    document.getElementById("continguts").style.display = "block";
                } else {
                    document.getElementById("continguts").style.display = "none";
                }
            }


        </script>
        <!-- JS del calendari {{{-->
        <script type="text/javascript">
            function initCalendar() {
                Calendar.setup(
                        {
                            inputField: "dataIniciPublicacio", // ID of the input field
                            ifFormat: "%d/%m/%Y", // the date format
                            button: "publicacio_inici", // ID of the button
                            firstDay: 1
                        }
                );
                Calendar.setup(
                        {
                            inputField: "dataFinalPublicacio", // ID of the input field
                            ifFormat: "%d/%m/%Y", // the date format
                            button: "publicacio_fi", // ID of the button
                            firstDay: 1
                        }
                );
                /*
                 Calendar.setup(
                 {
                 inputField  : "dataIniciCatalogacio",         // ID of the input field
                 ifFormat    : "%d/%m/%Y",    // the date format
                 button      : "catalogacio_inici",       // ID of the button
                 firstDay    : 1
                 }
                 );
                 Calendar.setup(
                 {
                 inputField  : "dataFinalCatalogacio",         // ID of the input field
                 ifFormat    : "%d/%m/%Y",    // the date format
                 button      : "catalogacio_fi",       // ID of the button
                 firstDay    : 1			  
                 }
                 );
                 */
            }

            function recoverValues() {
                var nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value
                if (nivell_educatiu > 0) {
                    change_cicle();
                    var cicle = getCookie("cicle");
                    if (cicle != null) {
                        for (var i = 0; i < document.cerca.cicle.length; i++)
                        {
                            if (document.cerca.cicle[i].value == cicle) {
                                document.cerca.cicle.selectedIndex = i;
                            }
                        }
                        change_area();
                    }
                    var area_curricular = getCookie("area_curricular");
                    for (var i = 0; i < document.cerca.area_curricular.length; i++)
                    {
                        if (document.cerca.area_curricular[i].value == area_curricular) {
                            document.cerca.area_curricular.selectedIndex = i;
                        }
                    }
                    if (area_curricular != null) {
                        getProfileArea();
                    }
                }
//initCalendar();
            }


            function setVisibleUnitat(elem) {
                if (elem.value == "<%=TipusFitxer.FISIC%>") {
                    document.cerca.unitatCerca.disabled = false;
                } else {
                    document.cerca.unitatCerca.disabled = true;
                }
            }
            function setVisibleUnitat2(elem) {
                if (elem.checked) {
                    document.cerca.unitatCerca.disabled = false;
                } else {
                    document.cerca.unitatCerca.disabled = true;
                }
            }
            function setFormatsRecurs(elem) {
                rol = document.cerca.recursOnline.checked;	//boolean
                rf = document.cerca.recursFisic.checked;	//
                document.cerca.formatRecurs.length = 0;
                if ((!rol && !rf) || (rol && rf)) {		//si estan tots seleccionats o ningun seleccionat mostrem tots els tipus
                    canviaComboFormats("ambdos");
                }
                else if (rol)
                {
                    canviaComboFormats("online");
                }
                else if (rf)
                {
                    canviaComboFormats("fisics");
                }
            }

            function canviaComboFormats(esfisic) {
                $("select[name='formatRecurs']").load('comboFormats.jsp', {esfisic: esfisic});
            }

            function anyClick(obj) {
                obj.value = '';
                obj.className = 'blacktext';
            }
            function anyBlur(obj, txt) {
                if (obj.value == '') {
                    obj.value = txt;
                    obj.className = 'greytext';
                }
            }

            jQuery(document).ready(function() {
                setFormatsRecurs();
            });

        </script>
        <!-- }}} -->

        <script type="text/javascript" src="<%=urlLocal%>/scripts/cookies.js"></script>	
        <!-- link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/liveUpdater.css" media="all"/-->
        <!-- link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/cercador_complet.css" media="all"/-->
        <link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/merli.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/merli-print.css" media="print"/>

        <!-- <link rel="stylesheet" type="text/css" href="../css/suggest.css" media="all"/>
        <script type="text/javascript" src="<%=urlLocal%>/scripts/suggest.js"> 
        </script> -->




        <style type="text/css">@import url(<%=urlLocal%>/css/calendar-xtec.css);</style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <!--[if IE 6]>
 <link rel="stylesheet" href="<%=urlLocal%>/css/ie6.css" type="text/css" />
 <![endif]-->

    </HEAD>
    <body onLoad="recoverValues()">
        <div id="non-footer">
            <form name="cerca" action="/<%=Configuracio.contextWebAplicacio%>/ServletCerca" method="post" id="formCercaAv">	
                <input type="hidden" name="sheetId" value="<%=sheetId%>"/>
                <input type="hidden" name="tipus" value="completa"/>
                <input type="hidden" name="nivell" value="0"/>
                <input type="hidden" name="ordenacio" value=""/>
                <input type="hidden" name="novaCerca" value="si"/>
                <input type="hidden" name="userGeneric" value="<%=userGeneric%>"/>
                <input type="hidden" name="nomUsuari" value="<%=usuari%>"/>    
                <input type="hidden" name="inxtec" value="<%=inx%>"/> 

                <div id="header">		    
                    <% if (inx <= ResultGeneratorUtil.SHOW_ALL) {%>
                    <%=ResultGeneratorUtil.htmlHeaderCabecera("/" + Configuracio.contextWebAplicacio, "", UtilsCercador.getLastUrl(request))%>			

                    <%=ResultGeneratorUtil.htmlHeaderMenu(usuari, usuariNomComplet, "/" + Configuracio.contextWebAplicacio, comentarisSuspesos, sLang)%>

                    <%}%>		
                    <!--   <div id="barra_buscador">	
                                      <div id="cercadorOptions" class="cercadorAv">
                                              <input type="text" class="textCerca" alt="text cerca" tabindex="1" value="" id="textCerca" name="textCerca" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.textInicial", sLang)%>" onClick="javascript:document.cerca.textCerca.value=''">
                                              <button class="butoMerli small red" onClick="javascript:doSubmit();return false;"><%=XMLCollection.getProperty("cerca.directoriInicial.cerca", sLang)%></button>
                                              &nbsp; <a href="<%=XMLCollection.getProperty("url.ajuda", sLang)%>" target="_blank"><img style="border:0;" src="<%=urlLocal%>/imatges/ajuda.png"/></a>				
                                      </div>
                              </div> -->
                    <!--  hasta aqui cabecera   -->    

                    <div id="barra_resultats" class="barra_cav_left">		  	            			
                        <div id="barra_fitxa_left"><b><%=XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", sLang)%></b></div>			
                    </div>
                </div>  
                <style>
                    .cgngrup{
                        display:table;
                        width:100%;  
                    }
                    #cercador_complet_right{
                        float:left;
                    }
                    #cercador_complet button.butoMerli {
                        float:left;
                        margin:30px 0;
                        position:relative;
                        text-align:center;
                    }
                    .footer_cercador {
                        margin-top:0px;
                    }
                    #barra_resultats {
                        border-top:0px;
                    }
                    .clear {
                        clear:both;
                        height:20px;
                    }
                    .autocomplete {margin-top:52px;}
                </style>


                <div id="cercador_complet">

                    <div class="cgngrup">
                        <div id="cercador_complet_left">
                                <!--label for="textCerca"><%=XMLCollection.getProperty("cerca.cercaCompleta.text", sLang)%>: </label>
                                <input class="text_lliure" type="text" id="textCerca" name="textCerca" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.textInicial", sLang)%>" onClick="javascript:document.cerca.textCerca.value=''";/>
                                <br/>
                            -->
                            <label for="autocomplete-input"><%=XMLCollection.getProperty("cerca.cercaCompleta.textLliure", sLang)%>: &nbsp;&nbsp;
                                <a href="<%=XMLCollection.getProperty("cerca.urlajuda.textlliure", sLang)%>" target="_blank"><img style="border: 0pt none" src="<%=urlLocal%>/imatges/ajuda.png"/></a>  </label>                
                            <input type="text" class="etb" alt="text cerca" tabindex="1" value="" id="textCerca" name="textCerca" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.textInicial", sLang)%>" onClick="javascript:document.cerca.textCerca.value = ''">		
                        </div>
                        <div id="cercador_complet_right">
                            <label for="autocomplete-input"><%=XMLCollection.getProperty("cerca.cercaCompleta.paraulaClau", sLang)%>:        
                                <a style="visibility : hidden;" href="<%=XMLCollection.getProperty("url.ajuda", sLang)%>" target="_blank"><img style="border: 0pt none" src="<%=urlLocal%>/imatges/ajuda.png"/></a>  </label>                
                            <input class="etb" autocomplete="off" id="autocomplete-input" name="keywords" value="" type="text"/>
                            <div id="autocomplete-popup" class="autocomplete">
                                <br/>
                            </div>
                            <br/>
                        </div>
                    </div>
                    <div class="clear"></div>

                    <HR align="center" width=95% color="#DDDDDD"> 

                        <div class="cgngrup">
                            <div id="cercador_complet_left">
                                <label for="nivell_educatiu" ><%=XMLCollection.getProperty("cerca.cercaCompleta.nivellEducatiu", sLang)%>: </label>
                                <select id="nivell_educatiu" name="nivell_educatiu" onchange="change_cicle()">
                                    <option value="-1" selected ><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>		   
                                    <%
                                        i = 0;
                                        while (i < allLevels.size()) {
                                            DucObject ducObject = (DucObject) allLevels.get(i);
                                              // FIXME: Treure quan FP estigui disponible al DUC
                                            //					 if(ducObject.getTerm(sLang).indexOf("FP")<0){
%> 
                                    <option value="<%=ducObject.id%>"><%=ducObject.getTerm(idioma)%></option>
                                    <%
//					 }
                                            i++;
                                        }
                                    %>
                                </select>
                                <br/>

                                <label for="cicle"><%=XMLCollection.getProperty("cerca.cercaCompleta.cicle", sLang)%>: </label>
                                <select id="cicle" name="cicle" disabled="disabled" ONCHANGE="change_area()">
                                    <option value="-1" selected><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                </select>		   
                                <br/>		 		 				 		 	

                                <label for="area_curricular"><%=XMLCollection.getProperty("cerca.cercaCompleta.areaCurricular", sLang)%>: </label>
                                <select id="area_curricular" name="area_curricular" disabled="disabled" ONCHANGE="getProfileArea()">
                                    <option value="-1" selected ><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                </select>
                                <br/>
                            </div>

                            <div id="cercador_complet_right">
                                <!-- <label for="profileSection"> --> 
                                <label id="continguts" style="display:none;"><b><%=XMLCollection.getProperty("cerca.cercaCompleta.categoriaDUC", sLang)%>:</b></label>
                                <br/>
                                <div id="profileSection" name="profileSection" class="profileSection">
                                    <br/>
                                </div>		   
                                <br/>      
                            </div>

                        </div>
                        <div class="clear"></div>

                        <HR align="center" width=95% color="#DDDDDD"> 

                            <div class="cgngrup">
                                <div id="cercador_complet_left">
                                    <label for="autor" ><%=XMLCollection.getProperty("cerca.cercaCompleta.autor", sLang)%>: </label>
                                    <input type="text" name="autorCerca" id="autor" value=""/>
                                    <br/>

                                    <label for="idioma"><%=XMLCollection.getProperty("cerca.cercaCompleta.idioma", sLang)%>: </label>
                                    <select name="idiomaCerca" id="idioma">
                                        <option value="" selected="true"><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                        <%
                                            ArrayList allLanguages = UtilsCercador.getAllLanguages();
                                            i = 0;
                                            while (i < allLanguages.size()) {
                                                Idioma myIdioma = (Idioma) allLanguages.get(i);
                                                String name = myIdioma.getName();
                                                if ((name != null) && !name.trim().equals("")) {
                                        %>
                                        <option value="<%=myIdioma.getCode()%>"><%=myIdioma.getName()%></option>
                                        <%
                                                }
                                                i++;
                                            }
                                        %>       


                                    </select>                      
                                    <BR CLEAR=LEFT>

                                        <label for="recursFisicOnline" ><%=XMLCollection.getProperty("cerca.cercaCompleta.tipusrecurs", sLang)%>: </label> 
                                        <BR CLEAR=LEFT>
                                            <div style="float:left; padding-right:10px;height:24px;">
                                                <input type="checkbox" style="display:block;width:20px;background-color:#E5E5E5;" name="recursOnline" style="clear:left" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.enlinia", sLang)%>" onclick="setFormatsRecurs(this);" />
                                                <%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.enlinia", sLang)%>
                                            </div>
                                            <div style="float:left;height:24px;">
                                                <input type="checkbox" style="display:block;width:20px;background-color:#E5E5E5;" name="recursFisic" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.fisic", sLang)%>" onclick="setVisibleUnitat2(this);
                                                        setFormatsRecurs(this);" />
                                                <%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.fisic", sLang)%>
                                            </div>
                                            <BR CLEAR=LEFT>
                                                <!-- 	<select id="recursFisicOnline" name="recursFisicOnline" onchange="setVisibleUnitat(this);">
                                                                 <option value="" selected="true"><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                                                 <option value="<%=TipusFitxer.FISIC%>"><%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.fisic", sLang)%></option>
                                                                 <option value="<%=TipusFitxer.ENLINIA%>"><%=XMLCollection.getProperty("cerca.cercaCompleta.tipus.enlinia", sLang)%></option>               
                                                                </select>   
                                                                <br/>
                                                                
                                                                <label for="llicencia" ><%=XMLCollection.getProperty("cerca.cercaCompleta.llicencia", sLang)%>: </label>
                                                                <input type="text" name="llicenciaCerca" id="llicencia" value=""/>
                                                                <br/>--> 

                                                <label for="llicencia"><%=XMLCollection.getProperty("cerca.cercaCompleta.llicencia", sLang)%>: </label>
                                                <select name="llicenciaCerca" id="llicenciaCerca">
                                                    <option value="" selected="true"><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                                    <option value="<%=XMLCollection.getProperty("cerca.cercaCompletaTest.attribution.value", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.attribution", sLang)%></option>
                                                    <option value="<%=XMLCollection.getProperty("cerca.cercaCompletaTest.noncomercial.value", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.noncomercial", sLang)%></option>
                                                    <option value="<%=XMLCollection.getProperty("cerca.cercaCompletaTest.noderivateworks.value", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.noderivateworks", sLang)%></option>
                                                    <option value="<%=XMLCollection.getProperty("cerca.cercaCompletaTest.sharealike.value", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.sharealike", sLang)%></option>
                                                    <option value="<%=XMLCollection.getProperty("cerca.cercaCompletaTest.xtec.value", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.xtec", sLang)%></option>
                                                </select>
                                                <br><br>

                                                        <label for="destinatari" ><%=XMLCollection.getProperty("cerca.cercaCompleta.destinatari", sLang)%>: </label>                                                         
                                                        <select name="destinatariCerca" id="destinatari">
                                                            <option value="" selected="true"><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                                            <option value="author"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.author", sLang)%></option>
                                                            <option value="counsellor"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.counsellor", sLang)%></option>
                                                            <option value="learner"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.learner", sLang)%></option>
                                                            <option value="manager"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.manager", sLang)%></option>
                                                            <option value="parent"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.parent", sLang)%></option>
                                                            <option value="teacher"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.teacher", sLang)%></option>
                                                            <option value="other"><%=XMLCollection.getProperty("cerca.cercaCompletaTest.other", sLang)%></option>
                                                        </select>   
                                                        <br/>

                                                        </div>
                                                        <div id="cercador_complet_right">

                                                            <label for="editorial" ><%=XMLCollection.getProperty("cerca.cercaCompleta.editorialProduccio", sLang)%>: </label>
                                                            <input type="text" name="editorialCerca" id="editorial" value=""/>


                                                            <label for="dataIniciPublicacio" ><%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacio", sLang)%>:</label>			
                                                            <input type="text" name="dataIniciPublicacio" id="dataIniciPublicacio" size="10" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioPosterior", sLang)%>" style="width:70px;height:18px;" class="greytext" onblur="javascript:anyBlur(this, '<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioPosterior", sLang)%>');" onfocus="javascript:anyClick(this)"/>
                                                            <input type="text" id="dataFinalPublicacio" name="dataFinalPublicacio" size="10" value="<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioAnterior", sLang)%>" style="width:70px;clear:none;height:18px;" class="greytext" onblur="javascript:anyBlur(this, '<%=XMLCollection.getProperty("cerca.cercaCompleta.dataPublicacioAnterior", sLang)%>');" onfocus="javascript:anyClick(this)"/>


                                                            <label for="tipus"><%=XMLCollection.getProperty("cerca.cercaCompleta.formatRecurs", sLang)%>: </label>
                                                            <select name="formatRecurs" id="tipus">
                                                            </select>


                                                            <label for="unitat"><%=XMLCollection.getProperty("cerca.cercaCompleta.unitat", sLang)%>: </label>
                                                            <select name="unitatCerca" disabled="true" id="unitat">
                                                                <option value="" selected="true"><%=XMLCollection.getProperty("cerca.cercaCompleta.qualsevol", sLang)%></option>
                                                                <%
                                                                    ArrayList allUnits = UtilsCercador.getUnitats();
                                                                    i = 0;
                                                                    Idioma unitat;
                                                                    while (i < allUnits.size()) {
                                                                        unitat = (Idioma) allUnits.get(i);
                                                                        String name = unitat.getName();
                                                                        if ((name != null) && !name.trim().equals("")) {
                                                                %>
                                                                <option value="<%=unitat.getCode()%>"><%=name%></option>
                                                                <%
                                                                        }
                                                                        i++;
                                                                    }
                                                                %>       
                                                            </select> 
                                                            <BR CLEAR=LEFT>
                                                                <BR CLEAR=LEFT>
                                                                    <button  style="clear:left; margin:10pt 0;" class="butoMerli small red" onClick="javascript:doSubmit();
                                                                            return false;"><%=XMLCollection.getProperty("cerca.directoriInicial.cerca", sLang)%></button>

                                                                    <a href="<%=XMLCollection.getProperty("url.ajuda", sLang)%>" target="_blank"><img style="border: 0pt none; margin:10pt;" src="<%=urlLocal%>/imatges/ajuda.png"/></a>                 
                                                                    </div>
                                                                    </div>




                                                                    <div class="clear"></div>






                                                                    </form>	
                                                                    </div>		
                                                                    </div>
                                                                    <!-- JS del calendari {{{-->
                                                                    <script type="text/javascript">
                                                                        /*		  Calendar.setup(
                                                                         {
                                                                         inputField  : "data_publicacio_inici",         // ID of the input field
                                                                         ifFormat    : "%d-%m-%Y",    // the date format
                                                                         button      : "publicacio_inici"       // ID of the button
                                                                         }
                                                                         );
                                                                         Calendar.setup(
                                                                         {
                                                                         inputField  : "data_publicacio_fi",         // ID of the input field
                                                                         ifFormat    : "%d-%m-%Y",    // the date format
                                                                         button      : "publicacio_fi"       // ID of the button
                                                                         }
                                                                         );
                                                                         Calendar.setup(
                                                                         {
                                                                         inputField  : "data_catalogacio_inici",         // ID of the input field
                                                                         ifFormat    : "%d-%m-%Y",    // the date format
                                                                         button      : "catalogacio_inici"       // ID of the button
                                                                         }
                                                                         );
                                                                         Calendar.setup(
                                                                         {
                                                                         inputField  : "data_catalogacio_fi",         // ID of the input field
                                                                         ifFormat    : "%d-%m-%Y",    // the date format
                                                                         button      : "catalogacio_fi"       // ID of the button
                                                                         }
                                                                         );
                                                                         */
                                                                    </script>
                                                                    <!-- }}} -->


                                                                    <script type="text/javascript">
                                                                        autocomplete('autocomplete-input', 'autocomplete-popup', '/<%=Configuracio.contextWebAplicacio%>/ServletAutocompletion')
                                                                    </script>
                                                                    <% if (inx <= ResultGeneratorUtil.SHOW_ALL) {%>
                                                                    <div class="footer_cercador">	                                          
                                                                        <div id="pie">                                                  
                                                                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.avisLegal", sLang)%></a> |                                                  
                                                                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.privadesa", sLang)%></a> |                                                  
                                                                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.condicionsUs", sLang)%></a> |                              
                                                                            <a href="#"> Copyright © 2014, Generalitat de Catalunya</a> <br/>       
                                                                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.responsabilitat", sLang)%></a>                                                            
                                                                        </div>	 	                             
                                                                    </div>		  
                                                                    <% }%>
                                                                    <!-- GOOGLE ANALYTICS -->
                                                                    <script type="text/javascript">
                                                                        var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
                                                                        document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
                                                                    </script>
                                                                    <script type="text/javascript">
                                                                        try {
                                                                            var pageTracker = _gat._getTracker("UA-6935294-1");
                                                                            pageTracker._trackPageview();
                                                                        } catch (err) {
                                                                        }</script>
                                                                    <!-- END -->	  	

                                                                    </body>
                                                                    </html>




                                                                    <%
                                                                            //}
                                                                        } catch (Exception e) {
                                                                            logger.error(e);
                                                                        } finally {
                                                                            if (myConnection != null) {
                                                                                myConnection.close();
                                                                            }
                                                                        }
                                                                    %>	