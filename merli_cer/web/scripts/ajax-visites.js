    var httpRequest;
  

   /**
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function addVisita(contextWeb, idRecurs)
    {
        var url = "/" + contextWeb + '/ServletVisites';
        if (window.ActiveXObject)
        {
            httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else if (window.XMLHttpRequest)
        {
            httpRequest = new XMLHttpRequest();
        }
        
        httpRequest.open("POST", url, false);   // true -> async
        httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=ISO-8859-1');        
        httpRequest.onreadystatechange = function() {processRequest(); } ;

        httpRequest.send("idRecurs=" + idRecurs);
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

        if (httpRequest.readyState == 4)
        {

            if(httpRequest.status == 200)
            {

            	// document.location.href = myUrl;
                //get the XML send by the servlet
              //  var profileXML = httpRequest.responseXML.getElementsByTagName("Profile")[0];
                
                //Update the HTML
                // updateHTML(profileXML);
            }
            else
            {
                alert("Error loading page\n"+ httpRequest.status +":"+ httpRequest.statusText);
            }
        }
    }

