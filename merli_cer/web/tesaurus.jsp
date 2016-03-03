<%@ page import="simpple.xtec.web.util.Configuracio" %>
<html>
<head>
<script type="text/javascript">
    var httpRequest;
  
   /**
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function getProfile(authorSelected)
    {
        var url = '/<%=Configuracio.contextWebAplicacio%>/ServletAjax?author=' + authorSelected;

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
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function addVisita(idRecurs, myUrl)
    {
        var url = '/<%=Configuracio.contextWebAplicacio%>/ServletVisites?idRecurs=' + idRecurs;

        if (window.ActiveXObject)
        {
            httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else if (window.XMLHttpRequest)
        {
            httpRequest = new XMLHttpRequest();
        }
        
        httpRequest.open("POST", url, true);   // async
        httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');        

        httpRequest.onreadystatechange = function() {processRequest(); } ;
        httpRequest.send(null);
        // document.location.href = myUrl;
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
           
        //Create the Text Node with the data received
        var profileBody = document.createTextNode(profileText);
                      
        //Get the reference of the DIV in the HTML DOM by passing the ID
        var profileSection = document.getElementById("profileSection");
           
        //Check if the TextNode already exist
        if(profileSection.childNodes[0])
        {
            //If yes then replace the existing node with the new one
            profileSection.replaceChild(profileBody, profileSection.childNodes[0]);
        }
        else
        {
            //If not then append the new Text node
            profileSection.appendChild(profileBody);
        }       
    }
       
</script>

<script type="text/javascript" src="scripts/liveUpdater.js">
</script>
<link rel="stylesheet" type="text/css" href="css/liveUpdater.css"
  media="all"/>
</head>
<body>
<input autocomplete="off" id="autocomplete-input" value="" type="text"/>

<div id="autocomplete-popup" class="autocomplete"><span></span></div>
   <script type="text/javascript">
    	<%
    		String sURL = "/"+Configuracio.contextWebAplicacio+"/ServletAutocompletion";
    		if (request.getParameter("lang")!=null){
    			sURL+="?lang="+request.getParameter("lang");
    		}
    	%>
        autocomplete('autocomplete-input', 'autocomplete-popup', '<%=sURL%>')
   </script>
   
</body>   
</html>