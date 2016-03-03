<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%-- JSTL tag libs --%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean-el.tld" %>
<%@ include file="/web/taglibs.jsp"%>
<%@ page import="edu.xtec.merli.utils.Utility, edu.xtec.merli.Unitat, edu.xtec.merli.MerliBean, edu.xtec.merli.segur.User, edu.xtec.merli.segur.operations.MerliOperations,java.util.ArrayList" %>
<!-- PRUEBA PRUEBA -->
<html:html locale="true">
<meta http-equiv="content-type" content="text/html;charset=ISO-8859-15" />
<head>
<title><bean:message key="application.window.title"/></title>
<link type="text/css" href="web/css/merli.css" rel="stylesheet" media="screen">
<link type="text/css" href="web/css/gestor.css" rel="stylesheet" media="screen">
<link type="text/css" href="web/css/veurec.css" rel="stylesheet" media="screen">
<link href="web/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link rel="SHORTCUT ICON" href="web/images/favicon.ico" />
<jsp:useBean id="veuRecForm"  scope="request" class="edu.xtec.merli.gestorrec.VeuRecursForm"/>
<jsp:useBean id="gesrecurs" scope="request" class="edu.xtec.merli.gestorrec.GestorForm"/>

<script>
var subject="";
var body=""; 
var titleCaOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getTitle())--%>";
var titleEsOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getTitleEs())--%>";
var titleEnOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getTitleEn()) --%>";
var titleOcOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getTitleOc()) --%>";
var descriptionCaOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getDescription())--%>");
var descriptionEsOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getDescriptionEs()) --%>");
var descriptionEnOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getDescriptionEn())--%>");
var descriptionOcOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getDescriptionOc())--%>");
var rightsCaOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getRightsDesc())--%>";
var rightsEsOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getRightsEs())--%>";
var rightsEnOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getRightsEn()) --%>";
var rightsOcOrig = "";//<%--= Utility.xmlEncode(veuRecForm.getRightsOc()) --%>";
var pestAct = "title";
</script>
<script type="text/javascript" src="web/JS/inforec.js"></script>
<script type="text/javascript" src="web/JS/etiqAJAX.js"></script>
<script type="text/javascript" src="web/JS/jquery-1.4.2.min.js"></script> 
</head>

<body>

<jsp:include page="cap_generalitat.jsp"></jsp:include>

<html:form action="veurecurs.do">

			<html:hidden property="idRecurs"></html:hidden>
			<html:hidden property="llistat"></html:hidden>
			<html:hidden property="operation"></html:hidden>
			<html:hidden property="usuari"></html:hidden>
			
			
	<jsp:include page="capsa_logo.jsp"/>
	
	<div id="menu_capcalera" class="banner">
		<div id="titolRecursCatalogat"></div>
		<div id="modes">
		<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> LLISTA ACCIONS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->			
			<%if (veuRecForm.getOperation().equals("pubRec") && ((User)request.getSession().getAttribute("user")).hasPermissionPublicarTots() &&
					 String.valueOf(MerliBean.ESTAT_M_REALITZAT).equals(veuRecForm.getRm().getEstat())){%>	
			<!-- EDITA -->
				<a href="#" title="<bean:message key='help.op.editar'/>" onclick="setOperation('editar',this);">
					<bean:message key='merli.op.editar'/>
				</a>
				<span class="barraGris"></span>	
			<!-- RETORNA -->
				<a href="#" title="<bean:message key='help.op.retornar'/>" onclick="setOperation('retornar',this); setMissatge('retornat', '<%=veuRecForm.getIdRecurs()%>', '<%=veuRecForm.getTitle().replaceAll("'","\\\\'")%>');">
					<bean:message key='merli.op.retornar'/>
				</a>
				<span class="barraGris"></span>	
			<!-- DENEGA -->
				<a href="#" title="<bean:message key='help.merli.denegar'/>" onclick="setOperation('denegar',this); setMissatge('denegat', '<%=veuRecForm.getIdRecurs()%>', '<%=veuRecForm.getTitle().replaceAll("'","\\\\'")%>');">
					<bean:message key='merli.op.denegar'/>
				</a>
				<span class="barraGris"></span>	
			<!-- PUBLICA -->
				<a href="#" title="<bean:message key='help.op.validar'/>" onclick="setOperation('validar',this);">
					<bean:message key='merli.op.validar'/>
				</a>
				<span class="barraGris"></span>	
			<%}%>
			<%if (veuRecForm.getOperation().equals("tradRec") && ((User)request.getSession().getAttribute("user")).hasPermission(MerliOperations.TRADUCT) && String.valueOf(MerliBean.ESTAT_M_PUBLICAT).equals(veuRecForm.getRm().getEstat())){%>							
			<!-- TRADUEIX -->
				<a 	href="#"
					title="<bean:message key='help.op.traduir'/>" 
					onclick="enviar('traduir');">
					<bean:message key='merli.op.traduir' />
				</a> 
			<span class="barraGris"></span>	
			<%} %>
			<!-- TORNA -->
			<%if (veuRecForm.getOperation().equals("veureRec2")){%>	
			<a href="#" title="<bean:message key='help.merli.tancar'/>" onclick="window.close();">
				<bean:message key='merli.op.tancar'/>
			</a>
			<%}else {%>
			<a href="#" title="<bean:message key='help.merli.tornar'/>" onclick="goto('tornar');">
				<bean:message key='merli.op.tornar'/>
			</a>
			<%} %>
		</div> 
	</div>
	<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TRADUCCIO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
<%	String escond="";
	if (!(veuRecForm.getOperation().equals("tradRec") && ((User)request.getSession().getAttribute("user")).hasPermission(MerliOperations.TRADUCT) && String.valueOf(MerliBean.ESTAT_M_PUBLICAT).equals(veuRecForm.getRm().getEstat()))) escond="style=\"display:none;\"";%>							
	<div class="esquerra" <%=escond%> >
		<div id="operacions">
				<div class="titOperacions">
					<bean:message key='merli.op.traduct.fields'/>
				</div>	
				<ul>	
					<li title="<bean:message key='help.merli.op.traduct.general'/>">
						<strong><bean:message key='merli.op.traduct.general'/></strong>
						<ul>
							<li title="<bean:message key='help.merli.op.traduct.title'/>">
								<a href="#" onclick="tradOperation('titolRecTrad','<bean:message key='merli.op.traduct.title.ca'/>');">
									<bean:message key='merli.op.traduct.title'/>
								</a>
							</li>
							<li title="<bean:message key='help.merli.op.traduct.descripcio'/>">
								<a href="#" onclick="tradOperation('descripcioTrad','<bean:message key='merli.op.traduct.descripcio.ca'/>');">
									<bean:message key='merli.op.traduct.descripcio'/>
								</a>
							</li>
						</ul>
					</li>
					
					<li title="<bean:message key='help.merli.op.traduct.credits'/>">
						<strong><bean:message key='merli.op.traduct.credits'/></strong>
						<ul>
							<li title="<bean:message key='help.merli.op.traduct.rights'/>">
								<a href="#" onclick="tradOperation('rightsTrad','<bean:message key='merli.op.traduct.rights.ca'/>');">
									<bean:message key='merli.op.traduct.rights'/>
								</a>
							</li>
						</ul>
					</li>
				</ul>
		</div>
		<div id="validTrad">
			<div class="titOperacions" title="<bean:message key='help.merli.op.traduct.validar'/>">
				<bean:message key='merli.op.traduct.validar'/>	
			</div>
			<ul>
				<li>
				<%String aux="";
					if (veuRecForm.getRm().isEstatCa())
						aux = "ca";
					%>
					<INPUT type="checkbox" style="border:0px;" name="vtradCa" title="Català" <%=(veuRecForm.getRm().isEstatCa())?"checked":""%> value="ca"/>
						<bean:message key='merli.op.traduct.validar.ca'/>
					<br>
					<INPUT type="checkbox" style="border:0px;" name="vtradEs" title="Castellà" <%=(veuRecForm.getRm().isEstatEs())?"checked":""%> value="es"/>
						<bean:message key='merli.op.traduct.validar.es'/>				
					<br/>
					<INPUT type="checkbox" style="border:0px;" name="vtradEn" title="Anglès" <%=(veuRecForm.getRm().isEstatEn())?"checked":""%> value="en"/>
						<bean:message key='merli.op.traduct.validar.en'/>				
					<br/>
					<INPUT type="checkbox" style="border:0px;" name="vtradOc" title="Aranés" <%=(veuRecForm.getRm().isEstatOc())?"checked":""%> value="oc"/>
						<bean:message key='merli.op.traduct.validar.oc'/>				
				</li>
			</ul>
		</div>
	</div>

	<div id="cos" class="content_fitxa">
		<div id="titleContent" style="color:#AD2114;margin-top:35px;">
			<span id="titol">
				<jsp:getProperty name="veuRecForm" property="title"/>
			</span>				
		</div>
		<div id="content">			
			<div class="mitjaVista">
			<!--  PRUEBA -->
			<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IDENTIFICACIO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
			<div class="opcions">
					<div class="title"><bean:message key='etiq.title.identificacio' /></div>
					<!-- IDENTIFICADOR -->
					<div title="Identificador del recurs"><span class="camp"><bean:message key='etiq.idrec' /></span> 
						<span id="idRecVal" class="valor"><jsp:getProperty name="veuRecForm" property="idRecurs"/>&nbsp;</span>
					</div>
					<!-- TITOL -->
					<div title="Títol del recurs"><span class="camp"><bean:message key='etiq.title' /></span> 
						<span id="titolRecVal" class="valor"><jsp:getProperty name="veuRecForm" property="title"/>&nbsp;</span>
					</div>		
					<!-- ENLLAC -->
					<% if (veuRecForm.getUrl() != null && !"".equals(veuRecForm.getUrl()) && !"Error".equals(veuRecForm.getUrl())){%>
					<div title="Enllaç del recurs"><span class="camp"><bean:message key='etiq.url' /></span>
						<span class="valor"><A href="<jsp:getProperty name="veuRecForm" property="url"/>" target="_blank" style="font-weight: normal;"><jsp:getProperty name="veuRecForm" property="url"/></A>&nbsp;</span>
					</div>
					<% } %>
					<!-- ID FISIC -->
					<% if (veuRecForm.getIdFisic()!=null && veuRecForm.getIdFisic().size()>0){%>				
					<div class="" title="<bean:message key='help.idFisic'/>">
						<span class="camp"><bean:message key='etiq.idFisic' /></span>
						<span class="valor">
						<%
							for (int i=0; i< veuRecForm.getIdFisic().size(); i++){					
								if (i>0){
								%>, <%
								}
							%>
							<%=veuRecForm.getTipusIdFisicSel().get(i)%>: <%=veuRecForm.getIdFisic().get(i)%>
							<%}%>
							&nbsp;
						</span>						
					</div>
					<% } %>
					<!-- CODI NU -->
					<% if (veuRecForm.getRm().getNu() != null && !"".equals(veuRecForm.getRm().getNu())){%>
						<div class="" title="<bean:message key='help.nu'/>">
							<span class="camp"><bean:message key='etiq.nu' /></span>
							<span class="valor"><%= veuRecForm.getRm().getNu() %>&nbsp;</span>		
						</div>
					<%}%>
				</div>
				<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DESCRIPCIO CURRICULAR <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
				<!-- DESC CURRICULARS -->
				<div class="opcions">
					<div class="title"><bean:message key='etiq.title.relCur' /></div>
					<div title="<bean:message key='help.relCur.fitxa'/>">
						<span class="camp"><bean:message key='etiq.curriculum'/></span> 
						<span class="valor"><jsp:getProperty name="veuRecForm" property="curriculum"/>&nbsp;</span>
					</div>
				</div>
				<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DESCRIPCIO TEMATICA <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
				<div class="opcions">
					<div class="title"><bean:message key='etiq.parClau' /></div>
					<!-- DESC TESAURE -->
					<div title="<bean:message key='help.parClau.fitxa'/>">
						<span class="camp"><bean:message key='etiq.paraulesclau'/></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="taxonTerm"/>&nbsp;</span>
					</div>
					<!-- DESC OBERTS -->
					<% 
						ArrayList paraules=veuRecForm.getRm().getParaules();
						if(paraules.size()>0)
						{
						%>
							<div title="Paraules obertes">
								<span class="camp"><bean:message key='etiq.paraulesobertes'/></span>
								<span class="valor">
									<% 
										String paraula;
										for(int p=0; p<paraules.size(); p++)
										{
											paraula=(String)paraules.get(p);
											if(p<(paraules.size()-1))paraula+=",";
									 %>
										 <%=paraula %>
									 <% } %>
									 &nbsp;							 	
								</span>
							</div>
						<% } %>		
				</div>
				<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> DESCRIPCIO GENERAL <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
				<div class="opcions">
					<div class="title"><bean:message key='etiq.title.descGen' /></div>
					
					<!-- RESUM -->
					<div title="Resum del recurs"><span class="camp"><bean:message key='etiq.description' /></span>
						<span id="descripcioVal" class="valor"><jsp:getProperty name="veuRecForm" property="description"/>&nbsp;</span>
					</div>
					<!-- AMBIT -->
					<% if (veuRecForm.getEndUserRol() != null && !"".equals(veuRecForm.getEndUserRol()) && !"Error".equals(veuRecForm.getEndUserRol())){%>
					<div title="<bean:message key='help.destinatari' />"> 
						<span class="camp"><bean:message key='etiq.destinatari'/></span> 
						<span class="valor"><jsp:getProperty name="veuRecForm" property="endUserRol"/>&nbsp;</span>
					</div>
					<% } %>
					<!-- CONTEXT -->
					<% if (veuRecForm.getContext() != null && !"".equals(veuRecForm.getContext()) && !"Error".equals(veuRecForm.getContext())){%>
					<div title="<bean:message key='help.context' />">
						<span class="camp"><bean:message key='etiq.context' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="context"/>&nbsp;</span>
					</div>
					<% } %>
					<!-- TIPUS DE RECURS -->
					<% if (veuRecForm.getResourceType() != null && !"".equals(veuRecForm.getResourceType()) && !"Error".equals(veuRecForm.getResourceType())){%>
					<div title="<bean:message key='help.resourcetype' />"> 
						<span class="camp"><bean:message key='etiq.resourcetype' /></span> 
						<span class="valor"><jsp:getProperty name="veuRecForm" property="resourceType"/>&nbsp;</span>
					</div>
					<% } %>	
					<!-- MARC -->
					<% if (veuRecForm.getAmbit() != null && !"".equals(veuRecForm.getAmbit()) && !"Error".equals(veuRecForm.getAmbit())){%>
					<div title="<bean:message key='help.background' />">
						<span class="camp"><bean:message key='etiq.background' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="ambit"/>&nbsp;</span>
					</div>		
					<% } %>	
					<!-- CONTEXTUALITZACIO -->					
					<% if (veuRecForm.getRm().getContext2() != null && !"".equals(veuRecForm.getRm().getContext2()) && !"Error".equals(veuRecForm.getRm().getContext2())){%>
						<div title="<bean:message key='help.context2' />"> 
							<span class="camp"><bean:message key='etiq.context2' /></span>
							<span class="valor"><%= veuRecForm.getRm().getContext2() %>&nbsp;</span>
						</div>
					<% } %>
					<!-- TIPUS DE RELACIO -->
					<% if (veuRecForm.getRecRel()!=null && veuRecForm.getRecRel().size()>0){%>				
					<div class="" title="<bean:message key='help.recRel'/>">
						<span class="camp"><bean:message key='etiq.tipusRel' /></span>
						<span class="valor">
						<%
							for (int i=0; i< veuRecForm.getRecRel().size(); i++){	
								String sTipusRel;
								if(veuRecForm.getTipusRel().get(i).equals("té part"))	sTipusRel="Inclou";	
								else 													sTipusRel="Forma part de";				
								if (i>0){
								%>, <%
								}
							%>
							<%=sTipusRel%> <i><%=veuRecForm.getDescRel().get(i)%></i>&nbsp;(<%=veuRecForm.getRecRel().get(i)%>)
							<%}%>
							&nbsp;
						</span>						
					</div>
					<% } %>
					<!-- IDIOMES -->
					<div title="<bean:message key='help.language' />"> 
						<span class="camp"><bean:message key='etiq.language' /></span>
						<span class="valor"><%= veuRecForm.getLanguageNoms()%>&nbsp;</span>
						<!-- <span class="valor"><jsp:getProperty name="veuRecForm" property="language"/>&nbsp;</span> -->
					</div>
					<!-- FORMATS -->
					<div title="<bean:message key='help.format' />"> 
						<span class="camp"><bean:message key='etiq.format' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="format"/>&nbsp;</span>
					</div>
					<!-- DURADA -->
					<%
						String hores, minuts, segons;
						hores=veuRecForm.getDuraHora();
						minuts=veuRecForm.getDuraMin();
						segons=veuRecForm.getDuraSeg();
						if(!(("".equals(hores) || "0".equals(hores)) && ("".equals(minuts) || "0".equals(minuts)) && ("".equals(segons) || "0".equals(segons))))
						{
					%>
					<div title="<bean:message key='help.duration' />"> 
						<span class="camp"><bean:message key='etiq.duration' /></span> 
						<span class="valor">
						<% 	
							if(!"".equals(hores) && !"0".equals(hores))
							{%>
								<jsp:getProperty name="veuRecForm" property="duraHora"/>&nbsp;<bean:message key='etiq.duration.hour.title' />&nbsp;&nbsp;
							<%}
							if(!"".equals(minuts) && !"0".equals(minuts))
							{%>
								<jsp:getProperty name="veuRecForm" property="duraMin"/>&nbsp;<bean:message key='etiq.duration.minute.title'/>&nbsp;&nbsp;
							<%}
							if(!"".equals(segons) && !"0".equals(segons))
							{%>
								<jsp:getProperty name="veuRecForm" property="duraSeg"/>&nbsp;<bean:message key='etiq.duration.second.title'/>
							<%}%>
							&nbsp;
						</span>
					</div>
					<% }%>
					<!-- CARACTERISTIQUES -->
					<% if (veuRecForm.getCaractRFisic() != null && !"".equals(veuRecForm.getCaractRFisic()) && !"Error".equals(veuRecForm.getCaractRFisic())){%>
					<div title="<bean:message key='help.caractRFisic' />"> 
						<span class="camp"><bean:message key='etiq.caractRFisic' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="caractRFisic"/>&nbsp;</span>
					</div>		
					<% }%>	
					<!-- EDAT, DIFICULTAT i NU
					<div title="<bean:message key='help.age' />"> 
						<span class="camp"><bean:message key='etiq.age'/></span> 
						<span class="valor"><bean:message key='etiq.age.from.title'/>&nbsp;<jsp:getProperty name="veuRecForm" property="minAge"/>&nbsp;<bean:message key='etiq.age.to.title'/>&nbsp;<jsp:getProperty name="veuRecForm" property="maxAge"/></span>
					</div>
					<div title="<bean:message key='help.difficulty' />"> 
						<span class="camp"><bean:message key='etiq.difficulty' /></span> 
						<span class="valor"><jsp:getProperty name="veuRecForm" property="difficulty"/>&nbsp;</span>
					</div>-->
					<%-- if (veuRecForm.getRm().getNu("") != null && !"".equals(veuRecForm.getRm().getNu(""))){%>
						<div title="<bean:message key='help.nu' />"> 
							<span class="camp"><bean:message key='etiq.nu' /></span> 
							<span class="valor"><%=veuRecForm.getRm().getNu("")%></span>
						</div>
					<%} --%>	
				</div>
				<!-- >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CREDITS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< -->
				<div class="opcions">
					<div class="title"><bean:message key='etiq.title.dretsC' /></div>
					<!-- AUTORIA -->
					<% if (veuRecForm.getAutor() != null){%>
					<div title="<bean:message key='help.author' />"> 
						<span class="camp"><bean:message key='etiq.author' /></span>
						<span class="valor"><%= veuRecForm.getAutor() %>&nbsp;</span>
					</div>
					<%}%>
					<!-- EDITORIAL/PRODUCTORA -->
					<% if (veuRecForm.getEditor() != null && !"".equals(veuRecForm.getEditor()) && !"Error".equals(veuRecForm.getEditor())){%>
					<div title="<bean:message key='help.editor' />"> 
						<span class="camp"><bean:message key='etiq.editor' /></span>
						<span class="valor"><%= veuRecForm.getEditor() %>&nbsp;</span>
					</div>
					<%}%>
					<!-- DATA -->
					<% if (veuRecForm.getDateAutor() != null && !"".equals(veuRecForm.getDateAutor()) && veuRecForm.getDateAutor().length()>=4){%>
					<div title="<bean:message key='help.date' />">
						<span class="camp"><bean:message key='etiq.date' /></span>
						<%
							String data= veuRecForm.getDateAutor();
							if(veuRecForm.getRm().getEsFisic()) data=data.substring(data.length()-4);
						%>
						<span class="valor"><%=data%>&nbsp;</span>
					</div>
					<%}%>
					<!-- LLICENCIA -->
					<% if (veuRecForm.getLicense() != null && !"".equals(veuRecForm.getLicense())){%>
					<div title="<bean:message key='help.license' />"> 
						<span class="camp"><bean:message key='etiq.license' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="license"/>&nbsp;</span>
					</div>
					<% } %>
					<!-- DESC. DRETS -->
					<% 
					String amaga="";
					if (!(veuRecForm.getRightsDesc() != null && !"".equals(veuRecForm.getRightsDesc()))) amaga="style=\"display:none;\"";%>
					<div title="<bean:message key='help.copyrightdescription' />" <%=amaga%>> 
						<span class="camp"><bean:message key='etiq.rights.description' />&nbsp;</span>
						<span id="rightsVal" class="valor"><jsp:getProperty name="veuRecForm" property="rightsDesc"/>&nbsp;</span>
					</div>
					<!-- COST -->
					<% if (veuRecForm.getCost() != null && !"".equals(veuRecForm.getCost())){%>
					<div title="<bean:message key='help.cost' />"> 
						<span class="camp"><bean:message key='etiq.cost' /></span>
						<span class="valor"><jsp:getProperty name="veuRecForm" property="cost"/>&nbsp;</span>
					</div>
					<%}%>
					<!-- ESTAT -->
					<div title="<bean:message key='help.estat' />"> 
						<span class="camp"><bean:message key='etiq.estat' /></span>
						<span class="valor">
						<%
							int iEstat=Integer.parseInt(veuRecForm.getRm().getEstat());
							String sEstat="";
							switch(iEstat)
							{
								case edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT:
									%><bean:message key='merli.estat.-2' /><%
									break;
								case edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT:
									%><bean:message key='merli.estat.-1' /><%
									break;
								case edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES:
									%><bean:message key='merli.estat.0' /><%
									break;
								case edu.xtec.merli.MerliBean.ESTAT_M_PENDENT:
									%><bean:message key='merli.estat.10' /><%
									break;
								case edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT:
									%><bean:message key='merli.estat.2' /><%
									break;
								case edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT:
									%><bean:message key='merli.estat.4' /><%
									break;
								default:
									break;
							}
							%>&nbsp;
						</span>
					</div>
					<!-- CATALOGADOR -->
					<div title="<bean:message key='help.responsable' />"> 
						<span class="camp"><bean:message key='etiq.responsable' /></span> 
						<span class="valor"><%=	veuRecForm.getEtiquetadorConvertit()%>&nbsp;
						(<%= veuRecForm.getDataEtiquetador()%>)&nbsp;</span>
					</div>
					<!-- REVISAT PER -->
					<%if ("3".equals(veuRecForm.getRm().getEstat()) || "4".equals(veuRecForm.getRm().getEstat())){
						if(!veuRecForm.getValidador().equals("")){%>
						<div title="<bean:message key='help.validador' />"> 
							<span class="camp"><bean:message key='etiq.validador' /></span> 
							<span class="valor"><jsp:getProperty name="veuRecForm" property="validador"/>&nbsp;
							(<%= veuRecForm.getDataValidador()%>)&nbsp;</span>
						</div>
					<%}} %>
					<!-- CORRECTOR -->
					<%-- if ("4".equals(veuRecForm.getRm().getEstat())){
						if(!veuRecForm.getCorrector().equals("")){%>
						<div title="<bean:message key='help.corrector' />"> 
							<span class="camp"><bean:message key='etiq.corrector' /></span> 
							<span class="valor"><jsp:getProperty name="veuRecForm" property="corrector"/>&nbsp;</span>
						</div>
					<%}} --%>	
					<!-- VERSIO -->
					<% if (veuRecForm.getVersion() != null && !"".equals(veuRecForm.getVersion()) && !"Error".equals(veuRecForm.getVersion())){%>
					<div title="<bean:message key='help.version' />"> 
						<span class="camp"><bean:message key='etiq.version' /></span> 
						<span class="valor"><jsp:getProperty name="veuRecForm" property="version"/>&nbsp;</span>
					</div>
					<%}%>	
					<!-- DISPONIBILITAT -->
					<% if (veuRecForm.getUnitats()!=null && veuRecForm.getUnitats().size()>0){%>				
					<div class="" title="<bean:message key='help.unitats'/>">
						<span class="camp"><bean:message key='etiq.unitats' /></span>
						<span class="valor">
						<%
						   Unitat uni = null;
							for (int i=0; i< veuRecForm.getUnitats().size(); i++){
									uni = (Unitat)veuRecForm.getUnitats().get(i);					
								if (i>0){
								%>, <%
								}
							%>
							<%=uni.getName()%>
							<%}%>
							&nbsp;
						</span>						
					</div>
					<% } %>
					
				</div>
			</div>
		</div>
		<div id="assignar">
				<div class="title">A quin validador vols assignar el recurs?</div>
				<p>El recurs passarà a la teva llista de recursos en procés.</p>
				<div>
					<div class="itemOperacions" style="text-align:center">
						<a href="#" onclick="javascript:assignar('<%=((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser()%>');">
							Assigna
						</a>
						<a href="#" onclick="javascript:cancel('assignar');">
							<!--img class="operacio" src="merli14_files/esborrar.png" alt="cancel·la"-->
							Cancel·la
						</a>
					</div>		
				</div>	
			</div>
			<div id="negar" class="missatgeUser">
					<div class="title" style="background-color:#AD2114; padding: 10px; margin: 0px; -moz-border-radius:5px 5px 0px 0px; -webkit-border-radius:5px 5px 0px 0px;">Escriu el missatge destinat a l'usuari informant-lo de la teva decisió:</div>
					<div>
						<p>
							Assumpte<br/>
							<input id="assmis" type="text" name="subject" maxlength="2000" style="padding: 5px; width: 296px; border-color: #AAA;" />
						</p>
						<p>
							Missatge<br/>
							<textarea name="missatge" rows="9" style="padding: 5px; width: 296px; border-color: #AAA;"></textarea>
						</p>
						<br/>
					</div>
					<div>
						<div class="itemOperacions"> <!-- mailto:<jsp:getProperty name='veuRecForm' property='etiquetadorMail'/> -->
							<button type="button" title="Envia" class="butoMerli small red" onClick="javascript:enviaMissatge();"><img src="web/images/enviar_boto.png">&nbsp;Envia</button>&nbsp;				
							<button type="button" title="Cancel·la" class="butoMerli small red" onClick="javascript:cancel('negar');"><img src="web/images/cancelar_boto.png">&nbsp;Cancel·la</button>		
				
						
							<!-- 
							<a href="#" onclick="javascript:missatge(this);">
								<img class="operacio" src="web/images/enviar_boto.png" alt="E">
								envia
							</a>
							<a href="#" onclick="javascript:cancel('negar');">
								<img class="operacio" src="web/images/cancelar_boto.png" alt="E">
								cancel·la
							</a> 
							-->
							
						</div>			
					</div>			
			</div>
			<%--  if (((User)request.getSession().getAttribute("user")).hasPermission(MerliOperations.PUBLICAR) &&
					(veuRecForm.getRm().getEstat().equals(String.valueOf(MerliBean.LLISTA_M_REALITZATS)))){%>
				<jsp:include page="/web/recurs/corrector.jsp"></jsp:include>
			<%} --%>	 			

			<%if (((User)request.getSession().getAttribute("user")).hasPermission(MerliOperations.TRADUCT) && String.valueOf(MerliBean.ESTAT_M_PUBLICAT).equals(veuRecForm.getRm().getEstat())){%>
					<jsp:include page="/web/recurs/traductor.jsp"></jsp:include>
			<%} %>
	</div>

<script>
var subject="";
var body="";
if(document.forms.veuRecForm.title)			titleCaOrig = document.forms.veuRecForm.title.value;
if(document.forms.veuRecForm.titleEs)		titleEsOrig = document.forms.veuRecForm.titleEs.value;
if(document.forms.veuRecForm.titleEn)		titleEnOrig = document.forms.veuRecForm.titleEn.value;
if(document.forms.veuRecForm.titleOc)		titleOcOrig = document.forms.veuRecForm.titleOc.value;
if(document.forms.veuRecForm.description) 	descriptionCaOrig = document.forms.veuRecForm.description.value;
if(document.forms.veuRecForm.descriptionEs)	descriptionEsOrig = document.forms.veuRecForm.descriptionEs.value;
if(document.forms.veuRecForm.descriptionEn) descriptionEnOrig = document.forms.veuRecForm.descriptionEn.value;
if(document.forms.veuRecForm.descriptionOc) descriptionOcOrig = document.forms.veuRecForm.descriptionOc.value;
if(document.forms.veuRecForm.rightsDesc) rightsCaOrig = document.forms.veuRecForm.rightsDesc.value;
if(document.forms.veuRecForm.rightsEs) rightsEsOrig = document.forms.veuRecForm.rightsEs.value;
if(document.forms.veuRecForm.rightsEn) rightsEnOrig = document.forms.veuRecForm.rightsEn.value;
if(document.forms.veuRecForm.rightsOc) rightsOcOrig = document.forms.veuRecForm.rightsOc.value;
pestAct = "title";
</script>
</html:form>

	<jsp:include page="peu_generalitat.jsp"></jsp:include>
</body>
</html:html>
