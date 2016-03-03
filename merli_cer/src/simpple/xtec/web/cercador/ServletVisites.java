package simpple.xtec.web.cercador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*; 

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.Indexador;
import simpple.xtec.web.util.UtilsCercador;


/**
 * This servlet handles the get author profile
 * ajax request. It gets the author name as parameter
 * queries the AuthorBean for the author profile and
 * returns the profile back.
 * 
 * @author Rahul Sapkal(rahul@javareference.com)
 */
public class ServletVisites extends HttpServlet 
{    

	 // logger
	 static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.ServletVisites.class);
	// static Connection myConnection2 = null;
	 static PreparedStatement prepStmtMerge = null;
	 static PreparedStatement prepStmtUpdate = null;
	 static PreparedStatement prepStmtSelectVisites = null;
	 static PreparedStatement prepStmtInsertVisites = null;
	 static PreparedStatement prepStmtUpdateVisites = null;	 
	 static Connection myConnection2 = null;
		/**
		 * Loads the database driver 
		 */
		
		public void init(ServletConfig config) throws ServletException {
			try {
//			   UtilsCercador.carregarDriverOracle();
			   } catch (Exception e) {
			   logger.error(e);
			   }
		}	  	 
		
		public void destroy() {
			try {
			 logger.debug("destroy -> in");
			// myConnection2.close();
			 logger.debug("destroy -> out");
			} catch (Exception e) {
			logger.error(e);	
			}
			
		}
	/**
     * This method is overriden from the base class to handle the
     * get request. 
     */
    protected void doGet(HttpServletRequest requestObj, HttpServletResponse responseObj)
                   throws IOException
                
    {
/*    	Random myRandom = new Random();
        int idRecursInt = myRandom.nextInt(2000);
      String idRecurs= "" + idRecursInt;
        logger.error("*************************");        
        logger.error("ID RECURSSSSS: " + idRecurs);
        logger.error("*************************");
        
        // Connection myConnection = null;
        Indexador myIndexador = null;
        
        try {
          	
          if ((myConnection2 == null) || myConnection2.isClosed()) {
        	 logger.info("Creating prepared statement"); 
        	 createPreparedStatement(); 
        	 } 
logger.error("addVisitaRecurs...");
          addVisitaRecurs (myConnection2, idRecurs);
logger.error("marcarRecursModificat...");
          marcarRecursModificat (myConnection2, idRecurs);
logger.error("end...");
    //      myIndexador = new Indexador();
  //        myIndexador.modificar(idRecurs);
          } catch (Exception e) {
          logger.error(e + "   " + idRecurs);	  
          } finally {
            try {
      //        myConnection.close();	
            } catch (Exception e) {
        	logger.error(e);  
            }
          }
  */      
    }        

    
    private void createPreparedStatement () {
    	try {
/*    		String sqlMerge = "";			
    		sqlMerge += "MERGE INTO visites v1 USING (SELECT idRecurs FROM (SELECT idRecurs FROM visites WHERE idRecurs =? AND dia=?"; 
    		sqlMerge +=	" union all select NULL idRecurs from dual) WHERE rownum=1) v2";
    		sqlMerge +=	" ON ((v1.idRecurs = v2.idRecurs) AND (v1.dia = v2.dia)) WHEN MATCHED THEN";
    		sqlMerge += " UPDATE SET v1.numVisites = v1.numVisites + 1";
    		sqlMerge +=	" WHEN NOT MATCHED THEN INSERT (v1.idRecurs, v1.numVisites, v1.dia)";
    		sqlMerge +=	" VALUES (?, 1, ?)";
    		logger.error("createPreparedStatement");
    		// myConnection = UtilsCercador.getConnectionFromPool();
    		prepStmtMerge = myConnection2.prepareStatement (sqlMerge);
    		*/
  
    			
    		String sqlSelectVisites = "SELECT idRecurs FROM visites WHERE idRecurs=? AND dia=?";
            prepStmtSelectVisites = myConnection2.prepareStatement(sqlSelectVisites);
            
    		String sqlInsertVisites = "INSERT INTO VISITES (idRecurs, numVisites, dia) VALUES (?, 1, ?)";
    		prepStmtInsertVisites = myConnection2.prepareStatement(sqlInsertVisites);
    		
    		String sqlUpdateVisites = "UPDATE VISITES set numVisites = numVisites + 1 WHERE idRecurs=? and dia=?";
    		prepStmtUpdateVisites = myConnection2.prepareStatement(sqlUpdateVisites);
    		
    		String sqlUpdate = "UPDATE recursos SET modificat=1 WHERE id=?";
    		prepStmtUpdate  = myConnection2.prepareStatement (sqlUpdate);
    	  } catch (Exception e) {
    	  logger.error(e);	  
    	  }
    	
    }
    
	public void addVisitaRecurs (
			Connection myConnection,
			String idRecurs
	) throws SQLException, Exception {
		
		
		String dataActual = "";
		ResultSet rs = null;
		
		try {


            logger.info("Add visita recurs.....");
			// logger.error(sqlMerge);
  		    Calendar rightNow = Calendar.getInstance();			
  		    dataActual = UtilsCercador.calculaDataActual(rightNow);

  		    prepStmtSelectVisites.setString(1, idRecurs);
  		    prepStmtSelectVisites.setString(2, dataActual);
  		    rs = prepStmtSelectVisites.executeQuery();
  		    if (rs.next()) {
  		      prepStmtUpdateVisites.setString(1, idRecurs);
  		      prepStmtUpdateVisites.setString(2, dataActual);
  		      prepStmtUpdateVisites.executeUpdate();
  		      } else {
  		      prepStmtInsertVisites.setString(1, idRecurs);
  		      prepStmtInsertVisites.setString(2, dataActual);  	
  		      prepStmtInsertVisites.executeUpdate();
  		      }
/*			
  		  prepStmtMerge.setString(1, idRecurs);
  		prepStmtMerge.setString(2, dataActual);			
  		prepStmtMerge.setString(3, idRecurs);
  		prepStmtMerge.setString(4, dataActual);			
			
			logger.info("[actualitzaDades] " + prepStmtMerge);
			prepStmtMerge.executeUpdate(); 
  		    
  */		    
  		    
	/*		selectStatement = "SELECT numVisites FROM visites WHERE idRecurs=? and dia=?";
			prepStmtSelect = myConnection.prepareStatement (selectStatement);
			prepStmtSelect.setString(1, idRecurs);
			prepStmtSelect.setString(2, dataActual);			
			
			logger.info("[actualitzaDades] " + prepStmtSelect);
			rs = prepStmtSelect.executeQuery(); 
			
            if (rs.next()) {
  			  updateStatement = "UPDATE visites SET numVisites=numVisites+1 WHERE idRecurs=? and dia=?";									
  			  prepStmtUpdate = myConnection.prepareStatement (updateStatement);
  			  prepStmtUpdate.setString(1, idRecurs);
  			  prepStmtUpdate.setString(2, dataActual);			
  			  logger.info("[actualitzaDades] " + prepStmtUpdate);
  			  prepStmtUpdate.executeUpdate();
              } else {			              	
  			  insertStatement = "INSERT INTO visites (idRecurs, dia, numVisites) VALUES (?, ?, 1)";									
  			  prepStmtInsert = myConnection.prepareStatement (insertStatement);
  			  prepStmtInsert.setString(1, idRecurs);
  			  prepStmtInsert.setString(2, dataActual);			

  			  logger.info("[actualitzaDades] " + prepStmtInsert);
  			  prepStmtInsert.executeUpdate(); 

              }
        */    
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			if (rs != null) {
			  rs.close();
   		      }			
			if (prepStmtUpdateVisites != null) {
				prepStmtUpdateVisites.close();
			  }
			if (prepStmtSelectVisites != null) {
				prepStmtSelectVisites.close();
			  }
			if (prepStmtInsertVisites != null) {
				prepStmtInsertVisites.close();
			  }
			try {
			/* if (prepStmtMerge != null) {
				prepStmtMerge.close();
			  }*/
			} catch (Exception e) {}
			
		}	
	}	
    
    
    /**
     * This method is overriden from the base class to handle the
     * get request. 
     */
    protected void doPost(HttpServletRequest requestObj, HttpServletResponse responseObj)
                   throws IOException
    {
    	PrintWriter writer = null;
    	try {
 

        //set the content type
        responseObj.setContentType("text/xml");
        
        responseObj.setHeader("Cache-Control", "no-cache");
        
        //get the PrintWriter object to write the html page
        writer = responseObj.getWriter();
        
        //get parameters store into the hashmap
        HashMap paramsMap = new HashMap();
        Enumeration paramEnum = requestObj.getParameterNames();
        while(paramEnum.hasMoreElements())
        {
            String paramName = (String)(paramEnum.nextElement());
            paramsMap.put(paramName, requestObj.getParameter(paramName));
        }
        //get the author name passed
        String idRecurs= (String)paramsMap.get("idRecurs");
        
        logger.info("*************************");        
        logger.info("ID RECURS: " + idRecurs);
        logger.info("*************************");
        
        // Connection myConnection = null;
        Indexador myIndexador = null;
        
        try {
        	logger.info("getConnectionFromPool...");
  	      //myConnection = UtilsCercador.getConnectionFromPool();
         /*   if ((myConnection2 == null) || myConnection2.isClosed()) {
           	 logger.info("Creating prepared statement"); 
           	 createPreparedStatement(); 
           	 }         	*/
           	  myConnection2 = UtilsCercador.getConnectionFromPool();
           	  createPreparedStatement(); 
  	    logger.info("addVisitaRecurs...");
          addVisitaRecurs (myConnection2, idRecurs);
          logger.info("marcarRecursModificat...");
          marcarRecursModificat (myConnection2, idRecurs);
          logger.info("end...");
    //      myIndexador = new Indexador();
  //        myIndexador.modificar(idRecurs);
          } catch (Exception e) {
          logger.error(e);	  
          } finally {
            try {
              myConnection2.close();	
            } catch (Exception e) {
        	logger.error(e);  
            }
            try {
            	writer.close();	
              } catch (Exception e) {
          	logger.error(e);  
              }            
          }
    	} catch (Exception e) {
    	logger.error(e);	
    	}
    	
    }

    
	/**
	 * Set a resource as modified
	 */
	
	public void marcarRecursModificat (Connection myConnection, String idRecurs) throws SQLException{
        
        Statement stmt = null;
        String query = "";
        try {
           logger.debug("marcarRecursModificat -> in");
    	  // stmt = myConnection.createStatement();
    	  // query = "UPDATE recursos SET modificat=1 WHERE id='" + idRecurs + "'";
     	   logger.info("SQL: " + prepStmtUpdate);
     	  logger.info("ID: " + idRecurs);
          // stmt.executeUpdate (query);        	
     	 prepStmtUpdate.setString(1, idRecurs);
     	 prepStmtUpdate.executeUpdate();
        } catch (SQLException e) {
         logger.error(e);        
        } catch (Exception e) {
         logger.error(e);
        } finally {
        if (stmt != null) {
           stmt.close();
           }
        }
    	logger.debug("marcarRecursModificat -> out");        
     }
    
    
}