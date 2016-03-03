<%@ include file="/web/taglibs.jsp"%>
<jsp:useBean id="etiqForm" scope="request"
	class="edu.xtec.merli.etiquetador.EtiqForm" />
	<% 	edu.xtec.merli.segur.User u = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user"));%>
				
			<div id="descGenC" class="opcions">
			<span id="titleContent2">
				<bean:message key='etiq.title.contingut'/>
				<a href="<bean:message key='etiq.urlajuda.general.contingut' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"/></a>
			</span><br><br><br>
			
				<!-- DESCRIPCIO/RESUM -->
				<div title="<bean:message key='help.description'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.description' />
						<a href="<bean:message key='etiq.urlajuda.general.descripcio' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"  class="ajudacamp"/></a>
					</span>
					<html:textarea property="descripcio" rows="4" cols="114" onkeydown="comptadorLletres('etiqForm','descripcio')" onkeyup="comptadorLletres('etiqForm','descripcio')">
					</html:textarea>&nbsp;&nbsp;
					<!--  <INPUT title="<bean:message key='help.description.comptador'/>" name=result value=100 size="4" readonly="true" style="position:absolute;">-->
					<span title="<bean:message key='help.description.comptador'/>" id="comptador" style="position:absolute;"></span>
				</div>
				<br>
				<!-- AMBIT/DESTINATARIS -->
				<div title="<bean:message key='help.destinatari'/>">
					<span class="camp"> 
						<bean:message key='etiq.destinatari' />
						<a href="<bean:message key='etiq.urlajuda.general.destinataris' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"  class="ajudacamp"/></a>
					</span>
					<!-- 
					<PHTM:Select name="etiqForm" property="rolUser" label="labRolUser" 
						multiple="multiple" size="5" possibleValues="posRolUser" /> -->
					<select multiple="multiple" size="5" id="rolUserTots" style="float:left;">
					<% 
							for (int j=0; j<etiqForm.getLabRolUser().length; j++)
							{
								boolean trobat=false;
								for(int k=0; k<etiqForm.getRolUser().length && !trobat;k++)
								{
									if(etiqForm.getPosRolUser()[j].equals(etiqForm.getRolUser()[k])) trobat=true;
								}
								if(!trobat)
								{
								%>	
								<option value="<%= etiqForm.getPosRolUser()[j] %>"><%= etiqForm.getLabRolUser()[j] %></option>
							<%}}%>
					</select>
					<div style="width:18px;float:left;padding-top:20px;">
						<img src="web/images/fletxa_dreta.gif" onclick="selecciona('rolUserTots','rolUser','valor');"/>
						<br><br>
						<img src="web/images/fletxa_esq.gif" onclick="selecciona('rolUser','rolUserTots','valor');"/>
					</div>
					<select multiple="multiple" size="5" id="rolUser" name="rolUser">
						<% 
						if(etiqForm.getRolUser()!=null)
						for(int k=0; k<etiqForm.getRolUser().length;k++)
						{
							int pos=Integer.parseInt(etiqForm.getRolUser()[k])-1;
							%>	
							<option value="<%= etiqForm.getRolUser()[k] %>"><%= etiqForm.getLabRolUser()[pos] %></option>
						<%}%>
					</select>
				</div>
				<br>
				<!-- CONTEXT (vell) -->
				<div title="<bean:message key='help.context'/>">	
					<span class="camp">
						<bean:message key='etiq.context' />
						<a href=<bean:message key='etiq.urlajuda.general.context' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span> 
					<table>
						<tr>
							<td colspan="2">
							<table>
								<colgroup span="3" style="width:165px;"></colgroup>
								<tbody>
									<tr valign=top>
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
				<br>
				<!-- TIPUS -->
				<div title="<bean:message key='help.resourcetype'/>">
					<span class="camp">
						<bean:message key='etiq.resourcetype' />
						<a href=<bean:message key='etiq.urlajuda.general.tipus' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span><br>
					<div id="tipusRec">
						<PHTM:Multibox name="etiqForm" property="tipRec" label="labTipRec" possibleValues="posTipRec" breakLine="true" />
					</div>
				</div>
				<br>
				<!-- MARC/AMBIT -->
				<div title="<bean:message key='help.background'/>">
					<span class="camp">
						<bean:message key='etiq.background' />
						<a href=<bean:message key='etiq.urlajuda.general.marc' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span> 
					<select multiple="multiple" size="5" id="ambitTots"  style="float:left;">
					<% 
							for (int j=0; j<etiqForm.getLabAmbit().length; j++)
							{
								boolean trobat=false;
								for(int k=0; k<etiqForm.getAmbit().length && !trobat;k++)
								{
									if(etiqForm.getPosAmbit()[j].equals(etiqForm.getAmbit()[k])) trobat=true;
								}
								if(!trobat)
								{
								%>	
								<option value="<%= etiqForm.getPosAmbit()[j] %>"><%= etiqForm.getLabAmbit()[j] %></option>
							<%}}%>
					</select>
					<div  style="width:18px;float:left;padding-top:20px;">
						<img src="web/images/fletxa_dreta.gif" onclick="selecciona('ambitTots','ambit','no');"/>
						<br><br>
						<img src="web/images/fletxa_esq.gif" onclick="selecciona('ambit','ambitTots','no');"/>
					</div>
					<select multiple="multiple" size="5" id="ambit" name="ambit">
						<% 
						if(etiqForm.getAmbit()!=null)
						for(int k=0; k<etiqForm.getAmbit().length;k++)
						{
							int pos=0;
							for(int w=0; w<etiqForm.getPosAmbit().length;w++)
								if(etiqForm.getPosAmbit()[w].equals(etiqForm.getAmbit()[k])) pos=w;
							%>	
							<option value="<%= etiqForm.getAmbit()[k] %>"><%= etiqForm.getLabAmbit()[pos] %></option>
						<%}%>
					</select>
					<!-- <PHTM:Select name="etiqForm" property="ambit" label="labAmbit" possibleValues="posAmbit" multiple="multiple" size="5"/> -->
				</div>
				<br>
				<!-- CONTEXTUALITZACIO -->
				<div title="<bean:message key='help.context2'/>">
					<span class="camp">
						<bean:message key='etiq.context2' />
						<a href=<bean:message key='etiq.urlajuda.general.contextualitzacio' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:textarea property="context2" rows="2" cols="114" />
				</div>
				<br>
				<!-- RELACIONS -->
				<div id="c-idRelacions" title="<bean:message key='help.recRel'/>">
					<span class="camp">
						<bean:message key='etiq.tipusRel' />
						<a href=<bean:message key='etiq.urlajuda.general.relacio' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<div class="">
						<a title="<bean:message key='help.afegRecRel'/>" href="#" onclick="addRelacioRecurs();return false;">afegeix relació</a>
						
					</div>
					<% String idRelac = "c-idrelacions-copy"; 
					   String tirec = "";
					   String idrel = "";
					   String descrel = "";
					   String sStyle = ""; %>
						<% 
						int minum = 1;
						if (etiqForm.getRecRel()!=null && etiqForm.getRecRel().length>0){
							minum = etiqForm.getRecRel().length;
						}
						else	sStyle="style='color:#aaa';";
						for (int i=0; i<minum; i++){
							if (etiqForm.getRecRel()!=null && etiqForm.getRecRel().length>0){
								idrel = etiqForm.getRecRel()[i];
								tirec = etiqForm.getTipusRelSel()[i];
								descrel = etiqForm.getDescRel()[i];								
							}
						%>
					<div id="<%=idRelac%>" style="padding:0; margin:0;">
						<span class="camp">&nbsp;</span>
						<div>
							<select name="tipusRelSel" id="tipusRelSel" size="1" title="<bean:message key='help.recRel.tipus'/>" <%=sStyle%> onchange="colorSelect(this);">
							<% 
								if (etiqForm.getTipusRel()!=null)
							%>	
								<option <%=tirec.equals("")?"selected":""%> value="" style="color:#aaa;">Tipus de relació</option>
							<% 
								String labelRel="";
								for (int j=0; j<etiqForm.getTipusRel().length; j++){
									labelRel=etiqForm.getTipusRel()[j];
									if(etiqForm.getTipusRel()[j].indexOf("part")>0)
									{
										if(etiqForm.getTipusRel()[j].indexOf("de")>0)	labelRel="Forma part de";		//És part de
										else											labelRel="Inclou";				//Té part
									}
							%>	
									<option <%=(tirec.equals(etiqForm.getTipusRel()[j]))?"selected":""%> value="<%= etiqForm.getTipusRel()[j] %>" style="color:black;"><%= labelRel %></option>
							<%
								}
							%>
							</select>
							<input id="recRel" name="recRel" type="text" size="25" title="<bean:message key='help.recRel.id'/>" onkeypress="return disableEnterKey(event);" onkeydown="return noLetters(event)" value="<%=(!idrel.equals(""))?idrel:"Identificador recurs"%>" class="<%=(!idrel.equals(""))?"hintTextboxActive":"hintTextbox"%>">&nbsp;&nbsp;
							<input name="descRel" type="text" value="<%= descrel %>"  size="100" maxlength="1000" title="<bean:message key='help.recRel.desc'/>" onkeypress="return disableEnterKey(event);" style="display:none;" >
							<span title="<bean:message key='help.recRel.name'/>" onclick="javascript:mostraTitolRecRel(this);"  style="cursor:pointer;">   <img title="<bean:message key='help.recRel.name'/>" alt="<bean:message key='help.recRel.name'/>" src="web/images/veure_nom_recurs.png" class="operacio"></span>&nbsp;
							<span title="<bean:message key='help.elimRecRel'/>"  onclick="delRelacioRecurs(this, '<%=idRelac%>');"  style="cursor:pointer;"><img title="<bean:message key='help.elimRecRel' />" alt="<bean:message key='help.elimRecRel' />" src="web/images/elimina.png" class="operacio" style="height:10px;">&nbsp;</span>
							<span id="titRecRel"></span>
							</div>
					</div>
						<%idRelac="";
						}%>
				</div>
				
					
	<br><br><hr color="#DDD" width="95%"></hr><br><br>
				
				<span id="titleContent2">
					<bean:message key='etiq.title.aspectesformals'/>
					<a href="<bean:message key='etiq.urlajuda.general.aspectesformals' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png"/></a>
				</span><br><br><br>
				
				<!-- IDIOMES -->
				<div title="<bean:message key='help.language'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.language' />
						<a href=<bean:message key='etiq.urlajuda.general.idioma' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span> 
					<select multiple="multiple" size="5" id="llenguesTots"  style="float:left;">
							<% 
							for (int j=0; j<etiqForm.getLabLlengues().length; j++)
							{
								boolean trobat=false;
								for(int k=0; k<etiqForm.getLlengues().length && !trobat;k++)
								{
									if(etiqForm.getPosLlengues()[j].equals(etiqForm.getLlengues()[k])) trobat=true;
								}
								if(!trobat)
								{
								%>	
								<option value="<%= etiqForm.getPosLlengues()[j] %>"><%= etiqForm.getLabLlengues()[j] %></option>
							<%}}%>
					</select>
					<div style="width:18px;float:left;padding-top:20px;">
						<img src="web/images/fletxa_dreta.gif" onclick="selecciona('llenguesTots','llengues','no');"/>
						<br><br>
						<img src="web/images/fletxa_esq.gif" onclick="selecciona('llengues','llenguesTots','no');"/>
					</div>
					<select multiple="multiple" size="5" id="llengues" name="llengues">
						<% 
						if(etiqForm.getLlengues()!=null)
						for(int k=0; k<etiqForm.getLlengues().length;k++)
						{
							int pos=0;
							for(int w=0; w<etiqForm.getPosLlengues().length;w++)
								if(etiqForm.getPosLlengues()[w].equals(etiqForm.getLlengues()[k])) pos=w;
							%>	
							<option value="<%= etiqForm.getLlengues()[k] %>"><%= etiqForm.getLabLlengues()[pos] %></option>
						<%}%>
					</select>
					<!-- <PHTM:Select name="etiqForm"
							property="llengues" label="labLlengues" possibleValues="posLlengues"
							multiple="multiple" size="5" onchange="controlLlengues(this)" />  -->
				</div>
				<br>
				<!-- FORMAT -->
				<div title="<bean:message key='help.format'/>" class="mandatory">
					<span class="camp">
						<bean:message key='etiq.format' />
						<a href=<bean:message key='etiq.urlajuda.general.format' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<select multiple="multiple" size="5" id="formatTots" name="formatTots"  style="float:left;">
						<% 
						for (int j=0; j<etiqForm.getLabFormat().length; j++)
						{
							boolean trobat=false;
							for(int k=0; k<etiqForm.getFormat().length && !trobat;k++)
							{
								if(etiqForm.getPosFormat()[j].equals(etiqForm.getFormat()[k])) trobat=true;
							}
							if(!trobat)
							{
							%>	
							<option value="<%= etiqForm.getPosFormat()[j] %>"><%= etiqForm.getLabFormat()[j] %></option>
						<%}}%>
					</select>
					<div style="width:18px;float:left;padding-top:20px;"">
						<img src="web/images/fletxa_dreta.gif" onclick="selecciona('formatTots','format','alfabetic');"/>
						<br><br>
						<img src="web/images/fletxa_esq.gif" onclick="selecciona('format','formatTots','alfabetic');"/>
					</div>
					<select multiple="multiple" size="5" id="format" name="format">
						<% 
						if(etiqForm.getFormat()!=null)
						for(int k=0; k<etiqForm.getFormat().length;k++)
						{
							int pos=0;
							for(int w=0; w<etiqForm.getPosFormat().length;w++)
								if(etiqForm.getPosFormat()[w].equals(etiqForm.getFormat()[k])) pos=w;
							%>	
							<option value="<%= etiqForm.getFormat()[k] %>"><%= etiqForm.getLabFormat()[pos] %></option>
						<%}%>
					</select>
					<!-- <PHTM:Select name="etiqForm" property="format" label="labFormat" possibleValues="posFormat" 
					multiple="multiple" size="5" />  -->
				</div>
				<br>
				<!-- DURADA -->				
				<div title="<bean:message key='help.duration'/>">
					<span class="camp">
						<bean:message key='etiq.duration' />
						<a href=<bean:message key='etiq.urlajuda.general.durada' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span> 
					<bean:message key='etiq.duration.hour' />
					<html:text property="duraHora" maxlength="4" size="3" onkeydown="return noLetters(event)"/> &nbsp; 
					<bean:message key='etiq.duration.minute' /> 
					<html:text property="duraMin" maxlength="2" size="3" onkeydown="return noLetters(event)"/> &nbsp; 
					<bean:message key='etiq.duration.second' /> 
					<html:text property="duraSeg" maxlength="2" size="3" onkeydown="return noLetters(event)" />
				</div>
				<br>
				
				<% String esHidden2=""; if(etiqForm.getRecFisic()!=null && !etiqForm.getRecFisic().booleanValue())  esHidden2="style='display:none;'";%>
				<!-- CARACTERISTIQUES -->		
				<div id="c-caractRFisic" title="<bean:message key='help.caractRFisic'/>" <%=esHidden2 %> >
					<span class="camp">
						<bean:message key='etiq.caractRFisic' />
						<a href=<bean:message key='etiq.urlajuda.general.caracteristiques' /> title="Ajuda" target="_blank"><img src="web/images/ajuda.png" class="ajudacamp" /></a>
					</span>
					<html:textarea property="caractRFisic" rows="2" cols="114" /> 
				</div>	
					<!-- <div title="<bean:message key='help.age'/>">
					<span class="camp"><bean:message key='etiq.age'/> </span> 
					<bean:message key='etiq.age.from' /> 
					<html:text property="edatMin" maxlength="3" size="3" onchange="javascript:comprovarEdats();" /> &nbsp; 
					<bean:message key='etiq.age.to' /> 
					<html:text property="edatMax" maxlength="3" size="3" onchange="javascript:comprovarEdats();" />
				</div>
				<div title="<bean:message key='help.dificulty'/>">
					<span class="camp"><bean:message key='etiq.difficulty' /></span> 
					<PHTM:Select name="etiqForm" property="dificultat" label="labDificultat" possibleValues="posDificultat" />
				</div> -->
				
				
				<%--div title="<bean:message key='help.recRel'/>">
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
				</div--%>
		</div>