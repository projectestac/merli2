package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class Directori {
	
	 // logger
	 static final Logger logger = Logger.getLogger(simpple.xtec.web.util.Directori.class);
	
    Connection myConnection = null;	 
	
	public Directori (Connection myConnection) {
		try {	
		   if (Configuracio.isVoid()) {
			  Configuracio.carregaConfiguracio(); 
		      }
		   this.myConnection = myConnection;
//		   UtilsCercador.carregarDriverOracle();		   
		   } catch (Exception e) {
		   logger.error(e);
		   }	
	}
	
	public ArrayList getUltimesNoticies () {
		ArrayList ultimesNoticies = null; 
		String query = "";
		//Connection conn = null;
	    Statement stmt = null;		
 	    ResultSet rs = null;
		NoticiaObject noticiaObject = null;
		try {
		  ultimesNoticies = new ArrayList();
	      //conn = UtilsCercador.getConnectionFromPool();		  

	      stmt = myConnection.createStatement();
	      // query = "select titol, data_edicio from noticies where publicat='si' order by data_edicio desc limit 5;";
	      // query = "SELECT * from (select titol, data_edicio, cos from noticies where publicat=1 order by data_edicio desc) WHERE rownum <=5";
	      
	      query = "SELECT * FROM (select titol, data_edicio, cos, ROW_NUMBER() OVER (ORDER by data_edicio DESC) R from noticies where publicat=1) WHERE R <=5";
	      rs = stmt.executeQuery(query);
	      while (rs.next()) {
	    	 noticiaObject = new NoticiaObject();
	    	 noticiaObject.titol = rs.getString("titol");
		     noticiaObject.cos = rs.getString("cos");
		     noticiaObject.data_edicio = rs.getString("data_edicio");
		     ultimesNoticies.add(noticiaObject);
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
		return ultimesNoticies;
	    }
		

	public ArrayList getRecursosMesVisitats (int ducId) {
		ArrayList recursosMesVisitats = null; 
		String query = "";
	    Statement stmt = null;		
 	    ResultSet rs = null;
		RecursObject recursObject = null;
		String dataCerca = "";		
		try {

		   Calendar rightNow = Calendar.getInstance();
		   rightNow.add(Calendar.HOUR, (-24 * 31));
		   dataCerca = UtilsCercador.calculaDataActual (rightNow);
			
		   recursosMesVisitats = new ArrayList();


	      stmt = myConnection.createStatement();
	      // query = "select titol, data_edicio from noticies where publicat='si' order by data_edicio desc limit 5;";
	      //query = "SELECT * from (select id, titol, visites from recursos order by visites desc) WHERE rownum <=5";
	      if (ducId != -1) {	      
	         // query = "SELECT * from (select id, titol, visites, ROW_NUMBER() OVER (ORDER BY visites desc) R from recursos where ducinfo like '%" + ducId + "%' and visites>0) WHERE R <=5";
	         // query = "SELECT * from (select id, titol, numVisites, ROW_NUMBER() OVER (ORDER BY numVisites desc) R from recursos rec, visites vis where rec.ducinfo like '%2%' and vis.numVisites>0 and vis.idrecurs=rec.id) WHERE R <=5";
	         // query = "SELECT * from (SELECT idRecurs, sum(numVisites) s, ROW_NUMBER() OVER (ORDER BY s desc) R from visites where dia>'" + dataCerca + "' group by idRecurs) WHERE R <=5";
	         query = "SELECT * from (select v.*, r.titol from (SELECT idRecurs, sum(numVisites), ROW_NUMBER() OVER (ORDER BY sum(numVisites) desc) R from visites v WHERE idRecurs IN (SELECT id FROM recursos where ducinfo like '%" + ducId + "%' and found=1) group by idRecurs) v, recursos r where r.id=v.idRecurs) WHERE R <=5";	         
	         } else {
	         query = "SELECT * from (select v.*, r.titol from (SELECT idRecurs, sum(numVisites), ROW_NUMBER() OVER (ORDER BY sum(numVisites) desc) R from visites v group by idRecurs) v, recursos r where r.id=v.idRecurs and r.found=1) WHERE R <=5";	        	 
	         // query = "SELECT * from (SELECT idRecurs, sum(numVisites) s, ROW_NUMBER() OVER (ORDER BY s desc) R from visites where dia>'" + dataCerca + "' group by idRecurs) WHERE R <=5";
	         // query = "SELECT * from (select id, titol, numVisites, ROW_NUMBER() OVER (ORDER BY numVisites desc) R from recursos rec, visites vis where rec.id=vis.idRecurs and vis.numVisites>0) WHERE R <=5";
		     // query = "SELECT * from (select id, titol, visites, ROW_NUMBER() OVER (ORDER BY visites desc) R from recursos) WHERE R <=5";
	         }

	      logger.debug(query);
	      rs = stmt.executeQuery(query);
	      while (rs.next()) {
	    	 recursObject = new RecursObject();
	    	 String titol = rs.getString("titol");
	    	 if (titol.length() > 60) {
	    		titol = titol.substring(0, 59) + "..."; 	    		 
	    	    }
	    	 recursObject.titol = titol;
	    	 recursObject.id = rs.getString("idRecurs");
	    	 recursObject.numVisites = rs.getInt(2);
		     recursosMesVisitats.add(recursObject);
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


		return recursosMesVisitats;
	    }

	public ArrayList getRecursosMesComentats (int ducId) {
		ArrayList recursosMesVisitats = null; 
		String query = "";

	    Statement stmt = null;		
	    Statement stmt2 = null;	    
 	    ResultSet rs = null;
 	    ResultSet rs2 = null; 	    
		RecursObject recursObject = null;
		String dataEdicio = "";
		try {
		  recursosMesVisitats = new ArrayList();


	      stmt = myConnection.createStatement();
	      // query = "select titol, data_edicio from noticies where publicat='si' order by data_edicio desc limit 5;";
	      Calendar rightNow = Calendar.getInstance();
	      rightNow.add(Calendar.HOUR, (-24 * 30));
		  String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
		  dataEdicio = data_actual_string + " 00:00:00";

	      if (ducId != -1) {
	         query = "SELECT * from (select count(*) c, recurs_id  from comentaris com, recursos rec where com.recurs_id=rec.id and com.suspens=0 and com.data_edicio>'" + dataEdicio + "' and rec.ducinfo like '%" + ducId + "%' group by recurs_id order by c DESC) WHERE rownum <=5";
	         } else {
		    // query = "SELECT * from (select count(*) c, recurs_id  from comentaris where suspens=0 and data_edicio>'" + dataEdicio + "' group by recurs_id order by c DESC) WHERE rownum <=5";
		     query = "SELECT * from (select count(*) c, recurs_id  from comentaris com, recursos rec where com.suspens=0 and rec.found=1 and com.recurs_id=rec.id and com.data_edicio>'" + dataEdicio + "' group by recurs_id order by c DESC) WHERE rownum <=5";
	         }

	      logger.debug(query);

	      rs = stmt.executeQuery(query);
	      while (rs.next()) {
	    	 recursObject = new RecursObject();
	         query = "select * from recursos where id='" + (String)rs.getString("recurs_id") + "'";
	         recursObject.id = rs.getString("recurs_id");
	    	 recursObject.numComentaris = rs.getInt(1);
	         stmt2 = myConnection.createStatement();
	         rs2 = stmt2.executeQuery(query);
	         rs2.next();
	    	 String titol = rs2.getString("titol");
	    	 if (titol.length() > 60) {
	    		titol = titol.substring(0, 59) + "..."; 	    		 
	    	    }
	    	 recursObject.titol = titol;	        
		     recursosMesVisitats.add(recursObject);
	         }
		  } catch (Exception e) {
		  logger.error(e);	  
		  } finally {
		  try {
            if (rs != null) {
              rs.close();	
              }
            if (rs2 != null) {
              rs2.close();	
              }
            if (stmt != null) {
              stmt.close();	
              }
            if (stmt2 != null) {
              stmt2.close();	
              }

		    } catch (Exception e) {
		    logger.error(e);	
		    }			  
		  }
		return recursosMesVisitats;
	    }
	

	public ArrayList getRecursosMesBenValorats (int ducId) {
		ArrayList recursosMesBenValorats = null; 
		String query = "";

	    Statement stmt = null;		
	    Statement stmt2 = null;	    
 	    ResultSet rs = null;
 	    ResultSet rs2 = null; 	    
		RecursObject recursObject = null;

		try {
		  recursosMesBenValorats = new ArrayList();

          
	      stmt = myConnection.createStatement();
	      // query = "select titol, data_edicio from noticies where publicat='si' order by data_edicio desc limit 5;";
	      if (ducId != -1) {
	         query = "select * from (select avg(puntuacio) a, recurs_id from comentaris com, recursos rec where rec.found=1 and com.recurs_id=rec.id and com.suspens=0 and rec.ducinfo like '%" + ducId + "%' group by recurs_id order by a desc) where rownum <=5";
	         } else {
	      //   query = "select * from (select avg(puntuacio) a, recurs_id from comentaris where suspens=0 group by recurs_id order by a desc) where rownum <=5";	  
	         query = "select * from (select avg(puntuacio) a, recurs_id from comentaris com, recursos rec where com.suspens=0 and rec.found=1 and com.recurs_id=rec.id group by recurs_id order by a desc) where rownum <=5";	        	       	 
	         }

	      logger.debug(query);
	      rs = stmt.executeQuery(query);
	      while (rs.next()) {
	    	 recursObject = new RecursObject();

	    	 recursObject.id = rs.getString("recurs_id");
	    	 recursObject.puntuacio = rs.getInt(1);
	         query = "select * from recursos where id='" + (String)rs.getString("recurs_id") + "'";	    	 
	         stmt2 = myConnection.createStatement();
	         rs2 = stmt2.executeQuery(query);
	         rs2.next();	    	
	    	 String titol = rs2.getString("titol");
	    	 if (titol.length() > 60) {
	    		titol = titol.substring(0, 59) + "..."; 	    		 
	    	    }
	    	 recursObject.titol = titol;		         
	    	 recursosMesBenValorats.add(recursObject);
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
            if (rs2 != null) {
              rs2.close();	
              }
            if (stmt2 != null) {
              stmt2.close();	
              }

		    } catch (Exception e) {
		    logger.error(e);	
		    }			  
		  }
		return recursosMesBenValorats;
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
	
}