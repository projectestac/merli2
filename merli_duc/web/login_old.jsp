<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ include file="/web/comu/taglibs.jsp"%>
<html:html locale="true">
<meta http-equiv="content-type" content="text/html;charset=ISO-8859-15"/>
	<title><bean:message key="application.title"/></title>
<head>
<LINK REL="stylesheet" TYPE="text/css" HREF="web/css/merli.css" />
</head>

<body>
<html:form action="login.do" focus="userName">
<div class="esquerra">
	<div id="logo">
		<img id="img" src="web/images/logo.png" alt="DUC" title="DUC" />
	</div>
</div>
<div id="dreta">
	<div id="header">
		<div id="titleHeader">
			<div id="entorns">
			</div>
			<div class="inferior">
				<div class="esquerra2" id="modes">
					<a href="#" title="cerca"><bean:message key="application.title"/></a>
				</div>
				<div class="dreta2" id="username">
					<a href="usuaris.do" title="Editar usuari"><bean:message key="login.nonregistred"/></a>
				</div>
			</div>
		</div>
	</div>
	<DIV id="cos">
		<div id="titleContent">
			<bean:message key="login.message"/>
		</div>
		<div id="content">
		<table align="center" id="login">
	  		<tr align="center">
				<td>
	  				<table align="center">
		  				<tr>
		    				<td align="right">
								<bean:message key="login.username"/>
							</td>
		   					<td align="left">
								<html:text 	property="userName" 
		    								size="15" 
		    								maxlength="15" />
								<html:errors property="userName" />
							</td>
		  				</tr>	
		  				<tr>
		   					<td align="right">
								<bean:message key="login.password"/>
							</td>
		   					<td align="left">
								<html:password 	property="password" 
		    									size="15" 
		    									maxlength="15" 
		    									redisplay="false"/>
								<html:errors property="password" />
							</td>
		 				</tr>	
		 				<tr>
							<td colspan="2" align="center">
								<html:errors property="login" />
								<html:submit styleClass="buto">
									<bean:message key="login.button.signon"/>
								</html:submit>
							</td>
		  				</tr>
	  				</table>
				</td>
	  		</tr>
		</table>
		</div>
	</DIV>
</div>	
</html:form>
</body>
</html:html>