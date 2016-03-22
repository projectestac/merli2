<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.DucObject" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.Hashtable, java.util.ArrayList, simpple.xtec.web.util.XMLCollection" %>
<%@ page pageEncoding="UTF-8" %>

<%

    Logger logger = Logger.getLogger("cercaAnonima.jsp");
    String sLang = XMLCollection.getLang(request);
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    logger.debug("Nivell educatiu -> " + request.getParameter("nivell_educatiu"));
    logger.debug("Area curricular -> " + request.getParameter("area_curricular"));
    session.setAttribute("userGeneric", "edu365");
    String idioma = (String) session.getAttribute("idioma");
    if (idioma == null) {
        idioma = Configuracio.idioma;
    }
    Locale myLocale = new Locale(idioma);
    int comentarisSuspesos = 0;
    Connection myConnection = UtilsCercador.getConnectionFromPool();
    ArrayList allLevels = (ArrayList) session.getAttribute("levels");
    Hashtable allCicles = (Hashtable) session.getAttribute("cicles");
    Hashtable allAreas = (Hashtable) session.getAttribute("areas");
    if (allLevels == null) {
        allLevels = UtilsCercador.getAllLevels(myConnection);
        allCicles = UtilsCercador.getAllCicles(myConnection, allLevels);
        allAreas = UtilsCercador.getAllAreas(myConnection, allLevels);
        session.setAttribute("levels", allLevels);
        session.setAttribute("cicles", allCicles);
        session.setAttribute("areas", allAreas);
    } else {
        logger.debug("Cached!");
    }

    try {

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
    <head>
        <title><%=XMLCollection.getProperty("cerca.cercaSimple.titol", sLang)%></title>
        <link rel="stylesheet" type="text/css" href="../css/merli.css" media="all"/>
        <script language="Javascript" type="text/javascript">
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


            function save_area() {
                var area_curricular = document.cerca.area_curricular[document.cerca.area_curricular.selectedIndex].value
                setCookie("area_curricular", area_curricular);
            }


            function recoverValues() {
                var nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value
                if (nivell_educatiu > 0) {
                    change_area();
                    var area_curricular = getCookie("area_curricular");
                    for (var i = 0; i < document.cerca.area_curricular.length; i++)
                    {
                        if (document.cerca.area_curricular[i].value == area_curricular) {
                            document.cerca.area_curricular.selectedIndex = i;
                        }
                    }
                }
            }

            function doSubmit() {
                var value = document.cerca.textCerca.value;
                document.cerca.textCerca.value = value.toString();
                document.cerca.submit();
            }
        </script>
        <script type="text/javascript" src="../scripts/cookies.js"></script>
    </head>	
    <body onLoad="recoverValues()">
        <div id="non-footer">  

            <div id="login">
                <a href="/<%=Configuracio.contextWebAplicacio%>/loginSSO.jsp?logOn=true"><%=XMLCollection.getProperty("cerca.directoriInicialArea.login", sLang)%></a>
            </div>


            <div id="header_formulari">
                <div id="header_formulari_left">
                    <img src="../imatges/logo_cercador.gif">
                </div>
                <div id="header_formulari_right">
                    <h2>
                        <a href="/<%=Configuracio.contextWebAplicacio%>/cerca/cercaCompleta.jsp"><%=XMLCollection.getProperty("cerca.cercaSimple.cercaCompleta", sLang)%></a> |		
                        <a href="/<%=Configuracio.contextWebAplicacio%>/cerca/directoriInicial.jsp"><%=XMLCollection.getProperty("cerca.cercaCompleta.catalegMerli", sLang)%></a> |		
                        <a href=" <%=XMLCollection.getProperty("cerca.urlajuda.cercaavancada", sLang)%>"><%=XMLCollection.getProperty("cerca.cercaSimpleTest.ajuda", sLang)%></a>
                    </h2>
                </div>
                <div class="clear"></div>
            </div> <!-- header_formulari -->

            <div id="barra_resultats">
                <div id="barra_resultats_left">
                    Cerca simple
                </div>
                <div id="barra_resultats_right">
                </div>
                <div class="clear"></div>
            </div>  <!-- barra_resultats -->

            <div id="cercador_complet">  
                <form name="cerca" action="/<%=Configuracio.contextWebAplicacio%>/ServletCerca" method="get">
                    <input type="hidden" name="tipus" value="simple"/>
                    <input type="hidden" name="nivell" value="0"/>
                    <input type="hidden" name="ordenacio" value=""/>
                    <input type="hidden" name="direccio" value=""/>
                    <input type="hidden" name="novaCerca" value="si"/>
                    <input type="hidden" name="userGeneric" value="edu365"/>
                    <fieldset>
                        <br/>
                        <label for="textCerca"><%=XMLCollection.getProperty("cerca.cercaSimpleTest.textLliure", sLang)%>: </label>						   
                        <input class="text_lliure" type="text" id="textCerca" name="textCerca" value="<%=XMLCollection.getProperty("cerca.cercaSimpleTest.textInicial", sLang)%>" onClick="javascript:document.cerca.textCerca.value = '';"/>
                        <br/>

                        <label for="nivell_educatiu"><%=XMLCollection.getProperty("cerca.cercaSimpleTest.nivellEducatiu", sLang)%>: </label>
                        <select id="nivell_educatiu" name="nivell_educatiu" onchange="change_area()">		   
                            <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.nivell", sLang)%></option>
                            <%

                                i = 0;
                                while (i < allLevels.size()) {
                                    DucObject ducObject = (DucObject) allLevels.get(i);
                                      // FIXME: Treure quan FP estigui disponible al DUC
                                    //if(ducObject.getTerm(sLang).indexOf("FP")<0){
%>   
                            <option value="<%=ducObject.id%>"><%=ducObject.getTerm(idioma)%></option>
                            <%
                                    //}
                                    i++;
                                }
                            %>
                        </select>
                        <br/>
                        <label for="area_curricular"><%=XMLCollection.getProperty("cerca.cercaSimpleTest.areaCurricular", sLang)%>: </label>
                        <select id="area_curricular" name="area_curricular" onchange="save_area()">		   
                            <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.area", sLang)%></option>
                        </select>
                        <br/>
                        <!--NADIM 03/07/2015-->
                        <button class="submit" onClick="javascript:doSubmit();
                                return false;"><%=XMLCollection.getProperty("cerca.cercaSimpleTest.cerca", sLang)%></button>
                                        <!--<input class="submit" type="submit" name="cerca" value="<%=XMLCollection.getProperty("cerca.cercaSimpleTest.cerca", sLang)%>"/>-->
                        <br/>
                    </fieldset>
                </form>	

            </div>
        </div>
        <div class="footer_cercador">
            <div>
                <img style="float:right" src="../imatges/xtec_edu365.gif">
            </div>		
        </div>


        <%
            } catch (Exception e) {
                logger.error(e);
            } finally {
                try {
                    myConnection.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }

        %>   



    </body>
</html>
