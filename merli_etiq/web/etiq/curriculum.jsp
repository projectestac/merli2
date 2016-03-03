<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean2"%>
<%@ include file="/web/taglibs.jsp"%>
<div id="relCur">
	<div class="curriculum" id="divlevel">
		<div class="title">
			<bean:message key='application.level'/>
			<!-- <a href="<bean:message key='etiq.urlajuda.cur.level' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a>  -->
		</div>
		<div class="content" id="cLevel">

		</div>
	</div>

	<div class="curriculum" id="divarea">
		<div class="title">
			<bean:message key='application.area'/>
			<!-- <a href="<bean:message key='etiq.urlajuda.cur.area' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a> -->
		</div>
		<div class="content" id="cArea">

		</div>	
	</div>
	<div class="curriculum" id="divcontent">
		<div class="title">
			<bean:message key='application.content'/>
			<!-- <a href="<bean:message key='etiq.urlajuda.cur.content' />" title="Ajuda" target="_blank"><img src="web/images/ajuda.png" /></a> -->
		</div>
		<div class="content" id="cContent">

		</div>	
	</div>
<div id="down">
	<div class="curriculum" id="seleCur">	
		<div class="title">
			<bean:message key='application.descriptors.curriculum'/>
		</div>	
		<table>
			<colgroup>
				<col width="25%"/>
				<col width="40%"/>
				<col width="30%"/>
				<col width="5%"/>
				<col width="1%"/>
			</colgroup>
		<thead>
			<tr style="color:#AD2114; font-size:14px; background-color:#EBEBEB;">
				<td class="selecLevel"><bean:message key='application.level'/></td>
				<td class="selecArea"><bean:message key='application.area'/></td>
				<td><bean:message key='application.content'/></td>			
				<td></td>	
				<td></td>
			</tr>			
		</thead>
		<!--<div id="divBodySeleCur"> -->
		<tbody id="bodySeleCur">
		<logic:present name="etiqForm" property="contentCurriculum">
			<logic:iterate id="elem" name="etiqForm" property="contentCurriculum">
				<logic:present name="elem" property="idContent">
					<tr id="selcontent<bean2:write name="elem" property="idContent" />">
				</logic:present>
				<logic:notPresent name="elem" property="idContent">
					<tr id="selarea<bean2:write name="elem" property="idArea" />">
				</logic:notPresent>
				<td class="selecLevel">
					<a href=# 
						onclick="getCurriculumList(<bean2:write name="elem" property="idLevel" />,'level')">
						<bean2:write name="elem" property="level" />
					</a>
				</td>
				<td class="selecArea">
					<a href=# 
						onclick="getCurriculumList(<bean2:write name="elem" property="idLevel" />,'level');getCurriculumList(<bean2:write name="elem" property="idArea" />,'area')">
						<bean2:write name="elem" property="area" />
					</a>
				</td>
				<td class="selecContent">
					<a href=# 
						onclick="getCurriculumList(<bean2:write name="elem" property="idLevel" />,'level');getCurriculumList(<bean2:write name="elem" property="idArea" />,'area'),getCurriculumList(<bean2:write name="elem" property="idContent" />,'content')">
						<bean2:write name="elem" property="content" />
					</a>
				</td>
				<td class="selecContent">
					<a style="cursor:pointer"
						class="link" 
						onclick='javascript:var idContingut=new String("content<bean2:write name="elem" property="idContent" />"); if(idContingut=="content") idContingut="area<bean2:write name="elem" property="idArea" />";delCurriculumValue(idContingut);delSelecContent(idContingut);'>
							<img align="top" title="Esborra" alt="E" src="web/images/elimina.png" class="operacio">
					</a>
				</td>	
				<td></td>	
				</tr>
				</logic:iterate>
				</logic:present>
				<tr id="final_taula" style="height:30px; background-color:#EBEBEB;"><td></td><td></td><td></td><td></td><td></td></tr>
		
			</tbody>
		</table>		
	</div>
<%--
	<div class="curriculum" id="thesaurusCurriculum">
		<div class="title"><bean:message key='application.descriptors.etb' /></div>
		<div id="cosDUCETB">
			<UL id="termesContent">		
					<li id="termesContentBuit">
						<script>noTermesETBDUC="<bean:message key='application.descriptors.etb.buit'/>";</script>
						<bean:message key='application.descriptors.etb.buit'/>
					</li>
			</UL>
		</div>	
	</div>--%>
</div>
</div>
<span style="visibility:hidden;">text ajuda relacionada</span>