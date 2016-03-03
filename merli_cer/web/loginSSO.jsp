<%@page import="org.apache.log4j.Logger"%>
<%@page import="simpple.xtec.web.util.XMLCollection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="tags" uri="/WEB-INF/taglibs-i18n.tld"%>
<%
    //String user = request.getRemoteUser();
    String user = (String)session.getAttribute("user");
    if (user == null) {
        Logger logger = Logger.getLogger("cercaCompleta.jsp");
        String sLang = XMLCollection.getLang(request);
        // response.setHeader("Osso-Paranoid", "false" );     
       /*response.setHeader("Osso-Return-Url","/CercadorEducacio2/login.jsp");
         response.sendError(499, "Oracle SSO");*/
%>

<html locale="true">
    <meta http-equiv="content-type" content="text/html;charset=ISO-8859-15" />
    <link rel="shortcut icon" href="web/images/favicon_duc.ico" type="image/x-icon">
    <link rel="icon" href="web/images/favicon_duc.ico" type="image/x-icon">
    <title>MerLí Cercador - Login</title>
    <head>
        <LINK REL="stylesheet" TYPE="text/css" HREF="css/merli_login.css" />

    </head>

    <body>
        <form action="login" focus="username" method="POST">

            <div id="login">
                <div id="login-header">
                    <div id="login-header-logo"><img src="imatges/logoCercador.png" alt="Cercador"/></div>
                    <div id="login-header-text" class="login-text"><bean:message key="login.message" /></div>
                </div>
                <div id="login-box">
                    <div id="login-content">
                        <div id="login-content-text">
                            <errors property="login" />
                            <p class="login-text">
                                <%=XMLCollection.getProperty("administracio.usuaris.usuari", sLang)%>
                            <input type="text" size="15" maxlength="15" name="username" style="float: right; "/> <br><errors property="username" /></p>
                            <p class="login-text">
                                <%=XMLCollection.getProperty("administracio.usuaris.contrasenya", sLang)%>
                            <input type="password" size="15" maxlength="15" name="password" style="float: right;"/><br><errors property="password" />
                            </p>

                            <p><input type="submit" class="buto" style="margin-left:120px;" value="<%=XMLCollection.getProperty("cerca.cercaSimple.login", sLang)%>"/></p>
                        </div>
                    </div>
                    <span id="login-bg"></span>
                </div>  
            </div>  



            <!-- div class="esquerra">
            <div id="logo"><img id="img" src="web/images/logo.png" alt="MeRLí"
                    title="MeRLí" /></div>
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
            <errors property="login" />
            <tr>
                    <td align="right"><bean:message key="login.username" /></td>
                    <td align="left"><text property="userName" size="15"
                       maxlength="15" /> <errors property="userName" /></td>
       </tr>
       <tr>
               <td align="right"><bean:message key="login.password" /></td>
               <td align="left"><password property="password" size="15"
                           maxlength="15" redisplay="false" /> <errors
                           property="password" /></td>
           </tr>
           <tr>
                   <td colspan="2" align="center"><submit styleClass="buto">
                           <bean:message key="login.button.signon" />
            </submit></td>
    </tr>
</table>
</td>
</tr>
</table>
</div>
</DIV>
</div-->
        </form>
    </body>
</html>

<%    } else {
        String sParam = ("true".equalsIgnoreCase(request.getParameter("comment"))) ? "&comment=true" : "";
        response.sendRedirect((String) session.getAttribute("lastUrl") + sParam);
    }
%>