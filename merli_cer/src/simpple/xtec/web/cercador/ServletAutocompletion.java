package simpple.xtec.web.cercador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.io.*; 
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.UtilsCercador;



/**
 * This servlet handles the tesaurus ajax request 
 */

public class ServletAutocompletion extends HttpServlet 
{    
	
	private static final long serialVersionUID = 1L;

	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.UtilsCercador.class);
	
	
	/**
	 * Return a list of words that matches with the given string 
	 */

	public static ArrayList getWordsAutocompletion (String startString, String sLang) {
 	    Connection myConnection = null;
 	    Statement stmt = null;
 	    ResultSet rs = null;
 	    String sql = "";
 	    ArrayList wordList = null;
 	    try {
 	    	logger.debug("getWordsAutocompletion -> in");
 	    	wordList = new ArrayList(); 	    	
 	    	myConnection = UtilsCercador.getConnectionFromPool();
 	        stmt = myConnection.createStatement();
 	        sql = "SELECT distinct paraula FROM tesaurus WHERE paraula LIKE '" + startString + "%' AND idioma='"+sLang+"'";
 	        logger.info(sql);
 	        rs = stmt.executeQuery(sql); 	        
 	        while (rs.next()) {
 	        	wordList.add((String)rs.getString("paraula"));
 	            }
 	        
 	       } catch (Exception e) {
 	  	   logger.error(e);
 	  	   } finally {
 	  	   try {
 	  	     if (rs != null) {
 	   	  	   rs.close();
 	  	       }
 	  	     if (stmt != null) {
 	  	  	   stmt.close();
 	  	       }
 	  	     if (myConnection != null) {
 	  	     	myConnection.close();
 	 	       }
 	   	     } catch (Exception e) {
 	  	     logger.error(e);	
 	  	     }
 	  	   }
	    logger.debug("getWordsAutocompletion -> out"); 	  	   
 	  	return wordList;
   	}
	

	/**
	 * Handles the request 
	 */
	
    protected void doGet(HttpServletRequest requestObj, HttpServletResponse responseObj)
                   throws IOException
    {
      try {
    	// Get current lang
    	String sLang = getLang(requestObj);
    	  
    	logger.debug("doGet -> in");  
        //set the content type
		 requestObj.setCharacterEncoding("UTF-8");

        //set the content type
        responseObj.setContentType("text/xml;charset=utf-8");       
        responseObj.setHeader("Cache-Control", "no-cache");
        
        //get the PrintWriter object to write the html page
        PrintWriter writer = responseObj.getWriter();
        String s = requestObj.getParameter("s");  
        logger.debug("string retrieved: " + s);
       
        String outputXML = "";
        outputXML += "<Resultat><![CDATA[";        
/*        outputXML += "<body>";
        outputXML += "<div id=\"autocomplete-popup\" class=\"autocomplete\">"; */
        outputXML += "<ul>"; 
//        outputXML += "  <table>";
      
//        outputXML += "<list>\n";
        ArrayList wordList = getWordsAutocompletion(s, sLang);
        int i = 0;
        while (i < wordList.size()) {
  //      	outputXML += "    <tr><td>" + wordList.get(i) + "</td></tr>";
           outputXML += "<li>" + wordList.get(i) + "</li>";
           //outputXML += "    <name>" + wordList.get(i) + "</name>";
           i ++;
           }
//        outputXML += "</list>\n";        
       outputXML += "</ul>";
        //outputXML += "  </table>";
        /*  outputXML += "</div>";
        outputXML += "</body>"; */ 
        outputXML += "]]></Resultat>\n";      

        logger.debug(outputXML);
        
        writer.println(outputXML);
        //close the write
        writer.flush();
        writer.close();
        } catch (Exception e) {
        logger.error(e);	
        }
    logger.debug("doGet -> out");        
    }    
    
    protected String getLang(HttpServletRequest oRequest){
    	String sLang = "ca";
    	if (oRequest.getParameter("lang")!=null){
    		sLang = oRequest.getParameter("lang");
    		oRequest.getSession().setAttribute("lang", sLang);
    	}else if (oRequest.getSession().getAttribute("lang")!=null){
    		sLang = (String)oRequest.getSession().getAttribute("lang");
    	}
    	return sLang;
    }



}