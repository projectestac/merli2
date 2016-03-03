	<%@ include file="/web/taglibs.jsp"%>
	<script type="text/javascript" src="web/JS/inforec.js"></script>
	<jsp:useBean id="veuRecForm"  scope="request" class="edu.xtec.merli.gestorrec.VeuRecursForm"/>
			
			<div id="titolRecTrad" class="missatgeUser" style="padding-bottom: 20px;">
				<div class="title" title="<bean:message key='help.merli.op.traduct.title'/>" style="background-color:#AD2114; margin: 0px; -moz-border-radius:5px 5px 0px 0px; -webkit-border-radius: 5px 5px 0px 0px; padding: 10px;">
					<bean:message key='merli.op.traduct.title.trad'/>
				</div>
				
				<div id="titleProperty">
					<p>
						<bean:message key='merli.op.traduct.title.ca'/>	<br>
						<input type="text" id="tit" value="<%= veuRecForm.getTitle()%>"  style="width:299px; color:grey; border-color: #AAA;" maxlength="950" />
					</p>
				</div>
					
				<div id="idiomesT" style="position:absolute;left:237px;top: 89px; width:100px;">
					<a id="titCa" href="#" class="selec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.title.ca'/>','title',document.veuRecForm.title.value,'titCa','titEs','titEn','titOc');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
						<bean:message key='merli.op.traduct.lang.ca'/>
					</a>
					<a id="titEs" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.title.es'/>','titleEs',document.veuRecForm.titleEs.value,'titCa','titEs','titEn','titOc');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
						<bean:message key='merli.op.traduct.lang.es'/>
					</a>					
					<a id="titEn" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.title.en'/>','titleEn',document.veuRecForm.titleEn.value,'titCa','titEs','titEn','titOc');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
						<bean:message key='merli.op.traduct.lang.en'/>
					</a>						
					<a id="titOc" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.title.oc'/>','titleOc',document.veuRecForm.titleOc.value,'titCa','titEs','titEn','titOc');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
						<bean:message key='merli.op.traduct.lang.oc'/>
					</a>		 						 
				</div>
				<br><br>
				
				<div id="traduccioTitleCa">	
					<p>				
						<bean:message key='merli.op.traduct.ca'/>
						<html:text property="title" style="width:299px; color:grey; border-color: #AAA;" maxlength="950" onchange=""/>
					</p>
					<br/>
				</div>					
				<div id="traduccioTitleEs">					
					<p>
						<bean:message key='merli.op.traduct.es'/>
						<html:text property="titleEs" style="width:299px; color:grey; border-color: #AAA;" maxlength="950" onchange="" styleId="titleEs"/>
					</p>
					<br/>
				</div>					
				<div id="traduccioTitleEn">		
					<p>		
						<bean:message key='merli.op.traduct.en'/>
						<html:text property="titleEn" style="width:299px; color:grey; border-color: #AAA;" maxlength="950" onchange="" />
					</p>
					<br/>				
				</div>				
				<div id="traduccioTitleOc">		
					<p>		
						<bean:message key='merli.op.traduct.oc'/>
						<html:text property="titleOc" style="width:299px; color:grey; border-color: #AAA;" maxlength="950" onchange="" />
					</p>
					<br/>				
				</div>
				
				<div id="idiomesTitle" style="position:absolute;left:237px;top: 184px; width:100px;">	
					<div id="idiomesTraduct">
						<a id="traductTitleCa" class="noselec" href="#" onclick="tradOperation('traduccioFinalCa','Title');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
							<bean:message key='merli.op.traduct.lang.ca'/>
						</a>
						<a id="traductTitleEs" class="selec" href="#" onclick="tradOperation('traduccioFinalEs','Title');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
							<bean:message key='merli.op.traduct.lang.es'/>
						</a>
						<a id="traductTitleEn" class="noselec" href="#" onclick="tradOperation('traduccioFinalEn','Title');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
							<bean:message key='merli.op.traduct.lang.en'/>
						</a>
						<a id="traductTitleOc" class="noselec" href="#" onclick="tradOperation('traduccioFinalOc','Title');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
							<bean:message key='merli.op.traduct.lang.oc'/>
						</a>			 						 
					</div>				
				</div>
				<div>					
					<div class="itemOperacions">
						<button type="button" class="butoMerli small red" onclick="javascript:traduir('titolRecTrad');" title="<bean:message key='help.merli.op.acceptar'/>"><img src="web/images/acceptar_boto.png" style="height:10px;">&nbsp;<bean:message key='merli.op.acceptar'/></button>	&nbsp;			
						<button type="button" class="butoMerli small red"  onclick="javascript:cancel('titolRecTrad');" title="<bean:message key='help.merli.op.cancelar'/>"><img src="web/images/cancelar_boto.png">&nbsp;<bean:message key='merli.op.cancelar'/></button>
					</div>			
				</div>													
			</div>
			
			<div id="descripcioTrad" class="missatgeUser" style="padding-bottom: 20px;">
				<div class="title" title="<bean:message key='help.merli.op.traduct.description'/>"  style="background-color:#AD2114; padding: 10px; margin: 0px; -moz-border-radius:5px 5px 0px 0px;-webkit-border-radius: 5px 5px 0px 0px;">
					<bean:message key='merli.op.traduct.descripcio.trad'/>
				</div>
				
				<div id="descProperty">
					<p>
						<bean:message key='merli.op.traduct.descripcio.ca'/>	<br>
						<input type="text" id="desc" value="<%= veuRecForm.getDescription()%>" style="width:299px; color:grey; border-color: #AAA;" maxlength="2000" />
					</p>
				</div>
					
				<div id="idiomesDG" style="position:absolute;left:237px;top: 89px; width:100px;">
						<a id="descCa" href="#" class="selec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.ca'/>','description',document.veuRecForm.description.value,'descCa','descEs','descEn','descOc');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
							<bean:message key='merli.op.traduct.lang.ca'/>
						</a>
						<a id="descEs" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.es'/>','descriptionEs',document.veuRecForm.descriptionEs.value,'descCa','descEs','descEn','descOc');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
							<bean:message key='merli.op.traduct.lang.es'/>
						</a>
						<a id="descEn" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.en'/>','descriptionEn',document.veuRecForm.descriptionEn.value,'descCa','descEs','descEn','descOc');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
							<bean:message key='merli.op.traduct.lang.en'/>
						</a>
						<a id="descOc" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.oc'/>','descriptionOc',document.veuRecForm.descriptionOc.value,'descCa','descEs','descEn','descOc');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
							<bean:message key='merli.op.traduct.lang.oc'/>
						</a>				 						 
				</div>
				<div id="traduccioDescCa">					
					<p>		
						<bean:message key='merli.op.traduct.ca'/>
						<html:textarea property="description" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>
					<br/>
				</div>
				
				<div id="traduccioDescEs">					
					<p>
						<bean:message key='merli.op.traduct.es'/>
						<html:textarea property="descriptionEs" style="color:grey; border-color: #AAA; width:299px; height:108px;" styleId="descriptionEs"></html:textarea>
					</p>
					<br/>
				</div>	
				
				<div id="traduccioDescEn">					
					<p>
						<bean:message key='merli.op.traduct.en'/>
						<html:textarea property="descriptionEn" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>
					<br/>
				</div>	
					
				<div id="traduccioDescOc">					
					<p>
						<bean:message key='merli.op.traduct.oc'/>
						<html:textarea property="descriptionOc" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>
					<br/>
				</div>					

				<div id="idiomesDesc" style="position:absolute;left:237px;top: 231px; width:100px;">
					<div id="idiomesTraduct">
						<a id="traductDescCa" href="#" class="noselec" onclick="tradOperation('traduccioFinalCa','Desc');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
							<bean:message key='merli.op.traduct.lang.ca'/>
						</a>
						<a id="traductDescEs" href="#" class="selec" onclick="tradOperation('traduccioFinalEs','Desc');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
							<bean:message key='merli.op.traduct.lang.es'/>
						</a>
						<a id="traductDescEn" href="#" class="noselec" onclick="tradOperation('traduccioFinalEn','Desc');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
							<bean:message key='merli.op.traduct.lang.en'/>
						</a>
						<a id="traductDescOc" href="#" class="noselec" onclick="tradOperation('traduccioFinalOc','Desc');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
							<bean:message key='merli.op.traduct.lang.oc'/>
						</a>			 						 
					</div>
				</div>		
					
				<div>					
					<div class="itemOperacions">
						<button type="button" class="butoMerli small red" onclick="javascript:traduir('descripcioTrad');" title="<bean:message key='help.merli.op.acceptar'/>"><img src="web/images/acceptar_boto.png" style="height:10px;">&nbsp;<bean:message key='merli.op.acceptar'/></button>				
						<button type="button" class="butoMerli small red"  onclick="javascript:cancel('descripcioTrad');" title="<bean:message key='help.merli.op.cancelar'/>"><img src="web/images/cancelar_boto.png">&nbsp;<bean:message key='merli.op.cancelar'/></button>
					</div>			
				</div>							
			</div>

			<div id="rightsTrad" class="missatgeUser" style="padding-bottom: 20px;">
				<div class="title" title="<bean:message key='help.merli.op.traduct.rights'/>" style="background-color:#AD2114; padding: 10px; margin: 0px; -moz-border-radius:5px 5px 0px 0px; -webkit-border-radius: 5px 5px 0px 0px;">
					<bean:message key='merli.op.traduct.rights.trad'/>
				</div>			
				<div id="rightsProperty">
					<p>
						<bean:message key='merli.op.traduct.rights.ca'/>	<br>
						<input type="text" id="right" value="<%= veuRecForm.getRightsDesc()%>" style="width:299px; color:grey; border-color: #AAA;" maxlength="2000" />
					</p>
				</div>
				<div id="idiomesDD" style="position:absolute;left:237px;top: 89px; width:100px;">
						<a id="rightCa" href="#" class="selec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.ca'/>','rightsDesc',document.veuRecForm.rightsDesc.value,'rightCa','rightEs','rightEn','rightOc');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
							<bean:message key='merli.op.traduct.lang.ca'/>
						</a>
						<a id="rightEs" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.es'/>','rightsEs',document.veuRecForm.rightsEs.value,'rightCa','rightEs','rightEn','rightOc');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
							<bean:message key='merli.op.traduct.lang.es'/>
						</a>
						<a id="rightEn" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.en'/>','rightsEn',document.veuRecForm.rightsEn.value,'rightCa','rightEs','rightEn','rightOc');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
							<bean:message key='merli.op.traduct.lang.en'/>
						</a>
						<a id="rightOc" href="#" class="noselec" onclick="changeTraduccio('<bean:message key='merli.op.traduct.descripcio.oc'/>','rightsOc',document.veuRecForm.rightsOc.value,'rightCa','rightEs','rightEn','rightOc');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
							<bean:message key='merli.op.traduct.lang.oc'/>
						</a>			 						 
				</div>
				
				<div id="traduccioRightCa">					
					<p>
						<bean:message key='merli.op.traduct.ca'/>
						<html:textarea property="rightsDesc" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>
					<br/>
				</div>
				<div id="traduccioRightEs">	
					<p>
						<bean:message key='merli.op.traduct.es'/>
						<html:textarea property="rightsEs" style="color:grey; border-color: #AAA; width:299px; height:108px;" styleId="rightsEs"></html:textarea>
					</p>				
					<br/>
				</div>	
				<div id="traduccioRightEn">		
					<p>
						<bean:message key='merli.op.traduct.en'/>
						<html:textarea property="rightsEn" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>				
					<br/>
				</div>
				<div id="traduccioRightOc">	
					<p>
						<bean:message key='merli.op.traduct.oc'/>
						<html:textarea property="rightsOc" style="color:grey; border-color: #AAA; width:299px; height:108px;"></html:textarea>
					</p>					
					<br/>
				</div>						
	
				<div id="idiomesRight" style="position:absolute;left:237px;top: 231px; width:100px;">
					<div id="idiomesTraduct">
						<a id="traductRightCa" href="#" class="noselec" onclick="tradOperation('traduccioFinalCa','Right');" title="<bean:message key='help.merli.op.traduct.lang.ca'/>">
							<bean:message key='merli.op.traduct.lang.ca'/>
						</a>
						<a id="traductRightEs" href="#" class="selec" onclick="tradOperation('traduccioFinalEs','Right');" title="<bean:message key='help.merli.op.traduct.lang.es'/>">
							<bean:message key='merli.op.traduct.lang.es'/>
						</a>
						<a id="traductRightEn" href="#" class="noselec" onclick="tradOperation('traduccioFinalEn','Right');" title="<bean:message key='help.merli.op.traduct.lang.en'/>">
							<bean:message key='merli.op.traduct.lang.en'/>
						</a>
						<a id="traductRightOc" href="#" class="noselec" onclick="tradOperation('traduccioFinalOc','Right');" title="<bean:message key='help.merli.op.traduct.lang.oc'/>">
							<bean:message key='merli.op.traduct.lang.oc'/>
						</a>			 						 
					</div>
				</div>
										
				<div>
					<div class="itemOperacions">
						<button type="button" class="butoMerli small red" onclick="javascript:traduir('rightsTrad');" title="<bean:message key='help.merli.op.acceptar'/>"><img src="web/images/acceptar_boto.png" style="height:10px;">&nbsp;<bean:message key='merli.op.acceptar'/></button>				
						<button type="button" class="butoMerli small red"  onclick="javascript:cancel('rightsTrad');" title="<bean:message key='help.merli.op.cancelar'/>"><img src="web/images/cancelar_boto.png">&nbsp;<bean:message key='merli.op.cancelar'/></button>
					</div>								
				</div>													
			</div>	
	