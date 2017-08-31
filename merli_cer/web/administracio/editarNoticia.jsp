<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, simpple.xtec.web.util.UtilsCercador" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Logger logger = Logger.getLogger("editarNoticia.jsp");
    //String usuari = (String)request.getRemoteUser();
    String usuari = (String) session.getAttribute("user");

    logger.debug("Usuari 1: " + usuari);

    //if ( (!UtilsCercador.isUserInRole(usuari) ||(usuari == null)) && Configuracio.sso.equals("si")){
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


<%
%>   
<jsp:include page="topAdministracio.jsp?selected=7" />

<script language="Javascript">
    function editar(idNoticia) {
        document.editar.idNoticia.value = idNoticia;
        document.editar.submit();
    }
    function eliminar(idNoticia) {
        if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.editarNoticia.eliminar_confirmacio"))%>")) {
            document.noticia.operacio.value = 'eliminar';
            document.noticia.idNoticia.value = idNoticia;
            document.noticia.submit();
        }
    }
    function publicar(idNoticia) {
        document.noticia.operacio.value = 'publicar';
        document.noticia.idNoticia.value = idNoticia;
        document.noticia.submit();
    }
    function despublicar(idNoticia) {
        document.noticia.operacio.value = 'despublicar';
        document.noticia.idNoticia.value = idNoticia;
        document.noticia.submit();
    }
    function novaNoticia() {
        document.location.href = 'noticies.jsp';
    }



</script>
<form name="editar" methor="POST" action="editarNoticia.jsp"/>
<input type="hidden" name="idNoticia" value=""/>
</form>
<%

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "";
    String idNoticiaParam = "";
    try {

        idNoticiaParam = request.getParameter("idNoticia");

        if (Configuracio.isVoid()) {
            Configuracio.carregaConfiguracio();
        }

        conn = UtilsCercador.getConnectionFromPool();

        stmt = conn.createStatement();
        sql = "SELECT * from noticies WHERE id=" + idNoticiaParam;
        rs = stmt.executeQuery(sql);
        rs.next();
        String titolNoticia = rs.getString("titol");
        String cosNoticia = rs.getString("cos");
        // cosNoticia = cosNoticia.replaceAll("'", "\\\\\'");
%>     
<script type="text/javascript" src="../scripts/editorfck/fckeditor.js"></script>
<div id="content">
    <form name="noticia" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/ManagerNoticies">
        <input type="hidden" name="operacio" value="editar"/>
        <input type="hidden" name="idNoticia" value="<%=idNoticiaParam%>"/>
        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.editarNoticia.editarNoticia")%></legend>
            <div id="linia">
                <label class="filtrat" for="titolNoticia"><%=XMLCollection.getProperty("administracio.editarNoticia.titol")%></label>
                <input type="text" name="titolNoticia" size="53" value="<%=titolNoticia%>" />
            </div>
            <div id="content_hidden" style="visibility:hidden;display:none">
                <%=cosNoticia%>
            </div>
            <div id="linia">
                <script type="text/javascript">

<!--
// Automatically calculates the editor base path based on the _samples directory.
// This is usefull only for these samples. A real application should use something like this:
// oFCKeditor.BasePath = '/fckeditor/' ;	// '/fckeditor/' is the default value.
    var sBasePath = document.location.pathname.substring(0, document.location.pathname.lastIndexOf('_samples'));

    var oFCKeditor = new FCKeditor('FCKeditor1');
    oFCKeditor.BasePath = '/<%=Configuracio.contextWebAplicacio%>/';
    oFCKeditor.Height = 300;
    oFCKeditor.Value = document.getElementById("content_hidden").innerHTML;
    oFCKeditor.ToolbarSet = 'Basic';
    oFCKeditor.Create();
//-->
                </script>	
            </div>			
            <!-- <div id="linia">
                    <label class="filtrat" for="cosNoticia">Text</label>
                    <textarea name="cosNoticia" rows="5" cols="40" value=""><%=cosNoticia%></textarea>
            </div> -->

            <div id="linia">
                <input type="submit" value="<%=XMLCollection.getProperty("administracio.editarNoticia.desa")%>" />
                <input type="button" name="Afegir nova notcia" value="<%=XMLCollection.getProperty("administracio.editarNoticia.afegir")%>" class="tableButton" onClick="javascript:novaNoticia()">
            </div>
        </fieldset>		
    </form>
    <br/>		
    <table class="taula_comentaris" width="500">
        <caption>
            <%=XMLCollection.getProperty("administracio.editarNoticia.noticies")%>
        </caption>


        <%
            stmt = conn.createStatement();
            sql = "SELECT * from noticies ORDER by data_edicio DESC";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int idNoticia = rs.getInt("id");
                String titol = rs.getString("titol");
                int publicar = rs.getInt("publicat");
                String cos = rs.getString("cos");
                if (!idNoticiaParam.equals("" + idNoticia)) {
        %>
        <tr>
            <td class="pelat"> 
                <table class="taula_edicio">
                    <tr>
                        <td class="taula_noticies_titol"><%=titol%></td>
                        <td class="taula_noticies_edicio">
                            <% if (publicar == 1) {%>
                            <a href="javascript:despublicar('<%=idNoticia%>');"><%=XMLCollection.getProperty("administracio.editarNoticia.no_publica")%></a> | 
                            <%    } else {%>
                            <a href="javascript:publicar('<%=idNoticia%>');"><%=XMLCollection.getProperty("administracio.editarNoticia.publica")%></a> | 
                            <%    }%>                      
                            <a href="javascript:editar('<%=idNoticia%>');"><%=XMLCollection.getProperty("administracio.editarNoticia.edita")%></a> | 
                            <a href="javascript:eliminar('<%=idNoticia%>');"><%=XMLCollection.getProperty("administracio.editarNoticia.elimina")%></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="taula_comentaris_text"><%=cos%></td>
        </tr>
        <%         }
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