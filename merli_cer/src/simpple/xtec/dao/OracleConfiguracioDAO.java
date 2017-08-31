package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.ConfiguracioFragments;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the ConfiguracioDAO interface
 * 
 * @author descuer
 *
 */

public class OracleConfiguracioDAO implements ConfiguracioDAO {
	
	Connection myConnection = null;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleConfiguracioDAO.class);
	
	/**
	 * Constructor
	 */
	
	public OracleConfiguracioDAO () {
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

	/**
	 * Updates one weight value on the db
	 */
	
	public void actualitzaPes (
			String tipusCercador,
			String nomPes,
			float valorPes
	) throws SQLException, Exception {
		
		String updateStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("actualitzaPes -> in");
			logger.debug("tipusCercador: " + tipusCercador);
			logger.debug("nomPes: " + nomPes);
			logger.debug("valorPes: " + valorPes);
			
			updateStatement = "UPDATE config_pesos_index SET ";
			updateStatement += "valor=? WHERE cercador_id=? and nom_camp=?";

			prepStmt = myConnection.prepareStatement (updateStatement);
			prepStmt.setFloat(1, valorPes);
			if (tipusCercador.equals("edu365")) {
				prepStmt.setInt(2, 1);
				} else {
				prepStmt.setInt(2, 2);
				}			
			prepStmt.setString(3, nomPes);
            logger.debug("SQL: " + prepStmt);

			prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			prepStmt.close();	       
		}
	 logger.debug("actualitzaPes -> out");		
	}	
	
	
	 
    /**
     * Updates the info of the 'configuracio' table
     * 
     */
	
	public void actualitzaFragments (
			String tipusCercador, 
			int longitudDescripcio, 
			int resultatsPagina, 
			int nombreNovetats, 
			int tempsVidaNovetat) throws SQLException, Exception {
		
		String updateStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("actualitzaFragments -> in");
			logger.debug("tipusCercador: " + tipusCercador);
			logger.debug("longitudDescripcio: " + longitudDescripcio);
			logger.debug("resultatsPagina: " + resultatsPagina);
			logger.debug("nombreNovetats: " + nombreNovetats);
			logger.debug("tempsVidaNovetat: " + tempsVidaNovetat);
			
			updateStatement = "UPDATE config_cerca SET long_desc=?";
			updateStatement += ",max_resultats_pag=?";			
			updateStatement += ",nombre_novetats=?";
			updateStatement += ",temps_vida_novetat=?";			
			updateStatement += " WHERE cercador_id=?";
			
			logger.debug("[actualitzaDades] " + updateStatement);
			
			prepStmt = myConnection.prepareStatement (updateStatement);
			prepStmt.setInt(1, longitudDescripcio);
			prepStmt.setInt(2, resultatsPagina);
			prepStmt.setInt(3, nombreNovetats);
			prepStmt.setInt(4, tempsVidaNovetat);			
			if (tipusCercador.equals("edu365")) {
			   prepStmt.setInt(5, 1);
			   }
			   else if(tipusCercador.equals("altres")){
				prepStmt.setInt(5, 3);
				}
			   else {
			   prepStmt.setInt(5, 2);
			   }
            logger.info("SQL: " + prepStmt);
			prepStmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
      logger.debug("actualitzaFragments -> out");		
	  }	
	
	
	/**
	 * Loads the data from the db to a static class 
	 */
	
	public void reload (String tipusCercador) {
       try {
    	  logger.debug("reload -> in");    	   
    	  ConfiguracioFragments.loadConfiguracio(myConnection, tipusCercador);
    	  
        } catch (Exception e) {
        logger.error(e);	   
        }
  	  logger.debug("reload -> out");        
	  }
	
	/**
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
}