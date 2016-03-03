<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<!--<@page contentType="text/html;charset=UTF-8" language="java"> -->
<%-- JSTL tag libs --%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld"%>
<%@ taglib uri="/WEB-INF/PHTM.tld" prefix="PHTM"%>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld"%>
<%@ include file="/web/taglibs.jsp"%>
<html:html locale="true">
<meta http-equiv="content-type" content="text/html;charset=ISO-8859-15" />
<head>
<title><bean:message key="application.window.title"/></title>
<link type="text/css" href="web/css/merli.css" rel="stylesheet">
<link type="text/css" href="web/css/etiq.css" rel="stylesheet">
<link type="text/css" href="web/css/thickbox.css" rel="stylesheet">
<link rel="SHORTCUT ICON" href="web/images/favicon.ico" />
<!--script	type="text/javascript" src="web/JS/calendari.js"></script--> 
<script	type="text/javascript" src="web/JS/etiq.js"></script> 
<script	type="text/javascript" src="web/JS/AJAXScript.js"></script> 
<script	type="text/javascript" src="web/JS/etiqAJAX.js"></script> 
<script type="text/javascript" src="web/JS/curriAJAX.js"></script> 
<script type="text/javascript" src="web/JS/jquery-1.4.2.min.js"></script> 
<script type="text/javascript" src="web/JS/catalogador-jq.js"></script>
<script type="text/javascript" src="web/JS/thickbox.js"></script>  
<script type="text/javascript" src="web/JS/hintTextBoxes.js"></script>
<script>
	var errorRequest = "<bean:message key='error.etiq.request'/>";
	var maxLlengues =  "<bean:message key='error.etiq.max.llengues'/>";
	var estatValidat = "<% out.print(edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES);%>";
	var edatMinBiggerMax = "<bean:message key='error.etiq.age.value'/>";
	var thesSelectedTerm = "<bean:message key='error.etiq.thesaurus.selected'/>";
	var misExitEtiq = "<bean:message key='etiq.op.exit.mis'/>";
	var misModificar="<bean:message key='etiq.missatge.modificar'/>";
	var misEnviar="<bean:message key='etiq.missatge.enviar'/>";
	var misCrear="<bean:message key='etiq.missatge.crear'/>";
	var misValidar="<bean:message key='etiq.missatge.validar'/>";
	var license = new Array();
	
	//afegir el text per cada una de les llicències.
	license[0] = "<bean:message key='etiq.license.id1'/>";
	license[1] = "<bean:message key='etiq.license.id1'/>";
	license[2] = "<bean:message key='etiq.license.id2'/>";
	license[3] = "<bean:message key='etiq.license.id3'/>";
	license[4] = "<bean:message key='etiq.license.id4'/>";
	license[5] = "<bean:message key='etiq.license.id5'/>";
	license[6] = "<bean:message key='etiq.license.id6'/>";
	license[7] = "<bean:message key='etiq.license.id7'/>";
	license[8] = "<bean:message key='etiq.license.id8'/>";
	license[9] = "<bean:message key='etiq.license.id9'/>";
	// XTEC ******* AFEGIT - Added 2 type of licence in the database thats why added those licence details here also.
	// ************ 2014.04.15 @naseq
		license[10] = "<bean:message key='etiq.license.id10'/>";
		license[11] = "<bean:message key='etiq.license.id11'/>";
	// ************ FI (2014.04.15 @naseq)	
	
	var pagina = new Array();
	pagina[1] = "identiRec";
	pagina[2] = "relCur";
	pagina[3] = "parClau";
	pagina[4] = "descGenC";
	pagina[5] = "dretsC";

	/*
	var cal = new CalendarPopup("calendari");
	cal.setCssPrefix("TEST");
	cal.showYearNavigation();
	cal.setMonthNames(<bean:message key='etiq.month'/>);
	cal.setDayHeaders(<bean:message key='etiq.week'/>);
	cal.setWeekStartDay(1);
	cal.setTodayText(<bean:message key='etiq.today'/>);
	cal.setReturnFunction("setDataFormat");
	*/
	var clics= 0;
	var usuari=" ";
	var perm="no";
</script>
</head>

<body onload="javascript:carregaEtiquetador(usuari,perm);">

<jsp:useBean id="etiqBean" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqBean" />
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
<jsp:useBean id="gesrecurs" scope="request"
	class="edu.xtec.merli.gestorrec.GestorForm" />

<jsp:include page="cap_generalitat.jsp"></jsp:include>
	
<html:form action="etiquetar.do" focus="titol">
	<html:hidden property="idRecurs"></html:hidden>
	<html:hidden property="operacio"></html:hidden>
	<html:hidden property="selecTerm"></html:hidden>
	<html:hidden property="selecLabel"></html:hidden>
	<html:hidden property="selecParaulaId"></html:hidden>
	<html:hidden property="selecParaula"></html:hidden>
	<html:hidden property="curLevel"></html:hidden>
	<html:hidden property="curArea"></html:hidden>
	<html:hidden property="curContent"></html:hidden>
	<html:hidden property="curriculum"></html:hidden>
	<html:hidden property="llistat"></html:hidden>
	<html:hidden property="comprovaHidden"></html:hidden>
	
	<jsp:include page="capsa_logo.jsp"/>

	<%
		String us=((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")).getUser(); 
		String p=(((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")).isSuperuser() || ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))?"si":"no";
	%>
	<script>
		usuari="<% out.print(us); %>";
		perm="<% out.print(p); %>";
	</script>

	<div id="menu_capcalera" class="banner">
		<div id="titolRecursCatalogat">
			<%if (etiqForm.getOperacio().equals("newRec")) {
				%><bean:message key='etiq.resource.nou' /><%}
			else{%><bean:message key='etiq.resource.edit' /> <%}%>
			<div id="resourceTitle">
				<span> <jsp:getProperty name="etiqForm" property="titol" /> 
				<%if (!etiqForm.getOperacio().equals("newRec")) {
					%>(<jsp:getProperty name="etiqForm" property="idRecurs" />) 
				<%}else{%>
					<bean:message key='etiq.resource.titol.nou' />
				<%} %>
				</span>
			</div>
		</div>
		<div id="modes">
			<%if (((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).isSuperuser())
			{ //INI operació ADMIN
			%>
				<a 	href="#" 
					title="<bean:message key='help.op.save'/>"  
					onclick="desarAdmin();">
						<bean:message key='etiq.op.save' />
				</a>						
				<span class="barraGris"></span>			
				<%
			}//FI operacio ADMIN
			else
			{ // INI opcions usuari estandard.
				int estatActual=0;
				try{
						estatActual = Math.min(Integer.parseInt(etiqForm.getEstatSel()[0]),edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT);
					}
					catch(Exception e){}
				String hidden=""; if (etiqForm.getOperacio().equals("newRec") && etiqForm.getComprovaHidden()=="0") hidden="style='display:none;'";
				 %>
				<a id="desa" href="#" <%=hidden%>
					title="<bean:message key='help.op.save'/>"  
					onclick="enviar('<%=estatActual%>');">
						<bean:message key='etiq.op.save' />
				</a>					
				<span id="barradesa" class="barraGris" <%=hidden%> ></span>			
				<%
			}	// FI opcions usuari estandard.
			%>						
			<a 	href="#"
				title="<bean:message key='help.op.exit'/>"
				onclick="cancela();"><!-- onclick="if (!confirm('<bean:message key='etiq.op.exit.mis'/>')){ return false;}else{cancela();}"> -->
					<bean:message key='etiq.op.exit' />
			</a>
			<!--span> | </span>
			<a href="login.jsp" title="<bean:message key='help.op.logout'/>">
				<bean:message key='etiq.op.logout' />
			</a-->
		</div>	
	</div>

 	<%
		 String oc2=""; String oc3=""; String oc4=""; String oc5="";
		 if (!etiqForm.getOperacio().equals("newRec") || etiqForm.getComprovaHidden()!="0")
		 {
		 	oc2="onclick='veure(2,this);'";oc3="onclick='veure(3,this);'";oc4="onclick='veure(4,this);'";oc5="onclick='veure(5,this);'";
		 }
	%>
	<div id="top_menu_varios" class="banner"><a name="topmenu"></a>
		<ul class="menuCatalogadorPassos">
			<li>
				<a id="menuPasDefault" onclick="veure(1,this);" title="<bean:message key='help.descIdenti'/>">
					<span class="numeroMenu">1</span>
					<bean:message key='etiq.identificacio' />
				</a>
			</li>
			<li>
				<a id="menuPas2" <%=oc2%> title="<bean:message key='help.relCur'/>">
					<span class="numeroMenu">2</span>
					<bean:message key='etiq.relCur' />
				</a>
			</li>
			<li>
				<a id="menuPas3" <%=oc3%> title="<bean:message key='help.parClau'/>">
					<span class="numeroMenu">3</span>
					<bean:message key='etiq.parClau' />
				</a>
			</li>
			<li>
				<a id="menuPas4" <%=oc4%> title="<bean:message key='help.descGen'/>">
					<span class="numeroMenu">4</span>
					<bean:message key='etiq.descGen' />
				</a>
			</li><!--
			<li>
				<a href="#" onclick="veure('descTecC',this);" title="<bean:message key='help.descTec'/>">
					<span class="numeroMenu">5</span>
					<bean:message key='etiq.descTec' />
				</a>
			</li>-->
			<li class="lastMenu">
				<a id="menuPas5" <%=oc5%> title="<bean:message key='help.dretsC'/>">
					<span class="numeroMenu">5</span>
					<bean:message key='etiq.dretsC' />
				</a>
			</li><!--
			<li class="lastMenu">
				<a href="#" onclick="veure('edRec',this);" title="<bean:message key='help.edRec'/>">
					<span class="numeroMenu">7</span>
					<bean:message key='etiq.edRec' />
				</a>
			</li>-->
		</ul>
	</div>
	
	<div id="errors" class="rightInfo banner">
			<html:errors property="titol" /> 
			<html:errors property="recFisic" />
			<html:errors property="url" /> 			
			<html:errors property="curriculum" />			
			<html:errors property="thesaurus" />			
			<html:errors property="descripcio" />
			<html:errors property="context2" />
			<html:errors property="descRel" />
			<html:errors property="idioma" />
			<html:errors property="formats" />
			<html:errors property="caractRFisic" />
			<html:errors property="autor" />
			<html:errors property="editor" />
			<html:errors property="descDrets" /> 
			<html:errors property="responsable" /> 
			<html:errors property="validador" />
			<!-- <html:errors property="corrector" /> -->
			<html:errors property="recRel" />
	</div>
	
	
	<div id="cos" class="content">
		<div id="titleContent">
			<span id="tit1" title="<bean:message key='help.descIdenti'/>">
				1. <bean:message key='etiq.title.identificacio' />
				<a href="<bean:message key='etiq.urlajuda.id'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
			</span> 
			<span id="tit2" title="<bean:message key='help.relCur'/>">
				2. <bean:message key='etiq.title.relCur' />
				<a href="<bean:message key='etiq.urlajuda.cur'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
			</span> 
			<span id="tit3" title="<bean:message key='help.parClau'/>">
				3. <bean:message key='etiq.title.parClau' />
				<a href="<bean:message key='etiq.urlajuda.tes'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
			</span> 
			<span id="tit4" title="<bean:message key='help.descGen'/>">
				4. <bean:message key='etiq.title.descGen' />
				<a href="<bean:message key='etiq.urlajuda.general'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
			</span> 
			<span id="tit5" title="<bean:message key='help.dretsC'/>">
				5. <bean:message key='etiq.title.dretsC' />
				<a href="<bean:message key='etiq.urlajuda.credits'/>" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>
			</span>
		</div>
		<div id="content">
			<jsp:include page="/web/etiq/identificacio.jsp"></jsp:include>
			<jsp:include page="/web/etiq/thesaurus.jsp"></jsp:include>
			<jsp:include page="/web/etiq/descripcio.jsp"></jsp:include>
			<jsp:include page="/web/etiq/curriculum.jsp"></jsp:include>
			<jsp:include page="/web/etiq/drets.jsp"></jsp:include>	
		<%-- 
			<jsp:include page="/web/etiq/general.jsp"></jsp:include>
			<jsp:include page="/web/etiq/educacio.jsp"></jsp:include>
			<jsp:include page="/web/etiq/thesaurus.jsp"></jsp:include>
			<jsp:include page="/web/etiq/tecnica.jsp"></jsp:include>
			<jsp:include page="/web/etiq/curriculum.jsp"></jsp:include>
			<jsp:include page="/web/etiq/drets.jsp"></jsp:include>	
			<jsp:include page="/web/etiq/edRec.jsp"></jsp:include>			
		 --%>
		 <%
		 
		 //si es un recurs nou i no ha passat el comprova amaga el boto seguent
		 //si no es un recurs nou amaga el boto comprova
		 String hidSeg=""; String hidComp="";
		 if (etiqForm.getOperacio().equals("newRec") && etiqForm.getComprovaHidden()=="0")	hidSeg="style='visibility: hidden'"; 
		 if (!etiqForm.getOperacio().equals("newRec")) 										hidComp="style='visibility: hidden'";
		  %>
			 <div id="buttons-nav">
				<a id="comprovaLink" href="#" class="thickbox" style="text-decoration: none;">
					<button type="button" title="<bean:message key='help.comprova'/>" class="butoMerli small red comprova" onClick="javascript:comprova();return false;" id="b_compr"><bean:message key='etiq.resource.comprova' /></button>
				</a>			
				<button type="button" title="<bean:message key='help.anterior'/>" class="butoMerli small red last" onClick="javascript:lastPage();location='#topmenu';return false;"><bean:message key='etiq.resource.anterior' /></button>	&nbsp;			
				<button type="button" title="<bean:message key='help.seguent'/>" class="butoMerli small red next" onClick="javascript:nextPage();location='#topmenu';return false;" <%=hidSeg%> id="b_seg" ><bean:message key='etiq.resource.seguent' /></button>		
			</div>
		</div>
	</div>
</html:form>


<%-- Calendari pels camps. --%>
<jsp:include page="/web/calendari.jsp">
	<jsp:param name="function" value="setDataFormat"/>
</jsp:include>		

<%-- Carrega descripcio llicencia --%>
<!-- script>
if(document.etiqForm.descDrets.value==""){
	document.etiqForm.llicencia.options[0].selected=true;
	document.etiqForm.descDrets.value=license[1];
}
</script-->

	<jsp:include page="peu_generalitat.jsp"></jsp:include>
</body>
</html:html>
