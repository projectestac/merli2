<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%-- JSTL tag libs --%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ include file="/web/taglibs.jsp"%>

<html:html locale="true">
<head>
<title><bean:message key="application.title" /></title>
<link type="text/css" href="web/css/merli.css" rel="stylesheet">
<link type="text/css" href="web/css/magics.css" rel="stylesheet">
<link rel="shortcut icon" href="web/images/favicon.ico" type="image/x-icon">
<link rel="icon" href="web/images/favicon.ico" type="image/x-icon">

<script type="text/javascript" src="web/JS/merli.js"></script>
<script type="text/javascript" src="web/JS/userAJAX.js"></script>

</head>

<body>
<html:form action="usuaris.do">
			<html:hidden property="username"></html:hidden>
			<html:hidden property="email"></html:hidden>
			<html:hidden property="unitat"></html:hidden>
			<html:hidden property="operation"></html:hidden>
<div class="esquerra">
	<div id="logo">
		<img id="img" src="web/images/logo.png" title="MeRLí"/>
	</div>
	<div id="operacions">
		<ul>
			<li class="title">	
			Administració
			</li>
			<%if (((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.Operations.MAGICS.USERSET)){%>
			<li title="Usuaris">
				<a href="#" title="cerca">USUARIS</a>
			</li>
			<%} %>
			<!--
			<li title="Modificar usuari">
				<img class="operacio" src="web/images/duplicar.png" alt="D">
				<a href="#" onclick="javascript:modificar();">
					Modifica
				</a>
			</li>-->
			<%if (((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.Operations.MAGICS.PERMSET)){%>
			<li title="Permisos">
					<a href="permisos.do" title="cerca">PERMISOS</a>
			</li>
			<%} %>
			<!-- 
			<li title="Permisos usuari">
				<img class="operacio" src="web/images/modificar.png" alt="P">
				<a href="#" onclick="javascript:permisos();">
					Permisos
				</a>
			</li> -->
		</ul>
	</div>
</div>
<div id="dreta" style="width:860px;">
	<div id="header">
		<div id="titleHeader">
			<div id="entorns">
			</div>
			<div class="inferior">
				<div class="esquerra2" id="modes">
					<a href="#" onclick="javascript:nou();" title="crea nou usuari">
						CREA
					</a>
				<span> | </span>
					<a href="#" onclick="javascript:esborrar();" title="esborra usuari">
						ESBORRA
					</a>
				<span> | </span>
					<a href="logout.do" title="finalitza sessió">
						SURT
					</a>
				</div>
				<div class="dreta2" id="username">
					<a href="usuaris.do" title="Editar usuari"><%
	if (request.getSession() != null && request.getSession().getAttribute("user") != null){
		out.println(((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser());	
	//}
	//else{
	%><!-- jsp:getProperty name="permisos" property="user"/--><%
	}
%></a>
				</div>
			</div>
		</div>
	</div>
	<div id="cos">
		<div id="titleContent">
			Usuaris
		</div>
		<div id="content">
				<!--p class="comentari">Usuaris/aries del sistema:</p--><%
		if (request.getParameter("nodeType") != null && request.getParameter("nodeType").compareTo("area")==0){
			%>
				<PHTM:ul name="usuaris" id="permission" property="list" onclick=""></PHTM:ul>	
		<%}else{
		%>
				<PHTM:ul name="usuaris" id="user" property="list" onclick=""></PHTM:ul>	
		<%}%>	
				<p class="" id="nouuser">	</p>				
		</div>
	</div>
</div>
</html:form>
</body>
</html:html>