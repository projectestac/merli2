<%@ page import="java.sql.*,simpple.xtec.web.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
      String tipusCercador = "edu365"; 

      int highlightFragmentSizeInBytes = -1;
      int maxNumFragmentsRequired = -1;
      String fragmentSeparator = "";
      int maxSizeFragment = -1;
      int resultatsPagina = -1;
      
      Logger logger = Logger.getLogger("fragments.jsp");
      
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

     String sql = "";
  
     try {

	conn = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
	stmt = conn.createStatement();
        sql = "select * from configuracio where tipusCercador='" + tipusCercador + "'";
        rs = stmt.executeQuery(sql);
        rs.next();
        highlightFragmentSizeInBytes = rs.getInt("highlightFragmentSizeInBytes");
        maxNumFragmentsRequired = rs.getInt("maxNumFragmentsRequired");
        fragmentSeparator = rs.getString("fragmentSeparator");
        maxSizeFragment = rs.getInt("maxSizeFragment");
        resultatsPagina = rs.getInt("numResultatsPerPagina");
        } catch (Exception e) {
        logger.error(e);
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
<jsp:include page="topEdu365.jsp" />

     <script language="Javascript">
        function doSubmit(){
           var value1 = document.modificar.highlightFragmentSizeInBytes.value;
           if (isNaN(value1)){
               alert('El valor del primer camp ha de ser un número enter');
               return;
               }             
           var value2 = document.modificar.maxNumFragmentsRequired.value;
           if (isNaN(value2)){
               alert('El valor del segon camp ha de ser un número enter');
               return;
               }
           document.modificar.submit();
           }
     </script>
   <table class="tableMenuDreta"  cellspacing="5">
      <form name="modificar" action="/<%=Configuracio.contextWebAplicacio%>/ServletFragments" method="post">
         <input type="hidden" name="tipusCercador" value="<%=tipusCercador%>"/>
         <tr>
            <td colspan="2" class="formTitle">Configuració > Fragments</td>
         </tr>
         <tr>
            <td colspan="2"><hr/></td>
         </tr>
         <tr>
           <td class="tableText">Tamany dels fragments (en bytes): </td>
           <td><input type="tableText" name="highlightFragmentSizeInBytes" value="<%=highlightFragmentSizeInBytes%>" class="text"/></td>
         </tr>
         <tr>
           <td class="tableText">Tamany total màxim d'un fragment (en bytes):: </td>
           <td><input type="tableText" name="maxSizeFragment" value="<%=maxSizeFragment%>" class="text"/></td>
         </tr>
         
         <tr>
	   <td class="tableText">Número màxim de fragments:</td>
	   <td><input type="tableText" name="maxNumFragmentsRequired" value="<%=maxNumFragmentsRequired%>" class="text"/></td>
	 </tr>   
         <tr>
            <td class="tableText">Separador de fragments:</td>
            <td><input type="tableText" name="fragmentSeparator" value="<%=fragmentSeparator%>" class="text"/></td>            
         </tr>	   
         <tr>
            <td class="tableText">Resultats per pàgina:</td>
            <td><input type="tableText" name="resultatsPagina" value="<%=resultatsPagina%>" class="text"/></td>            
         </tr>	   
         <tr>
            <td colspan="2">&nbsp;</td>
         </tr>   	     
         <tr>
            <td colspan="2" align="center"><input type="button" name="Modificar" value="Modificar" class="tableButton" onClick="javascript:doSubmit();"/></td>
         </tr>   	     
      </form>
   </table>
<jsp:include page="bottomEdu365.html" />