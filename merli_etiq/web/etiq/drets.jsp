<%@ include file="/web/taglibs.jsp"%>
<%@ page import="edu.xtec.merli.Unitat"%>
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
<% 	edu.xtec.merli.segur.User u = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user")); %>	
		<div id="dretsC">
		<span id="titleContent2">
			<bean:message key='etiq.title.creditsrecurs'/>
			<a href="<bean:message key='etiq.urlajuda.credits.delrecurs' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"/></a>
		</span><br><br><br>
				<!-- AUTOR -->
				<div title="<bean:message key='help.author'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.author' />
						<a href="<bean:message key='etiq.urlajuda.credits.autors' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:text property="autor" size="64" maxlength="1000" onkeypress="return disableEnterKey(event);"/>
				</div>
				<!-- EDITOR -->
				<div title="<bean:message key='help.editor'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.editor' /> 
						<a href="<bean:message key='etiq.urlajuda.credits.editorial' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:text property="editor" size="64" maxlength="1000" onkeypress="return disableEnterKey(event);" /> 
				</div>	
				<!-- DATA -->
				<div title="<bean:message key='help.date'/>">
					<span class="camp">
						<bean:message key='etiq.date' />
						<a href="<bean:message key='etiq.urlajuda.credits.data' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:text property="data" size="20" onkeypress="return disableEnterKey(event);" /> 
					<a class="rodona" href="#"
						onclick="cal.select(document.etiqForm.data,'anchor1','MM/dd/yyyy'); return false;"
						NAME="anchor1" ID="anchor1"> 
						<span title="<bean:message key='help.calendar'/>">
							&nbsp;+&nbsp; 
						</span>
					</a>
				</div>	
				<!-- LLICENCIA -->
				<div title="<bean:message key='help.license'/>">
					<span class="camp">
						<bean:message key='etiq.license' />
						<a href="<bean:message key='etiq.urlajuda.credits.llicencia' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<PHTM:Select name="etiqForm"
						property="llicencia" label="labLlicencia"
						onchange="document.etiqForm.descDrets.value=license[this.value]"
						possibleValues="posLlicencia" />
				</div>
				<!-- DESCRIPCIO DRETS -->
				<div title="<bean:message key='help.rights.description'/>">
					<span class="camp">
						<bean:message key='etiq.rights.description' />
						<a href="<bean:message key='etiq.urlajuda.credits.descripciodrets' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span><html:textarea property="descDrets" rows="6" cols="48" style=""></html:textarea>
				</div>
				<!-- COST -->
				<div title="<bean:message key='help.cost'/>">
					<span class="camp">
						<bean:message key='etiq.cost' />
						<a href="<bean:message key='etiq.urlajuda.credits.cost' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<select name="cost" size="1" >
						<option <%=etiqForm.getCost().equals("2")?"selected":""%> value="2" %></option>
						<option <%=etiqForm.getCost().equals("0")?"selected":""%> value="0" %><bean:message key='application.gratuit' /></option>
						<option <%=etiqForm.getCost().equals("1")?"selected":""%> value="1" %><bean:message key='application.pagament' /></option>
					</select>
					<!-- <html:radio property="cost" value="0">
						<bean:message key='application.gratuit' />
					</html:radio> 
					<html:radio property="cost" value="1">
						<bean:message key='application.pagament' />
					</html:radio>-->
				</div>
				
	<br><br><hr color="#DDD" width="95%"></hr><br><br>
				
			<span id="titleContent2">
				<bean:message key='etiq.title.creditscatalogacio'/>
				<a href="<bean:message key='etiq.urlajuda.credits.catalogacio' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"/></a>
			</span><br><br><br>
				
				<!-- ESTAT -->
				<div title="<bean:message key='help.estat'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.estat' />
						<a href="<bean:message key='etiq.urlajuda.credits.estat' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<script>
						var usuari="<% out.print(u.getUser());%>";
						var perm='<% out.print((u.isSuperuser() || (u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR)))?"si":"no");%>';
					</script>
					<html:select property="estatSel" onchange="javascript:setEstat(usuari,perm);">
					<% if (u.isSuperuser()){%>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PENDENT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_PENDENT)){out.write(" selected");}%>><bean:message key='merli.estat.10' />
						</option>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){out.write(" selected");}%>><bean:message key='merli.estat.0' />
						</option>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT)){out.write(" selected");}%>><bean:message key='merli.estat.2' />
						</option>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT)){out.write(" selected");}%>><bean:message key='merli.estat.-1' />
						</option>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT %>" <%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT)){out.write(" selected");}%>><bean:message key='merli.estat.-2' />
						</option>
						<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT)){out.write(" selected");}%>><bean:message key='merli.estat.4' />
						</option>
					<%}
					else
					{
						if(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PENDEDIT))
						{%>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PENDENT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_PENDENT)){out.write(" selected");}%>><bean:message key='merli.estat.10' />
							</option>
					<%	}
						if(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))
						{	%>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){out.write(" selected");}%>><bean:message key='merli.estat.0' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT)){out.write(" selected");}%>><bean:message key='merli.estat.-1' />
							</option>	
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT %>" <%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT)){out.write(" selected");}%>><bean:message key='merli.estat.-2' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT)){out.write(" selected");}%>><bean:message key='merli.estat.4' />
							</option>	
					<%	}
						else
						{ %>
								<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){out.write(" selected");}%>><bean:message key='merli.estat.0' />
								</option>
								<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT %>"<%if (Integer.parseInt(etiqForm.getEstatSel()[0]) == (edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT)){out.write(" selected");}%>><bean:message key='merli.estat.2' />
								</option>	
					<%	}
					} 	%>
					</html:select>
				</div>
				<!-- CATALOGADOR -->
				<div title="<bean:message key='help.responsable'/>" class="mandatory" id="capaResp">
					<span class="camp">
						<bean:message key='etiq.responsable' />
						<a href="<bean:message key='etiq.urlajuda.credits.catalogacio' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<% 	if (u.isSuperuser() || 
							   	(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))){ 
					%>
						<html:text property="responsable" size="20" maxlength="10" onkeypress="return disableEnterKey(event);"/>
					<%		
						}else{
					%>
						<html:text property="responsable" readonly="true" size="20" maxlength="10" onkeypress="return disableEnterKey(event);"/>
					<%} %>
					<span><%=etiqForm.getDataResponsable()%></span>
				</div>
				<!-- REVISAT PER/VALIDADOR -->
				<%if (u.isSuperuser() || 
					   	(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))){ 
					if (Integer.parseInt(etiqForm.getEstatSel()[0]) >= (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){%>
				<div title="<bean:message key='help.validador'/>" id="capaVal">
						<span class="camp">
							<bean:message key='etiq.validador' />
							<a href="<bean:message key='etiq.urlajuda.credits.revisio' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
						</span>
						<html:text property="validador" size="20" maxlength="10" onkeypress="return disableEnterKey(event);"/>
						<span><%=etiqForm.getDataValidador()%></span>
					</div>
					<%} 
				} %>
				<!-- VERSIO -->
				<div title="<bean:message key='help.version'/>">
					<span class="camp">
						<bean:message key='etiq.version' />
						<a href="<bean:message key='etiq.urlajuda.credits.versio' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:text property="versio" maxlength="50" size="20" onkeypress="return disableEnterKey(event);"/>
				</div>
				
				<!-- DISPONIBILITAT -->
				<% String esHidden3="";				
					if(!u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.RECFISIC) || (etiqForm.getRecFisic()!=null && !etiqForm.getRecFisic().booleanValue()))
						esHidden3="style='display:none;'";
					else if (etiqForm.getOperacio().equals("newRec"))
					{
						if(u.getUnitat()!=null)
						{
							String[] a={String.valueOf(u.getUnitat().intValue())};
							etiqForm.setUnitats(a);	
						}
					}
				%>
				<div class="" id="c-disp" title="<bean:message key='help.unitats'/>" <%=esHidden3%> >
					<span class="camp">
						<bean:message key='etiq.unitats' />
						<a href="<bean:message key='etiq.urlajuda.credits.disponibilitat' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<select multiple="multiple" size="5" id="unitatsTots" style="width:400px; float:left;">
						<% 
						for (int j=0; j<etiqForm.getLabUnitats().length; j++)
						{
							boolean trobat=false;
							for(int k=0; k<etiqForm.getUnitats().length && !trobat;k++)
								if(etiqForm.getPosUnitats()[j].equals(etiqForm.getUnitats()[k])) trobat=true;
							if(!trobat)
							{
							%>	
							<option value="<%= etiqForm.getPosUnitats()[j] %>"><%= etiqForm.getLabUnitats()[j] %></option>
						<%}}%>
					</select>
					<div style="width:18px;float:left;margin-top:20px;">
						<img src="web/images/fletxa_dreta.gif" onclick="selecciona('unitatsTots','unitats','alfabetic');"/>
						<br><br>
						<img src="web/images/fletxa_esq.gif" onclick="selecciona('unitats','unitatsTots','alfabetic');"/>
					</div>
					<select multiple="multiple" size="5" id="unitats" name="unitats" style="width:400px;">
						<% 
						if(etiqForm.getUnitats()!=null)
						for(int k=0; k<etiqForm.getUnitats().length;k++)
						{
							int pos=0;
							for(int w=0; w<etiqForm.getPosUnitats().length;w++)
								if(etiqForm.getPosUnitats()[w].equals(etiqForm.getUnitats()[k])) pos=w;
							%>	
							<option value="<%= etiqForm.getUnitats()[k] %>"><%= etiqForm.getLabUnitats()[pos] %></option>
						<%}%>
					</select>
				</div>
				
				<%if (u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.AGREGA)){ %>
					<div title="<bean:message key='help.agrega'/>">
						<span class="camp"><bean:message key='etiq.agrega' /></span>
						<%if (etiqForm.getAgregaDate()!="" && etiqForm.getAgregaDate()!="null"){%>
							<bean:message key='etiq.agrega.fet' /> <%= etiqForm.getAgregaDate()%>.
						<% }else{ %>
							<html:select property="agrega">
								<option value="0" <%if ("0".equals(etiqForm.getAgrega())){out.write(" selected");}%>><bean:message key='etiq.agrega.no' />
								</option>
								<option value="1" <%if ("1".equals(etiqForm.getAgrega())){out.write(" selected");}%>><bean:message key='etiq.agrega.si' />
								</option>
							</html:select>
						<%}%>
					</div>
				<%}%>
				
			<!-- 	<% if (etiqForm.getNu() != null && !"".equals(etiqForm.getNu())) {%>
				<div title="<bean:message key='help.nu'/>">
					<span class="camp"><bean:message key='etiq.nu' />:</span>
					<span class="valor"><%= etiqForm.getNu()%></span>
				</div>
				<% } %>
				<%if (u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.AGREGA)){ %>
					<div title="<bean:message key='help.agrega'/>">
						<span class="camp"><bean:message key='etiq.agrega' /></span>
						<%if (etiqForm.getAgregaDate()!="" && etiqForm.getAgregaDate()!="null"){%>
							<bean:message key='etiq.agrega.fet' /> <%= etiqForm.getAgregaDate()%>.
						<% }else{ %>
							<html:select property="agrega">
								<option value="0" <%if ("0".equals(etiqForm.getAgrega())){out.write(" selected");}%>><bean:message key='etiq.agrega.no' />
								</option>
								<option value="1" <%if ("1".equals(etiqForm.getAgrega())){out.write(" selected");}%>><bean:message key='etiq.agrega.si' />
								</option>
							</html:select>
						<%}%>
					</div>
				<%}%> -->
		</div>