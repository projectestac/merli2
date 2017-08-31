<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.UtilsCercador" %>


<%
    if (Configuracio.isVoid()){
        Configuracio.carregaConfiguracio(); 
        } 


           
   String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb;      
   String contextWeb = Configuracio.contextWebAplicacio;
  // String cssFile = urlLocal + "/" + contextWeb + "/css/panellNew.css";

   String cssFile = "";
   String imgFile = "";
   String urlCercador = "";
  
     
   if (!Configuracio.servidorWeb.equals("") && !Configuracio.portWeb.equals("") && !Configuracio.contextWebAplicacio.equals("")) {   
       cssFile = urlLocal + "/" + contextWeb + "/css/admin_cerca.css";
       imgFile = urlLocal + "/" + contextWeb + "/img/pix_rojo.gif";
       urlCercador = urlLocal + "/" + contextWeb + "/administracio/";
      }
   
   String selected = request.getParameter("selected");
   if (selected == null) {
      selected = "";
      }
   if (selected.equals("10")) {
      cssFile = "../css/admin_cerca.css";
      imgFile = "../img/pix_rojo.gif";

      }

   String usuariNomComplet = (String)session.getAttribute("usuariNomComplet");
   if (usuariNomComplet == null) {
      usuariNomComplet = UtilsCercador.getNomComplet((String) session.getAttribute("user"));
      session.setAttribute("usuariNomComplet", usuariNomComplet);
      }


%>
<html>
<head>
<title> Administració Sistema de Cerca XTEC </title>
<meta http-equiv="content-type" content="text/html"/>
<link rel="stylesheet" type="text/css" href="<%=cssFile%>"/>
 <% if (selected.equals("7")) { %>
 <link rel="alternate" type="application/rss+xml" title="Feed notícies" href="<%=urlLocal%>/rss/noticies.rss" />
 <%  } %>
</head>

<body>
<div id="container">

	<div id="top">
<!--		<h1><a class="titol" href="inicial.jsp">Administraci&oacute; del sistema de cerca</a></h1> -->
		<h1>Administraci&oacute; del sistema de cerca</h1>
	</div>
    <div id="login">
	  Hola, <%=usuariNomComplet%> | <a href="logout.jsp">Logout</a>
	</div>


	<div id="leftnav">
		<ul>
		  <% if (selected.equals("0")) { %>
			<li><a id="current" href="<%=urlCercador%>inicial.jsp">Inicial</a></li>
		  <%    } else {  %>
		    <li><a href="<%=urlCercador%>inicial.jsp">Inicial</a></li>
		  <%    }  %>
		  <% if (selected.equals("1")) { %>
			<li><a id="current" href="<%=urlCercador%>indexacio.jsp">Indexaci&oacute;</a></li>
		  <%    } else {  %>
		    <li><a href="<%=urlCercador%>indexacio.jsp">Indexaci&oacute;</a></li>
		  <%    }  %>
		  <% if (selected.equals("2")) { %>
			<li><a id="current" href="<%=urlCercador%>cercadorEdu365.jsp">Cercador edu365</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>cercadorEdu365.jsp">Cercador edu365</a></li>
		  <%    }  %>
		  <% if (selected.equals("3")) { %>
			<li><a id="current" href="<%=urlCercador%>cercadorAltres.jsp">Cercador Embedded</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>cercadorAltres.jsp">Cercador Embedded</a></li>
		  <%    }  %>
		  <% if (selected.equals("4")) { %>
			<li><a id="current" href="<%=urlCercador%>cercadorXtec.jsp">Cercador XTEC</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>cercadorXtec.jsp">Cercador XTEC</a></li>
		  <%    }  %>
		  <% if (selected.equals("5")) { %>
			<li><a id="current" href="<%=urlCercador%>comentaris.jsp">Comentaris</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>comentaris.jsp">Comentaris</a></li>
		  <%    }  %>
		  <% if (selected.equals("6")) { %>
			<li><a id="current" href="<%=urlCercador%>accessos.jsp">Accessos</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>accessos.jsp">Accessos</a></li>
		  <%    }  %>
		  <% if (selected.equals("7")) { %>
			<li><a id="current" href="<%=urlCercador%>noticies.jsp">Not&iacute;cies cat&agrave;leg</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>noticies.jsp">Not&iacute;cies cat&agrave;leg</a></li>
		  <%    }  %>
		  <% if (selected.equals("8")) { %>
			<li><a id="current" href="<%=urlCercador%>usuaris.jsp">Usuaris</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>usuaris.jsp">Usuaris</a></li>
		  <%    }  %>
		  <% if (selected.equals("9")) { %>
			<li><a id="current" href="<%=urlCercador%>tipusFitxers.jsp">Tipus de fitxers</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>tipusFitxers.jsp">Tipus de fitxers</a></li>
		  <%    }  %>
		  <% if (selected.equals("10")) { %>
			<li><a id="current" href="<%=urlCercador%>configuracio.jsp">Configuraci&oacute; del sistema</a></li>
		  <%    } else {  %>
			<li><a href="<%=urlCercador%>configuracio.jsp">Configuraci&oacute; del sistema</a></li>
		  <%    }  %>
  		</ul>
	</div>

