<%@ page import="java.sql.*,simpple.xtec.web.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
       String tipusCercador = "complet"; 
      
       Logger logger = Logger.getLogger("pesos.jsp");
       float pes_title = (float)0.0;
       float pes_description = (float)0.0;
       float pes_text = (float)0.0;
       float pes_keywords = (float)0.0;
     
       if (Configuracio.isVoid()){
          Configuracio.carregaConfiguracio(); 
          }
/* 
       try {
	 Class.forName(Configuracio.nomDriverBD).newInstance();
         } catch (Exception e) {
	 out.println("No es pot carregar el driver.");
	 e.printStackTrace();
         }
*/
       Connection conn = null;
       Statement stmt = null;
       ResultSet rs = null;
  
       try {
	 conn = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
	 stmt = conn.createStatement();
         rs = stmt.executeQuery("select * from pesos where tipusCercador='" + tipusCercador + "'");
         rs.next();        
      
         pes_title = rs.getFloat("pes_title");
         pes_description = rs.getFloat("pes_description");
         pes_text = rs.getFloat("pes_text");
         pes_keywords = rs.getFloat("pes_keywords");
        } catch (Exception e) {
        logger.debug(e);
        } finally {
        try {
         rs.close();
         stmt.close();
         conn.close();
         } catch (Exception e) {
         logger.error(e);
         }
        }
%>        
<jsp:include page="topComplet.jsp" />
     <script language="Javascript">
        function doSubmit(){
           document.modificar.submit();
           }     
        function restaurar(){
           document.modificar.pes_title.value='1.0';
           document.modificar.pes_description.value='1.0';
           document.modificar.pes_text.value='1.0';
           document.modificar.pes_keywords.value='1.0';
           }
     </script>
   <table border="0" class="tableMenuDreta"  cellspacing="5">
      <form name="modificar" action="/<%=Configuracio.contextWebAplicacio%>/ServletPesos" method="post">
         <input type="hidden" name="tipusCercador" value="<%=tipusCercador%>"/>
         <tr>
            <td colspan="2" class="formTitle">Configuració > Pesos</td>
         </tr>   	           
         <tr>
            <td colspan="2"><hr/></td>
         </tr>	           
         <tr>
           <td class="tableText">títol: </td>
           <td><input type="tableText" name="pes_title" value="<%=pes_title%>" class="text"/></td>
         </tr>
         <tr>
           <td class="tableText">descripció: </td>
           <td><input type="tableText" name="pes_description" value="<%=pes_description%>" class="text"/></td>
         </tr>
         <tr>
           <td class="tableText">keywords: </td>
           <td><input type="tableText" name="pes_keywords" value="<%=pes_keywords%>" class="text"/></td>
         </tr>
         <tr>
           <td class="tableText">text: </td>
           <td><input type="tableText" name="pes_text" value="<%=pes_text%>" class="text"/></td>
         </tr>
         <tr>
            <td colspan="2">&nbsp;</td>
         </tr>   	     
         <tr>
            <td align="center"><input type="button" name="Restaurar valors per defecte" value="Restaurar valors per defecte" class="tableButton" onClick="javascript:restaurar();"/></td>
            <td align="center"><input type="button" name="Modificar" value="Modificar" class="tableButton" onClick="javascript:doSubmit();"/></td>
         </tr>   	     
      </form>
   </table>	    

<jsp:include page="bottomComplet.html" />