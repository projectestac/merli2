<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.XMLCollection,java.util.ArrayList, simpple.xtec.web.util.ComentariObject, simpple.xtec.web.util.Inicial" %>
<%@ page pageEncoding="UTF-8" %>

<%
    Logger logger = Logger.getLogger("inicial.jsp");
    session.setAttribute("idioma", "ca");
    String idioma = (String) session.getAttribute("idioma");
    Locale myLocale = new Locale(idioma);
   //String usuari = (String)request.getRemoteUser();
    String usuari = (String) session.getAttribute("user");
    logger.debug("Usuari 1: " + usuari);

    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }

    //if ( (!UtilsCercador.isUserInRole(usuari) ||(usuari == null)) && Configuracio.sso.equals("si")){
    if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null))) {
        /*logger.debug("Redirect SSO");
        response.setHeader("Osso-Paranoid", "true");
        response.sendError(499, "Oracle SSO");*/
        response.sendRedirect("../loginSSO.jsp");

    } else {

%> 

<jsp:include page="topAdministracio.jsp?selected=0" />

<%    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    try {

        conn = UtilsCercador.getConnectionFromPool();
        Inicial inicial = new Inicial(conn);

        String baseScripts = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;
%>   



<script type="text/javascript" src="<%=baseScripts%>/scripts/chart/excanvas.js"></script>
<script type="text/javascript" src="<%=baseScripts%>/scripts/chart/chart.js"></script>
<script type="text/javascript" src="<%=baseScripts%>/scripts/chart/canvaschartpainter.js"></script>
<link rel="stylesheet" type="text/css" href="<%=baseScripts%>/scripts/chart/canvaschart.css" />

<div id="content">
    <p>Benviguts a l'aplicació d'administració del sistema d'indexació i cerca del catàleg de recursos educatius de la xarxa XTEC.</p>

    <table class="taula_admin_cerca" width="500">
        <div id="chart" class="chart" style="width: 550px; height: 200px;"></div>    
        <br/>
        <caption>
            <%=XMLCollection.getProperty("administracio.inicial.ultimesCerques")%>
        </caption>
        <thead>
            <tr>
                <th><%=XMLCollection.getProperty("administracio.inicial.cerca")%></th>
                <th><%=XMLCollection.getProperty("administracio.inicial.data")%></th>
                <th><%=XMLCollection.getProperty("administracio.inicial.hora")%></th>
                <th><%=XMLCollection.getProperty("administracio.inicial.cercador")%></th>	
            </tr>      
        </thead>
        <tbody>     

            <%    stmt = conn.createStatement();
                logger.debug("a1");

                sql = "SELECT * FROM (SELECT cerca, data_cerca, cercador_id, ROW_NUMBER() OVER (ORDER BY data_cerca DESC) R FROM log_cerques) WHERE R <=5";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String cerca = rs.getString("cerca");
                    String dataCerca = rs.getString("data_cerca");
                    String dia = UtilsCercador.getDia(dataCerca);
                    String hora = UtilsCercador.getHora(dataCerca);
                    double cercador_id = rs.getDouble("cercador_id");
            %>
            <tr>
                <td class="esquerra"><%=cerca%></td>
                <td class="centre"><%=dia%></td>
                <td class="centre"><%=hora%></td>
                <% if (cercador_id == 1) { %>
                <td class="centre">edu365</td>
                <%  } else { %>
                <td class="centre">XTEC</td>
                <%  } %>
            </tr>	
            <%
                }
            %>       
        </tbody>
    </table>
    <br/>
    <br/>
    <table class="taula_admin_cerca" width="500">
        <caption>
            <%=XMLCollection.getProperty("administracio.inicial.ultimsComentaris")%>
        </caption>
        <%
            ArrayList lastComments = inicial.getUltimsComentaris();
            int i = 0;
            while (i < lastComments.size()) {
                ComentariObject myComentari = (ComentariObject) lastComments.get(i);
                long id = myComentari.id;
                String comentari = myComentari.comentari;
                String titol = myComentari.titol;
                int puntuacio = myComentari.puntuacio;
                String autor = myComentari.autor;
                String dataEdicio = myComentari.dataEdicio;
                String dia = UtilsCercador.getDia(dataEdicio);
                String hora = UtilsCercador.getHora(dataEdicio);
                String idRecurs = myComentari.idRecurs;

                String fitxaUrl = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/";
                fitxaUrl += "cerca/fitxaRecurs.jsp?idRecurs=" + idRecurs + "#" + id;
        %>         
        <tr>
            <td class="pelat"> 
                <table class="taula_edicio">
                    <tr>
                        <td class="taula_edicio_autor"><%=autor%> el <%=dia%> a les <%=hora%></td>
                        <td class="taula_edicio_edicio"><a href="<%=fitxaUrl%>" target="_blank"><%=XMLCollection.getProperty("administracio.inicial.veu")%></a></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="taula_comentaris_text"><span class="taula_comentaris_text"><%=comentari%></span></td>
        </tr>

        <%
                    i++;
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
                } catch (Exception e) {
                    logger.error(e);
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }

                } catch (Exception e) {
                    logger.error(e);
                }
            }

            logger.debug("b2");
            String llegenda = UtilsCercador.getLlistaHoresChart();
            logger.debug("b3");
            String valors = UtilsCercador.getLlistaValorsChart();
            logger.debug("b4");
            int maxCerques = UtilsCercador.getMaxCerquesChart(valors);
            logger.debug("b5");
        %>         
    </table>
</div>

<script type="text/javascript">
    function draw() {
        var c = new Chart(document.getElementById('chart'));
        c.setDefaultType(CHART_AREA | CHART_STACKED);
            c.setGridDensity(24, 5);
            c.setVerticalRange(0, <%=maxCerques%>);
            c.setHorizontalLabels([<%=llegenda%>]);
        c.add('Número de cerques', '#4040FF', [<%=valors%>]);
        c.draw();
    }

    draw();

</script>

<jsp:include page="bottomAdministracio.jsp" />
<% }%>