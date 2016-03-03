<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- JSTL tag libs --%>
<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>

<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>

<html:html locale="true"/>
<head>
	<fmt:setBundle basename="ApplicationResources"/>
	<title><fmt:message key="etiq.title"/></title>
</head>
<body>
<html:form action="Metiq.do" focus="titol">
	<table align="center">
  		<tr align="center">
    		<td><H1><fmt:message key="etiq.descGeneral"/></H1></td>
  		</tr>
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.titol"/>
						</td>
	   					<td align="left">
							<html:text 	property="titol" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="titol"/>
						</td>
	  				</tr>	
	  				<tr> 
	   					<td align="right">
							<fmt:message key="etiq.descripcio"/>
						</td>
	   					<td align="left">
							<html:textarea 	property="descripcio"
	    									cols="40" 
	    									rows="6"
	    									style="wrap:virtual;"
	    									/>
							<html:errors property="descripcio" />
						</td>
	 				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.url"/>
						</td>
	   					<td align="left">
							<html:text 	property="url" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="url" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.versio"/>
						</td>
	   					<td align="left">
							<html:text 	property="versio" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="versio" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.dataPublicacio"/>
						</td>
	   					<td align="left">
							<html:text 	property="dataPublicacio" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="dataPublicacio" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.responsable"/>
						</td>
	   					<td align="left">
							<html:text 	property="responsable" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="responsable" />
						</td>
	  				</tr>	
  		<tr align="center">
    		<td><H1><fmt:message key="etiq.descEducativa"/></H1></td>
  		</tr>
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.dificultat"/>
						</td>
	   					<td align="left">
							<html:text 	property="dificultat" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="dificultat" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.duracio"/>
						</td>
	   					<td align="left">
							<html:text 	property="duracio" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="duracio" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.edatMin"/>
						</td>
	   					<td align="left">
							<html:text 	property="edatMin" 
	    								size="4" 
	    								maxlength="3" />
							<html:errors property="edatMin" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.edatMax"/>
						</td>
	   					<td align="left">
							<html:text 	property="edatMax" 
	    								size="4" 
	    								maxlength="3" />
							<html:errors property="edatMax" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.nivell"/>
						</td>
	   					<td align="left">
							<html:text 	property="nivell" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="nivell" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.rolUsuari"/>
						</td>
	   					<td align="left">
							<html:text 	property="rolUsuari" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="rolUsuari" />
						</td>
	  				</tr>	
  		<tr align="center">
    		<td><H1><fmt:message key="etiq.descTecnica"/></H1></td>
  		</tr>
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.tipusRecurs"/>
						</td>
	   					<td align="left">
							<html:text 	property="tipusRecurs" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="tipusRecurs" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.format"/>
						</td>
	   					<td align="left">
							<html:text 	property="format" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="format" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.llengues"/>
						</td>
	   					<td align="left">
							<html:text 	property="llengues" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="llengues" />
						</td>
	  				</tr>
  		<tr align="center">
    		<td><H1><fmt:message key="etiq.descCredits"/></H1></td>
  		</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.drets"/>
						</td>
	   					<td align="left">
							<html:text 	property="drets" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="drets" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.descDrets"/>
						</td>
	   					<td align="left">
							<html:text 	property="descDrets" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="descDrets" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.idAccio"/>
						</td>
	   					<td align="left">
	   						<html:select property="idAccio" >
								<html:option value="1" >1</html:option>
								<html:option value="2" >2</html:option>
								<html:option value="3" >3</html:option>
							</html:select>						
					</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.idAmbit"/>
						</td>
	   					<td align="left">
							<html:text 	property="idAmbit" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="idAmbit" />
						</td>
	  				</tr>	
  		<tr align="center">
    		<td><H1><fmt:message key="etiq.descDescriptors"/></H1></td>
  		</tr>
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.parClauSel"/>
						</td>
	   					<td align="left">
							<html:text 	property="parClauSel" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="parClauSel" />
						</td>
	  				</tr>	
  		<tr align="center">
    		<td><H1>Interns</H1></td>
  		</tr>
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.idRec"/>
						</td>
	   					<td align="left">
							<html:text 	property="idRec" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="idRec" />
						</td>
	  				</tr>	
	  				<tr>
	    				<td align="right">
							<fmt:message key="etiq.operacio"/>
						</td>
	   					<td align="left">
							<html:text 	property="operacio" 
	    								size="15" 
	    								maxlength="256" />
							<html:errors property="operacio" />
						</td>
	  				</tr>	
	 				<tr>
						<td colspan="2" align="center">
							<html:submit>
								<fmt:message key="etiq.buto.submit"/>
							</html:submit>
						</td>
	  				</tr>  				
	</table>
</html:form>
</body>
</html>
