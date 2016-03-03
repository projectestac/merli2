<%@ include file="/web/taglibs.jsp"%>
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
		<div id="descGenC" class="opcions">
				<div title="<bean:message key='help.title'/>">
					<span class="camp"><bean:message key='etiq.title' /></span>
					<html:text property="titol" size="53" maxlength="250" onchange="javascript:posaReferencia();" />
				</div>
				<div title="<bean:message key='help.description'/>">
					<span class="camp"><bean:message key='etiq.description' /></span>
					<html:textarea property="descripcio" rows="6" cols="40" style="">
					</html:textarea>
				</div>
				<div title="<bean:message key='help.url'/>">
					<span class="camp"><bean:message key='etiq.url' /></span>
					<html:text property="url" maxlength="250" size="53" onfocus="javascript:document.etiqForm.url.select();" onchange="validateClear();" /> 
					<!-- Versio XTEC--> 
					<!--
						<A href="#" onclick="window.open(document.etiqForm.url.value);return false;" title="<bean:message key='help.validate'/>">
						?
						</A>
					--> 
					<!-- Versio N'ctl --> 
					<A id="validateUrl" href="#" onclick="validateUrl(document.etiqForm.url.value);return false;" title="<bean:message key='help.validate'/>"> <bean:message key='help.validate.button'/> </A>
				</div>
				
				<div title="<bean:message key='help.responsable'/>">
					<span class="camp"><bean:message key='etiq.responsable' /></span>
				<% 	edu.xtec.merli.segur.User u = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user"));
					if (u.isSuperuser() || 
						   	(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))){ 
				%>
					<html:text property="responsable" size="20" maxlength="8" />
				<%		
					}else{
				%>
					<html:text property="responsable" readonly="true" size="20" maxlength="8" />
				<%} %>
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
				<%if (u.isSuperuser() || 
					   	(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR))){ 
					if (Integer.parseInt(etiqForm.getEstat()) >= (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){%>
					<div title="<bean:message key='help.validador'/>">
						<span class="camp"><bean:message key='etiq.validador' /></span>
						<html:text property="validador" size="20" maxlength="8" />
					</div>
					<%} 
					if (Integer.parseInt(etiqForm.getEstat()) >= (edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT)){%>
					<div title="<bean:message key='help.corrector'/>">
						<span class="camp"><bean:message key='etiq.corrector' /></span>
						<html:text property="corrector" size="20" maxlength="8" />
					</div>
				<%}
				} %>
				<% if (u.isSuperuser()){%>
				<div title="<bean:message key='help.estat'/>">
						<span class="camp"><bean:message key='etiq.estat' /></span>
						<html:select property="estat">
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT %>" <%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_DENEGAT)){out.write(" selected");}%>><bean:message key='merli.estat.-2' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_RETORNAT)){out.write(" selected");}%>><bean:message key='merli.estat.-1' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_EN_PROCES)){out.write(" selected");}%>><bean:message key='merli.estat.0' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PENDENT %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_PENDENT)){out.write(" selected");}%>><bean:message key='merli.estat.10' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT)){out.write(" selected");}%>><bean:message key='merli.estat.2' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT)){out.write(" selected");}%>><bean:message key='merli.estat.2' />
							</option>
							<option value="<%=edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT %>"<%if (Integer.parseInt(etiqForm.getEstat()) == (edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT)){out.write(" selected");}%>><bean:message key='merli.estat.4' />
							</option>
						</html:select>
					</div>
				<%}else{ %>
					<html:hidden property="estat"></html:hidden>
				<%} %>
			</div>