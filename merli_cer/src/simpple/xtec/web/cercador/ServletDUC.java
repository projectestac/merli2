package simpple.xtec.web.cercador;

import java.util.*;
import java.io.*; 
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.DucBean;
import simpple.xtec.web.util.DucObject;
import simpple.xtec.web.util.XMLCollection;

/*
 * Handles the duc ajax requests
 */

public class ServletDUC extends HttpServlet 
{    

	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.ServletDUC.class);
	
	public String splitTerm (String term) {
	  String newTerm = "";
	  int currentLength = 0;
	  try {
		StringTokenizer myTokenizer = new StringTokenizer(term);
		while (myTokenizer.hasMoreTokens()) {
           if ((currentLength > 30) && (currentLength > 0)){
         	   currentLength = 0;
         	   newTerm += "<br/>";
               } else {
               newTerm += " ";  
               }		
           String currentTerm = (String)myTokenizer.nextToken();
           newTerm += currentTerm;
           currentLength += currentTerm.length();
		   }
		} catch (Exception e) {
		logger.error(e);	
		}
	  return newTerm;	
	}
	
    protected void doGet(HttpServletRequest requestObj, HttpServletResponse responseObj)
                   throws IOException
    {
    	try {
    	String sLang = XMLCollection.getLang(requestObj);
    	if (sLang==null)
    		sLang="ca";
    	
		 requestObj.setCharacterEncoding("UTF-8");

        //set the content type
        responseObj.setContentType("text/xml;charset=utf-8");       
        responseObj.setHeader("Cache-Control", "no-cache");
        
        //get the PrintWriter object to write the html page
        PrintWriter writer = responseObj.getWriter();
        
        //get parameters store into the hashmap
        HashMap paramsMap = new HashMap();
        Enumeration paramEnum = requestObj.getParameterNames();
        while(paramEnum.hasMoreElements())
        {
            String paramName = (String)(paramEnum.nextElement());
            paramsMap.put(paramName, requestObj.getParameter(paramName));
        }
        //get parameters
        String ducId= (String)paramsMap.get("ducId");
        logger.debug("ducId: " + ducId);
        String levelId= (String)paramsMap.get("levelId");
        logger.debug("levelId: " + levelId);
        
        String tipus= (String)paramsMap.get("tipus");
        logger.debug("tipus: " + tipus);        
        
        //creating the bean
        DucBean ducBean = new DucBean();
        
        //writing the result
        writer.println("<Resultat><![CDATA[");
        ArrayList allContents = ducBean.getAllContents(ducId, levelId, tipus);
        int i = 0;
        DucObject ducObject = null;
 
        if (tipus.equals("area")) {
          //writer.println("<table border=\"0\">");
          //writer.println("<tr><td>\n");
          }
        writer.println("<ul>\n");

        while (i < allContents.size()) {
            ducObject = (DucObject)allContents.get(i);
            String term = ducObject.getTerm(sLang);
            //term = splitTerm(term);
            if (ducObject.hasChilds) {
           	   String urlJavascript= "javascript:getProfile('" + ducObject.id + "', '" + ducObject.id + "', 'content');";            	
          //     writer.println("   <li><a href=\"" + urlJavascript + "\"><span id=\"" +  ducObject.id + "Text\">+</span></a><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\"/>" + ducObject.term + "(" + ducObject.id + ")<span id=\"" + ducObject.id + "\"></span></li>");
           	   writer.println("   <li><a href=\"" + urlJavascript + "\"><span id=\"" +  ducObject.id + "Text\" >+</span></a><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\"/> " + term + "<span id=\"" + ducObject.id + "\"></span></li>");
               } else {
            //   writer.println("   <li><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\"/>" + ducObject.term + "(" + ducObject.id + ")<span id=\"" + ducObject.id + "\"></span></li>");            	   
               //writer.println("   <li><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\"/>" + ducObject.term + "<span id=\"" + ducObject.id + "\"></span></li>");
            	 //  writer.println("   <li><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\">" + ducObject.term + "</input><span id=\"" + ducObject.id + "\"></span></li>");
               writer.println("   <li><span id=\"" +  ducObject.id + "Text\" >&nbsp;&nbsp;</span><input type=\"checkbox\" name=\"ducContent\" value=\"" + ducObject.id + "\"> " +  term + "</input><span id=\"" + ducObject.id + "\"></span></li>");
            	   
               }
        
            i ++;
            }
        writer.println("  </ul>\n");
        if (tipus.equals("area")) {
          //writer.println("</td></tr>\n");                      
          //writer.println("</table>\n");
          }
        
        writer.println("]]></Resultat>\n");        
        writer.flush();        
        writer.close();
    	} catch (Exception e) {
    	logger.error(e);	
    	}
    }        


}