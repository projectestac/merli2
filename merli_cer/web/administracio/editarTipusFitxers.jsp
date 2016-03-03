<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.UtilsCercador" %>
<%@ page pageEncoding="UTF-8" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }

    Logger logger = Logger.getLogger("editarTipusFitxers.jsp");
    String idioma = (String) session.getAttribute("idioma");

    //String usuari = (String)request.getRemoteUser();
    String usuari = (String) session.getAttribute("user");
    String idTipusFitxers = request.getParameter("idTipusFitxers");
    logger.debug("Usuari 1: " + usuari);

    //if ( (!UtilsCercador.isUserInRole(usuari) ||(usuari == null)) && Configuracio.sso.equals("si")){
    if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null))) {
        /*logger.debug("Redirect SSO");
         response.setHeader("Osso-Paranoid", "true");
         response.sendError(499, "Oracle SSO");*/
        response.sendRedirect("../loginSSO.jsp");
    } else {

        if (idioma == null) {
            idioma = Configuracio.idioma;
        }
        Locale myLocale = new Locale(idioma);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";

        String nomGrupOld = "";
        String mimeTypeOld = "";
        try {

            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            conn = UtilsCercador.getConnectionFromPool();
            stmt = conn.createStatement();
            sql = "SELECT * from tipus_fitxers WHERE id=" + idTipusFitxers;
            rs = stmt.executeQuery(sql);
            rs.next();
            nomGrupOld = rs.getString("nomGrup");
            mimeTypeOld = rs.getString("mimeType");
            rs.close();
            stmt.close();

%> 

<jsp:include page="topAdministracio.jsp?selected=8" />

<script language="Javascript">
    function eliminar(idTipusFitxers) {
        if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.tipusFitxers.eliminar_confirmacio"))%>")) {
            document.tipusFitxers.operacio.value = 'eliminar';
            document.tipusFitxers.idTipusFitxers.value = idTipusFitxers;
            document.tipusFitxers.submit();
        }
    }
    function editar(idTipusFitxers) {
        document.editarTipusFitxers.idTipusFitxers.value = idTipusFitxers;
        document.editarTipusFitxers.submit();
    }

</script>

<div id="content">
    <form name="editarTipusFitxers" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/cerca/editarTipusFitxers.jsp">
        <input type="hidden" name="idTipusFitxers" value=""/>
    </form>  
    <form name="tipusFitxers" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/ManagerTipusFitxers">
        <input type="hidden" name="operacio" value="modificar"/>
        <input type="hidden" name="idTipusFitxers" value="<%=idTipusFitxers%>"/>
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.tipusFitxers.titol")%></legend>
            <div id="linia">
                <label for="nouGrup"><%=XMLCollection.getProperty("administracio.tipusFitxers.tipusFitxers")%></label>
                <input type="text" name="nouGrup" id="nouGrup" value="<%=nomGrupOld%>"/><br/>
                <label for="mimeType"><%=XMLCollection.getProperty("administracio.tipusFitxers.mimeType")%></label>	    
                <!-- <input type="text" name="mimeType" id="mimeType" value=""/><br/>-->
                <textarea name="mimeType" id="mimeType" value="" cols="40"><%=mimeTypeOld%></textarea><br/>
                <input type="submit" value="<%=XMLCollection.getProperty("administracio.tipusFitxers.Modificar")%>" name="<%=XMLCollection.getProperty("administracio.tipusFitxers.Modificar")%>"/>
            </div>
        </fieldset>     
    </form>
    <br/>

    <table class="taula_usuaris">
        <caption>
            <%=XMLCollection.getProperty("administracio.tipusFitxers.titol_disponibles")%>
        </caption>   
        <%

            stmt = conn.createStatement();
            sql = "SELECT * from tipus_fitxers ORDER by nomGrup ASC";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String nomGrup = rs.getString("nomGrup");
                String mimeType = rs.getString("mimeType");
                int id = rs.getInt("id");
                if (!idTipusFitxers.equals("" + id)) {
        %>
        <tr>
            <td class="taula_usuaris_nom"><%=nomGrup%></td>
            <td class="taula_usuaris_nom"><%=mimeType%></td>	   
            <td><a href="javascript:editar('<%=id%>');"><%=XMLCollection.getProperty("administracio.tipusFitxers.editar")%></a></td>	   
            <td><a href="javascript:eliminar('<%=id%>');"><%=XMLCollection.getProperty("administracio.tipusFitxers.eliminar")%></a></td>
        </tr>

        <%
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }

                } catch (Exception e) {
                    logger.error(e);
                }
            }
        %>     
    </table>
</div>
<jsp:include page="bottomAdministracio.jsp" />
<% }%>