<%@ page import="org.apache.commons.io.FileUtils"%>
﻿<%@ page import="java.sql.*,simpple.xtec.indexador.util.Configuracio, simpple.xtec.indexador.util.Utils, java.io.*" %>
<%@ page pageEncoding="UTF-8" %>
<%
    File file = new File("/serveis/dades/int/w02/dataapps/merli/index");
    /*file.mkdir();
    file.setExecutable(true, false);
    file.setReadable(true, false);
    file.setWritable(true, false);*/
    /*File dir = new File("/serveis/log/int/w02/DOM_W02_INT/webapps/merli");
    FileUtils.cleanDirectory(dir); */

    boolean isOk = true;
    Connection myConnection = null;
    try {
        if (Configuracio.isVoid()) {
            Configuracio.carregaConfiguracio();
        }
        myConnection = Utils.getConnectionFromPool();
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
        } catch (Exception e) {
        }
    }
    File indexDir = null;
    try {
        indexDir = new File(Configuracio.indexDir);
        if (!indexDir.canWrite()) {%>

<% throw new Exception();
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
        if (!indexDir2.canWrite()) {
            throw new Exception();
        }

    } catch (Exception e) {
        isOk = false;
%>
<font color="#ff0000">ERROR: no es pot accedir al directori <%=Configuracio.indexDir2%></font>
<%
    }

    if (isOk) {%>OK<%}%>    