<%@ page import="simpple.xtec.web.util.Configuracio, org.apache.log4j.Logger" %>

<%     
    Logger logger = Logger.getLogger("logout.jsp");
    session.setAttribute("nomUsuari", null);
    session.setAttribute("user", null);
    session.setAttribute("usuariNomComplet", null);
    String urlInicial = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/directoriInicial.jsp";
    logger.debug("Url inicial: " + urlInicial);
    response.setHeader("Osso-Return-Url", urlInicial );
    // Send Dynamic Directive for logout
    //response.sendError(470, "Oracle SSO");
     response.sendRedirect("../");
%>