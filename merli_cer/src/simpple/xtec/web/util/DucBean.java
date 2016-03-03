package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class DucBean {

   // logger
   static final Logger logger = Logger.getLogger(simpple.xtec.web.util.DucBean.class);
	
	/*
   public List getAllAuthors () {
	   ArrayList myList = new ArrayList();
	   myList.add("author1");
	   myList.add("author2");
	   return myList;
       }
   */
   public ArrayList getAllContents (String idDUC, String idLevel, String tipus) {
	   Connection myConnection = null;
	   ArrayList allContents = null;
	   try {
		  Integer idLevelInt = new Integer(idLevel); 
		  Integer idDUCInt = new Integer(idDUC);		  
		  myConnection = UtilsCercador.getConnectionFromPool();
		  
		  // myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
		  if (tipus.equals("area")) {		  
	        allContents = UtilsCercador.getContentsFromDUC (myConnection, idDUCInt.intValue(), idLevelInt.intValue(), "area");
		    } else {
		    allContents = UtilsCercador.getContentsFromDUC (myConnection, idLevelInt.intValue(), idLevelInt.intValue(),"content");		    	
		    }
/*		  DucObject duc1 = new DucObject();
		  duc1.id = 1111;
		  duc1.term = "aaaa" + idLevel;
		  DucObject duc2 = new DucObject();
		  duc2.id = 2222;
		  duc2.term = "bbbb";
		  allContents = new ArrayList();
		  allContents.add(duc1);
		  allContents.add(duc2);		  
	*/	  
	      } catch (Exception e) {
		  logger.error(e);
	      } finally {
	      try {
	    	myConnection.close();  
	        } catch (Exception e) {
	        logger.error(e);	
	        }
	      }
	   return allContents;   
       }
   }