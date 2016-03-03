<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean2"%>

	<div class="curriculum" id="divlevel">
		<div class="title"><bean:message key='application.level'/></div>
		<div class="content" id="cLevel">

		</div>
	</div>

	<div class="curriculum" id="divarea">
		<div class="title"><bean:message key='application.area'/></div>
		<div class="content" id="cArea">

		</div>	
	</div>
	<div class="curriculum" id="divcontent">
		<div class="title"><bean:message key='application.content'/></div>
		<div class="content" id="cContent">

		</div>	
	</div>
<div id="down">
	<div class="curriculum" id="seleCur">		
		<table>
		<thead>
			<tr>
			<td colspan="5" class="title">
				<bean:message key='application.descriptors.curriculum'/></td>
			</tr>
			<tr style="color:#AD2114; font-size:14px;" class="trmig">
				<td><bean:message key='application.level'/></td>
				<td><bean:message key='application.area'/></td>
				<td><bean:message key='application.content'/></td>			
				<td></td>	
				<td></td>
			</tr>			
		</thead>
		<!--<div id="divBodySeleCur"> -->
		<tbody id="bodySeleCur">
		<logic:present name="etiqForm" property="contentCurriculum">
			<logic:iterate id="elem" name="etiqForm" property="contentCurriculum">
				<tr id="selcontent<bean2:write name="elem" property="idContent" />">
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
					<a href="#" 
						class="link" 
						onclick="delCurriculumValue('content<bean2:write name="elem" property="idContent" />');delSelecContent('content<bean2:write name="elem" property="idContent" />')">
							<strong>X</strong>
					</a>
				</td>	
				</tr>
			</logic:iterate>
		</logic:present>
		</tbody>
		</table>		
	</div>
	<div class="curriculum" id="thesaurusCurriculum">
		<div class="title"><bean:message key='application.descriptors.etb' /></div>
		<div id="cosDUCETB">
			<UL id="termesContent">		
					<li id="termesContentBuit">
						<script>noTermesETBDUC="<bean:message key='application.descriptors.etb.buit'/>";</script>
						<bean:message key='application.descriptors.etb.buit'/>
					</li>
				<!-- <div id="termesContentBuit">
				</div> -->
			</UL>
		</div>	
	</div>
</div>