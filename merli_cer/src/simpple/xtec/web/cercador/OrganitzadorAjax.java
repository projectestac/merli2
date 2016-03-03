package simpple.xtec.web.cercador;

import java.util.*;
import java.io.*; 
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.SoapManager;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;
/**
 * This servlet handles the organizer ajax request 
 */

public class OrganitzadorAjax extends HttpServlet 
{    
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.OrganitzadorAjax.class);
	
    protected void doPost(HttpServletRequest requestObj, HttpServletResponse responseObj)
                   throws IOException
    {
    	try {
          logger.debug("doPost -> in");   
          	HttpSession session = requestObj.getSession();      
         // String indexActual = (String)session.getAttribute("indexActual");
         String indexActual = null;
         if (indexActual == null) {
           indexActual = UtilsCercador.getIndexActual(Configuracio.indexDir, Configuracio.indexDir2);
           }
          logger.debug("Index actual: "  + indexActual);          		
          // set the content type
          responseObj.setContentType("text/xml");       
          responseObj.setHeader("Cache-Control", "no-cache");
        
          //get the PrintWriter object to write the response
          PrintWriter writer = responseObj.getWriter();
          logger.debug("Getting params..");
          //get parameters store into the hashmap
          HashMap paramsMap = new HashMap();
          Enumeration paramEnum = requestObj.getParameterNames();
          while(paramEnum.hasMoreElements())
          {
            String paramName = (String)(paramEnum.nextElement());
            paramsMap.put(paramName, requestObj.getParameter(paramName));
            }
          // get the params from the hashtable
          String nomUsuari= (String)paramsMap.get("nomUsuari");
          logger.debug("nomUsuari: " + nomUsuari);                    
          String id= (String)paramsMap.get("id");
          logger.debug("id: " + id);
          String sheetId= (String)paramsMap.get("sheetId");
          logger.debug("sheetId: " + sheetId);          
               
          // Call the organizer web service
          SoapManager mySoapManager = new SoapManager(indexActual);
          logger.debug("Calling webservice...");
          if (mySoapManager.doCall (nomUsuari, id, sheetId)) {          
            writer.println("<Feedback><![CDATA[Recurs afegit correctament");
            writer.println("]]></Feedback>\n");
            } else {
            writer.println("<Feedback><![CDATA[El recurs no s'ha pogut afegir");
            writer.println("]]></Feedback>\n");            
            }
  
          writer.close();       
    	  } catch (Exception e) {
    	  logger.error(e);	
    	  }
       logger.debug("doPost -> out");    	  
       }        

}