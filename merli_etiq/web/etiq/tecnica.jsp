<%@ include file="/web/taglibs.jsp"%>
			<div id="descTecC">
				<div title="<bean:message key='help.resourcetype'/>">
					<span class="camp"><bean:message key='etiq.resourcetype' /></span>
					<div id="barraEsq">
						<PHTM:Multibox name="etiqForm" property="tipRec" label="labTipRec" possibleValues="posTipRec" breakLine="true" />
					</div>
				</div>
				<div title="<bean:message key='help.format'/>">
					<span class="camp"><bean:message key='etiq.format' /></span> 
					<PHTM:Select name="etiqForm" property="format" label="labFormat" possibleValues="posFormat" 
					multiple="multiple" size="5" />
				</div>
				<div title="<bean:message key='help.language'/>">
					<span class="camp"><bean:message key='etiq.language' /></span> 
					<PHTM:Select name="etiqForm"
							property="llengues" label="labLlengues" possibleValues="posLlengues"
							multiple="multiple" size="5" onchange="controlLlengues(this)" />
				</div>
			</div>