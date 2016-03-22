<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld"%>
<%@ include file="/web/taglibs.jsp"%>
<jsp:useBean id="MagicBean" scope="request" class="edu.xtec.merli.MagicBean"/>
<html:html locale="true">
<meta http-equiv="content-type" content="text/html;charset=ISO-8859-15" />
<link rel="shortcut icon" href="web/images/favicon.ico" type="image/x-icon">
<link rel="icon" href="web/images/favicon.ico" type="image/x-icon">
<title><bean:message key="application.title" /></title>
<head>
<LINK REL="stylesheet" TYPE="text/css" HREF="web/css/merli_login.css" />

</head>

<body>
<html:form action="login.do" focus="userName">

<div id="login">
  <div id="login-header">
	<div id="login-header-logo"><img src="web/images/logo.png" alt="Magics"/></div>
	<div id="login-header-text" class="login-text"><bean:message key="login.message" /></div>
  </div>
  <div id="login-box">
	  <div id="login-content">
		<div id="login-content-text">
			<html:errors property="login" />
			<p class="login-text"><bean:message key="login.username" />: <html:text property="userName" size="15" maxlength="15" /> <br><html:errors property="userName" /></p>
			<p class="login-text"><bean:message key="login.password" />: <html:password property="password" size="15" maxlength="15" redisplay="false" style="margin-left:30px;"/><br><html:errors property="password" /></p>
			<p><html:submit styleClass="buto" style="margin-left:120px;">
				<bean:message key="login.button.signon" />
			   </html:submit></p>
		</div>
	  </div>
	  <span id="login-bg"></span>
	  <div style="color: #707070; float: right; padding-top: 125px;">
		  <jsp:getProperty name="MagicBean" property="versionControl"/>
	  </div>
  </div>  
</div>  




</html:form>
</body>
</html:html>
