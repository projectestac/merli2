<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean2"%>
<%@ page import="edu.xtec.merli.utils.Utility, java.util.ArrayList"%>
<%@ include file="/web/taglibs.jsp"%>
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
<div id="parClau">
				
				<div id="buscaTermes">
					<SPAN style="vertical-align: middle;">
						<!-- <IMG src="web/images/lupa.png" />  -->
						<div class="cercador_thesaurus"><bean:message key="etiq.search.words" /></div>
						<INPUT id="buscaTermesInput" type="text" onchange="idTerme=null;" onBlur="if (!show)document.getElementById('resultTermes').style.display='none';"
								onkeypress="return disableEnterKey(event);"
								autocomplete="off" onkeyup="doCompletion();" name="consulta"
								title="<bean:message key='help.search.words'/>" style="position:relative;float:left;"/> 
						<span id="addGo"> 
							<div class="cercador_thesaurus">
								<A id="a_addGo" onclick="addGeneral('');" title="<bean:message key='help.search.add'/>"  style="color:white; cursor:pointer;" >
									<bean:message key='etiq.search.add' />
								</A>
								<A id="a_showGo" class="a_showGo_off" onclick="navigateTo(idTerme);" title="<bean:message key='help.search.go'/>">
									<bean:message key='etiq.search.go' />
								</A> 
							</div>
						</span> 
					</SPAN>
				</div>
				<div id="formThesaure">
					<div id="microThes">
						<div id="titMicroThes"><bean:message key="etiq.microthesaurus" /></div>						
						<div id="listThes">
						</div>
					</div>
					<div id="resultTermes" onmouseover="show=true;" onmouseout="show=false;">
					</div>
				<PHTM:thesaurus name="etiqForm" property="selecTerm" label="selecLabel" />

				<div id="paraules" class="thesaurus"> 
					<!-- <a href="<bean:message key='etiq.urlajuda.tes.lliures' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a> -->
					<bean:message key="etiq.paraulesobertes" />:&nbsp;
					<%
					if (etiqForm.getSelecParaulaId()!=null && etiqForm.getSelecParaula()!=null){
						ArrayList lProp = Utility.toList(etiqForm.getSelecParaulaId(),";");
						ArrayList lLab = Utility.toList(etiqForm.getSelecParaula(),";");				
					  	for (int i = 0; i< lProp.size(); i++){
					%>
						<span class="keyValue" id="paraulaValue<%=lProp.get(i)%>">
						  	<a 	class="paraulaoberta"
						  		id="paraula<%=lProp.get(i)%>">
					<%
							  if (lLab.get(i) != null){
								  %><%=Utility.xmlEncode((String) lLab.get(i))%><%
							  }else{
								  %> term<%=lProp.get(i)%><%
							  }%>
						   	</a>
						   	
						   	<a href="#"
								onclick="delParaula(<%=lProp.get(i)%>);"
							  	id="delParaula<%=lProp.get(i)%>">
							  <img src="web/images/elimina.png" title="Elimina la paraula <%=Utility.xmlEncode((String) lLab.get(i))%>" alt="Elimina el terme <%=Utility.xmlEncode((String) lLab.get(i))%>"  />
						    </a>&nbsp;
						</span>
					<% 	} 
					}%>
				</div>
				
					<!--<div id="path" class="thesaurus">
						 Crear un fil d'ariadna 
						<span id="parClauActual"></span>
					</div>-->
					<!--<div id="thesaurus" class="thesaurus"></div>
					 <div id="keys" class="thesaurus"></div> -->
				</div>
			</div>