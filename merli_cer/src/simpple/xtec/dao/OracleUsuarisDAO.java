package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the UsuarisDAO interface
 * 
 * @author descuer
 *
 */

public class OracleUsuarisDAO implements UsuarisDAO {

	
	Connection myConnection = null;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleUsuarisDAO.class);
	
	/**
	 * Constructor
	 */
	
	public OracleUsuarisDAO () {
		try {
			logger.debug("constructor -> in");				
			if (Configuracio.isVoid()) {
				Configuracio.carregaConfiguracio();
			    }
		    // Get connection from pool					
			myConnection = UtilsCercador.getConnectionFromPool();
		} catch (Exception e) {
		logger.error(e);
		}
		logger.debug("constructor -> out");			
	}
	
	
	
	/**
	 * Adds an user to the database
	 */
	
	public void afegirUsuari (
			String usuari
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("afegirUsuari -> in");			

			insertStatement = "INSERT INTO admin_users (id, xtec_username) VALUES (s_admin_users.nextVal, ?)";
			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setString(1, usuari);
			logger.info("SQL: " + prepStmt);
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();
		}	
	  logger.debug("afegirUsuari -> out");		
	}		
	

	/**
	 * Deletes an user from the database
	 */
	
	public void eliminarUsuari (
			int idUsuari
	) throws SQLException, Exception {
		
		String deleteStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("eliminarUsuari -> in");			
			deleteStatement = "DELETE FROM admin_users WHERE id=?";
			prepStmt = myConnection.prepareStatement (deleteStatement);			
			prepStmt.setLong(1, idUsuari);	
			logger.info("SQL: " + prepStmt);
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
	logger.debug("eliminarUsuari -> out");		
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