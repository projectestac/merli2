package simpple.xtec.web.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class RSSGenerator {
	
   Connection myConnection = null;	
   
   // logger
   static final Logger logger = Logger.getLogger(simpple.xtec.web.util.RSSGenerator.class);
	
	
   public RSSGenerator () {
		try {
			if (Configuracio.isVoid()) {
				Configuracio.carregaConfiguracio();
			    }
//			UtilsCercador.carregarDriver();
			myConnection = UtilsCercador.getConnectionFromPool();

		} catch (Exception e) {
		logger.error(e);
		}
      }
	
   public void generateRssComentaris (String pathFile) {
	  String selectStatement = "";
	  PreparedStatement prepStmt = null;
	  ResultSet rs = null;
      PrintWriter pw = null;
      String rss = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"; 
	  try {
		  logger.debug("Updating rss file... " + pathFile);
		  pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFile), "UTF-8")));		  
		  //pw = new PrintWriter(new FileOutputStream(pathFile));
		  rss += " <rss version=\"2.0\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n";
	      rss += "  <channel>\n";
	      rss += "    <title>Merl\u00ed</title>\n";
	      rss += "    <link>http://integracio.merli.xtec.cat/</link>\n";
	      rss += "    <description>Canal de comentaris dels recursos de Merl\u00ed</description>\n";
	      rss += "    <language>ca</language>\n";
	      
	      selectStatement = "SELECT * FROM (select comentari, titol, xtec_username, data_edicio , ROW_NUMBER() OVER (order by data_edicio DESC) R from comentaris where suspens=0) WHERE R <=100";	      
		  prepStmt = myConnection.prepareStatement (selectStatement);
		  rs = prepStmt.executeQuery();
		  while (rs.next()) {
			  rss += "      <item>\n";
/*			  rss += "        <title>" + + "</title>\n";
			  rss += "        <link>http://www.xml.com/pub/a/2002/12/04/normalizing.html</link>\n";
			  */
			  rss += "        <title>" + rs.getString("titol") + "</title>\n";			  
			  rss += "        <description>" + rs.getString("comentari") + "</description>\n";
			  rss += "        <dc:creator>" + rs.getString("xtec_username") + "</dc:creator>\n";
			  rss += "        <dc:date>" + rs.getString("data_edicio") + "</dc:date>\n";
			  rss += "     </item>\n";
		      }
	      rss += "  </channel>\n";
	      rss += " </rss>\n";
	      pw.println(rss);
	    } catch (Exception e) {
        logger.error(e);	   
	    } finally {
		try {
		   if (pw != null) {
			   pw.flush();
			   pw.close();
		       }
		   if (rs != null) {
			   rs.close();
			   }
		   if (prepStmt != null) {
			   prepStmt.close();
			   }
		   } catch (Exception e ) {}
	    }
      }

   public void generateRssNoticies (String pathFile) {
		  String selectStatement = "";
		  PreparedStatement prepStmt = null;
		  ResultSet rs = null;
	      PrintWriter pw = null;
 
	      String rss = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
	      int i = 0;
		  try {
			  logger.debug("Updating rss file... " + pathFile);

			  
			  pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFile), "UTF-8")));
			  //pw = new PrintWriter(new FileOutputStream(pathFile));
			  rss += " <rss version=\"2.0\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n";
		      rss += "  <channel>\n";
		      rss += "    <title>Merl\u00ed</title>\n";
		      rss += "    <link>http://integracio.merli.xtec.cat/</link>\n";
		      rss += "    <description>Canal de not\u00edcies i novetats relacionades amb el projecte Merl\u00ed</description>\n";
		      rss += "    <language>ca</language>\n";
		      
		      selectStatement = "SELECT * FROM (select titol, cos, data_publicacio, ROW_NUMBER() OVER (order by data_publicacio DESC) R from noticies WHERE publicat=1) WHERE R <=100";
		      logger.debug(selectStatement);
			  prepStmt = myConnection.prepareStatement (selectStatement);
			  rs = prepStmt.executeQuery();
			  String titol = "";
			  String description = "";
			  while (rs.next() && (i < 15)) {
				  titol = rs.getString("titol");
				  titol = titol.replaceAll("&", "&amp;");				  
				  titol = titol.replaceAll("<", "&lt;");
				  titol = titol.replaceAll(">", "&gt;");			  
				  description = rs.getString("cos");
				  description = description.replaceAll("&", "&amp;");				  
				  description = description.replaceAll("<", "&lt;");
				  description = description.replaceAll(">", "&gt;");			  
				  
				  rss += "      <item>\n";
				  rss += "        <title>" + titol + "</title>\n";
				  /* rss += "        <link>http://www.xml.com/pub/a/2002/12/04/normalizing.html</link>\n";
				  */
				  rss += "        <description>" + description + "</description>\n";
				  // rss += "        <dc:creator>" + rs.getString("xtec_username") + "</dc:creator>\n";
				  rss += "        <dc:date>" + rs.getString("data_publicacio") + "</dc:date>\n";
				  rss += "     </item>\n";
				  i ++;
			      }
		      rss += "  </channel>\n";
		      rss += " </rss>\n";
		      pw.println(rss);
		    } catch (Exception e) {
	        logger.error(e);	   
		    } finally {
			try {
			   if (pw != null) {
				   pw.flush();
				   pw.close();
			       }
			   if (rs != null) {
				   rs.close();
				   }
			   if (prepStmt != null) {
				   prepStmt.close();
				   }
			   } catch (Exception e ) {}
		    }
	      }
   
   public void generateRssComentarisRecurs (String pathFile, String idRecurs) {
		  String selectStatement = "";
		  PreparedStatement prepStmt = null;
		  ResultSet rs = null;
	      PrintWriter pw = null;
	      String rss = "<?xml version=\"1.0\"?>\n"; 
	      int i = 0;
		  try {
			  logger.debug("Updating rss file... " + pathFile);
			  pw = new PrintWriter(new FileOutputStream(pathFile));
			  rss += " <rss version=\"2.0\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n";
		      rss += "  <channel>\n";
		      rss += "    <title>XTEC</title>\n";
		      rss += "    <link>http://www.xtec.cat/</link>\n";
		      rss += "    <description>RSS de comentaris del recurs " + idRecurs + "</description>\n";
		      rss += "    <language>ca</language>\n";
		      
		      selectStatement = "select * from comentaris where suspens=0 and recurs_id='" + idRecurs + "' order by data_edicio DESC ";	      
			  prepStmt = myConnection.prepareStatement (selectStatement);
			  rs = prepStmt.executeQuery();
			  while (rs.next() && (i < 15)) {
				  rss += "      <item>\n";
				  rss += "        <description>" + rs.getString("comentari") + "</description>\n";
				  rss += "        <dc:creator>" + rs.getString("xtec_username") + "</dc:creator>\n";
				  rss += "        <dc:date>" + rs.getString("data_edicio") + "</dc:date>\n";
				  rss += "     </item>\n";
				  i ++;
			      }
		      rss += "  </channel>\n";
		      rss += " </rss>\n";
		      pw.println(rss);
		    } catch (Exception e) {
	        logger.error(e);	   
		    } finally {
			try {
			   if (pw != null) {
				   pw.flush();
				   pw.close();
			       }
			   if (rs != null) {
				   rs.close();
				   }
			   if (prepStmt != null) {
				   prepStmt.close();
				   }
			   } catch (Exception e ) {}
		    }
	      }

   
   
   
	public void disconnect () {
	   try {
		  myConnection.close();
	      } catch (Exception e) {
	      logger.error(e); 	 
	      }
	   }
   
}