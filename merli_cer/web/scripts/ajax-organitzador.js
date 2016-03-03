    var httpRequest;
  

   /**
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function addToOrganitzador(contextWeb, nomUsuari, id, sheetId)
    {
        var url = '/' + contextWeb + '/OrganitzadorAjax';

        if (window.ActiveXObject)
        {
	       
            httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else if (window.XMLHttpRequest)
        {
            httpRequest = new XMLHttpRequest();
        }

        httpRequest.open("POST", url, true);   // true -> async

        httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');        

        httpRequest.onreadystatechange = function() {processRequest(); } ;
        //Get the reference of the DIV in the HTML DOM by passing the ID
        var feedbackSection = document.getElementById("afegirOrganitzador");
           
        feedbackSection.innerHTML = "Afegint recurs....";
        httpRequest.send("nomUsuari=" + nomUsuari + "&id=" + id + "&sheetId=" + sheetId);
                //get the XML send by the servlet
                var feedbackXML = httpRequest.responseXML.getElementsByTagName("Feedback")[0];

                //Update the HTML
                updateHTML(feedbackXML);        
    //    httpRequest.send("idRecurs=" + idRecurs);
        // alert('b');
        //document.location.href = myUrl;
   }

  
   /**
    * This is the call back method
    * If the call is completed when the readyState is 4
    * and if the HTTP is successfull when the status is 200
    * update the profileSection DIV
    */
    function processRequest()
    {
    	// alert('a');
        if (httpRequest.readyState == 4)
        {
            if(httpRequest.status == 200)
            {
            	
                //get the XML send by the servlet
                var feedbackXML = httpRequest.responseXML.getElementsByTagName("Feedback")[0];

                //Update the HTML
                updateHTML(feedbackXML);
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
    function updateHTML(feedbackXML)
    {

        //The node valuse will give actual data
        var feedbackText = feedbackXML.childNodes[0].nodeValue;

        //Create the Text Node with the data received
        var feedbackBody = document.createTextNode(feedbackText);

        //Get the reference of the DIV in the HTML DOM by passing the ID
        var feedbackSection = document.getElementById("afegirOrganitzador");
           
        feedbackSection.innerHTML = feedbackText;
    }    
    
