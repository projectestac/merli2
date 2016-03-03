<%@ page import="edu.xtec.merli.MerliBean, edu.xtec.merli.segur.User,edu.xtec.merli.segur.operations.MerliOperations"%>
<%@ include file="/web/taglibs.jsp"%>
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
		<div id="identiRec" class="opcions">
				<div title="<bean:message key='help.title'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.title' />
						<a href="<bean:message key='etiq.urlajuda.id.titol' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp"/></a>
					</span>
					<html:text property="titol" size="53" maxlength="950" onchange="javascript:posaReferencia();" onkeypress="return disableEnterKey(event)"/>
				</div>
				<%
				boolean tePermis = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.MerliOperations.RECFISIC); 
				if(!tePermis) etiqForm.setRecFisic(new Boolean(false));
				//((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR)
				 %>
				<div title="<bean:message key='help.recFisic'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.recFisic' />
						<a href="<bean:message key='etiq.urlajuda.id.fisiconline' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"  class="ajudacamp" /></a>
					</span>
					<html:radio property="recFisic" value="false" onclick="setRecursFisic(false);" style="border:0px;">
						<bean:message key='application.online' />
					</html:radio>
					<%
					if(!tePermis)
					{
					 %>
					<html:radio property="recFisic" value="true" onclick="setRecursFisic(true);" disabled="true" style="border:0px;">
						<bean:message key='application.fisic' />
					</html:radio> 
					<%}
					else
					{ %>
					<html:radio property="recFisic" value="true" onclick="setRecursFisic(true);" style="border:0px;">
						<bean:message key='application.fisic' />
					</html:radio> 
					<%} %>
				</div>
				
				<% String esMandatory=""; if(etiqForm.getRecFisic()!=null && !etiqForm.getRecFisic().booleanValue()) esMandatory="class='mandatory'"; %>
				<div id="c-url" title="<bean:message key='help.url'/>" <%=esMandatory %> >
					<span class="camp">
						<bean:message key='etiq.url' />
						<a href="<bean:message key='etiq.urlajuda.id.url' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"  class="ajudacamp"/></a>
					</span>
					<html:text property="url" maxlength="950" size="53" onfocus="javascript:document.etiqForm.url.select();"  onkeypress="return disableEnterKey(event)"  onchange="validateClear();" /> 
					<!-- Versio XTEC--> 
					<!--
						<A href="#" onclick="window.open(document.etiqForm.url.value);return false;" title="<bean:message key='help.validate'/>">
						?
						</A>
					--> 
					<!-- Versio N'ctl --> 
					<span id="validateUrl" title="<bean:message key='help.validate'/>">  </span>
					<!-- <A id="validateUrl" href="#" onclick="validateUrl(document.etiqForm.url.value);return false;" title="<bean:message key='help.validate'/>"> <bean:message key='help.validate.button'/> </A>
					<A id="comprova2" href="#" onclick="comprova(document.etiqForm.titol.value,document.etiqForm.url.value);return false;" title="<bean:message key='help.comprova'/>"> <bean:message key='help.comprova.button'/> </A> -->
			
				</div>
				<% String esHidden="";	if(esMandatory.length()>0) esHidden="style='display:none;'";//si es recurs online
				%>
				
				<div id="c-idFisic" title="<bean:message key='help.idFisic'/>" <%=esHidden %> >
					<span class="camp">
						<bean:message key='etiq.idFisic' />
						<a href="<bean:message key='etiq.urlajuda.id.idfisic' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"  class="ajudacamp"/></a>
					</span>
					<div class="">
						<a href="#" title="<bean:message key='help.afegIdFisic' />" onclick="addIdentificadorFisic();return false;">afegeix identificador</a>
					</div>
					<% 
						String idCopy = "c-idfisic-copy"; 
					   String tipus = "";
					   String idfisic = ""; %>
						<% 
						int mineles = 1;
						if (etiqForm.getIdFisic()!=null && etiqForm.getIdFisic().length>0){
							mineles = etiqForm.getIdFisic().length;
						}
						for (int i=0; i<mineles; i++){
							if (etiqForm.getIdFisic()!=null && etiqForm.getIdFisic().length>0){
								idfisic = etiqForm.getIdFisic()[i];
								tipus = etiqForm.getTipusIdFisicSel()[i];								
							}
						%>
					<div id="<%=idCopy%>" style="padding:0; margin:0;">
						<span class="camp">&nbsp;</span>
						<div>
							<select name="tipusIdFisicSel" size="1" >
							<% 
								if (etiqForm.getTipusIdFisic()!=null)
								for (int j=0; j<etiqForm.getTipusIdFisic().length; j++){
								%>	
									<option <%=(tipus.equals(etiqForm.getTipusIdFisic()[j]))?"selected":""%> value="<%= etiqForm.getTipusIdFisic()[j] %>"><%= etiqForm.getTipusIdFisic()[j] %></option>
								<%
								}
							%>
							</select>
							<input name="idFisic" type="text" value="<%= idfisic %>" size="20" maxlength="95"  onkeypress="return disableEnterKey(event)">&nbsp;&nbsp;
							<span title="<bean:message key='help.elimIdFisic' />" onclick="delIdentificadoFisic(this, '<%=idCopy%>');" style="cursor:pointer;"><img title="<bean:message key='help.elimIdFisic' />" alt="<bean:message key='help.elimIdFisic' />" src="web/images/elimina.png" class="operacio"></span>
						</div>
					</div>
						<%idCopy="";
						}
				%> 
				</div>
				<div id="comprova"></div>
			<!-- <div id="waitingComprova" style="float:right;width:30%;display:none;">
				<bean:message key='merli.waiting'/>
				<br/><BR/>
				<IMG src="web/images/activIndica.gif"/>
			</div> -->
		</div>