	<%@ include file="/web/taglibs.jsp"%>
	<script type="text/javascript" src="web/JS/inforec.js"></script>
	
			<div id="titolRec" class="missatgeUser">
				<div class="title">Corregeix el títol del recurs:</div>
					<div>
						<span class="camp"><bean:message key='etiq.title' /> <a href="https://sites.google.com/a/xtec.cat/merli/" title="Ajuda"><img src="web/images/ajuda.png" /></a></span>
						<html:text property="title" size="50" maxlength="250" onchange="" />
						<br/>
					</div>
					<div>
					
						<div class="itemOperacions">
							<a href="#" onclick="javascript:corregir('titolRec',document.veuRecForm.title.value);">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								accepta
							</a>
							<a href="#" onclick="javascript:cancel('titolRec');">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								cancel·la
							</a>
						</div>			
					</div>			
			</div>
			<div id="descripcio" class="missatgeUser">
					<div class="title">Corregeix la descripció del recurs:</div>
					<div>
						<span class="camp">
							<bean:message key='etiq.description' />
						</span>
							<html:textarea property="description" rows="6" cols="40" style="">
							</html:textarea>
							<br/>
					</div>
					<div>
						<div class="itemOperacions">
							<a href="#" onclick="javascript:corregir('descripcio',document.veuRecForm.description.value);">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								accepta
							</a>
							<a href="#" onclick="javascript:cancel('descripcio');">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								cancel·la
							</a>
						</div>			
					</div>			
			</div>
			<div id="rights" class="missatgeUser">
					<div class="title">Corregeix la descripció del drets del recurs:</div>
					<div>
						<span class="camp">
							<bean:message key='etiq.rights.description' /> <a href="https://sites.google.com/a/xtec.cat/merli/" title="Ajuda"><img src="web/images/ajuda.png" /></a>
						</span> 
						<html:textarea property="rightsDesc" rows="6" cols="40" style="">
						</html:textarea>
						<br/>
					</div>
					<div>
						<div class="itemOperacions">
							<a href="#" onclick="javascript:corregir('rights',document.veuRecForm.rightsDesc.value);">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								accepta
							</a>
							<a href="#" onclick="javascript:cancel('rights');">
								<img class="operacio" src="web/images/esborrar.png" alt="E">
								cancel·la
							</a>
						</div>			
					</div>			
			</div>
