var req;
var message;
 
function validate()
	{
    var idField = document.getElementById("userid");
    var url = "Responder.do?id=" + escape(idField.value);
    if (window.XMLHttpRequest)
    	{
        req = new XMLHttpRequest();
    	}
    else if (window.ActiveXObject)
    	{
        req = new ActiveXObject("Microsoft.XMLHTTP");
   		}//alert(url);
   req.open("GET", url, true);
   req.onreadystatechange = callback;
   req.send(null);
	}
 
function callback(){
  	if (req.readyState == 4){
		if (req.status == 200){
           // alert(req.responseXML.getElementsByTagName("message")[0].childNodes[0].nodeValue);
            mdiv = document.getElementById("userIdMessage");
    		if (req.responseXML.getElementsByTagName("message")[0].childNodes[0].nodeValue == "invalid")
    			{
       			mdiv.innerHTML = "<div style=\"color:red\">Invalid User Id<>";
    			}
    		else
    			{
      			mdiv.innerHTML = "<div style=\"color:green\">Valid User Id<>";
    			}
		}else{alert("inner"+req.status)}
	}
        //else{alert("outer"+req.readyStat)}
}
 
//function parseMessage()
	//{
    //message = req.responseXML.getElementsByTagName("message")[0];
    //setMessage(message.childNodes[0].nodeValue);
	//}