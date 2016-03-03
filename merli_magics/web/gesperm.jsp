<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page import="edu.xtec.merli.MagicBean,edu.xtec.merli.utils.Utility, java.util.Iterator,java.util.ArrayList,java.util.Hashtable"%>
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

<script type="text/javascript" src="web/JS/permis.js"></script>
<script type="text/javascript" src="web/JS/permisAJAX.js"></script>

</head>

<body>

<html:form action="permisos.do">
			<html:hidden property="permission"></html:hidden>
			<html:hidden property="idPermission"></html:hidden>
			<html:hidden property="operation"></html:hidden>
			<html:hidden property="description"></html:hidden>
			<html:hidden property="listOperations"></html:hidden>
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
					<a href="usuaris.do" title="cerca">USUARIS</a>				
			</li>
			<%} %>
			<!-- 
			<li title="Modificar permís">
				<img class="operacio" src="web/images/duplicar.png" alt="SET">
				<a href="#" onclick="javascript:modificar('permis');">
					Duplica
				</a>
			</li> -->
			<%if (((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.Operations.MAGICS.PERMSET)){%>
			<li title="Permisos">
					<a href="permisos.do" title="cerca">PERMISOS</a>
			</li>
			<%} %>
		</ul>
	</div>
	<div id="operacions">
		<ul>
			<li class="title">Permís per defecte:</li>
			<li>
				<select name="defaultPerm" onchange="document.getElementById('operacioDefPerm').style.display='block';">
					<option value=""> - </option>
				<%
				MagicBean mb = new MagicBean();
				ArrayList al = mb.executeOperation("permission","list");
				String defp=mb.getDefaultPermission();
				Integer defPerm = new Integer(-1);
				if (defp!=null && !"".equals(defp)){
					defPerm = Integer.valueOf(defp);
				}
				Iterator it = al.iterator();
				Hashtable h;
				String checked="";
				while (it.hasNext()){
					h = (Hashtable)it.next();
					if (defPerm.equals((Integer)h.get("id"))){
						checked="selected=\"selected\"";
					}else{
						checked="";
					}
					%><option <%=checked%> value="<%=h.get("id")%>"><%=Utility.xmlEncode((String) h.get("name"))%></option><%
				}
				%>
				</select>
			</li>
			<li id="operacioDefPerm" style="display:none">
				<input type="button" onclick="defaultPermAJAX();" value="Desa" class="buto">
			</li>
			<li id="resultDefPerm" style="display:none">
				enviant petició...
			</li>
		</ul>
	</div>
</div>
<div id="dreta">
	<div id="header">
		<div id="titleHeader">
			<div id="entorns">
			</div>
			<div class="inferior">
				<div class="esquerra2" id="modes">
					<a href="#" onclick="javascript:nouAJAX();" title="crea nou permís">
						CREA
					</a>				
				<span> | </span>
				<a href="#" onclick="javascript:esborrar('permis');" title="esborra permís">
					ESBORRA
				</a>
				<span> | </span>
					<a href="login.jsp" title="finalitza sessió">
						SURT
					</a>
				</div>
				<div class="dreta2" id="username">
					<a href="usuaris.do" title="Editar usuari"><%
	if (request.getSession() != null && request.getSession().getAttribute("user") != null){
		out.println(((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser());	
	//}else{
		%><!-- jsp:getProperty name="permisos" property="user"/--><%
	}
%></a>
				</div>
			</div>
		</div>
	</div>
	<div id="cos">
		<div id="titleContent">
			Permisos
		</div>
		<div id="content">
				<!-- p class="comentari">Permisos del sistema:</p-->
				<PHTM:ul name="permisos" id="permission" property="list" onclick=""></PHTM:ul>	

				<div class="" id="nouperm">
					<div class="modifperm" id="permnouop"></div>
				</div>				
		</div>
	</div>
</div>
</html:form>
</body>
</html:html>