<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList" %>
<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador,simpple.xtec.web.util.XMLCollection, simpple.xtec.web.util.DucObject" %>
<%@ page pageEncoding="UTF-8" %>
<jsp:useBean id="DucBean" scope="session" class="simpple.xtec.web.util.DucBean" />
<%
  if (Configuracio.isVoid()) {
      Configuracio.carregaConfiguracio();
      }  
  Logger logger = Logger.getLogger("ducTest.jsp");
  
  //Class.forName(Configuracio.nomDriverBD).newInstance();
  //Connection myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
  
  String sLang = XMLCollection.getLang(request);
  Connection myConnection = UtilsCercador.getConnectionFromPool();  
  ArrayList allLevels = UtilsCercador.getAllLevels(myConnection);      
  
  try {
%>
<html>
  <head>
	<script language="Javascript">
	<% int i = 0;
	   while (i < allLevels.size()) {
	     DucObject ducLevel = (DucObject)allLevels.get(i); %>
	     var areas_<%=ducLevel.id%>=new Array("<%=XMLCollection.getProperty("cerca.select.nivell", sLang)%>", "-1"
	     <%
	     ArrayList allAreas = UtilsCercador.getAreasFromLevel(myConnection, ducLevel.id);
	     int j = 0;
	     while (j < allAreas.size()) {
	       DucObject ducArea = (DucObject)allAreas.get(j);
	     %>
	     ,"<%=ducArea.getTerm(sLang)%>","<%=ducArea.id%>"
	      <%
	        j ++;
	        }
	       %>
	       );
	<%   i ++;
	     }   %>
	   	   	   
	function change_area(){
	    //tomo el valor del select del pais elegido
	    var nivell_educatiu
	    nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value
	    //miro a ver si el pais está definido
	    if (nivell_educatiu != 0) {


	       mis_areas=eval("areas_" + nivell_educatiu)

	       num_areas = (mis_areas.length / 2)


	       document.cerca.area_curricular.length = num_areas
	       //para cada provincia del array, la introduzco en el select
	       for(i=0;i<(num_areas * 2);i=i+2){
	          document.cerca.area_curricular.options[i/2].text=mis_areas[i]
	          document.cerca.area_curricular.options[i/2].value=mis_areas[i + 1]	          
	       }
	    }else{
	       //si no había provincia seleccionada, elimino las provincias del select
	       document.cerca.area_curricular.length = 1
	       //coloco un guión en la única opción que he dejado
	       document.cerca.area_curricular.options[0].value = "-1"
	       document.cerca.area_curricular.options[0].text = "<%=XMLCollection.getProperty("cerca.select.area", sLang)%>"
	    }
	    //marco como seleccionada la opción primera de provincia
	    document.cerca.area_curricular.options[0].selected = true
	} 

    var httpRequest;
  
   /**
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function getProfile(authorSelected)
    {
        var url = '/CercadorEducacio/ServletDUC?author=' + authorSelected;

        if (window.ActiveXObject)
        {
            httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else if (window.XMLHttpRequest)
        {
            httpRequest = new XMLHttpRequest();
        }
        
        httpRequest.open("GET", url, true);
        httpRequest.onreadystatechange = function() {processRequest(); } ;
        httpRequest.send(null);
   }
  
   /**
    * This is the call back method
    * If the call is completed when the readyState is 4
    * and if the HTTP is successfull when the status is 200
    * update the profileSection DIV
    */
    function processRequest()
    {
        if (httpRequest.readyState == 4)
        {
            if(httpRequest.status == 200)
            {
                //get the XML send by the servlet
                var profileXML = httpRequest.responseXML.getElementsByTagName("Profile")[0];
                
                //Update the HTML
                updateHTML(profileXML);
            }
            else
            {
                alert("Error loading page\n"+ httpRequest.status +":"+ httpRequest.statusText);
            }
        }
    }
       
   /**
    * This function parses the XML and updates the 
    * HTML DOM by creating a new text node is not present
    * or replacing the existing text node.
    */
    function updateHTML(profileXML)
    {

        //The node valuse will give actual data
        var profileText = profileXML.childNodes[0].nodeValue;
	    // alert(profileText);
        //Create the Text Node with the data received
        var profileBody = document.createTextNode(profileText);
	//    alert(profileBody);
        //Get the reference of the DIV in the HTML DOM by passing the ID
        var profileSection = document.getElementById("profileSection");
           
        //Check if the TextNode already exist
        if(profileSection.childNodes[0])
        {


            //If yes then replace the existing node with the new one
      //      profileSection.replaceChild(profileBody, profileSection.childNodes[0]);
            profileSection.innerHTML = profileText;
        }
        else
        {

            //If not then append the new Text node
            profileSection.appendChild(profileBody);
        }       
    }
       
	
		
	</script>
  </head>
  <body>  
<form name="cerca">
Nivells
<br/>
<select id="nivell_educatiu" onchange="change_area()">
  <option value="-1"><%=XMLCollection.getProperty("cerca.select.area", sLang)%></option>
<% i = 0;
   while (i < allLevels.size()) {
      DucObject ducLevel = (DucObject)allLevels.get(i); %>
      <option value="<%=ducLevel.id%>"><%=ducLevel.getTerm("idioma")%></option>
<%    i ++;
      }      
      %>
</select> 
<br/>
<br/>
Arees
<br/>
<!-- <select id="area_curricular" ONCHANGE="getProfile(this.options[this.selectedIndex].value)"> -->
<select id="area_curricular" ONCHANGE="getProfile(document.cerca.nivell_educatiu.options[this.selectedIndex].value)">
  <option value="-1"><%=XMLCollection.getProperty("cerca.select.nivell", sLang)%></option>
</select> 
<br/>
<br/>
Continguts
<br/>

<table border="1">
    <div id="profileSection">
          <br><br>
    <div>
</table>

<br/>
<br/>

<%
    } catch (Exception e) {
	logger.error(e);
    }
%> 
</form>
</body>
</html>