
<%@ include file="/web/taglibs.jsp"%>
		<div id="edRec">
				<div title="<bean:message key='help.context2'/>">
					<span class="camp"><bean:message key='etiq.context2' /></span>
					<html:textarea property="context2" rows="2" cols="48" />
				</div>
				<br/>
				<div title="<bean:message key='help.editor'/>">
					<span class="camp"><bean:message key='etiq.editor' /></span>
					<html:text property="editor" size="20" /> 
				</div>	
				<br/>
				<div title="<bean:message key='help.recRel'/>">
				<table>
						<tr>
							<td><span class="camp"><bean:message key='etiq.tipusRel' /></span></td>
							<td><span class="camp"><bean:message key='etiq.recRel' /></span></td>
						</tr>
						<tr>
							<td>
								<PHTM:Select name="etiqForm"
									property="tipusRelSel" label="tipusRel"
									onchange="document.etiqForm.tipusRel.value=this.selectedIndex"
									possibleValues="tipusRel" 
									size="1"/>
							</td>
							<td valign="top"><html:text property="recRel" maxlength="50" size="20" /></td>
						</tr>
					</table>
				</div>
				<div title="<bean:message key='help.descRel'/>">
					<span class="camp"><bean:message key='etiq.descRel' /></span>
					<html:textarea property="descRel" rows="2" cols="48" style=""></html:textarea>
				</div>
				<br/>
				<div title="<bean:message key='help.recFisic'/>">
					<span class="camp"><bean:message key='etiq.recFisic' /></span> 
					<html:radio property="recFisic" value="true">
						<bean:message key='application.fisic' />
					</html:radio> 
					<html:radio property="recFisic" value="false">
						<bean:message key='application.online' />
					</html:radio>
				</div>
				<div title="<bean:message key='help.idFisic'/>">
					<table>
						<tr>
							<td><span class="camp"><bean:message key='etiq.tipusIdFisic' /></span></td>
							<td><span class="camp"><bean:message key='etiq.idFisic' /></span></td>
						</tr>
						<tr>
							<td>
								<PHTM:Select name="etiqForm"
									property="tipusIdFisicSel" label="tipusIdFisic"
									onchange="document.etiqForm.tipusIdFisic.value=this.selectedIndex"
									possibleValues="tipusIdFisic" 
									size="1"/>
							</td>
							<td valign="top"><html:text property="idFisic" size="20" /></td>
						</tr>
					</table>
				</div>	
				<div title="<bean:message key='help.caractRFisic'/>">
					<span class="camp"><bean:message key='etiq.caractRFisic' /></span>
					<html:textarea property="caractRFisic" rows="2" cols="48" /> 
				</div>	
		</div>
