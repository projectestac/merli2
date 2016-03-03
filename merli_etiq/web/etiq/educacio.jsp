<%@ include file="/web/taglibs.jsp"%>
		<div id="descEduC" class="opcions">
				<div title="<bean:message key='help.destinatari'/>">
					<span class="camp"> <bean:message key='etiq.destinatari' /></span> 
					<PHTM:Select name="etiqForm" property="rolUser" label="labRolUser" 
						multiple="multiple" size="5" possibleValues="posRolUser" />
				</div>
				<!--<div title="<bean:message key='help.age'/>">
					<span class="camp"><bean:message key='etiq.context' /></span> 
					<bean:message key='etiq.age.from' /> 
					<html:text property="edatMin" maxlength="3" size="3" onchange="javascript:comprovarEdats();" /> &nbsp; 
					<bean:message key='etiq.age.to' /> 
					<html:text property="edatMax" maxlength="3" size="3" onchange="javascript:comprovarEdats();" />
				 	
				</div>
					-->
				<div>	
					<bean:message key='etiq.context' />
					<table>
						<tr>
							<td colspan="2">
							<!-- Linia treta. <hr> -->
							<table>
								<tbody>
									<tr>
										<td class="dades">
											<PHTM:Multibox name="etiqForm"
												property="context" label="labNivellSc"
												possibleValues="posNivellSc" breakLine="true" max="maxNivellSc"
												min="minNivellSc" onchange="javascript:afegirEdats(this);" />
										</td>
										<td class="dades">
											<PHTM:Multibox name="etiqForm"
												property="context" label="labNivellSp"
												possibleValues="posNivellSp" breakLine="true" max="maxNivellSp"
												min="minNivellSp" onchange="javascript:afegirEdats(this);" />
										</td>
										<td class="dades">
											<PHTM:Multibox name="etiqForm"
												property="context" label="labNivellAd"
												possibleValues="posNivellAd" breakLine="true" max="maxNivellAd"
												min="minNivellAd" onchange="javascript:afegirEdats(this);" />
										</td>
									</tr>
								</tbody>
							</table>							
							</td>
						</tr>
					</table><br/>
				</div>
	<!-- 		<div title="<bean:message key='help.age'/>">
					<span class="camp"><bean:message key='etiq.age'/> </span> 
					<bean:message key='etiq.age.from' /> 
					<html:text property="edatMin" maxlength="3" size="3" onchange="javascript:comprovarEdats();" /> &nbsp; 
					<bean:message key='etiq.age.to' /> 
					<html:text property="edatMax" maxlength="3" size="3" onchange="javascript:comprovarEdats();" />
				</div>  -->	
				<div title="<bean:message key='help.duration'/>">
					<span class="camp"><bean:message key='etiq.duration' /></span> 
					<bean:message key='etiq.duration.hour' />
					<html:text property="duraHora" maxlength="4" size="3" /> &nbsp; 
					<bean:message key='etiq.duration.minute' /> 
					<html:text property="duraMin" maxlength="2" size="3" />
					<bean:message key='etiq.duration.second' /> 
					<html:text property="duraSeg" maxlength="2" size="3" />
				</div>
			<!-- 	<div title="<bean:message key='help.dificulty'/>">
					<span class="camp"><bean:message key='etiq.difficulty' /></span> 
					<PHTM:Select name="etiqForm" property="dificultat" label="labDificultat" possibleValues="posDificultat" />
				</div>-->	
		</div>