<%@ page import="java.sql.*, simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, java.util.Locale, org.apache.log4j.Logger" %>
<%@ page pageEncoding="UTF-8" %>

<% 
  if (Configuracio.isVoid()) {
     Configuracio.carregaConfiguracio();
     }    
  String idioma = (String)session.getAttribute("idioma");
  if (idioma == null) {
     idioma = Configuracio.idioma;
     }
  Locale myLocale = new Locale(idioma); 
  %> 

<%

  Logger logger = Logger.getLogger("indexacio.jsp");

   String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb;      
   String contextWeb = Configuracio.contextWebAplicacio;
   String cssFile = urlLocal + "/" + contextWeb + "/css/admin_cerca.css";


  Connection conn = null;
  Statement stmt = null;
  ResultSet rs = null;
  String sql = "";
  
  try {

	conn = UtilsCercador.getConnectionFromPool();

%>
<html>
<head>
<title> Administracio Sistema de Cerca XTEC </title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" type="text/css" href="<%=cssFile%>"/>
</head>

<body>
<div id="container_popup">
  

<table class="taula_admin_cerca">
   <caption>
	Indexació
   </caption>
   <thead>
      <tr>
          <th>Recurs</th>
          <th>Data</th>
          <th>XML vàlid</th>
          <th>URL vàlida</th>
          <th>URL recurs</th>
      </tr>
   </thead>
   <tbody> 
   
<%
	stmt = conn.createStatement();
	// sql = "SELECT * FROM log_indexacio ORDER BY data_log DESC";
	sql = "SELECT * FROM log_indexacio ORDER BY TO_CHAR(TO_DATE(data_log, 'yyyy-mm-dd hh24:mi:ss'),'yyyymmdd') DESC, (xml_valid + url_valid) ASC";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
           String recurs_id = rs.getString("recurs_id");
           String data_log = rs.getString("data_log");
           int xml_valid = rs.getInt("xml_valid");
           int url_valid = rs.getInt("url_valid");    
           String urlRecurs = rs.getString("url_recurs");           
                  
%>
        <tr>
          <td class="esquerra"><%=recurs_id%></td>
          <td><%=data_log%></td>
          <% if (xml_valid == 1) { %>
            <td><img src="../img/ok.gif"/></td>
          <%   } else { %>
            <td><img src="../img/cancel.gif"/></td>
          <%   } %>  
          <% if (url_valid == 1) { %>          
            <td><img src="../img/ok.gif"/></td>
          <%   } else { %>
            <td><img src="../img/cancel.gif"/></td>
          <%   } %>  
          <td><%=urlRecurs%></td>
        </tr>

<%
           }
           
        } catch (Exception e) {
	e.printStackTrace();
        } finally {
        if (rs != null) {
           rs.close();
           }
        if (stmt != null) {
           stmt.close();
           }
        if (conn != null) {
           conn.close();
           }

        }           
           
%>         
       </tbody>
      </table> 
</div>