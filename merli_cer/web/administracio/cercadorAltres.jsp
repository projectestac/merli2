<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale" %>
<%@ page pageEncoding="UTF-8" %>
<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Logger logger = Logger.getLogger("cercadorAltres.jsp");
    //String usuari = (String)request.getRemoteUser();
    String usuari = (String) session.getAttribute("user");

    logger.debug("Usuari 1: " + usuari);

    //if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null)) && Configuracio.sso.equals("si")) {
    if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null))) {
        /*logger.debug("Redirect SSO");
         response.setHeader("Osso-Paranoid", "true");
         response.sendError(499, "Oracle SSO");*/
        response.sendRedirect("../loginSSO.jsp");

    } else {

        String idioma = (String) session.getAttribute("idioma");
        if (idioma == null) {
            idioma = Configuracio.idioma;
        }
        Locale myLocale = new Locale(idioma);

%> 
<%    String tipusCercador = "altres";

    int longitudDescripcio = -1;
    int resultatsPagina = -1;
    int nombreNovetats = -1;
    int tempsVidaNovetat = -1;
    float pes_title = (float) 0.0;
    float pes_description = (float) 0.0;
    float pes_text = (float) 0.0;
    float pes_keywords = (float) 0.0;

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    String sql = "";

    try {
        conn = UtilsCercador.getConnectionFromPool();

        stmt = conn.createStatement();
        sql = "select * from config_cerca where cercador_id=3";
        rs = stmt.executeQuery(sql);
        rs.next();
        longitudDescripcio = rs.getInt("long_desc");
        resultatsPagina = rs.getInt("max_resultats_pag");
        nombreNovetats = rs.getInt("nombre_novetats");
        tempsVidaNovetat = rs.getInt("temps_vida_novetat");
        /*      sql = "select * from pesos where tipusCercador='" + tipusCercador + "'";
         rs = stmt.executeQuery(sql);
         rs.next();       
         pes_title = rs.getFloat("pes_title");
         pes_description = rs.getFloat("pes_description");
         pes_text = rs.getFloat("pes_text");
         pes_keywords = rs.getFloat("pes_keywords");        */
%>

<jsp:include page="topAdministracio.jsp?selected=3" />
<script language="Javascript">
    function doSubmit() {
        var myForm = document.modificar;
        for (i = 0; i < myForm.elements.length; i++) {
            if (myForm.elements[i].type == "text" && isNaN(myForm.elements[i].value)) {
                alert("<%=XMLCollection.getProperty("administracio.cercadorAltres.errorNumeric")%>");
                myForm.elements[i].focus();
                return false;
            }
            if (myForm.elements[i].type == "text" && (myForm.elements[i].value < 0)) {
                alert("<%=XMLCollection.getProperty("administracio.cercadorAltres.errorPositiu")%>");
                myForm.elements[i].focus();
                return false;
            }
            if (myForm.elements[i].type == "text" && (myForm.elements[i].value == '')) {
                alert("<%=XMLCollection.getProperty("administracio.cercadorAltres.errorValor")%>");
                myForm.elements[i].focus();
                return false;
            }

        }
        document.modificar.submit();
    }
</script>

<div id="content">
    <form name="modificar" action="/<%=Configuracio.contextWebAplicacio%>/ServletConfiguracio" method="post">     
        <input type="hidden" name="tipusCercador" value="<%=tipusCercador%>"/>
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.cercadorAltres.parametres_cercador")%></legend>
            <p class="area_form"><%=XMLCollection.getProperty("administracio.cercadorAltres.aparenca_resultats")%></p>

            <div id="linia">
                <label class="cercador" for="longitudDescripcio"><%=XMLCollection.getProperty("administracio.cercadorAltres.longitud_descripcio")%></label>
                <input type="text" name="longitudDescripcio" size="3" maxlength="3" value="<%=longitudDescripcio%>" />
            </div>
            <div id="linia">
                <label class="cercador" for="resultatsPagina"><%=XMLCollection.getProperty("administracio.cercadorAltres.resultats_per_pagina")%></label>
                <input type="text" name="resultatsPagina" size="3" maxlength="3" value="<%=resultatsPagina%>"/>
            </div>
            <div id="linia">
                <label class="cercador" for="nombreNovetats"><%=XMLCollection.getProperty("administracio.cercadorAltres.nombre_novetats")%></label>
                <input type="text" name="nombreNovetats" size="3" maxlength="3" value="<%=nombreNovetats%>"/>
            </div>
            <div id="linia">
                <label class="cercador" for="tempsVidaNovetat"><%=XMLCollection.getProperty("administracio.cercadorAltres.temps_vida_novetat")%></label>
                <input type="text" name="tempsVidaNovetat" size="3" maxlength="3" value="<%=tempsVidaNovetat%>"/>
            </div>

            <p class="area_form"><%=XMLCollection.getProperty("administracio.cercadorAltres.algoritme_ordenacio")%></p>

            <%
                stmt = conn.createStatement();
                sql = "select * from config_pesos_index where cercador_id=3";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String nom = rs.getString("nom");
                    String nomCamp = rs.getString("nom_camp");
                    String valor = rs.getString("valor");
            %>

            <div id="linia">
                <label class="cercador" for="pes_<%=nomCamp%>"><%=nom%></label>
                <input type="text" name="pes_<%=nomCamp%>" size="3" maxlength="3" value="<%=valor%>" />
            </div>

            <%           }
                } catch (Exception e) {
                    logger.error(e);
                } finally {
                    try {
                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }


            %>

            <div id="linia">
                <input type="button" name="Modificar" value="<%=XMLCollection.getProperty("administracio.cercadorAltres.modificar")%>" class="tableButton" onClick="javascript:doSubmit();"/>

            </div>
        </fieldset>
    </form>
</div>   


<jsp:include page="bottomAdministracio.jsp" />
<% }%>