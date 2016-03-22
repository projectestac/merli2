<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador,simpple.xtec.web.util.Directori,simpple.xtec.web.util.NoticiaObject,simpple.xtec.web.util.RecursObject" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, java.util.Hashtable, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.DucObject, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="simpple.xtec.web.util.ResultGeneratorUtil"%>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%
    Logger logger = Logger.getLogger("directoriInicialArea.jsp");
    logger.debug("Last url: " + UtilsCercador.getLastUrl(request));
    Directori myDirectori = null;
    session.setAttribute("lastUrl", UtilsCercador.getLastUrl(request));

    String sLang = XMLCollection.getLang(request);
    String imprimir = request.getParameter("imprimir");
    String urlImprimir = "";
    if (imprimir == null) {
        imprimir = "no";
    }
    if (imprimir.equals("no")) {
        urlImprimir = UtilsCercador.getLastUrl(request);
        urlImprimir = urlImprimir.replaceAll("&imprimir=no", "");
        urlImprimir = urlImprimir.replaceAll("&imprimir=si", "");
        if (urlImprimir.indexOf("imprimir") == -1) {
            urlImprimir += "&imprimir=si";
        }
    }

    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }

    String usuari = (String) session.getAttribute("nomUsuari");
    String usuariNomComplet = "";
    int comentarisSuspesos = 0;
    if (usuari == null) {
        //usuari = (String) request.getRemoteUser();
        usuari = (String) session.getAttribute("user");
    }

    logger.debug("Usuari: " + usuari);
    String userGeneric = (String) session.getAttribute("userGeneric");
    if (userGeneric == null) {
        userGeneric = "";
    }

    Connection myConnection = UtilsCercador.getConnectionFromPool();
    myDirectori = new Directori(myConnection);

    usuariNomComplet = (String) session.getAttribute("usuariNomComplet");
    if (usuariNomComplet == null) {
        usuariNomComplet = UtilsCercador.getNomComplet(usuari);
        session.setAttribute("usuariNomComplet", usuariNomComplet);
    }
    comentarisSuspesos = myDirectori.getComentarisSuspesos(usuari);

    String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb;
    logger.debug("URL local: " + urlLocal);
    String contextWeb = Configuracio.contextWebAplicacio;
    String cssFile = urlLocal + "/" + contextWeb + "/css/merli.css";
    String cssFilePrint = urlLocal + "/" + contextWeb + "/css/merli-print.css";
    String cssFileIE = urlLocal + "/" + contextWeb + "/css/ie6.css";
    String cercaCompleta = urlLocal + "/" + contextWeb + "/cerca/cercaCompleta.jsp";
    String cercaSimple = urlLocal + "/" + contextWeb + "/cerca/cercaSimple.jsp";

    String idLevel = request.getParameter("idLevel");
    int idLevelInt = (new Integer(idLevel)).intValue();

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

    String nivellSeleccionat = "";

    if (idLevel.equals("2215")) {
        nivellSeleccionat = XMLCollection.getProperty("cerca.educacio.infantil", sLang);
    }
    if (idLevel.equals("2219")) {
        nivellSeleccionat = XMLCollection.getProperty("cerca.educacio.primaria", sLang);
    }
    if (idLevel.equals("2221")) {
        nivellSeleccionat = XMLCollection.getProperty("cerca.educacio.secundaria", sLang);
    }
    if (idLevel.equals("2234")) {
        nivellSeleccionat = XMLCollection.getProperty("cerca.educacio.batxillerat", sLang);
    }

%> 


<html lang="ca">
    <head>
        <title><%=XMLCollection.getProperty("cerca.directoriInicial.titol", sLang)%></title>
        <link rel="shortcut icon" href="<%=urlLocal%>/<%=Configuracio.contextWebAplicacio%>/imatges/merli.ico" />
        <link rel="stylesheet" type="text/css" href="<%=cssFile%>"/>
        <link rel="stylesheet" href="<%=cssFilePrint%>" media="print" type="text/css" />
        <script type="text/javascript">
            <% int i = 0;
                while (i < allLevels.size()) {
                    DucObject ducLevel = (DucObject) allLevels.get(i);
            %>
            var areas_<%=ducLevel.id%> = new Array("<%=XMLCollection.getProperty("cerca.select.area", sLang)%>", "-1"
            <%
                ArrayList allAreasLevel = (ArrayList) allAreas.get(new Integer(ducLevel.id));
                if (allAreasLevel != null) {
                    int j = 0;
                    while (j < allAreasLevel.size()) {
                        DucObject ducArea = (DucObject) allAreasLevel.get(j);
                        if (!ducArea.term.startsWith("Competències")) {
            %>
            , "<%=ducArea.getTerm(sLang)%>", "<%=ducArea.id%>"
            <%
                        }
                        j++;
                    }
                }
            %>
            );
            <%   i++;
                }%>

            function change_area() {

                var nivell_educatiu
                nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value

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
                    document.cerca.area_curricular.options[0].text = "<%=XMLCollection.getProperty("cerca.select.area", sLang)%>"
                }

                document.cerca.area_curricular.options[0].selected = true
            }

            function doSubmit() {
                var value = document.cerca.textCerca.value;
                document.cerca.textCerca.value = value.toString().replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/'/g, "&#39;").replace(/"/g, "&#34;");
                document.cerca.nivell.value = 0;
                document.cerca.tipus.value = 'simple';
                document.cerca.novaCerca.value = 'si';
                document.cerca.submit();
            }
        </script>
        <!--[if IE 6]>
        <link rel="stylesheet" href="<%=cssFileIE%>" type="text/css" />
        <![endif]-->

    </head>
    <body>
        <div id="non-footer">
            <% if (imprimir.equals("no")) {%>

            <div id="header">		
                <%=ResultGeneratorUtil.htmlHeaderCabecera("/" + Configuracio.contextWebAplicacio, "edu365.cat", UtilsCercador.getLastUrl(request))%>                        

                <%=ResultGeneratorUtil.htmlHeaderMenu(usuari, usuariNomComplet, "/" + Configuracio.contextWebAplicacio, comentarisSuspesos, sLang)%>                                  

            </div>		 		                                    
            <div id="barra_buscador">		             			 		                      	                      
                <div id="cercadorOptions">

                    <form name="cerca" action="/<%=Configuracio.contextWebAplicacio%>/ServletCerca" method="post">
                        <fieldset>
                            <input type="hidden" name="tipus" value="simple"/>
                            <input type="hidden" name="nivell" value="0"/>
                            <input type="hidden" name="ordenacio" value=""/>
                            <input type="hidden" name="direccio" value=""/>
                            <input type="hidden" name="novaCerca" value="si"/>	
                            <input type="hidden" name="usuari" value="<%=usuari%>"/>
                            <input type="hidden" name="userGeneric" value="<%=userGeneric%>"/>
                            <input type="hidden" name="nomUsuari" value="<%=usuari%>"/>            
                            <input type="hidden" name="cataleg" value="si"/> 

                            <label for="textCerca" class="hide">Introdu&iuml;u les paraules a cercar</label>
                            <input type="text" size="20" name="textCerca" id="textCerca" tabindex="1" alt="text cerca" class="textCerca" />
                            <select id="nivell_educatiu" name="nivell_educatiu" onchange="change_area()" tabindex="2" class="select">		   
                                <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.nivell", sLang)%></option>
                                <%
                                    i = 0;
                                    while (i < allLevels.size()) {
                                        DucObject ducObject = (DucObject) allLevels.get(i);
                                        // FIXME: Treure quan FP estigui disponible al DUC
                                        // if(ducObject.getTerm(sLang).indexOf("FP")<0){
                                        if (ducObject.id == idLevelInt) {
                                %>   
                                <option value="<%=ducObject.id%>" selected><%=ducObject.getTerm(sLang)%></option>
                                <% } else {%>
                                <option value="<%=ducObject.id%>"><%=ducObject.getTerm(sLang)%></option>
                                <% } %>

                                <%
                                        //}
                                        i++;
                                    }
                                %>
                            </select>
                            <select id="area_curricular" name="area_curricular" tabindex="3" class="select">		   
                                <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.area", sLang)%></option>
                            </select><button class="butoMerli small red" onClick="javascript:doSubmit();
                                    return false;"><%=XMLCollection.getProperty("cerca.directoriInicial.cerca", sLang)%></button>
                            <a href="<%=cercaCompleta%>"><%=XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", sLang)%></a>
                            &nbsp; <a href="<%=XMLCollection.getProperty("url.ajuda", sLang)%>" target="_blank"><img style="border:0;" src="/<%=Configuracio.contextWebAplicacio%>/imatges/ajuda.png"/></a>

                        </fieldset>
                    </form>
                </div>	
            </div>		                                   
            <div id="barra_nivell">

                <div id="menu_nivell">				                                                    
                    <ul>
                        <li><a href="directoriInicialArea.jsp?idLevel=2215"><%=XMLCollection.getProperty("cerca.educacio.infantil", sLang)%></a></li><li>
                            <a href="directoriInicialArea.jsp?idLevel=2219" class="linial"><%=XMLCollection.getProperty("cerca.educacio.primaria", sLang)%></a></li><li>
                            <a href="directoriInicialArea.jsp?idLevel=2221" class="linial"><%=XMLCollection.getProperty("cerca.educacio.secundaria", sLang)%></a></li><li>
                            <a href="directoriInicialArea.jsp?idLevel=2234" class="linial"><%=XMLCollection.getProperty("cerca.educacio.batxillerat", sLang)%></a></li><li>
                            <a href="directoriInicialArea.jsp?idLevel=9698" class="linial"><%=XMLCollection.getProperty("cerca.educacio.fpmitja", sLang)%></a></li><li>
                            <a href="directoriInicialArea.jsp?idLevel=9337" class="linial"><%=XMLCollection.getProperty("cerca.educacio.fpsuperior", sLang)%></a></li></ul>  			
                </div>
            </div> 


            <!--  hasta aqui cabecera   -->   

            <%  } %>
            <div id="main">

                <!-- left {{{-->
                <div id="left">
                    <%
                        String prep = "de ";
                        if (nivellSeleccionat.startsWith("E")) {
                            prep = "d'";
                        }
                    %>
                    <h1><%=XMLCollection.getProperty("cerca.cataleg", sLang)%> <%=prep%><%=nivellSeleccionat%></h1>
                    <h2><%=XMLCollection.getProperty("cerca.directoriInicialArea.areesCurriculars", sLang)%></h2>
                    <div id="arees">
                        <ul>
                            <%
                                ArrayList allAreasLevel = (ArrayList) allAreas.get(new Integer(idLevelInt));
                                if (allAreasLevel == null) {
                                    allAreasLevel = new ArrayList();
                                }
                                // ArrayList allAreas = UtilsCercador.getAreasFromLevel(myConnection, idLevelInt); 	      
                                i = 0;
                                while (i < allAreasLevel.size()) {
                                    DucObject ducObject = (DucObject) allAreasLevel.get(i);
                                    if (!ducObject.term.startsWith("Compet�ncies")) {
                                        String urlArea = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/ServletCerca?tipus=completa&nivell=0&ordenacio=&novaCerca=si&textCerca=&nivell_educatiu=" + idLevelInt + "&area_curricular=" + ducObject.id + "&cataleg=si";
                            %>
                            <li><a href="<%=urlArea%>"><%=ducObject.getTerm(sLang)%></a></li>			
                                <%
                                        }
                                        i++;
                                    }
                                %>
                        </ul>
                    </div>
                </div>
                <!-- }}}-->			

                <!-- right {{{-->
                <div id="right">

                    <%
                        try {
                            myDirectori = new Directori(myConnection);
                            logger.debug("Getting recursosMesBenValorats..");
                            ArrayList recursosMesBenValorats = myDirectori.getRecursosMesBenValorats(idLevelInt);
                            if (recursosMesBenValorats.size() > 0) {
                    %>    	 
                    <div id="taula">
                        <h1><%=XMLCollection.getProperty("cerca.directoriInicialArea.recursosValorats", sLang)%></h1>
                        <%     }
                            i = 0;
                            logger.debug("Size: " + recursosMesBenValorats.size());
                            while (i < recursosMesBenValorats.size()) {
                                RecursObject recursObject = (RecursObject) recursosMesBenValorats.get(i);

                                String urlRecurs = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + recursObject.id;
                                int puntuacio = recursObject.puntuacio;
                                if (i == 0) {
                        %>
                        <div id="primera_fila_table">
                            <%     } else {
                            %>
                            <div id="fila_table">
                                <%
                                    }
                                %>
                                <table summary="">
                                    <caption/>
                                    <tr>
                                        <td class="fila_left_wai">
                                            <a href="<%=urlRecurs%>"><%=recursObject.titol%></a>
                                        </td>
                                        <td class="fila_right_wai">				 

                                            <% if (puntuacio <= 0) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (puntuacio == 1) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (puntuacio == 2) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (puntuacio == 3) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (puntuacio == 4) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (puntuacio == 5) {%>
                                            <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/><img src="/<%=Configuracio.contextWebAplicacio%>/imatges/stars-full.gif" alt=""/>
                                            <% } %>				
                                        </td>
                                        <!-- </div> -->
                                    </tr>
                                </table>
                            </div>

                            <%     i++;
                                }
                                if (recursosMesBenValorats.size() > 0) {
                            %>    	 
                        </div>
                        <%     }
                        %>

                        <%
                            logger.debug("Getting recursosMesVisitats..");
                            ArrayList recursosMesVisitats = myDirectori.getRecursosMesVisitats(idLevelInt);
                            logger.debug("Size: " + recursosMesVisitats.size());
                            if (recursosMesVisitats.size() > 0) {
                        %>    	 
                        <div id="taula">
                            <h1><%=XMLCollection.getProperty("cerca.directoriInicialArea.recursosVisitats", sLang)%></h1>
                            <%     }
                                i = 0;
                                while (i < recursosMesVisitats.size()) {
                                    RecursObject recursObject = (RecursObject) recursosMesVisitats.get(i);
                                    String id = recursObject.id;
                                    int numVisites = recursObject.numVisites;
                                    String titol = recursObject.titol;
                                    String urlRecurs = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + id;
                                    String textVisites = "visites";
                                    if (numVisites == 1) {
                                        textVisites = "visita";
                                    }
                                    if (i == 0) { %>
                            <div id="primera_fila">
                                <%     } else { %>
                                <div id="fila">
                                    <%	  	   }%>
                                    <a href="<%=urlRecurs%>"><%=titol%></a><h2>(<%=numVisites%> <%=textVisites%>)</h2>
                                </div>

                                <%     i++;
                                    }
                                    if (recursosMesVisitats.size() > 0) {
                                %>    	 
                            </div>
                            <%     }
                            %>

                            <%
                                logger.debug("Getting recursosMesComentats..");
                                ArrayList recursosMesComentats = myDirectori.getRecursosMesComentats(idLevelInt);
                                if (recursosMesComentats.size() > 0) {
                            %>    	 
                            <div id="taula">
                                <h1><%=XMLCollection.getProperty("cerca.directoriInicialArea.recursosComentats", sLang)%></h1>
                                <%     }
                                    i = 0;
                                    logger.debug("Size: " + recursosMesComentats.size());
                                    while (i < recursosMesComentats.size()) {
                                        RecursObject recursObject = (RecursObject) recursosMesComentats.get(i);

                                        String urlRecurs = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + recursObject.id;

                                        if (i == 0) {
                                %>
                                <div id="primera_fila">
                                    <%     } else {
                                    %>
                                    <div id="fila">
                                        <%
                                            }
                                        %>
                                        <% String textComentaris = "comentaris";
                                            if (recursObject.numComentaris == 1) {
                                                textComentaris = "comentari";
                                            }
                                        %>  
                                        <a href="<%=urlRecurs%>"><%=recursObject.titol%></a><h2>(<%=recursObject.numComentaris%> <%=textComentaris%>)</h2>
                                    </div>			

                                    <%     i++;
                                        }
                                        if (recursosMesComentats.size() > 0) {
                                    %>    	 
                                </div>
                                <%     }
                                %>

                                <div class="espai">&nbsp;<img style="float:right" src="<%=urlLocal%>/<%=contextWeb%>/img/footer_generic.png" alt=""></div>
                            </div>  <!-- right -->
                            <!-- <div id="clear"></div> -->
                            <!-- <div class="espai">&nbsp;</div> -->
                        </div>		<!-- main -->		
                        <div class="espai">&nbsp;<img style="float:right" src="<%=urlLocal%>/<%=contextWeb%>/img/footer_generic.png" alt=""></div> 
                    </div>  <!-- non-footer -->
                    <% if (imprimir.equals("no")) {%>
                    <div class="footer_cercador">	                                          
                        <div id="pie">                                                  
                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.avisLegal", sLang)%></a> |                                                  
                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.privadesa", sLang)%></a> |                                                  
                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.condicionsUs", sLang)%></a> |                              
                            <a href="#"> Copyright © 2014, Generalitat de Catalunya</a> <br/>       
                            <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.responsabilitat", sLang)%></a>                               
                        </div>	 	                             
                    </div>	
                    <%   }   %>

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


                    <script type="text/javascript">
                        change_area();
                    </script>

                    <%

                        } catch (Exception e) {
                            logger.error(e);
                        } finally {
                            try {
                                if (myConnection != null) {
                                    myConnection.close();
                                }
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        }
                    %>   	