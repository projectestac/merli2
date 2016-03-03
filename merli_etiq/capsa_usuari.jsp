<%@ taglib prefix="fmt" uri="/WEB-INF/fmt.tld" %>
<%-- Struts provided Taglibs --%>
<%@ taglib prefix="html" uri="/WEB-INF/struts-html-el.tld" %>
<%@ include file="/web/taglibs.jsp"%>
<%org.apache.struts.util.MessageResources mresources = org.apache.struts.util.MessageResources.getMessageResources("ApplicationResources");%>

<jsp:useBean id="gesrecurs" scope="request" class="edu.xtec.merli.gestorrec.GestorForm"/>
				<div class="dreta2" id="username">
					<!-- <a href="usuaris.do" title="Editar usuari"> --><%
						if (request.getSession() != null && request.getSession().getAttribute("user") != null){
							out.println(((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser());	
						}else{
							%><jsp:getProperty name="gesrecurs" property="user"/><%
						}%>
					<!-- </a> -->
					<a class="logout" href="logout.do" title="<bean:message key='help.op.logout'/>">
					<!--a href="login.jsp" title="<bean:message key='help.op.logout'/>"-->
						<bean:message key='etiq.op.logout' />
					</a>
				</div>