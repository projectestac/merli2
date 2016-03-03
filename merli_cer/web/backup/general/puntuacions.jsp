<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
   if (Configuracio.isVoid()){
       Configuracio.carregaConfiguracio(); 
       }
   Logger logger = Logger.getLogger("puntuacions.jsp");
   
   String searchType = request.getParameter("searchType");
   if (searchType == null) {
      searchType = ""; 
      }    
   String recursCerca = request.getParameter("recursCerca");
   if (recursCerca == null) {
      recursCerca = ""; 
      }    
   String usuariCerca = request.getParameter("usuariCerca");
   if (usuariCerca == null) {
      usuariCerca = ""; 
      }
%>  
<jsp:include page="topGeneral.jsp" />

 <script language="Javascript">
    function doSearch(mySearchType) {
       document.search.searchType.value = mySearchType;
       document.search.submit();	          
       }              
 </script>
   <table class="tableMenuDreta"  cellspacing="5">
      <form name="search" method="POST" action="puntuacions.jsp">
         <input type="hidden" name="searchType" value=""/>           
         <tr>
            <td colspan="2" class="formTitle">Gestió de puntuacions</td>
         </tr>
         <tr>
            <td colspan="2"><hr/></td>
         </tr> 
 
   
   
    <tr>
      <td class="tableText">
        Recurs
      </td>
      <td>
        <input type="text" name="recursCerca" class="tableText" size="30" value="<%=recursCerca%>">
      </td>
      <td class="tableText">
        <input type="submit" name="Cercar" value="Cercar" class="tableButton" onClick="javascript:doSearch('recurs');">
      </td>
     </tr>  
    <tr>
      <td class="tableText">
        Usuari
      </td>
      <td>
        <input type="text" name="usuariCerca" class="tableText" size="30" value="<%=usuariCerca%>">
      </td>
      <td class="tableText">
        <input type="submit" name="Cercar" value="Cercar" class="tableButton" onClick="javascript:doSearch('usuari');">
      </td>
     </tr>  
    </form>
  </table>      
  
 <% 
   if ((searchType != null) && !searchType.equals("")) {	
   %>   
  <table border="1"> 
   <tr>
     <td><b>Usuari</b></td>
     <td><b>Recurs</b></td>
     <td><b>Puntuació</b></td>
   </tr>
   <%
      String sql = "";
      try {
	 Class.forName(Configuracio.nomDriverBD).newInstance();
 
      if (searchType.equals("usuari")) {
         sql = "select * from puntuacio p, recurs r where p.idRecurs=r.idRecurs and p.nomUsuari like '%" + usuariCerca + "%'";
         
         } 

      if (searchType.equals("recurs")) {
         sql = "select * from puntuacio p, recurs r where p.idRecurs=r.idRecurs and r.nomRecurs like '%" + recursCerca + "%'";
         }
       
       Connection conn = null;
       Statement stmt = null;
       ResultSet rs = null;
      
	 conn = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
	 stmt = conn.createStatement();
         rs = stmt.executeQuery(sql);
         while (rs.next()) {           
            String nomUsuari = rs.getString("p.nomUsuari");
            String nomRecurs = rs.getString("r.nomRecurs");
            int idRecurs = rs.getInt("p.idRecurs");
            int puntuacio = rs.getInt("p.puntuacio");
            
 %>
     <tr>
       <td><%=nomUsuari%></td>
       <td><%=nomRecurs%></td>
       <td><%=puntuacio%></td>
     </tr>  
 
 <%        
            }
        } catch (Exception e) {
        logger.error(e);
        }
        %>
   </table>         
        <%
     }
 %>  


 
<jsp:include page="bottomGeneral.html" />