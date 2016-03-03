<%@ page import="java.sql.*,simpple.xtec.web.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
   if (Configuracio.isVoid()){
       Configuracio.carregaConfiguracio(); 
       }
   Logger logger = Logger.getLogger("comentaris.jsp");
   
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

   String comentariCerca = request.getParameter("comentariCerca");
   if (comentariCerca == null) {
      comentariCerca = ""; 
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
      <form name="search" method="POST" action="comentaris.jsp">
         <input type="hidden" name="searchType" value=""/>           
         <tr>
            <td colspan="2" class="formTitle">Gestió de comentaris</td>
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
    <tr>
      <td class="tableText">
        Comentari
      </td>
      <td>
        <input type="text" name="comentariCerca" class="tableText" size="30" value="<%=comentariCerca%>">
      </td>
      <td class="tableText">
        <input type="submit" name="Cercar" value="Cercar" class="tableButton" onClick="javascript:doSearch('comentari');">
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
     <td><b>Comentari</b></td>
   </tr>
   <%
      String sql = "";
      try {
	 Class.forName(Configuracio.nomDriverBD).newInstance();
 
      if (searchType.equals("usuari")) {
         sql = "select * from recurs r, comentari c where c.idRecurs=r.idRecurs and c.nomUsuari like '%" + usuariCerca + "%'";         
         }

      if (searchType.equals("recurs")) {
         sql = "select * from recurs r, comentari c where c.idRecurs=r.idRecurs and r.nomRecurs like '%" + recursCerca + "%'";
         }
      
      if (searchType.equals("comentari")) {
         sql = "select * from recurs r, comentari c where c.idRecurs=r.idRecurs and c.comentari like '%" + comentariCerca + "%'";
         }

       Connection conn = null;
       Statement stmt = null;
       ResultSet rs = null;
      
	 conn = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
	 stmt = conn.createStatement();
         rs = stmt.executeQuery(sql);
         while (rs.next()) {
            String nomUsuari = rs.getString("c.nomUsuari");
            String nomRecurs = rs.getString("r.nomRecurs");
            int idRecurs = rs.getInt("c.idRecurs");
            String comentari = rs.getString("c.comentari");
            
 %>
     <tr>
       <td><%=nomUsuari%></td>
       <td><%=nomRecurs%></td>
       <td><%=comentari%></td>
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