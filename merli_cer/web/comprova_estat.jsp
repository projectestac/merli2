<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, java.io.*" %>
<%@ page pageEncoding="UTF-8" %>
<% 
  boolean isOk = true;
  Connection myConnection = null;
  try {
    if (Configuracio.isVoid()) {
      Configuracio.carregaConfiguracio();
      }    
    myConnection = UtilsCercador.getConnectionFromPool();   
    if (myConnection == null) {
	   throw new Exception(); 
       }
    } catch (Exception e) {
	isOk = false;    
%>
    <font color="#ff0000">ERROR: database connection "CercadorConnectionPoolDS" failed</font>
    <br/><br/>
<%
    } finally {
	try {
	  if (myConnection != null) {
		 myConnection.close(); 
	     }	
	  } catch (Exception e) {}
    }
%> 
<%
   File indexDir = null;
   try {
	  indexDir = new File(Configuracio.indexDir); 
	  if (!indexDir.canRead()) {
         throw new Exception();
	     }
    } catch (Exception e) {
	isOk = false;    
%>
    <font color="#ff0000">ERROR: no es pot accedir al directori <%=Configuracio.indexDir%></font>
    <br/><br/>
<%
    }

   File indexDir2 = null;
   try {
	  indexDir2 = new File(Configuracio.indexDir2); 
	  if (!indexDir2.canRead()) {
         throw new Exception();		  
	     }
    } catch (Exception e) {
	isOk = false;    
%>
    <font color="#ff0000">ERROR: no es pot accedir al directori <%=Configuracio.indexDir2%></font>
<%
    }
    
    if (isOk) {%>OK<%}%>    