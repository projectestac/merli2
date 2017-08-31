<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, java.util.StringTokenizer, simpple.xtec.web.util.ComentariObject" %>
<%@ page pageEncoding="UTF-8" %>

<%
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Logger logger = Logger.getLogger("comentaris.jsp");
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

        String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;

%> 

<head>  
    <link rel="alternate" type="application/rss+xml" title="Feed comentaris" href="<%=urlLocal%>/rss/comentaris.rss" /> 

    <script language="JavaScript" type="text/javascript">

        function checkCR(evt) {

            var evt = (evt) ? evt : ((event) ? event : null);

            var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);

            if ((evt.keyCode == 13) && (node.type == "text")) {
                doSubmit();
                return false;
            }

        }

        document.onkeypress = checkCR;

    </script> 
</head>

<%

    String recursCerca = (String) request.getAttribute("comentaris.recursCerca");
    logger.debug("Recurs cerca: " + recursCerca);
    if (recursCerca == null) {
        recursCerca = "";
    }

    String usuariCerca = (String) request.getAttribute("comentaris.usuariCerca");
    if (usuariCerca == null) {
        usuariCerca = "";
    }

    String textCerca = (String) request.getAttribute("comentaris.textCerca");
    if (textCerca == null) {
        textCerca = "";
    }

    String titolCerca = (String) request.getAttribute("comentaris.titolCerca");
    if (titolCerca == null) {
        titolCerca = "";
    }

    String dataIniciCerca = (String) request.getAttribute("comentaris.dataIniciCerca");
    if (dataIniciCerca == null) {
        dataIniciCerca = "";
    }

    String dataFinalCerca = (String) request.getAttribute("comentaris.dataFinalCerca");
    if (dataFinalCerca == null) {
        dataFinalCerca = "";
    }

    String suspes = (String) request.getAttribute("comentaris.suspes");
    if (suspes == null) {
        suspes = "";
    }

    String tipusCerca = (String) request.getAttribute("comentaris.tipusCerca");
    String operacio = (String) request.getAttribute("comentaris.operacio");
    /*   
     Integer numResultatsInt = (Integer)request.getAttribute("comentaris.resultats.numResultats");
     int numResultats = 0;
     if (numResultatsInt != null) {
     numResultats = numResultatsInt.intValue();
     }
     */
    int numResultats = 3;
    logger.debug("num resultats: " + numResultats);
    String nivellString = (String) request.getAttribute("comentaris.resultats.nivell");
    int nivell = 0;
    if (nivellString != null) {
        nivell = new Integer(nivellString).intValue();
    }

    ArrayList resultats = (ArrayList) request.getAttribute("comentaris.resultats");
%>  
<jsp:include page="topAdministracio.jsp?selected=5" />
<!-- <link rel="stylesheet" type="text/css" media="all" href="<%=urlLocal%>/css/theme.css" title="Aqua" /> -->
<!-- import the calendar script -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-main.js"></script>
<!-- import the calendar script -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar.js"></script>
<!-- import the language module -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-ca.js"></script>
<!-- import the setup module -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="<%=urlLocal%>/scripts/date-validation.js"></script>
<style type="text/css">@import url(<%=urlLocal%>/css/calendar-xtec.css);</style>

<script language="Javascript">
        function eliminarComentari(idComentari) {
            if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.comentaris.eliminar_confirmacio"))%>")) {
                document.search.operacio.value = 'eliminar';
                document.search.idComentari.value = idComentari;
                document.search.submit();
            }
        }
        function suspendreComentari(idComentari, idRecurs) {
            if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.comentaris.suspendre_confirmacio"))%>")) {
                document.search.operacio.value = 'suspendre';
                document.search.idComentari.value = idComentari;
                document.search.idRecurs.value = idRecurs;
                document.search.submit();
            }
        }

        function publicarComentari(idComentari, idRecurs) {
            if (confirm("<%=UtilsCercador.fromAcute(XMLCollection.getProperty("administracio.comentaris.publicar_confirmacio"))%>")) {
                document.search.operacio.value = 'publicar';
                document.search.idComentari.value = idComentari;
                document.search.idRecurs.value = idRecurs;
                document.search.submit();
            }
        }


        function goToPage(nivell) {
            document.search.nivell.value = nivell;
            document.search.submit();
        }

        function doSubmit() {
            if ((document.search.dataIniciCerca.value != "") && (isDate(document.search.dataIniciCerca.value) == false)) {
                alert('<%=XMLCollection.getProperty("administracio.comentaris.errorData")%>');
                return false;
            }
            if ((document.search.dataFinalCerca.value != "") && (isDate(document.search.dataFinalCerca.value) == false)) {
                alert('<%=XMLCollection.getProperty("administracio.comentaris.errorData")%>');
                return false;
            }
            document.search.submit();
        }

</script>
<!-- helper script that uses the calendar -->

<div id="content">

    <form name="search" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/ServletComentaris"> 
        <input type="hidden" name="operacio" value="cercar"/>
        <input type="hidden" name="idComentari" value=""/>
        <input type="hidden" name="idRecurs" value=""/>       
        <input type="hidden" name="nivell" value="0"/>

        <fieldset>
            <legend><%=XMLCollection.getProperty("administracio.comentaris.filtratComentaris")%></legend>         

            <div id="linia">
                <label class="filtrat" for="usuariCerca"><%=XMLCollection.getProperty("administracio.comentaris.autor")%></label>
                <input type="text" name="usuariCerca" value="<%=usuariCerca%>" />
            </div>
            <div id="linia">
                <label class="filtrat" for="titolCerca"><%=XMLCollection.getProperty("administracio.comentaris.titol")%></label>
                <input type="text" name="titolCerca" value="<%=titolCerca%>" size="50" maxlength="50" />
            </div>
            <div id="linia">
                <label class="filtrat" for="textCerca"><%=XMLCollection.getProperty("administracio.comentaris.text")%></label>
                <input type="text" name="textCerca" value="<%=textCerca%>" size="50" maxlength="50" />
            </div>
            <div id="linia">
                <label class="filtrat" for="dataIniciCerca"><%=XMLCollection.getProperty("administracio.comentaris.dataInici")%></label>
                <input type="text" name="dataIniciCerca" id="dataIniciCerca" size="10" value="<%=dataIniciCerca%>">
                <button id="cerca_inici">...</button>
            </div>
            <div id="linia">
                <label class="filtrat" for="dataFinalCerca"><%=XMLCollection.getProperty("administracio.comentaris.dataFinal")%></label>
                <input type="text" name="dataFinalCerca" id="dataFinalCerca" size="10" value="<%=dataFinalCerca%>">
                <button id="cerca_fi">...</button>
            </div>
            <div id="linia">
                <label class="filtrat" for="suspes"><%=XMLCollection.getProperty("administracio.comentaris.suspes")%></label>
                <% if (suspes.equals("1")) { %>
                <input type="checkbox" name="suspes" id="suspes" size="10" checked="true">
                <%   } else { %>
                <input type="checkbox" name="suspes" id="suspes" size="10">		
                <%   }%>
            </div>

            <div id="linia">
                <input type="button" value="<%=XMLCollection.getProperty("administracio.comentaris.filtra")%>" onClick="javascript:doSubmit()"/>
            </div>

        </fieldset>
    </form>
    <br/>


    <%
        if (resultats != null) {
            Integer numResultatsInt = (Integer) resultats.get(0);
            numResultats = numResultatsInt.intValue();
    %>

    <table class="taula_comentaris" width="500">
        <caption>
            <%=XMLCollection.getProperty("administracio.comentaris.comentaris")%> (<%=numResultats%>)
        </caption>
        <%

            int i = 1;
            while (i < resultats.size()) {
                //  if ( (i >= (nivell * Configuracio.numResultatsPagina)) && (i < ((nivell * Configuracio.numResultatsPagina) + Configuracio.numResultatsPagina)) ){
                ComentariObject comentariLog = (ComentariObject) resultats.get(i);
                String dataEdicio = comentariLog.dataEdicio;
                String dia = UtilsCercador.getDia(dataEdicio);
                String hora = UtilsCercador.getHora(dataEdicio);
                String titol = comentariLog.titol;
                if ((titol == null) || titol.equals("null")) {
                    titol = "Sense títol";
                }
                String fitxaUrl = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/";
                fitxaUrl += "cerca/fitxaRecurs.jsp?idRecurs=" + comentariLog.idRecurs + "#" + comentariLog.id;

        %>
        <tr>
            <td class="pelat"> 
                <table class="taula_edicio">
                    <tr>
                        <td class="taula_edicio_autor"><%=comentariLog.nomUsuari%> el <%=dia%> a les <%=hora%></td>
                        <td class="taula_edicio_edicio"><a href="<%=fitxaUrl%>" target="_blank">veu</a> | 
                            <% if (comentariLog.suspens == 0) {%>
                            <a href="javascript:suspendreComentari('<%=comentariLog.id%>', '<%=comentariLog.idRecurs%>')">suspen</a>
                            <% } else {%>
                            <a href="javascript:publicarComentari('<%=comentariLog.id%>', '<%=comentariLog.idRecurs%>')">publica</a>
                            <% }%>  
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>	
            <td class="taula_comentaris_text"><%=titol%></td>
        </tr>


        <%
                //   }
                i++;
            }
        %>       
    </table>
    <p class="paginacio">
        <%
            int numPagines = numResultats / Configuracio.numResultatsPagina;
            if ((numResultats % Configuracio.numResultatsPagina) != 0) {
                numPagines++;
            }
            int j = 0;
            int top = 10;
            if (numPagines > 1) {
                j = nivell - 4;
                if (j <= 0) {
                    j = 0;
                } else {
        %>	  
        ...
        <%
            }
            top = j + 10;
            if (top > numPagines) {
                top = numPagines;
            }
            while (j < top) {
                if (j != nivell) {
        %>
        <a href="javascript:goToPage('<%=j%>')"><%=j + 1%></a>
        <%
        } else {
        %>    
        <%=j + 1%>
        <%
                }
                j++;
            }
            if (j != numPagines) {%>
        ...
        <% }
                }
            }

        %>
    </p>
    <div id="rss"><a href="<%=urlLocal%>/rss/comentaris.rss"><%=XMLCollection.getProperty("administracio.comentaris.subscriureComentaris")%></a></div>
</div>
<!-- JS del calendari {{{-->
<script type="text/javascript">
    Calendar.setup(
            {
                inputField: "dataIniciCerca", // ID of the input field
                ifFormat: "%d/%m/%Y", // the date format
                button: "cerca_inici", // ID of the button
                firstDay: 1
            }
    );
    Calendar.setup(
            {
                inputField: "dataFinalCerca", // ID of the input field
                ifFormat: "%d/%m/%Y", // the date format
                button: "cerca_fi", // ID of the button
                firstDay: 1
            }
    );
</script>
<!-- }}} -->



<jsp:include page="bottomAdministracio.jsp" />
<% }%>