<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.XMLCollection" %>
<%@ page pageEncoding="UTF-8" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Logger logger = Logger.getLogger("indexacio.jsp");
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


<%    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String programacioTemporal = "";
    try {

        conn = UtilsCercador.getConnectionFromPool();

        stmt = conn.createStatement();
        sql = "SELECT * FROM config_indexacio";
        rs = stmt.executeQuery(sql);
        rs.next();
        programacioTemporal = rs.getString("ordre_cron");


%>

<jsp:include page="topAdministracio.jsp?selected=1" />

<script language="Javascript">
    function indexarAra() {
        document.indexacio.operacio.value = 'indexar';
        document.indexacio.submit();
    }
    function openLog() {
        window.open("logIndexacio.jsp", "_blank", "width=500,height=450,resizable=yes, scrollbars=yes");
    }
</script>     
<div id="content">

    <%    stmt = conn.createStatement();
        sql = "SELECT max(data_log) FROM log_indexacio where length(data_log)=19";
        rs = stmt.executeQuery(sql);
        rs.next();
        String dataLog = rs.getString(1);
        String dia = UtilsCercador.getDia(dataLog);
        String hora = UtilsCercador.getHora(dataLog);

        stmt = conn.createStatement();
        sql = "SELECT count(*) FROM log_indexacio WHERE found=1";
        rs = stmt.executeQuery(sql);
        rs.next();
        int numRecursos = rs.getInt(1);

        sql = "SELECT count(*) FROM log_indexacio WHERE isNew=1";
        rs = stmt.executeQuery(sql);
        rs.next();
        int numRecursosNous = rs.getInt(1);

    %>        
    <p class="caixa">
        <%=XMLCollection.getProperty("administracio.indexacio.ultimaIndexacio")%><br/>
        <b><%=dia%></b> a les <b><%=hora%></b>.<br/>
        <%=XMLCollection.getProperty("administracio.indexacio.nombreRecursos")%> <b><%=numRecursos%></b><br/>
        <%=XMLCollection.getProperty("administracio.indexacio.nombreRecursosNous")%> <b><%=numRecursosNous%></b><br/>
        <a href="javascript:openLog();"><%=XMLCollection.getProperty("administracio.indexacio.logIndexacio")%></a>
    </p>   
    <form name="indexacio" action="/<%=Configuracio.contextWebAplicacio%>/ServletIndexacio" method="post">  
        <input type="hidden" name="operacio" value="modificar"/>   
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.indexacio.programacioTemporal")%></legend>
            <div id="linia">
                <label for="cronline"><%=XMLCollection.getProperty("administracio.indexacio.programacio")%>*</label>
                <input type="text" name="programacioTemporal" id="cronline" value="<%=programacioTemporal%>" />
                <input type="submit" value="<%=XMLCollection.getProperty("administracio.indexacio.modificar")%>" />
            </div>
            <p class="anotacio">* <%=XMLCollection.getProperty("administracio.indexacio.formatCron")%>: <br/>
                [segon] [minut] [hora] [dia del mes] [mes de l'any] [dia setmana (MON=dilluns, TUE=dimarts)] <br/>
                Exemple: 0 15 20 * * ? </p>
        </fieldset>
    </form>
    <form name="indexacio" action="/<%=Configuracio.contextWebAplicacio%>/ServletIndexacio" method="post">  
        <input type="hidden" name="operacio" value="indexar"/>   
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.indexacio.indexacioInmediata")%></legend>
            <input type="submit" value="<%=XMLCollection.getProperty("administracio.indexacio.indexar")%>" />
        </fieldset>
    </form>
    <%

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        }


    %>   


</div>
<%=Configuracio.versionControl%>
<jsp:include page="bottomAdministracio.jsp" />
<% }%>