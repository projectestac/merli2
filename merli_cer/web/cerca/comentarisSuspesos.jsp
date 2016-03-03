<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador,simpple.xtec.web.util.Directori,simpple.xtec.web.util.ComentariObject,simpple.xtec.web.util.RecursObject" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, java.util.Hashtable, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.DucObject, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="simpple.xtec.web.util.ResultGeneratorUtil"%>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%
    Logger logger = Logger.getLogger("comentarisSuspesos.jsp");

    String sLang = XMLCollection.getLang(request);
    logger.debug("Last url: " + UtilsCercador.getLastUrl(request));
    session.setAttribute("lastUrl", UtilsCercador.getLastUrl(request));

    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }

    String usuari = (String) session.getAttribute("nomUsuari");
    String usuariNomComplet = "";
    int comentarisSuspesos = 0;
    if (usuari == null) {
        //usuari = (String)request.getRemoteUser();
        usuari = (String) session.getAttribute("user");
    }

    logger.debug("Usuari: " + usuari);
    String userGeneric = (String) session.getAttribute("userGeneric");
    if (userGeneric == null) {
        userGeneric = "";
    }

   // Passen usuaris validats, i usuari anonim XTEC
    //if ((usuari == null) && Configuracio.sso.equals("si") && !userGeneric.equals("XTEC")){
    if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null))) {
        /*logger.debug("Redirect SSO");
         response.setHeader("Osso-Paranoid", "true");
         response.sendError(499, "Oracle SSO");*/
        response.sendRedirect("../loginSSO.jsp");
    } else {
        logger.debug("Usuari 2: " + usuari);
        usuariNomComplet = (String) session.getAttribute("usuariNomComplet");
        if (usuariNomComplet == null) {
            usuariNomComplet = UtilsCercador.getNomComplet(usuari);
            session.setAttribute("usuariNomComplet", usuariNomComplet);
        }
        comentarisSuspesos = UtilsCercador.getComentarisSuspesos(usuari);

        Connection myConnection = UtilsCercador.getConnectionFromPool();

        String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb;
        String contextWeb = Configuracio.contextWebAplicacio;
        String cssFile = urlLocal + "/" + contextWeb + "/css/merli.css";
        String cssFilePrint = urlLocal + "/" + contextWeb + "/css/merli-print.css";
        String cssFileIE = urlLocal + "/" + contextWeb + "/css/ie6.css";
        String cercaCompleta = urlLocal + "/" + contextWeb + "/cerca/cercaCompleta.jsp";
        String cercaSimple = urlLocal + "/" + contextWeb + "/cerca/cercaSimple.jsp";

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

        Directori myDirectori = null;


%> 
<html lang="ca">
    <head>
        <title><%=XMLCollection.getProperty("cerca.directoriInicial.titol", sLang)%></title>
        <link rel="stylesheet" type="text/css" href="<%=cssFile%>"/>
        <link rel="stylesheet" href="<%=cssFilePrint%>" media="print" type="text/css" />
        <link rel="shortcut icon" href="<%=urlLocal%>/<%=Configuracio.contextWebAplicacio%>/imatges/merli.ico" />
        <script type="text/javascript">
            <% int i = 0;
                while (i < allLevels.size()) {
                    DucObject ducLevel = (DucObject) allLevels.get(i);
            %>
            var areas_<%=ducLevel.id%> = new Array("<%=XMLCollection.getProperty("cerca.select.nivell", sLang)%>", "-1"
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

                if (nivell_educatiu != 0) {


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

        </script>
        <!--[if IE 6]>
        <link rel="stylesheet" href="<%=cssFileIE%>" type="text/css" />
        <![endif]-->

    </head>
    <body>
        <div id="non-footer">

            <div id="header">		    
                <!--  empieza cabecera   -->    
                <%=ResultGeneratorUtil.htmlHeaderCabecera("/" + Configuracio.contextWebAplicacio, "edu365.cat", UtilsCercador.getLastUrl(request))%>                        

                <%=ResultGeneratorUtil.htmlHeaderMenu(usuari, usuariNomComplet, "/" + Configuracio.contextWebAplicacio, comentarisSuspesos, sLang)%>

                <div id="barra_buscador">		             			 		                      	                      
                </div>
            </div>		                                   

            <!--  hasta aqui cabecera   -->    
            <div id="barra_resultats">		  	            			
                <div id="barra_fitxa_left"><b><%=XMLCollection.getProperty("cerca.comentaris.suspesos", sLang)%></b></div>			
                <div id="barra_fitxa_right">	<!-- 		1-10 de 582 resultats  --></div>	    
            </div>


            <% try { %>

            <div id="comentaris_suspesos">
                <!--   <div id="cos_blau"> -->

                <%
                    ArrayList allComments = UtilsCercador.getComentarisSuspesosUsuari(usuari);
                    i = 0;
                    while (i < allComments.size()) {
                        ComentariObject myComentari = (ComentariObject) allComments.get(i);
                        long id = myComentari.id;
                        String comentari = myComentari.comentari;
                        String titol = myComentari.titol;
                        int puntuacio = myComentari.puntuacio;
                        String autor = myComentari.autor;

                        String dataEdicio = myComentari.dataEdicio;

                        String dia = UtilsCercador.getDia(dataEdicio);
                        String hora = UtilsCercador.getHora(dataEdicio);
                        String urlRecurs = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + myComentari.idRecurs;
                        if ((i % 2) == 0) {
                %>           
                <div id="comentari_senar2">
                    <% } else { %>
                    <div id="comentari_parell2">           
                        <% } %>
                        <table border="0" summary="">
                            <caption/>
                            <tr>
                                <td align="left">
                                    <% if (puntuacio <= 0) { %>
                                    <img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                    <% } %>
                                    <% if (puntuacio == 1) { %>
                                    <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                    <% } %>
                                    <% if (puntuacio == 2) { %>
                                    <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                    <% } %>
                                    <% if (puntuacio == 3) { %>
                                    <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                    <% } %>
                                    <% if (puntuacio == 4) { %>
                                    <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif"  alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                    <% } %>
                                    <% if (puntuacio == 5) { %>
                                    <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/>
                                    <% }%>		
                                    <a href="<%=urlRecurs%>"><b><%=titol%></b></a>
                                    <span class="autor_comentari">
                                        <b><%=dia%></b>
                                    </span>
                                </td></tr>
                            <tr><td><%=comentari%></td></tr></table>
                    </div>



                    <%
                            i++;
                        }
                    %>


                    <!-- </div> -->
                </div>


            </div>			
            <div class="footer_cercador">	                                          
                <div id="pie">                                                  
                    <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.avisLegal", sLang)%></a> |                                                  
                    <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.privadesa", sLang)%></a> |                                                  
                    <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.condicionsUs", sLang)%></a> |                              
                    <a href="#"> Copyright © 2014, Generalitat de Catalunya</a> <br/>         
                    <a href="http://www.xtec.cat/web/guest/avis"><%=XMLCollection.getProperty("cerca.directoriInicial.responsabilitat", sLang)%></a>                             
                </div>	 	                             
            </div>

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
    }
%>