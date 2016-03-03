package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;



public class Inicial {
	
	 // logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.Inicial.class);
	Connection myConnection = null;
	
	public Inicial (Connection myConnection) {
	   if (Configuracio.isVoid()) {
		  Configuracio.carregaConfiguracio();
		  }
	   this.myConnection = myConnection;
	   }
	
    public Document getDocument (IndexSearcher mySearcher, String idRecurs) throws SQLException {
        QueryParser myQueryParser = null;
        String textQuery = "";
        Query queryRecurs = null;
        Hits resultats = null;
        Document resultat = null;
        try {
          myQueryParser = new QueryParser("idRecurs", new StandardAnalyzer());
          textQuery = "(idRecurs:\"" + idRecurs + "\")";
          logger.debug(textQuery);
          queryRecurs = myQueryParser.parse(textQuery);
      	  resultats = mySearcher.search(queryRecurs);  
      	  resultat = (Document)resultats.doc(0);        
          } catch (Exception e) {
          logger.error(e);
          } 
        return resultat;
        }   	
    

     
   public ArrayList getUltimsComentaris () {
    	String sqlQuery = "";

	    Statement stmt = null;
	    ResultSet rs = null;
	    ArrayList allComentaris = null;
	    try {
	      if (Configuracio.isVoid()) {
		    Configuracio.carregaConfiguracio();  
	        }
	      allComentaris = new ArrayList();


	      stmt = myConnection.createStatement();
	      sqlQuery = "SELECT * FROM (SELECT id, titol, puntuacio, xtec_username, comentari, data_edicio, recurs_id, ROW_NUMBER() OVER (ORDER by data_edicio DESC) R FROM comentaris WHERE suspens=0 ) WHERE R <=5";
	      logger.debug(sqlQuery);
	      rs = stmt.executeQuery (sqlQuery);
	      while (rs.next()) {
	    	 ComentariObject comentariObject = new ComentariObject();
	    	 comentariObject.id = (long)rs.getLong("id");
	    	 comentariObject.setComentari((String)rs.getString("comentari"), 60);
	    	 comentariObject.titol = (String)rs.getString("titol");           
	         comentariObject.puntuacio = (int)rs.getInt("puntuacio");
	         comentariObject.autor = (String)rs.getString("xtec_username");
	         comentariObject.dataEdicio = (String)rs.getString("data_edicio");	    	 
	         comentariObject.idRecurs = (String)rs.getString("recurs_id");	         
             allComentaris.add(comentariObject);
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
       } catch (Exception e) {
       logger.error(e);	            	
       }
	  }
	return allComentaris;
   }      


   public ArrayList getRecomanacionsFromRecurs(String idRecurs) {
   	String sqlQuery = "";

	    Statement stmt = null;
	    Statement stmt2 = null;	    
	    ResultSet rs = null;
	    ResultSet rs2 = null;	    
	    ArrayList allRecursos = null;
	    try {
	      if (Configuracio.isVoid()) {
		    Configuracio.carregaConfiguracio();  
	        }
	      allRecursos = new ArrayList();


	      stmt = myConnection.createStatement();
	      sqlQuery = "SELECT * FROM recomanacions WHERE recursid='" + idRecurs + "'";
	      rs = stmt.executeQuery (sqlQuery);
	      if (rs.next()) {
	      //if (1 == 1) {
	    	  String recursosRecomanats = rs.getString("recursosRecomanats");
	    	  //String recursosRecomanats = "1812 2120 2000 1000";
	    	  StringTokenizer myTokenizer = new StringTokenizer(recursosRecomanats);
	    	  RecursObject recursObject = null;
 	    	  while (myTokenizer.hasMoreTokens()) {
 	    		 String recomanat = (String)myTokenizer.nextToken();
 	       	     recursObject = new RecursObject();
	    	     recursObject.id = recomanat; 	
	    	  //   stmt.close();
	    	     stmt2 = myConnection.createStatement();
	    	     logger.debug("SELECT avg(puntuacio) from comentaris where recurs_id=" + recomanat);
	    	     rs2 = stmt2.executeQuery("SELECT avg(puntuacio) from comentaris where recurs_id=" + recomanat);
	    	     if (rs2.next()) {
	    	       recursObject.puntuacio = rs2.getInt(1);	 
	    	       }
	    	     rs2.close();
	    	     logger.debug("SELECT titol from recursos where id=" + recomanat);	    	     
	    	     rs2 = stmt2.executeQuery("SELECT titol from recursos where id=" + recomanat);
	    	     if (rs2.next()) {
	    	       recursObject.titol = rs2.getString("titol");	 
	    	       }
	    	     
	    	     allRecursos.add(recursObject);
	    	     stmt2.close();
 	    	     }
	          } 
	  } catch (Exception e) {
	  logger.error("1 -> " + e);
	  } finally {
    try {
    	if (rs != null) {
    	      logger.error("21 ->");    		
     	   rs.close();
     	   }
    	// rs2 closed when stm2 closes
    /*	if (rs2 != null) {
  	      logger.error("22 ->");    		
      	   rs2.close();
      	   }
    	*/
  	if (stmt != null) {  		
  	   stmt.close();
  	   }
  /*	if (stmt2 != null) {
	      logger.error("23 ->");  		
	   stmt2.close();
	   }        	
  	*/
      } catch (Exception e) {
      logger.error(e);	            	
      }
	  }
	return allRecursos;
  }      
   
   
   
   public Hashtable getNivellsArees (String duc, String sLang) {
   	   String sqlQuery = "";
   	   String sqlQueryNivell = "";   	   
	   Statement stmt = null;
	   ResultSet rs = null;
	   ResultSet rsNivell = null;
	   Hashtable results = null;
	   String ducItem = "";
	   int parent_id = -1;
	   try {
	      if (Configuracio.isVoid()) {
		    Configuracio.carregaConfiguracio();  
	        }
	      results = new Hashtable();

	      stmt = myConnection.createStatement();
	      
	      StringTokenizer myTokenizer = new StringTokenizer(duc);
	      while (myTokenizer.hasMoreTokens()) {
	    	 ducItem = (String)myTokenizer.nextToken(); 
	    	 ducItem = ducItem.trim();
	    	 sqlQuery = "SELECT parent_id FROM duc_info WHERE id=" + ducItem + " AND relationType='RAL'";
	    	 logger.debug(sqlQuery);
	    	 rs = stmt.executeQuery (sqlQuery);
	    	 if (rs.next()) {
	    		 parent_id = rs.getInt("parent_id");
	    		 String nomArea = UtilsCercador.getTermDuc(myConnection, "" + ducItem,sLang);
	    		 String nomNivell = UtilsCercador.getTermDuc(myConnection, "" + parent_id,sLang);
	             // XTEC: Added this check to hide Competency area
	    		 //if (nomArea!=null && !nomArea.startsWith("Competències")){
	    			 results.put(nomNivell, nomArea);
	    		 //}
	    		 results.put(nomNivell, nomArea);
    	     }
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
      } catch (Exception e) {
      logger.error(e);	            	
      }
	  }
	return results;
  }      
   
   public int getComentarisSuspesos (String login) {
       int comentarisSuspesos = 0;
       Statement stmt = null;
       ResultSet rs = null;
       String sql = "";
       try {
    	   stmt = myConnection.createStatement();
    	   sql = "SELECT count(*) FROM comentaris WHERE xtec_username='" + login + "' and suspens=1";
    	   logger.debug(sql);
    	   rs = stmt.executeQuery(sql);
    	   if (rs.next()) {
    		  comentarisSuspesos = rs.getInt(1);  
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
          
         } catch (Exception e) {        	
         }
        }
       return comentarisSuspesos; 
       }

	/*
	 * DEPRECATED
	 */
   public static String getImageFormat (String format) {
   	String imgUrl = "";
   	try {
   		if (format.trim().equalsIgnoreCase("Vídeo") || format.trim().equalsIgnoreCase("V?deo")) {
   		   imgUrl = "video.gif";
   		   }
   		if (format.trim().equalsIgnoreCase("Interactiu Flash")) {
   		   imgUrl = "flash.gif";
   		   }
   		if (format.trim().equalsIgnoreCase("Pàgina web") || format.trim().equalsIgnoreCase("P?gina web")) {
   		   imgUrl = "web.gif";
   		   }
   		if (format.trim().equalsIgnoreCase("Imatge")) {
   		   imgUrl = "imatges.gif";
   		   }
   		if (format.trim().equalsIgnoreCase("PDF")) {
   		   imgUrl = "pdf.gif";
   		   }		
   		if (format.trim().equalsIgnoreCase("Àudio") || format.trim().equalsIgnoreCase("?udio")) {
    		   imgUrl = "audio.gif";
    		   }
    		if (format.trim().equalsIgnoreCase("Fitxer comprimit")) {
    		   imgUrl = "compressio.gif";
    		   }
    		if (format.trim().equalsIgnoreCase("Full de càlcul") || format.trim().equalsIgnoreCase("Full de c?lcul")) {
    		   imgUrl = "calcul.gif";
    		   }		
    		if (format.trim().equalsIgnoreCase("Presentació") || format.trim().equalsIgnoreCase("Presentaci?")) {
     		   imgUrl = "presentacion.gif";
     		   }		
    		if (format.trim().equalsIgnoreCase("Document de text")) {
      		   imgUrl = "text.gif";
      		   }		

   	 } catch (Exception e) {
   	 logger.error(e);	
   	 }
     return imgUrl;	
   }
   
}