<%@page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--<?xml version="1.0" encoding="ISO-8859-1" ?> -->
<%-- JSTL tag libs --%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ page import="edu.xtec.merli.MerliBean, edu.xtec.merli.segur.User,edu.xtec.merli.segur.operations.MerliOperations,edu.xtec.merli.gestorrec.GestorForm"%>
<%@ include file="/web/taglibs.jsp"%>
<%org.apache.struts.util.MessageResources mresources = org.apache.struts.util.MessageResources.getMessageResources("ApplicationResources");%>
<html:html locale="true">

<head>
<title><bean:message key="application.window.title"/></title>
<link type="text/css" href="web/css/merli.css" rel="stylesheet">
<link type="text/css" href="web/css/gestor.css" rel="stylesheet">
<link rel="SHORTCUT ICON" href="web/images/favicon.ico" />

<script type="text/javascript">
var titpropis="<bean:message key='merli.title.propis'/>";
var titperdefecte="<bean:message key='merli.title.defecte'/>";
var tittots2="<bean:message key='merli.title.tots2'/>";
var titpendents="<bean:message key='merli.title.pendents'/>";
var titrealitzats="<bean:message key='merli.title.validar'/>";
var titvalidar="<bean:message key='merli.title.validar'/>";
var titenproces="<bean:message key='merli.title.enproces'/>";
var tittraduct="<bean:message key='merli.title.traduct.ca'/>";
var tittraductes="<bean:message key='merli.title.traduct.es'/>";
var tittraducten="<bean:message key='merli.title.traduct.en'/>";
var tittraductoc="<bean:message key='merli.title.traduct.oc'/>";
var tittots="<bean:message key='merli.title.tots'/>";
//var titperenviar="<bean:message key='merli.title.perenviar'/>";
var titnoacceptat="<bean:message key='merli.title.noacceptat'/>";
var titretornades="<bean:message key='merli.title.retornades'/>";
var titdenegades="<bean:message key='merli.title.denegades'/>";
var titcercador="<bean:message key='merli.title.cercador'/>";
var titagregapend="<bean:message key='merli.title.agrega.pend'/>";
var titagregafet="<bean:message key='merli.title.agrega.fet'/>";
var ready = false;
var datasel="ini";
function start(){ 
try{
	init();
}catch(err){
	setTimeout("start()",200);
}
}

function amagarElement(idElem){
	var elem=document.getElementById(idElem);
	if (elem)
		elem.style.display = "none";
}

</script>
<script	type="text/javascript" src="web/JS/etiq.js"></script> 
<script	type="text/javascript" src="web/JS/calendari.js"></script> 
<script type="text/javascript" src="web/JS/recAJAX.js"></script>
<script type="text/javascript" src="web/JS/recurs.js"></script>
<script type="text/javascript" src="web/JS/hintTextBoxes.js"></script>
<script type="text/javascript" src="web/JS/jquery-1.4.2.min.js"></script> 
</head>

<%
	String estatSessio = (String) request.getSession().getAttribute("estat");
	String desplSessio = (String) request.getSession().getAttribute("despl");
	String idSessio = (String) request.getSession().getAttribute("id");
	String cercaSessio = (String) request.getSession().getAttribute("cerca");
	String fisicSessio = (String) request.getSession().getAttribute("fisic");
	String data_iSessio = (String) request.getSession().getAttribute("data_i");
	String data_fSessio = (String) request.getSession().getAttribute("data_f");
	String id_unitatSessio = (String) request.getSession().getAttribute("id_unitat");
	String id_catalogadorSessio = (String) request.getSession().getAttribute("id_catalogador");
	String descripcioSessio = (String) request.getSession().getAttribute("descripcioC");
	String ordenacioSessio = (String) request.getSession().getAttribute("ord");
	String valueSessio = (String) request.getSession().getAttribute("value");
	
	boolean cercaAvancadaSessio = false;
	if ((fisicSessio != null && !"0".equals(fisicSessio)) || data_iSessio != null || data_fSessio != null || id_unitatSessio != null || id_catalogadorSessio != null || descripcioSessio != null) {
		cercaAvancadaSessio = true;
	}			
%>

<body onload="javascript:start();initHintTextboxes();<%if(cercaAvancadaSessio) {%> loadCercaAvancada(document.getElementById('cercaAvancada')); <%}%>">

<jsp:useBean id="merlibean" scope="request" class="edu.xtec.merli.MerliBean"/>
<jsp:useBean id="gesrecurs" scope="request" class="edu.xtec.merli.gestorrec.GestorForm"/>

<jsp:include page="cap_generalitat.jsp"></jsp:include>

<html:form action="gesrecurs.do">
	<html:hidden property="idRecurs"></html:hidden>
	<html:hidden property="llistat"></html:hidden>
	<html:hidden property="operation"></html:hidden>		
	<jsp:include page="capsa_logo.jsp"/>

	<input type="hidden" name="valueSession" value="<%=(valueSessio!=null)?valueSessio:"0"%>"/>

	<div id="menu_capcalera_cercador" class="banner">
		<div id="barra_buscador">
			<table valign="center" style="width: 100%">
				<tr>
					<td style="padding: 6px 6px 0px;"><bean:message key='merli.cercador.cercadorFitxes'/></td>
					<td style="padding: 6px 6px 0px;"><bean:message key='merli.cercador.fitxes'/></td>
					<td style="padding: 6px 6px 0px;"><bean:message key='merli.cercador.ordenatPer'/></td>
					<td></td>
				</tr>
				<tr>
					<td style="padding:5px; width: 140px;">
						<%User usuari = (edu.xtec.merli.segur.User)request.getSession().getAttribute("user");%>
						<% 
							if (estatSessio==null){
								//seleccio per defecte del desplegable, ha de coincidir amb la jerarquia de la funcio getLlistatRecursosInicial de MerliBean
								if(usuari.hasPermission(MerliOperations.RECSET) || usuari.hasPermission(MerliOperations.RECADD)) estatSessio=String.valueOf(MerliBean.LLISTA_M_EN_PROCES);
								else if(usuari.hasPermissionPublicarTots())estatSessio=String.valueOf(MerliBean.LLISTA_M_REALITZATS);
								else if(usuari.hasPermission(MerliOperations.TRADUCT))estatSessio=String.valueOf(MerliBean.LLISTA_M_PUBLICATS);
								else if(usuari.hasPermission(MerliOperations.PENDEDIT))estatSessio=String.valueOf(MerliBean.LLISTA_M_PENDENTS);
								else estatSessio=String.valueOf(MerliBean.LLISTA_M_PUBLICATS);
							}
						%>
						<select name="estat" value="<bean:message key='merli.cercador.estat'/>" id="selestat" value="Estat" class="hintTextbox" > <!-- onchange="canviarTitol(this);">  -->
							<!--<option value="<%=MerliBean.LLISTA_M_PROPIS%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_PROPIS)))?"selected":""%> title="<bean:message key='help.merli.propis'/>">
									<bean:message key='merli.list.propis'/>
							</option>--> 
							<option value="<%=MerliBean.LLISTA_M_TOTS2%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_TOTS2)))?"selected":""%> title="<bean:message key='help.merli.tots'/>">
							</option>
							<% if (usuari.hasPermission(MerliOperations.RECSET) || usuari.hasPermission(MerliOperations.RECADD)) {%>
							<% if (usuari.hasPermission(MerliOperations.PENDEDIT) || usuari.hasPermission(MerliOperations.PENDVIEW)) {%>
							<option value="<%=MerliBean.LLISTA_M_PENDENTS%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_PENDENTS)))?"selected":""%> title="<bean:message key='help.merli.pendents'/>">
									<bean:message key='merli.list.pendents'/>
							</option>
							<% } %>							
							<option value="<%=MerliBean.LLISTA_M_EN_PROCES%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_EN_PROCES)))?"selected":""%> title="<bean:message key='help.merli.enproces'/>">
									<bean:message key='merli.list.enproces'/>
							</option>
							<% } 
							if (usuari.hasPermissionPublicarTots() || ((usuari.hasPermission(MerliOperations.RECSET) || usuari.hasPermission(MerliOperations.RECADD)) && !usuari.isAutoadmin())) {%>
							<option value="<%=MerliBean.LLISTA_M_REALITZATS%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_REALITZATS)))?"selected":""%> title="<bean:message key='help.merli.realitzats'/>">
									<bean:message key='merli.list.realitzats'/>
							</option>
							<% } 
							if (usuari.hasPermission(MerliOperations.RECSET) || usuari.hasPermission(MerliOperations.RECADD)) {%>						
							<option value="<%=MerliBean.LLISTA_M_RETORNADES%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_RETORNADES)))?"selected":""%> title="<bean:message key='help.merli.retornades'/>">
									<bean:message key='merli.list.retornades'/>
							</option>
							<option value="<%=MerliBean.LLISTA_M_DENEGADES%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_DENEGADES)))?"selected":""%> title="<bean:message key='help.merli.denegades'/>">
									<bean:message key='merli.list.denegades'/>
							</option>
							<% }%>
							<option value="<%=MerliBean.LLISTA_M_PUBLICATS%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_PUBLICATS)))?"selected":""%> title="<bean:message key='help.merli.publicats'/>">
									<bean:message key='merli.list.publicats'/>
							</option>
							<% if (usuari.hasPermission(MerliOperations.TRADUCT)) {%>
							<optgroup label="<bean:message key='merli.list.traduct'/>">
								<option value="<%=MerliBean.LLISTA_M_TRADUCT_CA%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_TRADUCT_CA)))?"selected":""%> title="<bean:message key='help.merli.traduct.ca'/>">
										<bean:message key='merli.list.traduct.ca'/>
								</option>
								<option value="<%=MerliBean.LLISTA_M_TRADUCT_ES%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_TRADUCT_ES)))?"selected":""%> title="<bean:message key='help.merli.traduct.es'/>">
										<bean:message key='merli.list.traduct.es'/>
								</option>
								<option value="<%=MerliBean.LLISTA_M_TRADUCT_EN%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_TRADUCT_EN)))?"selected":""%> title="<bean:message key='help.merli.traduct.en'/>">
										<bean:message key='merli.list.traduct.en'/>
								</option>
								<option value="<%=MerliBean.LLISTA_M_TRADUCT_OC%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_TRADUCT_OC)))?"selected":""%> title="<bean:message key='help.merli.traduct.oc'/>">
										<bean:message key='merli.list.traduct.oc'/>
								</option>
							</optgroup>
							<% } %>
							<% if (usuari.hasPermission(MerliOperations.AGREGA)) {%>
							<optgroup label="<bean:message key='merli.list.agrega'/>">
								<option value="<%=MerliBean.LLISTA_M_AGREGA_PEND%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_AGREGA_PEND)))?"selected":""%> title="<bean:message key='help.merli.agrega.pend'/>">
										<bean:message key='merli.list.agrega.pend'/>
								</option>
								<option value="<%=MerliBean.LLISTA_M_AGREGA_FET%>" <%=(estatSessio.equals(String.valueOf(MerliBean.LLISTA_M_AGREGA_FET)))?"selected":""%> title="<bean:message key='help.merli.agrega.fet'/>">
										<bean:message key='merli.list.agrega.fet'/>
								</option>
							</optgroup>
							<% } %>
						</select>
					</td>
					<% if(usuari.getUnitat()!=null && usuari.hasPermission(MerliOperations.RECFISIC))
					{
						String blanc="";
						String propis="";
						String uni="";
						//seleccio per defecte del desplegable, ha de coincidir amb la jerarquia de la funcio getLlistatRecursosInicial de MerliBean
						if(desplSessio!=null && !desplSessio.equals(""))
						{
							if(desplSessio.equals("0")) blanc="selected";
							if(desplSessio.equals("1")) propis="selected";
							if(desplSessio.equals("2")) uni="selected";							
						}
						else
						{
							if(usuari.hasPermission(MerliOperations.RECADD) || usuari.hasPermission(MerliOperations.RECSET)) uni="selected";
							else blanc="selected";
						}
						%>
					<td style="border-right: 2px groove #D2D2D2; #FFFFFF; padding: 0px 5px 0px 10px; width: 507px;">
						<select name="despl" type="select"  value="Personals">
							<option value="0" title="" <%=blanc%>></option>
							<option value="1" title="<bean:message key='help.merli.propis'/>" <%=propis%>><bean:message key='merli.list.propis'/></option>
							<option value="2" title="<bean:message key='help.merli.unitat'/>" <%=uni%>><bean:message key='merli.list.unitat'/></option>
						</select>	
					<%}
					else
					{%>
					<td style="border-right: 2px groove #D2D2D2; #FFFFFF; padding: 0px 5px 0px 10px; width: 365px;">
					<%}%>
						<input type="text" name="id" size="20" style="width:140px;" maxlength="250" value="<%=(idSessio!=null)?idSessio:"Id del recurs"%>" class="<%=idSessio!=null?"hintTextboxActive":"hintTextbox"%>"  onkeydown="if (event.keyCode == 13) llistaCerca(0,1);"/>
						<input type="text" name="cerca" size="20" style="width:140px;" maxlength="250" value="<%=(cercaSessio!=null)?cercaSessio:mresources.getMessage(request.getLocale(),"merli.cercador.titolCerca")%>" class="<%=cercaSessio!=null?"hintTextboxActive":"hintTextbox"%>" onkeydown="if (event.keyCode == 13) llistaCerca(0,1);"/>
						
						<img class="cerca" src="web/images/cerca.png" onclick="llistaCerca(0,1);" id="cerca1" title="Cerca" align="top"/>
						<img id="cercaAvancada" class="cerca" src="web/images/cercaAvancada.png" onclick="loadCercaAvancada(this);" title="Cerca avan&ccedil;ada" align="top"/>
					</td>
					<% String data_ord=""; String alf_ord=""; String id_ord="";
					if(ordenacioSessio==null)	data_ord="checked";
					else
					{
						if(ordenacioSessio.equals("Data")) 			data_ord="checked";
						if(ordenacioSessio.startsWith("Alfa")) 		alf_ord="checked";
						if(ordenacioSessio.equals("Identificador")) id_ord="checked";
					}
					%>
					<td style="font-size: 10pt; font-weight: normal; font-size: 10pt; font-weight: normal; padding: 0px 10px;">
						<input type="radio" class="radio" name="ordenacio" value="<bean:message key='merli.cercador.ordDataV'/>" onclick="llistaCerca(0,1)" <%=data_ord%>  style="background-color:#D2D2D2;" >&nbsp;<bean:message key='merli.cercador.ordData'/>
						<input type="radio" class="radio" name="ordenacio" value="<bean:message key='merli.cercador.ordAlfabeticV'/>" onclick="llistaCerca(0,1)" <%=alf_ord%>  style="background-color:#D2D2D2;" >&nbsp;<bean:message key='merli.cercador.ordAlfabetic'/>
						<input type="radio" class="radio" name="ordenacio" value="<bean:message key='merli.cercador.ordIdV'/>" onclick="llistaCerca(0,1)" <%=id_ord%>  style="background-color:#D2D2D2;" >&nbsp;<bean:message key='merli.cercador.ordId'/>
					</td>
					<td style="text-align:right; padding-right: 28px;" valign="top">
					<%	if (usuari.hasPermission(MerliOperations.RECFISIC)){%>
						<a href="#" title="Exporta llistat" onclick="exporta();" style="color: #AD2114;"> Exporta llistat </a>&nbsp;
						<a href="<bean:message key='etiq.urlajuda.exportacio'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
					<%}%>
					</td>
				</tr>
			</table>
		</div>
		<div id="barra_buscador_avancat" style="display:none;">
		<%	boolean mediateco=usuari.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.RECFISIC);
		boolean publicador=usuari.hasPermissionPublicarTots();
		boolean traductor=usuari.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.TRADUCT);
		boolean administrador=usuari.isSuperuser();
		String fiss=""; String onls="";
		if(fisicSessio!=null)
		{
			if(fisicSessio.equals("1")) onls="checked";
			if(fisicSessio.equals("2")) fiss="checked";
			if(fisicSessio.equals("3")) {onls="checked"; fiss="checked";}
		}		
		%>
		<table valign="center" style="width: 100%;">
				<tr>
					<% if(mediateco){%>
					<td style="padding: 6px 6px 0px;"><bean:message key='merli.cercador.recurs'/></td>
					<%}%>
					<td style="padding: 6px 6px 0px;"><bean:message key='merli.cercador.data'/></td>
					<td style="padding: 6px 6px 0px;"><br></td>
				</tr>
				<tr>
					<% if(mediateco){%>
					<td style="border-right: 2px groove #FFFFFF; padding:  0px 5px 5px; width: 160px; font-weight: normal;">
						<input type="checkbox" style="width:20px;background-color:#D2D2D2" name="recursOnline" class="checkbox" value="<bean:message key='merli.cercador.online'/>" <%=onls%> /><bean:message key='merli.cercador.online'/>
						<input type="checkbox" style="width:20px;background-color:#D2D2D2" name="recursFisic" class="checkbox" value="<bean:message key='merli.cercador.fisics'/>" <%=fiss%> /><bean:message key='merli.cercador.fisics'/>		
					</td>
					<%}
					String barragris=""; if(mediateco||publicador||traductor||administrador) barragris="border-right: 2px groove #FFFFFF;";
					%>
					<td style="<%=barragris%>padding: 0px 5px 5px; width: 240px; font-weight: normal;">
						<input type="text" name="data_i" style="width:85px;" id="data_i" value="<%=data_iSessio!=null?data_iSessio:"Posterior a"%>" class="<%=data_iSessio!=null?"hintTextboxActive":"hintTextbox"%>" onkeydown="if (event.keyCode == 13) llistaCerca(0,1);"/> 	
						<a class="rodona" href="#"	onclick="cal.select(document.gestorForm.data_i,'anchor2','yyyy-MM-dd'); datasel='ini'; return false;"	name="anchor1" id="anchor1"> 
							<span title="<bean:message key='help.calendar'/>">&nbsp;+&nbsp;</span>
						</a>
						&nbsp;&nbsp;&nbsp;  
						<input type="text" name="data_f" style="width:85px;" id="data_f" value="<%=(data_fSessio!=null)?data_fSessio:"Anterior a"%>" class="<%=data_fSessio!=null?"hintTextboxActive":"hintTextbox"%>" onkeydown="if (event.keyCode == 13) llistaCerca(0,1);"/>
						<a class="rodona" href="#" onclick="cal.select(document.gestorForm.data_f,'anchor2','yyyy-MM-dd'); datasel='fi'; return false;" name="anchor2" id="anchor2"> 
							<span title="<bean:message key='help.calendar'/>">&nbsp;+&nbsp;</span>
						</a>
					</td>
					<td style="font-size: 10pt; font-weight: normal; padding: 0px 5px 5px;">
						<% if(mediateco){
							if(id_unitatSessio!= null)	((GestorForm)pageContext.findAttribute("gestorForm")).setId_unitat(id_unitatSessio);
						%>
						<PHTM:Select name="gestorForm" property="id_unitat" label="posUnitat" possibleValues="labUnitat" size="1" style="width: 140px;" value="" />
						<% }
						if(publicador||traductor||administrador){%>
						<input type="text" name="id_catalogador" size="20" style="width:140px;" maxlength="250" value="<%=(id_catalogadorSessio!=null)?id_catalogadorSessio:"Catalogat per"%>" class="<%=id_catalogadorSessio!=null?"hintTextboxActive":"hintTextbox"%>"   id="id_catalogador" onkeydown="if (event.keyCode == 13) llistaCerca(0,1);"/>
						<%}%>
						<input type="text" name="descripcioC" size="20" style="width:140px;" maxlength="250" value="<%=(descripcioSessio!=null)?descripcioSessio:mresources.getMessage(request.getLocale(),"merli.cercador.descripcio")%>" class="<%=descripcioSessio!=null?"hintTextboxActive":"hintTextbox"%>"  id="descripcioC" onkeydown="if (event.keyCode == 13) llistaCerca(0,1);" />
						<img class="cerca" src="web/images/cerca.png" onclick="llistaCerca(0,1);" title="Cerca" align="top"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="dreta">
		<div id="header">
			<div id="titleHeader">
			</div>
		</div>

		<div id="cos">
			<div id="titleContent">
				<span id="titol">
				<bean:message key='merli.title.propis'/>
				</span>&nbsp;			
				<a href="<bean:message key='etiq.urlajuda.llistats'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
				<%
				if(usuari.isSuperuser() || usuari.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.RECADD))
				{
				 %>
				<div style="float:right;padding-right:22px;">
					<a href="#" title="<bean:message key='help.merli.nou'/>" onclick="javascript:nou();" style="text-align: right; color: rgb(173, 33, 20); font-size: 14px; font-weight: bold;">
						<bean:message key='merli.op.nou'/>
					</a>
					&nbsp;
					<a href="<bean:message key='etiq.urlajuda.crear'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
				</div>
				<% } %>
			</div>
			<% 	String acres = (String)request.getAttribute("actionResult");
			if (acres != null && !"null".equals(acres)&& !"".equals(acres)){ %>		
			<!-- div id="missatgeResultat" 
				<% if ("ok".equals(acres)){%>
					class="infoOK">
					<span><bean:message key='merli.operacio.resultat.ok'/></span>
				<% }else{ %>
					class="infoFAIL">
					<span><bean:message key='merli.operacio.resultat.fail'/> <%=(String)request.getAttribute("actionMessage")%></span>
				<% } %>
			</div -->
			<script>setTimeout("amagarElement('missatgeResultat')",10000);</script>
			<% } %>	
			<div id="content">
				<div id="lisrec"> 
					<p class="comentari"><bean:message key='merli.head.propis'/></p>
				</div>
				<div id="paginacio"></div>
				<div id="waiting">
					<bean:message key='merli.waiting'/>
					<br/><BR/>
					<IMG src="web/images/activIndica.gif"/>
				</div>
			</div>
		</div>
	</div>
	</html:form>
	<script>
	function setDataFormat(y,m,d) {
		zerodia="";
		zeromes="";
		if(d<10) zerodia="0";
		if(m<10) zeromes="0";
		if(datasel=="ini")
		{
			document.gestorForm.data_i.value=zerodia+d+"-"+zeromes+m+"-"+y;
			document.gestorForm.data_i.className = HintActiveClass;
		}
		else
		{
			document.gestorForm.data_f.value=zerodia+d+"-"+zeromes+m+"-"+y;
			document.gestorForm.data_f.className = HintActiveClass;
		}	
     }
	</script>
	<%-- Calendari pels camps. --%>
	<jsp:include page="/web/calendari.jsp">
		<jsp:param name="function" value="setDataFormat"/>
		<jsp:param name="data" value="inicial"/>
	</jsp:include>			
	<%-- Fi calendari. --%>

</body>
</html:html>