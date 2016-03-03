<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, simpple.xtec.web.util.XMLCollection" %>
<%@ page pageEncoding="UTF-8" %>

<%
  session.setAttribute("idioma", "ca");
  Locale myLocale = new Locale("ca");
  //String usuari = request.getRemoteUser();
  String usuari = (String)session.getAttribute("user");
  if (usuari != null) {
  
	//response.sendError(470, "Oracle SSO");	 
       response.sendRedirect("../loginSSO.jsp");
    }
%>

<html>
<head>
<title> Administracio Sistema de Cerca XTEC </title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
</head>
<body>
<b>XTEC no registrat</b><br/>
<a href="./cerca/cercaSimple.jsp?userGeneric=XTEC">Usuari XTEC</a><br/><br/>
<b>XTEC registrat</b><br/>
<a href="./cerca/cercaCompleta.jsp?userGeneric=No">Login</a><br/><br/>
<b>Edu365</b><br/>
<a href="./cerca/cercaAnonima.jsp">Edu365</a><br/><br/>
</body>
</html>