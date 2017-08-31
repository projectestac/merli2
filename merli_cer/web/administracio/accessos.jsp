<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.XMLCollection" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, simpple.xtec.web.util.AccessLogObject" %>
<%@ page pageEncoding="UTF-8" %>

<%
   Logger logger = Logger.getLogger("accessos.jsp");
   //String usuari = (String)request.getRemoteUser();
   String usuari = (String)session.getAttribute("user");
   
   if (Configuracio.isVoid()) {
     Configuracio.carregaConfiguracio();
     }   
   
   logger.debug("Usuari 1: " + usuari);

   //if ( (!UtilsCercador.isUserInRole(usuari) ||(usuari == null)) && Configuracio.sso.equals("si")){
 if ((!UtilsCercador.isUserInRole(usuari) || (usuari == null))) {
        /*logger.debug("Redirect SSO");
        response.setHeader("Osso-Paranoid", "true");
        response.sendError(499, "Oracle SSO");*/
        response.sendRedirect("../loginSSO.jsp");

      } else {

      
   
   String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;      
   
   String tipusResultat = (String)request.getAttribute("accessos.tipusResultat");   
   if (tipusResultat == null) {
      tipusResultat = "";
      }
   logger.debug("Tipus resultat: " + tipusResultat);   
   String filtreXtec = (String)request.getAttribute("accessos.filtreXtec");
   if (filtreXtec == null) {
      filtreXtec = "";
      }
   logger.debug("filtreXtec: " + filtreXtec);         
   String filtreEdu365 = (String)request.getAttribute("accessos.filtreEdu365");
   if (filtreEdu365 == null) {
      filtreEdu365 = "";
      }
   logger.debug("filtreEdu365: " + filtreEdu365);   
   String dataIniciCerca = (String)request.getAttribute("accessos.dataIniciCerca");
   if (dataIniciCerca == null) {
      dataIniciCerca = "";
      }
   logger.debug("dataIniciCerca: " + dataIniciCerca);   
   String dataFinalCerca = (String)request.getAttribute("accessos.dataFinalCerca");
   if (dataFinalCerca == null) {
      dataFinalCerca = "";
      }
   logger.debug("dataFinalCerca:  " + dataFinalCerca);   
   
/*   String numResultatsParam = (String)request.getAttribute("accessos.resultats.numResultats");
   Integer numResultatsInt = null; */
   int numResultats = 0;
/*   if (numResultatsParam != null) {
      numResultatsInt = new Integer(numResultatsParam);
      numResultats = numResultatsInt.intValue();
      }
*/
   logger.debug("num resultats: " + numResultats);
   String nivellString = (String)request.getAttribute("accessos.resultats.nivell");
   int nivell = 0;
   if (nivellString != null) {
      nivell = new Integer(nivellString).intValue();
      }


   
   ArrayList resultats = (ArrayList)request.getAttribute("accessos.resultats"); 
   
%>   

<% 
 
  String idioma = (String)session.getAttribute("idioma");
  if (idioma == null) {
     idioma = Configuracio.idioma;
     }
  Locale myLocale = new Locale(idioma); 

  %> 



<jsp:include page="topAdministracio.jsp?selected=6" />
	

<!-- <link rel="stylesheet" type="text/css" media="all" href="<%=urlLocal%>/css/theme.css" title="Aqua" /> -->
<!-- import the calendar script -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-main.js"></script>
<!-- import the calendar script -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar.js"></script>
<!-- import the language module -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-ca.js"></script>
<!-- import the setup module -->
<script type="text/javascript" src="<%=urlLocal%>/scripts/calendar-setup.js"></script>
<script type="text/javascript" src="<%=urlLocal%>/scripts/date-validation.js"></script>
<style type="text/css">@import url(<%=urlLocal%>/css/calendar-xtec.css);</style>

<script language="JavaScript" type="text/javascript">

  function checkCR(evt) {

    var evt  = (evt) ? evt : ((event) ? event : null);

    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);

    if ((evt.keyCode == 13) && (node.type=="text")) {return false;}
    if ((evt.keyCode == 13) && (node.type=="checkbox")) {return false;}
    if ((evt.keyCode == 13) && (node.type=="radio")) {return false;}    
  }

  document.onkeypress = checkCR;

</script> 
<script type="text/javascript">
    function cercar() {
       document.accessos.operacio.value = 'cercar';   
       if ((document.accessos.dataIniciCerca.value != "") && (isDate(document.accessos.dataIniciCerca.value) == false)) {
            alert('<%=XMLCollection.getProperty("administracio.accessos.errorData")%>');	         
	        return false;
            }  
       if ((document.accessos.dataFinalCerca.value != "") && (isDate(document.accessos.dataFinalCerca.value) == false)) {
            alert('<%=XMLCollection.getProperty("administracio.accessos.errorData")%>');	         
	        return false;
            }  
       document.accessos.submit();
       }
    function exportar() {
       document.accessos.operacio.value = 'exportar';
       document.accessos.submit();       
       }
    function goToPage (nivell) {
       document.accessos.nivell.value = nivell;       
       document.accessos.submit();	              
       }        
</script>
<div id="content">
    <form name="accessos" method="POST" action="/<%=Configuracio.contextWebAplicacio%>/ServletAccessos">
       <input type="hidden" name="nivell" value="0"/>
       <input type="hidden" name="operacio" value="cercar"/>   
       <fieldset>
	   <legend><%=XMLCollection.getProperty("administracio.accessos.titol")%></legend>
		<div id="linia">
	 	   <%=XMLCollection.getProperty("administracio.accessos.cerquesedu365")%>
                   <% if (filtreEdu365.equals("edu365")) { %>                            	 	   
		      <input type="checkbox" name="filtreEdu365" value="edu365" checked="true"/>
		   <%    } else {   %>
		      <input type="checkbox" name="filtreEdu365" value="edu365" />
                   <%    }  %>		      		   
	 	   <%=XMLCollection.getProperty("administracio.accessos.cerquesxtec")%>
                   <% if (filtreXtec.equals("xtec")) { %>	 	   
		      <input type="checkbox" name="filtreXtec" value="xtec" checked="true"/>
		   <%    } else {   %>
		      <input type="checkbox" name="filtreXtec" value="xtec" />
                   <%    }  %>		   
		</div>
		<div id="linia">
			<%=XMLCollection.getProperty("administracio.accessos.llistat")%>
                        <% if (tipusResultat.equals("llistat") || tipusResultat.equals("")) { %>			
			   <input type="radio" name="tipusResultat" value="llistat" checked="true"/>
	                <%    } else {   %>
			   <input type="radio" name="tipusResultat" value="llistat"/>
                        <%    }  %>	                
			<%=XMLCollection.getProperty("administracio.accessos.histograma")%>
                        <% if (tipusResultat.equals("histograma")) { %>			
			   <input type="radio" name="tipusResultat" value="histograma" checked="true"/>
	                <%    } else {   %>
			   <input type="radio" name="tipusResultat" value="histograma"/>
                        <%    }  %>	                	
		</div>       
		<div id="linia">
			<label class="filtrat" for="dataIniciCerca"><%=XMLCollection.getProperty("administracio.accessos.dataInici")%></label>
			<input type="text" name="dataIniciCerca" id="dataIniciCerca" size="10" value="<%=dataIniciCerca%>">
			<button id="cerca_inici">...</button>
		</div>
		<div>
			<label class="filtrat" for="dataFinalCerca"><%=XMLCollection.getProperty("administracio.accessos.dataFinal")%></label>
			<input type="text" name="dataFinalCerca" id="dataFinalCerca" size="10" value="<%=dataFinalCerca%>">
			<button id="cerca_fi">...</button>
		</div>

		<div id="linia">
			<input type="button" value="<%=XMLCollection.getProperty("administracio.accessos.filtra")%>" onClick="javascript:cercar();"/>
		</div>
	</fieldset>
     </form>
     <br/>

<% if (resultats != null) {
	  Integer numResultatsInt = (Integer)resultats.get(0);
	  numResultats = numResultatsInt.intValue();
	
	 %>   
     <table class="taula_admin_cerca" width="500">
	<caption>
		<%=XMLCollection.getProperty("administracio.accessos.accessos")%>
	</caption>
	<thead>
	<tr>
	   <th><%=XMLCollection.getProperty("administracio.accessos.cerca")%></th>
           <% if (tipusResultat.equals("llistat")) { %>
	     <th><%=XMLCollection.getProperty("administracio.accessos.data")%></th>
	     <th><%=XMLCollection.getProperty("administracio.accessos.hora")%></td>
	     <th><%=XMLCollection.getProperty("administracio.accessos.cercador")%></th>
	   <%   } else {  %>
   	     <th><%=XMLCollection.getProperty("administracio.accessos.numero_cerques")%></th>
         <th><%=XMLCollection.getProperty("administracio.accessos.cercador")%></th>   	     
	   <%   } %>  
 	</tr>
	</tr>
	</thead>
	<tfoot>
	   <tr>
	      <td class="centre" colspan="4"><a href="javascript:exportar();"><%=XMLCollection.getProperty("administracio.accessos.exportarCSV")%></a></td>
	   </tr>
	</tfoot>
	<tbody>
<%    
    int i = 1;
    while (i < resultats.size()) {
       // if ( (i >= (nivell * Configuracio.numResultatsPagina)) && (i < ((nivell * Configuracio.numResultatsPagina) + Configuracio.numResultatsPagina)) ){
        AccessLogObject accessLog = (AccessLogObject)resultats.get(i);
        String dataLog = accessLog.data;
        String dia = UtilsCercador.getDia(dataLog);
        String hora = UtilsCercador.getHora(dataLog);          
        int cercador = accessLog.cercador;     
%>
        <tr>
          <td class="esquerra"><%=accessLog.cerca%></td>
          <% if (tipusResultat.equals("llistat")) { %>
             <td class="centre"><%=dia%></td>
             <td class="centre"><%=hora%></td>
          <%   } else { %>
             <td class="centre"><%=accessLog.numCerques%></td>          
          <%   }    %>
          <% //if (tipusResultat.equals("llistat")) { 
                if (cercador == 1) { %>
             <td class="centre">edu365</td>
          <%     } else { %>
             <td class="centre">XTEC</td>          
          <%     }
             // }    %>
        </tr>
<% 
          // }
       i ++;
       }  
%>
    </tbody>
  </table>
  <p class="paginacio">
<%
   int numPagines = numResultats / Configuracio.numResultatsPagina;
   if ((numResultats % Configuracio.numResultatsPagina) != 0) {
      numPagines ++;
      }

      
      
   int j = 0;
   int top = 10;
   if (numPagines > 1) {
	   j = nivell - 4;				
	   if (j <= 0) {
   		  j = 0;
   		  } else {
	   	  %>	  
		  ...
		  <%	
		  }
	   top = j + 10;
	   if (top > numPagines) {
	       top = numPagines;
		   }
	   while (j < top) {
	      if (j != nivell) {
		      %>
		      <a href="javascript:goToPage('<%=j%>')"><%=j + 1%></a>
		      <%
	          } else {
		      %>    
			  <%=j + 1%>
			  <%         
			  }
	       j ++;
	       }  			
	    if (j != numPagines) {%>
	    	...
 	      <% } 
   	    }
    } 
   
%>

   </p>
  </div> 
  
		<!-- JS del calendari {{{-->
		<script type="text/javascript">
		  Calendar.setup(
			{
			  inputField  : "dataIniciCerca",         // ID of the input field
			  ifFormat    : "%d/%m/%Y",    // the date format
			  button      : "cerca_inici",       // ID of the button
			  firstDay    : 1
			}
		  );
		  Calendar.setup(
			{
			  inputField  : "dataFinalCerca",         // ID of the input field
			  ifFormat    : "%d/%m/%Y",    // the date format
			  button      : "cerca_fi",       // ID of the button
			  firstDay    : 1
			}
		  );
		</script>
		<!-- }}} -->

  
<jsp:include page="bottomAdministracio.jsp" />
<% } %>