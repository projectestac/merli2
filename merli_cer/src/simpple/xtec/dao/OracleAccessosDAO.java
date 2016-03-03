package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.AccessLogObject;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the AccessosDAO interface
 * 
 * @author descuer
 *
 */

public class OracleAccessosDAO implements AccessosDAO {
	
	Connection myConnection = null;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleAccessosDAO.class);

	
	/**
	 * Constructor
	 */
	
	public OracleAccessosDAO () {
		try {
	       logger.debug("constructor -> in");
		   if (Configuracio.isVoid()) {
		       Configuracio.carregaConfiguracio();
	      	   }
	       // Get connection from pool
		   myConnection = UtilsCercador.getConnectionFromPool();
	       logger.debug("constructor -> out");		   
		} catch (Exception e) {
		logger.error(e);
		}
	}	

	/*
     * Close the db connection
     */ 
	public void disconnect () {
       try {
     	   logger.debug("disconnect -> in");
		   myConnection.close();
	   	   logger.debug("disconnect -> out");		   
		  } catch (Exception e) {
		  logger.error(e); 	 
		  }
		}

	
	/**
	 * Do the search, and return an arraylist filled with instances of AccessLogObject class
	 */

	public ArrayList doSearch (int nivell, boolean filtreEdu365, boolean filtreXtec, String dataIniciCerca, String dataFinalCerca, boolean fullResults) {
		String selectStatement = "";
		String selectStatementCount = "";		
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtCount = null;		
		ResultSet rs = null;
		ResultSet rsCount = null;		
		ArrayList resultats =null;
		int limitInferior = 0;
		int limitSuperior = 0;		
		
		try {
			logger.debug("doSearch -> in");			
			logger.debug("filtreEdu365:" + filtreEdu365);			
			logger.debug("filtreXtec:" + filtreXtec);
			logger.debug("dataIniciCerca:" + dataIniciCerca);
			logger.debug("dataFinalCerca:" + dataFinalCerca);			
			resultats = new ArrayList();
			selectStatement = "SELECT * FROM (SELECT cerca, data_cerca, cercador_id, ROW_NUMBER() OVER (ORDER BY data_cerca DESC) R FROM log_cerques ";
			selectStatementCount = "SELECT count(*) FROM log_cerques ";			

			if (filtreEdu365 && !filtreXtec) {
		      selectStatement += "WHERE cercador_id=1";		
		      selectStatementCount += "WHERE cercador_id=1";		      
			  }
			if (filtreXtec && !filtreEdu365) {
			  selectStatement += "WHERE cercador_id=2";
			  selectStatementCount += "WHERE cercador_id=2";			  
			  }
			if (filtreXtec && filtreEdu365) {
			  selectStatement += "WHERE cercador_id<3";
			  selectStatementCount += "WHERE cercador_id<3";			  
			  }
			if (!filtreXtec && !filtreEdu365) {
			  selectStatement += "WHERE cercador_id<3";
			  selectStatementCount += "WHERE cercador_id<3";			  
			  }

			if (!dataIniciCerca.equals("")) {
			  selectStatement += " AND data_cerca>'" + dataIniciCerca + "' ";
			  selectStatementCount += " AND data_cerca>'" + dataIniciCerca + "' ";			  
			  }
			if (!dataFinalCerca.equals("")) {
			  selectStatement += " AND data_cerca<'" + dataFinalCerca + "' ";
			  selectStatementCount += " AND data_cerca<'" + dataFinalCerca + "' ";			  
			  }


			logger.info("SQL: " + selectStatementCount);			
			prepStmtCount = myConnection.prepareStatement (selectStatementCount);
			rsCount = prepStmtCount.executeQuery();
			rsCount.next();
					
			if (!fullResults) {
			   limitInferior = nivell * Configuracio.numResultatsPagina;
			   limitSuperior = (nivell + 1) * Configuracio.numResultatsPagina;				
			   } else {
			   limitInferior = 0;
			   limitSuperior = new Integer(rsCount.getInt(1)).intValue();				  
			   }
			
			
			selectStatement += ") WHERE R>=" + limitInferior + " AND R <=" + limitSuperior;			
			logger.info("SQL: " + selectStatement);			

			// First position of array -> number of results
			resultats.add(new Integer(rsCount.getInt(1)));

			prepStmt = myConnection.prepareStatement (selectStatement);
			rs = prepStmt.executeQuery();

			while (rs.next()) {
				AccessLogObject accessLog = new AccessLogObject();
				accessLog.cerca = rs.getString("cerca");
				accessLog.data = rs.getString("data_cerca");
				accessLog.cercador = rs.getInt("cercador_id");
				resultats.add(accessLog);
			    }	
		  } catch (Exception e) {
		  logger.error(e);	
		  } finally {
		  try {	  
			if (rs != null) {
			  rs.close();
			  }
			if (rsCount != null) {
			  rsCount.close();
			  }		
			if (prepStmt != null) {
			  prepStmt.close();
			  }
			if (prepStmtCount != null) {
			  prepStmtCount.close();
			  }
		    } catch (Exception e ) {		    	
		    }
		  }
		logger.debug("numResultats:" + resultats.size());
		logger.debug("doSearch -> out");				
		return resultats;
	}

	/**
	 * Do the search, and return an arraylist filled with instances of AccessLogObject class
	 */
		
	public ArrayList doSearchHistograma (int nivell, boolean filtreEdu365, boolean filtreXtec, String dataIniciCerca, String dataFinalCerca, boolean fullResults) {
		String selectStatement = "";
		String selectStatementCount = "";		
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtCount = null;		
		ResultSet rs = null;
		ResultSet rsCount = null;		
		ArrayList resultats =null;		
		int limitInferior = 0;
		int limitSuperior = 0;		
		try {
			logger.debug("doSearchHistograma -> in");			
			logger.debug("filtreEdu365:" + filtreEdu365);			
			logger.debug("filtreXtec:" + filtreXtec);
			logger.debug("dataIniciCerca:" + dataIniciCerca);
			logger.debug("dataFinalCerca:" + dataFinalCerca);						
			resultats = new ArrayList();
			
			selectStatement = "SELECT * FROM (SELECT count(*) c, cerca, cercador_id, ROW_NUMBER() OVER (ORDER BY count(*) DESC) R FROM log_cerques ";
			selectStatementCount = "SELECT count(distinct(CONCAT(cerca, cercador_id))) FROM log_cerques ";
			
			if (filtreEdu365 && !filtreXtec) {
		      selectStatement += "WHERE cercador_id=1";		
		      selectStatementCount += "WHERE cercador_id=1";		      
			  }
			if (filtreXtec && !filtreEdu365) {
			  selectStatement += "WHERE cercador_id=2";
			  selectStatementCount += "WHERE cercador_id=2";			  
			  }
			if (filtreXtec && filtreEdu365) {
			  selectStatement += "WHERE cercador_id<3";
			  selectStatementCount += "WHERE cercador_id<3";			  
			  }
			if (!filtreXtec && !filtreEdu365) {
			  selectStatement += "WHERE cercador_id<3";
			  selectStatementCount += "WHERE cercador_id<3";			  
			  }

			
			if (!dataIniciCerca.equals("")) {
			  selectStatement += " AND data_cerca>'" + dataIniciCerca + "' ";
			  selectStatementCount += " AND data_cerca>'" + dataIniciCerca + "' ";			  
			  }
			if (!dataFinalCerca.equals("")) {
			  selectStatement += " AND data_cerca<'" + dataFinalCerca + "' ";
			  selectStatementCount += " AND data_cerca<'" + dataFinalCerca + "' ";			  
			  }
			//selectStatement += " GROUP BY cerca, cercador_id ORDER BY c DESC";
			selectStatement += " GROUP BY cerca, cercador_id";		


			logger.info("SQL: " + selectStatementCount);			
			prepStmtCount = myConnection.prepareStatement (selectStatementCount);
			rsCount = prepStmtCount.executeQuery();
			rsCount.next();
					
			if (!fullResults) {
			   limitInferior = nivell * Configuracio.numResultatsPagina;
			   limitSuperior = (nivell + 1) * Configuracio.numResultatsPagina;				
			   } else {
			   limitInferior = 0;
			   limitSuperior = new Integer(rsCount.getInt(1)).intValue();				  
			   }
			
			selectStatement += ") WHERE R>=" + limitInferior + " AND R <=" + limitSuperior;
			logger.info("SQL: " + selectStatement);
			// First position of array -> number of results
			resultats.add(new Integer(rsCount.getInt(1)));
		
			prepStmt = myConnection.prepareStatement (selectStatement);
			rs = prepStmt.executeQuery();
			while (rs.next()) {
				AccessLogObject accessLog = new AccessLogObject();
				accessLog.cerca = rs.getString("cerca");
				accessLog.numCerques = rs.getInt(1);
				accessLog.cercador = rs.getInt("cercador_id");				
				resultats.add(accessLog);				
			}
		  } catch (Exception e) {
		  logger.error(e);	
		  } finally {
		  try {	  
			if (rs != null) {
			  rs.close();
			  }
			if (rsCount != null) {
			  rsCount.close();
			  }		
			if (prepStmt != null) {
			  prepStmt.close();
			  }
			if (prepStmtCount != null) {
			  prepStmtCount.close();
			  }

		    } catch (Exception e ) {
		    	
		    }
		  }
		logger.debug("numResultats:" + resultats.size());
		logger.debug("doSearchHistograma -> out"); 
		return resultats;
	}
	
	
	
}	