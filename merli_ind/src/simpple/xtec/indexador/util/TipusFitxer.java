package simpple.xtec.indexador.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class TipusFitxer {
   
   public static Hashtable allTipusFitxer = null;	
   // logger
   static final Logger logger = Logger.getLogger(simpple.xtec.indexador.util.TipusFitxer.class);

   
   public static String getGrupFormat (String mimeType) {
	   String tipusFitxer = "";
	   try {
		 mimeType = mimeType.trim();  
		 Enumeration allKeys = allTipusFitxer.keys();
		 while (allKeys.hasMoreElements()) {
			String nextElement = (String)allKeys.nextElement();
			ArrayList allMimeTypes = (ArrayList)allTipusFitxer.get(nextElement);
			if (allMimeTypes.contains(mimeType)) {
				if (!"".equals(tipusFitxer)){
					tipusFitxer += " # ";
				}
			   tipusFitxer += nextElement+ " ";	
			   }
		    }
	     } catch (Exception e) {
	     logger.error(e);	   
	     }
	   return tipusFitxer;
   }
   
   public static void carregaTipusFitxer2 (String nameFile) {
      // RandomAccessFile propertiesFile = null;
      String line = "";

      String tipusFitxer = "";
      String mimeTypes = "";
      ArrayList allMimeTypes = null;
      BufferedReader in = null;

      try {
      	nameFile = nameFile.replaceAll("indexador_educacio", "filetypes");    	
      	in = new BufferedReader(new FileReader(nameFile));
        allTipusFitxer = new Hashtable();  
         while ((line = in.readLine()) != null) {
         	line = new String(line.getBytes(), "UTF-8");

            StringTokenizer myTokenizer = new StringTokenizer(line,"#");
            tipusFitxer = (String)myTokenizer.nextToken();
            mimeTypes = (String)myTokenizer.nextToken();
            allMimeTypes = new ArrayList();
            StringTokenizer myTokenizer2 = new StringTokenizer(mimeTypes," ");
            while (myTokenizer2.hasMoreTokens()) {
            	allMimeTypes.add((String)myTokenizer2.nextToken());
                }
            logger.debug("Tipus fitxer ----> " + tipusFitxer);
            allTipusFitxer.put(tipusFitxer,allMimeTypes);
            }
        	
        } catch (Exception e) {
        logger.error(e);
        }
      }

   
   public static void carregaTipusFitxer () {
	      // RandomAccessFile propertiesFile = null;
	      String line = "";

	      String tipusFitxer = "";
	      String mimeTypes = "";
		String idTipusFitxer = "";
	      ArrayList allMimeTypes = null;
	      BufferedReader in = null;
          Connection myConnection = null;
          Statement stmt = null;
          ResultSet rs = null;
	      try {
  
	    	// myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
	    	  myConnection = Utils.getConnectionFromPool();
	    	stmt = myConnection.createStatement();
	    	rs = stmt.executeQuery("SELECT * FROM tipus_fitxers");
	        allTipusFitxer = new Hashtable();  
	         while (rs.next()) {
	         	mimeTypes = rs.getString("mimeType");
                tipusFitxer = rs.getString("nomGrup");
				idTipusFitxer = rs.getString("id");
	            allMimeTypes = new ArrayList();
	            StringTokenizer myTokenizer2 = new StringTokenizer(mimeTypes," ");
	            while (myTokenizer2.hasMoreTokens()) {
	            	allMimeTypes.add((String)myTokenizer2.nextToken());
	                }
				logger.debug("Tipus fitxer ----> " + tipusFitxer + ", " + idTipusFitxer);
				allTipusFitxer.put(idTipusFitxer, allMimeTypes);
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
		       Utils.commit(myConnection);    
	           myConnection.close();
	           }
	          } catch (Exception e) {
	          logger.error(e);	  
	          }
	        }
	      }
   
   
   public static boolean isVoid () {
	   if (allTipusFitxer == null) {
		   return true; 
	       }
	   return false;
       }
}