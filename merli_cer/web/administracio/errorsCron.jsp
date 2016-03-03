<%@ page import="org.apache.log4j.Logger, simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.XMLCollection, simpple.xtec.web.util.UtilsCercador" %>
<%@ page pageEncoding="UTF-8" %>


<%
    //String usuari = (String)request.getRemoteUser();
    String usuari = (String) session.getAttribute("user");

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

<jsp:include page="topAdministracio.jsp" />
Error parsejant l'expressió cron
<jsp:include page="bottomAdministracio.jsp" />
<% }%>