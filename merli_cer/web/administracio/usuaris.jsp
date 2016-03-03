<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.UtilsCercador" %>
<%@ page pageEncoding="UTF-8" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Logger logger = Logger.getLogger("usuaris.jsp");
    String idioma = (String) session.getAttribute("idioma");

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

        if (idioma == null) {
            idioma = Configuracio.idioma;
        }
        Locale myLocale = new Locale(idioma);

%> 

<%
%>   
<jsp:include page="topAdministracio.jsp?selected=7" />	
<script language="Javascript">
    function eliminar(idUsuari) {
        if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.usuaris.eliminar_confirmacio"))%>")) {
            document.usuari.operacio.value = 'eliminar';
            document.usuari.idUsuari.value = idUsuari;
            document.usuari.submit();
        }
    }
</script>

<div id="content">

    <form name="usuari" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/ManagerUsuaris">
        <input type="hidden" name="operacio" value="afegir"/>
        <input type="hidden" name="idUsuari" value=""/>
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.usuaris.usuaris_administradors_titol")%></legend>
            <div id="linia">
                <label for="usuari"><%=XMLCollection.getProperty("administracio.usuaris.usuari")%></label>
                <input type="text" name="nouUsuari" value=""/>
                <input type="submit" value="Afegeix" name="<%=XMLCollection.getProperty("administracio.usuaris.Afegir")%>"/>
            </div>
        </fieldset>     
    </form>
    <br/>

    <table class="taula_usuaris">
        <caption>
            <%=XMLCollection.getProperty("administracio.usuaris.usuaris_administradors_llista")%>
        </caption>   
        <%

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            try {

                if (Configuracio.isVoid()) {
                    Configuracio.carregaConfiguracio();
                }

                conn = UtilsCercador.getConnectionFromPool();
                stmt = conn.createStatement();
                sql = "SELECT * from admin_users ORDER by xtec_username ASC";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String username = rs.getString("xtec_username");
                    int id = rs.getInt("id");
        %>
        <tr>
            <td class="taula_usuaris_nom"><%=username%></td>
            <td><a href="javascript:eliminar('<%=id%>');"><%=XMLCollection.getProperty("administracio.usuaris.eliminar")%></a></td>
        </tr>

        <%
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