<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.UtilsCercador" %>
<%@ page pageEncoding="UTF-8" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }

    Logger logger = Logger.getLogger("configuracio.jsp");
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
        String contextWeb = "../";
        /*  if (!Configuracio.contextWebAplicacio.equals("")) {
         contextWeb = "/" + Configuracio.contextWebAplicacio + "/";
         } */
%> 

<%

%>   
<jsp:include page="topAdministracio.jsp?selected=9" />	
<script language="Javascript">
    function eliminar(idConfiguracio) {
        if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.configuracio.eliminar_confirmacio"))%>")) {
            document.configuracio.operacio.value = 'eliminar';
            document.configuracio.idConfiguracio.value = idConfiguracio;
            document.configuracio.submit();
        }
    }
    function editar(idConfiguracio) {
        document.editarConfiguracio.idConfiguracio.value = idConfiguracio;
        document.editarConfiguracio.submit();
    }

</script>

<div id="content">
    <form name="editarConfiguracio" method="POST" action="editarConfiguracio.jsp">
        <input type="hidden" name="idConfiguracio" value=""/>
    </form>  
    <form name="configuracio" method="POST" action="<%=contextWeb%>ManagerConfiguracio">
        <input type="hidden" name="operacio" value="afegir"/>
        <input type="hidden" name="idConfiguracio" value=""/>
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.configuracio.titol")%></legend>
            <div id="linia">
                <label for="novaClau"><%=XMLCollection.getProperty("administracio.configuracio.novaClau")%></label>
                <input type="text" name="novaClau" id="novaClau" value=""/><br/>
                <label for="valor"><%=XMLCollection.getProperty("administracio.configuracio.valor")%></label>	    
                <!-- <input type="text" name="mimeType" id="mimeType" value=""/><br/>-->
                <textarea name="valor" id="valor" value="" cols="40"></textarea><br/>
                <input type="submit" value="Afegeix" name="<%=XMLCollection.getProperty("administracio.configuracio.Afegir")%>"/>
            </div>
        </fieldset>
    </form>
    <br/>

    <table class="taula_usuaris">
        <caption>
            <%=XMLCollection.getProperty("administracio.configuracio.titol_disponibles")%>
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
                sql = "SELECT * from configuracio ORDER by clau ASC";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String clau = rs.getString("clau");
                    String valor = rs.getString("valor");
                    int id = rs.getInt("id");
        %>
        <tr>
            <td class="taula_usuaris_nom"><%=clau%></td>
            <td class="taula_usuaris_nom"><%=valor%></td>	   
            <td><a href="javascript:editar('<%=id%>');"><%=XMLCollection.getProperty("administracio.configuracio.editar")%></a></td>
            <td><a href="javascript:eliminar('<%=id%>');"><%=XMLCollection.getProperty("administracio.configuracio.eliminar")%></a></td>
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