<HTML>
<HEAD>
<TITLE>
    AJAX DEMO : Getting JavaReference Author Profile using Ajax interaction
</TITLE>
</HEAD>

<%@ page import="java.util.*" %>
<jsp:useBean id="AuthorsBean" scope="session" class="simpple.xtec.web.util.AuthorsBean" />

<script type="text/javascript">
    var httpRequest;
  
   /**
    * This method is called when the author is selected
    * It creates XMLHttpRequest object to communicate with the 
    * servlet 
    */
    function getProfile(authorSelected)
    {
        var url = '/CercadorEducacio/ServletAjax?author=' + authorSelected;

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

<BODY>
<%
    //get author list
    List authors = AuthorsBean.getAllAuthors();
%>

<TABLE align=left border=0 cellPadding=3 cellSpacing=1 width="100%" >
    <TR>
        <TD align="center">
            <STRONG>Getting JavaReference Author Profile using Ajax interaction.</STRONG>
            <br>
        </TD>
    </TR>
    <TR bgColor="#C6D3E7">
        <TD>
            <SELECT id=authors name=authorComboBox ONCHANGE="getProfile(this.options[this.selectedIndex].value)"> 
             <% Iterator it = authors.iterator();                
            while(it.hasNext())
            {
                String authorName = (String)(it.next()); %>
                    
                <OPTION value='<%=authorName%>' ><%=authorName%></OPTION>
            <%
            }%>      
            </SELECT>
            &nbsp;&nbsp;&nbsp;<<<&nbsp;Select Author
        </TD>
    </TR>
    <TR bgColor="#FFD0B1">
        <TD>
            <div id="profileSection">
                <br><br>
            <div>
            
            <script type="text/javascript">
                getProfile(authorComboBox.options[authorComboBox.selectedIndex].value);
            </script>
            
        </TD>
    </TR>
</TABLE>    

</BODY>
</HTML> 