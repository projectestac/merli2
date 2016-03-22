<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld"%>
<%@ include file="/web/comu/taglibs.jsp"%>
<jsp:useBean id="LoginForm" scope="request" class="edu.xtec.merli.segur.login.LoginForm"/>
<html:html locale="true">
    <meta http-equiv="content-type" content="text/html;charset=ISO-8859-15" />
    <link rel="shortcut icon" href="web/images/favicon_duc.ico" type="image/x-icon">
    <link rel="icon" href="web/images/favicon_duc.ico" type="image/x-icon">
    <title><bean:message key="application.title" /></title>
    <head>
        <LINK REL="stylesheet" TYPE="text/css" HREF="web/css/merli_login.css" />

    </head>

    <body>
        <% if (request.getSession().getAttribute("user") == null){%>
        <html:form action="login.do" focus="userName">

            <div id="login">
                <div id="login-header">
                    <div id="login-header-logo"><img src="web/images/logo.png" alt="DUC"/></div>
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
                    <br>
                    <div style="color: #707070; float: right; padding-top: 125px;">
                        <jsp:getProperty name="LoginForm" property="versionControl"/>
                    </div>
                </div>  
            </div>  



            <!-- div class="esquerra">
            <div id="logo"><img id="img" src="web/images/logo.png" alt="MeRL�"
                    title="MeRL�" /></div>
            </div>
            <div id="dreta">
            <div id="header">
            <div id="titleHeader">
            <div id="entorns"></div>
            <div class="inferior">
            <div class="esquerra2" id="modes"><a href="#" title="cerca"><bean:message
                key="application.title" /></a></div>
        <div class="dreta2" id="username"><a href="usuaris.do"
                title="Editar usuari"><bean:message key="login.nonregistred" /></a></div>
        </div>
        </div>
        </div>
        <DIV id="cos">
        <div id="titleContent"><bean:message key="login.message" /></div>
        <div id="content">
        <table align="center" id="login">
                <tr align="center">
                        <td>
                        <table align="center">
            <html:errors property="login" />
            <tr>
                    <td align="right"><bean:message key="login.username" /></td>
                    <td align="left"><html:text property="userName" size="15"
                       maxlength="15" /> <html:errors property="userName" /></td>
       </tr>
       <tr>
               <td align="right"><bean:message key="login.password" /></td>
               <td align="left"><html:password property="password" size="15"
                           maxlength="15" redisplay="false" /> <html:errors
                           property="password" /></td>
           </tr>
           <tr>
                   <td colspan="2" align="center"><html:submit styleClass="buto">
                <bean:message key="login.button.signon" />
            </html:submit></td>
    </tr>
</table>
</td>
</tr>
</table>
</div>
</DIV>
</div-->
        </html:form>
        <% }else{response.sendRedirect ("gesNodes.do");}%>
    </body>
</html:html>
